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

public class Control45 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control45(ControlResultAccumulatorV2 acc, LongAccumulator errorsCount) {
        this.controlResult = acc;
        this.errorsCount = errorsCount;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {

        String dateOfEventIncurredString = "";
        if( headers.indexOf(Headers.DATE_OF_EVENT_INCURRED) != -1){
            dateOfEventIncurredString = row[headers.indexOf(Headers.DATE_OF_EVENT_INCURRED)];
        }
        String dateOfCommencementString = "";
        if( headers.indexOf(Headers.DATE_OF_COMMENCEMENT) != -1){
            dateOfCommencementString = row[headers.indexOf(Headers.DATE_OF_COMMENCEMENT)];
        }
        String waitingPeriod_1 = "";
        if( headers.indexOf(Headers.WAITING_PERIOD_1) != -1){
            waitingPeriod_1 = row[headers.indexOf(Headers.WAITING_PERIOD_1)];
        }
        String waitingPeriod_1_freq = "";
        if( headers.indexOf(Headers.WAITING_PERIOD_1_FREQ) != -1){
            waitingPeriod_1_freq = row[headers.indexOf(Headers.WAITING_PERIOD_1_FREQ)];
        }
        String waitingPeriod_2 = "";
        if( headers.indexOf(Headers.WAITING_PERIOD_2) != -1){
            waitingPeriod_2 = row[headers.indexOf(Headers.WAITING_PERIOD_2)];
        }
        String waitingPeriod_2_freq = "";
        if( headers.indexOf(Headers.WAITING_PERIOD_2_FREQ) != -1){
            waitingPeriod_2_freq = row[headers.indexOf(Headers.WAITING_PERIOD_2_FREQ)];
        }
        String waitingPeriod_3 = "";
        if( headers.indexOf(Headers.WAITING_PERIOD_3) != -1){
            waitingPeriod_3 = row[headers.indexOf(Headers.WAITING_PERIOD_3)];
        }
        String waitingPeriod_3_freq = "";
        if( headers.indexOf(Headers.WAITING_PERIOD_3_FREQ) != -1){
            waitingPeriod_3_freq = row[headers.indexOf(Headers.WAITING_PERIOD_3_FREQ)];
        }





        if(dateOfCommencementString.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$") && dateOfEventIncurredString.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            if ((StringUtils.isNotBlank(waitingPeriod_1) && StringUtils.isNotBlank(waitingPeriod_1_freq)) || (StringUtils.isNotBlank(waitingPeriod_2)&& StringUtils.isNotBlank(waitingPeriod_2_freq)) || (StringUtils.isNotBlank(waitingPeriod_3) && StringUtils.isNotBlank(waitingPeriod_3_freq ))) {


                String pattern = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                try {
                    Date dateOfCommencement = sdf.parse(dateOfCommencementString);
                    Date dateofEventIncurred = sdf.parse(dateOfEventIncurredString);
                    Calendar calendar = Calendar.getInstance();

                    Date minDate = new Date(Long.MAX_VALUE) ;
                    int min_index = 0 ;

                    if (StringUtils.isNotBlank(waitingPeriod_1) && StringUtils.isNotBlank(waitingPeriod_1_freq)) {
                        calendar.setTime(dateOfCommencement);
                        Tuple2<Integer,Integer> waitingfreq =  PeriodFreq_To_DAYS(waitingPeriod_1_freq) ;
                        calendar.add(waitingfreq._1, waitingfreq._2 * Integer.parseInt(waitingPeriod_1));
                        Date resultDateToCompare = calendar.getTime();
                        if(minDate.after(resultDateToCompare)){
                            min_index = 1 ;
                            minDate = resultDateToCompare;
                        }
                    }

                    if (StringUtils.isNotBlank(waitingPeriod_2) && StringUtils.isNotBlank(waitingPeriod_2_freq )) {
                        calendar.setTime(dateOfCommencement);
                        Tuple2<Integer,Integer> waitingfreq =  PeriodFreq_To_DAYS(waitingPeriod_2_freq) ;
                        calendar.add(waitingfreq._1, waitingfreq._2 * Integer.parseInt(waitingPeriod_2));
                        Date resultDateToCompare = calendar.getTime();
                        if(minDate.after(resultDateToCompare)){
                            min_index = 2 ;
                            minDate = resultDateToCompare;
                        }
                    }

                    if (StringUtils.isNotBlank(waitingPeriod_3) && StringUtils.isNotBlank(waitingPeriod_3_freq)) {
                        calendar.setTime(dateOfCommencement);
                        Tuple2<Integer,Integer> waitingfreq =  PeriodFreq_To_DAYS(waitingPeriod_3_freq) ;
                        calendar.add(waitingfreq._1, waitingfreq._2 * Integer.parseInt(waitingPeriod_3));
                        Date resultDateToCompare = calendar.getTime();
                        if(minDate.after(resultDateToCompare)){
                            min_index = 3 ;
                            minDate = resultDateToCompare;
                        }
                    }







                        if (minDate.after(dateofEventIncurred)) {
                            List<AffectedColumn> affectedColumns = new ArrayList<>();
                            affectedColumns.add(new AffectedColumn("waiting_period_" + min_index + "/" + "waiting_period_" + min_index + "_freq", 1, new ArrayList<>(Arrays.asList(String.join(";", row)))));
                            controlResult.add(new ControlResult("Waiting Period", affectedColumns));
                            errorsCount.add(1);
                        }


                    } catch(Exception e){

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
