//package com.scor.dataProcessing.bulkInsert.models;
//
//import java.io.Serializable;
//import java.util.stream.Collectors;
//
//import com.scor.persistance.entities.RefLobEntity;
//import com.scor.persistance.entities.StudyEntity;
//
//public class Study implements Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -3862725261446568617L;
//
//	private int id;
//	private String code;
//	private String status;
////	ST_REQUESTER
//	private String requester ; 
////	ST_DATASOURCE
//	private String dataSource;
////	ST_COMMENT
//	private String st_Comment ;
////	ST_CLIENT
//	private String St_Client;
////	ST_CLIENT_GROUP
//	private String StClient_Group;
////	ST_CLIENT_SHORT_NAME
//	private String stShortName ;
////	ST_DISTRIBUTION_BRAND
//	private String  stDistributionBrand ;
////	ST_CLIENT_COUNTRY
//	private String St_Client_Country;
////	ST_TREATY_NUMBER
//	private String St_Treaty_Number ;
////	ST_LINE_BUSINESS
//	private String  St_Line_Business ;
////	ST_CALCULATION_ENGINE
//	private String St_Calculation_Engine;
//	
//	public Study() {
//
//	}
//
//	public Study(StudyEntity study) {
//		this.id = study.getStId();
//		this.code = study.getStCode();
//		this.status = study.getStStatus();
//		this.requester = study.getRefRequesterByStStcId().getStcName();
//		this.dataSource = study.getRefDataSourceByStRdsId().getRdsName();
//		this.st_Comment = study.getStComment();
//		this.St_Client = study.getRefCedentNameByStRcnId().getRcnShortName();
//		this.StClient_Group = study.getRefParentGroupByStRpgId().getRpgName();
//		this.stShortName = study.getStShortName();
//		this.stDistributionBrand = study.getStDistributionBrand();
//		this.St_Client_Country = study.getRefCedentNameByStRcnId().getRefCountryById().getRcName();
//		this.St_Treaty_Number = study.getRefTreatyByStRtId()!=null ? study.getRefTreatyByStRtId().getRtName() : null;
//		this.St_Line_Business = study.getRefLobsById().stream().map(RefLobEntity::getRlobName)
//				.collect(Collectors.toList()).toString();
//		this.St_Calculation_Engine = study.getRefCalculationEngineTypeByStRcetId().getRcetName();
//	}
//
//
//
//	public String getSt_Comment() {
//		return st_Comment;
//	}
//
//	public void setSt_Comment(String st_Comment) {
//		this.st_Comment = st_Comment;
//	}
//
//	public String getSt_Client() {
//		return St_Client;
//	}
//
//	public void setSt_Client(String st_Client) {
//		St_Client = st_Client;
//	}
//
//	public String getStClient_Group() {
//		return StClient_Group;
//	}
//
//	public void setStClient_Group(String stClient_Group) {
//		StClient_Group = stClient_Group;
//	}
//
//	public String getStShortName() {
//		return stShortName;
//	}
//
//	public void setStShortName(String stShortName) {
//		this.stShortName = stShortName;
//	}
//
//	public String getStDistributionBrand() {
//		return stDistributionBrand;
//	}
//
//	public void setStDistributionBrand(String stDistributionBrand) {
//		this.stDistributionBrand = stDistributionBrand;
//	}
//
//	public String getSt_Client_Country() {
//		return St_Client_Country;
//	}
//
//	public void setSt_Client_Country(String st_Client_Country) {
//		St_Client_Country = st_Client_Country;
//	}
//
//	public String getSt_Treaty_Number() {
//		return St_Treaty_Number;
//	}
//
//	public void setSt_Treaty_Number(String st_Treaty_Number) {
//		St_Treaty_Number = st_Treaty_Number;
//	}
//
//	public String getSt_Line_Business() {
//		return St_Line_Business;
//	}
//
//	public void setSt_Line_Business(String st_Line_Business) {
//		St_Line_Business = st_Line_Business;
//	}
//
//	public String getSt_Calculation_Engine() {
//		return St_Calculation_Engine;
//	}
//
//	public void setSt_Calculation_Engine(String st_Calculation_Engine) {
//		St_Calculation_Engine = st_Calculation_Engine;
//	}
//
//	public static long getSerialversionuid() {
//		return serialVersionUID;
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
//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
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
//	public String getRequester() {
//		return requester;
//	}
//
//	public void setRequester(String requester) {
//		this.requester = requester;
//	}
//
//	public String getDataSource() {
//		return dataSource;
//	}
//
//	public void setDataSource(String dataSource) {
//		this.dataSource = dataSource;
//	}
//
//}
