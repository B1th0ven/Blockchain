package com.scor.dataProcessing.bulkInsert.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.dataProcessing.bulkInsert.config.ResultProperties;

@Service
public class JdbcConnection implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 998724016047769367L;

	private final static Logger LOGGER = Logger.getLogger(JdbcConnection.class);

	private Connection connection;
	
	@Autowired
	private ResultProperties resultProperties;

	public JdbcConnection(ResultProperties resultProperties)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		getConnection(resultProperties);
	}

	private void getConnection(ResultProperties resultProperties) {
		try {
			String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			Class.forName(driver);
			String url = resultProperties.dbResultUrl;
			LOGGER.info("Trying to open connection with database , url : " + url);
			this.connection = DriverManager.getConnection(url, resultProperties.dbResultUsername,
					resultProperties.dbResuPassword);
			LOGGER.info("Connection opened with url : " + url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getConnectionTodb() {
		try {
			String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			Class.forName(driver);
			String url = resultProperties.dbResultUrl;
			LOGGER.info("ReTrying to open connection with database , url : " + url);
			this.connection = DriverManager.getConnection(url, resultProperties.dbResultUsername,
					resultProperties.dbResuPassword);
			LOGGER.info("Connection opened with url : " + url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {
		if(connection == null || connection.isClosed()) {
			getConnectionTodb();
		}
		return connection;
	}
	
}
