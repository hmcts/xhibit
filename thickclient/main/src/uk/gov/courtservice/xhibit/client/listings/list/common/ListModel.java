package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * Interface for daily/warned/firm list models.
 * 
 * @author uphillj
 *
 */
public interface ListModel {
	
	/**
	 * Get Xhibit application controller.
	 * 
	 * @return
	 */
	XhibitApplicationController getXac();

	/**
	 * Get the tree node factory.
	 * 
	 * @return
	 */
	TreeNodeFactory getTreeNodeFactory();
	
	/**
	 * Set the tree node factory.
	 * 
	 * @param factory
	 */
	void setTreeNodeFactory(TreeNodeFactory factory);

	/**
	 * Get the list type.
	 * 
	 * @return
	 */
	ListTypeEnum getListType();
	
	/**
	 * Get list basic value
	 * 
	 * @return list basic value
	 */
	ListBasicValue getList();
	
	/**
	 * Get list start date.
	 * 
	 * @return list start date
	 */
	Calendar getListStartDate();

	/**
	 * Get list end date.
	 * 
	 * @return list end date
	 */
	Calendar getListEndDate();

	/**
	 * Set list end date.
	 */
	void setListEndDate(Calendar date);

	/**
	 * Return true if list is new.
	 * 
	 * @return
	 */
	boolean isListNew();
	
	/**
	 * Return true if list is draft.
	 * 
	 * @return
	 */
	boolean isListDraft();

	/**
	 * Change list to be draft.
	 */
	void setListDraft();
	
	/**
	 * Return true if list is final.
	 * 
	 * @return
	 */
	boolean isListFinal();
	
	/**
	 * Change list to be final.
	 */
	void setListFinal();
	
	/**
	 * Return true if list has been published at least
	 * once since its status was set to draft or final.
	 * List number is reset when status changes and so
	 * it is not possible to check if published before.
	 * 
	 * @return
	 */
	boolean hasListPublished();
	
	/**
	 * Get the sittings on list.
	 * 
	 * @return sittings on list
	 */
	Collection<SittingOnListComplexValue> getSittingsOnList();
	
	/**
	 * Get the cases on list.
	 * 
	 * @return cases on list
	 */
	Collection<CaseOnListComplexValue> getCasesOnList();
	
	/**
	 * Get the cases on list with supplied hearing types.
	 * 
	 * @param hearingTypes
	 * @return trial cases on list
	 */
	Collection<CaseOnListComplexValue> getCasesOnList(Collection<String> hearingTypes);

	/**
	 * Load the list from the database.
	 * 
	 * @param listId
	 */
	void openList(Integer listId) throws CSRecoverableException;
	
	/**
	 * Save the list to the database.
	 * 
	 * @throws CSRecoverableException
	 */
	boolean saveList() throws CSRecoverableException;
	
	/**
	 * Set the list as published in the database.
	 */
	void publishList() throws CSRecoverableException;
	
	/**
	 * Return true if all the sittings and cases have been loaded.
	 * 
	 * @return
	 */
	boolean isListPopulated();
	
	/**
	 * Return true if the list has changes which have yet to be saved.
	 * 
	 * @return true if unsaved changes
	 */
	boolean hasUnsavedChanges();
	
	/**
	 * Create sitting under the specified court room.
	 *  
	 * @param courtRoom
	 * @param sittingNumber
	 * @param dateListed
	 * @return
	 */
	SittingOnListComplexValue createSitting(XhbCourtRoomBasicValue courtRoom, Integer sittingNumber, Date dateListed);

	/**
	 * Move sitting under the specified court room.
	 * 
	 * @param sittingOnList
	 * @param courtRoom
	 * @param sittingNumber
	 * @param dateListed
	 */
	void moveSitting(SittingOnListComplexValue sittingOnList, XhbCourtRoomBasicValue courtRoom, Integer sittingNumber, Date dateListed);
	
	/**
	 * Remove sitting from the model if never saved or mark as obsolete if previously saved.
	 * 
	 * @param sittingOnList
	 */
	void deleteSitting(SittingOnListComplexValue sittingOnList);
	
	/**
	 * Add the case to the specified parent folder.
	 * 
	 * @param defaults
	 * @param caze
	 * @param caseListingEntry
	 * @param directionsForCase
	 * @param hearingType
	 * @param parentType
	 * @param sequenceNumber
	 * @param parents
	 * @return
	 */
	CaseOnListComplexValue addCase(CaseOnListBasicValue defaults, CaseBasicValue caze, CaseListingEntryBasicValue caseListingEntry, DirectionsForCaseBasicValue directionsForCase,
									RefHearingTypeBasicValue hearingType, CaseOnListType parentType, Integer sequenceNumber, Object... parents);
	
	/**
	 * Move case under the specified parent folder
	 * 
	 * @param caseOnList
	 * @param parentType
	 * @param sequenceNumber
	 * @param parents
	 */
	void moveCase(CaseOnListComplexValue caseOnList, CaseOnListType parentType, Integer sequenceNumber, Object... parents);

	/**
	 * Remove the case from the model if never saved or mark as obsolete if previously saved.
	 * 
	 * @param caseOnList
	 */
	void removeCase(CaseOnListComplexValue caseOnList);

	/**
	 * Returns true if the case is listed on the date.
	 * 
	 * @param caseId
	 * @param dateListed
	 * @return
	 */
	boolean isCaseListed(Integer caseId, Date dateListed);

	/**
	 * Returns true if the case is listed on a different case on list to the one supplied.
	 * 
	 * @param caseId
	 * @param caseOnListId
	 * @return
	 */
	boolean isCaseListedElsewhere(Integer caseId, Integer caseOnListId);

	/**
	 * Returns true if the fixture for the case is listed.
	 * 
	 * @param caseId
	 * @param caseDiaryFixtureId
	 * @return
	 */
	boolean isFixtureListed(Integer caseId, Integer caseDiaryFixtureId);

	/**
	 * Returns true if the non-fixed case taken from a previous
	 * firm/warned list is listed in this list.
	 * 
	 * @param caseId
	 * @param parentCaseOnListId
	 * @return
	 */
	boolean isNonFixedListed(Integer caseId, Integer parentCaseOnListId);
	
	/**
	 * Add the defendant to the listed case.
	 * 
	 * @param caseOnList
	 * @param defendantOnCaseId
	 * @return
	 */
	DefOnCaseOnListBasicValue addDefendant(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId);
	
	/**
	 * Add the defendant to the listed case with a court room list indicator specified.
	 * 
	 * @param caseOnList
	 * @param defendantOnCaseId
	 * @param courtRoomList
	 * @return
	 */
	DefOnCaseOnListBasicValue addDefendant(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId, String courtRoomList);
	
	/**
	 * Remove the defendant from the listed case.
	 * 
	 * @param caseOnList
	 * @param defendantOnCaseId
	 */
	void removeDefendant(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId);
	
	/**
	 * Update the defendant on the listed case with the court room list indicator specified.
	 * 
	 * @param caseOnList
	 * @param defendantOnCaseId
	 * @param courtRoomList
	 * @return
	 */
	void updateDefendant(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId, String courtRoomList);

	/**
	 * Returns true if the defendant is on the case on list.
	 * 
	 * @param caseOnList
	 * @param defendantOnCaseId
	 * @return
	 */
	boolean isDefendantListed(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId);

	/**
	 * Returns true if the defendant is listed on the date.
	 * 
	 * @param dateListed
	 * @param caseId
	 * @param defendantOnCaseId
	 * @return
	 */
	boolean isDefendantListed(Date dateListed, Integer caseId, Integer defendantOnCaseId);

	/**
	 * Returns true if the defendant is on a different case on list to the one supplied.
	 * 
	 * @param caseId
	 * @param defendantOnCaseId
	 * @param caseOnListId
	 * @return
	 */
	boolean isDefendantListedElsewhere(Integer caseId, Integer defendantOnCaseId, Integer caseOnListId);

	/**
	 * Returns true if the defendant cannot be listed on the supplied case on list.
	 * 
	 * @param caseOnList
	 * @param defendantOnCaseId
	 * @return
	 */
	boolean isDefendantNotListable(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId);
	
	/**
	 * Returns Y/N/NULL if the defendant is on the court room list, for the supplied case on list.
	 * Method needs to differentiate between N and NULL
	 * 
	 * @param caseOnList
	 * @param defendantOnCaseId
	 * @return String
	 */
	String isDefendantOnCourtRoomList(CaseOnListComplexValue caseOnList, Integer defendantOnCaseId);

	/**
	 * Refresh elements of the list from the database  
	 */
	void refresh() throws CSRecoverableException;

	/**
	 * Show the listing screen version of the optimistic lock message  
	 */
	void showOptimisticLockMsg();
}
