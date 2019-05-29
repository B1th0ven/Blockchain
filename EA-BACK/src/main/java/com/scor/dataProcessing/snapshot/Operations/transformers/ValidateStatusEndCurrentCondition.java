package com.scor.dataProcessing.snapshot.Operations.transformers;

import com.google.common.collect.Lists;
import com.scor.dataProcessing.Helpers.Grouping;
import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.snapshot.Operations.Utils.RDDTransformation;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class ValidateStatusEndCurrentCondition implements Serializable {
    @Autowired
    RDDTransformation rddTransformation;


    public JavaRDD<String> rddSameEventSameStatus(JavaRDD<String> rdd, List<String> names, Integer reportPeriodIndex
            , String reportingMax) {


        JavaPairRDD<String, Iterable<String[]>> missingValueRegroup = rdd.mapToPair(new Grouping(names)).groupByKey();
        ;
        JavaPairRDD<String, List<String[]>> sortedMissingValueData = missingValueRegroup.mapValues(line -> {
            List<String[]> ele = Lists.newArrayList(line);
            Collections.sort(ele, new Comparator<String[]>() {
                @Override
                public int compare(String[] row1, String[] row2) {
                    return row1[reportPeriodIndex].compareTo(row2[reportPeriodIndex]);
                }

            });
            return ele;
        });


        JavaPairRDD<String, List<List<String>>> noMoreMissingValues = sortedMissingValueData.mapValues(tupleValues -> {
            List<List<String>> resultRows = new ArrayList<>();

            for (String[] row_arr : tupleValues) {
                if (tupleValues.size() > 1) {
                    String event = row_arr[names.indexOf(Headers.TYPE_OF_EVENT)];
                    if (event.equalsIgnoreCase("withdrawal")) {

                        row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = " withdrawn";
                    } else if (event.equalsIgnoreCase("death")) {

                        row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = " dead";
                    } else {
                        row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = " claimant";
                    }


                } else {
                    String coverEndDate = coverEndDate(row_arr, names);
                    String observationmax = row_arr[names.indexOf("observation_max")];
                    if (row_arr[names.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)].equalsIgnoreCase(coverEndDate)) {
                        row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = "expiry";
                    } else if (observationmax.compareToIgnoreCase(reportingMax) < 0) {
                        //test
                        row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = "withdrawn";
                    } else {
                        row_arr[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = "active";
                    }


                }

                resultRows.add(Arrays.asList((row_arr)));
            }
            return resultRows;
        });


        JavaRDD<String> result = rddTransformation.getJavaRDDBack(noMoreMissingValues);

        return result;
    }


    public String coverEndDate(String[] row_arr, List<String> headers) {

        String coverEndDate = "vide";
        if (headers.contains((Headers.BENEFIT_END_DATE))) {
            if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.BENEFIT_END_DATE)])
                    && !row_arr[headers.indexOf(Headers.BENEFIT_END_DATE)].equalsIgnoreCase("null")) {

                coverEndDate = row_arr[headers.indexOf(Headers.BENEFIT_END_DATE)];
            }
        } else if (headers.contains(Headers.BENEFIT_TERM_YEARS)) {

            if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.BENEFIT_TERM_YEARS)])) {
                String dateCom = row_arr[headers.indexOf(Headers.DATE_OF_COMMENCEMENT)];
                LocalDate end1 = LocalDate.parse(dateCom,
                        DateTimeFormatter.ofPattern
                                ("dd/MM/yyyy", Locale.ENGLISH));
                LocalDate addingBTYearsToDateOfComm = end1.plusYears(Integer.parseInt(row_arr[headers.indexOf(Headers.BENEFIT_TERM_YEARS)]));

                LocalDate fix = LocalDate.parse(addingBTYearsToDateOfComm.toString(),
                        DateTimeFormatter.ofPattern
                                ("yyyy-MM-dd", Locale.ENGLISH));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                String test = formatter.format(fix);
                coverEndDate = test;

            }
        } else if (headers.contains(Headers.BENEFIT_MAX_AGE)) {
            if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.BENEFIT_MAX_AGE)])) {
                String dateOfBirth = row_arr[headers.indexOf(Headers.DATE_OF_BIRTH)];
                LocalDate dateOfBirthLocal = LocalDate.parse(dateOfBirth,
                        DateTimeFormatter.ofPattern
                                ("dd/MM/yyyy", Locale.ENGLISH));
                LocalDate l = dateOfBirthLocal.plusYears(Integer.parseInt(row_arr[headers.indexOf(Headers.BENEFIT_MAX_AGE)]));

                LocalDate fix = LocalDate.parse(l.toString(),
                        DateTimeFormatter.ofPattern
                                ("yyyy-MM-dd", Locale.ENGLISH));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                String test = formatter.format(fix);
                coverEndDate = test;

            }
        } else {
            coverEndDate = "vide";
        }

        return coverEndDate;
    }


}
