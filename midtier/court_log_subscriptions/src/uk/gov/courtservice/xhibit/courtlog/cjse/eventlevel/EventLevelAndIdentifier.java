package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

/**
 * <p>
 * Title: EventLevelAndIdentifier
 * </p>
 * <p>
 * Description: Holds the level and identifier for an event.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: EventLevelAndIdentifier.java,v 1.1 2004/04/20 14:40:55 pznwc5
 *          Exp $
 */
public class EventLevelAndIdentifier {
    private Integer _eventLevel;

    private String _eventIdentifier;

    public EventLevelAndIdentifier(Integer eventLevel, String eventIdentifier) {
        _eventLevel = eventLevel;
        _eventIdentifier = eventIdentifier;
    }

    /**
     * Returns the event identifier, the casenumber for a case level event, ASN
     * for a defendant level event, CRN for a crn level event. May be null for
     * defendant or crn events if no ASN\CRN is available.
     * 
     * @return
     */
    public String getEventIdentifier() {
        return _eventIdentifier;
    }

    /**
     * Returns the event level as defined in
     * \config\components\cjse.event.level.properties
     * 
     * @return the event level
     */
    public Integer getEventLevel() {
        return _eventLevel;
    }
}