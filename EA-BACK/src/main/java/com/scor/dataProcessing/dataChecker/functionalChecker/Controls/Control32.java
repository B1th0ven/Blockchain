package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control32 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control32(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if (product == null) {
            List<AffectedColumn> affectedColumns = new ArrayList<>();
            affectedColumns.add(new AffectedColumn("Product Id doesn't exists in product file", 1,
                    new ArrayList<>(Arrays.asList(String.join(";",row)))));
            controlResult.add(
                    new ControlResult("Checking if Product Id exists in product file", affectedColumns));
            errorsCount.add(1);
        }
    }
}
