package uk.gov.courtservice.xhibit.courtlog.helpers;

import java.util.Map;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.courtlog.helpers.xml.CourtLogXmlHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * Assembler that can be used to construct <code>CourtLogCRUDValue</code>'s.
 * 
 * @author tz0d5m
 * @version $Revision: 1.4 $
 */
public class CrudValueAssembler {
    private CrudValueAssembler() {
        // private constructor to prevent external instantiation
    }

    /**
     * Create a new <code>CourtLogCRUDValue</code> from the passed in
     * <code>XhbCourtLogEntryBasicValue</code>.
     * 
     * @param cle
     *            The <code>XhbCourtLogEntryBasicValue</code> to create the
     *            value from
     * @return The newly created <code>CourtLogCRUDValue</code>
     */
    public static CourtLogCRUDValue createCourtLogCRUDValue(XhbCourtLogEntryBasicValue cle) {
        final CourtLogCRUDValue cLCV = new CourtLogCRUDValue(cle.getVersion());
        doPopulate(cle, cLCV);
        return cLCV;
    }

    /**
     * Performs the actual population
     * 
     * @param cle
     *            The <code>XhbCourtLogEntryBasicValue</code> to copy the
     *            values from
     * @param cLCV
     *            The <code>CourtLogCRUDValue</code> to copy the values to
     */
    private static void doPopulate(XhbCourtLogEntryBasicValue cle, CourtLogCRUDValue cLCV) {
        cLCV.setCaseId(cle.getCaseId());
        cLCV.setDefendantOnCaseId(cle.getDefendantOnCaseId());
        cLCV.setDefendantOnOffenceId(cle.getDefendantOnOffenceId());
        cLCV.setScheduledHearingId(cle.getScheduledHearingId());

        cLCV.setEntryDate(cle.getDateTime());
        cLCV.setEventType(EntityHelper.getXhbCourtLogEventDesc(cle.getEventDescId()).getEventType());
        cLCV.setLogEntryId(cle.getEntryId());
        cLCV.setLastUpdateDate(cle.getLastUpdateDate());

        final Map properties = CourtLogXmlHelper.getPropertySet(cle.getLogEntryXml());
        cLCV.setPropertyMap(properties);
        cLCV.setEntryFreeText((String) properties.get(CourtLogCRUDValue.ENTRY_FREE_TEXT));
    }
}
