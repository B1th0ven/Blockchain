package com.scor.dataProcessing.snapshot.Operations.Utils;

import com.google.common.collect.Lists;
import com.scor.dataProcessing.Helpers.Grouping;
import com.scor.dataProcessing.Helpers.GroupingV2;
import com.scor.dataProcessing.Helpers.GroupingV3;
import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.sparkConnection.Connection;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Service
public class RDDTransformation implements Serializable {


    static final JavaSparkContext sc = Connection.getContext();
    static final SparkSession sparkSession = Connection.getSession();

    public JavaPairRDD<String, List<List<String>>> missingValuePair(JavaRDD<String> rdd, List<String> names, Integer reportPeriodIndex) {
        List<String> unchagebaleColumns = new ArrayList<>();
        unchagebaleColumns.add(Headers.STATUS_BEGIN_CURRENT_CONDITION);
        unchagebaleColumns.add(Headers.STATUS_END_CURRENT_CONDITION);
        unchagebaleColumns.add(Headers.DATE_OF_BEGIN_CURRENT_CONDITION);
        unchagebaleColumns.add(Headers.DATE_OF_END_CURRENT_CONDITION);

        JavaPairRDD<String, Iterable<String[]>> missingValueRegroup = rdd.mapToPair(new GroupingV3(names)).groupByKey();

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
            String[] old_row = tupleValues.get(0);
            resultRows.add(Arrays.asList(old_row));
            for (int i = 1; i < tupleValues.size(); i++) {
                String[] new_row = tupleValues.get(i);
                for (int j = 0; j < new_row.length; j++) {
                    if (StringUtils.isBlank(new_row[j]) && !unchagebaleColumns.contains(names.get(j))) {
                        new_row[j] = old_row[j];
                    }
                }
                old_row = new_row;
                resultRows.add(Arrays.asList(old_row));
            }
            return resultRows;
        });
        return noMoreMissingValues;
    }


    public JavaPairRDD<String, List<List<String>>> incrementedIdPairWithStatusAndReporting(JavaRDD<String> rdd, List<String> names, Integer reportPeriodIndex, String variableTime, Integer Status_begin_current_condition_index) {
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

        JavaPairRDD<String, List<List<String>>> sortedDataIncrementedId = sortedData.mapValues(line -> {
            String[] oldline = line.get(0);
            List<String> oldLineToList = new ArrayList<>(Arrays.asList(oldline));
            List<List<String>> result = new ArrayList<>();
            oldLineToList.add("1");
            LocalDate oldDate = null;
            String oldDateString = oldLineToList.get(reportPeriodIndex);

            int indexIncrementedId = oldLineToList.size() - 1;

            result.add(oldLineToList);
            for (int i = 1; i < line.size(); i++) {
                List<String> lineToList = new ArrayList<>(Arrays.asList(line.get(i)));
                LocalDate expectedDate = null;
                String expectedDateString = "";

                if (variableTime.equalsIgnoreCase(Headers.REPORTING_MONTH)) {
                    Integer variableYear = Integer.parseInt(oldDateString.split("m")[0]);
                    Integer variableMonth = Integer.parseInt(oldDateString.split("m")[1]);
                    oldDate = LocalDate.of(variableYear, variableMonth, 1);
                    expectedDate = oldDate.plusMonths(1);
                    expectedDateString = expectedDate.getYear() + "m" + expectedDate.getMonthValue();
                } else if (variableTime.equalsIgnoreCase(Headers.REPORTING_YEAR)) {
                    Integer variableYear = Integer.parseInt(oldDateString);
                    oldDate = LocalDate.of(variableYear, 1, 1);
                    expectedDate = oldDate.plusYears(1);
                    expectedDateString = expectedDate.getYear() + "";
                } else {
                    Integer variableYear = Integer.parseInt(oldDateString.toLowerCase().split("q")[0]);
                    Integer variableQuarter = Integer.parseInt(oldDateString.toLowerCase().split("q")[1]);
                    oldDate = LocalDate.of(variableYear, variableQuarter * 3, 1);
                    expectedDate = oldDate.plusMonths(3);
                    expectedDateString = expectedDate.getYear() + "q" + expectedDate.getMonthValue();
                }


                if (!lineToList.get(Status_begin_current_condition_index).equals(oldLineToList.get(Status_begin_current_condition_index)) | !lineToList.get(reportPeriodIndex).equalsIgnoreCase(expectedDateString) |  !lineToList.get(reportPeriodIndex).equalsIgnoreCase(oldDate.toString()))
                    lineToList.add(Integer.toString((Integer.parseInt(oldLineToList.get(indexIncrementedId)) + 1)));
                else {
                    lineToList.add(oldLineToList.get(indexIncrementedId));
                }
                oldLineToList = lineToList;
                oldDateString = lineToList.get(reportPeriodIndex);
                result.add(lineToList);
            }

            return result;
        });
        return sortedDataIncrementedId;
    }


    public JavaPairRDD<String, List<List<String>>> incrementedIdPairWithStatusOnly(JavaRDD<String> rdd, List<String> names, Integer reportPeriodIndex, String variableTime, Integer Status_begin_current_condition_index) {
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

        JavaPairRDD<String, List<List<String>>> sortedDataIncrementedId = sortedData.mapValues(line -> {
            String[] oldline = line.get(0);
            List<String> oldLineToList = new ArrayList<>(Arrays.asList(oldline));
            List<List<String>> result = new ArrayList<>();
            oldLineToList.add("1");


            int indexIncrementedId = oldLineToList.size() - 1;

            result.add(oldLineToList);
            for (int i = 1; i < line.size(); i++) {
                List<String> lineToList = new ArrayList<>(Arrays.asList(line.get(i)));


                if (!lineToList.get(Status_begin_current_condition_index).equals(oldLineToList.get(Status_begin_current_condition_index)) )
                    lineToList.add(Integer.toString((Integer.parseInt(oldLineToList.get(indexIncrementedId)) + 1)));
                else {
                    lineToList.add(oldLineToList.get(indexIncrementedId));
                }
                oldLineToList = lineToList;

                result.add(lineToList);
            }

            return result;
        });
        return sortedDataIncrementedId;
    }


    public JavaRDD<String> groupByObservationMinMax(JavaRDD<String> pipelineSecondStage, List<String> names, Integer reportPeriodIndex){
        JavaPairRDD<String,Iterable<String>> secondRegroup =pipelineSecondStage.mapToPair(new GroupingV2(names,reportPeriodIndex)).groupByKey();
        JavaRDD<String> resultAfterSecondGroup = secondRegroup.map( stringIterableTuple2 -> {
                    Iterator<String> it = stringIterableTuple2._2.iterator() ;
                    String tempValue = it.next();
                    String minValue = tempValue;
                    String maxValue = tempValue;
                    while(it.hasNext()){
                        tempValue = it.next();
                        if(minValue.compareTo(tempValue) > 0) minValue  = tempValue;
                        if(maxValue.compareTo(tempValue) < 0) maxValue  = tempValue;

                    }
                    return stringIterableTuple2._1+minValue +";"+ maxValue;
                }
        );
        return resultAfterSecondGroup;
    }


    public JavaRDD<String> getJavaRDDBack(JavaPairRDD<String, List<List<String>>> pairRDD){
        JavaRDD<List<String>> pairRDDregrouppedMissingValues = pairRDD.map( element -> {
            List<String> allStrings = new ArrayList<>();

            for (List<String> stringList : element._2){
                stringList.toArray();

                allStrings.add(String.join(";", stringList.toArray(stringList.toArray(new String[stringList.size()]))));
            }
            return allStrings;
        });
        JavaRDD<String> noMissingValueRDD = pairRDDregrouppedMissingValues.flatMap(new FlatMapFunction<List<String>,String>() {
            public Iterator<String> call(List<String> li) throws Exception {
                return li.iterator();
            }
        });

        return noMissingValueRDD ;
    }
}
