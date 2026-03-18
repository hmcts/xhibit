package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.PageController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XTextArea;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.TextRegexValidator;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CourtSiteLocationPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private static final int COURTROOMNO_COL = 0;
	private static final String YES = "Y";
	private HomeCourtDialog parentDialog;

    
	/**
	 * Fields on Left panel.
	 */
	private JLabel lblSiteName = null;
	private JLabel lblAddress = null;
	private JLabel lblTown = null;
	private JLabel lblCounty = null;
	private JLabel lblPostcode = null;
	private JLabel lblTelNumber = null;
	private XTextField txtSiteName = null;
	private XTextField txtAddress1 = null;
	private XTextField txtAddress2 = null;
	private XTextField txtAddress3 = null;
	private XTextField txtAddress4 = null;
	private XTextField txtTown = null;
	private XTextField txtCounty = null;
	private XTextField txtPostcode = null;
	private XTextField txtTelNumber = null;
	private JLabel lblManSiteName = null;
	private JLabel lblManAddress1 = null;
	private JLabel lblAddress2InvalidEntry = null;
	private JLabel lblAddress3InvalidEntry = null;
	private JLabel lblAddress4InvalidEntry = null;
	private JLabel lblTownInvalidEntry = null;
	private JLabel lblCountyInvalidEntry = null;
	private JLabel lblManPostcode = null;
	private JLabel lblTelNumberInvalidEntry = null;
	
	/**
	 * Fields on Site panel.
	 */
	private JLabel lblSiteCode = null;
	private JLabel lblSiteGroup = null;
	private XTextField txtSiteCode = null;
	private XTextField txtSiteGroup = null;
	private JLabel lblManSiteCode = null;
	private JLabel lblManSiteGroup = null;
	
	/**
	 * Fields on List panel.
	 */
	private JLabel lblListNameLocation = null;
	private XTextField txtListNameLocation = null;
	private JLabel lblListNameLocationInvalidEntry = null;	
	
	/**
	 * Fields on Free Text line for Daily List Floating Cases panel.
	 */
	private JLabel lblFreeTextLine = null;
	private XTextArea txtFreeTextLine = null;
	private JScrollPane scrollPaneFreeTextLine = null;
	private JLabel lblFreeTextLineInvalidEntry = null;
	
	/**
	 * Fields on Fax Number panel.
	 */
	private JLabel lblFaxNumber = null;
	private XTextField txtFaxNumber = null;
	private JLabel lblFaxNumberInvalidEntry = null;
	
	/**
	 * Fields on Lower panel.
	 */
	private JLabel lblSatelliteCourt = null;
	private JLabel lblLCDCode = null;
	private JLabel lblTier = null;
	private JLabel lblNotInUse = null;
	private JCheckBox chkBxSatelliteCourt = null;
	private TextField txtLCDCode = null;
	private TextField txtTier = null;
	private JCheckBox chkBxNotInUse = null;
	private JLabel lblNotInUseInvalidEntry = null;
	
	/**
	 * Fields on Courtrooms panel.
	 */
	private JTable courtRoomsTable = null;
	private JScrollPane courtRoomsTableScrollPane = null;
	
	/**
	 * Fields on Add button panel.
	 */
	private JButton btnAdd = null;

	/**
	 * Fields on Back and Save button panel.
	 */
	private JButton btnBack = null;
	private JButton btnSave = null;
	
	/**
	 * JPanels.
	 */
	private JPanel courtSitePanel = null;
	private JPanel leftPanel = null;
	private JPanel detailsPanel = null;
	private JPanel rightPanel = null;
	private JPanel sitePanel = null;
	private JPanel listPanel = null;
	private JPanel freeTextLinePanel = null;
	private JPanel faxNumberPanel = null;
	private JPanel lowerPanel = null;
	private JPanel courtRoomsPanel = null;
	private JPanel addButtonPanel = null;
	private JPanel backSaveButtonPanel = null;

	
	protected static final BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper
			.getBizRefDelegate();
	
	protected static final Integer COURT_ID = XhibitSingleton.getInstance().getCourtId();
	
	private static final String COURT = "Court";

	private static final String COURT_ROOM = "Court Room";

	protected static final String DISPLAY_NAME = XhibitSingleton.getInstance().getUserSession()
			.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);

	private CourtRoomTableModel courtRoomsTableModel;
	
	private CourtSiteLocationModel courtSiteModel;
	
	private HomeCourtModel homeCourtModel;
	
	protected static final String TRUE = "Y";

	protected static final String FALSE = "N";
	
	protected LocalPageController pageController = new LocalPageController();
	
	/**
	 * Validators.
	 */
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	
	// array of all the mandatory fields
	Vector<Object> mandatoryFields = new Vector<Object>();
	
	private static final Logger log = CSServices.getLogger(CourtSiteLocationPanel.class);	

	public CourtSiteLocationPanel(HomeCourtDialog parentDialog, CourtSiteLocationModel courtSiteModel,
			HomeCourtModel homeCourtModel) throws CSRecoverableException {
		this.courtSiteModel = courtSiteModel;
		this.homeCourtModel = homeCourtModel;
		this.parentDialog = parentDialog;
	
		stepInitialise();
		jbInit();
	}
	
	/**
	 * Initialises the look and feel of the panel.	
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(1150, 750));
		GridBagConstraints gbc = getGridBagLayout();
		
		this.add(getCourtSitePanel(), gbc);

		configureTabOrder();	
	}
	
	public JPanel getCourtSitePanel() {
		if (courtSitePanel == null) {
			courtSitePanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			courtSitePanel.setLayout(new GridBagLayout());
			
			courtSitePanel.setBorder(BorderFactory.createTitledBorder(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.CourtSitePanelTitle")));
			
			gbc.weightx = 0.25;
			gbc.weighty = 0.75;
			gbc.fill = GridBagConstraints.BOTH;
			
			courtSitePanel.add(getLeftPanel(), gbc);
			
			gbc.weighty = 1;
			gbc.weightx = 0.75;
			gbc.gridx = 1;
			gbc.fill = GridBagConstraints.BOTH;
			
			courtSitePanel.add(getRightPanel(), gbc);
			
			gbc.weighty = 1;
			gbc.weightx = 0.75;
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			
			courtSitePanel.add(getLowerPanel(), gbc);
			gbc.gridy++;

			gbc.weighty = 1;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.BOTH;
			
			courtSitePanel.add(getCourtRoomsPanel(), gbc);
			
			gbc.gridy++;
			gbc.weighty = 0.2;
			gbc.anchor = GridBagConstraints.EAST;
			courtSitePanel.add(getBackSaveButtonPanel(), gbc);
		}
		return courtSitePanel;
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

			lblSiteName = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.SiteNameLabel"));
			detailsPanel.add(lblSiteName, gbc);
			gbc.gridy += 2;
			
			lblAddress = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.AddressLabel"));
			detailsPanel.add(lblAddress, gbc);
			//No need for Address 2, 3 and 4 labels
			gbc.gridy += 8;
			
			lblTown = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.TownLabel"));
			detailsPanel.add(lblTown, gbc);
			gbc.gridy += 2;
			
			lblCounty = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.CountyLabel"));
			detailsPanel.add(lblCounty, gbc);
			gbc.gridy += 2;
			
			lblPostcode = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.PostcodeLabel"));
			detailsPanel.add(lblPostcode, gbc);
			gbc.gridy += 2;
			
			lblTelNumber = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.TelNumberLabel"));
			detailsPanel.add(lblTelNumber, gbc);
			gbc.gridy += 2;
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			detailsPanel.add(getSiteName(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getAddress1(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getAddress2(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getAddress3(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getAddress4(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getTown(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getCounty(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getPostcode(), gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(getTelNumber(), gbc);
			
			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			detailsPanel.add(lblManSiteName, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblManAddress1, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblAddress2InvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblAddress3InvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblAddress4InvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblTownInvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblCountyInvalidEntry, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblManPostcode, gbc);
			gbc.gridy += 2;
			
			detailsPanel.add(lblTelNumberInvalidEntry, gbc);
			gbc.gridy += 2;
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
			rightPanel.add(getSitePanel(), gbc);
			gbc.gridy++;
			
			gbc.weighty = 1;
			gbc.weightx = 0.75;
			rightPanel.add(getListPanel(), gbc);
			gbc.gridy++;
			
			gbc.weighty = 1;
			gbc.weightx = 0.75;
			rightPanel.add(getFreeTextLinePanel(), gbc);
			gbc.gridy++;
			
			gbc.weighty = 1;
			gbc.weightx = 0.75;
			gbc.fill = GridBagConstraints.NONE;
			rightPanel.add(getFaxNumberPanel(), gbc);
		}
		return rightPanel;
	}
	
	public JPanel getSitePanel() {
		if (sitePanel == null) {
			sitePanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.NONE;
			sitePanel.setLayout(new GridBagLayout());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblSiteCode = new JLabel(XHIBITConstant.getResource(XhibitBundles.HomeCourt,
					"CourtSite.SiteCodeLabel"));
			sitePanel.add(lblSiteCode, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;
			
			sitePanel.add(getSiteCode(), gbc);
			
			/* Top Of Column */
			gbc.gridy = 0;
			gbc.gridx = 1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			
			sitePanel.add(lblManSiteCode, gbc);
			
			/* Next Column */
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			
			lblSiteGroup = new JLabel(XHIBITConstant.getResource(XhibitBundles.HomeCourt,
					"CourtSite.SiteGroupLabel"));
			sitePanel.add(lblSiteGroup, gbc);
			
			/* Top of Column */
			gbc.gridy = 1;
			gbc.gridx++;
			
			sitePanel.add(getSiteGroup(), gbc);
			
			/* Top Of Column */
			gbc.gridy = 0;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			
			sitePanel.add(lblManSiteGroup, gbc);
		}
		return sitePanel;
	}
	
	public JPanel getListPanel() {
		if (listPanel == null) {
			listPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			listPanel.setLayout(new GridBagLayout());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblListNameLocation = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.ListNameLocationLabel"));
			listPanel.add(lblListNameLocation, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;
			
			listPanel.add(getListNameLocation(), gbc);
			
			/* Top Of Column */
			gbc.gridy = 0;
			gbc.gridx = 1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			
			listPanel.add(lblListNameLocationInvalidEntry, gbc);
		}
		return listPanel;
	}
	
	public JPanel getFreeTextLinePanel() {
		if (freeTextLinePanel == null) {
			freeTextLinePanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			freeTextLinePanel.setLayout(new GridBagLayout());
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			gbc.gridwidth = 2;

			lblFreeTextLine = new JLabel(XHIBITConstant.getResource(XhibitBundles.HomeCourt,
					"CourtSite.FreeTextLineForDailyListFloatingCasesLabel"));
			freeTextLinePanel.add(lblFreeTextLine, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 2;
			
			scrollPaneFreeTextLine = new JScrollPane();
			scrollPaneFreeTextLine.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			freeTextLinePanel.add(scrollPaneFreeTextLine, gbc);
			scrollPaneFreeTextLine.setViewportView(getFreeTextLine());
			
			/* Top Of Column */
			gbc.gridy = 1;
			gbc.gridx = 1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			
			freeTextLinePanel.add(lblFreeTextLineInvalidEntry, gbc);
		}
		return freeTextLinePanel;
	}
	
	public JPanel getFaxNumberPanel() {
		if (faxNumberPanel == null) {
			faxNumberPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.NONE;
			faxNumberPanel.setLayout(new GridBagLayout());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblFaxNumber = new JLabel(XHIBITConstant.getResource(XhibitBundles.HomeCourt,
					"CourtSite.FaxNumberLabel"));
			faxNumberPanel.add(lblFaxNumber, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;
			
			faxNumberPanel.add(getFaxNumber(), gbc);
			
			/* Top Of Column */
			gbc.gridy = 0;
			gbc.gridx = 1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			
			faxNumberPanel.add(lblFaxNumberInvalidEntry, gbc);
		}
		return faxNumberPanel;
	}
	
	public JPanel getLowerPanel() {
		if (lowerPanel == null) {
			lowerPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.NONE;
			lowerPanel.setLayout(new GridBagLayout());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblSatelliteCourt = new JLabel(XHIBITConstant.getResource(XhibitBundles.HomeCourt,
					"CourtSite.SatelliteCourtLabel"));
			lowerPanel.add(lblSatelliteCourt, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;
			
			lowerPanel.add(getSatelliteCourtCheckbox(), gbc);
			
			/* Top Of Column */
			gbc.gridy = 0;
			gbc.gridx = 1;
			
			/* Next Column */
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			
			lblLCDCode = new JLabel(XHIBITConstant.getResource(XhibitBundles.HomeCourt,
					"CourtSite.LCDCodeLabel"));
			lowerPanel.add(lblLCDCode, gbc);
			
			/* Top of Column */
			gbc.gridy = 1;
			gbc.gridx++;
			
			lowerPanel.add(getLCDCode(), gbc);
			
			/* Top Of Column */
			gbc.gridy = 0;
			
			/* Next Column */
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			
			lblTier = new JLabel(XHIBITConstant.getResource(XhibitBundles.HomeCourt,
					"CourtSite.TierLabel"));
			lowerPanel.add(lblTier, gbc);
			
			/* Top of Column */
			gbc.gridy = 1;
			gbc.gridx++;
			
			lowerPanel.add(getTier(), gbc);
			
			/* Top Of Column */
			gbc.gridy = 0;
			
			/* Next Column */
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			
			lblNotInUse = new JLabel(XHIBITConstant.getResource(XhibitBundles.HomeCourt,
					"CourtSite.NotInUseLabel"));
			lowerPanel.add(lblNotInUse, gbc);
			
			/* Top of Column */
			gbc.gridy = 1;
			gbc.gridx++;
			
			lowerPanel.add(getNotInUseCheckbox(), gbc);
			
			/* Top Of Column */
			gbc.gridy = 0;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			
			lowerPanel.add(lblNotInUseInvalidEntry, gbc);
		}
		return lowerPanel;
	}
	
	public JPanel getCourtRoomsPanel() {
		if (courtRoomsPanel == null) {
			courtRoomsPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.BOTH;
			courtRoomsPanel.setLayout(new GridBagLayout());
			
			courtRoomsPanel.setBorder(BorderFactory.createTitledBorder(
					XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.CourtroomsPanelTitle")));
			
			courtRoomsTableScrollPane = new JScrollPane();
			courtRoomsTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);			
			courtRoomsPanel.add(courtRoomsTableScrollPane, gbc);				
			courtRoomsTableScrollPane.setViewportView(getCourtRoomsTable());

			gbc.weightx = 0.1;
			gbc.gridx++;

			courtRoomsPanel.add(getAddButtonPanel(), gbc);
		}
		return courtRoomsPanel;
	}
	
	public JPanel getAddButtonPanel() {
		if (addButtonPanel == null) {
			addButtonPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.SOUTH;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			addButtonPanel.setLayout(new GridBagLayout());
			
			addButtonPanel.add(getAddButton(), gbc);
		}
		return addButtonPanel;
	}
	
	public JPanel getBackSaveButtonPanel() {
		if (backSaveButtonPanel == null) {
			backSaveButtonPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.EAST;
			gbc.fill = GridBagConstraints.NONE;
			backSaveButtonPanel.setLayout(new GridBagLayout());
			
			backSaveButtonPanel.add(getBackButton(), gbc);
			gbc.gridx++;
			gbc.anchor = GridBagConstraints.EAST;
			gbc.weightx = 0.1;
			
			backSaveButtonPanel.add(getSaveButton(), gbc);
		}
		return backSaveButtonPanel;
	}
	
	public XTextField getSiteName() {
		if (lblManSiteName == null) {
			lblManSiteName = new JLabel(" ");
		}
		if (txtSiteName == null) {
			txtSiteName = new XTextField();
			txtSiteName.setMaxLength(35);
			txtSiteName.setColumns(10);
			txtSiteName.setMinimumSize(txtSiteName.getPreferredSize());
			TextValidationController siteNameTxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtSiteName, lblManSiteName, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(siteNameTxtValidation);
			mandatoryFields.add(txtSiteName);
		}
		return txtSiteName;
	}
	
	public XTextField getAddress1() {
		if (lblManAddress1 == null) {
			lblManAddress1 = new JLabel(" ");
		}
		if (txtAddress1 == null) {
			txtAddress1 = new XTextField();
			txtAddress1.setMaxLength(30);
			txtAddress1.setColumns(10);
			txtAddress1.setMinimumSize(txtAddress1.getPreferredSize());
			TextValidationController address1TxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtAddress1, lblManAddress1, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address1TxtValidation);
			mandatoryFields.add(txtAddress1);
		}
		return txtAddress1;
	}
	
	public XTextField getAddress2() {
		if (lblAddress2InvalidEntry == null) {
			lblAddress2InvalidEntry = new JLabel(" ");
		}
		if (txtAddress2 == null) {
			txtAddress2 = new XTextField();
			txtAddress2.setMaxLength(30);
			txtAddress2.setColumns(10);
			txtAddress2.setMinimumSize(txtAddress2.getPreferredSize());
			TextValidationController address2TxtValidation = ValidationControllerFactory.createText(this,
					txtAddress2, lblAddress2InvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address2TxtValidation);
		}
		return txtAddress2;
	}
	
	public XTextField getAddress3() {
		if (lblAddress3InvalidEntry == null) {
			lblAddress3InvalidEntry = new JLabel(" ");
		}
		if (txtAddress3 == null) {
			txtAddress3 = new XTextField();
			txtAddress3.setMaxLength(30);
			txtAddress3.setColumns(10);
			txtAddress3.setMinimumSize(txtAddress3.getPreferredSize());
			TextValidationController address3TxtValidation = ValidationControllerFactory.createText(this,
					txtAddress3, lblAddress3InvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address3TxtValidation);
		}
		return txtAddress3;
	}
	
	public XTextField getAddress4() {
		if (lblAddress4InvalidEntry == null) {
			lblAddress4InvalidEntry = new JLabel(" ");
		}
		if (txtAddress4 == null) {
			txtAddress4 = new XTextField();
			txtAddress4.setMaxLength(30);
			txtAddress4.setColumns(10);
			txtAddress4.setMinimumSize(txtAddress4.getPreferredSize());
			TextValidationController address4TxtValidation = ValidationControllerFactory.createText(this,
					txtAddress4, lblAddress4InvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address4TxtValidation);
		}
		return txtAddress4;
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
	
	public XTextField getSiteCode() {
		if (lblManSiteCode == null) {
			lblManSiteCode = new JLabel(" ");
		}
		if (txtSiteCode == null) {
			txtSiteCode = new XTextField();
			txtSiteCode.setMaxLength(1);
			txtSiteCode.setColumns(3);
			txtSiteCode.setUpperCase(true);
			txtSiteCode.setText(true);
			txtSiteCode.setMinimumSize(txtSiteCode.getPreferredSize());
			TextValidationController siteCodeTxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtSiteCode, lblManSiteCode, new TextRegexValidator("^[A-Y]{1}$"));
			validationControllers.add(siteCodeTxtValidation);
			mandatoryFields.add(txtSiteCode);
		}
		return txtSiteCode;
	}
	
	public XTextField getSiteGroup() {
		if (lblManSiteGroup == null) {
			lblManSiteGroup = new JLabel(" ");
		}
		if (txtSiteGroup == null) {
			txtSiteGroup = new XTextField();
			txtSiteGroup.setMaxLength(2);
			txtSiteGroup.setNumeric(true);
			txtSiteGroup.setColumns(3);
			txtSiteGroup.setUpperCase(true);
			txtSiteGroup.setMinimumSize(txtSiteGroup.getPreferredSize());
			TextValidationController siteGroupTxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtSiteGroup, lblManSiteGroup, new TextRegexValidator("[0-9]+"));
			validationControllers.add(siteGroupTxtValidation);
			mandatoryFields.add(txtSiteGroup);
		}
		return txtSiteGroup;
	}
	
	public XTextField getListNameLocation() {
		if (lblListNameLocationInvalidEntry == null) {
			lblListNameLocationInvalidEntry = new JLabel(" ");
		}
		if (txtListNameLocation == null) {
			txtListNameLocation = new XTextField();
			txtListNameLocation.setMaxLength(39);
			txtListNameLocation.setColumns(10);
			txtListNameLocation.setUpperCase(true);
			txtListNameLocation.setMinimumSize(txtListNameLocation.getPreferredSize());
			TextValidationController listNameLocationTxtValidation = ValidationControllerFactory.createText(this,
					txtListNameLocation, lblListNameLocationInvalidEntry, new TextRegexValidator("^.{1,39}$"));
			validationControllers.add(listNameLocationTxtValidation);
		}
		return txtListNameLocation;
	}
	
	public XTextArea getFreeTextLine() {
		if (lblFreeTextLineInvalidEntry == null) {
			lblFreeTextLineInvalidEntry = new JLabel(" ");
		}
		if (txtFreeTextLine == null) {
			txtFreeTextLine = new XTextArea();
			txtFreeTextLine.setRows(5);
			txtFreeTextLine.setColumns(30);
			txtFreeTextLine.setLimit(100);
			txtFreeTextLine.setLineWrap(true);
			txtFreeTextLine.setMaximumSize(txtFreeTextLine.getPreferredSize());
			TextValidationController freeTextLineTxtValidation = ValidationControllerFactory.createText(this,
					txtFreeTextLine, lblFreeTextLineInvalidEntry, new TextRegexValidator("^.{1,100}$"));
			validationControllers.add(freeTextLineTxtValidation);
		}
		return txtFreeTextLine;
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
	
	public JCheckBox getSatelliteCourtCheckbox() {
		if (chkBxSatelliteCourt == null) {
			chkBxSatelliteCourt = new JCheckBox();
			chkBxSatelliteCourt.setEnabled(false);
		}
		return chkBxSatelliteCourt;
	}
	
	public XTextField getLCDCode() {
		if (txtLCDCode == null) {
			txtLCDCode = new TextField(true);
			txtLCDCode.setMaxLength(3);
			txtLCDCode.setNumeric(true);
			txtLCDCode.setColumns(10);
			txtLCDCode.setUpperCase(true);
			txtLCDCode.setMinimumSize(txtLCDCode.getPreferredSize());
		}
		return txtLCDCode;
	}
	
	public XTextField getTier() {
		if (txtTier == null) {
			txtTier = new TextField(true);
			txtTier.setMaxLength(1);
			txtTier.setNumeric(true);
			txtTier.setColumns(10);
			txtTier.setUpperCase(true);
			txtTier.setMinimumSize(txtTier.getPreferredSize());
		}
		return txtTier;
	}
	
	public JCheckBox getNotInUseCheckbox() {
		if (lblNotInUseInvalidEntry == null) {
			lblNotInUseInvalidEntry = new JLabel(" ");
		}
		if (chkBxNotInUse == null) {
			chkBxNotInUse = new JCheckBox();
		}
		return chkBxNotInUse;
	}
	
	/**
	 * Returns the table that will hold the CourtRooms table
	 * @return
	 */
	public JTable getCourtRoomsTable() {
		if (courtRoomsTable == null) {
			courtRoomsTable = XTableFactory.getInstance().createDefaultTable(getCourtRoomsTableModel());
			courtRoomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			courtRoomsTable.setPreferredScrollableViewportSize(courtRoomsTable.getPreferredSize());
			CourtRoomNumberRenderer renderer = new CourtRoomNumberRenderer(
					courtRoomsTable.getDefaultRenderer(Integer.class), getCourtRoomsTableModel());
			courtRoomsTable.getColumnModel().getColumn(COURTROOMNO_COL).setCellRenderer(renderer);
			CourtRoomNumberCellEditor cellEditor = new CourtRoomNumberCellEditor(getCourtRoomNoCellEditor());
			courtRoomsTable.getColumnModel().getColumn(COURTROOMNO_COL).setCellEditor(cellEditor);
		}
		return courtRoomsTable;
	}
	
	//Used for the court rooms table cell editor
	private XTextField getCourtRoomNoCellEditor() {
		XTextField textField = new XTextField(2, "([1-9][0-9])|(0?[1-9])");
		textField.setNumeric(true);
		textField.setMaxLength(2);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int rowIndex = courtRoomsTable.getSelectedRow();
				if (rowIndex >= 0) {
					// Get the edited value
					String courtRoomNo = (String)courtRoomsTable.getColumnModel().getColumn(COURTROOMNO_COL).getCellEditor().getCellEditorValue();
					if(courtRoomNo!=null && !courtRoomNo.equals("")) {
						Integer value = Integer.parseInt(courtRoomNo);				
						// Update the basicValue with the edited value
						getCourtRoomBasicValue(rowIndex).setCrestCourtRoomNo(value);
						// Log that there has been a change
						pageController.setPageChanged();
					}
				}
			}
		});
		return textField;
	}
	
	/**
	 * Getter for Site Locations Table Model.
	 * 
	 * @return
	 */
	private CourtRoomTableModel getCourtRoomsTableModel() {
		if (courtRoomsTableModel == null) {
			courtRoomsTableModel = new CourtRoomTableModel();
			courtRoomsTableModel.addTableModelListener(new TableModelListener() {

				@Override
				public void tableChanged(TableModelEvent e) {
					if (e.getType() == TableModelEvent.UPDATE) {
						if (e.getColumn() != -1) {
							pageController.setPageChanged();
						}
					}
				}
			});	
		}

		return courtRoomsTableModel;
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

		public AddAction(CourtSiteLocationPanel parent) {
			populateFromBundle("CourtSiteAdd");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			if (validateAllCourtRooms()) {
				getCourtRoomsTableModel().addRow(new Object[] { null, false, false, false });
				getCourtRoomsTable().requestFocus();
				getCourtRoomsTable().changeSelection(0, COURTROOMNO_COL, false, false);
			}
		}
		
	}
	
	/**
	 * Returns the Back button
	 * @return
	 */
	public JButton getBackButton() {
		if (btnBack == null) {
			btnBack = new JButton(new BackAction(this));
		}
		return btnBack;
	}
	
	private class BackAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public BackAction(CourtSiteLocationPanel parent) {
			populateFromBundle("CourtSiteBack");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			if (btnSave.isEnabled()) {
				showUnsavedCancelConfirmationDialog();
			}
			performBackAction(false);
		}
		
	}
	
	/**
	 * Returns the Save button
	 * @return
	 */
	public JButton getSaveButton() {
		if (btnSave == null) {
			btnSave = new JButton(new SaveAction(this));
		}
		return btnSave;
	}
	
	private class SaveAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public SaveAction(CourtSiteLocationPanel parent) {
			populateFromBundle("CourtSiteSave");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			performSave();
		}
		
	}
	
	/**
	 * @return the model
	 */
	public CourtSiteLocationModel getCourtSiteModel() {
		return courtSiteModel;
	}

	/**
	 * @param courtSiteModel
	 *            the courtSiteModel to set
	 */
	public void setCourtSiteModel(CourtSiteLocationModel courtSiteModel) {
		this.courtSiteModel = courtSiteModel;
	}
	
	/**
	 * @return the homeCourtModel
	 */
	public HomeCourtModel getHomeCourtModel() {
		return homeCourtModel;
	}

	/**
	 * @param homeCourtModel
	 *            the homeCourtModel to set
	 */
	public void setHomeCourtModel(HomeCourtModel homeCourtModel) {
		this.homeCourtModel = homeCourtModel;
	}
	
	private String getTxtSiteName() {
		return txtSiteName.getText();
	}

	private String getTxtAddress1() {
		return txtAddress1.getText();
	}

	private String getTxtAddress2() {
		return txtAddress2.getText();
	}

	private String getTxtAddress3() {
		return txtAddress3.getText();
	}

	private String getTxtAddress4() {
		return txtAddress4.getText();
	}

	private String getTxtTown() {
		return txtTown.getText();
	}

	private String getTxtPostcode() {
		return txtPostcode.getText();
	}

	private String getTxtCounty() {
		return txtCounty.getText();
	}

	private String getTxtTelNumber() {
		return txtTelNumber.getText();
	}

	private String getTxtFaxNumber() {
		return txtFaxNumber.getText();
	}

	private String isSatellite() {
		if (chkBxSatelliteCourt.isSelected()) {
			return TRUE;
		} else {
			return FALSE;
		}
	}

	private String getTxtLCDCode() {
		return txtLCDCode.getText();
	}

	private String getTxtTier() {
		return txtTier.getText();
	}

	private String isObsolete() {
		if (chkBxNotInUse.isSelected()) {
			return TRUE;
		} else {
			return FALSE;
		}
	}

	private String getTxtSiteCode() {
		return txtSiteCode.getText();
	}

	private Integer getTxtSiteGroup() {
		try {
			String text = txtSiteGroup.getText();
			if (text != null && !text.equals("")) {
				return Integer.parseInt(text);
			}
		} catch (NumberFormatException nfe) {
			log.error("Number Format exception when converting Site Group to Integer");
		}
		return null;
	}

	private String getTxtListNameLocation() {
		return txtListNameLocation.getText();
	}

	private String getTxtFreeTextLine() {
		return txtFreeTextLine.getText();
	}

	private List<CourtRoomBasicValue> getCourtRooms() {
		if (getCourtSiteModel().getCourtSite() != null && getCourtSiteModel().getCourtSite().getId() != null) {
			return getCourtRoomsForSaving(getCourtSiteModel().getCourtSite().getId());
		} else {
			return getCourtRoomsForSaving(null);
		}
	}

	private void setTxtSiteName(String siteName) {
		txtSiteName.setText(siteName);
	}

	private void setTxtAddress1(String address1) {
		txtAddress1.setText(address1);
	}

	private void setTxtAddress2(String address2) {
		txtAddress2.setText(address2);
	}

	private void setTxtAddress3(String address3) {
		txtAddress3.setText(address3);
	}

	private void setTxtAddress4(String address4) {
		txtAddress4.setText(address4);
	}

	private void setTxtTown(String town) {
		txtTown.setText(town);
	}

	private void setTxtCounty(String county) {
		txtCounty.setText(county);
	}

	private void setTxtPostcode(String postcode) {
		txtPostcode.setText(postcode);
	}

	private void setTxtTelNumber(String telNumber) {
		txtTelNumber.setText(telNumber);
	}

	private void setSatelliteCourt(CourtSiteComplexValue val) {
		if (val == null || val.getCourtSatellite() == null) {
			chkBxSatelliteCourt.setSelected(false);
		} else if (val.getCourtSatellite() != null) {
			if ("Y".equals(val.getCourtSatellite().getObsInd())) {
				chkBxSatelliteCourt.setSelected(false);
			} else {
				chkBxSatelliteCourt.setSelected(true);
			}
		}
	}

	private void setNotInUse() {
		chkBxNotInUse.setSelected(false);
	}

	private void setTxtLCDCode(String lcdCode) {
		txtLCDCode.setText(lcdCode);
	}

	private void setTxtSiteCode(String siteCode) {
		txtSiteCode.setText(siteCode);
	}

	private void setTxtSiteGroup(Integer siteGroup) {
		if (siteGroup != null) {
			txtSiteGroup.setText(siteGroup.toString());
		} else {
			txtSiteGroup.setText("");
		}
	}

	private void setTxtListNameLocation(String listName) {
		txtListNameLocation.setText(listName);
	}

	private void setTxtFreeTextLine(String floaterTxt) {
		txtFreeTextLine.setText(floaterTxt);
	}

	private void setTxtFaxNumber(String faxNumber) {
		txtFaxNumber.setText(faxNumber);
	}

	private void setTxtTier(String tier) {
		txtTier.setText(tier);
	}
	
	protected void performBackAction(Boolean update) {
		if (update) {
			parentDialog.showHomeCourtPanelAgain(update);
		} else {
			parentDialog.showHomeCourtPanel();
		}
		resetFields();
		hideErrorLabels();
		this.parentDialog.getCancelButton().setEnabled(true);
		// Set the default button to cancel.
		
		
		final JRootPane rootPane = SwingUtilities.getRootPane(this.parentDialog.getCancelButton());
		rootPane.setDefaultButton(this.parentDialog.getCancelButton());
	}
	
	protected void resetFields() {
		setTxtSiteName("");
		setTxtAddress1("");
		setTxtAddress2("");
		setTxtAddress3("");
		setTxtAddress4("");
		setTxtTown("");
		setTxtCounty("");
		setTxtPostcode("");
		setTxtTelNumber("");
		setSatelliteCourt(null);
		setNotInUse();
		setTxtLCDCode("");
		setTxtSiteCode("");
		setTxtSiteGroup(null);
		setTxtListNameLocation("");
		setTxtFreeTextLine("");
		setTxtFaxNumber("");
		setTxtTier("");

		// reset table
		getCourtRoomsTableModel().setData(new CourtSiteBasicValue[] {});

		// clear model
		getCourtSiteModel().setCourtSite(null);
		//setModified(false);
	}
	
	/**
	 * Hide any error labels displaying.
	 */
	public void hideErrorLabels() {
		lblManSiteName.setText(" ");
		lblManAddress1.setText(" ");
		lblAddress2InvalidEntry.setText(" ");
		lblAddress3InvalidEntry.setText(" ");
		lblAddress4InvalidEntry.setText(" ");
		lblTownInvalidEntry.setText(" ");
		lblCountyInvalidEntry.setText(" ");
		lblManPostcode.setText(" ");
		lblTelNumberInvalidEntry.setText(" ");
		lblManSiteCode.setText(" ");
		lblManSiteGroup.setText(" ");
		lblListNameLocationInvalidEntry.setText(" ");
		lblFreeTextLineInvalidEntry.setText(" ");
		lblFaxNumberInvalidEntry.setText(" ");
		lblNotInUseInvalidEntry.setText(" ");
	}
	
	/**
	 * Checks to see if the entered Court Site Code doesn't already exist in any
	 * of the other Court Sites.  This checks obsolete site codes as well (insert only)
	 * 
	 * @param siteCode
	 * @return
	 */
	private boolean checkForDuplicateSiteCode(String siteCode) {
		boolean isValid = true;
		
		Integer courtSiteId;
		if (getCourtSiteModel().getCourtSite() != null) {
			// Updating a court site.  Check that there are no live duplicates in case any duplicates
			// already exist
			courtSiteId = getCourtSiteModel().getCourtSite().getId();
			for (CourtSiteBasicValue basic : getHomeCourtModel().getCourtSites()) {
				Integer iterationId = basic.getId();
				if ( siteCode.equals(basic.getCourtSiteCode()) && !iterationId.equals(courtSiteId) &&
						!YES.equals(basic.getObsInd())) {
					showDuplicateSiteCodeDialog();
					isValid = false;
					txtSiteCode.setForeground(Color.RED);
					txtSiteCode.requestFocus();
					break;
				}
			}
		} else {
			// Inserting a court site.  Check that there are no duplicates (live or obsolete)
			courtSiteId = 0;
			for (CourtSiteBasicValue basic : getHomeCourtModel().getCourtSites()) {
				Integer iterationId = basic.getId();
				if ( siteCode.equals(basic.getCourtSiteCode()) && !iterationId.equals(courtSiteId) ) {
					showDuplicateSiteCodeDialog();
					isValid = false;
					txtSiteCode.setForeground(Color.RED);
					txtSiteCode.requestFocus();
					break;
				}
			}
		}
		
		if (isValid) {
			txtSiteCode.setForeground(Color.BLACK);
		}
		return isValid;
	}
	
	public void showDuplicateSiteCodeDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.DuplicateSiteCodeTitle"), true,
				XMessageBox.ICONERROR,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.DuplicateSiteCodeMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}
	
	/**
	 * Validates all Court Rooms prior to saving.
	 * 
	 */
	private boolean validateAllCourtRooms() {
		boolean isValid = true;
		Collection<Object> allCourtRooms = getCourtRoomsTableModel().getDataAsCollection();
		if (allCourtRooms.size() > 0) {
			int index = 0;
			for (Iterator<Object> iterator = allCourtRooms.iterator(); iterator.hasNext();) {
				iterator.next();
				if (!validateRoomNo(index)) {
					isValid = false;
					break;
				}
				index++;
			}
		}
		return isValid;
	}
	
	/**
	 * Checks whether if the mandatory field is entered or if the same Court
	 * Room Number doesn't exist already for a given row number.
	 * 
	 * @param roomNo
	 * @return
	 */
	private boolean validateRoomNo(int rowIndex) {
		boolean isValid = true;
		if (!validateMandatoryRoomNo(rowIndex) || !validateDuplicateRoomNo(rowIndex)) {
				isValid = false;
			}
		return isValid;
	}
	
	private CourtRoomBasicValue getCourtRoomBasicValue(int rowIndex) {
		CourtRoomBasicValue basicValue = (CourtRoomBasicValue) getCourtRoomsTableModel().getDataAt(rowIndex);
		return basicValue;
	}
	
	private boolean validateMandatoryRoomNo(int rowIndex) {
		boolean isValid = true;
		CourtRoomBasicValue basicValue = getCourtRoomBasicValue(rowIndex);
		Integer roomNo = basicValue.getCrestCourtRoomNo();
		if (roomNo == null) {
				showMissingCourtRoomNumberDialog();
			setFocusOnRoomNumberField(rowIndex);
			isValid = false;
		}
		return isValid;
	}
	
	public void showMissingCourtRoomNumberDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.MissingRoomNumberTitle"), true,
				XMessageBox.ICONERROR,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.MissingRoomNumberMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}
	
	private boolean validateDuplicateRoomNo(int rowIndex) {
		boolean isValid = true;
		CourtRoomBasicValue basicValue = getCourtRoomBasicValue(rowIndex);
		Integer roomNo = basicValue.getCrestCourtRoomNo();
		if (roomNo != null) {
			int index = 0;
			for (Object obj : getCourtRoomsTableModel().getDataAsCollection()) {
				CourtRoomBasicValue value = (CourtRoomBasicValue) obj;
				if (value.getCrestCourtRoomNo() != null) {
					if (roomNo.equals(value.getCrestCourtRoomNo()) && rowIndex != index) {
							showDuplicateCourtRoomNumberDialog();
						setFocusOnRoomNumberField(rowIndex);
						isValid = false;
						break;
					}
				}
				index++;
			}
		}
		return isValid;
	}
	
	public void showDuplicateCourtRoomNumberDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.DuplicateRoomNumberTitle"), true,
				XMessageBox.ICONERROR,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.DuplicateRoomNumberMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}

	private void setFocusOnRoomNumberField(int rowIndex) {
		getCourtRoomsTable().requestFocus();
	}
	
	private void performSave() throws CSValidationException, CSRecoverableException {
		boolean hasSaved = false;

		stepValidate();
		if (validateAllCourtRooms()) {
			if (checkForDuplicateSiteCode(getTxtSiteCode())) {
				String initialObs = null;
				if(getCourtSiteModel().getCourtSite()!=null) {
					if(getCourtSiteModel().getCourtSite().getObsInd()!=null) {
						initialObs = getCourtSiteModel().getCourtSite().getObsInd();
					} else {
						initialObs = "N";
					}
				} else {
					initialObs = "N";
				}
				moveScreenToModel();
				// If Insert needed
				if (!(getCourtSiteModel().getCourtSite().getId() != null
						&& !getCourtSiteModel().getCourtSite().getId().equals(""))) {
					hasSaved = insertCourtSite(getCourtSiteModel().getCourtSite());
				} else {
					hasSaved = updateCourtSite(getCourtSiteModel().getCourtSite(), initialObs);
				}
				if (hasSaved) {
					showSaveSuccessDialog();
					performBackAction(true);
				}
			}
		}
	}
	
	private void showUnsavedCancelConfirmationDialog() throws CSRecoverableException {
		boolean messageBoxReply = false;
		messageBoxReply = XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.UnsavedChangesTitle"), true,
				XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.UnsavedChangesMessage"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);
		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}

	public void showSaveSuccessDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.SaveSuccessTitle"), true,
				XMessageBox.ICONINFORMATION,
				XHIBITConstant.getResource(XhibitBundles.HomeCourt, "CourtSite.SaveSuccessMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}
	
	/**
	 * Insert a new Court Site and any associated Court Rooms in the database.
	 * If exception occurs a custom failure message will be displayed by calling
	 * method.
	 * 
	 * @return
	 */
	private boolean insertCourtSite(CourtSiteComplexValue courtSite) {
		boolean hasInserted = false;
		try {
			// Create Address
			AddressBasicValue address = new AddressBasicValue();
			address.setAddress1(courtSite.getAddress1());
			address.setAddress2(courtSite.getAddress2());
			address.setAddress3(courtSite.getAddress3());
			address.setAddress4(courtSite.getAddress4());
			address.setTown(courtSite.getTown());
			address.setCounty(courtSite.getCounty());
			address.setPostcode(courtSite.getPostcode());
			courtSite.setAddress(address);

			// Create Contacts
			if (!(getTxtTelNumber().equals(""))) {
				courtSite.setTelephoneNumber(getTxtTelNumber());
			}
			if (!(getTxtFaxNumber().equals(""))) {
				courtSite.setFaxNumber(getTxtFaxNumber());
			}

			// Create Court Site
			courtSite.setCourtId(COURT_ID);
			courtSite.setShortName(
					getShortName(getHomeCourtModel().getCourt().getShortName(), courtSite.getCourtSiteCode()));
			Integer courtSiteId = bizRefDelegate.createCourtSite(courtSite, DISPLAY_NAME);
			log.debug("Created COURT SITE ID: " + courtSiteId);
			courtSite.setId(courtSiteId);

			this.getCourtSiteModel().setCourtSite(courtSite);

			hasInserted = true;
		} catch (Exception er) {
			log.error("Error occurred while saving Court Site: " + er);
			XHIBITConstant.handleError(er);
		}

		return hasInserted;
	}
	
	/**
	 * Update an existing Court Site and associated Court Rooms in the database.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean updateCourtSite(CourtSiteComplexValue details, String initialObsolete) {
		boolean hasUpdated = false;

		try {
			bizRefDelegate.updateCourtSite(details.getId(), details, DISPLAY_NAME, initialObsolete);
			hasUpdated = true;
		} catch (Exception er) {
			log.error("Error occurred while saving Court Site: " + er);
			XHIBITConstant.handleError(er);
		}

		return hasUpdated;
	}
	
	/**
	 * Update Court Rooms list with court site id before passing to midtier for
	 * insert.
	 * 
	 * @param courtSiteId
	 * @return
	 */
	private List<CourtRoomBasicValue> getCourtRoomsForSaving(Integer courtSiteId) {
		List<CourtRoomBasicValue> retList = new ArrayList<CourtRoomBasicValue>();

		for (Object obj : getCourtRoomsTableModel().getDataAsCollection()) {
			CourtRoomBasicValue retValue = (CourtRoomBasicValue) obj;
			retValue.setCourtSiteId(courtSiteId);
			retValue.setCourtRoomName(COURT + " " + retValue.getCrestCourtRoomNo());
			retValue.setDescription(COURT_ROOM + " " + retValue.getCrestCourtRoomNo());
			retValue.setDisplayName(COURT_ROOM + " " + retValue.getCrestCourtRoomNo());
			retList.add(retValue);
		}
		return retList;
	}
	
	private String getShortName(String courtShortName, String siteCode) {
		StringBuffer retVal = new StringBuffer();
		if (courtShortName != null && !courtShortName.equals("")) {
			if (courtShortName.length() >= 5) {
				retVal.append(courtShortName.substring(0, 5));
			} else {
				retVal.append(courtShortName);
			}
		}
		if (siteCode != null && !siteCode.equals("")) {
			if (siteCode.length() > 1) {
				retVal.append(siteCode.substring(0, 1));
			} else {
				retVal.append(siteCode);
			}
		}
		return retVal.toString();
	}
	
	/**
	 * Populate the Court Site controls.
	 * 
	 * @param courtSite
	 */
	private void populateCourtSite(CourtSiteComplexValue courtSite) {
		setTxtSiteName(courtSite.getCourtSiteName());
		setTxtAddress1(courtSite.getAddress1());
		setTxtAddress2(courtSite.getAddress2());
		setTxtAddress3(courtSite.getAddress3());
		setTxtAddress4(courtSite.getAddress4());
		setTxtTown(courtSite.getTown());
		setTxtCounty(courtSite.getCounty());
		setTxtPostcode(courtSite.getPostcode());
		setTxtTelNumber(courtSite.getTelephoneNumber());
		setSatelliteCourt(courtSite);
		setTxtLCDCode(courtSite.getCrestCourtId());
		setTxtSiteCode(courtSite.getCourtSiteCode());
		setTxtSiteGroup(courtSite.getSiteGroup());
		setTxtListNameLocation(courtSite.getListName());
		setTxtFreeTextLine(courtSite.getFloaterText());
		setTxtFaxNumber(courtSite.getFaxNumber());
		setTxtTier(courtSite.getTier());
		if(courtSite.getObsInd()!=null && courtSite.getObsInd().equals("Y")) {
			getNotInUseCheckbox().setSelected(true);
		} else {
			getNotInUseCheckbox().setSelected(false);
		}
	}
	
	/**
	 * Populates the table with latest Court Sites data from model.
	 */
	private void populateCourtRoomsTable(List<CourtRoomBasicValue> courtRooms) {
		if (courtRooms != null) {
			Collections.sort(courtRooms, new CourtRoomSorter());
			getCourtRoomsTableModel().addCourtRooms(courtRooms);
		}
	}
	
	public void refreshData() {
		moveModelToScreen();
	}
	
	public void configureTabOrder() {
		parentDialog.setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { txtSiteName, txtAddress1, txtAddress2,
				txtAddress3, txtAddress4, txtTown, txtCounty, txtPostcode, txtTelNumber, chkBxSatelliteCourt,
				txtLCDCode, txtSiteCode, txtSiteGroup, txtListNameLocation, txtFreeTextLine, txtFaxNumber, txtTier,
				btnAdd, btnBack, btnSave}));
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
			}
		}
		
		btnSave.setEnabled(isValid);
	}
	
	protected void setDefaultButtonForEnter(final boolean saveEnabled) {
		if (saveEnabled) {
			// Set the default button to save.
			final JRootPane rootPane = SwingUtilities.getRootPane(btnSave);
			rootPane.setDefaultButton(btnSave);
		} else {
			// Set the default button to cancel.
			final JRootPane rootPane = SwingUtilities.getRootPane(btnBack);
			rootPane.setDefaultButton(btnBack);
			this.revalidate();
		}
	}
	
	private void setupChangeListeners() {
		pageController.addChangeListeners(detailsPanel.getComponents());
		pageController.addChangeListeners(sitePanel.getComponents());
		pageController.addChangeListeners(listPanel.getComponents());
		pageController.addChangeListeners(freeTextLinePanel.getComponents());
		pageController.addChangeListeners(faxNumberPanel.getComponents());
		pageController.addChangeListeners(lowerPanel.getComponents());
	}
	
    /**
	 * If the model is not null then populate the fields with the values.
	 * Also sets the caret to 0 so that if the field is too long then it'll
	 * show the first half of the string instead of the end of the string.
	 */
	private void moveModelToScreen() {
		pageController.setEnabled(false);
		if (getCourtSiteModel().getCourtSite() != null) {
			populateCourtSite(getCourtSiteModel().getCourtSite());
			populateCourtRoomsTable(getCourtSiteModel().getCourtSite().getCourtRooms());
			enableNotInUse();
		} else {
			getNotInUseCheckbox().setEnabled(false);
		}
		
		txtSiteName.requestFocus();
		setupChangeListeners();
		pageController.reset();
	}
	
	private void enableNotInUse() {
		if (getCourtSiteModel().getCourtSite().getId() != null) {
			getNotInUseCheckbox().setEnabled(true);
		} else {
			getNotInUseCheckbox().setEnabled(false);
		}
	}
	
	 /**
     * Set the data entered on screen into the model.
     */
	private void moveScreenToModel() {
		if (getCourtSiteModel().getCourtSite() != null && !("".equals(getCourtSiteModel().getCourtSite().getId()))) {
			this.getCourtSiteModel().updateValueObject(getTxtSiteName(), getTxtSiteCode(), getTxtAddress1(), getTxtAddress2(),
					getTxtAddress3(), getTxtAddress4(), getTxtTown(), getTxtCounty(), getTxtPostcode(), getTxtTelNumber(), getTxtFaxNumber(),
					isSatellite(), getTxtLCDCode(), getTxtTier(), isObsolete(), getTxtSiteGroup(), getTxtListNameLocation(), getTxtFreeTextLine(),
					getCourtRooms());
		} else {
			this.getCourtSiteModel().createValueObject(getTxtSiteName(), getTxtSiteCode(), getTxtAddress1(), getTxtAddress2(),
					getTxtAddress3(), getTxtAddress4(), getTxtTown(), getTxtCounty(), getTxtPostcode(), getTxtTelNumber(), getTxtFaxNumber(),
					isSatellite(), getTxtLCDCode(), getTxtTier(), isObsolete(), getTxtSiteGroup(), getTxtListNameLocation(), getTxtFreeTextLine(),
					getCourtRooms());
		}
	}
    
	public void disableEnableSiteCode(boolean isEnabled) {
			txtSiteCode.setEnabled(isEnabled);
		
	}
	@Override
	public void stepInitialise() throws CSRecoverableException {

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
			throw new CSValidationException("validation.general", " Field validation Failed");
		}
		if (!(lblManPostcode.getText().equals(" "))) {
			throw new CSValidationException("validation.general", " Field validation failed");
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
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
	}	
		
	private class TextField extends XTextField {
		
		private static final long serialVersionUID = 1L;
		private boolean readOnly;
		
		public TextField(boolean readOnly) {
			super();
			this.readOnly = readOnly;
			setFocusable(!this.readOnly);
			setEditable(!this.readOnly);
		}
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
			setDefaultButtonForEnter(enabled);
		}
	}
}
