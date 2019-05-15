package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.accumulators.ControlResultAccumulatorV2;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.Product;

import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FloatComparaison implements IRule {

    List<List<String>> floats_to_comp;
    ControlResultAccumulatorV2 controlResult;
    LongAccumulator errorsCount;

    public FloatComparaison(ControlResultAccumulatorV2 acc, LongAccumulator errorsNo){
        this.controlResult = acc;
        this.errorsCount = errorsNo;
        floats_to_comp = new ArrayList<>();
        floats_to_comp.add(new ArrayList<>(Arrays.asList("risk_amount_reinsurer", "risk_amount_insurer")));
    }


    @Override
    public void validate(String[] row, Product product, List<String> headers) {
        for (List<String> ftc : floats_to_comp) {
            if (headers.containsAll(ftc)) {
                try {
                    ArrayList<Float> floats = new ArrayList<>();
                    boolean empty = false;
                    for (String col : ftc) {
                        if (headers.indexOf(col) == -1) {
                            empty = true;
                            break;
                        } else if (row[headers.indexOf(col)].equals("")) {
                            empty = true;
                            break;
                        }
                        floats.add(Float.parseFloat(row[headers.indexOf(col)].replaceAll("\\,", "\\.")));
                    }
                    if (empty)
                        continue;
                    for (int j = 0; j < floats.size() - 1; j++) {
                        if (floats.get(j) > floats.get(j + 1)) {
                            List<AffectedColumn> affectedColumns = new ArrayList<>();
                            affectedColumns.add(new AffectedColumn(
                                    String.join(" & ",
                                            ftc.stream().map(s -> s.replace("_", " "))
                                                    .collect(Collectors.toList())),
                                    1, new ArrayList<>(Arrays.asList(String.join(";",row)))));
                            controlResult.add(new ControlResult("Amount Comparison", affectedColumns));
                            errorsCount.add(1);
                            break;
                        }
                    }
                } catch (Exception e) {
                    List<AffectedColumn> affectedColumns = new ArrayList<>();
                    affectedColumns.add(new AffectedColumn(
                            String.join(" & ",
                                    ftc.stream().map(s -> s.replace("_", " "))
                                            .collect(Collectors.toList())),
                            1, new ArrayList<>(Arrays.asList(String.join(";",row)))));
                    controlResult.add(new ControlResult("Amount Comparison", affectedColumns));
                    errorsCount.add(1);
                }
            }
        }
    }
}
