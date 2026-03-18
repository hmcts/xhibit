package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.CourtLogSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictTypeEvent;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.helpers.event.EventLevelHelper;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * Abstract class to allow extraction of common code from the court log helpers.
 * Contained within this are the standard methods required when creating a court
 * log event from a save value.
 * 
 * @author tz0d5m
 * @version $Revision: 1.4 $
 */
public abstract class AbstractCourtLogHelper {
    protected final Logger log = CSServices.getLogger(getClass());

    /**
     * Log the creation/deletion/update of the passed in value object to the
     * court log. No event will be created if <i>null</i> is returned from the
     * getCourtLogEvent() method on the value object.
     * 
     * @param value
     *            An implementation of <code>CourtLogSaveValue</code>
     * @throws ResultsControllerException
     *             If there is any error creating the court log event
     */
    public void log(CourtLogSaveValue value) throws ResultsControllerException {
        try {
            final Integer courtLogEvent = value.getCourtLogEvent();

            if (courtLogEvent != null) {
                CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

                logEntry.setEventType(value.getCourtLogEvent());
                logEntry.setInCourt(value.isInCourt());
                // This hack is to show the case level criminal appeal result
                // before the offence level appeal results.
                if (value instanceof VerdictSaveValue
                        && ((VerdictSaveValue) value).getVerdictType() == VerdictTypeEvent.CASE_LEVEL_CRIMINAL_APPEAL_RESULT_TYPE) {
                    logEntry.setEntryDate(new Date(value.getCourtLogDate().getTime() - 1000));
                } else {
                    logEntry.setEntryDate(value.getCourtLogDate());
                }
                logEntry.setCaseId(value.getCourtLogCaseId());
                logEntry.setScheduledHearingId(value.getScheduledHearingId());
                addCjseCourtLogParameters(logEntry, value);
                logEntry.setPropertyMap(createPropertyMap(value));

                CourtLogWorkFlow.newEntry(logEntry);
            }
        } catch (CourtLogBusinessException clbe) {
            CSServices.getDefaultErrorHandler().handleError(clbe, DisposalCourtLogHelper.class);
            throw new ResultsControllerException(clbe.getUserMessageAsMessage().getKey(), clbe.getMessage(), clbe);
        }
    }

    protected void addCjseCourtLogParameters(CourtLogCRUDValue entry, CourtLogSaveValue value) {
        if (value.isOnOffence()) {
            EventLevelHelper.addCjseCourtLogParameters(entry, null, value.getDefendantOnOffenceId());
        } else {
            EventLevelHelper.addCjseCourtLogParameters(entry, value.getDefendantOnCaseId(), null);
        }
    }

    protected abstract Map createPropertyMap(CourtLogSaveValue value);
}
