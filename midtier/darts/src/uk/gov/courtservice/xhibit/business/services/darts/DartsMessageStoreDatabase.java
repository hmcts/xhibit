package uk.gov.courtservice.xhibit.business.services.darts;

import uk.gov.courtservice.framework.jdbc.core.AbstractDartsDatabase;

import java.sql.SQLException;
import java.sql.Types;
import java.sql.CallableStatement;
import java.sql.Connection;
import oracle.sql.*;


import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
//import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Database utility class for Darts components to access DARTS_MESSAGE_STORE table.
 * </p>
 * <p>
 * Description: Allows access to the DARTS_MESSAGE_STORE table through a set of stored functions
 * and stored procedures.   
 * </p>
 * <p>
 * Company: logica
 * </p>
 * 
 * @author Luis Valenzuela
 */
public class DartsMessageStoreDatabase extends AbstractDartsDatabase {
    
    private static final Logger log = CSServices.getLogger(DartsMessageStoreDatabase.class);

    private static final String INSERT_DARTS_MESSAGE_STORE = "{ ? = call dar_message_pkg.insert_darts_message_store(?,?,?) }";
    private static final String REPORT_SUCCESS_MESSAGE_STORE = "{ ? = call dar_message_pkg.success_darts_msg_store(?) }";
    private static final String REPORT_FAIL_MESSAGE_STORE = "{ ? = call dar_message_pkg.fail_darts_msg_store(?,?) }";
    
    /**
     * Insert a new Darts message into the database.
     * 
     * @param value -
     *            The value object containing the information to insert.
     */
    public Long insertDartsMessageStoreEntry(DartsMessageVO value) throws DataAccessException 
    {
        final StoredFunction sf = createStoredFunction(INSERT_DARTS_MESSAGE_STORE);
        
        sf.registerInTypes(new int[] { Types.VARCHAR, 
                                       Types.VARCHAR, 
                                       Types.CLOB });
        
        Long messageId = (Long) sf.executeFunction(  new Object[] { value.get_xhibitMessageCode(), 
                                                                    value.get_exissMessageCode(), 
                                                                    value.get_payload() }, 
                                                                    Types.BIGINT);
        
        /*log.debug("Added new Darts message < message_id: " + messageId + "  xhibitCode:" + value.get_xhibitMessageCode() +
                " exissCode: " + value.get_exissMessageCode() + ">");*/
        return messageId;
    } // end of insertDartsMessageStoreEntry()
    
    
    /**
     * Update all the messages DAR_MESSAGE_STORE that have been successfully sent.
     * The code does not use framework classes as a Custom data type is being used.
     * 
     * @param messageIds -
     *            An Integer Array of all the success message Ids
     */
    public void reportSuccesses(Integer[] messageIds) throws DataAccessException 
    {
        Connection conn = null;
        CallableStatement cStm = null;
        try {
            conn = getDataSource().getConnection();
            ArrayDescriptor desc = ArrayDescriptor.createDescriptor("DARTS_MESSAGE_ID_ARRAY", conn );
            ARRAY newArray = new ARRAY( desc, conn, messageIds );
            cStm = conn.prepareCall(REPORT_SUCCESS_MESSAGE_STORE);
            
            cStm.registerOutParameter(1, Types.NUMERIC);
            cStm.setObject(2, newArray);
            cStm.execute();
            cStm.close();
            conn.close();
           
        } catch (SQLException e) {
            log.error("reportSuccesses() failed against DAR_MESSAGE_STORE: " +e.getMessage() );
            e.printStackTrace();
        }        
        finally{
            
            try {
                if(cStm != null)  cStm.close();  
                if(conn != null)  conn.close();
            } catch (SQLException e) {
                log.error("ERROR closing connection after reportSuccesses(): " + e.getMessage() );
                e.printStackTrace();
            }  
        }
        if(log.isDebugEnabled()){
            log.debug("Updated "+ messageIds.length + " messages with success." );
        }
        
        return;
    }// end of reportSuccesses()
    
    
    /**
     * Update all the messages DAR_MESSAGE_STORE that have been unsuccessfully sent.
     * The code does not use framework classes as a Custom data type is being used.
     * 
     * The params are separated into 2 arrays to allow for simple parameter passing into 
     * the Oracle Stored procedure.
     * 
     * @param failIds -
     *            An Integer Array of all the failure message Ids
     * @param failDetails -
     *            An String Array of all the failure message details
     */
    public void reportFailures(Integer[] failIds, String[] failDetails)throws DataAccessException 
    {
        Connection conn = null;
        CallableStatement cStm = null;
                      
        try {
            conn = getDataSource().getConnection();
            ArrayDescriptor descId = ArrayDescriptor.createDescriptor("DARTS_MESSAGE_ID_ARRAY", conn );
            ArrayDescriptor descDetail = ArrayDescriptor.createDescriptor("DARTS_MESSAGE_FAIL_ARRAY", conn );
            
            ARRAY newIdArray = new ARRAY( descId, conn, failIds );
            ARRAY newDetailArray = new ARRAY( descDetail, conn, failDetails );
            
            cStm = conn.prepareCall(REPORT_FAIL_MESSAGE_STORE);
            cStm.registerOutParameter(1, Types.NUMERIC);
            cStm.setObject(2, newIdArray);
            cStm.setObject(3, newDetailArray);
            cStm.execute();
            cStm.close();
            conn.close();

            /*if(log.isDebugEnabled()){
                log.debug("Updated "+ failIds.length + " messages with failure." );
            }*/    
           
        } catch (SQLException e) {
            log.error("reportFailures() failed against DAR_MESSAGE_STORE: " +e.getMessage() );
            e.printStackTrace();
        }        
        finally{
            
            try {
                if(cStm != null)  cStm.close();  
                if(conn != null)  conn.close();
            } catch (SQLException e) {
                log.error("ERROR closing connection after reportSuccesses(): " + e.getMessage() );
                e.printStackTrace();
            }  
        }
        return;
    }// end of reportFailures()
    
    
}// end of DartsMessageStoreDatabase Class
