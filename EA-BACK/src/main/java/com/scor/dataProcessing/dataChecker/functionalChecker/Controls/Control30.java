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

public class Control30 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control30(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }
    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if (headers.contains("age_at_commencement")) {
            Integer min_ac = null;
            Integer max_ac = null;
            Integer ac = null;
            if (product != null) {
                if (product.getMin_age_at_com() != null && product.getMin_age_at_com().matches("\\d+"))
                    min_ac = Integer.parseInt(product.getMin_age_at_com());
                if (product.getMax_age_at_com() != null && product.getMax_age_at_com().matches("\\d+"))
                    max_ac = Integer.parseInt(product.getMax_age_at_com());
            }

            if (row[headers.indexOf("age_at_commencement")] != null
                    && row[headers.indexOf("age_at_commencement")].matches("\\d+"))
                ac = Integer.parseInt(row[headers.indexOf("age_at_commencement")]);

            if (min_ac != null && max_ac != null && ac != null && (min_ac > ac || ac > max_ac)) {
                // System.out.println("**************** "+min_ac+" <= "+ac+" <= "+max_ac);
                List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns.add(new AffectedColumn(
                        "min age at commencement <= age at commencement <= max age at commencement", 1,
                        new ArrayList<>(Arrays.asList(String.join(";",row) + ";;;;;;" + min_ac + ";" + max_ac + ";"))));
                controlResult.add(new ControlResult("Date Comparison", affectedColumns));
                errorsCount.add(1);
            }

        }
    }
}
