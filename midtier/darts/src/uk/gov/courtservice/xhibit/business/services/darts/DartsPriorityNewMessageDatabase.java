package uk.gov.courtservice.xhibit.business.services.darts;


import java.sql.Types;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractDartsDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;


public class DartsPriorityNewMessageDatabase  extends AbstractDartsDatabase implements CommonDartsNewMessageDB{

    private static final Logger log = CSServices.getLogger(DartsPriorityNewMessageDatabase.class);
    private static final String GET_DARTS_MESSAGES = "{ ? = call darts_pr_new_msgs_pkg.get_pr_darts_message_refcur() }";
    private static final String INSERT_DARTS_RETRY = "{ ? = call darts_pr_new_msgs_pkg.insert_pr_darts_message_retry(?,?,?,?,?,?,?,?) }";
    private static CommonDartsNewMessageDB me;

    /**
     * 
     *  Singleton instance, only called from within the class.
     */
    private DartsPriorityNewMessageDatabase() {
        log.debug("DartsPriorityNewMessageDatabase constructor called");
    }

    
    /**
     * Singleton Accessor method.
     * 
     * @returns
     *      DartsPriorityNewMessageDatabase
     */
    public static CommonDartsNewMessageDB getInstance() {
        if (me == null) {
            me = new DartsPriorityNewMessageDatabase();
        }
        return me;
    }

    
    /* (non-Javadoc)
     * @see uk.gov.courtservice.xhibit.business.services.darts.CommonDartsNewMessageDB#getMessages()
     */
    public DartsMessageVO[] getMessages() {
    	log.debug("Getting Priority New Messages from DB");
        DartsMessageVO[] messages = getMessagesFromDatabase();
        return messages;
    }

    
    /**
     *  Internal method for retrieving Darts priority messages from the DB.
     *   
     * @returns DartsMessageVO[] 
     *            An array of new messages from DAR_PRIORITY_NEW_MESSAGES table.
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

    
    
    /* (non-Javadoc)
     * @see uk.gov.courtservice.xhibit.business.services.darts.CommonDartsNewMessageDB#reportMessageToBeRetried(uk.gov.courtservice.xhibit.business.services.darts.DartsMessageVO)
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
        
        log.debug("DartsPriorityNewMessageDatabase : Insert message for retry ID: " + message.getId());
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
