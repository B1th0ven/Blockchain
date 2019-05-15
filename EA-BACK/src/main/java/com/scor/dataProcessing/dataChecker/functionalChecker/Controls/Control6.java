package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control6 implements IRule {
    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;
    String type;

    public Control6(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo, String type){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
        this.type = type;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        String begin_stat = row[headers.indexOf("status_begin_current_condition")].trim();

        if(headers.indexOf("status_end_current_condition") > -1) {
            String end_stat = row[headers.indexOf("status_end_current_condition")].trim();

            boolean checkEventExistance = eventExistanceCheck(type, headers, row, begin_stat, end_stat);
            if (!checkEventExistance) {
                // System.out.println("censored & expired != "+end_stat);
                List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns.add(new AffectedColumn("date of event incurred & type of event", 1,
                        new ArrayList<>(Arrays.asList(String.join(";",row)))));
                controlResult.add(new ControlResult("Event Existence Check", affectedColumns));
                errorsCount.add(1);
            }
        }
    }

    private boolean eventExistanceCheck(String type, List<String> names, String[] row_arr, String begin_stat,
                                        String end_stat) {
        if(type.equalsIgnoreCase("split") && (!names.contains("exposure_or_event") || !"exposure + event".equalsIgnoreCase(row_arr[names.indexOf("exposure_or_event")]))) {
            return true;
        }
        if(begin_stat.equals(end_stat)) {
            return true;
        }
        if((StringUtils.isBlank(end_stat) || end_stat.equalsIgnoreCase("censored") || end_stat.equalsIgnoreCase("expired"))
                && (StringUtils.isBlank(row_arr[names.indexOf("date_of_event_incurred")]) && StringUtils.isBlank(row_arr[names.indexOf("type_of_event")])) ) {
            return true;
        }
        if(StringUtils.isNotBlank(row_arr[names.indexOf("date_of_event_incurred")]) && StringUtils.isNotBlank(row_arr[names.indexOf("type_of_event")])) {
            return true;
        }
        return false;
    }
}
