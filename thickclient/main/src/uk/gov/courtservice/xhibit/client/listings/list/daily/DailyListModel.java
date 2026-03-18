package uk.gov.courtservice.xhibit.client.listings.list.daily;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingDiaryModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Model for daily list panel.
 * 
 * @author uphillj
 *
 */
public class DailyListModel extends AbstractDailyFirmListModel {

	public DailyListModel(XhibitApplicationController xac) {
		super(xac, ListTypeEnum.Daily);
	}
	
	/**
	 * Create new daily list for supplied date
	 * 
	 * @param listDate
	 */
	public void createList(Calendar listDate) {
		super.createList(listDate, listDate);
	}

	/**
	 * Create new daily list for supplied date generated from a previous list
	 * 
	 * @param listDate
	 * @param listParentId
	 */
	public void createList(final Calendar listDate, final Integer listParentId) {
		// Call super class to create the list basic value
		super.createList(listDate, listDate, listParentId);
		
		// Copy the sittings and cases to model as a background task to allow the dialog
		// to carry on initialising while this potentially long running task completes
		SwingWorker worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					// Copy the relevant sittings and cases from the parent list
					copySittingsAndCases(listParentId);
				} finally {				
					// List is now populated with all the sittings and cases
					setListPopulated(true);
				}
				return null;
			}

			@Override
			protected void done() {
				// If the background task completed, call the get method to
				// check if an exception occurred, which if it did will be
				// re-thrown wrapped in an ExecutionException exception.
				if (!isCancelled()) {
					try {
						get();
					} catch (ExecutionException e) {
						Throwable cause = e.getCause();
						if (cause instanceof Exception) {
							XHIBITConstant.handleError((Exception)e.getCause());
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		worker.execute();
	}

	@Override
	public CaseOnListComplexValue addCase(CaseOnListBasicValue defaults, CaseBasicValue caze, CaseListingEntryBasicValue caseListingEntry, DirectionsForCaseBasicValue directionsForCase,
											RefHearingTypeBasicValue hearingType, CaseOnListType parentType, Integer sequenceNumber, Object... parents) {
		// Call super class method to create a new populated case on list
		CaseOnListComplexValue caseOnList = super.addCase(defaults, caze, caseListingEntry, directionsForCase, hearingType, parentType, sequenceNumber, parents);
		
		// Cases on daily lists are by default part of the court room list
		caseOnList.setIsCourtRoomListEntry(YES);
		
		return caseOnList;
	}
	
	public DefOnCaseOnListBasicValue addDefendant(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId) {
		// Call super class method to create a new populated def on case on list with the court room list set to 'Y' by default
		DefOnCaseOnListBasicValue defOnCaseOnList = super.addDefendant(caseOnList, defendantOnCaseId, YES);
		return defOnCaseOnList;
	}

	@Override
	public boolean isDefendantNotListable(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId) {
		// Returns true if the defendant is already listed in a different case on list in the model
		return super.isDefendantOnCaseOnListComplexValues(caseOnList.getCaseId(), defendantOnCaseId, caseOnList.getCaseOnListId());
	}

	/**
	 * Copy all the sittings with cases and any relevant trial cases
	 * from the parent list into the model in this list.
	 * 
	 * @param listParentId
	 * @throws CSRecoverableException 
	 */
	private void copySittingsAndCases(Integer listParentId) throws CSRecoverableException {
		// Get all the sittings and cases on the parent list
		ListComplexValue parentList = XhibitDelegateHelper.getListingsDelegate().findListDetail(listParentId);
		
		// Filter the list of sittings and cases to copy based on whether a daily or a firm list
		Collection<CaseOnListComplexValue> casesOnList;
		Collection<SittingOnListComplexValue> sittingsOnList;
		if (ListTypeEnum.Daily.equals(ListingDropdownPopulation.getListType(parentList.getListTypeId()))) {
			casesOnList = filterDailyListCasesToCopy(parentList.getCasesOnList());
			sittingsOnList = filterDailyListSittingsToCopy(parentList.getSittingsOnList(), casesOnList);
		} else {
			casesOnList = filterFirmListCasesToCopy(parentList.getCasesOnList());
			sittingsOnList = filterFirmListSittingsToCopy(parentList.getSittingsOnList(), casesOnList);
		}
		
		// Populate the list model with the copied sittings and cases
		populateListModel(sittingsOnList, casesOnList);
		
		// Renumber all sittings and all cases under all sittings/floaters
		renumberAllSittingsAndAllCases();
	}

	/**
	 * Return filtered list with all cases under sittings with certain hearing types.
	 * The date also needs to be adjusted as the previous daily list is on a different
	 * day. Reset the cases to copy to be new so they can be added to this model.
	 * 
	 * @param casesOnList
	 * @return casesOnList
	 * @throws CSRecoverableException 
	 */
	private Collection<CaseOnListComplexValue> filterDailyListCasesToCopy(Collection<CaseOnListComplexValue> casesOnList) throws CSRecoverableException {
		List<CaseOnListComplexValue> filteredCasesOnList = new ArrayList<CaseOnListComplexValue>();
		
		// Get the hearing types for cases that are to be copied from previous sittings 
		List<String> sittingHearingTypes = ListingDropdownPopulation.getRefListingDataValues("LIST_COPY_CASE_SITTING_HEARING_TYPES");
		
		// If any case matches required criteria to copy from daily list,
		// add to the filtered list and reset case on list to new
		for (CaseOnListComplexValue caseOnList : casesOnList) {
			if (isDailyListCaseValidToCopy(caseOnList, sittingHearingTypes)) {
				filteredCasesOnList.add(caseOnList);
				resetCaseOnListToCopy(caseOnList);
			}
		}
		
		return filteredCasesOnList;
	}
	
	/**
	 * Return filtered list with all the cases on the date of the firm list.
	 * Reset the cases to copy to be new so they can be added to this model.
	 * 
	 * @param casesOnList
	 * @return casesOnList
	 */
	private Collection<CaseOnListComplexValue> filterFirmListCasesToCopy(Collection<CaseOnListComplexValue> casesOnList) {
		List<CaseOnListComplexValue> filteredCasesOnList = new ArrayList<CaseOnListComplexValue>();

		// If any case matches required criteria to copy from firm list,
		// add to the filtered list and reset case on list to new
		for (CaseOnListComplexValue caseOnList : casesOnList) {
			if (DateTimeUtilities.isDaySame(getListStartDate().getTime(), caseOnList.getTimeListedDate())
				&& !YES.equals(caseOnList.getReserved())) {
				filteredCasesOnList.add(caseOnList);
				resetCaseOnListToCopy(caseOnList);
			}
		}
		
		return filteredCasesOnList;
	}
	
	/**
	 * Return filtered list with all the sittings from the previous daily list.
	 * Reset the sittings to copy to be new so they can be added to this model.
	 * 
	 * @param sittingsOnList
	 * @param casesOnList
	 * @return sittingsOnList
	 */
	private Collection<SittingOnListComplexValue> filterDailyListSittingsToCopy(Collection<SittingOnListComplexValue> sittingsOnList, Collection<CaseOnListComplexValue> casesOnList) {
		List<SittingOnListComplexValue> filteredSittingsOnList = new ArrayList<SittingOnListComplexValue>();

		// Add all sittings to the list and reset sitting on list to new
		for (SittingOnListComplexValue sittingOnList : sittingsOnList) {
			filteredSittingsOnList.add(sittingOnList);
			resetSittingOnListToCopy(sittingOnList, casesOnList);
		}
		
		return filteredSittingsOnList;
	}
	
	/**
	 * Return filtered list with all the sittings which have cases or a judge.
	 * Reset the sittings to copy to be new so they can be added to this model.
	 * 
	 * @param sittingsOnList
	 * @param casesOnList
	 * @return sittingsOnList
	 */
	private Collection<SittingOnListComplexValue> filterFirmListSittingsToCopy(Collection<SittingOnListComplexValue> sittingsOnList, Collection<CaseOnListComplexValue> casesOnList) {
		List<SittingOnListComplexValue> filteredSittingsOnList = new ArrayList<SittingOnListComplexValue>();

		// If any sitting has a judge or cases being copied to this list,
		// add to the filtered list and reset sitting on list to new
		for (SittingOnListComplexValue sittingOnList : sittingsOnList) {
			if (isFirmListSittingValidToCopy(sittingOnList, casesOnList)) {
				filteredSittingsOnList.add(sittingOnList);
				resetSittingOnListToCopy(sittingOnList, casesOnList);
			}
		}
		
		return filteredSittingsOnList;
	}
	
	/**
	 * Case is valid to copy from a daily list if it is under a sitting
	 * and has a certain hearing type.
	 * 
	 * @param caseOnList
	 * @param sittingHearingTypes
	 * @return
	 */
	private boolean isDailyListCaseValidToCopy(CaseOnListComplexValue caseOnList, Collection<String> sittingHearingTypes) {
		boolean valid = false;
		
		// Only cases under a sitting can be copied, i.e. not floater cases
		if (caseOnList.getSittingOnListId() != null) {
			// Get the hearing type for the case which should never be
			// null once added to a list but be defensive just in case
			String hearingTypeCode = null;
			if (caseOnList.getHearingType() != null) {
				hearingTypeCode = caseOnList.getHearingType().getHearingTypeCode();
			}
			
			// If hearing type is one of the special hearing types to be
			// copied across then the case is a valid one to copy
			if (hearingTypeCode != null && sittingHearingTypes.contains(hearingTypeCode)) {
				valid = true;
			}
		}
		
		return valid;
	}

	/**
	 * Sitting is valid to copy from a firm list if it has
	 * a case underneath it or has an assigned judge.
	 * 
	 * @param sittingOnList
	 * @param casesOnList
	 * @return
	 */
	private boolean isFirmListSittingValidToCopy(SittingOnListComplexValue sittingOnList, Collection<CaseOnListComplexValue> casesOnList) {
		boolean valid = false;
		
		// If sitting has a judge...
		if (sittingOnList.getJudgeRefId() != null) {
			//...and its Daily...
			if (ListTypeEnum.Daily.equals(ListingDropdownPopulation.getListType(getList().getListTypeId()))) {
				// ...then it is copied only if the sitting is for the date of the list 
				valid = DateTimeUtilities.isDaySame(getListStartDate().getTime(), sittingOnList.getTimeListedDate());
			} else {
				// ...then it is always copied
				valid = true;
			}
		}
		
		// Else check if there is a case under the sitting
		if (!valid) {
			for (CaseOnListComplexValue caseOnList : casesOnList) {
				if (sittingOnList.getSittingOnListId().equals(caseOnList.getSittingOnListId())) {
					valid = true;
					break;
				}
			}
		}
		
		return valid;
	}
	
	/**
	 * Reset the case on list and its defendant on case on list children
	 * with a new case on list id and other properties reset as new so
	 * it is ready to be added to this list model and saved as new.
	 * 
	 * @param caseOnList
	 */
	private void resetCaseOnListToCopy(CaseOnListComplexValue caseOnList) {
		// Reset case on list to be a new record for this list
		caseOnList.setId(XhibitDelegateHelper.getListingsDelegate().getNextCaseOnListId());
		caseOnList.setCaseOnListId(caseOnList.getId());
		caseOnList.setTimeListedDate(getListStartDate().getTime());
		caseOnList.setIsCourtRoomListEntry(YES);
		caseOnList.setNhaFirmList(null);
		caseOnList.setCrackedIneffectiveId(null);
		
		caseOnList.setVersion(-1);
		caseOnList.setDirty(true);
		
		// Reset defendants on case on list to be new records for this list
		for (DefOnCaseOnListBasicValue defOnCaseOnList : caseOnList.getDefOnCaseOnLists()) {
			defOnCaseOnList.setId(null);
			defOnCaseOnList.setDefOnCaseOnListId(null);
			defOnCaseOnList.setCaseOnListId(caseOnList.getCaseOnListId());
			defOnCaseOnList.setIsCourtRoomListEntry(YES);
			defOnCaseOnList.setVersion(-1);
			defOnCaseOnList.setDirty(true);
		}
	}

	/**
	 * Reset the sitting on list and any cases underneath it with a new
	 * sittings id and other properties reset as new so it is ready to
	 * be added to this list model and saved as new.
	 * 
	 * @param sittingOnList
	 * @param casesOnList
	 */
	private void resetSittingOnListToCopy(SittingOnListComplexValue sittingOnList, Collection<CaseOnListComplexValue> casesOnList) {
		// Create new primary key to use for the new sitting
		Integer newSittingOnListId = XhibitDelegateHelper.getListingsDelegate().getNextSittingOnListId();

		// Update the sitting on list id on any cases underneath the sitting
		for (CaseOnListComplexValue caseOnList : casesOnList) {
			if (sittingOnList.getSittingOnListId().equals(caseOnList.getSittingOnListId())) {
				caseOnList.setSittingOnListId(newSittingOnListId);
			}
		}
		
		// Reset sitting on list to be a new record for this list
		sittingOnList.setId(newSittingOnListId);
		sittingOnList.setSittingOnListId(sittingOnList.getId());
		sittingOnList.setTimeListedDate(getListStartDate().getTime());
		sittingOnList.setVersion(-1);
		sittingOnList.setDirty(true);
	}
	
	/**
	 * Renumber all the sittings and all the cases on the list. This is
	 * required when creating a list from a previous list because not all
	 * sittings or cases have necessarily been copied from the previous
	 * list. This means there could be gaps in the sequence numbers and
	 * so this method renumbers everything to make sure there are no gaps.
	 */
	private void renumberAllSittingsAndAllCases() {
    	// Get court sites for court
        CourtStructureValue courtStructure = XhibitSingleton.getInstance().getCourtStructureValue();
        
        // Renumber all sittings and all cases under sittings/floaters
        for (int i = 0; i < courtStructure.getCourtSites().length; i++) {
        	// Get court site and its court rooms
            XhbCourtSiteBasicValue courtSite = courtStructure.getCourtSites()[i];
            XhbCourtRoomBasicValue courtRooms[] = courtStructure.getCourtRoomsForSite(courtSite.getCourtSiteId());

            // Cycle through all the court rooms
            for (int j = 0; j < courtRooms.length; j++) {
            	// Get court room
                XhbCourtRoomBasicValue courtRoom = courtRooms[j];

                // Get all the sittings under the court room
                List<SittingOnListComplexValue> sittingsOnList = getSittingsOnList(getListStartDate().getTime(), courtRoom.getCourtRoomId());

                // Renumber all the sittings under the court room
				renumberSittingsOnList(sittingsOnList);
				
				// Cycle through all the sittings and renumber all the cases under each
                for (SittingOnListComplexValue sittingOnList : sittingsOnList) {
                	renumberCasesOnList(getCasesOnListForSitting(sittingOnList.getSittingOnListId()));
                }
            }
    		
			// Renumber all the cases under the floater for the court site
			renumberCasesOnList(getCasesOnListForFloater(getListStartDate().getTime(), courtSite.getCourtSiteId()));
        }		
	}

	public ListingDiaryModel getListingDiaryModel() {
		return new ListingDiaryModel(getListStartDate(), this);
	}
}