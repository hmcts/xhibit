package uk.gov.courtservice.xhibit.business.services.exiss.itemtrackingnotifier;

import java.util.StringTokenizer;

import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingMessageFactory;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the
 * ExissItemTrackingNotifierQueue
 * </p>
 * <p>
 * Description: Inspects the "queueNames" property of the message to determine
 * what status to send to the ExissItemTrackingQueue.
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
 * @weblogic.message-driven destination-jndi-name="xhibit/jms/ExissItemTrackingNotifierQueue"
 * @weblogic.dispatch-policy item.tracking.notifier.execute.queue
 *
 * @author Will, Fardell
 * @version $Id: ItemTrackingNotifierMessageBean.java,v 1.8 2014/06/20 16:46:38 atwells Exp $
 */
public class ItemTrackingNotifierMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(ItemTrackingNotifierMessageBean.class);

    /**
     * Method called when a JMS message is received by this MDB. If there is
     * only 1 entry (it will be the entry for the
     * ExissItemTrackingNotifierQueueConsumer), this indicates that the message
     * was only sent to this MDB and is effectively discarded so send a "Message
     * Discarded" status to the ExissItemTrackingQueue. If there is more 1
     * entry, this indicates that the message is effectively being processed by
     * some ExISS component so send a "Processing Message" status to the
     * ExissItemTrackingQueue.
     *
     * @param msg
     *            a message containing the queues to which an ExISS message has
     *            been sent
     * @ejb.interface-method view-type="local"
     */
    public void onTextMessage(final TextMessage textMessage) throws Exception {
        int queueNameCount = countQueues(textMessage.getStringProperty("XHBQueueNames"));

        ItemTrackingInternalCode code;
        if (queueNameCount < 2) {
            code = ItemTrackingInternalCode.DISCARDED;
        } else {
            code = ItemTrackingInternalCode.PROCESSING_MESSAGE;
        }

        long itemId = textMessage.getLongProperty("XHBItemId");

        CSServices.getJMSServices().send(new ItemTrackingMessageFactory("xhibit/jms/ExissItemTrackingQueue", itemId, code));

        log.info("Forwarded code " + code + " to item tracking for id " + itemId);
    }

    /**
     * Counts the number of tokens breaking on commas and spaces
     *
     * @param String
     *            containing a comma seperated list of JNDI queue names
     * @return A count of the number of entries
     */
    protected int countQueues(String fullString) {
        int x = 0;

        if (fullString != null && fullString.length() > 0) {
            StringTokenizer st = new StringTokenizer(fullString, ", ");
            x = st.countTokens();
        }

        return x;
    }
}
