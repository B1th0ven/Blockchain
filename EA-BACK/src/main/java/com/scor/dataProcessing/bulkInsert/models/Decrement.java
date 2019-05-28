//package com.scor.dataProcessing.bulkInsert.models;
//
//import java.io.Serializable;
//// Decrement
//import java.sql.Date;
//import java.util.stream.Collectors;
//
//import com.scor.persistance.entities.DecrementExpectedTableEntity;
//import com.scor.persistance.entities.DecrementParametersEntity;
//import com.scor.persistance.entities.RefExpectedTableEntity;
//public class Decrement implements Serializable  {
//
//	private static final long serialVersionUID = 3165998868199094112L;
//	//	DP_RUN_ID
//	// *foreign key run table*
//	private Integer dpRunId; 
//	//	DP_ID
//	private Integer dpId ; 
//	//	DP_DECREMENT
//	private String dpDecrement ; 
//	//	DP_SLICING_DIMENSIONS
//	private String slicingDimensions ; 
//	// DP_LEADING_SLICING_DIMENSION
//	private String dpLeadingSlicingDimension ; 
//	// DP_ATTAINED_AGE_DEF
//	private String dpAttainedAgeDef ; 
//	//DP_STUDY_PERIOD_END_DATE
//	private Date dpStudyPeriodEndDate ; 
//	// DP_STUDY_PERIOD_START_DATE 
//	private Date dpStudyPeriodStartDate ; 
//	// DP_EXP_METHOD
//	private String dpExpMethod ; 
//	// BASE_EXPECTED_TABLES_IDS
//	private String refExpectedTableByRetBase ;
//	// TREND_EXPECTED_TABLES_IDS
//	private String	refExpectedTableByRetTrend ; 
//	// ADJUSTMENT_EXPECTED_TABLES_IDS
//	private String	refExpectedTableByRetAdjustment ; 
//	// POLICY_EXPECTED_TABLES_IDS
//	private String	refExpectedTableByRetPolicy ;
//
//
//	public Decrement() {
//	}
//
//	public Decrement(DecrementParametersEntity decrementEntity) {
//
//		this.dpRunId = decrementEntity.getRunByDpRunId().getRunId();
//		this.dpId = decrementEntity.getDpId();
//		this.dpDecrement = decrementEntity.getDpDecrement();
//		this.slicingDimensions = "[" + (decrementEntity.getDpSlicingDimensionAge() ? "Attained Age," : "")
//				+ (decrementEntity.getDpSlicingDimensionCalenderYear() ? "Calendar Year," : "")
//				+ (decrementEntity.getDpSlicingDimensionDuration() ? "Policy Duration" : "") + "]";
//		this.dpLeadingSlicingDimension = decrementEntity.getDpLeadingSlicingDimension();
//		this.dpAttainedAgeDef = decrementEntity.getDpAttainedAgeDef();
//		this.dpStudyPeriodEndDate = decrementEntity.getDpStudyPeriodEndDate();
//		this.dpStudyPeriodStartDate = decrementEntity.getDpStudyPeriodStartDate();
//		this.dpExpMethod = decrementEntity.getDpExpMethod();
//		this.refExpectedTableByRetBase = decrementEntity.getDecrementExpectedTablesByDpId().stream()
//				.map(DecrementExpectedTableEntity::getRefExpectedTableByRetBase).map(ele -> ele !=null ? ele.getRetCode() : null)
//				.collect(Collectors.toList()).toString();
//		this.refExpectedTableByRetTrend = decrementEntity.getDecrementExpectedTablesByDpId().stream()
//				.map(DecrementExpectedTableEntity::getRefExpectedTableByRetTrend)
//				.map(ele -> ele !=null ? ele.getRetCode() : "").collect(Collectors.toList()).toString();
//				
//		
//		this.refExpectedTableByRetAdjustment = decrementEntity.getDecrementExpectedTablesByDpId().stream()
//				.map(DecrementExpectedTableEntity::getRefExpectedTableByRetAdjustment)
//				.map(ele -> ele !=null ? ele.getRetCode() : "").collect(Collectors.toList()).toString();
//		;
//		this.refExpectedTableByRetPolicy = decrementEntity.getDecrementExpectedTablesByDpId().stream()
//				.map(DecrementExpectedTableEntity::getRefExpectedTableByRetPolicy)
//				.map(ele -> ele !=null ? ele.getRetCode() : "").collect(Collectors.toList()).toString();
//		
//	}
//
//	public Integer getDpRunId() {
//		return dpRunId;
//	}
//
//	public void setDpRunId(Integer dpRunId) {
//		this.dpRunId = dpRunId;
//	}
//
//	public Integer getDpId() {
//		return dpId;
//	}
//
//	public void setDpId(Integer dpId) {
//		this.dpId = dpId;
//	}
//
//	public String getDpDecrement() {
//		return dpDecrement;
//	}
//
//	public void setDpDecrement(String dpDecrement) {
//		this.dpDecrement = dpDecrement;
//	}
//
//	public String getSlicingDimensions() {
//		return slicingDimensions;
//	}
//
//	public void setSlicingDimensions(String slicingDimensions) {
//		this.slicingDimensions = slicingDimensions;
//	}
//
//	public String getDpLeadingSlicingDimension() {
//		return dpLeadingSlicingDimension;
//	}
//
//	public void setDpLeadingSlicingDimension(String dpLeadingSlicingDimension) {
//		this.dpLeadingSlicingDimension = dpLeadingSlicingDimension;
//	}
//
//	public String getDpAttainedAgeDef() {
//		return dpAttainedAgeDef;
//	}
//
//	public void setDpAttainedAgeDef(String dpAttainedAgeDef) {
//		this.dpAttainedAgeDef = dpAttainedAgeDef;
//	}
//
//	public Date getDpStudyPeriodEndDate() {
//		return dpStudyPeriodEndDate;
//	}
//
//	public void setDpStudyPeriodEndDate(Date dpStudyPeriodEndDate) {
//		this.dpStudyPeriodEndDate = dpStudyPeriodEndDate;
//	}
//
//	public Date getDpStudyPeriodStartDate() {
//		return dpStudyPeriodStartDate;
//	}
//
//	public void setDpStudyPeriodStartDate(Date dpStudyPeriodStartDate) {
//		this.dpStudyPeriodStartDate = dpStudyPeriodStartDate;
//	}
//
//	public String getDpExpMethod() {
//		return dpExpMethod;
//	}
//
//	public void setDpExpMethod(String dpExpMethod) {
//		this.dpExpMethod = dpExpMethod;
//	}
//
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
//
//
//
//	public String getRefExpectedTableByRetTrend() {
//		return refExpectedTableByRetTrend;
//	}
//
//	public void setRefExpectedTableByRetTrend(String refExpectedTableByRetTrend) {
//		this.refExpectedTableByRetTrend = refExpectedTableByRetTrend;
//	}
//
//	public String getRefExpectedTableByRetAdjustment() {
//		return refExpectedTableByRetAdjustment;
//	}
//
//	public void setRefExpectedTableByRetAdjustment(String refExpectedTableByRetAdjustment) {
//		this.refExpectedTableByRetAdjustment = refExpectedTableByRetAdjustment;
//	}
//
//	public String getRefExpectedTableByRetPolicy() {
//		return refExpectedTableByRetPolicy;
//	}
//
//	public void setRefExpectedTableByRetPolicy(String refExpectedTableByRetPolicy) {
//		this.refExpectedTableByRetPolicy = refExpectedTableByRetPolicy;
//	}
//
//	public String getRefExpectedTableByRetBase() {
//		return refExpectedTableByRetBase;
//	}
//
//	public void setRefExpectedTableByRetBase(String refExpectedTableByRetBase) {
//		this.refExpectedTableByRetBase = refExpectedTableByRetBase;
//	}
//	
//}
