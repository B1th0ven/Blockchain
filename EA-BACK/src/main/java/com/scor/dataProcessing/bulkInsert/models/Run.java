//package com.scor.dataProcessing.bulkInsert.models;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//
//import com.scor.persistance.entities.RunEntity;
//
//public class Run implements Serializable {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 4107956184319605392L;
////	RUN_ID
//	private int id;
////	RUN_CODE
//	private String code;
////	RUN_ST_ID
//	private int studyId;
////	RUN_DS_ID
//	private Integer datasetId;
////	RUN_STATUS
//	private String status;
////	RUN_DIMENSIONS
////(runFilterJsonUrl)
//	private String dimensions ;
////	RUN_DESCRIPTION
//// (runDescription)
//	private String description ;
////	RUN_EXPOSURE_METRIC
//	private String exposureMetric;
////	RUN_BY_COUNT_ANALYSIS
////	(runByCountAnalysis)
//	private Boolean countAnalysis;
////	RUN_BY_AMOUNT_ANALYSIS
////	(runByAmountAnalysis)
//	private Boolean byamountAnalysis ;
////	RUN_BY_AMOUNT_ANALYSIS_BASIS
////	(runByAmountAnalysisBasis)
//	private String byamountAnalysisBasis ;
////	RUN_BY_AMOUNT_CAPPED
//// (runByAmountCapped)
//	private Boolean byamountCapped ;
////	RUN_CAPPED_AMOUNT
//// (runCappedAmount)
//	private BigDecimal cappedAmount ;
////	RUN_LOSS_RATIO_ANALYSIS
//// (runLossRatioAnalysis)
//	private Boolean lossRatioAnalysis ;
////	RUN_LOSS_RATIO_ANALYSIS_BASIS
//// (runLossRatioAnalysisBasis)
//	private String lossRatioAnalysisBasis ;
//
//
////	RUN_AUT_RISK_AMOUNT_CHANGE (runAutRiskAmountChangeSplit)
//	private Boolean autRiskAmountChangeSplit ;
////	RUN_RATING (runRating)
//	private String rating ;
////	RUN_LIFE_JOINT (runLifeJoint)
//	private String lifeJoint ;
////	RUN_IBNR_METHOD
//
//	private String ibnrMethod;
//	// MASTER_RUN_QX
//	private Boolean masterRunQx;
//	//MASTER_RUN_IX
//	private Boolean masterRunIx;
//	// MASTER_RUN_IXQX
//	private Boolean masterRunIxQx;
//	//MASTER_RUN_WX
//	private Boolean masterRunWx;
//	//RUN_RETAINED
//	private Boolean runRetained;
//
//	public Run(RunEntity runEntity) {
//
//		this.id = runEntity.getRunId();
//		this.code =  runEntity.getRunCode();
//		this.studyId =  runEntity.getStudy().getStId();
//		this.datasetId =  runEntity.getRunDsId();
//		this.status =  runEntity.getRunStatus();
//		this.dimensions =  runEntity.getRunFilterJsonUrl();
//		this.description =  runEntity.getRunDescription();
//		this.exposureMetric =  runEntity.getRunExposureMetric();
//		this.countAnalysis =  runEntity.getRunByCountAnalysis();
//		this.byamountAnalysis =  runEntity.getRunByAmountAnalysis();
//		this.byamountAnalysisBasis =  runEntity.getRunByAmountAnalysisBasis();
//		this.byamountCapped =  runEntity.getRunByAmountCapped();
//		this.cappedAmount =  runEntity.getRunCappedAmount();
//		this.lossRatioAnalysis =  runEntity.getRunLossRatioAnalysis();
//		this.lossRatioAnalysisBasis =  runEntity.getRunLossRatioAnalysisBasis();
//		this.autRiskAmountChangeSplit =  runEntity.getRunAutRiskAmountChangeSplit();
//		this.rating =  runEntity.getRunRating();
//		this.lifeJoint =  runEntity.getRunLifeJoint();
//		this.ibnrMethod =  runEntity.getRunIbnrMethod();
//		this.masterRunQx =  runEntity.getMasterRunQx();
//		this.masterRunIx =  runEntity.getMasterRunIx();
//		this.masterRunIxQx =  runEntity.getMasterRunIxQx();
//		this.masterRunWx =  runEntity.getMasterRunWx();
//		this.runRetained =  runEntity.getRunRetained();
//	}
//
//
//
//	public Run() {
//
//	}
//
//
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}
//
//	public int getStudyId() {
//		return studyId;
//	}
//
//	public void setStudyId(int studyId) {
//		this.studyId = studyId;
//	}
//
//	public Integer getDatasetId() {
//		return datasetId;
//	}
//
//	public void setDatasetId(Integer datasetId) {
//		this.datasetId = datasetId;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	public String getDimensions() {
//		return dimensions;
//	}
//
//	public void setDimensions(String dimensions) {
//		this.dimensions = dimensions;
//	}
//
//
//
//public String getDescription() {
//		return description;
//	}
//
//
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//
//
//	public String getExposureMetric() {
//		return exposureMetric;
//	}
//
//
//
//	public void setExposureMetric(String exposureMetric) {
//		this.exposureMetric = exposureMetric;
//	}
//
//
//
//	public Boolean isCountAnalysis() {
//		return countAnalysis;
//	}
//
//
//
//	public void setCountAnalysis(Boolean countAnalysis) {
//		this.countAnalysis = countAnalysis;
//	}
//
//
//
//	public Boolean isByamountAnalysis() {
//		return byamountAnalysis;
//	}
//
//
//
//	public void setByamountAnalysis(Boolean byamountAnalysis) {
//		this.byamountAnalysis = byamountAnalysis;
//	}
//
//
//
//	public String getByamountAnalysisBasis() {
//		return byamountAnalysisBasis;
//	}
//
//
//
//	public void setByamountAnalysisBasis(String byamountAnalysisBasis) {
//		this.byamountAnalysisBasis = byamountAnalysisBasis;
//	}
//
//
//
//	public Boolean isByamountCapped() {
//		return byamountCapped;
//	}
//
//
//
//	public void setByamountCapped(Boolean byamountCapped) {
//		this.byamountCapped = byamountCapped;
//	}
//
//
//
//	public BigDecimal getCappedAmount() {
//		return cappedAmount;
//	}
//
//
//
//	public void setCappedAmount(BigDecimal cappedAmount) {
//		this.cappedAmount = cappedAmount;
//	}
//
//
//
//	public Boolean isLossRatioAnalysis() {
//		return lossRatioAnalysis;
//	}
//
//
//
//	public void setLossRatioAnalysis(Boolean lossRatioAnalysis) {
//		this.lossRatioAnalysis = lossRatioAnalysis;
//	}
//
//
//
//	public String getLossRatioAnalysisBasis() {
//		return lossRatioAnalysisBasis;
//	}
//
//
//
//	public void setLossRatioAnalysisBasis(String lossRatioAnalysisBasis) {
//		this.lossRatioAnalysisBasis = lossRatioAnalysisBasis;
//	}
//
//
//
//	public Boolean isAutRiskAmountChangeSplit() {
//		return autRiskAmountChangeSplit;
//	}
//
//
//
//	public void setAutRiskAmountChangeSplit(Boolean autRiskAmountChangeSplit) {
//		this.autRiskAmountChangeSplit = autRiskAmountChangeSplit;
//	}
//
//
//
//	public String getRating() {
//		return rating;
//	}
//
//
//
//	public void setRating(String rating) {
//		this.rating = rating;
//	}
//
//
//
//	public String getLifeJoint() {
//		return lifeJoint;
//	}
//
//
//
//	public void setLifeJoint(String lifeJoint) {
//		this.lifeJoint = lifeJoint;
//	}
//
//
//
//	public String getIbnrMethod() {
//		return ibnrMethod;
//	}
//
//
//
//	public void setIbnrMethod(String ibnrMethod) {
//		this.ibnrMethod = ibnrMethod;
//	}
//
//
//
//
//	public Boolean getMasterRunQx() {
//		return masterRunQx;
//	}
//
//
//
//	public void setMasterRunQx(Boolean masterRunQx) {
//		this.masterRunQx = masterRunQx;
//	}
//
//
//
//	public Boolean getMasterRunIx() {
//		return masterRunIx;
//	}
//
//
//
//	public void setMasterRunIx(Boolean masterRunIx) {
//		this.masterRunIx = masterRunIx;
//	}
//
//
//
//	public Boolean getMasterRunIxQx() {
//		return masterRunIxQx;
//	}
//
//
//
//	public void setMasterRunIxQx(Boolean masterRunIxQx) {
//		this.masterRunIxQx = masterRunIxQx;
//	}
//
//
//
//	public Boolean getMasterRunWx() {
//		return masterRunWx;
//	}
//
//
//
//	public void setMasterRunWx(Boolean masterRunWx) {
//		this.masterRunWx = masterRunWx;
//	}
//
//
//
//	public Boolean getRunRetained() {
//		return runRetained;
//	}
//
//
//
//	public void setRunRetained(Boolean runRetained) {
//		this.runRetained = runRetained;
//	}
//
//
//
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
//
//
//}
