package com.scor.dataProcessing.bulkInsert.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.scalactic.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.scor.dataProcessing.bulkInsert.models.Decrement;
//import com.scor.dataProcessing.bulkInsert.models.Run;
//import com.scor.dataProcessing.bulkInsert.models.Study;

@Component
public class ResultRepository implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7334510728213650686L;

	private final static Logger LOGGER = Logger.getLogger(ResultRepository.class);
	
	@Autowired
	private JdbcConnection connection;

//	private Connection conn;

//	public ResultRepository(ResultProperties resultProperties)
//			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		getConnection(resultProperties);
//	}
//
//	private void getConnection(ResultProperties resultProperties) {
//		try {
//			String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//			Class.forName(driver);
//			String url = resultProperties.dbResultUrl;
//			LOGGER.info("Trying to open connection with database , url : " + url);
//			this.conn = DriverManager.getConnection(url, resultProperties.dbResultUsername,
//					resultProperties.dbResuPassword);
//			LOGGER.info("Connection opened with url : " + url);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public Integer booleanToBitSQL(Boolean b){
//		if(b == null){
//			return null;
//		}
//		else {
//			return (b.booleanValue()? 1 : 0);
//		}
//	}
//
//	public String stringToStringSQL(String s){
//		if(s == null){
//			return null;
//		}
//		return "'"+s+"'";
//
//	}

//	public void insertRun(Run run) throws Exception {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "INSERT INTO RUN(RUN_ID," +
//					"RUN_CODE," +
//					"RUN_ST_ID," +
//					"RUN_STATUS," +
//					"RUN_DS_ID," +
//					"RUN_DIMENSIONS," +
//					"RUN_DESCRIPTION," +
//					"RUN_EXPOSURE_METRIC," +
//					"RUN_BY_COUNT_ANALYSIS," +
//					"RUN_BY_AMOUNT_ANALYSIS," +
//					"RUN_BY_AMOUNT_ANALYSIS_BASIS," +
//					"RUN_BY_AMOUNT_CAPPED," +
//					"RUN_CAPPED_AMOUNT," +
//					"RUN_LOSS_RATIO_ANALYSIS," +
//					"RUN_LOSS_RATIO_ANALYSIS_BASIS," +
//					"RUN_AUT_RISK_AMOUNT_CHANGE," +
//					"RUN_RATING," +
//					"RUN_LIFE_JOINT," +
//					"RUN_IBNR_METHOD," +
//					"MASTER_RUN_QX," +
//					"MASTER_RUN_IX," +
//					"MASTER_RUN_IXQX," +
//					"MASTER_RUN_WX," +
//					"RUN_RETAINED" +
//					") VALUES " + "("
//					+ run.getId() + ","
//					+ stringToStringSQL(run.getCode())+ ","
//					+ run.getStudyId() + ","
//					+ stringToStringSQL(run.getStatus()) + ","
//					+ (run.getDatasetId() != null ? run.getDatasetId() : null) + ","
//					+stringToStringSQL(run.getDimensions()) + ","
//					+stringToStringSQL(run.getDescription()) + ","
//					+stringToStringSQL(run.getExposureMetric()) + ","
//					+booleanToBitSQL(run.isCountAnalysis()) + ","
//					+booleanToBitSQL(run.isByamountAnalysis())  + ","
//					+stringToStringSQL(run.getByamountAnalysisBasis()) + ","
//					+booleanToBitSQL(run.isByamountCapped()) + ","
//					+run.getCappedAmount() + ","
//					+booleanToBitSQL(run.isLossRatioAnalysis()) + ","
//					+stringToStringSQL(run.getLossRatioAnalysisBasis())+ ","
//					+booleanToBitSQL(run.isAutRiskAmountChangeSplit()) + ","
//					+stringToStringSQL(run.getRating()) + ","
//					+stringToStringSQL(run.getLifeJoint()) + ","
//					+stringToStringSQL(run.getIbnrMethod()) + ","
//					+booleanToBitSQL(run.getMasterRunQx() )  + ","
//					+booleanToBitSQL(run.getMasterRunIx() ) + ","
//					+booleanToBitSQL(run.getMasterRunIxQx() ) + ","
//					+booleanToBitSQL(run.getMasterRunWx()) + ","
//					+booleanToBitSQL(run.getRunRetained())
//					+ ")";
//			int count = stmt.executeUpdate(SQL);
//			LOGGER.info("Run saved successfully");
//		} catch (SQLException e) {
//			LOGGER.error(e);
//			throw new Exception(e) ; 
//		}
//		
//	}

//	public void insertStudy(Study study) throws Exception {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "INSERT INTO Study(ST_STATUS,"
//					+ "ST_ID,"
//					+ "ST_CODE,"
//					+ "ST_REQUESTER,"
//					+ "ST_DATASOURCE,"
//					+ "ST_COMMENT,"
//					+ "ST_CLIENT,"
//					+ "ST_CLIENT_GROUP,"
//					+ "ST_CLIENT_SHORT_NAME,"
//					+ "ST_DISTRIBUTION_BRAND,"
//					+ "ST_CLIENT_COUNTRY,"
//					+ "ST_TREATY_NUMBER,"
//					+ "ST_LINE_BUSINESS,"
//					+ "ST_CALCULATION_ENGINE"
//					+ ") VALUES (" + stringToStringSQL(study.getStatus())
//					+ ", "+ study.getId()
//					+ "," + stringToStringSQL(study.getCode())
//					+ "," + stringToStringSQL(study.getRequester())
//					+ "," + stringToStringSQL(study.getDataSource())
//					+ "," + stringToStringSQL(study.getSt_Comment())
//					+ "," + stringToStringSQL(study.getSt_Client())
//					+ "," + stringToStringSQL(study.getStClient_Group())
//					+ "," + stringToStringSQL(study.getStShortName())
//					+ "," + stringToStringSQL(study.getStDistributionBrand())
//					+ "," + stringToStringSQL(study.getSt_Client_Country())
//					+ "," + stringToStringSQL(study.getSt_Treaty_Number())
//					+ "," + stringToStringSQL(study.getSt_Line_Business())
//					+ ","	+ stringToStringSQL(study.getSt_Calculation_Engine()) + ")";
//			int count = stmt.executeUpdate(SQL);
//			LOGGER.info("Study saved successfully");
//		} catch ( Exception e) {
//			LOGGER.error(e); 
//			throw new Exception(e) ;
//		}
//		
//		
//	}

	public void insertUserAccess(int userId, int runId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "INSERT INTO [dbo].[USER_RUN_ACCESS]" + "([USER_ID],[RUN_ID])" + "VALUES (" + userId + ","
					+ runId + ")";
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error(e);
		}
		LOGGER.info("UserAcces saved successfully");
	}

	public Integer getUserAccess(int userId, int runId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "SELECT ID" + ",USER_ID" + ",RUN_ID" + "  FROM USER_RUN_ACCESS" + "WHERE USER_ID = " + userId
					+ " and RUN_ID = " + runId;
			ResultSet resultSet = stmt.executeQuery(SQL);
			resultSet.next();
			int id = resultSet.getInt("ID");
			return id;
		} catch (SQLException e) {
			LOGGER.error("No access found for run id : " + runId + " and user : " + userId);
			LOGGER.error(e.getMessage());
			LOGGER.error(e);
		}
		return null;
	}
	
	public Integer getUserAccessCalculated(int runId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "SELECT ID" + ",USER_ID" + ",RUN_ID" + "  FROM USER_RUN_ACCESS" + "WHERE"
					+ " RUN_ID = " + runId;
			ResultSet resultSet = stmt.executeQuery(SQL);
			resultSet.next();
			int id = resultSet.getInt("ID");
			return id;
		} catch (SQLException e) {
			LOGGER.error("No access found for run id : " + runId);
			LOGGER.error(e.getMessage());
			LOGGER.error(e);
		}
		return null;
	}

//	public Study getStudy(int stId) {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "Select ST_STATUS,ST_ID,ST_CODE,ST_REQUESTER,ST_DATASOURCE,ST_COMMENT,ST_CLIENT,ST_CLIENT_GROUP,ST_CLIENT_SHORT_NAME,ST_DISTRIBUTION_BRAND,ST_CLIENT_COUNTRY,ST_TREATY_NUMBER,ST_LINE_BUSINESS,ST_CALCULATION_ENGINE FROM Study where ST_ID=" + stId;
//
//			ResultSet resultSet = stmt.executeQuery(SQL);
//			resultSet.next();
//			Study result = new Study();
//			int id = resultSet.getInt("ST_ID");
//			result.setId(id);
//			String code = resultSet.getString("ST_CODE");
//			result.setCode(code);
//			String status = resultSet.getString("ST_STATUS");
//			result.setStatus(status);
//			String requester = resultSet.getString("ST_REQUESTER");
//			result.setRequester(requester);
//			String dataSource = resultSet.getString("ST_DATASOURCE");
//			result.setDataSource(dataSource);
//			String st_Comment = resultSet.getString("ST_COMMENT");
//			result.setSt_Comment(st_Comment);
//			String st_client = resultSet.getString("ST_CLIENT");
//			result.setSt_Client(st_client);
//			String StClient_Group = resultSet.getString("ST_CLIENT_GROUP");
//			result.setStClient_Group(StClient_Group);
//			String stShortName = resultSet.getString("ST_CLIENT_SHORT_NAME");
//			result.setStShortName(stShortName);
//			String stDistributionBrand = resultSet.getString("ST_DISTRIBUTION_BRAND");
//			result.setStDistributionBrand(stDistributionBrand);
//			String St_Client_Country = resultSet.getString("ST_CLIENT_COUNTRY");
//			result.setSt_Client_Country(St_Client_Country);
//			String St_Treaty_Number = resultSet.getString("ST_TREATY_NUMBER");
//			result.setSt_Treaty_Number(St_Treaty_Number);
//			String St_Line_Business = resultSet.getString("ST_LINE_BUSINESS");
//			result.setSt_Line_Business(St_Line_Business);
//			String St_Calculation_Engine = resultSet.getString("ST_CALCULATION_ENGINE");
//			result.setSt_Calculation_Engine(St_Calculation_Engine);
//
//			return result;
//		} catch (SQLException e) {
//			LOGGER.info("No study found for study id : " + stId);
//		}
//		return null;
//	}

//	public Run getRun(int runId) {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "Select RUN_ID,RUN_CODE,RUN_ST_ID,RUN_STATUS,RUN_DS_ID,RUN_DIMENSIONS,RUN_DESCRIPTION,RUN_EXPOSURE_METRIC,RUN_BY_COUNT_ANALYSIS,RUN_BY_AMOUNT_ANALYSIS,RUN_BY_AMOUNT_ANALYSIS_BASIS,RUN_BY_AMOUNT_CAPPED,RUN_CAPPED_AMOUNT,RUN_LOSS_RATIO_ANALYSIS,RUN_LOSS_RATIO_ANALYSIS_BASIS,RUN_AUT_RISK_AMOUNT_CHANGE,RUN_RATING,RUN_LIFE_JOINT,RUN_IBNR_METHOD,RUN_IBNR_ALLOCATION,RUN_IBNR_AMOUNT,RUN_IBNR_MANUEL_UDF,MASTER_RUN_QX,MASTER_RUN_IX,MASTER_RUN_IXQX,MASTER_RUN_WX,RUN_RETAINED FROM RUN where RUN_ID=" + runId;
//			ResultSet resultSet = stmt.executeQuery(SQL);
//			Run result = new Run();
//			resultSet.next();
//			int id = resultSet.getInt("RUN_ID");
//			result.setId(id);
//			String code = resultSet.getString("RUN_CODE");
//			result.setCode(code);
//			String status = resultSet.getString("RUN_STATUS");
//			result.setStatus(status);
//			int studyId = resultSet.getInt("RUN_ST_ID");
//			result.setStudyId(studyId);
//			int dsId = resultSet.getInt("RUN_DS_ID");
//			result.setDatasetId(dsId);
//
//			String dimensions = resultSet.getString("RUN_DIMENSIONS") ;
//			result.setDimensions(dimensions);
//			String description = resultSet.getString("RUN_DESCRIPTION");
//			result.setDescription(description);
//			String exposureMetric = resultSet.getString("RUN_EXPOSURE_METRIC");
//			result.setExposureMetric(exposureMetric);
//			Boolean countAnalysis = (Boolean)resultSet.getObject("RUN_BY_COUNT_ANALYSIS");
//			result.setCountAnalysis(countAnalysis);
//			Boolean byamountAnalysis = (Boolean)resultSet.getObject("RUN_BY_AMOUNT_ANALYSIS");
//			result.setByamountAnalysis(byamountAnalysis);
//			String byamountAnalysisBasis = resultSet.getString("RUN_BY_AMOUNT_ANALYSIS_BASIS");
//			result.setByamountAnalysisBasis(byamountAnalysisBasis);
//			Boolean byamountCapped = (Boolean)resultSet.getObject("RUN_BY_AMOUNT_CAPPED");
//			result.setByamountCapped(byamountCapped);
//			BigDecimal cappedAmount = resultSet.getBigDecimal("RUN_CAPPED_AMOUNT");
//			result.setCappedAmount(cappedAmount);
//			Boolean lossRatioAnalysis = (Boolean)resultSet.getObject("RUN_LOSS_RATIO_ANALYSIS") ;
//			result.setLossRatioAnalysis(lossRatioAnalysis);
//			String lossRatioAnalysisBasis = resultSet.getString("RUN_LOSS_RATIO_ANALYSIS_BASIS");
//			result.setLossRatioAnalysisBasis(lossRatioAnalysisBasis);
//			Boolean autRiskAmountChangeSplit = (Boolean)resultSet.getObject("RUN_AUT_RISK_AMOUNT_CHANGE");
//			result.setAutRiskAmountChangeSplit(autRiskAmountChangeSplit);
//			String rating = resultSet.getString("RUN_RATING");
//			result.setRating(rating);
//			String lifeJoint = resultSet.getString("RUN_LIFE_JOINT");
//			result.setLifeJoint(lifeJoint);
//			String ibnrMethod = resultSet.getString("RUN_IBNR_METHOD");
//			result.setIbnrMethod(ibnrMethod);
//			Boolean masterRunQx =(Boolean) resultSet.getObject("MASTER_RUN_QX");
//			result.setMasterRunQx(masterRunQx);
//			Boolean masterRunIx = (Boolean)resultSet.getObject("MASTER_RUN_IX");
//			result.setMasterRunIx(masterRunIx);
//			Boolean masterRunIxQx = (Boolean)resultSet.getObject("MASTER_RUN_IXQX");
//			result.setMasterRunIxQx(masterRunIxQx);
//			Boolean masterRunWx = (Boolean)resultSet.getObject("MASTER_RUN_WX");
//			result.setMasterRunWx(masterRunWx);
//			Boolean runRetained = (Boolean)resultSet.getObject("RUN_RETAINED");
//			result.setRunRetained(runRetained);
//
//
//
//			return result;
//		} catch (SQLException e) {
//			LOGGER.info("No run found for run id : " + runId);
//		}
//		return null;
//	}
	
//	public List<Integer> getRunsIds() {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "Select RUN_ID FROM RUN";
//			ResultSet resultSet = stmt.executeQuery(SQL);
//			List<Integer> ids = new ArrayList<>();
//			while(resultSet.next()) {
//				int id = resultSet.getInt("RUN_ID");
//				ids.add(id);
//			}
//			return ids;
//			
//		} catch (SQLException e) {
//			LOGGER.error("Errror while getting runs ids");
//		}
//		return null;
//	}

//	public boolean deleteRun(int runId) {
//		try (Statement stmt = connection.getConnection().createStatement()) {
//			String SQL = "DELETE FROM RUN WHERE run_id=" + runId;
//			int count = stmt.executeUpdate(SQL);
//			if (count == 0)
//				return false;
//			return true;
//		} catch (SQLException e) {
//			LOGGER.error(e);
//		}
//		return false;
//	}
//	
	public boolean deleteIbnrUdf(int runId) {
		try (Statement stmt = connection.getConnection().createStatement()) {
			String SQL = "DELETE FROM IBNR_UDF_INPUT_DATA WHERE run_id=" + runId;
			int count = stmt.executeUpdate(SQL);
			if (count == 0)
				return false;
			return true;
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return false;
	}
	
	public boolean deleteIbnrAllocation(int runId) {
		try (Statement stmt = connection.getConnection().createStatement()) {
			String SQL = "DELETE FROM IBNR_ALLOCATION_INPUT_DATA WHERE run_id=" + runId;
			int count = stmt.executeUpdate(SQL);
			if (count == 0)
				return false;
			return true;
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return false;
	}
	
	public boolean deleteIbnrAmount(int runId) {
		try (Statement stmt = connection.getConnection().createStatement()) {
			String SQL = "DELETE FROM IBNR_AMOUNT_INPUT_DATA WHERE run_id=" + runId;
			int count = stmt.executeUpdate(SQL);
			if (count == 0)
				return false;
			return true;
		} catch (SQLException e) {
			LOGGER.error(e);
		}
		return false;
	}

//	public boolean changeStudyStatus(int studyId, String status) {
//		try (Statement stmt = connection.getConnection().createStatement()) {
//			String SQL = "UPDATE STUDY set ST_STATUS = '"+status+"' WHERE ST_ID=" + studyId;
//			int count = stmt.executeUpdate(SQL);
//			if (count == 0)
//				return false;
//			return true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}

	public boolean deleteResultByRunIdAndDecrement(int runId, String decrement) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "DELETE FROM RESULT_DATA WHERE run_id=" + runId;
			if (StringUtils.isNotBlank(decrement)) {
				if (decrement.equals("qx") || decrement.equals("wx") || decrement.equals("ix") || decrement.equals("ix+qx")) {
					SQL += " and decrement like '" + decrement + "'";
				} else {
					throw new SQLException("Decrement invalid");
				}
			}
			int count = stmt.executeUpdate(SQL);
			if (count == 0)
				return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
//	public Decrement getDecrement(int dpId) {
//		try (Statement stmt = connection.getConnection().createStatement()) {
//			String SQL = "Select DP_RUN_ID,DP_ID,DP_DECREMENT,DP_SLICING_DIMENSIONS,DP_LEADING_SLICING_DIMENSION,DP_ATTAINED_AGE_DEF,DP_STUDY_PERIOD_END_DATE,DP_STUDY_PERIOD_START_DATE,DP_EXP_METHOD,BASE_EXPECTED_TABLES_IDS,TREND_EXPECTED_TABLES_IDS,ADJUSTMENT_EXPECTED_TABLES_IDS,POLICY_EXPECTED_TABLES_IDS FROM DECREMENT_PARAMETERS where DP_ID=" + dpId;
//			ResultSet resultSet = stmt.executeQuery(SQL);
//			Decrement result = new Decrement();
//			resultSet.next();
//
//
//
//			int dpRunId = resultSet.getInt("DP_RUN_ID");
//			result.setDpRunId(dpRunId);
//
//			result.setDpId(dpId);
//
//			String dpDecrement = resultSet.getString("DP_DECREMENT");
//			result.setDpDecrement(dpDecrement);
//			String slicingDimensions = resultSet.getString("DP_SLICING_DIMENSIONS");
//			result.setSlicingDimensions(slicingDimensions);
//			String dpLeadingSlicingDimension = resultSet.getString("DP_LEADING_SLICING_DIMENSION");
//			result.setDpLeadingSlicingDimension(dpLeadingSlicingDimension);
//			String dpAttainedAgeDef = resultSet.getString("DP_ATTAINED_AGE_DEF");
//			result.setDpAttainedAgeDef(dpAttainedAgeDef);
//			Date dpStudyPeriodEndDate = resultSet.getDate("DP_STUDY_PERIOD_END_DATE");
//			result.setDpStudyPeriodEndDate(dpStudyPeriodEndDate);
//			Date dpStudyPeriodStartDate = resultSet.getDate("DP_STUDY_PERIOD_START_DATE");
//			result.setDpStudyPeriodStartDate(dpStudyPeriodStartDate);
//			String dpExpMethod = resultSet.getString("DP_EXP_METHOD");
//			result.setDpExpMethod(dpExpMethod);
//			String refExpectedTableByRetBase = resultSet.getString("BASE_EXPECTED_TABLES_IDS");
//			result.setRefExpectedTableByRetBase(refExpectedTableByRetBase);
//			String refExpectedTableByRetTrend = resultSet.getString("TREND_EXPECTED_TABLES_IDS");
//			result.setRefExpectedTableByRetTrend(refExpectedTableByRetTrend);
//			String refExpectedTableByRetAdjustment = resultSet.getString("ADJUSTMENT_EXPECTED_TABLES_IDS");
//			result.setRefExpectedTableByRetAdjustment(refExpectedTableByRetAdjustment);
//			String refExpectedTableByRetPolicy = resultSet.getString("POLICY_EXPECTED_TABLES_IDS");
//			result.setRefExpectedTableByRetPolicy(refExpectedTableByRetPolicy);
//
//
//			return result;
//		} catch (SQLException e) {
//			LOGGER.error("No decrement found for decrement id : " + dpId);
//		}
//
//		return null ; 
//	}
//	
//	public void InsertDecrement(Decrement decrement) throws Exception {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "INSERT INTO DECREMENT_PARAMETERS(DP_RUN_ID,"
//					+"DP_ID,"
//					+"DP_DECREMENT,"
//					+"DP_SLICING_DIMENSIONS,"
//					+"DP_LEADING_SLICING_DIMENSION,"
//					+"DP_ATTAINED_AGE_DEF,"
//					+"DP_STUDY_PERIOD_END_DATE,"
//					+"DP_STUDY_PERIOD_START_DATE,"
//					+"DP_EXP_METHOD,"
//					+"BASE_EXPECTED_TABLES_IDS,"
//					+"TREND_EXPECTED_TABLES_IDS,"
//					+"ADJUSTMENT_EXPECTED_TABLES_IDS,"
//					+"POLICY_EXPECTED_TABLES_IDS"
//					+ ") VALUES (" + decrement.getDpRunId()
//					+ ","+ decrement.getDpId()
//					+ ","+ stringToStringSQL(decrement.getDpDecrement())
//					+ ","+ stringToStringSQL(decrement.getSlicingDimensions())
//					+ ","+ stringToStringSQL(decrement.getDpLeadingSlicingDimension())
//					+ ","+ stringToStringSQL(decrement.getDpAttainedAgeDef())
//					+ ",'"+ decrement.getDpStudyPeriodEndDate()
//					+ "','"+ decrement.getDpStudyPeriodStartDate()
//					+ "',"+ stringToStringSQL(decrement.getDpExpMethod())
//					+ ","+ stringToStringSQL(decrement.getRefExpectedTableByRetBase())
//					+ ","+ stringToStringSQL(decrement.getRefExpectedTableByRetTrend())
//					+ ","+ stringToStringSQL(decrement.getRefExpectedTableByRetAdjustment())
//					+ ","	+ stringToStringSQL(decrement.getRefExpectedTableByRetPolicy())+ ")";
//			int count = stmt.executeUpdate(SQL);
//			LOGGER.info("Decrement saved successfully");
//		} catch (SQLException e) {
//			LOGGER.error(e);
//			throw new Exception(e) ; 
//		}
//	}


}
