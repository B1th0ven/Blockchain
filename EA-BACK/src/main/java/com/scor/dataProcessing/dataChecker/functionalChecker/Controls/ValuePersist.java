package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;


import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.accumulators.MapAccumulator;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValuePersist implements IRule {

    MapAccumulator controlResult;
    LongAccumulator errorsCount;

    public ValuePersist(MapAccumulator acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
    }

    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        Map<String, List<String>> map = new HashMap<>();
        int indexOfMainRiskType = headers.indexOf(Headers.MAIN_RISK_TYPE);
        if (indexOfMainRiskType > -1) {
            String mainRiskType = row[indexOfMainRiskType];
            map.put(Headers.MAIN_RISK_TYPE, Arrays.asList(mainRiskType));
//			List<AffectedColumn> affectedColumns = new ArrayList<>();
//			affectedColumns.add(new AffectedColumn(Headers.MAIN_RISK_TYPE, 1, new ArrayList<>(Arrays.asList(mainRiskType))));
//			valuesPersistAccumulator.add(new ControlResult("values_persist", affectedColumns));
        }
        int indexOfAccelerationRiskType = headers.indexOf(Headers.ACCELERATION_RISK_TYPE);
        if (indexOfAccelerationRiskType > -1) {
            String accelerationRiskType = row[indexOfAccelerationRiskType];
            map.put(Headers.ACCELERATION_RISK_TYPE, Arrays.asList(accelerationRiskType));

//			List<AffectedColumn> affectedColumns = new ArrayList<>();
//			affectedColumns.add(new AffectedColumn(Headers.ACCELERATION_RISK_TYPE, 1, new ArrayList<>(Arrays.asList(accelerationRiskType))));
//			valuesPersistAccumulator.add(new ControlResult("values_persist", affectedColumns));
        }
        controlResult.add(map);
    }
}
