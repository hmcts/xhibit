package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
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
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * Base class for daily/firm list models.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractDailyFirmListModel extends AbstractListModel {

	private static final String Reserve_Key = "-1";
	
	// Sitting on list ids for a room with key date-room, i.e. YYYYMMDD-123
	private Map<String,List<Integer>> sittingOnListIdsRoom = new HashMap<String,List<Integer>>();

	// Case on list ids for a sitting with key sittingId
	private Map<Integer,List<Integer>> caseOnListIdsSitting = new HashMap<Integer,List<Integer>>();
	
	// Case on list ids for floater cases with key date-site, i.e. YYYYMMDD-123
	private Map<String,List<Integer>> caseOnListIdsFloater = new HashMap<String,List<Integer>>();
	
	// Case on list ids for reserve cases with key -1
	private Map<String,List<Integer>> caseOnListIdsReserve = new HashMap<String,List<Integer>>();

	public AbstractDailyFirmListModel(XhibitApplicationController xac, ListTypeEnum listType) {
		super(xac, listType);
	}

	@Override
	public SittingOnListComplexValue createSitting(XhbCourtRoomBasicValue courtRoom, Integer sittingNumber, Date dateListed) {
		// Call super class method to create a new populated sitting
		SittingOnListComplexValue sittingOnList = super.createSitting(courtRoom, sittingNumber, dateListed);
		
		// Add sitting to the map which tracks sittings per room
		addSittingToRoom(sittingOnList);
		
		// Re-sequence sitting numbers on court room for date
		renumberSittingsOnList(getSittingsOnList(dateListed, courtRoom.getCourtRoomId()), sittingOnList.getSittingOnListId());
		
		return sittingOnList;
	}

	@Override
	public void moveSitting(SittingOnListComplexValue sittingOnList, XhbCourtRoomBasicValue courtRoom, Integer sittingNumber, Date dateListed) {
		// Remove sitting from the map which tracks sittings per room
		removeSittingFromRoom(sittingOnList);
		
		// Re-sequence sitting numbers on original court room for date
		renumberSittingsOnList(getSittingsOnList(sittingOnList.getTimeListedDate(), sittingOnList.getCourtRoomId()));

		// Must check for the edge case when moving a sitting below its
		// current position under the same parent node. This means sitting
		// number must be one less as the new number must exclude itself
		if (sittingOnList.getCourtRoomId().equals(courtRoom.getCourtRoomId())
			&& isDaySame(sittingOnList.getTimeListedDate(), dateListed)
			&& sittingNumber > sittingOnList.getSittingNumber()) {
			sittingNumber--;
		}
		
		// Call super class method to move the sitting
		super.moveSitting(sittingOnList, courtRoom, sittingNumber, dateListed);
		
		// Update any case on lists under this sitting with its new location
		for (CaseOnListComplexValue caseOnList : getCasesOnListForSitting(sittingOnList.getSittingOnListId())) {
			caseOnList.setCourtSiteId(sittingOnList.getCourtSiteId());
			caseOnList.setCourtRoomId(sittingOnList.getCourtRoomId());
			caseOnList.setTimeListedDate(sittingOnList.getTimeListedDate());
			caseOnList.setDirty(true);
		}
		
		// Add updated sitting to the map which tracks sittings per room
		addSittingToRoom(sittingOnList);
		
		// Re-sequence sitting numbers on new court room for date
		renumberSittingsOnList(getSittingsOnList(dateListed, courtRoom.getCourtRoomId()), sittingOnList.getSittingOnListId());
	}

	@Override
	public void deleteSitting(SittingOnListComplexValue sittingOnList) {
		// Call super class method to delete the sitting
		super.deleteSitting(sittingOnList);
		
		// Remove sitting from the map which tracks sittings per room
		removeSittingFromRoom(sittingOnList);
		
		// Re-sequence sitting numbers on court room for date
		renumberSittingsOnList(getSittingsOnList(sittingOnList.getTimeListedDate(), sittingOnList.getCourtRoomId()));
	}

	@Override
	public CaseOnListComplexValue addCase(CaseOnListBasicValue defaults, CaseBasicValue caze, CaseListingEntryBasicValue caseListingEntry, DirectionsForCaseBasicValue directionsForCase,
											RefHearingTypeBasicValue hearingType, CaseOnListType parentType, Integer sequenceNumber, Object... parents) {
		// Call super class method to create a new populated case on list
		CaseOnListComplexValue caseOnList = super.addCase(defaults, caze, caseListingEntry, directionsForCase, hearingType, parentType, sequenceNumber, parents);

		// Populate additional fields for cases added to sittings
		if (CaseOnListType.Sitting.equals(parentType)) {
			// Get the supplied parent parameters
			SittingOnListComplexValue sitting = (SittingOnListComplexValue)parents[0];
			
			// Update case on list with sitting specific values
			caseOnList.setCourtSiteId(sitting.getCourtSiteId());
			caseOnList.setCourtRoomId(sitting.getCourtRoomId());
			caseOnList.setSittingOnListId(sitting.getSittingOnListId());
			caseOnList.setTimeListedDate(sitting.getTimeListedDate());
		}
		// Populate additional fields for cases added to floater cases
		else if (CaseOnListType.Floater.equals(parentType)) {
			// Get the supplied parent parameters
			Calendar listDate = (Calendar)parents[0];
			XhbCourtSiteBasicValue courtSite = (XhbCourtSiteBasicValue)parents[1];
			
			// Update case on list with floater specific values
			caseOnList.setCourtSiteId(courtSite.getCourtSiteId());
			caseOnList.setTimeListedDate(listDate.getTime());
			caseOnList.setFloaterCase(YES);
		}
		
		// Add relevant cases on list to sitting or floater
		addCaseToSittingOrFloater(caseOnList);
		
		// Re-sequence relevant cases on list under sitting or floater
		renumberAddedCaseToSittingOrFloater(caseOnList);
		
		return caseOnList;
	}

	@Override
	public void moveCase(CaseOnListComplexValue caseOnList, CaseOnListType parentType, Integer sequenceNumber, Object... parents) {
		// Remove relevant cases on list from sitting or floater
		removeCaseFromSittingOrFloater(caseOnList);
		
		// Re-sequence relevant cases affected by removing case
		renumberRemovedCaseFromSittingOrFloater(caseOnList);

		// Must check for the edge case when moving a case below its
		// current position under the same parent node. This means seq
		// number must be one less as the new number must exclude itself
		if (CaseOnListType.Sitting.equals(parentType)) {
			// Get the supplied parent parameters
			SittingOnListComplexValue sitting = (SittingOnListComplexValue)parents[0];
			
			// If case just moving down the same sittings node 
			if (sitting.getSittingOnListId().equals(caseOnList.getSittingOnListId())
				&& sequenceNumber > caseOnList.getSeqNo()) {
				sequenceNumber--;
			}
		}
		else if (CaseOnListType.Floater.equals(parentType)) {
			// Get the supplied parent parameters
			Calendar listDate = (Calendar)parents[0];
			XhbCourtSiteBasicValue courtSite = (XhbCourtSiteBasicValue)parents[1];
			
			// If case just moving down the same floater cases node 
			if (YES.equals(caseOnList.getFloaterCase()) && isDaySame(caseOnList.getTimeListedDate(), listDate.getTime())
				&& caseOnList.getCourtSiteId().equals(courtSite.getCourtSiteId())
				&& sequenceNumber > caseOnList.getSeqNo()) {
				sequenceNumber--;
			}
		}
		
		// Call super class method to move the case
		super.moveCase(caseOnList, parentType, sequenceNumber, parents);

		// Update case for the new parent sitting
		if (CaseOnListType.Sitting.equals(parentType)) {
			// Get the supplied parent parameters
			SittingOnListComplexValue sitting = (SittingOnListComplexValue)parents[0];
			
			// Update case on list with sitting specific values
			caseOnList.setCourtSiteId(sitting.getCourtSiteId());
			caseOnList.setCourtRoomId(sitting.getCourtRoomId());
			caseOnList.setSittingOnListId(sitting.getSittingOnListId());
			caseOnList.setTimeListedDate(sitting.getTimeListedDate());
		}
		// Update case for the new parent floater cases
		else if (CaseOnListType.Floater.equals(parentType)) {
			// Get the supplied parent parameters
			Calendar listDate = (Calendar)parents[0];
			XhbCourtSiteBasicValue courtSite = (XhbCourtSiteBasicValue)parents[1];
			
			// Update case on list with floater specific values
			caseOnList.setCourtSiteId(courtSite.getCourtSiteId());
			caseOnList.setTimeListedDate(listDate.getTime());
			caseOnList.setFloaterCase(YES);
		}
		
		// Add relevant cases on list to sitting or floater
		addCaseToSittingOrFloater(caseOnList);
		
		// Re-sequence relevant cases on list under sitting or floater
		renumberAddedCaseToSittingOrFloater(caseOnList);
	}

	@Override
	public void removeCase(CaseOnListComplexValue caseOnList) {
		// Call super class method to remove the case
		super.removeCase(caseOnList);
		
		// Remove relevant cases on list from sitting or floater
		removeCaseFromSittingOrFloater(caseOnList);
		
		// Re-sequence relevant cases affected by removing case
		renumberRemovedCaseFromSittingOrFloater(caseOnList);
	}

	/**
	 * Return list of sittings for date and court room sorted by sitting number.
	 * 
	 * @param date
	 * @param courtRoomId
	 * @return
	 */
	public List<SittingOnListComplexValue> getSittingsOnList(Date date, Integer courtRoomId) {
		List<SittingOnListComplexValue> sittingsOnList = new ArrayList<SittingOnListComplexValue>();
		
		// Create key to use to find any sittings that exist for the court room on the date
		String sittingOnListRoomKey = formatKey(date, courtRoomId);
		
		// If sittings exist for the court room and date, add each sitting to the list
		if (sittingOnListIdsRoom.containsKey(sittingOnListRoomKey)) {
			for (Integer sittingOnListId : sittingOnListIdsRoom.get(sittingOnListRoomKey)) {
				sittingsOnList.add(getSittingOnList(sittingOnListId));
			}
		}
		
		// Sort sittings on list by their sitting number
		sortSittingsOnList(sittingsOnList);
		
		return sittingsOnList;
	}

	/**
	 * Return list of cases for sitting sorted by sequence number.
	 * 
	 * @param sittingOnListId
	 * @return
	 */
	public List<CaseOnListComplexValue> getCasesOnListForSitting(Integer sittingOnListId) {
		List<CaseOnListComplexValue> casesOnList = new ArrayList<CaseOnListComplexValue>();
		
		// If cases exist for the sitting, add each case to the list
		if (caseOnListIdsSitting.containsKey(sittingOnListId)) {
			for (Integer caseOnListId : caseOnListIdsSitting.get(sittingOnListId)) {
				casesOnList.add(getCaseOnList(caseOnListId));
			}
		}
		
		// Sort cases on list by their sequence number
		sortCasesOnList(casesOnList);
		
		return casesOnList;
	}
	
	/**
	 * Return list of cases for date and court site sorted by sequence number.
	 * 
	 * @param date
	 * @param courtSiteId
	 * @return
	 */
	public List<CaseOnListComplexValue> getCasesOnListForFloater(Date date, Integer courtSiteId) {
		List<CaseOnListComplexValue> casesOnList = new ArrayList<CaseOnListComplexValue>();
		
		// Create key to use to find any cases that exist for the court site on the date
		String caseOnListSiteKey = formatKey(date, courtSiteId);
		
		// If cases exist for the court site and date, add each case to the list
		if (caseOnListIdsFloater.containsKey(caseOnListSiteKey)) {
			for (Integer caseOnListId : caseOnListIdsFloater.get(caseOnListSiteKey)) {
				casesOnList.add(getCaseOnList(caseOnListId));
			}
		}
		
		// Sort cases on list by their sequence number
		sortCasesOnList(casesOnList);
		
		return casesOnList;
	}
	
	/**
	 * Return list of cases on reserve sorted by sequence number.
	 * 
	 * @return
	 */
	public List<CaseOnListComplexValue> getCasesOnListForReserve() {
		List<CaseOnListComplexValue> casesOnList = new ArrayList<CaseOnListComplexValue>();

		// If cases exist for the court site and date, add each case to the list
		if (caseOnListIdsReserve.containsKey(Reserve_Key)) {
			for (Integer caseOnListId : caseOnListIdsReserve.get(Reserve_Key)) {
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
		
		// Add all the sittings on list to room
		for (SittingOnListComplexValue sittingOnList : sittingsOnList) {
			addSittingToRoom(sittingOnList);
		}
		
		// Add all the cases on list to either sitting or floater map
		for (CaseOnListComplexValue caseOnList : casesOnList) {
			addCaseToSittingOrFloater(caseOnList);
		}
	}

	@Override
	protected void clearListModel() {
		// Call super class method to clear its variables
		super.clearListModel();
		
		// Clear variables with sittings and cases on list
		sittingOnListIdsRoom.clear();
		caseOnListIdsSitting.clear();
		caseOnListIdsFloater.clear();
		caseOnListIdsReserve.clear();
	}
	
	/**
	 * Add the sitting on list to the map which tracks sittings per room.
	 * 
	 * @param sitting
	 */
	private void addSittingToRoom(SittingOnListComplexValue sitting) {
		// Create key to use to find any sittings that exist for the court room on the date
		String sittingOnListRoomKey = formatKey(sitting.getTimeListed(), sitting.getCourtRoomId());
		
		// If sittings do not yet exist for the court room and date, create list for room on date
		if (!sittingOnListIdsRoom.containsKey(sittingOnListRoomKey)) {
			sittingOnListIdsRoom.put(sittingOnListRoomKey, new ArrayList<Integer>());
		}
		
		// Add the sitting on list id to the list for room on date
		sittingOnListIdsRoom.get(sittingOnListRoomKey).add(sitting.getSittingOnListId());
	}
	
	/**
	 * Remove the sitting on list from the map which tracks sittings per room.
	 * 
	 * @param sitting
	 */
	private void removeSittingFromRoom(SittingOnListComplexValue sitting) {
		// Create key to use to find any sittings that exist for the court room on the date
		String sittingOnListRoomKey = formatKey(sitting.getTimeListed(), sitting.getCourtRoomId());
		
		// Remove the sitting on list id from the list for room on date
		sittingOnListIdsRoom.get(sittingOnListRoomKey).remove(sitting.getSittingOnListId());
	}
	
	/**
	 * Add the case on list to either the map which tracks cases per sitting
	 * or the map which tracks cases per floater as it cannot be in both.
	 * 
	 * @param caseOnList
	 */
	private void addCaseToSittingOrFloater(CaseOnListComplexValue caseOnList) {
		// If case is under a sitting, add to sitting list
		if (caseOnList.getSittingOnListId() != null) {
			// If cases do not yet exist for the sitting, create list for sitting
			if (!caseOnListIdsSitting.containsKey(caseOnList.getSittingOnListId())) {
				caseOnListIdsSitting.put(caseOnList.getSittingOnListId(), new ArrayList<Integer>());
			}
			
			// Add the case on list id to the list for sitting
			caseOnListIdsSitting.get(caseOnList.getSittingOnListId()).add(caseOnList.getCaseOnListId());
		}
		// Else if case is under a floater, add to floater list
		else if (YES.equals(caseOnList.getFloaterCase())) {
			// Create key to use to find any cases that exist for the court site floater
			String caseOnListFloaterKey = formatKey(caseOnList.getTimeListed(), caseOnList.getCourtSiteId());
			
			// If cases do not yet exist for the floater, create list for court site floater
			if (!caseOnListIdsFloater.containsKey(caseOnListFloaterKey)) {
				caseOnListIdsFloater.put(caseOnListFloaterKey, new ArrayList<Integer>());
			}
			
			// Add the case on list id to the list for court site floater
			caseOnListIdsFloater.get(caseOnListFloaterKey).add(caseOnList.getCaseOnListId());
		}
		// Else if case is under a Reserved, add to reserved list
		else if (YES.equals(caseOnList.getReserved())) {
			
			// If cases do not yet exist for the floater, create list for court site floater
			if (!caseOnListIdsReserve.containsKey(Reserve_Key)) {
				caseOnListIdsReserve.put(Reserve_Key, new ArrayList<Integer>());
			}
			
			// Add the case on list id to the list for court site floater
			caseOnListIdsReserve.get(Reserve_Key).add(caseOnList.getCaseOnListId());
		}
	}

	/**
	 * Remove the case on list from either the map which tracks cases per sitting
	 * or the map which tracks cases per floater as it cannot be in both.
	 * 
	 * @param caseOnList
	 */
	private void removeCaseFromSittingOrFloater(CaseOnListComplexValue caseOnList) {
		// If case is under a sitting, remove from sitting list
		if (caseOnList.getSittingOnListId() != null) {
			// Remove the case on list id from the list for its sitting
			caseOnListIdsSitting.get(caseOnList.getSittingOnListId()).remove(caseOnList.getCaseOnListId());
		}
		// Else if case is under a floater, remove from floater list
		else if (YES.equals(caseOnList.getFloaterCase())) {
			// Create key to use to find any cases that exist for the court site floater
			String caseOnListFloaterKey = formatKey(caseOnList.getTimeListed(), caseOnList.getCourtSiteId());
			
			// Remove the case on list id from the list for court site floater
			caseOnListIdsFloater.get(caseOnListFloaterKey).remove(caseOnList.getCaseOnListId());
		}
		else if (YES.equals(caseOnList.getReserved())) {
			// Remove the case on list id from the list for the reserve folder
			caseOnListIdsReserve.get(Reserve_Key).remove(caseOnList.getCaseOnListId());
		}
	}
	
	/**
	 * Adjust the sequence numbers for all the cases on list
	 * relative to the supplied case on list being added.
	 * 
	 * @param caseOnList
	 */
	private void renumberAddedCaseToSittingOrFloater(CaseOnListComplexValue caseOnList) {
		// If case is under a sitting, renumber the sitting list
		if (caseOnList.getSittingOnListId() != null) {
			// Renumber the cases on list under sitting after the new case on list
			renumberCasesOnList(getCasesOnListForSitting(caseOnList.getSittingOnListId()), caseOnList.getCaseOnListId());
		}
		// Else if case is under a floater, renumber the floater list
		else if (YES.equals(caseOnList.getFloaterCase())) {
			// Renumber the cases on list under floater after the new case on list
			renumberCasesOnList(getCasesOnListForFloater(caseOnList.getTimeListedDate(), caseOnList.getCourtSiteId()), caseOnList.getCaseOnListId());
		}
	}
	
	/**
	 * Adjust the sequence numbers for the remaining cases on list for
	 * any gaps in the sequence numbers after deleting supplied case.
	 * 
	 * @param caseOnList
	 */
	private void renumberRemovedCaseFromSittingOrFloater(CaseOnListComplexValue caseOnList) {
		// If case is under a sitting, renumber the sitting list
		if (caseOnList.getSittingOnListId() != null) {
			// Renumber the cases on list under sitting of deleted case on list
			renumberCasesOnList(getCasesOnListForSitting(caseOnList.getSittingOnListId()));
		}
		// Else if case is under a floater, renumber the floater list
		else if (YES.equals(caseOnList.getFloaterCase())) {
			// Renumber the cases on list under floater of deleted case on list
			renumberCasesOnList(getCasesOnListForFloater(caseOnList.getTimeListedDate(), caseOnList.getCourtSiteId()));
		}
	}
}
