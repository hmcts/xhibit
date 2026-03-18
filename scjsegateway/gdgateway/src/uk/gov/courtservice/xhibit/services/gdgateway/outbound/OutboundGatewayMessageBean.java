package uk.gov.courtservice.xhibit.services.gdgateway.outbound;

import java.util.Date;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.OutboundConstants;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the OutboundGatewayQueue in
 * the GDGateway
 * </p>
 * <p>
 * Description: MDB takes messages from the OutboundGatewayQueue and processes it
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
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/GdOutboundGatewayQueue"
 * @weblogic.dispatch-policy outbound.gateway.message.execute.queue
 *
 * @author GJS
 * @version $Id: OutboundGatewayMessageBean.java,v 1.4 2007/03/06 11:30:33 qz4rwx Exp $ Exp $
 */

public class OutboundGatewayMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;

    private OutboundGatewayMessageProcessor outboundGatewayMessageProcessor;

    /**
     * Create any resources used by the bean
     *
     * @throws CreateException
     *             when there is a problem in configuration.
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        outboundGatewayMessageProcessor = new OutboundGatewayMessageProcessor();
    }

    /**
     * Method called when a JMS message is received by this MDB.
     *
     * @param message:an object message
     * @ejb.interface-method view-type="local"
     * @ejb.permission role-name="XHBInternal"
     */
    public void onTextMessage(TextMessage message) throws Exception
    {
        Long requestId = new Long(message.getLongProperty(OutboundConstants.REQUEST_ID));
        Date messageTimeStamp = new Date(message.getJMSTimestamp());

        log.debug("OutboundGatewayMessageBean passed message with RequestId:" + requestId);
        log.debug("OutboundGatewayMessageBean passed message with TimeStamp:" + messageTimeStamp);

        outboundGatewayMessageProcessor.processMessage(requestId);
    }
}
