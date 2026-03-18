package uk.gov.courtservice.xhibit.business.services.exiss.messagebuilder;

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
 * ExissMessageBuilderDeadLetterQueue
 * </p>
 * <p>
 * Description: Notifies the item tracker that an error occured in the builder
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
 * @weblogic.message-driven destination-jndi-name="xhibit/jms/ExissMessageBuilderDeadLetterQueue"
 * @weblogic.dispatch-policy exiss.deadletter.execute.queue
 * 
 * @author Will, Fardell
 * @version $Id: MessageBuilderDeadLetterMessageBean.java,v 1.1 2006/07/14
 *          19:24:01 bzjrnl Exp $
 */
public class MessageBuilderDeadLetterMessageBean extends AbstractDeadLetterMessageBean implements MessageDrivenBean,
        MessageListener {
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
