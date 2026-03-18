package uk.gov.courtservice.xhibit.business.services.exiss.itemtracking;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import uk.gov.courtservice.framework.services.jms.TextMessageFactory;

public class ItemTrackingMessageFactory extends TextMessageFactory {

    private final long id;

    private final ItemTrackingInternalCode code;

    public ItemTrackingMessageFactory(String queueName, long id, ItemTrackingInternalCode code) {
        super(queueName);
        if (code == null) {
            throw new IllegalArgumentException("code: null");
        }
        this.id = id;
        this.code = code;
    }

    public void populate(TextMessage newTextMessage) throws JMSException {
        newTextMessage.setLongProperty(ItemTrackingMessagePropertyName.ITEM_ID.toString(), id);
        newTextMessage.setStringProperty(ItemTrackingMessagePropertyName.INTERNAL_CODE.toString(), code.getInternalCode());
    }
}
