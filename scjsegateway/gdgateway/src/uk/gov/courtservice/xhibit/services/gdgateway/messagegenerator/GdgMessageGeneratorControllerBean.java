package uk.gov.courtservice.xhibit.services.gdgateway.messagegenerator;

import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.jms.MessageFactory;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.EventMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.MessageGeneratorDatabase;
import uk.gov.courtservice.xhibit.services.gdgateway.messagebroker.MessageBrokerMessageBean;
import uk.gov.courtservice.xhibit.services.scjsegateway.common.ScjseGatewayMessageFactory;

/**
 * 
 * Controller Bean For Starting Timers For Generating Messages on the scjse gateway
 * 
 * @ejb.bean name="GdgMessageGeneratorController" description="Message Generator
 *           Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="GdgMessageGeneratorControllerHome"
 *           local-jndi-name="GdgMessageGeneratorControllerLocalHome"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 * @author Will Fardell
 * @version $Id: GdgMessageGeneratorControllerBean.java,v 1.1 2006/07/04 13:04:04
 *          szn20z Exp $
 */
public class GdgMessageGeneratorControllerBean extends CSSessionBean implements SessionBean {
    
    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(GdgMessageGeneratorControllerBean.class);
    
    private static final String ALL_MESSAGE_TYPES = "ALL";

    private MessageGeneratorDatabase database;
    
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        database = new MessageGeneratorDatabase();
    }
    
    public void doTask(String taskName) {
        processMessages();
    }

    public void processMessages() {
        log.info("processMessages() called");
        
        EventMessageVO[] messages = database.getMessages(ALL_MESSAGE_TYPES);
        log.debug("Number of messages returned <" + messages.length + ">");

        MessageFactory[] factories = new MessageFactory[messages.length];            
        for (int i = 0; i < messages.length; i++) {
            log.debug("About to build a message to GdMessageBrokerQueue!");
            HashMap<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put(MessageBrokerMessageBean.GDGITEMID, messages[i].getMessageId().toString());
            propertyMap.put(MessageBrokerMessageBean.GDGTARGET, messages[i].getMessageType());
            factories[i] = new ScjseGatewayMessageFactory(
               "scjsegateway/jms/GdMessageBrokerQueue"
               ,propertyMap);
        }
        
        if( factories.length > 0 ) {
            log.debug("about to send <" + factories.length + "> messages!");
            CSServices.getJMSServices().send(factories);
            log.debug("sent them!");
        }
        else {
            log.info("Nothing to send on this iteration!");
        }

        log.info("processMessages() completed");
    }
}
