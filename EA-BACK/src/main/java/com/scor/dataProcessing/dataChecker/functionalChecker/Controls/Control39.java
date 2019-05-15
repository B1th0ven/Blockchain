package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control39 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control39(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }


    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if(headers.containsAll(Arrays.asList("event_amount_reinsurer","event_amount_insurer"))) {
            if(eventAmountReInsurerLowerThanInsurer(row[headers.indexOf("event_amount_reinsurer")],row[headers.indexOf("event_amount_insurer")])) {
                List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns
                        .add(new AffectedColumn("Event Amount Reinsurer <= Event Amount Insurer",
                                1, new ArrayList<>(Arrays.asList(String.join(";",row)))));
                controlResult.add(new ControlResult("Event Amount Reinsurer <= Event Amount Insurer", affectedColumns));
                errorsCount.add(1);
            }
        }
    }

    private boolean eventAmountReInsurerLowerThanInsurer(String eventAmountReinsurer, String eventAmountInsurer) {
        if(StringUtils.isBlank(eventAmountInsurer) || StringUtils.isBlank(eventAmountReinsurer)) {
            return false;
        }
        try {
            double eventAmountReinsurerDouble = Double.parseDouble(eventAmountReinsurer.replaceAll("\\,", "\\."));
            Double eventAmountInsurerDouble = Double.parseDouble(eventAmountInsurer.replaceAll("\\,", "\\."));
            return !(eventAmountReinsurerDouble<=eventAmountInsurerDouble);
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
