package uk.gov.courtservice.xhibit.cpp.scripts.db;

import java.sql.Connection;
import java.sql.SQLException;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.cpp.scripts.pojo.DBConnectionProperties;


/**
 * Setup a new Apache Commons Pool Connection Pool using their BasicDataSource
 * Includes a method to get a connection
 * 
 * @author atwells
 *
 */
public class DBCPDataSource {
	
	final static Logger logger = Logger.getLogger(DBCPDataSource.class);
	
	static BasicDataSource bds; // Apache DBCP connection to setup and use

	public static void setupConnectionPool(DBConnectionProperties dbProps) {
		logger.debug(System.currentTimeMillis()+" :: METHOD ENTRY:: setupConnectionPool");

		logger.debug(System.currentTimeMillis()+" :: Before creating pool:: setupConnectionPool");
		bds = new BasicDataSource();
		bds.setUrl(dbProps.getUrl());
		bds.setUsername(dbProps.getUsername());
		bds.setPassword(dbProps.getPassword());
		bds.setMinIdle(dbProps.getMinIdle());
		bds.setMaxIdle(dbProps.getMaxIdle());
		bds.setMaxOpenPreparedStatements(dbProps.getMaxOpenStatements());
		logger.debug(System.currentTimeMillis()+" :: After creating pool:: setupConnectionPool");
	
		logger.debug(System.currentTimeMillis()+" :: METHOD EXIT:: setupConnectionPool");
	}
	
	public static Connection getConnection() throws SQLException {
		return bds.getConnection();
	}
	
	/**
	 * Method to shutdown the datasource.
	 * 
	 * @param ds
	 * @throws SQLException
	 */
	public static void shutdownDataSource() throws SQLException {
		bds.close();
	}
		
	private DBCPDataSource() {}
}
