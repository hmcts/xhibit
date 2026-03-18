package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.common.results.vos.CourtLogSaveValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;


public class DeportationCourtLogHelper extends AbstractCourtLogHelper {

    private static final DeportationCourtLogHelper instance = new DeportationCourtLogHelper();
    private static final String YES = "Y";
    private static final Integer AUTH_RESULTS_DEPORTATION = new Integer(40791);

    private DeportationCourtLogHelper() {
        // empty
    }

    public static DeportationCourtLogHelper getInstance() {
        return instance;
    }


    /**
     * Description: Method creates a new deportation log event  
     * @param caseID
     * @param courtLogDate
     * @param scheduledHearingID
     * @param defendantOnCaseID
     * @param defendantName
     * @throws CourtLogBusinessException
     */
    public void log(Integer caseID, Calendar courtLogDate, Integer scheduledHearingID, Integer defendantOnCaseID,
            String defendantName) throws CourtLogBusinessException {
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();
        DefendantOnCase doc = getDefendantOnCaseValue(defendantOnCaseID);

        if (getDeportationReason(doc).equals("")) {
            // Not a deportation event.
            return;
        }

        logEntry.setEventType(AUTH_RESULTS_DEPORTATION);
        logEntry.setCaseId(caseID);
        logEntry.setScheduledHearingId(scheduledHearingID);
        logEntry.setDefendantOnCaseId(defendantOnCaseID);
        logEntry.setEntryDate(courtLogDate.getTime());

        logEntry.getPropertyMap().put("defendant_name", defendantName);
        logEntry.getPropertyMap().put("defendant_nationality", doc.getNationality());
        logEntry.getPropertyMap().put("defendant_deportationReason", getDeportationReason(doc));

        CourtLogWorkFlow.newEntry(logEntry);
    }
    
    /**
     * Description: Retreive the defendant on case details using the defendant on case id
     * @param docId
     * @return defendantOnCase
     */
    public DefendantOnCase getDefendantOnCaseValue(Integer docId){
        DefendantHelper helper = new DefendantHelper();
        return  helper.getDefendantOnCaseDetails(docId);       
    }

    /**
     * Description: Retrieve deportationReason
     * @param doc
     * @return String: reason
     */
    public String getDeportationReason(DefendantOnCase doc){
 
        if(doc.getCustodial() != null){
            if(doc.getCustodial().toUpperCase().equals(YES)){
                return "custodial";
            }
        }else if(doc.getSuspended() != null){
            if(doc.getSuspended().toUpperCase().equals(YES)){
                return "suspended";
            }
        }else if(doc.getSeriousDrugOffence() != null){
            if(doc.getSeriousDrugOffence().toUpperCase().equals(YES)){
                return "seriousDrugOffence";
            }
        }else if(doc.getRecommendedDeportation() != null){
            if(doc.getRecommendedDeportation().toUpperCase().equals(YES)){
                return "recommendedDeportation";
            }
        }

        return "";
    }

    protected Map createPropertyMap(CourtLogSaveValue value) {
        final Map propertyMap = new HashMap();
        return propertyMap;
    }
}

