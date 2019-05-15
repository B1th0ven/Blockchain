package com.scor.persistance.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "REF_EXPECTED_TABLE", schema = "dbo", catalog = "EXPAN")
public class RefExpectedTableEntity  implements Serializable {
    private int retId;
    private String retCode;
    private String retName;
    private String retUrl;
    private int retRcId;
    private String retDecrement;
    private int retVersion;
    private String retOrigin;
    private String retExposureMethod;
    private String retType;
    private int retApplicationYear;
    private int retPublicationYear;
    private String retComment;
    private int retAgeMin;
    private int retAgeMax;
    private int retCalYearMin;
    private int retCalYearMax;
    private int retDurationMin;
    private int retDurationMax;
    private boolean retLatestVersion;
    private String retSource;
    private String retCompReport;
    private String retTechReport;
    private String retFuncReport;
    private RefCountryEntity country;
    private Date retCreationDate;
    private String retDimensions;
    private String retStatus;
    private boolean retIsConfidential;



    private RefUserEntity retCreatedBy;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RET_ID")
    public int getRetId() {
        return retId;
    }

    public void setRetId(int retId) {
        this.retId = retId;
    }

    @Basic
    @Column(name = "RET_CODE")
    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    @Basic
    @Column(name = "RET_NAME")
    public String getRetName() {
        return retName;
    }

    public void setRetName(String retName) {
        this.retName = retName;
    }

    @Basic
    @Column(name = "RET_URL")
    public String getRetUrl() {
        return retUrl;
    }

    public void setRetUrl(String retUrl) {
        this.retUrl = retUrl;
    }

    @Basic
    @Column(name = "RET_RC_ID")
    public int getRetRcId() {
        return retRcId;
    }

    public void setRetRcId(int retRcId) {
        this.retRcId = retRcId;
    }

    @Basic
    @Column(name = "RET_DECREMENT")
    public String getRetDecrement() {
        return retDecrement;
    }


    public void setRetDecrement(String retDecrement) {
        this.retDecrement = retDecrement;
    }

    @Basic
    @Column(name = "RET_VERSION")
    public int getRetVersion() {
        return retVersion;
    }

    public void setRetVersion(int retVersion) {
        this.retVersion = retVersion;
    }

    @Basic
    @Column(name = "RET_ORIGIN")
    public String getRetOrigin() {
        return retOrigin;
    }

    public void setRetOrigin(String retOrigin) {
        this.retOrigin = retOrigin;
    }

    @Basic
    @Column(name = "RET_EXPOSURE_METHOD")
    public String getRetExposureMethod() {
        return retExposureMethod;
    }

    public void setRetExposureMethod(String retExposureMethod) {
        this.retExposureMethod = retExposureMethod;
    }

    @Basic
    @Column(name = "RET_TYPE")
    public String getRetType() {
        return retType;
    }

    public void setRetType(String retType) {
        this.retType = retType;
    }

    @Basic
    @Column(name = "RET_APPLICATION_YEAR")
    public int getRetApplicationYear() {
        return retApplicationYear;
    }

    public void setRetApplicationYear(int retApplicationYear) {
        this.retApplicationYear = retApplicationYear;
    }

    @Basic
    @Column(name = "RET_PUBLICATION_YEAR")
    public int getRetPublicationYear() {
        return retPublicationYear;
    }

    public void setRetPublicationYear(int retPublicationYear) {
        this.retPublicationYear = retPublicationYear;
    }

    @Basic
    @Column(name = "RET_COMMENT")
    public String getRetComment() {
        return retComment;
    }

    public void setRetComment(String retComment) {
        this.retComment = retComment;
    }

    @Basic
    @Column(name = "RET_AGE_MIN")
    public int getRetAgeMin() {
        return retAgeMin;
    }

    public void setRetAgeMin(int retAgeMin) {
        this.retAgeMin = retAgeMin;
    }

    @Basic
    @Column(name = "RET_AGE_MAX")
    public int getRetAgeMax() {
        return retAgeMax;
    }

    public void setRetAgeMax(int retAgeMax) {
        this.retAgeMax = retAgeMax;
    }

    @Basic
    @Column(name = "RET_CAL_YEAR_MIN")
    public int getRetCalYearMin() {
        return retCalYearMin;
    }

    public void setRetCalYearMin(int retCalYearMin) {
        this.retCalYearMin = retCalYearMin;
    }

    @Basic
    @Column(name = "RET_CAL_YEAR_MAX")
    public int getRetCalYearMax() {
        return retCalYearMax;
    }

    public void setRetCalYearMax(int retCalYearMax) {
        this.retCalYearMax = retCalYearMax;
    }

    @Basic
    @Column(name = "RET_DURATION_MIN")
    public int getRetDurationMin() {
        return retDurationMin;
    }

    public void setRetDurationMin(int retDurationMin) {
        this.retDurationMin = retDurationMin;
    }

    @Basic
    @Column(name = "RET_DURATION_MAX")
    public int getRetDurationMax() {
        return retDurationMax;
    }

    public void setRetDurationMax(int retDurationMax) {
        this.retDurationMax = retDurationMax;
    }

    @Basic
    @Column(name = "RET_LATEST_VERSION")
    public boolean isRetLatestVersion() {
        return retLatestVersion;
    }

    public void setRetLatestVersion(boolean retLatestVersion) {
        this.retLatestVersion = retLatestVersion;
    }

    @Basic
    @Column(name = "RET_TECH_REPORT")
    public String getRetTechReport() {
        return retTechReport;
    }

    public void setRetTechReport(String retTechReport) {
        this.retTechReport = retTechReport;
    }

    @Basic
    @Column(name = "RET_FUNC_REPORT")
    public String getRetFuncReport() {
        return retFuncReport;
    }

    public void setRetFuncReport(String retFuncReport) {
        this.retFuncReport = retFuncReport;
    }

    @Basic
    @Column(name = "RET_CREATION_DATE")
    public Date getRetCreationDate() {
        return retCreationDate;
    }

    public void setRetCreationDate(Date retCreationDate) {
        this.retCreationDate = retCreationDate;
    }


    @Basic
    @Column(name = "RET_SOURCE")
    public String getRetSource() {
        return retSource;
    }

    public void setRetSource(String retSource) {
        this.retSource = retSource;
    }


    @Basic
    @Column(name = "RET_COMP_REPORT")
    public String getRetCompReport() {
        return retCompReport;
    }

    public void setRetCompReport(String retCompReport) {
        this.retCompReport = retCompReport;
    }

    @ManyToOne
    @JoinColumn( name = "RET_RC_ID" , insertable = false , updatable = false )
    public RefCountryEntity getCountry() {
        return country;
    }

    public void setCountry(RefCountryEntity country) {
        this.country = country;
    }

    @Basic
    @Column( name = "RET_DIMENSIONS" )
    public String getRetDimensions() {
        return retDimensions;
    }

    public void setRetDimensions(String val){this.retDimensions = val;}

    @ManyToOne
    @JoinColumn( name = "RET_CREATED_BY")
    public RefUserEntity getRetCreatedBy() {
        return retCreatedBy;
    }

    public void setRetCreatedBy(RefUserEntity retCreatedBy) {
        this.retCreatedBy = retCreatedBy;
    }

    @Basic
    @Column( name = "RET_STATUS")
    public String getRetStatus() {
        return retStatus;
    }

    public void setRetStatus(String retStatus) {
        this.retStatus = retStatus;
    }

    @Basic
    @Column( name = "RET_IS_CONFIDENTIAL")
	public boolean isRetIsConfidential() {
		return retIsConfidential;
	}

	public void setRetIsConfidential(boolean retIsConfidential) {
		this.retIsConfidential = retIsConfidential;
	}

}
