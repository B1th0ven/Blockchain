package com.scor.persistance.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "RUN", schema = "dbo", catalog = "EXPAN")
public class RunEntity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6612451077815061620L;
	private int runId;
    private Integer runRimId;
    private String runCode;
    private String runDescription;
    private String runStatus;
    private Date runCreationDate;
    private Boolean runMasterRun;
    private RefUserEntity runCreatedBy;
    private String runExposureMetric;
    private String runAttainedAgeDef;
    private Boolean runAutRiskAmountChangeSplit;
    private Boolean runByCountAnalysis;
    private Boolean runByAmountAnalysis;
    private String runByAmountAnalysisBasis;
    private Boolean runByAmountCapped;
    private Boolean runLossRatioAnalysis;
    private String runLossRatioAnalysisBasis;
    private BigDecimal runCappedAmount;
    private String runIbnrManuelUdf;
    private String runIbnrAmount;
    private String runIbnrAllocation;
    private String runFilterJsonUrl;
    private String runIbnrMethod;
    private String runLifeJoint;
    private String runRating;
    private Integer runDsId;
    private Integer runStId;
    private Collection<DecrementParametersEntity> decrementParametersByRunId;
    
    //Master Run
    private Boolean masterRunQx;
    private Boolean masterRunIx;
    private Boolean masterRunIxQx;
    private Boolean masterRunWx;
    private Boolean runRetained;

    private RunCalcEntity runCalcEntity;
    private StudyEntity study;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RUN_ID")
    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    @Basic
    @Column(name = "RUN_RIM_ID")
    public Integer getRunRimId() {
        return runRimId;
    }

    public void setRunRimId(Integer runRimId) {
        this.runRimId = runRimId;
    }

    @Basic
    @Column(name = "RUN_CODE")
    public String getRunCode() {
        return runCode;
    }

    public void setRunCode(String runCode) {
        this.runCode = runCode;
    }

    @Basic
    @Column(name = "RUN_DESCRIPTION")
    public String getRunDescription() {
        return runDescription;
    }

    public void setRunDescription(String runDescription) {
        this.runDescription = runDescription;
    }

    @Basic
    @Column(name = "RUN_STATUS")
    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    @Basic
    @Column(name = "RUN_CREATION_DATE")
    public Date getRunCreationDate() {
        return runCreationDate;
    }

    public void setRunCreationDate(Date runCreationDate) {
        this.runCreationDate = runCreationDate;
    }

    @Basic
    @Column(name = "RUN_MASTER_RUN")
    public Boolean getRunMasterRun() {
        return runMasterRun;
    }

    public void setRunMasterRun(Boolean runMasterRun) {
        this.runMasterRun = runMasterRun;
    }

    @ManyToOne
    @JoinColumn(name = "RUN_CREATED_BY", referencedColumnName = "RU_ID")
    public RefUserEntity getRunCreatedBy() {
        return runCreatedBy;
    }

    public void setRunCreatedBy(RefUserEntity runCreatedBy) {
        this.runCreatedBy = runCreatedBy;
    }

    @Basic
    @Column(name = "RUN_EXPOSURE_METRIC")
    public String getRunExposureMetric() {
        return runExposureMetric;
    }

    public void setRunExposureMetric(String runExposureMetric) {
        this.runExposureMetric = runExposureMetric;
    }

    @Basic
    @Column(name = "RUN_ATTAINED_AGE_DEF")
    public String getRunAttainedAgeDef() {
        return runAttainedAgeDef;
    }

    public void setRunAttainedAgeDef(String runAttainedAgeDef) {
        this.runAttainedAgeDef = runAttainedAgeDef;
    }

    @Basic
    @Column(name = "RUN_AUT_RISK_AMOUNT_CHANGE_SPLIT")
    public Boolean getRunAutRiskAmountChangeSplit() {
        return runAutRiskAmountChangeSplit;
    }

    public void setRunAutRiskAmountChangeSplit(Boolean runAutRiskAmountChangeSplit) {
        this.runAutRiskAmountChangeSplit = runAutRiskAmountChangeSplit;
    }

    @Basic
    @Column(name = "RUN_BY_COUNT_ANALYSIS")
    public Boolean getRunByCountAnalysis() {
        return runByCountAnalysis;
    }

    public void setRunByCountAnalysis(Boolean runByCountAnalysis) {
        this.runByCountAnalysis = runByCountAnalysis;
    }

    @Basic
    @Column(name = "RUN_BY_AMOUNT_ANALYSIS")
    public Boolean getRunByAmountAnalysis() {
        return runByAmountAnalysis;
    }

    public void setRunByAmountAnalysis(Boolean runByAmountAnalysis) {
        this.runByAmountAnalysis = runByAmountAnalysis;
    }

    @Basic
    @Column(name = "RUN_BY_AMOUNT_ANALYSIS_BASIS")
    public String getRunByAmountAnalysisBasis() {
        return runByAmountAnalysisBasis;
    }

    public void setRunByAmountAnalysisBasis(String runByAmountAnalysisBasis) {
        this.runByAmountAnalysisBasis = runByAmountAnalysisBasis;
    }

    @Basic
    @Column(name = "RUN_BY_AMOUNT_CAPPED")
    public Boolean getRunByAmountCapped() {
        return runByAmountCapped;
    }

    public void setRunByAmountCapped(Boolean runByAmountCapped) {
        this.runByAmountCapped = runByAmountCapped;
    }

    @Basic
    @Column(name = "RUN_LOSS_RATIO_ANALYSIS")
    public Boolean getRunLossRatioAnalysis() {
        return runLossRatioAnalysis;
    }

    public void setRunLossRatioAnalysis(Boolean runLossRatioAnalysis) {
        this.runLossRatioAnalysis = runLossRatioAnalysis;
    }

    @Basic
    @Column(name = "RUN_LOSS_RATIO_ANALYSIS_BASIS")
    public String getRunLossRatioAnalysisBasis() {
        return runLossRatioAnalysisBasis;
    }

    public void setRunLossRatioAnalysisBasis(String runLossRatioAnalysisBasis) {
        this.runLossRatioAnalysisBasis = runLossRatioAnalysisBasis;
    }

    @Basic
    @Column(name = "RUN_CAPPED_AMOUNT")
    public BigDecimal getRunCappedAmount() {
        return runCappedAmount;
    }

    public void setRunCappedAmount(BigDecimal runCappedAmount) {
        this.runCappedAmount = runCappedAmount;
    }

    @Basic
    @Column(name = "RUN_IBNR_MANUEL_UDF")
    public String getRunIbnrManuelUdf() {
        return runIbnrManuelUdf;
    }

    public void setRunIbnrManuelUdf(String runIbnrManuelUdf) {
        this.runIbnrManuelUdf = runIbnrManuelUdf;
    }

    @Basic
    @Column(name = "RUN_IBNR_AMOUNT")
    public String getRunIbnrAmount() {
        return runIbnrAmount;
    }

    public void setRunIbnrAmount(String runIbnrAmount) {
        this.runIbnrAmount = runIbnrAmount;
    }

    @Basic
    @Column(name = "RUN_IBNR_ALLOCATION")
    public String getRunIbnrAllocation() {
        return runIbnrAllocation;
    }

    public void setRunIbnrAllocation(String runIbnrAllocation) {
        this.runIbnrAllocation = runIbnrAllocation;
    }

    @Basic
    @Column(name = "RUN_FILTER_JSON_URL")
    public String getRunFilterJsonUrl() {
        return runFilterJsonUrl;
    }

    public void setRunFilterJsonUrl(String runFilterJsonUrl) {
        this.runFilterJsonUrl = runFilterJsonUrl;
    }

    @Basic
    @Column(name = "RUN_IBNR_METHOD")
    public String getRunIbnrMethod() {
        return runIbnrMethod;
    }

    public void setRunIbnrMethod(String runIbnrMethod) {
        this.runIbnrMethod = runIbnrMethod;
    }

    @Basic
    @Column(name = "RUN_LIFE_JOINT")
    public String getRunLifeJoint() {
        return runLifeJoint;
    }

    public void setRunLifeJoint(String runLifeJoint) {
        this.runLifeJoint = runLifeJoint;
    }

    @Basic
    @Column(name = "RUN_RATING")
    public String getRunRating() {
        return runRating;
    }

    public void setRunRating(String runRating) {
        this.runRating = runRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RunEntity)) return false;
        RunEntity runEntity = (RunEntity) o;
        return getRunId() == runEntity.getRunId() &&
                Objects.equals(getRunRimId(), runEntity.getRunRimId()) &&
                Objects.equals(getRunCode(), runEntity.getRunCode()) &&
                Objects.equals(getRunDescription(), runEntity.getRunDescription()) &&
                Objects.equals(getRunStatus(), runEntity.getRunStatus()) &&
                Objects.equals(getRunCreationDate(), runEntity.getRunCreationDate()) &&
                Objects.equals(getRunMasterRun(), runEntity.getRunMasterRun()) &&
                Objects.equals(getRunCreatedBy(), runEntity.getRunCreatedBy()) &&
                Objects.equals(getRunExposureMetric(), runEntity.getRunExposureMetric()) &&
                Objects.equals(getRunAttainedAgeDef(), runEntity.getRunAttainedAgeDef()) &&
                Objects.equals(getRunAutRiskAmountChangeSplit(), runEntity.getRunAutRiskAmountChangeSplit()) &&
                Objects.equals(getRunByCountAnalysis(), runEntity.getRunByCountAnalysis()) &&
                Objects.equals(getRunByAmountAnalysis(), runEntity.getRunByAmountAnalysis()) &&
                Objects.equals(getRunByAmountAnalysisBasis(), runEntity.getRunByAmountAnalysisBasis()) &&
                Objects.equals(getRunByAmountCapped(), runEntity.getRunByAmountCapped()) &&
                Objects.equals(getRunLossRatioAnalysis(), runEntity.getRunLossRatioAnalysis()) &&
                Objects.equals(getRunLossRatioAnalysisBasis(), runEntity.getRunLossRatioAnalysisBasis()) &&
                Objects.equals(getRunCappedAmount(), runEntity.getRunCappedAmount()) &&
                Objects.equals(getRunIbnrManuelUdf(), runEntity.getRunIbnrManuelUdf()) &&
                Objects.equals(getRunIbnrAmount(), runEntity.getRunIbnrAmount()) &&
                Objects.equals(getRunIbnrAllocation(), runEntity.getRunIbnrAllocation()) &&
                Objects.equals(getRunFilterJsonUrl(), runEntity.getRunFilterJsonUrl()) &&
                Objects.equals(getRunIbnrMethod(), runEntity.getRunIbnrMethod()) &&
                Objects.equals(getRunLifeJoint(), runEntity.getRunLifeJoint()) &&
                Objects.equals(getRunRating(), runEntity.getRunRating()) &&
                Objects.equals(getRunDsId(), runEntity.getRunDsId()) &&
                Objects.equals(getRunStId(), runEntity.getRunStId()) &&
                Objects.equals(getDecrementParametersByRunId(), runEntity.getDecrementParametersByRunId()) &&
                Objects.equals(getRunCalcEntity(), runEntity.getRunCalcEntity());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getRunId(), getRunRimId(), getRunCode(), getRunDescription(), getRunStatus(), getRunCreationDate(), getRunMasterRun(), getRunCreatedBy(), getRunExposureMetric(), getRunAttainedAgeDef(), getRunAutRiskAmountChangeSplit(), getRunByCountAnalysis(), getRunByAmountAnalysis(), getRunByAmountAnalysisBasis(), getRunByAmountCapped(), getRunLossRatioAnalysis(), getRunLossRatioAnalysisBasis(), getRunCappedAmount(), getRunIbnrManuelUdf(), getRunIbnrAmount(), getRunIbnrAllocation(), getRunFilterJsonUrl(), getRunIbnrMethod(), getRunLifeJoint(), getRunRating(), getRunDsId(), getRunStId(), getDecrementParametersByRunId(), getRunCalcEntity());
    }

    @Basic
    @Column(name = "RUN_DS_ID")
    public Integer getRunDsId() {
        return runDsId;
    }

    public void setRunDsId(Integer runDsId) {
        this.runDsId = runDsId;
    }

    @Basic
    @Column(name = "RUN_ST_ID")
    public Integer getRunStId() {
        return runStId;
    }

    public void setRunStId(Integer runStId) {
        this.runStId = runStId;
    }


    @OneToMany(cascade = CascadeType.ALL ,orphanRemoval = true)
    @JoinColumn(name = "DP_RUN_ID")
    public Collection<DecrementParametersEntity> getDecrementParametersByRunId() {
        return decrementParametersByRunId;
    }

    public void setDecrementParametersByRunId(Collection<DecrementParametersEntity> decrementParametersByRunId) {
        this.decrementParametersByRunId = decrementParametersByRunId;
    }

    @Transient
    public RunCalcEntity getRunCalcEntity() {
        return runCalcEntity;
    }

    public void setRunCalcEntity(RunCalcEntity runCalcEntity) {
        this.runCalcEntity = runCalcEntity;
    }
    @Basic
    @Column(name = "MASTER_RUN_QX")
	public Boolean getMasterRunQx() {
		return masterRunQx;
	}

	public void setMasterRunQx(Boolean masterRunQx) {
		this.masterRunQx = masterRunQx;
	}
	@Basic
    @Column(name = "MASTER_RUN_IX")
	public Boolean getMasterRunIx() {
		return masterRunIx;
	}

	public void setMasterRunIx(Boolean masterRunIx) {
		this.masterRunIx = masterRunIx;
	}
	@Basic
    @Column(name = "MASTER_RUN_WX")
	public Boolean getMasterRunWx() {
		return masterRunWx;
	}

	public void setMasterRunWx(Boolean masterRunWx) {
		this.masterRunWx = masterRunWx;
	}
	@Basic
    @Column(name = "RUN_RETAINED")
	public Boolean getRunRetained() {
		return runRetained;
	}

	public void setRunRetained(Boolean runRetained) {
		this.runRetained = runRetained;
	}
	
	@Basic
    @Column(name = "MASTER_RUN_IXQX")
	public Boolean getMasterRunIxQx() {
		return masterRunIxQx;
	}

	public void setMasterRunIxQx(Boolean masterRunIxQx) {
		this.masterRunIxQx = masterRunIxQx;
	}
	
	@ManyToOne
    @JoinColumn(name = "RUN_ST_ID", referencedColumnName = "ST_ID" , insertable = false ,updatable = false)
    public StudyEntity getStudy() {
        return study;
    }
	
	public void setStudy(StudyEntity study) {
		this.study = study;
	}
    
    
}
