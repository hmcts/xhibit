package uk.gov.hmcts.pdda.common.publicdisplay.events;

import java.io.Serializable;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.EventType;

/**
 * <p>
 * Title: Public Display Event
 * </p>
 * 
 * <p>
 * Description: The super class for all events to be sent to the public display.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PublicDisplayEvent.java,v 1.4 2006/06/05 12:28:23 bzjrnl Exp $
 */
public interface PublicDisplayEvent extends Serializable {
    /**
     * Returns an object that uniquely identifies the event
     * 
     * @return EventType
     */
    public EventType getEventType();

    /**
     * Get the court ID for this event.
     * 
     * @return the court Id for this event.
     */
    public Integer getCourtId();

}
