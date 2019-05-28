package com.scor.TableUpdateReport.model;



public class ControlView {

    private int controlNumber;
    private String controledFile;
    private String category;



    public ControlView(int controlNumber, String controledFile,String category) {
        this.controlNumber = controlNumber;
        this.controledFile = controledFile;
        this.category = category;
    }

    public int getControlNumber() {
        return controlNumber;
    }

    public String getControledFile() {
        return controledFile;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "ControlView{" +
                "controlNumber=" + controlNumber +
                ", controledFile='" + controledFile + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
