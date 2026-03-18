package uk.gov.courtservice.xhibit.business.services.messagebroker;

public enum MessageBrokerMessagePropertyName {
    ITEM_ID("XHBItemId"),
    TARGET("XHBTarget"),
    ITEM_TYPE("XHBItemType"),
    QUEUE_NAMES("XHBQueueNames");

    private final String name;
    
    private MessageBrokerMessagePropertyName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return name;
    }    
}
