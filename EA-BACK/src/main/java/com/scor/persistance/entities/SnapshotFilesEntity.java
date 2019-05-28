package com.scor.persistance.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "EA_SNAPSHOT_FILES", schema = "dbo", catalog = "EXPAN")
public class SnapshotFilesEntity implements Serializable {
    private Long esfId ;
    private Integer dataSet ;
    private String fileType;
    private String fileName;
    private String fileLink;
    private String fileHeader;
    private String inconsistentColumns;
    private String ReportingYear;




    public SnapshotFilesEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESF_ID")
    public Long getEsfId() {
        return esfId;
    }

    public void setEsfId(Long esfId) {
        this.esfId = esfId;
    }

    @Basic
    @Column(name = "ESF_DS_ID")
    public Integer getDataSet() {
        return dataSet;
    }

    public void setDataSet(Integer dataSet) {
        this.dataSet = dataSet;
    }

    @Basic
    @Column(name = "ESF_FILE_TYPE")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Basic
    @Column(name = "ESF_NAME")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Basic
    @Column(name = "ESF_LINK")
    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    @Basic
    @Column(name = "ESF_HEADER")
    public String getFileHeader() {
        return fileHeader;
    }

    public void setFileHeader(String fileHeader) {
        this.fileHeader = fileHeader;
    }


    @Basic
    @Column(name = "ESF_INCONSISTENT_COLUMNS")
    public String getInconsistentColumns() {
        return inconsistentColumns;
    }

    public void setInconsistentColumns(String inconsistentColumns) {
        this.inconsistentColumns = inconsistentColumns;
    }

    @Basic
    @Column(name = "ESF_REPORTING_YEAR")
    public String getReportingYear() {
        return ReportingYear;
    }

    public void setReportingYear(String reportingYear) {
        ReportingYear = reportingYear;
    }
}
