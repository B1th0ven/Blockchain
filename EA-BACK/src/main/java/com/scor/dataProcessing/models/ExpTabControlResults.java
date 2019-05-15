package com.scor.dataProcessing.models;

import java.util.List;

public class ExpTabControlResults{
    private List<List<String>> compCheckRes;
    private ControlResults techControlRes;
    private ControlResults funcControlRes;

    public ExpTabControlResults(ControlResults func,ControlResults tech,List<List<String>> comp){
        compCheckRes = comp;
        techControlRes = tech;
        funcControlRes = func;

    }
    public List<List<String>> getCompCheckRes() {
        return compCheckRes;
    }

    public void setCompCheckRes(List<List<String>> compCheckRes) {
        this.compCheckRes = compCheckRes;
    }

    public ControlResults getTechControlRes() {
        return techControlRes;
    }

    public void setTechControlRes(ControlResults techControlRes) {
        this.techControlRes = techControlRes;
    }

    public ControlResults getFuncControlRes() {
        return funcControlRes;
    }

    public void setFuncControlRes(ControlResults funcControlRes) {
        this.funcControlRes = funcControlRes;
    }






}
