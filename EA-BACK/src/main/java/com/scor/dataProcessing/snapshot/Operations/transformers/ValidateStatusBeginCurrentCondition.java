package com.scor.dataProcessing.snapshot.Operations.transformers;


import com.scor.dataProcessing.Helpers.Headers;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.function.Function;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ValidateStatusBeginCurrentCondition implements Function<String, String> {

    //Map<String, String> mapdateOfEndCurrentCondition;
    List<String> headers;
    String reportingMax;
    String variableTime;

    public ValidateStatusBeginCurrentCondition(String variableTime, List<String> headers, String reportingMax) {

        //  this.mapdateOfEndCurrentCondition = mapdateOfEndCurrentCondition;
        this.headers = headers;
        this.reportingMax = reportingMax;
        this.variableTime = variableTime;
    }


    @Override
    public String call(String row_arr1) throws Exception {

        String[] row_arr = (row_arr1.toLowerCase().split(";", -1));
        if (row_arr[headers.indexOf(Headers.EXPOSURE_OR_EVENT)].equalsIgnoreCase("exposure")) {


            if (headers.contains(Headers.STATUS_BEGIN_CURRENT_CONDITION)) {
                row_arr[headers.indexOf(Headers.STATUS_BEGIN_CURRENT_CONDITION)] = "active";
            }

        }
        return (String.join(";", row_arr));
    }


}
        
        

