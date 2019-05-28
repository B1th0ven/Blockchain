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

public class Control44 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control44(ControlResultAccumulatorV2 acc, LongAccumulator errorsCount) {
        this.controlResult = acc;
        this.errorsCount = errorsCount;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {

        String dateOfEventPaidString = "";
        if( headers.indexOf(Headers.DATE_OF_EVENT_PAID) != -1){
            dateOfEventPaidString = row[headers.indexOf(Headers.DATE_OF_EVENT_PAID)];
        }

        String dateOfEventIncurredString = "";
        if( headers.indexOf(Headers.DATE_OF_EVENT_INCURRED) != -1){
            dateOfEventIncurredString= row[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)];
        }
        String claimPaymentDefferedPeriod = "";
        if( headers.indexOf(Headers.CLAIM_PAYMENT_DEFERRED_PERIOD) != -1){
            claimPaymentDefferedPeriod= row[headers.indexOf(Headers.CLAIM_PAYMENT_DEFERRED_PERIOD)];
        }
        String paymentDefferedPeriodFreq = "";
        if( headers.indexOf(Headers.PAYMENT_DEFERRED_PERIOD_FREQ) != -1){
            paymentDefferedPeriodFreq= row[headers.indexOf(Headers.PAYMENT_DEFERRED_PERIOD_FREQ)];
        }
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);


            if (dateOfEventIncurredString.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")&& dateOfEventPaidString.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
                try {
                    Date dateOfEventPaid = sdf.parse(dateOfEventPaidString);
                    Date dateOfEventIncurred = sdf.parse(dateOfEventIncurredString);

                    if (StringUtils.isNotBlank(claimPaymentDefferedPeriod) && StringUtils.isNotBlank(paymentDefferedPeriodFreq)) {


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dateOfEventIncurred);
                        Tuple2<Integer,Integer> waitingPeriod =  PeriodFreq_To_DAYS(paymentDefferedPeriodFreq);
                        calendar.add(waitingPeriod._1, Integer.parseInt(claimPaymentDefferedPeriod) * waitingPeriod._2);
                        Date resultDateToCompare = calendar.getTime();

                        if (resultDateToCompare.after(dateOfEventPaid)) {
                            List<AffectedColumn> affectedColumns = new ArrayList<>();
                            affectedColumns.add(new AffectedColumn("Date of Event Paid => Date of Event Incurred + Deferred Period", 1, new ArrayList<>(Arrays.asList(String.join(";", row)))));
                            controlResult.add(new ControlResult("Date of Event Paid => Date of Event Incurred + Deferred Period", affectedColumns));
                            errorsCount.add(1);
                        }
                    }


                } catch (Exception e) {
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
