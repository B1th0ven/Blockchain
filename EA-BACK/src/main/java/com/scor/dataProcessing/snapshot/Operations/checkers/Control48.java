package com.scor.dataProcessing.snapshot.Operations.checkers;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.scor.dataProcessing.Helpers.Grouping;
import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.snapshot.Operations.checkers.Control48Utils.ValidateControl;
import com.scor.dataProcessing.sparkConnection.Connection;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
public class Control48 implements Serializable {

    @Autowired
    ValidateControl validateControl;
    private static final JavaSparkContext sc = Connection.getContext();

    public String control48(List<String> paths, String maxReportingPeriod) {


        for (String path : paths) {

            JavaRDD<String> data = sc.textFile(path);
            String header = data.first();
            List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));
            String variableTime;
            if (names.contains(Headers.REPORTING_MONTH)) {
                variableTime = Headers.REPORTING_MONTH;
            } else if (names.contains(Headers.REPORTING_YEAR)) {
                variableTime = Headers.REPORTING_YEAR;
            } else if (names.contains(Headers.REPORTING_QUARTER)) {
                variableTime = Headers.REPORTING_QUARTER;
            } else {
                variableTime = "";
            }
            Integer reportPeriodIndex = names.indexOf(variableTime);


            if (!variableTime.equalsIgnoreCase("") && names.contains(Headers.MAIN_RISK_TYPE) && names.contains(Headers.STATUS_END_CURRENT_CONDITION)) {
        

                String listeOfLastRowControl48 = listOfLastRowControl48(data, reportPeriodIndex, names, maxReportingPeriod);

                return listeOfLastRowControl48;

            }


        }
        return "true";

    }

    public String listOfLastRowControl48(JavaRDD<String> rdd, int reportPeriodIndex, List<String> names, String ReprotingMax) {


        JavaPairRDD<String, Iterable<String[]>> groupData = rdd.mapToPair(new Grouping(names)).groupByKey();

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
            Boolean booleanResult = validateControl.TestControl(lineToTreat, names, ReprotingMax);
            return booleanResult.toString();
        });

        String result = sortedDataIncrementedId.map(tuple -> tuple._2).min(cmp);

        return result;
    }

}
