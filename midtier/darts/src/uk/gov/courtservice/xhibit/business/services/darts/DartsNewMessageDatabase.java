package uk.gov.courtservice.xhibit.business.services.darts;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractDartsDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;


public class DartsNewMessageDatabase  extends AbstractDartsDatabase implements CommonDartsNewMessageDB{

    private static final Logger log = CSServices.getLogger(DartsNewMessageDatabase.class);
    private static final String GET_DARTS_MESSAGES = "{ ? = call darts_new_msgs_pkg.get_darts_message_refcur() }";
    private static final String INSERT_DARTS_RETRY = "{ ? = call darts_new_msgs_pkg.insert_darts_message_retry(?,?,?,?,?,?,?,?) }";
    private static DartsNewMessageDatabase me;

    /**
     * 
     *  Singleton instance, only called from within the class.
     */
    private DartsNewMessageDatabase() {
        log.info("DartsNewMessageDatabase constructor called");
    }

    
    /**
     * Singleton Accessor method.
     * 
     * @returns
     *      DartsNewMessageDatabase
     */
    public static DartsNewMessageDatabase getInstance() {
        if (me == null) {
            me = new DartsNewMessageDatabase();
        }
        return me;
    }

    
    public DartsMessageVO[] getMessages() {
    	log.debug("Getting New Messages from DB");
        DartsMessageVO[] messages = getMessagesFromDatabase();
        return messages;
    }

    
    /**
     *  Internal method for retrieving Darts messages from the DB.
     *   
     * @returns DartsMessageVO[] 
     *            An array of new messages from DAR_NEW_MESSAGES table.
     */
    private DartsMessageVO[] getMessagesFromDatabase() {

        DartsMessageVO[] messages = null;
        final DartsMessageRowProcessor rp = new DartsMessageRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_DARTS_MESSAGES);
        sp.setRowProcessor(rp);
        sp.execute(new Object[]{});
        messages = rp.getMessages();
        /*if(log.isDebugEnabled()){
            log.debug("Messages returned from Database." );
        }*/  
        return messages;
    }//  end of getMessagesFromDatabase

    
    
    /**
     *  Method to insert failed messages back into DAR_NEW_MESSAGES for retry.
     *  Message is inserted in the same state as retrieved and the SF updates the
     *  relevant fields.
     */
    public void reportMessageToBeRetried(DartsMessageVO message) {
             
        final  StoredFunction sf = createStoredFunction(INSERT_DARTS_RETRY);
        sf.registerInTypes( new int[]{Types.NUMERIC, 
                                      Types.VARCHAR, 
                                      Types.VARCHAR,
                                      Types.CLOB, 
                                      Types.NUMERIC, 
                                      Types.DATE,
                                      Types.DATE, 
                                      Types.DATE});
        
        log.debug("DartsMessageDatabase : Insert message for retry ID: " + message.getId());
        sf.executeFunction(new Object[]{ new Integer(message.getId()),
                                         message.get_xhibitMessageCode(),
                                         message.get_exissMessageCode(),
                                         message.get_payload(),
                                         new Integer(message.getRetryCount()),
                                         message.getNextRetryTime(),
                                         message.getCreationDate(),
                                         message.getCreationDate()},
                                         Types.NUMERIC);
        return;
   }// end of reportMessageToBeRetried

}
