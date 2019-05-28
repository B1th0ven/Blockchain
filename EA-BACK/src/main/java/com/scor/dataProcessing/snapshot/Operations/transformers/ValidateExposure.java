package com.scor.dataProcessing.snapshot.Operations.transformers;


import com.scor.dataProcessing.Helpers.Headers;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.Function;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ValidateExposure implements Function<String, String> {
    List<String> headers;
   // Map<String, String> MapOfdateend;
    String Annual_snapshot_extraction_timing;


    public ValidateExposure(List<String> headers, String Annual_snapshot_extraction_timing) {
        this.headers = headers;
        this.Annual_snapshot_extraction_timing = Annual_snapshot_extraction_timing;
    }


    @Override
    public String call(String row_arr1) throws Exception {

        String[] row_arr = (row_arr1.toLowerCase().split(";", -1));


        if (headers.contains(Headers.EXPOSURE_OR_EVENT)) {


        if (row_arr[headers.indexOf(Headers.EXPOSURE_OR_EVENT)].equalsIgnoreCase("exposure")) {

                if ( headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION) != -1 &&
                        "withdrawn".equalsIgnoreCase(row_arr[headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION)])) {

                    row_arr[headers.indexOf(Headers.TYPE_OF_EVENT)] = "Withdrawal";
                    row_arr[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)] = row_arr[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)];
                    row_arr[headers.indexOf(Headers.EXPOSURE_OR_EVENT)] = "exposure + event";

                }

            }
        }


        return (String.join(";", row_arr));
    }




}
