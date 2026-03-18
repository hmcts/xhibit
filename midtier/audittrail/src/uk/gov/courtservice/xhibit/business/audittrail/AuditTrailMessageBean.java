package uk.gov.courtservice.xhibit.business.audittrail;

import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;

/**
 * <p>
 * Title: AuditTrailMessageBean
 * </p>
 * <p>
 * Description: This is the message bean which consumes messages from
 * the AuditTrailQueue JMS queue. 
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
 * @weblogic.message-driven destination-jndi-name="xhibit/jms/AuditTrailQueue"
 * 
 *
 * @author James Powell
 * @version 1.0 20090423 
 */

public class AuditTrailMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener{
    private static final long serialVersionUID = 1L;
    
    private static Logger log = Logger.getLogger(AuditTrailMessageBean.class);
    
    /**
     * When a text message is received, this method encrypts the
     * message using the XhibitAuditEncryptor and uses the AuditAppender
     * class to write the encrypted message to the log
     */
    protected void onTextMessage(TextMessage message) throws Exception {
        String messageString = message.getText();
        log.debug(messageString);
        String encryptedMessage =  XhibitAuditEncryptor.getInstance().encryptMessage(messageString);
        AuditAppender.writeAuditMessage(encryptedMessage);
    }
}
