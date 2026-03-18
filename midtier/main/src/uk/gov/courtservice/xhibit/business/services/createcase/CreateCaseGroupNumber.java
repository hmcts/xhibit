package uk.gov.courtservice.xhibit.business.services.createcase;

import org.apache.log4j.Logger;

import oracle.jdbc.internal.OracleTypes;
import uk.gov.courtservice.framework.jdbc.core.JdbcHelper;
import uk.gov.courtservice.framework.services.CSServices;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * 
 */
public class CreateCaseGroupNumber {

	/**
	 * Query against the database.
	 */
    private static final String GET_CASE_GROUP_NUMBER = "{ = call xhb_case_group_generator.get_case_group_number(?,?) }";
	protected static final Logger log = CSServices.getLogger(CreateCaseGroupNumber.class);

	
	/**
	 * 
	 */
	public static int getCaseGroupNumber(int courtId) {	
        Connection conn = null;
        CallableStatement statement = null;
		int groupNum = 0;
		
		try {
	        conn= getConnection();
			statement = conn.prepareCall(GET_CASE_GROUP_NUMBER);
			statement.registerOutParameter(1, OracleTypes.NUMBER);
	
			//set objects to pass in
	        statement.setObject(2, courtId);
			statement.execute();

			//get the sequence number returned from DB
			groupNum = statement.getInt(1);

		} catch (SQLException e) {
			log.error("Unable to create case group number : "+ e);
		} finally {
			closeConnection(conn, statement);
		}

		return groupNum;		
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
