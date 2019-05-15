package com.scor.dataProcessing.sparkConnection;
//package com.scor.dataProcessing.spark;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.VoidFunction;
//import org.apache.spark.util.LongAccumulator;
//import org.springframework.stereotype.Service;
//
//import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
//import com.scor.dataProcessing.accumulators.HashMapAccumulator;
//import com.scor.dataProcessing.common.DataPivot;
//import com.scor.dataProcessing.models.AffectedColumn;
//import com.scor.dataProcessing.models.ControlResult;
//import com.scor.dataProcessing.models.ControlResults;
//import com.scor.dataProcessing.models.PivotCol;
//import com.scor.persistance.entities.RefDataEntity;
//
//@Service
//public class TechControls implements Serializable {
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	public ControlResults run(String path, String type, Map<String, List<String>> refData) {
//
//		JavaSparkContext sc = Connection.getContext();
//		List<PivotCol> pivotCols = DataPivot.getPivotCols();
//
//		JavaRDD<String> data = sc.textFile(path);
//		String header = data.first();
//		List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));
//
//		// Long threshold = (10*(data.count()-1)*names.size())/100 ;
//		Long threshold = (10 * (data.count() - 1)) / 100;
//
//		// System.out.println("___________________________");
//		// System.out.println(threshold);
//		// System.out.println("___________________________");
//
//		List<String> types = new ArrayList<>();
//		List<Boolean> is_mand = new ArrayList<>();
//		List<List<String>> possibleVals = new ArrayList<>();
//		ControlResult headersControl = new ControlResult("Header format", new ArrayList<>());
//		LongAccumulator errorsCount = sc.sc().longAccumulator();
//		int emptyHeader = 0;
//		for (String name : names) {
//			if (StringUtils.isBlank(name)) {
//				emptyHeader++;
//				List<AffectedColumn> affectedColumns = new ArrayList<>();
//				affectedColumns.add(new AffectedColumn("N/A", emptyHeader, new ArrayList<>(
//						Arrays.asList(header.replace("Data_Line", "Header").replace("Policy_ID", "N/A")))));
//				headersControl = new ControlResult("Header format", affectedColumns);
//				errorsCount.add(1);
//				types.add("free Text");
//				is_mand.add(false);
//				possibleVals.add(null);
//				continue;
//			}
//			boolean found = false;
//			for (PivotCol pc : pivotCols) {
//
//				if (name.equals(pc.getName())) {
//					found = true;
//					types.add(pc.getType());
//					if ((type.equalsIgnoreCase("combine")
//							& (name.equalsIgnoreCase("exposure_or_event")
//									|| name.equalsIgnoreCase("date_of_event_incurred")
//									|| name.equalsIgnoreCase("type_of_event"))
//							|| pc.getPossiblesValues().contains("empty"))) {
//						is_mand.add(false);
//					} else {
//						is_mand.add(pc.isMandatory());
//					}
//					possibleVals.add(pc.getPossiblesValues());
//					break;
//				}
//			}
//			if (!found) {
//				types.add("free Text");
//				is_mand.add(false);
//				possibleVals.add(null);
//			}
//		}
//
//		ControlResultAccumulator df_acc = new ControlResultAccumulator(
//				new ControlResult("Date format", new ArrayList<>()));
//		sc.sc().register(df_acc);
//
//		ControlResultAccumulator nf_acc = new ControlResultAccumulator(
//				new ControlResult("Numeric format", new ArrayList<>()));
//		sc.sc().register(nf_acc);
//
//		ControlResultAccumulator cf_acc = new ControlResultAccumulator(
//				new ControlResult("Code format", new ArrayList<>()));
//		sc.sc().register(cf_acc);
//
//		ControlResultAccumulator refDataAcc = new ControlResultAccumulator(
//				new ControlResult("RefData format", new ArrayList<>()));
//		sc.sc().register(refDataAcc);
//
//		HashMapAccumulator numericValueAccumulator = new HashMapAccumulator();
//		sc.sc().register(numericValueAccumulator);
//
//		data.foreach(new VoidFunction<String>() {
//			/**
//			 *
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void call(String line) throws Exception {
//				// if(errorsCount.value() <= threshold) {
//				if (true) {
//					if (!line.equals("") & !line.equalsIgnoreCase(header)) {
//						String[] arr = line.toLowerCase().trim().split(";", -1);
//						String classification = null;
//						if (type.equalsIgnoreCase("split"))
//							classification = arr[names.indexOf("exposure_or_event")];
//						if (classification == null || classification.equalsIgnoreCase("exposure")
//								|| classification.equalsIgnoreCase("event")
//								|| classification.equalsIgnoreCase("exposure + event")) {
//							List<String> expo_mand_cols = Arrays.asList("exposure_or_event",
//									"date_of_begin_current_condition", "date_of_commencement",
//									"status_begin_current_condition", "status_end_current_condition",
//									"acceleration_risk_type", "main_risk_type", "product_id");
//							List<String> event_mand_cols = Arrays.asList("exposure_or_event", "date_of_event_incurred",
//									"type_of_event", "date_of_commencement", "acceleration_risk_type", "main_risk_type",
//									"product_id");
////							List<String> event_expo_mand_cols = Arrays.asList("exposure_or_event","date_of_commencement",
////									"main_risk_type","acceleration_risk_type","status_end_current_condition",
////									"status_begin_current_condition","date_of_begin_current_condition","product_id",
////									"type_of_event","date_of_event_incurred");
//							List<String> event_expo_mand_cols = Arrays.asList("status_begin_current_condition",
//									"date_of_begin_current_condition");
//
//							// System.out.println("*****************");
//							// System.out.println(arr.length);
//							// System.out.println(types.size());
//							// System.out.println(possibleVals.size());
//							for (int i = 0; i < arr.length; i++) {
//								if (i < types.size()) {
//									switch (types.get(i).trim().toLowerCase()) {
//									case "date":
//										if (!arr[i].matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//											if (arr[i].equals("")) {
//												if ("exposure".equalsIgnoreCase(classification)) {
//													if (expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														df_acc.add(new ControlResult("Date format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("event".equalsIgnoreCase(classification)) {
//													if (event_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														df_acc.add(new ControlResult("Date format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("exposure + event".equalsIgnoreCase(classification)) {
//													if (event_expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														df_acc.add(new ControlResult("Date format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if (is_mand.get(i)) {
//													List<AffectedColumn> affectedColumns = new ArrayList<>();
//													affectedColumns.add(new AffectedColumn(names.get(i), 1,
//															new ArrayList<>(Arrays.asList(line))));
//													df_acc.add(new ControlResult("Date format", affectedColumns));
//													errorsCount.add(1);
//												}
//											} else {
//												List<AffectedColumn> affectedColumns = new ArrayList<>();
//												affectedColumns.add(new AffectedColumn(names.get(i), 1,
//														new ArrayList<>(Arrays.asList(line))));
//												df_acc.add(new ControlResult("Date format", affectedColumns));
//												errorsCount.add(1);
//											}
//										} else {
//											try {
//												LocalDate doc = LocalDate.parse(arr[i],
//														DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//											} catch (Exception e) {
//												List<AffectedColumn> affectedColumns = new ArrayList<>();
//												affectedColumns.add(new AffectedColumn(names.get(i), 1,
//														new ArrayList<>(Arrays.asList(line))));
//												df_acc.add(new ControlResult("Date format", affectedColumns));
//												errorsCount.add(1);
//											}
//										}
//										break;
//									case "numeric":
//										HashMap<String, String> numericMap = numericValueAccumulator.value();
//										if (StringUtils.isNotBlank(arr[i]) && !arr[i].matches("\\d+")) {
//											if (!numericMap.containsKey(names.get(i))) {
//												if (arr[i].matches("^\\-?\\d+\\.?\\d*$")) {
//													HashMap<String, String> map = new HashMap<>();
//													map.put(names.get(i), ".");
//													numericValueAccumulator.add(map);
//												} else if (arr[i].matches("^\\-?\\d+\\,?\\d*$")) {
//													HashMap<String, String> map = new HashMap<>();
//													map.put(names.get(i), ",");
//													numericValueAccumulator.add(map);
//												}
//											}
//											if ((arr[i].matches("^\\-?\\d+\\.?\\d*$")
//													&& numericMap.get(names.get(i)) == ",")
//													|| (arr[i].matches("^\\-?\\d+\\,?\\d*$")
//															&& numericMap.get(names.get(i)) == ".")) {
//												List<AffectedColumn> affectedColumns = new ArrayList<>();
//												affectedColumns.add(new AffectedColumn(names.get(i), 1,
//														new ArrayList<>(Arrays.asList(line))));
//												nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//												errorsCount.add(1);
//											}
//										}
//										if (!arr[i].replaceAll("\\,", "\\.").matches("^\\-?\\d+\\.?\\d*$")) {
//											if (arr[i].equals("")) {
//												if ("exposure".equalsIgnoreCase(classification)) {
//													if (expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														nf_acc.add(
//																new ControlResult("Numeric format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("event".equalsIgnoreCase(classification)) {
//													if (event_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														nf_acc.add(
//																new ControlResult("Numeric format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("exposure + event".equalsIgnoreCase(classification)) {
//													if (event_expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														nf_acc.add(
//																new ControlResult("Numeric format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if (is_mand.get(i)) {
//													List<AffectedColumn> affectedColumns = new ArrayList<>();
//													affectedColumns.add(new AffectedColumn(names.get(i), 1,
//															new ArrayList<>(Arrays.asList(line))));
//													nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//													errorsCount.add(1);
//												}
//											} else {
//												List<AffectedColumn> affectedColumns = new ArrayList<>();
//												affectedColumns.add(new AffectedColumn(names.get(i), 1,
//														new ArrayList<>(Arrays.asList(line))));
//												nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//												errorsCount.add(1);
//											}
//										} else if (Double.parseDouble(arr[i].replaceAll("\\,", "\\.")) < 0
//												& names.get(i).matches(".*age.*")) {
//											List<AffectedColumn> affectedColumns = new ArrayList<>();
//											affectedColumns.add(new AffectedColumn(names.get(i), 1,
//													new ArrayList<>(Arrays.asList(line))));
//											nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//											errorsCount.add(1);
//										}
//										break;
//									case "numeric+":
//										HashMap<String, String> numericPlusMap = numericValueAccumulator.value();
//										if (StringUtils.isNotBlank(arr[i]) && !arr[i].matches("\\d+")) {
//											if (!numericPlusMap.containsKey(names.get(i))) {
//												if (arr[i].matches("^\\-?\\d+\\.\\d*$")) {
//													HashMap<String, String> map = new HashMap<>();
//													map.put(names.get(i), ".");
//													numericValueAccumulator.add(map);
//												} else if (arr[i].matches("^\\-?\\d+\\,\\d*$")) {
//													HashMap<String, String> map = new HashMap<>();
//													map.put(names.get(i), ",");
//													numericValueAccumulator.add(map);
//												}
//											}
//											if ((arr[i].matches("^\\-?\\d+\\.\\d*$")
//													&& numericPlusMap.get(names.get(i)) == ",")
//													|| (arr[i].matches("^\\-?\\d+\\,\\d*$")
//															&& numericPlusMap.get(names.get(i)) == ".")) {
//												List<AffectedColumn> affectedColumns = new ArrayList<>();
//												affectedColumns.add(new AffectedColumn(names.get(i), 1,
//														new ArrayList<>(Arrays.asList(line))));
//												nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//												errorsCount.add(1);
//											}
//										}
//										if (!arr[i].replaceAll("\\,", "\\.").matches("^\\-?\\d+\\.?\\d*$")) {
//											if (arr[i].equals("")) {
//												if ("exposure".equalsIgnoreCase(classification)) {
//													if (expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														nf_acc.add(
//																new ControlResult("Numeric format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("event".equalsIgnoreCase(classification)) {
//													if (event_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														nf_acc.add(
//																new ControlResult("Numeric format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("exposure + event".equalsIgnoreCase(classification)) {
//													if (event_expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														nf_acc.add(
//																new ControlResult("Numeric format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if (is_mand.get(i)) {
//													List<AffectedColumn> affectedColumns = new ArrayList<>();
//													affectedColumns.add(new AffectedColumn(names.get(i), 1,
//															new ArrayList<>(Arrays.asList(line))));
//													nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//													errorsCount.add(1);
//												}
//											} else {
//												List<AffectedColumn> affectedColumns = new ArrayList<>();
//												affectedColumns.add(new AffectedColumn(names.get(i), 1,
//														new ArrayList<>(Arrays.asList(line))));
//												nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//												errorsCount.add(1);
//											}
//										} else {
//											if (possibleVals.get(i) != null
//													&& possibleVals.get(i).contains("integer")) {
//												try {
//													int value = Integer.parseInt(arr[i]);
//													if (possibleVals.get(i).contains("not null")) {
//														if (value == 0) {
//															throw new Exception();
//														}
//													}
//												} catch (Exception e) {
//													List<AffectedColumn> affectedColumns = new ArrayList<>();
//													affectedColumns.add(new AffectedColumn(names.get(i), 1,
//															new ArrayList<>(Arrays.asList(line))));
//													nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//													errorsCount.add(1);
//													break;
//												}
//											} else if (possibleVals.get(i) != null
//													&& possibleVals.get(i).contains("double")) {
//												try {
//													double value = Double.parseDouble(arr[i]);
//													if (possibleVals.get(i).contains("null")) {
//														if (value < 0) {
//															throw new Exception();
//														}
//													}
//												} catch (Exception e) {
//													List<AffectedColumn> affectedColumns = new ArrayList<>();
//													affectedColumns.add(new AffectedColumn(names.get(i), 1,
//															new ArrayList<>(Arrays.asList(line))));
//													nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//													errorsCount.add(1);
//													break;
//												}
//
//											} else {
//												double doubleValue = Double
//														.parseDouble(arr[i].replaceAll("\\,", "\\."));
//												if (doubleValue <= 0) {
//													List<AffectedColumn> affectedColumns = new ArrayList<>();
//													affectedColumns.add(new AffectedColumn(names.get(i), 1,
//															new ArrayList<>(Arrays.asList(line))));
//													nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//													errorsCount.add(1);
//												}
//
//											}
//
//										}
//										break;
//									case "age":
//										if (!arr[i].matches("\\d+")) {
//											if (arr[i].equals("")) {
//												if ("exposure".equalsIgnoreCase(classification)) {
//													if (expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														nf_acc.add(
//																new ControlResult("Numeric format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("event".equalsIgnoreCase(classification)) {
//													if (event_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														nf_acc.add(
//																new ControlResult("Numeric format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("exposure + event".equalsIgnoreCase(classification)) {
//													if (event_expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														nf_acc.add(
//																new ControlResult("Numeric format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if (is_mand.get(i)) {
//													List<AffectedColumn> affectedColumns = new ArrayList<>();
//													affectedColumns.add(new AffectedColumn(names.get(i), 1,
//															new ArrayList<>(Arrays.asList(line))));
//													nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//													errorsCount.add(1);
//												}
//											} else {
//												List<AffectedColumn> affectedColumns = new ArrayList<>();
//												affectedColumns.add(new AffectedColumn(names.get(i), 1,
//														new ArrayList<>(Arrays.asList(line))));
//												nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//												errorsCount.add(1);
//											}
//										} else if (!(Integer.parseInt(arr[i]) >= 0)
//												|| !(Integer.parseInt(arr[i]) <= 150)) {
//											List<AffectedColumn> affectedColumns = new ArrayList<>();
//											affectedColumns.add(new AffectedColumn(names.get(i), 1,
//													new ArrayList<>(Arrays.asList(line))));
//											nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//											errorsCount.add(1);
//										}
//										break;
//									case "code":
//										if (possibleVals.get(i).contains(" ") && StringUtils.isBlank(arr[i])) {
//											continue;
//										}
//										if (!possibleVals.get(i).contains(arr[i]) || arr[i].equalsIgnoreCase("empty")) {
//
//											if (arr[i].equals("")) {
//												if ("exposure".equalsIgnoreCase(classification)) {
//													if (expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														cf_acc.add(new ControlResult("Code format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("event".equalsIgnoreCase(classification)) {
//													if (event_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														cf_acc.add(new ControlResult("Code format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if ("exposure + event".equalsIgnoreCase(classification)) {
//													if (event_expo_mand_cols.contains(names.get(i))) {
//														List<AffectedColumn> affectedColumns = new ArrayList<>();
//														affectedColumns.add(new AffectedColumn(names.get(i), 1,
//																new ArrayList<>(Arrays.asList(line))));
//														cf_acc.add(new ControlResult("Code format", affectedColumns));
//														errorsCount.add(1);
//													}
//												} else if (is_mand.get(i)) {
//													List<AffectedColumn> affectedColumns = new ArrayList<>();
//													affectedColumns.add(new AffectedColumn(names.get(i), 1,
//															new ArrayList<>(Arrays.asList(line))));
//													cf_acc.add(new ControlResult("Code format", affectedColumns));
//													errorsCount.add(1);
//												}
//											} else {
//												List<AffectedColumn> affectedColumns = new ArrayList<>();
//												affectedColumns.add(new AffectedColumn(names.get(i), 1,
//														new ArrayList<>(Arrays.asList(line))));
//												cf_acc.add(new ControlResult("Code format", affectedColumns));
//												errorsCount.add(1);
//											}
//										}
//										break;
//									case "snumeric":
//										try {
//											Double value = Double.parseDouble(arr[i].trim().replaceAll("\\,", "\\."));
//											if (value < 0 || value > 1) {
//												List<AffectedColumn> affectedColumns = new ArrayList<>();
//												affectedColumns.add(new AffectedColumn(names.get(i), 1,
//														new ArrayList<>(Arrays.asList(line))));
//												nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//												errorsCount.add(1);
//											}
//
//										} catch (Exception e) {
//											System.out.println(e);
//										}
//										break;
//									case "refdata":
//										if (StringUtils.isEmpty(arr[i]) && !is_mand.get(i)) {
//											continue;
//										}
//										List<String> possibleValues = refData.get(names.get(i));
//										if (possibleValues.contains(arr[i].trim())) {
//											continue;
//										}
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										refDataAcc.add(new ControlResult("RefData format", affectedColumns));
//										errorsCount.add(1);
//										break;
//									default:
//										break;
//									}
//								}
//							}
//						} else {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(
//									new AffectedColumn("Exposure or Event", 1, new ArrayList<>(Arrays.asList(line))));
//							cf_acc.add(new ControlResult("Code format", affectedColumns));
//							errorsCount.add(1);
//						}
//
//					}
//				}
//
//			}
//
//		});
//
//		List<ControlResult> controlResultsList = new ArrayList<>();
//		controlResultsList.add(df_acc.value());
//		controlResultsList.add(nf_acc.value());
//		controlResultsList.add(cf_acc.value());
//		controlResultsList.add(refDataAcc.value());
//		controlResultsList.add(headersControl);
//
//		return new ControlResults(threshold, errorsCount.value(), controlResultsList, header
//				+ ";age_at_commencement_definition;product_start_date;product_end_date;min_face_amount;max_face_amount;min_age_at_commencement;max_age_at_commencement;duplicated_product_id;client_risk_carrier_name;study_client;client_group;study_client_group;treaty_number_omega;study_treaty_number;distribution_brand_name;distribution_brand;client_country;study_country",
//				new HashMap<>());
//	}
//
//	public ControlResults runProduct(String path, Map<String, List<String>> refData) {
//
//		JavaSparkContext sc = Connection.getContext();
//		List<PivotCol> pivotColsProduct = DataPivot.getPivotColsProduct();
//
//		JavaRDD<String> data = sc.textFile(path);
//		String header = data.first();
//		List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));
//
//		// Long threshold = (10*(data.count()-1)*names.size())/100 ;
//		Long threshold = (10 * (data.count() - 1)) / 100;
//
//		// System.out.println("___________________________");
//		// System.out.println(threshold);
//		// System.out.println("___________________________");
//		LongAccumulator errorsCount = sc.sc().longAccumulator();
//
//		List<String> types = new ArrayList<>();
//		List<Boolean> is_mand = new ArrayList<>();
//		List<List<String>> possibleValsProd = new ArrayList<>();
//		int emptyHeader = 0;
//		ControlResult headersControl = new ControlResult("Header format", new ArrayList<>());
//		for (String name : names) {
//			boolean found = false;
//			if (StringUtils.isBlank(name)) {
//				emptyHeader++;
//				List<AffectedColumn> affectedColumns = new ArrayList<>();
//				affectedColumns.add(new AffectedColumn("N/A", emptyHeader, new ArrayList<>(
//						Arrays.asList(header.replace("Data_Line", "Header").replace("Policy_ID", "N/A")))));
//				headersControl = new ControlResult("Header format", affectedColumns);
//				errorsCount.add(1);
//				types.add("free Text");
//				is_mand.add(false);
//				possibleValsProd.add(null);
//				continue;
//			}
//			for (PivotCol pc : pivotColsProduct) {
//				if (name.equals(pc.getName())) {
//					found = true;
//					types.add(pc.getType());
//					is_mand.add(pc.isMandatory());
//					possibleValsProd.add(pc.getPossiblesValues());
//					break;
//				}
//			}
//			if (!found) {
//				types.add("free Text");
//				is_mand.add(false);
//				possibleValsProd.add(null);
//			}
//		}
//
//		ControlResultAccumulator df_acc = new ControlResultAccumulator(
//				new ControlResult("Date format", new ArrayList<>()));
//		sc.sc().register(df_acc);
//
//		ControlResultAccumulator nf_acc = new ControlResultAccumulator(
//				new ControlResult("Numeric format", new ArrayList<>()));
//		sc.sc().register(nf_acc);
//
//		ControlResultAccumulator cf_acc = new ControlResultAccumulator(
//				new ControlResult("Code format", new ArrayList<>()));
//		sc.sc().register(cf_acc);
//
//		ControlResultAccumulator refDataAcc = new ControlResultAccumulator(
//				new ControlResult("RefData format", new ArrayList<>()));
//		sc.sc().register(refDataAcc);
//
//		HashMapAccumulator numericValueAccumulator = new HashMapAccumulator();
//		sc.sc().register(numericValueAccumulator);
//
//		data.foreach(new VoidFunction<String>() {
//
//			@Override
//			public void call(String line) throws Exception {
//				// if(errorsCount.value() <= threshold) {
//				if (true) {
//					if (!line.equals("") & !line.equalsIgnoreCase(header)) {
//
//						String[] arr = line.toLowerCase().trim().split(";", -1);
//
//						for (int i = 0; i < arr.length; i++) {
//							switch (types.get(i)) {
//							case "date":
//								if (!arr[i].matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//									if (arr[i].equals("")) {
//										if (is_mand.get(i)) {
//											List<AffectedColumn> affectedColumns = new ArrayList<>();
//											affectedColumns.add(new AffectedColumn(names.get(i), 1,
//													new ArrayList<>(Arrays.asList(line))));
//											df_acc.add(new ControlResult("Date format", affectedColumns));
//											errorsCount.add(1);
//										}
//									} else {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										df_acc.add(new ControlResult("Date format", affectedColumns));
//										errorsCount.add(1);
//									}
//								} else {
//									try {
//										LocalDate doc = LocalDate.parse(arr[i],
//												DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//									} catch (Exception e) {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										df_acc.add(new ControlResult("Date format", affectedColumns));
//										errorsCount.add(1);
//									}
//								}
//								break;
//							case "numeric":
//								HashMap<String, String> numericMap = numericValueAccumulator.value();
//								if (StringUtils.isNotBlank(arr[i]) && !arr[i].matches("\\d+")) {
//									if (!numericMap.containsKey(names.get(i))) {
//										if (arr[i].matches("^\\-?\\d+\\.?\\d*$")) {
//											HashMap<String, String> map = new HashMap<>();
//											map.put(names.get(i), ".");
//											numericValueAccumulator.add(map);
//										} else if (arr[i].matches("^\\-?\\d+\\,?\\d*$")) {
//											HashMap<String, String> map = new HashMap<>();
//											map.put(names.get(i), ",");
//											numericValueAccumulator.add(map);
//										}
//									}
//									if ((arr[i].matches("^\\-?\\d+\\.?\\d*$") && numericMap.get(names.get(i)) == ",")
//											|| (arr[i].matches("^\\-?\\d+\\,?\\d*$")
//													&& numericMap.get(names.get(i)) == ".")) {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//										errorsCount.add(1);
//									}
//								}
//								if (!arr[i].matches("^\\-?\\d+\\,?\\d*$")) {
//									if (arr[i].equals("")) {
//										if (is_mand.get(i)) {
//											List<AffectedColumn> affectedColumns = new ArrayList<>();
//											affectedColumns.add(new AffectedColumn(names.get(i), 1,
//													new ArrayList<>(Arrays.asList(line))));
//											nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//											errorsCount.add(1);
//										}
//									} else {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//										errorsCount.add(1);
//									}
//								}
//								break;
//							case "numeric+":
//								HashMap<String, String> numericPlusMap = numericValueAccumulator.value();
//								if (StringUtils.isNotBlank(arr[i]) && !arr[i].matches("\\d+")) {
//									if (!numericPlusMap.containsKey(names.get(i))) {
//										if (arr[i].matches("^\\-?\\d+\\.?\\d*$")) {
//											HashMap<String, String> map = new HashMap<>();
//											map.put(names.get(i), ".");
//											numericValueAccumulator.add(map);
//										} else if (arr[i].matches("^\\-?\\d+\\,?\\d*$")) {
//											HashMap<String, String> map = new HashMap<>();
//											map.put(names.get(i), ",");
//											numericValueAccumulator.add(map);
//										}
//									}
//									if ((arr[i].matches("^\\-?\\d+\\.?\\d*$")
//											&& numericPlusMap.get(names.get(i)) == ",")
//											|| (arr[i].matches("^\\-?\\d+\\,?\\d*$")
//													&& numericPlusMap.get(names.get(i)) == ".")) {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//										errorsCount.add(1);
//									}
//								}
//								if (!arr[i].replaceAll("\\,", "\\.").trim().matches("^\\-?\\d+\\.?\\d*$")) {
//									if (arr[i].equals("")) {
//										if (is_mand.get(i)) {
//											List<AffectedColumn> affectedColumns = new ArrayList<>();
//											affectedColumns.add(new AffectedColumn(names.get(i), 1,
//													new ArrayList<>(Arrays.asList(line))));
//											nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//											errorsCount.add(1);
//										}
//									} else {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//										errorsCount.add(1);
//									}
//
//								} else {
//									if (possibleValsProd.get(i) != null
//											&& possibleValsProd.get(i).contains("integer")) {
//										try {
//											int value = Integer.parseInt(arr[i]);
//											if (possibleValsProd.get(i).contains("not null")) {
//												if (value == 0) {
//													throw new Exception();
//												}
//											}
//										} catch (Exception e) {
//											List<AffectedColumn> affectedColumns = new ArrayList<>();
//											affectedColumns.add(new AffectedColumn(names.get(i), 1,
//													new ArrayList<>(Arrays.asList(line))));
//											nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//											errorsCount.add(1);
//											break;
//										}
//									} else {
//										double doubleValue = Double.parseDouble(arr[i].replaceAll("\\,", "\\."));
//										if (doubleValue <= 0) {
//											List<AffectedColumn> affectedColumns = new ArrayList<>();
//											affectedColumns.add(new AffectedColumn(names.get(i), 1,
//													new ArrayList<>(Arrays.asList(line))));
//											nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//											errorsCount.add(1);
//										}
//									}
//								}
//								break;
//							case "age":
//								if (!arr[i].matches("\\d+")) {
//									if (arr[i].equals("")) {
//										if (is_mand.get(i)) {
//											List<AffectedColumn> affectedColumns = new ArrayList<>();
//											affectedColumns.add(new AffectedColumn(names.get(i), 1,
//													new ArrayList<>(Arrays.asList(line))));
//											nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//											errorsCount.add(1);
//										}
//									} else {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//										errorsCount.add(1);
//									}
//								} else if (!(Integer.parseInt(arr[i]) >= 0) || !(Integer.parseInt(arr[i]) <= 150)) {
//									List<AffectedColumn> affectedColumns = new ArrayList<>();
//									affectedColumns.add(
//											new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//									nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//									errorsCount.add(1);
//								}
//								break;
//							case "code":
//								if (!(possibleValsProd.get(i).contains(arr[i])) || arr[i].equalsIgnoreCase("empty")) {
////									System.out.println("---------->"+possibleValsProd.get(i)+"<-- do not contains --->"+arr[i]+"<--");
//									if (arr[i].equals("")) {
//										if (is_mand.get(i)) {
//											List<AffectedColumn> affectedColumns = new ArrayList<>();
//											affectedColumns.add(new AffectedColumn(names.get(i), 1,
//													new ArrayList<>(Arrays.asList(line))));
//											cf_acc.add(new ControlResult("Code format", affectedColumns));
//											errorsCount.add(1);
//										}
//									} else {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										cf_acc.add(new ControlResult("Code format", affectedColumns));
//										errorsCount.add(1);
//									}
//								}
//								break;
//							case "refdata":
//								if (StringUtils.isEmpty(arr[i]) && !is_mand.get(i)) {
//									continue;
//								}
//								List<String> possibleValues = refData.get(names.get(i));
//								if (possibleValues.contains(arr[i].trim())) {
//									continue;
//								}
//								List<AffectedColumn> affectedColumns = new ArrayList<>();
//								affectedColumns
//										.add(new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//								refDataAcc.add(new ControlResult("RefData format", affectedColumns));
//								errorsCount.add(1);
//								break;
//
//							default:
//								break;
//							}
//						}
//					}
//				}
//
//			}
//
//		});
//
//		List<ControlResult> controlResultsList = new ArrayList<>();
//		controlResultsList.add(df_acc.value());
//		controlResultsList.add(nf_acc.value());
//		controlResultsList.add(cf_acc.value());
//		controlResultsList.add(refDataAcc.value());
//		controlResultsList.add(headersControl);
//
//		return new ControlResults(threshold, errorsCount.value(), controlResultsList, header, new HashMap<>());
//
//	}
//
//}
