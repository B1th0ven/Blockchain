package com.scor.dataProcessing.snapshot.Operations.transformers;

import com.scor.dataProcessing.Helpers.Headers;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.Function;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class ValidateDateOfEndVersion2 implements Function<String, String> {


    List<String> headers;
    String reportingMax;
    String maxdateOfEndCurrentCondition;
    String annualSnapshotExtractionTiming;

    String variableTime;

    public ValidateDateOfEndVersion2(String variableTime, List<String> headers, String reportingMax, String maxdateOfEndCurrentCondition, String annualSnapshotExtractionTiming) {


        this.headers = headers;
        this.reportingMax = reportingMax;
        this.maxdateOfEndCurrentCondition = maxdateOfEndCurrentCondition;
        this.annualSnapshotExtractionTiming = annualSnapshotExtractionTiming;
        this.variableTime = variableTime;
    }

    @Override
    public String call(String row_arr1) {
        String policyID = null;
        String lifeID = null;
        String benefitId;
        String code;
        String[] row_arr = (row_arr1.toLowerCase().split(";", -1));
        if (("exposure").equalsIgnoreCase(row_arr[headers.indexOf(Headers.EXPOSURE_OR_EVENT)])) {
            if (headers.indexOf(Headers.POLICY_ID) != -1) {
                policyID = row_arr[headers.indexOf(Headers.POLICY_ID)];
            }
            if (headers.indexOf(Headers.LIFE_ID) != -1) {
                lifeID = row_arr[headers.indexOf(Headers.LIFE_ID)];
            }
            if (headers.indexOf(Headers.BENEFIT_ID) != -1) {
                benefitId = row_arr[headers.indexOf(Headers.BENEFIT_ID)];
                code = lifeID + policyID + benefitId;
            } else {
                code = lifeID + policyID;
            }

            String coverEndDate = coverEndDate(row_arr) ; 

            


// start
            if (headers.contains(Headers.DATE_OF_END_CURRENT_CONDITION)) {
                if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)])) {

                    row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)] = maxdateOfEndCurrentCondition;
                    ;
                }
                else {

                    if (StringUtils.isNotBlank(coverEndDate)) {


                        LocalDate cover_end_Date = LocalDate.parse(coverEndDate,
                                DateTimeFormatter.ofPattern
                                        ("dd/MM/yyyy", Locale.ENGLISH));


                        if (variableTime.contains(Headers.REPORTING_YEAR)) {
                            coveryearEnd(row_arr, cover_end_Date, coverEndDate);

                        } else if (variableTime.contains(Headers.REPORTING_MONTH)) {
                            covermonthend(row_arr, cover_end_Date, coverEndDate);

                        } else if (variableTime.contains(Headers.REPORTING_QUARTER)) {
                            coverquarterend(row_arr, cover_end_Date, coverEndDate);

                        }
                    }


                    else {

                        if (variableTime.contains(Headers.REPORTING_YEAR)) {
                            nocoveryearend(row_arr);

                        } else if (variableTime.contains(Headers.REPORTING_MONTH)) {

                            nocovermonthend(row_arr);

                        } else if (variableTime.contains(Headers.REPORTING_QUARTER)) {

                            nocoverquarterend(row_arr);
                        }
                    }


                }


            }
        }
        return (String.join(";", row_arr));
    }


    public void coveryearEnd(String[] row_arr, LocalDate cover_end_Date, String coverEndDate) {


        String observationmax = row_arr[headers.indexOf("observation_max")];

        String StartObservationmax = "01/01/" + observationmax;
        String StartObservationmaxPlusOne = "01/01/" + String.valueOf(Integer.parseInt(observationmax) + 1);
        LocalDate StartObservationmaxDate = LocalDate.parse(StartObservationmax,
                DateTimeFormatter.ofPattern
                        ("dd/MM/yyyy", Locale.ENGLISH));
        LocalDate StartObservationmaxDatePlusOne = LocalDate.parse(StartObservationmaxPlusOne,
                DateTimeFormatter.ofPattern
                        ("dd/MM/yyyy", Locale.ENGLISH));

        if (cover_end_Date.isAfter(StartObservationmaxDate) && cover_end_Date.isBefore(StartObservationmaxDatePlusOne)) {
            row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)] = coverEndDate;

        } else {

            nocoveryearend(row_arr);
        }
    }

    public void covermonthend(String[] row_arr, LocalDate cover_end_Date, String coverEndDate) {


        String yearPlusOne;
        String monthPlusOne;
        String observationmax = row_arr[headers.indexOf("observation_max")];
        String year = observationmax.split("m")[0];
        String month = observationmax.split("m")[1];

        if (month.equalsIgnoreCase("12")) {
            yearPlusOne = String.valueOf(Integer.parseInt(year) + 1);
            monthPlusOne = "1";
        } else {
            monthPlusOne = String.valueOf(Integer.parseInt(month) + 1);
            yearPlusOne = year;
        }

        String startOb = null;
        if (Integer.parseInt(month) > 9) {
            startOb = "01/" + month + "/" + year;
        } else {
            startOb = "01/0" + month + "/" + year;
        }

        String startObPlusOne = null;
        if (Integer.parseInt(monthPlusOne) > 9) {
            startObPlusOne = "01/" + monthPlusOne + "/" + yearPlusOne;
        } else {
            startObPlusOne = "01/0" + monthPlusOne + "/" + yearPlusOne;
        }

        LocalDate Date = LocalDate.parse(startOb,
                DateTimeFormatter.ofPattern
                        ("dd/MM/yyyy", Locale.ENGLISH));

        LocalDate DatePlusOne = LocalDate.parse(startObPlusOne,
                DateTimeFormatter.ofPattern
                        ("dd/MM/yyyy", Locale.ENGLISH));

        if (cover_end_Date.isAfter(Date) && cover_end_Date.isBefore(DatePlusOne)) {
            row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)] = coverEndDate;
        } else {

            nocovermonthend(row_arr);
        }
    }


    public void coverquarterend(String[] row_arr, LocalDate cover_end_Date, String coverEndDate) {


        String observationmax = row_arr[headers.indexOf("observation_max")];
        String yearOb = observationmax.split("q")[0];
        String endObservationMin = null;
        String startObservationMin = null;
        String yearObPlusOne;
        String QuarterObPlusOne;
        String startObservationMinPlusOne1 = null;
        String QuarterOb = observationmax.split("q")[1];
        if (QuarterOb.equalsIgnoreCase("4")) {
            QuarterObPlusOne = "1";
            yearObPlusOne = String.valueOf(Integer.parseInt(yearOb) + 1);
        } else {
            QuarterObPlusOne = String.valueOf(Integer.parseInt(QuarterOb) + 1);
            yearObPlusOne = yearOb;
        }

        switch (QuarterOb) {
            case "1":
                endObservationMin = "31/03/" + yearOb;
                startObservationMin = "01/01/" + yearOb;
                break;
            case "2":
                endObservationMin = "31/06/" + yearOb;
                startObservationMin = "01/04/" + yearOb;
                break;
            case "3":
                endObservationMin = "31/09/" + yearOb;
                startObservationMin = "01/07/" + yearOb;
                break;
            case "4":
                endObservationMin = "31/12/" + yearOb;
                startObservationMin = "01/10/" + yearOb;
                break;
            default:
                break;
        }


        switch (QuarterObPlusOne) {
            case "1":
                startObservationMinPlusOne1 = "01/01/" + yearObPlusOne;
                break;
            case "2":
                startObservationMinPlusOne1 = "01/04/" + yearObPlusOne;
                break;
            case "3":
                startObservationMinPlusOne1 = "01/07/" + yearObPlusOne;
                break;
            case "4":
                startObservationMinPlusOne1 = "01/10/" + yearObPlusOne;
                break;
            default:
                break;

        }

        LocalDate Date = LocalDate.parse(startObservationMin,
                DateTimeFormatter.ofPattern
                        ("dd/MM/yyyy", Locale.ENGLISH));
        LocalDate DatePlusOne = LocalDate.parse(startObservationMinPlusOne1,
                DateTimeFormatter.ofPattern
                        ("dd/MM/yyyy", Locale.ENGLISH));
        if (cover_end_Date.isAfter(Date) || cover_end_Date.isBefore(DatePlusOne)) {
            row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)] = coverEndDate;

        } else {

            nocoverquarterend(row_arr);
        }
    }


    public void nocoveryearend(String[] row_arr) {
        String observationmax = row_arr[headers.indexOf("observation_max")];
        if (!StringUtils.isNotBlank(annualSnapshotExtractionTiming) || !annualSnapshotExtractionTiming.equalsIgnoreCase("31/12")) {
            row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)] = "30/06/" + String.valueOf(Integer.parseInt(observationmax) + 1);

        } else {
            String var = annualSnapshotExtractionTiming + "/" + observationmax;
            LocalDate varDate = LocalDate.parse(var,
                    DateTimeFormatter.ofPattern
                            ("dd/MM/yyyy", Locale.ENGLISH)).plusMonths(6);

            LocalDate fix = LocalDate.parse(varDate.toString(),
                    DateTimeFormatter.ofPattern
                            ("yyyy-MM-dd", Locale.ENGLISH));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            String test = formatter.format(fix);

            row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)] = test.toString();


        }

    }


    public void nocovermonthend(String[] row_arr) {
        String yearPlusOne;
        String monthPlusOne;
        String observationmax = row_arr[headers.indexOf("observation_max")];
        String year = observationmax.split("m")[0];
        String month = observationmax.split("m")[1];


        if (month.equalsIgnoreCase("12")) {
            yearPlusOne = String.valueOf(Integer.parseInt(year) + 1);
            monthPlusOne = "01";
        } else {
            monthPlusOne = String.valueOf(Integer.parseInt(month) + 1);
            yearPlusOne = year;
        }

        if (Integer.parseInt(monthPlusOne) < 10) {
            monthPlusOne = "0" + monthPlusOne;
        }

        row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)] = "15/" + monthPlusOne + "/" + yearPlusOne;

    }

    public void nocoverquarterend(String[] row_arr) {

        String observationmax = row_arr[headers.indexOf("observation_max")];
        String yearOb = observationmax.split("q")[0];
        String endObservationMin = null;
        String startObservationMin = null;
        String yearObPlusOne;
        String QuarterObPlusOne;
        String startObservationMinPlusOne1 = null;
        String QuarterOb = observationmax.split("q")[1];
        if (QuarterOb.equalsIgnoreCase("4")) {
            QuarterObPlusOne = "1";
            yearObPlusOne = String.valueOf(Integer.parseInt(yearOb) + 1);
        } else {
            QuarterObPlusOne = String.valueOf(Integer.parseInt(QuarterOb) + 1);
            yearObPlusOne = yearOb;
        }

        switch (QuarterOb) {
            case "1":
                endObservationMin = "31/03/" + yearOb;
                break;
            case "2":
                endObservationMin = "31/06/" + yearOb;
                break;
            case "3":
                endObservationMin = "31/09/" + yearOb;
                break;
            case "4":
                endObservationMin = "31/12/" + yearOb;
                break;
            default:
                break;
        }

        String Date2 = LocalDate.parse(endObservationMin,
                DateTimeFormatter.ofPattern
                        ("dd/MM/yyyy", Locale.ENGLISH)).plusMonths(1).minusDays(15).toString();

        LocalDate fix = LocalDate.parse(Date2,
                DateTimeFormatter.ofPattern
                        ("yyyy-MM-dd", Locale.ENGLISH));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        String test = formatter.format(fix);
        row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)] = test;
    }
public String coverEndDate ( String [] row_arr ) {

String coverEndDate = "" ;
    if (headers.contains((Headers.BENEFIT_END_DATE))) {
        if (StringUtils.isNotBlank(row_arr[headers.indexOf(Headers.BENEFIT_END_DATE)]) ) {

            coverEndDate = row_arr[headers.indexOf(Headers.BENEFIT_END_DATE)];
        }
    }
    else if (headers.contains(Headers.BENEFIT_TERM_YEARS)) {

        if (StringUtils.isNotBlank(row_arr[headers.indexOf(Headers.BENEFIT_TERM_YEARS)])) {
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
        if (StringUtils.isNotBlank(row_arr[headers.indexOf(Headers.BENEFIT_MAX_AGE)])) {
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

return coverEndDate ; 
}
}


