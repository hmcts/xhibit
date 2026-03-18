package uk.gov.courtservice.xhibit.services.gdgateway.outbound;

import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.jms.AbstractDeadLetterToFileMDB;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingMessageFactory;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the GdOutboundGatewayDeadLetterQueue
 * </p>
 * <p>
 * Description: Logs to file the message that failed from the GdOutboundGatewayQueue
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
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/GdOutboundGatewayDeadLetterQueue"
 *
 * @author GJS
 * @version $Id: OutGWMessQDLFileMessageBean.java,v 1.3 2007/01/24 12:25:00 qz4rwx Exp $
 */
public class OutGWMessQDLFileMessageBean extends AbstractDeadLetterToFileMDB implements MessageDrivenBean, MessageListener {
    private static final long serialVersionUID = 1L;

    @Override
    public String getTransactionName() {
        return "GdOutboundGatewayQueue";
    }

    /**
     * Overridden to audit to the Item Tracking Queue
     *
     * @param the transaction id
     * @param the message itself
     * @param the message properties
     */
    public void performAdditionalAuditing(String messageIdString, String payload, String properties)
    {
        try
        {
            if(messageIdString!=null)
            {
                long itemId = new Long(messageIdString).longValue();

                ItemTrackingInternalCode code = ItemTrackingInternalCode.UNDELIVERABLE;

                CSServices.getJMSServices().send(new ItemTrackingMessageFactory("scjsegateway/jms/ScjseItemTrackingQueue", itemId, code));

                log.info("JMS message from: " + getTransactionName() + " failed and forwarded code " + code + " to item tracking for id " + itemId + ".");
            }
        }
        catch(Exception e)
        {
            log.error("Failed message not audited to Item Tracking Queue, " +
                      ",JMS queue:"   + getTransactionName() +
                      ",transaction id:"     + messageIdString +
                      ",message payload:"    + payload +
                      ",message properties:" + properties, e);
        }
    }
}
