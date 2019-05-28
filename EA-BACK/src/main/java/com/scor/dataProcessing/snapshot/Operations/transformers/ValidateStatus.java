package com.scor.dataProcessing.snapshot.Operations.transformers;


import com.scor.dataProcessing.Helpers.Headers;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.Function;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ValidateStatus implements Function<String, String> {

    //Map<String, String> mapdateOfEndCurrentCondition;
    List<String> headers;
    String reportingMax;
    String variableTime ;

    public ValidateStatus(String variableTime, List<String> headers, String reportingMax) {

      //  this.mapdateOfEndCurrentCondition = mapdateOfEndCurrentCondition;
        this.headers = headers;
        this.reportingMax = reportingMax;
        this.variableTime =  variableTime ;
    }


    @Override
    public String call(String row_arr1) throws Exception {
        String policyID = null;
        String lifeID = null;
        String benefitID;
        String code ;
        String[] row_arr = (row_arr1.toLowerCase().split(";", -1));
        if (row_arr[headers.indexOf(Headers.EXPOSURE_OR_EVENT)].equalsIgnoreCase("exposure")) {
            if (headers.indexOf(Headers.POLICY_ID) != -1) {
                policyID = row_arr[headers.indexOf(Headers.POLICY_ID)];
            }
            if (headers.indexOf(Headers.LIFE_ID) != -1) {
                lifeID = row_arr[headers.indexOf(Headers.LIFE_ID)];
            }

            if (headers.indexOf(Headers.BENEFIT_ID) != -1) {
                benefitID = row_arr[headers.indexOf(Headers.BENEFIT_ID)];
                code = policyID + lifeID + benefitID;
            } else {
                code = policyID + lifeID;
            }

            String coverEndDate = coverEndDate ( row_arr )  ; 

            

            String observationmax = row_arr[headers.indexOf("observation_max")];

            
            String event = row_arr[headers.indexOf(Headers.TYPE_OF_EVENT)];
            if (headers.contains(Headers.STATUS_BEGIN_CURRENT_CONDITION)) {
                row_arr[headers.indexOf(Headers.STATUS_BEGIN_CURRENT_CONDITION)] = "active";
            }
            if (headers.contains(Headers.STATUS_END_CURRENT_CONDITION)) {

                if ("11".equalsIgnoreCase(code)) {
                    if (event.equalsIgnoreCase("withdrawal")) {

                        row_arr[headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = "withdrawn";
                    } else if (event.equalsIgnoreCase("death")) {

                        row_arr[headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = "dead";
                    } else {
                        row_arr[headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = "claimant";
                    }

                } else if (row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)].equalsIgnoreCase(coverEndDate)) {
                    row_arr[headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = "expiry";
                } else if (observationmax.compareToIgnoreCase(reportingMax) < 0) {
                    row_arr[headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = "withdrawn";
                } else {
                    row_arr[headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION)] = "active";
                }
            }

        }
        return (String.join(";", row_arr));
    }

    public String coverEndDate ( String [] row_arr ) {

        String coverEndDate = "vide" ; 
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
        
        return coverEndDate ; 
        }
        }
        
        

