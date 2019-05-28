package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.util.LongAccumulator;
import scala.Tuple2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Control46 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control46(ControlResultAccumulatorV2 controlResult, LongAccumulator errorsCount) {
        this.controlResult = controlResult;
        this.errorsCount = errorsCount;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {


        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        String statusBeginCurrentCondition = "" ;
        if(headers.indexOf(Headers.STATUS_BEGIN_CURRENT_CONDITION)  != -1 ){
            statusBeginCurrentCondition = row[headers.indexOf(Headers.STATUS_BEGIN_CURRENT_CONDITION) ];
        }

        String dateOfEndCurrentConditionString = "";
        if( headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION) != -1){
            dateOfEndCurrentConditionString = row[headers.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)];
        }

        String dateOfClaimCommencementString = "";
        if( headers.indexOf(Headers.DATE_OF_CLAIM_COMMENCEMENT) != -1){
            dateOfClaimCommencementString = row[headers.indexOf(Headers.DATE_OF_CLAIM_COMMENCEMENT)];
        }

        String claimPayementDeferredPeriod = "";
        if( headers.indexOf(Headers.CLAIM_PAYMENT_DEFERRED_PERIOD) != -1){
            claimPayementDeferredPeriod = row[headers.indexOf(Headers.CLAIM_PAYMENT_DEFERRED_PERIOD)];
        }

        String paymentDeferredPeriodFreq = "";
        if( headers.indexOf(Headers.PAYMENT_DEFERRED_PERIOD_FREQ) != -1){
            paymentDeferredPeriodFreq = row[headers.indexOf(Headers.PAYMENT_DEFERRED_PERIOD_FREQ)];
        }
        String claimPaymentTermYears = "";
        if( headers.indexOf(Headers.CLAIM_PAYMENT_TERM_YEARS) != -1){
            claimPaymentTermYears = row[headers.indexOf(Headers.CLAIM_PAYMENT_TERM_YEARS)];
        }
        if("claimant".equalsIgnoreCase(statusBeginCurrentCondition) && StringUtils.isNotBlank(claimPayementDeferredPeriod) && StringUtils.isNotBlank(paymentDeferredPeriodFreq) && StringUtils.isNotBlank(claimPaymentTermYears))
        {

            if(dateOfClaimCommencementString.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && dateOfEndCurrentConditionString.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {


                try {
                    Date dateOfEndCurrentCondition = sdf.parse(dateOfEndCurrentConditionString);
                    Date dateOfClaimCommencement = sdf.parse(dateOfClaimCommencementString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateOfClaimCommencement);
                    Tuple2<Integer,Integer> waitingPeriod =  PeriodFreq_To_DAYS(paymentDeferredPeriodFreq);
                    calendar.add(waitingPeriod._1, Integer.parseInt(claimPayementDeferredPeriod) * waitingPeriod._2);
                    calendar.add(Calendar.YEAR, Integer.parseInt(claimPaymentTermYears));
                    Date resultDate = calendar.getTime();
                    if (resultDate.before(dateOfEndCurrentCondition)) {
                        List<AffectedColumn> affectedColumns = new ArrayList<>();
                        affectedColumns.add(new AffectedColumn("Event Paid <= Claim Payment End", 1, new ArrayList<>(Arrays.asList(String.join(";", row)))));
                        controlResult.add(new ControlResult("Event Paid <= Claim Payment End", affectedColumns));
                        errorsCount.add(1);
                    }


                } catch (Exception e) {
                }
            }
        }
    }


    private Tuple2<Integer,Integer> PeriodFreq_To_DAYS(String freq){
        if(freq.equalsIgnoreCase("daily")){
            return new Tuple2<>(Calendar.DATE,1);
        }
        if(freq.equalsIgnoreCase("weekly")){
            return new Tuple2<>(Calendar.DATE,7);
        }
        if(freq.equalsIgnoreCase("monthly")){
            return new Tuple2<>(Calendar.MONTH,1);
        }
        if(freq.equalsIgnoreCase("quarterly")){
            return new Tuple2<>(Calendar.MONTH, 3);
        }
        if(freq.equalsIgnoreCase("biannually")){
            return new Tuple2<>(Calendar.MONTH, 6);
        }
        if(freq.equalsIgnoreCase("annually")){
            return new Tuple2<>(Calendar.YEAR, 1);
        }

        return new Tuple2<>(0,0);
    }


}
