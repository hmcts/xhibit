package uk.gov.courtservice.xhibit.courtlog.helpers;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: SubscriptionsHelper
 * </p>
 * <p>
 * Description: Methods taken\modified from CourtLogHelper
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: SubscriptionsHelper.java,v 1.7 2006/06/05 12:28:53 bzjrnl Exp $
 */
public class SubscriptionsHelper {
    private SubscriptionsHelper() {
        // prevent external instantiation...
    }

    /**
     * Builds a CourtLogViewValue from the primary key of a court log entry
     * 
     * @param logEntryId
     *            the primary key of the court log entry
     * @return the court log view value
     * @throws CourtLogBusinessException
     *             If there's a problem finding the event type of this entry
     */
    public static CourtLogViewValue createCourtLogViewValue(Long logEntryId) {
        final XhbCourtLogEntry cle = EntityHelper.getXhbCourtLogEntry(logEntryId);

        CourtLogViewValue cLVV = new CourtLogViewValue(cle.getVersion());

        cLVV.setCaseId(cle.getCaseId());
        cLVV.setDefendantOnCaseId(cle.getDefendantOnCaseId());
        cLVV.setDefendantOnOffenceId(cle.getDefendantOnOffenceId());
        cLVV.setEntryDate(cle.getDateTime());
        cLVV.setEventType(lookupEventType(cle.getEventDescId()));
        cLVV.setLogEntry(cle.getLogEntryXml());
        cLVV.setLogEntryId(cle.getEntryId());
        cLVV.setLastUpdateDate(cle.getLastUpdateDate());

        return cLVV;
    }

    /**
     * Finds the event type from the primary key of an event description
     * 
     * @param eventDescId
     *            the primary key of the event description
     * @return The event type
     * @throws CourtLogBusinessException
     *             If there's a problem finding the event type
     */
    public static Integer lookupEventType(Integer eventDescId) {
        return EntityHelper.getXhbCourtLogEventDesc(eventDescId).getEventType();
    }
}