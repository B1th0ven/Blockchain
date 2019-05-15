package com.scor.dataProcessing.sparkConnection.SparkExpectedTable;
//package com.scor.dataProcessing.spark.SparkExpectedTable;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.VoidFunction;
//import org.apache.spark.util.LongAccumulator;
//
//import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
//import com.scor.dataProcessing.common.ExpTablePivot;
//import com.scor.dataProcessing.models.AffectedColumn;
//import com.scor.dataProcessing.models.ControlResult;
//import com.scor.dataProcessing.models.ControlResults;
//import com.scor.dataProcessing.models.PivotCol;
//import com.scor.dataProcessing.spark.Connection;
//
//public class ExpTableTechControls implements Serializable {
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	public static ControlResults run(String path, String type, Map<String, List<String>> refData) throws Exception {
//
//		JavaSparkContext sc = Connection.getContext();
//		List<PivotCol> pivCols = ExpTablePivot.getPivotCols(type.trim().toLowerCase());
//
//		JavaRDD<String> data = sc.textFile(path);
//		String header = data.first();
//		List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));
//
//		List<String> types = new ArrayList<>();
//		List<Boolean> is_mand = new ArrayList<>();
//		List<List<String>> possibleVals = new ArrayList<>();
//
//		for (String name : names) {
//			boolean found = false;
//			for (PivotCol pc : pivCols) {
//
//				if (name.equals(pc.getName())) {
//					found = true;
//					types.add(pc.getType());
//					is_mand.add(pc.isMandatory());
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
//		ControlResultAccumulator nf_acc = new ControlResultAccumulator(
//				new ControlResult("Numeric format", new ArrayList<>()));
//		sc.sc().register(nf_acc);
//
//		ControlResultAccumulator cf_acc = new ControlResultAccumulator(
//				new ControlResult("Code format", new ArrayList<>()));
//		sc.sc().register(cf_acc);
//
//		ControlResultAccumulator rd_acc = new ControlResultAccumulator(
//				new ControlResult("RefData format", new ArrayList<>()));
//		sc.sc().register(rd_acc);
//
//		ControlResultAccumulator pointAccumulator = new ControlResultAccumulator(
//				new ControlResult("Numeric format", new ArrayList<>()));
//		sc.sc().register(pointAccumulator);
//
//		ControlResultAccumulator commaAccumulator = new ControlResultAccumulator(
//				new ControlResult("Numeric format", new ArrayList<>()));
//		sc.sc().register(commaAccumulator);
//
//		LongAccumulator errorsCount = sc.sc().longAccumulator();
//
//		int indexOfRate = names.indexOf("rate");
//		int indexOfBaseTable1Weightfactor = names.indexOf("base_table_1_weight_factor");
//
//		data.foreach(new VoidFunction<String>() {
//			/**
//			 *
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void call(String line) throws Exception {
//				if (!line.isEmpty() && !line.equalsIgnoreCase(header)) {
//					String[] arr = line.toLowerCase().split(";", -1);
//					for (int i = 0; i < arr.length; i++) {
//						switch (types.get(i).trim().toLowerCase()) {
//						case "numeric":
//
//							if (!arr[i].replaceAll("\\,", "\\.").trim().matches("^\\-?\\d+\\.?\\d*$")) {
//								List<AffectedColumn> affectedColumns = new ArrayList<>();
//								affectedColumns
//										.add(new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//								nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//								errorsCount.add(1);
//								break;
//							}
//							if (!possibleVals.get(i).isEmpty() && possibleVals.get(i).contains("integer")) {
//								try {
//									int value = Integer.parseInt(arr[i]);
//								} catch (Exception e) {
//									List<AffectedColumn> affectedColumns = new ArrayList<>();
//									affectedColumns.add(
//											new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//									nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//									errorsCount.add(1);
//								}
//								break;
//							}
//							if (arr[i].contains(".")) {
//								List<AffectedColumn> affectedColumns = new ArrayList<>();
//								affectedColumns
//										.add(new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//								pointAccumulator.add(new ControlResult("Numeric format", affectedColumns));
//							}
//							if (arr[i].contains(",")) {
//								List<AffectedColumn> affectedColumns = new ArrayList<>();
//								affectedColumns
//										.add(new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//								commaAccumulator.add(new ControlResult("Numeric format", affectedColumns));
//							}
//							break;
//						case "numeric+":
//							if (!arr[i].replaceAll("\\,", "\\.").trim().matches("^\\-?\\d+\\.?\\d*$")) {
//								List<AffectedColumn> affectedColumns = new ArrayList<>();
//								affectedColumns
//										.add(new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//								nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//								errorsCount.add(1);
//							} else {
//								if (!possibleVals.get(i).isEmpty() && possibleVals.get(i).contains("integer")) {
//									try {
//										int value = Integer.parseInt(arr[i]);
//										if (value < 0) {
//											throw new Exception();
//										}
//										if (possibleVals.get(i).contains("not null") && value == 0) {
//											throw new Exception();
//										}
//									} catch (Exception e) {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//										errorsCount.add(1);
//									}
//									break;
//								} else {
//									double doubleValue = Double.parseDouble(arr[i].replaceAll("\\,", "\\."));
//									if ((!type.trim().toLowerCase().equals("policy") && doubleValue <= 0)
//											|| doubleValue < 0) {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//										errorsCount.add(1);
//										break;
//									} else if ((i == indexOfRate || i == indexOfBaseTable1Weightfactor)
//											&& doubleValue > 1) {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//										errorsCount.add(1);
//										break;
//									}
//									if (arr[i].contains(".")) {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										pointAccumulator.add(new ControlResult("Numeric format", affectedColumns));
//									}
//									if (arr[i].contains(",")) {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(names.get(i), 1,
//												new ArrayList<>(Arrays.asList(line))));
//										commaAccumulator.add(new ControlResult("Numeric format", affectedColumns));
//									}
//								}
//							}
//							break;
//						case "age":
//							if (!arr[i].trim().matches("\\d+") || (arr[i].isEmpty() && possibleVals.get(i).isEmpty())
//									|| (arr[i].isEmpty() && !possibleVals.get(i).isEmpty()
//											&& !possibleVals.get(i).contains("empty"))) {
//
//								List<AffectedColumn> affectedColumns = new ArrayList<>();
//								affectedColumns
//										.add(new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//								nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//								errorsCount.add(1);
//							} else if (!arr[i].isEmpty()) {
//								if (Integer.parseInt(arr[i]) < 0 || Integer.parseInt(arr[i]) > 150) {
//									List<AffectedColumn> affectedColumns = new ArrayList<>();
//									affectedColumns.add(
//											new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//									nf_acc.add(new ControlResult("Numeric format", affectedColumns));
//									errorsCount.add(1);
//								}
//							}
//							break;
//						case "code":
//							if (!possibleVals.get(i).isEmpty() && (!possibleVals.get(i).contains(arr[i]))) {
//								if (is_mand.get(i) || (!is_mand.get(i) && StringUtils.isNotBlank(arr[i]))) {
//									List<AffectedColumn> affectedColumns = new ArrayList<>();
//									affectedColumns.add(
//											new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//									cf_acc.add(new ControlResult("Code format", affectedColumns));
//									errorsCount.add(1);
//								}
//							}
//							/*
//							 * if(!possibleVals.get(i).isEmpty() &&
//							 * possibleVals.get(i).contains(arr[i].trim()) &&
//							 * Character.isWhitespace(arr[i].charAt(0))) { List<AffectedColumn>
//							 * affectedColumns = new ArrayList<>(); affectedColumns.add(new
//							 * AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//							 * cf_acc.add(new ControlResult("Code format", affectedColumns));
//							 * errorsCount.add(1); }
//							 */
//							break;
//						case "refdata":
//							if (StringUtils.isEmpty(arr[i]) && !is_mand.get(i)) {
//								continue;
//							}
//							List<String> possibleValues = refData.get(names.get(i));
//							if (possibleValues.contains(arr[i].trim())) {
//								continue;
//							}
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns
//									.add(new AffectedColumn(names.get(i), 1, new ArrayList<>(Arrays.asList(line))));
//							rd_acc.add(new ControlResult("RefData format", affectedColumns));
//							errorsCount.add(1);
//							break;
//						default:
//							break;
//
//						}
//					}
//				}
//			}
//		});
//
//		List<ControlResult> controlResultsList = new ArrayList<>();
//		controlResultsList.add(nf_acc.value());
//		controlResultsList.add(cf_acc.value());
//		controlResultsList.add(rd_acc.value());
//		ControlResult pointAndCommaControl = new ControlResult("Numeric format", new ArrayList<>());
//		pointAccumulator.value().getAffectedColumns().forEach(affectedCol -> {
//			String colName = affectedCol.getName();
//			for (AffectedColumn aff : commaAccumulator.value().getAffectedColumns()) {
//				if (colName.equals(aff.getName())) {
//					pointAndCommaControl.getAffectedColumns().add(affectedCol);
//					errorsCount.add(affectedCol.getErrorsNumber());
//					break;
//				}
//			}
//
//		});
//		controlResultsList.add(pointAndCommaControl);
//
//		return new ControlResults(null, errorsCount.value(), controlResultsList, header, new HashMap<>());
//	}
//
//}
