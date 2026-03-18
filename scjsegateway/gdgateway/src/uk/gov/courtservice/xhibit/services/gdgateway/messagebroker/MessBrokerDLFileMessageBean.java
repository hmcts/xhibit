package uk.gov.courtservice.xhibit.services.gdgateway.messagebroker;

import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.jms.AbstractDeadLetterToFileMDB;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingMessageFactory;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the GdMessageBrokerQueue
 * </p>
 * <p>
 * Description: Logs to file the message that failed from the GdMessageBrokerDeadLetterQueue
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
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/GdMessageBrokerDeadLetterQueue"
 *
 * @author GJS
 * @version $Id: MessBrokerDLFileMessageBean.java,v 1.3 2007/01/24 12:24:52 qz4rwx Exp $
 */
public class MessBrokerDLFileMessageBean extends AbstractDeadLetterToFileMDB implements MessageDrivenBean, MessageListener {
    private static final long serialVersionUID = 1L;

    private static final String GDGTARGET = "GDGTarget";
    private static final String OUTBOUND  = "OUTBOUND";

    @Override
    public String getTransactionName() {
        return "GdMessageBrokerQueue";
    }

    /**
     * Overridden to audit to the Item Tracking Queue
     * Only outbound messages will be audited in this way
     *
     * @param the transaction id
     * @param the message itself
     * @param the message properties
     */
    public void performAdditionalAuditing(String messageIdString, String payload, String properties)
    {
        try
        {
            String truncatedPropertiesString = null;

            if(properties.toString().indexOf(GDGTARGET) >= 0)
            {
                truncatedPropertiesString = properties.substring(properties.toString().indexOf(GDGTARGET));
            }

            if(truncatedPropertiesString==null)
            {
                return;
            }

            String gdgTargetType = null;

            if (truncatedPropertiesString.toString().indexOf("<PropertyValue>")  >= 0 &&
                truncatedPropertiesString.toString().indexOf("</PropertyValue>") >= 0)
            {
                int startGdgTargetType = truncatedPropertiesString.indexOf("<PropertyValue>") + 15;
                int endGdgTargetType   = truncatedPropertiesString.indexOf("</PropertyValue>");

                gdgTargetType = truncatedPropertiesString.substring(startGdgTargetType, endGdgTargetType);

                log.debug("gdgTargetType:" + gdgTargetType);
            }

            if(gdgTargetType==null)
            {
                return;
            }

            if(isOutbound(gdgTargetType))
            {
                if(messageIdString!=null)
                {
                    long itemId = new Long(messageIdString).longValue();

                    ItemTrackingInternalCode code = ItemTrackingInternalCode.UNDELIVERABLE;

                    CSServices.getJMSServices().send(new ItemTrackingMessageFactory("scjsegateway/jms/ScjseItemTrackingQueue", itemId, code));

                    log.info("JMS message from: " + getTransactionName() + " failed and forwarded code " + code + " to item tracking for id " + itemId + ".");
                }
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

    private boolean isOutbound(String target) {
        return (target != null && target.equalsIgnoreCase(OUTBOUND) ? true : false);
    }
}
