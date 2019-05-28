package com.scor.dataProcessing.snapshot.Operations.checkers;


import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.sparkConnection.Connection;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
public class CheckReporting implements Serializable {

    private static final JavaSparkContext sc = Connection.getContext();


    public String checkReporting(List<String> paths) {
        List<String> result3 = new ArrayList<>();
        List<String> result2 = new ArrayList<>();
        List<String> result4 = new ArrayList<>();
        for (String path : paths) {
            JavaRDD<String> data = sc.textFile(path);
            String header = data.first();
            List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));
            boolean isReportingYear = names.contains(Headers.REPORTING_YEAR);
            boolean isReportingQuarter = names.contains(Headers.REPORTING_QUARTER);
            boolean isReportingMonth = names.contains(Headers.REPORTING_MONTH);

            if (isReportingYear) {
                result2.add(Headers.REPORTING_YEAR);
                result4.add(Headers.REPORTING_YEAR + " " + path);
            } else if (isReportingMonth) {
                result2.add(Headers.REPORTING_MONTH);
                result4.add(Headers.REPORTING_MONTH + " " + path);
            } else if (isReportingQuarter) {
                result2.add(Headers.REPORTING_QUARTER);
                result4.add(path + " " + Headers.REPORTING_QUARTER);
            } else
                result2.add("no reporting period");
            result4.add(path + " " + "no reporting period");
        }
        String resultReporting = "";

        if (!(result2.stream().distinct().count() <= 1)) {
            result2.stream().distinct().forEach(s -> result3.add(s));
            for (String period : result3) {
                resultReporting = resultReporting + period + " " + " , ";
            }
            resultReporting = resultReporting.substring(0, resultReporting.length() - 2);

        } else {
            resultReporting = "true";
        }

        // With PATHS

        /*
         String resultReporting = "";
         if (!(result2.stream().distinct().count() <= 1)) {
            result4.stream().distinct().forEach(s -> result3.add(s));
            for (String periodAndPaths : result4) {
                resultReporting = resultReporting + periodAndPaths + " " + " , ";
            }
            resultReporting = resultReporting.substring(0, resultReporting.length() - 1);

        } else {
            resultReporting = "true";
        }

         */


        return resultReporting;
    }



}