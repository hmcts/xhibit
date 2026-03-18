package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler;
import uk.gov.courtservice.xhibit.courtlog.helpers.event.EventHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * @author pznwc5
 */
public class UpdateHelper extends CrudHelper {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(UpdateHelper.class);

    private UpdateHelper() {
        // made private to prevent external instantiation...
    }
    
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
    public UpdateHelper(OperationContext context) {
        this.crudVal = context.getCrudValue();
        viewValueAssembler = ViewValueAssembler.newInstance(crudVal);
    }

    /**
     * Updates the court log entry
     * 
     * @param OperationContext -
     *            the context for this update request
     * @return CourtLogViewValue - an updated version of the CourtLogCRUDValue
     *         suitable for viewing
     */
    public CourtLogViewValue[] updateEntry(OperationContext context) throws CourtLogBusinessException {
        CourtLogCRUDValue crudValue = context.getCrudValue();

        return updateEntry(crudValue);
    }

    /**
     * Updates the court log entry
     * 
     * @param CourtLogCRUDValue -
     *            the court log CRUD value to be updated
     * @return CourtLogViewValue - an updated version of the CourtLogCRUDValue
     *         suitable for viewing
     */
    public CourtLogViewValue[] updateEntry(final CourtLogCRUDValue crudVal) throws CourtLogBusinessException{
        final String methodName = "updateEntry() - ";
        LOG.debug(methodName + "called :: crudVal: " + crudVal);

        ViewValueAssembler viewValueAssembler = ViewValueAssembler.newInstance(crudVal);

        // Validate entry details
        String logEntryXml = validateEntry(crudVal);
        LOG.debug(methodName + logEntryXml);

        // get the value from the database, so that we do not need to set the
        // fields we do not care about...
        XhbCourtLogEntryBasicValue basicVal = XhbCourtLogEntryBeanHelper2
                .findByPrimaryKeyValue(crudVal.getLogEntryId());

        basicVal.setVersion(crudVal.getVersion());
        basicVal.setDateTime(new Timestamp(crudVal.getEntryDate().getTime()));
        basicVal.setLogEntryXml(logEntryXml);

        // Update CMR fields...
        basicVal.setCaseId(crudVal.getCaseId());
        basicVal.setDefendantOnCaseId(crudVal.getDefendantOnCaseId());
        basicVal.setDefendantOnOffenceId(crudVal.getDefendantOnOffenceId());
        basicVal.setScheduledHearingId(crudVal.getScheduledHearingId());
        basicVal.setEventDescId(EventHelper.getXhbCourtLogEventDescByEventType(crudVal).getEventDescId());

        XhbCourtLogEntry courtLogEntry = XhbCourtLogEntryBeanHelper2.updateLocal(basicVal);
        CourtLogViewValue[] returnVal={viewValueAssembler.assembleViewValue(courtLogEntry.getData())};
        return returnVal;
    }
}
