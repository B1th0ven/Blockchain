package com.scor.dataProcessing.snapshot.Operations.transformers;

import com.scor.dataProcessing.Helpers.Headers;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.Function;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ValidateDateOfBegin implements  Function<String, String> {


    List<String> headers;
    String reportingMin;
    String portfolio_inception_date;
    String firstSnapshot;
    String mindateOfBegin ;

    String annualSnapshotExtractionTiming;
    String variableTime ;
    public ValidateDateOfBegin( String variableTime ,  List<String> headers, String reportingMin, String portfolio_inception_date,
                               String firstSnapshot, String mindateOfBegin,String annualSnapshotExtractionTiming) {

        this.headers = headers;
        this.reportingMin = reportingMin;
        this.portfolio_inception_date = portfolio_inception_date;
        this.firstSnapshot = firstSnapshot;
        this.mindateOfBegin = mindateOfBegin  ;
        this.annualSnapshotExtractionTiming = annualSnapshotExtractionTiming;
        this.variableTime = variableTime ;
    }


    @Override
    public String call(String row) throws Exception {

        String observation_min = "";
        String[] row_arr = (row.toLowerCase().split(";", -1));
        String startObservationMinDateString = "";
        String endObservationMin = "";

        String startReportingMin = "";
        if (row_arr[headers.indexOf(Headers.EXPOSURE_OR_EVENT)].equalsIgnoreCase("exposure")) {




            if (variableTime.contains(Headers.REPORTING_YEAR)) {

               observation_min = row_arr[headers.indexOf("observation_min")] ;
               startReportingMin = "01/01/" + reportingMin;
                endObservationMin = "31/12/" + observation_min;
                startObservationMinDateString = "01/01/" + observation_min;


            }
            if (variableTime.contains(Headers.REPORTING_MONTH)) {
               // observation_min = observation_minList.get(code + "reporting_month");
                observation_min = row_arr[headers.indexOf("observation_min")] ;
                if (observation_min.contains("m")) {

                    String yearReportingMin = reportingMin.split("m")[0];
                    String MonthReportingMin = reportingMin.split("m")[1];
                    String yearOb = observation_min.split("m")[0];
                    String MonthOb = observation_min.split("m")[1];



                    if (Integer.parseInt(MonthOb) > 9) {
                        endObservationMin = "31/" + MonthOb + "/" + yearOb;
                        startObservationMinDateString = "01/" + MonthOb + "/" + yearOb;
                    } else {
                        endObservationMin = "31/0" + MonthOb + "/" + yearOb;
                        startObservationMinDateString = "01/0" + MonthOb + "/" + yearOb;
                    }

                    if (Integer.parseInt(MonthReportingMin) > 9) {
                        startReportingMin = "01/" + MonthReportingMin + "/" + yearReportingMin;

                    } else {
                        startReportingMin = "01/0" + MonthReportingMin + "/" + yearReportingMin;

                    }


                }

            }
            if (variableTime.contains(Headers.REPORTING_QUARTER)) {

                observation_min = row_arr[headers.indexOf("observation_min")] ;
                if (observation_min.contains("q")) {
                    String yearOb = observation_min.split("q")[0];
                    String quarterOb = observation_min.split("q")[1];
                    String yearReportingMin = reportingMin.split("q")[0];
                    String quarterReportingMin = reportingMin.split("q")[1];


                    switch (quarterReportingMin) {
                        case "1":
                            startReportingMin = "01/01/" + yearReportingMin;

                            break;
                        case "2":
                            startReportingMin = "01/04/" + yearReportingMin;


                            break;
                        case "3":
                            startReportingMin = "01/07/" + yearReportingMin;

                            break;
                        case "4":
                            startReportingMin = "01/10/" + yearReportingMin;

                            break;
                        default:
                            break;
                    }


                    switch (quarterOb) {
                        case "1":
                            endObservationMin = "31/03/" + yearOb;
                            startObservationMinDateString = "01/01/" + yearOb;
                            break;
                        case "2":
                            endObservationMin = "31/06/" + yearOb;
                            startObservationMinDateString = "01/04/" + yearOb;
                            break;
                        case "3":
                            endObservationMin = "31/09/" + yearOb;
                            startObservationMinDateString = "01/07/" + yearOb;
                            break;
                        case "4":
                            endObservationMin = "31/12/" + yearOb;
                            startObservationMinDateString = "01/10/" + yearOb;
                            break;
                        default:
                            break;
                    }
                }

            }

// strat of traitment
            if (headers.contains(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)) {

                if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)])) {
                    row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = mindateOfBegin;
                } else {
                    if (reportingMin.compareToIgnoreCase(observation_min) < 0) {

                        if (!StringUtils.isBlank(row_arr[headers.indexOf(Headers.DATE_OF_COMMENCEMENT)])) {
                            LocalDate commencement = LocalDate.parse(row_arr[headers.indexOf(Headers.DATE_OF_COMMENCEMENT)],
                                    DateTimeFormatter.ofPattern
                                            ("dd/MM/yyyy", Locale.ENGLISH));
                            LocalDate endObservationMinDate = LocalDate.parse(endObservationMin,
                                    DateTimeFormatter.ofPattern
                                            ("dd/MM/yyyy", Locale.ENGLISH));
                            LocalDate startObservationMinDate = LocalDate.parse(startObservationMinDateString,
                                    DateTimeFormatter.ofPattern
                                            ("dd/MM/yyyy", Locale.ENGLISH));

                            if (commencement.isAfter(startObservationMinDate) && commencement.isBefore(endObservationMinDate)) {
                                row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = row_arr[headers.indexOf(Headers.DATE_OF_COMMENCEMENT)];
                            }
                        } else {
                            if (variableTime.contains(Headers.REPORTING_YEAR)) {
                                if (StringUtils.isNotBlank(annualSnapshotExtractionTiming) &&   annualSnapshotExtractionTiming.matches("^[0-9]{2}/[0-9]{2}$") &&  !annualSnapshotExtractionTiming.equalsIgnoreCase("31/12")) {
                                    String var = annualSnapshotExtractionTiming + "/" + observation_min;
                                    LocalDate varDate = LocalDate.parse(var,
                                            DateTimeFormatter.ofPattern
                                                    ("dd/MM/yyyy", Locale.ENGLISH));

                                    row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = varDate.minusMonths(6).plusDays(1).toString();

                                } else {
                                    row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = "01/07/" + observation_min;
                                }

                            } else if (variableTime.contains(Headers.REPORTING_MONTH)) {
                                //observation_min = observation_minList.get(code + "reporting_month");
                                observation_min= row_arr[headers.indexOf("observation_min")] ;
                                if (observation_min.contains("m")) {
                                    String yearOb = observation_min.split("m")[0];
                                    String monthOb = observation_min.split("m")[1];
                                    if (Integer.parseInt(monthOb) < 10) {
                                        row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = "16/0" + monthOb + "/" + yearOb;
                                    } else {
                                        row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = "16/" + monthOb + "/" + yearOb;
                                    }

                                }
                            } else {

                                    observation_min= row_arr[headers.indexOf("observation_min")] ;
                                    String yearOb = observation_min.split("q")[0];
                                    String Month = observation_min.split("q")[1];
                                    switch (Month) {
                                        case "1":
                                            Month = "02";
                                            break;
                                        case "2":
                                            Month = "05";
                                            break;
                                        case "3":
                                            Month = "07";
                                            break;
                                        case "4":
                                            Month = "11";
                                            break;
                                        default:
                                            Month = "01";
                                    }

                                    row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = "16/" + Month + "/" + yearOb;


                            }
                        }


                    } else if (observation_min.equalsIgnoreCase(reportingMin) &&
                            (firstSnapshot.equalsIgnoreCase("Inforce portfolio"))) {
                        LocalDate startReportingMinDate = LocalDate.parse(startReportingMin,
                                DateTimeFormatter.ofPattern
                                        ("dd/MM/yyyy", Locale.ENGLISH));

                        LocalDate portfolioInceptionDate ;
                        if ( StringUtils.isNotBlank(portfolio_inception_date)) {
                            portfolioInceptionDate   = LocalDate.parse(portfolio_inception_date);

                        }

                        else {
                            portfolioInceptionDate =startReportingMinDate ;


                        }



                        if (startReportingMinDate.isAfter(portfolioInceptionDate)) {
                            row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = startReportingMin;
                        } else {

                            row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = portfolioInceptionDate.toString();
                        }


                    }
                    if (observation_min.equalsIgnoreCase(reportingMin) &&
                            (firstSnapshot.equalsIgnoreCase("New portfolio"))) {
                        LocalDate startReportingDate = LocalDate.parse(startReportingMin,
                                DateTimeFormatter.ofPattern
                                        ("dd/MM/yyyy", Locale.ENGLISH));

                        LocalDate portfolioDate ;
                        if ( StringUtils.isNotBlank(portfolio_inception_date)) {
                            portfolioDate   = LocalDate.parse(portfolio_inception_date);

                        }

                        else {

                            portfolioDate = startReportingDate ;

                        }



                        if (portfolioDate.isBefore(startReportingDate) || portfolioDate.isEqual(startReportingDate)) {
                            if (variableTime.contains(Headers.REPORTING_YEAR)) {
                                if ( StringUtils.isNotBlank(annualSnapshotExtractionTiming) &&   annualSnapshotExtractionTiming.matches("^[0-9]{2}/[0-9]{2}$")    && !annualSnapshotExtractionTiming.equalsIgnoreCase("31/12")) {
                                    String var = annualSnapshotExtractionTiming + "/" + observation_min;
                                    LocalDate varDate = LocalDate.parse(var,
                                            DateTimeFormatter.ofPattern
                                                    ("dd/MM/yyyy", Locale.ENGLISH));

                                    row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = varDate.minusMonths(6).plusDays(1).toString();

                                } else {
                                    row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = "01/07/" + observation_min;
                                }

                            } else if (variableTime.contains(Headers.REPORTING_MONTH)) {
                               // observation_min = observation_minList.get(code + "reporting_month");
                                observation_min= row_arr[headers.indexOf("observation_min")] ;

                                if (row_arr[variableTime.indexOf(Headers.REPORTING_MONTH)].contains("m")) {
                                    String yearOb = observation_min.split("m")[0];
                                    String monthOb = observation_min.split("m")[1];
                                    if (Integer.parseInt(monthOb) < 10) {
                                        row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = "16/0" + monthOb + "/" + yearOb;
                                    } else {
                                        row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = "16/" + monthOb + "/" + yearOb;
                                    }

                                }
                            } else {
                                if (row_arr[variableTime.indexOf(Headers.REPORTING_QUARTER)].contains("q")) {

                                    observation_min= row_arr[headers.indexOf("observation_min")] ;
                                    String yearOb = observation_min.split("q")[0];
                                    String Month = observation_min.split("q")[1];
                                    switch (Month) {
                                        case "1":
                                            Month = "02";
                                            break;
                                        case "2":
                                            Month = "05";
                                            break;
                                        case "3":
                                            Month = "07";
                                            break;
                                        case "4":
                                            Month = "11";
                                            break;
                                        default:
                                            Month = "01";
                                    }

                                    row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = "16/" + Month + yearOb;
                                }

                            }
                        }
                    } else {
                        LocalDate startReportingDate = LocalDate.parse(endObservationMin,
                                DateTimeFormatter.ofPattern
                                        ("dd/MM/yyyy", Locale.ENGLISH));
                        LocalDate portfolio ;
                        if (StringUtils.isNotBlank(portfolio_inception_date) ) {
                            portfolio   = LocalDate.parse(portfolio_inception_date);


                        }

                        else {

                            portfolio = startReportingDate ;
                        }


                        if (startReportingDate.isAfter(portfolio)) {
                            ArrayList<Date> dates = new ArrayList<Date>();
                            Date l = convertToDateViaSqlDate(startReportingDate);
                            Date m = convertToDateViaSqlDate(portfolio);
                            dates.add(l);
                            dates.add(m);
                            BigInteger total = BigInteger.ZERO;

                            for (Date date : dates) {
                                total = total.add(BigInteger.valueOf(date.getTime()));
                            }
                            BigInteger averageMillis = total.divide(BigInteger.valueOf(dates.size()));
                            Date averageDate = new Date(averageMillis.longValue());
                            String avg = averageDate.toString();
                            LocalDateTime conv = LocalDateTime.ofInstant(averageDate.toInstant(), ZoneId.systemDefault());
                            String convDate = conv.toString().split("T")[0];
                            LocalDate fix = LocalDate.parse(convDate,
                                    DateTimeFormatter.ofPattern
                                            ("yyyy-MM-dd", Locale.ENGLISH));


                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                            String test = formatter.format(fix);

                            if (!avg.equalsIgnoreCase(null)) {
                                row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = test;
                            }
                        } else {
                            row_arr[headers.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)] = portfolio.toString();

                        }
                    }
                }

            }

        }
            return (String.join(";", row_arr));

        }


    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

}
