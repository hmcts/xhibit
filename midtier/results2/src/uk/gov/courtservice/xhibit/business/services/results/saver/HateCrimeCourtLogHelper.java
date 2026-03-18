package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_hate_sentencing.XhbHateSentencing;
import uk.gov.courtservice.xhibit.business.entities.xhb_hate_sentencing.XhbHateSentencingBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.common.results.vos.CourtLogSaveValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;


public class HateCrimeCourtLogHelper extends AbstractCourtLogHelper {

    private static final HateCrimeCourtLogHelper instance = new HateCrimeCourtLogHelper();
    private static final String YES = "Y";
    private static final Integer AUTH_RESULTS_HATE_CRIME = new Integer(40792);

    private HateCrimeCourtLogHelper() {
        // empty
    }

    public static HateCrimeCourtLogHelper getInstance() {
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

        ArrayList hateCrimeReasons = getHateCrimeReasons(doc); 
        if (hateCrimeReasons.size() == 0) {
            // Nothing to log
            return;
        }

        logEntry.setEventType(AUTH_RESULTS_HATE_CRIME);
        logEntry.setCaseId(caseID);
        logEntry.setScheduledHearingId(scheduledHearingID);
        logEntry.setDefendantOnCaseId(defendantOnCaseID);
        logEntry.setEntryDate(courtLogDate.getTime());

        logEntry.getPropertyMap().put("defendant_name", defendantName);
        logEntry.getPropertyMap().put("defendant_nationality", doc.getNationality());
        // Loop round and log hate crime options
        for (int i=0; i<hateCrimeReasons.size(); i++) {
            logEntry.getPropertyMap().put("defendant_hateCrimeReason", hateCrimeReasons.get(i).toString());
        }

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


    protected Map createPropertyMap(CourtLogSaveValue value) {
        final Map propertyMap = new HashMap();
        return propertyMap;
    }
    
    /**
     * Description: Retrieve the hate crime reasons
     * @param defendantOnCase
     * @return String
     */
    public ArrayList getHateCrimeReasons(DefendantOnCase doc) {
        
        Integer defendantOnCaseId = doc.getDefendantOnCaseId();
        XhbDefendantOnCase defendantOnCase = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defendantOnCaseId);
        ArrayList<XhbHateSentencing> hateSentencing = new ArrayList<XhbHateSentencing>(XhbHateSentencingBeanHelper2.findByDefOnCaseId(defendantOnCase.getDefendantOnCaseId()));

        ArrayList hateCrimeReasons = new ArrayList();
        for (int i=0; i<hateSentencing.size(); i++) {
            // Each element will be a row in the hate sentencing database; need to convert the id to its type
            if (hateSentencing.get(i).getRefHateSentTypeId() != null) {
                if ((hateSentencing.get(i).getObsInd() == null) || !(hateSentencing.get(i).getObsInd().equals("Y"))) {
                    if (hateSentencing.get(i).getRefHateSentTypeId().intValue() == 1) {
                        hateCrimeReasons.add("general_disability");
                    } else if (hateSentencing.get(i).getRefHateSentTypeId().intValue() == 2) {
                        hateCrimeReasons.add("victim_disability");
                    } else if (hateSentencing.get(i).getRefHateSentTypeId().intValue() == 3) {
                        hateCrimeReasons.add("racially_aggravated");
                    } else if (hateSentencing.get(i).getRefHateSentTypeId().intValue() == 4) {
                        hateCrimeReasons.add("religiously_aggravated");
                    } else if (hateSentencing.get(i).getRefHateSentTypeId().intValue() == 5) {
                        hateCrimeReasons.add("racially_and_religiously_aggravated");
                    } else if (hateSentencing.get(i).getRefHateSentTypeId().intValue() == 6) {
                        hateCrimeReasons.add("general_sex");
                    } else if (hateSentencing.get(i).getRefHateSentTypeId().intValue() == 7) {
                        hateCrimeReasons.add("victim_sex");
                    } else if (hateSentencing.get(i).getRefHateSentTypeId().intValue() == 8) {
                        hateCrimeReasons.add("general_transgender");
                    } else if (hateSentencing.get(i).getRefHateSentTypeId().intValue() == 9) {
                        hateCrimeReasons.add("victim_transgender");
                    }
                }
            }
        }

        return hateCrimeReasons;
    }
}

