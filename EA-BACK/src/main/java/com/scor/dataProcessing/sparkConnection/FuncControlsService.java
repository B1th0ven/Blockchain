package com.scor.dataProcessing.sparkConnection;
//package com.scor.dataProcessing.spark;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.util.LongAccumulator;
//import org.springframework.stereotype.Service;
//
//import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
//import com.scor.dataProcessing.models.AffectedColumn;
//import com.scor.dataProcessing.models.ControlResult;
//import com.scor.dataProcessing.models.ControlResults;
//import com.scor.dataProcessing.spark.Helpers.Grouping;
//import com.scor.dataProcessing.spark.Helpers.GroupingByLifeId;
//import com.scor.dataProcessing.spark.Helpers.filters.FilterForClaimsExistance;
//import com.scor.dataProcessing.spark.Operations.Functionals.ControlByGroup;
//import com.scor.dataProcessing.spark.Operations.Functionals.ControlByGroupLifeId;
//
//import scala.Serializable;
//
//
//@Service
//public class FuncControlsService implements Serializable {
//    /**
//     *
//     */
//    private static final long serialVersionUID = 1L;
//
//
//    public static ControlResults statusConsistencyCheck(JavaRDD<String> data, List<String> names, String type) {
//        JavaSparkContext sc = Connection.getContext();
//        List<ControlResult> res = new ArrayList<>();
//        ControlResultAccumulator cc_acc = new ControlResultAccumulator(
//                new ControlResult("Consistenct Check", new ArrayList<>()));
//        sc.sc().register(cc_acc);
//        ControlResultAccumulator overlap_acc = new ControlResultAccumulator(
//                new ControlResult("Overlap Check", new ArrayList<>()));
//        sc.sc().register(overlap_acc);
//        ControlResultAccumulator StatusDateSpaceAccumulator = new ControlResultAccumulator(
//                new ControlResult("status date space", new ArrayList<>()));
//        sc.sc().register(StatusDateSpaceAccumulator);
//        LongAccumulator errorsCount = sc.sc().longAccumulator();
//        data.filter(s -> !s.toLowerCase().trim().replace(";", "").equalsIgnoreCase("")
//                && !s.toLowerCase().trim().equalsIgnoreCase(String.join(";", names)))
//                .mapToPair(new Grouping(names)).groupByKey()
//                .foreach(new ControlByGroup(cc_acc, overlap_acc,StatusDateSpaceAccumulator, type, names, errorsCount));
//
//        res.add(cc_acc.value());
//        res.add(overlap_acc.value());
//        res.add(StatusDateSpaceAccumulator.value());
//        return new ControlResults(null, errorsCount.value(), res, null,new HashMap<>());
//
//    }
//
//
//    public boolean control13(String statusBegin, String statusEnd, String typeOfEvent) {
//        Map<String, Map<String, String>> typeOfEventMap = FuncControlsService.typeOfEventMapper();
//        if (StringUtils.isBlank(statusBegin))
//            statusBegin = "empty";
//        if (StringUtils.isBlank(statusEnd))
//            statusEnd = "empty";
//        if (StringUtils.isBlank(typeOfEvent))
//            typeOfEvent = "empty";
//
//        Map<String, String> typeOfEventStatusEndMap = typeOfEventMap.get(statusBegin);
//        if (typeOfEventStatusEndMap == null) {
//            return false;
//        }
//        String typeOfEventExpected = typeOfEventStatusEndMap.get(statusEnd);
//        if (typeOfEventExpected == null) {
//            return false;
//        }
//        for (String values : typeOfEventExpected.split(";")) {
//            if (values.equals(typeOfEvent.toLowerCase())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static Map<String, Map<String, String>> typeOfEventMapper() {
//        Map<String, Map<String, String>> typeOfEventMap = new HashMap<>();
//        Map<String, String> activeMap = new HashMap<>();
//        activeMap.put("active", "empty");
//        activeMap.put("claimant", "incidence");
//        activeMap.put("dead", "death");
//        activeMap.put("withdrawn", "withdrawal");
//        activeMap.put("expired", "empty");
//        activeMap.put("censored", "empty");
//        activeMap.put("empty", "empty");
//        typeOfEventMap.put("active", activeMap);
//        Map<String, String> claimantMap = new HashMap<>();
//        claimantMap.put("active", "claim termination");
//        claimantMap.put("claimant", "empty");
//        claimantMap.put("dead", "claim termination");
//        claimantMap.put("withdrawn", "claim termination");
//        claimantMap.put("expired", "empty");
//        claimantMap.put("censored", "empty;claim termination");
//        claimantMap.put("empty", "empty");
//        typeOfEventMap.put("claimant", claimantMap);
//        return typeOfEventMap;
//    }
//
//    public boolean control24(String typeOfEvent, String expensesIncluded, String settlementDecision,
//                             String eventAmount, String riskAmount) {
//        if (StringUtils.isBlank(expensesIncluded)) {
//            expensesIncluded = "no";
//        }
//        if (StringUtils.isBlank(settlementDecision)) {
//            settlementDecision = "no";
//        }
//        if ((typeOfEvent.equalsIgnoreCase("death") || typeOfEvent.equalsIgnoreCase("withdrawal") || typeOfEvent.equalsIgnoreCase("incidence_death"))) {
//            if (expensesIncluded.equalsIgnoreCase("no")) {
//                if (settlementDecision.equalsIgnoreCase("yes")) {
//                    boolean compareEaToRa = !eventAmount.isEmpty() && !riskAmount.isEmpty()
//                            && Float.parseFloat(eventAmount) <= Float.parseFloat(riskAmount);
//                    if (eventAmount.isEmpty() || riskAmount.isEmpty() || compareEaToRa) {
//                        return true;
//                    }
//                } else if (settlementDecision.equalsIgnoreCase("no")) {
//                    boolean compareEaToRa = !eventAmount.isEmpty() && !riskAmount.isEmpty()
//                            && Float.parseFloat(eventAmount) == Float.parseFloat(riskAmount);
//                    if (eventAmount.isEmpty() || riskAmount.isEmpty() || compareEaToRa) {
//                        return true;
//                    }
//                } else return true;
//            } else if (settlementDecision.equalsIgnoreCase("no")) {
//                boolean compareEaToRa = !eventAmount.isEmpty() && !riskAmount.isEmpty()
//                        && Float.parseFloat(eventAmount) >= Float.parseFloat(riskAmount);
//                if (eventAmount.isEmpty() || riskAmount.isEmpty() || compareEaToRa) {
//                    return true;
//                }
//            } else return true;
//
//            return false;
//        }
//        return true;
//    }
//
//    //	-	Date of Event Incurred <= Date of Event Notified <= Date of Event Settled <= Date of Event Paid
//    public boolean control20(String dateOfEventIncurred, String dateOfEventNotified, String dateOfEventSettled, String dateOfEventPaid) {
//        List<LocalDate> dateToComapre = new ArrayList<>();
//        if (StringUtils.isNotBlank(dateOfEventIncurred) && dateOfEventIncurred.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//            try {
//				dateToComapre.add(
//				        LocalDate.parse(dateOfEventIncurred, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
//			} catch (Exception e) {
//
//			}
//        }
//        if (StringUtils.isNotBlank(dateOfEventNotified) && dateOfEventNotified.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//            try {
//				dateToComapre.add(
//				        LocalDate.parse(dateOfEventNotified, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
//			} catch (Exception e) {
//
//			}
//        }
//        if (StringUtils.isNotBlank(dateOfEventSettled) && dateOfEventSettled.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//            try {
//				dateToComapre.add(
//				        LocalDate.parse(dateOfEventSettled, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
//			} catch (Exception e) {
//
//			}
//        }
//        if (StringUtils.isNotBlank(dateOfEventPaid) && dateOfEventPaid.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
//            try {
//				dateToComapre
//				        .add(LocalDate.parse(dateOfEventPaid, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)));
//			} catch (Exception e) {
//
//			}
//        }
//
//        for (int j = 0; j < dateToComapre.size() - 1; j++) {
//            if (dateToComapre.get(j).isAfter(dateToComapre.get(j + 1))) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    public static ControlResults control21(JavaRDD<String> data, List<String> names, String type) {
//        JavaSparkContext sc = Connection.getContext();
//        ControlResultAccumulator claims_acc = new ControlResultAccumulator(
//                new ControlResult("Claims existence check", new ArrayList<>()));
//        sc.sc().register(claims_acc); // sprint 3
//        LongAccumulator errorsCount = sc.sc().longAccumulator();
//        data.filter(new FilterForClaimsExistance(names,type)).mapToPair(new GroupingByLifeId(names)).groupByKey()
//                .foreach(new ControlByGroupLifeId(claims_acc, errorsCount,names, incidenceMap()));
//        List<ControlResult> controlResultsList = new ArrayList<ControlResult>();
//        controlResultsList.add(claims_acc.value());
//        return new ControlResults(null, errorsCount.value(), controlResultsList, null,new HashMap<>());
//    }
//
//    private static Map<String,List<String>> incidenceMap() {
//    	Map<String,List<String>> map = new HashMap<String, List<String>>();
//    	map.put("ci", Arrays.asList("ci","lifeci"));
//    	map.put("tpd", Arrays.asList("tpd","lifetpd"));
//    	map.put("ltc", Arrays.asList("ltc","lifeltc"));
//    	map.put("lifeci", Arrays.asList("ci","lifeci"));
//    	map.put("lifetpd", Arrays.asList("tpd","lifetpd"));
//    	map.put("lifeltc", Arrays.asList("ltc","lifeltc"));
//    	return map;
//    }
//
//    //Product File Controls
//    public static ControlResult CheckClientAndClientGroupAndTreaties(String path_prod, String Client, String ClientGroup, String Treaty, String distributionBrand,String country, String row_res) throws IOException {
//        BufferedReader br = null;
//        try {
//            List<ControlResult> result = new ArrayList<>();
//            FileReader fr = new FileReader(path_prod);
//            br = new BufferedReader(fr);
//            String header = br.readLine();
//            List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));
//            String row;
//
//            HashMap<String, List<String>> ClientCheck = new HashMap<>();
//            HashMap<String, List<String>> ClientGroupCheck = new HashMap<>();
//            HashMap<String, List<String>> TreatiesCheck = new HashMap<>();
//            HashMap<String, List<String>> distributionBrandCheck = new HashMap<>();
//            HashMap<String, List<String>> countryCheck = new HashMap<>();
//
//            int indexClientRiskCarrierName = names.indexOf("client_risk_carrier_name");
//            int indexClientGroup = names.indexOf("client_group");
//            int indexTreatyNumberOmega = names.indexOf("treaty_number_omega");
//            int indexdistributionBrand = names.indexOf("distribution_brand_name");
//            int indexCountry = names.indexOf("client_country");
//            ArrayList<String> examples = new ArrayList<>();
//            String row_example_country = row_res.substring(0, row_res.length() - 1);
//            while ((row = br.readLine()) != null) {
//                if (!"".equals(row)) {
//                    String[] values = row.split(";", -1);
//                    if (indexClientRiskCarrierName != -1) {
//                        FillHmap(ClientCheck, values, indexClientRiskCarrierName);
//                    }
//                    if (indexClientGroup != -1) {
//                        FillHmap(ClientGroupCheck, values, indexClientGroup);
//                    }
//                    if (indexTreatyNumberOmega != -1) {
//                        FillHmap(TreatiesCheck, values, indexTreatyNumberOmega);
//                    }
//                    if (indexdistributionBrand != -1) {
//                        FillHmap(distributionBrandCheck, values, indexdistributionBrand);
//                    }
//                    if (indexCountry != -1) {
//                        String productCountry = values[indexCountry].toLowerCase().trim();
//                        if(!productCountry.equalsIgnoreCase(country))
//                            examples.add(row_example_country + productCountry + ";" + country);
//                    }
//
//                }
//
//            }
//
//            List<AffectedColumn> affectedColumns = new ArrayList<>();
//
//            if (examples != null && examples.size()>0) {
//				AffectedColumn affectedColumn = new AffectedColumn(
//						"client_country/study_country", examples.size(),
//						new ArrayList<>(examples.subList(0, Integer.min(examples.size(), 5))));
//				affectedColumns.add(affectedColumn);
//			}
//			AffectedColumn client = checkClientAndClientGroupAndTreaty(ClientCheck, Client, "client_risk_carrier_name/study_client", row_res, indexClientRiskCarrierName);
//            if (client != null) affectedColumns.add(client);
//            AffectedColumn group = checkClientAndClientGroupAndTreaty(ClientGroupCheck, ClientGroup, "client_group/study_client_group", row_res, indexClientGroup);
//            if (group != null) affectedColumns.add(group);
//            AffectedColumn treaty = checkClientAndClientGroupAndTreaty(TreatiesCheck, Treaty, "treaty_number_omega/study_treaty_number", row_res, indexTreatyNumberOmega);
//            if (treaty != null) affectedColumns.add(treaty);
//            AffectedColumn distibutionBrandName = checkClientAndClientGroupAndTreaty(distributionBrandCheck, distributionBrand, "distribution_brand_name/distribution_brand", row_res, indexdistributionBrand);
//            if (distibutionBrandName != null) affectedColumns.add(distibutionBrandName);
////            affectedColumns.add(countryControl);
//            ControlResult countryControl =  new ControlResult("Product file information should match study metadata", affectedColumns);
//
//
//            return countryControl;
//        } catch (IOException e) {
//            throw e;
//        } finally {
//            if (br != null)
//                br.close();
//        }
//    }
//
//    private static void FillHmap(HashMap<String, List<String>> hmp, String[] values, int index) {
//        if (hmp.containsKey(values[index])) {
//            List<String> rows = hmp.get(values[index]);
//            rows.add(String.join(";", values));
//            hmp.put(values[index].trim().toLowerCase(), rows);
//        } else {
//            List<String> rows = new ArrayList<>();
//            rows.add(String.join(";", values));
//            hmp.put(values[index].trim().toLowerCase(), rows);
//        }
//
//    }
//
//    private static AffectedColumn checkClientAndClientGroupAndTreaty(HashMap<String, List<String>> hmp, String val, String ControlName, String row, int index) {
//        row = row.substring(0, row.length() - 1);
//        if (ControlName.equalsIgnoreCase("client_risk_carrier_name/study_client")) row = row.substring(0, row.length() - 8);
//        if (ControlName.equalsIgnoreCase("client_group/study_client_group")) row = row.substring(0, row.length() - 6);
//        if (ControlName.equalsIgnoreCase("treaty_number_omega/study_treaty_number")) row = row.substring(0, row.length() - 4);
//        if (ControlName.equalsIgnoreCase("distribution_brand_name/distribution_brand")) row = row.substring(0, row.length() - 2);
//
//
//
//        final String row_exp = row;
//
//        AffectedColumn result = null;
//        Set<String> values = hmp.keySet();
//		if (!ControlName.equalsIgnoreCase("distribution_brand_name/distribution_brand")) {
//            if (values.size() == 1) {
//                if (!values.contains("") && !values.iterator().next().equalsIgnoreCase(val)) {
//                    result = buildControl(hmp, row_exp, val, index, ControlName);
//                }
//                if (values.contains("")) {
//                    if ((ControlName.equalsIgnoreCase("client_risk_carrier_name/study_client") || ControlName.equalsIgnoreCase("client_group/study_client_group")) && !val.equalsIgnoreCase("multiple") && !val.equalsIgnoreCase("other")) {
//                        result = buildControl(hmp, row_exp, val, index, ControlName);
//                    } else if (ControlName.equalsIgnoreCase("treaty_number_omega/study_treaty_number") && !val.equalsIgnoreCase("multiple") && !val.equalsIgnoreCase("")) {
//                        result = buildControl(hmp, row_exp, val, index, ControlName);
//                    }
//                }
//            } else if (values.size() >= 2 && !val.equalsIgnoreCase("multiple")) {
//                result = buildControl(hmp, row_exp, val, index, ControlName);
//            }
//        } else {
//        	if(values.size() == 1 && !values.contains(val)) {
//                result = buildControl(hmp, row_exp, val, index, ControlName);
//        	} else if (values.size() >= 2 && StringUtils.isNotBlank(val)) {
//                result = buildControl(hmp, row_exp, val, index, ControlName);
//            }
//        }
//        return result;
//    }
//
//    private static ArrayList<String> extractExamples(HashMap<String, List<String>> hmp, String row, String val, int index) {
//
//        ArrayList<String> examples = new ArrayList<>();
//        Iterator it = hmp.keySet().iterator();
//        while (it.hasNext()) {
//            hmp.get(it.next()).parallelStream().forEach(s -> {
//              //  System.out.println("row size----->"+s.length()+"  "+index);
//
//                examples.add(row + s.split(";", -1)[index] + ";" + val);
//            });
//            it.remove();
//        }
//
//        return examples;
//    }
//
//    private static AffectedColumn buildControl(HashMap<String, List<String>> hmp, String row, String val, int index, String ControlName) {
//        AffectedColumn affectedColumn;
//        ArrayList<String> examples = extractExamples(hmp, row, val, index);
////        List<AffectedColumn> affectedColumns = new ArrayList<>();
//		affectedColumn = new AffectedColumn(ControlName, examples.size(),
//				new ArrayList<>(examples.subList(0, Integer.min(examples.size(), 5))));
//		return affectedColumn;
////        affectedColumns.add(affectedColumn);
////        return new ControlResult("Product file information " + ControlName + " should match study metadata", affectedColumns);
////        return new ControlResult("Product file information should match study metadata", affectedColumns);
//    }
//
//
//}
