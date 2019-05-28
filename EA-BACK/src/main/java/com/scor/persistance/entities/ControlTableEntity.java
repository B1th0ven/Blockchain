package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CONTROL_TABLE", schema = "dbo", catalog = "EXPAN")
public class ControlTableEntity implements Serializable {




    Integer ctId ;
    Long ctErrorNumber;
    String ctFile;
    String ctMessage;
    String ctName;
    String ctCategory;
    int ctNumber;
    int dsId;

    public ControlTableEntity() {
    }

    public ControlTableEntity(Integer ctId, Long ctErrorNumber, String ctFile, String ctMessage, String ctName, String ctCategory, int ctNumber, int dsId) {
        this.ctId = ctId;
        this.ctErrorNumber = ctErrorNumber;
        this.ctFile = ctFile;
        this.ctMessage = ctMessage;
        this.ctName = ctName;
        this.ctCategory = ctCategory;
        this.ctNumber = ctNumber;
        this.dsId = dsId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CT_ID", updatable = false, nullable = false)
    public Integer getCtId() {
        return ctId;
    }

    public void setCtId(Integer ctId) {
        this.ctId = ctId;
    }

    @Basic
    @Column(name = "CT_ERROR_NUMBER")
    public Long getCtErrorNumber() {
        return ctErrorNumber;
    }

    public void setCtErrorNumber(Long ctErrorNumber) {
        this.ctErrorNumber = ctErrorNumber;
    }

    @Basic
    @Column(name = "CT_FILE")
    public String getCtFile() {
        return ctFile;
    }

    public void setCtFile(String ctFile) {
        this.ctFile = ctFile;
    }

    @Basic
    @Column(name = "CT_MESSAGE")
    public String getCtMessage() {
        return ctMessage;
    }

    public void setCtMessage(String ctMessage) {
        this.ctMessage = ctMessage;
    }

    @Basic
    @Column(name = "CT_NAME")
    public String getCtName() {
        return ctName;
    }

    public void setCtName(String ctName) {
        this.ctName = ctName;
    }

    @Basic
    @Column(name = "CT_CATEGORY")
    public String getCtCategory() {
        return ctCategory;
    }

    public void setCtCategory(String ctCategory) {
        this.ctCategory = ctCategory;
    }

    @Basic
    @Column(name = "DS_ID")
    public int getDsId() {
        return dsId;
    }

    public void setDsId(int dsId) {
        this.dsId = dsId;
    }

    @Basic
    @Column(name = "CT_NUMBER")
    public int getCtNumber() {
        return ctNumber;
    }

    public void setCtNumber(int ctNumber) {
        this.ctNumber = ctNumber;
    }
}
