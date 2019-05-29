package com.scor.dataProcessing.snapshot.Operations.checkers;

import com.scor.dataProcessing.Helpers.Headers;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
@Service
public class CheckDate {

    public String checkDate(String portfolio, List<String> allheaders, String reportingMin) {
        List<String> names = new ArrayList<>();
        allheaders.forEach(s -> names.add(s.toLowerCase()));
        String endPeriod = null ;
        String startPeriod = null ;
        boolean isReportingYear = names.contains(Headers.REPORTING_YEAR);
        boolean isReportingQuarter = names.contains(Headers.REPORTING_QUARTER);
        boolean isReportingMonth = names.contains(Headers.REPORTING_MONTH);
        String ReportingPeriod;

        if (isReportingYear) {
            startPeriod = "01/01/" + reportingMin;
            endPeriod = "31/12/" + reportingMin;
            ReportingPeriod = Headers.REPORTING_YEAR;
        } else if (isReportingMonth) {
            ReportingPeriod = Headers.REPORTING_MONTH;
            if (reportingMin.contains("m")) {
                String year = reportingMin.split("m")[0];
                String Month = reportingMin.split("m")[1];
                if (Integer.parseInt(Month) > 9) {
                    startPeriod = "01/" + Month + "/" + year;
                    endPeriod = "31/" + Month + "/" + year;
                } else {
                    startPeriod = "01/0" + Month + "/" + year;
                    endPeriod = "31/0" + Month + "/" + year;
                }

            }
        } else if (isReportingQuarter) {
            ReportingPeriod = Headers.REPORTING_QUARTER;

            if (reportingMin.contains("q")) {
                String year = reportingMin.split("q")[0];
                String Month = reportingMin.split("q")[1];


                switch (Month) {
                    case "1":
                        startPeriod = "01/01/" + year;
                        endPeriod = "31/03/" + year;
                        break;
                    case "2":
                        startPeriod = "01/04/" + year;
                        endPeriod = "31/06/" + year;
                        break;
                    case "3":
                        startPeriod = "01/07/" + year;
                        endPeriod = "31/09/" + year;
                        break;
                    case "4":
                        startPeriod = "01/10/" + year;
                        endPeriod = "31/12/" + year;
                        break;
                    default:
                        break;

                }

            }

        } else {
            return "false";
        }

        LocalDate minReportingPeriod = LocalDate.parse(endPeriod,
                DateTimeFormatter.ofPattern
                        ("dd/MM/yyyy", Locale.ENGLISH));
        LocalDate variableTimeDate;
        if (StringUtils.isNotBlank(portfolio)) {

            variableTimeDate = LocalDate.parse(portfolio);
        } else {
            variableTimeDate = LocalDate.parse(startPeriod, DateTimeFormatter.ofPattern
                    ("dd/MM/yyyy", Locale.ENGLISH));

        }
        String resultat;

        if (variableTimeDate.isAfter(minReportingPeriod)) {
            resultat = "true";
        } else {
            resultat = ReportingPeriod + "  " + reportingMin;
        }
        return resultat;
    }

}
