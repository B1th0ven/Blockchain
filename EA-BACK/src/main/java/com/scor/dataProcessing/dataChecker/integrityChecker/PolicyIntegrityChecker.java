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

public class PolicyIntegrityChecker implements InterfaceToIntegrityChecker {

	private static final long serialVersionUID = -1056032277995758898L;
	private static final PolicyIntegrityChecker instance = new PolicyIntegrityChecker();

	private PolicyIntegrityChecker() {
	}

	public static PolicyIntegrityChecker getInstance() {
		return instance;
	}

	public ControlResults run(Map<String, List<String>> refData, String path, String type) {

		List<PivotCol> pivotCols = DataPivot.getPivotCols();

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
		List<List<String>> possibleVals = new ArrayList<>();
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
				possibleVals.add(null);
				continue;
			}
			for (PivotCol pc : pivotCols) {

				if (name.equals(pc.getName())) {
					found = true;
					types.add(pc.getType());
					if ((type.equalsIgnoreCase("combine")
							& (name.equalsIgnoreCase("exposure_or_event")
									|| name.equalsIgnoreCase("date_of_event_incurred")
									|| name.equalsIgnoreCase("type_of_event"))
							|| pc.getPossiblesValues().contains("empty"))) {
						is_mand.add(false);
					} else {
						is_mand.add(pc.isMandatory());
					}
					possibleVals.add(pc.getPossiblesValues());
					break;
				}
			}
			if (!found) {
				types.add("free Text");
				is_mand.add(false);
				possibleVals.add(null);
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
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(String line) throws Exception {

				if (StringUtils.isNotBlank(line) & !line.equalsIgnoreCase(header)) {
					String[] arr = line.toLowerCase().trim().split(";", -1);
					String classification = null;
					if (type.equalsIgnoreCase("split"))
						classification = arr[names.indexOf("exposure_or_event")];
					if (classification == null || classification.equalsIgnoreCase("exposure")
							|| classification.equalsIgnoreCase("event")
							|| classification.equalsIgnoreCase("exposure + event")) {
						List<String> expo_mand_cols = Arrays.asList("exposure_or_event",
								"date_of_begin_current_condition", "date_of_commencement",
								"status_begin_current_condition", "status_end_current_condition",
								"acceleration_risk_type", "main_risk_type", "product_id");
						List<String> event_mand_cols = Arrays.asList("exposure_or_event", "date_of_event_incurred",
								"type_of_event", "date_of_commencement", "acceleration_risk_type", "main_risk_type",
								"product_id");
						List<String> event_expo_mand_cols = Arrays.asList("status_begin_current_condition",
								"date_of_begin_current_condition");

						for (int i = 0; i < arr.length; i++) {
							if (i < types.size()) {
								Boolean isMandatory = StringUtils.isNotBlank(arr[i]) ? null
										: is_mand.get(i)
												|| ("exposure".equalsIgnoreCase(classification)
														&& expo_mand_cols.contains(names.get(i)))
												|| ("event".equalsIgnoreCase(classification)
														&& event_mand_cols.contains(names.get(i)))
												|| ("exposure + event".equalsIgnoreCase(classification)
														&& event_expo_mand_cols.contains(names.get(i)));
								;
								switch (types.get(i).trim().toLowerCase()) {
								case "date":
									FormatControlUtility.dateValidator(arr[i], names.get(i), isMandatory, line, df_acc,
											errorsCount);
									break;
								case "numeric":
									FormatControlUtility.numericValidator(arr[i], names.get(i), is_mand.get(i), line,
											decimalSeparator, nf_acc, errorsCount, possibleVals.get(i),false);
									try {
										Double valueofNumber = Double.parseDouble(arr[i].replaceAll("\\,", "\\."));
										if (names.get(i).matches(".*age.*") & valueofNumber < 0)
											FormatControlUtility.invalideFieldCounter(names.get(i), nf_acc, errorsCount,
													line);
									} catch (NullPointerException | NumberFormatException e) {
									}
									break;
								case "numeric+":
									FormatControlUtility.numericValidator(arr[i], names.get(i), isMandatory, line,
											decimalSeparator, nf_acc, errorsCount, possibleVals.get(i),true);
									break;
								case "age":
									FormatControlUtility.ageValidator(arr[i], names.get(i), is_mand.get(i), line,
											nf_acc, errorsCount);
									break;
								case "code":

									FormatControlUtility.codeValidator(arr[i], names.get(i), isMandatory, line, cf_acc,
											errorsCount, possibleVals.get(i));
									break;
								case "refdata":
									if (StringUtils.isEmpty(arr[i]) && !is_mand.get(i)) {
										continue;
									}
									List<String> possibleValues = refData.get(names.get(i));
									if (possibleValues.contains(arr[i].trim())) {
										continue;
									}
									FormatControlUtility.invalideFieldCounter(names.get(i), refDataAcc, errorsCount,
											line);
									break;
								default:
									break;
								}
							}
						}
					} else {
						List<AffectedColumn> affectedColumns = new ArrayList<>();
						affectedColumns
								.add(new AffectedColumn("Exposure or Event", 1, new ArrayList<>(Arrays.asList(line))));
						cf_acc.add(new ControlResult("Code format", affectedColumns));
						errorsCount.add(1);
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

		return new ControlResults(threshold, errorsCount.value(), controlResultsList, header
				+ ";age_at_commencement_definition;product_start_date;product_end_date;min_face_amount;max_face_amount;min_age_at_commencement;max_age_at_commencement;duplicated_product_id;client_risk_carrier_name;study_client;client_group;study_client_group;treaty_number_omega;study_treaty_number;distribution_brand_name;distribution_brand;client_country;study_country",
				new HashMap<>());

	}

}
