package com.scor.dataProcessing.bulkInsert.repository;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRunAccessRepository implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1609755343116545413L;
	
	private static final Logger LOGGER = Logger.getLogger(UserRunAccessRepository.class);
	@Autowired
	private JdbcConnection connection;
	
	public void saveDataset(int dsId, int userId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "INSERT INTO [dbo].[USER_RUN_ACCESS] " + "([USER_ID] ,[DS_ID])" + "values (" + userId + " , "
					+ dsId + ")";
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteByDatasetId(int id) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "DELETE FROM [dbo].[USER_RUN_ACCESS] WHERE [DS_ID] = " + id;
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteByRunId(int id) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "DELETE FROM [dbo].[USER_RUN_ACCESS] WHERE [RUN_ID] = " + id;
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Integer findByDsIdAndUserId(int dsId, int userId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "SELECT [DS_ID]"
					+ ",[USER_ID]"
					+ "FROM [EXPAN_IO].[dbo].[USER_RUN_ACCESS] "
					+ "Where [DS_ID] = " + dsId
					+ "AND [USER_ID] = " + userId;
			ResultSet resultSet = stmt.executeQuery(SQL);
			resultSet.next();
			int result = resultSet.getInt("DS_ID");
			
			return result;
		} catch (SQLException e) {
			LOGGER.error("No Access found for dsid : " + dsId + " userId : " + userId);
		}
		return null;
	}
	
	public Integer findByRunIdAndUserId(int runid, int userId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "SELECT [RUN_Id]"
					+ ",[USER_ID]"
					+ "FROM [EXPAN_IO].[dbo].[USER_RUN_ACCESS] "
					+ "Where [RUN_Id] = " + runid
					+ "AND [USER_ID] = " + userId;
			ResultSet resultSet = stmt.executeQuery(SQL);
			resultSet.next();
			int result = resultSet.getInt("RUN_Id");
			
			return result;
		} catch (SQLException e) {
			LOGGER.error("No Access found for dsid : " + runid + " userId : " + userId);
		}
		return null;
	}
	
	public void deleteByDatasetIdAndUserId(int dsId, int userId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "DELETE FROM [dbo].[USER_RUN_ACCESS] WHERE [DS_ID] = " + dsId + "AND [USER_ID] = " + userId;
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteByRunIdAndUserId(int runId, int userId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "DELETE FROM [dbo].[USER_RUN_ACCESS] WHERE [RUN_ID] = " + runId + "AND [USER_ID] = " + userId;
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveRun(int runId, int userId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "INSERT INTO [dbo].[USER_RUN_ACCESS]" + "([USER_ID],[RUN_ID])" + "VALUES (" + userId + ","
					+ runId + ")";
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteByUserId(int userId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "DELETE FROM [dbo].[USER_RUN_ACCESS] WHERE [USER_ID] = " + userId;
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
