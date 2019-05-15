package com.scor.fileManager.entities;

import java.io.Serializable;

public class change implements Serializable {

    private String id;
    private String type;
    private String description;
    private String component;
    private String func;

    public change(String id, String type, String description, String component, String func) {
        this.id = id;
        this.type = type.toUpperCase().trim();
        this.description = description;
        this.component = component;
        this.func = func;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }


}
