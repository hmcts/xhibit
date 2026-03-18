package uk.gov.courtservice.xhibit.courtlog.helpers;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.courtlog.helpers.event.EventHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogViewValue;

/**
 * @author pznwc5
 * @version $Id: ViewValueAssembler.java,v 1.14 2006/06/05 12:28:53 bzjrnl Exp $
 */
public class ViewValueAssembler {
    private static final Logger log = Logger.getLogger(ViewValueAssembler.class);

    /**
     * Creates a new instance of the view value assembler based on the CRUD
     * value
     * 
     * @param crudVal
     * @return
     */
    public static ViewValueAssembler newInstance(CourtLogCRUDValue crudVal) {
        if (crudVal instanceof MultiCaseCourtLogCRUDValue) {
            log.debug("Creating multi case view value assembler");
            return new MultiCaseViewValueAssembler((MultiCaseCourtLogCRUDValue) crudVal);
        }

        log.debug("Creating basic case view value assembler");
        return new ViewValueAssembler();
    }

    /**
     * Create a new <code>CourtLogViewValue</code> from the passed in
     * <code>XhbCourtLogEntryBasicValue</code>.
     * 
     * @param cle
     *            The <code>XhbCourtLogEntryBasicValue</code> to create the
     *            value from
     * @return The newly created <code>CourtLogViewValue</code>
     */
    public static CourtLogViewValue createCourtLogViewValue(XhbCourtLogEntryBasicValue cle) {
        CourtLogViewValue cLVV = new CourtLogViewValue(cle.getVersion());
        doPopulate(cLVV, cle);
        return cLVV;
    }

    /**
     * Create a new <code>CourtLogViewValue</code> for each of the passed in
     * <code>XhbCourtLogEntryBasicValue</code>s, and return in a
     * <code>Collection</code>.
     * 
     * @param courtLogEntries
     *            The <code>XhbCourtLogEntryBasicValue[]</code> to create the
     *            values from
     * @return The newly created <code>CourtLogViewValue</code>s
     */
    public static CourtLogViewValue[] createCourtLogViewValues(XhbCourtLogEntryBasicValue[] courtLogEntries) {
        final CourtLogViewValue[] returnArray = new CourtLogViewValue[courtLogEntries.length];

        for (int i = 0; i < courtLogEntries.length; i++) {
            returnArray[i] = createCourtLogViewValue(courtLogEntries[i]);
        }

        return returnArray;
    }

    /**
     * Populates the passed in view value with the values from the basic value
     * 
     * @param viewVal
     *            The value to be populated
     * @param basicVal
     *            Newly created court log entry
     */
    public static void doPopulate(CourtLogViewValue viewVal, XhbCourtLogEntryBasicValue basicVal) {
        viewVal.setCaseId(basicVal.getCaseId());
        viewVal.setDefendantOnCaseId(basicVal.getDefendantOnCaseId());
        viewVal.setDefendantOnOffenceId(basicVal.getDefendantOnOffenceId());
        viewVal.setScheduledHearingId(basicVal.getScheduledHearingId());
        viewVal.setEntryDate(basicVal.getDateTime());
        viewVal.setEventType(EventHelper.getXhbCourtLogEventDescByEventId(basicVal).getEventType());
        viewVal.setLogEntry(basicVal.getLogEntryXml());
        viewVal.setLogEntryId(basicVal.getEntryId());
        viewVal.setLastUpdateDate(basicVal.getLastUpdateDate());
    }

    /**
     * Creates the view value from basic value
     * 
     * @param basicVal
     *            Newly created court log entry
     * @return View value for the new entry
     */
    public CourtLogViewValue assembleViewValue(XhbCourtLogEntryBasicValue basicVal) {
        // simply delegate to the static create method...
        return createCourtLogViewValue(basicVal);
    }

    /**
     * @author pznwc5 Helper for assembling multicase view values
     */
    public static class MultiCaseViewValueAssembler extends ViewValueAssembler {
        /** Original crud value */
        private MultiCaseCourtLogCRUDValue crudVal;

        /** Initializes the crud value */
        protected MultiCaseViewValueAssembler(MultiCaseCourtLogCRUDValue crudVal) {
            this.crudVal = crudVal;
        }

        /**
         * Creates the view value from basic value
         * 
         * @param basicVal
         *            Newly created court log entry
         * @return View value for the new entry
         */
        public CourtLogViewValue assembleViewValue(XhbCourtLogEntryBasicValue basicVal) {
            MultiCaseCourtLogViewValue viewVal = new MultiCaseCourtLogViewValue(basicVal.getVersion());
            viewVal.setCaseIds(crudVal.getCaseIds());
            doPopulate(viewVal, basicVal);

            return viewVal;
        }
    }
}
