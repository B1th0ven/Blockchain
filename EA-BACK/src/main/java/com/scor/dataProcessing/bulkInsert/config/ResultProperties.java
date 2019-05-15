package com.scor.dataProcessing.bulkInsert.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResultProperties implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -7914290878707090783L;

	@Value("${ea.db.result.url}")
	public String dbResultUrl;
	
	@Value("${ea.db.result.username}")
	public String dbResultUsername;
	
	@Value("${ea.db.result.password}")
	public String dbResuPassword;
	
	@Value("${azure.db.server.server}")
	public String dbServer;

	@Value("${azure.db.server.databaseName}")
	public String dbName;

	@Value("${azure.db.server.user}")
	public String dbUser;

	@Value("${azure.db.server.password}")
	public String dbPassword;

	@Value("${azure.db.server.dir}")
	public String workspace;

	@Value("${azure.db.server.instance}")
	public String dbinstance;
}
