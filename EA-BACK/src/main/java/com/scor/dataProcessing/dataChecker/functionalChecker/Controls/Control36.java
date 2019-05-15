package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.spark.util.LongAccumulator;

import static com.scor.dataProcessing.Helpers.Headers.ACCELERATION_RISK_TYPE;
import static com.scor.dataProcessing.Helpers.Headers.MAIN_RISK_TYPE;
import static com.scor.dataProcessing.Helpers.Headers.TYPE_OF_EVENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control36 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control36(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        //CR  Events should be coherent with the main risk type and the acceleration risk type For each line
        if (headers.contains(ACCELERATION_RISK_TYPE)) {
            String type_of_event = row[headers.indexOf(TYPE_OF_EVENT)].toLowerCase().trim();
            String main_risk_type = row[headers.indexOf(MAIN_RISK_TYPE)].toLowerCase().trim();
            String acceleration_risk_type = row[headers.indexOf(ACCELERATION_RISK_TYPE)].toLowerCase().trim();

            switch (type_of_event) {
                case "incidence":
                    // sprint 11 :  The control should execute even if Acceleration Risk Type is missing
                    if (!"ci,di,ltc,tpd,health".contains(main_risk_type) && (!main_risk_type.equalsIgnoreCase("life") )) {
                        // if (!"ci,di,ltc,tpd,health".contains(main_risk_type) && (!main_risk_type.equalsIgnoreCase("life") || acceleration_risk_type.isEmpty())) {
                        List<AffectedColumn> affectedColumns = new ArrayList<>();
                        affectedColumns.add(new AffectedColumn(
                                "Events should be coherent with the main risk type and the acceleration risk type", 1,
                                new ArrayList<>(Arrays.asList(String.join(";",row)))));
                        controlResult.add(new ControlResult("Events should be coherent with the main risk type and the acceleration risk type", affectedColumns));
                        errorsCount.add(1);
                    }
                    break;
                case "claim termination":
                    if (!"di,ltc,unemployment".contains(main_risk_type)) {
                        List<AffectedColumn> affectedColumns = new ArrayList<>();
                        affectedColumns.add(new AffectedColumn(
                                "Events should be coherent with the main risk type and the acceleration risk type", 1,
                                new ArrayList<>(Arrays.asList(String.join(";", String.join(";",row))))));
                        controlResult.add(new ControlResult("Events should be coherent with the main risk type and the acceleration risk type", affectedColumns));
                        errorsCount.add(1);
                    }
                    break;
                default:
                    break;
            }


        }
    }
}
