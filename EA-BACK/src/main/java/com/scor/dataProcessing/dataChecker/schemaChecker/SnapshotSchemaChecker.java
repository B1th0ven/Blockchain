package com.scor.dataProcessing.dataChecker.schemaChecker;


import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Ordering;
import com.scor.dataProcessing.Helpers.Headers;

import com.scor.dataProcessing.snapshot.dto.MinMaxDto;
import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.springframework.beans.factory.annotation.Autowired;

public class SnapshotSchemaChecker implements InterfaceToSchemaChecker {

    private static final SnapshotSchemaChecker instance = new SnapshotSchemaChecker();

    private SnapshotSchemaChecker() {
    }

    ;

    public static SnapshotSchemaChecker getInstance() {
        return instance;
    }

    public List<List<String>> run(String path) {
        JavaRDD<String> data = sc.textFile(path);
        String header = data.first();
        List<String> HeadersCols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";", -1)));
        List<String> cols = new ArrayList<String>(Arrays.asList(header.toLowerCase().trim().split(";", -1)));

        boolean isreporting_year = HeadersCols.contains(Headers.REPORTING_YEAR);
        boolean isreporting_quarter = HeadersCols.contains(Headers.REPORTING_QUARTER);
        boolean isreporting_month = HeadersCols.contains(Headers.REPORTING_MONTH);

        List<String> columns = pivCols.stream().filter(s -> HeadersCols.contains(s)).collect(Collectors.toList());
        List<String> missingColumns = compCols.stream().filter(s -> !cols.contains(s)).collect(Collectors.toList());

      //Status_begin_current_condition / Status_end_current_condition / Date_begin_current_condition / Date_end_current_condition not compulsory

        if (missingColumns.contains(Headers.STATUS_BEGIN_CURRENT_CONDITION)) missingColumns.remove(Headers.STATUS_BEGIN_CURRENT_CONDITION) ;
        if (missingColumns.contains(Headers.STATUS_END_CURRENT_CONDITION)) missingColumns.remove(Headers.STATUS_END_CURRENT_CONDITION) ;
        if (missingColumns.contains(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)) missingColumns.remove(Headers.DATE_OF_BEGIN_CURRENT_CONDITION) ;
        if (missingColumns.contains(Headers.DATE_OF_END_CURRENT_CONDITION)) missingColumns.remove(Headers.DATE_OF_END_CURRENT_CONDITION) ;
       // if (missingColumns.contains(Headers.EXPOSURE_OR_EVENT)) missingColumns.remove(Headers.EXPOSURE_OR_EVENT) ;

if (isreporting_year) {

    if (!isreporting_month) {
        missingColumns.remove(Headers.REPORTING_MONTH) ;
    }
    if (!isreporting_quarter) {
        missingColumns.remove(Headers.REPORTING_QUARTER) ;
    }

}


        if (isreporting_month) {

            if (!isreporting_year) {
                missingColumns.remove(Headers.REPORTING_YEAR) ;
            }
            if (!isreporting_quarter) {
                missingColumns.remove(Headers.REPORTING_QUARTER) ;
            }

        }

        if (isreporting_quarter) {

            if (!isreporting_year) {
                missingColumns.remove(Headers.REPORTING_YEAR) ;
            }
            if (!isreporting_month) {
                missingColumns.remove(Headers.REPORTING_MONTH) ;
            }

        }



        if ((!isreporting_month && !isreporting_quarter && !isreporting_year)) {
            missingColumns.add("no Reporting period");}



        List<String> ignoredColumns = cols.stream().filter(s -> !pivCols.contains(s)).collect(Collectors.toList());
        List<String> duplicatedColumns = cols.stream().filter(i -> Collections.frequency(cols, i) > 1).distinct()
                .collect(Collectors.toList());

        List<String> minReportingPeriod = new ArrayList<>() ;
        List<String> maxReportingPeriod = new ArrayList<>() ;


        MinMaxDto minMaxDto = getMinMaxReportingPeriodFromSinglePolicyFile(path) ;
        if (minMaxDto == null){
            minReportingPeriod.add("notFound");
            maxReportingPeriod.add("notFound");
            return Arrays.asList( new ArrayList<>(columns), missingColumns, ignoredColumns, duplicatedColumns,minReportingPeriod ,maxReportingPeriod,HeadersCols);
        }
        String reportingMin = minMaxDto.getMinReportingPeriod() ;
        String reportingMax = minMaxDto.getMaxReportingPeirod();

        minReportingPeriod.add(reportingMin);
        maxReportingPeriod.add(reportingMax);

        return Arrays.asList( new ArrayList<>(columns), missingColumns, ignoredColumns, duplicatedColumns,minReportingPeriod ,maxReportingPeriod,HeadersCols);
    }



    public MinMaxDto  getMinMaxReportingPeriodFromSinglePolicyFile(String path) {

        JavaRDD<String> data = sc.textFile(path);
        String header = data.first();
        List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));

        JavaRDD<String> data_without_header = data.filter(line -> !line.equalsIgnoreCase(header) && !line.isEmpty());
        JavaRDD<String[]> data_splitted = data_without_header.map(x -> x.toLowerCase().trim().split(";",-1));

        String variableTime ;
        if (names.contains(Headers.REPORTING_MONTH)) {
            variableTime = Headers.REPORTING_MONTH ;
        }
        else if (names.contains(Headers.REPORTING_YEAR)) {
            variableTime = Headers.REPORTING_YEAR; }
        else {
            variableTime =Headers.REPORTING_QUARTER ;
        }
        if (variableTime.isEmpty()) {

            return  null ;
        }else {
            JavaRDD<String> rddTime = data_splitted.map(s -> {
                if (names.indexOf(variableTime) != -1) return s[names.indexOf(variableTime)];
                return null;
            }).filter(Objects::nonNull);
            return MinAndMaxReportingPeriodFromRdd (rddTime);
        }
    }
    private MinMaxDto MinAndMaxReportingPeriodFromRdd (JavaRDD<String> rdd) {
        if (!rdd.isEmpty()) {
            rdd.cache();
            Object ordering = Ordering.natural();

            final Comparator<String> cmp = (Comparator<String>) ordering;
            String min = rdd.min(cmp);
            String max = rdd.max(cmp);
            return new MinMaxDto(min,max);
        } else {
            return (null);
        }
    }


}