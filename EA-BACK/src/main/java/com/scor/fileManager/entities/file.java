package com.scor.fileManager.entities;

import java.io.Serializable;

public class file implements Serializable {
    private String      name;
    private String      path;
    private int         size;
    private String      type;
    private Boolean isUserFile;

    public file(String name, String path, int size, String type) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.type = type;
        this.isUserFile = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getUserFile() {
        return isUserFile;
    }

    public void setUserFile(Boolean userFile) {
        isUserFile = userFile;
    }

}
