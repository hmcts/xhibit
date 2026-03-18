package uk.gov.courtservice.xhibit.client.listings.notes;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.services.caze.CaseStatusIndicator;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.MultiLineEditField;
import uk.gov.courtservice.xhibit.client.util.PageController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractTextValidator;
import uk.gov.courtservice.xhibit.client.util.validation.ComboBoxValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrAfterTodayValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateRequiredValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.util.validation.Validator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public abstract class AbstractNotesPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;

	private static final String DATE_FORMAT = "dd-MMM-yyyy";
	
	private JTable notesTable;
	protected JButton newButton;
	protected JButton deleteButton;
	protected JButton saveButton;
	protected JButton closeButton;
	private List<RefListingDataBasicValue> noteTypeArray;
	private XComboBox noteType;
	private XDatePanelWithEvent noteDiaryDate;
	private JLabel noteDiaryDateErrorLabel;
	private JLabel noteClassErrorLabel;
	private List<RefListingDataBasicValue> noteClassArray;
	private XComboBox noteClass;
	private MultiLineEditField noteEntry;
	private JLabel noteDiaryDateLabel;
	private JLabel noteEntryErrorLabel;

	private JPanel tablePanel;
	private JPanel noteDetailPanel;
	
	private String defaultNoteType = getResourceBundle("CaseNotesDefaultNoteType");
	private String defaultClassificationCode = getResourceBundle("CaseNotesDefaultNoteClassification");
	
	private AbstractNotesModel model;
	protected XDialog parent;
	
	private HashMap<String, List<String>> conversionMap; 
	
	private MandatoryDateValidator mandatoryDateValidator;
	private FutureDateValidator futureDateValidator;
	private DateValidationController noteDiaryDateVC;
	private TextValidationController noteEntryVC;
	private ComboBoxValidationController noteClassVC;
	protected PageController pageController = new PageController();
	protected List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	
	public AbstractNotesPanel(XDialog parent, AbstractNotesModel model) throws CSRecoverableException {
		this.model = model;	 		
		this.parent = parent;
		
        stepInitialise();
        jbInit();
        configureTabOrder();
	}
	
	protected abstract String[] getColumnHeaders();	
	protected abstract FocusTraversalOnArray getTabOrder();	
	protected abstract void initNoteDetailPanel();
	protected abstract Collection<DiaryNoteEntryComplexValue> getDiaryNotesFromDB();
	protected abstract Object[] getNotesTableRow(DiaryNoteEntryComplexValue diaryNote);
	
	protected void jbInit() {
		initButtons();
		initMainPanel();
	}
	
	protected JPanel getMainPanel(GridBagConstraints gbc) {
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(850, 350));
		this.add(scrollPane, gbc);
		return mainPanel;
	}
	
	protected void initMainPanel() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		
		// Initialise the main panel
		JPanel mainPanel = getMainPanel(gbc);
		
		// Initialise the panels
		initTablePanel();
		initNoteDetailPanel();
		
	 	// Panel - Table
	    gbc.weighty = 0.6;
	    mainPanel.add(getTablePanel(), gbc);
		
		// Panel - Note Detail 
	    gbc.weighty = 0.4;
		gbc.gridy++;
		mainPanel.add(getNoteDetailPanel(), gbc);
	}
	
	protected void initButtons() {
		CustomButtonPanel buttonPanel = (CustomButtonPanel)parent.getButtonPanel();
        buttonPanel.setShowCancelButton(true);
	    buttonPanel.setCancelText("Close");
	    buttonPanel.setCancelToolTip("Close");
        newButton = buttonPanel.addButton("caseNotesNew", false, false);
	    saveButton = buttonPanel.addButton("caseNotesSave", false, true);
	    deleteButton = buttonPanel.addButton("caseNotesDelete", false, false);
	    closeButton = buttonPanel.cancelButton;
	    setButtonState(false);
	}
	
	protected abstract void setEditable(boolean isEditable);
	protected abstract boolean isReadOnly();
	protected abstract void setButtonState(boolean isExistingEntry);
	
	protected Integer[] getColumnWidths() {
		Integer noOfColumns = getColumnHeaders().length;
		Integer[] columnWidths = new Integer[noOfColumns];
		return columnWidths;
	}
	
	protected JLabel getNoteTypeLabel() {
		return new JLabel(getResourceBundle("caseNotesNoteType"));
	}
	
	protected JLabel getNoteDiaryDateLabel() {
		if (noteDiaryDateLabel == null) {
			setNoteDiaryDateLabel(getResourceBundle("caseNotesDiaryDate")); 
		}		
		return noteDiaryDateLabel;
	}
	
	protected void setNoteDiaryDateLabel(String text) {
		if (noteDiaryDateLabel == null) {
			noteDiaryDateLabel = new JLabel(text);
		} else {
			noteDiaryDateLabel.setText(text);
		}
	}
	
	protected JLabel getNoteClassificationLabel() {
		return new JLabel(getResourceBundle("caseNotesClassification"));
	}
	
	protected JLabel getNoteEntryLabel() {
		return new JLabel(getResourceBundle("caseNotesNoteEntry"));
	}

	protected JLabel getNoteEntryErrorLabel() {
		if (noteEntryErrorLabel == null) {
			noteEntryErrorLabel = new JLabel(" ");
		}
		return noteEntryErrorLabel;
	}
	
	protected String getResourceBundle(String key) {
		return XHIBITConstant.getResource(XhibitBundles.Notes, key);
	}
	
	protected boolean isResourceAvailable(String key) {
		return XHIBITConstant.isResourceAvailable(XhibitBundles.Notes, key);
	}
	
	protected String getResourceBundle(String resourceKey, Object[] objects) {
		return MessageFormat.format(getResourceBundle(resourceKey), objects);
	}
	
	protected JLabel getNoteDiaryDateErrorLabel() {
		if (noteDiaryDateErrorLabel == null) {
			noteDiaryDateErrorLabel = new JLabel(" ");
		}
		return noteDiaryDateErrorLabel;
	}
	
	protected XDatePanel getNoteDiaryDate() {
		if (noteDiaryDate == null) {
			noteDiaryDate = new XDatePanelWithEvent(getNoteDetailPanel(), null, false) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void fireEvent() {
				
				if ( ValidationUtils.hasDate(noteDiaryDate) && noteDiaryDate.isEnabled() ) {
					try {
						final Date prevDate = getModel().getDiaryDate();
						Date selectedDate = getModel().getDateWithoutTime(noteDiaryDate.getDateComponent().getValue());
						if (getModel().getDiaryDate() == null || !getModel().getDiaryDate().equals(selectedDate)) {
							// Keep the model date in sync
							getModel().setDiaryDate(selectedDate);
							// Check if this is a sitting day
							if ( !validateNonSittingDate(selectedDate) ) {
									SwingUtilities.invokeLater(new Runnable() {
						            public void run() {
						            	// Put the date back to its original value
						            	noteDiaryDate.setDate(prevDate);
										getModel().setDiaryDate(prevDate);
						            }
						        });
							}
						}	
					} catch (ParseException ex) {
						XHIBITConstant.handleError(ex);
					}
				}

			}
		};
			validationControllers.add(getNoteDiaryDateVC(noteDiaryDate));
		}
		return noteDiaryDate;
	}
	
	protected List<Validator<XDatePanel>> getDateValidators() {
		List<Validator<XDatePanel>> dateValidators = new ArrayList<Validator<XDatePanel>>();
		dateValidators.add(getMandatoryDateValidator());
		dateValidators.add(getFutureDateValidator());
		return dateValidators;
	}
	
	protected MandatoryDateValidator getMandatoryDateValidator() {
		if (mandatoryDateValidator == null) {
			mandatoryDateValidator = new MandatoryDateValidator();
		}
		return mandatoryDateValidator;
	}

	protected FutureDateValidator getFutureDateValidator() {
		if (futureDateValidator == null) {
			futureDateValidator = new FutureDateValidator();
		}
		return futureDateValidator;
	}

	protected DateValidationController getNoteDiaryDateVC(XDatePanel datePanel) {
		if (noteDiaryDateVC == null) {
			noteDiaryDateVC = ValidationControllerFactory.createDate(this,
					datePanel, getNoteDiaryDateErrorLabel(), getDateValidators());
		}
		return noteDiaryDateVC;
	}
	
	protected boolean isDiaryDateEditable() {
		boolean isEditable = true;
		try {
			if (getNoteDiaryDate().getDate() != null) {
				isEditable = !getNoteDiaryDate().getDate().before(getToday());
			}
		} catch (CSValidationException ex) {
			XHIBITConstant.handleError(ex);
		}
		return isEditable;
	}	
	
	/**
	 * List of mappings of which note type can be converted into another note type. 
	 */
	protected HashMap<String, List<String>> getConversionMap() {
		if (conversionMap == null) {
			conversionMap = new HashMap<String, List<String>>();
			// Case Notes
			conversionMap.put(RefListingDataBasicValue.DataValue.CASE_NOTE, 
					Arrays.asList(RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE));
			// List Notes
			conversionMap.put(RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE, 
					Arrays.asList(RefListingDataBasicValue.DataValue.CASE_NOTE));
			// Highlight Note
			conversionMap.put(RefListingDataBasicValue.DataValue.HIGHLIGHT_NOTE, 
					Arrays.asList(RefListingDataBasicValue.DataValue.CASE_NOTE));			
		}
		return conversionMap;
	}
	
	protected boolean isUniqueInTable(RefListingDataBasicValue noteType) {
		return RefListingDataBasicValue.DataValue.HIGHLIGHT_NOTE.equals(noteType.getRefDataValue()) || 
			   RefListingDataBasicValue.DataValue.INTERPRETER_NOTE.equals(noteType.getRefDataValue());
	}

	protected boolean isCaseListingEntry(RefListingDataBasicValue noteType) {
		return RefListingDataBasicValue.DataValue.DEFAULT_CASE_NOTE.equals(noteType.getRefDataValue()) ||
			   RefListingDataBasicValue.DataValue.HIGHLIGHT_NOTE.equals(noteType.getRefDataValue()) ||
			   RefListingDataBasicValue.DataValue.INTERPRETER_NOTE.equals(noteType.getRefDataValue());
	}

	protected boolean isClassificationEditable() {
		RefListingDataBasicValue noteType = getSelectedNoteType();
		return (RefListingDataBasicValue.DataValue.CASE_NOTE.equals(noteType.getRefDataValue()) ||
				RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE.equals(noteType.getRefDataValue()));
	}
	
	protected boolean isDiaryDateMandatory() {
		RefListingDataBasicValue noteType = getSelectedNoteType();
		return RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE.equals(noteType.getRefDataValue());
	}
	
	protected JLabel getNoteClassErrorLabel() {
		if (noteClassErrorLabel == null) {
			noteClassErrorLabel = new JLabel(" ");
		}
		return noteClassErrorLabel;
	}
	
	protected MultiLineEditField getNoteEntry() {
		if (noteEntry == null) {
			noteEntry = new MultiLineEditField(1, 200);
			noteEntry.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					// Enable the New button as the record has changed
					validateAll();
				}
				@Override
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_ENTER){			
						e.consume();
			     	}
				}
			});
			
			noteEntryVC = ValidationControllerFactory.createTextRequired(this, noteEntry, getNoteEntryErrorLabel());
			validationControllers.add(noteEntryVC);
		}
		return noteEntry;
	}
	
	protected JPanel getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new JPanel();
		}
		return tablePanel;
	}
	
	protected JPanel getNoteDetailPanel() {
		if (noteDetailPanel == null) {
			noteDetailPanel = new JPanel();
		}
		return noteDetailPanel;
	}
	
	protected XComboBox getNoteType() {
		if (noteType == null) {
			noteType = new XComboBox();	
			ListingDropdownBoxCellRenderer translatableRenderer = new ListingDropdownBoxCellRenderer();
			translatableRenderer.setTranslationKey("CaseNotesNoteTypeArray_");
			setDropdownBoxArray(noteType, getNoteTypeArray().toArray(), translatableRenderer);
			noteType.addActionListener(new NoteTypeComboBoxAction());
			setNoteTypeDefault();
		}
		return noteType;
	}
	
	protected RefListingDataBasicValue getSelectedNoteType() {
		return (RefListingDataBasicValue) getNoteType().getSelectedItem();
	}

	protected void noteTypeChanged() {
	}
	
	protected XComboBox getNoteClass() {
		if (noteClass == null) {
			noteClass = new XComboBox();
			noteClassVC = ValidationControllerFactory.createComboBoxRequired(this, noteClass, getNoteClassErrorLabel());
			validationControllers.add(noteClassVC);
			setDropdownBoxArray(noteClass, getNoteClassificationsArray().toArray());
			setClassificationDefault();
		}
		return noteClass;		
	}
	
	protected JTable getNotesTable() {
		if (notesTable == null) {
			DefaultTableModel notesTableModel = new DefaultTableModel(new Object[][] {}, getColumnHeaders()) {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			notesTable = XTableFactory.getInstance().createDefaultTable(notesTableModel);
			TableUtils.setupDefaultsOnJTable(notesTable);
			notesTable.getCellSelectionEnabled();
			setNotesTableColumnWidths();
			notesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
				@Override
				public void valueChanged(ListSelectionEvent e) {
					DiaryNoteEntryComplexValue diaryNote = getSelectedRow();
					if (diaryNote != null) {
						selectNoteEntry(diaryNote);
					}
				}
			});
		}
		return notesTable;
	}
	
	protected DiaryNoteEntryComplexValue getSelectedRow() {
		DiaryNoteEntryComplexValue result = null;
		int rowSelected = getNotesTable() != null ? getNotesTable().getSelectedRow() : -1;
		if (!Integer.valueOf(-1).equals(rowSelected)) {
			List<DiaryNoteEntryComplexValue> diaryNotes = (List<DiaryNoteEntryComplexValue>) getModel().getDiaryNotes();
			result = diaryNotes.get(rowSelected);
		}
		return result;
	}
	
	protected List<String> getNoteTypeCodesInTable() {
		List<String> results = new ArrayList<String>();
		for (DiaryNoteEntryComplexValue diaryNote : getModel().getDiaryNotes()) {
			if (!results.contains(diaryNote.getNoteType().getRefDataValue())) {
				results.add(diaryNote.getNoteType().getRefDataValue());
			}
		}
		return results;
	}
	
	protected void selectNoteEntry(DiaryNoteEntryComplexValue diaryNote) {
		if (diaryNote != null) {
			getNoteEntry().setText(diaryNote.getDiaryNoteText());
			getNoteClass().setSelectedItemById(diaryNote.getNoteClassificationId());
		} else {
			notesTable.getSelectionModel().clearSelection();
			getNoteEntry().setText(null);
			if (getNoteClass() != null) {
				setClassificationDefault();
			}
		}
		setButtonState(diaryNote != null);
		clearAllErrors();
		pageController.reset();
	}
	
	protected void setNotesTableColumnWidths() {
		Integer[] customColumnWidths = getColumnWidths();
		if (customColumnWidths != null) {
			notesTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			for (int columnNo = 0; columnNo < notesTable.getModel().getColumnCount(); columnNo++) {
				if (customColumnWidths[columnNo] != null) {
					notesTable.getColumnModel().getColumn(columnNo).setPreferredWidth(customColumnWidths[columnNo]);		
				}
			}
		}
	}
	
	protected DefaultTableModel getNotesTableModel() {
		return (DefaultTableModel) getNotesTable().getModel();
	}
	
	protected void populateNotesTable() {
		getNotesTableModel().setRowCount(0);
		if (getModel().getDiaryNotes() != null && !getModel().getDiaryNotes().isEmpty()) {
			for (DiaryNoteEntryComplexValue diaryNote : (Collection<DiaryNoteEntryComplexValue>) getModel().getDiaryNotes()) {
				getNotesTableModel().addRow(getNotesTableRow(diaryNote));
			}
		}
	}
	
	protected List<RefListingDataBasicValue> getNoteClassificationsArray() {
		if (noteClassArray == null) {
			noteClassArray = ListingDropdownPopulation.getNoteClassifications();
		}		
		return noteClassArray;
	}
	
	protected List<RefListingDataBasicValue> getNoteTypeArray() {
		if (noteTypeArray == null) {
			noteTypeArray = ListingDropdownPopulation.getNoteTypes();
		}
		return noteTypeArray;
	}
	
	protected RefListingDataBasicValue getNoteTypeByDataValue(String dataValue) {
		RefListingDataBasicValue result = null;
		for (RefListingDataBasicValue noteType :getNoteTypeArray()) {
			if (noteType.getRefDataValue() != null &&
					noteType.getRefDataValue().equals(dataValue)) {
				result = noteType;
				break;
			}
		}
		return result;
	}
	
	protected AbstractNotesModel getModel() {
		return this.model;
	}
	
	protected void initTablePanel() {	
		GridBagConstraints gbc = new GridBagConstraints(0, 0, getNotesTable().getColumnCount(), 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = getTablePanel();
		panel.setLayout(new GridBagLayout());
		
		// Add Table to Scroll Pane
		JScrollPane scrollPane = new JScrollPane();		
		scrollPane.setPreferredSize(new Dimension(gbc.gridwidth, 100));
		scrollPane.setViewportView(getNotesTable());		
		panel.add(scrollPane, gbc);
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		// Get the data from the database		
		Collection<DiaryNoteEntryComplexValue> diaryNotes = getDiaryNotesFromDB(); 
		
        // Populate the model for use in the screen
        populateModel(diaryNotes);	
	}
		 
	protected void populateModel(Collection<DiaryNoteEntryComplexValue> diaryNotes) {
		model.clearmodel();
		model.setDiaryNotes(diaryNotes);
	}
	
	protected void refreshNotesTableFromDB(boolean automatedRefresh) {
		Collection<DiaryNoteEntryComplexValue> diaryNotes = getDiaryNotesFromDB();
		model.setDiaryNotes(diaryNotes);
		populateNotesTable();
	}
	
	protected void moveModelToScreen() {
		populateNotesTable();
	}

	protected String getDefaultClassificationCode() {
		return defaultClassificationCode;
	}
	
	protected void setClassificationDefault() {
    	Integer selectedIndex = getComboBoxModelDefaultValue(getNoteClass().getModel(), getDefaultClassificationCode());
		if (selectedIndex != null) {
			getNoteClass().setSelectedIndex(selectedIndex);
		}
	}
	
	protected void setNoteTypeDefault() {
    	Integer selectedIndex = getComboBoxModelDefaultValue(getNoteType().getModel(), defaultNoteType);
		if (selectedIndex != null) {
			getNoteType().setSelectedIndex(selectedIndex);
		}
	}
	
	protected String getFormattedDate(Date date) {
		return date != null ? new SimpleDateFormat(DATE_FORMAT).format(date.getTime()) : null;
	}
	
	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();

		// Initialise display
		stepUpdateViewState();
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
    	// Throw exception if failures to prevent navigation 
    	if (!ValidationControllerFactory.validateComponents(validationControllers)) {
    		throw new CSValidationException();
    	}		
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		moveScreenToModel();
	}
	
	@Override
	public abstract void validationUpdatedView(ValidationController<?> validationController);
	
	protected void clearAllErrors() {
		for (ValidationController<?> validationController : validationControllers) {
			validationController.clearErrors();
		}
	}
	
	protected void validateAll() {
		for (ValidationController<?> validationController : validationControllers) {
			validationUpdatedView(validationController);
		}
	}
	
	protected boolean isDiaryNoteEditable() {
		return !isReadOnly();
	}
	
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (update) {
			if (saveButton.equals(getDeinitialiseSource())) {
				// Save the record
				XhibitDelegateHelper.getListingsDelegate().saveDiaryNoteEntry((DiaryNoteEntryBasicValue) getModel().getSelectedDiaryNote(), getUsername());
				// Flag the data as changed
				getModel().setDataChanged(true);
				// Refresh the screen from the DB
				refreshTheScreenFromDB(true);
			}
		} else {
			if (newButton.equals(getDeinitialiseSource())) {
				selectNoteEntry(null);
			} else if (deleteButton.equals(getDeinitialiseSource())) {
				// Update the model with the selected row
				getModel().setSelectedDiaryNote(getSelectedRow());
				// Ask for confirmation to delete
				showDeleteConfirmationMsg();
				// Delete the record
				XhibitDelegateHelper.getListingsDelegate().deleteDiaryNoteEntry((DiaryNoteEntryBasicValue) getModel().getSelectedDiaryNote(), getUsername());
				// Flag the data as changed
				getModel().setDataChanged(true);
				// Refresh the screen from the DB
				refreshTheScreenFromDB(true);
			} else if (pageController.isPageChanged()) {
				showCancelConfirmationMsg();
			}
		}
	}
	
	protected void refreshTheScreenFromDB(boolean automatedRefresh) {
		// Refetch the notes
		refreshNotesTableFromDB(automatedRefresh);
		// Clear selection
		selectNoteEntry(null);
	}

	protected void moveScreenToModel() throws CSRecoverableException {
		// Get the selected row (if one is selected)
		getModel().setSelectedDiaryNote(getSelectedRow());
		// Create a new value if one hasn't been selected
		if (getModel().getSelectedDiaryNote() == null) {
			getModel().setSelectedDiaryNote(new DiaryNoteEntryComplexValue());
		}
		// Populate the selected diary note from the screen values		
		getModel().getSelectedDiaryNote().setDiaryNoteText(getNoteEntry().getText());
		RefListingDataBasicValue noteClass = (RefListingDataBasicValue) getNoteClass().getSelectedItem();
		getModel().getSelectedDiaryNote().setNoteClassification(noteClass != null ? (RefListingDataBasicValue) getNoteClass().getSelectedItem() : null);
		getModel().getSelectedDiaryNote().setNoteClassificationId(noteClass != null ? noteClass.getRefListingDataId() : null);
	}
	
	protected void setComboEnabled(XComboBox combo, boolean enabled) {
		combo.setEnabled(enabled);
		combo.setFocusable(enabled);
	}
	
	protected String getUsername() {
		return XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
	}
	
	protected Integer getCourtId() {
		return XhibitSingleton.getInstance().getCourtId();
	}
	
	protected void configureTabOrder() {
		parent.setFocusTraversalPolicyProvider(true);
		parent.setFocusTraversalPolicy(getTabOrder());
	}
	
	protected boolean showConfirmationMsg(String title, String msg) throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parent, title, true,
				XMessageBox.ICONQUESTION, msg, XMessageBox.YESNO,
				XMessageBox.DEFAULTCANCEL);
		return messageBoxReply;
	}
	
	protected void showErrorMsg(String title, String msg) throws CSRecoverableException {
		XMessageBox.alert(parent, title, true,
				XMessageBox.ICONERROR, msg, XMessageBox.OK_ONLY, 
				XMessageBox.DEFAULTOK);
		throw new UserCancelException();
	}
	
	protected void showCancelConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = showConfirmationMsg(getResourceBundle("caseNotesCancelConfirmationTitle"),
				getResourceBundle("caseNotesCancelConfirmationMessage"));

		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}
	
	protected boolean showNonSittingDayConfirmationMsg(Date date) throws CSRecoverableException {
		return showConfirmationMsg(getResourceBundle("caseNotesNonSittingDayTitle"),
				getResourceBundle("caseNotesNonSittingDayMessage", new Object[] { getFormattedDate(date) }));
	}
	
	protected boolean isSittingDate(Date date) throws CSRecoverableException {
		boolean isSittingDate = XhibitDelegateHelper.getBizRefDelegate().isCourtAvailableOnDate(
				XhibitSingleton.getInstance().getCourtId(), date);
		return isSittingDate;
	}
	
	protected String deleteConfirmationMessage() {
		String noteEntry = getResourceBundle("caseNotesDeleteNoteEntry");
		return getResourceBundle("caseNotesDeleteConfirmationMessage", new Object[] { noteEntry });
	}
	
	protected boolean validateNonSittingDate(Date date) {
		try {
			if (!isSittingDate(date)) {
				return showNonSittingDayConfirmationMsg(date);
			}
		} catch (CSRecoverableException ex) {
			XHIBITConstant.handleError(ex);
		}
		return true;
	}

	private void showDeleteConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parent, getResourceBundle( 
				"caseNotesDeleteConfirmationTitle"), true,
				XMessageBox.ICONQUESTION, deleteConfirmationMessage(), XMessageBox.YESNO,
				XMessageBox.DEFAULTNO);
		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}
	
	private Calendar getToday() {
		return DateTimeUtilities.stripTimeToCalendar(new Date());
	}
	
    private void setDropdownBoxArray(XComboBox comboBox, final Object[] arrayItems) 
    {
    	setDropdownBoxArray(comboBox, arrayItems, new DropdownBoxCellRender());
    }
    
	/**
	 * Set the array and if the array is empty disable the dropdown box
	 */
    private void setDropdownBoxArray(XComboBox comboBox, final Object[] arrayItems, DropdownBoxCellRender renderer) 
    {
    	comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
	    	comboBox.setRenderer(renderer);
			comboBox.enableAutoSelect();
		} else {
			comboBox.setEnabled(false);
		}
    }
    
	protected Integer getComboBoxModelDefaultValue(ComboBoxModel model, String defaultValue) {
		if (model != null && defaultValue != null) {
			String elementValue = null;
			for (int i = 0; i < model.getSize(); i++) {
				elementValue = ((RefListingDataBasicValue) model.getElementAt(i)).getRefDataValue();
				if (elementValue != null && elementValue.equals(defaultValue)) {
					return i;
				}
			}
		}
		return null;
	}

	protected boolean validateCaseStatus(Integer caseId)
	{
		boolean valid = true;
		
		String caseStatus = XhibitDelegateHelper.getCaseDelegate().determineCaseStatus(caseId);
		
		if ( CaseStatusIndicator.INCOMPLETE_N.equals(caseStatus) ) {
			// There must be at least one defendant/appellant AND a prosecutor/respondent on the case.
			String errorMsg = getErrorResourceBundle("listings.caseListingDetails.alert.partyDetailsIncomplete");
			XMessageBox.alert(parent,
					getErrorResourceBundle("listings.caseListingDetails.alert.title"), true,
					XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			valid = false;
		}
		else if ( CaseStatusIndicator.INCOMPLETE_I.equals(caseStatus) ) {
			// The number of defendants stated on the case must also equal the number of defendants
			String errorMsg = XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.caseListingDetails.alert.incorrectNoDefendants");
			XMessageBox.alert(parent,
					XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.caseListingDetails.alert.title"), true,
					XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			valid = false;
		}
		
		return valid;
	}
	
	protected String getErrorResourceBundle(String resourceKey) {
		return XHIBITConstant.getResource(XhibitBundles.ErrorText, resourceKey); 
	}
	
	private class NoteTypeComboBoxAction extends XAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			noteTypeChanged();
		}
	}
	
	private class ListingDropdownBoxCellRenderer extends DropdownBoxCellRender {

		private static final long serialVersionUID = 1L;
		private String translationKey;
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			String actualTranslationKey = translationKey + getText().toUpperCase();
			if (isResourceAvailable(actualTranslationKey)) {
				setText(getResourceBundle(actualTranslationKey));
			}
			return component;
		}
		
		public void setTranslationKey(String translationKey) {
			this.translationKey = translationKey;
		}
	}
	
	protected static class CaseNumberValidator extends AbstractTextValidator {
		
		private static final int LENGTH_OF_CASE_TEXT = 9;
		private static final String INVALID_CASE_NUMBER = "Invalid Case Number";

		@Override
		public void validate(JTextComponent target, List<String> errors) {
			if (!target.getText().isEmpty() && !isValidCaseNumber(target.getText())) {
				errors.add(INVALID_CASE_NUMBER);
			}
		}

		protected static boolean isValidCaseNumber(String text) {
			return isLengthValid(text) && 
					isCaseTypeValid(text) &&
					isCaseNumberDigitsValid(text);
		}

		protected static char getCaseType(String text) {
			return text.charAt(0);
		}
		
		protected static String getCaseNumberDigits(String text) {
			String caseNumberDigits; 
			try {
	        	caseNumberDigits = text.substring(1);
	            Long.parseLong(caseNumberDigits);
	    	} catch (NumberFormatException nfe) {
	    		caseNumberDigits = null;
	    	}
			return caseNumberDigits;
		}
		
		private static boolean isLengthValid(String text) {
	    	return text.length() == LENGTH_OF_CASE_TEXT;
	    }
	     
	    private static boolean isCaseTypeValid(String text) {
	    	return Character.isLetter(getCaseType(text));
	    }
	    
	    private static boolean isCaseNumberDigitsValid(String text) {	    	
	    	return text.length() > 0 && getCaseNumberDigits(text) != null;
	    }
	}
	
	protected class MandatoryDateValidator extends DateRequiredValidator {
		@Override
		public void validate(XDatePanel target, List<String> errors) {
			if (isDiaryDateMandatory()) {
				super.validate(target, errors);
			}
		}
	};
	
	protected class FutureDateValidator extends DateEqualOrAfterTodayValidator {
		
		private boolean isEnabled = true;		
		
		@Override
		public void validate(XDatePanel target, List<String> errors) {
			if (isEnabled && isDiaryDateMandatory()) {
				super.validate(target, errors);
			}
		}
		
		public void setEnabled(boolean isEnabled) {
			this.isEnabled = isEnabled;
		}
	};
}
