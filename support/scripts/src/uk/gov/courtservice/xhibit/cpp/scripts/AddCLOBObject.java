package uk.gov.courtservice.xhibit.cpp.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import oracle.jdbc.internal.OracleTypes;
import uk.gov.courtservice.framework.jdbc.core.JdbcHelper;

/**
 * Purpose: Add a CLOB to the database. The CLOB can be given as a string or can be read directly from a file
 * Uses the app server (WL) datasources.
 * 
 * Will be called from the command line with the necessry arguments. Likely it will be called from a BASH shell script.
 * 
 * N.B. The original idea was to use SQLPlus called from a BASH script to do this but as inserting into the CLOB
 * has to be done in small (less than 2500 chars) chunks then the job of splitting up the CLOB and then 
 * cycling through an array of strings to pass in was either (a) really slow or (b) impossible to pass the array index.
 * Alternative solutions such as using Perl could possibly have been implemented given time.
 * 
 * @author atwells
 *
 */
public class AddCLOBObject {
	
	private String useFile;
	private String fileDirectory;
	private File xmlFile;
	private String clobData;
	private String url;
	private String username;
	private String password;
	private boolean dbTestRequired;
	private long clobId;
	
	static Connection conn = null;
	
	final static Logger logger = Logger.getLogger(AddCLOBObject.class);
	
	
	/**
	 * The main method :-)
	 * 
	 * Exit values:
	 * 0: All ok
	 * 1: Exception thrown
	 * 2: No data to process but no exception
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		if (logger.isDebugEnabled())
			logger.debug("METHOD ENTRY: main");
		
		int exitValue = 0;
		
		try {
			
			AddCLOBObject aco = new AddCLOBObject();
			
			// Get incoming properties and data
			try {
				aco.getIncomingProperties(args);
			} catch (Exception e) {
				logger.error("Got an error processing incoming properties");
				logger.error(e.getLocalizedMessage());
				System.exit(1);
			}
	
			// Check if using a file and if so get the data for the CLOB
			try {
				if (aco.checkIfUsingFile()) {
					aco.setClobData(); 
				} else {
					// The CLOB data has been given to us in the incoming arguments but we need to make sure its ok
					aco.parseIncomingClobData();
				}
			} catch (Exception e) {
				logger.error("Error checking if we can get the clob data from the file");
				logger.error(e.getLocalizedMessage());
				System.exit(1);
			}
			
			if (aco.clobData.length() > 0) {
				// Setup context and db conn
				boolean setupOk = aco.setup();
				
				if (setupOk) {
					// Add the CLOB
					try {
						aco.addClobToDB();
					} catch (SQLException e) {
						logger.error("Error adding the CLOB to the DB");
						logger.error(e.getLocalizedMessage());
					}
				} else {
					if (logger.isDebugEnabled())
						logger.debug("There was an error during setup");
					System.exit(2);
				}
			} else {
				if (logger.isDebugEnabled())
					logger.debug("There is no data to process");
				System.exit(2);
			}
			
			if (logger.isDebugEnabled())
				logger.debug("clobid="+ aco.clobId+"");
			
			// This must go to system out as its returned to the script
			System.out.println(aco.clobId+"");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error closing datasource connection");
					logger.error(e.getLocalizedMessage());
				}
			}
			
			System.exit(exitValue);
		}
	}
	
	
	/**
	 * Check if we are to get the clob data from a file or use the input parameter
	 * 
	 * @return
	 * @throws Exception 
	 */
	private boolean checkIfUsingFile() throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("METHOD ENTRY: checkIfUsingFile");
		
		if (this.useFile.length() > 0) {
			if (useFile.equals("Y") || useFile.equals("y")) {
				
				// Check that the file directory and filename are valid
				if (this.fileDirectory.length() > 0) {
					
					File file = new File(this.fileDirectory);
					if (file.isFile()) {
						if (logger.isDebugEnabled()) {
							logger.debug("Filename: " + file.getName());
							logger.debug("Filepath: " + file.getAbsolutePath());
						}
						
						this.xmlFile = file;
						return true;
					} else {
						if (logger.isDebugEnabled())
							logger.debug("File cannot be found: "+this.fileDirectory);
					}

				} else {
					throw new Exception("Invalid filename.");
				}
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 */
	private void setClobData() {
		if (logger.isDebugEnabled())
			logger.debug("METHOD ENTRY: setClobData");
		
		// Read the contents of the file into clob data
		StringBuffer sb = new StringBuffer();
		String thisLine;
		Scanner scnr;
		try {
			scnr = new Scanner(new FileInputStream(this.xmlFile), "utf-8");
			int line = 1;
			while (scnr.hasNextLine()) {
				thisLine = scnr.nextLine();
				if (line == 1) {
					// Remove any leading characters before the first angle bracket
					thisLine = thisLine.substring(thisLine.indexOf("<"));
					line++;
				}
				sb.append(thisLine.trim().replace("'", "''"));
			}
			scnr.close();
			this.clobData = sb.toString();
		} catch (FileNotFoundException e) {
			logger.error("Error setting the clob data from the incoming file");
			logger.error(e.getLocalizedMessage());
		}
	}
	
	
	/**
	 * Some characters such as single quotes will make teh insert/update fail so these have to be escaped
	 */
	private void parseIncomingClobData() {
		if (logger.isDebugEnabled())
			logger.debug("METHOD ENTRY: parseIncomingClobData");
		
		if (this.clobData.length() > 0) {
			// Ensure the data can be put into an insert/update SQL statement
			this.clobData = this.clobData.replace("'", "''");
		}
	}
	
	
	
	/**
	 * Setup data based on incoming properties:
	 * - CLOB data
	 * - Name of WL data source to be used
	 * - WL Context Factory
	 * - WL Provider URL
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public void getIncomingProperties(String args[]) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("METHOD ENTRY: getIncomingProperties");
		
		if (args.length == 7) {
			useFile = args[0];
			fileDirectory = args[1];
			clobData = args[2];
			url = args[3];
			username = args[4];
			password = args[5];	
			dbTestRequired = new Boolean(args[6]).booleanValue();
			
			/** 
			 * Example incoming properties if reading from a file:
			 * "Y C:\xhibit\CPPX_and_HK\Examples\FromCPP\2Mar\FirmList_457_20200302093137.xml USEFILEINSTEAD jdbc:oracle:thin:@x.x.127.85:1521:o10tst4 username password false"
			 */
			if (logger.isDebugEnabled()) {
				logger.debug("useFile:"+this.useFile);
				logger.debug("fileDirectory:"+this.fileDirectory);
				logger.debug("clobData:"+this.clobData);
				logger.debug("url:"+this.url);
				logger.debug("username:"+this.username);
				logger.debug("password:"+this.password);
				logger.debug("dbTestRequired:"+this.dbTestRequired);
			}
		} else {
			System.out.println("Invalid number of parameters given\nUsage: java AddCLOBObject <using a file: y or n> <full path of file including filename> <clob> <url> <username> <password> <dbtestrequired as boolean>");
			throw new Exception("Invalid number of parameters given\nUsage: java AddCLOBObject <using a file: y or n> <full path of file including filename> <clob> <url> <username> <password> <dbtestrequired as boolean>");
		}
	}
	
	
	/**
	 * Configure connection to Database
	 */
	private boolean setup() {
		if (logger.isDebugEnabled())
			logger.debug("METHOD ENTRY: setup");
		
		boolean allOk = false;
		if (logger.isDebugEnabled())
			logger.debug("About to setup");

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			
			conn = DriverManager.getConnection(url, username, password);
			if (conn == null) {
				allOk = false;
			} else {
				allOk = true;
				if (logger.isDebugEnabled())
					logger.debug("Connection details: "+conn);
			}
		} catch (Exception e) {
			logger.error("Error setting the connection using app server details");
			logger.error(e.getStackTrace());
		}
		
		return allOk;
	}
	
	/**
	 * Before trying to insert the CLOB, run a quick check
	 * 
	 * @return
	 * @throws SQLException
	 */
	private boolean runDBConnTest() throws SQLException {
		if (logger.isDebugEnabled())
			logger.debug("METHOD ENTRY: runDBConnTest");
		
		if (conn != null) {
			Statement stmt = null;
			try {
				stmt = conn.createStatement();
				String query = "select 1 from dual";
				ResultSet rs = stmt.executeQuery(query);
				if (rs != null) {
					if (logger.isDebugEnabled())
						logger.debug("Data found.");
					return true;
				} else {
					logger.error("No data found; database connectivity test failed.");
					return false;
				}
			} catch (SQLException e) {
	        	logger.error("Database Connection Test Issue");
	        	throw e;
			} finally {
				JdbcHelper.closeStatement(stmt);
			}
		} else {
			if (logger.isDebugEnabled())
				logger.debug("Connection is null!!!!");
			return false;
		}
	}
	
	
	/**
	 * @throws SQLException 
	 * 
	 */
	private void addClobToDB() throws SQLException {
		if (logger.isDebugEnabled())
			logger.debug("METHOD ENTRY: addClobToDB");
		
		boolean dbOk = true;
		if (dbTestRequired) {
			dbOk = runDBConnTest();
		}
		
		if ((dbOk) && (this.clobData.length() > 0)) {
			
			if (logger.isDebugEnabled())
				logger.debug("Length of CLOB to insert: " + this.clobData.length());
			
			String insQuery = "BEGIN insert into xhb_clob (clob_data) values(to_clob(?)) returning clob_id into ?; END;";
			String q3 = "update xhb_clob set clob_data=clob_data||to_clob('";
			String q4 = "') where clob_id=";
			String updQuery = "";

			long clobId=0;
			ArrayList<String> clobElements = (ArrayList<String>) splitEqually(this.clobData, 2400); // Using 2400 (rather than 2499 max) as some characters may be escaped so need to leave room for this
			
			if (logger.isDebugEnabled())
				logger.debug("CLOB has been split up");
			
			for (int i=0; i<clobElements.size(); i++) {
				if (logger.isDebugEnabled())
					logger.debug("CLOB loop: "+i);
				
				String thisPart = clobElements.get(i);
				if (i == 0) {
					clobId = insertCLOB(insQuery, thisPart);
					if (logger.isDebugEnabled())
						logger.debug("ClobId="+clobId);
					
				} else {
					updQuery = q3+thisPart+q4+clobId;
					updateCLOB(updQuery);
				}
			}
			
			this.clobId = clobId;
		} else {
			logger.error("There is no data to insert into the CLOB.");
		}
	}
	
	private long insertCLOB(String insertQuery, String clobData) throws SQLException {
		
        CallableStatement statement = null;
    	
        try {
			statement = conn.prepareCall(insertQuery);	
			statement.setString(1, clobData);
			statement.registerOutParameter(2, OracleTypes.NUMBER);
			statement.execute();	
			return statement.getLong(2);
        } catch (SQLException e) {
        	logger.error("Unable to insert CLOB");
        	throw e;
		} finally {
			closeStatement(statement);
		}
	}
	
	private void updateCLOB(String updateQuery) throws SQLException {
		
		Statement stmt = null;
    	
        try {
	        stmt = conn.createStatement();
	        stmt.executeQuery(updateQuery);
        } catch (SQLException e) {
        	logger.error("Unable to update CLOB");
        	throw e;
		} finally {
			closeStatement(stmt);
		}
	}
	
	
	/**
	 * Split the string into small enough chucks to insert into database
	 * 
	 * @param text
	 * @param sizeOfEachElement
	 * @return
	 */
	private List<String> splitEqually(String text, int sizeOfEachElement) {
		if (logger.isDebugEnabled())
			logger.debug("METHOD ENTRY: splitEqually");
		
		List<String> ret = new ArrayList<String>((text.length() + sizeOfEachElement - 1) / sizeOfEachElement);
		
		for (int start=0; start<text.length(); start+=sizeOfEachElement) {
			ret.add(text.substring(start, Math.min(text.length(), start+sizeOfEachElement)));
		}
		return ret;
	}
	
	private void closeStatement(final Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (final Throwable t) {
            	logger.error("Error closing Statement", t);
            }
        }
    }

}
