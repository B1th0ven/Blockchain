package com.scor.dataProcessing.bulkInsert.repository;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputDataRepository implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8463158225296641293L;
	
	@Autowired
	private JdbcConnection connection;
	
	public void deletePolicy(int dsId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "DELETE FROM [dbo].[POLICY_INPUT_DATA] WHERE [DS_ID] = " + dsId;
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteProduct(int dsId) {
		try (Statement stmt = connection.getConnection().createStatement();) {
			String SQL = "DELETE FROM [dbo].[PRODUCT_INPUT_DATA] WHERE [DS_ID] = " + dsId;
			int count = stmt.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
