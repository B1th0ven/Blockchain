package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control27 implements IRule {

    ControlResultAccumulator controlResult;
    LongAccumulator errorsCount;

    public Control27(ControlResultAccumulator acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if (headers.contains("risk_amount_reinsurer")) {
            Double mfa = null;
            Double rari = null;
            if (product != null) {
                if (product.getMax_face_amount() != null && !product.getMax_face_amount().isEmpty()
                        && product.getMax_face_amount().replaceAll("\\,", "\\.")
                        .matches("^\\-?\\d+\\.?\\d*$"))
                    mfa = Double.parseDouble(product.getMax_face_amount().replaceAll("\\,", "\\."));
            }

            if (row[headers.indexOf("risk_amount_reinsurer")] != null
                    && row[headers.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\.")
                    .matches("^\\-?\\d+\\.?\\d*$"))
                rari = Double.parseDouble(
                        row[headers.indexOf("risk_amount_reinsurer")].replaceAll("\\,", "\\."));

            if (mfa != null && rari != null && rari > mfa) {
                List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns.add(new AffectedColumn("risk amount reinsurer <= max face amount", 1,
                        new ArrayList<>(Arrays.asList(String.join(";",row) + ";;;;;" + product.getMax_face_amount() + ";;;"))));
                controlResult.add(new ControlResult("Amount Comparison", affectedColumns));
                errorsCount.add(1);
            }
        }
    }
}
