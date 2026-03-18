package uk.gov.courtservice.xhibit.courtlog.helpers.event;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDesc;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDescBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * Helper class for events
 * 
 * @author pznwc5
 * @version $Revision: 1.14 $
 */
public class EventHelper {
    private EventHelper() {
        // prevent external instantiation...
    }

    /**
     * Looks up the court log event description by event type
     * 
     * @param crudVal
     *            Original value
     * @return Event description
     * @throws EventTypeDescNotFoundException
     */
    public static XhbCourtLogEventDesc getXhbCourtLogEventDescByEventType(CourtLogCRUDValue crudVal) {
        return getXhbCourtLogEventDescByEventType(crudVal.getEventType());
    }

    /**
     * Looks up the court log event description by event type
     * 
     * @param eventType
     *            event type
     * @return Event description
     * 
     * @deprecated Use
     *             XhbCourtLogEventDescBeanHelper2.findByEventType(eventType)
     *             directly
     */
    public static XhbCourtLogEventDesc getXhbCourtLogEventDescByEventType(Integer eventType) {
        return XhbCourtLogEventDescBeanHelper2.findByEventType(eventType);
    }

    /**
     * Looks up the court log event description by event id
     * 
     * @param basicVal
     *            Original value
     * @return Event description
     * @throws EventTypeDescNotFoundException
     */
    public static XhbCourtLogEventDesc getXhbCourtLogEventDescByEventId(XhbCourtLogEntryBasicValue basicVal) {
        return EntityHelper.getXhbCourtLogEventDesc(basicVal.getEventDescId());
    }
}
