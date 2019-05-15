package com.scor.dataProcessing.sparkConnection;
//package com.scor.dataProcessing.spark;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//
//import com.scor.dataProcessing.common.DataPivot;
//import com.scor.dataProcessing.models.NotExecutedDto;
//import com.scor.dataProcessing.spark.Helpers.Headers;
//
//public class CompColsCheck implements Serializable{
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -5694642429985547065L;
//	static JavaSparkContext sc = Connection.getContext();
//	static List<String> compCols = DataPivot.getCompCols();
//	static List<String> pivCols = DataPivot.getPivotCols().stream().map(s -> s.getName()).collect(Collectors.toList());
//	static List<String> compColsProduct = DataPivot.getCompColsProduct();
//	static List<String> pivColsProduct = DataPivot.getPivotColsProduct().stream().map(s -> s.getName())
//			.collect(Collectors.toList());
//
//	public List<List<String>> run(String path, String type) {
//
//		JavaRDD<String> data = sc.textFile(path);
//		String header = data.first();
//
//		List<String> HeadersCols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";",-1)));
//		List<String> cols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";",-1)));
//
////		if (type.equalsIgnoreCase("combine") && !cols.contains("exposure_or_event"))
////			cols.add("exposure_or_event");
//
//		List<String> columns = pivCols.stream().filter(s -> HeadersCols.contains(s)).collect(Collectors.toList());
//		List<String> missingColumns = compCols.stream().filter(s -> !cols.contains(s)).collect(Collectors.toList());
//		List<String> ignoredColumns = cols.stream().filter(s -> !pivCols.contains(s)).collect(Collectors.toList());
//		List<String> duplicatedColumns = cols.stream().filter(i -> Collections.frequency(cols, i) > 1).distinct()
//				.collect(Collectors.toList());
//		if(type.equalsIgnoreCase("combine")) {
//			if(cols.contains("exposure_or_event")) {
//				columns.remove("exposure_or_event");
//				ignoredColumns.add("exposure_or_event");
//			} else {
//				missingColumns.remove("exposure_or_event");
//			}
//		}
//		return Arrays.asList(new ArrayList<String>(columns), missingColumns, ignoredColumns, duplicatedColumns);
//	}
//
//	public List<List<String>> runProduct(String path) {
//
//		// Long start = System.currentTimeMillis() ;
//
//		JavaRDD<String> data = sc.textFile(path);
//		String header = data.first();
//
//		List<String> cols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";")));
//
//		return Arrays.asList(
//				new ArrayList<String>(
//						pivColsProduct.stream().filter(s -> cols.contains(s)).collect(Collectors.toList())),
//				compColsProduct.stream().filter(s -> !cols.contains(s)).collect(Collectors.toList()),
//				cols.stream().filter(s -> !pivColsProduct.contains(s)).collect(Collectors.toList()),
//				cols.stream().filter(i -> Collections.frequency(cols, i) > 1).distinct().collect(Collectors.toList()));
//	}
//
//	public List<NotExecutedDto> notExecutedCtrl(String path, String path_product, String type, String op_start,
//			String op_end) throws IOException {
//
//		JavaRDD<String> data = sc.textFile(path);
//		JavaRDD<String> data_product = sc.textFile(path_product);
//
//		String header = data.first();
//		String header_product = data_product.first();
//
//		List<String> cols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";")));
//		List<String> cols_product = new ArrayList<String>(
//				Arrays.asList(header_product.toLowerCase().trim().split(";")));
//		List<NotExecutedDto> notExecutedList = new ArrayList<>();
////		List<String> notEcecuted = new ArrayList<>();
//
//		if (!cols.containsAll(Arrays.asList("life_id", "policy_id"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "life_id", "policy_id");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("status at begin current condition & status at end current condition");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("status at begin current condition & status at end current condition");
//		}
//		if (!cols.containsAll(Arrays.asList("date_of_birth", "date_of_commencement"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_birth", "date_of_commencement");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of birth & date of commencement");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("date of birth & date of commencement");
//		}
//		if (!cols.containsAll(Arrays.asList("risk_amount_reinsurer", "risk_amount_insurer"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "risk_amount_reinsurer", "risk_amount_insurer");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("risk amount reinsurer & risk amount insurer");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("risk amount reinsurer & risk amount insurer");
//		}
//		if (!cols.containsAll(Arrays.asList("date_of_begin_current_condition", "date_of_end_current_condition"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_begin_current_condition",
//					"date_of_end_current_condition");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of begin current condition & date of end current condition");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("date of begin current condition & date of end current condition");
//		}
//		if (!cols.containsAll(Arrays.asList("life_id", "policy_id", "date_of_end_current_condition"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "life_id", "policy_id",
//					"date_of_end_current_condition");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("Overlap Check");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("Overlap Check");
//		}
//		if (!cols.containsAll(Arrays.asList("date_of_commencement", "date_of_event_incurred"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_commencement", "date_of_event_incurred");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of commencement <= date of event incurred");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("date of commencement <= date of event incurred");
//		}
//		if (!cols.containsAll(Arrays.asList("date_of_commencement", "benefit_end_date"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_commencement", "benefit_end_date");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of commencement & benefit end date");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("date of commencement & cover expiry date");
//		}
//		if (!cols.containsAll(Arrays.asList("date_of_commencement", "date_of_begin_current_condition"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_commencement",
//					"date_of_begin_current_condition");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of commencement <= date of begin current condition");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("date of commencement <= date of begin current condition");
//		}
//		if (!cols.containsAll(Arrays.asList("type_of_event", "status_end_current_condition"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "type_of_event", "status_end_current_condition");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("type of event & status at end current condition");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("type of event & status at end current condition");
//		}
//
//		if (!cols.containsAll(Arrays.asList("date_of_event_incurred", "benefit_end_date"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_event_incurred", "benefit_end_date");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of event incurred & benefit end date");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("date of event incurred & cover expiry date");
//		} // sprint 3
//		if (!cols.containsAll(Arrays.asList("date_of_end_current_condition", "benefit_end_date"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_end_current_condition",
//					"benefit_end_date");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of end current condition & benefit end date");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("date of end current condition & cover expiry date");
//		} // sprint 3
//		if (!cols.containsAll(Arrays.asList("benefit_end_date")) || (op_start == null || op_start.isEmpty())) {
//			List<String> missingcols = checkNotExecutedControl(cols, "benefit_end_date");
//			if (op_start == null || op_start.isEmpty()) {
//				missingcols.add("op_start");
//			}
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("start of observation period <= benefit end date");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("start of observation period <= cover expiry date");
//		} // sprint 3
//		if (!cols.containsAll(Arrays.asList("date_of_end_current_condition"))
//				|| (op_end == null || op_end.isEmpty())) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_end_current_condition",
//					"benefit_end_date");
//			if (op_end == null || op_end.isEmpty()) {
//				missingcols.add("op_end");
//			}
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of end current condition <= end of observation period");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("date of end current condition <= end of observation period");
//		} // sprint 3
//		if (!cols.containsAll(Arrays.asList("date_of_event_incurred"))
//				|| (op_start == null || op_start.isEmpty())) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_event_incurred");
//			if (op_start == null || op_start.isEmpty()) {
//				missingcols.add("op_start");
//			}
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("start of observation period <= date of event incurred");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("start of observation period <= date of begin current condition");
//		} // sprint3
//		if (!cols.containsAll(Arrays.asList("life_id", "policy_id", "date_of_end_current_condition"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "life_id", "policy_id",
//					"date_of_end_current_condition");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("claims existence");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("claims existence");
//		} // sprint 3
//		if (!cols.contains("date_of_event_notified") && !cols.contains("date_of_event_settled") && !cols.contains("date_of_event_paid")) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_event_notified", "date_of_event_settled",
//					"date_of_event_paid");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl(
//					"date of event incurred & date of event notified & date of event settled & date of event paid");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add(
////					"date of event incurred & date of event notified & date of event settled & date of event paid");
//		} // sprint 3
//		if (!cols.containsAll(Arrays.asList("event_amount_insurer", "risk_amount_insurer")) && !cols.containsAll(Arrays.asList( "event_amount_reinsurer",
//				"risk_amount_reinsurer"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "event_amount_insurer", "risk_amount_insurer",
//					"event_amount_reinsurer", "risk_amount_reinsurer");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("when death / withdrawal (lump sum), risk amount = event amount");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("when death / withdrawal (lump sum), risk amount = event amount");
//		} // sprint 3
//		if ( !cols.contains("date_of_end_current_condition") || (!cols.contains("retro_legal_entity") && !cols.contains("life_id") && !cols.contains("policy_id") && !cols.contains("benefit_id"))
//				|| (op_end == null || op_end.isEmpty())) {
//			List<String> missingcols = checkNotExecutedControl(cols, "life_id", "policy_id", "benefit_id",
//					"date_of_end_current_condition");
//			if (op_end == null || op_end.isEmpty()) {
//				missingcols.add("op_end");
//			}
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("exposure end coherent with status");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("exposure end coherent with status");
//		} // sprint 3
//		if (!cols.contains("risk_amount_insurer") && !cols.contains("event_amount_insurer")
//				&& !cols.contains("risk_amount_reinsurer") && !cols.contains("event_amount_reinsurer")
//				&& !cols.contains("annual_premium_insurer") && !cols.contains("annual_premium_reinsurer")
//				&& (!cols.contains("acceleration_risk_type") || (!cols.contains("acceleration_risk_amount_insurer")
//						&& !cols.contains("acceleration_risk_amount_reinsur")))
//				&& (!cols.contains("benefit_change_rate_type") || (!cols.contains("benefit_change_rate")
//						&& !cols.contains("benefit_term") && !cols.contains("benefit_change_frequency")))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_birth", "date_of_commencement",
//					"risk_amount_insurer", "event_amount_insurer", "risk_amount_reinsurer", "event_amount_reinsurer",
//					"annual_premium_insurer", "annual_premium_reinsurer", "acceleration_risk_type",
//					"acceleration_risk_amount_insurer", "acceleration_risk_amount_reinsur",
//					"benefit_change_rate_type", "benefit_change_rate", "benefit_term", "benefit_change_frequency");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("Missing Values Check");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("Missing Values Check");
//		}
//
//		if (!cols.containsAll(Arrays.asList("age_at_commencement", "date_of_birth", "date_of_commencement"))
//				|| !cols_product.containsAll(Arrays.asList("age_at_commencement_definition"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "age_at_commencement", "date_of_birth",
//					"date_of_commencement");
//			missingcols.addAll(checkNotExecutedControl(cols_product, "age_at_commencement_definition"));
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of commencement = age at commencement - date of birth");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("date of commencement = age at commencement - date of birth");
//		}
//
//		if (!cols.containsAll(Arrays.asList("benefit_max_age", "benefit_end_date", "date_of_birth"))
//				|| !cols_product.containsAll(Arrays.asList("age_at_commencement_definition"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "benefit_max_age", "benefit_end_date",
//					"date_of_birth");
//			missingcols.addAll(checkNotExecutedControl(cols_product, "age_at_commencement_definition"));
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("cover expiry date - date of birth <= cover expiry age");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("cover expiry date - date of birth <= cover expiry age");
//		} // sprint 3
//
//		// sprint 6
//		if (!cols_product.containsAll(Arrays.asList("product_start_date", "product_end_date"))
//				|| !cols.contains("date_of_commencement")) {
//			List<String> missingcols = checkNotExecutedControl(cols, "product_start_date", "product_end_date");
//			missingcols.addAll(checkNotExecutedControl(cols_product, "date_of_commencement"));
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("product start date <= date of commencement <= product end date");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////notEcecuted.add("product start date <= date of commencement <= product end date");
//		}
//		if (!cols.containsAll(Arrays.asList("risk_amount_insurer"))
//				|| !cols_product.containsAll(Arrays.asList("max_face_amount"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "risk_amount_insurer");
//			missingcols.addAll(checkNotExecutedControl(cols_product, "max_face_amount"));
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("risk amount insurer <= max face amount");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("risk amount insurer <= max face amount");
//		}
//		if (!cols.containsAll(Arrays.asList("risk_amount_reinsurer"))
//				|| !cols_product.containsAll(Arrays.asList("max_face_amount"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "risk_amount_reinsurer");
//			missingcols.addAll(checkNotExecutedControl(cols_product, "max_face_amount"));
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("risk amount reinsurer <= max face amount");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("risk amount reinsurer <= max face amount");
//		}
//		if (!cols.containsAll(Arrays.asList("risk_amount_insurer"))
//				|| !cols_product.containsAll(Arrays.asList("min_face_amount"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "risk_amount_insurer");
//			missingcols.addAll(checkNotExecutedControl(cols_product, "min_face_amount"));
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("min face amount <= risk amount insurer");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("min face amount <= risk amount insurer");
//		}
//		if (!cols.containsAll(Arrays.asList("risk_amount_reinsurer"))
//				|| !cols_product.containsAll(Arrays.asList("min_face_amount"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "risk_amount_reinsurer");
//			missingcols.addAll(checkNotExecutedControl(cols_product, "min_face_amount"));
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("min face amount <= risk amount reinsurer");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("min face amount <= risk amount reinsurer");
//		}
//		if (!cols.containsAll(Arrays.asList("age_at_commencement"))
//				&& !cols_product.containsAll(Arrays.asList("min_age_at_commencement", "max_age_at_commencement"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "age_at_commencement");
//			missingcols.addAll(
//					checkNotExecutedControl(cols_product, "min_age_at_commencement", "max_age_at_commencement"));
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("min age at commencement <= age at commencement <= max age at commencement");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("min age at commencement <= age at commencement <= max age at commencement");
//		}
//
////		    Sprint 8 CR Events should be coherent with the main risk type and the acceleration risk type For each line
//		if (!cols.containsAll(Arrays.asList("acceleration_risk_type"))) {
//			List<String> missingcols = checkNotExecutedControl(cols, "acceleration_risk_type");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("Events should be coherent with the main risk type and the acceleration risk type");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("min age at commencement <= age at commencement <= max age at commencement");
//		}
//		
//		if(!cols.contains("date_of_birth") && !cols.contains("date_of_commencement")) {
////			Missing Values Check_2
//			List<String> missingcols = checkNotExecutedControl(cols, "date_of_birth", "date_of_commencement");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("Missing Values Check_2");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//		}
//		
//		if (!cols.containsAll(Arrays.asList("event_amount_reinsurer","event_amount_insurer"))) {
//			List<String> missingcols = checkNotExecutedControl(cols,"event_amount_insurer", "event_amount_reinsurer");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("Event Amount Reinsurer <= Event Amount Insurer");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("min age at commencement <= age at commencement <= max age at commencement");
//		}
//		
//		if (!cols.containsAll(Arrays.asList("date_of_last_medical_selection","date_of_commencement"))) {
//			List<String> missingcols = checkNotExecutedControl(cols,"date_of_last_medical_selection", "date_of_commencement");
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("Consistent date of last medical selection");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
////			notEcecuted.add("min age at commencement <= age at commencement <= max age at commencement");
//		}
//
//		if (!cols.containsAll(Arrays.asList(Headers.DATE_OF_BEGIN_CURRENT_CONDITION,Headers.DATE_OF_END_CURRENT_CONDITION))) {
//			List<String> missingcols = checkNotExecutedControl(cols,Headers.DATE_OF_BEGIN_CURRENT_CONDITION, Headers.DATE_OF_END_CURRENT_CONDITION);
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("status date space");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//
//		}
//
//		if(!cols.containsAll(Arrays.asList(Headers.BENEFIT_MAX_AGE)) || !cols_product.containsAll(Arrays.asList(Headers.MAX_BENEFIT_EXPIRY_AGE))){
//			List<String> missingcols = checkNotExecutedControl(cols, Headers.BENEFIT_MAX_AGE);
//			missingcols.addAll(checkNotExecutedControl(cols_product, Headers.MAX_BENEFIT_EXPIRY_AGE));
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("Coherence of Benefit Max Age with policy file");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//		}
//
//		if(!cols.containsAll(Arrays.asList(Headers.STATUS_END_CURRENT_CONDITION))){
//			List<String> missingcols = checkNotExecutedControl(cols, Headers.STATUS_END_CURRENT_CONDITION);
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("Incidence_Death XOR Incidence/Death");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//		}
//
//		if(!cols.containsAll(Arrays.asList(Headers.STATUS_END_CURRENT_CONDITION))){
//			List<String> missingcols = checkNotExecutedControl(cols, Headers.STATUS_END_CURRENT_CONDITION);
//			NotExecutedDto notExecuted = new NotExecutedDto();
//			notExecuted.setControl("date of event incurred & type of event");
//			notExecuted.setField(missingcols);
//			notExecutedList.add(notExecuted);
//		}
//
//
//
//
//		return notExecutedList;
//	}
//
//	private List<String> checkNotExecutedControl(List<String> cols, String... param) {
//		List<String> missingColumn = new ArrayList<>();
//		for (String string : param) {
//			if (cols.contains(string)) {
//				continue;
//			}
//			missingColumn.add(string);
//		}
//		return missingColumn;
//	}
//}
