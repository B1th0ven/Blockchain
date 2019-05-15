package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.util.LongAccumulator;

import static com.scor.dataProcessing.Helpers.Headers.DATE_OF_END_CURRENT_CONDITION;
import static com.scor.dataProcessing.Helpers.Headers.DATE_OF_EVENT_INCURRED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control33 implements IRule {
    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control33(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        // Blockin Control (Date of Event = Date of END Current Condition)
        if (headers.indexOf(DATE_OF_EVENT_INCURRED) != -1
                && StringUtils.isNotBlank(row[headers.indexOf(DATE_OF_EVENT_INCURRED)])
                && (headers.indexOf(DATE_OF_END_CURRENT_CONDITION) == -1
                || !row[headers.indexOf(DATE_OF_EVENT_INCURRED)]
                .equals(row[headers.indexOf(DATE_OF_END_CURRENT_CONDITION)]))) {

            List<AffectedColumn> affectedColumns = new ArrayList<>();
            affectedColumns.add(new AffectedColumn("Date of Event = Date of End Current Condition", 1,
                    new ArrayList<>(Arrays.asList(String.join(";", row)))));
            controlResult.add(
                    new ControlResult("Date of Event = Date of End Current Condition", affectedColumns));
            errorsCount.add(1);
        }
    }
}
