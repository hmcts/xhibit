package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogCRUDValue;

/**
 * @author pznwc5
 * 
 * Creates basic values for multiple cases
 */
public class MultiCaseCreateHelper extends CreateHelper {
    /**
     * Initializes the crud value
     * 
     * @param crudValue
     */
    public MultiCaseCreateHelper(OperationContext context) {
        super(context);
    }

    /**
     * Creates multiple basic values from a CRUD value.
     * 
     * @todo Current logic returns the last created basic value. Not sure
     *       whether it is right.
     * @param logEntryXml
     *            Court log entry XML
     * @return Basic value
     * @throws CourtLogBusinessException
     */
    protected CourtLogViewValue[] createEntry(final String logEntryXml) throws CourtLogBusinessException {

        XhbCourtLogEntryBasicValue basicVal = null;
        Integer caseIds[] = ((MultiCaseCourtLogCRUDValue) crudVal).getCaseIds();

        for (int i = 0; i < caseIds.length; i++) {
            crudVal.setCaseId(caseIds[i]);
            basicVal = super.createBasicValue(logEntryXml);
        }

        CourtLogViewValue[] viewValues = new CourtLogViewValue[1];
        viewValues[0] = viewValueAssembler.assembleViewValue(basicVal);

        return viewValues;
    }
}
