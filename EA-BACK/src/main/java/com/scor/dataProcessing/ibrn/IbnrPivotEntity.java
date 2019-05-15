package com.scor.dataProcessing.ibrn;

import java.util.List;

public class IbnrPivotEntity {
    String name;
    boolean required = false;
    boolean unique = false;
    String type;
    String regex;
    List<String> uniqueWithList;
    int unicityId;

    public IbnrPivotEntity(String name, boolean required, boolean unique, String type, String regex, List<String> uniqueWithList, int unicityId) {
        this.name = name;
        this.required = required;
        this.unique = unique;
        this.type = type;
        this.regex = regex;
        this.uniqueWithList = uniqueWithList;
        this.unicityId = unicityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public List<String> getUniqueWithList() {
        return uniqueWithList;
    }

    public void setUniqueWithList(List<String> uniqueWithList) {
        this.uniqueWithList = uniqueWithList;
    }

    public int getUnicityId() {
        return unicityId;
    }

    public void setUnicityId(int unicityId) {
        this.unicityId = unicityId;
    }

    @Override
    public String toString() {
        return "IbnrPivotEntity{" +
                "name='" + name + '\'' +
                ", required=" + required +
                ", unique=" + unique +
                ", type='" + type + '\'' +
                ", regex='" + regex + '\'' +
                ", uniqueWithList=" + uniqueWithList +
                ", unicityId=" + unicityId +
                '}';
    }
}
