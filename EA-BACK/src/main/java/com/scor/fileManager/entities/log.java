package com.scor.fileManager.entities;

import java.io.Serializable;
import java.util.List;

public class log implements Serializable {

    String version;
    String date;
    List<change> changes;

    public log(String version, String date, List<change> changes) {
        this.version = version;
        this.date = date;
        this.changes = changes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<change> getChanges() {
        return changes;
    }

    public void setChanges(List<change> changes) {
        this.changes = changes;
    }


}
