package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.results.authorise.CaseRule;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;

/**
 * <p>
 * Title: Rule for validating that a case closed has been recorded before authorisation.
 * </p>
 * <p>
 * Description: This rule will be called for all cases.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2023
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 */
public class CaseClosedRule implements CaseRule {

    private static final Logger LOG = CSServices.getLogger(CaseClosedRule.class);

    private static final Integer AUTHORISE_RESULTS_EVENT_TYPE = Integer.valueOf(40790);
    private static final Integer CASE_CLOSED_EVENT_TYPE = Integer.valueOf(30300);
    private static final String CASE_CLOSED_REQUIRED_MSGKEY = "results.authorise.authorisationFailed.caseClosed.message";
    private static final String FALSE = "false";
    private static final String TRUE = "true";
    
    private static CaseClosedRule instance = new CaseClosedRule();

    private CaseClosedRule() {
    }

    public static CaseClosedRule getInstance() {
        return instance;
    }

	public String[] process(ResultsCompositeValue rcv, Map <Integer,DefendantChargesCompositeVO> selectedDefendantChargesCompositeMap) {
    	LOG.debug("process()");
        List<String> returnString = new ArrayList<String>();

        // Get the latest event
        XhbCourtLogEntryBasicValue latestCaseClosedEvent = getLatestCaseClosedEvent(rcv.getCaseId());
        
        // Get the selected defendants
    	List<DefendantOnCaseBasicValue> selectedDefendants = getSelectedDefendants(rcv.getChargeCompositeValue().getAllDefendants(), selectedDefendantChargesCompositeMap);
    	
    	// Determine if a new case closed event is required 
        boolean isCaseClosedEventRequired = isCaseClosedEventRequired(rcv.getCaseId(), latestCaseClosedEvent, selectedDefendants);
        LOG.debug("isCaseClosedEventRequired = "+(isCaseClosedEventRequired ? TRUE : FALSE));
        
        // Add the validation error to the return array
        if (isCaseClosedEventRequired) {
        	LOG.debug("process() - "+CASE_CLOSED_REQUIRED_MSGKEY);
        	returnString.add(CASE_CLOSED_REQUIRED_MSGKEY);
        }
        return returnString.toArray(new String[returnString.size()]); 
    }
	
	/**
	 * Check if a case closed event is required for selected defendants
	 */
	private boolean isCaseClosedEventRequired(Integer caseId, XhbCourtLogEntryBasicValue latestCaseClosedEvent, List<DefendantOnCaseBasicValue> selectedDefendants) {
		if (selectedDefendants.size() > 0) {
    		// Loop through the selected defendants and determine if we need a new case closed event
        	for (DefendantOnCaseBasicValue defendantOnCaseBasicValue : selectedDefendants ) {
        		LOG.debug("defendantOnCaseBasicValue.id:"+defendantOnCaseBasicValue.getDefendantOnCaseId());
        		if (isCaseClosedEventRequiredForDefendant(caseId, defendantOnCaseBasicValue.getDefendantOnCaseId(), latestCaseClosedEvent)) {
        			return true;
        		}
        	}
    	}
		return false;
	}
	
	/**
	 * Check if a case closed event is required for a defendant
	 */
	private boolean isCaseClosedEventRequiredForDefendant(Integer caseId, Integer defendantOnCaseId, XhbCourtLogEntryBasicValue latestCaseClosedEvent) {
		LOG.debug("isCaseClosedEventRequiredForDefendant("+defendantOnCaseId+")");
		if (latestCaseClosedEvent == null) {
			LOG.debug("No previous closed event");
			return true;
		} else if (isLatestAuthoriseEventAfterLastCaseClosedEvent(caseId, defendantOnCaseId, latestCaseClosedEvent)) {
			LOG.debug("Last authorise event after last closed event");
			return true;
		}
		return false;
	}

	/**
	 * Check if a latest authorise event is after case closed event
	 */
	private boolean isLatestAuthoriseEventAfterLastCaseClosedEvent(Integer caseId, Integer defendantOnCaseId, XhbCourtLogEntryBasicValue latestCaseClosedEvent) {
		LOG.debug("isLatestAuthoriseEventAfterLastCaseClosedEvent()");
		XhbCourtLogEntryBasicValue latestAuthoriseEventForDOC = getLatestAuthoriseEventForDefendantOnCase(caseId, defendantOnCaseId);
		Date latestAuthoriseEventDate = latestAuthoriseEventForDOC != null ? latestAuthoriseEventForDOC.getCreationDate() : null;
        Date latestCaseClosedEventDate = latestCaseClosedEvent != null ? latestCaseClosedEvent.getCreationDate() : null;
        Long latestAuthoriseEventId = latestAuthoriseEventForDOC != null ? latestAuthoriseEventForDOC.getEntryId() : null;
        Long latestCaseClosedEventId = latestCaseClosedEvent != null ? latestCaseClosedEvent.getEntryId() : null;
		return isDate1AfterDate2(latestAuthoriseEventDate, latestCaseClosedEventDate) || 
				(isDateSame(latestAuthoriseEventDate, latestCaseClosedEventDate) && isEntryId1GreaterThanEntryId2(latestAuthoriseEventId, latestCaseClosedEventId));
	}
	
	/**
	 * Check if date1 is after date2 
	 */
	private boolean isDate1AfterDate2(Date date1, Date date2) {
		return (date1 != null && date1.after(date2));
	}
	
	/**
	 * Check if date1 has the same date and time as date2 
	 */
	private boolean isDateSame(Date date1, Date date2) {
		boolean isSame = (date1 != null && date2 != null && date1 == date2);
		LOG.debug("isDateSame() - "+(isSame ? TRUE : FALSE));	
		return isSame;
	}
	
	/**
	 * Check if entryId1 is greater than entryId2 
	 */
	private boolean isEntryId1GreaterThanEntryId2(Long entryId1, Long entryId2) {
		return (entryId1 != null && entryId2 != null && entryId1 > entryId2);
	}
	
	/**
	 * Get a list of the selected Defendants 
	 */
	private List<DefendantOnCaseBasicValue> getSelectedDefendants(Collection<DefendantValue> allDefendantValues, 
			Map <Integer,DefendantChargesCompositeVO> selectedDefendantChargesCompositeMap) {
		LOG.debug("getSelectedDefendants()");
		List<DefendantOnCaseBasicValue> results = new ArrayList<DefendantOnCaseBasicValue>();
		if (selectedDefendantChargesCompositeMap.size() > 0) {
			// Loop through all the defendants
			for (DefendantValue defendantValue : allDefendantValues) {
	    		DefendantOnCaseBasicValue defOnCaseBasicValue = defendantValue.getDefOnCaseBasicValue();
	    		// Filter on only selected defendants
	    		if (selectedDefendantChargesCompositeMap.containsKey(defOnCaseBasicValue.getDefendantOnCaseId())) {
	    			results.add(defOnCaseBasicValue);
	    		}
			}
		}
		return results;
	}

	/**
	 * Get the latest Authorise Result Event
	 */
	private XhbCourtLogEntryBasicValue getLatestAuthoriseEventForDefendantOnCase(Integer caseId, Integer defendantOnCaseId) {
		LOG.debug("getLatestAuthoriseEventForDefendantOnCase("+defendantOnCaseId+")");
		// Get the authorise events (latest first)
		XhbCourtLogEntryBasicValue[] courtLogEntries = 
				XhbCourtLogEntryBeanHelper2.findByDefOnCaseIdEventTypeToCreationDateDescendingValue(
						caseId, defendantOnCaseId, AUTHORISE_RESULTS_EVENT_TYPE, new Date());
		// If we have found an event, return the latest
		if (courtLogEntries != null && courtLogEntries.length > 0) {
			XhbCourtLogEntryBasicValue courtLogEntry = courtLogEntries[0];
			LOG.debug("LatestAuthoriseEvent.entryId:"+courtLogEntry.getEntryId());
			LOG.debug("LatestAuthoriseEvent.creationDate:"+courtLogEntry.getCreationDate());
			return courtLogEntry;
		}
		return null;
	}
	
	/**
	 * Get the latest Case Closed Event
	 */
	private XhbCourtLogEntryBasicValue getLatestCaseClosedEvent(Integer caseId) {
		LOG.debug("getLatestCaseClosedEvent("+caseId+")");
		// Get the case closed events (latest first)
		XhbCourtLogEntryBasicValue[] courtLogEntries = 
				XhbCourtLogEntryBeanHelper2.findByCaseIdEventTypeToCreationDateDescendingValue(
						caseId, CASE_CLOSED_EVENT_TYPE, new Date());
		// If we have found an event, return the latest
		if (courtLogEntries != null && courtLogEntries.length > 0) {
			XhbCourtLogEntryBasicValue courtLogEntry = courtLogEntries[0];
			LOG.debug("LatestCaseClosedEvent.entryId:"+courtLogEntry.getEntryId());
			LOG.debug("LatestCaseClosedEvent.creationDate:"+courtLogEntry.getCreationDate());
			return courtLogEntry;
		}
		return null;
	}
}