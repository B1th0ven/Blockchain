package com.scor.dataProcessing.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Event implements Serializable {
    private String Name;
    private LocalDate Date;
    private String PolicyID;
    private String main_risk_type;
    private String acceleration_risk_type;

    public Event(String n,String d,String pid,String mrt,String art){
        Name = n;
        if(d.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
             Date = LocalDate.parse(d,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
        } else Date = null;
        PolicyID = pid;
        main_risk_type = mrt;
        acceleration_risk_type = art;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public LocalDate getDate() {

       return Date;
    }

    public void setDate(String date) {
        if(date.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            LocalDate Date = LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
        } else Date = null;
    }

    public String getPolicyID() {
        return PolicyID;
    }

    public void setPolicyID(String policyID) {
        PolicyID = policyID;
    }

    public String getMain_risk_type() {
        return main_risk_type;
    }

    public void setMain_risk_type(String main_risk_type) {
        this.main_risk_type = main_risk_type;
    }

    public String getAcceleration_risk_type() {
        return acceleration_risk_type;
    }

    public void setAcceleration_risk_type(String acceleration_risk_type) {
        this.acceleration_risk_type = acceleration_risk_type;
    }
}
