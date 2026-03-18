package uk.gov.courtservice.xhibit.services.scjsegateway.outbound;

import java.util.Enumeration;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the ScjseOutboundQueue
 * </p>
 * <p>
 * Description:
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
 * @ejb.security-identity run-as="XHBInternal"
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/ScjseOutboundQueue"
 * @weblogic.dispatch-policy outbound.message.execute.queue
 * 
 * @author Bal Bhamra
 * @version $Id: OutboundMessageBean.java,v 1.9 2007/03/06 11:31:42 qz4rwx Exp $
 */
public class OutboundMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;

    private static final String JMSX_DELIVERY_COUNT      = "JMSXDeliveryCount";
    private static final String JMS_BEA_REDELIVERY_LIMIT = "JMS_BEA_RedeliveryLimit";
    private static final String JMS_BEA_DELIVERY_FAILURE_REASON =  "JMS_BEA_DeliveryFailureReason";

    private static final Logger log = CSServices.getLogger(OutboundMessageBean.class);

    /**
     * Instantiates a RulesManager
     *
     * @throws CreateException
     *             when there is a problem in configuration.
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Method called when a JMS message is received by this MDB.
     *
     * @param msg
     *            a message containing key information that can be used by a
     *            Selector
     * @ejb.interface-method view-type="local"
     * @ejb.permission role-name="XHBInternal"
     */
    public void onTextMessage(TextMessage textMessage) throws Exception {
        ScjseOutboundProcessor scjseOutboundProcessor = null;
        String payload = null;
        String propertyName = null;
        String propertyValue = null;

        Enumeration e = textMessage.getPropertyNames();

        // Create a new HashMap
        HashMap<String, String> propertiesMap = new HashMap<String, String>();

        while (e.hasMoreElements()) {
            propertyName = (String) e.nextElement();
            log.debug("propertyName:" + propertyName);

            if (propertyName.equals(JMSX_DELIVERY_COUNT) ||
                propertyName.equals(JMS_BEA_REDELIVERY_LIMIT)||
                propertyName.equals(JMS_BEA_DELIVERY_FAILURE_REASON)) {
                continue;
            }

            // Get propertyValue
            propertyValue = textMessage.getStringProperty(propertyName);
            log.debug("propertyValue:" + propertyValue);

            // Place property name and value in HashMap
            propertiesMap.put(propertyName, propertyValue);
        }

        // Get JMS Body
        payload = textMessage.getText();

        //Get scjseOutboundProcessor Implementation from factory.
        scjseOutboundProcessor = ScjseOutboundProcessorFactory.getInstance().getScjseOutboundProcessor();
        log.debug("BEFORE ScjseOutboundProcessor.processRequest");
        scjseOutboundProcessor.processRequest(propertiesMap, payload);
        log.debug("AFTER ScjseOutboundProcessor.processRequest");
    }
}
