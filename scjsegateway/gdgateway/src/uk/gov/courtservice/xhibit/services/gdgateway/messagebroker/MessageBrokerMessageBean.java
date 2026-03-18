package uk.gov.courtservice.xhibit.services.gdgateway.messagebroker;

import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.services.gdgateway.inbound.InboundGatewayMessageBean;
import uk.gov.courtservice.xhibit.services.scjsegateway.common.ScjseGatewayMessageFactory;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.OutboundConstants;

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
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/GdMessageBrokerQueue"
 * @weblogic.dispatch-policy message.broker.execute.queue
 *
 * @author Steve Tully, Will, Fardell
 * @version $Id: MessageBrokerMessageBean.java,v 1.5 2007/03/06 11:28:55 qz4rwx Exp $
 */
public class MessageBrokerMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(MessageBrokerMessageBean.class);

    public static final String GDGITEMID = "GDGItemId";
    public static final String GDGTARGET = "GDGTarget";
    public static final String INBOUND   = "INBOUND";
    public static final String OUTBOUND  = "OUTBOUND";

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
        log.info("onTextMessage called");

        // Get the ID property
        String itemId = textMessage.getStringProperty(GDGITEMID);
        log.debug("Item ID: " + itemId);

        // Get the Type property
        String target = textMessage.getStringProperty(GDGTARGET);
        log.debug("Target: " + target);

        if( isInbound(target) ) {
            // If the Type is "INBOUND", send a message to scjsegateway/jms/ScjseInboundQueue
            HashMap<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put(InboundGatewayMessageBean.GDGITEMID, itemId);

            CSServices.getJMSServices().send(
                new ScjseGatewayMessageFactory(
                   "scjsegateway/jms/GdInboundGatewayQueue"
                   ,propertyMap));
        }
        else if( isOutbound(target) ) {
            // If the Type is "OUTBOUND", send a message to scjsegateway/jms/GdOutboundGatewayQueue
            HashMap<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put(OutboundConstants.REQUEST_ID, itemId);

            CSServices.getJMSServices().send(
                new ScjseGatewayMessageFactory(
                   "scjsegateway/jms/GdOutboundGatewayQueue"
                   ,propertyMap));
        }
        else {
            log.warn("Target <" + target + "> is unrecognised!");
        }

        log.info("onTextMessage completed");
    }

    protected boolean isInbound(String target) {
        return (target != null && target.equalsIgnoreCase(INBOUND) ? true : false);
    }

    protected boolean isOutbound(String target) {
        return (target != null && target.equalsIgnoreCase(OUTBOUND) ? true : false);
    }
}
