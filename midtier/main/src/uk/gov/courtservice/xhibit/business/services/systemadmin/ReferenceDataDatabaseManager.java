package uk.gov.courtservice.xhibit.business.services.systemadmin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jdbc.internal.OracleTypes;
import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.JdbcHelper;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientBasicValue;

public class ReferenceDataDatabaseManager extends AbstractXhibitDatabase {
	
    private static final String ADD_CHAMBER = "{ call xhb_ref_chamber_pkg.populate_ref_chamber(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";    
    private static final String UPDATE_CHAMBER_REF = "{ call xhb_ref_advocate_pkg.update_chamber_ref(?, ?, ?) }";
    private static final String UPDATE_CHAMBER_DETAILS = "{ call xhb_ref_advocate_pkg.update_chamber_details(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private static final String UPDATE_COUNSEL_DETAILS = "{ call xhb_ref_advocate_pkg.update_counsel_details(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private static final String INSERT_COUNSEL_DETAILS = "{ call xhb_ref_advocate_pkg.insert_counsel_details(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private static final String DELETE_COUNSEL_DETAILS = "{ call xhb_ref_advocate_pkg.delete_counsel_details(?, ?) }";
    private static final String COUNT_BY_CREST_CHAMBER_ID = "{ call xhb_ref_advocate_pkg.find_by_crest_chamber_id(?, ?) }";
    private static final String DELETE_REF_CHAMBER = "{ call xhb_ref_chamber_pkg.delete_ref_chamber(?,?) }";
    private static final String DELETE_SH_JUSTICE = "{call xhb_housekeeping_pkg.delete_sh_justice(?) }";
    
    /**
     *  Find xhb_ref_chamber objects by the given crest_chamber_id and where obs_ind != 'Y'
     *  and return the count
     *  @param crestChamberId the id of the crest chamber to link to xhb_ref_chamber
     */
    public Integer countRefAdvocateWithCrestChamberId(Integer crestChamberId) throws SQLException{
    	if (log.isDebugEnabled()) {
    		log.debug("countRefAdvocateWithCrestChamberId(crestChamberId="+crestChamberId+")");
    	}
        Connection conn = null;
        CallableStatement statement = null;
    	
        try {
	        conn = getConnection();
			statement = conn.prepareCall(COUNT_BY_CREST_CHAMBER_ID);	
			statement.registerOutParameter(1, OracleTypes.NUMBER);
	        statement.setObject(2, crestChamberId);
			statement.execute();	
			return statement.getInt(1);	
        } catch (SQLException e) {
        	log.error("Unable to return count of num ref advocates with given crestChamberId: " + crestChamberId);
        	throw e;
		} finally {
			closeConnection(conn, statement);
		}
    }
    
    /**
     *  Delete xhb_ref_chamber objects by setting obs_ind = Y for the given crest_chamber_id for each court
     *  @param crestChamberId the id of the crest chambers to delete
     *  @param userDisplayName user name of logged in user
     * @throws SQLException 
     */
    public void deleteRefChamber(Integer crestChamberId, String userDisplayName) throws SQLException {
    	if (log.isDebugEnabled()) {
    		log.debug("deleteRefChamber(crestChamberId="+crestChamberId+", userDisplayName="+userDisplayName+")");
    	}
        Connection conn = null;
        CallableStatement statement = null;
    	
        try {
	        conn = getConnection();
			statement = conn.prepareCall(DELETE_REF_CHAMBER);	
	        statement.setObject(1, crestChamberId);
	        statement.setObject(2, userDisplayName);
			statement.execute();	
        } catch (SQLException e) {
          log.error("Unable to delete ref chamber objects with given crestChamberId: " + crestChamberId);
        	throw e;
		} finally {
			closeConnection(conn, statement);
		}
    }
    
    /**
     *  Delete xhb_sh_justice objects by deleting the matching entry in XHB_SH_JUSTICE for SH_JUSTICE_ID
     *  @param shJusticeId the id of the sh justice table entry to delete
     * @throws SQLException 
     */
    public void deleteShJustice(Integer shJusticeId) throws SQLException {
    	if (log.isDebugEnabled()) {
    		log.debug("deleteShJustice(shJusticeId="+shJusticeId+")");
    	}
        Connection conn = null;
        CallableStatement statement = null;
    	
        try {
	        conn = getConnection();
			statement = conn.prepareCall(DELETE_SH_JUSTICE);	
	        statement.setObject(1, shJusticeId);
			statement.execute();	
        } catch (SQLException e) {
          log.error("Unable to delete sh justice entry with given sh justice id: " + shJusticeId);
        	throw e;
		} finally {
			closeConnection(conn, statement);
		}
    }
    
	/**
	 * Invokes the determine_case_status function on the xhb_case_pkg package to determine the status
	 * of a given case
	 * @param caseId The identifier of the case to be checked
	 * @return	the case status
	 */
	public Integer getNextCrestChamberId() {
		if (log.isDebugEnabled()) {
			log.debug("getNextCrestChamberId()");
		}
		final StoredFunction sf = createStoredFunction("{ ? = call xhb_ref_chamber_pkg.get_next_crest_chamber_id() }");
		final Integer crestChamberId = (Integer) sf.executeFunction(new Object[0], Types.INTEGER);
        return crestChamberId;
	}
		
	public void addChamber(Integer refChamberId, String obsInd, String isGlobal, String dxRef, String locationCode,
			Integer crestChamberId, String firmName, Integer addressId, String clerkName, String userName) throws SQLException{
		if (log.isDebugEnabled()) {
			log.debug("addChamber(refChamberId="+refChamberId+", obsInd="+obsInd+", isGlobal="+isGlobal+", dxRef="+dxRef+", locationCode="+locationCode+
				", crestChamberId="+crestChamberId+", firmName="+firmName+", addressId="+addressId+", clerkName="+clerkName+", userName="+userName+")");
		}
		Connection conn = null;
        CallableStatement statement = null;
		
		try {
	        conn= getConnection();
			statement = conn.prepareCall(ADD_CHAMBER);	
			//set objects to pass in
	        statement.setObject(1, refChamberId);
	        statement.setObject(2, obsInd);
	        statement.setObject(3, isGlobal);
	        statement.setObject(4, dxRef);
	        statement.setObject(5, locationCode);
	        statement.setObject(6, crestChamberId);
	        statement.setObject(7, firmName);
	        statement.setObject(8, addressId);
	        statement.setObject(9, clerkName);
	        statement.setObject(10, userName);

			statement.execute();

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, statement);
		}
	}
	
	public void updateChamberRef(Integer crestAdvocateId, Integer crestChamberId, String userName) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("updateChamberRef(crestAdvocateId="+crestAdvocateId+", crestChamberId="+crestChamberId+", userName="+userName+")");
		}
		Connection conn = null;
		CallableStatement statement = null;

		try {
			conn = getConnection();
			statement = conn.prepareCall(UPDATE_CHAMBER_REF);
			// set objects to pass in
			statement.setObject(1, crestAdvocateId);
			statement.setObject(2, crestChamberId);
			statement.setObject(3, userName);

			statement.execute();

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, statement);
		}
	}
	
	public void updateChamberDetails(Integer crestChamberId, String dxRef, String locationCode, String firmName,
			Integer addressId, String clerkName, String address1, String address2, String address3, String address4,
			String town, String county, String postcode, String country, String userName, String phoneNumber,
			String faxNumber, String email, String secureEmail) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("updateChamberDetails(crestChamberId="+crestChamberId+", dxRef="+dxRef+", locationCode="+locationCode+", firmName="+firmName+", addressId="+addressId+
				", clerkName="+clerkName+", address1="+address1+", address2="+address2+", address3="+address3+", address4="+address4+", town="+town+", county="+county+", postcode="+postcode+
				", country="+country+", userName="+userName+", phoneNumber="+phoneNumber+", faxNumber="+faxNumber+", email="+email+", secureEmail="+secureEmail+")");
		}
		Connection conn = null;
		CallableStatement statement = null;

		try {
			conn = getConnection();
			statement = conn.prepareCall(UPDATE_CHAMBER_DETAILS);
			// set objects to pass in
			statement.setObject(1, crestChamberId);
			statement.setObject(2, dxRef);
			statement.setObject(3, locationCode);
			statement.setObject(4, firmName);
			statement.setObject(5, addressId);
			statement.setObject(6, clerkName);
			statement.setObject(7, address1);
			statement.setObject(8, address2);
			statement.setObject(9, address3);
			statement.setObject(10, address4);
			statement.setObject(11, town);
			statement.setObject(12, county);
			statement.setObject(13, postcode);
			statement.setObject(14, country);
			statement.setObject(15, userName);
			statement.setObject(16, phoneNumber);
			statement.setObject(17, faxNumber);
			statement.setObject(18, email);
			statement.setObject(19, secureEmail);

			statement.execute();

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, statement);
		}
	}

	public void updateCounselDetails(Integer refAdvocateId, String firstName, String middleName, String surname,
			String title, String initials, String legalRepType, Integer yearOfCall, String vatNo, Integer barNo,
			String honours, String advTypeInd, String userName) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("updateCounselDetails(refAdvocateId="+refAdvocateId+", firstName="+firstName+", middleName="+middleName+", surname="+surname+", title="+title+
				", initials="+initials+", legalRepType="+legalRepType+", yearOfCall="+yearOfCall+", vatNo="+vatNo+", barNo="+barNo+", honours="+honours+", advTypeInd="+advTypeInd+
				", userName="+userName+")");
		}
		Connection conn = null;
		CallableStatement statement = null;

		try {
			conn = getConnection();
			statement = conn.prepareCall(UPDATE_COUNSEL_DETAILS);
			// set objects to pass in
			statement.setObject(1, refAdvocateId);
			statement.setObject(2, firstName);
			statement.setObject(3, middleName);
			statement.setObject(4, surname);
			statement.setObject(5, title);
			statement.setObject(6, initials);
			statement.setObject(7, legalRepType);
			statement.setObject(8, yearOfCall);
			statement.setObject(9, vatNo);
			statement.setObject(10, barNo);
			statement.setObject(11, honours);
			statement.setObject(12, advTypeInd);
			statement.setObject(13, userName);

			statement.execute();

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, statement);
		}
	}
	
	public void addCounsel(Integer refAdvocateId, String firstName, String middleName, String surname,
			String title, String initials, String legalRepType, Integer yearOfCall, String vatNo, Integer barNo,
			String honours, String advTypeInd, Integer crestChamberId, String userName) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("addCounsel(refAdvocateId="+refAdvocateId+", firstName="+firstName+", middleName="+middleName+", surname="+surname+", title="+title+
				", initials="+initials+", legalRepType="+legalRepType+", yearOfCall="+yearOfCall+", vatNo="+vatNo+", barNo="+barNo+", honours="+honours+", advTypeInd="+advTypeInd+
				", crestChamberId="+crestChamberId+", userName="+userName+")");
		}
		Connection conn = null;
		CallableStatement statement = null;

		try {
			conn = getConnection();
			statement = conn.prepareCall(INSERT_COUNSEL_DETAILS);
			// set objects to pass in
			statement.setObject(1, refAdvocateId);
			statement.setObject(2, firstName);
			statement.setObject(3, middleName);
			statement.setObject(4, surname);
			statement.setObject(5, title);
			statement.setObject(6, initials);
			statement.setObject(7, legalRepType);
			statement.setObject(8, yearOfCall);
			statement.setObject(9, vatNo);
			statement.setObject(10, barNo);
			statement.setObject(11, honours);
			statement.setObject(12, advTypeInd);
			statement.setObject(13, crestChamberId);
			statement.setObject(14, userName);

			statement.execute();

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, statement);
		}
	}
	
	public void deleteCounselDetails(Integer crestAdvocateId, String userName) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("deleteCounselDetails(crestAdvocateId="+crestAdvocateId+", userName="+userName+")");
		}
		Connection conn = null;
		CallableStatement statement = null;

		try {
			conn = getConnection();			
			statement = conn.prepareCall(DELETE_COUNSEL_DETAILS);
			// set objects to pass in
			statement.setObject(1, crestAdvocateId);
			statement.setObject(2, userName);

			statement.execute();

		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(conn, statement);
		}
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

    /**
     * Get the unsubscribed wll recipient values for the specified court
     * 
     * @param courtId
     *            the court to get the data for
     * @param crestSolFirmId
     * 			  crest sol firm id to match with
     * @return an array containing the recipient data
     * 
     * @throws IllegalArgumentException
     *             if the courtId is <i>null</i>.
     */
    public XhbWllRecipientBasicValue[] getWllRecipientValues(final Integer courtId, final Integer crestSolFirmId) {
    	if (log.isDebugEnabled()) {
    		log.debug("getWllRecipientValues(courtId="+courtId+", crestSolFirmId="+crestSolFirmId+")");
    	}

        validateParameterNotNull("courtId", courtId);
        validateParameterNotNull("crestSolFirmId", crestSolFirmId);

        final WllRecipientComplexValuesRowProcessor rp = new WllRecipientComplexValuesRowProcessor();
        final StoredProcedure sp = createStoredProcedure("{ ? = call xhb_list_distribution_pkg.get_wll_rec_crest_sof(?,?) }");
        sp.registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { courtId, crestSolFirmId });
        return rp.getWllRecipientBasicValues();
    }
    
}