package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine;

import java.util.HashMap;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.EventType;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine.exceptions.RulesConfigurationException;

/**
 * <p>
 * Title: Event Mappings
 * </p>
 * <p>
 * Description: This class stores arrays of ConditionalDocuments keyed by
 * EventType
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class EventMappings {
    private final HashMap hashMap;

    public EventMappings() {
        hashMap = new HashMap();
    }

    /**
     * Returns the arrays of Conditional Documents for the given event.
     * 
     * @param eventType
     * @return ConditionalDocument[]. This may be empty but never null.
     */
    public ConditionalDocument[] getConditionalDocumentsForEvent(EventType eventType) {
        if (!containsKey(eventType)) {
            throw new RulesConfigurationException("Event type not found.");
        }
        return (ConditionalDocument[]) hashMap.get(eventType);
    }

    /**
     * Add the ConditionalDocument array to the map.
     * 
     * @param eventType
     * @param conditionalDocuments
     */
    public void putConditionalDocumentsForEvent(EventType eventType, ConditionalDocument[] conditionalDocuments) {
        hashMap.put(eventType, conditionalDocuments);
    }

    public boolean containsKey(Object key) {
        return hashMap.containsKey(key);
    }

}