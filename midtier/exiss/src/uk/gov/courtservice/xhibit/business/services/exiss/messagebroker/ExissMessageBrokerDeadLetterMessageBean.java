package uk.gov.courtservice.xhibit.business.services.exiss.messagebroker;

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
 * ExissMessageBrokerDeadLetterQueue
 * </p>
 * <p>
 * Description: Notifies the item tracker that an error occured in the exiss broker
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
 * @weblogic.message-driven destination-jndi-name="xhibit/jms/ExissMessageBrokerDeadLetterQueue"
 * @weblogic.dispatch-policy exiss.deadletter.execute.queue
 * 
 * @author Will, Fardell
 * @version $Id: ExissMessageBrokerDeadLetterMessageBean.java,v 1.6 2014/06/20 16:46:58 atwells Exp $
 */
public class ExissMessageBrokerDeadLetterMessageBean extends AbstractDeadLetterMessageBean implements MessageDrivenBean, MessageListener {
    private static final long serialVersionUID = 1L;

    @Override
    public ItemTrackingInternalCode getInternalCode() {
        return ItemTrackingInternalCode.UNDELIVERABLE;
    }
    
    @Override
    public long getItemId(TextMessage message) throws JMSException {
        //NOTE: XHBMessageIdentifier and MessageBrokerMessagePropertyName.ITEM_ID.toString()
        //contain the same value - this message will contain XHBMessageIdentifier though
        return message.getLongProperty("XHBMessageIdentifier");
    }
}
