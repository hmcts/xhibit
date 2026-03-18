package uk.gov.courtservice.xhibit.courtlog.helpers;

import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanNotFoundException;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogAbstractValue;

/**
 * Utility class to lookup CMR fields
 * 
 * @author pznwc5
 * @version $Revision: 1.16 $
 */
public class LookupHelper {
    private LookupHelper() {
        // prevent external instantiation...
    }

    /**
     * Looks up the scheduled hearing
     * 
     * @param courtLogVal
     *            Original value
     * @return Scheduled hearing local reference
     */
    public static XhbScheduledHearing getXhbScheduledHearing(CourtLogAbstractValue courtLogVal) {
        if (courtLogVal.getScheduledHearingId() != null) {
            // if a not found exception is thrown, do not handle here...
            return EntityHelper.getXhbScheduledHearing(courtLogVal.getScheduledHearingId());
        }

        try {
            return EntityHelper.getXhbScheduledHearing(courtLogVal.getCaseId(), courtLogVal.getEntryDate());
        } catch (XhbScheduledHearingBeanNotFoundException e) {
            // this is possible, but simply ignore and return null...

            // @todo - remove stack trace once development done...
            e.printStackTrace();
        }

        // we could not find a scheduled hearing, so return null...
        return null;
    }
}
