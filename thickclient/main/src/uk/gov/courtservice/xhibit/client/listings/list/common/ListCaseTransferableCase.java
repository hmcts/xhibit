package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Class used to move cases from the tables to the tree.
 * 
 * @author uphillj
 *
 */
public class ListCaseTransferableCase extends TransferableCase {

	private ListCaseTableRow listCaseTableRow;

	public ListCaseTransferableCase(TreeNodeFactory treeNodeFactory, ListCaseTableRow listCaseTableRow) {
		super(treeNodeFactory);
		this.listCaseTableRow = listCaseTableRow;
	}

	@Override
	protected DefaultMutableTreeNode getTransferTarget() throws CSRecoverableException {
		// Retrieve the case basic value
		final Integer caseId = listCaseTableRow.getCaseId();
		final CaseControllerBeanBusinessDelegate caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		final CaseComplexValue caseValue = caseDelegate.getCaseComplexValueByPrimaryKey(caseId);

		// Return a new tree node with a case on list populated with values required to
		// add a case to a list (e.g. hearing type and case) but without a primary key  
		// to indicate the case is being added to the tree rather than moving within
		final CaseOnListComplexValue caseOnList = new CaseOnListComplexValue();
		caseOnList.setCaseDiaryFixtureId(listCaseTableRow.getCaseDiaryFixtureId());
		caseOnList.setParentCaseOnListId(listCaseTableRow.getParentCaseOnListId());
		caseOnList.setListNotePredefinedId(listCaseTableRow.getListNotePredefinedId());
		caseOnList.setListNoteText(listCaseTableRow.getListNoteText());
		caseOnList.setHearingType(getHearingType(listCaseTableRow.getHearingTypeId()));
		caseOnList.setDefOnCaseOnLists(createDefOnCaseOnLists(listCaseTableRow.getDefendantOnCaseIds()));
		caseOnList.setCaseListingEntry(caseValue.getCaseListingEntry());
		caseOnList.setDirectionsForCase(caseValue.getDirectionsForCase());
		caseOnList.setCaseId(caseValue.getCaseId());
		caseOnList.setCase(caseValue);
		return getTreeNodeFactory().createCase(caseOnList);
	}

	@Override
	protected Object getTransferSource() {
		return listCaseTableRow;
	}
	
	/**
	 * Get the hearing type basic value for the hearing type
	 * 
	 * @param hearingTypeId
	 * @return
	 */
	protected RefHearingTypeBasicValue getHearingType(Integer hearingTypeId) {
		RefHearingTypeBasicValue refHearingType = null;
        if (hearingTypeId != null) {
			try {
				// Get the ref hearing type for its primary key
				refHearingType = (RefHearingTypeBasicValue)
									(XhibitDelegateHelper.getBizRefDelegate().findHearingTypeById(hearingTypeId));				
			} catch (BisRefControllerException e) {
				XHIBITConstant.handleError(e);
			}
        }
		return refHearingType;
	}

	/**
	 * Create a list of defendant on case on list for the required defendants,
	 * populated with just the defendant on case id, which will be used to add
	 * the defendants to the list model.
	 * 
	 * @param defendantOnCaseIds
	 * @return
	 */
	protected Collection<DefOnCaseOnListBasicValue> createDefOnCaseOnLists(Collection<Integer> defendantOnCaseIds) {
		List<DefOnCaseOnListBasicValue> defOnCaseOnLists = new ArrayList<DefOnCaseOnListBasicValue>();
		
		// Cycle through all the defendant on case ids and create an equivalent defOnCaseOnList
		for (Integer defendantOnCaseId : defendantOnCaseIds) {
			DefOnCaseOnListBasicValue defOnCaseOnListValue = new DefOnCaseOnListBasicValue();
			defOnCaseOnListValue.setDefendantOnCaseId(defendantOnCaseId);
			defOnCaseOnLists.add(defOnCaseOnListValue);
		}
		
		return defOnCaseOnLists;
	}
}
