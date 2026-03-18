package uk.gov.courtservice.framework.services.audit;

/**
 * <p>
 * Title: EventType
 * </p>
 * <p>
 * Description: Hold a description of an event type
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class EventType {
    String type;

    /**
     * Construct an EventType
     * 
     * @param typeDescription
     *            description of the event type
     */
    public EventType(String typeDescription) {
        type = typeDescription;
    }

    /**
     * Get the description
     * 
     * @return description of the event
     */
    public final String toString() {
        return type;
    }
}