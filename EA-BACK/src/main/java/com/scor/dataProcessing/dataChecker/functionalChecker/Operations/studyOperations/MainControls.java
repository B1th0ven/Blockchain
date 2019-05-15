package com.scor.dataProcessing.dataChecker.functionalChecker.Operations.studyOperations;
//package com.scor.dataProcessing.Operations.Functionals;
//package com.scor.ea.spark.Operations.Functionals;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.spark.api.java.function.VoidFunction;
//import org.apache.spark.util.LongAccumulator;
//
//import com.scor.ea.entities.AffectedColumn;
//import com.scor.ea.entities.ControlResult;
//import com.scor.ea.entities.Product;
//import com.scor.ea.spark.FuncControlsService;
//import com.scor.ea.spark.Operations.BasicControls;
//
//public class MainControls implements VoidFunction<String> {
//	
//	private List<String> names;
//	private LongAccumulator errorsCount;
//	private Map<String, Product> products;
//	private String header;
//	private List<String> namesP;
//	private String op_start;
//	private String op_end;
//	
//	private Map<Integer, Integer> missingValuesWithConditionHeaders;
//	private HashMap<Integer, String> missingValuesCheckHeaders;
//	private Map<Integer, Integer> missingValuesWithConditionHeaders2;
//	
//	private BasicControls basicControls;
//	private FuncControlsService funcService;
//	
//	public MainControls() {
//		missingValuesWithConditionHeaders = fillHeadersForEmptyCheckWithCondition(names);
//		missingValuesCheckHeaders = fillHeadersForEmptyCheck(names);
//		missingValuesWithConditionHeaders2 = fillHeadersForEmptyCheckWithCondition2(names);
//	}
//	
//
//	private List<AffectedColumn> missingValuesCheck(HashMap<Integer, String> missingValuesCheckHeaders,
//			Map<Integer, Integer> missingValuesWithConditionHeaders, String[] row_arr, String row) {
//		List<AffectedColumn> affectedColumnss = new ArrayList<>();
//		missingValuesCheckHeaders.entrySet().forEach(header -> {
//			if (StringUtils.isBlank(row_arr[header.getKey()])) {
//				errorsCount.add(1);
//				affectedColumnss.add(
//						new AffectedColumn(header.getValue(), 1, new ArrayList<>(Arrays.asList(row))));
//			}
//		});
//		missingValuesWithConditionHeaders.entrySet().forEach(header -> {
//			if (StringUtils.isNotBlank(row_arr[header.getValue()])
//					&& StringUtils.isBlank(row_arr[header.getKey()])) {
//				errorsCount.add(1);
//				affectedColumnss.add(
//						new AffectedColumn(names.get(header.getKey()), 1, new ArrayList<>(Arrays.asList(row))));
//			}
//		});
////		if (affectedColumnss.isEmpty()) {
//			return affectedColumnss;
////		}
////		List<AffectedColumn> affectedColumns = new ArrayList<>();
////		affectedColumns.add(new AffectedColumn("Missing Values Check", affectedColumnss.size(),
////				new ArrayList<>(Arrays.asList(row))));
////		return affectedColumns;
//	}
//
//	private List<AffectedColumn> missingValuesCheckBlocking(Map<Integer, Integer> missingValuesWithConditionHeaders, String[] row_arr, String row) {
//		List<AffectedColumn> affectedColumnss = new ArrayList<>();
//		missingValuesWithConditionHeaders.entrySet().forEach(header -> {
//			if (StringUtils.isNotBlank(row_arr[header.getValue()])
//					&& StringUtils.isBlank(row_arr[header.getKey()])) {
//				errorsCount.add(1);
//				affectedColumnss.add(
//						new AffectedColumn(names.get(header.getKey()), 1, new ArrayList<>(Arrays.asList(row))));
//			}
//		});
////		if (affectedColumnss.isEmpty()) {
//			return affectedColumnss;
////		}
////		List<AffectedColumn> affectedColumns = new ArrayList<>();
////		affectedColumns.add(new AffectedColumn("Missing Values Check Blocking", affectedColumnss.size(),
////				new ArrayList<>(Arrays.asList(row))));
////		return affectedColumns;
//	}
//
//	@Override
//	public void call(String row) throws Exception {
//		if (!row.equals("") & !row.equalsIgnoreCase(header)) {
//			String[] row_arr = row.toLowerCase().trim().split(";", -1);
//			Product product = products.get(row_arr[names.indexOf("product_id")].trim());
//
//			List<AffectedColumn> affectedColumnsMissingValues = missingValuesCheck(missingValuesCheckHeaders,
//					missingValuesWithConditionHeaders, row_arr, row);
//			if (!affectedColumnsMissingValues.isEmpty()) {
//				missingValuesCheckAccumulator
//						.add(new ControlResult("Missing Values Check", affectedColumnsMissingValues));
//			}
//
////			if (names.containsAll(Arrays.asList("type_of_event", "date_of_event_incurred",
////					"smoker_status_detailed", "smoker_status", "benefit_term_years", "benefit_end_age",
////					"benefit_term_type", "multiplicative_rated_status", "additive_rated_status",
////					"waiting_period_1_type", "waiting_period_1", "waiting_period_2_type", "waiting_period_2",
////					"waiting_period_3_type", "waiting_period_3", "temp_mult_extra_rating_dur_1",
////					"temp_mult_extra_rating_1", "temp_mult_extra_rating_dur_2", "temp_mult_extra_rating_2",
////					"temp_add_extra_rating_dur_1", "temp_add_extra_rating_1", "temp_add_extra_rating_dur_2",
////					"temp_add_extra_rating_2", "child_benefit_type", "child_benefit", "acceleration_risk_type",
////					"acceleration_benefit", "buyback_option_type", "buyback_option", "benefit_change_rate",
////					"benefit_change_frequency"))) {
//				List<AffectedColumn> affectedColumnsMissingValues2 = missingValuesCheckBlocking(
//						missingValuesWithConditionHeaders2, row_arr, row);
//				if (!affectedColumnsMissingValues2.isEmpty()) {
//					missingValuesCheckAccumulator2.add(
//							new ControlResult("Missing Values Check Blocking", affectedColumnsMissingValues2));
//				}
////			}
//
//
//			basicControls.control_33(names,row_arr,dateOfEventEqDateOfBeginCurrentConditionAccumulator,errorsCount);
//
//			// When death / withdrawal (lump sum), Risk Amount = Event Amount sprint 3
//			if (names.containsAll(
//					Arrays.asList("expenses_included", "settlement_decision", "event_amount_insurer",
//							"risk_amount_insurer", "event_amount_reinsurer", "risk_amount_reinsurer"))) {
//				String te = row_arr[names.indexOf("type_of_event")];
//				String ei = row_arr[names.indexOf("expenses_included")];
//				String sd = row_arr[names.indexOf("settlement_decision")];
//				String eai = row_arr[names.indexOf("event_amount_insurer")].replace(",", ".");
//				String rai = row_arr[names.indexOf("risk_amount_insurer")].replace(",", ".");
//				String eari = row_arr[names.indexOf("event_amount_reinsurer")].replace(",", ".");
//				String rari = row_arr[names.indexOf("risk_amount_reinsurer")].replace(",", ".");
//
//				if(!funcService.control24(te, ei, sd, eai, rai, eari, rari)) {
//					List<AffectedColumn> affectedColumns = new ArrayList<>();
//					affectedColumns.add(new AffectedColumn(
//							"when death / withdrawal (lump sum), risk amount = event amount", 1,
//							new ArrayList<>(Arrays.asList(row))));
//					lum_sum_acc.add(new ControlResult("Lump sum", affectedColumns));
//					errorsCount.add(1);
//				}
//
//
//			}
//
//		basicControls.DatesComparaison(names,row_arr,dates_to_comp,dc_acc,errorsCount);
//
//
//
//			String dateOfEventIncurred = (names.indexOf("date_of_event_incurred") >= 0)
//					? row_arr[names.indexOf("date_of_event_incurred")]
//					: "";
//			String dateOfEventNotified = (names.indexOf("date_of_event_notified") >= 0)
//					? row_arr[names.indexOf("date_of_event_notified")]
//					: "";
//			String dateOfEventSettled = (names.indexOf("date_of_event_settled") >= 0)
//					? row_arr[names.indexOf("date_of_event_settled")]
//					: "";
//			String dateOfEventPaid = (names.indexOf("date_of_event_paid") >= 0)
//					? row_arr[names.indexOf("date_of_event_paid")]
//					: "";
//			if (!funcService.control20(dateOfEventIncurred, dateOfEventNotified, dateOfEventSettled,
//					dateOfEventPaid)) {
//				List<AffectedColumn> affectedColumns = new ArrayList<>();
//				affectedColumns.add(new AffectedColumn(
//						"date of event incurred & date of event notified & date of event settled & date of event paid",
//						1, new ArrayList<>(Arrays.asList(row))));
//				dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//				errorsCount.add(1);
//			}
//
//			// sprint 6 Product start date <= Date of Commencement <= Product end date
//
//			if (namesP.containsAll(Arrays.asList("product_start_date", "product_end_date"))) {
//				String prod_id = row_arr[names.indexOf("product_id")].trim();
//				String p_start = null;
//				String p_end = null;
//				if (product != null) {
//					p_start = product.getP_start_date();
//					p_end = product.getP_end_date();
//				}
//
//				if (names.indexOf("date_of_commencement") >= 0
//						&& row_arr[names.indexOf("date_of_commencement")]
//								.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")
//						&& p_start != null && p_start.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && p_end != null
//						&& p_end.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//
//					LocalDate doc = LocalDate.parse(row_arr[names.indexOf("date_of_commencement")],
//							DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//					LocalDate p_start_date = LocalDate.parse(p_start,
//							DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//					LocalDate p_end_date = LocalDate.parse(p_end,
//							DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//
//					if (p_start_date.isAfter(doc) || doc.isAfter(p_end_date)
//							|| p_start_date.isAfter(p_end_date)) {
//
//						List<AffectedColumn> affectedColumns = new ArrayList<>();
//						affectedColumns.add(new AffectedColumn(
//								"product start date <= date of commencement <= product end date", 1,
//								new ArrayList<>(Arrays.asList(row + ";;" + p_start + ";" + p_end + ";;;;;"))));
//						dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//						errorsCount.add(1);
//					}
//
//				}
//
//			}
//
//			if (product == null) {
//				List<AffectedColumn> affectedColumns = new ArrayList<>();
//				affectedColumns.add(new AffectedColumn("Product Id doesn't exists in product file", 1,
//						new ArrayList<>(Arrays.asList(row))));
//				product_id_accumulator.add(
//						new ControlResult("Checking if Product Id exists in product file", affectedColumns));
//				errorsCount.add(1);
//			}
//
//			// sprint6 Risk Amount Insurer <= Max Face Amount
//
//			if (names.contains("risk_amount_insurer") && namesP.contains("max_face_amount")) {
//				Double mfa = null;
//				Double rai = null;
//
//				if (product != null) {
//					if (product.getMax_face_amount() != null && !product.getMax_face_amount().isEmpty()
//							&& product.getMax_face_amount().replaceAll("\\,", "\\.")
//									.matches("^\\-?\\d+\\.?\\d*$"))
//						mfa = Double.parseDouble(product.getMax_face_amount().replaceAll("\\,", "\\."));
//				}
//				if (row_arr[names.indexOf("risk_amount_insurer")] != null
//						&& row_arr[names.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\.")
//								.matches("^\\-?\\d+\\.?\\d*$"))
//					rai = Double.parseDouble(
//							row_arr[names.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\."));
//
//				if (mfa != null && rai != null && rai > mfa) {
//					List<AffectedColumn> affectedColumns = new ArrayList<>();
//					affectedColumns.add(new AffectedColumn("risk amount insurer <= max face amount", 1,
//							new ArrayList<>(Arrays.asList(row + ";;;;;" + product.getMax_face_amount() + ";;;"))));
//					fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//					errorsCount.add(1);
//				}
//			}
//
//			// sprint6 Risk Amount Reinsurer <= Max Face Amount
//
//			if (names.contains("risk_amount_reinsurer") && namesP.contains("max_face_amount")) {
//				Double mfa = null;
//				Double rari = null;
//				if (product != null) {
//					if (product.getMax_face_amount() != null && !product.getMax_face_amount().isEmpty()
//							&& product.getMax_face_amount().replaceAll("\\,", "\\.")
//									.matches("^\\-?\\d+\\.?\\d*$"))
//						mfa = Double.parseDouble(product.getMax_face_amount().replaceAll("\\,", "\\."));
//				}
//
//				if (row_arr[names.indexOf("risk_amount_reinsurer")] != null
//						&& row_arr[names.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\.")
//								.matches("^\\-?\\d+\\.?\\d*$"))
//					rari = Double.parseDouble(
//							row_arr[names.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\."));
//
//				if (mfa != null && rari != null && rari > mfa) {
//					List<AffectedColumn> affectedColumns = new ArrayList<>();
//					affectedColumns.add(new AffectedColumn("risk amount reinsurer <= max face amount", 1,
//							new ArrayList<>(Arrays.asList(row + ";;;;;" + product.getMax_face_amount() + ";;;"))));
//					fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//					errorsCount.add(1);
//				}
//			}
//
//			// sprint 6 Min Face Amount <= Risk Amount Insurer
//
//			if (names.contains("risk_amount_insurer") && namesP.contains("min_face_amount")) {
//				Double min_fa = null;
//				Double rai = null;
//
//				if (product != null) {
//					if (product.getMin_face_amount() != null && !product.getMin_face_amount().isEmpty()
//							&& product.getMin_face_amount().replaceAll("\\,", "\\.")
//									.matches("^\\-?\\d+\\.?\\d*$"))
//						min_fa = Double.parseDouble(product.getMin_face_amount().replaceAll("\\,", "\\."));
//				}
//				if (row_arr[names.indexOf("risk_amount_insurer")] != null
//						&& row_arr[names.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\.")
//								.matches("^\\-?\\d+\\.?\\d*$"))
//					rai = Double.parseDouble(
//							row_arr[names.indexOf("risk_amount_insurer")].replaceAll("\\,", "\\."));
//
//				// System.out.println("****************"+min_fa+"**"+rai);
//
//				if (min_fa != null && rai != null && rai < min_fa) {
//					List<AffectedColumn> affectedColumns = new ArrayList<>();
//					affectedColumns.add(new AffectedColumn("min face amount <= risk amount insurer", 1,
//							new ArrayList<>(Arrays.asList(row + ";;;;" + product.getMin_face_amount() + ";;;;"))));
//					fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//					errorsCount.add(1);
//				}
//			}
//
//			// sprint 6 Min Face Amount <= Risk Amount Reinsurer
//
//			if (names.contains("risk_amount_reinsurer") && namesP.contains("min_face_amount")) {
//				Double min_fa = null;
//				Double rari = null;
//
//				if (product != null) {
//					if (product.getMin_face_amount() != null && !product.getMin_face_amount().isEmpty()
//							&& product.getMin_face_amount().replaceAll("\\,", "\\.")
//									.matches("^\\-?\\d+\\.?\\d*$"))
//						min_fa = Double.parseDouble(product.getMin_face_amount().replaceAll("\\,", "\\."));
//				}
//				if (row_arr[names.indexOf("risk_amount_reinsurer")] != null
//						&& row_arr[names.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\.")
//								.matches("^\\-?\\d+\\.?\\d*$"))
//					rari = Double.parseDouble(
//							row_arr[names.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\."));
//
//				if (min_fa != null && rari != null && rari < min_fa) {
//					List<AffectedColumn> affectedColumns = new ArrayList<>();
//					affectedColumns.add(new AffectedColumn("min face amount <= risk amount reinsurer", 1,
//							new ArrayList<>(Arrays.asList(row + ";;;;" + product.getMin_face_amount() + ";;;;"))));
//					fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//					errorsCount.add(1);
//				}
//			}
//
//			// sprint 6 Min Age at commencement <= Age at Commencement <= Max Age at
//			// commencement
//
//			if (names.contains("age_at_commencement") && namesP
//					.containsAll(Arrays.asList("min_age_at_commencement", "max_age_at_commencement"))) {
//				Integer min_ac = null;
//				Integer max_ac = null;
//				Integer ac = null;
//				if (product != null) {
//					if (product.getMin_age_at_com() != null && product.getMin_age_at_com().matches("\\d+"))
//						min_ac = Integer.parseInt(product.getMin_age_at_com());
//					if (product.getMax_age_at_com() != null && product.getMax_age_at_com().matches("\\d+"))
//						max_ac = Integer.parseInt(product.getMax_age_at_com());
//				}
//
//				if (row_arr[names.indexOf("age_at_commencement")] != null
//						&& row_arr[names.indexOf("age_at_commencement")].matches("\\d+"))
//					ac = Integer.parseInt(row_arr[names.indexOf("age_at_commencement")]);
//
//				if (min_ac != null && max_ac != null && ac != null && (min_ac > ac || ac > max_ac)) {
//					// System.out.println("**************** "+min_ac+" <= "+ac+" <= "+max_ac);
//					List<AffectedColumn> affectedColumns = new ArrayList<>();
//					affectedColumns.add(new AffectedColumn(
//							"min age at commencement <= age at commencement <= max age at commencement", 1,
//							new ArrayList<>(Arrays.asList(row + ";;;;;;" + min_ac + ";" + max_ac + ";"))));
//					dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//					errorsCount.add(1);
//				}
//
//			}
//
//			//CR  Events should be coherent with the main risk type and the acceleration risk type For each line
//            basicControls.EventsAndMainRiskTypeCoherence(names,row_arr,coherenceCheck,errorsCount);
//
//
//
//			// sprint 3 controles
//			// Start of observation period ≤ Cover Expiry Date
//			// Date of End Current Condition <= End of observation Period
//			// Start of observation period) <= Date of Begin Current Condition
//
//            basicControls.DatesComparaisonToInput(names,row_arr,dates_to_com_to_input,op_start,op_end,dc_acc,errorsCount);
//
//
//
//            basicControls.FloatsComparaison(names,row_arr,floats_to_comp,fc_acc,errorsCount);
//
//			// EVENT EXISTENCE AT ALL STATUS CHANGES :
//			String begin_stat = row_arr[names.indexOf("status_begin_current_condition")].trim();
//			String end_stat = row_arr[names.indexOf("status_end_current_condition")].trim();
//
//			boolean checkEventExistance = eventExistanceCheck(type, names, row_arr, begin_stat, end_stat);
//			if (!checkEventExistance) {
//				// System.out.println("censored & expired != "+end_stat);
//				List<AffectedColumn> affectedColumns = new ArrayList<>();
//				affectedColumns.add(new AffectedColumn("date of event incurred & type of event", 1,
//						new ArrayList<>(Arrays.asList(row))));
//				eec_acc.add(new ControlResult("Event Existence Check", affectedColumns));
//				errorsCount.add(1);
//			}
//
//			// STATUS AT BEGIN SHOULD BE ALWAYS ACTIVE :
//			if ((type.equalsIgnoreCase("combine") || (type.equalsIgnoreCase("split")
//					&& names.contains("exposure_or_event")
//					&& ("exposure + event".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")])
//							|| "exposure".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")]))))
//					&& !"Active".equalsIgnoreCase(begin_stat)) {
//				List<AffectedColumn> affectedColumns = new ArrayList<>();
//				affectedColumns.add(
//						new AffectedColumn("exposure termination", 1, new ArrayList<>(Arrays.asList(row))));
//				active_acc.add(new ControlResult("No exposure following terminating status at end condition",
//						affectedColumns));
//				errorsCount.add(1);
//			}
//
//			// Status coherent with event type :
//
//			if ((type.equalsIgnoreCase("combine")
//					|| (type.equalsIgnoreCase("split") && names.contains("exposure_or_event")
//							&& "exposure + event"
//									.equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")])))
//					&& !funcService.control13(row_arr[names.indexOf("status_begin_current_condition")],
//							row_arr[names.indexOf("status_end_current_condition")],
//							row_arr[names.indexOf("type_of_event")])) {
//
//				List<AffectedColumn> affectedColumns = new ArrayList<>();
//				affectedColumns.add(new AffectedColumn("Type of Event & Status at End Current Condition", 1,
//						new ArrayList<>(Arrays.asList(row))));
//				coh_acc.add(new ControlResult("Status coherent with event type", affectedColumns));
//				errorsCount.add(1);
//			}
//
//			if (names.containsAll(Arrays.asList("date_of_birth"))
//					&& namesP.containsAll(Arrays.asList("age_at_commencement_definition"))) {
//
//				String DOB_s = row_arr[names.indexOf("date_of_birth")].trim();
//				String prod_id = row_arr[names.indexOf("product_id")].trim();
//				String age_def = null;
//				if (product != null) {
//					age_def = product.getAge_def().trim();
//				}
//				if (names.containsAll(Arrays.asList("age_at_commencement", "date_of_commencement"))) {
//					String AAC_s = row_arr[names.indexOf("age_at_commencement")];
//					String DC_s = row_arr[names.indexOf("date_of_commencement")];
//					if (AAC_s.matches("^\\-?\\d+\\,?\\d*$") && DC_s.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")
//							&& DOB_s.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && age_def != null
//							&& !age_def.isEmpty()) {
//						int AAC = Integer.parseInt(AAC_s);
//						LocalDate DC = LocalDate.parse(DC_s,
//								DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//						LocalDate DOB = LocalDate.parse(DOB_s,
//								DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//
//						long daysBetween = ChronoUnit.DAYS.between(DOB, DC);
//						double age = daysBetween / (double) 365.25;
//						int calculatedAge = 0;
//
//						switch (age_def) {
//						case "age last birthday":
//							calculatedAge = (int) age;
//							break;
//						case "age nearest birthday":
//							calculatedAge = (int) Math.round(age);
//							break;
//						case "age next birthday":
//							calculatedAge = (int) age + 1;
//							break;
//						default:
//							break;
//						}
//						if (AAC != calculatedAge) {
//							List<AffectedColumn> affectedColumns = new ArrayList<>();
//							affectedColumns.add(new AffectedColumn(
//									"date of commencement = age at commencement - date of birth", 1,
//									new ArrayList<>(Arrays.asList(row + ";" + age_def))));
//							dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//							errorsCount.add(1);
//						}
//					}
//				}
//
//				// sprint 3 Cover Expiry Date - Date of Birth <= Cover Expiry Age
//
//				if (DOB_s.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && age_def != null && !age_def.isEmpty()
//						&& names.containsAll(Arrays.asList("benefit_end_date"))
//						&& names.containsAll(Arrays.asList("benefit_max_age"))
//						&& row_arr[names.indexOf("benefit_max_age")].matches("^\\-?\\d+\\,?\\d*$")) {
//
//					int coverExpiryAge = 0;
//					int cea = Integer.parseInt(row_arr[names.indexOf("benefit_max_age")]);
//					LocalDate ced = LocalDate.parse(row_arr[names.indexOf("benefit_end_date")],
//							DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//					LocalDate dob = LocalDate.parse(row_arr[names.indexOf("date_of_birth")],
//							DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//
//					long daysBetween = ChronoUnit.DAYS.between(dob, ced);
//					double age = daysBetween / (double) 365.25;
//					switch (age_def) {
//					case "age last birthday":
//						coverExpiryAge = (int) age;
//						break;
//					case "age nearest birthday":
//						coverExpiryAge = (int) Math.round(age);
//						break;
//					case "age next birthday":
//						coverExpiryAge = (int) age + 1;
//						break;
//					default:
//						break;
//					}
//
//					if (cea < coverExpiryAge) {
//						List<AffectedColumn> affectedColumns = new ArrayList<>();
//						affectedColumns
//								.add(new AffectedColumn("cover expiry date - date of birth <= cover expiry age",
//										1, new ArrayList<>(Arrays.asList(row + ";" + age_def))));
//						dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//						errorsCount.add(1);
//					}
//				}
//
//			}
//		}
//	}
//
//	private boolean eventExistanceCheck(String type, List<String> names, String[] row_arr, String begin_stat,
//			String end_stat) {
//		boolean checkEventExistance = (type.equalsIgnoreCase("combine") 
//											|| (type.equalsIgnoreCase("split") && names.contains("exposure_or_event")
//													&& "exposure + event".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")]))
//										) 
//										&& 
//										( !begin_stat.isEmpty() && !end_stat.isEmpty() && !begin_stat.equals(end_stat))
//										&& 
//										("".equals(row_arr[names.indexOf("date_of_event_incurred")])
//												|| "".equals(row_arr[names.indexOf("type_of_event")]))
//										&& 
//										!Arrays.asList("censored", "expired").contains(end_stat);
//		return !checkEventExistance;
//	}
//	
//	
//	private static HashMap<Integer, String> fillHeadersForEmptyCheck(List<String> names) {
//		List<String> headersToCheck = Arrays.asList("date_of_birth", "date_of_commencement", "risk_amount_insurer",
//				"event_amount_insurer", "risk_amount_reinsurer", "event_amount_reinsurer", "annual_premium_insurer",
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
//	private static Map<Integer, Integer> fillHeadersForEmptyCheckWithCondition2(List<String> names) {
//		Map<Integer, Integer> indexOfHeaders = new HashMap<>();
//		if (names.contains("type_of_event") && names.contains("date_of_event_incurred")) {
//			indexOfHeaders.put(names.indexOf("date_of_event_incurred"), names.indexOf("type_of_event"));
//			indexOfHeaders.put(names.indexOf("type_of_event"), names.indexOf("date_of_event_incurred"));
//		}
//		if (names.contains("temp_mult_extra_rating_dur_1") && names.contains("temp_mult_extra_rating_1")) {
//			indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_1"), names.indexOf("temp_mult_extra_rating_dur_1"));
//			indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_dur_1"), names.indexOf("temp_mult_extra_rating_1"));
//		}
//		if (names.contains("temp_add_extra_rating_dur_1") && names.contains("temp_add_extra_rating_1")) {
//			indexOfHeaders.put(names.indexOf("temp_add_extra_rating_1"), names.indexOf("temp_add_extra_rating_dur_1"));
//			indexOfHeaders.put(names.indexOf("temp_add_extra_rating_dur_1"), names.indexOf("temp_add_extra_rating_1"));
//		}
//		if (names.contains("temp_mult_extra_rating_dur_2") && names.contains("temp_mult_extra_rating_2")) {
//			indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_2"), names.indexOf("temp_mult_extra_rating_dur_2"));
//			indexOfHeaders.put(names.indexOf("temp_mult_extra_rating_dur_2"), names.indexOf("temp_mult_extra_rating_2"));
//		}
//		if (names.contains("temp_add_extra_rating_dur_2") && names.contains("temp_add_extra_rating_2")) {
//			indexOfHeaders.put(names.indexOf("temp_add_extra_rating_2"), names.indexOf("temp_add_extra_rating_dur_2"));
//			indexOfHeaders.put(names.indexOf("temp_add_extra_rating_dur_2"), names.indexOf("temp_add_extra_rating_2"));
//		}
//		if (names.contains("smoker_status_detailed") && names.contains("smoker_status")) {
//			indexOfHeaders.put(names.indexOf("smoker_status"), names.indexOf("smoker_status_detailed"));
//		}
//		if (names.contains("benefit_term_years") && names.contains("benefit_term_type")) {
//			indexOfHeaders.put(names.indexOf("benefit_term_type"), names.indexOf("benefit_term_years"));
//		}
//		if (names.contains("benefit_end_age") && names.contains("benefit_term_type")) {
//			indexOfHeaders.put(names.indexOf("benefit_term_type"), names.indexOf("benefit_end_age"));
//		}
//		if (names.contains("multiplicative_rated_status") && names.contains("rated_status")) {
//			indexOfHeaders.put(names.indexOf("rated_status"), names.indexOf("multiplicative_rated_status"));
//		}
//		if (names.contains("additive_rated_status") && names.contains("rated_status")) {
//			indexOfHeaders.put(names.indexOf("rated_status"), names.indexOf("additive_rated_status"));
//		}
//		if (names.contains("waiting_period_1_type") && names.contains("waiting_period_1")) {
//			indexOfHeaders.put(names.indexOf("waiting_period_1"), names.indexOf("waiting_period_1_type"));
//		}
//		if (names.contains("waiting_period_2_type") && names.contains("waiting_period_2")) {
//			indexOfHeaders.put(names.indexOf("waiting_period_2"), names.indexOf("waiting_period_2_type"));
//		}
//		if (names.contains("waiting_period_3_type") && names.contains("waiting_period_3")) {
//			indexOfHeaders.put(names.indexOf("waiting_period_3"), names.indexOf("waiting_period_3_type"));
//		}
//		if (names.contains("child_benefit_type") && names.contains("child_benefit")) {
//			indexOfHeaders.put(names.indexOf("child_benefit"), names.indexOf("child_benefit_type"));
//		}
//		if (names.contains("acceleration_risk_type") && names.contains("acceleration_benefit")) {
//			indexOfHeaders.put(names.indexOf("acceleration_benefit"), names.indexOf("acceleration_risk_type"));
//		}
//		if (names.contains("buyback_option_type") && names.contains("buyback_option")) {
//			indexOfHeaders.put(names.indexOf("buyback_option"), names.indexOf("buyback_option_type"));
//		}
//		if (names.contains("benefit_change_rate") && names.contains("benefit_change_frequency")) {
//			indexOfHeaders.put(names.indexOf("benefit_change_frequency"), names.indexOf("benefit_change_rate"));
//		}
//		return indexOfHeaders;
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
//			if (names.contains("benefit_change_rate")) {
//				indexOfHeaders.put(names.indexOf("benefit_change_rate"), names.indexOf("benefit_change_rate_type"));
//			}
//			if (names.contains("benefit_term")) {
//				indexOfHeaders.put(names.indexOf("benefit_term"), names.indexOf("benefit_change_rate_type"));
//			}
//			if (names.contains("benefit_change_frequency")) {
//				indexOfHeaders.put(names.indexOf("benefit_change_frequency"),
//						names.indexOf("benefit_change_rate_type"));
//			}
//		}
//		return indexOfHeaders;
//	}
//
//
//}
