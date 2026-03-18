/**
 * <p>
 * Title: CaseListingDetailsPanel
 * </p>
 * <p>
 * Changes: Added processing for clicking of non additional days button CTX-1313
 * @author Gurinder Brar
 */

package uk.gov.courtservice.xhibit.client.listings.details;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseStatusIndicator;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ComboHelperVO;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseListingEntryValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchListingJudgeAction;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownBoxCellRenderer;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryDialog;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryModel;
import uk.gov.courtservice.xhibit.client.listings.casesummary.DisplayOnlyField;
import uk.gov.courtservice.xhibit.client.listings.createlist.CreateListDialog;
import uk.gov.courtservice.xhibit.client.listings.createlist.CreateListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.listings.nonavailabledays.NonAvailableDaysDialog;
import uk.gov.courtservice.xhibit.client.listings.nonavailabledays.NonAvailableDaysModel;
import uk.gov.courtservice.xhibit.client.listings.notes.CaseNotesDialog;
import uk.gov.courtservice.xhibit.client.listings.notes.CaseNotesModel;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.MultiLineEditField;
import uk.gov.courtservice.xhibit.client.util.PageController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractComboBoxValidator;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.ComboBoxValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CaseListingDetailPanel extends XPanel implements ValidationListener {

	private static final int CUSTODY_TIME_LIMIT = 181;
	private static final int NOTE_MAX_LENGTH = 200;
	private static final long serialVersionUID = 1L;
	private static final int PROSECUTION_ID = 0;
	private static final String DATE_FORMAT = "dd-MMM-yyyy";
	private static final String EMPTY_STRING = "";
	private static final String[] VALID_FIXTURE_CASE_TYPES = new String[] { "T", "A", "S" };
	private static final Insets smallNonContainerInsets = new Insets(2,2,2,2);
	private static final String CTL_APPLIES_CODE_YES = "Y";
	private static final String CTL_APPLIES_CODE_NO = "N";

	private DisplayOnlyField caseTitleText;
	private MultiLineEditField highlightNote;
	private EditableComboBox highlightNoteClass;
	private ButtonGroup judgeTypeRadioButtonGroup;
	private JRadioButton highCourtRadioButton;
	private JRadioButton circuitJudgeRadioButton;
	private EditableCheckBox secureCheckBox;
	private EditableCheckBox videoLinkCheckBox;
	private ArrayList<RefListingDataBasicValue> preDefinedArray;
	private EditableComboBox preDefined;
	private MultiLineEditField freeText;
	private DisplayOnlyField ticketType;
	private EditableField timeEst;
	private Vector timeEstUnitArray;
	private EditableComboBox timeEstUnit;
	private ArrayList<CourtSiteBasicValue> courtSiteArray;
	private EditableComboBox courtSite;
	private ArrayList<RefListingDataBasicValue> noteClassArray;
	private EditableField interpreterNote;
	private DisplayOnlyField reqJudgeText;
	private EditableCheckBox reqJudgeCheckBox;
	private JButton reqJudgeSearchButton;
	private EditableField hearingTypeCode;
	private JButton verifyHearingTypeButton;
	private ArrayList<RefHearingTypeBasicValue> hearingTypeArray;
	private EditableComboBox defaultHearingType;
	private XTable fixturesTable;
	private DefaultTableModel fixturesTableModel;
	private JButton fixturesAddButton;
	private JButton fixturesEditButton;
	private JButton fixturesDeleteButton;
	private JButton addToListButton;
	private JButton caseNoteButton;
	private JButton nonAvailableDaysButton;
	private JButton caseSummaryButton;
	private JButton saveButton;
	private ArrayList<RefListingDataBasicValue> section28SelectionArray;
	private JPanel section28Panel;
	private XComboBox section28Selection;
	private Integer section28DefendantId;
	private EditableField section28Name1;
	private EditableField section28Name2;
	private EditableField section28Phone1;
	private EditableField section28Phone2;
	private ArrayList<DropdownCodeStringValue> bcStatusArray;
	private EditableComboBox bcStatus;
	private ArrayList<DropdownCodeStringValue> ctlAppliesArray;
	private EditableComboBox ctlApplies;
	private XDatePanelWithEvent custodyTimeLimit;
	private JLabel custodyTimeLimitText;
	private EditableCheckBox inCustodyCheckBox;
	private List<RefSystemCodeBasicValue> judgeTypesArray;

	private CaseListingDetailModel model;
	private CaseListingDetailDialog parent;

	private LocalPageController pageController = new LocalPageController();
	private DateValidationController custodyTimeLimitValidation;
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private ComboBoxValidationController ctlAppliesVC;

	private static final String BAIL = "B";
	private static final String CUSTODY = "C";
	private static final String INCARE = "J";
	private static final String NOTAPPLICABLE = "N";

	private static final Logger log = CSServices.getLogger(CaseListingDetailPanel.class);

	public CaseListingDetailPanel(CaseListingDetailDialog parent, CaseListingDetailModel model)
			throws CSRecoverableException {
		this.model = model;
		this.parent = parent;

		if (XAction.internalDebug) {
			log.debug("CaseId = " + this.model.getCaseId().toString());
		}

		stepInitialise();
		jbInit();
		configureTabOrder();
	}

	protected CaseListingDetailModel getModel() {
		return this.model;
	}

	private void jbInit() {

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(1000, 700));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);

		// Setup a main parent panel with vertical and horizonal scrollbars to
		// prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(950, 650));
		this.add(scrollPane, gbc);

		CustomButtonPanel buttonPanel = (CustomButtonPanel) parent.getButtonPanel();
		buttonPanel.setShowCancelButton(true);
		buttonPanel.setCancelText("Close");
		buttonPanel.setCancelToolTip("Close");
		addToListButton = buttonPanel.addButton("ListingDetailsAddToList", false, false);
		caseSummaryButton = buttonPanel.addButton("ListingDetailsCaseSummary", false, false);
		nonAvailableDaysButton = buttonPanel.addButton("ListingDetailsNonAvailDays", false, false);
		caseNoteButton = buttonPanel.addButton("ListingDetailsCaseNotes", false, false);
		saveButton = buttonPanel.addButton("ListingDetailsSave", false, true);
		pageController.setPageButtonsEnabled();

		// Content Panel Elements
		gbc.weighty = 0.05;
		JPanel caseTitlePanel = initCaseTitlePanel();
		mainPanel.add(caseTitlePanel, gbc);

		// Panel - Radio Buttons / Notes
		gbc.weighty = 0.25;
		gbc.gridy++;
		JPanel radioButtonAndNotePanel = initRadioButtonAndNotePanel();
		mainPanel.add(radioButtonAndNotePanel, gbc);

		// Panel - Interpreter / Judge Required
		gbc.weighty = 0.05;
		gbc.gridy++;
		JPanel interpreterAndJudgePanel = initInterpreterAndJudgePanel();
		mainPanel.add(interpreterAndJudgePanel, gbc);

		// Panel - Hearing Type
		gbc.weighty = 0.05;
		gbc.gridy++;
		JPanel hearingTypePanel = initHearingTypePanel();
		mainPanel.add(hearingTypePanel, gbc);

		// Panel - Section 28
		gbc.weighty = 0.3;
		gbc.gridy++;
		JPanel section28Panel = initSection28Panel();
		mainPanel.add(section28Panel, gbc);

		// Panel - Time Requirement
		gbc.gridy++;
		gbc.weighty = 0.3;
		JPanel fixturesPanel = initFixturesPanel();
		mainPanel.add(fixturesPanel, gbc);
	}

	private JPanel initRadioButtonAndNotePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, smallNonContainerInsets, 0, 0);
		JPanel radioButtonAndNotePanel = new JPanel();
		radioButtonAndNotePanel.setLayout(new GridBagLayout());

		// Panel - Judge Type Radio Buttons
		gbc.weightx = 0.25;
		gbc.weighty = 0.5;
		JPanel judgeTypePanel = initJudgeTypePanel();
		radioButtonAndNotePanel.add(judgeTypePanel, gbc);

		// Panel - Notes
		gbc.weightx = 0.75;
		gbc.weighty = 1;
		gbc.gridheight = 2;
		gbc.gridx++;
		JPanel notesPanel = initNotesPanel();
		radioButtonAndNotePanel.add(notesPanel, gbc);

		// Panel - Court Room Req Checkboxes
		gbc.weightx = 0.25;
		gbc.weighty = 0.5;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		JPanel courtRoomReqPanel = initCourtRoomReqPanel();
		radioButtonAndNotePanel.add(courtRoomReqPanel, gbc);

		return radioButtonAndNotePanel;
	}

	private JPanel initNotesPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel notePanel = new JPanel();
		notePanel.setLayout(new GridBagLayout());

		// Panel - Highlight Note
		gbc.weightx = 0.2;
		JPanel highlightNotePanel = initHighlightNotePanel();
		notePanel.add(highlightNotePanel, gbc);

		// Panel - Default List Note
		gbc.gridy++;
		gbc.weightx = 0.6;
		JPanel defaultNotePanel = initDefaultNotePanel();
		notePanel.add(defaultNotePanel, gbc);

		// Panel - Ticket Type
		gbc.gridy++;
		gbc.weightx = 0.2;
		JPanel ticketTypePanel = initTicketTypePanel();
		notePanel.add(ticketTypePanel, gbc);

		return notePanel;
	}

	private JPanel initInterpreterAndJudgePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel interpreterAndJudgePanel = new JPanel();
		interpreterAndJudgePanel.setLayout(new GridBagLayout());

		// Panel - Required Judge
		gbc.weightx = 0.5;
		JPanel judgeReqPanel = initJudgeReqPanel();
		interpreterAndJudgePanel.add(judgeReqPanel, gbc);

		// Panel - Interpreter Required
		gbc.gridx++;
		gbc.weightx = 0.5;
		JPanel interpreterReqPanel = initInterpreterReqPanel();
		interpreterAndJudgePanel.add(interpreterReqPanel, gbc);

		return interpreterAndJudgePanel;
	}

	private JPanel initHearingTypePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel hearingTypePanel = new JPanel();
		hearingTypePanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailDefaultHearingType")));
		hearingTypePanel.setLayout(new GridBagLayout());

		gbc.weightx = 0.25;
		hearingTypeCode = new EditableField();
		hearingTypeCode.setMaxLength(3);
		hearingTypeCode.setUpperCase(true);
		hearingTypePanel.add(hearingTypeCode, gbc);

		gbc.gridx++;
		gbc.weightx = 0.25;
		verifyHearingTypeButton = new JButton(new VerifyHearingTypeButtonAction());
		verifyHearingTypeButton.setEnabled(!model.isReadOnly());
		hearingTypePanel.add(verifyHearingTypeButton, gbc);

		if (hearingTypeArray == null) {
			hearingTypeArray = ListingDropdownPopulation.getHearingTypes();
		}
		gbc.gridx++;
		gbc.weightx = 0.50;
		defaultHearingType = new EditableComboBox();
		setDropdownBoxArray(defaultHearingType, hearingTypeArray.toArray());
		hearingTypePanel.add(defaultHearingType, gbc);
		defaultHearingType.addActionListener(new HearingTypeComboBoxAction());

		pageController.addChangeListeners(hearingTypePanel.getComponents());
		return hearingTypePanel;
	}

	private JPanel initSection28Panel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		section28Panel = new JPanel();
		section28Panel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28PanelTitle")));
		section28Panel.setLayout(new GridBagLayout());

		gbc.gridx = 0;
		gbc.weightx = 0.03;
		//gbc.anchor = GridBagConstraints.;
		JLabel selectionLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28Select"));
		section28Panel.add(selectionLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.97;
		JPanel section28DetailsPanel = initSection28DetailsPanel();
		section28Panel.add(section28DetailsPanel, gbc);
		
		gbc.weightx = 0.03;
		gbc.gridx = 0;
		gbc.gridy++;
		JLabel name1Label = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28Name1"));
		section28Panel.add(name1Label, gbc);

		gbc.gridx++;
		gbc.weightx = 0.97;
		JPanel advocate1Panel = initSection28Advocate1Panel();
		section28Panel.add(advocate1Panel, gbc);

		gbc.weightx = 0.03;
		gbc.gridx = 0;
		gbc.gridy++;
		JLabel name2Label = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28Name2"));
		section28Panel.add(name2Label, gbc);

		gbc.gridx++;
		gbc.weightx = 0.97;
		JPanel advocate2Panel = initSection28Advocate2Panel();
		section28Panel.add(advocate2Panel, gbc);

		return section28Panel;
	}

	private JPanel initSection28Advocate1Panel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel(new GridBagLayout());

		gbc.weightx = 0.55;
		gbc.gridx++;
		section28Name1 = new EditableField();
		section28Name1.setMaxLength(50);
		panel.add(section28Name1, gbc);

		gbc.weightx = 0.05;
		gbc.gridx++;
		JLabel phone1Label = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28Phone1"));
		panel.add(phone1Label, gbc);

		gbc.weightx = 0.40;
		gbc.gridx++;
		section28Phone1 = new EditableField();
		section28Phone1.setMaxLength(15);
		panel.add(section28Phone1, gbc);

		pageController.addChangeListeners(panel.getComponents());
		return panel;
	}

	private JPanel initSection28Advocate2Panel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel(new GridBagLayout());

		gbc.weightx = 0.55;
		gbc.gridx++;
		section28Name2 = new EditableField();
		section28Name2.setMaxLength(50);
		panel.add(section28Name2, gbc);

		gbc.weightx = 0.05;
		gbc.gridx++;
		JLabel phone2Label = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28Phone2"));
		panel.add(phone2Label, gbc);

		gbc.weightx = 0.40;
		gbc.gridx++;
		section28Phone2 = new EditableField();
		section28Phone2.setMaxLength(15);
		panel.add(section28Phone2, gbc);

		pageController.addChangeListeners(panel.getComponents());
		return panel;
	}

	private JPanel initSection28DetailsPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHWEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		gbc.weightx = 0.20;
		if (section28SelectionArray == null) {
			section28SelectionArray = getDefaultSection28SelectionArray();
		}
		section28Selection = new XComboBox();
		setDropdownBoxArray(section28Selection, section28SelectionArray.toArray());
		panel.add(section28Selection, gbc);
		section28Selection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					boolean isPageChanged = pageController.isPageChanged();
					if (section28DefendantId != null) {
						moveSection28ScreenToModel(section28DefendantId);
					}
					Integer newSelectedDefendantId = getSection28SelectedDefendantId();
					moveSection28ModelToScreen(newSelectedDefendantId);
					if (!isPageChanged) {
						pageController.reset();
					}
				} catch (CSRecoverableException ex) {
					XHIBITConstant.handleError(ex);
				}
			}
		});

		gbc.gridx++;
		gbc.weightx = 0.03;
		JLabel bcStatusLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28BcStatus"));
		panel.add(bcStatusLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.17;
		if (bcStatusArray == null) {
			bcStatusArray = ListingDropdownPopulation.getBcStatus();
		}
		bcStatus = new EditableComboBox();
		bcStatus.addActionListener(new BCStatusComboBoxAction());
		setDropdownBoxArray(bcStatus, bcStatusArray.toArray());
		// Field should be disabled for Miscellaneous Appeal cases
		if ( model.getCaseType().equals("A") && model.getCaseBasicValue().getCaseSubType().equals("O") ) {
			bcStatus.setEnabled(false);
			bcStatus.setFocusable(false);
		}
		panel.add(bcStatus, gbc);
		
		gbc.weightx = 0.05;
		gbc.gridx++;
		JLabel ctlAppliedLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28CustodyTimeLimitApplied"));
		panel.add(ctlAppliedLabel, gbc);
		
		ctlApplies = new EditableComboBox();
		gbc.gridx++;
		gbc.weightx = 0.20;
		if (ctlAppliesArray == null) {
			ctlAppliesArray = ListingDropdownPopulation.getCTLAppledValues();
		}

		ctlApplies.addActionListener(new CTLAppliesComboBoxAction());
		setDropdownBoxArray(ctlApplies, ctlAppliesArray.toArray());
		// Field should be disabled for Miscellaneous Appeal cases
		if ( model.getCaseType().equals("A") && model.getCaseBasicValue().getCaseSubType().equals("O") ) {
			ctlApplies.setEnabled(false);
			ctlApplies.setFocusable(false);
		}
		panel.add(ctlApplies, gbc);		
		ctlApplies.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					ctlAppliesVC.validate();
				}
			}
		);

		gbc.weightx = 0.03;
		gbc.gridx++;
		JLabel custodyTimeLimitLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28CustodyTimeLimit"));
		panel.add(custodyTimeLimitLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.17;
		custodyTimeLimit = new XDatePanelWithEvent(this, null, false) {

			private static final long serialVersionUID = 1L;

			private Timestamp getPreviousDate() {
				Timestamp result = null;
				Integer selectedDefendantId = getSection28SelectedDefendantId();
				if (selectedDefendantId != null) {
					DefendantValue selectedDefendantValue = getSelectedDefendantValue(selectedDefendantId);
					if (selectedDefendantValue != null && selectedDefendantValue.getDefOnCaseBasicValue() != null) {
						result = selectedDefendantValue.getDefOnCaseBasicValue().getCustodyTimeLimit();
					}
				}
				return result;
			}
			
			private boolean isDateUnchanged() {
				boolean result = false;
				try {
					Timestamp currentDate = getTimestamp();
					Timestamp previousDate = getPreviousDate();
					if (currentDate == null) {
						result = previousDate == null;
					} else {
						result = currentDate.equals(previousDate);
					}
				} catch (CSRecoverableException ex) {
					//Do not throw validation at this point
				}
				return result;
			}
			
			@Override
			protected void fireEvent() {
			   if (!isDateUnchanged()) {
				   pageController.setPageChanged();
				   custodyTimeLimitValidation.validate();
			   }
			}
		};
		// Field should be disabled for non-Trial cases
		enableCustodyTimeLimit(true);
		panel.add(custodyTimeLimit, gbc);
		custodyTimeLimitText = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28NoCustodyTimeLimit"));
		custodyTimeLimitText.setVisible(false);
		panel.add(custodyTimeLimitText, gbc);

		gbc.gridx++;
		gbc.weightx = 0.15;
		inCustodyCheckBox = new EditableCheckBox(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28InCustody"), model.isReadOnly());
		inCustodyCheckBox.setEnabled(false);
		inCustodyCheckBox.setFocusable(false);
		panel.add(inCustodyCheckBox, gbc);

			
		// Custody time limit warning label
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 1;
		
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridwidth = 3;
		JLabel custodyTimeLimitWarningLabel = new JLabel(" ");		
		panel.add(custodyTimeLimitWarningLabel, gbc);

		custodyTimeLimitValidation = ValidationControllerFactory.createDateValid(this,
				custodyTimeLimit, custodyTimeLimitWarningLabel, new AbstractDateValidator() {
					@Override
					public void validate(XDatePanel target, List<String> errors) {
						if (hasDate(target)) {
							Calendar calculatedCTLDate = getCalculatedCTLDate();
							if (hasDate(target) && target.isDateValidate()
									&& calculatedCTLDate != null
									&& getDate(target).before(calculatedCTLDate)) {
								errors.add("Earliest CTL date allowed for this defendant is "+ getFormattedDate(calculatedCTLDate));
							}
						}
					}
				});
		validationControllers.add(custodyTimeLimitValidation);
		
		gbc.gridx = 3;
		gbc.gridwidth = 5;
		JLabel ctlAppliesErrorLabel = new JLabel(" ");
		panel.add(ctlAppliesErrorLabel, gbc);

		ctlAppliesVC = ValidationControllerFactory.createComboBox(this, ctlApplies, ctlAppliesErrorLabel,
				new AbstractComboBoxValidator() {
					@Override
					public void validate(JComboBox target, List<String> errors) {
						if(isCTLAppliesEditable() && ctlApplies.getSelectedIndex() == 0){
							errors.add(XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28CustodyTimeLimitAppliesMandatory"));
						}
					}
				});
		
		validationControllers.add(ctlAppliesVC);
		
		//empty row
		gbc.gridy++;
		gbc.gridx = 0;
		//gbc.insets = XHIBITConstant.errorLabelInsets;
		//gbc.weightx = 1;
		JLabel emptyLabel = new JLabel(" ");		
		panel.add(emptyLabel, gbc);
		
		pageController.addChangeListeners(panel.getComponents(), new Component[] {section28Selection});
		return panel;
	}
	
	protected String getFormattedDate(Calendar date) {
		return XDateFormat.format(date, XDateFormat.DATEFORMAT);
	}
	
	private Calendar getCalculatedCTLDate() {
		Calendar cal = null;
		Integer selectedDefendantId = getSection28SelectedDefendantId();
		if (!Integer.valueOf(PROSECUTION_ID).equals(selectedDefendantId)) {
			DefendantValue defendant = getSelectedDefendantValue(selectedDefendantId);
			Date magCourtFirstHearingDate = defendant.getDefOnCaseBasicValue().getMagCourtFirstHearingDate();
			if (magCourtFirstHearingDate != null) {
				cal = Calendar.getInstance();
				cal.setTime( magCourtFirstHearingDate );
				cal.add(Calendar.DAY_OF_YEAR, CUSTODY_TIME_LIMIT);
			}
		}
		return cal;
	}

	private JPanel initTicketTypePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel ticketTypePanel = new JPanel();
		ticketTypePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.15;
		JLabel courtSiteLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCourtSite"));
		ticketTypePanel.add(courtSiteLabel, gbc);
		if (courtSiteArray == null) {
			courtSiteArray = ListingDropdownPopulation.getCourtSites();
		}
		gbc.gridx++;
		gbc.weightx = 0.20;
		courtSite = new EditableComboBox();
		setDropdownBoxArray(courtSite, courtSiteArray.toArray());
		ticketTypePanel.add(courtSite, gbc);

		gbc.gridx++;
		gbc.weightx = 0.15;
		JLabel ticketTypeLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailTicketType"));
		ticketTypePanel.add(ticketTypeLabel, gbc);
		gbc.gridx++;
		gbc.weightx = 0.15;
		ticketType = new DisplayOnlyField();
		ticketTypePanel.add(ticketType, gbc);

		gbc.gridx++;
		gbc.weightx = 0.10;
		JLabel timeEstLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailTimeEstimate"));
		ticketTypePanel.add(timeEstLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.15;
		timeEst = new EditableField();
		timeEst.setMaxLength(2);
		timeEst.setNumeric(true);
		ticketTypePanel.add(timeEst, gbc);

		gbc.gridx++;
		gbc.weightx = 0.10;
		if (timeEstUnitArray == null) {
			timeEstUnitArray = ListingDropdownPopulation.getTimeEstimateUnits();
		}
		timeEstUnit = new EditableComboBox();
		timeEstUnit.setModel(new DefaultComboBoxModel(timeEstUnitArray));
		ticketTypePanel.add(timeEstUnit, gbc);

		pageController.addChangeListeners(ticketTypePanel.getComponents());
		return ticketTypePanel;
	}

	private JPanel initCaseTitlePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, smallNonContainerInsets, 0, 0);
		JPanel caseTitlePanel = new JPanel();
		caseTitlePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		JLabel caseTitleLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCaseTitle"));
		caseTitlePanel.add(caseTitleLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.95;
		caseTitleText = new DisplayOnlyField();
		caseTitlePanel.add(caseTitleText, gbc);

		return caseTitlePanel;
	}

	private JPanel initJudgeTypePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, smallNonContainerInsets, 0, 0);
		JPanel judgeTypePanel = new JPanel();
		judgeTypePanel.setBorder(BorderFactory
				.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailJudgeType")));
		judgeTypePanel.setLayout(new GridBagLayout());

		judgeTypeRadioButtonGroup = new ButtonGroup();

		// Add the panel elements
		highCourtRadioButton = new JRadioButton(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailHighCourt"));
		highCourtRadioButton.setEnabled(!model.isReadOnly());
		judgeTypeRadioButtonGroup.add(highCourtRadioButton);
		judgeTypePanel.add(highCourtRadioButton, gbc);

		gbc.gridy++;
		circuitJudgeRadioButton = new JRadioButton(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCircuitJudge"));
		circuitJudgeRadioButton.setEnabled(!model.isReadOnly());
		judgeTypeRadioButtonGroup.add(circuitJudgeRadioButton);
		judgeTypePanel.add(circuitJudgeRadioButton, gbc);

		pageController.addChangeListeners(judgeTypePanel.getComponents());
		return judgeTypePanel;
	}

	private JPanel initHighlightNotePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel highlightNotePanel = new JPanel();
		highlightNotePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridwidth = 2;
		JLabel highlightErrorLabel = new JLabel(" ");
		highlightNotePanel.add(highlightErrorLabel, gbc);
		gbc.gridwidth = 1;
		gbc.insets = XHIBITConstant.nonContainerInsets;

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.05;
		JLabel highlightNoteLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailHighlightNote"));
		highlightNotePanel.add(highlightNoteLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.80;
		highlightNote = new MultiLineEditField(2, 200);
		highlightNotePanel.add(highlightNote, gbc);

		gbc.gridx++;
		gbc.weightx = 0.15;
		highlightNoteClass = new EditableComboBox(true);
		setDropdownBoxArray(highlightNoteClass, getNoteClassArray().toArray());
		highlightNotePanel.add(highlightNoteClass, gbc);

		pageController.addChangeListeners(highlightNotePanel.getComponents());
		return highlightNotePanel;
	}

	private JPanel initDefaultNotePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel defaultNotePanel = new JPanel();
		defaultNotePanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailDefaultListNote")));
		defaultNotePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.05;
		JLabel preDefinedLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailPreDefined"));
		defaultNotePanel.add(preDefinedLabel, gbc);
		if (preDefinedArray == null) {
			preDefinedArray = ListingDropdownPopulation.getPreDefinedNotes();
		}
		gbc.gridx++;
		gbc.weightx = 0.95;
		preDefined = new EditableComboBox();
		setDropdownBoxArray(preDefined, preDefinedArray.toArray());
		defaultNotePanel.add(preDefined, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.05;
		JLabel freeTextLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailFreeText"));
		defaultNotePanel.add(freeTextLabel, gbc);

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
		defaultNotePanel.add(freeText, gbc);

		pageController.addChangeListeners(defaultNotePanel.getComponents());
		return defaultNotePanel;
	}
	
	private JPanel initCourtRoomReqPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, smallNonContainerInsets, 0, 0);
		JPanel courtRoomReqPanel = new JPanel();
		courtRoomReqPanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCourtRoomRequirement")));
		courtRoomReqPanel.setLayout(new GridBagLayout());

		// Add the panel elements
		secureCheckBox = new EditableCheckBox(XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSecure"), model.isReadOnly());
		courtRoomReqPanel.add(secureCheckBox, gbc);

		gbc.gridy++;
		videoLinkCheckBox = new EditableCheckBox(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailVideoLink"), model.isReadOnly());
		courtRoomReqPanel.add(videoLinkCheckBox, gbc);

		pageController.addChangeListeners(courtRoomReqPanel.getComponents());
		return courtRoomReqPanel;
	}

	private JPanel initInterpreterReqPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel interpreterReqPanel = new JPanel();
		interpreterReqPanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailInterpreter")));
		interpreterReqPanel.setLayout(new GridBagLayout());

		// Add the panel elements
		interpreterNote = new EditableField();
		interpreterNote.setMaxLength(NOTE_MAX_LENGTH);
		interpreterReqPanel.add(interpreterNote, gbc);

		pageController.addChangeListeners(interpreterReqPanel.getComponents());
		return interpreterReqPanel;
	}

	private JPanel initJudgeReqPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel reqJudgePanel = new JPanel();
		reqJudgePanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailRequiredJudge")));
		reqJudgePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		reqJudgeCheckBox = new EditableCheckBox(null,model.isReadOnly());
		reqJudgeCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleReqJudge();
			}
		});
		reqJudgePanel.add(reqJudgeCheckBox, gbc);

		gbc.gridx++;
		gbc.weightx = 0.75;
		reqJudgeText = new DisplayOnlyField();
		reqJudgePanel.add(reqJudgeText, gbc);

		gbc.gridx++;
		gbc.weightx = 0.20;
		XAction openSearchListingJudgeAction = XhibitActions.getAction(model.getXac(),
				XhibitActions.OpenSearchListingJudge);
		openSearchListingJudgeAction.setCaller(this);
		reqJudgeSearchButton = new JButton(openSearchListingJudgeAction);
		reqJudgePanel.add(reqJudgeSearchButton, gbc);

		pageController.addChangeListeners(reqJudgePanel.getComponents());
		return reqJudgePanel;
	}

	private JPanel initFixturesPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel fixturesPanel = new JPanel();
		fixturesPanel.setBorder(BorderFactory
				.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailFixtures")));
		fixturesPanel.setLayout(new GridBagLayout());

		// Table
		JPanel tablePanel = initTablePanel();
		gbc.weightx = 0.99;
		fixturesPanel.add(tablePanel, gbc);

		// Add the Button panel
		gbc.gridx++;
		gbc.weightx = 0.01;
		JPanel tableButtonPanel = initTableButtonPanel();
		fixturesPanel.add(tableButtonPanel, gbc);

		return fixturesPanel;
	}

	private JPanel initTablePanel() {
		String[] columnHeaders = new String[] {
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailListingDate"),
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailHearingType"),
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailListingNote") };
		GridBagConstraints gbc = new GridBagConstraints(0, 0, columnHeaders.length, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		fixturesTableModel = new DefaultTableModel(new Object[][] {}, columnHeaders)  {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		fixturesTable = XTableFactory.getInstance().createDefaultTable(fixturesTableModel);
		TableUtils.setupDefaultsOnJTable(fixturesTable);
		
		//Set the column widths.
		setFixtureTableColumnWidth();
		
		fixturesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				pageController.setFixtureButtonsEnabled();
			}
		});

		// Add Table to Scroll Pane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(gbc.gridwidth, 100));
		scrollPane.setViewportView(fixturesTable);
		tablePanel.add(scrollPane, gbc);

		return tablePanel;
	}
	
	private void setFixtureTableColumnWidth() {
		fixturesTable.getColumnModel().getColumn(0).setPreferredWidth(95);
		fixturesTable.getColumnModel().getColumn(1).setPreferredWidth(350);
		fixturesTable.getColumnModel().getColumn(2).setPreferredWidth(475);
		fixturesTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	}

	private JPanel initTableButtonPanel() {
		GridBagConstraints gbc =new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		JPanel tableButtonPanel = new JPanel();
		tableButtonPanel.setLayout(new GridBagLayout());

		// Add the panel elements
		fixturesAddButton = new JButton(new FixturesAddButtonAction(this));
		tableButtonPanel.add(fixturesAddButton, gbc);

		gbc.gridy++;
		fixturesEditButton = new JButton(new FixturesEditButtonAction(this));
		tableButtonPanel.add(fixturesEditButton, gbc);
		gbc.gridy++;
		fixturesDeleteButton = new JButton(new FixturesDeleteButtonAction(this));
		tableButtonPanel.add(fixturesDeleteButton, gbc);

		pageController.addChangeListeners(tableButtonPanel.getComponents());
		return tableButtonPanel;
	}

	/**
	 * Set the array and if the array is empty disable the dropdown box
	 */
	private void setDropdownBoxArray(XComboBox comboBox, final Object[] arrayItems) {
		comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
			comboBox.setRenderer(new ListingDropdownBoxCellRenderer());
			comboBox.enableAutoSelect();
		} else {
			comboBox.setEnabled(false);
		}
	}

	/*
	 * Display events
	 */
	private void toggleReqJudge() {
		reqJudgeText.setEnabled(reqJudgeCheckBox.isSelected());
		reqJudgeSearchButton.setEnabled(reqJudgeCheckBox.isSelected());
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		boolean invalid = (validationController.hasErrors()
				|| !ValidationControllerFactory.validateComponents(validationControllers));
		saveButton.setEnabled(!invalid && pageController.isPageChanged());
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		retrieveDataForModel();
	}
	
	/**
	 * Retrieves data for the screen and stores it in the data model
	 * @throws CSRecoverableException
	 */
	private void retrieveDataForModel() throws CSRecoverableException {
		// Get the data from the database
		CaseListingEntryComplexValue caseListingEntry = XhibitDelegateHelper.getListingsDelegate()
				.getCaseListingEntryByCaseIdAndCourtId(model.getCaseId(), model.getCourtId());

		// Populate the model for use in the screen
		populateModel(caseListingEntry);
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();

		// Initialise display
		stepUpdateViewState();
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		toggleReqJudge();
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
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (update) {
			if (saveButton.equals(getDeinitialiseSource())) {
				CaseListingEntryValue caseListingEntryValue = new CaseListingEntryValue(
						model.getCaseListingEntryBasicValue(), model.getCaseBasicValue(),
						model.getHighlightDiaryNoteEntry(), model.getInterpreterDiaryNoteEntry(),
						model.getPreDefinedDiaryNoteEntry(), model.getFreeTextDiaryNoteEntry(),
						model.getDirectionsForCase(), model.getDefendants());
				XhibitDelegateHelper.getListingsDelegate().saveCaseListingEntry(caseListingEntryValue, XhibitSingleton
						.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				
				// Retrieve the details from the database and repopulate the model
				retrieveDataForModel();
				
				// Reset the page listener flag
				pageController.reset();
			}
		} else {
			if (caseNoteButton.equals(getDeinitialiseSource())) {
				if (validateCase()) {
					// Display the notes screen
					CaseNotesModel notesModel = new CaseNotesModel(model.getCaseId(), model.getCaseType(),
							model.getCaseNumber(), model.getCaseListingEntryId(), model.getCaseBasicValue().getDateTransTo());
					CaseNotesDialog notesDialog = new CaseNotesDialog(parent, notesModel);
					notesDialog.setVisible(true);
					
					if (notesDialog.getModel().isDataChanged()) {
						// Refresh the screen
						retrieveDataForModel();
						moveModelToScreen();
					}
				}
			} else if (addToListButton.equals(getDeinitialiseSource())) {
				CreateListDialog createListDialog = new CreateListDialog(parent, new CreateListModel());
				createListDialog.setVisible(true);
			} else if (caseSummaryButton.equals(getDeinitialiseSource())) {
				CaseSummaryModel summaryModel = new CaseSummaryModel(model.getCaseId());
				summaryModel.setFromListScreen(parent.isFromListScreen());
				CaseSummaryDialog caseSummaryDialog = new CaseSummaryDialog(parent, summaryModel);
				caseSummaryDialog.setVisible(true);
				if (summaryModel.isDataChanged()) {
					// Refresh the screen
					retrieveDataForModel();
					moveModelToScreen();
				}
			} else if (nonAvailableDaysButton.equals(getDeinitialiseSource())) {
				NonAvailableDaysModel nonAvailableDaysModel = new NonAvailableDaysModel(model.getCaseId(),
						model.getCaseType(), model.getCaseNumber(), model.getCaseListingEntryId());
				NonAvailableDaysDialog nonAvailableDaysDialog = new NonAvailableDaysDialog(parent,
						nonAvailableDaysModel);
				nonAvailableDaysDialog.setVisible(true);
				nonAvailableDaysDialog.setResizable(true);
			} else {
				if (pageController.isPageChanged()) {
					showCancelConfirmationMsg();
				}
			}
		}
	}

	protected final void populateModel(CaseListingEntryComplexValue caseListingEntry) throws CSRecoverableException {
		model.clearmodel();

		String defaultClassificationCode = XHIBITConstant.getResource(XhibitBundles.Listings,
				"listingDropdownDefaultNoteClassification");
		String defaultHighlightClassificationCode = XHIBITConstant.getResource(XhibitBundles.Listings,
				"listingDropdownDefaultHighlightNoteClassification");

		// Case Listing Entry elements
		model.setCaseListingEntryBasicValue((CaseListingEntryBasicValue) caseListingEntry);

		// Ref Judge Type
		model.setRefJudgeType(caseListingEntry.getRefJudgeType());

		// Ref Judge
		model.setRefJudge(caseListingEntry.getRefJudge());

		//Ticket Type
		model.setTicketType(caseListingEntry.getTicketType());
		
		// Case elements
		model.setCaseBasicValue(caseListingEntry.getCaseBasicValue());

		// Directions For Case elements
		model.setDirectionsForCase(caseListingEntry.getDirectionsForCase());

		// Get the note types
		RefListingDataBasicValue highlightNoteType = caseListingEntry.getHighlightNoteType();
		RefListingDataBasicValue defaultCaseNoteType = caseListingEntry.getDefaultCaseNoteType();
		RefListingDataBasicValue interpreterNoteType = caseListingEntry.getInterpreterNoteType();

		// Highlight Note elements
		DiaryNoteEntryBasicValue highlightDiaryNoteEntry = caseListingEntry.getHighlightDiaryNoteEntry();
		if (highlightDiaryNoteEntry == null) {
			highlightDiaryNoteEntry = initialiseDiaryNoteEntry(highlightNoteType.getRefListingDataId(),
					defaultHighlightClassificationCode);
			}
		model.setHighlightDiaryNoteEntry(highlightDiaryNoteEntry);

		// Pre-defined Note elements
		DiaryNoteEntryBasicValue preDefinedDiaryNoteEntry = caseListingEntry.getPreDefinedDiaryNoteEntry();
		if (preDefinedDiaryNoteEntry ==  null) {
			preDefinedDiaryNoteEntry = initialiseDiaryNoteEntry(defaultCaseNoteType.getRefListingDataId(),
					defaultClassificationCode);
			}
		model.setPreDefinedDiaryNoteEntry(preDefinedDiaryNoteEntry);

		// Free Text elements
		DiaryNoteEntryBasicValue freeTextDiaryNoteEntry = caseListingEntry.getFreeTextDiaryNoteEntry();
		if (freeTextDiaryNoteEntry == null) {
			freeTextDiaryNoteEntry = initialiseDiaryNoteEntry(defaultCaseNoteType.getRefListingDataId(),
					defaultClassificationCode);
		}
		model.setFreeTextDiaryNoteEntry(freeTextDiaryNoteEntry);

		// Interpreter elements
		DiaryNoteEntryBasicValue interpreterDiaryNoteEntry = caseListingEntry.getInterpreterDiaryNoteEntry();
		if (interpreterDiaryNoteEntry == null) {
			interpreterDiaryNoteEntry = initialiseDiaryNoteEntry(interpreterNoteType.getRefListingDataId(),
					defaultHighlightClassificationCode);
		}
		model.setInterpreterDiaryNoteEntry(interpreterDiaryNoteEntry);

		// Defendants
		model.setDefendants(caseListingEntry.getDefendants());

		// Set Fixtures
		model.setFixtures(caseListingEntry.getFixtures());
	}

	@SuppressWarnings("unchecked")
	/**
	 * Refreshes the diary fixtures by retrieving them from the database
	 * 
	 * @param caseListingEntryId
	 *            Case Listing Entry Id to retrieve fixtures for
	 */
	private void refreshDiaryFixtures(Integer caseListingEntryId) {
		Collection<CaseDiaryFixtureComplexValue> newFixtures;
		try {
			newFixtures = XhibitDelegateHelper.getListingsDelegate()
					.findPendingCaseDiaryFixturesByCaseEntryId(caseListingEntryId);
			if (newFixtures.isEmpty()) {
				Collection emptyList = new ArrayList();
				model.setFixtures(emptyList);
			} else {
				model.setFixtures(newFixtures);
			}
		} catch (ListingsControllerException ex) {
			XHIBITConstant.handleError(ex);
		}
	}

	private DiaryNoteEntryBasicValue initialiseDiaryNoteEntry(Integer noteTypeId, String defaultClassValue) {
		DiaryNoteEntryBasicValue basicValue = new DiaryNoteEntryBasicValue();
		basicValue.setCaseListingEntryId(model.getCaseListingEntryId());
		basicValue.setCourtId(model.getCourtId());
		basicValue.setNoteTypeId(noteTypeId);
		RefListingDataBasicValue defaultNoteClassification = getClassificationByValue(defaultClassValue);
		basicValue.setNoteClassificationId(
				defaultNoteClassification != null ? defaultNoteClassification.getRefListingDataId() : null);
		return basicValue;
	}

	private Integer getSection28SelectedDefendantId() {
		Integer selectedDefendantId = null;
		if (section28Selection != null) {
			selectedDefendantId = ((RefListingDataBasicValue) section28Selection.getSelectedItem())
					.getRefListingDataId();
		}
		return selectedDefendantId != null ? selectedDefendantId : PROSECUTION_ID;
	}

	private void moveModelToScreen() {
		// Disable the validation checking
		pageController.setEnabled(false);
		// Set the screen title
		String dialogTitle = XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailTitle");
		parent.setTitle(dialogTitle.concat(" - ").concat(model.getCaseType()).concat(model.getCaseNumber().toString()));

		caseTitleText.setText(model.getCaseTitle());
		secureCheckBox.setSelected(model.getSecureCourtAsBoolean());
		videoLinkCheckBox.setSelected(model.getVideoLinkRequiredAsBoolean());
		interpreterNote.setText(model.getInterpreterNote());
		highlightNote.setText(model.getHighlightNote());
		freeText.setText(model.getFreeText());
		reqJudgeText.setText(model.getReqJudge());
		reqJudgeCheckBox.setSelected(model.getReqJudge() != null);
        timeEst.setText(model.getTimeEst());
		ticketType.setText(model.getTicketType() != null ? model.getTicketType().getDecode() : null);
		// Set the comboboxes
     	preDefined.setSelectedItemByCode(model.getPreDefinedNoteTypeId());
     	highlightNoteClass.setSelectedItemByCode(model.getHighlightClassificationId());
		highlightNoteClass.setEditable(false);
        courtSite.setSelectedItemByCode(model.getCourtSiteId());
        timeEstUnit.setSelectedItemByCode(model.getTimeEstUnit());
        defaultHearingType.setSelectedItemById(model.getDefaultHearingTypeId());
        if (defaultHearingType.getSelectedIndex() == 0) {
			RefHearingTypeBasicValue selectedHearingType = (RefHearingTypeBasicValue) defaultHearingType
					.getSelectedItem();
			if (selectedHearingType != null) {
				hearingTypeCode.setText(selectedHearingType.getHearingTypeCode());
			}
		}
		// Add the defendants to section 28 selector
		if (!model.getDefendants().isEmpty()) {
			for (DefendantValue defendant : model.getDefendants()) {
				RefListingDataBasicValue defendantOption = createSection28SelectionOption(defendant.getDefendantID(),
						defendant.getFirstName() + " " + defendant.getSurName());
				section28SelectionArray.add(section28SelectionArray.size() - 1, defendantOption);
			}

			setDropdownBoxArray(section28Selection, section28SelectionArray.toArray());
		}
		// Set the radio buttons
		if (model.isHighCourtJudgeType()) {
			highCourtRadioButton.setSelected(true);
		} else if (model.isCircuitJudgeType()) {
			circuitJudgeRadioButton.setSelected(true);
		} else {
			// Default the initial radio button value if not previously set
			String defaultJudgeType = model.getCaseBasicValue().getDefaultJudgeTypeCode();
			if ( CaseListingDetailModel.JudgeTypes.HIGHCOURT_JUDGE.equals(defaultJudgeType) ) {
				highCourtRadioButton.setSelected(true);
			}
			else if ( CaseListingDetailModel.JudgeTypes.CIRCUIT_JUDGE.equals(defaultJudgeType) ) {
				circuitJudgeRadioButton.setSelected(true);
			}
		}

		// Populate the initial section 28 view
		moveSection28ModelToScreen(getSection28SelectedDefendantId());

		// Set the Fixtures Table
		moveModelToFixturesTable();

		pageController.reset();
	}

	private void moveModelToFixturesTable() {
		fixturesTableModel.setRowCount(0);
		if (model.getFixtures() != null && !model.getFixtures().isEmpty()) {
			for (CaseDiaryFixtureComplexValue fixture : (Collection<CaseDiaryFixtureComplexValue>) model
					.getFixtures()) {
				String formattedListingDate = getFormattedDate(fixture.getListingDate());
				fixturesTableModel.addRow(new Object[] { formattedListingDate, fixture.getHearingTypeDesc(),
						fixture.getDisplayListNoteText() });
			}
			fixturesTable.setRowSelectionInterval(0, 0);
			if (!fixturesTable.getSelectionModel().isSelectionEmpty()) {
				fixturesTable.getSelectionModel().clearSelection();
			}
		}
	}

	private RefSystemCodeBasicValue getSelectedJudgeType() {
		RefSystemCodeBasicValue result = null;
		if (judgeTypesArray == null) {
			judgeTypesArray = ListingDropdownPopulation.getJudgeTypes();
		}
		for (RefSystemCodeBasicValue judgeType : judgeTypesArray) {
				if (highCourtRadioButton.isSelected()
						&& CaseListingDetailModel.JudgeTypes.HIGHCOURT_JUDGE.equals(judgeType.getCode())) {
				result = judgeType;
				break;
				} else if (circuitJudgeRadioButton.isSelected()
						&& CaseListingDetailModel.JudgeTypes.CIRCUIT_JUDGE.equals(judgeType.getCode())) {
				result = judgeType;
				break;
			}
		}
		return result;
	}

	private void moveScreenToModel() throws CSRecoverableException {
		// Update the model from the screen
		Integer selectedId;
		model.setSecureCourtAsBoolean(secureCheckBox.isSelected());
		model.setVideoLinkRequiredAsBoolean(videoLinkCheckBox.isSelected());
		model.setCourtSiteId(((CourtSiteBasicValue) courtSite.getSelectedItem()).getId());
		model.setRefJudgeType(getSelectedJudgeType());
		if (!reqJudgeCheckBox.isSelected()) {
			model.setRefJudge(null);
		}
		model.setHighlightNote(highlightNote.getText());
		selectedId = ((RefListingDataBasicValue) highlightNoteClass.getSelectedItem()).getId();
		model.setHighlightNoteClassificationId(selectedId);
		model.setInterpreterNote(interpreterNote.getText());
		model.setInterpreterNoteClassificationId(selectedId);
		selectedId = ((RefListingDataBasicValue) preDefined.getSelectedItem()).getId();
		model.setPreDefinedNoteTypeId(selectedId);
		model.setFreeTextNote(freeText.getText());
		model.setTimeEst(timeEst.getText());
		selectedId = Integer.valueOf(((ComboHelperVO) timeEstUnit.getSelectedItem()).getDbValue());
		model.setTimeEstUnit(selectedId);
		selectedId = ((RefHearingTypeBasicValue) defaultHearingType.getSelectedItem()).getId();
		model.setDefaultHearingTypeId(selectedId);
		moveSection28ScreenToModel(section28DefendantId);
	}

	private void moveSection28ScreenToModel(final Integer selectedDefendantId) throws CSRecoverableException {
		if (!Integer.valueOf(PROSECUTION_ID).equals(selectedDefendantId)) {
			// Defendants
			if (model.getDefendants() != null && !model.getDefendants().isEmpty()) {
				for (DefendantValue defendant : model.getDefendants()) {
					if (defendant.getDefendantID() != null && defendant.getDefendantID().equals(selectedDefendantId)) {
						DefendantOnCaseBasicValue defOnCase = defendant.getDefOnCaseBasicValue();
						defOnCase.setSection28Name1(section28Name1.getText());
						defOnCase.setSection28Name2(section28Name2.getText());
						defOnCase.setSection28Phone1(section28Phone1.getText());
						defOnCase.setSection28Phone2(section28Phone2.getText());
						defOnCase.setCurrentBcStatus(
								bcStatus.getSelectedIndex() > 0 ? getSelectedBCStatus().getCode() : null);
						defOnCase.setCtlApplies(
								ctlApplies.getSelectedIndex() > 0 ? getSelectedCTLAppliesValue().getCode() : null);
						defOnCase.setCustodyTimeLimit(
								ValidationUtils.hasLength(custodyTimeLimit) && isCustodyTimeLimitEditable()
										? custodyTimeLimit.getTimestamp() : null);
						defendant.setCurrentPrisonStatus(model.getStringFromBoolean(inCustodyCheckBox.isSelected()));
						break;
					}
				}
			}
		} else {
			// General
			model.setCaseSection28Name1(section28Name1.getText());
			model.setCaseSection28Name2(section28Name2.getText());
			model.setCaseSection28Phone1(section28Phone1.getText());
			model.setCaseSection28Phone2(section28Phone2.getText());
		}
	}

	private DefendantValue getSelectedDefendantValue(final Integer selectedDefendantId) {
		DefendantValue result = null;
		if (!Integer.valueOf(PROSECUTION_ID).equals(selectedDefendantId)) {
			if (model.getDefendants() != null && !model.getDefendants().isEmpty()) {
				for (DefendantValue defendant : model.getDefendants()) {
					if (defendant.getDefendantID() != null && defendant.getDefendantID().equals(selectedDefendantId)) {
						result = defendant;
						break;
					}
				}
			}
		}
		return result;
	}
	
	private void moveSection28ModelToScreen(final Integer selectedDefendantId) {
		if (!Integer.valueOf(PROSECUTION_ID).equals(selectedDefendantId)) {
			// Defendants
			DefendantValue defendant = getSelectedDefendantValue(selectedDefendantId);
			if (defendant != null) {
				DefendantOnCaseBasicValue defOnCase = defendant.getDefOnCaseBasicValue();
				section28DefendantId = defendant.getDefendantID();
				section28Name1.setText(defOnCase.getSection28Name1());
				section28Name2.setText(defOnCase.getSection28Name2());
				section28Phone1.setText(defOnCase.getSection28Phone1());
				section28Phone2.setText(defOnCase.getSection28Phone2());
				bcStatus.setSelectedItemByCode(
						defOnCase.getCurrentBcStatus() != null ? defOnCase.getCurrentBcStatus() : "");
				ctlApplies.setSelectedItemByCode(
						defOnCase.getCtlApplies() != null ? defOnCase.getCtlApplies() : "");
				custodyTimeLimit.setTimestamp(defOnCase.getCustodyTimeLimit());
				inCustodyCheckBox.setSelected(model.getBooleanFromString(defendant.getCurrentPrisonStatus()));
			}
			setSection28DetailsPanelEnabled(true);
		} else {
			// Prosecution
			section28DefendantId = PROSECUTION_ID;
			section28Name1.setText(model.getCaseSection28Name1());
			section28Name2.setText(model.getCaseSection28Name2());
			section28Phone1.setText(model.getCaseSection28Phone1());
			section28Phone2.setText(model.getCaseSection28Phone2());
			bcStatus.setSelectedIndex(0);
			ctlApplies.setSelectedIndex(0);
			custodyTimeLimit.clear();
			inCustodyCheckBox.setSelected(false);
			setSection28DetailsPanelEnabled(false);
		}
	}

	private void setSection28DetailsPanelEnabled(boolean enabled) {
		
		if ( !model.getCaseType().equals("A") || 
			(model.getCaseType().equals("A") && !model.getCaseBasicValue().getCaseSubType().equals("O")) ) {
			// BC Status can only be enabled if the case is not a Miscellaneous Appeal case
			bcStatus.setEnabled(enabled);
			bcStatus.setFocusable(enabled);
		}
		enableCTLApplies(enabled);
		enableCustodyTimeLimit(enabled);
	}

	private void enableCustodyTimeLimit(boolean enabled) {
		boolean enableField = enabled && model.getCaseType().equals("T") && !model.isReadOnly();
		// Custody Time Limit can only be enabled for Trial cases 
		custodyTimeLimit.setEnabled(enableField);
		custodyTimeLimit.setFocusable(enableField);
	}

	private void enableCTLApplies(boolean enabled) {
		boolean enableField = enabled && model.getCaseType().equals("T") && !model.isReadOnly();
		if(enableField){
			enableField = isCTLAppliesEditable();
		}

		ctlApplies.setEnabled(enableField);
		ctlApplies.setFocusable(enableField);		
	}

	private String getFormattedDate(Date date) {
		return date != null ? new SimpleDateFormat(DATE_FORMAT).format(date.getTime()) : null;
	}

	private RefListingDataBasicValue getClassificationByValue(String value) {
		RefListingDataBasicValue result = null;
		if (value != null) {
			for (RefListingDataBasicValue classification : getNoteClassArray()) {
				if (value.equals(classification.getRefDataValue())) {
					result = classification;
				}
			}
		}
		return result;
	}

	private DropdownCodeStringValue getSelectedBCStatus() {
		return (DropdownCodeStringValue) bcStatus.getSelectedItem();
	}
	
	private DropdownCodeStringValue getSelectedCTLAppliesValue() {
		return (DropdownCodeStringValue) ctlApplies.getSelectedItem();
	}

	private boolean isCustodyTimeLimitEditable() {
		return !BAIL.equals(getSelectedBCStatus().getCode()) &&
				!NOTAPPLICABLE.equals(getSelectedBCStatus().getCode()) &&
						!"".equals(getSelectedBCStatus().getCode())
						&& model.getCaseType().equals("T");
	}
	
	private void setCustodyTimeLimitEditable() {
		boolean isEditable = isCustodyTimeLimitEditable();
		custodyTimeLimit.setVisible(isEditable);
		custodyTimeLimitText.setVisible(!isEditable);
	}
	
	private void setCTLAppliesEditable() {
		boolean isEditable = isCTLAppliesEditable();
		if(!isEditable){
			ctlApplies.setSelectedIndex(0);
			
		}
		ctlApplies.setEnabled(isEditable);
	}
	
	private boolean isCTLAppliesEditable() {
		return !BAIL.equals(getSelectedBCStatus().getCode()) &&
				!NOTAPPLICABLE.equals(getSelectedBCStatus().getCode()) &&
				!"".equals(getSelectedBCStatus().getCode()) && model.getCaseType().equals("T");
	}

	private ArrayList<RefListingDataBasicValue> getNoteClassArray() {
		if (noteClassArray == null) {
			noteClassArray = ListingDropdownPopulation.getNoteClassifications();
		}
		return noteClassArray;
	}

	private ArrayList<RefListingDataBasicValue> getDefaultSection28SelectionArray() {
		ArrayList<RefListingDataBasicValue> results = new ArrayList<RefListingDataBasicValue>();
		RefListingDataBasicValue generalOption = createSection28SelectionOption(PROSECUTION_ID,
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailSection28ProsecutionOption"));
		results.add(generalOption);
		return results;
	}

	private RefListingDataBasicValue createSection28SelectionOption(Integer id, String selectionText) {
		RefListingDataBasicValue result = new RefListingDataBasicValue();
		result.setRefListingDataId(id);
		result.setRefDataValue(selectionText);
		return result;
	}

	private void configureTabOrder() {
		setFocusTraversalPolicyProvider(true);
	setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { highCourtRadioButton,
				circuitJudgeRadioButton, highlightNote, highlightNoteClass, preDefined,
				freeText, secureCheckBox, videoLinkCheckBox, courtSite, timeEst, timeEstUnit, reqJudgeCheckBox,
				reqJudgeSearchButton, interpreterNote, hearingTypeCode, verifyHearingTypeButton, defaultHearingType,
				section28Selection, bcStatus, custodyTimeLimit, section28Name1, section28Phone1, section28Name2,
				section28Phone2, fixturesTable, fixturesAddButton, fixturesEditButton, fixturesDeleteButton }));
	}

	public void processAddJudge(OpenSearchListingJudgeAction action) {
        log.debug("processAddJudge(OpenSearchListingJudgeAction " + action + ")");
        Collection col = action.getResults();
        log.debug("the results collection has " + col.size() + " objects in it.");

        Iterator it = col.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            try {
                log.debug("Found object " + o + " in the OpenSearchListingJudgeAction's results.");

                // Update the model with the selected judge
                RefJudgeBasicValue refJudgeBasicValue = (RefJudgeBasicValue) o;
                model.setRefJudge(refJudgeBasicValue);

                // Update the display
                reqJudgeText.setText(model.getReqJudge());
        		reqJudgeCheckBox.setSelected(model.getReqJudge() != null);
            } catch (final Exception e) {
                log.error("Exception thrown in processAddJudge whilst casting results objects.");
                log.error(e);
            }
        }
    }

	private class HearingTypeComboBoxAction extends XAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			RefHearingTypeBasicValue selectedHearingType = (RefHearingTypeBasicValue) defaultHearingType
					.getSelectedItem();
			hearingTypeCode.setText(selectedHearingType != null ? selectedHearingType.getHearingTypeCode() : null);
		}
	}

	private void showCancelConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parent,
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCancelConfirmationTitle"), true,
				XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCancelConfirmationMessage"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);

		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}

	/**
	 * Validate the case before moving away from this screen There must be at
	 * least one defendant/appellant AND a prosecutor/respondent on the case.
	 * The number of defendants stated on the case must also equal the number of
	 * defendants
	 * 
	 * @return true if case is valid, else false
	 */
	private boolean validateCase() {
		boolean valid = true;

		String caseStatus = XhibitDelegateHelper.getCaseDelegate().determineCaseStatus(model.getCaseId());

		if (CaseStatusIndicator.INCOMPLETE_N.equals(caseStatus)) {
			// There must be at least one defendant/appellant AND a
			// prosecutor/respondent on the case.
			String errorMsg = XHIBITConstant.getResource(XhibitBundles.ErrorText,
					"listings.caseListingDetails.alert.partyDetailsIncomplete");
			XMessageBox.alert(parent,
					XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.caseListingDetails.alert.title"),
					true, XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			valid = false;
		} else if (CaseStatusIndicator.INCOMPLETE_I.equals(caseStatus)) {
			// The number of defendants stated on the case must also equal the
			// number of defendants
			String errorMsg = XHIBITConstant.getResource(XhibitBundles.ErrorText,
					"listings.caseListingDetails.alert.incorrectNoDefendants");
			XMessageBox.alert(parent,
					XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.caseListingDetails.alert.title"),
					true, XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			valid = false;
		}

		return valid;
	}

	private class VerifyHearingTypeButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

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
			populateFromBundle("ListingsDetailVerifyHearingType");
		}

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {
			Integer selectedIndex = getComboBoxModelIndexForCode(defaultHearingType.getModel(),
					hearingTypeCode.getText());
			boolean isValid = ValidationUtils.hasText(hearingTypeCode) && selectedIndex != null;
			String errorMsg = MessageFormat.format(
					XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.validation.hearingType.notKnown"),
					new Object[] { hearingTypeCode.getText() });
			defaultHearingType.setSelectedIndex(selectedIndex != null ? selectedIndex : 0);
			if (!isValid) {
				XMessageBox.alert(parent,
						XHIBITConstant.getResource(XhibitBundles.XhibitConstant, "exception.validation.title"), true,
						XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			}
		}
	}

	private class FixturesAddButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public FixturesAddButtonAction(CaseListingDetailPanel parent) {
			populateFromBundle("ListingsDetailFixturesAdd");
			setCaller(parent);
		}

		private boolean isValidCaseType() {
			boolean isValid = Arrays.asList(VALID_FIXTURE_CASE_TYPES).contains(model.getCaseType());
			if (!isValid) {
				String errorMsg = XHIBITConstant.getResource(XhibitBundles.ErrorText,
						"listings.fixture.invalidCaseType");
				XMessageBox.alert(parent,
						XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.caseListingDetails.alert.title"),
						true, XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			}
			return isValid;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {

			if (isValidCaseType() && validateCase()) {
			CaseListingDetailModel parentModel = ((CaseListingDetailPanel) getCaller()).getModel();
			CaseDiaryFixtureComplexValue fixtureValue = new CaseDiaryFixtureComplexValue();
			fixtureValue.setCaseListingEntryId(model.getCaseListingEntryId());
			
			ArrayList<CaseDiaryFixtureComplexValue> allSavedFixtures = (ArrayList<CaseDiaryFixtureComplexValue>) model
					.getFixtures();
			RefHearingTypeBasicValue hearingType = (RefHearingTypeBasicValue) defaultHearingType.getSelectedItem();
			fixtureValue.setListNoteText(freeText.getText());
			fixtureValue.setListingDate(XHIBITConstant.getTomorrowsDate());
			fixtureValue.setHearingTypeId(hearingType != null ? hearingType.getId() : null);

			fixtureValue.setPreDefNoteClassId(parentModel.getPreDefinedClassificationId());
			if (preDefined.getSelectedIndex() > 0) {
					fixtureValue.setListNotePreDefinedId(
							(((RefListingDataBasicValue) preDefined.getSelectedItem()).getId()));
			}
			fixtureValue.setFreeTextNoteClassId(parentModel.getFreeTextClassificationId());
			CaseListingFixtureModel fixtureModel = new CaseListingFixtureModel(fixtureValue,
						parentModel.getCaseType(), parentModel.getCaseNumber(), parentModel.getCourtId(),
						parentModel.getCaseId());
			fixtureModel.setHearingTypeArray(hearingTypeArray);
			fixtureModel.setCourtSiteArray(courtSiteArray);
			fixtureModel.setPreDefinedNotesArray(preDefinedArray);
			fixtureModel.setDefendants(parentModel.getDefendants());
				fixtureModel.setFixturesOnCase(allSavedFixtures);
			CaseListingFixtureDialog caseListingFixtureDialog = new CaseListingFixtureDialog(parent, fixtureModel);
			caseListingFixtureDialog.setVisible(true);

				// after return
			CaseDiaryFixtureComplexValue createdFixture = fixtureModel.getFixture();
			if (createdFixture.getCaseDiaryFixtureId() != null) {
					// Initialise fixtures to an empty list if the current list
					// is null
					if (model.getFixtures() == null) {
					Collection emptyList = new ArrayList();
					model.setFixtures(emptyList);
				}
				model.getFixtures().add(createdFixture);
			}

				// Refresh displayed data if have created a new fixture and need
				// to retrieve the listing entry id
				if (model.getCaseListingEntryId() == null && createdFixture.getCaseListingEntryId() != null) {
					// New fixture added before the case listing entry has been
					// saved which means a blank
					// case listing entry will have been created with only
					// default values. Retrieve this
					// copy and put the id and version on top of our case
					// listing entry object
				CaseListingEntryComplexValue caseListingEntry = XhibitDelegateHelper.getListingsDelegate()
						.getCaseListingEntryByCaseIdAndCourtId(parentModel.getCaseId(), parentModel.getCourtId());

					parentModel.getCaseListingEntryBasicValue().setId(createdFixture.getCaseListingEntryId());
					parentModel.getCaseListingEntryBasicValue()
							.setCaseListingEntryId(createdFixture.getCaseListingEntryId());
					parentModel.getCaseListingEntryBasicValue().setVersion(caseListingEntry.getVersion());
			}
			moveModelToFixturesTable();
		}
	}
	}

	private class FixturesEditButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public FixturesEditButtonAction(CaseListingDetailPanel parent) {
			populateFromBundle("ListingsDetailFixturesEdit");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {
			CaseListingDetailModel parentModel = ((CaseListingDetailPanel) getCaller()).getModel();
			CaseDiaryFixtureComplexValue fixtureValue = ((List<CaseDiaryFixtureComplexValue>) parentModel.getFixtures())
					.get(fixturesTable.getSelectedRow());
			CaseListingFixtureModel fixtureModel = new CaseListingFixtureModel(fixtureValue, parentModel.getCaseType(),
					parentModel.getCaseNumber(), parentModel.getCourtId(), parentModel.getCaseId());
			fixtureModel.setHearingTypeArray(hearingTypeArray);
			fixtureModel.setCourtSiteArray(courtSiteArray);
			fixtureModel.setPreDefinedNotesArray(preDefinedArray);
			fixtureModel.setDefendants(parentModel.getDefendants());
			fixtureModel.setFixturesOnCase((ArrayList<CaseDiaryFixtureComplexValue>) parentModel.getFixtures());
			CaseListingFixtureDialog caseListingFixtureDialog = new CaseListingFixtureDialog(parent, fixtureModel);
			caseListingFixtureDialog.setVisible(true);
			((List<CaseDiaryFixtureComplexValue>) parentModel.getFixtures()).set(fixturesTable.getSelectedRow(),
					fixtureModel.getFixture());
			moveModelToFixturesTable();
		}
	}

	/**
	 * Implementation of the Fixtures Delete Button XAction
	 */
	private class FixturesDeleteButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public FixturesDeleteButtonAction(CaseListingDetailPanel parent) {
			populateFromBundle("ListingsDetailFixturesDelete");
			setCaller(parent);
		}

		/**
		 * Launches the Delete Fixture popup, sending the popup the currently
		 * selected Fixture as a CaseDiaryFixtureBasicValue
		 */
		public void xActionPerformed(ActionEvent ae) throws Exception {
			CaseListingDetailModel parentModel = ((CaseListingDetailPanel) getCaller()).getModel();
			CaseDiaryFixtureComplexValue fixtureValue = ((List<CaseDiaryFixtureComplexValue>) parentModel.getFixtures())
					.get(fixturesTable.getSelectedRow());
			CaseListingDeleteFixtureModel fixtureModel = new CaseListingDeleteFixtureModel(fixtureValue);
			CaseListingDeleteFixtureDialog caseListingDeleteFixtureDialog = new CaseListingDeleteFixtureDialog(parent,
					fixtureModel);
			caseListingDeleteFixtureDialog.setVisible(true);

			// To be performed after the popup closes
			if ("D".equals(fixtureModel.getFixture().getStatus())
					&& "Y".equals(fixtureModel.getFixture().getObsInd())) {
				// Fixture has been deleted refresh the list of fixtures on the
				// CaseListingEntry record and update the table
				refreshDiaryFixtures(model.getCaseListingEntryId());
				moveModelToFixturesTable();
			}
		}
	}

	private class BCStatusComboBoxAction extends XAction {

		private static final long serialVersionUID = 1L;

		private String getPreviousBCStatus() {
			String result = null;
			Integer selectedDefendantId = getSection28SelectedDefendantId();
			if (selectedDefendantId != null) {
				DefendantValue selectedDefendantValue = getSelectedDefendantValue(selectedDefendantId);
			    if (selectedDefendantValue != null && selectedDefendantValue.getDefOnCaseBasicValue() != null) {
			    	result = selectedDefendantValue.getDefOnCaseBasicValue().getCurrentBcStatus();
			    }
			}
			return result != null ? result : EMPTY_STRING;
		}
		
		private String getCurrentBCStatus() {
			String result = getSelectedBCStatus().getCode();
			return result != null ? result : EMPTY_STRING;
		}
		
		private boolean isInCustody(String bcStatus) {
			return CUSTODY.equals(bcStatus) || INCARE.equals(bcStatus);
		}
		
		private boolean isOnBail(String bcStatus) {
			return BAIL.equals(bcStatus);
		}
		
		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			String previousBCStatus = getPreviousBCStatus();
			String currentBCStatus = getCurrentBCStatus();
			if (!previousBCStatus.equals(currentBCStatus)) {
				boolean previouslyInCustodyNowOnBail = previousBCStatus != null && isInCustody(previousBCStatus) && isOnBail(currentBCStatus);
				boolean currentlyInCustody = isInCustody(currentBCStatus);
				inCustodyCheckBox.setSelected(previouslyInCustodyNowOnBail || currentlyInCustody);
			}
			setCustodyTimeLimitEditable();
			setCTLAppliesEditable();
			ctlAppliesVC.validate();
		}
	}
	
	private class CTLAppliesComboBoxAction extends XAction {

		private static final long serialVersionUID = 1L;
		
		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			String ctlAppliesCode = getSelectedCTLAppliesValue().getCode();

			if(ctlAppliesCode != null && ctlAppliesCode.equals(CTL_APPLIES_CODE_YES)){
				custodyTimeLimit.setVisible(true);				
				custodyTimeLimit.setEnabled(true);		
				custodyTimeLimitText.setVisible(false);
			}
			else if(ctlAppliesCode != null && ctlAppliesCode.equals(CTL_APPLIES_CODE_NO)){
				custodyTimeLimit.setVisible(false);				
				custodyTimeLimit.setEnabled(false);		
				custodyTimeLimit.clear();
				custodyTimeLimitText.setVisible(true);
			}
		}
	}
	
	private class EditableField extends XTextField {

		private static final long serialVersionUID = 1L;

		public EditableField() {
			this(model.isReadOnly());
		}

		public EditableField(boolean isDisplayOnly) {
			super();
			if (isDisplayOnly) {
				this.setEditable(false);
				this.setFocusable(false);
			}
		}
	}
	
	private class EditableComboBox extends XComboBox {
		
		private static final long serialVersionUID = 1L;

		public EditableComboBox() {
			this(model.isReadOnly());
		}
		
		public EditableComboBox(boolean isDisplayOnly) {
			super();
			if (isDisplayOnly) {
				this.setEnabled(false);
			}
		}
		
		@Override
		public void setEnabled(boolean b) {
			super.setEnabled(b && !model.isReadOnly());
		}
	}
	
	private class EditableCheckBox extends JCheckBox {

		private static final long serialVersionUID = 1L;
		
		public EditableCheckBox(String text, boolean isDisplayOnly) {
			super(text);
			setFocusable(!isDisplayOnly);
		}

		@Override
		protected void processMouseEvent(MouseEvent e) {
			// Stop selection via mouse click
			if (this.isFocusable()) {
				super.processMouseEvent(e);
			}
		}
	}
	
	// Class to change listeners to each component within form
	private class LocalPageController extends PageController {
		
		@Override
		public void reset() {
			super.reset();
			saveButton.setEnabled(false);
			setPageButtonsEnabled();
			setFixtureButtonsEnabled();
		}
		
		public void addChangeListeners(Component[] components, Component[] excluded) {
			final List<Component> excludedComponents = Arrays.asList(excluded);
			final Component[] actualComponents = new Component[components.length - excluded.length];
			int arrayNo = 0;
			for (Component component : Arrays.asList(components)) {
				if (!excludedComponents.contains(component)) {
					actualComponents[arrayNo] = component;
					arrayNo++;
				}
			}
			addChangeListeners(actualComponents);
		}
		
		@Override
		protected void setPageChanged() {
			if (!model.isReadOnly()) {
				super.setPageChanged();
				if (enabled) {
					saveButton.setEnabled(isPageChanged() && ValidationControllerFactory.validateComponents(validationControllers));
					setPageButtonsEnabled();
					setFixtureButtonsEnabled();
				}
			}
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		
		private void setPageButtonsEnabled() {
			caseNoteButton.setEnabled(!isPageChanged());
			nonAvailableDaysButton.setEnabled(!isPageChanged() && !getModel().isReadOnly());
			caseSummaryButton.setEnabled(!isPageChanged());
			addToListButton.setEnabled(!isPageChanged() && !getModel().isReadOnly() && !parent.isFromListScreen());
		}
		
		private void setFixtureButtonsEnabled() {
			boolean isTableRowSelected = !fixturesTable.getSelectionModel().isSelectionEmpty();
			fixturesAddButton.setEnabled(!isPageChanged() && !getModel().isReadOnly());
			fixturesEditButton.setEnabled(isTableRowSelected && !isPageChanged() && !getModel().isReadOnly());
			fixturesDeleteButton.setEnabled(isTableRowSelected && !isPageChanged());
		}
	}
}
