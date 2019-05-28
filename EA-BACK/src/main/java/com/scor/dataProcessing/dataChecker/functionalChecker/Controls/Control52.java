package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control52 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control52(ControlResultAccumulatorV2 controlResult, LongAccumulator errorsCount) {
        this.controlResult = controlResult;
        this.errorsCount = errorsCount;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        List<AffectedColumn> affectedColumns = new ArrayList<>();
        String statusEndCurrentCondition = "" ;
        if(headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION)  != -1 ){
            statusEndCurrentCondition = row[headers.indexOf(Headers.STATUS_END_CURRENT_CONDITION) ];
        }
        

        if("claimant_dead".equalsIgnoreCase(statusEndCurrentCondition)){

            if ( (headers.indexOf(Headers.RISK_AMOUNT_INSURER ) != -1)  && (headers.indexOf(Headers.ACCELERATION_RISK_AMOUNT_INSURER )!= -1 ) && StringUtils.isNotBlank(statusEndCurrentCondition)) {
                //insurer columnn is found

                String riskAmountInsurer = row[headers.indexOf(Headers.RISK_AMOUNT_INSURER) ];
                String accelerationRiskAmountInsurer = row[headers.indexOf(Headers.ACCELERATION_RISK_AMOUNT_INSURER) ];

                if(!riskAmountInsurer.equalsIgnoreCase(accelerationRiskAmountInsurer) && StringUtils.isNotBlank(accelerationRiskAmountInsurer)){
                    affectedColumns.add(new AffectedColumn(Headers.RISK_AMOUNT_INSURER +"/"+Headers.ACCELERATION_RISK_AMOUNT_INSURER, 1,
                            new ArrayList<>(Arrays.asList(String.join(";", row)))));
                    controlResult.add(new ControlResult("Acceleration Risk Amount and Risk Amount", affectedColumns));
                    errorsCount.add(1);
                }

            }
            else if ( (headers.indexOf(Headers.RISK_AMOUNT_REINSURER ) != -1)  && (headers.indexOf(Headers.ACCELERATION_RISK_AMOUNT_REINSUR )!= -1 ) && StringUtils.isNotBlank(statusEndCurrentCondition )){
                String riskAmountReinsurer = row[headers.indexOf(Headers.RISK_AMOUNT_REINSURER) ];
                String accelerationRiskAmountReinsurer = row[headers.indexOf(Headers.ACCELERATION_RISK_AMOUNT_REINSUR) ];

                if(!riskAmountReinsurer.equalsIgnoreCase(accelerationRiskAmountReinsurer) && StringUtils.isNotBlank(accelerationRiskAmountReinsurer)){

                    affectedColumns.add(new AffectedColumn(Headers.RISK_AMOUNT_REINSURER +"/"+Headers.ACCELERATION_RISK_AMOUNT_REINSUR, 1, new ArrayList<>(Arrays.asList(String.join(";", row)))));
                    controlResult.add(new ControlResult("Acceleration Risk Amount and Risk Amount", affectedColumns));
                    errorsCount.add(1);

                }
            }

        }


    }
}
