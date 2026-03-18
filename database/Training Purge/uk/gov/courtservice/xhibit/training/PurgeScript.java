package uk.gov.courtservice.xhibit.training;

import java.sql.*;

/**
 * <p>Training Script: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Alex Brown
 * @version 1.0
 */

public class PurgeScript
{
	static String DSN = "XHIBIT";

	/**
	 *
	 * @param userName
	 * @param password
	 */
	public static void runScript(String userName, String password) throws Exception
	{
		System.out.println("********************** pwd " + password);
		Connection conn;
		Statement stmt;
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection("jdbc:odbc:"+DSN, userName, password);
			stmt = conn.createStatement();
			String s = "begin PURGESCRIPT(2); end;";
			stmt.execute(s);
			stmt.close();
			conn.close();
		}
		catch(Exception e)
		{
			System.err.println("Exception whilst running the purge script");
			e.printStackTrace();
			throw e;
		}
		finally
		{
			stmt = null;
			conn = null;
		}
	}


	public static void main( String args[])
	{
			String User = args[0];
			String PW = args[1];

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:odbc:"+DSN, User, PW);
			Statement stmt = conn.createStatement();
			String s = "begin PURGESCRIPT(2); end;";
			stmt.execute(s);
			stmt.close();
			conn.close();
		}
		catch (Throwable t){
			System.out.println(t);
		}
	}
}
