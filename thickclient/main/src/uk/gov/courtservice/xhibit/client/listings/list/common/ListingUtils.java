package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ComboHelperVO;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Class used by listings
 * @author groenm
 *
 */
public class ListingUtils {
	private static final String pdLookup = "E" + PDHConstants.CASE_TRIALTIME + "_Time_Estimate_Options";
	
	/**
	 * Returns the time estimate unit as string. 
	 * @return time estimate units
	 */
    public static String getTimeEstimateUnits(Integer trialTimeUnit) {
    	// Get the restrictions from 40711.xsd file
		Collection restrictions = RestrictionFinder.getRestrictingValues(PDHConstants.CASE_TRIALTIME.toString() + ".xsd", pdLookup);
		// Build the vector
		String result = "";
        Iterator iter = restrictions.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            ComboHelperVO cvo = new ComboHelperVO(item, XHIBITConstant.getResource(XhibitBundles.Directions, item
                    + "_DB"), XHIBITConstant.getResource(XhibitBundles.Directions, item));
            if(cvo.getDbValue().equals(trialTimeUnit.toString())){
            	result = cvo.getDisplayText();
            	break;
            }
        }
        return result;
    }
    
    /**
     * Util method to create a ListCaseTableRow from case, hearing and defendants.
     * 
     * @param caseBasicValue The case basic value
     * @param hearingTypeBasicValue The hearing type basic value
     * @param directionsForCaseBasicValue The directions for case basic value
     * @param defendantOnCaseIds The list of defendant on case ids to be listed
     * @param rowObject, a object implementing the interface ListCaseTableRow to set all defaults on.
     */
	public static void populateListCaseTableRow(
			final CaseBasicValue caseBasicValue, final RefHearingTypeBasicValue hearingTypeBasicValue,
			final DirectionsForCaseBasicValue directionsForCaseBasicValue, final List<Integer> defendantOnCaseIds,
			final Integer listNotePredefinedId, final String listNoteText, final ListCaseTableRow rowObject) {

		// set up hearing type id and code
		Integer hearingTypeId = null;
		String hearingTypeCode = "";
		// if a specific hearing type has been supplied, then use that
		if (hearingTypeBasicValue != null) {
			hearingTypeId = hearingTypeBasicValue.getId();
			hearingTypeCode = hearingTypeBasicValue.getHearingTypeCode();
		}

		// set up display data
		String caseType = caseBasicValue.getCaseType() == null ? "" : caseBasicValue.getCaseType();
		String caseNumber = caseBasicValue.getCaseNumber() == null ? ""
				: caseBasicValue.getCaseNumber().toString();
		String caseTitle = caseBasicValue.getCaseTitle() == null ? ""
				: caseBasicValue.getCaseTitle();

		final String estimateAsString = getTimeEstimateShortText(directionsForCaseBasicValue);
		
		rowObject.setCaseId(caseBasicValue.getCaseId());
		rowObject.setHearingTypeId(hearingTypeId);
		rowObject.setHearingTypeCode(hearingTypeCode);
		rowObject.setGroupNumber(caseBasicValue.getCaseGroupNumber());
		rowObject.setCaseNumber(caseType + caseNumber);
		rowObject.setCaseTitle(caseTitle);
		rowObject.setEst(estimateAsString);
		rowObject.setDefendantOnCaseIds(defendantOnCaseIds);
		rowObject.setListNotePredefinedId(listNotePredefinedId);
		rowObject.setListNoteText(listNoteText);
		
	}

	/**
	 * Creates a time estimate String from a directionsForCaseBasicValue
	 * @param directionsForCaseBasicValue
	 * @return The time estimate. 
	 */
	public static String getTimeEstimateText(final DirectionsForCaseBasicValue directionsForCaseBasicValue) {

		Float trialTimeEst = null;
		Integer trialTimeUnit = null;
		String estimateAsString = "";

		// calculate time estimate
		if (directionsForCaseBasicValue != null) {
			trialTimeEst = directionsForCaseBasicValue.getTrialTimeEstimate();
			trialTimeUnit = directionsForCaseBasicValue.getTrialTimeUnit();
		}

		// set up the Est string correctly
		if (trialTimeEst != null && trialTimeUnit != null) {
			estimateAsString = trialTimeEst.intValue() + " " + ListingUtils.getTimeEstimateUnits(trialTimeUnit);
		}
		return estimateAsString;
	}

	/**
	 * Creates a short format time estimate string from a directionsForCaseBasicValue
	 * @param directionsForCaseBasicValue
	 * @return The time estimate. 
	 */
	public static String getTimeEstimateShortText(final DirectionsForCaseBasicValue directionsForCaseBasicValue) {

		Float trialTimeEst = null;
		Integer trialTimeUnit = null;
		String estimateAsString = "";

		// calculate time estimate
		if (directionsForCaseBasicValue != null) {
			trialTimeEst = directionsForCaseBasicValue.getTrialTimeEstimate();
			trialTimeUnit = directionsForCaseBasicValue.getTrialTimeUnit();
		}

		// set up the est string in short format, i.e. 4w, 2d, 1m
		if (trialTimeEst != null && trialTimeUnit != null) {
			String trialTimeUnitAsString = ListingUtils.getTimeEstimateUnits(trialTimeUnit);
			if (trialTimeUnitAsString != null && trialTimeUnitAsString.length() > 0) {
				estimateAsString = trialTimeEst.intValue() + trialTimeUnitAsString.toLowerCase().substring(0, 1);
			}
		}
		return estimateAsString;
	}
}
