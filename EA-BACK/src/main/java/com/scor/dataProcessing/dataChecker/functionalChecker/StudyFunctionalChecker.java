package com.scor.dataProcessing.dataChecker.functionalChecker;

import com.scor.dataProcessing.Helpers.*;
import com.scor.dataProcessing.Helpers.filters.FilterForClaimsExistance;
import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.common.DataProduct;
import com.scor.dataProcessing.dataChecker.functionalChecker.Controls.*;
import com.scor.dataProcessing.dataChecker.functionalChecker.Operations.studyOperations.ControlByGroup;
import com.scor.dataProcessing.dataChecker.functionalChecker.Operations.studyOperations.ControlByGroupLifeId;
import com.scor.dataProcessing.dataChecker.functionalChecker.Operations.studyOperations.ControlReduceBy;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.ControlResults;
import com.scor.dataProcessing.models.Product;
import com.scor.dataProcessing.sparkConnection.Connection;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;
import org.omg.CORBA.DATA_CONVERSION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class StudyFunctionalChecker implements InterfaceToFunctionalChecker {



    private static final long serialVersionUID = 8186936945898986237L;

    private static final StudyFunctionalChecker instance = new StudyFunctionalChecker();

    public static StudyFunctionalChecker getInstance() {
        return instance;
    }

    private StudyFunctionalChecker() {

    };




    public  ControlResults run(String path, String path_prod, String type, String op_start, String op_end,String Client,String ClientGroup,String Treaty,String distributionBrand,String country)
            throws IOException {
        // Getting product file and putting it in List of product object
        DataProduct dataProduct = new DataProduct();
//		List<Product> products = dataProduct.getProd(path_prod);
        Map<String, Product> products = dataProduct.getProduct(path_prod);
        List<Product> duplicatedProduct = dataProduct.getDuplicatedProduct(path_prod);
        // Policy file Functional MainControls
        ControlResults controlResultPolicy = runPolicyControl(path, path_prod, type, op_start, op_end, products);
        // Product File Functional MainControls
        ControlResults controlResultProduct = runProductControl(path, path_prod, type, op_start, op_end,
                duplicatedProduct, controlResultPolicy.getHeader().split(";").length,Client,ClientGroup,Treaty,distributionBrand,country);
        // Merging and returning result
        List<ControlResult> controlResults = controlResultPolicy.getControlResultsList();
        controlResults.addAll(controlResultProduct.getControlResultsList());
        return new ControlResults(null,
                controlResultPolicy.getNumber_of_errors() + controlResultProduct.getNumber_of_errors(), controlResults,
                controlResultPolicy.getHeader() + ";duplicated_product_id",controlResultPolicy.getFileValues());
    }

    private  ControlResults runProductControl(String path, String path_prod, String type, String op_start,
                                              String op_end, List<Product> duplicatedProducts, int headerSize,String Client, String ClientGroup, String Treaty,String distributionBrand,String country) throws IOException {
        String row = "";
        for (int i = 0; i < headerSize; i++)
            row += ";";
        final String row_2 = row;
        // Defining Control Result
        List<ControlResult> controlResultsList = new ArrayList<>();
        AffectedColumn affectedColumn = new AffectedColumn("Unique ProductID Control", 0, new ArrayList<>());

        if (duplicatedProducts != null && !duplicatedProducts.isEmpty()) {
            String rowExemple = "";
            for (int i = 0; i < headerSize-10; i++)
                rowExemple += ";";
            final String rowExemple_2 = rowExemple;
            duplicatedProducts.parallelStream().forEach(product -> {
                affectedColumn.setErrorsNumber(affectedColumn.getErrorsNumber() + 1);
                affectedColumn.getExamples().add(rowExemple_2 + product.getId());
            });
        }
        List<AffectedColumn> affectedColumns = new ArrayList<>();
        if (affectedColumn.getErrorsNumber() > 0) {
            affectedColumns.add(affectedColumn);
        }
        ControlResult productCoherenceMetadata = this.CheckClientAndClientGroupAndTreaties(path_prod,Client,ClientGroup,Treaty,distributionBrand,country,row_2);

        controlResultsList.add(new ControlResult("Unique ProductID Control", affectedColumns));
        controlResultsList.add(productCoherenceMetadata);

        Control53 control53 = new Control53();

        ControlResult controlResult53 = control53.control53review(path_prod);
        if (controlResult53 != null){
            controlResultsList.add(controlResult53);
        }

        return new ControlResults(null, (long) affectedColumn.getErrorsNumber(), controlResultsList, "", new HashMap<>());
    }

    private ControlResults runPolicyControl(String path, String path_prod, String type, String op_start,
                                            String op_end, Map<String, Product> products){

        JavaRDD<String> data = sc.textFile(path);
        String header = data.first();
        List<String> names = Arrays.asList(header.toLowerCase().split(";", -1));
        ControlResultAccumulator exposure_coherent_status = new ControlResultAccumulator(
                new ControlResult("Exposure end coherent with status", new ArrayList<>()));
        sc.sc().register(exposure_coherent_status);


        RuleFactory rf = new RuleFactory(sc,type,op_start,op_end,names);
        List<IRule> rules = rf.getRules();

        data.foreach(new MainControls(rules,header,names,products));

        ControlResults groupedControls = null;
        if (names.containsAll(Arrays.asList(Headers.LIFE_ID, Headers.POLICY_ID))) {
            // before all we need to sort our rdd by date of start of current condition :
            JavaRDD<String> data_sorted = data.sortBy(new SortByDateBCC(names),true, data.getNumPartitions());
            groupedControls = this.statusConsistencyCheck(data_sorted, names, type);
            this.reduceByControl(exposure_coherent_status,rf.getErrorsCount(),type,op_end,names,data);
        } 
//        else {
//			List<AffectedColumn> affectedColumns = new ArrayList<>();
//			affectedColumns.add(new AffectedColumn("Missing Life ID or Policy_ID", 1,null));
//			exposure_coherent_status
//					.add(new ControlResult("Exposure end coherent with status ", affectedColumns));
//			rf.getErrorsCount().add(1);
//		}


        ControlResults claimsExistanceControl  = null;
        if (names.containsAll(Arrays.asList(Headers.LIFE_ID))) {
            claimsExistanceControl = this.control21(data, names,type);
        }
        ControlResultAccumulator controlResultacc = new ControlResultAccumulator(
                new ControlResult("Variables consistency", new ArrayList<>()));
        sc.sc().register(controlResultacc); // sprint 3
        new Control51(controlResultacc, rf.getErrorsCount(), names, type).validate(data); ; 
        
        List<ControlResult> controlResultsList = this.collectResult(rf,groupedControls,claimsExistanceControl,exposure_coherent_status);
        controlResultsList.add(controlResultacc.value()) ; 
        return new ControlResults(null, rf.getErrorsCount().value(), controlResultsList, header
                + ";age_at_commencement_definition;product_start_date;product_end_date;min_face_amount;max_face_amount;min_age_at_commencement;max_age_at_commencement;client_risk_carrier_name;study_client;client_group;study_client_group;treaty_number_omega;study_treaty_number;distribution_brand_name;distribution_brand;Client_Country;Study_Country",rf.getValuesPersistAccumulator().value());
    }

    private void reduceByControl (ControlResultAccumulator exposure_coherent_status,LongAccumulator errorsCount, String type,String op_end,List<String> names, JavaRDD<String> data){
        if (names.containsAll(Arrays.asList(Headers.DATE_OF_END_CURRENT_CONDITION))) {
            data.filter(s -> !s.toLowerCase().trim().replace(";", "").equalsIgnoreCase(""))
                    .mapToPair(new Grouping(names)).reduceByKey(new ReduceByDateBCC(names)).foreach(new ControlReduceBy(exposure_coherent_status,type,op_end,names,errorsCount));

        }

    }

    public  ControlResults control21(JavaRDD<String> data, List<String> names, String type) {
        JavaSparkContext sc = Connection.getContext();
        ControlResultAccumulator claims_acc = new ControlResultAccumulator(
                new ControlResult("Claims existence check", new ArrayList<>()));
        sc.sc().register(claims_acc); // sprint 3
        LongAccumulator errorsCount = sc.sc().longAccumulator();
        data.filter(new FilterForClaimsExistance(names,type)).mapToPair(new GroupingByLifeId(names)).groupByKey()
                .foreach(new ControlByGroupLifeId(claims_acc, errorsCount,names, incidenceMap()));
        List<ControlResult> controlResultsList = new ArrayList<ControlResult>();
        controlResultsList.add(claims_acc.value());
        return new ControlResults(null, errorsCount.value(), controlResultsList, null,new HashMap<>());
    }

    private static Map<String,List<String>> incidenceMap() {
        Map<String,List<String>> map = new HashMap<String, List<String>>();
        map.put("ci", Arrays.asList("ci","lifeci"));
        map.put("tpd", Arrays.asList("tpd","lifetpd"));
        map.put("ltc", Arrays.asList("ltc","lifeltc"));
        map.put("lifeci", Arrays.asList("ci","lifeci"));
        map.put("lifetpd", Arrays.asList("tpd","lifetpd"));
        map.put("lifeltc", Arrays.asList("ltc","lifeltc"));
        return map;
    }


    private  List<ControlResult> collectResult(RuleFactory rf,ControlResults groupedControls,ControlResults claimsExistanceControl,ControlResultAccumulator exposure_coherent_status){
        ControlResult dateOfLastMedicalSelectionControl = Control40.collectdateOfLastMedicalSelectionControlResult2(rf.getAcc());
        ControlResult Inc_Death_Result = Control43.incidenceDeathXORIncidence_DeathResult2(rf.getAcc(),rf.getErrorsCount().value()); // getting the result

        List<ControlResult> controlResultsList = new ArrayList<>();
        //controlResultsList.add(rf.getCr_acc().value());
        for(ControlResult cntrl : rf.getAcc().value().getControlResults() ) {
            if(cntrl.getAffectedColumns().size() != 0
                    ||!cntrl.getControl().equalsIgnoreCase("Incidence_Death_Check")
                    ||!cntrl.getControl().equalsIgnoreCase("Claimant_Dead_Check"))
                controlResultsList.add(cntrl);
        }
        if(exposure_coherent_status.value().getAffectedColumns().size()!= 0)
            controlResultsList.add(exposure_coherent_status.value()); //*

        if(Inc_Death_Result.getAffectedColumns().size() != 0)
            controlResultsList.add(Inc_Death_Result);


        if(claimsExistanceControl != null ) { //*
            controlResultsList.addAll(claimsExistanceControl.getControlResultsList());
            rf.getErrorsCount().add(claimsExistanceControl.getNumber_of_errors());
        }
        if(groupedControls != null ) { //*
            controlResultsList.addAll(groupedControls.getControlResultsList());
            rf.getErrorsCount().add(groupedControls.getNumber_of_errors());
        }
        if(dateOfLastMedicalSelectionControl != null) {
            controlResultsList.add(dateOfLastMedicalSelectionControl);
            rf.getErrorsCount().add(dateOfLastMedicalSelectionControl.getAffectedColumns().get(0).getErrorsNumber());
        }

        return controlResultsList;

    }

    public  ControlResults statusConsistencyCheck(JavaRDD<String> data, List<String> names, String type) {
        JavaSparkContext sc = Connection.getContext();
        List<ControlResult> res = new ArrayList<>();
        ControlResultAccumulator cc_acc = new ControlResultAccumulator(
                new ControlResult("Consistenct Check", new ArrayList<>()));
        sc.sc().register(cc_acc);
        ControlResultAccumulator overlap_acc = new ControlResultAccumulator(
                new ControlResult("Overlap Check", new ArrayList<>()));
        sc.sc().register(overlap_acc);
        ControlResultAccumulator StatusDateSpaceAccumulator = new ControlResultAccumulator(
                new ControlResult("status date space", new ArrayList<>()));
        sc.sc().register(StatusDateSpaceAccumulator);
        LongAccumulator errorsCount = sc.sc().longAccumulator();
        data.filter(s -> !s.toLowerCase().trim().replace(";", "").equalsIgnoreCase("")
                && !s.toLowerCase().trim().equalsIgnoreCase(String.join(";", names)))
                .mapToPair(new Grouping(names)).groupByKey()
                .foreach(new ControlByGroup(cc_acc, overlap_acc,StatusDateSpaceAccumulator, type, names, errorsCount));

        res.add(cc_acc.value());
        res.add(overlap_acc.value());
        res.add(StatusDateSpaceAccumulator.value());
        return new ControlResults(null, errorsCount.value(), res, null,new HashMap<>());

    }

    public  ControlResult CheckClientAndClientGroupAndTreaties(String path_prod, String Client, String ClientGroup, String Treaty, String distributionBrand,String country, String row_res) throws IOException {
        BufferedReader br = null;
        try {
            List<ControlResult> result = new ArrayList<>();
            FileReader fr = new FileReader(path_prod);
            br = new BufferedReader(fr);
            String header = br.readLine();
            List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));
            String row;

            HashMap<String, List<String>> ClientCheck = new HashMap<>();
            HashMap<String, List<String>> ClientGroupCheck = new HashMap<>();
            HashMap<String, List<String>> TreatiesCheck = new HashMap<>();
            HashMap<String, List<String>> distributionBrandCheck = new HashMap<>();
            HashMap<String, List<String>> countryCheck = new HashMap<>();

            int indexClientRiskCarrierName = names.indexOf("client_risk_carrier_name");
            int indexClientGroup = names.indexOf("client_group");
            int indexTreatyNumberOmega = names.indexOf("treaty_number_omega");
            int indexdistributionBrand = names.indexOf("distribution_brand_name");
            int indexCountry = names.indexOf("client_country");
            ArrayList<String> examples = new ArrayList<>();
            String row_example_country = row_res.substring(0, row_res.length() - 1);
            while ((row = br.readLine()) != null) {
                if (!"".equals(row)) {
                    String[] values = row.split(";", -1);
                    if (indexClientRiskCarrierName != -1) {
                        FillHmap(ClientCheck, values, indexClientRiskCarrierName);
                    }
                    if (indexClientGroup != -1) {
                        FillHmap(ClientGroupCheck, values, indexClientGroup);
                    }
                    if (indexTreatyNumberOmega != -1) {
                        FillHmap(TreatiesCheck, values, indexTreatyNumberOmega);
                    }
                    if (indexdistributionBrand != -1) {
                        FillHmap(distributionBrandCheck, values, indexdistributionBrand);
                    }
                    if (indexCountry != -1) {
                        String productCountry = values[indexCountry].toLowerCase().trim();
                        if(!productCountry.equalsIgnoreCase(country))
                            examples.add(row_example_country + productCountry + ";" + country);
                    }

                }

            }

            List<AffectedColumn> affectedColumns = new ArrayList<>();

            if (examples != null && examples.size()>0) {
                AffectedColumn affectedColumn = new AffectedColumn(
                        "client_country/study_country", examples.size(),
                        new ArrayList<>(examples.subList(0, Integer.min(examples.size(), 5))));
                affectedColumns.add(affectedColumn);
            }
            AffectedColumn client = checkClientAndClientGroupAndTreaty(ClientCheck, Client, "client_risk_carrier_name/study_client", row_res, indexClientRiskCarrierName);
            if (client != null) affectedColumns.add(client);
            AffectedColumn group = checkClientAndClientGroupAndTreaty(ClientGroupCheck, ClientGroup, "client_group/study_client_group", row_res, indexClientGroup);
            if (group != null) affectedColumns.add(group);
            AffectedColumn treaty = checkClientAndClientGroupAndTreaty(TreatiesCheck, Treaty, "treaty_number_omega/study_treaty_number", row_res, indexTreatyNumberOmega);
            if (treaty != null) affectedColumns.add(treaty);
            AffectedColumn distibutionBrandName = checkClientAndClientGroupAndTreaty(distributionBrandCheck, distributionBrand, "distribution_brand_name/distribution_brand", row_res, indexdistributionBrand);
            if (distibutionBrandName != null) affectedColumns.add(distibutionBrandName);
//            affectedColumns.add(countryControl);
            ControlResult countryControl =  new ControlResult("Product file information should match study metadata", affectedColumns);


            return countryControl;
        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null)
                br.close();
        }
    }

    private static void FillHmap(HashMap<String, List<String>> hmp, String[] values, int index) {
        if (hmp.containsKey(values[index])) {
            List<String> rows = hmp.get(values[index]);
            rows.add(String.join(";", values));
            hmp.put(values[index].trim().toLowerCase(), rows);
        } else {
            List<String> rows = new ArrayList<>();
            rows.add(String.join(";", values));
            hmp.put(values[index].trim().toLowerCase(), rows);
        }

    }

    private  AffectedColumn checkClientAndClientGroupAndTreaty(HashMap<String, List<String>> hmp, String val, String ControlName, String row, int index) {
        row = row.substring(0, row.length() - 1);
        if (ControlName.equalsIgnoreCase("client_risk_carrier_name/study_client")) row = row.substring(0, row.length() - 8);
        if (ControlName.equalsIgnoreCase("client_group/study_client_group")) row = row.substring(0, row.length() - 6);
        if (ControlName.equalsIgnoreCase("treaty_number_omega/study_treaty_number")) row = row.substring(0, row.length() - 4);
        if (ControlName.equalsIgnoreCase("distribution_brand_name/distribution_brand")) row = row.substring(0, row.length() - 2);



        final String row_exp = row;

        AffectedColumn result = null;
        Set<String> values = hmp.keySet();
        if (!ControlName.equalsIgnoreCase("distribution_brand_name/distribution_brand")) {
            if (values.size() == 1) {
                if (!values.contains("") && !values.iterator().next().equalsIgnoreCase(val)) {
                    result = buildControl(hmp, row_exp, val, index, ControlName);
                }
                if (values.contains("")) {
                    if ((ControlName.equalsIgnoreCase("client_risk_carrier_name/study_client") || ControlName.equalsIgnoreCase("client_group/study_client_group")) && !val.equalsIgnoreCase("multiple") && !val.equalsIgnoreCase("other")) {
                        result = buildControl(hmp, row_exp, val, index, ControlName);
                    } else if (ControlName.equalsIgnoreCase("treaty_number_omega/study_treaty_number") && !val.equalsIgnoreCase("multiple") && !val.equalsIgnoreCase("")) {
                        result = buildControl(hmp, row_exp, val, index, ControlName);
                    }
                }
            } else if (values.size() >= 2 && !val.equalsIgnoreCase("multiple")) {
                result = buildControl(hmp, row_exp, val, index, ControlName);
            }
        } else {
            if(values.size() == 1 && !values.contains(val)) {
                result = buildControl(hmp, row_exp, val, index, ControlName);
            } else if (values.size() >= 2 && StringUtils.isNotBlank(val)) {
                result = buildControl(hmp, row_exp, val, index, ControlName);
            }
        }
        return result;
    }

    private  ArrayList<String> extractExamples(HashMap<String, List<String>> hmp, String row, String val, int index) {

        ArrayList<String> examples = new ArrayList<>();
        Iterator it = hmp.keySet().iterator();
        while (it.hasNext()) {
            hmp.get(it.next()).parallelStream().forEach(s -> {
                //  System.out.println("row size----->"+s.length()+"  "+index);

                examples.add(row + s.split(";", -1)[index] + ";" + val);
            });
            it.remove();
        }

        return examples;
    }

    private  AffectedColumn buildControl(HashMap<String, List<String>> hmp, String row, String val, int index, String ControlName) {
        AffectedColumn affectedColumn;
        ArrayList<String> examples = extractExamples(hmp, row, val, index);
//        List<AffectedColumn> affectedColumns = new ArrayList<>();
        affectedColumn = new AffectedColumn(ControlName, examples.size(),
                new ArrayList<>(examples.subList(0, Integer.min(examples.size(), 5))));
        return affectedColumn;
//        affectedColumns.add(affectedColumn);
//        return new ControlResult("Product file information " + ControlName + " should match study metadata", affectedColumns);
//        return new ControlResult("Product file information should match study metadata", affectedColumns);
    }





}
