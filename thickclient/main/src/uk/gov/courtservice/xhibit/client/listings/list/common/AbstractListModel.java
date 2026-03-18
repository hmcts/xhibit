package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.lang.StringUtils;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListSaveResult;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.ListValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Base class for daily/warned/firm list models.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractListModel implements ListModel {

	protected static final Integer ONE = 1;
	
	protected final String YES = "Y";
	protected final String NO = "N";
	
	private ListTypeEnum listType;

	private XhibitApplicationController xac;
	
	private TreeNodeFactory treeNodeFactory;
	
	private ListBasicValue listBasicValue;
	
	private boolean listNew = true;
	
	private boolean listPopulated;
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	// All sitting on list complex values with key sittingOnListId
	private Map<Integer,SittingOnListComplexValue> allSittingOnListComplexValues = new HashMap<Integer,SittingOnListComplexValue>();
	
	// All case on list complex values with key caseOnListId
	private Map<Integer,CaseOnListComplexValue> allCaseOnListComplexValues = new HashMap<Integer,CaseOnListComplexValue>();
	
	// Case on list ids for a case with key caseId
	private Map<Integer,List<Integer>> caseOnListIdsCase = new HashMap<Integer,List<Integer>>();
	
	public AbstractListModel(XhibitApplicationController xac, ListTypeEnum listType) {
		this.xac = xac;
		this.listType = listType;
	}
	
	@Override
	public XhibitApplicationController getXac() {
		return xac;
	}

	@Override
	public TreeNodeFactory getTreeNodeFactory() {
		return treeNodeFactory;
	}

	@Override
	public void setTreeNodeFactory(TreeNodeFactory treeNodeFactory) {
		this.treeNodeFactory = treeNodeFactory;
	}
	
	@Override
	public ListTypeEnum getListType() {
		return listType;
	}

	@Override
	public ListBasicValue getList() {
		return listBasicValue;
	}

	@Override
	public Calendar getListStartDate() {
		return DateTimeUtilities.convertToCalendar(listBasicValue.getListStartDate());
	}

	@Override
	public Calendar getListEndDate() {
		return DateTimeUtilities.convertToCalendar(listBasicValue.getListEndDate());
	}

	@Override
	public void setListEndDate(Calendar date) {
		listBasicValue.setListEndDate(date != null ? date.getTime() : null);
	}

	@Override
	public boolean isListNew() {
		return listNew;
	}

	@Override
	public boolean isListDraft() {
		return listBasicValue.isDraft();
	}

	@Override
	public void setListDraft() {
		listBasicValue.setDraftOrFinal(ListBasicValue.DraftOrFinal.DRAFT);
		resetListBasicValueListNumber();
		resetListBasicValuePublished();
	}

	@Override
	public boolean isListFinal() {
		return !listBasicValue.isDraft();
	}

	@Override
	public void setListFinal() {
		listBasicValue.setDraftOrFinal(ListBasicValue.DraftOrFinal.FINAL);
		resetListBasicValueListNumber();
		resetListBasicValuePublished();
	}

	@Override
	public boolean hasListPublished() {
		return (listBasicValue.isPublished() || !ONE.equals(listBasicValue.getListNumber()));
	}

	@Override
	public Collection<SittingOnListComplexValue> getSittingsOnList() {
		Collection<SittingOnListComplexValue> sittingsOnList = new ArrayList<SittingOnListComplexValue>();
		for (SittingOnListComplexValue sittingOnList : allSittingOnListComplexValues.values()) {
			if (!YES.equals(sittingOnList.getObsInd())) {
				sittingsOnList.add(sittingOnList);
			}
		}
		return sittingsOnList;
	}

	@Override
	public Collection<CaseOnListComplexValue> getCasesOnList() {
		Collection<CaseOnListComplexValue> casesOnList = new ArrayList<CaseOnListComplexValue>();
		for (CaseOnListComplexValue caseOnList : allCaseOnListComplexValues.values()) {
			if (!YES.equals(caseOnList.getObsInd())) {
				casesOnList.add(caseOnList);
			}
		}
		return casesOnList;
	}

	@Override
	public Collection<CaseOnListComplexValue> getCasesOnList(Collection<String> hearingTypes) {
		Collection<CaseOnListComplexValue> casesOnList = new ArrayList<CaseOnListComplexValue>();
		for (CaseOnListComplexValue caseOnList : getCasesOnList()) {
			// Get the hearing type for the case which should never be
			// null once added to a list but be defensive just in case
			String hearingTypeCode = null;
			if (caseOnList.getHearingType() != null) {
				hearingTypeCode = caseOnList.getHearingType().getHearingTypeCode();
			}
			
			// If hearing type is one of the required hearing types, add to list
			if (hearingTypeCode != null && hearingTypes.contains(hearingTypeCode)) {
				casesOnList.add(caseOnList);
			}
		}
		return casesOnList;
	}

	@Override
	public void openList(final Integer listId) throws CSRecoverableException {
		// Open list method is shared across all lists so listNew default
		// is true and then this method just resets to false when opening
		listNew = false;
		
		// Add the list to model which is the initial minimum data required
		ListBasicValue list = XhibitDelegateHelper.getListingsDelegate().findList(listId);
		addListBasicValue(list);
		
		// Add the sittings and cases to model as a background task to allow the dialog
		// to carry on initialising while this potentially long running task completes
		SwingWorker worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					// Get all the sittings and cases on the list and populate model
					ListComplexValue list = XhibitDelegateHelper.getListingsDelegate().findListDetail(listId);
					populateListModel(list.getSittingsOnList(), list.getCasesOnList());
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
	public void refresh() throws CSRecoverableException {
		Integer listId = getList() != null ? getList().getListId() : null;
		if (listId != null) {
			ListComplexValue list = XhibitDelegateHelper.getListingsDelegate().findListDetail(listId);
			if (!getList().isDirty()) {
				addListBasicValue((ListBasicValue) list);
			}
			populateListModel(list.getSittingsOnList(), list.getCasesOnList());
		}
	}

	@Override
	public boolean saveList() throws CSRecoverableException {
		// If the list is currently at a published status (success or failure)
		// then the list number needs to be incremented to indicate new version
		// and the published status needs to be cleared
		if (listBasicValue.isPublished()) {
			increaseListBasicValueListNumber();
			resetListBasicValuePublished();
		}
		
		// Create list value with list and dirty sittings on list and cases on list
		ListValue listValue = createDirtyListValue();
		
		// Save the changes made to the list
		boolean saveSuccess = saveListValue(listValue);
		
		return saveSuccess;
	}

	@Override
	public void publishList() throws CSRecoverableException {
		// Publish the list
		try {
			XhibitDelegateHelper.getListingsDelegate().publishList(listBasicValue.getListId());
		} catch (CSRecoverableException e) {
			// Update the fields for a publish failure
			String publishErrorReason = StringUtils.left(e.getUserMessage(), 200);
			XhibitDelegateHelper.getListingsDelegate().setListPublishFailureStatus(
					listBasicValue.getListId(), publishErrorReason);
			// Re-throw exception to report failure to user
			throw e;
		}
	}

	@Override
	public boolean isListPopulated() {
		return listPopulated;
	}

	protected void setListPopulated(boolean listPopulated) {
		this.listPopulated = listPopulated;
		if (listBasicValue != null && listBasicValue.getListId() == null) {
			try {
				saveList();
				refresh();
			} catch (CSRecoverableException e) {
				XHIBITConstant.handleError(e);
			}
		}
	}
	
	@Override
	public boolean hasUnsavedChanges() {
		// If the list is dirty, there are unsaved changes
		if (listBasicValue.isDirty()) {
			return true;
		}
		
		// If any sittings on list are dirty, there are unsaved changes
		for (SittingOnListComplexValue value : allSittingOnListComplexValues.values()) {
			if (value.isDirty()) {
				return true;
			}
		}
		
		// If any cases on list are dirty, there are unsaved changes
		for (CaseOnListComplexValue value : allCaseOnListComplexValues.values()) {
			if (value.isDirty()) {
				return true;
			}
		}
		
		// If reached here, then nothing has been changed since
		// the last time the method saveList was called
		return false;
	}

	@Override
	public SittingOnListComplexValue createSitting(XhbCourtRoomBasicValue courtRoom, Integer sittingNumber, Date dateListed) {
		// Return populated new sitting on list which has been added to model
		return createSittingOnListComplexValue(courtRoom, sittingNumber, dateListed);
	}

	@Override
	public void moveSitting(SittingOnListComplexValue sittingOnList, XhbCourtRoomBasicValue courtRoom, Integer sittingNumber, Date dateListed) {
		// Update sitting for its new parent court room and position
		moveSittingOnListComplexValue(sittingOnList.getSittingOnListId(), courtRoom, sittingNumber, dateListed);
	}

	@Override
	public void deleteSitting(SittingOnListComplexValue sittingOnList) {
		// Remove sitting from model or mark as obsolete if previously saved
		deleteSittingOnListComplexValue(sittingOnList.getSittingOnListId());
	}

	@Override
	public CaseOnListComplexValue addCase(CaseOnListBasicValue defaults, CaseBasicValue caze, CaseListingEntryBasicValue caseListingEntry, DirectionsForCaseBasicValue directionsForCase,
											RefHearingTypeBasicValue hearingType, CaseOnListType parentType, Integer sequenceNumber, Object... parents) {
		// Return populated new case on list which has been added to model
		return createCaseOnListComplexValue(defaults, caze, caseListingEntry, directionsForCase, hearingType, sequenceNumber);
	}

	@Override
	public void moveCase(CaseOnListComplexValue caseOnList, CaseOnListType parentType, Integer sequenceNumber, Object... parents) {
		// Update case on list sequence and clear fields used to link case to parent; sub-classes will re-populate relevant fields
		moveCaseOnListComplexValue(caseOnList.getCaseOnListId(), sequenceNumber);
	}

	@Override
	public void removeCase(CaseOnListComplexValue caseOnList) {
		// Remove case from model or mark as obsolete if previously saved
		deleteCaseOnListComplexValue(caseOnList.getCaseOnListId());
	}

	@Override
	public boolean isCaseListed(Integer caseId, Date dateListed) {
		// Returns true if there is a case on list on the date in the model
		return isDateOnCaseOnListComplexValues(caseId, dateListed);
	}

	@Override
	public boolean isCaseListedElsewhere(Integer caseId, Integer caseOnListId) {
		// Returns true if there is a case on list with the hearing type in the model
		return isCaseIdOnCaseOnListComplexValues(caseId, caseOnListId);
	}

	@Override
	public boolean isFixtureListed(Integer caseId, Integer caseDiaryFixtureId) {
		// Returns true if there is a case on list with the fixture id in the model
		return isFixtureOnCaseOnListComplexValues(caseId, caseDiaryFixtureId);
	}

	@Override
	public boolean isNonFixedListed(Integer caseId, Integer parentCaseOnListId) {
		// Returns true if there is a case on list with the parent case on list id in the model
		return isParentOnCaseOnListComplexValues(caseId, parentCaseOnListId);
	}

	@Override
	public DefOnCaseOnListBasicValue addDefendant(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId) {
		// Return populated new def on case on list which has been added to model.  Pass Court Room List as
		// NULL if not specified.
		return createDefOnCaseOnListBasicValue(caseOnList.getCaseOnListId(), defendantOnCaseId, null);
	}
	
	@Override
	public DefOnCaseOnListBasicValue addDefendant(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId, String courtRoomList) {
		// Return populated new def on case on list which has been added to model
		return createDefOnCaseOnListBasicValue(caseOnList.getCaseOnListId(), defendantOnCaseId, courtRoomList);
	}

	@Override
	public void removeDefendant(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId) {
		// Remove def on case on list from model or mark as obsolete if previously saved
		deleteDefOnCaseOnListBasicValue(caseOnList.getCaseOnListId(), defendantOnCaseId);
	}
	
	@Override
	public void updateDefendant(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId, String courtRoomList) {
		// Update def on case on list with Court Room List indicator
		updateDefOnCaseOnListBasicValue(caseOnList.getCaseOnListId(), defendantOnCaseId, courtRoomList);
	}

	@Override
	public boolean isDefendantListed(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId) {
		// Return true if the defendant is on the case on list in the model
		return isDefendantOnCaseOnListComplexValue(caseOnList.getCaseOnListId(), defendantOnCaseId);
	}

	@Override
	public boolean isDefendantListed(Date dateListed, Integer caseId, Integer defendantOnCaseId) {
		// Returns true if the defendant is listed on the date in the model
		return isDefendantOnCaseOnListComplexValues(dateListed, caseId, defendantOnCaseId);
	}

	@Override
	public boolean isDefendantListedElsewhere(Integer caseId, Integer defendantOnCaseId, Integer caseOnListId) {
		// Returns true if the defendant is listed on the date in the model
		return isDefendantOnCaseOnListComplexValues(caseId, defendantOnCaseId, caseOnListId);
	}
	
	@Override
	public String isDefendantOnCourtRoomList(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId) {
		return isDefendantOnCaseOnListOnCourtList(caseOnList.getCaseOnListId(), defendantOnCaseId);
	}
	
	public abstract ListingDiaryModel getListingDiaryModel();

	/**
	 * Create new list for supplied dates
	 * 
	 * @param startDate
	 * @param endDate
	 */
	protected void createList(Calendar startDate, Calendar endDate) {
		// Create new list basic value for dates
		createListBasicValue(startDate, endDate);
		
		// New lists have no sittings or cases, so nothing to populate
		setListPopulated(true);
	}
	
	/**
	 * Create new list for supplied dates from parent list
	 * 
	 * @param startDate
	 * @param endDate
	 * @param listParentId
	 */
	protected void createList(Calendar startDate, Calendar endDate, Integer listParentId) {
		// Create new list basic value for dates
		createListBasicValue(startDate, endDate);
		
		// Set the additional parent list properties
		listBasicValue.setListParentId(listParentId);
	}
	
	/**
	 * Populate the model with the supplied sittings and cases, which
	 * sub-classes override to populate their additional model variables.
	 * 
	 * @param sittingsOnList
	 * @param casesOnList
	 */
	protected void populateListModel(Collection<SittingOnListComplexValue> sittingsOnList, Collection<CaseOnListComplexValue> casesOnList) {
		// Clear the current sittings and cases
		clearListModel();
		
		// Add all the sittings on list to model
		for (SittingOnListComplexValue sittingOnList : sittingsOnList) {
			addSittingOnListComplexValue(sittingOnList);
		}
		
		// Add all the cases on list to model
		for (CaseOnListComplexValue caseOnList : casesOnList) {
			addCaseOnListComplexValue(caseOnList);
		}
	}
	
	/**
	 * Clear sittings and cases on list, which sub-classes
	 * override to clear their additional model variables.
	 */
	protected void clearListModel() {
		allSittingOnListComplexValues.clear();
		allCaseOnListComplexValues.clear();
		caseOnListIdsCase.clear();
	}
	
	/**
	 * Get the sitting on list complex value.
	 * 
	 * @param sittingOnListId
	 * @return
	 */
	protected SittingOnListComplexValue getSittingOnList(Integer sittingOnListId) {
		return allSittingOnListComplexValues.get(sittingOnListId);
	}

	/**
	 * Get the case on list complex value.
	 * 
	 * @param caseOnListId
	 * @return
	 */
	protected CaseOnListComplexValue getCaseOnList(Integer caseOnListId) {
		return allCaseOnListComplexValues.get(caseOnListId);
	}

	/**
	 * Get the case on list ids for the case id.
	 * 
	 * @param caseId
	 * @return
	 */
	protected Collection<Integer> getCaseOnListIds(Integer caseId) {
		List<Integer> casesOnListIds = new ArrayList<Integer>();

		// Cases on list might not yet exist for the case id, so first
		// check existence of cases on list and only then get their ids
		if (caseOnListIdsCase.containsKey(caseId)) {
			casesOnListIds.addAll(caseOnListIdsCase.get(caseId));
		}
		
		return casesOnListIds;
	}

	/**
	 * Save the list value and update the local copy of the list
	 * basic value with the id and version after the save.
	 * 
	 * @param listValue
	 * @return If the save has been successful.
	 * @throws CSRecoverableException 
	 */
	protected boolean saveListValue(ListValue listValue) throws CSRecoverableException {
		// Check if another user has made the list obsolete
		if (YES.equals(listValue.getList().getObsInd())) {
			showListDeletedMsg();
			return false;
		}
		
		// Save the changes made to the list for publish
		ListSaveResult result = XhibitDelegateHelper.getListingsDelegate().saveList(listValue,
			XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
		
		if (!result.isSuccess()) {
			showOptimisticLockMsg();
			return false;
		} else {

			// Update the version of the list basic value first now it has been
			// saved
			ListBasicValue savedList = XhibitDelegateHelper.getListingsDelegate().findList(result.getListId());
			updateListBasicValue(result.getListId(), savedList.getVersion());
			return true;
		}
	}
	
	@Override
	public void showOptimisticLockMsg() {
		final String currMessage = getErrorResource(XhibitBundles.Listings, "mainListingOptimisticLockMesssage");
		JOptionPane.showMessageDialog(xac, currMessage, getFatalErrorTitle(), JOptionPane.ERROR_MESSAGE);
	}

	public void showListDeletedMsg() {
		final String currMessage = getErrorResource(XhibitBundles.Listings, "mainListingListDeletedMesssage");
		JOptionPane.showMessageDialog(xac, currMessage, getFatalErrorTitle(), JOptionPane.ERROR_MESSAGE);
	}
	
    private static String getErrorResource(String bundle, String resourceKey) {
        return XHIBITConstant.getResource(bundle, resourceKey);
    }
	
    private static String getFatalErrorTitle() {
            return XHIBITConstant.getResource(XhibitBundles.XhibitConstant, "exception.fatalerror.title");
    }
    
	/**
	 * Common create method used by sub-classes to create a new list
	 * 
	 * @param startDate
	 * @param endDate
	 */
	private void createListBasicValue(Calendar startDate, Calendar endDate) {
		// Get ref listing value for the list type to get its id
		RefListingDataBasicValue refListType = ListingDropdownPopulation.getListType(listType);

		// Create new list basic value and populate common fields
		listBasicValue = new ListBasicValue();
		listBasicValue.setListTypeId(refListType.getRefListingDataId());
		listBasicValue.setCourtId(XhibitSingleton.getInstance().getCourtId());
		listBasicValue.setDraftOrFinal(ListBasicValue.DraftOrFinal.DRAFT);
		listBasicValue.setListNumber(1);
		listBasicValue.setListStartDate(startDate.getTime());
		listBasicValue.setListEndDate(endDate.getTime());
		listBasicValue.setDirty(true);
	}
	
	/**
	 * Reset the list number which indicates new version of list
	 */
	private void resetListBasicValueListNumber() {
		listBasicValue.setListNumber(1);
		listBasicValue.setDirty(true);
	}
	
	/**
	 * Increment the list number which indicates new version of list
	 */
	private void increaseListBasicValueListNumber() {
		listBasicValue.setListNumber(listBasicValue.getListNumber() + 1);
		listBasicValue.setDirty(true);
	}

	/**
	 * Reset the publish status back to its initial save status
	 */
	private void resetListBasicValuePublished() {
		listBasicValue.setPublishStatus(null);
		listBasicValue.setDirty(true);
	}
	
	/**
	 * Create a list value with the list and dirty sittings on list and cases on list.
	 * 
	 * @return
	 */
	private ListValue createDirtyListValue() {
		List<SittingOnListBasicValue> sittingsOnList = new ArrayList<SittingOnListBasicValue>();
		List<CaseOnListBasicValue> casesOnList = new ArrayList<CaseOnListBasicValue>();
		List<DefOnCaseOnListBasicValue> defOnCasesOnList = new ArrayList<DefOnCaseOnListBasicValue>();

		// Find all the dirty sittings on list to save
		for (SittingOnListComplexValue value : allSittingOnListComplexValues.values()) {
			if (value.isDirty()) {
				sittingsOnList.add(value);
			}
		}
		
		// Find all the dirty cases on list and any def on cases on lists to save
		for (CaseOnListComplexValue value : allCaseOnListComplexValues.values()) {
			if (value.isDirty()) {
				casesOnList.add(value);
			}
			
			for (DefOnCaseOnListBasicValue doc : value.getDefOnCaseOnLists()) {
				if (doc.isDirty()) {
					defOnCasesOnList.add(doc);
				}
			}
		}
		
		// Create list value with values to save
		return new ListValue(listBasicValue, sittingsOnList, casesOnList, defOnCasesOnList);
	}
	
	/**
	 * Add a previously created list to model.
	 * 
	 * @param list
	 */
	private void addListBasicValue(ListBasicValue list) {
		listBasicValue = list;
	}

	/**
	 * Update list basic value after save.
	 * 
	 * @param listId
	 * @param version
	 */
	private void updateListBasicValue(Integer listId, Integer version) {
		// If new list, list id will be null
		listBasicValue.setId(listId);
		listBasicValue.setListId(listId);
		listBasicValue.setVersion(version);
		listBasicValue.setDirty(false);
	}

	/**
	 * Create a new sitting on list and add to model.
	 * 
	 * @param courtRoom
	 * @param sittingNumber
	 * @param dateListed
	 * @return
	 */
	private SittingOnListComplexValue createSittingOnListComplexValue(XhbCourtRoomBasicValue courtRoom, Integer sittingNumber, Date dateListed) {
		// Create new sitting on list complex value which requires call to get next id
		SittingOnListComplexValue sittingOnList = new SittingOnListComplexValue();
		sittingOnList.setId(XhibitDelegateHelper.getListingsDelegate().getNextSittingOnListId());
		sittingOnList.setSittingOnListId(sittingOnList.getId());
		sittingOnList.setCourtSiteId(courtRoom.getCourtSiteId());
		sittingOnList.setCourtRoomId(courtRoom.getCourtRoomId());
		sittingOnList.setSittingNumber(sittingNumber);
		sittingOnList.setTimeListedDate(dateListed);
		sittingOnList.setDirty(true);
		
		// Add sitting on list to the model
		addSittingOnListComplexValue(sittingOnList);
		
		return sittingOnList;
	}
	
	/**
	 * Add a previously created sitting on list to model.
	 * 
	 * @param sittingOnList
	 */
	private void addSittingOnListComplexValue(SittingOnListComplexValue sittingOnList) {
		// Add the sitting on list to map using its id as key
		allSittingOnListComplexValues.put(sittingOnList.getSittingOnListId(), sittingOnList);
	}
	
	/**
	 * Move a sitting on list.
	 * 
	 * @param sittingOnListId
	 * @param courtRoom
	 * @param sittingNumber
	 * @param dateListed
	 */
	private void moveSittingOnListComplexValue(Integer sittingOnListId, XhbCourtRoomBasicValue courtRoom, Integer sittingNumber, Date dateListed) {
		// Get the sitting on list from model
		SittingOnListComplexValue sittingOnList = getSittingOnList(sittingOnListId);

		// Update sitting for new court room, sitting number and date
		sittingOnList.setCourtSiteId(courtRoom.getCourtSiteId());
		sittingOnList.setCourtRoomId(courtRoom.getCourtRoomId());
		sittingOnList.setSittingNumber(sittingNumber);
		sittingOnList.setTimeListedDate(dateListed);
		sittingOnList.setDirty(true);
	}
	
	/**
	 * Delete a sitting on list.
	 * 
	 * @param sittingOnListId
	 */
	private void deleteSittingOnListComplexValue(Integer sittingOnListId) {
		// Get the sitting on list from model
		SittingOnListComplexValue sittingOnList = getSittingOnList(sittingOnListId);

		// If sitting has never been saved to database, just remove from model
		if (sittingOnList.getVersion().equals(-1)) {
			allSittingOnListComplexValues.remove(sittingOnListId);
		}
		// Else sitting has been saved to database, so mark it as obsolete
		else {
			sittingOnList.setObsInd(YES);
			sittingOnList.setDirty(true);
		}
	}
	
	/**
	 * Create a new case on list and add to model.
	 * 
	 * @param defaults
	 * @param caze
	 * @param caseListingEntry
	 * @param directionsForCase
	 * @param hearingType
	 * @param sequenceNumber
	 * @return
	 */
	private CaseOnListComplexValue createCaseOnListComplexValue(CaseOnListBasicValue defaults, CaseBasicValue caze, CaseListingEntryBasicValue caseListingEntry,
			 									DirectionsForCaseBasicValue directionsForCase, RefHearingTypeBasicValue hearingType, Integer sequenceNumber) {
		// Create new case on list complex value which requires call to get next id
		CaseOnListComplexValue caseOnList = new CaseOnListComplexValue();
		caseOnList.setId(XhibitDelegateHelper.getListingsDelegate().getNextCaseOnListId());
		caseOnList.setCaseOnListId(caseOnList.getId());
		caseOnList.setCaseDiaryFixtureId(defaults.getCaseDiaryFixtureId());
		caseOnList.setParentCaseOnListId(defaults.getParentCaseOnListId());
		caseOnList.setListNotePredefinedId(defaults.getListNotePredefinedId());
		caseOnList.setListNoteText(defaults.getListNoteText());
		caseOnList.setCaseId(caze.getCaseId());
		caseOnList.setCase(caze);
		caseOnList.setCaseListingEntry(caseListingEntry);
		caseOnList.setDirectionsForCase(directionsForCase);
		caseOnList.setSeqNo(sequenceNumber);
		caseOnList.setDirty(true);

		// Hearing type could be initially null when dropping an 'Other Case' if it has
		// no default hearing type, but user will subsequently be forced to enter one
		if (hearingType != null) {
			caseOnList.setHearingTypeId(hearingType.getId());
			caseOnList.setHearingType(hearingType);
		}
		
		// Add case on list to the model
		addCaseOnListComplexValue(caseOnList);

		return caseOnList;
	}
	
	/**
	 * Add a previously created case on list to model.
	 * 
	 * @param caseOnList
	 */
	private void addCaseOnListComplexValue(CaseOnListComplexValue caseOnList) {
		// Add the case on list to map using its id as key
		allCaseOnListComplexValues.put(caseOnList.getCaseOnListId(), caseOnList);
		
		// If cases on list do not yet exist for the case id, create list for case id
		if (!caseOnListIdsCase.containsKey(caseOnList.getCaseId())) {
			caseOnListIdsCase.put(caseOnList.getCaseId(), new ArrayList<Integer>());
		}
		
		// Add the case on list id to the list for case id
		caseOnListIdsCase.get(caseOnList.getCaseId()).add(caseOnList.getCaseOnListId());
	}
	
	/**
	 * Move a case on list.
	 * 
	 * @param caseOnListId
	 * @param sequenceNumber
	 */
	private void moveCaseOnListComplexValue(Integer caseOnListId, Integer sequenceNumber) {
		// Get the case on list from model
		CaseOnListComplexValue caseOnList = getCaseOnList(caseOnListId);

		// Update sequence and clear fields used to link case to parent; sub-classes will re-populate relevant fields
		caseOnList.clearTimeListedDate();
		caseOnList.setCourtSiteId(null);
		caseOnList.setCourtRoomId(null);
		caseOnList.setSittingOnListId(null);
		caseOnList.setFloaterCase(null);
		caseOnList.setReserved(null);
		caseOnList.setSeqNo(sequenceNumber);
		caseOnList.setDirty(true);
	}

	/**
	 * Delete a case on list.
	 * 
	 * @param caseOnListId
	 */
	private void deleteCaseOnListComplexValue(Integer caseOnListId) {
		// Get the case on list from model
		CaseOnListComplexValue caseOnList = getCaseOnList(caseOnListId);

		// If case on list has never been saved to database, just remove from model
		if (caseOnList.getVersion().equals(-1)) {
			allCaseOnListComplexValues.remove(caseOnListId);
		}
		// Else case on list has been saved to database, so mark it as obsolete
		else {
			// Mark case on list as obsolete
			caseOnList.setObsInd(YES);
			caseOnList.setDirty(true);
			
			// Delete all def on case on list
			deleteDefOnCaseOnListBasicValues(caseOnListId);
		}
		
		// Remove entry from case on list ids for case id
		caseOnListIdsCase.get(caseOnList.getCaseId()).remove(caseOnListId);
	}

	/**
	 * Returns true if there is a case on list, which is not
	 * the case on list for the supplied case on list id.
	 * 
	 * @param caseId
	 * @param caseOnListId
	 * @return
	 */
	private boolean isCaseIdOnCaseOnListComplexValues(Integer caseId, Integer caseOnListId) {
		boolean found = false;
		
		// If there are any case on list values for the case which are not the
		// case on list identified by the supplied case on list id, then listed
		for (CaseOnListComplexValue caseOnList : getCaseOnListComplexValues(caseId)) {
			if (!caseOnList.getCaseOnListId().equals(caseOnListId)) {
				found = true;
				break;
			}
		}
		
		return found;
	}

	/**
	 * Returns true if there is a case on list for the case on the particular date.
	 * 
	 * @param caseId
	 * @param dateListed
	 * @return
	 */
	private boolean isDateOnCaseOnListComplexValues(Integer caseId, Date dateListed) {
		boolean found = false;
		
		// Cycle through all the case on list values from model for case and if
		// any of them are on the particular date, then currently being listed
		for (CaseOnListComplexValue caseOnList : getCaseOnListComplexValues(caseId)) {
			if (isDaySame(dateListed, caseOnList.getTimeListedDate())) {
				found = true;
				break;
			}
		}
		
		return found;
	}

	/**
	 * Returns true if there is a case on list for the case
	 * which was created from the particular fixture.
	 * 
	 * @param caseId
	 * @param caseDiaryFixtureId
	 * @return
	 */
	private boolean isFixtureOnCaseOnListComplexValues(Integer caseId, Integer caseDiaryFixtureId) {
		boolean found = false;
		
		// Cycle through all the case on list values from model for case and if the
		// case diary fixture id is on any of them, then currently being listed
		for (CaseOnListComplexValue caseOnList : getCaseOnListComplexValues(caseId)) {
			if (caseDiaryFixtureId.equals(caseOnList.getCaseDiaryFixtureId())) {
				found = true;
				break;
			}
		}
		
		return found;
	}

	/**
	 * Returns true if there is a case on list for the case
	 * which was created from the particular listing on a
	 * previous firm/warned list.
	 * 
	 * @param caseId
	 * @param parentCaseOnListId
	 * @return
	 */
	private boolean isParentOnCaseOnListComplexValues(Integer caseId, Integer parentCaseOnListId) {
		boolean found = false;
		
		// Cycle through all the case on list values from model for case and if the
		// parent case on list id is on any of them, then currently being listed
		for (CaseOnListComplexValue caseOnList : getCaseOnListComplexValues(caseId)) {
			if (parentCaseOnListId.equals(caseOnList.getParentCaseOnListId())) {
				found = true;
				break;
			}
		}
		
		return found;
	}

	/**
	 * Get the case on lists for the case id.
	 * 
	 * @param caseId
	 * @return
	 */
	private Collection<CaseOnListComplexValue> getCaseOnListComplexValues(Integer caseId) {
		List<CaseOnListComplexValue> casesOnLists = new ArrayList<CaseOnListComplexValue>();

		// Cases on list might not yet exist for the case id, so first check
		// existence of cases on list and only then get their basic values
		if (caseOnListIdsCase.containsKey(caseId)) {
			for (Integer caseOnListId : caseOnListIdsCase.get(caseId)) {
				casesOnLists.add(getCaseOnList(caseOnListId));
			}
		}
		
		return casesOnLists;
	}

	/**
	 * Create a new def on case on list and add to model.
	 * 
	 * @param caseOnListId
	 * @param defendantOnCaseId
	 * @param courtRoomList
	 * @return
	 */
	private DefOnCaseOnListBasicValue createDefOnCaseOnListBasicValue(Integer caseOnListId, Integer defendantOnCaseId, String courtRoomList) {
		// Get the case on list from model
		CaseOnListComplexValue caseOnList = getCaseOnList(caseOnListId);

		// Defendant could already be on the case on list if deleted but not yet
		// saved so check for an existing object for this particular defendant
		DefOnCaseOnListBasicValue defOnCaseOnList =
				getDefOnCaseOnListBasicValue(caseOnList.getDefOnCaseOnLists(), defendantOnCaseId);
		
		// Create new def on case on list basic value if one does not exist
		if (defOnCaseOnList == null) {
			// Create the value and populate all the foreign keys
			defOnCaseOnList = new DefOnCaseOnListBasicValue();
			defOnCaseOnList.setCaseId(caseOnList.getCaseId());
			defOnCaseOnList.setCaseOnListId(caseOnList.getCaseOnListId());
			defOnCaseOnList.setDefendantOnCaseId(defendantOnCaseId);
			defOnCaseOnList.setIsCourtRoomListEntry(courtRoomList);
			
			// Add the new value to the case on list collection
			caseOnList.getDefOnCaseOnLists().add(defOnCaseOnList);
		}
		else {
			// Ensure not deleted in case previously marked as obsolete
			defOnCaseOnList.setObsInd(null);
			// Update Court Room List value
			defOnCaseOnList.setIsCourtRoomListEntry(courtRoomList);
		}
		
		// Mark def on case on list dirty and also parent case on list to
		// ensure after the save that the id and version are retrieved 
		defOnCaseOnList.setDirty(true);
		caseOnList.setDirty(true);
		
		return defOnCaseOnList;
	}
	
	/**
	 * Delete a def on case on list.
	 * 
	 * @param caseOnListId
	 * @param defendantOnCaseId
	 */
	private void deleteDefOnCaseOnListBasicValue(Integer caseOnListId, Integer defendantOnCaseId) {
		// Get the case on list from model
		CaseOnListComplexValue caseOnList = getCaseOnList(caseOnListId);

		// Get the def on case on list from the case on list
		DefOnCaseOnListBasicValue defOnCaseOnList =
				getDefOnCaseOnListBasicValue(caseOnList.getDefOnCaseOnLists(), defendantOnCaseId);

		// If def on case on list has never been saved to database, just remove from model
		if (defOnCaseOnList.getVersion().equals(-1)) {
			caseOnList.getDefOnCaseOnLists().remove(defOnCaseOnList);
		}
		// Else def on case on list has been saved to database, so mark it as obsolete
		// and dirty, as well as marking its parent case on list as dirty, to ensure
		// that after the save that the id and version are retrieved.  Also update the 
		// def on case on list court room list flag
		else {
			defOnCaseOnList.setIsCourtRoomListEntry(NO);
			defOnCaseOnList.setObsInd(YES);
			defOnCaseOnList.setDirty(true);
			caseOnList.setDirty(true);
		}
	}

	/**
	 * Delete all def on case on list for a case on list.
	 * 
	 * @param caseOnListId
	 */
	private void deleteDefOnCaseOnListBasicValues(Integer caseOnListId) {
		// Get the case on list from model
		CaseOnListComplexValue caseOnList = getCaseOnList(caseOnListId);

		// Delete each def on case on list on the case on list. This must be done by
		// cycling through a new list, rather than directly through the collection as
		// when an item is deleted, the collection on the case on list is changed
		List<DefOnCaseOnListBasicValue> defOnCaseOnLists = new ArrayList<DefOnCaseOnListBasicValue>(caseOnList.getDefOnCaseOnLists());
		for (DefOnCaseOnListBasicValue defOnCaseOnList : defOnCaseOnLists) {
			deleteDefOnCaseOnListBasicValue(caseOnListId, defOnCaseOnList.getDefendantOnCaseId());
		}
	}
	
	/**
	 * Update a def on case on list with the Court Room List indicator.
	 * 
	 * @param caseOnListId
	 * @param defendantOnCaseId
	 * @param courtRoomList
	 * @return
	 */
	private void updateDefOnCaseOnListBasicValue(Integer caseOnListId, Integer defendantOnCaseId, String courtRoomList) {
		// Get the case on list from model
		CaseOnListComplexValue caseOnList = getCaseOnList(caseOnListId);

		// Defendant could already be on the case on list if deleted but not yet
		// saved so check for an existing object for this particular defendant
		DefOnCaseOnListBasicValue defOnCaseOnList =
				getDefOnCaseOnListBasicValue(caseOnList.getDefOnCaseOnLists(), defendantOnCaseId);
		
		// Providing the value exists, update it
		if (defOnCaseOnList != null) {
			// Update Court Room List value and set dirty flag
			defOnCaseOnList.setIsCourtRoomListEntry(courtRoomList);
			defOnCaseOnList.setDirty(true);
		}
	}

	/**
	 * Returns true if there is a def on case on list on the
	 * case on list and it has not been marked to be deleted.
	 * 
	 * @param caseOnListId
	 * @param defendantOnCaseId
	 * @return
	 */
	protected boolean isDefendantOnCaseOnListComplexValue(Integer caseOnListId, Integer defendantOnCaseId) {
		// Get the case on list from model
		CaseOnListComplexValue caseOnList = getCaseOnList(caseOnListId);

		// Get the def on case on list from the case on list
		DefOnCaseOnListBasicValue defOnCaseOnList =
				getDefOnCaseOnListBasicValue(caseOnList.getDefOnCaseOnLists(), defendantOnCaseId);

		// Defendant is on the case on list if a def on case on list object
		// was found on the list and the object is not set to be deleted
		return (defOnCaseOnList != null && !YES.equals(defOnCaseOnList.getObsInd()));
	}

	/**
	 * Returns true if there is a case on list for the date
	 * and case which has the particular defendant.
	 * 
	 * @param dateListed
	 * @param caseId
	 * @param defendantOnCaseId
	 * @return
	 */
	protected boolean isDefendantOnCaseOnListComplexValues(Date dateListed, Integer caseId, Integer defendantOnCaseId) {
		boolean found = false;
		
		// Cycle through all the case on list values from model for case and if the
		// defendant is on any of them for the date, which are not set to be deleted,
		// then the defendant is currently being listed for the case
		for (CaseOnListComplexValue caseOnList : getCaseOnListComplexValues(caseId)) {
			if (isDefendantOnCaseOnListComplexValue(caseOnList.getCaseOnListId(), defendantOnCaseId)
				&& isDaySame(dateListed, caseOnList.getTimeListedDate())) {
				found = true;
				break;
			}
		}
		
		return found;
	}

	/**
	 * Returns true if there is a case on list, which is not
	 * the case on list for the supplied case on list id, for
	 * the date and case which has the particular defendant.
	 * 
	 * @param dateListed
	 * @param caseId
	 * @param defendantOnCaseId
	 * @param caseOnListId
	 * @return
	 */
	protected boolean isDefendantOnCaseOnListComplexValues(Date dateListed, Integer caseId, Integer defendantOnCaseId, Integer caseOnListId) {
		boolean found = false;
		
		// Cycle through all the case on list values from model for case which is
		// not the case on id identified by the supplied case on list id and if the
		// defendant is on any of them for the date, which are not set to be deleted,
		// then the defendant is currently being listed elsewhere for the case
		for (CaseOnListComplexValue caseOnList : getCaseOnListComplexValues(caseId)) {
			if (!caseOnList.getCaseOnListId().equals(caseOnListId)
				&& isDefendantOnCaseOnListComplexValue(caseOnList.getCaseOnListId(), defendantOnCaseId)
				&& isDaySame(dateListed, caseOnList.getTimeListedDate())) {
				found = true;
				break;
			}
		}
		
		return found;
	}

	/**
	 * Returns true if there is a case on list, which is not
	 * the case on list for the supplied case on list id, for
	 * the case which has the particular defendant.
	 * 
	 * @param caseId
	 * @param defendantOnCaseId
	 * @param caseOnListId
	 * @return
	 */
	protected boolean isDefendantOnCaseOnListComplexValues(Integer caseId, Integer defendantOnCaseId, Integer caseOnListId) {
		boolean found = false;
		
		// Cycle through all the case on list values from model for case which is
		// not the case on id identified by the supplied case on list id and if the
		// defendant is on any of them, which are not set to be deleted, then the
		// defendant is currently being listed elsewhere for the case
		for (CaseOnListComplexValue caseOnList : getCaseOnListComplexValues(caseId)) {
			if (!caseOnList.getCaseOnListId().equals(caseOnListId)
				&& isDefendantOnCaseOnListComplexValue(caseOnList.getCaseOnListId(), defendantOnCaseId)) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	/**
	 * Returns the value of the isCourtRoomListEntry for a DefOnCaseOnListBasicValue
	 * 
	 * @param caseOnListId
	 * @param defendantOnCaseId
	 * @return
	 */
	protected String isDefendantOnCaseOnListOnCourtList(Integer caseOnListId, Integer defendantOnCaseId) {
		// Get the case on list from model
		CaseOnListComplexValue caseOnList = getCaseOnList(caseOnListId);

		// Get the def on case on list from the case on list
		DefOnCaseOnListBasicValue defOnCaseOnList =
				getDefOnCaseOnListBasicValue(caseOnList.getDefOnCaseOnLists(), defendantOnCaseId);

		// Defendant is on the case on list if a def on case on list object
		// was found on the list and the object is not set to be deleted
		return defOnCaseOnList.getIsCourtRoomListEntry();
	}

	/**
	 * Get the def on case on list from the collection by its defendant on case id.
	 * 
	 * @param defOnCaseOnLists
	 * @param defendantOnCaseId
	 * @return
	 */
	private DefOnCaseOnListBasicValue getDefOnCaseOnListBasicValue(
										Collection<DefOnCaseOnListBasicValue> defOnCaseOnLists,
										Integer defendantOnCaseId) {

		// Get the def on case on list from the supplied list
		DefOnCaseOnListBasicValue defOnCaseOnList = null;
		for (DefOnCaseOnListBasicValue value : defOnCaseOnLists) {
			if (defendantOnCaseId.equals(value.getDefendantOnCaseId())) {
				defOnCaseOnList = value;
				break;
			}
		}
		
		return defOnCaseOnList;
	}
	
	/**
	 * Return true if the two dates are on the same day,
	 * i.e. day, month, year are equal regardless of time.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	protected boolean isDaySame(Date date1, Date date2) {
		// If either date is null, it is impossible for the days to be identical
		if (date1 == null || date2 == null) {
			return false;
		}
		
		// Convert to calendar and compare the date components of the date
		Calendar cal1 = DateTimeUtilities.convertToCalendar(date1);
		Calendar cal2 = DateTimeUtilities.convertToCalendar(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * Return the date component of a calendar in YYYYMMDD string format.
	 * 
	 * @param date
	 * @return date in YYYYMMDD format
	 */
	protected String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	/**
	 * Return the date component and id in the format used for map keys.
	 * 
	 * @param date
	 * @param id
	 * @return date and id in YYYYMMDD-123 format
	 */
	protected String formatKey(Date date, Integer id) {
		return formatDate(date) + "-" + id;
	}
	
	/**
	 * Return the date component and id in the format used for map keys.
	 * 
	 * @param cal
	 * @param id
	 * @return date and id in YYYYMMDD-123 format
	 */
	protected String formatKey(Calendar cal, Integer id) {
		return formatKey(cal.getTime(), id);
	}
	
	/**
	 * Adjust the sitting numbers on the supplied sittings on list for
	 * any gaps in the sitting numbers after a delete.
	 * 
	 * @param sittingsOnList
	 */
	protected void renumberSittingsOnList(List<SittingOnListComplexValue> sittingsOnList) {
		// Sort the sittings on list by their current sitting numbers
		sortSittingsOnList(sittingsOnList);
		
		// Adjust the sittings on list for any gaps in sitting numbers
		int number = 1;
		for (SittingOnListComplexValue sittingOnList : sittingsOnList) {
			if (!sittingOnList.getSittingNumber().equals(number)) {
				sittingOnList.setSittingNumber(number);
				sittingOnList.setDirty(true);
			}
			number++;
		}
	}
	
	/**
	 * Adjust the sitting numbers on the supplied sittings on list
	 * relative to the sitting on list being added to the list.
	 * 
	 * @param sittingsOnList
	 * @param sittingOnListId
	 */
	protected void renumberSittingsOnList(List<SittingOnListComplexValue> sittingsOnList, Integer sittingOnListId) {
		// Get the sitting number of the sitting on list being added
		int number = getSittingOnList(sittingOnListId).getSittingNumber();
		
		// Adjust the sittings on list for the new sitting on list
		for (SittingOnListComplexValue sittingOnList : sittingsOnList) {
			if (!sittingOnList.getSittingOnListId().equals(sittingOnListId)
					&& sittingOnList.getSittingNumber() >= number) {
				sittingOnList.setSittingNumber(sittingOnList.getSittingNumber() + 1);
				sittingOnList.setDirty(true);
			}
		}
	}
	
	/**
	 * Adjust the sequence numbers on the supplied cases on list for
	 * any gaps in the sequence numbers after a delete.
	 * 
	 * @param casesOnList
	 */
	protected void renumberCasesOnList(List<CaseOnListComplexValue> casesOnList) {
		// Sort the cases on list by their current sequence numbers
		Collections.sort(casesOnList, new CaseOnListComparator());
		
		// Adjust the cases on list for any gaps in sequence numbers
		int number = 1;
		for (CaseOnListComplexValue caseOnList : casesOnList) {
			if (!caseOnList.getSeqNo().equals(number)) {
				caseOnList.setSeqNo(number);
				caseOnList.setDirty(true);
			}
			number++;
		}
	}
	
	/**
	 * Adjust the sequence numbers on the supplied cases on list
	 * relative to the case on list being added to the list.
	 * 
	 * @param casesOnList
	 * @param caseOnListId
	 */
	protected void renumberCasesOnList(List<CaseOnListComplexValue> casesOnList, Integer caseOnListId) {
		// Get the sequence number of the case on list being added
		int number = getCaseOnList(caseOnListId).getSeqNo();
		
		// Adjust the cases on list for the new case on list
		for (CaseOnListComplexValue caseOnList : casesOnList) {
			if (!caseOnList.getCaseOnListId().equals(caseOnListId)
					&& caseOnList.getSeqNo() >= number) {
				caseOnList.setSeqNo(caseOnList.getSeqNo() + 1);
				caseOnList.setDirty(true);
			}
		}
	}
	
	/**
	 * Sort sittings on list by their sitting number.
	 * 
	 * @param sittingsOnList
	 */
	protected void sortSittingsOnList(List<SittingOnListComplexValue> sittingsOnList) {
		Collections.sort(sittingsOnList, new SittingOnListComparator());
	}
	
	/**
	 * Sort cases on list by sequence number.
	 * 
	 * @param casesOnList
	 */
	protected void sortCasesOnList(List<CaseOnListComplexValue> casesOnList) {
		Collections.sort(casesOnList, new CaseOnListComparator());
	}
	
	/**
	 * Comparator to order sittings on list by their sitting number.
	 */
	private class SittingOnListComparator implements Comparator<SittingOnListComplexValue> {
		@Override
		public int compare(SittingOnListComplexValue sitting1, SittingOnListComplexValue sitting2) {
			return sitting1.getSittingNumber().compareTo(sitting2.getSittingNumber());
		}
	}
	
	/**
	 * Comparator to order cases on list by their sequence number.
	 */
	private class CaseOnListComparator implements Comparator<CaseOnListComplexValue> {
		@Override
		public int compare(CaseOnListComplexValue caseOnList1, CaseOnListComplexValue caseOnList2) {
			return caseOnList1.getSeqNo().compareTo(caseOnList2.getSeqNo());
		}
	}
}
