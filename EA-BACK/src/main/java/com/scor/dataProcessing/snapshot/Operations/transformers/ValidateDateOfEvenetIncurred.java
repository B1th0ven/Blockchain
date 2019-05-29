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
public class ValidateDateOfEvenetIncurred implements Serializable {

    @Autowired
    RDDTransformation rddTransformation;


    public JavaRDD<String> rddSameEventSameDate(JavaRDD<String> rdd, List<String> names, Integer reportPeriodIndex
            , String reportingMax, String Annual_snapshot_extraction_timing) {


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
            String dateOfLastEventIncurred = tupleValues.get(tupleValues.size() - 1)[names.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)];

            for (String[] row_arr : tupleValues) {
                if (StringUtils.isBlank(row_arr[names.indexOf(Headers.DATE_OF_EVENT_INCURRED)])) {

                    if (tupleValues.size() > 1) {
                        row_arr[names.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = dateOfLastEventIncurred;

                    } else {
                        String middlePeriod = null;

                        if (names.contains(Headers.REPORTING_YEAR)) {
                            String reportingYear = row_arr[names.indexOf(Headers.REPORTING_YEAR)];
                            middlePeriod = middleYear(Annual_snapshot_extraction_timing, reportingYear);

                        }
                        if (names.contains(Headers.REPORTING_MONTH)) {
                            middlePeriod = middleMonth(row_arr, names);

                        }
                        if (names.contains(Headers.REPORTING_QUARTER)) {

                            middlePeriod = middleQuarter(row_arr, names);

                        }

                        row_arr[names.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = middlePeriod;
                        String coverEndDate = coverEndDate(row_arr, names);

                        if (StringUtils.isNotBlank(coverEndDate) && names.contains(Headers.DATE_OF_EVENT_INCURRED)) {
                            LocalDate cover_end_Date = LocalDate.parse(coverEndDate,
                                    DateTimeFormatter.ofPattern
                                            ("dd/MM/yyyy", Locale.ENGLISH));
                            LocalDate dateOfEventIncurred = LocalDate.parse(row_arr[names.indexOf(Headers.DATE_OF_EVENT_INCURRED)],
                                    DateTimeFormatter.ofPattern
                                            ("dd/MM/yyyy", Locale.ENGLISH));
                            if (cover_end_Date.isAfter(dateOfEventIncurred)) {
                                row_arr[names.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = row_arr[names.indexOf(Headers.DATE_OF_EVENT_INCURRED)];
                            } else {

                                row_arr[names.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = coverEndDate;
                            }
                        }


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

        String coverEndDate = "";
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
            coverEndDate = "";
        }

        return coverEndDate;
    }

    public String middleYear(String Annual_snapshot_extraction_timing, String reportingYear) {

        String startPeriod = null;

        if (StringUtils.isBlank(Annual_snapshot_extraction_timing)) {
            startPeriod = "01/01/" + reportingYear;

        } else {
            startPeriod = Annual_snapshot_extraction_timing + "/" + (Integer.parseInt(reportingYear) - 1);


        }

        String datee = LocalDate.parse(startPeriod,
                DateTimeFormatter.ofPattern
                        ("dd/MM/yyyy", Locale.ENGLISH)).plusMonths(6).toString();

        LocalDate fix = LocalDate.parse(datee,
                DateTimeFormatter.ofPattern
                        ("yyyy-MM-dd", Locale.ENGLISH));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return formatter.format(fix);


    }


    public String middleMonth(String[] row_arr, List<String> headers) {
        String startPeriod = null;
        String middle = null;

        if (row_arr[headers.indexOf(Headers.REPORTING_MONTH)].contains("m")) {
            String year = row_arr[headers.indexOf(Headers.REPORTING_MONTH)].split("m")[0];
            String Month = row_arr[headers.indexOf(Headers.REPORTING_MONTH)].split("m")[1];
            if (Integer.parseInt(Month) < 10) {

                startPeriod = "01/0" + Month + "/" + year;

            } else {
                startPeriod = "01/" + Month + "/" + year;


            }
            LocalDate dateOfStart = LocalDate.parse(startPeriod,
                    DateTimeFormatter.ofPattern
                            ("dd/MM/yyyy", Locale.ENGLISH)).plusMonths(6);
            LocalDate fix = LocalDate.parse(dateOfStart.toString(),
                    DateTimeFormatter.ofPattern
                            ("yyyy-MM-dd", Locale.ENGLISH));


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            middle = formatter.format(fix);
        }
        return middle;
    }


    public String middleQuarter(String[] row_arr, List<String> headers) {

        String middlePeriod = null;


        if (row_arr[headers.indexOf(Headers.REPORTING_QUARTER)].contains("q")) {
            String year = row_arr[headers.indexOf(Headers.REPORTING_QUARTER)].split("q")[0];
            String Month = row_arr[headers.indexOf(Headers.REPORTING_QUARTER)].split("q")[1];
            switch (Month) {
                case "1":
                    middlePeriod = "15/02/" + year;
                    break;
                case "2":

                    middlePeriod = "15/05/" + year;

                    break;
                case "3":

                    middlePeriod = "15/08" + year;

                    break;
                case "4":

                    middlePeriod = "15/11" + year;

                    break;
                default:
                    break;

            }

        }
        return middlePeriod;
    }

}

