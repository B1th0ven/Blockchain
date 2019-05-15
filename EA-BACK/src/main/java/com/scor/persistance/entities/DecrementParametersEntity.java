package com.scor.persistance.entities;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "DECREMENT_PARAMETERS", schema = "dbo", catalog = "EXPAN")
public class DecrementParametersEntity  implements Serializable {
    private int dpId;
    private String dpDecrement;
    private Boolean dpSlicingDimensionAge;
    private Boolean dpSlicingDimensionDuration;
    private Boolean dpSlicingDimensionCalenderYear;
    private String dpLeadingSlicingDimension;
    private String dpSlicingGranularity;
    private String dpMonthlyDuration;
    private String dpMonthlyDurationBy;
    private Date dpStudyPeriodStartDate;
    private Date dpStudyPeriodEndDate;
    private Boolean dpStudyPeriodTruncated;
    private Boolean dpIncludePartial;
    private String dpAttainedAgeDef;
    private String dpExpMethod;
    private Collection<DecrementExpectedTableEntity> decrementExpectedTablesByDpId;
    private RunEntity runByDpRunId;
    private Integer dbExpCalibration;
    private String dbCalibrationUrl;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DP_ID")
    public int getDpId() {
        return dpId;
    }

    public void setDpId(int dpId) {
        this.dpId = dpId;
    }

    @Basic
    @Column(name = "DP_DECREMENT")
    public String getDpDecrement() {
        return dpDecrement;
    }

    public void setDpDecrement(String dpDecrement) {
        this.dpDecrement = dpDecrement;
    }

    @Basic
    @Column(name = "DP_SLICING_DIMENSION_AGE")
    public Boolean getDpSlicingDimensionAge() {
        return dpSlicingDimensionAge;
    }

    public void setDpSlicingDimensionAge(Boolean dpSlicingDimensionAge) {
        this.dpSlicingDimensionAge = dpSlicingDimensionAge;
    }

    @Basic
    @Column(name = "DP_SLICING_DIMENSION_DURATION")
    public Boolean getDpSlicingDimensionDuration() {
        return dpSlicingDimensionDuration;
    }

    public void setDpSlicingDimensionDuration(Boolean dpSlicingDimensionDuration) {
        this.dpSlicingDimensionDuration = dpSlicingDimensionDuration;
    }

    @Basic
    @Column(name = "DP_SLICING_DIM_CALENDER_YEAR")
    public Boolean getDpSlicingDimensionCalenderYear() {
        return dpSlicingDimensionCalenderYear;
    }

    public void setDpSlicingDimensionCalenderYear(Boolean dpSlicingDimensionCalenderYear) {
        this.dpSlicingDimensionCalenderYear = dpSlicingDimensionCalenderYear;
    }

    @Basic
    @Column(name = "DP_LEADING_SLICING_DIMENSION")
    public String getDpLeadingSlicingDimension() {
        return dpLeadingSlicingDimension;
    }

    public void setDpLeadingSlicingDimension(String dpLeadingSlicingDimension) {
        this.dpLeadingSlicingDimension = dpLeadingSlicingDimension;
    }

    @Basic
    @Column(name = "DP_SLICING_GRANULARITY")
    public String getDpSlicingGranularity() {
        return dpSlicingGranularity;
    }

    public void setDpSlicingGranularity(String dpSlicingGranularity) {
        this.dpSlicingGranularity = dpSlicingGranularity;
    }

    @Basic
    @Column(name = "DP_MONTHLY_DURATION")
    public String getDpMonthlyDuration() {
        return dpMonthlyDuration;
    }

    public void setDpMonthlyDuration(String dpMonthlyDuration) {
        this.dpMonthlyDuration = dpMonthlyDuration;
    }

    @Basic
    @Column(name = "DP_MONTHLY_DURATION_BY")
    public String getDpMonthlyDurationBy() {
        return dpMonthlyDurationBy;
    }

    public void setDpMonthlyDurationBy(String dpMonthlyDurationBy) {
        this.dpMonthlyDurationBy = dpMonthlyDurationBy;
    }

    @Basic
    @Column(name = "DP_STUDY_PERIOD_START_DATE")
    public Date getDpStudyPeriodStartDate() {
        return dpStudyPeriodStartDate;
    }

    public void setDpStudyPeriodStartDate(Date dpStudyPeriodStartDate) {
        this.dpStudyPeriodStartDate = dpStudyPeriodStartDate;
    }

    @Basic
    @Column(name = "DP_STUDY_PERIOD_END_DATE")
    public Date getDpStudyPeriodEndDate() {
        return dpStudyPeriodEndDate;
    }

    public void setDpStudyPeriodEndDate(Date dpStudyPeriodEndDate) {
        this.dpStudyPeriodEndDate = dpStudyPeriodEndDate;
    }

    @Basic
    @Column(name = "DP_STUDY_PERIOD_TRUNCATED")
    public Boolean getDpStudyPeriodTruncated() {
        return dpStudyPeriodTruncated;
    }

    public void setDpStudyPeriodTruncated(Boolean dpStudyPeriodTruncated) {
        this.dpStudyPeriodTruncated = dpStudyPeriodTruncated;
    }

    @Basic
    @Column(name = "DP_INCLUDE_PARTIAL")
    public Boolean getDpIncludePartial() {
        return dpIncludePartial;
    }

    public void setDpIncludePartial(Boolean dpIncludePartial) {
        this.dpIncludePartial = dpIncludePartial;
    }

    @Basic
    @Column(name = "DP_ATTAINED_AGE_DEF")
    public String getDpAttainedAgeDef() {
        return dpAttainedAgeDef;
    }

    public void setDpAttainedAgeDef(String dpAttainedAgeDef) {
        this.dpAttainedAgeDef = dpAttainedAgeDef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DecrementParametersEntity)) return false;
        DecrementParametersEntity that = (DecrementParametersEntity) o;
        return getDpId() == that.getDpId() &&
                Objects.equals(getDpDecrement(), that.getDpDecrement()) &&
                Objects.equals(getDpSlicingDimensionAge(), that.getDpSlicingDimensionAge()) &&
                Objects.equals(getDpSlicingDimensionDuration(), that.getDpSlicingDimensionDuration()) &&
                Objects.equals(getDpSlicingDimensionCalenderYear(), that.getDpSlicingDimensionCalenderYear()) &&
                Objects.equals(getDpLeadingSlicingDimension(), that.getDpLeadingSlicingDimension()) &&
                Objects.equals(getDpSlicingGranularity(), that.getDpSlicingGranularity()) &&
                Objects.equals(getDpMonthlyDuration(), that.getDpMonthlyDuration()) &&
                Objects.equals(getDpMonthlyDurationBy(), that.getDpMonthlyDurationBy()) &&
                Objects.equals(getDpStudyPeriodStartDate(), that.getDpStudyPeriodStartDate()) &&
                Objects.equals(getDpStudyPeriodEndDate(), that.getDpStudyPeriodEndDate()) &&
                Objects.equals(getDpStudyPeriodTruncated(), that.getDpStudyPeriodTruncated()) &&
                Objects.equals(getDpIncludePartial(), that.getDpIncludePartial()) &&
                Objects.equals(getDpAttainedAgeDef(), that.getDpAttainedAgeDef()) &&
                Objects.equals(getDecrementExpectedTablesByDpId(), that.getDecrementExpectedTablesByDpId()) &&
                Objects.equals(getRunByDpRunId(), that.getRunByDpRunId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDpId(), getDpDecrement(), getDpSlicingDimensionAge(), getDpSlicingDimensionDuration(), getDpSlicingDimensionCalenderYear(), getDpLeadingSlicingDimension(), getDpSlicingGranularity(), getDpMonthlyDuration(), getDpMonthlyDurationBy(), getDpStudyPeriodStartDate(), getDpStudyPeriodEndDate(), getDpStudyPeriodTruncated(), getDpIncludePartial(), getDpAttainedAgeDef(), getDecrementExpectedTablesByDpId(), getRunByDpRunId());
    }


    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "RET_DP_ID", referencedColumnName = "DP_ID")
    @JsonManagedReference
    public Collection<DecrementExpectedTableEntity> getDecrementExpectedTablesByDpId() {
        return decrementExpectedTablesByDpId;
    }

    public void setDecrementExpectedTablesByDpId(Collection<DecrementExpectedTableEntity> decrementExpectedTablesByDpId) {
        this.decrementExpectedTablesByDpId = decrementExpectedTablesByDpId;
    }

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "DP_RUN_ID", referencedColumnName = "RUN_ID")
    public RunEntity getRunByDpRunId() {
        return runByDpRunId;
    }

    public void setRunByDpRunId(RunEntity runByDpRunId) {
        this.runByDpRunId = runByDpRunId;
}

    @Basic
    @Column(name = "DP_EXP_METHOD")
    public String getDpExpMethod() {
        return dpExpMethod;
    }

    public void setDpExpMethod(String dpExpMethod) {
        this.dpExpMethod = dpExpMethod;
    }

    @Basic
    @Column(name = "DP_EXP_Calibration")
	public Integer getDbExpCalibration() {
		return dbExpCalibration;
	}

	public void setDbExpCalibration(Integer dbExpCalibration) {
		this.dbExpCalibration = dbExpCalibration;
	}

	@Basic
    @Column(name = "DP_CALIBRATION_URL")
	public String getDbCalibrationUrl() {
		return dbCalibrationUrl;
	}

	public void setDbCalibrationUrl(String dbCalibrationUrl) {
		this.dbCalibrationUrl = dbCalibrationUrl;
	}
	
	
    
    
}
