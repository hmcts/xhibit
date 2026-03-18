package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server;

import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import java.util.Enumeration;
import javax.ejb.CreateException;

/**
 * <p>
 * Title: Message Driven Bean that is used for testing inbound messages
 * </p>
 * <p>
 * Description: Listens to the InboundTestQueue and passes messages on to helper class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @ejb.bean acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           subscription-durability="NonDurable"
 * @ejb.transaction type="Required"
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/InboundTestQueue"
 *
 * @author GJS
 * @version $Id: InboundStubMessageBean.java,v 1.2 2006/10/26 11:16:36 qz4rwx Exp $
 */
public class InboundStubMessageBean extends CSTextMessageBean implements MessageDrivenBean,MessageListener
{
    private static final long serialVersionUID = 1L;


    /**
     * Instantiates a RulesManager
     * 
     * @throws CreateException
     *             when there is a problem in configuration.
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
    
    public void onTextMessage(final TextMessage textMessage) throws Exception {

		String payload = null;
		String propertyName = null;
        String id = null;
        String type = null;
        
		Enumeration e = textMessage.getPropertyNames();

		while (e.hasMoreElements()) {
		   propertyName = (String) e.nextElement();
		   log.debug("propertyName:" + propertyName);

		   if (propertyName.equalsIgnoreCase("id")) {
               id = textMessage.getStringProperty(propertyName);
		       log.debug("id:" + id);
		   }
           if (propertyName.equalsIgnoreCase("type")) {
               type = textMessage.getStringProperty(propertyName);
               log.debug("type:" + type);
           }
		}

		payload = textMessage.getText();

		InboundStubHelper inboundStubHelper = new InboundStubHelper();

		inboundStubHelper.sendDeliverMessages(id,type,payload);

	}
}
