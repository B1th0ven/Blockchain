package com.scor.dataProcessing.sparkConnection;
//package com.scor.dataProcessing.spark;
//
//import java.io.IOException;
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
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
//import com.scor.dataProcessing.accumulators.HashMapAccumulator;
//import com.scor.dataProcessing.accumulators.MapAccumulator;
//import com.scor.dataProcessing.common.DataProduct;
//import com.scor.dataProcessing.models.AffectedColumn;
//import com.scor.dataProcessing.models.ControlResult;
//import com.scor.dataProcessing.models.ControlResults;
//import com.scor.dataProcessing.models.Product;
//import com.scor.dataProcessing.spark.Helpers.Grouping;
//import com.scor.dataProcessing.spark.Helpers.Headers;
//import com.scor.dataProcessing.spark.Helpers.ReduceByDateBCC;
//import com.scor.dataProcessing.spark.Helpers.SortByDateBCC;
//import com.scor.dataProcessing.spark.Operations.BasicControls;
//import com.scor.dataProcessing.spark.Operations.Functionals.ControlReduceBy;
//
//@Service
//public class FuncControls implements Serializable {
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 897291079237263173L;
//
//	@Autowired
//	private FuncControlsService funcService;
//
//	@Autowired
//	private BasicControls basicControls;
//
//	public  ControlResults run(String path, String path_prod, String type, String op_start, String op_end,String Client,String ClientGroup,String Treaty,String distributionBrand,String country)
//			throws IOException {
//		// Getting product file and putting it in List of product object
//		DataProduct dataProduct = new DataProduct();
////		List<Product> products = dataProduct.getProd(path_prod);
//		Map<String, Product> products = dataProduct.getProduct(path_prod);
//		List<Product> duplicatedProduct = dataProduct.getDuplicatedProduct(path_prod);
//		// Policy file Functional Controls
//		ControlResults controlResultPolicy = runPolicyControl(path, path_prod, type, op_start, op_end, products);
//		// Product File Functional Controls
//		ControlResults controlResultProduct = runProductControl(path, path_prod, type, op_start, op_end,
//				duplicatedProduct, controlResultPolicy.getHeader().split(";").length,Client,ClientGroup,Treaty,distributionBrand,country);
//		// Merging and returning result
//		List<ControlResult> controlResults = controlResultPolicy.getControlResultsList();
//		controlResults.addAll(controlResultProduct.getControlResultsList());
//		return new ControlResults(null,
//				controlResultPolicy.getNumber_of_errors() + controlResultProduct.getNumber_of_errors(), controlResults,
//				controlResultPolicy.getHeader() + ";duplicated_product_id",controlResultPolicy.getFileValues());
//	}
//
//	public  ControlResults runProductControl(String path, String path_prod, String type, String op_start,
//			String op_end, List<Product> duplicatedProducts, int headerSize,String Client, String ClientGroup, String Treaty,String distributionBrand,String country) throws IOException {
//		String row = "";
//		for (int i = 0; i < headerSize; i++)
//			row += ";";
//		final String row_2 = row;
//		// Defining Control Result
//		List<ControlResult> controlResultsList = new ArrayList<>();
//		AffectedColumn affectedColumn = new AffectedColumn("Unique ProductID Control", 0, new ArrayList<>());
//
//		if (duplicatedProducts != null && !duplicatedProducts.isEmpty()) {
//			String rowExemple = "";
//			for (int i = 0; i < headerSize-10; i++)
//				rowExemple += ";";
//			final String rowExemple_2 = rowExemple;
//			duplicatedProducts.parallelStream().forEach(product -> {
//				affectedColumn.setErrorsNumber(affectedColumn.getErrorsNumber() + 1);
//				affectedColumn.getExamples().add(rowExemple_2 + product.getId());
//			});
//		}
//		List<AffectedColumn> affectedColumns = new ArrayList<>();
//		if (affectedColumn.getErrorsNumber() > 0) {
//			affectedColumns.add(affectedColumn);
//		}
//		ControlResult productCoherenceMetadata = funcService.CheckClientAndClientGroupAndTreaties(path_prod,Client,ClientGroup,Treaty,distributionBrand,country,row_2);
//
//		controlResultsList.add(new ControlResult("Unique ProductID Control", affectedColumns));
//		controlResultsList.add(productCoherenceMetadata);
//		return new ControlResults(null, (long) affectedColumn.getErrorsNumber(), controlResultsList, "", new HashMap<>());
//	}
//
//	public  ControlResults runPolicyControl(String path, String path_prod, String type, String op_start,
//			String op_end, Map<String, Product> products) throws IOException {
//		JavaSparkContext sc = Connection.getContext();
//
//		JavaRDD<String> data = sc.textFile(path);
//		String header = data.first();
//		List<String> names = Arrays.asList(header.toLowerCase().split(";", -1));
//
//		JavaRDD<String> dataP = sc.textFile(path_prod);
//		String headerP = dataP.first();
//		List<String> namesP = Arrays.asList(headerP.toLowerCase().split(";", -1));
//
//		ArrayList<ArrayList<String>> dates_to_comp = new ArrayList<>();
//		dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_birth", "date_of_commencement")));
//		dates_to_comp.add(
//				new ArrayList<>(Arrays.asList("date_of_begin_current_condition", "date_of_end_current_condition")));
//		dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_commencement", "date_of_event_incurred")));
//		dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_commencement", "benefit_end_date")));
//		dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_commencement", "date_of_begin_current_condition")));
//
//		dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_event_incurred", "benefit_end_date"))); // sprint 3
//		dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_end_current_condition", "benefit_end_date"))); // sprint3
////		dates_to_comp.add(new ArrayList<>(Arrays.asList("date_of_event_incurred", "date_of_event_notified",
////				"date_of_event_settled", "date_of_event_paid"))); // sprint3
//
//		ArrayList<ArrayList<String>> floats_to_comp = new ArrayList<>();
//		floats_to_comp.add(new ArrayList<>(Arrays.asList("risk_amount_reinsurer", "risk_amount_insurer")));
//
//		ArrayList<ArrayList<String>> dates_to_com_to_input = new ArrayList<>();
//		dates_to_com_to_input.add(new ArrayList<>(Arrays.asList("start_of_observation_period", "benefit_end_date")));
//		dates_to_com_to_input
//				.add(new ArrayList<>(Arrays.asList("date_of_end_current_condition", "end_of_observation_period")));
//		dates_to_com_to_input
//				.add(new ArrayList<>(Arrays.asList("start_of_observation_period", "date_of_event_incurred")));
//
//		ArrayList<String> risks_to_com = new ArrayList<>();
//		risks_to_com.add("cici");
//		risks_to_com.add("cilifeci");
//		risks_to_com.add("tpdtpd");
//		risks_to_com.add("tpdlifetpd");
//		risks_to_com.add("ltcltc");
//		risks_to_com.add("ltclifeltc");
//		risks_to_com.add("lifecici");
//		risks_to_com.add("lifecilifeci");
//		risks_to_com.add("lifetpdtpd");
//		risks_to_com.add("lifetpdlifetpd");
//		risks_to_com.add("lifeltcltc");
//		risks_to_com.add("lifeltclifeltc");
//
//		ControlResultAccumulator dc_acc = new ControlResultAccumulator(
//				new ControlResult("Date Comparison", new ArrayList<>()));
//		sc.sc().register(dc_acc);
//
//		ControlResultAccumulator fc_acc = new ControlResultAccumulator(
//				new ControlResult("Amount Comparison", new ArrayList<>()));
//		sc.sc().register(fc_acc);
//
//		ControlResultAccumulator cc_acc = new ControlResultAccumulator(
//				new ControlResult("Consistenct Check", new ArrayList<>()));
//		sc.sc().register(cc_acc);
//
//		ControlResultAccumulator overlap_acc = new ControlResultAccumulator(
//				new ControlResult("Overlap Check", new ArrayList<>()));
//		sc.sc().register(overlap_acc);
//
//		ControlResultAccumulator eec_acc = new ControlResultAccumulator(
//				new ControlResult("Event Existence Check", new ArrayList<>()));
//		sc.sc().register(eec_acc);
//
//		ControlResultAccumulator active_acc = new ControlResultAccumulator(
//				new ControlResult("No exposure following terminating status at end condition", new ArrayList<>()));
//		sc.sc().register(active_acc);
//
//		ControlResultAccumulator coh_acc = new ControlResultAccumulator(
//				new ControlResult("Status coherent with event type", new ArrayList<>()));
//		sc.sc().register(coh_acc);
//
////		ControlResultAccumulator claims_acc = new ControlResultAccumulator(
////				new ControlResult("Claims existence check", new ArrayList<>()));
////		sc.sc().register(claims_acc); // sprint 3
//
//		ControlResultAccumulator lum_sum_acc = new ControlResultAccumulator(
//				new ControlResult("Lump sum", new ArrayList<>()));
//		sc.sc().register(lum_sum_acc);
//
//		ControlResultAccumulator exposure_coherent_status = new ControlResultAccumulator(
//				new ControlResult("Exposure end coherent with status", new ArrayList<>()));
//		sc.sc().register(exposure_coherent_status);
//
//		ControlResultAccumulator product_id_accumulator = new ControlResultAccumulator(
//				new ControlResult("Checking if Product Id exists in product file", new ArrayList<>()));
//		sc.sc().register(product_id_accumulator);
//
//		ControlResultAccumulator dateOfEventEqDateOfBeginCurrentConditionAccumulator = new ControlResultAccumulator(
//				new ControlResult("Date of Event = Date of Begin Current Condition", new ArrayList<>()));
//		sc.sc().register(dateOfEventEqDateOfBeginCurrentConditionAccumulator);
//
//		ControlResultAccumulator missingValuesCheckAccumulator = new ControlResultAccumulator(
//				new ControlResult("Missing Values Check", new ArrayList<>()));
//		sc.sc().register(missingValuesCheckAccumulator);
//
//		ControlResultAccumulator missingValuesCheck_2Accumulator = new ControlResultAccumulator(
//				new ControlResult("Missing Values Check_2", new ArrayList<>()));
//		sc.sc().register(missingValuesCheck_2Accumulator);
//
//		ControlResultAccumulator missingValuesCheckAccumulator2 = new ControlResultAccumulator(
//				new ControlResult("Missing Values Check Blocking", new ArrayList<>()));
//		sc.sc().register(missingValuesCheckAccumulator2);
//
//		ControlResultAccumulator coherenceCheck = new ControlResultAccumulator(
//				new ControlResult("Coherence Check", new ArrayList<>()));
//		sc.sc().register(coherenceCheck);
//
//		ControlResultAccumulator eventAmountReInsurerLowerThanInsurerAccumulator = new ControlResultAccumulator(
//				new ControlResult("Event Amount Reinsurer <= Event Amount Insurer", new ArrayList<>()));
//		sc.sc().register(eventAmountReInsurerLowerThanInsurerAccumulator);
//
//		ControlResultAccumulator consistentDateOfLastMedicalSelectionAccumulator = new ControlResultAccumulator(
//				new ControlResult("Consistent date of last medical selection", new ArrayList<>()));
//		sc.sc().register(consistentDateOfLastMedicalSelectionAccumulator);
//
//		ControlResultAccumulator coherenceOfBenefitMaxAge = new ControlResultAccumulator(
//				new ControlResult("Coherence of Benefit Max Age with policy file", new ArrayList<>()));
//		sc.sc().register(coherenceOfBenefitMaxAge);
//
//		ControlResultAccumulator incDeathCheck = new ControlResultAccumulator(
//				new ControlResult("Incidence_Death_Check", new ArrayList<>()));
//		sc.sc().register(incDeathCheck);
//		ControlResultAccumulator claiDeadCheck = new ControlResultAccumulator(
//				new ControlResult("Claimant_Dead_Check", new ArrayList<>()));
//		sc.sc().register(claiDeadCheck);
//
//		MapAccumulator valuesPersistAccumulator = new MapAccumulator();
//		sc.sc().register(valuesPersistAccumulator);
//
//        HashMapAccumulator incDeath_claiDead_Check = new HashMapAccumulator();
//        sc.sc().register(incDeath_claiDead_Check);
//
//		final HashMap<Integer, String> missingValuesCheckHeaders = fillHeadersForEmptyCheck(names);
//		final HashMap<Integer, String> missingValuesCheckHeadersEntireFile = fillHeadersForEmptyCheckEntireFile(names);
//		final Map<Integer, Integer> missingValuesWithConditionHeaders = fillHeadersForEmptyCheckWithCondition(names);
//		final Map<Integer, String> missingValuesWithConditionHeaders2 = fillHeadersForEmptyCheckWithCondition2(names);
//
//		LongAccumulator errorsCount = sc.sc().longAccumulator();
//
////		EventAccumulator eventsAcc = new EventAccumulator();
////		sc.sc().register(eventsAcc); // sprint 3
//
//		//data.cache();
//
//
//		data.foreach(new VoidFunction<String>() {
//
//
//			/**
//			 *
//			 */
//			private static final long serialVersionUID = -7136621943484045923L;
//
//			private List<AffectedColumn> missingValuesCheck(HashMap<Integer, String> missingValuesCheckHeaders,
//					Map<Integer, Integer> missingValuesWithConditionHeaders, String[] row_arr, String row) {
//				List<AffectedColumn> affectedColumnss = new ArrayList<>();
//				missingValuesCheckHeaders.entrySet().forEach(header -> {
//					if (StringUtils.isBlank(row_arr[header.getKey()])) {
//						errorsCount.add(1);
//						affectedColumnss.add(
//								new AffectedColumn(header.getValue() + "/" + header.getValue(), 1, new ArrayList<>(Arrays.asList(row))));
//					}
//				});
//				missingValuesWithConditionHeaders.entrySet().forEach(header -> {
//					if (StringUtils.isNotBlank(row_arr[header.getValue()])
//							&& StringUtils.isBlank(row_arr[header.getKey()])) {
//						errorsCount.add(1);
//						affectedColumnss.add(
//								new AffectedColumn(names.get(header.getKey()) + "/" + names.get(header.getValue()), 1, new ArrayList<>(Arrays.asList(row))));
//					}
//				});
//				return affectedColumnss;
//			}
//
//			private List<AffectedColumn> missingValuesCheckEntireFile(HashMap<Integer, String> missingValuesCheckHeadersEntireFile, String[] row_arr, String row) {
//				List<AffectedColumn> affectedColumnss = new ArrayList<>();
//				missingValuesCheckHeadersEntireFile.entrySet().forEach(header -> {
//					if (StringUtils.isBlank(row_arr[header.getKey()])) {
//						errorsCount.add(1);
//						affectedColumnss.add(
//								new AffectedColumn(header.getValue() + "/" + header.getValue(), 1, new ArrayList<>(Arrays.asList(row))));
//					}
//				});
//				return affectedColumnss;
//			}
//
//			private List<AffectedColumn> missingValuesCheckBlocking(
//					Map<Integer, String> missingValuesWithConditionHeaders, String[] row_arr, String row, Product product) {
//				List<AffectedColumn> affectedColumnss = new ArrayList<>();
//				missingValuesWithConditionHeaders.entrySet().forEach(header -> {
//					String headerName = header.getValue();
//					String value = null;
//					if("rating_type_1".equals(headerName)) {
//						if(product != null){
//						value = product.getRating_type_1();
//						headerName += "/product_id";
//						}
//					} else if("rating_type_2".equals(headerName)) {
//						if(product != null) {
//							value = product.getRating_type_2();
//							headerName += "/product_id";
//						}
//					} else {
//						value = names.indexOf(headerName) > -1 ? row_arr[names.indexOf(headerName)] : "";
//					}
//					if (StringUtils.isNotBlank(row_arr[header.getKey()]) && (StringUtils.isBlank(value))) {
//						errorsCount.add(1);
//						affectedColumnss.add(new AffectedColumn(headerName + "/" + names.get(header.getKey()), 1,
//								new ArrayList<>(Arrays.asList(row))));
//					}
//				});
//				return affectedColumnss;
//			}
//
//			@Override
//			public void call(String row) throws Exception {
//				if (!row.equals("") & !row.equalsIgnoreCase(header)) {
//					String[] row_arr = row.toLowerCase().trim().split(";", -1);
//					Product product = products.get(row_arr[names.indexOf("product_id")].trim());
//
//					if (type.equalsIgnoreCase("combine") || (type.equalsIgnoreCase("split")
//							&& names.contains("exposure_or_event")
//							&& ("exposure + event".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")])
//									|| "exposure".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")])))) {
//						List<AffectedColumn> affectedColumnsMissingValues = missingValuesCheck(
//								missingValuesCheckHeaders, missingValuesWithConditionHeaders, row_arr, row);
//						if (!affectedColumnsMissingValues.isEmpty()) {
//							missingValuesCheckAccumulator
//									.add(new ControlResult("Missing Values Check", affectedColumnsMissingValues));
//						}
//					}
//					List<AffectedColumn> affectedColumnsMissingValuesEntireFile = missingValuesCheckEntireFile(missingValuesCheckHeadersEntireFile,
//							 row_arr, row);
//					if (!affectedColumnsMissingValuesEntireFile.isEmpty()) {
//						missingValuesCheck_2Accumulator
//								.add(new ControlResult("Missing Values Check_2", affectedColumnsMissingValuesEntireFile));
//					}
//
////					if (names.containsAll(Arrays.asList("type_of_event", "date_of_event_incurred",
////							"smoker_status_detailed", "smoker_status", "benefit_term_years", "benefit_end_age",
////							"benefit_term_type", "multiplicative_rated_status", "additive_rated_status",
////							"waiting_period_1_type", "waiting_period_1", "waiting_period_2_type", "waiting_period_2",
////							"waiting_period_3_type", "waiting_period_3", "temp_mult_extra_rating_dur_1",
////							"temp_mult_extra_rating_1", "temp_mult_extra_rating_dur_2", "temp_mult_extra_rating_2",
////							"temp_add_extra_rating_dur_1", "temp_add_extra_rating_1", "temp_add_extra_rating_dur_2",
////							"temp_add_extra_rating_2", "child_benefit_type", "child_benefit", "acceleration_risk_type",
////							"acceleration_benefit", "buyback_option_type", "buyback_option", "benefit_change_rate",
////							"benefit_change_frequency"))) {
//						List<AffectedColumn> affectedColumnsMissingValues2 = missingValuesCheckBlocking(
//								missingValuesWithConditionHeaders2, row_arr, row,product);
//						if (!affectedColumnsMissingValues2.isEmpty()) {
//							missingValuesCheckAccumulator2.add(
//									new ControlResult("Missing Values Check Blocking", affectedColumnsMissingValues2));
//						}
////					}
//
//
//					basicControls.control_33(names,row_arr,dateOfEventEqDateOfBeginCurrentConditionAccumulator,errorsCount);
//
//					// When death / withdrawal (lump sum), Risk Amount = Event Amount INSURER sprint 3
//					if (names.containsAll(Arrays.asList("event_amount_insurer", "risk_amount_insurer"))) {
//						String te = row_arr[names.indexOf("type_of_event")];
//						String ei = names.indexOf("expenses_included") > -1 ? row_arr[names.indexOf("expenses_included")] : "";
//						String sd = names.indexOf("settlement_decision") > -1 ? row_arr[names.indexOf("settlement_decision")] : "";
//						String eai = row_arr[names.indexOf("event_amount_insurer")].replace(",", ".");
//						String rai = row_arr[names.indexOf("risk_amount_insurer")].replace(",", ".");
//
//						if(!funcService.control24(te, ei, sd, eai, rai)) {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn(
//									"when death / withdrawal (lump sum), risk amount = event amount", 1,
//									new ArrayList<>(Arrays.asList(row))));
//							lum_sum_acc.add(new ControlResult("Lump sum", affectedColumns));
//							errorsCount.add(1);
//						}
//
//
//					}
//
//					// When death / withdrawal (lump sum), Risk Amount = Event Amount REINSURER sprint 3
//					if (names.containsAll(Arrays.asList("event_amount_reinsurer", "risk_amount_reinsurer"))) {
//						String te = row_arr[names.indexOf("type_of_event")];
//						String ei = names.indexOf("expenses_included") > -1 ? row_arr[names.indexOf("expenses_included")] : "";
//						String sd = names.indexOf("settlement_decision") > -1 ? row_arr[names.indexOf("settlement_decision")] : "";
//						String eari = row_arr[names.indexOf("event_amount_reinsurer")].replace(",", ".");
//						String rari = row_arr[names.indexOf("risk_amount_reinsurer")].replace(",", ".");
//
//						if(!funcService.control24(te, ei, sd, eari, rari)) {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn(
//									"when death / withdrawal (lump sum), risk amount = event amount", 1,
//									new ArrayList<>(Arrays.asList(row))));
//							lum_sum_acc.add(new ControlResult("Lump sum", affectedColumns));
//							errorsCount.add(1);
//						}
//
//
//					}
//
//				basicControls.DatesComparaison(names,row_arr,dates_to_comp,dc_acc,errorsCount);
//
//
//
//					String dateOfEventIncurred = (names.indexOf("date_of_event_incurred") >= 0)
//							? row_arr[names.indexOf("date_of_event_incurred")]
//							: "";
//					String dateOfEventNotified = (names.indexOf("date_of_event_notified") >= 0)
//							? row_arr[names.indexOf("date_of_event_notified")]
//							: "";
//					String dateOfEventSettled = (names.indexOf("date_of_event_settled") >= 0)
//							? row_arr[names.indexOf("date_of_event_settled")]
//							: "";
//					String dateOfEventPaid = (names.indexOf("date_of_event_paid") >= 0)
//							? row_arr[names.indexOf("date_of_event_paid")]
//							: "";
//					if (!funcService.control20(dateOfEventIncurred, dateOfEventNotified, dateOfEventSettled,
//							dateOfEventPaid)) {
//						List<AffectedColumn> affectedColumns = new ArrayList<>();
//						affectedColumns.add(new AffectedColumn(
//								"date of event incurred & date of event notified & date of event settled & date of event paid",
//								1, new ArrayList<>(Arrays.asList(row))));
//						dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//						errorsCount.add(1);
//					}
//
//					// sprint 6 Product start date <= Date of Commencement <= Product end date
//
//					if (namesP.containsAll(Arrays.asList("product_start_date", "product_end_date"))) {
//						String prod_id = row_arr[names.indexOf("product_id")].trim();
//						String p_start = null;
//						String p_end = null;
//						if (product != null) {
//							p_start = product.getP_start_date();
//							p_end = product.getP_end_date();
//						}
//
//						if (names.indexOf("date_of_commencement") >= 0
//								&& row_arr[names.indexOf("date_of_commencement")]
//										.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")
//								&& p_start != null && p_start.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && p_end != null
//								&& p_end.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//
//							try {
//								LocalDate doc = LocalDate.parse(row_arr[names.indexOf("date_of_commencement")],
//										DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//								LocalDate p_start_date = LocalDate.parse(p_start,
//										DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//								LocalDate p_end_date = LocalDate.parse(p_end,
//										DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//
//								if (p_start_date.isAfter(doc) || doc.isAfter(p_end_date)
//										|| p_start_date.isAfter(p_end_date)) {
//
//									List<AffectedColumn> affectedColumns = new ArrayList<>();
//									affectedColumns.add(new AffectedColumn(
//											"product start date <= date of commencement <= product end date", 1,
//											new ArrayList<>(Arrays.asList(row + ";;" + p_start + ";" + p_end + ";;;;;"))));
//									dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//									errorsCount.add(1);
//								}
//							} catch (Exception e) {
//
//							}
//
//						}
//
//					}
//
//					if (product == null) {
//						List<AffectedColumn> affectedColumns = new ArrayList<>();
//						affectedColumns.add(new AffectedColumn("Product Id doesn't exists in product file", 1,
//								new ArrayList<>(Arrays.asList(row))));
//						product_id_accumulator.add(
//								new ControlResult("Checking if Product Id exists in product file", affectedColumns));
//						errorsCount.add(1);
//					}
//
//					// sprint6 Risk Amount Insurer <= Max Face Amount
//
//					if (names.contains("risk_amount_insurer") && namesP.contains("max_face_amount")) {
//						Double mfa = null;
//						Double rai = null;
//
//						if (product != null) {
//							if (product.getMax_face_amount() != null && !product.getMax_face_amount().isEmpty()
//									&& product.getMax_face_amount().replaceAll("\\,", "\\.")
//											.matches("^\\-?\\d+\\.?\\d*$"))
//								mfa = Double.parseDouble(product.getMax_face_amount().replaceAll("\\,", "\\."));
//						}
//						if (row_arr[names.indexOf("risk_amount_insurer")] != null
//								&& row_arr[names.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\.")
//										.matches("^\\-?\\d+\\.?\\d*$"))
//							rai = Double.parseDouble(
//									row_arr[names.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\."));
//
//						if (mfa != null && rai != null && rai > mfa) {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn("risk amount insurer <= max face amount", 1,
//									new ArrayList<>(Arrays.asList(row + ";;;;;" + product.getMax_face_amount() + ";;;"))));
//							fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//							errorsCount.add(1);
//						}
//					}
//
//					// sprint6 Risk Amount Reinsurer <= Max Face Amount
//
//					if (names.contains("risk_amount_reinsurer") && namesP.contains("max_face_amount")) {
//						Double mfa = null;
//						Double rari = null;
//						if (product != null) {
//							if (product.getMax_face_amount() != null && !product.getMax_face_amount().isEmpty()
//									&& product.getMax_face_amount().replaceAll("\\,", "\\.")
//											.matches("^\\-?\\d+\\.?\\d*$"))
//								mfa = Double.parseDouble(product.getMax_face_amount().replaceAll("\\,", "\\."));
//						}
//
//						if (row_arr[names.indexOf("risk_amount_reinsurer")] != null
//								&& row_arr[names.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\.")
//										.matches("^\\-?\\d+\\.?\\d*$"))
//							rari = Double.parseDouble(
//									row_arr[names.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\."));
//
//						if (mfa != null && rari != null && rari > mfa) {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn("risk amount reinsurer <= max face amount", 1,
//									new ArrayList<>(Arrays.asList(row + ";;;;;" + product.getMax_face_amount() + ";;;"))));
//							fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//							errorsCount.add(1);
//						}
//					}
//
//					// sprint 6 Min Face Amount <= Risk Amount Insurer
//
//					if (names.contains("risk_amount_insurer") && namesP.contains("min_face_amount")) {
//						Double min_fa = null;
//						Double rai = null;
//
//						if (product != null) {
//							if (product.getMin_face_amount() != null && !product.getMin_face_amount().isEmpty()
//									&& product.getMin_face_amount().replaceAll("\\,", "\\.")
//											.matches("^\\-?\\d+\\.?\\d*$"))
//								min_fa = Double.parseDouble(product.getMin_face_amount().replaceAll("\\,", "\\."));
//						}
//						if (row_arr[names.indexOf("risk_amount_insurer")] != null
//								&& row_arr[names.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\.")
//										.matches("^\\-?\\d+\\.?\\d*$"))
//							rai = Double.parseDouble(
//									row_arr[names.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\."));
//
//						// System.out.println("****************"+min_fa+"**"+rai);
//
//						if (min_fa != null && rai != null && rai < min_fa) {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn("min face amount <= risk amount insurer", 1,
//									new ArrayList<>(Arrays.asList(row + ";;;;" + product.getMin_face_amount() + ";;;;"))));
//							fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//							errorsCount.add(1);
//						}
//					}
//
//					// sprint 6 Min Face Amount <= Risk Amount Reinsurer
//
//					if (names.contains("risk_amount_reinsurer") && namesP.contains("min_face_amount")) {
//						Double min_fa = null;
//						Double rari = null;
//
//						if (product != null) {
//							if (product.getMin_face_amount() != null && !product.getMin_face_amount().isEmpty()
//									&& product.getMin_face_amount().replaceAll("\\,", "\\.")
//											.matches("^\\-?\\d+\\.?\\d*$"))
//								min_fa = Double.parseDouble(product.getMin_face_amount().replaceAll("\\,", "\\."));
//						}
//						if (row_arr[names.indexOf("risk_amount_reinsurer")] != null
//								&& row_arr[names.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\.")
//										.matches("^\\-?\\d+\\.?\\d*$"))
//							rari = Double.parseDouble(
//									row_arr[names.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\."));
//
//						if (min_fa != null && rari != null && rari < min_fa) {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn("min face amount <= risk amount reinsurer", 1,
//									new ArrayList<>(Arrays.asList(row + ";;;;" + product.getMin_face_amount() + ";;;;"))));
//							fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//							errorsCount.add(1);
//						}
//					}
//
//					// sprint 6 Min Age at commencement <= Age at Commencement <= Max Age at
//					// commencement
//
//					if (names.contains("age_at_commencement") && namesP
//							.containsAll(Arrays.asList("min_age_at_commencement", "max_age_at_commencement"))) {
//						Integer min_ac = null;
//						Integer max_ac = null;
//						Integer ac = null;
//						if (product != null) {
//							if (product.getMin_age_at_com() != null && product.getMin_age_at_com().matches("\\d+"))
//								min_ac = Integer.parseInt(product.getMin_age_at_com());
//							if (product.getMax_age_at_com() != null && product.getMax_age_at_com().matches("\\d+"))
//								max_ac = Integer.parseInt(product.getMax_age_at_com());
//						}
//
//						if (row_arr[names.indexOf("age_at_commencement")] != null
//								&& row_arr[names.indexOf("age_at_commencement")].matches("\\d+"))
//							ac = Integer.parseInt(row_arr[names.indexOf("age_at_commencement")]);
//
//						if (min_ac != null && max_ac != null && ac != null && (min_ac > ac || ac > max_ac)) {
//							// System.out.println("**************** "+min_ac+" <= "+ac+" <= "+max_ac);
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn(
//									"min age at commencement <= age at commencement <= max age at commencement", 1,
//									new ArrayList<>(Arrays.asList(row + ";;;;;;" + min_ac + ";" + max_ac + ";"))));
//							dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//							errorsCount.add(1);
//						}
//
//					}
//
//					//CR  Events should be coherent with the main risk type and the acceleration risk type For each line
//                    basicControls.EventsAndMainRiskTypeCoherence(names,row_arr,coherenceCheck,errorsCount);
//
//
//
//					// sprint 3 controles
//					// Start of observation period ≤ Cover Expiry Date
//					// Date of End Current Condition <= End of observation Period
//					// Start of observation period) <= Date of Begin Current Condition
//
//                    basicControls.DatesComparaisonToInput(names,row_arr,dates_to_com_to_input,op_start,op_end,dc_acc,errorsCount);
//
//
//
//                    basicControls.FloatsComparaison(names,row_arr,floats_to_comp,fc_acc,errorsCount);
//
//					String begin_stat = row_arr[names.indexOf("status_begin_current_condition")].trim();
//
//					if(names.indexOf("status_end_current_condition") > -1) {
//					// EVENT EXISTENCE AT ALL STATUS CHANGES :
//					String end_stat = row_arr[names.indexOf("status_end_current_condition")].trim();
//
//					boolean checkEventExistance = eventExistanceCheck(type, names, row_arr, begin_stat, end_stat);
//					if (!checkEventExistance) {
//						// System.out.println("censored & expired != "+end_stat);
//						List<AffectedColumn> affectedColumns = new ArrayList<>();
//						affectedColumns.add(new AffectedColumn("date of event incurred & type of event", 1,
//								new ArrayList<>(Arrays.asList(row))));
//						eec_acc.add(new ControlResult("Event Existence Check", affectedColumns));
//						errorsCount.add(1);
//					}
//                    }
//
//					// STATUS AT BEGIN SHOULD BE ALWAYS ACTIVE :
//					if ((type.equalsIgnoreCase("combine") || (type.equalsIgnoreCase("split")
//							&& names.contains("exposure_or_event")
//							&& ("exposure + event".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")])
//									|| "exposure".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")]))))
//							&& !"Active".equalsIgnoreCase(begin_stat)) {
//						List<AffectedColumn> affectedColumns = new ArrayList<>();
//						affectedColumns.add(
//								new AffectedColumn("exposure termination", 1, new ArrayList<>(Arrays.asList(row))));
//						active_acc.add(new ControlResult("No exposure following terminating status at end condition",
//								affectedColumns));
//						errorsCount.add(1);
//					}
//
//					// Status coherent with event type :
//					if (names.indexOf("status_end_current_condition") > -1 && (type.equalsIgnoreCase("combine")
//							|| (type.equalsIgnoreCase("split") && names.contains("exposure_or_event")
//									&& "exposure + event"
//											.equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")])))
//							&& !funcService.control13(row_arr[names.indexOf("status_begin_current_condition")],
//									row_arr[names.indexOf("status_end_current_condition")],
//									row_arr[names.indexOf("type_of_event")])) {
//
//						List<AffectedColumn> affectedColumns = new ArrayList<>();
//						affectedColumns.add(new AffectedColumn("Type of Event & Status at End Current Condition", 1,
//								new ArrayList<>(Arrays.asList(row))));
//						coh_acc.add(new ControlResult("Status coherent with event type", affectedColumns));
//						errorsCount.add(1);
//					}
//
//					if (names.containsAll(Arrays.asList("date_of_birth"))
//							&& namesP.containsAll(Arrays.asList("age_at_commencement_definition"))) {
//
//						String DOB_s = row_arr[names.indexOf("date_of_birth")].trim();
//						String prod_id = row_arr[names.indexOf("product_id")].trim();
//						String age_def = null;
//						if (product != null) {
//							age_def = product.getAge_def().trim();
//						}
//						if (names.containsAll(Arrays.asList("age_at_commencement", "date_of_commencement"))) {
//							String AAC_s = row_arr[names.indexOf("age_at_commencement")];
//							String DC_s = row_arr[names.indexOf("date_of_commencement")];
//							if (AAC_s.matches("^\\-?\\d+\\,?\\d*$") && DC_s.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")
//									&& DOB_s.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && age_def != null
//									&& !age_def.isEmpty()) {
//								try {
//									int AAC = Integer.parseInt(AAC_s);
//									LocalDate DC = LocalDate.parse(DC_s,
//											DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//									LocalDate DOB = LocalDate.parse(DOB_s,
//											DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//
////								long daysBetween = ChronoUnit.DAYS.between(DOB, DC);
////								double age = daysBetween / (double) 365.25;
//									int calculatedAge = 0;
//									int years = DC.getYear() - DOB.getYear();
//									float days = DC.getDayOfYear() - DOB.getDayOfYear();
//									float daysNearest = DC.getDayOfYear() - DOB.getDayOfYear() + 1;
//									int nbOfDaysPerYear = 365;
//									if((DC.getYear() % 4) == 0) {
//										nbOfDaysPerYear = 366;
//									}
//									double ageNext = years + (days / nbOfDaysPerYear);
//									double ageNearest = years + (daysNearest / nbOfDaysPerYear);
//
//									switch (age_def) {
//									case "age last birthday":
//										calculatedAge = (int) ageNext;
//										break;
//									case "age nearest birthday":
//										calculatedAge = (int) Math.round(ageNearest);
//										break;
//									case "age next birthday":
//										calculatedAge = (int) ageNext + 1;
//										break;
//									default:
//										break;
//									}
//									if (AAC != calculatedAge) {
//										List<AffectedColumn> affectedColumns = new ArrayList<>();
//										affectedColumns.add(new AffectedColumn(
//												"date of commencement = age at commencement - date of birth", 1,
//												new ArrayList<>(Arrays.asList(row + ";" + age_def))));
//										dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//										errorsCount.add(1);
//									}
//								} catch (Exception e) {
//
//								}
//							}
//						}
//
//						// sprint 3 Cover Expiry Date - Date of Birth <= Cover Expiry Age
//
//						if (DOB_s.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && age_def != null && !age_def.isEmpty()
//								&& names.containsAll(Arrays.asList("benefit_end_date"))
//								&& names.containsAll(Arrays.asList("benefit_max_age"))
//								&& row_arr[names.indexOf("benefit_max_age")].matches("^\\-?\\d+\\,?\\d*$")) {
//
//							try {
//								int coverExpiryAge = 0;
//								int cea = Integer.parseInt(row_arr[names.indexOf("benefit_max_age")]);
//								LocalDate ced = LocalDate.parse(row_arr[names.indexOf("benefit_end_date")],
//										DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//								LocalDate dob = LocalDate.parse(row_arr[names.indexOf("date_of_birth")],
//										DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//
////							long daysBetween = ChronoUnit.DAYS.between(dob, ced);
////							double age = daysBetween / (double) 365.25;
//								int years = ced.getYear() - dob.getYear();
//								float days = ced.getDayOfYear() - dob.getDayOfYear();
//								float daysNearest = ced.getDayOfYear() - dob.getDayOfYear() + 1;
//								int nbOfDaysPerYear = 365;
//								if((ced.getYear() % 4) == 0) {
//									nbOfDaysPerYear = 366;
//								}
//								double age = years + (days / nbOfDaysPerYear);
//								double ageNearest = years + (daysNearest / nbOfDaysPerYear);
//								switch (age_def) {
//								case "age last birthday":
//									coverExpiryAge = (int) age;
//									break;
//								case "age nearest birthday":
//									coverExpiryAge = (int) Math.round(ageNearest);
//									break;
//								case "age next birthday":
//									coverExpiryAge = (int) age + 1;
//									break;
//								default:
//									break;
//								}
//
//								if (cea < coverExpiryAge) {
//									List<AffectedColumn> affectedColumns = new ArrayList<>();
//									affectedColumns
//											.add(new AffectedColumn("cover expiry date - date of birth <= cover expiry age",
//													1, new ArrayList<>(Arrays.asList(row + ";" + age_def))));
//									dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//									errorsCount.add(1);
//								}
//							} catch (Exception e) {
//
//							}
//						}
//
//					}
//					if(names.containsAll(Arrays.asList("event_amount_reinsurer","event_amount_insurer"))) {
//						if(basicControls.eventAmountReInsurerLowerThanInsurer(row_arr[names.indexOf("event_amount_reinsurer")],row_arr[names.indexOf("event_amount_insurer")])) {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns
//									.add(new AffectedColumn("Event Amount Reinsurer <= Event Amount Insurer",
//											1, new ArrayList<>(Arrays.asList(row))));
//							eventAmountReInsurerLowerThanInsurerAccumulator.add(new ControlResult("Event Amount Reinsurer <= Event Amount Insurer", affectedColumns));
//							errorsCount.add(1);
//						}
//					}
//					if(names.indexOf("date_of_last_medical_selection")>-1 && names.indexOf("date_of_commencement")>-1) {
//						Boolean control = basicControls.dateOfLastMedicalSelectionControl(
//								row_arr[names.indexOf("date_of_last_medical_selection")],
//								row_arr[names.indexOf("date_of_commencement")]);
//						if (control != null && control) {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn("After", 1, new ArrayList<>(Arrays.asList(row))));
//							consistentDateOfLastMedicalSelectionAccumulator.add(new ControlResult(
//									"Consistent date of last medical selection", affectedColumns));
//						} else if( control != null && !control){
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn("Before", 1, new ArrayList<>(Arrays.asList(row))));
//							consistentDateOfLastMedicalSelectionAccumulator.add(new ControlResult(
//									"Consistent date of last medical selection", affectedColumns));
//						}
//					}
//					basicControls.valuesPersist(row_arr,names,valuesPersistAccumulator);
//
//					if(names.indexOf("benefit_max_age") > -1 && product != null && product.getMax_benefit_expiry_age() != null){
//						if(basicControls.coherenceOfBenefitMaxAge(row_arr[names.indexOf("benefit_max_age")],product.getMax_benefit_expiry_age())){
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn(
//									"Coherence of Benefit Max Age with policy file", 1,
//									new ArrayList<>(Arrays.asList(row))));
//							coherenceOfBenefitMaxAge.add(new ControlResult("Coherence of Benefit Max Age with policy file", affectedColumns));
//							errorsCount.add(1);
//						}
//					}
//
//					if(names.indexOf(Headers.STATUS_END_CURRENT_CONDITION) > -1) {
//
//						basicControls.CheckincidenceDeathXORIncidence_Death(row_arr,row, names,incDeathCheck,claiDeadCheck); // filling the HashMap
//					}
//				}
//			}
//
//
//
//            private boolean eventExistanceCheck(String type, List<String> names, String[] row_arr, String begin_stat,
//					String end_stat) {
//				if(type.equalsIgnoreCase("split") && (!names.contains("exposure_or_event") || !"exposure + event".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")]))) {
//					return true;
//				}
//				if(begin_stat.equals(end_stat)) {
//					return true;
//				}
//				if((StringUtils.isBlank(end_stat) || end_stat.equalsIgnoreCase("censored") || end_stat.equalsIgnoreCase("expired"))
//						&& (StringUtils.isBlank(row_arr[names.indexOf("date_of_event_incurred")]) && StringUtils.isBlank(row_arr[names.indexOf("type_of_event")])) ) {
//					return true;
//				}
//				if(StringUtils.isNotBlank(row_arr[names.indexOf("date_of_event_incurred")]) && StringUtils.isNotBlank(row_arr[names.indexOf("type_of_event")])) {
//					return true;
//				}
//				return false;
//
////				boolean checkEventExistance = (type.equalsIgnoreCase("combine")
////													|| (type.equalsIgnoreCase("split") && names.contains("exposure_or_event")
////															&& "exposure + event".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")]))
////												)
////												&&
////												( !begin_stat.isEmpty() && !end_stat.isEmpty() && !begin_stat.equals(end_stat))
////												&&
////												("".equals(row_arr[names.indexOf("date_of_event_incurred")])
////														|| "".equals(row_arr[names.indexOf("type_of_event")]))
////												&&
////												!Arrays.asList("censored", "expired").contains(end_stat);
////				return !checkEventExistance;
//			}
//		});
//
//        ControlResult Inc_Death_Result = basicControls.incidenceDeathXORIncidence_DeathResult(incDeathCheck,claiDeadCheck,errorsCount.value()); // getting the result
//
//
//        ControlResults groupedControls = null;
//
//		if (names.contains("retro_legal_entity") || names.contains("life_id") || names.contains("policy_id") || names.contains("benefit_id")) {
//			// before all we need to sort our rdd by date of start of current condition :
//			JavaRDD<String> data_sorted = data.sortBy(new SortByDateBCC(names),true, data.getNumPartitions());
//
//
//
//
//				groupedControls = funcService.statusConsistencyCheck(data_sorted, names, type);
//
//
//
//
//
//			if (names.containsAll(Arrays.asList("date_of_end_current_condition"))) {
//
//				data.filter(s -> !s.toLowerCase().trim().replace(";", "").equalsIgnoreCase(""))
//						.mapToPair(new Grouping(names)).reduceByKey(new ReduceByDateBCC(names)).foreach(new ControlReduceBy(exposure_coherent_status,type,op_end,names,errorsCount));
//
//			}
//
//		}
//		ControlResults claimsExistanceControl  = null;
//		if (names.containsAll(Arrays.asList("life_id"))) {
//			claimsExistanceControl = funcService.control21(data, names,type);
//		}
//
//		ControlResult dateOfLastMedicalSelectionControl = collectdateOfLastMedicalSelectionControlResult(consistentDateOfLastMedicalSelectionAccumulator);
//
//		List<ControlResult> controlResultsList = new ArrayList<>();
//		controlResultsList.add(dc_acc.value());
//		controlResultsList.add(fc_acc.value());
//		controlResultsList.add(eec_acc.value());
//		controlResultsList.add(active_acc.value());
//		controlResultsList.add(coh_acc.value());
//		controlResultsList.add(lum_sum_acc.value());
//		controlResultsList.add(exposure_coherent_status.value());
//		controlResultsList.add(product_id_accumulator.value());
//		controlResultsList.add(dateOfEventEqDateOfBeginCurrentConditionAccumulator.value());
//		controlResultsList.add(missingValuesCheckAccumulator.value());
//		controlResultsList.add(missingValuesCheck_2Accumulator.value());
//		controlResultsList.add(missingValuesCheckAccumulator2.value());
//		controlResultsList.add(coherenceCheck.value());
//		controlResultsList.add(eventAmountReInsurerLowerThanInsurerAccumulator.value());
//		controlResultsList.add(coherenceOfBenefitMaxAge.value());
//		if(Inc_Death_Result != null)
//		controlResultsList.add(Inc_Death_Result);
//		if(claimsExistanceControl != null ) {
//			controlResultsList.addAll(claimsExistanceControl.getControlResultsList());
//			errorsCount.add(claimsExistanceControl.getNumber_of_errors());
//		}
//		if(groupedControls != null ) {
//			controlResultsList.addAll(groupedControls.getControlResultsList());
//			errorsCount.add(groupedControls.getNumber_of_errors());
//		}
//		if(dateOfLastMedicalSelectionControl != null) {
//			controlResultsList.add(dateOfLastMedicalSelectionControl);
//			errorsCount.add(dateOfLastMedicalSelectionControl.getAffectedColumns().get(0).getErrorsNumber());
//		}
//
//
//
//		return new ControlResults(null, errorsCount.value(), controlResultsList, header
//				+ ";age_at_commencement_definition;product_start_date;product_end_date;min_face_amount;max_face_amount;min_age_at_commencement;max_age_at_commencement;client_risk_carrier_name;study_client;client_group;study_client_group;treaty_number_omega;study_treaty_number;distribution_brand_name;distribution_brand;Client_Country;Study_Country",valuesPersistAccumulator.value());
//	}
//
//	private ControlResult collectdateOfLastMedicalSelectionControlResult(
//			ControlResultAccumulator consistentDateOfLastMedicalSelectionAccumulator) {
//		AffectedColumn affectedColumnAfter = null;
//		AffectedColumn affectedColumnBefore = null;
//		for (AffectedColumn affectedColumn : consistentDateOfLastMedicalSelectionAccumulator.value()
//				.getAffectedColumns()) {
//			if (affectedColumn.getName().equals("After")) {
//				affectedColumn.setName("Consistent date of last medical selection");
//				affectedColumnAfter = affectedColumn;
//			} else if (affectedColumn.getName().equals("Before")) {
//				affectedColumn.setName("Consistent date of last medical selection");
//				affectedColumnBefore = affectedColumn;
//			}
//		}
//		if (affectedColumnAfter == null || affectedColumnBefore == null) {
//			return null;
//		}
//		if (affectedColumnAfter.getErrorsNumber() - affectedColumnBefore.getErrorsNumber() >= 0) {
//			return new ControlResult("Consistent date of last medical selection", Arrays.asList(affectedColumnBefore));
//		} else {
//			return new ControlResult("Consistent date of last medical selection", Arrays.asList(affectedColumnAfter));
//		}
//	}
//
//	private static Map<Integer, Integer> fillHeadersForEmptyCheckWithCondition(List<String> names) {
//		Map<Integer, Integer> indexOfHeaders = new HashMap<>();
//		if (names.contains("acceleration_risk_type")) {
//			if (names.contains("acceleration_risk_amount_insurer")) {
//				indexOfHeaders.put(names.indexOf("acceleration_risk_amount_insurer"),
//						names.indexOf("acceleration_risk_type"));
//			}
//			if (names.contains("acceleration_risk_amount_reinsur")) {
//				indexOfHeaders.put(names.indexOf("acceleration_risk_amount_reinsur"),
//						names.indexOf("acceleration_risk_type"));
//			}
//		}
//		if (names.contains("benefit_change_rate_type")) {
//			if (names.contains("benefit_change_rate_annual")) {
//				indexOfHeaders.put(names.indexOf("benefit_change_rate_annual"), names.indexOf("benefit_change_rate_type"));
//			}
//			if (names.contains("benefit_term_years")) {
//				indexOfHeaders.put(names.indexOf("benefit_term_years"), names.indexOf("benefit_change_rate_type"));
//			}
//			if (names.contains("benefit_change_frequency")) {
//				indexOfHeaders.put(names.indexOf("benefit_change_frequency"),
//						names.indexOf("benefit_change_rate_type"));
//			}
//		}
//		if (names.contains("type_of_event")) {
//			if (names.contains("event_amount_reinsurer")) {
//				indexOfHeaders.put(names.indexOf("event_amount_reinsurer"), names.indexOf("type_of_event"));
//			}
//			if (names.contains("event_amount_insurer")) {
//				indexOfHeaders.put(names.indexOf("event_amount_insurer"), names.indexOf("type_of_event"));
//			}
//		}
//		return indexOfHeaders;
//	}
//
//	private static HashMap<Integer, String> fillHeadersForEmptyCheck(List<String> names) {
//		List<String> headersToCheck = Arrays.asList("risk_amount_insurer",
//				"risk_amount_reinsurer", "annual_premium_insurer",
//				"annual_premium_reinsurer");
//		HashMap<Integer, String> missingValuesHeadersToCheck = new HashMap<>();
//		for (String header : headersToCheck) {
//			if (!names.contains(header)) {
//				continue;
//			}
//			missingValuesHeadersToCheck.put(names.indexOf(header), header);
//		}
//
//		return missingValuesHeadersToCheck;
//	}
//
//	private static HashMap<Integer, String> fillHeadersForEmptyCheckEntireFile(List<String> names) {
//		List<String> headersToCheck = Arrays.asList("date_of_birth", "date_of_commencement","age_at_commencement");
//		HashMap<Integer, String> missingValuesHeadersToCheck = new HashMap<>();
//		for (String header : headersToCheck) {
//			if (!names.contains(header)) {
//				continue;
//			}
//			missingValuesHeadersToCheck.put(names.indexOf(header), header);
//		}
//
//		return missingValuesHeadersToCheck;
//	}
//
//	private static Map<Integer, String> fillHeadersForEmptyCheckWithCondition2(List<String> names) {
//		Map<Integer, String> indexOfHeaders = new HashMap<>();
//
//		/// Vice Versa controls :
//		if (names.contains("type_of_event")) {
//			indexOfHeaders.put(names.indexOf("type_of_event"), "date_of_event_incurred");
//		}
//		if (names.contains("date_of_event_incurred")) {
//			indexOfHeaders.put(names.indexOf("date_of_event_incurred"), "type_of_event");
//		}
//
//		if (names.contains("temp_mult_extra_rating_term_1")) {
//			indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_term_1"), "temp_mult_extra_rating_1");
//		}
//		if (names.contains("temp_mult_extra_rating_1")) {
//			indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_1"), "temp_mult_extra_rating_term_1");
//		}
//
//		if (names.contains("temp_add_extra_rating_term_1")) {
//			indexOfHeaders.put(names.indexOf("temp_add_extra_rating_term_1"), "temp_add_extra_rating_1");
//		}
//		if (names.contains("temp_add_extra_rating_1")) {
//			indexOfHeaders.put(names.indexOf("temp_add_extra_rating_1"), "temp_add_extra_rating_term_1");
//		}
//
//		if (names.contains("temp_mult_extra_rating_term_2")) {
//			indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_term_2"), "temp_mult_extra_rating_2");
//		}
//		if (names.contains("temp_mult_extra_rating_2")) {
//			indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_2"), "temp_mult_extra_rating_term_2");
//		}
//
//		if (names.contains("temp_add_extra_rating_term_2")) {
//			indexOfHeaders.put(names.indexOf("temp_add_extra_rating_term_2"), "temp_add_extra_rating_2");
//		}
//		if (names.contains("temp_add_extra_rating_2")) {
//			indexOfHeaders.put(names.indexOf("temp_add_extra_rating_2"), "temp_add_extra_rating_term_2");
//		}
//
//		/// Simple Controls :
//		if(names.contains("smoker_status_detailed")) {
//			indexOfHeaders.put(names.indexOf("smoker_status_detailed"), "smoker_status");
//		}
//		if (names.contains("benefit_term_years")) {
//			indexOfHeaders.put(names.indexOf("benefit_term_years"), "benefit_term_type");
//		}
//		if (names.contains("benefit_end_date")) {
//			indexOfHeaders.put(names.indexOf("benefit_end_date"), "benefit_term_type");
//		}
//		if (names.contains("multiplicative_rated_status")) {
//			indexOfHeaders.put(names.indexOf("multiplicative_rated_status"), "rated_status");
//		}
//		if (names.contains("additive_rated_status")) {
//			indexOfHeaders.put(names.indexOf("additive_rated_status"), "rated_status");
//		}
//		if (names.contains("waiting_period_1_type")) {
//			indexOfHeaders.put(names.indexOf("waiting_period_1_type"), "waiting_period_1");
//		}
//		if (names.contains("waiting_period_2_type")) {
//			indexOfHeaders.put(names.indexOf("waiting_period_2_type"), "waiting_period_2");
//		}
//		if (names.contains("waiting_period_3_type")) {
//			indexOfHeaders.put(names.indexOf("waiting_period_3_type"), "waiting_period_3");
//		}
////		if (names.contains("child_benefit_type") && names.contains("child_benefit")) {
////			indexOfHeaders.put(names.indexOf("child_benefit"), names.indexOf("child_benefit_type"));
////		}
//		if (names.contains("child_benefit_type")) {
//			indexOfHeaders.put(names.indexOf("child_benefit_type"), "child_benefit");
//		}
////		if (names.contains("acceleration_risk_type") && names.contains("acceleration_benefit")) {
////			indexOfHeaders.put(names.indexOf("acceleration_benefit"), names.indexOf("acceleration_risk_type"));
////		}
//		if (names.contains("acceleration_risk_type")) {
//			indexOfHeaders.put(names.indexOf("acceleration_risk_type"), "acceleration_benefit");
//		}
////		if (names.contains("buyback_option_type") && names.contains("buyback_option")) {
////			indexOfHeaders.put(names.indexOf("buyback_option"), names.indexOf("buyback_option_type"));
////		}
//		if (names.contains("buyback_option_type")) {
//			indexOfHeaders.put(names.indexOf("buyback_option_type"), "buyback_option");
//		}
//		if (names.contains("benefit_change_rate_annual")) {
//			indexOfHeaders.put(names.indexOf("benefit_change_rate_annual"), "benefit_change_frequency");
//		}
//		if (names.contains("mult_rating_upper_band_1")) {
//			indexOfHeaders.put(names.indexOf("mult_rating_upper_band_1"), "rating_type_1");
//		}
//		if (names.contains("add_rating_upper_band_1")) {
//			indexOfHeaders.put(names.indexOf("add_rating_upper_band_1"), "rating_type_1");
//		}
//		if (names.contains("mult_rating_upper_band_2")) {
//			indexOfHeaders.put(names.indexOf("mult_rating_upper_band_2"), "rating_type_2");
//		}
//		if (names.contains("add_rating_upper_band_2")) {
//			indexOfHeaders.put(names.indexOf("add_rating_upper_band_2"), "rating_type_2");
//		}
//		return indexOfHeaders;
//	}
//
//}
