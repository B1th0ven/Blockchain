//package com.scor.dataProcessing.bulkInsert.models;
//
//import java.io.Serializable;
//import java.sql.Date;
//
//import com.scor.persistance.entities.DataSetEntity;
//
//public class Dataset implements Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 8177433590330404130L;
////	DS_ID
//	private int id;
////	DS_NAME
//	private String name;
////	DS_CODE
//	private String code;
////	DS_EXPOSURE_EXTRACTION_DATE
//	private String exposureExtractionDate;
////	DS_EVENT_EXTRACTION_DATE
//	private String eventExtractionDate;
////	DS_DATA_STRUCTURE_TYPE
//	private String dataStructureType;
////	DS_EVENT_EXPOSURE_FILE_ID
//	private Integer eventExposureFileId;
////	DS_PRODUCT_FILE_ID
//	private Integer productFileId;
////	DS_TECH_REPORT
//	private String techReport;
////	DS_FUNC_REPORT
//	private String funcReport;
////	DS_NOT_EXECUTED
//	private String notExecutedReport;
////	DS_COMMENT
//	private String comment;
////	DS_CREATED_DATE
//	private Date creationDate;
////	DS_CREATED_BY
//	private Integer runId;
//// DS_DataDeletion
//	private Date DataDeletion ; 
//// DS_DataRestriction 
//	private String DataRestriction ; 
//	
//	public Dataset() {
//		
//	}
//	public Dataset(DataSetEntity datasetEntity) {
//		this.id  =datasetEntity.getDsId();
//		this.code=datasetEntity.getDsCode();
//		this.name=datasetEntity.getDsName();
//		this.comment=datasetEntity.getDsComment();
//		this.creationDate=datasetEntity.getDsCreatedDate();
//		this.dataStructureType=datasetEntity.getDsDataStructureType();
//		this.eventExposureFileId=datasetEntity.getDsEventExposureFile().getEafId();
//		this.eventExtractionDate=datasetEntity.getDsEventExtractionDate();
//		this.exposureExtractionDate=datasetEntity.getDsExposureExtractionDate();
//		this.funcReport=datasetEntity.getDsFuncReport();
//		this.notExecutedReport=datasetEntity.getDsNotExecuted();
//		this.productFileId=datasetEntity.getDsProductFile().getEafId();
//		this.techReport=datasetEntity.getDsTechReport();
//		this.DataDeletion=(Date) datasetEntity.getDsEventExposureFile().getEafDataDeletion() ; 
//		this.DataRestriction=datasetEntity.getDsEventExposureFile().getEafDataRestriction() ; 
//	}
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
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
//	public String getExposureExtractionDate() {
//		return exposureExtractionDate;
//	}
//
//	public void setExposureExtractionDate(String exposureExtractionDate) {
//		this.exposureExtractionDate = exposureExtractionDate;
//	}
//
//	public String getEventExtractionDate() {
//		return eventExtractionDate;
//	}
//
//	public void setEventExtractionDate(String eventExtractionDate) {
//		this.eventExtractionDate = eventExtractionDate;
//	}
//
//	public String getDataStructureType() {
//		return dataStructureType;
//	}
//
//	public void setDataStructureType(String dataStructureType) {
//		this.dataStructureType = dataStructureType;
//	}
//
//	public Integer getEventExposureFileId() {
//		return eventExposureFileId;
//	}
//
//	public void setEventExposureFileId(Integer eventExposureFileId) {
//		this.eventExposureFileId = eventExposureFileId;
//	}
//
//	public Integer getProductFileId() {
//		return productFileId;
//	}
//
//	public void setProductFileId(Integer productFileId) {
//		this.productFileId = productFileId;
//	}
//
//	public String getTechReport() {
//		return techReport;
//	}
//
//	public void setTechReport(String techReport) {
//		this.techReport = techReport;
//	}
//
//	public String getFuncReport() {
//		return funcReport;
//	}
//
//	public void setFuncReport(String funcReport) {
//		this.funcReport = funcReport;
//	}
//
//	public String getNotExecutedReport() {
//		return notExecutedReport;
//	}
//
//	public void setNotExecutedReport(String notExecutedReport) {
//		this.notExecutedReport = notExecutedReport;
//	}
//
//	public String getComment() {
//		return comment;
//	}
//
//	public void setComment(String comment) {
//		this.comment = comment;
//	}
//
//	public Date getCreationDate() {
//		return creationDate;
//	}
//
//	public void setCreationDate(Date creationDate) {
//		this.creationDate = creationDate;
//	}
//
//	public Integer getRunId() {
//		return runId;
//	}
//
//	public void setRunId(Integer runId) {
//		this.runId = runId;
//	}
//	public Date getDataDeletion() {
//		return DataDeletion;
//	}
//	public void setDataDeletion(Date dataDeletion) {
//		DataDeletion = dataDeletion;
//	}
//	public String getDataRestriction() {
//		return DataRestriction;
//	}
//	public void setDataRestriction(String dataRestriction) {
//		DataRestriction = dataRestriction;
//	}
//	
//	
//
//}
