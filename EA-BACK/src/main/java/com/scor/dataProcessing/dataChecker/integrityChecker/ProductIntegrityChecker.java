package com.scor.dataProcessing.dataChecker.integrityChecker;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.util.LongAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.common.DataPivot;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.ControlResults;
import com.scor.dataProcessing.models.PivotCol;



public class ProductIntegrityChecker implements InterfaceToIntegrityChecker {

	private static final long serialVersionUID = -5681925153113499873L;
	private static final ProductIntegrityChecker instance = new ProductIntegrityChecker();

	private ProductIntegrityChecker() {
	}

	public static ProductIntegrityChecker getInstance() {
		return instance;
	};

	public ControlResults run(Map<String, List<String>> refData, String path) {
		List<PivotCol> pivotColsProduct = DataPivot.getPivotColsProduct();

		JavaRDD<String> data = sc.textFile(path);
		String header = data.first();
		List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));

		// Long threshold = (10*(data.count()-1)*names.size())/100 ;
		Long threshold = (10 * (data.count() - 1)) / 100;

		// System.out.println("___________________________");
		// System.out.println(threshold);
		// System.out.println("___________________________");


		List<String> types = new ArrayList<>();
		List<Boolean> is_mand = new ArrayList<>();
		List<List<String>> possibleValsProd = new ArrayList<>();
		ControlResult headersControl = new ControlResult("Header format", new ArrayList<>());
		LongAccumulator errorsCount = sc.sc().longAccumulator();
		int emptyHeader = 0;
		for (String name : names) {
			boolean found = false;
			if (StringUtils.isBlank(name)) {
				emptyHeader++;
				List<AffectedColumn> affectedColumns = new ArrayList<>();
				affectedColumns.add(new AffectedColumn("N/A", emptyHeader, new ArrayList<>(
						Arrays.asList(header.replace("Data_Line", "Header").replace("Policy_ID", "N/A")))));
				headersControl = new ControlResult("Header format", affectedColumns);
				errorsCount.add(1);
				types.add("free Text");
				is_mand.add(false);
				possibleValsProd.add(null);
				continue;
			}
			for (PivotCol pc : pivotColsProduct) {
				if (name.equals(pc.getName())) {
					found = true;
					types.add(pc.getType());
					is_mand.add(pc.isMandatory());
					possibleValsProd.add(pc.getPossiblesValues());
					break;
				}
			}
			if (!found) {
				types.add("free Text");
				is_mand.add(false);
				possibleValsProd.add(null);
			}
		}

		ControlResultAccumulator df_acc = new ControlResultAccumulator(
				new ControlResult("Date format", new ArrayList<>()));
		sc.sc().register(df_acc);

		ControlResultAccumulator nf_acc = new ControlResultAccumulator(
				new ControlResult("Numeric format", new ArrayList<>()));
		sc.sc().register(nf_acc);

		ControlResultAccumulator cf_acc = new ControlResultAccumulator(
				new ControlResult("Code format", new ArrayList<>()));
		sc.sc().register(cf_acc);

		ControlResultAccumulator refDataAcc = new ControlResultAccumulator(
				new ControlResult("RefData format", new ArrayList<>()));
		sc.sc().register(refDataAcc);

	
		Broadcast<Map<String, String>> decimalSeparator = sc.broadcast(FormatControlUtility.getDecimalSeparator(data, header, types, names));
		
		data.foreach(new VoidFunction<String>() {

			private static final long serialVersionUID = 8326635604837410335L;

			@Override
			public void call(String line) throws Exception {
				// if(errorsCount.value() <= threshold) {

				if (StringUtils.isNotBlank(line) & !line.equalsIgnoreCase(header)) {

					String[] arr = line.toLowerCase().trim().split(";", -1);

					for (int i = 0; i < arr.length; i++) {
						switch (types.get(i)) {
						case "date":
							FormatControlUtility.dateValidator(arr[i], names.get(i), is_mand.get(i), line, df_acc,
									errorsCount);
							break;
						case "numeric":
							FormatControlUtility.numericValidator(arr[i], names.get(i), is_mand.get(i), line,
									 decimalSeparator, nf_acc, errorsCount, possibleValsProd.get(i), false);
							break;
						case "numeric+":
							FormatControlUtility.numericValidator(arr[i], names.get(i), is_mand.get(i), line,
									decimalSeparator, nf_acc, errorsCount, possibleValsProd.get(i), true);
							break;
						case "age":
							FormatControlUtility.ageValidator(arr[i], names.get(i), is_mand.get(i), line, nf_acc,
									errorsCount);
							break;
						case "code":
							FormatControlUtility.codeValidator(arr[i], names.get(i), is_mand.get(i), line, cf_acc,
									errorsCount, possibleValsProd.get(i));
							break;
						case "refdata":
							if (StringUtils.isEmpty(arr[i]) && !is_mand.get(i)) {
								continue;
							}
							List<String> possibleValues = refData.get(names.get(i));
							if (possibleValues!=null && possibleValues.contains(arr[i].trim())) {
								continue;
							}
							FormatControlUtility.invalideFieldCounter(names.get(i), refDataAcc, errorsCount, line);
							break;
						default:
							break;
						}
					}
				}

			}

		});

		List<ControlResult> controlResultsList = new ArrayList<>();
		controlResultsList.add(df_acc.value());
		controlResultsList.add(nf_acc.value());
		controlResultsList.add(cf_acc.value());
		controlResultsList.add(refDataAcc.value());
		controlResultsList.add(headersControl);

		return new ControlResults(threshold, errorsCount.value(), controlResultsList, header, new HashMap<>());

	}
}
