package uk.gov.courtservice.xhibit.services.scjsegateway.common;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import uk.gov.courtservice.framework.services.jms.TextMessageFactory;

public class ScjseGatewayMessageFactory extends TextMessageFactory {

    private final Map <String, String> propertyMap;

    private final String messageBody;

    /**
     * Construct a new message factory 
     * @param queueName
     * @param propertyMap
     * @param text
     */
    public ScjseGatewayMessageFactory(String queueName, Map <String, String> propertyMap, String messageBody) {
        super(queueName);
        if(propertyMap == null) {
            throw new IllegalArgumentException("propertyMap: null");
        }
        if(messageBody == null) {
            throw new IllegalArgumentException("messageBody: null");
        }
        this.propertyMap = propertyMap; 
        this.messageBody = messageBody;
    }

    /**
     * Construct a new message factory 
     * @param queueName
     * @param propertyMap
     */
    public ScjseGatewayMessageFactory(String queueName, Map <String, String> propertyMap) {
        super(queueName);
        if(propertyMap == null) {
            throw new IllegalArgumentException("propertyMap: null");
        }
        this.propertyMap = propertyMap; 
        this.messageBody = null;
    }

    /**
     * Populate the message with the properties and text
     */
    @Override
    public void populate(TextMessage message) throws JMSException {
        for(String key: propertyMap.keySet()) {
            message.setStringProperty(key, propertyMap.get(key));
        }
        if( messageBody != null ) {
            message.setText(messageBody);
        }
    }
}
