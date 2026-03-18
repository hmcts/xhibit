package uk.gov.courtservice.xhibit.business.services.messagegenerator;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.jms.MessageFactory;
import uk.gov.courtservice.xhibit.business.services.messagebroker.MessageBrokerMessageFactory;
import uk.gov.courtservice.xhibit.business.vos.messagegenerator.MessageVO;
import uk.gov.courtservice.xhibit.database.messagegenerator.MessageGeneratorExissDatabase;

/**
 * 
 * Controller Bean For Starting Timers For Generating Messages
 * 
 * @ejb.bean name="MessageGeneratorController" description="Message Generator
 *           Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="MessageGeneratorControllerHome"
 *           local-jndi-name="MessageGeneratorControllerLocalHome"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 * @author Will Fardell
 * @version $Id: MessageGeneratorControllerBean.java,v 1.1 2006/07/04 13:04:04
 *          bzjrnl Exp $
 */
public class MessageGeneratorControllerBean extends CSSessionBean implements SessionBean {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(MessageGeneratorControllerBean.class);

    private MessageGeneratorExissDatabase exissDatabase;

    /**
     * Initialises all of the instance variables for this session bean.
     * 
     * @see uk.gov.courtservice.framework.business.services.CSSessionBean
     *      #ejbCreate()
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        exissDatabase = new MessageGeneratorExissDatabase();
    }

    /**
     * Implementation of RemoteTask so that this process is called by the timer
     * process. This method must have the same transactional behaviour as
     * processFormattingDocument
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"

     */
    public void doTask(String taskName) {
        processMessages();
    }

    /**
     * Implementation of RemoteTask so that this process is called by the timer
     * process. This method must have the same transactional behaviour as
     * processFormattingDocument
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void processMessages() {
        MessageVO[] messages = exissDatabase.getMessages();

        MessageFactory[] factories = new MessageFactory[messages.length];
        for (int i = 0; i < factories.length; i++) {
            // The following condition is only required for testing! WF 14/07/2006
            if("ERROR".equals(messages[i].getTarget())) {
                throw new RuntimeException("Detected ERROR target, raising error!");
            }
            factories[i] = new MessageBrokerMessageFactory("xhibit/jms/MessageBrokerQueue", messages[i].getItemId(),
                    messages[i].getTarget(), messages[i].getItemType());
        }
        CSServices.getJMSServices().send(factories);
        
        log.info("Sent " + messages.length + " messages to the message broker.");
    }

}
