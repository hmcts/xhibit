package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtSiteCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.casemanagement.GeneralDropdownPopulation;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.PageController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextArea;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.validation.TextRegexValidator;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class HomeCourtPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private HomeCourtModel model;
	private HomeCourtDialog parentDialog;
		
	/**
	 * Fields on left panel.
	 */
	private JLabel lblName = null;
	private JLabel lblOfficeAddress = null;
	private JLabel lblTown = null;
	private JLabel lblCounty = null;
	private JLabel lblPostcode = null;
	private JLabel lblTelNumber = null;
	private JLabel lblDocExRef = null;
	private JLabel lblPoliceForce = null;
	private JLabel lblLCDCodeCrown = null;
	private XTextField txtName = null;
	private XTextField txtOfficeAddress1 = null;
	private XTextField txtOfficeAddress2 = null;
	private XTextField txtOfficeAddress3 = null;
	private XTextField txtOfficeAddress4 = null;
	private XTextField txtTown = null;
	private XTextField txtCounty = null;
	private XTextField txtPostcode = null;
	private XTextField txtTelNumber = null;
	private XTextField txtDocExRef = null;
	private XTextField txtLCDCodeCrown = null;
	private XComboBox comboPoliceForce = null;
	private JLabel lblManName = null;
	private JLabel lblManOfficeAddress1 = null;
	private JLabel lblOfficeAddress2InvalidEntry = null;
	private JLabel lblOfficeAddress3InvalidEntry = null;
	private JLabel lblOfficeAddress4InvalidEntry = null;
	private JLabel lblTownInvalidEntry = null;
	private JLabel lblCountyInvalidEntry = null;
	private JLabel lblManPostcode = null;
	private JLabel lblTelNumberInvalidEntry = null;
	private JLabel lblDocExRefInvalidEntry = null;
	private JLabel lblPoliceForceInvalidEntry = null;
	private JLabel lblLCDCodeCrownInvalidEntry = null;
	
	/**
	 * Fields on Firm List Details panel.
	 */
	private JLabel lblFirmListSortOrderOfLists = null;
	private JLabel lblUsualTimeForStartOfCourtDay = null;
	private JLabel lblTimeDivider = null;
	private XComboBox comboFirmSortOrderOfLists = null;
	private XComboBox comboCourtStartHour = null;
	private XComboBox comboCourtStartMinute = null;
	private JLabel lblFirmListSortOrderOfListsInvalidEntry = null;
	private JLabel lblCourtStartHourInvalidEntry = null;
	private JLabel lblCourtStartMinuteInvalidEntry = null;
	
	/**
	 * Fields on Warned List Details panel.
	 */
	private JLabel lblWarnedListSortOrderOfLists = null;
	private JLabel lblRepresentationDeadline = null;
	private JLabel lblDays = null;
	private JLabel lblBy = null;
	private JLabel lblTime = null;
	private JLabel lblFreeTextNote = null;
	private XComboBox comboWarnedSortOrderOfLists = null;
	private XTextField txtDays = null;
	private XTextField txtTime = null;
	private XTextArea txtFreeTextNote = null;
	private JScrollPane scrollPaneFreeTextNote = null;
	private JLabel lblWarnedListSortOrderOfListsInvalidEntry = null;
	private JLabel lblDaysInvalidEntry = null;
	private JLabel lblTimeInvalidEntry = null;
	private JLabel lblFreeTextNoteInvalidEntry = null;
	
	/**
	 * Fields on Site Locations panel.
	 */
	private JTable siteLocationsTable = null;
	private JScrollPane siteLocationsTableScrollPane = null;
	private JButton btnUpdateCourtSite = null;
	private JButton btnAdd = null;
		
	/**
	 * Fields on Fax/Tier/County panel.
	 */
	private JLabel lblFaxNumber = null;
	private JLabel lblTier = null;
	private JLabel lblCountyCode = null;
	private XTextField txtFaxNumber = null;
	private XTextField txtTier = null;
	private XTextField txtCountyCode = null;
	private JLabel lblFaxNumberInvalidEntry = null;
	private JLabel lblTierInvalidEntry = null;
	private JLabel lblCountyCodeInvalidEntry = null;

		
	/**
	 * Fields on Button panel.
	 */
	private JButton btnUpdate = null;
	
	/**
	 * JPanels.
	 */
	private JPanel courtCentrePanel = null;
	private JPanel leftPanel = null;
	private JPanel detailsPanel = null;
	private JPanel rightPanel = null;
	private JPanel firmListDetailsPanel = null;
	private JPanel timePanel = null;
	private JPanel warnedListDetailsPanel = null;
	private JPanel faxTierCountyPanel = null;
	private JPanel siteLocationsPanel = null;
	private JPanel updateAddButtonPanel = null;
	
	protected static final BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper
			.getBizRefDelegate();
	protected static final Integer COURT_ID = XhibitSingleton.getInstance().getCourtId();
	@SuppressWarnings("unchecked")
	Vector<String> firmListSortValues = ResourceBundleHelper.getResourcesStartingWith(
			ResourceBundleHelper.getResourceBundle(XhibitBundles.HomeCourt),
			"firmList.sortOrderOfLists");
	@SuppressWarnings("unchecked")
	Vector<String> warnedListSortValues = ResourceBundleHelper.getResourcesStartingWith(
			ResourceBundleHelper.getResourceBundle(XhibitBundles.HomeCourt),
			"warnedList.sortOrderOfLists");
	private static String[] hours = new String[24];
	private static String[] minutes = new String[60];
	private ArrayList<RefSystemCodeBasicValue> policeForceTypes;
	private DefaultTableModel siteLocationsTableModel;
	
	private static String userDisplayName = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
	protected LocalPageController pageController = new LocalPageController();
	
	/**
	 * Validators.
	 */
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	
	// array of all the mandatory fields
	Vector<Object> mandatoryFields = new Vector<Object>();
	
	private static final Logger log = CSServices.getLogger(HomeCourtPanel.class);	
	
    private static interface ContactType {
	    public static final String FAX = "FAX";
		public static final String TEL = "TEL";
    }
	public HomeCourtPanel(HomeCourtDialog parentDialog,
			HomeCourtModel model) throws CSRecoverableException {
		this.model = model;
		this.parentDialog = parentDialog;
		stepInitialise();
		jbInit();
	}
	
	/**
	 * Initialises the look and feel of the panel.	
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		parentDialog.setMinimumSize(new Dimension(1050, 725));
		this.setPreferredSize(new Dimension(1150, 750));
		GridBagConstraints gbc = getGridBagLayout();
		
		this.add(getCourtCentrePanel(), gbc);

		configureTabOrder();
	}
	
	public JPanel getCourtCentrePanel() {
		if (courtCentrePanel == null) {
			courtCentrePanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			courtCentrePanel.setLayout(new GridBagLayout());
			
			courtCentrePanel.setBorder(BorderFactory.createTitledBorder(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.CourtCentrePanelTitle")));
			
			gbc.weightx = 0.25;
			gbc.weighty = 0.75;
			gbc.fill = GridBagConstraints.BOTH;
			
			courtCentrePanel.add(getLeftPanel(), gbc);

			gbc.weighty = 1;
			gbc.weightx = 0.75;
			gbc.gridx = 1;
			gbc.fill = GridBagConstraints.BOTH;
			
			courtCentrePanel.add(getRightPanel(), gbc);
			
			gbc.weighty = 1;
			gbc.weightx = 0.75;
			gbc.gridwidth = 2;
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.BOTH;
			
			courtCentrePanel.add(getSiteLocationsPanel(), gbc);
			
			gbc.gridy++;		
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.EAST;
			
			courtCentrePanel.add(getUpdateButton(), gbc);
		}
		return courtCentrePanel;
	}
	
	public JPanel getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			leftPanel.setLayout(new GridBagLayout());
			
			gbc.weighty = 1;
			gbc.weightx = 0.75;
			leftPanel.add(getDetailsPanel(), gbc);
		}
		return leftPanel;
	}
	
	public JPanel getDetailsPanel() {
		if (detailsPanel == null) {
			detailsPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			detailsPanel.setLayout(new GridBagLayout());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblName = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.NameLabel"));
			detailsPanel.add(lblName, gbc);
			gbc.gridy += 2;
			
			lblOfficeAddress = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.OfficeAddressLabel"));
			detailsPanel.add(lblOfficeAddress, gbc);
			//No need for Address 2, 3 and 4 labels
			gbc.gridy += 8;
			
			lblTown = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.TownLabel"));
			detailsPanel.add(lblTown, gbc);
			gbc.gridy += 2;
			
			lblCounty = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.CountyLabel"));
			detailsPanel.add(lblCounty, gbc);
			gbc.gridy += 2;
			
			lblPostcode = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.PostcodeLabel"));
			detailsPanel.add(lblPostcode, gbc);
			gbc.gridy += 2;
			
			lblTelNumber = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.TelNumberLabel"));
			detailsPanel.add(lblTelNumber, gbc);
			gbc.gridy += 2;
			
			lblDocExRef = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.DocExRefLabel"));
			detailsPanel.add(lblDocExRef, gbc);
			gbc.gridy += 2;
			
			lblPoliceForce = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.PoliceForceLabel"));
			detailsPanel.add(lblPoliceForce, gbc);
			gbc.gridy += 2;
			
			lblLCDCodeCrown = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.LCDCodeCrownLabel"));
			detailsPanel.add(lblLCDCodeCrown, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			detailsPanel.add(getCourtName(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getOfficeAddress1(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getOfficeAddress2(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getOfficeAddress3(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getOfficeAddress4(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getTown(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getCounty(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getPostcode(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getTelNumber(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getDocExRef(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getPoliceForce(), gbc);
			gbc.gridy += 2;
			
			gbc.fill = GridBagConstraints.NONE;
			detailsPanel.add(getLCDCodeCrown(), gbc);
			gbc.gridy += 2;
			
			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			detailsPanel.add(lblManName, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblManOfficeAddress1, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblOfficeAddress2InvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblOfficeAddress3InvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblOfficeAddress4InvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblTownInvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblCountyInvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblManPostcode, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblTelNumberInvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblDocExRefInvalidEntry, gbc);
			gbc.gridy += 2;
			
			//Not needed but keeps layout consistent
			detailsPanel.add(lblPoliceForceInvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblLCDCodeCrownInvalidEntry, gbc);
			
			pageController.addChangeListeners(detailsPanel.getComponents());
		}
		return detailsPanel;
	}
	
	public JPanel getRightPanel() {
		if (rightPanel == null) {
			rightPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			rightPanel.setLayout(new GridBagLayout());
			
			gbc.weighty = 1;
			gbc.weightx = 0.75;
			gbc.gridx = 1;
			rightPanel.add(getFirmListDetailsPanel(), gbc);
			gbc.gridy++;
			
			rightPanel.add(getWarnedListDetailsPanel(), gbc);
			gbc.gridy++;
			
			rightPanel.add(getFaxTierCountyPanel(), gbc);
		}
		return rightPanel;
	}
	
	public JPanel getFirmListDetailsPanel() {
		if (firmListDetailsPanel == null) {
			firmListDetailsPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.NONE;
			firmListDetailsPanel.setLayout(new GridBagLayout());
			
			firmListDetailsPanel.setBorder(BorderFactory.createTitledBorder(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.FirmListDetailsPanelTitle")));
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblFirmListSortOrderOfLists = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.SortOrderOfListsLabel"));
			firmListDetailsPanel.add(lblFirmListSortOrderOfLists, gbc);
			gbc.gridy += 2;
			
			lblUsualTimeForStartOfCourtDay = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.UsualTimeForStartOfCourtDayLabel"));
			firmListDetailsPanel.add(lblUsualTimeForStartOfCourtDay, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;

			firmListDetailsPanel.add(getFirmSortOrderOfLists(), gbc);
			gbc.gridy += 2;
			
			timePanel = new JPanel();
			timePanel.setLayout(new GridBagLayout());
			GridBagConstraints gbcTimePanel = getGridBagLayout();
			
			timePanel.add(getCourtStartHour(), gbcTimePanel);
			firmListDetailsPanel.add(timePanel, gbc);
			gbc.gridx++;
			
			lblTimeDivider = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.TimeDividerLabel"));
			gbcTimePanel.gridx++;
			timePanel.add(lblTimeDivider, gbcTimePanel);
			gbc.gridx++;
			
			gbcTimePanel.gridx++;
			timePanel.add(getCourtStartMinute(), gbcTimePanel);
			
			/* Top Of Column */
			gbc.gridy = 0;
			gbc.gridx = 1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			firmListDetailsPanel.add(lblFirmListSortOrderOfListsInvalidEntry, gbc);
			gbc.gridy += 2;

			pageController.addChangeListeners(firmListDetailsPanel.getComponents());
		}
		return firmListDetailsPanel;
	}
	
	public JPanel getWarnedListDetailsPanel() {
		if (warnedListDetailsPanel == null) {
			warnedListDetailsPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			warnedListDetailsPanel.setLayout(new GridBagLayout());
			
			warnedListDetailsPanel.setBorder(BorderFactory.createTitledBorder(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.WarnedListDetailsPanelTitle")));
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblWarnedListSortOrderOfLists = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.SortOrderOfListsLabel"));
			warnedListDetailsPanel.add(lblWarnedListSortOrderOfLists, gbc);
			gbc.gridy += 2;
			
			lblRepresentationDeadline = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.RepresentationDeadlineLabel"));
			warnedListDetailsPanel.add(lblRepresentationDeadline, gbc);
			gbc.gridy += 2;
			
			lblFreeTextNote = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.FreeTextNoteLabel"));
			warnedListDetailsPanel.add(lblFreeTextNote, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;

			warnedListDetailsPanel.add(getWarnedSortOrderOfLists(), gbc);
			gbc.gridy += 2;
			
			warnedListDetailsPanel.add(getNumberOfDays(), gbc);
			gbc.gridx++;
			
			lblDays = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.DaysLabel"));
			warnedListDetailsPanel.add(lblDays, gbc);
			gbc.gridx++;
			
			lblBy = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.ByLabel"));
			warnedListDetailsPanel.add(lblBy, gbc);
			gbc.gridx++;
			
			warnedListDetailsPanel.add(getTime(), gbc);
			gbc.gridx++;
			
			lblTime = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.TimeLabel"));
			warnedListDetailsPanel.add(lblTime, gbc);
			gbc.gridy += 2;
			
			gbc.gridx = 1;
			gbc.gridwidth = 5;

			scrollPaneFreeTextNote = new JScrollPane();
			scrollPaneFreeTextNote.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			warnedListDetailsPanel.add(scrollPaneFreeTextNote, gbc);

			scrollPaneFreeTextNote.setViewportView(getFreeTextNote());
			
			/* Top Of Column */
			gbc.gridy = 0;
			gbc.gridx = 1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			warnedListDetailsPanel.add(lblWarnedListSortOrderOfListsInvalidEntry, gbc);
			gbc.gridy += 2;
			
			warnedListDetailsPanel.add(lblDaysInvalidEntry, gbc);
			gbc.gridx+= 3;
			
			warnedListDetailsPanel.add(lblTimeInvalidEntry, gbc);
			gbc.gridx-= 3;
			gbc.gridy += 2;
			
			warnedListDetailsPanel.add(lblFreeTextNoteInvalidEntry, gbc);
			
			pageController.addChangeListeners(warnedListDetailsPanel.getComponents());
		}
		return warnedListDetailsPanel;
	}
	
	public JPanel getFaxTierCountyPanel() {
		if (faxTierCountyPanel == null) {
			faxTierCountyPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			faxTierCountyPanel.setLayout(new GridBagLayout());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			
			lblFaxNumber = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.FaxNumberLabel"));
			faxTierCountyPanel.add(lblFaxNumber, gbc);
			gbc.gridy += 2;
			
			lblTier = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.TierLabel"));
			faxTierCountyPanel.add(lblTier, gbc);
			gbc.gridy += 2;
			
			lblCountyCode = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.CountyLabel"));
			faxTierCountyPanel.add(lblCountyCode, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.NONE;

			faxTierCountyPanel.add(getFaxNumber(), gbc);
			gbc.gridy += 2;
			
			faxTierCountyPanel.add(getTier(), gbc);
			gbc.gridy += 2;
			
			faxTierCountyPanel.add(getCountyCode(), gbc);
			
			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			faxTierCountyPanel.add(lblFaxNumberInvalidEntry, gbc);
			gbc.gridy += 2;
			
			faxTierCountyPanel.add(lblTierInvalidEntry, gbc);
			gbc.gridy += 2;
			
			faxTierCountyPanel.add(lblCountyCodeInvalidEntry, gbc);
			
			pageController.addChangeListeners(faxTierCountyPanel.getComponents());
		}
		return faxTierCountyPanel;
	}
	
	public JPanel getSiteLocationsPanel() {
		if (siteLocationsPanel == null) {
			siteLocationsPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.BOTH;
			siteLocationsPanel.setLayout(new GridBagLayout());
			
			siteLocationsPanel.setBorder(BorderFactory.createTitledBorder(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.SiteLocationsPanelTitle")));
			
			siteLocationsTableScrollPane = new JScrollPane();
			siteLocationsTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);			
			siteLocationsPanel.add(siteLocationsTableScrollPane, gbc);				
			siteLocationsTableScrollPane.setViewportView(getSiteLocationsTable());
			
			gbc.weightx = 0.1;
			gbc.gridx++;
			
			siteLocationsPanel.add(getUpdateAddButtonPanel(), gbc);
		}
		return siteLocationsPanel;
	}
	
	public JPanel getUpdateAddButtonPanel() {
		if (updateAddButtonPanel == null) {
			updateAddButtonPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			updateAddButtonPanel.setLayout(new GridBagLayout());
			
			updateAddButtonPanel.add(getUpdateCourtSiteButton(), gbc);
			gbc.gridy++;

			gbc.anchor = GridBagConstraints.SOUTH;
			updateAddButtonPanel.add(getAddButton(), gbc);
		}
		return updateAddButtonPanel;
	}
	
	public XTextField getCourtName() {
		if (lblManName == null) {
			lblManName = new JLabel(" ");
		}
		if (txtName == null) {
			txtName = new XTextField();
			txtName.setMaxLength(255);
			txtName.setColumns(10);
			txtName.setMinimumSize(txtName.getPreferredSize());
			txtName.setEnabled(false);
			txtName.setDisabledTextColor(Color.BLACK);
		}
		return txtName;
	}
	
	public XTextField getOfficeAddress1() {
		if (lblManOfficeAddress1 == null) {
			lblManOfficeAddress1 = new JLabel(" ");
		}
		if (txtOfficeAddress1 == null) {
			txtOfficeAddress1 = new XTextField();
			txtOfficeAddress1.setMaxLength(30);
			txtOfficeAddress1.setColumns(10);
			txtOfficeAddress1.setMinimumSize(txtOfficeAddress1.getPreferredSize());
			TextValidationController officeAddress1TxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtOfficeAddress1, lblManOfficeAddress1, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(officeAddress1TxtValidation);
			mandatoryFields.add(txtOfficeAddress1);
		}
		return txtOfficeAddress1;
	}
	
	public XTextField getOfficeAddress2() {
		if (lblOfficeAddress2InvalidEntry == null) {
			lblOfficeAddress2InvalidEntry = new JLabel(" ");
		}
		if (txtOfficeAddress2 == null) {
			txtOfficeAddress2 = new XTextField();
			txtOfficeAddress2.setMaxLength(30);
			txtOfficeAddress2.setColumns(10);
			txtOfficeAddress2.setMinimumSize(txtOfficeAddress2.getPreferredSize());
			TextValidationController officeAddress2TxtValidation = ValidationControllerFactory.createText(this,
					txtOfficeAddress2, lblOfficeAddress2InvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(officeAddress2TxtValidation);
		}
		return txtOfficeAddress2;
	}
	
	public XTextField getOfficeAddress3() {
		if (lblOfficeAddress3InvalidEntry == null) {
			lblOfficeAddress3InvalidEntry = new JLabel(" ");
		}
		if (txtOfficeAddress3 == null) {
			txtOfficeAddress3 = new XTextField();
			txtOfficeAddress3.setMaxLength(30);
			txtOfficeAddress3.setColumns(10);
			txtOfficeAddress3.setMinimumSize(txtOfficeAddress3.getPreferredSize());
			TextValidationController officeAddress3TxtValidation = ValidationControllerFactory.createText(this,
					txtOfficeAddress3, lblOfficeAddress3InvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(officeAddress3TxtValidation);
		}
		return txtOfficeAddress3;
	}
	
	public XTextField getOfficeAddress4() {
		if (lblOfficeAddress4InvalidEntry == null) {
			lblOfficeAddress4InvalidEntry = new JLabel(" ");
		}
		if (txtOfficeAddress4 == null) {
			txtOfficeAddress4 = new XTextField();
			txtOfficeAddress4.setMaxLength(30);
			txtOfficeAddress4.setColumns(10);
			txtOfficeAddress4.setMinimumSize(txtOfficeAddress4.getPreferredSize());
			TextValidationController officeAddress4TxtValidation = ValidationControllerFactory.createText(this,
					txtOfficeAddress4, lblOfficeAddress4InvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(officeAddress4TxtValidation);
		}
		return txtOfficeAddress4;
	}
	
	public XTextField getTown() {
		if (lblTownInvalidEntry == null) {
			lblTownInvalidEntry = new JLabel(" ");
		}
		if (txtTown == null) {
			txtTown = new XTextField();
			txtTown.setMaxLength(30);
			txtTown.setColumns(10);
			txtTown.setMinimumSize(txtTown.getPreferredSize());
			TextValidationController townTxtValidation = ValidationControllerFactory.createText(this,
					txtTown, lblTownInvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(townTxtValidation);
		}
		return txtTown;
	}
	
	public XTextField getCounty() {
		if (lblCountyInvalidEntry == null) {
			lblCountyInvalidEntry = new JLabel(" ");
		}
		if (txtCounty == null) {
			txtCounty = new XTextField();
			txtCounty.setMaxLength(30);
			txtCounty.setColumns(10);
			txtCounty.setMinimumSize(txtCounty.getPreferredSize());
			TextValidationController countyTxtValidation = ValidationControllerFactory.createText(this,
					txtCounty, lblCountyInvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(countyTxtValidation);
		}
		return txtCounty;
	}
	
	public XTextField getPostcode() {
		if (lblManPostcode == null) {
			lblManPostcode = new JLabel(" ");
			lblManPostcode.setForeground(Color.RED);
		}
		if (txtPostcode == null) {
			txtPostcode = new XTextField(8, "", lblManPostcode, true, true);
			txtPostcode.setMaxLength(8);
			txtPostcode.setColumns(10);
			txtPostcode.setUpperCase(true);
			txtPostcode.setMinimumSize(txtPostcode.getPreferredSize());
			txtPostcode.setGridBagLayout(true);
			mandatoryFields.add(txtPostcode);
		}
		return txtPostcode;
	}
	
	public XTextField getTelNumber() {
		if (lblTelNumberInvalidEntry == null) {
			lblTelNumberInvalidEntry = new JLabel(" ");
		}
		if (txtTelNumber == null) {
			txtTelNumber = new XTextField();
			txtTelNumber.setMaxLength(14);
			txtTelNumber.setColumns(10);
			txtTelNumber.setUpperCase(true);
			txtTelNumber.setMinimumSize(txtTelNumber.getPreferredSize());
			TextValidationController telNumberTxtValidation = ValidationControllerFactory.createText(this,
					txtTelNumber, lblTelNumberInvalidEntry, new TextRegexValidator("^[0-9 ]{1,14}$"));
			validationControllers.add(telNumberTxtValidation);
		}
		return txtTelNumber;
	}
	
	public XTextField getDocExRef() {
		if (lblDocExRefInvalidEntry == null) {
			lblDocExRefInvalidEntry = new JLabel(" ");
		}
		if (txtDocExRef == null) {
			txtDocExRef = new XTextField();
			txtDocExRef.setMaxLength(35);
			txtDocExRef.setColumns(10);
			txtDocExRef.setUpperCase(true);
			txtDocExRef.setMinimumSize(txtDocExRef.getPreferredSize());
			TextValidationController docExRefTxtValidation = ValidationControllerFactory.createText(this,
					txtDocExRef, lblDocExRefInvalidEntry, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(docExRefTxtValidation);
		}
		return txtDocExRef;
	}
	
	public XComboBox getPoliceForce() {
		if (lblPoliceForceInvalidEntry == null) {
			lblPoliceForceInvalidEntry = new JLabel(" ");
		}
		if (comboPoliceForce == null) {
			comboPoliceForce = new XComboBox(policeForceTypes.toArray());
			comboPoliceForce.setRenderer(new PoliceForceRenderer());
		}
		return comboPoliceForce;
	}
	
	public XTextField getLCDCodeCrown() {
		if (lblLCDCodeCrownInvalidEntry == null) {
			lblLCDCodeCrownInvalidEntry = new JLabel(" ");
		}
		if (txtLCDCodeCrown == null) {
			txtLCDCodeCrown = new XTextField();
			txtLCDCodeCrown.setMaxLength(3);
			txtLCDCodeCrown.setNumeric(true);
			txtLCDCodeCrown.setColumns(3);
			txtLCDCodeCrown.setUpperCase(true);
			txtLCDCodeCrown.setMinimumSize(txtLCDCodeCrown.getPreferredSize());
			txtLCDCodeCrown.setEnabled(false);
			txtLCDCodeCrown.setDisabledTextColor(Color.BLACK);
		}
		return txtLCDCodeCrown;
	}
	
	public XComboBox getFirmSortOrderOfLists() {
		if (lblFirmListSortOrderOfListsInvalidEntry == null) {
			lblFirmListSortOrderOfListsInvalidEntry = new JLabel(" ");
		}
		if (comboFirmSortOrderOfLists == null) {
			comboFirmSortOrderOfLists = new XComboBox();
			comboFirmSortOrderOfLists.setModel(new DefaultComboBoxModel(
					createDropdownValues(firmListSortValues).toArray()));
			comboFirmSortOrderOfLists.setRenderer(new DropdownBoxCellRender());
			// Mandatory - but don't add to madatoryFields as has no blank value so will always have a selection 
		}
		return comboFirmSortOrderOfLists;
	}
	
	private ArrayList<DropdownCodeStringValue> createDropdownValues(Vector<String> codes) {
		ArrayList<DropdownCodeStringValue> val = new ArrayList<DropdownCodeStringValue>();
		for (int i = 0; i < codes.size(); i++) {
			String toAdd[] = codes.get(i).split(":");
			val.add(new DropdownCodeStringValue(toAdd[0], toAdd[1]));
		}
		return val;
	}
	
	public XComboBox getCourtStartHour() {
		if (lblCourtStartHourInvalidEntry == null) {
			lblCourtStartHourInvalidEntry = new JLabel(" ");
		}
		if (comboCourtStartHour == null) {
			comboCourtStartHour = new XComboBox(hours);
		}
		return comboCourtStartHour;
	}
	
	public XComboBox getCourtStartMinute() {
		if (lblCourtStartMinuteInvalidEntry == null) {
			lblCourtStartMinuteInvalidEntry = new JLabel(" ");
		}
		if (comboCourtStartMinute == null) {
			comboCourtStartMinute = new XComboBox(minutes);
		}
		return comboCourtStartMinute;
	}
	
	public XComboBox getWarnedSortOrderOfLists() {
		if (lblWarnedListSortOrderOfListsInvalidEntry == null) {
			lblWarnedListSortOrderOfListsInvalidEntry = new JLabel(" ");
		}
		if (comboWarnedSortOrderOfLists == null) {
			comboWarnedSortOrderOfLists = new XComboBox();
			comboWarnedSortOrderOfLists.setModel(new DefaultComboBoxModel(
					createDropdownValues(warnedListSortValues).toArray()));
			comboWarnedSortOrderOfLists.setRenderer(new DropdownBoxCellRender());
			// Mandatory - but don't add to madatoryFields as has no blank value so will always have a selection 
		}
		return comboWarnedSortOrderOfLists;
	}
	
	public XTextField getNumberOfDays() {
		if (lblDaysInvalidEntry == null) {
			lblDaysInvalidEntry = new JLabel(" ");
		}
		if (txtDays == null) {
			txtDays = new XTextField();
			txtDays.setMaxLength(2);
			txtDays.setNumeric(true);
			txtDays.setColumns(3);
			txtDays.setUpperCase(true);
		}
		return txtDays;
	}
	
	public XTextField getTime() {
		if (lblTimeInvalidEntry == null) {
			lblTimeInvalidEntry = new JLabel(" ");
		}
		if (txtTime == null) {
			txtTime = new XTextField();
			txtTime.setMaxLength(8);
			txtTime.setColumns(3);
		}
		return txtTime;
	}
	
	public XTextArea getFreeTextNote() {
		if (lblFreeTextNoteInvalidEntry == null) {
			lblFreeTextNoteInvalidEntry = new JLabel(" ");
		}
		if (txtFreeTextNote == null) {
			txtFreeTextNote = new XTextArea();
			txtFreeTextNote.setRows(5);
			txtFreeTextNote.setColumns(30);
			txtFreeTextNote.setLimit(240);
			txtFreeTextNote.setLineWrap(true);
			txtFreeTextNote.setMaximumSize(txtFreeTextNote.getPreferredSize());
			TextValidationController freeTextNoteTxtValidation = ValidationControllerFactory.createText(this,
					txtFreeTextNote, lblFreeTextNoteInvalidEntry, new TextRegexValidator("^.{1,240}$"));
			validationControllers.add(freeTextNoteTxtValidation);
		}
		return txtFreeTextNote;
	}
	
	public XTextField getFaxNumber() {
		if (lblFaxNumberInvalidEntry == null) {
			lblFaxNumberInvalidEntry = new JLabel(" ");
		}
		if (txtFaxNumber == null) {
			txtFaxNumber = new XTextField();
			txtFaxNumber.setMaxLength(14);
			txtFaxNumber.setColumns(10);
			txtFaxNumber.setUpperCase(true);
			txtFaxNumber.setMinimumSize(txtFaxNumber.getPreferredSize());
			TextValidationController faxNumberTxtValidation = ValidationControllerFactory.createText(this,
					txtFaxNumber, lblFaxNumberInvalidEntry, new TextRegexValidator("^[0-9 ]{1,14}$"));
			validationControllers.add(faxNumberTxtValidation);
		}
		return txtFaxNumber;
	}
	
	public XTextField getTier() {
		if (lblTierInvalidEntry == null) {
			lblTierInvalidEntry = new JLabel(" ");
		}
		if (txtTier == null) {
			txtTier = new XTextField();
			txtTier.setMaxLength(1);
			txtTier.setNumeric(true);
			txtTier.setColumns(3);
			txtTier.setUpperCase(true);
			TextValidationController tierTxtValidation = ValidationControllerFactory.createText(this,
					txtTier, lblTierInvalidEntry, new TextRegexValidator("[1-3]+"));
			validationControllers.add(tierTxtValidation);
		}
		return txtTier;
	}
	
	public XTextField getCountyCode() {
		if (lblCountyCodeInvalidEntry == null) {
			lblCountyCodeInvalidEntry = new JLabel(" ");
		}
		if (txtCountyCode == null) {
			txtCountyCode = new XTextField();
			txtCountyCode.setMaxLength(5);
			txtCountyCode.setAlphaNumeric(true);
			txtCountyCode.setColumns(3);
			txtCountyCode.setUpperCase(true);
			txtCountyCode.setEnabled(false);
			txtCountyCode.setDisabledTextColor(Color.BLACK);
		}
		return txtCountyCode;
	}
	
	/**
	 * Returns the table that will hold the Site Locations table
	 * @return
	 */
	public JTable getSiteLocationsTable() {
		if (siteLocationsTable == null) {
			siteLocationsTable = new JTable();
			siteLocationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			siteLocationsTable.setModel(getSiteLocationsTableModel());
			siteLocationsTable.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
			siteLocationsTable.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);			
			siteLocationsTable.setPreferredScrollableViewportSize(siteLocationsTable.getPreferredSize());
			siteLocationsTable.getSelectionModel().addListSelectionListener(new SiteLocationsTableListener());
		}
		return siteLocationsTable;
	}
	
	/**
	 * Getter for Site Locations Table Model.
	 * 
	 * @return
	 */
	private DefaultTableModel getSiteLocationsTableModel() {
		if (siteLocationsTableModel == null) {
			String[] columnHeaders = new String[] {
					XHIBITConstant.getResource(XhibitBundles.HomeCourt,
							"HomeCourt.SiteLocationsTableSiteNameColumn"),
					XHIBITConstant.getResource(XhibitBundles.HomeCourt,
							"HomeCourt.SiteLocationsTableSiteCodeColumn"),
					XHIBITConstant.getResource(XhibitBundles.HomeCourt,
							"HomeCourt.SiteLocationsTableAddressColumn") };

			siteLocationsTableModel = new DefaultTableModel(new Object[][] {}, columnHeaders) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		return siteLocationsTableModel;
	}
	
	private class SiteLocationsTableListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			e.getFirstIndex();
			enableUpdateLocsBtn();
		}
		
	}

	
	/**
	 * Returns the Update Court Site button
	 * @return
	 */
	public JButton getUpdateCourtSiteButton() {
		if (btnUpdateCourtSite == null) {
			btnUpdateCourtSite = new JButton(new UpdateCourtSiteAction(this));
		}
		return btnUpdateCourtSite;
	}
	
	private class UpdateCourtSiteAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public UpdateCourtSiteAction(HomeCourtPanel parent) {
			populateFromBundle("HomeCourtDetailsUpdate");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			updateSiteLocations();
		}
		
	}
	
	/**
	 * Returns the Add button
	 * @return
	 */
	public JButton getAddButton() {
		if (btnAdd == null) {
			btnAdd = new JButton(new AddAction(this));
		}
		return btnAdd;
	}
	
	private class AddAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public AddAction(HomeCourtPanel parent) {
			populateFromBundle("HomeCourtDetailsAdd");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			addSiteLocation();
		}
		
	}
	
	/**
	 * Returns the Update Court Site button
	 * @return
	 */
	public JButton getUpdateButton() {
		if (btnUpdate == null) {
			btnUpdate = new JButton(new UpdateAction(this));
		}
		return btnUpdate;
	}
	
	private class UpdateAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public UpdateAction(HomeCourtPanel parent) {
			populateFromBundle("HomeCourtDetailsSave");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			stepValidate();
			moveScreenToModel();
			updateCourt();
		}
		
	}
	
	// Getters and Setters for Fields

	private String getNameTxt() {
		return txtName.getText();
	}

	private void setNameTxt(String courtName) {
		txtName.setText(courtName);
	}

	private String getOfficeText() {
		return txtOfficeAddress1.getText();
	}

	private void setOfficeText(String text) {
		txtOfficeAddress1.setText(text);
	}

	private String getOfficeText2() {
		return txtOfficeAddress2.getText();
	}

	private void setOfficeText2(String text) {
		txtOfficeAddress2.setText(text);
	}

	private String getOfficeText3() {
		return txtOfficeAddress3.getText();
	}

	private void setOfficeText3(String text) {
		txtOfficeAddress3.setText(text);
	}

	private String getOfficeText4() {
		return txtOfficeAddress4.getText();
	}

	private void setOfficeText4(String text) {
		txtOfficeAddress4.setText(text);
	}

	private String getTownText() {
		return txtTown.getText();
	}

	private void setTownText(String text) {
		txtTown.setText(text);
	}

	private String getCountyText() {
		return txtCounty.getText();
	}

	private void setCountyText(String text) {
		txtCounty.setText(text);
	}

	private String getPostCodeText() {
		return txtPostcode.getText();
	}

	private void setPostCodeText(String text) {
		txtPostcode.setText(text);
	}

	private String getTelNumberText() {
		return txtTelNumber.getText();
	}

	private void setTelNumberText(String text) {
		txtTelNumber.setText(text);
	}

	private String getFaxNumberText() {
		return txtFaxNumber.getText();
	}

	private void setFaxNumberText(String text) {
		txtFaxNumber.setText(text);
	}

	private String getDocxRefText() {
		return txtDocExRef.getText();
	}

	private void setDocxRefText(String text) {
		txtDocExRef.setText(text);
	}

	private String getTierText() {
		return txtTier.getText();
	}

	private void setTierText(String text) {
		txtTier.setText(text);
	}

	private void setLcdCrownCourtTxt(String text) {
		txtLCDCodeCrown.setText(text);
	}

	private String getCounty2Text() {
		return txtCountyCode.getText();
	}

	private void setCounty2Text(String text) {
		txtCountyCode.setText(text);
	}
	
	private Integer getPoliceForceTypeSelection() {
		RefSystemCodeBasicValue selection = (RefSystemCodeBasicValue) comboPoliceForce.getSelectedItem();
		if (selection != null) {
			return selection.getId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the selections on the Police Force combo box.
	 * 
	 * @param value
	 */
	private void setPoliceForceTypeSelection(Integer val) {
		int index = 0;
		if (val != null && val.intValue() != 0) {
			for (RefSystemCodeBasicValue code : policeForceTypes) {
				if (code.getId() != null) {
					if (val.intValue() == code.getId().intValue()) {
						comboPoliceForce.setSelectedIndex(index);
						break;
					}
				}
				index++;
			}
		}
		setModified(false);
	}
	
	private String getFirmListSortSelection() {
		return ((DropdownCodeStringValue) comboFirmSortOrderOfLists.getSelectedItem()).getCode();
	}

	/**
	 * Sets the selections on the Police Force combo box.
	 * 
	 * @param value
	 */
	private void setFirmListSortSelection(String val) {
		if (val != null) {
			comboFirmSortOrderOfLists.setSelectedItemByCode(val);
		}
		setModified(false);
	}
	
	private String getFirmListStartTime() {
		if (comboCourtStartHour.getSelectedItem() == null || comboCourtStartMinute.getSelectedItem() == null) {
			return null;
		}
		String hr = (String) comboCourtStartHour.getSelectedItem();
		String min = (String) comboCourtStartMinute.getSelectedItem();

		StringBuffer startTime = new StringBuffer();
		startTime.append(hr);
		startTime.append(":");
		startTime.append(min);

		return startTime.toString();
	}

	/**
	 * Sets the selection for the CourtStartTime combo boxes.
	 * 
	 * @param startTime
	 */
	private void setFirmListStartTime(String startTime) {
		if (startTime != null) {
			String[] time = startTime.split(":");
			if (time.length == 2) {
				comboCourtStartHour.setSelectedIndex(Integer.parseInt(time[0]));
				comboCourtStartMinute.setSelectedIndex(Integer.parseInt(time[1]));
			}
		}
		setModified(false);
	}
	
	private String getWarnedListSortSelection() {
		return ((DropdownCodeStringValue) comboWarnedSortOrderOfLists.getSelectedItem()).getCode();
	}

	/**
	 * Sets the selections on the Police Force combo box.
	 * 
	 * @param value
	 */
	private void setWarnedListSortSelection(String val) {
		if (val != null) {
			comboWarnedSortOrderOfLists.setSelectedItemByCode(val);
		}
		setModified(false);
	}
	
	private Integer getWarnedListPeriod() {
		String text = txtDays.getText();
		if (text != null && (!text.equals(""))) {
			return Integer.valueOf(txtDays.getText());
		} else {
			return null;
		}
	}

	private void setWarnedListPeriod(Integer period) {
		if (period != null) {
			this.txtDays.setText(Integer.toString(period));
		}
	}

	private String getWarnedListTime() {
		return txtTime.getText();
	}

	private void setWarnedListTime(String time) {
		txtTime.setText(time);
	}

	private String getWarnedListFreeText() {
		return txtFreeTextNote.getText();
	}

	private void setWarnedListFreeText(String text) {
		txtFreeTextNote.setText(text);
	}
	
	/**
	 * Populate the Court Centre controls.
	 * 
	 * @param court
	 */
	private void populateCourtCentre(CourtComplexValue court) {
		setNameTxt(court.getCourtName());
		setOfficeText(court.getAddress1());
		setOfficeText2(court.getAddress2());
		setOfficeText3(court.getAddress3());
		setOfficeText4(court.getAddress4());
		setTownText(court.getTown());
		setCountyText(court.getCounty());
		setPostCodeText(court.getPostcode());
		setTelNumberText(court.getTelephoneNumber());
		setDocxRefText(court.getDxRef());
		setPoliceForceTypeSelection(court.getPoliceForceCode());
		setLcdCrownCourtTxt(court.getCrestCourtId());
		setFirmListSortSelection(court.getFlRepSort());
		setFirmListStartTime(court.getCourtStartTime());
		setWarnedListSortSelection(court.getWlRepSort());
		setWarnedListPeriod(court.getWlRepPeriod());
		setWarnedListTime(court.getWlRepTime());
		setWarnedListFreeText(court.getWlFreeText());
		setFaxNumberText(court.getFaxNumber());
		setTierText(court.getTier());
		setCounty2Text(court.getCountyLocCode());
	}
	
	/**
	 * Populates the table with latest Court Sites data from model.
	 */
	private void populateCourtSiteTable() {
		try {
			siteLocationsTableModel.setRowCount(0);

			for (CourtSiteComplexValue courtSite : model.getCourtSites()) {
				AddressBasicValue address = bizRefDelegate.findByPK(courtSite.getAddressId());
				String siteName = courtSite.getCourtSiteName();
				String siteCode = courtSite.getCourtSiteCode();
				String addressLine = getAddressValue(address);

				siteLocationsTableModel.addRow(new Object[] { siteName, siteCode, addressLine });
			}
		} catch (Exception ex) {
			log.error("Error occurred while populating Court Site: " + ex);
			XHIBITConstant.handleError(ex);
		}
	}

	/**
	 * Returns the address1, address2, town and postcode concatenated.
	 * 
	 * @param address
	 * @return
	 */
	private String getAddressValue(AddressBasicValue address) {
		StringBuffer addressLine = new StringBuffer();
		addressLine.append(address.getAddress1());
		if (address.getAddress2() != null && !address.getAddress2().equals("")) {
			addressLine.append(", ");
			addressLine.append(address.getAddress2());
		}
		if (address.getTown() != null && !address.getTown().equals("")) {
			addressLine.append(", ");
			addressLine.append(address.getTown());
		}
		addressLine.append(", ");
		addressLine.append(address.getPostcode());

		return addressLine.toString();
	}
	
	private void enableUpdateLocsBtn() {
		int index = siteLocationsTable.getSelectedRow();
		if (index > -1) {
			btnUpdateCourtSite.setEnabled(true);
		} else {
			btnUpdateCourtSite.setEnabled(false);
		}
	}
	
	/**
	 * Saves the Court details after making changes.
	 * 
	 * @return success or failure
	 */
	@SuppressWarnings("unchecked")
	private void updateCourt() {

		try {

			bizRefDelegate.updateCourt(COURT_ID, model.getCourt(),
					userDisplayName);
			
			pageController.reset();
			showUpdateSuccessDialog();
		} catch (Exception er) {
			log.error("Error occurred while saving Court: " + er);
			XHIBITConstant.handleError(er);
		}
	}
	
	public void showUpdateSuccessDialog() {
		XMessageBox.alert(parentDialog, XHIBITConstant.getResource(XhibitBundles.HomeCourt, 
				"HomeCourt.SaveSuccessTitle"), true,
				XMessageBox.ICONINFORMATION, XHIBITConstant.getResource(XhibitBundles.HomeCourt, 
						"HomeCourt.SaveSuccessMessage"), XMessageBox.OK_ONLY,
				XMessageBox.DEFAULTOK);
	}
	
	private void showUnsavedCancelConfirmationDialog() throws CSRecoverableException {
		boolean messageBoxReply = false;
		messageBoxReply = XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.UnsavedChangesTitle"), true,
				XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.UnsavedChangesMessage"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);
		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}
	
	private void showCancelConfirmationDialog() throws CSRecoverableException {
		boolean messageBoxReply = false;
		messageBoxReply = XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.CancelTitle"), true,
				XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "HomeCourt.CancelMessage"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);
		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}
	
	// iterate through all validators and see if label is "Field is mandatory" text
	public void enableSaveButton() {
		boolean isValid = pageController.isPageChanged();
		if (isValid) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				if (mandatoryFields.get(i).getClass() == XTextField.class) {
					if (((XTextField) mandatoryFields.get(i)).isEnabled()) {
						String s = ((XTextField) mandatoryFields.get(i)).getText();
						if (s.equals("") || s == null) {
							isValid = false;
							break;
						}
					}
				}
				if (mandatoryFields.get(i).getClass() == XComboBox.class) {
					if (((XComboBox) mandatoryFields.get(i)).isEnabled()) {
						if (((XComboBox) mandatoryFields.get(i)).getSelectedIndex() == 0) {
							isValid = false;
							break;
						}
					}
				}
			}
		}
		
		setDefaultButtonForEnter(isValid);
		btnUpdate.setEnabled(isValid);
	}

	private void setDefaultButtonForEnter(final boolean saveEnabled) {
		if (saveEnabled) {
			// Set the default button to save.
			final JRootPane rootPane = SwingUtilities.getRootPane(btnUpdate);
			rootPane.setDefaultButton(btnUpdate);
		} else {
			// Set the default button to cancel.
			final JRootPane rootPane = SwingUtilities.getRootPane(parentDialog.getCancelButton());
			
			//This is null on initialisation.
			if (rootPane != null ){
				rootPane.setDefaultButton(parentDialog.getCancelButton());
			}
		}
	}
		
	private void updateSiteLocations() throws CSRecoverableException {
		if (pageController.isPageChanged()) {
			showUnsavedCancelConfirmationDialog();
		}
		pageController.reset();
		parentDialog.showSitePanel(getSelectedSite());
	}

	/**
	 * Clicking on the Add button first checks if any unsaved changes exist in
	 * the Home Court Panel that need to be saved before passing control in the
	 * Court Site panel.
	 * @throws CSRecoverableException 
	 */
	private void addSiteLocation() throws CSRecoverableException {
		if (pageController.isPageChanged()) {
			showUnsavedCancelConfirmationDialog();
		}
		pageController.reset();
		parentDialog.showSitePanel(null);
	}
	

	private CourtSiteComplexValue getSelectedSite() {
		try {
			int index = siteLocationsTable.convertRowIndexToModel(siteLocationsTable.getSelectedRow());
			if (index > -1) {
				return model.getCourtSites().get(index);
			}
		} catch (Exception ex) {
			log.error("Error occurred while finding Court Site");
			XHIBITConstant.handleError(ex);
		}
		return null;
	}
	
	public void configureTabOrder() {
		parentDialog.setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { txtName, txtOfficeAddress1,
				txtOfficeAddress2, txtOfficeAddress3, txtOfficeAddress4, txtTown, txtCounty, txtPostcode, txtTelNumber,
				txtDocExRef, comboPoliceForce, comboFirmSortOrderOfLists, comboCourtStartHour,
				comboCourtStartMinute, comboWarnedSortOrderOfLists, txtDays, txtTime, txtFreeTextNote, txtFaxNumber,
				txtTier, txtCountyCode, siteLocationsTable, btnUpdateCourtSite, btnAdd, btnUpdate,
				parentDialog.getCancelButton() }));
	}
	
    /**
	 * If the model is not null then populate the fields with the values.
	 * Also sets the caret to 0 so that if the field is too long then it'll
	 * show the first half of the string instead of the end of the string.
	 */
	private void moveModelToScreen() {
		// Disable the validation checking
		pageController.setEnabled(false);
		// Populate the screen
		populateCourtCentre(this.model.getCourt());
		populateCourtSiteTable();
		pageController.reset();
	}
	
	 /**
     * Set the data entered on screen into the model.
     */
	private void moveScreenToModel() {
		CourtComplexValue court = model.getCourt();

		court.setCourtName(getNameTxt());
		court.setAddress1(getOfficeText());
		court.setAddress2(getOfficeText2());
		court.setAddress3(getOfficeText3());
		court.setAddress4(getOfficeText4());
		court.setTown(getTownText());
		court.setCounty(getCountyText());
		court.setPostcode(getPostCodeText());
		court.setTelephoneNumber(getTelNumberText());
		court.setDxRef(getDocxRefText());
		court.setPoliceForceCode(getPoliceForceTypeSelection());
		court.setFlRepSort(getFirmListSortSelection());
		court.setCourtStartTime(getFirmListStartTime());
		court.setWlRepSort(getWarnedListSortSelection());
		court.setWlRepPeriod(getWarnedListPeriod());
		court.setWlRepTime(getWarnedListTime());
		court.setWlFreeText(getWarnedListFreeText());
		court.setFaxNumber(getFaxNumberText());
		court.setTier(getTierText());
		court.setCountyLocCode(getCounty2Text());
	}
	
	public void refreshData() {
		try {
			stepInitialise();
			moveModelToScreen();
		} catch (CSRecoverableException ex) {
			log.error("Error occurred while refreshing Home Court data: " + ex);
			XHIBITConstant.handleError(ex);
		}
	}
    
	@SuppressWarnings("unchecked")
	@Override
	public void stepInitialise() throws CSRecoverableException {
		
		if (policeForceTypes == null) {
			policeForceTypes = GeneralDropdownPopulation.getPoliceForceCode();
		}
		
		try {
			if (model.getCourt() == null) {
				CourtComplexValue courtComplexValue = bizRefDelegate.findHomeCourtById(COURT_ID);
				if (courtComplexValue != null) {
					model.setCourt(courtComplexValue);
				}
			}

			// Get Court Sites
			CourtSiteCriteria criteria = new CourtSiteCriteria();
			criteria.setCourtId(Integer.toString(COURT_ID));
			criteria.setObsInd("N");
			List<CourtSiteComplexValue> courtSites = (List<CourtSiteComplexValue>) bizRefDelegate
					.findAllCourtSitesByComplex(COURT_ID);
			if (courtSites != null && courtSites.size() > 0) {
				Collections.sort(courtSites, new CourtSiteSorter());
				model.setCourtSites((ArrayList<CourtSiteComplexValue>) courtSites);
			}

			for (int i = 0; i < 10; i++) {
				hours[i] = "0"+Integer.toString(i);
			}
			for (int i=10;i<24;i++) {
				hours[i] = Integer.toString(i);
			}

			for (int i = 0; i < 10; i++) {
				minutes[i] = "0"+Integer.toString(i);
			}
			
			for (int i = 10; i < 60; i++) {
				minutes[i] = Integer.toString(i);
			}
			
		} catch (Exception ex) {
			log.error("Error occurred while initialising Home Court Panel: " + ex);
			throw new CSUnrecoverableException(ex);
		}
	}
	/**
	 * Default gridbag that's used throughout the panels.
	 * @return gridbagconstraints
	 */
	private GridBagConstraints getGridBagLayout() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
	}

	/**
	 * moves the actual values into the fields.
	 */
	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
		btnUpdateCourtSite.setEnabled(false);
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		
	}

	/**
	 * Checks if any of the validation on the page is incorrect.
	 */
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		// Throw exception if validation failures to prevent saving
		if (!ValidationControllerFactory.validateComponents(validationControllers)) {
			throw new CSValidationException("validation.general", "Field validation Failed");
		}
		if (!(lblManPostcode.getText().equals(" "))) {
			throw new CSValidationException("validation.general", "Field validation failed");
		}
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	/**
	 * If save button clicked then check validation and then save the database changes
	 */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (!update) {
			if (pageController.isPageChanged()) {
				showUnsavedCancelConfirmationDialog();
			} else {
				showCancelConfirmationDialog();
			}
		}
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
	}
	
	// Class to change listeners to each component within form
	private class LocalPageController extends PageController {
		
		@Override
		public void reset() {
			super.reset();
			enableSaveButton();
		}
		
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		
		@Override
		protected void setPageChanged() {
			super.setPageChanged();
			if (enabled) {
				enableSaveButton();
			}
		}
	}
}
