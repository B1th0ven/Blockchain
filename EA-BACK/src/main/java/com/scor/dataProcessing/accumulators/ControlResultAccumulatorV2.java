package com.scor.dataProcessing.accumulators;

import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.dataProcessing.models.ControlUnit;
import org.apache.spark.util.AccumulatorV2;

import java.util.ArrayList;
import java.util.List;

public class ControlResultAccumulatorV2  extends AccumulatorV2<ControlUnit, ControlUnit> {

    private ControlUnit res;

    public ControlResultAccumulatorV2(ControlUnit res) {
        this.res = res;
    }

    public ControlResultAccumulatorV2(){
        this.res = new ControlUnit();
    }

    @Override
    public boolean isZero() {
        return res.getControlResults().isEmpty();
    }

    @Override
    public AccumulatorV2<ControlUnit, ControlUnit> copy() {
        return (new ControlResultAccumulatorV2(res));
    }

    @Override
    public void reset() {
        res = new ControlUnit();
    }

    @Override
    public void add(ControlUnit controlUnit) {
        for (ControlResult cntrl : controlUnit.getControlResults()) {
            add(cntrl);
        }
    }

    @Override
    public void merge(AccumulatorV2<ControlUnit, ControlUnit> accumulatorV2) {
        for(ControlResult ctrl : accumulatorV2.value().getControlResults())
            add(ctrl);
    }

    @Override
    public ControlUnit value() {
        return res;
    }




    public void add(ControlResult arg){
        List<AffectedColumn> affectedColumns = arg.getAffectedColumns();
        ControlResult currentControlResult = null ;
        for(ControlResult cntrl : res.getControlResults()){
            if (cntrl.getControl().equalsIgnoreCase(arg.getControl())){
                currentControlResult = cntrl;
                break;
            }
        }
        if (currentControlResult != null){
            List<AffectedColumn> curr_affectedColumns = currentControlResult.getAffectedColumns();
            for (AffectedColumn ac : affectedColumns) {
                boolean found = false;
                for (AffectedColumn curr_ac : curr_affectedColumns) {
                    if (curr_ac.getName().equals(ac.getName())) {
                        found = true;
                        curr_ac.setErrorsNumber(curr_ac.getErrorsNumber() + ac.getErrorsNumber());
                        if (curr_ac.getExamples().size() < 5) {
                            // if (true) {
                            ArrayList<String> examples = curr_ac.getExamples();
                            examples.addAll(ac.getExamples());
                            curr_ac.setExamples(examples);
                        }
                        break;
                    }
                }
                if (!found) {
                    curr_affectedColumns.add(ac);
                    currentControlResult.setAffectedColumns(curr_affectedColumns);
                }
            }
        }

        else {
            res.getControlResults().add(arg);
        }



    }
}
