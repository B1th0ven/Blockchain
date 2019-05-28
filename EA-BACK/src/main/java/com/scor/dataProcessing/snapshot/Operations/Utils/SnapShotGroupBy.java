package com.scor.dataProcessing.snapshot.Operations.Utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.scor.dataProcessing.Helpers.Grouping;
import com.scor.dataProcessing.Helpers.Headers;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.stereotype.Service;
import py4j.StringUtil;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SnapShotGroupBy implements Serializable {



 /*   public Map<String,String> MapDateOfEndCurrentConditionByIds(JavaRDD <String> data , List<String> headers , int reportPeriodIndex ) {


        JavaPairRDD<String, Iterable<String[]>> groupData = data.mapToPair(new Grouping(headers)).groupByKey();


        JavaPairRDD<String, List<String[]>> sortedData = groupData.mapValues(line -> {
            List<String[]> ele = Lists.newArrayList(line);
            Collections.sort(ele, new Comparator<String[]>() {
                @Override
                public int compare(String[] row1, String[] row2) {
                    return row1[reportPeriodIndex].compareTo(row2[reportPeriodIndex]);
                }

            });
            return ele;
        });
        Object ordering = Ordering.natural();

        final Comparator<String> cmp = (Comparator<String>) ordering;


        JavaPairRDD<String, String> sortedDataIncrementedId = sortedData.mapValues(line -> {
            String[] lineToTreat = line.get(line.size() - 1);
            String booleanResult = lineToTreat[0];
            return booleanResult.toString();
        });

        String result = sortedDataIncrementedId.map(tuple -> tuple._2).toString() ;




        String header = data.first();
        JavaRDD<String> data_without_header = data.filter(line -> !line.equalsIgnoreCase(header) && !line.isEmpty());


        JavaRDD<String[]> data_splitted = data_without_header.map(x -> x.toLowerCase().trim().split(";",-1));


        JavaRDD<String> rddlife = data_splitted.map(s -> {
            if(headers.indexOf(Headers.LIFE_ID) != -1) return s[headers.indexOf(Headers.LIFE_ID)];
            return null ;
        }).filter(Objects::nonNull);


        List<String> lifeListId = rddlife.collect().stream().distinct().collect(Collectors.toList());

        JavaRDD<String> rddpolicy = data_splitted.map(s -> {
            if(headers.indexOf(Headers.POLICY_ID) != -1) return s[headers.indexOf(Headers.POLICY_ID)];
            return null ;
        }).filter(Objects::nonNull);

        List<String> policyListId = rddpolicy.collect().stream().distinct().collect(Collectors.toList());
        Map<String,String> resMap = new HashMap<>();

        for (String lifeId : lifeListId) {

            JavaRDD<String[]> dataLife = data_splitted.filter(row -> (row[headers.indexOf(Headers.LIFE_ID)].equalsIgnoreCase(lifeId))) ;
            for (String policyId : policyListId) {
                JavaRDD<String[]> dataPolicy = dataLife.filter(row -> (row[headers.indexOf(Headers.POLICY_ID)].equalsIgnoreCase(policyId)));

                JavaRDD<String> rddTime = dataPolicy.map(s -> {
                    if(headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION) != -1) return s[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)];
                    return null ;
                }).filter(Objects::nonNull);
                rddTime.cache();


                String lastDateOfEndCurrentCondition  = rddTime.max(cmp);

                resMap.put(lifeId + policyId ,lastDateOfEndCurrentCondition) ;
            }

        }
        return resMap;
    }
*/

    public String minMaxVariableDate(JavaRDD <String> data , List<String> headers , String variable , boolean needMin ) {

        Object ordering = Ordering.natural();
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        final Comparator<Date> cmp = (Comparator<Date>) ordering;
        JavaRDD<String> data_without_header = data.filter(line -> !line.isEmpty());



        JavaRDD<String[]> data_splitted = data_without_header.map(x -> x.toLowerCase().trim().split(";",-1));

        JavaRDD<String>  rddTime = data_splitted.map(s -> {
            if(headers.indexOf(variable) != -1  ) {
                return s[headers.indexOf(variable)];
            }
            return "" ;
        }).filter(s -> StringUtils.isNotBlank(s));
     if(rddTime.isEmpty())

     {
       return ("27/12/1995")  ;

     }
        Date temp ;

        if (needMin == true)
        temp = rddTime.map(s -> sdf.parse(s)).min(cmp);
        else  temp = rddTime.map(s -> sdf.parse(s)).max(cmp);



        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String strDate = dateFormat.format(temp);

        return (strDate) ;

    }













    /*public Map<String,String> MapVariableByIds(JavaRDD <String> data , List<String> headers , String variable ) {

        String header = data.first();
        JavaRDD<String> data_without_header = data.filter(line -> !line.equalsIgnoreCase(header) && !line.isEmpty());
        JavaRDD<String[]> data_splitted = data_without_header.map(x -> x.toLowerCase().trim().split(";",-1));

        Object ordering = Ordering.natural();

        final Comparator<String> cmp = (Comparator<String>) ordering;

        JavaRDD<String> rddlife = data_splitted.map(s -> {
            if(headers.indexOf(Headers.LIFE_ID) != -1) return s[headers.indexOf(Headers.LIFE_ID)];
            return null ;
        }).filter(Objects::nonNull);

        List<String> lifeListId = rddlife.collect().stream().distinct().collect(Collectors.toList());

        JavaRDD<String> rddpolicy = data_splitted.map(s -> {
            if(headers.indexOf(Headers.POLICY_ID) != -1) return s[headers.indexOf(Headers.POLICY_ID)];
            return null ;
        }).filter(Objects::nonNull);

        List<String> policyListId = rddpolicy.collect().stream().distinct().collect(Collectors.toList());

        JavaRDD<String> rddbenefit = data_splitted.map(s -> {
            if(headers.indexOf(Headers.BENEFIT_ID) != -1) return s[headers.indexOf(Headers.BENEFIT_ID)];
            return null ;
        }).filter(Objects::nonNull);

        List<String> benefitListId = rddbenefit.collect().stream().distinct().collect(Collectors.toList());

        Map<String,String> resMap = new HashMap<>();
        if (!headers.contains(Headers.BENEFIT_ID)) {
            for (String lifeId : lifeListId) {

                JavaRDD<String[]> dataLife = data_splitted.filter(row -> (row[headers.indexOf(Headers.LIFE_ID)].equalsIgnoreCase(lifeId)));
                for (String policyId : policyListId) {
                    JavaRDD<String[]> dataPolicy = dataLife.filter(row -> (row[headers.indexOf(Headers.POLICY_ID)].equalsIgnoreCase(policyId)));

                        JavaRDD<String> rddTime = dataPolicy.map(s -> {
                            if (headers.indexOf(variable) != -1) return s[headers.indexOf(variable)];
                            return null;
                        }).filter(Objects::nonNull);

                        String lastVaribleByThisIds = rddTime.max(cmp);

                        resMap.put(lifeId + policyId, lastVaribleByThisIds);


                }
            }
        } else {

            for (String lifeId : lifeListId) {

                JavaRDD<String[]> dataLife = data_splitted.filter(row -> (row[headers.indexOf(Headers.LIFE_ID)].equalsIgnoreCase(lifeId)));
                for (String policyId : policyListId) {
                    JavaRDD<String[]> dataPolicy = dataLife.filter(row -> (row[headers.indexOf(Headers.POLICY_ID)].equalsIgnoreCase(policyId)));
                    for (String benefitId : benefitListId) {
                        JavaRDD<String[]> databenefit = dataPolicy.filter(row -> (row[headers.indexOf(Headers.BENEFIT_ID)].equalsIgnoreCase(benefitId)));

                        JavaRDD<String> rddTime = databenefit.map(s -> {
                            if (headers.indexOf(variable) != -1) return s[headers.indexOf(variable)];
                            return null;
                        }).filter(Objects::nonNull);
                        rddTime.cache();
                        String lastVaribleByThisIds = rddTime.max(cmp);

                        resMap.put(lifeId + policyId, lastVaribleByThisIds);
                    }



                }

            }
        }

        return resMap;}*/










}