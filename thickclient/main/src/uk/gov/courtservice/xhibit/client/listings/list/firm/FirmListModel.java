package uk.gov.courtservice.xhibit.client.listings.list.firm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingDiaryModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * Model for firm list panel.
 * 
 * @author uphillj
 *
 */
public class FirmListModel extends AbstractDailyFirmListModel {

	private static final String CASE_TYPE_APPEAL = "A";
	
	// Case on list ids for reserve
	private List<Integer> caseOnListIdsReserve = new ArrayList<Integer>();

	public FirmListModel(XhibitApplicationController xac) {
		super(xac, ListTypeEnum.Firm);
	}
	
	/**
	 * Create new firm list for supplied dates
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
		
		// Populate additional fields for cases added to reserve
		if (CaseOnListType.Reserve.equals(parentType)) {
			caseOnList.setTimeListedDate(getListStartDate().getTime());
			caseOnList.setReserved(YES);
		}
		
		// All appeal cases added to a firm list which were not dragged from
		// a fixture, need to be marked as such for reporting purposes
		if (CASE_TYPE_APPEAL.equalsIgnoreCase(caseOnList.getCase().getCaseType()) &&
			caseOnList.getCaseDiaryFixtureId() == null) {
			caseOnList.setNhaFirmList(YES);
		}
		
		// Add relevant cases on list to reserve list
		addCaseToReserve(caseOnList);
		
		// Re-sequence relevant cases on list under reserve
		renumberAddedCaseToReserve(caseOnList);
		
		return caseOnList;
	}

	@Override
	public void moveCase(CaseOnListComplexValue caseOnList, CaseOnListType parentType, Integer sequenceNumber, Object... parents) {
		// Remove relevant cases on list from reserve list
		removeCaseFromReserve(caseOnList);
		
		// Re-sequence relevant cases affected by removing case
		renumberRemovedCaseFromReserve(caseOnList);
		
		// Must check for the edge case when moving a case below its
		// current position under the same parent node. This means seq
		// number must be one less as the new number must exclude itself
		if (CaseOnListType.Reserve.equals(parentType)) {
			// If case just moving down the same reserve cases node 
			if (YES.equals(caseOnList.getReserved())
				&& sequenceNumber > caseOnList.getSeqNo()) {
				sequenceNumber--;
			}
		}

		// Call super class method to move the case
		super.moveCase(caseOnList, parentType, sequenceNumber, parents);
		
		// Update case for the new parent reserve
		if (CaseOnListType.Reserve.equals(parentType)) {
			caseOnList.setTimeListedDate(getListStartDate().getTime());
			caseOnList.setReserved(YES);
		}
		
		// Add relevant cases on list to reserve list
		addCaseToReserve(caseOnList);
		
		// Re-sequence relevant cases on list under reserve
		renumberAddedCaseToReserve(caseOnList);
	}

	@Override
	public void removeCase(CaseOnListComplexValue caseOnList) {
		// Call super class method to remove the case
		super.removeCase(caseOnList);
		
		// Remove relevant cases on list from reserve list
		removeCaseFromReserve(caseOnList);
		
		// Re-sequence relevant cases affected by removing case
		renumberRemovedCaseFromReserve(caseOnList);
	}

	@Override
	public boolean isCaseListed(Integer caseId, Date dateListed) {
		// If case is listed in reserve, it is regarded as already listed on all list days
		if (isCaseListedInReserve(caseId)) {
			return true;
		} else {
			return super.isCaseListed(caseId, dateListed);
		}
	}

	@Override
	public boolean isDefendantListed(Date dateListed, Integer caseId, Integer defendantOnCaseId) {
		// If defendant is listed in reserve, it is regarded as already listed on all list days
		if (isDefendantListedInReserve(caseId, defendantOnCaseId)) {
			return true;
		} else {
			return super.isDefendantListed(dateListed, caseId, defendantOnCaseId);
		}
	}

	@Override
	public boolean isDefendantNotListable(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId) {
		// Cases in the reserve folder have no date so check all cases on list for the defendant
		if (YES.equals(caseOnList.getReserved())) {
			return super.isDefendantOnCaseOnListComplexValues(caseOnList.getCaseId(), defendantOnCaseId, caseOnList.getCaseOnListId());
		}
		// Else if defendant is listed in reserve, it is regarded as already listed on all list days
		else if (isDefendantListedInReserve(caseOnList.getCaseId(), defendantOnCaseId)) {
			return true;
		}
		// Else check cases under sittings and floater folders for the defendant for the date
		else {
			return super.isDefendantOnCaseOnListComplexValues(caseOnList.getTimeListedDate(), caseOnList.getCaseId(), defendantOnCaseId, caseOnList.getCaseOnListId());
		}
	}
	
	/**
	 * Return list of reserve cases sorted by sequence number.
	 * 
	 * @return
	 */
	public List<CaseOnListComplexValue> getCasesOnListForReserve() {
		List<CaseOnListComplexValue> casesOnList = new ArrayList<CaseOnListComplexValue>();
		
		// Add all reserve cases to list
		for (Integer caseOnListId : caseOnListIdsReserve) {
			casesOnList.add(getCaseOnList(caseOnListId));
		}
		
		// Sort cases on list by their sequence number
		sortCasesOnList(casesOnList);
		
		return casesOnList;
	}
	
	@Override
	protected void populateListModel(Collection<SittingOnListComplexValue> sittingsOnList, Collection<CaseOnListComplexValue> casesOnList) {
		// Call super class method to populate its variables
		super.populateListModel(sittingsOnList, casesOnList);
		
		// Add all the relevant cases on list to reserve list
		for (CaseOnListComplexValue caseOnList : casesOnList) {
			addCaseToReserve(caseOnList);
		}
	}

	@Override
	protected void clearListModel() {
		// Call super class method to clear its variables
		super.clearListModel();
		
		// Clear variables with sittings and cases on list
		caseOnListIdsReserve.clear();
	}

	/**
	 * Returns true if the case is listed in the reserve folder.
	 * 
	 * @param caseId
	 * @return
	 */
	private boolean isCaseListedInReserve(Integer caseId) {
		boolean found = false;
		
		// If any of the case on list values are in reserve, then listed
		Collection<Integer> caseOnListIds = getCaseOnListIds(caseId);
		for (Integer caseOnListId : caseOnListIds) {
			if (caseOnListIdsReserve.contains(caseOnListId)) {
				found = true;
				break;
			}
		}
		
		return found;
	}

	/**
	 * Returns true if the defendant is listed in the reserve folder.
	 * 
	 * @param caseId
	 * @param defendantOnCaseId
	 * @return
	 */
	private boolean isDefendantListedInReserve(Integer caseId, Integer defendantOnCaseId) {
		boolean found = false;
		
		// If the defendant is on any of the case on list values in reserve, then listed
		Collection<Integer> caseOnListIds = getCaseOnListIds(caseId);
		for (Integer caseOnListId : caseOnListIds) {
			if (caseOnListIdsReserve.contains(caseOnListId) &&
				isDefendantOnCaseOnListComplexValue(caseOnListId, defendantOnCaseId)) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	/**
	 * Add the case on list to the list of reserve cases.
	 * 
	 * @param caseOnList
	 */
	private void addCaseToReserve(CaseOnListComplexValue caseOnList) {
		if (YES.equals(caseOnList.getReserved())) {
			caseOnListIdsReserve.add(caseOnList.getCaseOnListId());
		}
	}
	
	/**
	 * Remove the case on list from the list of reserve cases.
	 * 
	 * @param caseOnList
	 */
	private void removeCaseFromReserve(CaseOnListComplexValue caseOnList) {
		if (YES.equals(caseOnList.getReserved())) {
			caseOnListIdsReserve.remove(caseOnList.getCaseOnListId());
		}
	}

	/**
	 * Adjust the sequence numbers for all the cases on list
	 * relative to the supplied case on list being added.
	 * 
	 * @param caseOnList
	 */
	private void renumberAddedCaseToReserve(CaseOnListComplexValue caseOnList) {
		if (YES.equals(caseOnList.getReserved())) {
			// Renumber the cases on list under reserve after the new case on list
			renumberCasesOnList(getCasesOnListForReserve(), caseOnList.getCaseOnListId());
		}
	}

	/**
	 * Adjust the sequence numbers for the remaining cases on list for
	 * any gaps in the sequence numbers after deleting supplied case.
	 * 
	 * @param caseOnList
	 */
	private void renumberRemovedCaseFromReserve(CaseOnListComplexValue caseOnList) {
		if (YES.equals(caseOnList.getReserved())) {
			// Renumber the remaining cases on list under the reserve list
			renumberCasesOnList(getCasesOnListForReserve());
		}
	}

	public ListingDiaryModel getListingDiaryModel() {
		return new ListingDiaryModel(getListStartDate(), this);
	}
}