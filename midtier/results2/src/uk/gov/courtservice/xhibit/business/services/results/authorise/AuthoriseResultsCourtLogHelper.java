package uk.gov.courtservice.xhibit.business.services.results.authorise;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnCaseHelper;
import uk.gov.courtservice.xhibit.business.services.results.saver.AbstractCourtLogHelper;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.common.results.vos.CourtLogSaveValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version 1.0
 */

public class AuthoriseResultsCourtLogHelper extends AbstractCourtLogHelper {

    private static final Integer AUTH_RESULTS = new Integer(40790);

    private static final AuthoriseResultsCourtLogHelper instance = new AuthoriseResultsCourtLogHelper();

    public AuthoriseResultsCourtLogHelper() {
    }

    public static AuthoriseResultsCourtLogHelper getInstance() {
        return instance;
    }

    public void log(Integer caseID, Calendar courtLogDate, Integer scheduledHearingID, Integer defendantOnCaseID,
            String defendantName) throws CourtLogBusinessException {
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        logEntry.setEventType(AUTH_RESULTS);
        logEntry.setCaseId(caseID);
        logEntry.setScheduledHearingId(scheduledHearingID);
        logEntry.setDefendantOnCaseId(defendantOnCaseID);
        logEntry.setEntryDate(courtLogDate.getTime());

        logEntry.getPropertyMap().put("defendant_name", defendantName);

        CourtLogWorkFlow.newEntry(logEntry);

    }
    
    

    protected Map createPropertyMap(CourtLogSaveValue value) {
        final Map propertyMap = new HashMap();
        return propertyMap;

    }

}