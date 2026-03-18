package uk.gov.courtservice.framework.services.audittrail;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: JMSAuditProvider
 * </p>
 * <p>
 * Description: This AuditProvider sends the message to a JMS queue
 * for asynchronous processing
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */
public class JMSAuditProvider implements AuditProvider {
    
    public static Logger log = Logger.getLogger(JMSAuditProvider.class);
    
    private static final String FACTORY_CLASS = "uk.gov.courtservice.xhibit.business.audittrail.AuditTrailMessageFactoryImpl";
    
    /**
     * Send the message to a JMS queue
     */
    public void sendMessage(String message) {
        log.debug("Sending message to xhibit/jms/AuditTrailQueue");
        AuditTrailMessageFactory auditTrailMessageFactory = null;
        try{
            /*Need to lookup the class at runtime to avoid circulare reference issues at
            compile time*/
            auditTrailMessageFactory = 
                (AuditTrailMessageFactory) Class.forName(FACTORY_CLASS).getDeclaredConstructor(String.class,String.class).newInstance("xhibit/jms/AuditTrailQueue", message);
        } catch (Exception ex) {
            CSConfigurationException e = new CSConfigurationException(ex);
            CSServices.getDefaultErrorHandler().handleError(e, CSServices.class);
            throw e;
        }
        CSServices.getJMSServices().send(
                auditTrailMessageFactory);
        log.debug("finished Sending message to xhibit/jms/AuditTrailQueue");
    }
}
