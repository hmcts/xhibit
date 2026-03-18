package uk.gov.courtservice.xhibit.business.services.exiss.itemtrackingnotifier;

import javax.ejb.MessageDrivenBean;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import uk.gov.courtservice.xhibit.business.services.exiss.deadletter.AbstractDeadLetterMessageBean;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.services.messagebroker.MessageBrokerMessagePropertyName;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the
 * ExissItemTrackingNotifierDeadLetterQueue
 * </p>
 * <p>
 * Description: Notifies the item tracker that an error occured in the notifier
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
 * @weblogic.message-driven destination-jndi-name="xhibit/jms/ExissItemTrackingNotifierDeadLetterQueue"
 * @weblogic.dispatch-policy exiss.deadletter.execute.queue
 * 
 * @author Will, Fardell
 * @version $Id: ItemTrackingNotifierDeadLetterMessageBean.java,v 1.1 2006/07/14
 *          19:24:00 bzjrnl Exp $
 */
public class ItemTrackingNotifierDeadLetterMessageBean extends AbstractDeadLetterMessageBean implements
        MessageDrivenBean, MessageListener {
    private static final long serialVersionUID = 1L;

    @Override
    public ItemTrackingInternalCode getInternalCode() {
        return ItemTrackingInternalCode.UNDELIVERABLE;
    }

    @Override
    public long getItemId(TextMessage message) throws JMSException {
        return message.getLongProperty(MessageBrokerMessagePropertyName.ITEM_ID.toString());
    }
}
