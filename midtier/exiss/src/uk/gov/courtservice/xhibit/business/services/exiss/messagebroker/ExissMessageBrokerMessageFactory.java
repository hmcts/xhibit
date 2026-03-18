package uk.gov.courtservice.xhibit.business.services.exiss.messagebroker;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import uk.gov.courtservice.framework.services.jms.TextMessageFactory;

public class ExissMessageBrokerMessageFactory extends TextMessageFactory {

    private final Map <String, String> propertyMap;

    private final String text;

    /**
     * Construct a new message factory 
     * @param queueName
     * @param propertyMap
     * @param text
     */
    public ExissMessageBrokerMessageFactory(String queueName, Map <String, String> propertyMap, String text) {
        super(queueName);
        if(propertyMap == null) {
            throw new IllegalArgumentException("propertyMap: null");
        }
        if(text == null) {
            throw new IllegalArgumentException("text: null");
        }
        this.propertyMap = propertyMap; 
        this.text = text;
    }

    /**
     * Populate the message with the properties and text
     */
    @Override
    public void populate(TextMessage message) throws JMSException {
        for(String key: propertyMap.keySet()) {
            message.setStringProperty(key, propertyMap.get(key));
        }
        message.setText(text);
    }
}
