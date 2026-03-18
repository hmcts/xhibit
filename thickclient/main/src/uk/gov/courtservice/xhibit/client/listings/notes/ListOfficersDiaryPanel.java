package uk.gov.courtservice.xhibit.client.listings.notes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayLODReportAction;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class ListOfficersDiaryPanel extends AbstractNotesPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Integer courtId = XhibitSingleton.getInstance().getCourtId();
	private JPanel filterPanel;
	private XDatePanelWithEvent filterDate;
	private XTextField caseNumber;
	private JLabel caseNumberErrorLabel;
	
	private JButton printButton;
	
	private TextValidationController caseNumberVC;
	
	public ListOfficersDiaryPanel(XDialog parent, ListOfficersDiaryModel model) throws CSRecoverableException {
		super(parent, model); 
	}	

	@Override
	protected void initButtons() {
		CustomButtonPanel buttonPanel = (CustomButtonPanel)parent.getButtonPanel();
		
		// Switch button panel settings to place WEST pinned buttons
		buttonPanel.getGridBagConstraints().gridx = 0;
		buttonPanel.getGridBagConstraints().anchor = GridBagConstraints.WEST;
		buttonPanel.getGridBagConstraints().fill = GridBagConstraints.NONE; 
		
		printButton = buttonPanel.addButton("listOfficersDiaryPrint", false, false);
		
		// Switch button panel settings back to placing EAST pinned buttons
		buttonPanel.getGridBagConstraints().gridx = GridBagConstraints.RELATIVE;
		buttonPanel.getGridBagConstraints().fill = GridBagConstraints.HORIZONTAL; 
		buttonPanel.getGridBagConstraints().anchor = GridBagConstraints.EAST;
		super.initButtons();
	}
	
	@Override
	protected String[] getColumnHeaders() {
		return new String[] {  
			    XHIBITConstant.getResource(XhibitBundles.Notes, "caseNotesTableNotes"),
			    XHIBITConstant.getResource(XhibitBundles.Notes, "caseNotesTableCreationDate"),
			    XHIBITConstant.getResource(XhibitBundles.Notes, "caseNotesTableLastUpdatedBy"),
			    XHIBITConstant.getResource(XhibitBundles.Notes, "caseNotesTableLastUpdateDate")
			    };
	}
	
	@Override
	protected Integer[] getColumnWidths() {
		Integer[] columnWidths = super.getColumnWidths();
		columnWidths[0] = Integer.valueOf(350);
		return columnWidths;
	}
	
	@Override
	protected Object[] getNotesTableRow(DiaryNoteEntryComplexValue diaryNote) {
		String formattedCreationDate = getFormattedDate(diaryNote.getCreationDate());
		String formattedLastUpdateDate = getFormattedDate(diaryNote.getLastUpdateDate());
		return new Object[] {diaryNote.getDisplayDiaryText(), formattedCreationDate, diaryNote.getLastUpdatedBy(), formattedLastUpdateDate};
	}
	
	@Override
	protected ListOfficersDiaryModel getModel() {
		return (ListOfficersDiaryModel) super.getModel();
	}
	
	@Override
	protected JLabel getNoteDiaryDateLabel() {
		return new JLabel(getResourceBundle("listOfficersDiaryDate"));
	}
	
	@Override
	protected JLabel getNoteEntryLabel() {
		return new JLabel(getResourceBundle("listOfficersNoteEntry"));
	}
	
	private JLabel getFilterDateLabel() {
		return new JLabel(getResourceBundle("listOfficersFilterDate"));
	}
	
	@Override
	protected void initMainPanel() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);	
	
		// Initialise the main panel
		JPanel mainPanel = getMainPanel(gbc);
		
		// Initialise the panels
		initFilterPanel();
		initTablePanel();
		initNoteDetailPanel();
		
		// Panel - Filter
	    gbc.weighty = 0.1;
	    mainPanel.add(getFilterPanel(), gbc);
	    
	 	// Panel - Table
	    gbc.weighty = 0.5;
	    gbc.gridy++;
	    mainPanel.add(getTablePanel(), gbc);
		
		// Panel - Note Detail 
	    gbc.weighty = 0.4;
		gbc.gridy++;
		mainPanel.add(getNoteDetailPanel(), gbc);
	}
	
	private void initFilterPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = getFilterPanel();
		panel.setLayout(new GridBagLayout());

		//Error labels
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridx = 1;
		panel.add(getNoteDiaryDateErrorLabel(), gbc);
		gbc.gridx = 0;
		gbc.gridy++;		
		gbc.insets = XHIBITConstant.nonContainerInsets;

		gbc.weightx = 0.1;
		panel.add(getFilterDateLabel(), gbc);
		gbc.gridx++;
		gbc.weightx = 0.9;
		panel.add(getFilterDate(), gbc);
	}
	
	@Override
	protected void initNoteDetailPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = getNoteDetailPanel();
		panel.setLayout(new GridBagLayout());
				
		//Error labels
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridx = 1;
		gbc.gridwidth = 3;
		panel.add(getCaseNumberErrorLabel(), gbc);			
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		// Main fields
		gbc.gridx=0;
		gbc.gridy++;
		gbc.weightx = 0.1;
		gbc.gridwidth = 1;
		panel.add(getCaseNumberLabel(), gbc);
		gbc.gridx++;
		gbc.weightx = 0.1;
		panel.add(getCaseNumber(), gbc);
		gbc.gridx++;
		gbc.weightx = 0.8;
		gbc.gridwidth = 2;
		panel.add(XHIBITConstant.getSpacer(), gbc);
				
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridx = 1;
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.gridwidth = 3;
		panel.add(getNoteEntryErrorLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(getNoteEntryLabel(), gbc);
		gbc.gridx++;
		gbc.weightx = 0.9;
		gbc.gridwidth = 3;
		panel.add(getNoteEntry(), gbc);
		
		pageController.addChangeListeners(panel.getComponents());
	}
	
	private XDatePanel getFilterDate() {
		if (filterDate == null) {
			filterDate = new XDatePanelWithEvent(getFilterPanel(), null, true) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void fireEvent() {
					refreshNotesTableFromDB(false);
				}
			};
			getNoteDiaryDateVC(filterDate);			
		}
		return filterDate;
	}

	@Override
	protected FutureDateValidator getFutureDateValidator() {
		FutureDateValidator futureDateValidator = super.getFutureDateValidator();
		// Make sure initially the future date is disabled (so it does not fire in the validateAll)
		futureDateValidator.setEnabled(false);
		return futureDateValidator;
	}
	
	@Override
	protected void refreshNotesTableFromDB(boolean automatedRefresh) {
		if (ValidationUtils.hasDate(filterDate)) {
			try {
				final Date prevDate = getModel().getSelectedDate();
				Date selectedDate = getModel().getDateWithoutTime(filterDate.getDateComponent().getValue());
				if (getModel().getSelectedDate() == null || !getModel().getSelectedDate().equals(selectedDate)) {
					// Keep the model date in sync
					getModel().setSelectedDate(selectedDate);
					// Check if this is a sitting day
					if (automatedRefresh || validateNonSittingDate(selectedDate)) {
						// Refresh the table with the new date
						super.refreshNotesTableFromDB(automatedRefresh);
						// Clear the selection
						selectNoteEntry(null);
					} else {
						SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				            	// Put the date back to its original value
								filterDate.setDate(prevDate);
								getModel().setSelectedDate(prevDate);
				            }
				        });
						
					}
				}	
			} catch (ParseException ex) {
				XHIBITConstant.handleError(ex);
			}
		}
	}
	
	@Override
	protected void refreshTheScreenFromDB(boolean automatedRefresh) {
		// Clear the selected date on the model to force a DB refresh 
		getModel().setSelectedDate(null);
		// Refetch the data
		super.refreshTheScreenFromDB(automatedRefresh);
	}

	@Override
	protected void moveScreenToModel() throws CSRecoverableException {
		super.moveScreenToModel();
		// Set the diary Date
		getModel().getSelectedDiaryNote().setDiaryDate(getFilterDate().getDate() != null ? getFilterDate().getDate().getTime() : null);
		// Set the note type 
		if (getModel().getSelectedDiaryNote().getNoteTypeId() == null) {		
			RefListingDataBasicValue noteType = getSelectedNoteType();
			getModel().getSelectedDiaryNote().setNoteType(noteType != null ? noteType : null);
			getModel().getSelectedDiaryNote().setNoteTypeId(noteType != null ? noteType.getRefListingDataId() : null);
		}
		// Set the court Id
        if (getModel().getSelectedDiaryNote().getCourtId() == null) {
        	getModel().getSelectedDiaryNote().setCourtId(courtId);	
        }		
	}

	@Override
	protected void moveModelToScreen() {
		// Set the screen title
		parent.setTitle(getResourceBundle("listOfficersTitle"));

		// Set the UI defaults
		filterDate.setDate(getModel().getSelectedDate());
		
		// Populate the Table (via the superclass)
		super.moveModelToScreen();
		
		if (isReadOnly()) {
			setEditable(false);
		}
		
		pageController.reset();
	}
	
	@Override
	protected FocusTraversalOnArray getTabOrder() {
		return new FocusTraversalOnArray(new Component[] {
				getFilterDate(), getNotesTable(), getCaseNumber(), getNoteEntry(), 
				printButton, newButton, saveButton , deleteButton, closeButton
			}, 2);
	}
	
	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		boolean invalid = (validationController.hasErrors() || !ValidationControllerFactory.validateComponents(validationControllers));
		newButton.setEnabled(true);
		saveButton.setEnabled(!isReadOnly() && !invalid);
	}
	
	@Override
	protected boolean isReadOnly() {
		DiaryNoteEntryComplexValue selectedRow = getSelectedRow();
		boolean transferredOut = (selectedRow != null && selectedRow.getDateTransTo() != null);
		return transferredOut;
	}

	@Override
	protected void setEditable(boolean isEditable) {
		getCaseNumber().setEnabled(isEditable);
		getCaseNumber().setFocusable(isEditable);
		getNoteEntry().setEnabled(isEditable);
		getNoteEntry().setFocusable(isEditable);
	}
	
	@Override
	protected void setButtonState(boolean isExistingEntry) {
		boolean isDiaryNoteEditable = !isReadOnly() && isDiaryNoteEditable();
		newButton.setEnabled(true);
		deleteButton.setEnabled(isExistingEntry && isDiaryNoteEditable);
		saveButton.setEnabled(isExistingEntry && isDiaryNoteEditable);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected Collection<DiaryNoteEntryComplexValue> getDiaryNotesFromDB() {
		Collection<DiaryNoteEntryComplexValue> diaryNotes = null;
		try {
			diaryNotes = (Collection<DiaryNoteEntryComplexValue>) XhibitDelegateHelper.getListingsDelegate().findGeneralDiaryNotesByCourtIdAndDate(courtId, getModel().getSelectedDate());
		} catch (ListingsControllerException ex) {
			XHIBITConstant.handleError(ex);
		}
		return diaryNotes;
	}
	
	@Override
	protected void selectNoteEntry(DiaryNoteEntryComplexValue diaryNote) {	
		if (diaryNote !=  null) {
			getCaseNumber().setText(diaryNote.getDisplayCaseNumber());
		} else {
			getCaseNumber().setText(null);
		}
		super.selectNoteEntry(diaryNote);
		setEditable(!isReadOnly());
	}
	
	private JPanel getFilterPanel() {
		if (filterPanel == null) {
			filterPanel = new JPanel();
		}
		return filterPanel;
	}
	
	private JLabel getCaseNumberLabel() {		
		return new MultiLineJLabel(getResourceBundle("listOfficersCaseNumber"));
	}

	private JLabel getCaseNumberErrorLabel() {
		if (caseNumberErrorLabel == null) {
			caseNumberErrorLabel = new JLabel(" ");
		}
		return caseNumberErrorLabel;
	}
	
	private XTextField getCaseNumber() {
		if (caseNumber == null) {
			caseNumber = new XTextField();
			caseNumber.setUpperCase(true);
			caseNumber.setMaxLength(9);
			caseNumberVC = ValidationControllerFactory.createText(this, caseNumber, getCaseNumberErrorLabel(), new CaseNumberValidator());
			validationControllers.add(caseNumberVC);
		}
		return caseNumber;
	}	
	
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (!update && printButton.equals(getDeinitialiseSource())) {
			try {
				new DisplayLODReportAction(parent.getParentFrame(),filterDate.getDateComponent().getValue());
			} catch (ParseException e) {
				throw new CSRecoverableException();
			}
			super.stepDeinitialise(update);
		} else if (update && saveButton.equals(getDeinitialiseSource())) {
			// Validate the filter date is not in the past
			boolean isValid = validateFilterDate();
			// Get the latest caseId (only if it has been amended/entered for the first time)
			if (isValid && caseNumberHasChanged()) {
				Integer caseId = getCaseIdFromCaseNumber(getCaseNumber().getText());
				isValid = validateCaseStatus(caseId); 
				if (isValid) {
					((DiaryNoteEntryBasicValue) getModel().getSelectedDiaryNote()).setCaseId(caseId);
				}
			}				
			// If the validation has passed then save
			if (isValid) {
				super.stepDeinitialise(update);
			}
		} else {
			super.stepDeinitialise(update);
		}
	}
	
	@Override
	protected String deleteConfirmationMessage() {
		String noteEntry = getResourceBundle("listOfficersDeleteNoteEntry");
		return getResourceBundle("caseNotesDeleteConfirmationMessage", new Object[] { noteEntry });
	}
	
	private Integer getCaseIdFromCaseNumber(String caseNumber) throws CSRecoverableException {
		Integer caseId = null;
		if (caseNumber != null && !caseNumber.isEmpty()) {
			// Try to fetch the id for the keyed in case number
			if (CaseNumberValidator.isValidCaseNumber(caseNumber)) {
				String caseType = Character.toString(CaseNumberValidator.getCaseType(caseNumber));
				// Verify the case type is valid for this process
				if (caseType != null && !ListOfficersDiaryModel.ValidValues.CASE_TYPES.contains(caseType)) {
					showErrorMsg(getErrorResourceBundle("validation.caseType.title"),
							getErrorResourceBundle("validation.caseType.TSA"));
				}
				Integer caseNumberDigits = Integer.valueOf(CaseNumberValidator.getCaseNumberDigits(caseNumber));
				try {
					caseId = XhibitDelegateHelper.getCaseDelegate().findCaseId(caseType, caseNumberDigits, courtId);
				} catch (CaseControllerException ex) {
					// A case number has been keyed but no case was found...					
					throw new CSValidationException("gui.search.invalidCase",
		                    "Invalid case number entered: " + caseNumber);
				}
			}
		}
		return caseId;
	}
	
	private boolean caseNumberHasChanged() {		
		String original = getModel().getSelectedDiaryNote() != null ? getModel().getSelectedDiaryNote().getDisplayCaseNumber() : "";
		String current = getCaseNumber().getText();		
		return !original.equals(current);
	}

	@Override
	protected RefListingDataBasicValue getSelectedNoteType() {
		return getNoteTypeByDataValue(RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE);
	}
	
	private boolean validateFilterDate() {
		boolean isValid = true;
		if (getModel().getSelectedDiaryNote().getDiaryNoteEntryId() == null) {
			// Validate the filter date only on save
			getFutureDateValidator().setEnabled(true);
			isValid = getNoteDiaryDateVC(filterDate).validate();
			getFutureDateValidator().setEnabled(false);
		}
		return isValid;				
	}
	
	/*
	 * Extend JLabel to include multiline functionality
	 */
	private static class MultiLineJLabel extends JLabel {
	
		private static final long serialVersionUID = 1L;
		
		private static final String LINEFEED = "\n";
		
		public MultiLineJLabel(String text) {
			super(text);
		}

		@Override
		public void setText(String text) {			
			if (StringUtils.contains(text, LINEFEED)) {				
				setMultiLineText(text);
			} else {
				super.setText(text);
			}
		}
		
		private void setMultiLineText(String text) {
			StringBuilder newText = new StringBuilder();
			newText.append("<html>");
			newText.append(StringUtils.replace(text, LINEFEED, "<br>"));
			newText.append("</html>");
			super.setText(newText.toString());
		}
	}
	
	@Override
	protected void showCancelConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = showConfirmationMsg(getResourceBundle("listOfficersCancelConfirmationTitle"),
				getResourceBundle("listOfficersCancelConfirmationMessage"));

		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}
}
