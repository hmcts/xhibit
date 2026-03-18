package uk.gov.courtservice.xhibit.courtlog.subscriptions;

/**
 * @author pznwc5
 * @version $Revision: 1.5 $
 */
public class SubscriberType {
    public static final SubscriberType GLOBAL = new SubscriberType("global");

    public static final SubscriberType CATEGORY = new SubscriberType("category");

    public static final SubscriberType EVENT = new SubscriberType("event");

    private String type;

    private SubscriberType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}
