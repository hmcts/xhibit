package uk.gov.courtservice.xhibit.client.listings.list.warned;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingDiaryModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * Model for warned list panel.
 * 
 * @author uphillj
 *
 */
public class WarnedListModel extends AbstractListModel {
	
	// Case on list ids for no specific date with key courtSiteId
	private Map<Integer,List<Integer>> caseOnListIdsNoDate = new HashMap<Integer,List<Integer>>();
	
	public WarnedListModel(XhibitApplicationController xac) {
		super(xac, ListTypeEnum.Warned);
	}
	
	/**
	 * Create new warned list for supplied dates
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void createList(Calendar startDate, Calendar endDate) {
		super.createList(startDate, endDate);
	}

	@Override
	public CaseOnListComplexValue addCase(CaseOnListBasicValue defaults, CaseBasicValue caze, CaseListingEntryBasicValue caseListingEntry, DirectionsForCaseBasicValue directionsForCase,
											RefHearingTypeBasicValue hearingType, CaseOnListType parentType, Integer sequenceNumber, Object... parents) {
		// Call super class method to create a new populated case on list
		CaseOnListComplexValue caseOnList = super.addCase(defaults, caze, caseListingEntry, directionsForCase, hearingType, parentType, sequenceNumber, parents);
		
		// Get the supplied parent parameters
		XhbCourtSiteBasicValue courtSite = (XhbCourtSiteBasicValue)parents[0];

		// Update case on list with no date specific values
		caseOnList.setTimeListedDate(getListStartDate().getTime());
		caseOnList.setCourtSiteId(courtSite.getCourtSiteId());
		
		// Add case to the relevant map which tracks cases by date
		addCaseToNoDate(caseOnList);
		
		// Re-sequence relevant cases on list under date or no date
		renumberAddedCaseToNoDate(caseOnList);
		
		return caseOnList;
	}

	@Override
	public void moveCase(CaseOnListComplexValue caseOnList, CaseOnListType parentType, Integer sequenceNumber, Object... parents) {
		// Remove case from the relevant map which tracks cases by date
		removeCaseFromNoDate(caseOnList);
		
		// Re-sequence relevant cases affected by removing case
		renumberRemovedCaseFromNoDate(caseOnList);

		// Must check for the edge case when moving a case below its
		// current position under the same parent node. This means seq
		// number must be one less as the new number must exclude itself
		// Get the supplied parent parameters
		XhbCourtSiteBasicValue courtSite = (XhbCourtSiteBasicValue)parents[0];

		// If case just moving down the same no date node 
		if (courtSite.getCourtSiteId().equals(caseOnList.getCourtSiteId())
			&& !caseOnList.hasTimeListedDate()
			&& sequenceNumber > caseOnList.getSeqNo()) {
			sequenceNumber--;
		}

		// Call super class method to move the case
		super.moveCase(caseOnList, parentType, sequenceNumber, parents);
		
		// Update case on list with no date specific values
		caseOnList.setTimeListedDate(getListStartDate().getTime());
		caseOnList.setCourtSiteId(courtSite.getCourtSiteId());
		
		// Add case to the relevant map which tracks cases by date
		addCaseToNoDate(caseOnList);
		
		// Re-sequence relevant cases on list under date or no date
		renumberAddedCaseToNoDate(caseOnList);
	}

	@Override
	public void removeCase(CaseOnListComplexValue caseOnList) {
		// Call super class method to remove the case
		super.removeCase(caseOnList);
		
		// Remove case from the relevant map which tracks cases by date
		removeCaseFromNoDate(caseOnList);
		
		// Re-sequence relevant cases affected by removing case
		renumberRemovedCaseFromNoDate(caseOnList);
	}

	@Override
	public boolean isDefendantNotListable(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId) {
		// Returns true if the defendant is already listed in a different case on list in the model
		return super.isDefendantOnCaseOnListComplexValues(caseOnList.getCaseId(), defendantOnCaseId, caseOnList.getCaseOnListId());
	}

	/**
	 * Return list of cases for court site sorted by sequence number.
	 * 
	 * @param courtSiteId
	 * @return
	 */
	public List<CaseOnListComplexValue> getCasesOnListForNoDate(Integer courtSiteId) {
		List<CaseOnListComplexValue> casesOnList = new ArrayList<CaseOnListComplexValue>();
		
		// If cases exist for the sitting, add each case to the list
		if (caseOnListIdsNoDate.containsKey(courtSiteId)) {
			for (Integer caseOnListId : caseOnListIdsNoDate.get(courtSiteId)) {
				casesOnList.add(getCaseOnList(caseOnListId));
			}
		}
		
		// Sort cases on list by their sequence number
		sortCasesOnList(casesOnList);
		
		return casesOnList;
	}
	
	@Override
	protected void populateListModel(Collection<SittingOnListComplexValue> sittingsOnList, Collection<CaseOnListComplexValue> casesOnList) {
		// Call super class method to populate its variables
		super.populateListModel(sittingsOnList, casesOnList);
		
		// Add all the cases on list to either date or no date map
		for (CaseOnListComplexValue caseOnList : casesOnList) {
			addCaseToNoDate(caseOnList);
		}
	}

	@Override
	protected void clearListModel() {
		// Call super class method to clear its variables
		super.clearListModel();
		
		// Clear variables with cases on list
		caseOnListIdsNoDate.clear();
	}
	
	/**
	 * Add the case on list to map which tracks cases per no date.
	 * 
	 * @param caseOnList
	 */
	private void addCaseToNoDate(CaseOnListComplexValue caseOnList) {
		// If cases do not yet exist for the no date, create list for court site
		if (!caseOnListIdsNoDate.containsKey(caseOnList.getCourtSiteId())) {
			caseOnListIdsNoDate.put(caseOnList.getCourtSiteId(), new ArrayList<Integer>());
		}
		
		// Add the case on list id to the list for court site no date
		caseOnListIdsNoDate.get(caseOnList.getCourtSiteId()).add(caseOnList.getCaseOnListId());
	}

	/**
	 * Remove the case on list from map which tracks cases per no date.
	 * 
	 * @param caseOnList
	 */
	private void removeCaseFromNoDate(CaseOnListComplexValue caseOnList) {
		// Remove the case on list id from the list for court site no date
		caseOnListIdsNoDate.get(caseOnList.getCourtSiteId()).remove(caseOnList.getCaseOnListId());
	}

	/**
	 * Adjust the sequence numbers for all the cases on list
	 * relative to the supplied case on list being added.
	 * 
	 * @param caseOnList
	 */
	private void renumberAddedCaseToNoDate(CaseOnListComplexValue caseOnList) {
		// Renumber the cases on list under no date after the new case on list
		renumberCasesOnList(getCasesOnListForNoDate(caseOnList.getCourtSiteId()), caseOnList.getCaseOnListId());
	}

	/**
	 * Adjust the sequence numbers for the remaining cases on list for
	 * any gaps in the sequence numbers after deleting supplied case.
	 * 
	 * @param caseOnList
	 */
	private void renumberRemovedCaseFromNoDate(CaseOnListComplexValue caseOnList) {
		// Renumber the cases on list under no date of deleted case on list
		renumberCasesOnList(getCasesOnListForNoDate(caseOnList.getCourtSiteId()));
	}
	
	public ListingDiaryModel getListingDiaryModel() {
		return new WarnedListingDiaryModel(
			getListStartDate(),
			//The FS says to use the start Date for both.
			getListStartDate(),
			this);
	}
}