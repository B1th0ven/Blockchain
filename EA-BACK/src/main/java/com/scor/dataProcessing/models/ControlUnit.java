package com.scor.dataProcessing.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ControlUnit implements Serializable {

    List<ControlResult> controlResults;
    String name;

    public ControlUnit() {
        this.controlResults = new ArrayList<>();
        this.name ="newAccumulatorByAmine";
    }
    public ControlUnit(ControlResult ctr) {
        this.controlResults = new ArrayList<>();
        controlResults.add(ctr);
    }

    public List<ControlResult> getControlResults() {
        return controlResults;
    }

    public void setControlResults(List<ControlResult> controlResults) {
        this.controlResults = controlResults;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
