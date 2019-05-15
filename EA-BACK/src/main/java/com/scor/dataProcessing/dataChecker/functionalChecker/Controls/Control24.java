package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Control24 implements IRule {

    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public Control24(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        String ei = headers.indexOf("expenses_included") > -1 ? row[headers.indexOf("expenses_included")] : "";
        String sd = headers.indexOf("settlement_decision") > -1 ? row[headers.indexOf("settlement_decision")] : "";
        String te = row[headers.indexOf("type_of_event")];
        int c =0 ;
        boolean used = false ;
        if (headers.containsAll(Arrays.asList("event_amount_insurer", "risk_amount_insurer"))) {
            String eai = row[headers.indexOf("event_amount_insurer")].replace(",", ".");
            String rai = row[headers.indexOf("risk_amount_insurer")].replace(",", ".");
            if (!rai.isEmpty() && !eai.isEmpty()) {
                if (!check(te, ei, sd, eai, rai)) {
                    c++ ;

                    used = true;
                }
            }

        }

        if (headers.containsAll(Arrays.asList("event_amount_reinsurer", "risk_amount_reinsurer"))) {
            String eari = row[headers.indexOf("event_amount_reinsurer")].replace(",", ".");
            String rari = row[headers.indexOf("risk_amount_reinsurer")].replace(",", ".");
            if (!rari.isEmpty() && !eari.isEmpty()) {
                if(!check(te, ei, sd, eari, rari)) {
                    c++ ;
                        used = true;
                    }
                }



        }
    if (used) {

        List<AffectedColumn> affectedColumns = new ArrayList<>();
        affectedColumns.add(new AffectedColumn(
                "when death / withdrawal (lump sum), risk amount = event amount", c,
                new ArrayList<>(Arrays.asList(String.join(";", row)))));
        controlResult.add(new ControlResult("Lump sum", affectedColumns));
        errorsCount.add(1);
        
    }

    }

    private boolean check(String typeOfEvent, String expensesIncluded, String settlementDecision,
                          String eventAmount, String riskAmount) {
        if (StringUtils.isBlank(expensesIncluded)) {
            expensesIncluded = "no";
        }
        if (StringUtils.isBlank(settlementDecision)) {
            settlementDecision = "no";
        }
        if ((typeOfEvent.equalsIgnoreCase("death") || typeOfEvent.equalsIgnoreCase("withdrawal"))) {
            if (expensesIncluded.equalsIgnoreCase("no")) {
                if (settlementDecision.equalsIgnoreCase("yes")) {
                    boolean compareEaToRa = !eventAmount.isEmpty() && !riskAmount.isEmpty()
                            && Float.parseFloat(eventAmount) <= Float.parseFloat(riskAmount);
                    if (!eventAmount.isEmpty() && !riskAmount.isEmpty() && compareEaToRa) {
                        return true;
                    }
                } else if (settlementDecision.equalsIgnoreCase("no")) {
                    boolean compareEaToRa = !eventAmount.isEmpty() && !riskAmount.isEmpty()
                            && Float.parseFloat(eventAmount) == Float.parseFloat(riskAmount);
                    if (!eventAmount.isEmpty() && !riskAmount.isEmpty() && compareEaToRa) {
                        return true;
                    }
                } else return true;
            } else if (settlementDecision.equalsIgnoreCase("no")) {
                boolean compareEaToRa = !eventAmount.isEmpty() && !riskAmount.isEmpty()
                        && Float.parseFloat(eventAmount) >= Float.parseFloat(riskAmount);
                if (!eventAmount.isEmpty() && !riskAmount.isEmpty() && compareEaToRa) {
                    return true;
                }
            } else return true;

            return false;
        }
        return true;
    }
}