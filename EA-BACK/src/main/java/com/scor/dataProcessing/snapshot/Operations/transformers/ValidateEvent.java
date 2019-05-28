package com.scor.dataProcessing.snapshot.Operations.transformers;


import com.scor.dataProcessing.Helpers.Headers;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.Function;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ValidateEvent implements Function<String, String> {
    List<String> headers;
    //Map<String, String> MapOfdateend;
    String Annual_snapshot_extraction_timing;


    public ValidateEvent(  List<String> headers, String Annual_snapshot_extraction_timing) {
        this.headers = headers;
       // this.MapOfdateend = MapOfdateend;
        this.Annual_snapshot_extraction_timing = Annual_snapshot_extraction_timing;
    }


    @Override
    public String call(String row_arr1) throws Exception {
        String policyID = null;
        String lifeID = null;
        String startPeriod = null;
        String endPeriod = null;
        String benefitId = null;
        String code = null;
        String reportingYear = null;
        String middlePeriod = null;
        String[] row_arr = (row_arr1.toLowerCase().split(";", -1));
        if (headers.indexOf(Headers.REPORTING_YEAR) != -1) {
            reportingYear = row_arr[headers.indexOf(Headers.REPORTING_YEAR)];
        }
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

        String coverEndDate = coverEndDate(row_arr);



        if (headers.contains(Headers.REPORTING_YEAR)) {
   
            middlePeriod = middleYear(Annual_snapshot_extraction_timing, reportingYear); 

        }
        if (headers.contains(Headers.REPORTING_MONTH)) {
           middlePeriod = middleMonth(row_arr) ;

        }
        if (headers.contains(Headers.REPORTING_QUARTER)) {

            middlePeriod =   middleQuarter(row_arr) ; 

        }


        if (headers.contains(Headers.EXPOSURE_OR_EVENT)) {
            // For “exposure_or_event” = “event” only:
            if (row_arr[headers.indexOf(Headers.EXPOSURE_OR_EVENT)].equalsIgnoreCase("event")) {
                if (headers.contains(Headers.DATE_OF_EVENT_INCURRED)) {
                    if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)])) {

                        row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)];

                    } else {
                       // if (MapOfdateend.containsKey(code)) {
                        //    row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = MapOfdateend.get(code);
                      //  } else {
                            row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = middlePeriod;
                     //   }
                        if (!coverEndDate.equalsIgnoreCase("vide") && headers.contains(Headers.DATE_OF_EVENT_INCURRED)) {
                            LocalDate cover_end_Date = LocalDate.parse(coverEndDate,
                                    DateTimeFormatter.ofPattern
                                            ("dd/MM/yyyy", Locale.ENGLISH));
                            LocalDate dateOfEventIncurred = LocalDate.parse(row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)],
                                    DateTimeFormatter.ofPattern
                                            ("dd/MM/yyyy", Locale.ENGLISH));
                            if (cover_end_Date.isAfter(dateOfEventIncurred)) {
                                row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)];
                            } else {

                                row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = coverEndDate;
                            }
                        }


                    }
                }
            }



        }


        return (String.join(";", row_arr));
    }

public String coverEndDate (String[] row_arr) {
    String coverEndDate = "vide" ; 

    if (headers.contains((Headers.BENEFIT_END_DATE))) {
        if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.BENEFIT_END_DATE)])) {

            coverEndDate = row_arr[headers.indexOf(Headers.BENEFIT_END_DATE)];
        }
    } else if (headers.contains(Headers.BENEFIT_TERM_YEARS)) {

        if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.BENEFIT_TERM_YEARS)])) {
            String dateCom = row_arr[headers.indexOf(Headers.DATE_OF_COMMENCEMENT)];
            LocalDate end1 = LocalDate.parse(dateCom,
                    DateTimeFormatter.ofPattern
                            ("dd/MM/yyyy", Locale.ENGLISH));
            String l = end1.plusYears(Integer.parseInt(row_arr[headers.indexOf("benefit_term_years")])).toString();
            coverEndDate = l;

        }
    } else if (headers.contains(Headers.BENEFIT_MAX_AGE)) {
        if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.BENEFIT_MAX_AGE)])) {
            String dateCom = row_arr[headers.indexOf(Headers.DATE_OF_BIRTH)];
            LocalDate end1 = LocalDate.parse(dateCom,
                    DateTimeFormatter.ofPattern
                            ("dd/MM/yyyy", Locale.ENGLISH));
            String l = end1.plusYears(Integer.parseInt(row_arr[headers.indexOf(Headers.BENEFIT_MAX_AGE)])).toString();
            coverEndDate = l;

        } else {
            coverEndDate = "vide";
        }
    }

return coverEndDate ; 
}


 public String middleYear (String Annual_snapshot_extraction_timing , String reportingYear) {

String startPeriod = null ; 

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


 public String middleMonth (String[] row_arr ) {
   String startPeriod = null ; 
   String middle = null ;

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
       middle =  formatter.format(fix);
    }
return middle ; 
 }


 public String middleQuarter (String[] row_arr ) {

String middlePeriod = null ; 


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
return middlePeriod ; 
}
}
