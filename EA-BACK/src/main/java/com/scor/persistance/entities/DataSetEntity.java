package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "DATA_SET", schema = "dbo", catalog = "EXPAN")
public class DataSetEntity implements Serializable {
    private int dsId;
    private String dsName;
    private String dsCode;
    private String dsExposureExtractionDate;
    private String dsEventExtractionDate;
    private String dsDataStructureType;
    private String dsComment;
    //private Integer dsEventExposureFileId;
    //private Integer dsProductFileId;
    private Integer dsStId;

    private EaFilesEntity dsEventExposureFile;
    private EaFilesEntity dsProductFile;

    private String dsTechReport;
    private String dsFuncReport;
    private String dsNotExecuted;
    private Boolean dsDataAvailableTableau;


    private RefUserEntity dsCreatedBy;
    private Date dsCreatedDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DS_ID")
    public int getDsId() {
        return dsId;
    }

    public void setDsId(int dsId) {
        this.dsId = dsId;
    }

    @Basic
    @Column(name = "DS_NAME")
    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    @Basic
    @Column(name = "DS_CODE")
    public String getDsCode() {
        return dsCode;
    }

    public void setDsCode(String dsCode) {
        this.dsCode = dsCode;
    }

    @Basic
    @Column(name = "DS_EXPOSURE_EXTRACTION_DATE")
    public String getDsExposureExtractionDate() {
        return dsExposureExtractionDate;
    }

    public void setDsExposureExtractionDate(String dsExposureExtractionDate) {
        this.dsExposureExtractionDate = dsExposureExtractionDate;
    }

    @Basic
    @Column(name = "DS_EVENT_EXTRACTION_DATE")
    public String getDsEventExtractionDate() {
        return dsEventExtractionDate;
    }

    public void setDsEventExtractionDate(String dsEventExtractionDate) {
        this.dsEventExtractionDate = dsEventExtractionDate;
    }

    @Basic
    @Column(name = "DS_DATA_STRUCTURE_TYPE")
    public String getDsDataStructureType() {
        return dsDataStructureType;
    }

    public void setDsDataStructureType(String dsDataStructureType) {
        this.dsDataStructureType = dsDataStructureType;
    }

    @Basic
    @Column(name = "DS_CREATED_DATE")
    public Date getDsCreatedDate() {
        return dsCreatedDate;
    }

    public void setDsCreatedDate(Date dsCreatedDate) {
        this.dsCreatedDate = dsCreatedDate;
    }

    @Basic
    @Column(name = "DS_COMMENT")
    public String getDsComment() {
        return dsComment;
    }

    public void setDsComment(String dsComment) {
        this.dsComment = dsComment;
    }

    /*
    @Basic
    @Column(name = "DS_EVENT_EXPOSURE_FILE_ID")
    public Integer getDsEventExposureFileId() {
        return dsEventExposureFileId;
    }

    public void setDsEventExposureFileId(Integer dsEventExposureFileId) {
        this.dsEventExposureFileId = dsEventExposureFileId;
    }

    @Basic
    @Column(name = "DS_PRODUCT_FILE_ID")
    public Integer getDsProductFileId() {
        return dsProductFileId;
    }

    public void setDsProductFileId(Integer dsProductFileId) {
        this.dsProductFileId = dsProductFileId;
    }
*/

    @Basic
    @Column(name = "DS_TECH_REPORT")
    public String getDsTechReport() {
        return dsTechReport;
    }

    public void setDsTechReport(String dsTechReport) {
        this.dsTechReport = dsTechReport;
    }

    @Basic
    @Column(name = "DS_FUNC_REPORT")
    public String getDsFuncReport() {
        return dsFuncReport;
    }

    public void setDsFuncReport(String dsFuncReport) {
        this.dsFuncReport = dsFuncReport;
    }

    @Basic
    @Column(name = "DS_NOT_EXECUTED")
    public String getDsNotExecuted() {
        return dsNotExecuted;
    }

    public void setDsNotExecuted(String dsNotExecuted) {
        this.dsNotExecuted = dsNotExecuted;
    }

    @Basic
    @Column(name = "DS_ST_ID")
    public Integer getDsStId() {
        return dsStId;
    }

    public void setDsStId(Integer dsStId) {
        this.dsStId = dsStId;
    }

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "DS_EVENT_EXPOSURE_FILE_ID", referencedColumnName = "EAF_ID" )
    public EaFilesEntity getDsEventExposureFile() {
        return dsEventExposureFile;
    }

    public void setDsEventExposureFile(EaFilesEntity dsEventExposureFile) {
        this.dsEventExposureFile = dsEventExposureFile;
    }

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "DS_PRODUCT_FILE_ID", referencedColumnName = "EAF_ID")
    public EaFilesEntity getDsProductFile() {
        return dsProductFile;
    }

    public void setDsProductFile(EaFilesEntity dsProductFile) {
        this.dsProductFile = dsProductFile;
    }

    @ManyToOne
    @JoinColumn(name = "DS_CREATED_BY", referencedColumnName = "RU_ID")
    public RefUserEntity getDsCreatedBy() {
        return dsCreatedBy;
    }

    public void setDsCreatedBy(RefUserEntity dsCreatedBy) {
        this.dsCreatedBy = dsCreatedBy;
    }
    
    @Basic
    @Column(name = "DS_DATA_AVAILABLE_TABLEAU")
	public Boolean getDsDataAvailableTableau() {
		return dsDataAvailableTableau;
	}

	public void setDsDataAvailableTableau(Boolean dsDataAvailableTableau) {
		this.dsDataAvailableTableau = dsDataAvailableTableau;
	}
    
    
}
