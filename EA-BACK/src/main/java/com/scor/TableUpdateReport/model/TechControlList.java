package com.scor.TableUpdateReport.model;

import com.scor.dataProcessing.models.TechControlResults;


public class TechControlList {
    TechControlResults[] techControlResults;

    public TechControlList(TechControlResults[] techControlResults) {
        this.techControlResults = techControlResults;
    }

    public TechControlList() {
    }

    public TechControlResults[] getTechControlResults() {
        return techControlResults;
    }

    public void setTechControlResults(TechControlResults[] techControlResults) {
        this.techControlResults = techControlResults;
    }
}
