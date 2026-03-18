package uk.gov.courtservice.xhibit.business.audittrail;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailMessageFactory;

/**
 * <p>
 * Title: AuditTrailMessageFactoryImpl
 * </p>
 * <p>
 * Description: This is the implementation of the AuditTrailMessageFactory abstract class.
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

public class AuditTrailMessageFactoryImpl extends AuditTrailMessageFactory{
    private final String text;
    
    public AuditTrailMessageFactoryImpl(String queueName, String text){
        super(queueName);
        
        this.text = text;
    }
    
    /**
     * Populate the message using the text. There are currently no properties involved
     */
    public void populate(TextMessage message) throws JMSException {
        message.setText(text);
    }
}
