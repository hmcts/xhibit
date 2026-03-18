package uk.gov.courtservice.xhibit.cpp.scripts.tests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import junit.framework.TestCase;
import oracle.jdbc.internal.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class TestAddCLOBObject extends TestCase {

	private static String PROVIDER_URL = "t3://localhost:7003";
	private static String INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";
	static InitialContext ctx = null;
	
	Connection conn = null;
	DataSource ds = null;

	@Before
	public void setUp() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, PROVIDER_URL);

		try {
			ctx = new InitialContext(env);
			assertEquals(false, ctx == null);
			
			ds = (DataSource) ctx.lookup("XhibitOracleTxDataSource");
			assertEquals(false, ds == null);
			conn = (Connection) ds.getConnection();
			assertEquals(false, conn == null);
			System.out.println("Connection details: "+conn);
			
		} catch (NoInitialContextException nice) {
			System.out.println("Cannot find Initial Context:");
			nice.printStackTrace();
			fail();
		} catch (NamingException ne) {
			System.out.println("Naming Exception:");
			ne.printStackTrace();
			fail();
		} catch (SQLException e) {
			System.out.println("Error getting database connection.");
			e.printStackTrace();
			fail();
		}
	}
	
	@After
	public void teardown() {
		try {
			assertEquals(false, ctx == null);
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("Error getting database connection.");
			e.printStackTrace();
			fail();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing database connection.");
				e.printStackTrace();
			}
		}
	}
	
	@Ignore
	public void testGetDBConnection() {
		try {
			assertEquals(false, ctx == null);
			ds = (DataSource) ctx.lookup("XhibitOracleTxDataSource");
			assertEquals(false, ds == null);
			conn = (Connection) ds.getConnection();
			assertEquals(false, conn == null);
			System.out.println("Connection details: "+conn);
		} catch (NamingException e) {
			System.out.println("Cannot find data source.");
			e.printStackTrace();
			fail();
		} catch (SQLException e) {
			System.out.println("Error getting database connection.");
			e.printStackTrace();
			fail();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Error closing database connection.");
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	/**
	 * Need to ensure that a CLOB of more than 2500 chars can be added
	 */
	@Test
	public void testCreateLargeCLOB() {
		assertEquals(false, conn == null);
		try {
			Statement stmt = conn.createStatement();
			String query = "select 1 from dual";
			ResultSet rs = stmt.executeQuery(query);
			if (rs != null) {
				System.out.println("Data found.");
			} else {
				System.out.println("No data found.");
			}
			
			//String thisCLOB = "-rhrefhretgrhegdisdsfrefgh7ffsfsdfdsfdsfsfsdafadsntyj56rtgrewtgu8-hf8rewhr8e9g08ht834-rhrefhretgrhegdisdsfrefgh7f";
			StringBuffer sb = new StringBuffer();
			String partCLOB = "fsfsdfdsfdsfsfsdafadsntyj56rtgrewtgu8-hf8rewhr8e9g08ht834-rhrefhretgrhegdisdsfrefgh7ffsfsdfdsfdsfsfsdafadsntyj56rtgrewtgu8-hf8rewhr8e9g08ht834-rhrefhretgrhegdisdsfrefgh7ffsfsdfdsfdsfsfsdafadsntyj56rtgrewtgu8-hf8rewhr8e9g08ht834-rhrefhretgrhegdisdsfrefgh7ffsfsdfdsfdsfsfsdafadsntyj56rtgrewtgu8-hf8rewhr8e9g08ht834-rhrefhretgrhegdisdsfrefgh7ffsfsdfdsfdsfsfsdafadsntyj56rtgrewtgu8-hf8rewhr8e9g08ht834-rhrefhretgrhegdisdsfrefgh7ffsfsdfdsfdsfsfsdafadsntyj56rtgrewtgu8-hf8rewhr8e9g08ht834-rhrefhretgrhegdisdsfrefgh7ffsfsdfdsfdsfsfsdafadsntyj56rtgrewtgu8-hf8rewhr8e9g08ht834-rhrefhretgrhegdisdsfrefgh7ffsfsdfdsfdsfsfsdafadsntyj56rtgrewtgu8-hf8rewhr8e9g08ht834-rhrefhretgrhegdisdsfrefgh7f";
			for (int i=0; i<100; i++) {
				sb = sb.append(partCLOB);
			}
			String thisCLOB = sb.toString();			
			
			System.out.println(thisCLOB.length());
			
			query = "BEGIN insert into xhb_clob (clob_data) values(to_clob(?)) returning clob_id into ?; END;";
			String q3 = "update xhb_clob set clob_data=clob_data||to_clob('";
			String q4 = "') where clob_id=";

			long clobId=0;
			ArrayList<String> clobElements = (ArrayList<String>) splitEqually(thisCLOB, 2499);
			for (int i=0; i<clobElements.size(); i++) {
				String thisPart = clobElements.get(i);

				if (i == 0) {
					CallableStatement cs = conn.prepareCall(query);
					cs.setString(1, thisPart);
					cs.registerOutParameter(2, OracleTypes.NUMBER);
					cs.execute();
					clobId = cs.getLong(2);
					System.out.println("ClobId="+clobId);
					
				} else {
					query = q3+thisPart+q4+clobId;
					stmt.executeQuery(query);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
		
	private List<String> splitEqually(String text, int sizeOfEachElement) {
		List<String> ret = new ArrayList<String>((text.length() + sizeOfEachElement - 1) / sizeOfEachElement);
		
		for (int start=0; start<text.length(); start+=sizeOfEachElement) {
			ret.add(text.substring(start, Math.min(text.length(), start+sizeOfEachElement)));
		}
		return ret;
	}
	

}
