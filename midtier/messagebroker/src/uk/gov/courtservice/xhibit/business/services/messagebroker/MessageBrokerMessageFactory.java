package uk.gov.courtservice.xhibit.business.services.messagebroker;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import uk.gov.courtservice.framework.services.jms.TextMessageFactory;

import uk.gov.courtservice.xhibit.business.services.messagebroker.MessageBrokerMessagePropertyName;

public class MessageBrokerMessageFactory extends TextMessageFactory {

    private final long itemId;

    private final String target;

    private final String itemType;

    private final String queueNames;

    /**
     * Construct a new factory for inbound mesasages
     * 
     * @param itemId
     * @param target
     * @param itemType
     */
    public MessageBrokerMessageFactory(String jndiName, long itemId, String target, String itemType) {
        super(jndiName);
        if (target == null) {
            throw new IllegalArgumentException("target: null");
        }
        if (itemType == null) {
            throw new IllegalArgumentException("itemType: null");
        }
        this.itemId = itemId;
        this.target = target;
        this.itemType = itemType;
        queueNames = null;
    }

    /**
     * Construct a new factory for outbound messages
     * 
     * @param message
     * @param queueName
     * @param queueNames
     *            (optional)
     */
    public MessageBrokerMessageFactory(String queueName, Message message, String queueNames) throws JMSException {
        super(queueName);
        this.itemId = message.getLongProperty(MessageBrokerMessagePropertyName.ITEM_ID.toString());
        this.target = message.getStringProperty(MessageBrokerMessagePropertyName.TARGET.toString());
        this.itemType = message.getStringProperty(MessageBrokerMessagePropertyName.ITEM_TYPE.toString());
        this.queueNames = queueNames;
    }

    public void populate(TextMessage message) throws JMSException {
        message.setLongProperty(MessageBrokerMessagePropertyName.ITEM_ID.toString(), itemId);
        message.setStringProperty(MessageBrokerMessagePropertyName.TARGET.toString(), target);
        message.setStringProperty(MessageBrokerMessagePropertyName.ITEM_TYPE.toString(), itemType);
        if (queueNames != null) {
            message.setStringProperty(MessageBrokerMessagePropertyName.QUEUE_NAMES.toString(), queueNames);
        }
    }
}
