package uk.gov.courtservice.xhibit.client.listings.notes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class CaseNotesPanel extends AbstractNotesPanel {

	private static final long serialVersionUID = 1L;
	
	private List<RefListingDataBasicValue> editableNotesTypesArray;
	private String defaultHighlightClassificationCode = getResourceBundle("CaseNotesDefaultHighlightNoteClassification");
	
	public CaseNotesPanel(XDialog parent, CaseNotesModel model) throws CSRecoverableException {
		super(parent, model);
	}	

	@Override
	protected String[] getColumnHeaders() {
		return new String[] { 
				getResourceBundle("caseNotesTableType"), 
				getResourceBundle("caseNotesTableClassification"),
				getResourceBundle("caseNotesTableCreationDate"),
				getResourceBundle("caseNotesTableNotes"),
				getResourceBundle("caseNotesTableDiaryDate"),
				getResourceBundle("caseNotesTableLastUpdatedBy"),
				getResourceBundle("caseNotesTableLastUpdateDate")
			    };
	}
	
	@Override
	protected Integer[] getColumnWidths() {
		Integer[] columnWidths = super.getColumnWidths();
		columnWidths[0] = Integer.valueOf(30);
		columnWidths[1] = Integer.valueOf(30);
		columnWidths[3] = Integer.valueOf(300);
		return columnWidths;
	}

	@Override
	protected CaseNotesModel getModel() {
		return (CaseNotesModel) super.getModel();
	}
	
	@Override
	protected void initNoteDetailPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = getNoteDetailPanel();
		panel.setLayout(new GridBagLayout());
		
		//Error labels
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridx=3;
		panel.add(getNoteDiaryDateErrorLabel(), gbc);
		
		gbc.gridx=5;
		panel.add(getNoteClassErrorLabel(), gbc);
		gbc.gridx=0;
		gbc.gridy++;		
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		//Main fields
		panel.add(getNoteTypeLabel(), gbc);
		gbc.gridx++;
		panel.add(getNoteType(), gbc);
		
		gbc.gridx++;
		panel.add(getNoteDiaryDateLabel(), gbc);
		gbc.gridx++;	
		panel.add(getNoteDiaryDate(), gbc);
		
		gbc.gridx++;
		panel.add(getNoteClassificationLabel(), gbc);
		gbc.gridx++;
		panel.add(getNoteClass(), gbc);

		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.weightx = 1;
		gbc.gridx = 1;
		gbc.gridy++;
		gbc.gridwidth = 6;
		panel.add(getNoteEntryErrorLabel(), gbc);
		gbc.gridwidth = 1;				
		gbc.insets = XHIBITConstant.nonContainerInsets;

		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(getNoteEntryLabel(), gbc);
		gbc.gridx++;
		gbc.gridwidth = 5;
		panel.add(getNoteEntry(), gbc);
		
		pageController.addChangeListeners(panel.getComponents());
	}
	
	@Override
	protected void moveModelToScreen() {
		super.moveModelToScreen();
		
		// Set the screen title
		String dialogTitle = getResourceBundle("caseNotesTitle");
		parent.setTitle(dialogTitle.concat(" - ").concat(getModel().getCaseType()).concat(getModel().getCaseNumber().toString()));
		
		// Reset the note type combo to only show the valid entries
		setNoteTypeCombo(false);
		
		if (isReadOnly()) {
			setEditable(false);
			setNoteType(null);
		}

		pageController.reset();
	}
	
	@Override
	protected FocusTraversalOnArray getTabOrder() {
		return new FocusTraversalOnArray(new Component[] {
				getNotesTable(), getNoteType(), getNoteDiaryDate(), getNoteClass(), getNoteEntry(), 
				newButton, saveButton , deleteButton, closeButton
			}, 1);
	}

	@Override
	protected void noteTypeChanged() {
		// Clear the diary Date
		getNoteDiaryDate().clear();
		getModel().setDiaryDate(null);
		// Enable / Disable the screen
		setEditable(isDiaryNoteEditable());
		// Setup Classification Field
		setClassificationDefault();
		// Validate the entries
		validateAll();
	}
	
	@Override
	protected void setEditable(boolean isEditable) {
		getNoteDiaryDate().setEnabledAndFocusable(isEditable && isDiaryDateEditable());
		setNoteDiaryDateLabel(getResourceBundle(isDiaryDateMandatory() ? "caseNotesDiaryDateMandatory" : "caseNotesDiaryDate"));
		getNoteEntry().setEnabled(isEditable);
		getNoteEntry().setFocusable(isEditable);
		setComboEnabled(getNoteClass(), isEditable && isClassificationEditable());
	}
	
	@Override
	protected boolean isDiaryDateEditable() {
		boolean isEditable = super.isDiaryDateEditable();
		if (isEditable) {
			isEditable = isDiaryDateMandatory();
		}
		return isEditable;
	}
	
	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		boolean invalid = (validationController.hasErrors() || !ValidationControllerFactory.validateComponents(validationControllers));
		newButton.setEnabled(!isReadOnly());
		saveButton.setEnabled(!isReadOnly() && !invalid);
	}
	
	@Override
	protected boolean isReadOnly() {
		boolean isTransferredOut = getModel() != null && getModel().getDateTransTo() != null;
		return isTransferredOut;
	}

	@Override
	protected void setButtonState(boolean isExistingEntry) {
		boolean isDiaryNoteEditable = !isReadOnly() && isDiaryNoteEditable();
		newButton.setEnabled(isExistingEntry && !isReadOnly());
		deleteButton.setEnabled(isExistingEntry && isDiaryNoteEditable);
		saveButton.setEnabled(isExistingEntry && isDiaryNoteEditable);
	}
	
	@Override
	protected boolean isDiaryNoteEditable() {
		boolean isEditable = super.isDiaryNoteEditable();
		// Check if this is a note type we can edit
		if (isEditable) {
			isEditable = isEditableNoteType(getSelectedNoteType());
		}
		return isEditable;
	}
	
	private boolean isEditableNoteType(RefListingDataBasicValue noteType) {
		return noteType != null &&
				(RefListingDataBasicValue.DataValue.CASE_NOTE.equals(noteType.getRefDataValue()) ||
				 RefListingDataBasicValue.DataValue.HIGHLIGHT_NOTE.equals(noteType.getRefDataValue()) ||
				 RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE.equals(noteType.getRefDataValue()));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected Collection<DiaryNoteEntryComplexValue> getDiaryNotesFromDB() {
		Collection<DiaryNoteEntryComplexValue> diaryNotes = null;
		try {
			diaryNotes = (Collection<DiaryNoteEntryComplexValue>) XhibitDelegateHelper.getListingsDelegate().findCaseNotesByCaseId(getModel().getCaseId());
		} catch (ListingsControllerException ex) {
			XHIBITConstant.handleError(ex);
		}
		return diaryNotes;
	}
	
	@Override
	protected void selectNoteEntry(DiaryNoteEntryComplexValue diaryNote) {		
		if (diaryNote != null) {
			setNoteType(diaryNote.getNoteTypeId());
			getNoteDiaryDate().setDate(diaryNote.getDiaryDate());
			getModel().setDiaryDate(diaryNote.getDiaryDate());			
		} else {
			getNotesTable().getSelectionModel().clearSelection();
			setNoteType(null);
			getNoteDiaryDate().clear();
			getModel().setDiaryDate(null);			
		}
		getNoteDiaryDate().setEnabledAndFocusable(isDiaryDateEditable());		
		super.selectNoteEntry(diaryNote);					
	}	
	
	private List<RefListingDataBasicValue> getEditableNoteTypesArray() {
		if (editableNotesTypesArray == null) {			 
			editableNotesTypesArray = new ArrayList<RefListingDataBasicValue>();
			for (RefListingDataBasicValue noteType : getAllNoteTypeArray()) {
				if (isEditableNoteType(noteType)) {
					editableNotesTypesArray.add(noteType);		 
				}
			}			
		}
		return editableNotesTypesArray;
	}
	
	@Override
	protected List<RefListingDataBasicValue> getNoteTypeArray() {
		if (getSelectedRow() != null) {
			// Row selected, so show that one plus any ones that it can be converted to
			return getSelectedAndAssociatedNoteTypes();
		} else {
			// Nothing selected, so show the list minus any unique ones that already exist
			return getNewRecordNoteTypesArray();
		}
	} 
	
	/**
	 * Note Types Array for new records
	 * @return List<RefListingDataBasicValue>
	 */
	private List<RefListingDataBasicValue> getNewRecordNoteTypesArray() {
		List<RefListingDataBasicValue> results = new ArrayList<RefListingDataBasicValue>();

		// Get all the currently entered note types in the table
		List<String> noteTypeCodesInTable = getNoteTypeCodesInTable();

		// Filter the list of note types from the editable values 			
		for (RefListingDataBasicValue noteType : getEditableNoteTypesArray()) {

			// If this is not unique then add it to the list
			if (!isUniqueInTable(noteType)) {
				results.add(noteType);
				  	
			// Unique but does not appear in the table	
			} else if (isUniqueInTable(noteType) && !noteTypeCodesInTable.contains(noteType.getRefDataValue())) {
				results.add(noteType);
			}						
		}
		return results;
	}
	
	/**
	 * Note Types Array for selected note type
	 * @return List<RefListingDataBasicValue>
	 */
	private List<RefListingDataBasicValue> getSelectedAndAssociatedNoteTypes() {
		List<RefListingDataBasicValue> results = new ArrayList<RefListingDataBasicValue>();
		
		// Add the selected note type
		DiaryNoteEntryComplexValue selectedRow = getSelectedRow();
					
		// Add the note types that the selected one can be converted into (if any)
		List<String> conversionNoteTypeCodes = getConversionMap().get(getSelectedRow().getNoteType().getRefDataValue());
		if (conversionNoteTypeCodes != null && !conversionNoteTypeCodes.isEmpty()) {
			for (RefListingDataBasicValue noteType : getAllNoteTypeArray()) {
				if (noteType.getRefDataValue().equals(selectedRow.getNoteType().getRefDataValue())) {
					results.add(noteType);
				} else if (conversionNoteTypeCodes.contains(noteType.getRefDataValue())) {
					results.add(noteType);
				}
			}
		}
		return results;
	}
			
	private List<RefListingDataBasicValue> getAllNoteTypeArray() {
		return super.getNoteTypeArray();
	}
	
	@Override
	protected String getDefaultClassificationCode() {
		if (RefListingDataBasicValue.DataValue.HIGHLIGHT_NOTE.equals(getSelectedNoteType().getRefDataValue())) {
			return defaultHighlightClassificationCode;
		}
		return super.getDefaultClassificationCode();
	}
	
	private void setNoteTypeCombo(boolean showAll) {
		Object[] noteTypes = showAll ? getAllNoteTypeArray().toArray() : getNoteTypeArray().toArray();
		DefaultComboBoxModel comboModel = new DefaultComboBoxModel(noteTypes);
		getNoteType().setModel(comboModel);
	}
		
	private void setNoteType(Integer noteTypeId) {
		// Initialise to all being shown
		setNoteTypeCombo(true);
		// Select the note type
		if (noteTypeId == null) {
			setNoteTypeDefault();
		} else {
			getNoteType().setSelectedItemById(noteTypeId);
		}		
		// Is the selected noteType editable
		boolean isEditable = !isReadOnly() && isEditableNoteType(getSelectedNoteType());
		if (isEditable) {
			// Limit the available entries to the ones editable
			setNoteTypeCombo(false);
			// Reselect the correct entry using the new model
			if (noteTypeId != null) {
				getNoteType().setSelectedItemById(noteTypeId);
			}			
		}
		// Enable/Disable the Note Type Combo
		setComboEnabled(getNoteType(), isEditable);
	}
			
	@Override
	protected Object[] getNotesTableRow(DiaryNoteEntryComplexValue diaryNote) {
		String formattedDiaryDate = getFormattedDate(diaryNote.getDiaryDate());
		String formattedCreationDate = getFormattedDate(diaryNote.getCreationDate());
		String formattedLastUpdateDate = getFormattedDate(diaryNote.getLastUpdateDate());
		return new Object[] {diaryNote.getNotesTypeShortName(), diaryNote.getNoteClassificationShortName(), formattedCreationDate, 
				diaryNote.getDiaryNoteText(), formattedDiaryDate, diaryNote.getLastUpdatedBy(), formattedLastUpdateDate};
	}
	
	@Override
	protected void moveScreenToModel() throws CSRecoverableException {
		super.moveScreenToModel();
		// Set the diary Date
		getModel().getSelectedDiaryNote().setDiaryDate(getNoteDiaryDate().getDate() != null ? getNoteDiaryDate().getDate().getTime() : null);
		// Ensure we know the note type of ths selected note in case it is a note type being altered (e.g. we need to know if this was a Highlight Note and is changign to a Case Note)
		RefListingDataBasicValue oldNoteType = getModel().getSelectedDiaryNote().getNoteType();
		// Set note type
		RefListingDataBasicValue noteType = getSelectedNoteType();
		getModel().getSelectedDiaryNote().setNoteType(noteType != null ? noteType : null);	
		getModel().getSelectedDiaryNote().setNoteTypeId(noteType != null ? noteType.getRefListingDataId() : null);
		// Set the case listing entry id (if applicable)
		getModel().getSelectedDiaryNote().setCaseListingEntryId(isCaseListingEntry(noteType) ? getModel().getCaseListingEntryId() : null);
		getModel().getSelectedDiaryNote().setCourtId(getCourtId());
		// Set the case id on new notes
		if (getModel().getSelectedDiaryNote().getDiaryNoteEntryId() == null) {
			getModel().getSelectedDiaryNote().setCaseId(getModel().getCaseId());
		} else if (oldNoteType.getRefDataValue().equals(RefListingDataBasicValue.DataValue.HIGHLIGHT_NOTE)) { // If this was a highlight (i.e. no caseid and is now a case note then it needs a caseid assigned)
			getModel().getSelectedDiaryNote().setCaseId(getModel().getCaseId());
		}
	}

}
