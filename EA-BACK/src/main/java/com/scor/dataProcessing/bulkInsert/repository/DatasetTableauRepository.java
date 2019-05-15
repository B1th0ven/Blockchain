//package com.scor.dataProcessing.bulkInsert.repository;
//
//import java.io.Serializable;
//import java.sql.Date;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.scor.dataProcessing.bulkInsert.models.Dataset;
//
//@Component
//public class DatasetTableauRepository implements Serializable{
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -9195088768762564351L;
//	
//	private final static Logger LOGGER = Logger.getLogger(DatasetTableauRepository.class);
//	
//	@Autowired
//	private JdbcConnection connection;
//	
//	public void save(Dataset dataset) {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "INSERT INTO [dbo].[DATA_SET] "
//					+ "([DS_ID]"
//					+ ",[DS_RUN_ID]"
//					+ ",[DS_NAME]"
//					+ ",[DS_CODE]"
//					+ ",[DS_EXPOSURE_EXTRACTION_DATE]"
//					+ ",[DS_EVENT_EXTRACTION_DATE]"
//					+ ",[DS_DATA_STRUCTURE_TYPE]"
//					+ ",[DS_EVENT_EXPOSURE_FILE_ID]"
//					+ ",[DS_PRODUCT_FILE_ID]"
//					+ ",[DS_TECH_REPORT]"
//					+ ",[DS_FUNC_REPORT]"
//					+ ",[DS_NOT_EXECUTED]"
//					+ ",[DS_COMMENT]"
//					+ ",[DS_CREATED_DATE])"
//					+ "values ("
//					+ dataset.getId() +","
//					+ dataset.getRunId()+",'"
//					+ dataset.getName()+"','"
//					+ dataset.getCode()+"','"
//					+ dataset.getExposureExtractionDate()+"','"
//					+ dataset.getEventExtractionDate()+"','"
//					+ dataset.getDataStructureType()+"',"
//					+ dataset.getEventExposureFileId()+","
//					+ dataset.getProductFileId()+",'"
//					+ dataset.getTechReport()+"','"
//					+ dataset.getFuncReport()+"','"
//					+ dataset.getNotExecutedReport()+"','"
//					+ dataset.getComment()+"','"
//					+ dataset.getCreationDate()
//					+ "')";
//			System.out.println(SQL);
//			int count = stmt.executeUpdate(SQL);
//		} catch (SQLException e) {
//			LOGGER.error(e.getMessage());
//			LOGGER.error(e);
//		}
//		LOGGER.info("DataSet saved successfully");
//	}
//	
//	public void delete(int id) {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "DELETE FROM [dbo].[DATA_SET] WHERE [DS_ID] = " + id;
//			int count = stmt.executeUpdate(SQL);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public Dataset findOne(int dsId) {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "SELECT [DS_ID]"
//					+ ",[DS_RUN_ID]"
//					+ ",[DS_NAME] "
//					+ ",[DS_CODE]"
//					+ ",[DS_EXPOSURE_EXTRACTION_DATE]"
//					+ ",[DS_EVENT_EXTRACTION_DATE]"
//					+ ",[DS_DATA_STRUCTURE_TYPE]"
//					+ ",[DS_EVENT_EXPOSURE_FILE_ID]"
//					+ ",[DS_PRODUCT_FILE_ID]"
//					+ ",[DS_TECH_REPORT]"
//					+ ",[DS_FUNC_REPORT]"
//					+ ",[DS_NOT_EXECUTED]"
//					+ ",[DS_COMMENT]"
//					+ ",[DS_CREATED_DATE] "
//					+ "FROM [EXPAN_IO].[dbo].[DATA_SET] "
//					+ "Where [DS_ID] = " + dsId;
//			ResultSet resultSet = stmt.executeQuery(SQL);
//			resultSet.next();
//			Dataset result = mapResultSetToDataset(resultSet);
//			return result;
//		} catch (SQLException e) {
//			LOGGER.error("No Dataset found for dsid : " + dsId);
//		}
//		return null;
//	}
//	
//	private Dataset mapResultSetToDataset(ResultSet resultSet) throws SQLException {
//		Dataset dataset = new Dataset();
//		try {
//			int id = resultSet.getInt("DS_ID");
//			dataset.setId(id);
//		} catch (SQLException e) {
//			throw e;
//		}
//		try {
//			String code = resultSet.getString("DS_CODE");
//			dataset.setCode(code);
//		} catch (SQLException e) {
//		}
//		try {
//			String name = resultSet.getString("DS_NAME");
//			dataset.setName(name);
//		} catch (SQLException e) {
//		}
//		try {
//			String comment = resultSet.getString("DS_COMMENT");
//			dataset.setComment(comment);
//		} catch (SQLException e) {
//		}
//		try {
//			Date createdDate = resultSet.getDate("DS_CREATED_DATE");
//			dataset.setCreationDate(createdDate);
//		} catch (SQLException e) {
//		}
//		try {
//			String dsDataStructureType = resultSet.getString("DS_DATA_STRUCTURE_TYPE");
//			dataset.setDataStructureType(dsDataStructureType);
//		} catch (SQLException e) {
//		}
//		try {
//			Integer dsEventExposureFileId = resultSet.getInt("DS_EVENT_EXPOSURE_FILE_ID");
//			dataset.setEventExposureFileId(dsEventExposureFileId);
//		} catch (SQLException e) {
//		}
//		try {
//			String dsEventExtractionDate = resultSet.getString("DS_EVENT_EXTRACTION_DATE");
//			dataset.setEventExtractionDate(dsEventExtractionDate);
//		} catch (SQLException e) {
//		}
//		try {
//			String dsExposureExtractionDate = resultSet.getString("DS_EXPOSURE_EXTRACTION_DATE");
//			dataset.setExposureExtractionDate(dsExposureExtractionDate);
//		} catch (SQLException e) {
//		}
//		try {
//			String funcReport = resultSet.getString("DS_FUNC_REPORT");
//			dataset.setFuncReport(funcReport);
//		} catch (SQLException e) {
//		}
//		try {
//			String notExecuted = resultSet.getString("DS_NOT_EXECUTED");
//			dataset.setNotExecutedReport(notExecuted);
//		} catch (SQLException e) {
//		}
//		try {
//			Integer dsProductFileId = resultSet.getInt("DS_PRODUCT_FILE_ID");
//			dataset.setProductFileId(dsProductFileId);
//		} catch (SQLException e) {
//		}
//		try {
//			String techReport = resultSet.getString("DS_TECH_REPORT");
//			dataset.setTechReport(techReport);
//		} catch (SQLException e) {
//		}
//		return dataset;
//	}
//
//}
