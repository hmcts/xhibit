package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.table.TableModel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseFilterResultComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseListingFilterCriteria;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMaintain;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.list.common.caze.AddCaseDataModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.caze.AddCaseWizardDialog;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * A Other Cases panel.
 * Displays an Other Cases Panel 
 * @author groenm
 *
 */
public class OtherCasesPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String U_CASE_TYPE = "U";

	private JTable otherCasesTable;
	private RowSorter<? extends TableModel> otherCasesTableRowSorter;

	private JButton searchButton;
	private JButton selectButton;
	private JButton clearButton;
	private JButton addUCaseButton;
	private JButton addBCaseButton;
	
	private ListModel listModel;
	private ListCaseTableModel otherCasesListCaseTableModel;
	
	private boolean isPanelEnabled = true;
	
	public OtherCasesPanel(ListModel listModel) {
		super(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Other Cases"));
		this.setPreferredSize(new Dimension(300, 100));
		this.listModel = listModel; 
		jbInit();
	}
	
	private void jbInit() {
		
		// Create table
		JScrollPane scrollPane = new JScrollPane(getOtherCasesTable());
		this.add(scrollPane, BorderLayout.CENTER);
		
		// Create panel for the buttons
		JPanel buttons = new JPanel(new GridBagLayout());
		this.add(buttons, BorderLayout.SOUTH);
	
		// Create empty label in centre to force buttons to the right 
		buttons.add(getLeftButtonPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
	
		// Create buttons on the right of the panel
		GridBagConstraints gbc = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0.0, 0.0,
	 			GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				XHIBITConstant.nonContainerInsets, 0, 0);
		buttons.add(getSearchButton(), gbc);
		if (ListTypeEnum.Daily.equals(listModel.getListType())) {
			buttons.add(getAddUCaseButton(), gbc);
			buttons.add(getAddBCaseButton(), gbc);
		} else if (ListTypeEnum.Firm.equals(listModel.getListType())) {
			buttons.add(getAddUCaseButton(), gbc);
		}
	}
	
	private JPanel getLeftButtonPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		if (ListTypeEnum.Warned.equals(listModel.getListType())) {
			gbc.weightx = 0.01;
			panel.add(getSelectButton(), gbc);
			gbc.gridx++;
			gbc.weightx = 0.01;
			panel.add(getClearButton(), gbc);
			gbc.gridx++;
			gbc.weightx = 0.98;
		}
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(XHIBITConstant.getSpacer(), gbc);

		return panel;
	}	
	
	private JTable getOtherCasesTable() {
		if (otherCasesTable == null) {
			otherCasesListCaseTableModel = new ListCaseTableModel();
			otherCasesTable = XTableFactory.getInstance().createDefaultTable(otherCasesListCaseTableModel);
			TransferHandler transferHandler = new DefaultCaseTransferHandler(otherCasesListCaseTableModel, listModel.getTreeNodeFactory());
	
			TableUtils.setupDefaultsOnJTable(otherCasesTable);
			TableUtils.setupDragAndDropOnJTable(otherCasesTable, transferHandler);
			TableUtils.setupColumnHeaderToolTips(otherCasesTable, otherCasesListCaseTableModel.getColumnHeaderToolTips());
			TableUtils.setupColumnHorizontalAlignment(otherCasesTable, ListCaseTableModel.COL_GROUP_NO, SwingConstants.LEFT);
			TableUtils.setTableRowFilter(otherCasesTable, new DefaultTableModelRowFilter());
			otherCasesListCaseTableModel.setColumnWidths(otherCasesTable);
			otherCasesTableRowSorter = otherCasesTable.getRowSorter();
			
			final ListingCaseContextPopupMenuHelper helper = new ListingCaseContextPopupMenuHelper(listModel,
					otherCasesTable, otherCasesListCaseTableModel);
			helper.addCaseListingEntryPopupMenu();
			helper.addCaseSummaryPopupMenu();
		}
		return otherCasesTable;
	}
	
	private JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new JButton(new OtherCasesSearchAction());
		}
		return searchButton;
	}
    
	private void clearOtherCases() {
		otherCasesListCaseTableModel.removeAllCases();
		otherCasesListCaseTableModel.fireTableDataChanged();
	}

	private JButton getSelectButton() {
		if (selectButton == null) {
			selectButton = new JButton(new OtherCasesSelectAction());
		}
		return selectButton;
	}

	private JButton getClearButton() {
		if (clearButton == null) {
			clearButton = new JButton(new OtherCasesClearAction());
		}
		return clearButton;
	}

	private JButton getAddUCaseButton() {
		if (addUCaseButton == null) {
			addUCaseButton = new JButton(new OtherCasesAddUCaseAction());
		}
		return addUCaseButton;
	}
	
	private JButton getAddBCaseButton() {
		if (addBCaseButton == null) {
			addBCaseButton = new JButton(new OtherCasesAddBCaseAction());
		}
		return addBCaseButton;
	}
	
    private void enableButton(JButton button, boolean isEnabled) {
    	if (button != null) {
    		button.setEnabled(isEnabled);
    	}
    }

    private boolean isTablePopulated() {
    	return otherCasesListCaseTableModel != null && 
				otherCasesListCaseTableModel.getRowCount() == 0;
    }

    private void setButtonsEnabled() {
    	enableButton(selectButton,selectButton != null ? ((OtherCasesSelectAction) selectButton.getAction()).isEnabled() : false);
    	enableButton(clearButton,isPanelEnabled);
    	enableButton(searchButton,isPanelEnabled);
    	enableButton(addUCaseButton,isPanelEnabled);
    	enableButton(addBCaseButton,isPanelEnabled);
    }

    public void setPanelEnabled(boolean isEnabled) {
    	isPanelEnabled = isEnabled;
    	setButtonsEnabled();
    }

    private void setTableSortable(boolean isSortable) {
    	otherCasesTable.setRowSorter(isSortable ? otherCasesTableRowSorter : null);
    }

	/**
	 * Add the case returned from the search to the other cases model for display
	 * 
	 * @param caseComplexValue
	 */
	private void populateOtherCaseModel(final CaseComplexValue caseComplexValue, final CaseListingEntryComplexValue caseListingEntryComplexValue) {
		// get the list of defendant on case ids, which for an other case is all of them on the case
		List<Integer> defendantOnCaseIds = new ArrayList<Integer>();
		for (DefendantOnCaseBasicValue defOnCaseValue : caseComplexValue.getDefendantOnCases()) {
			defendantOnCaseIds.add(defOnCaseValue.getDefendantOnCaseId());
		}
		
		// get the default list notes if they exist as they are optional
		Integer listNotePredefinedId = null;
		String listNoteText = null;
		if (caseListingEntryComplexValue != null) {
			if (caseListingEntryComplexValue.getPreDefinedDiaryNoteEntry() != null) {
				listNotePredefinedId = caseListingEntryComplexValue.getPreDefinedDiaryNoteEntry().getDiaryNotePreDefinedId();
			}
			if (caseListingEntryComplexValue.getFreeTextDiaryNoteEntry() != null) {
				listNoteText = caseListingEntryComplexValue.getFreeTextDiaryNoteEntry().getDiaryNoteText();
			}
		}
		
		final ListCaseTableRow row = new OtherCasesTableRow();
		ListingUtils.populateListCaseTableRow(caseComplexValue,
				caseComplexValue.getDefaultHearingTypeBasicValue(), caseComplexValue.getDirectionsForCase(), defendantOnCaseIds,
				 listNotePredefinedId, listNoteText, row);
		
		otherCasesListCaseTableModel.addCase(row);
	}
	
	/**
	 * Retrieve the case objects for the case returned from search and then populate model.
	 * 
	 * @param caseId
	 * @throws CSRecoverableException
	 */
	private void retrieveCaseAndPopulateOtherCaseModel(final Integer caseId) throws CSRecoverableException {
		CaseListingFilterCriteria criteria = new CaseListingFilterCriteria(caseId); 
		@SuppressWarnings("unchecked")
		List<CaseFilterResultComplexValue> caseFilterResults = XhibitDelegateHelper.getListingsDelegate().getCasesByFilter(criteria);
		if (caseFilterResults != null && !caseFilterResults.isEmpty()) {
			for (CaseFilterResultComplexValue caseFilterResult : caseFilterResults) {
				// Populate the other cases model
				populateOtherCaseModel(caseFilterResult.getCaseComplexValue(), caseFilterResult.getCaseListingEntryComplexValue());
			}
		}
	}
	
	private class OtherCasesSearchAction extends XAction {

		private static final long serialVersionUID = 1L;

		public OtherCasesSearchAction() {
			populateFromBundle("MainListingOtherCasesSearch");
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			CaseMaintain controller;
			if (ListTypeEnum.Daily.equals(listModel.getListType())) {
				// All case types are allowed on a daily list
				controller = new CaseMaintain(listModel.getXac());
			} else if (ListTypeEnum.Firm.equals(listModel.getListType())) {
				// T, S, A and U case types are allowed on a firm list
				List<String> caseTypes = new ArrayList<String>(CaseType.CaseListingCaseTypes());
				caseTypes.add(U_CASE_TYPE);
				controller = new CaseMaintain(listModel.getXac(), caseTypes);
			} else {
				// T, S and A case types are allowed on a warned list
				controller = new CaseMaintain(listModel.getXac(), CaseType.CaseListingCaseTypes());
			}

			Integer caseId = controller.getCaseId();
			if (!caseId.equals(0)) {
				retrieveCaseAndPopulateOtherCaseModel(caseId);
			}
			setButtonsEnabled();
		}

	}

	private class OtherCasesSelectAction extends XAction {

		private static final long serialVersionUID = 1L;

		public OtherCasesSelectAction() {
			populateFromBundle("MainListingOtherCasesSelect");
		}

		public boolean isEnabled() {
			return isPanelEnabled && isTablePopulated();
		}
		
		public void xActionPerformed(ActionEvent ae) throws Exception {
			OtherCasesPanelFilterSelectionModel selectionModel = new OtherCasesPanelFilterSelectionModel(
					listModel.getXac());
			OtherCasesPanelFilterSelectionDialog otherCasesPanelFilterSelectionDialog = new OtherCasesPanelFilterSelectionDialog(
					listModel.getXac(), selectionModel);
			otherCasesPanelFilterSelectionDialog.setVisible(true);

			if (selectionModel.getCaseFilterResults() != null) {
				if ( selectionModel.getCaseFilterResults().size() > 0) {
					// Stop sorting on the columns
					setTableSortable(false);
					// Sort the results into the Order defined for the filter process
					Collections.sort(selectionModel.getCaseFilterResults(), CaseFilterResultComplexValue.SortByCaseType);
					// retrieve the cases to display
					for (CaseFilterResultComplexValue caseFilterResult : selectionModel.getCaseFilterResults()) {
						populateOtherCaseModel(caseFilterResult.getCaseComplexValue(), caseFilterResult.getCaseListingEntryComplexValue());
					}
				} else {
					// display a warning message stating no matches found
					XMessageBox.alert(listModel.getXac(), 
							OtherCasesPanelFilterSelectionPanel.getResource("Title"),  
							true,
							XMessageBox.ICONINFORMATION, 
							OtherCasesPanelFilterSelectionPanel.getResource("NoResults"),  
							XMessageBox.OK_ONLY,
							XMessageBox.DEFAULTOK);
				}
			}
			setButtonsEnabled();
		}
	}

	private class OtherCasesClearAction extends XAction {

		private static final long serialVersionUID = 1L;

		public OtherCasesClearAction() {
			populateFromBundle("MainListingOtherCasesClear");
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			clearOtherCases();
			setTableSortable(true);
			setButtonsEnabled();
		}
	}
	
	private class OtherCasesAddUCaseAction extends XAction {

		private static final String U_CASE = "U";
		private static final long serialVersionUID = 1L;

		public OtherCasesAddUCaseAction() {
			populateFromBundle("MainListingOtherCasesAddUCase");
			this.setController(listModel.getXac());
		}

		public void xActionPerformed(final ActionEvent ae) throws Exception {

			AddCaseDataModel caseModel = new AddCaseDataModel();
			AddCaseWizardDialog addHearingWizardDialog = new AddCaseWizardDialog(listModel.getXac(), U_CASE, caseModel);
			addHearingWizardDialog.setVisible(true);

			if (addHearingWizardDialog.getLatestEvent() == XWizardDialog.CANCEL_EVENT) {
				throw new UserCancelException();
			}
			retrieveCaseAndPopulateOtherCaseModel(caseModel.getCaseId());
			setButtonsEnabled();
		}
	}

	private class OtherCasesAddBCaseAction extends XAction {

		private static final String B_CASE = "B";
		private static final long serialVersionUID = 1L;

		public OtherCasesAddBCaseAction() {
			populateFromBundle("MainListingOtherCasesAddBCase");
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			AddCaseDataModel caseModel = new AddCaseDataModel();
			AddCaseWizardDialog addHearingWizardDialog = new AddCaseWizardDialog(listModel.getXac(), B_CASE, caseModel);
			addHearingWizardDialog.setVisible(true);

			if (addHearingWizardDialog.getLatestEvent() == XWizardDialog.CANCEL_EVENT) {
				throw new UserCancelException();
			}
			retrieveCaseAndPopulateOtherCaseModel(caseModel.getCaseId());
			setButtonsEnabled();
		}
	}
}