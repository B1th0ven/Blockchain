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

public class Control42 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control42(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if(headers.indexOf("benefit_max_age") > -1 && product != null && product.getMax_benefit_expiry_age() != null){
            if(coherenceOfBenefitMaxAge(row[headers.indexOf("benefit_max_age")],product.getMax_benefit_expiry_age())){
                List<AffectedColumn> affectedColumns = new ArrayList<>();
                affectedColumns.add(new AffectedColumn(
                        "Coherence of Benefit Max Age with policy file", 1,
                        new ArrayList<>(Arrays.asList(String.join(";",row)))));
                controlResult.add(new ControlResult("Coherence of Benefit Max Age with policy file", affectedColumns));
                errorsCount.add(1);
            }
        }
    }

    private boolean coherenceOfBenefitMaxAge (String benfitMaxAge,String benefitExpiryAge){
        try {
            Double bma = Double.parseDouble(benfitMaxAge.trim());
            Double bea = Double.parseDouble(benefitExpiryAge.trim());
            if(bma > bea) return true;
            else return false;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
