package uk.gov.courtservice.xhibit.client.listings.details;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseDiaryFixtureValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownBoxCellRenderer;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.util.MultiLineEditField;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrAfterTodayValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CaseListingFixturePanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;

	private static final int DEFENDANT_COL_POSITION = 0;
	private static final int ATTENDING_COL_POSITION = 1;
	private static final int CUSTODYTIMElIMIT_COL_POSITION = 2;
	private static final String ACTIVE = "A";
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	private static final String YES = "Y";
	private static final String NO = "N";
	private static final String RESULTS_VERIFIED = "E";
	private static final String VALIDATE_FIXTURE_WARNED = "WARNED";
	private static final String VALIDATE_FIXTURE_FIRM = "FIRM";

	public static final String ERROR_MESSAGE = null;

	private CaseListingFixtureModel model;
	private XDialog parent;
	private XDatePanelWithEvent selectDatePanel;
	private XComboBox preDefined;
	private MultiLineEditField freeText;
	private XTextField hearingType;
	private JButton verifyHearingTypeButton;
	private XComboBox hearingTypeCombo;
	private XComboBox courtSite;
	private JCheckBox fixtureNoticeReqdCheckBox;
	private DefendantTableModel defendantTableModel;
	private JTable defendantTable;
	private String regex = "[A-Z]{3}";
	private JLabel selectDateError;
	private DateValidationController selectDateValidator;
	protected List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();

	/**
	 * Constructor
	 * @param parent CaseListingFixtureDialog
	 * @param model CaseListingFixtureModel
	 * @throws CSRecoverableException
	 */
	public CaseListingFixturePanel(CaseListingFixtureDialog parent, CaseListingFixtureModel model)
			throws CSRecoverableException {
		this.model = model;
		this.parent = parent;

		stepInitialise();
		jbInit();
		configureTabOrder();
	}

	/**
	 * Initialise layout of screen
	 */
	private void jbInit() {
		// Set button state
		getButtonPanel().okButton.setEnabled(model.isEdit());
		// Add overall panel layout
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(900, 300));
		this.add(scrollPane, gbc);

		// Content Panel Element
		gbc.weightx = 0.20;
		JPanel dateAndHearingPanel = initDateAndHearingPanel();
		mainPanel.add(dateAndHearingPanel, gbc);

		// Panel Attending Defendant
		gbc.gridx++;
		gbc.weightx = 0.80;
		JPanel attendingDefendantsPanel = initAttendingDefendantsPanel();
		mainPanel.add(attendingDefendantsPanel, gbc);

		// Panel - Fixture Notice Check box
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		JPanel fixtureNoticePanel = initfixtureNoticePanel();
		mainPanel.add(fixtureNoticePanel, gbc);

		// Panel - List Note
		gbc.gridy++;

		JPanel defaultNotePanel = initListNotePanel();
		mainPanel.add(defaultNotePanel, gbc);
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		// Note: No required populateModel as the model is passed in
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
		String validationCode = XhibitDelegateHelper.getListingsDelegate().validateListsForFixture(
				model.getCaseId(), selectDatePanel.getTimestamp());
		
		if ( VALIDATE_FIXTURE_WARNED.equals(validationCode) || VALIDATE_FIXTURE_FIRM.equals(validationCode) ) {
			// The case is already on a Warned list or reserved Firm list scheduled for the listing date entered
			throw new CSValidationException("listings.fixture.caseAlreadyListed", // Error message displayed to user
					"Cannot create a fixture for this date.  Case already listed." // Message written to log
					);
		}
		if (!isSittingDate(selectDatePanel.getDate().getTime())) {
			// Fixture date entered is on a non-sitting date
			throw new CSValidationException("listings.fixture.nonSittingDay", // Error message displayed to user
					"This is not a court sitting date" // Message written to log
					);
		}
	}
	
	private boolean isDefendantOnAnotherFixture(Date listingDate, Integer defendantOnCaseId) {
		boolean result = false;
		if ( model.getFixturesOnCase() != null && !model.getFixturesOnCase().isEmpty() ) {
			for (CaseDiaryFixtureComplexValue fixture : model.getFixturesOnCase()) {
				// Date match and not the one we are editing then..
				if (!fixture.getId().equals(model.getFixture().getId()) &&
					fixture.getListingDate().equals(listingDate) ) {
					//...check if the defendant is already listed and is attending
					if ( isDefendantAttending(defendantOnCaseId, fixture.getFixtureDeftAttending()) ) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
	
	private boolean isDefendantAttending(Integer defendantOnCaseId, Collection<FixtureDeftAttendingBasicValue> fixtureDefendants) {
		boolean result = false;
		if (fixtureDefendants != null && !fixtureDefendants.isEmpty()) {
			for ( FixtureDeftAttendingBasicValue fixtureDefendant : fixtureDefendants ) {
				if (fixtureDefendant.getDefendantOnCaseId().equals(defendantOnCaseId)) {
					result = YES.equals(fixtureDefendant.getAttending());
					break;
				}
			}
		}
		return result;
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		 moveScreenToModel();
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (!update) {
			showCancelConfirmationMsg();
		} else{
			if(getButtonPanel().okButton.equals(getDeinitialiseSource())){
				// Save the fixture
				CaseDiaryFixtureValue caseDiaryFixtureValue = new CaseDiaryFixtureValue(
							model.getFixture(), model.getCourtId(), model.getCaseId());	
				Integer fixtureId = XhibitDelegateHelper.getListingsDelegate().saveCaseDiaryFixture(caseDiaryFixtureValue, 
						XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				if (fixtureId != null) {
					CaseDiaryFixtureComplexValue updatedFixture =  XhibitDelegateHelper.getListingsDelegate().findCaseDiaryFixture(fixtureId);
					model.setFixture(updatedFixture);
				}
			}
		}
	}

	/**
	 * Initialise the Listing Date and Hearing Fields panel
	 * @return JPanel
	 */
	private JPanel initDateAndHearingPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel dateAndHearingPanel = new JPanel();
		dateAndHearingPanel.setLayout(new GridBagLayout());

		// Panel - listing date
		gbc.weighty = 0.50;
		JPanel caseListingFixtureDateTitlePanel = initListingDatePanel();
		dateAndHearingPanel.add(caseListingFixtureDateTitlePanel, gbc);

		// Panel - Hearing Type
		gbc.gridy++;
		JPanel hearingTypePanel = initHearingTypePanel();
		dateAndHearingPanel.add(hearingTypePanel, gbc);

		return dateAndHearingPanel;
	}

	/**
	 * Initialise the Listing Date panel
	 * @return JPanel
	 */
	private JPanel initListingDatePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel caseListingFixtureDateTitlePanel = new JPanel();
		caseListingFixtureDateTitlePanel.setLayout(new GridBagLayout());

		// Add the error labels
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridwidth = 2;
		selectDateError = new JLabel(" ");
		caseListingFixtureDateTitlePanel.add(selectDateError, gbc);
		gbc.gridwidth = 1;
		gbc.gridy++;
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		// Add the panel elements
		gbc.weightx = 0.20;
		JLabel caseFixtureDateLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDate"));
		caseListingFixtureDateTitlePanel.add(caseFixtureDateLabel, gbc);

		gbc.weightx = 0.80;
		gbc.gridx++;
		selectDatePanel = new XDatePanelWithEvent(caseListingFixtureDateTitlePanel, null, true) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void fireEvent() {
				if (ValidationUtils.hasDate(this)) {
	            	try {
						setTableDefendants(getDate().getTime());
					} catch (CSValidationException ex) {
						XHIBITConstant.handleError(ex);
					}
				}
			}
		};
		selectDateValidator = ValidationControllerFactory.createDateRequired(this,
				selectDatePanel, selectDateError,
				new DateEqualOrAfterTodayValidator());
		validationControllers.add(selectDateValidator);
		caseListingFixtureDateTitlePanel.add(selectDatePanel, gbc);

		return caseListingFixtureDateTitlePanel;
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		setSaveEnabled();
	}
	
	private OkCancelPanel getButtonPanel() {
		return (OkCancelPanel) parent.getButtonPanel();
	}
	
	/**
	 * Initialise the Attending Defendants panel
	 * @return JPanel
	 */
	private JPanel initAttendingDefendantsPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 3, 2, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel attendingDefendantsPanel = new JPanel();
		attendingDefendantsPanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureDefendants")));
		attendingDefendantsPanel.setLayout(new GridBagLayout());

		// Table
		String[] columnHeaders = new String[] {
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureDefendant"),
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureAttending"),
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureistingCustodyTime") };

		// Add the panel elements
		defendantTableModel = new DefendantTableModel(new Object[][] {}, columnHeaders);
		defendantTable = XTableFactory.getInstance().createDefaultTable(defendantTableModel);
		defendantTable.setRowSorter(new FilteredTableRowSorter(defendantTableModel));
		defendantTable.getCellSelectionEnabled();
		TableUtils.setupDefaultsOnJTable(defendantTable);
		// Add SPACE Key Binding to the table to handle the ticking/unticking of the checkbox in the table
		String tabActionName = "Space";
		defendantTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), tabActionName);
		defendantTable.getActionMap().put(tabActionName, new AttendingDefendantsSpaceAction());

		// Add Table to Scroll Pane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(gbc.gridwidth, gbc.gridheight));
		scrollPane.setViewportView(defendantTable);
		attendingDefendantsPanel.add(scrollPane, gbc);

		return attendingDefendantsPanel;
	}

	/**
	 * Update the model from the screen
	 * @throws CSRecoverableException
	 */
	private void moveScreenToModel() throws CSRecoverableException {
		Integer selectedId;
		model.getFixture().setListingDate(selectDatePanel.getTimestamp());	
		selectedId = ((RefListingDataBasicValue) preDefined.getSelectedItem()).getId();
		model.getFixture().setListNotePreDefinedId(selectedId);	
		selectedId = ((RefHearingTypeBasicValue) hearingTypeCombo.getSelectedItem()).getId();
		model.getFixture().setHearingTypeId(selectedId);
		model.getFixture().setListNoteText(freeText.getText());
		selectedId = ((CourtSiteBasicValue) courtSite.getSelectedItem()).getId();
		model.getFixture().setCourtSiteId(selectedId);
		model.getFixture().setFixtureNoticeRequired(fixtureNoticeReqdCheckBox.isSelected() ? YES : NO);
		model.getFixture().setStatus(ACTIVE);
		model.getFixture().setFixtureDeftAttending(getFixtureDeftAttendingList());
	}
	
	/**
	 * Set the array and if the array is empty disable the dropdown box
	 */
	private void setDropdownBoxArray(JComboBox comboBox, final Object[] arrayItems) {
		comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
			comboBox.setRenderer(new ListingDropdownBoxCellRenderer());
		} else {
			comboBox.setEnabled(false);
		}
	}

	/**
	 * Initialise the Hearing Type panel
	 * @return JPanel
	 */
	private JPanel initHearingTypePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);

		JPanel hearingTypePanel = new JPanel();
		hearingTypePanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureHearingType")));
		hearingTypePanel.setLayout(new GridBagLayout());

		gbc.weightx = 0.60;
		hearingType = new XTextField();
		hearingType.setUpperCase(true);
		hearingType.setMaxLength(3);

		hearingTypePanel.add(hearingType, gbc);

		gbc.gridx++;
		gbc.weightx = 0.40;
		verifyHearingTypeButton = new JButton(new VerifyHearingTypeButtonAction());

		verifyHearingTypeButton.setEnabled(model.isEdit());
		hearingType.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (hearingType.getText().matches(regex)) {
					verifyHearingTypeButton.setEnabled(true);
				} else {
					verifyHearingTypeButton.setEnabled(false);
				}
			}
		});

		hearingTypePanel.add(verifyHearingTypeButton, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		hearingTypeCombo = new XComboBox();

		setDropdownBoxArray(hearingTypeCombo, model.getHearingTypeArray().toArray());
		hearingTypePanel.add(hearingTypeCombo, gbc);
		hearingTypeCombo.addActionListener(new HearingTypeComboBoxAction());
		Dimension d = hearingTypeCombo.getPreferredSize();
		hearingTypeCombo.setPreferredSize(new Dimension(200, d.height));

		return hearingTypePanel;
	}

	/**
	 * Action class for the Verify Hearing Type button
	 */
	private class VerifyHearingTypeButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		/**
		 * Identifies the index of the reference data item matching the selected code
		 * @param model	ComboBoxModel
		 * @param selectedCode Selected code
		 * @return Index of the selected code in the refdata model
		 */
		private Integer getComboBoxModelIndexForCode(ComboBoxModel model, String selectedCode) {
			if (model != null && selectedCode != null) {
				String elementCode = null;
				for (int i = 0; i < model.getSize(); i++) {
					elementCode = ((RefHearingTypeBasicValue) model.getElementAt(i)).getHearingTypeCode();
					if (elementCode != null && elementCode.equals(selectedCode)) {
						return i;
					}
				}
			}
			return null;
		}

		public VerifyHearingTypeButtonAction() {
			populateFromBundle("caseListingFixtureHearingVerify");
		}

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {
			Integer selectedIndex = getComboBoxModelIndexForCode(hearingTypeCombo.getModel(), hearingType.getText());
			hearingTypeCombo.setSelectedIndex(selectedIndex != null ? selectedIndex : 0);
		}
	}

	/**
	 * Action class for the Hearing Type Dropdown field
	 */
	private class HearingTypeComboBoxAction extends XAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			RefHearingTypeBasicValue selectedHearingType = (RefHearingTypeBasicValue) hearingTypeCombo
					.getSelectedItem();
			String errorMsg = MessageFormat.format(XHIBITConstant.getResource(XhibitBundles.ErrorText, 
					"listings.validation.hearingType.notKnown"), new Object[] { hearingType.getText()});
			hearingType.setText(selectedHearingType != null ? selectedHearingType.getHearingTypeCode() : null);
			if (!ValidationUtils.hasText(hearingType)) {
				XMessageBox.alert(parent,
						XHIBITConstant.getResource(XhibitBundles.XhibitConstant, "exception.validation.title"), true,
						XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			}

			setSaveEnabled();
		}
	}

	/**
	 * Enables/disables the Save button based on the data entered on screen.
	 * There must be at least one defendant attending and the listing
	 * date and hearing type field must be populated with a valid value.
	 */
	private void setSaveEnabled() {
		// First check status of validation controllers
		boolean valid = true;
		for (ValidationController<?> validationController : validationControllers) {
			if (validationController.hasErrors()) {
				valid = false;
				break;
			}
		}

		// If validation controllers okay, check status of defendants and hearing type
		if (valid) {
			valid = (isDefendantAttending() && hearingTypeCombo.getSelectedIndex() != 0);
		}

		// Only enable Save button if all fields valid
		getButtonPanel().okButton.setEnabled(valid);
	}
	
	/**
	 * Initialise the Fixture Notice Required and Court Site Panel
	 * @return JPanel
	 */
	private JPanel initfixtureNoticePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel fixtureNoticePanel = new JPanel();
		fixtureNoticePanel.setLayout(new GridBagLayout());
		
		// Add the panel elements
		gbc.weightx = 0.05;
		JLabel courtSiteLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCourtSite"));
		fixtureNoticePanel.add(courtSiteLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.3;
		courtSite = new XComboBox();
		setDropdownBoxArray(courtSite, model.getCourtSiteArray().toArray());
		fixtureNoticePanel.add(courtSite, gbc);

		gbc.gridx++;
		gbc.weightx = 0.65;
		fixtureNoticeReqdCheckBox = new JCheckBox(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureNotice"));
		fixtureNoticeReqdCheckBox.setSelected(true);
		fixtureNoticePanel.add(fixtureNoticeReqdCheckBox, gbc);
		return fixtureNoticePanel;
	}

	/**
	 * Initialise the List Note for Fixture panel
	 * @return JPanel
	 */
	private JPanel initListNotePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel listNotePanel = new JPanel();
		listNotePanel.setBorder(BorderFactory
				.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureListNote")));
		listNotePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		JLabel preDefinedLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailPreDefined"));
		listNotePanel.add(preDefinedLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.95;
		preDefined = new XComboBox();
		setDropdownBoxArray(preDefined, model.getPreDefinedNotesArray().toArray());
		listNotePanel.add(preDefined, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.05;
		JLabel freeTextLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailFreeText"));
		listNotePanel.add(freeTextLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.95;
		freeText = new MultiLineEditField(2,200);
		freeText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){			
					e.consume();
			}
			}
		});
		listNotePanel.add(freeText, gbc);

		return listNotePanel;
	}

	/**
	 * Move data from the model to the screen
	 */
	private void moveModelToScreen() throws CSRecoverableException {
		// Set the screen title	
		String dialogTitle = XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureTitle");
		parent.setTitle(dialogTitle.concat(" - ").concat(model.getCaseType()).concat(model.getCaseNumber().toString()));

		defendantTableModel.setRowCount(0);

		selectDatePanel.setDate(model.getFixture().getListingDate());
		
		Integer id = model.getFixture().getCourtSiteId();
		if (id != null) {
			courtSite.setSelectedItemByCode(id);
		}
		
		String noticeReqd = model.getFixture().getFixtureNoticeRequired();
		if ( noticeReqd != null ) {
			fixtureNoticeReqdCheckBox.setSelected( "Y".equals(noticeReqd) );
		}
		
		id = model.getFixture().getHearingTypeId();
		if (id != null) {
			hearingTypeCombo.setSelectedItemByCode(id);
			hearingType.setText(((RefHearingTypeBasicValue) hearingTypeCombo.getSelectedItem()).getHearingTypeCode());
		}

		id = model.getFixture().getListNotePreDefinedId();
		if (id != null) {
			preDefined.setSelectedItemByCode(model.getFixture().getListNotePreDefinedId());
		}

		freeText.setText(model.getFixture().getListNoteText());
		
		// Populate the Attending Defendants table
		if (model.getDefendants() != null && !model.getDefendants().isEmpty()) {
			setTableDefendants(selectDatePanel.getDate().getTime());
			defendantTable.getModel().addTableModelListener(new TableModelListener(){
				public void tableChanged(TableModelEvent e){
					setSaveEnabled();
				}
			});
		}
		setSaveEnabled();
	}

	/**
	 * Add the defendants to the table
	 */
	private void setTableDefendants(Date listingDate) {
		defendantTableModel.removeAllRows();
		Collection<FixtureDeftAttendingBasicValue> fixtureDeftAttending = model.getFixture().getFixtureDeftAttending();	
		for (DefendantValue row : model.getDefendants()) {
			Integer defendantOnCaseId = row.getDefOnCaseBasicValue().getDefendantOnCaseId();
			// Check if the defendant has been selected for the date on another fixture (if so ignore it)
			boolean includeRow = !isDefendantOnAnotherFixture(listingDate, defendantOnCaseId);
			boolean dealtWith = RESULTS_VERIFIED.equals(row.getDefOnCaseBasicValue().getResultsVerified());
			boolean attending = getAttending(dealtWith, fixtureDeftAttending, defendantOnCaseId);

			String formattedCustodyDate = null;
			if (row.getDefOnCaseBasicValue().getCustodyTimeLimit() != null) {
				formattedCustodyDate = getFormattedDate(
						row.getDefOnCaseBasicValue().getCustodyTimeLimit().getTime());
			}
			String defendantName = row.getFirstName() + " " + row.getSurName();
			defendantTableModel.addRow(includeRow, new Object[] { defendantName, attending, formattedCustodyDate });
		}
		// Select the first row
		if (defendantTable.getRowCount() > 0) {
			defendantTable.setRowSelectionInterval(0, 0);
		}
	}

	/**
	 * Determines whether a particular defendant is attending the fixture or not.  For new fixtures,
	 * defendants are ticked (attending) by default unless they have been 'dealt with'.  For existing
	 * fixtures, attendance is based upon previous selection except for new defendants who are unticked
	 * (not attending).
	 * @param dealtWith	boolean indicating if a defendant has been dealt with
	 * @param fixtureDeftAttendings	the list of FixtureDeftAttendingBasicValue objects on the fixture
	 * @param defendantOnCaseId	defendant on case id
	 * @return	true if the defendant is attending the fixture, else false
	 */
	private boolean getAttending(boolean dealtWith, Collection<FixtureDeftAttendingBasicValue> fixtureDeftAttendings,
			Integer defendantOnCaseId) {
		
		// Attending is unticked by default (means new defendants on existing fixtures won't be attending by default)
		boolean attending = false;
		if ( model.isEdit() ) {
			// Fixture screen in edit mode, for existing records, use previous saved value
			attending = isDefendantAttending(defendantOnCaseId, fixtureDeftAttendings);			
		}
		else {
			// When creating a new fixture, will be attending unless they have been 'dealt with'
			attending = !dealtWith;
		}

		return attending;
	}
	
	/**
	 * Determines whether or not at least one defendant is attending the fixture
	 * @return true if at least one defendant is attending the fixture, else false
	 */
	private boolean isDefendantAttending(){
		boolean attending = false ;
		for (int row = 0; row < defendantTableModel.getRowCount(); row++) {
			if ( defendantTableModel.isAttending(row) ) {
				attending = true;
				break;
			}
		}
		return attending;
	}
	
	/**
	 * Builds an Collection of FixtureDeftAttendingBasicValue objects based upon the contents of the
	 * Attending Defendants table
	 * @return ArrayList of FixtureDeftAttendingBasicValue objects
	 */
	private ArrayList<FixtureDeftAttendingBasicValue> getFixtureDeftAttendingList()
	{
		ArrayList<FixtureDeftAttendingBasicValue> newFixtureDeftAttendingList = new ArrayList<FixtureDeftAttendingBasicValue>();
		CaseDiaryFixtureComplexValue fixture = model.getFixture();
		Collection<FixtureDeftAttendingBasicValue> fixtureDeftAttendingList = fixture.getFixtureDeftAttending();
		
		// Loop through each row in the Attending Defendants table
		for (int row = 0; row < defendantTableModel.getRowCount(); row++) {
			// Retrieve the DefendantValue for the current table row
			DefendantValue defendantValue = ((List<DefendantValue>)model.getDefendants()).get(row);
			
			// Determine if the current defendant is attending or not
			String attending = defendantTableModel.isAttending(row) ? YES : NO;
			
			if ( fixtureDeftAttendingList == null || fixtureDeftAttendingList.isEmpty() )
			{
				// No FixtureDeftAttendingBasicValue objects present for the fixture so create new object
				newFixtureDeftAttendingList.add( createNewFixtureDeftAttending(defendantValue, fixture, attending) );
			}
			else
			{
				// There are existing FixtureDeftAttendingBasicValue, determine if the current defendant is one of them
				boolean found = false;
				for (FixtureDeftAttendingBasicValue fixtureDeftAttending : fixtureDeftAttendingList) {
					if ( fixtureDeftAttending.getDefendantOnCaseId().equals( 
							defendantValue.getDefOnCaseBasicValue().getDefendantOnCaseId() ) ) {
					
						found = true;
						fixtureDeftAttending.setAttending(attending);
						newFixtureDeftAttendingList.add(fixtureDeftAttending);
						break;
					}
				}
				
				if ( !found ) {
					// Matching FixtureDeftAttendingBasicValue not found in existing list so create new object
					newFixtureDeftAttendingList.add( createNewFixtureDeftAttending(defendantValue, fixture, attending) );
				}
			}
		}

		return newFixtureDeftAttendingList;
	}
	
	/**
	 * Creates a new FixtureDeftAttendingBasicValue object for a Defendant
	 * @param defendantValue The Defendant object
	 * @param fixture	The Fixture object
	 * @param attending	value to set attending to
	 * @return FixtureDeftAttendingBasicValue
	 */
	private FixtureDeftAttendingBasicValue createNewFixtureDeftAttending(DefendantValue defendantValue, 
			CaseDiaryFixtureComplexValue fixture, String attending)
	{
		FixtureDeftAttendingBasicValue fda = new FixtureDeftAttendingBasicValue();
		fda.setDefendantOnCaseId(defendantValue.getDefOnCaseBasicValue().getDefendantOnCaseId());
		if ( fixture.getCaseDiaryFixtureId() != null ) {
			fda.setCaseDiaryFixtureId(fixture.getCaseDiaryFixtureId());
		}
		fda.setAttending(attending);
		return fda;
	}
	
	/**
	 * Setup screen tab order
	 */
	private void configureTabOrder(){
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{
				selectDatePanel,
				hearingType,
				verifyHearingTypeButton,
				hearingTypeCombo,
				defendantTable,	
				courtSite, 
				fixtureNoticeReqdCheckBox,
				preDefined,
				freeText
		}));
	}

	/**
	 * Display the Cancel Confirmation popup message when exit screen
	 * @throws CSRecoverableException
	 */
	private void showCancelConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parent,
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCancelConfirmationTitle"), true,
				XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureCancelConfirmationMessage"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);

		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}

	/**
	 * Convert timestamp into formatted date (dd-MM-yyyy)
	 * @param timestamp Timestamp to convert
	 * @return Formatted Date
	 */
	private String getFormattedDate(Long timestamp) {
		return timestamp != null ? new SimpleDateFormat(DATE_FORMAT).format(timestamp) : null;
	}

	/**
	 * Class representing the Attending Defendants table model
	 */
	private class DefendantTableModel extends FilteredTableModel {

		private static final long serialVersionUID = 1L;

		public DefendantTableModel(Object[][] objects, String[] columnHeaders) {
			super(objects, columnHeaders);
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col == CUSTODYTIMElIMIT_COL_POSITION  || col == DEFENDANT_COL_POSITION) {
				return false;
			}
			return super.isCellEditable(row, col);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case ATTENDING_COL_POSITION:
				// Render the Attending column as a checkbox
				return Boolean.class;
			default:
				return super.getColumnClass(columnIndex);
			}
		}

		public Boolean isAttending(int rowNo) {
			return (Boolean) getValueAt(rowNo, ATTENDING_COL_POSITION) && isInclude(rowNo);
		}

	}
		
	/**
	 * Action class for clicking the Space Key on the Attending Defendants table
	 */
	private class AttendingDefendantsSpaceAction extends XAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {
			// Retrieve the currently selected row and the current value of the checkbox in that row
			int currentRow = defendantTable.getSelectedRow();
			String attending = defendantTableModel.isAttending(currentRow) ? YES : NO;
			
			if ( YES.equals(attending) ) {
				// Checkbox is currently ticked so untick it
				defendantTableModel.setValueAt(false, currentRow, ATTENDING_COL_POSITION);
			}
			else {
				// Checkbox is not currently ticked so tick it
				defendantTableModel.setValueAt(true, currentRow, ATTENDING_COL_POSITION);
			}
		}
	}

	/**
	 * Checks the fixture date against the list of sitting dates to see if valid
	 * @param date The date to check
	 * @return true if a sitting adte, else false
	 * @throws CSRecoverableException
	 */
	private boolean isSittingDate(Date date) throws CSRecoverableException {
		boolean isSittingDate = XhibitDelegateHelper.getBizRefDelegate().isCourtAvailableOnDate(
				XhibitSingleton.getInstance().getCourtId(), date);
		return isSittingDate;
	}

	/**
	 * Filtered Table Model
	 */
	private class FilteredTableModel extends DefaultTableModel {
		
		private static final long serialVersionUID = 1L;
		private List<Boolean> rowIncluded = new ArrayList<Boolean>();

		public FilteredTableModel(Object[][] objects, String[] columnHeaders) {
			super(objects, columnHeaders);
		}

		public void addRow(Boolean include, Object[] rowData) {
			rowIncluded.add(include);
			super.addRow(rowData);
		}
		
		@Override
		public void removeRow(int row) {
			rowIncluded.remove(row);
			super.removeRow(row);
		}
		
		public void removeAllRows() {
			rowIncluded.clear();
			super.setRowCount(0);
		}
		
		public boolean isInclude(int rowNo) {
			return rowIncluded.get(rowNo);
		}
	}

	/**
	 * Filtered Table Row Sorter
	 */
	public class FilteredTableRowSorter extends TableRowSorter<FilteredTableModel> {
		public FilteredTableRowSorter(FilteredTableModel model) {
			super(model);
			setRowFilter(new FilteredTableRowFilter());
		}
	}

	/**
	 * Filtered Table Row Filter
	 */
	public class FilteredTableRowFilter extends RowFilter<FilteredTableModel, Integer> {
		@Override
		public boolean include(RowFilter.Entry<? extends FilteredTableModel, ? extends Integer> entry) {	
			return entry.getModel().isInclude(entry.getIdentifier());
		}
	}
}
