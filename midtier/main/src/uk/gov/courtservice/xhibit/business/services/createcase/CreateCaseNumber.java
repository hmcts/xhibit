package uk.gov.courtservice.xhibit.business.services.createcase;

import org.apache.log4j.Logger;

import oracle.jdbc.internal.OracleTypes;
import uk.gov.courtservice.framework.jdbc.core.JdbcHelper;
import uk.gov.courtservice.framework.services.CSServices;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;


/**
 * Class used to create a case number for new cases.
 * Takes in the court id and case type.
 * 
 * @author waltersn
 *
 */
public class CreateCaseNumber {

	/**
	 * Query against the database.
	 */
    private static final String GET_CASE_NUMBER = "{ = call xhb_case_number_generator.get_case_number(?,?,?) }";
	protected static final Logger log = CSServices.getLogger(CreateCaseNumber.class);

	
	/**
	 * Method generates the case number from the database
	 * @param courtId The Id of the court logged in
	 * @param caseType The type of Case 
	 * @return String representation of the case number : FirstLetterOfCaseType+Year+UniqueIdFromDB
	 */
	public static String getCaseNumber(int courtId, String caseType) throws SQLException {	  
		log.debug("About to retrieve case number for court "+ courtId+" that is of type "+caseType);
	    String generatedNumber = null;   
        Connection conn = null;
        CallableStatement statement = null;
		
		try {
	        conn= getConnection();
			statement = conn.prepareCall(GET_CASE_NUMBER);
			statement.registerOutParameter(1, OracleTypes.NUMBER);
	

			//set objects to pass in
	        statement.setObject(2, courtId);
			statement.setObject(3, caseType);
			statement.execute();

			//get the sequence number returned from DB
			int res = statement.getInt(1);
			log.debug("Case number retrieved is : "+res);
			//get current year
			int year = Calendar.getInstance().get(Calendar.YEAR);
			log.debug("Year is "+year);
			generatedNumber = caseType.charAt(0)+""+year+String.format("%04d",res);
			log.debug("Generated case number "+generatedNumber);

		} catch (SQLException e) {
			log.error("An error occurred while retrieving the case number "+e);
			throw e;
		} finally {
			closeConnection(conn, statement);
		}
		return generatedNumber;
		
	}

	/**
     * Extracted helper method used to acquire a database connection. Extracted
     * to allow overriding classes (e.g. a test class) to return a custom
     * <code>Connection</code> object.
     * 
     * @return A database <code>Connection</code>
     * @throws SQLException
     *             If an error occurs whilst acquiring the
     *             <code>Connection</code>.
     */
    protected static Connection getConnection() throws SQLException {
        return CSServices.getServiceLocator().getDataSource().getConnection();
    }
    
    /**
     * Extracted helper method used to close the <code>Connection</code> used
     * by the persistListLetters method. Extracted to allow overriding class
     * (e.g. test class) to perform extra processing (such as rolling back an
     * active transaction, to allow out-of-container testing).
     * 
     * @param con
     *            The <code>Connection</code> to close.
     */
    protected static void closeConnection(final Connection con, CallableStatement statement) {
    	JdbcHelper.closeStatement(statement);
        JdbcHelper.closeConnection(con);
    }
	
}
