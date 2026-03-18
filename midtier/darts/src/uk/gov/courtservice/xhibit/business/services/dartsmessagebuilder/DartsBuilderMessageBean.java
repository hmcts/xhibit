package uk.gov.courtservice.xhibit.business.services.dartsmessagebuilder;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.darts.*;



/**
 * <p>
 * Title: Message Driven Bean that takes events from the DartsMessageOutboundQueue
 * </p>
 * <p>
 * Description: MDB takes messages from the DartsMessageOutboundQueue and writes
 * the messages to the DARTS_MESSAGE_AUDIT table in the darts schema as well as publishing the 
 * messsage on the DartsMessageOutboundTopic.
 * </p>
 * 
 * <p>
 * Company: Logica
 * </p>
 *
 * @ejb.bean acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           subscription-durability="NonDurable"
 * @ejb.transaction type="Required"
 * @weblogic.message-driven destination-jndi-name="jms/darts/DartsMessageOutboundQueue"
 * @weblogic.dispatch-policy DartsExecutionGroup
 *  
 * @author Luis Valenzuela
 * @version 1.1 20081125 
 */
public class DartsBuilderMessageBean  extends CSTextMessageBean 
implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = CSServices.getLogger(DartsBuilderMessageBean.class);
    
    private DartsMessageStoreDatabase _database;

    /**
     * Create any resources used by the bean
     *
     * @throws CreateException
     *             when there is a problem in configuration.
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        _database = new DartsMessageStoreDatabase();
    }


    /**
     * Method called when a JMS message is received by this MDB.
     *
     * @param TextMessage message
     *            An object message containing instances of
     *            the new darts message to be written to DB.
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="Required"
     */
    public void onTextMessage(TextMessage message) throws Exception {
        
        log.debug("\n=====================  Message recieved by DartsBuilderMessageBean.  ==========================\n");
        
        DartsMessageVO dartsDbInsert = new DartsMessageVO();
        dartsDbInsert.set_xhibitMessageCode(message.getStringProperty("xhibitMessageCode"));
        dartsDbInsert.set_exissMessageCode(message.getStringProperty("exissMessageCode"));
        dartsDbInsert.set_payload(message.getText());
        
        // Write the Darts message to the DARTS_MESSAGE_AUDIT table, upon insertion a trigger will
        // make the appropriate entry in the DARTS_NEW_MESSAGES_TABLE also.
        Long messageId = new Long(0);
        try {
            messageId = _database.insertDartsMessageStoreEntry(dartsDbInsert);
        } catch (DataAccessException dae) {
            handleDataAccessException(dae);
        } catch (Throwable t) {
            log.fatal("[insertDartsMessageAudit] catch Throwable " + t.getClass() + " : " + t.toString());
        }
        
        dartsDbInsert.setId( messageId.intValue() );
        // Now publish the message to the DartsMessageOutboundTopic, no subscribers currently
        // exist but was a requirement of the MOJ for future connectivity.
        
        Map<String, String> propertyMap = new HashMap<String, String>();
        propertyMap.put("xhibitMessageCode", dartsDbInsert.get_xhibitMessageCode());
        propertyMap.put("exissMessageCode", dartsDbInsert.get_exissMessageCode());   
        propertyMap.put("messageId", messageId.toString());
        
        log.debug( "Sending msg to Darts DartsMessageOutboundTopic: " + 
        dartsDbInsert.get_xhibitMessageCode() + " : " +  dartsDbInsert.get_exissMessageCode());
        CSServices.getJMSServices().send(
                           new DartsMessageFactory("jms/darts/DartsMessageOutboundTopic", propertyMap, message.getText())); 
                
    } /* End of onTextMessage */ 

    
    
    private void handleDataAccessException(DataAccessException dae) {
        log.info("catch DataAccessException " + dae.getClass());
        Throwable t = dae.getCause();
        log.fatal("catch DataAccessException about to rethrow");
        log.fatal("catch DataAccessException column :" + dae.getColumn());
        log.fatal("catch DataAccessException errorID:" + dae.getErrorID());
        log.fatal("catch DataAccessException message:" + dae.getMessage());
        log.fatal("catch DataAccessException cause  :" + dae.getCause());
        if (t instanceof SQLException) {
            log.fatal("SQLException");
            SQLException sqle = (SQLException) t;
            log.fatal("SQLException errorCode:" + sqle.getErrorCode());
            log.fatal("SQLException sqlState :" + sqle.getSQLState());
            log.fatal("SQLException message  :" + sqle.getMessage());
            log.fatal("SQLException cause    :" + sqle.getCause());
        }
        throw dae;
    } /* End of handleDataAccessException */
    
 }
