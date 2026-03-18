package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_event_desc.XhbCourtLogEventDesc;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler;
import uk.gov.courtservice.xhibit.courtlog.helpers.event.EventHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * The helper class implements court log creation logic
 * 
 * @author pznwc5
 */
public class CreateHelper extends CrudHelper {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(CreateHelper.class);

    /** Original CRUD value */
    protected CourtLogCRUDValue crudVal;

    /** View value assembler */
    protected ViewValueAssembler viewValueAssembler;

    /**
     * Initializes the CRUD value
     * 
     * @param crudVal
     *            CRUD value
     */
    public CreateHelper(OperationContext context) {
        this.crudVal = context.getCrudValue();
        viewValueAssembler = ViewValueAssembler.newInstance(crudVal);
    }

    /**
     * Creates a new court log entry
     * 
     * @param crudValue
     *            The value sent by the client
     * @return The newly created court log entry
     * @throws CourtLogBusinessException
     */
    public CourtLogViewValue[] newEntry() throws CourtLogBusinessException {

        LOG.debug("newEntry() -  called :: courtLogCRUDValue: " + crudVal);

        // acquire the XML from the crud value
        final String logEntryXml = validateEntry(crudVal);
        return createEntry(logEntryXml);
    }

    /**
     * Creates a basic value from a CRUD value
     * 
     * @param logEntryXml
     *            Court log entry XML
     * @return Basic value
     * @throws CourtLogBusinessException
     */
    protected CourtLogViewValue[] createEntry(final String logEntryXml) throws CourtLogBusinessException {
        CourtLogViewValue[] viewValues = new CourtLogViewValue[1];
        XhbCourtLogEntryBasicValue basicVal = createBasicValue(logEntryXml);
        viewValues[0] = viewValueAssembler.assembleViewValue(basicVal);

        return viewValues;
    }

    protected XhbCourtLogEntryBasicValue createBasicValue(String logEntryXml) {
        XhbCourtLogEntryBasicValue basicVal = new XhbCourtLogEntryBasicValue();

        basicVal.setDateTime(crudVal.getEntryDate());
        basicVal.setLogEntryXml(logEntryXml);

        // now set up the CMR fields...
        basicVal.setCaseId(crudVal.getCaseId());
        basicVal.setDefendantOnCaseId(crudVal.getDefendantOnCaseId());
        basicVal.setDefendantOnOffenceId(crudVal.getDefendantOnOffenceId());
        // for creates we can guarantee that it will be using the new code...
        basicVal.setScheduledHearingId(crudVal.getScheduledHearingId());

        final XhbCourtLogEventDesc xhbCourtLogEventDesc = EventHelper.getXhbCourtLogEventDescByEventType(crudVal);
        basicVal.setEventDescId(xhbCourtLogEventDesc.getEventDescId());

        return XhbCourtLogEntryBeanHelper2.create(basicVal);
    }
}
