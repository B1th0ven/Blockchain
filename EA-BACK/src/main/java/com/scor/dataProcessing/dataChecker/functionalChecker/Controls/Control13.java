package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.util.LongAccumulator;

import java.util.*;

public class Control13 implements IRule {
    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;
    String type;

    public Control13(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo, String type){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
        this.type = type;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        if (headers.indexOf("status_end_current_condition") > -1 && (type.equalsIgnoreCase("combine")
                || (type.equalsIgnoreCase("split") && headers.contains("exposure_or_event")
                && "exposure + event"
                .equalsIgnoreCase(row[headers.indexOf("exposure_or_event")])))
                && !check(row[headers.indexOf("status_begin_current_condition")],
                row[headers.indexOf("status_end_current_condition")],
                row[headers.indexOf("type_of_event")])) {

            List<AffectedColumn> affectedColumns = new ArrayList<>();
            affectedColumns.add(new AffectedColumn("Type of Event & Status at End Current Condition", 1,
                    new ArrayList<>(Arrays.asList(String.join(";",row)))));
            controlResult.add(new ControlResult("Status coherent with event type", affectedColumns));
            errorsCount.add(1);
        }
    }

    private boolean check(String statusBegin, String statusEnd, String typeOfEvent) {
        Map<String, Map<String, String>> typeOfEventMap = typeOfEventMapper();
        if (StringUtils.isBlank(statusBegin))
            statusBegin = "empty";
        if (StringUtils.isBlank(statusEnd))
            statusEnd = "empty";
        if (StringUtils.isBlank(typeOfEvent))
            typeOfEvent = "empty";

        Map<String, String> typeOfEventStatusEndMap = typeOfEventMap.get(statusBegin);
        if (typeOfEventStatusEndMap == null) {
            return false;
        }
        String typeOfEventExpected = typeOfEventStatusEndMap.get(statusEnd);
        if (typeOfEventExpected == null) {
            return false;
        }
        for (String values : typeOfEventExpected.split(";")) {
            if (values.equals(typeOfEvent.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private static Map<String, Map<String, String>> typeOfEventMapper() {
        Map<String, Map<String, String>> typeOfEventMap = new HashMap<>();
        Map<String, String> activeMap = new HashMap<>();
        activeMap.put("active", "empty");
        activeMap.put("claimant", "incidence");
        activeMap.put("dead", "death");
        activeMap.put("withdrawn", "withdrawal");
        activeMap.put("expired", "empty");
        activeMap.put("censored", "empty");
        activeMap.put("empty", "empty");
        activeMap.put("claimant_dead", "incidence_death");
        typeOfEventMap.put("active", activeMap);
        Map<String, String> claimantMap = new HashMap<>();
        claimantMap.put("active", "claim termination");
        claimantMap.put("claimant", "empty");
        claimantMap.put("dead", "claim termination");
        claimantMap.put("withdrawn", "claim termination");
        claimantMap.put("expired", "empty");
        claimantMap.put("censored", "empty;claim termination");
        claimantMap.put("empty", "empty");
        typeOfEventMap.put("claimant", claimantMap);
        return typeOfEventMap;
    }
}
