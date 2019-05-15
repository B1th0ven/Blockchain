//package com.scor.dataProcessing.bulkInsert.repository;
//
//import java.io.Serializable;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.scor.dataProcessing.bulkInsert.models.Dataset;
//import com.scor.dataProcessing.bulkInsert.models.User;
//
//@Component
//public class UsersRepository implements Serializable{
//	
//	private static final Logger LOGGER = Logger.getLogger(UsersRepository.class);
//	
//	@Autowired
//	private JdbcConnection connection;
//	
////	public void save(User user) {
////		try (Statement stmt = connection.getConnection().createStatement();) {
////			String SQL = "INSERT INTO [dbo].[REF_USER] " +
////					"([RU_ID]" + 
////					",[RU_FIRST_NAME]" + 
////					",[RU_LAST_NAME]" + 
////					",[RU_RM_ID]" + 
////					",[RU_LOGIN]" + 
////					",[RU_RC_ID]" + 
////					",[RU_RS_ID]" + 
////					",[RU_FUNCTION]" + 
////					",[RU_ROLE])" +
////					"values (" +
////					user.getId() + ",'" +
////					user.getFirstName() + "','" +
////					user.getLastName() + "'," +
////					user.getRmId() + ",'" + 
////					user.getLogin() + "'," + 
////					user.getRcId() + "," + 
////					user.getRsId() + ",'" + 
////					user.getFunction() + "','" + 
////					user.getRole() +
////					"')";
////			int count = stmt.executeUpdate(SQL);
////		} catch (SQLException e) {
////			e.printStackTrace();
////		}
////	}
////	
////	public void update(User user) {
////		try (Statement stmt = connection.getConnection().createStatement();) {
////			String SQL = "UPDATE [dbo].[REF_USER] SET" +
////					"[RU_FIRST_NAME] = '" + user.getFirstName() + "'"+ 
////					",[RU_LAST_NAME] = '" + user.getLastName() + "'" + 
////					",[RU_RM_ID] = " + user.getRmId()+ 
////					",[RU_LOGIN] = '" + user.getLogin() + "'" + 
////					",[RU_RC_ID] = " + user.getRcId() +
////					",[RU_RS_ID] = " + user.getRsId() +
////					",[RU_FUNCTION] = '" + user.getFunction() + "'" + 
////					",[RU_ROLE] = '" + user.getRole() + "'" + 
////					" WHERE [RU_ID] = "+ user.getId();
////			int count = stmt.executeUpdate(SQL);
////		} catch (SQLException e) {
////			e.printStackTrace();
////		}
////	}
//	
//	public User findOne(int id) {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "SELECT [RU_ID]" + 
//					",[RU_FIRST_NAME]" + 
//					",[RU_LAST_NAME]" + 
//					",[RU_RM_ID]" + 
//					",[RU_LOGIN]" + 
//					",[RU_RC_ID]" + 
//					",[RU_RS_ID]" + 
//					",[RU_FUNCTION]" + 
//					",[RU_ROLE] " +
//					"FROM [EXPAN_IO].[dbo].[REF_USER] "
//					+ "Where [RU_ID] = " + id;
//			ResultSet resultSet = stmt.executeQuery(SQL);
//			resultSet.next();
//			User result = mapResultSetToUser(resultSet);
//			return result;
//		} catch (SQLException e) {
//			LOGGER.error("No User found for userid : " + id);
//		}
//		return null;
//	}
//
//
//	private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
//		User user = new User();
//		try {
//			int id = resultSet.getInt("RU_ID");
//			user.setId(id);
//		} catch (SQLException e) {
//			throw e;
//		}
//		try {
//			String firstName = resultSet.getString("RU_FIRST_NAME");
//			user.setFirstName(firstName);
//		} catch (SQLException e) {
//		}
//		try {
//			String lastName = resultSet.getString("RU_LAST_NAME");
//			user.setLastName(lastName);
//		} catch (SQLException e) {
//		}
//		try {
//			Integer rmId = resultSet.getInt("RU_RM_ID");
//			user.setRmId(rmId);
//		} catch (SQLException e) {
//		}
//		try {
//			Integer rcId = resultSet.getInt("RU_RC_ID");
//			user.setRcId(rcId);
//		} catch (SQLException e) {
//		}
//		try {
//			Integer rsId = resultSet.getInt("RU_RS_ID");
//			user.setRsId(rsId);
//		} catch (SQLException e) {
//		}
//		try {
//			String function = resultSet.getString("RU_FUNCTION");
//			user.setFunction(function);
//		} catch (SQLException e) {
//		}
//		try {
//			String role = resultSet.getString("RU_ROLE");
//			user.setRole(role);
//		} catch (SQLException e) {
//		}
//		return user;
//	}
//
//	public void delete(int userId) {
//		try (Statement stmt = connection.getConnection().createStatement();) {
//			String SQL = "DELETE FROM [dbo].[REF_USER] WHERE [RU_ID] = " + userId;
//			int count = stmt.executeUpdate(SQL);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//}
