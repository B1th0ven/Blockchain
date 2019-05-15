//package com.scor.dataProcessing.dataChecker.functionalChecker.Operations;
//
//import org.apache.commons.collections4.map.HashedMap;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Service;
//
//import com.scor.dataProcessing.Helpers.Headers;
//import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
//import com.scor.dataProcessing.accumulators.HashMapAccumulator;
//import com.scor.dataProcessing.accumulators.MapAccumulator;
//import com.scor.dataProcessing.dataChecker.models.AffectedColumn;
//import com.scor.dataProcessing.dataChecker.models.ControlResult;
//
//import static com.scor.dataProcessing.Helpers.Headers.*;
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
//import java.util.concurrent.atomic.LongAccumulator;
//import java.util.stream.Collectors;
//
//@Service
//public class BasicControls implements Serializable {
//
//    /**
//     *
//     */
//    private static final long serialVersionUID = 1L;
//
//
//    public void control_33(List<String> names, String[] row_arr, ControlResultAccumulator dateOfEventEqDateOfBeginCurrentConditionAccumulator, org.apache.spark.util.LongAccumulator errorsCount) {
//        // Blockin Control (Date of Event = Date of END Current Condition)
//        if (names.indexOf(DATE_OF_EVENT_INCURRED) != -1
//                && StringUtils.isNotBlank(row_arr[names.indexOf(DATE_OF_EVENT_INCURRED)])
//                && (names.indexOf(DATE_OF_END_CURRENT_CONDITION) == -1
//                || !row_arr[names.indexOf(DATE_OF_EVENT_INCURRED)]
//                .equals(row_arr[names.indexOf(DATE_OF_END_CURRENT_CONDITION)]))) {
//
//            List<AffectedColumn> affectedColumns = new ArrayList<>();
//            affectedColumns.add(new AffectedColumn("Date of Event = Date of End Current Condition", 1,
//                    new ArrayList<>(Arrays.asList(String.join(";", row_arr)))));
//            dateOfEventEqDateOfBeginCurrentConditionAccumulator.add(
//                    new ControlResult("Date of Event = Date of End Current Condition", affectedColumns));
//            errorsCount.add(1);
//        }
//    }
//
//    public void DatesComparaison(List<String> names, String[] row_arr, ArrayList<ArrayList<String>> dates_to_comp, ControlResultAccumulator dc_acc, org.apache.spark.util.LongAccumulator errorsCount) {
//        for (ArrayList<String> dtc : dates_to_comp) {
//
//            if (names.containsAll(dtc)) {
//
//                ArrayList<LocalDate> dates = new ArrayList<>();
//                boolean empty = false;
//                for (String col : dtc) {
//                    if (names.indexOf(col) == -1) {
//                        empty = true;
//                        break;
//                    } else if (!row_arr[names.indexOf(col)].matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//                        empty = true;
//                        break;
//                    }
//                    try {
//						dates.add(LocalDate.parse(row_arr[names.indexOf(col)],
//						        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
//					} catch (Exception e) {
//						continue;
//					}
//                }
//                if (empty)
//                    continue;
//                for (int j = 0; j < dates.size() - 1; j++) {
//                    if (dates.get(j).isAfter(dates.get(j + 1))) {
//                        List<AffectedColumn> affectedColumns = new ArrayList<>();
//                        affectedColumns.add(new AffectedColumn(
//                                String.join(" & ",
//                                        dtc.stream().map(s -> s.replace("_", " "))
//                                                .collect(Collectors.toList())),
//                                1, new ArrayList<>(Arrays.asList(String.join(";", row_arr)))));
//                        dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//                        errorsCount.add(1);
//                        break;
//                    }
//                }
//            }
//
//        }
//
//
//    }
//
//    public void EventsAndMainRiskTypeCoherence(List<String> names, String[] row_arr, ControlResultAccumulator coherenceCheck, org.apache.spark.util.LongAccumulator errorsCount) {
//        //CR  Events should be coherent with the main risk type and the acceleration risk type For each line
//        if (names.contains(ACCELERATION_RISK_TYPE)) {
//            String type_of_event = row_arr[names.indexOf(TYPE_OF_EVENT)].toLowerCase().trim();
//            String main_risk_type = row_arr[names.indexOf(MAIN_RISK_TYPE)].toLowerCase().trim();
//            String acceleration_risk_type = row_arr[names.indexOf(ACCELERATION_RISK_TYPE)].toLowerCase().trim();
//
//            switch (type_of_event) {
//                case "incidence":
//                    if (!"ci,di,ltc,tpd,health".contains(main_risk_type) && (!main_risk_type.equalsIgnoreCase("life") || acceleration_risk_type.isEmpty())) {
//                        List<AffectedColumn> affectedColumns = new ArrayList<>();
//                        affectedColumns.add(new AffectedColumn(
//                                "Events should be coherent with the main risk type and the acceleration risk type", 1,
//                                new ArrayList<>(Arrays.asList(String.join(";", row_arr)))));
//                        coherenceCheck.add(new ControlResult("Events should be coherent with the main risk type and the acceleration risk type", affectedColumns));
//                        errorsCount.add(1);
//                    }
//                    break;
//                case "claim termination":
//                    if (!"di,ltc,health".contains(main_risk_type)) {
//                        List<AffectedColumn> affectedColumns = new ArrayList<>();
//                        affectedColumns.add(new AffectedColumn(
//                                "Events should be coherent with the main risk type and the acceleration risk type", 1,
//                                new ArrayList<>(Arrays.asList(String.join(";", row_arr)))));
//                        coherenceCheck.add(new ControlResult("Events should be coherent with the main risk type and the acceleration risk type", affectedColumns));
//                        errorsCount.add(1);
//                    }
//                    break;
//                default:
//                    break;
//            }
//
//
//        }
//    }
//
//    public void DatesComparaisonToInput(List<String> names, String[] row_arr, ArrayList<ArrayList<String>> dates_to_com_to_input, String op_start, String op_end, ControlResultAccumulator dc_acc, org.apache.spark.util.LongAccumulator errorsCount) {
//        // sprint 3 controles
//        // Start of observation period ≤ Cover Expiry Date
//        // Date of End Current Condition <= End of observation Period
//        // Start of observation period) <= Date of Begin Current Condition
//
//        if (!((op_start == null || op_start.isEmpty()) && (op_end == null || op_end.isEmpty()))) {
//            for (ArrayList<String> dtci : dates_to_com_to_input) {
//                ArrayList<LocalDate> dts = new ArrayList<>();
//				for (String d : dtci) {
//
//					if (d.equalsIgnoreCase("start_of_observation_period") && !(op_start == null || op_start.isEmpty())
//							&& op_start.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//						try {
//							dts.add(LocalDate.parse(op_start, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
//						} catch (Exception e) {
//							
//						}
//					} else if (d.equalsIgnoreCase("end_of_observation_period") && !(op_end == null || op_end.isEmpty())
//							&& op_end.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//						try {
//							dts.add(LocalDate.parse(op_end, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
//						} catch (Exception e) {
//							
//						}
//					} else if (names.indexOf(d) != -1 && !row_arr[names.indexOf(d)].isEmpty()
//							&& row_arr[names.indexOf(d)].matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//						try {
//							dts.add(LocalDate.parse(row_arr[names.indexOf(d)],
//									DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
//						} catch (Exception e) {
//							
//						}
//					}
//				}
//                if (dts.size() == 2) {
//                    if (dts.get(0).isAfter(dts.get(1))) {
//
//                        List<AffectedColumn> affectedColumns = new ArrayList<>();
//                        affectedColumns.add(new AffectedColumn(
//                                dtci.get(0).replace("_", " ") + " <= " + dtci.get(1).replace("_", " "), 1,
//                                new ArrayList<>(Arrays.asList(String.join(";", row_arr)))));
//                        dc_acc.add(new ControlResult("Date Comparison", affectedColumns));
//                        errorsCount.add(1);
//                    }
//                }
//            }
//        }
//    }
//
//    public void FloatsComparaison(List<String> names, String[] row_arr, ArrayList<ArrayList<String>> floats_to_comp, ControlResultAccumulator fc_acc, org.apache.spark.util.LongAccumulator errorsCount){
//        for (ArrayList<String> ftc : floats_to_comp) {
//            if (names.containsAll(ftc)) {
//                try {
//                    ArrayList<Float> floats = new ArrayList<>();
//                    boolean empty = false;
//                    for (String col : ftc) {
//                        if (names.indexOf(col) == -1) {
//                            empty = true;
//                            break;
//                        } else if (row_arr[names.indexOf(col)].equals("")) {
//                            empty = true;
//                            break;
//                        }
//                        floats.add(Float.parseFloat(row_arr[names.indexOf(col)].replaceAll("\\,", "\\.")));
//                    }
//                    if (empty)
//                        continue;
//                    for (int j = 0; j < floats.size() - 1; j++) {
//                        if (floats.get(j) > floats.get(j + 1)) {
//                            List<AffectedColumn> affectedColumns = new ArrayList<>();
//                            affectedColumns.add(new AffectedColumn(
//                                    String.join(" & ",
//                                            ftc.stream().map(s -> s.replace("_", " "))
//                                                    .collect(Collectors.toList())),
//                                    1, new ArrayList<>(Arrays.asList(String.join(";",row_arr)))));
//                            fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//                            errorsCount.add(1);
//                            break;
//                        }
//                    }
//                } catch (Exception e) {
//                    List<AffectedColumn> affectedColumns = new ArrayList<>();
//                    affectedColumns.add(new AffectedColumn(
//                            String.join(" & ",
//                                    ftc.stream().map(s -> s.replace("_", " "))
//                                            .collect(Collectors.toList())),
//                            1, new ArrayList<>(Arrays.asList(String.join(";",row_arr)))));
//                    fc_acc.add(new ControlResult("Amount Comparison", affectedColumns));
//                    errorsCount.add(1);
//                }
//            }
//        }
//    }
//
//	public boolean eventAmountReInsurerLowerThanInsurer(String eventAmountReinsurer, String eventAmountInsurer) {
//		if(StringUtils.isBlank(eventAmountInsurer) || StringUtils.isBlank(eventAmountReinsurer)) {
//			return false;
//		}
//		try {
//			double eventAmountReinsurerDouble = Double.parseDouble(eventAmountReinsurer.replaceAll("\\,", "\\."));
//			Double eventAmountInsurerDouble = Double.parseDouble(eventAmountInsurer.replaceAll("\\,", "\\."));
//			return !(eventAmountReinsurerDouble<=eventAmountInsurerDouble);
//		} catch (NumberFormatException e) {
//			return true;
//		}
//	}
//
//	public Boolean dateOfLastMedicalSelectionControl(String dateOfLastMedicalSelectionString, String dateOfCommencementString) {
//		try {
//			LocalDate dateOfLastMedicalSelection = LocalDate.parse(dateOfLastMedicalSelectionString,
//					DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//			LocalDate dateOfCommencement = LocalDate.parse(dateOfCommencementString,
//					DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
//			if(dateOfLastMedicalSelection.isAfter(dateOfCommencement)) {
//				return true;
//			}
//			if(dateOfLastMedicalSelection.isBefore(dateOfCommencement)) {
//				return false;
//			}
//			return null;
//		} catch (Exception e) {
//			return null;
//		}
//		
//		
//	}
//	
//	public void valuesPersist(String[] row_arr, List<String> names, MapAccumulator valuesPersistAccumulator) {
//		Map<String, List<String>> map = new HashMap<>();
//		int indexOfMainRiskType = names.indexOf(Headers.MAIN_RISK_TYPE);
//		if (indexOfMainRiskType > -1) {
//			String mainRiskType = row_arr[indexOfMainRiskType];
//			map.put(Headers.MAIN_RISK_TYPE, Arrays.asList(mainRiskType));
////			List<AffectedColumn> affectedColumns = new ArrayList<>();
////			affectedColumns.add(new AffectedColumn(Headers.MAIN_RISK_TYPE, 1, new ArrayList<>(Arrays.asList(mainRiskType))));
////			valuesPersistAccumulator.add(new ControlResult("values_persist", affectedColumns));
//		}
//		int indexOfAccelerationRiskType = names.indexOf(Headers.ACCELERATION_RISK_TYPE);
//		if (indexOfAccelerationRiskType > -1) {
//			String accelerationRiskType = row_arr[indexOfAccelerationRiskType];
//			map.put(Headers.ACCELERATION_RISK_TYPE, Arrays.asList(accelerationRiskType));
//
////			List<AffectedColumn> affectedColumns = new ArrayList<>();
////			affectedColumns.add(new AffectedColumn(Headers.ACCELERATION_RISK_TYPE, 1, new ArrayList<>(Arrays.asList(accelerationRiskType))));
////			valuesPersistAccumulator.add(new ControlResult("values_persist", affectedColumns));
//		}
//		valuesPersistAccumulator.add(map);
//	}
//
//
//	public boolean coherenceOfBenefitMaxAge (String benfitMaxAge,String benefitExpiryAge){
//        try {
//            Double bma = Double.parseDouble(benfitMaxAge.trim());
//            Double bea = Double.parseDouble(benefitExpiryAge.trim());
//            if(bma > bea) return true;
//            else return false;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//            return false;
//    }
//
//    public void CheckincidenceDeathXORIncidence_Death(String[] row, String rowstr, List<String> header,ControlResultAccumulator result,ControlResultAccumulator result2) {
//
//
//        String typeOfevent = row[header.indexOf(Headers.TYPE_OF_EVENT)].trim().toLowerCase();
//        String statusEndCurrentCondition = row[header.indexOf(Headers.STATUS_END_CURRENT_CONDITION)].trim().toLowerCase();
//        List<AffectedColumn> affectedColumns = new ArrayList<>();
//        List<AffectedColumn> affectedColumns2 = new ArrayList<>();
//
//        affectedColumns.add(new AffectedColumn(typeOfevent,1,new ArrayList<>(Arrays.asList(rowstr))));
//        result.add(new ControlResult("Incidence_Death_Check", affectedColumns));
//        affectedColumns2.add(new AffectedColumn(statusEndCurrentCondition,1,new ArrayList<>(Arrays.asList(rowstr))));
//        result2.add(new ControlResult("Claimant_Dead_Check", affectedColumns2));
//
//    
//    }
//
//    public ControlResult incidenceDeathXORIncidence_DeathResult(ControlResultAccumulator result, ControlResultAccumulator result2, Long errorCount){
//        ControlResult incValues = result.value();
//       ControlResult claiValues = result2.value();
//        List<AffectedColumn> affectedColumns = new ArrayList<>();
//
//
//
//        if(incValues.getAffectedColumns() != null && incValues.getAffectedColumns().size() > 1) {
//           List<String> incNames = incValues.getAffectedColumns().stream().map(s -> s.getName()).collect(Collectors.toList());
//           if(incNames.contains("incidence_death") && (incNames.contains("death") || incNames.contains("incidence"))) {
//               AffectedColumn incDea = incValues.getAffectedColumns().stream().filter(s -> s.getName().equalsIgnoreCase("incidence_death")).collect(Collectors.toList()).get(0);
//               affectedColumns.add(new AffectedColumn(
//                       Headers.TYPE_OF_EVENT+"/"+Headers.TYPE_OF_EVENT,incDea.getErrorsNumber(),
//                       incDea.getExamples()));
//               errorCount += incDea.getErrorsNumber();
//           }
//       }
//
//        if(claiValues.getAffectedColumns() != null && claiValues.getAffectedColumns().size() > 1) {
//            List<String> incNames = claiValues.getAffectedColumns().stream().map(s -> s.getName()).collect(Collectors.toList());
//            if(incNames.contains("claimant_dead") && (incNames.contains("dead") || incNames.contains("claimant"))) {
//                AffectedColumn claiDead = claiValues.getAffectedColumns().stream().filter(s -> s.getName().equalsIgnoreCase("claimant_dead")).collect(Collectors.toList()).get(0);
//                affectedColumns.add(new AffectedColumn(
//                        Headers.STATUS_END_CURRENT_CONDITION+"/"+Headers.STATUS_END_CURRENT_CONDITION,claiDead.getErrorsNumber(),
//                        claiDead.getExamples()));
//                errorCount += claiDead.getErrorsNumber();
//
//            }
//        }
//
//        if(!affectedColumns.isEmpty()) {
//            return new ControlResult("Incidence_Death XOR Incidence/Death", affectedColumns);
//        }
//
//        return null;
//
//        }
//
//
//}
