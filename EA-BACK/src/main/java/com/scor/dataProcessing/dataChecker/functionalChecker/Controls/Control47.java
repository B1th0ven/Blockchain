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

public class Control47 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control47(ControlResultAccumulatorV2 controlResult, LongAccumulator errorsCount) {
        this.controlResult = controlResult;
        this.errorsCount = errorsCount;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        List<AffectedColumn> affectedColumns = new ArrayList<>();

        String dateOfEndCurrentCondition = "";
        if( headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION) != -1){
            dateOfEndCurrentCondition = row[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)];
        }

        String typeOfEvent = "" ;
        if (headers.indexOf(Headers.TYPE_OF_EVENT) != -1){
            typeOfEvent = row[headers.indexOf(Headers.TYPE_OF_EVENT)];
        }


        String benefitEndDate = "";
        if( headers.indexOf(Headers.BENEFIT_END_DATE) != -1){
            benefitEndDate = row[headers.indexOf(Headers.BENEFIT_END_DATE)];
        }

        String claimPaymentEndDate = "" ;
        if( headers.indexOf(Headers.CLAIM_PAYMENT_END_DATE) != -1){
            claimPaymentEndDate = row[headers.indexOf(Headers.CLAIM_PAYMENT_END_DATE)];
        }

        String exposureOrEvent = "" ;
        if( headers.indexOf(Headers.EXPOSURE_OR_EVENT) != -1){
            exposureOrEvent = row[headers.indexOf(Headers.EXPOSURE_OR_EVENT)];
        }

        if(StringUtils.isNotBlank(dateOfEndCurrentCondition) && dateOfEndCurrentCondition.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && StringUtils.isNotBlank(typeOfEvent) && "Exposure + Event".equalsIgnoreCase(exposureOrEvent)){

            if(dateOfEndCurrentCondition.equalsIgnoreCase(benefitEndDate) ){
                        affectedColumns.add(new AffectedColumn("Status at benefit end / claim payment end", 1, new ArrayList<>(Arrays.asList(String.join(";", row)))));
                        controlResult.add(new ControlResult("Status at benefit end / claim payment end", affectedColumns));
                        errorsCount.add(1);
            }

            if(dateOfEndCurrentCondition.equalsIgnoreCase(claimPaymentEndDate)){
                    affectedColumns.add(new AffectedColumn("Status at benefit end / claim payment end", 1, new ArrayList<>(Arrays.asList(String.join(";", row)))));
                    controlResult.add(new ControlResult("Status at benefit end / claim payment end", affectedColumns));
                    errorsCount.add(1);

            }
        }

    }
}
