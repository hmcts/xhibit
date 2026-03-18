package uk.gov.courtservice.xhibit.client.casemanagement.publicrep;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.services.defoncaserefsolfirm.DefOnCaseRefSolFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.legalaidorder.LegalAidOrderControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.prosecutorrefsolfirm.ProsecutorRefSolFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.refsolicitorfirm.RefSolicitorFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidAmendmentBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidOrderBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm.ProsecutorRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayOARDAAction;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayOARRAction;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayOGRDAAction;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayOGRRAction;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayOWRDAAction;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayOWRRAction;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchSolicitorFirmAction;
import uk.gov.courtservice.xhibit.client.casemanagement.DefendantAppellantTab;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractTextValidator;
import uk.gov.courtservice.xhibit.client.util.validation.ComboBoxValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrBeforeTodayValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class PublicRepresentationPanel extends XPanel implements ValidationListener {

	// Strings for popup messages from resource bundle
	private String repOrderSuccess = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"repOrder.successMessage");
	private String repRevokeOrderSuccess = ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
			"repOrder.revokeSuccessMessage");
	private static final long serialVersionUID = 1L;
	private PublicRepresentationModel model;
	private PublicRepresentationDialog parentDialog;
	private DefOnCaseRefSolFirmValue privateToPublicRep;
	private ProsecutorRefSolFirmValue privateToPublicRepPros;
	private final String solAmend = "SOLICITOR";
	private final String counselAmend = "COUNSEL";
	private final String correctionAmend = "CORRECTION";
	private LegalAidAmendmentBasicValue lbv = null;

	@SuppressWarnings("unchecked")
	Vector<String> reasonForRevocationValues = ResourceBundleHelper.getResourcesStartingWith(
			ResourceBundleHelper.getResourceBundle(XhibitBundles.CaseMaintenanceResources),
			"repOrder.revocation.reasonDropdown");
  
  private Integer courtid = XhibitSingleton.getInstance().getCourtId();
	
	/**
	 * Date Panel fields.
	 */
	private JLabel startDateError = null;
	private XDatePanel startDate = null;
	private JLabel endDateError = null;
	private XDatePanel endDate = null;
	private JLabel startDateLabel = null;
	private JLabel endDateLabel = null;

	/**
	 * Fields on main rep order panel
	 */
	private JLabel orderDateLbl = null;
	private JLabel orderDateError = null;
	private XDatePanel orderDate = null;
	private JLabel grantedByLbl = null;
	private ButtonGroup grantedByBg = null;
	private JRadioButton laaRb = null;
	private JRadioButton crownCtRb = null;
	private JLabel psdRoRefLbl = null;
	private XTextField psdRoRefTf = null;
	private JLabel numOfAdvocatesErrorLabel = null;
	private JLabel numOfAdvocatesLbl = null;
	private XTextField numOfAdvocatesTf = null;
	private JLabel numOfQcsErrorLabel = null;
	private JLabel numOfQcsLbl = null;
	private XTextField numOfQcsTf = null;
	private JButton addAmendBtn = null;

	/**
	 * Fields on solicitor firm details panel.
	 */
	private JLabel solNameLbl = null;
	private XTextField solNameTf = null;
	private JLabel solAddressLbl = null;
	private XTextField solAddress1Tf = null;
	private XTextField solAddress2Tf = null;
	private XTextField solAddress3Tf = null;
	private XTextField solAddress4Tf = null;
	private XTextField solTownTf = null;
	private XTextField solCountyTf = null;
	private XTextField solPostcodeTf = null;
	private JLabel solReferenceLbl = null;
	private XTextField solReferenceTf = null;
	private JLabel solDocExRefLbl = null;
	private XTextField solDocExRefTf = null;
	private JLabel solTelephoneNoLbl = null;
	private XTextField solTelehponeNoTf = null;
	private JLabel solFaxNoLbl = null;
	private XTextField solFaxNoTf = null;
	private JLabel solSecureEmailLbl = null;
	private XTextField solSecureEmailTf = null;
	private JLabel solNonSecureEmailLbl = null;
	private XTextField solNonSecureEmailTf = null;

	/**
	 * Fields on amendment panel.
	 */
	private JLabel amendmentDateError;
	private JLabel amendmentDateLbl;
	private XDatePanel amendDate;
	private JLabel orderAmendmentError = null;
	private JLabel orderAmendmentlbl = null;

	private XComboBox orderAmendmentCmbBx = null;
	/**
	 * Fields on revocation panel.
	 */
	private JLabel revocationDateLbl = null;
	private JLabel revocationDateError = null;
	private XDatePanel revocationDate = null;
	private JLabel revocationReasonError = null;
	private JLabel revocationReasonlbl = null;
	private XComboBox reasonForRevocationCmbBx = null;

	/**
	 * Buttons.
	 */
	private JButton saveBtn;
	private JButton cancelBtn;
	private JButton printAmendmentBtn;
	private JButton printOrderBtn;
	private JButton deleteOrderBtn;
	private JButton revokeOrderBtn;
	private JButton addOrderBtn;
	private JButton amendOrderBtn;

	/**
	 * JPanels.
	 */
	private JPanel topPanel = null;
	private JPanel mainOrderPanel = null;
	private JPanel solicitorFirmPanel = null;
	private JPanel repDatePanel = null;
	private JPanel amendRevokePanel = null;
	private JPanel amendPanel = null;
	private JPanel revocationPanel = null;
	private JPanel mainButtonPanel = null;

	/**
	 * Validators.
	 */
	private DateValidationController orderDateValCon = null;
	private DateValidationController revocationDateValCon = null;
	private DateValidationController startDateValCon = null;
	private DateValidationController endDateValCon = null;
	private DateValidationController amendDateValCon = null;
	private TextValidationController qcValCon = null;
	private TextValidationController advocatesValCon = null;
	private ComboBoxValidationController revocationReasonValCon = null;
	private ComboBoxValidationController orderAmendmentValCon = null;

	/**
	 * DelegateHelpers
	 */
	private DefOnCaseRefSolFirmControllerBeanBusinessDelegate defOnCaseDelegate;
	private ProsecutorRefSolFirmControllerBeanBusinessDelegate prosecutorDelegate;
	private BisRefControllerBeanBusinessDelegate bizRefDelegate;
	private LegalAidOrderControllerBeanBusinessDelegate legalDelegate;
	private RefSolicitorFirmControllerBeanBusinessDelegate refSolDelegate;

	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();

	private ActionType actionTaken = ActionType.DEFAULT;
	private CustomButtonPanel buttonPanel;
	private String userDisplayName = XhibitSingleton.getInstance().getUserSession()
			.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);

	private Boolean fieldsChangedGlobal = false;

	private static final Logger log = CSServices.getLogger(PublicRepresentationPanel.class);
	
	/**
	 * Needed for knowing whether to write to the log
	 */
	private Date originalEndDate = null;

	public PublicRepresentationPanel(PublicRepresentationDialog parent, PublicRepresentationModel model)
			throws CSRecoverableException {

		// Set up various delegates
		defOnCaseDelegate = XhibitDelegateHelper.getDefOnCaseRefSolFirmDelegate();
		prosecutorDelegate = XhibitDelegateHelper.getProsRefSolFirmDelegate();
		bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		legalDelegate = XhibitDelegateHelper.getLegalAidDelegate();
		refSolDelegate = XhibitDelegateHelper.getRefSolicitorFirmController();

		this.model = model;
		this.parentDialog = parent;
		Collections.sort(reasonForRevocationValues);
		stepInitialise();
		jbInit();
	}

	/**
	 * Initialises the look and feel of the panel.
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(920, 600));
		GridBagConstraints gbc = getGridBagLayout();
		// Set the save and close on the parent dialog
		buttonPanel = (CustomButtonPanel) parentDialog.getButtonPanel();

		// Change so that it doesn't close the page when you click save
		saveBtn = buttonPanel.addButton("PublicRepSave", false, true);
		saveBtn.setEnabled(false);

		// had to change to its own button so that it doesn't by default close
		// the popup
		cancelBtn = buttonPanel.addButton("btnCancel", false, false);
		cancelBtn.setEnabled(true);

		gbc.weighty = 0.5;
		this.add(getTopPanel(), gbc);

		gbc.weighty = 0.1;
		gbc.gridy++;
		this.add(getRepDatePanel(), gbc);

		gbc.weighty = 0.3;
		gbc.gridy++;
		this.add(getAmendRevokePanel(), gbc);

		gbc.weighty = 0.1;
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(getMainButtonPanel(), gbc);

		// Set panels to be disabled
		moveModelToScreen();
		setUpState();

	}

	/**
	 * Returns a panel which contains the main orders and solicitor firm panel
	 * 
	 * @return
	 */
	public JPanel getTopPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (topPanel == null) {
			topPanel = new JPanel(new GridBagLayout());
			gbc.weightx = 0.25;
			gbc.weighty = 0.75;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			topPanel.add(getMainOrdersPanel(), gbc);

			gbc.fill = GridBagConstraints.BOTH;

			gbc.weighty = 1;
			gbc.weightx = 0.75;
			gbc.gridx = 1;
			topPanel.add(getSolicitorFirmPanel(), gbc);
		}

		return topPanel;

	}

	/**
	 * This returns the main orders panel including: OrderDate GrantedBy PSD R/O
	 * Ref Number of Advocates/Counsels Number of QCs Add Amend Button
	 * 
	 * @returns the ordersPanel
	 */
	public JPanel getMainOrdersPanel() {
		if (mainOrderPanel == null) {
			mainOrderPanel = new JPanel();
			mainOrderPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getGridBagLayout();

			gbc.gridx++;
			gbc.gridwidth = 2;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			orderDateError = new JLabel(" ");
			mainOrderPanel.add(orderDateError, gbc);

			// order Date
			gbc.gridx = 0;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridy++;
			orderDateLbl = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.orderDateLabel"));
			mainOrderPanel.add(orderDateLbl, gbc);

			gbc.gridx++;
			gbc.gridwidth = 2;
			mainOrderPanel.add(getOrderDate(), gbc);

			// Granted by (radio buttons)
			gbc.gridwidth = 1;
			gbc.gridx = 0;
			gbc.gridy++;
			grantedByLbl = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.grantedByLabel"));
			mainOrderPanel.add(grantedByLbl, gbc);

			grantedByBg = new ButtonGroup();
			gbc.gridx++;
			laaRb = new JRadioButton(GrantedBy.LA.getDisplayName(), true);
			laaRb.setForeground(Color.black);
			laaRb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					psdRoRefTf.setEnabled(true);
				}
			});
			grantedByBg.add(laaRb);
			mainOrderPanel.add(laaRb, gbc);

			gbc.gridx++;
			crownCtRb = new JRadioButton(GrantedBy.CC.getDisplayName());
			crownCtRb.setForeground(Color.black);
			crownCtRb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					psdRoRefTf.setText("");
					psdRoRefTf.setEnabled(false);

				}
			});
			grantedByBg.add(crownCtRb);
			mainOrderPanel.add(crownCtRb, gbc);

			// PSD R/O Ref (text field)
			gbc.gridx = 0;
			gbc.gridy++;
			psdRoRefLbl = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.psdLabel"));
			mainOrderPanel.add(psdRoRefLbl, gbc);

			gbc.gridx = 1;
			gbc.gridwidth = 2;
			mainOrderPanel.add(getPsdRoRef(), gbc);

			gbc.gridx = 0;
			gbc.gridy++;
			gbc.gridwidth = 3;
			numOfAdvocatesErrorLabel = new JLabel(" ");
			gbc.insets = XHIBITConstant.errorLabelInsets;
			mainOrderPanel.add(numOfAdvocatesErrorLabel, gbc);

			// Num of advocates (text field) - text displayed dependant on where
			// the screen was accessed
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.gridy++;
			gbc.gridheight = 2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				numOfAdvocatesLbl = new JLabel("<html>Number of<br> Advocates</html>");
			} else {
				numOfAdvocatesLbl = new JLabel("<html>Number of<br> Counsels</html>");
			}
			mainOrderPanel.add(numOfAdvocatesLbl, gbc);

			gbc.gridx++;
			gbc.gridheight = 1;
			mainOrderPanel.add(getNumOfAdvocates(), gbc);

			// adding an empty structure to stop the textbox filling both rows
			gbc.gridy++;
			gbc.gridheight = 1;
			mainOrderPanel.add(Box.createVerticalStrut(0), gbc);

			// Num of QCs (text field)
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.gridwidth = 3;
			numOfQcsErrorLabel = new JLabel(" ");
			gbc.insets = XHIBITConstant.errorLabelInsets;

			mainOrderPanel.add(numOfQcsErrorLabel, gbc);

			gbc.gridwidth = 1;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridx = 0;
			gbc.gridy++;
			numOfQcsLbl = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.numQCLabel"));
			mainOrderPanel.add(numOfQcsLbl, gbc);

			gbc.gridx++;
			mainOrderPanel.add(getNumOfQcs(), gbc);

			// Add amend solicitor firm button (button)
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			gbc.gridy++;

			XAction action = XhibitActions.getAction((XhibitApplicationController) parentDialog.getParentFrame(),
					XhibitActions.OpenSearchSolicitorFirm);
			action.setCaller(this);
			action.populateFromBundle("AddAmendSolFirm");
			addAmendBtn = new JButton(action);
			mainOrderPanel.add(addAmendBtn, gbc);
		}

		return mainOrderPanel;
	}

	/**
	 * This returns the solicitor firm panel.
	 */
	public JPanel getSolicitorFirmPanel() {
		if (solicitorFirmPanel == null) {
			solicitorFirmPanel = new JPanel();
			solicitorFirmPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant
					.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.solicitor.detailsLabel")));
			solicitorFirmPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getGridBagLayout();
			gbc.fill = GridBagConstraints.HORIZONTAL;

			gbc.weightx = 0.05;
			solNameLbl = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.solicitor.nameLabel"));
			solicitorFirmPanel.add(solNameLbl, gbc);

			gbc.weightx = 0.45;
			gbc.gridx++;
			solicitorFirmPanel.add(getSolName(), gbc);

			// address 1
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.weightx = 0.05;
			solAddressLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"repOrder.solicitor.address1Label"));
			solicitorFirmPanel.add(solAddressLbl, gbc);

			gbc.weightx = 0.45;
			gbc.gridx++;
			solicitorFirmPanel.add(getSolAddress1(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolAddress2(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolAddress3(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolAddress4(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolTown(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolCounty(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolPostcode(), gbc);

			// solicitor reference
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.weightx = 0.05;
			solReferenceLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"repOrder.solicitor.referenceLabel"));
			solicitorFirmPanel.add(solReferenceLbl, gbc);

			gbc.gridx++;
			solicitorFirmPanel.add(getSolReference(), gbc);

			// right hand side
			// Doc ex
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.weightx = 0.05;
			solDocExRefLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"repOrder.solicitor.docExLabel"));
			solicitorFirmPanel.add(solDocExRefLbl, gbc);

			gbc.weightx = 0.45;
			gbc.gridx++;
			solicitorFirmPanel.add(getSolDocEx(), gbc);

			// Telephone number
			gbc.gridx = 2;
			gbc.gridy++;
			gbc.weightx = 0.05;
			solTelephoneNoLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"repOrder.solicitor.telNoLabel"));
			solicitorFirmPanel.add(solTelephoneNoLbl, gbc);

			gbc.gridx++;
			gbc.weightx = 0.45;
			solicitorFirmPanel.add(getSolTelNo(), gbc);

			// Fax number
			gbc.gridx = 2;
			gbc.gridy++;
			gbc.weightx = 0.05;
			solFaxNoLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"repOrder.solicitor.faxNoLabel"));
			solicitorFirmPanel.add(solFaxNoLbl, gbc);

			gbc.gridx++;
			gbc.weightx = 0.45;
			solicitorFirmPanel.add(getSolFaxNo(), gbc);

			// secure email
			gbc.gridx = 2;
			gbc.gridy++;
			gbc.weightx = 0.05;
			solSecureEmailLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"repOrder.solicitor.secureEmailLabel"));
			solicitorFirmPanel.add(solSecureEmailLbl, gbc);

			gbc.gridx++;
			gbc.weightx = 0.45;
			solicitorFirmPanel.add(getSolSecureEmail(), gbc);

			// non secure email
			gbc.gridx = 2;
			gbc.gridy++;
			gbc.weightx = 0.05;
			solNonSecureEmailLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"repOrder.solicitor.nonSecureLabel"));
			solicitorFirmPanel.add(solNonSecureEmailLbl, gbc);

			gbc.gridx++;
			gbc.weightx = 0.45;
			solicitorFirmPanel.add(getSolNonSecureEmail(), gbc);

		}
		return solicitorFirmPanel;
	}

	/**
	 * Returns the Rep Date panel including start and end date
	 * 
	 * @return jpanel
	 */
	public JPanel getRepDatePanel() {
		GridBagConstraints gbc = getGridBagLayout();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		repDatePanel = new JPanel();
		repDatePanel.setLayout(new GridBagLayout());
		gbc.weightx = 0.5;

		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridx = 1;
		startDateError = new JLabel(" ");
		repDatePanel.add(startDateError, gbc);

		gbc.gridx = 0;
		gbc.gridy++;

		gbc.insets = XHIBITConstant.nonContainerInsets;

		// Add the panel elements
		startDateLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.startDateLabel"));
		repDatePanel.add(startDateLabel, gbc);

		gbc.gridx++;
		repDatePanel.add(getStartDate(), gbc);

		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridy = 0;
		gbc.gridx = gbc.gridx + 3;
		endDateError = new JLabel(" ");
		repDatePanel.add(endDateError, gbc);

		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridy++;
		gbc.gridx = 3;
		endDateLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.endDateLabel"));
		repDatePanel.add(endDateLabel, gbc);

		gbc.gridx++;
		repDatePanel.add(getEndDate(), gbc);

		gbc.gridx++;
		// add a dummy column in so that the fields are aligned correctly;
		repDatePanel.add(Box.createHorizontalStrut(150), gbc);

		return repDatePanel;
	}

	/**
	 * Returns the Amend and Revoke panels.
	 * 
	 * @return jpanel
	 */
	public JPanel getAmendRevokePanel() {
		GridBagConstraints gbc = getGridBagLayout();
		gbc.weightx = 0.5;
		amendRevokePanel = new JPanel();
		amendRevokePanel.setLayout(new GridBagLayout());
		amendRevokePanel.add(getAmendmentPanel(), gbc);

		gbc.gridx++;
		gbc.fill = GridBagConstraints.BOTH;
		amendRevokePanel.add(getRevocationPanel(), gbc);

		return amendRevokePanel;
	}

	public JPanel getAmendmentPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		amendPanel = new JPanel(new GridBagLayout());
		amendPanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.amendLabel")));
		
		gbc.gridx++;
		gbc.insets = XHIBITConstant.errorLabelInsets;
		orderAmendmentError = new JLabel(" ");
		amendPanel.add(orderAmendmentError, gbc);

		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx = 0;
		gbc.gridy++;
		orderAmendmentlbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.amendTypeLabel"));
		amendPanel.add(orderAmendmentlbl, gbc);
		gbc.gridx++;
		amendPanel.add(getOrderAmendment(), gbc);

		gbc.gridy++;

		gbc.insets = XHIBITConstant.errorLabelInsets;
		amendmentDateError = new JLabel(" ");
		amendPanel.add(amendmentDateError, gbc);
		gbc.weighty = 0.45;
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 1;
		amendmentDateLbl = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.amend.dateLabel"));
		amendPanel.add(amendmentDateLbl, gbc);

		gbc.gridx++;
		gbc.gridwidth = 2;
		amendPanel.add(getAmendDate(), gbc);

		return amendPanel;
	}

	/**
	 * Returns the Revocation Panel.
	 */
	public JPanel getRevocationPanel() {
		revocationPanel = new JPanel(new GridBagLayout());
		revocationPanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.revocationLabel")));
		GridBagConstraints gbc = getGridBagLayout();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.weighty = 0.1;
		gbc.gridx++;
		gbc.insets = XHIBITConstant.errorLabelInsets;
		revocationDateError = new JLabel(" ");
		revocationPanel.add(revocationDateError, gbc);

		gbc.weighty = 0.4;
		gbc.gridy++;
		gbc.gridx = 0;

		gbc.insets = XHIBITConstant.nonContainerInsets;
		revocationDateLbl = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.revocation.dateLabel"));
		revocationPanel.add(revocationDateLbl, gbc);

		gbc.gridx++;
		revocationPanel.add(getRevocationDate(), gbc);

		gbc.gridy++;
		gbc.insets = XHIBITConstant.errorLabelInsets;
		revocationReasonError = new JLabel(" ");
		revocationPanel.add(revocationReasonError, gbc);

		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx = 0;
		gbc.gridy++;
		revocationReasonlbl = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.revocation.reasonLabel"));
		revocationPanel.add(revocationReasonlbl, gbc);

		gbc.gridx++;
		revocationPanel.add(getReasonForRevocation(), gbc);

		return revocationPanel;
	}

	public JPanel getMainButtonPanel() {
		if (mainButtonPanel == null) {
			mainButtonPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = getGridBagLayout();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 0.25;

			printOrderBtn = new JButton(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"repOrder.button.printOrderLabel"));
			printOrderBtn.setAction(new PrintOrderButtonAction(this));
			
			mainButtonPanel.add(printOrderBtn, gbc);

			gbc.gridx++;
			printAmendmentBtn = new JButton(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"repOrder.button.printAmendLabel"));
			if (this.model.getCallingClass() instanceof DefendantAppellantTab) {
				// Print Defendant/Appellant Public Order
				printAmendmentBtn.setAction(new DisplayOARDAAction(this.model));
			}
			else {
				// Print Respondent Public Order
				printAmendmentBtn.setAction(new DisplayOARRAction(this.model));
			}

			mainButtonPanel.add(printAmendmentBtn, gbc);

			// Need to add blank column here so adding a horizontal strut
			gbc.gridx++;
			mainButtonPanel.add(Box.createHorizontalStrut(150), gbc);

			gbc.gridx++;
			deleteOrderBtn = new JButton(new DeleteOrderButtonAction(this));
			mainButtonPanel.add(deleteOrderBtn, gbc);

			gbc.gridx++;
			revokeOrderBtn = new JButton(new RevokeOrderButtonAction(this));
			revokeOrderBtn.addActionListener(new changeOccurred());
			mainButtonPanel.add(revokeOrderBtn, gbc);

			gbc.gridx++;
			addOrderBtn = new JButton(new AddOrderButtonAction(this));
			addOrderBtn.addActionListener(new changeOccurred());
			mainButtonPanel.add(addOrderBtn, gbc);

			gbc.gridx++;
			amendOrderBtn = new JButton(new AmendOrderButtonAction(this));
			amendOrderBtn.addActionListener(new changeOccurred());
			mainButtonPanel.add(amendOrderBtn, gbc);
		}
		return mainButtonPanel;
	}

	/**
	 * Returns the start date field. it's added to the validation controller
	 * when the new order button is pressed because it's only mandatory when
	 * it's enabled. Sets if it's null.
	 */
	public XDatePanel getStartDate() {
		if (startDate == null) {
			startDate = new XDatePanel(this, Calendar.getInstance(), false);
			startDate.getEntryField().getDisplay().setDisabledTextColor(Color.BLACK);
			startDateValCon = ValidationControllerFactory.createDateRequired(this, startDate, startDateError,
					new DateEqualOrBeforeTodayValidator());
			validationControllers.add(startDateValCon);
		}
		return startDate;
	}

	/**
	 * Returns the end date field. End date must be greater than start date Sets
	 * if it's null.
	 */
	public XDatePanel getEndDate() {
		endDate = new XDatePanel(this, null, false);
		endDate.getEntryField().getDisplay().setDisabledTextColor(Color.BLACK);
		endDateValCon = ValidationControllerFactory.createDateValid(this, endDate, endDateError,
				new AbstractDateValidator() {
					@Override
					public void validate(XDatePanel target, List<String> errors) {
						if (hasDate(target) && hasDate(startDate) && getDate(target).before(getDate(startDate))) {
							errors.add("End date before Start date");
						} else if (!hasDate(target) && getAmendType().equals(solAmend) && orderAmendmentCmbBx.isEnabled()) {
							errors.add("Field is mandatory");
						} else if (hasDate(target) && getAmendType().equals(solAmend) && orderAmendmentCmbBx.isEnabled()
								&& getDate(target).after(Calendar.getInstance())) {
							errors.add("Must be on/before today");
						}
					}
				},new DateEqualOrBeforeTodayValidator());
		validationControllers.add(endDateValCon);
		return endDate;
	}

	/**
	 * Returns the order date field. The order date cannot be in the future Sets
	 * if it's null.
	 */
	public XDatePanel getOrderDate() {
		if (orderDate == null) {
			orderDate = new XDatePanel(this, null, false);
			orderDate.getEntryField().getDisplay().setDisabledTextColor(Color.BLACK);
			orderDateValCon = ValidationControllerFactory.createDateRequired(this, orderDate, orderDateError,
					new DateEqualOrBeforeTodayValidator());
			validationControllers.add(orderDateValCon);
		}
		return orderDate;
	}

	/**
	 * Returns the amend date field. Will be auto-populated with current date.
	 * Sets if it's null.
	 */
	public XDatePanel getAmendDate() {
		if (amendDate == null) {
			amendDate = new XDatePanel(this, Calendar.getInstance(), false);
			amendDateValCon = ValidationControllerFactory.createDateValid(this, amendDate, amendmentDateError,
					new AbstractDateValidator() {
						@Override
						public void validate(XDatePanel target, List<String> errors) {
							if (getDate(target) == null && ((getAmendType().equals(solAmend) && orderAmendmentCmbBx.isEnabled())
									|| (getAmendType().equals(counselAmend) && orderAmendmentCmbBx.isEnabled()))) {
								errors.add("Field is mandatory");
							}
						}
					}, new DateEqualOrBeforeTodayValidator());
			validationControllers.add(amendDateValCon);
		}
		return amendDate;
	}

	/**
	 * Returns the revocation date field. By default this will be populated with
	 * current date, cannot be in the future. Sets if it's null.
	 */
	public XDatePanel getRevocationDate() {
		if (revocationDate == null) {
			revocationDate = new XDatePanel(this, null, false);
			revocationDate.setBorder(null);
			revocationDateValCon = ValidationControllerFactory.createDateRequired(this, revocationDate,
					revocationDateError, new DateEqualOrBeforeTodayValidator(), new AbstractDateValidator() {
						@Override
						public void validate(XDatePanel target, List<String> errors) {
							if (hasDate(target) && hasDate(startDate) && getDate(target).before(getDate(startDate))) {
								errors.add("Revocation date before Start date");
							}
						}
					});
			validationControllers.add(revocationDateValCon);
		}
		return revocationDate;
	}

	/**
	 * Returns the PSD R/O ref field. Sets if it's null.
	 */
	public XTextField getPsdRoRef() {
		if (psdRoRefTf == null) {
			psdRoRefTf = new XTextField();
			psdRoRefTf.setDisabledTextColor(Color.BLACK);
			psdRoRefTf.setUpperCase(true);
			psdRoRefTf.setMaxLength(20);
		}
		return psdRoRefTf;
	}

	/**
	 * Returns the Number of Advocates field. set max length to 3 and numeric to
	 * true so that you can only add numbers. Sets if it's null.
	 */
	public XTextField getNumOfAdvocates() {
		numOfAdvocatesTf = new XTextField();
		numOfAdvocatesTf.setMaxLength(1);
		numOfAdvocatesTf.setDisabledTextColor(Color.BLACK);
		numOfAdvocatesTf.setNumeric(true);

		// set up the validation controller on the field
		advocatesValCon = ValidationControllerFactory.createTextRequired(this, numOfAdvocatesTf,
				numOfAdvocatesErrorLabel, new AbstractTextValidator() {
					@Override
					public void validate(JTextComponent target, List<String> errors) {
						if (numOfAdvocatesTf.getText() != null && !numOfAdvocatesTf.getText().equals("")) {
							if (Integer.parseInt(numOfAdvocatesTf.getText()) > 3) {
								errors.add("Advocates must be 3 or less");
							} else {
								advocatesValCon.clearErrors();
							}
						} else if (numOfAdvocatesTf.getText() != null && !numOfAdvocatesTf.getText().equals("")) {
							advocatesValCon.clearErrors();
						}
						// call number of QCs validate
						qcValCon.validate(true);
					}
				});
		validationControllers.add(advocatesValCon);

		return numOfAdvocatesTf;
	}

	/**
	 * Returns the Number of QCs field. Sets max length to 1 and numeric to true
	 * so that you can only enter numeric. Sets if it's null.
	 */
	public XTextField getNumOfQcs() {
		numOfQcsTf = new XTextField();
		numOfQcsTf.setMaxLength(1);
		numOfQcsTf.setDisabledTextColor(Color.BLACK);
		numOfQcsTf.setNumeric(true);

		// set up the validation controller on the field
		qcValCon = ValidationControllerFactory.createText(this, numOfQcsTf, numOfQcsErrorLabel,
				new AbstractTextValidator() {
					@Override
					public void validate(JTextComponent target, List<String> errors) {
						if (numOfAdvocatesTf.getText() != null && !numOfAdvocatesTf.getText().equals("")
								&& numOfQcsTf.getText() != null && !numOfQcsTf.getText().equals("")) {
							if (Integer.parseInt(numOfQcsTf.getText()) > Integer.parseInt(numOfAdvocatesTf.getText())) {
								errors.add("KC must be less than/equal to advocates");
							} else if (Integer.parseInt(numOfQcsTf.getText()) > 1) {
								errors.add("KC must not be more than 1");
							} else {
								qcValCon.clearErrors();
							}
						} else if (numOfQcsTf.getText() != null && !numOfQcsTf.getText().equals("")
								&& Integer.parseInt(numOfQcsTf.getText()) > 1) {
							errors.add("KC must not be more than 1");
						} else {
							qcValCon.clearErrors();
						}
					}
				});
		validationControllers.add(qcValCon);
		return numOfQcsTf;
	}

	/**
	 * Returns the solicitor firm name. set columns to 15 for the size and reset
	 * the minimum size so that it resizes automatically and doesn't jump to a
	 * very small size. Sets if it's null.
	 */
	public XTextField getSolName() {
		if (solNameTf == null) {
			solNameTf = new XTextField();
			solNameTf.setDisabledTextColor(Color.BLACK);
			solNameTf.setColumns(15);
			solNameTf.setMinimumSize(solNameTf.getPreferredSize());
		}
		return solNameTf;
	}

	/**
	 * Returns the solicitor address. Sets if it's null.
	 */
	public XTextField getSolAddress1() {
		if (solAddress1Tf == null) {
			solAddress1Tf = new XTextField();
			solAddress1Tf.setDisabledTextColor(Color.BLACK);
		}
		return solAddress1Tf;
	}

	/**
	 * Returns the solicitor address. Sets if it's null.
	 */
	public XTextField getSolAddress2() {
		if (solAddress2Tf == null) {
			solAddress2Tf = new XTextField();
			solAddress2Tf.setDisabledTextColor(Color.BLACK);
		}
		return solAddress2Tf;
	}

	/**
	 * Returns the solicitor address. Sets if it's null.
	 */
	public XTextField getSolAddress3() {
		if (solAddress3Tf == null) {
			solAddress3Tf = new XTextField();
			solAddress3Tf.setDisabledTextColor(Color.BLACK);
		}
		return solAddress3Tf;
	}

	/**
	 * Returns the solicitor address. Sets if it's null.
	 */
	public XTextField getSolAddress4() {
		if (solAddress4Tf == null) {
			solAddress4Tf = new XTextField();
			solAddress4Tf.setDisabledTextColor(Color.BLACK);
		}
		return solAddress4Tf;
	}

	/**
	 * Returns the solicitor town. Sets if it's null.
	 */
	public XTextField getSolTown() {
		if (solTownTf == null) {
			solTownTf = new XTextField();
			solTownTf.setDisabledTextColor(Color.BLACK);
		}
		return solTownTf;
	}

	/**
	 * Returns the solicitor County. Sets if it's null.
	 */
	public XTextField getSolCounty() {
		if (solCountyTf == null) {
			solCountyTf = new XTextField();
			solCountyTf.setDisabledTextColor(Color.BLACK);
		}
		return solCountyTf;
	}

	/**
	 * Returns the solicitor Post code. Sets if it's null.
	 */
	public XTextField getSolPostcode() {
		if (solPostcodeTf == null) {
			solPostcodeTf = new XTextField();
			solPostcodeTf.setDisabledTextColor(Color.BLACK);
		}
		return solPostcodeTf;
	}

	/**
	 * Returns the solicitor reference. Sets if it's null, also has max length
	 * of 10.
	 */
	public XTextField getSolReference() {
		if (solReferenceTf == null) {
			solReferenceTf = new XTextField();
			solReferenceTf.setDisabledTextColor(Color.BLACK);
			solReferenceTf.setMaxLength(10);

		}
		return solReferenceTf;
	}

	/**
	 * Returns the solicitor doc ex ref. Sets if it's null. Resetting the
	 * minimum size otherwise when you resize the screen it just jumps to a
	 * small text box instead of gradually resizing (as it goes to the minimum
	 * size if it runs out of space).
	 */
	public XTextField getSolDocEx() {
		if (solDocExRefTf == null) {
			solDocExRefTf = new XTextField();
			solDocExRefTf.setDisabledTextColor(Color.BLACK);
			solDocExRefTf.setColumns(15);
			solDocExRefTf.setMinimumSize(solDocExRefTf.getPreferredSize());

		}
		return solDocExRefTf;
	}

	/**
	 * Returns the solicitor telephone number. Sets if it's null.
	 */
	public XTextField getSolTelNo() {
		if (solTelehponeNoTf == null) {
			solTelehponeNoTf = new XTextField();
			solTelehponeNoTf.setDisabledTextColor(Color.BLACK);
		}
		return solTelehponeNoTf;
	}

	/**
	 * Returns the solicitor fax number. Sets if it's null.
	 */
	public XTextField getSolFaxNo() {
		if (solFaxNoTf == null) {
			solFaxNoTf = new XTextField();
			solFaxNoTf.setDisabledTextColor(Color.BLACK);
		}
		return solFaxNoTf;
	}

	/**
	 * Returns the secure email. Sets if it's null.
	 */
	public XTextField getSolSecureEmail() {
		if (solSecureEmailTf == null) {
			solSecureEmailTf = new XTextField();
			solSecureEmailTf.setDisabledTextColor(Color.BLACK);

		}
		return solSecureEmailTf;
	}

	/**
	 * Returns the non secure email. Sets if it's null.
	 */
	public XTextField getSolNonSecureEmail() {
		if (solNonSecureEmailTf == null) {
			solNonSecureEmailTf = new XTextField();
			solNonSecureEmailTf.setDisabledTextColor(Color.BLACK);

		}
		return solNonSecureEmailTf;
	}

	/**
	 * Returns the Reason for Revocation
	 */
	public XComboBox getReasonForRevocation() {
		reasonForRevocationCmbBx = new XComboBox();

		if (!reasonForRevocationValues.isEmpty()) {
			reasonForRevocationCmbBx.setModel(new DefaultComboBoxModel(
					createDropdownValues("Select Revocation Type", reasonForRevocationValues).toArray()));
			reasonForRevocationCmbBx.setRenderer(new DropdownBoxCellRender());
		}

		revocationReasonValCon = ValidationControllerFactory.createComboBoxRequired(this, reasonForRevocationCmbBx,
				revocationReasonError);
		validationControllers.add(revocationReasonValCon);

		return reasonForRevocationCmbBx;
	}
	
	/**
	 * Returns the order for Amendment
	 */
	@SuppressWarnings("unchecked")
	public XComboBox getOrderAmendment() {
		orderAmendmentCmbBx = new XComboBox();
		
		Vector<String> orderAmendmentVec = new Vector<String>();
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			orderAmendmentVec = ResourceBundleHelper.getResourcesStartingWith(
					ResourceBundleHelper.getResourceBundle(XhibitBundles.CaseMaintenanceResources),
					"repOrder.defendant.amendDropdown");
		} else {
			orderAmendmentVec = ResourceBundleHelper.getResourcesStartingWith(
					ResourceBundleHelper.getResourceBundle(XhibitBundles.CaseMaintenanceResources),
					"repOrder.respondent.amendDropdown");
		}

		if (!orderAmendmentVec.isEmpty()) {
			orderAmendmentCmbBx.setModel(new DefaultComboBoxModel(
					createAmendmentValues("Select Amendment Type", orderAmendmentVec).toArray()));
			orderAmendmentCmbBx.setRenderer(new DropdownBoxCellRender());
		}

		orderAmendmentValCon = ValidationControllerFactory.createComboBoxRequired(this, orderAmendmentCmbBx,
				orderAmendmentError);
		validationControllers.add(orderAmendmentValCon);
		
		orderAmendmentCmbBx.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DropdownCodeStringValue value = (DropdownCodeStringValue)orderAmendmentCmbBx.getSelectedItem();
				if(value.getCode().equals(counselAmend) && orderAmendmentCmbBx.isEnabled()) {
					enableAdvocateCounsel();
				} else if(value.getCode().equals(solAmend) && orderAmendmentCmbBx.isEnabled()) {
					enableSolicitor();
				} else if(value.getCode().equals(correctionAmend) && orderAmendmentCmbBx.isEnabled()){
					enableCorrection();
				}
			}});
		
		return orderAmendmentCmbBx;
	}
	
	

	/**
	 * Disable/Enable all the buttons .
	 */
	public void disableEnableButtons(boolean isEnabled) {
		printOrderBtn.setEnabled(isEnabled);
		printAmendmentBtn.setEnabled(isEnabled);
		deleteOrderBtn.setEnabled(isEnabled);
		revokeOrderBtn.setEnabled(isEnabled);
		addOrderBtn.setEnabled(isEnabled);
		amendOrderBtn.setEnabled(isEnabled);
	}

	/**
	 * Disable/Enable all fields on the main order panel.
	 */
	public void disableEnableMainOrderFields(boolean isEnabled) {
		addAmendBtn.setEnabled(isEnabled);
		orderDate.setEnabled(isEnabled);
		laaRb.setEnabled(isEnabled);
		crownCtRb.setEnabled(isEnabled);
		if (laaRb.isSelected() && laaRb.isEnabled()) {
			psdRoRefTf.setEnabled(true);
		} else {
			psdRoRefTf.setEnabled(false);

		}
		numOfAdvocatesTf.setEnabled(isEnabled);
		numOfQcsTf.setEnabled(isEnabled);
	}

	/**
	 * Disable/Enable all fields on the solicitor panel.
	 */
	public void disableEnableSolicitorFields(boolean isEnabled) {
		solNameTf.setEnabled(isEnabled);
		solAddress1Tf.setEnabled(isEnabled);
		solAddress2Tf.setEnabled(isEnabled);
		solAddress3Tf.setEnabled(isEnabled);
		solAddress4Tf.setEnabled(isEnabled);
		solTownTf.setEnabled(isEnabled);
		solCountyTf.setEnabled(isEnabled);
		solPostcodeTf.setEnabled(isEnabled);
		solReferenceTf.setEnabled(isEnabled);
		solDocExRefTf.setEnabled(isEnabled);
		solTelehponeNoTf.setEnabled(isEnabled);
		solFaxNoTf.setEnabled(isEnabled);
		solSecureEmailTf.setEnabled(isEnabled);
		solNonSecureEmailTf.setEnabled(isEnabled);
	}

	/**
	 * Disable/Enable start/end date.
	 */
	public void disableEnableRepDates(boolean isEnabled) {
		startDate.setEnabled(isEnabled);
		endDate.setEnabled(isEnabled);
	}

	/**
	 * Disable/Enable Amendement.
	 */
	private void disableEnableAmendment(boolean isEnabled) {
		orderAmendmentCmbBx.setEnabled(isEnabled);
		amendDate.setEnabled(isEnabled);
	}

	/**
	 * Disable/Enable Revocation
	 */
	private void disableEnableRevocation(boolean isEnabled) {
		revocationDate.setEnabled(isEnabled);
		if(isEnabled) {
			revocationDate.setDate(Calendar.getInstance());
		}
		reasonForRevocationCmbBx.setEnabled(isEnabled);
	}

	/**
	 * Disable buttons in button panel apart from the 1 that isn't by default
	 * i.e. add new order
	 */
	private void setUpNewOrder() {
		disableAll();
		addOrderBtn.setEnabled(true);
	}

	private void setUpAmendOrder() {
		disableAll();
		amendOrderBtn.setEnabled(true);
		deleteOrderBtn.setEnabled(true);
		printOrderBtn.setEnabled(true);
		revokeOrderBtn.setEnabled(true);
		if (lbv!=null) {
			printAmendmentBtn.setEnabled(true);
		} 
	}

	private void disableAll() {
		disableEnableRepDates(false);
		disableEnableButtons(false);
		disableEnableSolicitorFields(false);
		disableEnableAmendment(false);
		disableEnableRevocation(false);
		disableEnableMainOrderFields(false);

	}

	private void setUpPrivate() {
		enableOrderButtons();
		if ((model.getDefOnCaseFirm() != null && model.getDefOnCaseFirm().getRefSolicitorFirmId() != null)
				|| (model.getProsFirm() != null && model.getProsFirm().getRefSolicitorFirmId() != null)) {
			solReferenceTf.setEnabled(true);
		}
		orderDate.requestFocus();
		// save current private rep (for deletion later on) and set current
		// model def on case to null
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			privateToPublicRep = model.getDefOnCaseFirm();
		} else {
			privateToPublicRepPros = model.getProsFirm();
		}
	}

	private void setUpRevoke() {
		disableAll();
		printOrderBtn.setEnabled(true);
		addOrderBtn.setEnabled(true);

	}

	/**
	 * Enable the buttons for order panel and disables all others.
	 */
	public void enableOrderButtons() {
		disableAll();
		disableEnableRepDates(true);
		disableEnableMainOrderFields(true);
		addAmendBtn.setEnabled(true);
		saveBtn.setEnabled(true);
		addMandatoryLabel(startDateLabel);
		addMandatoryLabel(orderDateLbl);
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			numOfAdvocatesLbl.setText("<html>Number of<br> Advocates*</html>");
		} else {
			numOfAdvocatesLbl.setText("<html>Number of<br> Counsels*</html>");
		}
		removeMandatoryLabel(revocationDateLbl);
		removeMandatoryLabel(revocationReasonlbl);
	}

	/**
	 * Enable the buttons for revoke panel and disables all others.
	 */
	public void enableRevokeOrderButtons() {
		disableAll();
		disableEnableRevocation(true);
		saveBtn.setEnabled(true);
	}

	/**
	 * Enable the amend buttons when amend order is pressed.
	 */
	public void enableAmendButtons() {
		disableAll();

		amendDate.setEnabled(true);
		orderAmendmentCmbBx.setEnabled(true);
		orderAmendmentCmbBx.setSelectedIndex(0);

	}

	/**
	 * Enabled validation when advocate/counsel is selected during amend
	 */
	private void enableAdvocateCounsel() {
		// disable all fields
		disableAll();

		// update it with the values from the db again so that the fields that
		// were disabled are re-set
		moveModelToScreen();

		// enable those we want to enable
		orderAmendmentCmbBx.setEnabled(true);

		numOfQcsTf.setEnabled(true);
		numOfAdvocatesTf.setEnabled(true);
		amendDate.setEnabled(true);
		amendDate.setDate(Calendar.getInstance());
		endDate.setEnabled(false);

		if ((model.getDefOnCaseFirm() != null && model.getDefOnCaseFirm().getRefSolicitorFirmId() != null)
				|| (model.getProsFirm() != null && model.getProsFirm().getRefSolicitorFirmId() != null)) {
			solReferenceTf.setEnabled(true);
		}
		saveBtn.setEnabled(true);

		numOfAdvocatesTf.requestFocus();

		addMandatoryLabel(amendmentDateLbl);
		removeMandatoryLabel(endDateLabel);
		removeMandatoryLabel(startDateLabel);
		removeMandatoryLabel(orderDateLbl);
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			numOfAdvocatesLbl.setText("<html>Number of<br> Advocates*</html>");
		} else {
			numOfAdvocatesLbl.setText("<html>Number of<br> Counsels*</html>");
		}	}

	/**
	 * Enabled validation when solicitor is selected during amend
	 */
	private void enableSolicitor() {
		// disable all fields
		disableAll();
		// update it with the values from the db again so that the fields that
		// were disabled are re-set
		moveModelToScreen();

		orderAmendmentCmbBx.setEnabled(true);

		endDate.setEnabled(true);
		amendDate.setEnabled(true);
		amendDate.setDate(Calendar.getInstance());
		saveBtn.setEnabled(true);

		if (model.getDefOnCaseFirm() != null && model.getDefOnCaseFirm().getRepEndDate() != null) {
			endDate.setDate(model.getDefOnCaseFirm().getRepEndDate());
		} else if (model.getProsFirm() != null && model.getProsFirm().getRepEndDate() != null) {
			endDate.setDate(model.getProsFirm().getRepEndDate());
		} else {
			endDate.setDate(Calendar.getInstance());
		}

		endDate.requestFocus();

		addMandatoryLabel(amendmentDateLbl);
		addMandatoryLabel(endDateLabel);
		removeMandatoryLabel(startDateLabel);
		removeMandatoryLabel(orderDateLbl);
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			numOfAdvocatesLbl.setText("<html>Number of<br> Advocates</html>");
		} else {
			numOfAdvocatesLbl.setText("<html>Number of<br> Counsels</html>");
		}
	}

	/**
	 * Enabled validation when correction is selected during amend
	 */
	private void enableCorrection() {
		// disable all fields
		disableAll();
		// update it with the values from the db again so that the fields that
		// were disabled are re-set
		moveModelToScreen();
		Calendar c =null;
		amendDate.setDate(c);

		orderDate.setEnabled(true);
		laaRb.setEnabled(true);
		crownCtRb.setEnabled(true);
		if (laaRb.isSelected()) {
			psdRoRefTf.setEnabled(true);
		}
		numOfAdvocatesTf.setEnabled(true);
		numOfQcsTf.setEnabled(true);
		if ((model.getDefOnCaseFirm() != null && model.getDefOnCaseFirm().getRefSolicitorFirmId() != null)
				|| (model.getProsFirm() != null && model.getProsFirm().getRefSolicitorFirmId() != null)) {
			solReferenceTf.setEnabled(true);
		}

		orderAmendmentCmbBx.setEnabled(true);

		startDate.setEnabled(true);
		endDate.setEnabled(true);
		if (model.getDefOnCaseFirm() != null && model.getDefOnCaseFirm().getRepEndDate() != null) {
			endDate.setDate(model.getDefOnCaseFirm().getRepEndDate());
		} else if (model.getProsFirm() != null && model.getProsFirm().getRepEndDate() != null) {
			endDate.setDate(model.getProsFirm().getRepEndDate());
		} else {
			Calendar nullCal = null;
			endDate.setDate(nullCal);
		}

		saveBtn.setEnabled(true);

		orderDate.requestFocus();

		addMandatoryLabel(startDateLabel);
		addMandatoryLabel(orderDateLbl);
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			numOfAdvocatesLbl.setText("<html>Number of<br> Advocates*</html>");
		} else {
			numOfAdvocatesLbl.setText("<html>Number of<br> Counsels*</html>");
		}
		removeMandatoryLabel(amendmentDateLbl);
		removeMandatoryLabel(endDateLabel);
	}

	/**
	 * Action for what happens when add order button is clicked. Currently
	 * enables the buttons, sets the dates to be mandatory and sets the
	 * traversal order.
	 * 
	 * @author waltersn
	 *
	 */
	private class AddOrderButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AddOrderButtonAction(PublicRepresentationPanel parent) {
			populateFromBundle("AddNewOrder");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			model.setLegalAidOrderValue(null);
			model.setDefOnCaseFirm(null);
			model.setProsFirm(null);
			moveModelToScreen();
			actionTaken = ActionType.ADD_NEW_ORDER;
			enableOrderButtons();
			orderDate.requestFocus();
		}
	}

	/**
	 * Action for what happens when revoke order button is clicked. All fields
	 * should be disabled bar date of revocation and revocation reason
	 * 
	 * @author waltersn
	 *
	 */
	private class RevokeOrderButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public RevokeOrderButtonAction(PublicRepresentationPanel parent) {
			populateFromBundle("RevokeOrder");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			actionTaken = ActionType.REVOKE_ORDER;
			enableRevokeOrderButtons();
			revocationDate.requestFocus();

			removeMandatoryLabel(startDateLabel);
			removeMandatoryLabel(orderDateLbl);
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				numOfAdvocatesLbl.setText("<html>Number of<br> Advocates</html>");
			} else {
				numOfAdvocatesLbl.setText("<html>Number of<br> Counsels</html>");
			}
			addMandatoryLabel(revocationDateLbl);
			addMandatoryLabel(revocationReasonlbl);

		}
	}

	/**
	 * Action for what happens when amend order button is clicked. Currently
	 * enables the buttons, sets the dates to be mandatory and sets the
	 * traversal order.
	 *
	 */
	private class AmendOrderButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AmendOrderButtonAction(PublicRepresentationPanel parent) {
			populateFromBundle("AmendOrder");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			actionTaken = ActionType.AMEND_ORDER;
			enableAmendButtons();
			addMandatoryLabel(startDateLabel);
			addMandatoryLabel(orderDateLbl);
			addMandatoryLabel(orderAmendmentlbl);
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				numOfAdvocatesLbl.setText("<html>Number of<br> Advocates*</html>");
			} else {
				numOfAdvocatesLbl.setText("<html>Number of<br> Counsels*</html>");
			}
		}
	}

	/**
	 * Action for what happens when delete order button is clicked.
	 *
	 */
	private class DeleteOrderButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public DeleteOrderButtonAction(PublicRepresentationPanel parent) {
			populateFromBundle("DeleteOrder");
			setCaller(parent);
		}

		@SuppressWarnings("unchecked")
		public void xActionPerformed(ActionEvent ae) throws Exception {

			int result;
			boolean refresh = false;
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				// Dealing with Defendant representation
				ArrayList<DefOnCaseRefSolFirmValue> defOnCaseRefSolFirm = (ArrayList<DefOnCaseRefSolFirmValue>)defOnCaseDelegate.findPublicRepByLegalAidOrderId(
						model.getLegalAidOrderValue().getId());
				if ( defOnCaseRefSolFirm.size() > 1 ) {
					// Multiple DefOnCaseRefSolFirm attached to this Legal Aid Order
					result = JOptionPane.showConfirmDialog((Component) getCaller(), 
							ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.delete.confirm.delete"),
							ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.delete.confirm.title"), 
							JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						// Set current public rep def solicitor to obsolete as well as the latest solicitor amendment on the legal aid order
						defOnCaseDelegate.deletePublicRepAndLatestSolAmendment(model.getDefOnCaseFirm(), userDisplayName);
						refresh = true;
					}

				} else {
					// Only one DefOnCaseRefSolFirm attached to this Legal Aid Order
					result = JOptionPane.showConfirmDialog((Component) getCaller(), 
							ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.delete.confirm.deleteAll"),
							ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.delete.confirm.title"), 
							JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						// Set legal aid order, solicitor & counsel amendments and the public rep def solicitor to obsolete
						legalDelegate.deleteOrder(model.getLegalAidOrderValue(), model.getDefOnCaseFirm(), userDisplayName);
						refresh = true;
					}
				}
			} 
			else {
				// Dealing with Prosecutor representation
				ArrayList<ProsecutorRefSolFirmValue> prosRefSolFirm = (ArrayList<ProsecutorRefSolFirmValue>)prosecutorDelegate.findPublicRepByLegalAidOrderId(
						model.getLegalAidOrderValue().getId());
				if ( prosRefSolFirm.size() > 1 ) {
					// Multiple ProsecutorRefSolFirm attached to this Legal Aid Order
					result = JOptionPane.showConfirmDialog((Component) getCaller(), 
							ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.delete.confirm.delete"),
							ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.delete.confirm.title"), 
							JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						// Set current public rep prosecutor solicitor to obsolete as well as the latest solicitor amendment on the legal aid order
						prosecutorDelegate.deletePublicRepAndLatestSolAmendment(model.getProsFirm(), userDisplayName);
						refresh = true;
					}

				} else {
					// Only one ProsecutorRefSolFirm attached to this Legal Aid Order
					result = JOptionPane.showConfirmDialog((Component) getCaller(), 
							ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.delete.confirm.deleteAll"),
							ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.delete.confirm.title"), 
							JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						// Set legal aid order, solicitor & counsel amendments and the public rep prosecutor solicitor to obsolete
						legalDelegate.deleteOrder(model.getLegalAidOrderValue(), model.getProsFirm(), userDisplayName);
						refresh = true;
					}
				}
			}
			
			if (refresh) {
				stepInitialise();
				moveModelToScreen();
				setUpState();
			}
		}
	}
	
	/**
	 * Action for what happens when you click Print order.
	 *
	 */
	private class PrintOrderButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public PrintOrderButtonAction(PublicRepresentationPanel parent) {
			populateFromBundle("PrintOrder");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {

			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				// Print Defendant/Appellant Public Order
				//if its revoke then print order will show revoke
				if(actionTaken == ActionType.REVOKE_ORDER) {
					DisplayOWRDAAction action = new DisplayOWRDAAction(model);
					action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				} else {
					DisplayOGRDAAction action = new DisplayOGRDAAction(model, parentDialog.getParentFrame());
					action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				}
			}
			else {
				// Print Respondent Public Order
				if(actionTaken == ActionType.REVOKE_ORDER) {
					DisplayOWRRAction action = new DisplayOWRRAction(model);
					action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				} else {
					DisplayOGRRAction action = new DisplayOGRRAction(model, parentDialog.getParentFrame());
					action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				}
			}

		}
	}

	/**
	 * If the model is not null then populate the fields with the values. Also
	 * sets the caret to 0 so that if the field is too long then it'll show the
	 * first half of the string instead of the end of the string.
	 */
	private void moveModelToScreen() {
		// clear all fields first and foremost before entering new data
		clearAllFields();
		saveBtn.setEnabled(false);
		if (model != null) {
			if (model.getDefOnCaseFirm() != null) {
				solReferenceTf.setText(model.getDefOnCaseFirm().getSolicitorRef());
				startDate.setDate(model.getDefOnCaseFirm().getRepStDate());
				endDate.setDate(model.getDefOnCaseFirm().getRepEndDate());
				if (model.getDefOnCaseFirm().getRefSolicitorFirmId() != null) {
					
						RefSolicitorFirmComplexValue refSolValue = refSolDelegate
								.findByPK(model.getDefOnCaseFirm().getRefSolicitorFirmId());

					clearSolFirms();
					solNameTf.setText(refSolValue.getSolicitorFirmName());
					solAddress1Tf.setText(refSolValue.getAddress1());
					solAddress2Tf.setText(refSolValue.getAddress2());
					solAddress3Tf.setText(refSolValue.getAddress3());
					solAddress4Tf.setText(refSolValue.getAddress4());
					solTownTf.setText(refSolValue.getTown());
					solCountyTf.setText(refSolValue.getCounty());
					solPostcodeTf.setText(refSolValue.getPostcode());
					solDocExRefTf.setText(refSolValue.getDxRef());
					if(refSolValue.getTelephoneNumber()!=null) {
						solTelehponeNoTf.setText(refSolValue.getTelephoneNumber());
					}
					if(refSolValue.getFaxNumber()!=null){
						solFaxNoTf.setText(refSolValue.getFaxNumber());
					}
					if(refSolValue.getNonsecureEmailAddress()!=null) {
						solNonSecureEmailTf.setText(refSolValue.getNonsecureEmailAddress());
					}
					if(refSolValue.getSecureEmailAddress()!=null) {
						solSecureEmailTf.setText(refSolValue.getSecureEmailAddress());
					}
					
					solNameTf.setCaretPosition(0);
					solAddress1Tf.setCaretPosition(0);
					solAddress2Tf.setCaretPosition(0);
					solAddress3Tf.setCaretPosition(0);
					solAddress4Tf.setCaretPosition(0);
					solTownTf.setCaretPosition(0);
					solCountyTf.setCaretPosition(0);
					solPostcodeTf.setCaretPosition(0);
					solDocExRefTf.setCaretPosition(0);
					solTelehponeNoTf.setCaretPosition(0);
					solFaxNoTf.setCaretPosition(0);
					solSecureEmailTf.setCaretPosition(0);
					solNonSecureEmailTf.setCaretPosition(0);
				}
			} else if (model.getProsFirm() != null) {
				solReferenceTf.setText(model.getProsFirm().getSolicitorRef());
				startDate.setDate(model.getProsFirm().getRepStDate());
				endDate.setDate(model.getProsFirm().getRepEndDate());
				if (model.getProsFirm().getRefSolicitorFirmId() != null) {

					RefSolicitorFirmComplexValue refSolValue = refSolDelegate
							.findByPK(model.getProsFirm().getRefSolicitorFirmId());

					clearSolFirms();
					solNameTf.setText(refSolValue.getSolicitorFirmName());
					solAddress1Tf.setText(refSolValue.getAddress1());
					solAddress2Tf.setText(refSolValue.getAddress2());
					solAddress3Tf.setText(refSolValue.getAddress3());
					solAddress4Tf.setText(refSolValue.getAddress4());
					solTownTf.setText(refSolValue.getTown());
					solCountyTf.setText(refSolValue.getCounty());
					solPostcodeTf.setText(refSolValue.getPostcode());
					solDocExRefTf.setText(refSolValue.getDxRef());
					if(refSolValue.getTelephoneNumber()!=null) {
						solTelehponeNoTf.setText(refSolValue.getTelephoneNumber());
					}
					if(refSolValue.getFaxNumber()!=null){
						solFaxNoTf.setText(refSolValue.getFaxNumber());
					}
					if(refSolValue.getNonsecureEmailAddress()!=null) {
						solNonSecureEmailTf.setText(refSolValue.getNonsecureEmailAddress());
					}
					if(refSolValue.getSecureEmailAddress()!=null) {
						solSecureEmailTf.setText(refSolValue.getSecureEmailAddress());
					}
					solNameTf.setCaretPosition(0);
					solAddress1Tf.setCaretPosition(0);
					solAddress2Tf.setCaretPosition(0);
					solAddress3Tf.setCaretPosition(0);
					solAddress4Tf.setCaretPosition(0);
					solTownTf.setCaretPosition(0);
					solCountyTf.setCaretPosition(0);
					solPostcodeTf.setCaretPosition(0);
					solDocExRefTf.setCaretPosition(0);
					solTelehponeNoTf.setCaretPosition(0);
					solFaxNoTf.setCaretPosition(0);
					solSecureEmailTf.setCaretPosition(0);
					solNonSecureEmailTf.setCaretPosition(0);
				}
			}

			else {
				clearSolFirms();
				solReferenceTf.setText("");
				startDate.setDate(Calendar.getInstance());
				Date d = null;
				endDate.setDate(d);
			}
			if (model.getLegalAidOrderValue() != null) {
				if (model.getLegalAidOrderValue().getOrderDate() != null) {
					orderDate.setDate(model.getLegalAidOrderValue().getOrderDate());
				}

				psdRoRefTf.setText(model.getLegalAidOrderValue().getPsdRoRef());
				if (model.getLegalAidOrderValue().getNumberOfQcs() != null) {
					numOfQcsTf.setText(Integer.toString(model.getLegalAidOrderValue().getNumberOfQcs()));
				}
				if (model.getLegalAidOrderValue().getNumberOfAdvocates() != null) {
					numOfAdvocatesTf.setText(Integer.toString(model.getLegalAidOrderValue().getNumberOfAdvocates()));
				}
				if (model.getLegalAidOrderValue().getGrantedBy() != null
						&& GrantedBy.valueOf(model.getLegalAidOrderValue().getGrantedBy()) == GrantedBy.LA) {
					laaRb.setSelected(true);
				} else if (model.getLegalAidOrderValue().getGrantedBy() != null
						&& GrantedBy.valueOf(model.getLegalAidOrderValue().getGrantedBy()) == GrantedBy.CC) {
					crownCtRb.setSelected(true);
				}
				if (model.getLegalAidOrderValue().getReasonForRevocationId() != null) {
					reasonForRevocationCmbBx
							.setSelectedItemByCode(model.getLegalAidOrderValue().getReasonForRevocationId());
				}
				if (model.getLegalAidOrderValue().getDateOfRevocation() != null) {
					revocationDate.setDate(model.getLegalAidOrderValue().getDateOfRevocation());
				}
			} else {
				orderDate.setDate(Calendar.getInstance());
				reasonForRevocationCmbBx.setSelectedIndex(0);
				psdRoRefTf.setText("");
				if (laaRb.isEnabled()) {
					psdRoRefTf.setEnabled(true);
				}
				laaRb.setSelected(true);
				numOfQcsTf.setText("");
				numOfAdvocatesTf.setText("");

			}
		}
	}

	/**
	 * Set the data entered on screen into the model.
	 */
	private void moveScreenToModel() {

		// set solicitor details
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			if (model.getDefOnCaseFirm() == null) {
				model.setDefOnCaseFirm(new DefOnCaseRefSolFirmValue());
			} else if (actionTaken == ActionType.PRIVATE_TO_PUBLIC) {
				if (model.getDefOnCaseFirm().getRefSolicitorFirmId() != null) {
					int id = model.getDefOnCaseFirm().getRefSolicitorFirmId();
					model.setDefOnCaseFirm(new DefOnCaseRefSolFirmValue());
					model.getDefOnCaseFirm().setRefSolicitorFirmId(id);
				} else {
					model.setDefOnCaseFirm(new DefOnCaseRefSolFirmValue());
				}

			}
			if (solReferenceTf.getText() != null && !solReferenceTf.getText().equals("")
					&& solReferenceTf.isEnabled()) {
				model.getDefOnCaseFirm().setSolicitorRef(solReferenceTf.getText());
			}
			try {
				if (startDate.getDate() != null && startDate.getDateComponent().isEnabled()) {
					model.getDefOnCaseFirm().setRepStDate(startDate.getDate().getTime());
				}
			} catch (CSValidationException e) {
				log.debug("Unable to set start date");
			}
			try {
				if (endDate.getDateComponent().isEnabled()) {
					if (endDate.getDate() != null) {
						model.getDefOnCaseFirm().setRepEndDate(endDate.getDate().getTime());
					} else {
						model.getDefOnCaseFirm().setRepEndDate(null);
					}
				}
			} catch (CSValidationException e) {
				log.debug("Unable to set end date");
			}
		} else {
			if (model.getProsFirm() == null) {
				model.setProsFirm(new ProsecutorRefSolFirmValue());
			} else if (actionTaken == ActionType.PRIVATE_TO_PUBLIC) {
				if (model.getProsFirm().getRefSolicitorFirmId() != null) {
					int id = model.getProsFirm().getRefSolicitorFirmId();
					model.setProsFirm(new ProsecutorRefSolFirmValue());
					model.getProsFirm().setRefSolicitorFirmId(id);
				} else {
					model.setProsFirm(new ProsecutorRefSolFirmValue());
				}

			}
			if (solReferenceTf.getText() != null && !solReferenceTf.getText().equals("")
					&& solReferenceTf.isEnabled()) {
				model.getProsFirm().setSolicitorRef(solReferenceTf.getText());
			}
			try {
				if (startDate.getDate() != null && startDate.getDateComponent().isEnabled()) {
					model.getProsFirm().setRepStDate(startDate.getDate().getTime());
				}
			} catch (CSValidationException e) {
				log.debug("Unable to set start date");
			}
			try {
				if (endDate.getDateComponent().isEnabled()) {
					if (endDate.getDate() != null) {
						model.getProsFirm().setRepEndDate(endDate.getDate().getTime());
					} else {
						model.getProsFirm().setRepEndDate(null);
					}
				}
			} catch (CSValidationException e) {
				log.debug("Unable to set end date");
			}
		}

		if (model.getLegalAidOrderValue() == null) {
			model.setLegalAidOrderValue(new LegalAidOrderBasicValue());
		}
		// set order details
		try {
			if (orderDate.getDate() != null && orderDate.getDateComponent().isEnabled()) {
				model.getLegalAidOrderValue().setOrderDate(orderDate.getDate().getTime());
			}
		} catch (CSValidationException e) {
			log.debug("Cannot set order date");
		}
		if (psdRoRefTf.isEnabled()) {
			model.getLegalAidOrderValue().setPsdRoRef(psdRoRefTf.getText());
		}
		if (numOfQcsTf.getText() != null && !numOfQcsTf.getText().equals("") && numOfQcsTf.isEnabled()) {
			model.getLegalAidOrderValue().setNumberOfQcs(Integer.parseInt(numOfQcsTf.getText()));
		} else if (numOfQcsTf.isEnabled()) {
			model.getLegalAidOrderValue().setNumberOfQcs(null);
		}
		if (numOfAdvocatesTf.getText() != null && !numOfAdvocatesTf.getText().equals("")
				&& numOfAdvocatesTf.isEnabled()) {
			model.getLegalAidOrderValue().setNumberOfAdvocates(Integer.parseInt(numOfAdvocatesTf.getText()));
		} else if (numOfAdvocatesTf.isEnabled()) {
			model.getLegalAidOrderValue().setNumberOfAdvocates(null);
		}

		// granted by
		// check which is selected
		if (laaRb.isEnabled() && laaRb.isSelected()) {
			model.getLegalAidOrderValue().setGrantedBy(GrantedBy.LA.nameToStore());
		} else if (crownCtRb.isEnabled() && crownCtRb.isSelected()) {
			model.getLegalAidOrderValue().setGrantedBy(GrantedBy.CC.nameToStore());
			model.getLegalAidOrderValue().setPsdRoRef("");
		}
		try {
			if (amendDate.getDate() != null && amendDate.getDateComponent().isEnabled()) {
				model.setDateOfAmendment(amendDate.getDate().getTime());
			}
		} catch (CSValidationException e) {
			log.debug("Cannot set amend date");
		}

		// add revoke info to legal aid
		if (actionTaken == ActionType.REVOKE_ORDER) {
			Integer revocationId = Integer
					.parseInt(((DropdownCodeStringValue) reasonForRevocationCmbBx.getSelectedItem()).getCode());
			model.getLegalAidOrderValue().setReasonForRevocationId(revocationId);
			model.getLegalAidOrderValue().setObsInd("Y");
			try {
				model.getLegalAidOrderValue().setDateOfRevocation(revocationDate.getDate().getTime());
			} catch (CSValidationException e) {
				log.error("Unable to save revocationDate");
			}

		} else {
			model.getLegalAidOrderValue().setReasonForRevocationId(null);
			Date d = null;
			model.getLegalAidOrderValue().setDateOfRevocation(d);
		}

	}

	/**
	 * Clear solicitor firm values
	 */
	public void clearSolFirms() {
		solNameTf.setText(null);
		solAddress1Tf.setText(null);
		solAddress2Tf.setText(null);
		solAddress3Tf.setText(null);
		solAddress4Tf.setText(null);
		solTownTf.setText(null);
		solCountyTf.setText(null);
		solPostcodeTf.setText(null);
		solDocExRefTf.setText(null);
		solTelehponeNoTf.setText(null);
		solFaxNoTf.setText(null);
		solSecureEmailTf.setText(null);
		solNonSecureEmailTf.setText(null);
	}

	/**
	 * Goes to the database to get the private / public rep info if there is
	 * any. At the moment this only does private rep but will be customised to
	 * include public rep in the future. Second database call is done in case
	 * data has changed since we started the process
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void stepInitialise() throws CSRecoverableException{
		this.model.clearmodel();
		fieldsChangedGlobal = false;
		
		
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			ArrayList<DefOnCaseRefSolFirmValue> docRefSolFirm = (ArrayList<DefOnCaseRefSolFirmValue>) defOnCaseDelegate
					.findPrivateRepByDefendantOnCaseId(this.model.getId());
			if (docRefSolFirm.size()>0) {
				originalEndDate = docRefSolFirm.get(0).getRepEndDate();
				// if active private rep
				if (docRefSolFirm.get(0).getRepType().equals("P") && docRefSolFirm.get(0).getRepEndDate()==null) {
					model.setDefOnCaseFirm(docRefSolFirm.get(0));
					actionTaken = ActionType.PRIVATE_TO_PUBLIC;
				//if ended private rep
				} else if(docRefSolFirm.get(0).getRepType().equals("P") && docRefSolFirm.get(0).getRepEndDate()!=null) {
					actionTaken = ActionType.ADD_NEW_ORDER;
					int j=0;
					ArrayList<LegalAidOrderBasicValue> legalAidValue = (ArrayList<LegalAidOrderBasicValue>)legalDelegate
							.findAllByDefendantOnCaseId(docRefSolFirm.get(0).getDefendantOnCaseId());
					for(int i=1;i<docRefSolFirm.size();i++) {
						if(docRefSolFirm.get(i).getRepType().equals("L")) {
							//check if there are any revoked and set the docRefSolfirm to revoked
							if(j<legalAidValue.size() && legalAidValue.get(j).getDateOfRevocation()!=null) {
								actionTaken = ActionType.REVOKE_ORDER;
								model.setLegalAidOrderValue(legalAidValue.get(j));
								model.setDefOnCaseFirm(docRefSolFirm.get(i));
								originalEndDate = docRefSolFirm.get(i).getRepEndDate();
								lbv = legalDelegate.getLatestLegalAidAmendmentByLegalAidOrderId(legalAidValue.get(j).getId());
								break;
							} else if(j<legalAidValue.size() && legalAidValue.get(j).getDateOfRevocation()==null) {
								j++;
							}
						}
					}
				} 
				//else if L and end date isn't null check it's revoked or amended 
				else if (docRefSolFirm.get(0).getRepType().equals("L") && docRefSolFirm.get(0).getRepEndDate()!=null) {
					ArrayList<LegalAidOrderBasicValue> legalAidValue = (ArrayList<LegalAidOrderBasicValue>)legalDelegate
							.findAllByDefendantOnCaseId(docRefSolFirm.get(0).getDefendantOnCaseId());
					//it's passed so in ammend 
					if(legalAidValue.size()>0 && legalAidValue.get(0).getDateOfRevocation()==null) {
						actionTaken = ActionType.AMEND_ORDER;
						model.setLegalAidOrderValue(legalAidValue.get(0));
						model.setDefOnCaseFirm(docRefSolFirm.get(0));
						lbv=legalDelegate.getLatestLegalAidAmendmentByLegalAidOrderId(legalAidValue.get(0).getId());
					} else if (legalAidValue.size()>0 && legalAidValue.get(0).getDateOfRevocation()!=null) {
						actionTaken = ActionType.REVOKE_ORDER;
						model.setLegalAidOrderValue(legalAidValue.get(0));
						model.setDefOnCaseFirm(docRefSolFirm.get(0));
						lbv=legalDelegate.getLatestLegalAidAmendmentByLegalAidOrderId(legalAidValue.get(0).getId());
					}
				}
				
				//else end date must be null so it's current 
				else {
					ArrayList<LegalAidOrderBasicValue> legalAidValue = (ArrayList<LegalAidOrderBasicValue>)legalDelegate
							.findAllByDefendantOnCaseId(docRefSolFirm.get(0).getDefendantOnCaseId());
					model.setDefOnCaseFirm(docRefSolFirm.get(0));
					if (legalAidValue.size()>0) {
						model.setLegalAidOrderValue(legalAidValue.get(0));
						lbv = legalDelegate.getLatestLegalAidAmendmentByLegalAidOrderId(legalAidValue.get(0).getId());
						actionTaken = ActionType.AMEND_ORDER;
					}
				}
			} else {
				actionTaken = ActionType.ADD_NEW_ORDER;
				//need to reset this for it to pick up post deletion
				lbv=null;
			}
		} 
		
		
		
		//Prosecutor
		else {
			ArrayList<ProsecutorRefSolFirmValue> prosRefSolFirm = (ArrayList<ProsecutorRefSolFirmValue>)prosecutorDelegate
					.findPrivateRepByCaseProsAgency(this.model.getId());
			if (prosRefSolFirm.size()>0) {
				originalEndDate = prosRefSolFirm.get(0).getRepEndDate();
				// get private rep info and fill up the private rep
				// fields
				if (prosRefSolFirm.get(0).getRepType().equals("P") && prosRefSolFirm.get(0).getRepEndDate()==null) {
					model.setProsFirm(prosRefSolFirm.get(0));
					actionTaken = ActionType.PRIVATE_TO_PUBLIC;
				//if ended private rep
				} else if(prosRefSolFirm.get(0).getRepType().equals("P") && prosRefSolFirm.get(0).getRepEndDate()!=null) {
					actionTaken = ActionType.ADD_NEW_ORDER;
					int j=0;
					ArrayList<LegalAidOrderBasicValue> legalAidValue = (ArrayList<LegalAidOrderBasicValue>)legalDelegate
							.findAllByCaseProsAgencyId(prosRefSolFirm.get(0).getCaseProsAgencyId());
					for(int i=1;i<prosRefSolFirm.size();i++) {
						if(prosRefSolFirm.get(i).getRepType().equals("L")) {
							//check if there are any revoked and set the docRefSolfirm to revoked
							if(j<legalAidValue.size() && legalAidValue.get(j).getDateOfRevocation()!=null) {
								actionTaken = ActionType.REVOKE_ORDER;
								model.setLegalAidOrderValue(legalAidValue.get(j));
								model.setProsFirm(prosRefSolFirm.get(i));
								originalEndDate = prosRefSolFirm.get(i).getRepEndDate();
								lbv = legalDelegate.getLatestLegalAidAmendmentByLegalAidOrderId(legalAidValue.get(j).getId());
								break;
							} else if(j<legalAidValue.size() && legalAidValue.get(j).getDateOfRevocation()==null) {
								j++;
							}
						}
					}
				} 
				//else if L and end date isn't null check it's revoked or amended 
				else if (prosRefSolFirm.get(0).getRepType().equals("L") && prosRefSolFirm.get(0).getRepEndDate()!=null) {
					ArrayList<LegalAidOrderBasicValue> legalAidValue = (ArrayList<LegalAidOrderBasicValue>)legalDelegate
							.findAllByCaseProsAgencyId(prosRefSolFirm.get(0).getCaseProsAgencyId());
					//it's passed so in ammend 
					if(legalAidValue.size()>0 && legalAidValue.get(0).getDateOfRevocation()==null) {
						actionTaken = ActionType.AMEND_ORDER;
						model.setProsFirm(prosRefSolFirm.get(0));
						model.setLegalAidOrderValue(legalAidValue.get(0));
						lbv = legalDelegate.getLatestLegalAidAmendmentByLegalAidOrderId(legalAidValue.get(0).getId());
					} else if (legalAidValue.size()>0 && legalAidValue.get(0).getDateOfRevocation()!=null) {
						actionTaken = ActionType.REVOKE_ORDER;
						model.setLegalAidOrderValue(legalAidValue.get(0));
						model.setProsFirm(prosRefSolFirm.get(0));
						lbv = legalDelegate.getLatestLegalAidAmendmentByLegalAidOrderId(legalAidValue.get(0).getId());
					}
				}
				
				//else end date must be null so it's current 
				else {
					ArrayList<LegalAidOrderBasicValue> legalAidValue = (ArrayList<LegalAidOrderBasicValue>)legalDelegate
							.findAllByCaseProsAgencyId(prosRefSolFirm.get(0).getCaseProsAgencyId());
					model.setProsFirm(prosRefSolFirm.get(0));

					if (legalAidValue.size()>0) {						
						model.setLegalAidOrderValue(legalAidValue.get(0));
						lbv = legalDelegate.getLatestLegalAidAmendmentByLegalAidOrderId(legalAidValue.get(0).getId());
						actionTaken = ActionType.AMEND_ORDER;
					}
				}
			} else {
				actionTaken = ActionType.ADD_NEW_ORDER;
			}
		} 
	}

	/**
	 * Default gridbag that's used throughout the panels.
	 * 
	 * @return gridbagconstraints
	 */
	private GridBagConstraints getGridBagLayout() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
	}

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
		// if save button has been clicked
		if ((numOfAdvocatesTf.getText() != null && !numOfAdvocatesTf.getText().equals(""))
				&& (numOfQcsTf.getText() != null && !numOfQcsTf.getText().equals(""))) {
			if (Integer.parseInt(numOfQcsTf.getText()) > Integer.parseInt(numOfAdvocatesTf.getText())) {
				throw new CSValidationException("validation.general", "Field validation failed");
			}
		} else if (!ValidationControllerFactory.validateComponents(validationControllers)) {
			throw new CSValidationException("validation.general", "Field validation failed");
		}

	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	/**
	 * If save button clicked then check validation and then save the database
	 * changes
	 */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (saveBtn.equals(getDeinitialiseSource())) {
			if (!ValidationControllerFactory.validateComponents(validationControllers)) {
				throw new CSValidationException("validation.general", "Field validation failed");
			}
			moveScreenToModel();
			// If we are adding a new one.
			if (actionTaken == ActionType.ADD_NEW_ORDER || actionTaken == ActionType.PRIVATE_TO_PUBLIC) {
				if (model.getCallingClass() instanceof DefendantAppellantTab) {
					model.getDefOnCaseFirm().setRepType("L");
					model.getDefOnCaseFirm().setDefendantOnCaseId(model.getId());
				} else {
					model.getProsFirm().setRepType("L");
					model.getProsFirm().setCaseProsAgencyId(model.getId());
				}
				// insert into LegalAidOrderBasicValue
				LegalAidOrderBasicValue legalBV = new LegalAidOrderBasicValue(
						model.getLegalAidOrderValue().getOrderDate(), model.getLegalAidOrderValue().getGrantedBy(),
						model.getLegalAidOrderValue().getPsdRoRef(),
						model.getLegalAidOrderValue().getNumberOfAdvocates(),
						model.getLegalAidOrderValue().getNumberOfQcs());
				if (model.getCallingClass() instanceof DefendantAppellantTab) {
					legalBV.setDefendantOnCaseId(model.getId());
				} else {
					legalBV.setCaseProsAgencyId(model.getId());
				}

				if (model.getId() != null) {
					if (model.getCallingClass() instanceof DefendantAppellantTab) {
						legalDelegate.createLegalAidOrderAndDefOnCaseRefSolFirm(legalBV, model.getDefOnCaseFirm(),
								userDisplayName, privateToPublicRep, courtid, model.getCaseType().getDbValue());
					} else {
						legalDelegate.createLegalAidOrderAndProsRefSolFirm(legalBV, model.getProsFirm(),
								userDisplayName, privateToPublicRepPros, courtid);

					}
				}
				//show success popup
				successMethod();
				// close down panel
				parentDialog.clearStatusBarScreenCode();
				parentDialog.dispose();

			} else if (actionTaken == ActionType.AMEND_ORDER) {
				if (getAmendType().equals(counselAmend)  || getAmendType().equals(correctionAmend)) {
					LegalAidAmendmentBasicValue amendValue = new LegalAidAmendmentBasicValue();
					if (getAmendType().equals(counselAmend)) {
						amendValue.setAmendmentType(getAmendType());
						amendValue.setAmendmentDate(model.getDateOfAmendment());
						if (solReferenceTf.isEnabled()) {
							if (model.getCallingClass() instanceof DefendantAppellantTab) {
								legalDelegate.updateLegalAidOrderAndDefOnCase(model.getLegalAidOrderValue(), amendValue,
										model.getDefOnCaseFirm(), userDisplayName, amendValue.getAmendmentType(), originalEndDate, courtid, model.getCaseType().getDbValue());
							} else {
								legalDelegate.updateLegalAidOrderAndProsRefSolFirm(model.getLegalAidOrderValue(),
										amendValue, model.getProsFirm(), userDisplayName,
										amendValue.getAmendmentType(), originalEndDate, courtid);
							}
						} else {
							legalDelegate.updateLegalAidOrder(model.getLegalAidOrderValue(), amendValue,
									userDisplayName);
						}
					} else {
						//still need this so that in the controller bean we can not create an amendment if its correction
						amendValue.setAmendmentType(getAmendType());
						if (model.getCallingClass() instanceof DefendantAppellantTab) {
							legalDelegate.updateLegalAidOrderAndDefOnCase(model.getLegalAidOrderValue(), amendValue,
									model.getDefOnCaseFirm(), userDisplayName, amendValue.getAmendmentType(), originalEndDate, courtid, model.getCaseType().getDbValue());
						} else {
							legalDelegate.updateLegalAidOrderAndProsRefSolFirm(model.getLegalAidOrderValue(),
									amendValue, model.getProsFirm(), userDisplayName, amendValue.getAmendmentType(), originalEndDate, courtid);

						}
					}
					if(amendValue.getAmendmentType().equals(counselAmend)){
					String[] options = { "Print Amendment Order", "Close" };
					Integer chosenOption = JOptionPane.showOptionDialog(this, repOrderSuccess, "Order amendment", JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
						if(chosenOption == 0){
							if (this.model.getCallingClass() instanceof DefendantAppellantTab) {
								// Print Defendant/Appellant Public Order
								DisplayOARDAAction action = new DisplayOARDAAction(this.model);
								action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
							}
							else {
								// Print Respondent Public Order
								DisplayOARRAction action = new DisplayOARRAction(this.model);
								action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
							}
							
						}
					} else {
						JOptionPane.showOptionDialog(parentDialog.getParentFrame(), "Public representation update successfully", "Success",
								JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, null, null);
					}
					removeMandatoryLabel(orderAmendmentlbl);
					removeMandatoryLabel(amendmentDateLbl);
					stepInitialise();
					moveModelToScreen();
					setUpState();
				} else {
					if (getAmendType().equals(solAmend) && orderAmendmentCmbBx.isEnabled()) {
						LegalAidAmendmentBasicValue amendValue = new LegalAidAmendmentBasicValue();
						amendValue.setAmendmentType(getAmendType());
						amendValue.setAmendmentDate(model.getDateOfAmendment());
						amendValue.setLegalAidOrderId(model.getLegalAidOrderValue().getId());
						if (model.getCallingClass() instanceof DefendantAppellantTab) {
							defOnCaseDelegate.updateDefOnCaseAndLegalAmend(model.getDefOnCaseFirm(), amendValue,
									userDisplayName, courtid, model.getCaseType().getDbValue());
						} else {
							prosecutorDelegate.updateProsRefSolFirmAndLegalAmend(model.getProsFirm(), amendValue,
									userDisplayName, courtid, "R");
						}
						JOptionPane.showMessageDialog(this, "Record saved successfully. Please select a new solicitor",
								"Success", JOptionPane.INFORMATION_MESSAGE);
						model.setDefOnCaseFirm(null);
						model.setProsFirm(null);
						moveModelToScreen();
						enableOrderButtons();
						orderDate.requestFocus();
						removeMandatoryLabel(endDateLabel);

					}
					// else if its second time save
					else if (getAmendType().equals(solAmend) && !orderAmendmentCmbBx.isEnabled()) {
						if (model.getCallingClass() instanceof DefendantAppellantTab) {
							// insert into def on case ref sol firm
							model.getDefOnCaseFirm().setRepType("L");
							model.getDefOnCaseFirm().setDefendantOnCaseId(model.getId());
							model.getDefOnCaseFirm().setLegalAidOrderId(model.getLegalAidOrderValue().getId());

							legalDelegate.createDefOnCaseAndUpdateLegalAidOrder(model.getLegalAidOrderValue(),
									model.getDefOnCaseFirm(), userDisplayName, courtid, model.getCaseType().getDbValue());
						} else {
							model.getProsFirm().setRepType("L");
							model.getProsFirm().setCaseProsAgencyId(model.getId());
							model.getProsFirm().setLegalAidOrderId(model.getLegalAidOrderValue().getId());

							legalDelegate.createProsRefSolFirmAndUpdateLegalAidOrder(model.getLegalAidOrderValue(),
									model.getProsFirm(), userDisplayName, courtid);
						}

						String[] options = { "Print Amendment Order", "Close" };
						Integer chosenOption = JOptionPane.showOptionDialog(this, repOrderSuccess, "Order amendment",
																			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
						
						if(chosenOption == 0){
							if (this.model.getCallingClass() instanceof DefendantAppellantTab) {
								// Print Defendant/Appellant Public Order
								DisplayOARDAAction action = new DisplayOARDAAction(this.model);
								action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));							
							}
							else {
								// Print Respondent Public Order
								DisplayOARRAction action = new DisplayOARRAction(this.model);
								action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));	
							}
						}
						
						stepInitialise();
						moveModelToScreen();
						setUpState();
					}
				}

			} else if (actionTaken == ActionType.REVOKE_ORDER) {
				if (model.getCallingClass() instanceof DefendantAppellantTab) {
					model.getDefOnCaseFirm().setRepEndDate(revocationDate.getDate().getTime());
					legalDelegate.revokeOrder(model.getLegalAidOrderValue(), model.getDefOnCaseFirm(), userDisplayName, courtid, model.getCaseType().getDbValue());
				} else {
					model.getProsFirm().setRepEndDate(revocationDate.getDate().getTime());
					legalDelegate.revokeOrder(model.getLegalAidOrderValue(), model.getProsFirm(), userDisplayName, courtid);
				}
				String[] options = { "Print Revoked Order", "Close" };
				Integer chosenOption = JOptionPane.showOptionDialog(this, repRevokeOrderSuccess, "Order revoked", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if(chosenOption == 0){
					if (this.model.getCallingClass() instanceof DefendantAppellantTab) {
						// Print Defendant/Appellant Public Order
						DisplayOWRDAAction action = new DisplayOWRDAAction(this.model);
						action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
					}
					else {
						// Print Respondent Public Order
						DisplayOWRRAction action = new DisplayOWRRAction(this.model);
						action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
					}
					
				}
				stepInitialise();
				moveModelToScreen();
				setUpState();

			}
		} else {
			if (fieldsChangedGlobal && cancelBtn.equals(getDeinitialiseSource())) {
				int close = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this process?",
						"Are you sure?", JOptionPane.YES_NO_OPTION);
				if (close == 0) {
					// close down panel
					parentDialog.clearStatusBarScreenCode();
					parentDialog.dispose();
				}
			} else if (cancelBtn.equals(getDeinitialiseSource())) {
				parentDialog.clearStatusBarScreenCode();
				parentDialog.dispose();
			}
		}

	}

	/**
	 * Used when adding a solicitor to the case.
	 * 
	 * @param openSearchSolicitorFirmAction
	 */
	@SuppressWarnings("unchecked")
	public void processSearchSolicitorFirm(OpenSearchSolicitorFirmAction openSearchSolicitorFirmAction) throws CSRecoverableException {
		Vector<RefSolicitorFirmComplexValue> solicitorFirms = new Vector<RefSolicitorFirmComplexValue>(
				openSearchSolicitorFirmAction.getResults());
		if (solicitorFirms.size() > 0) {
			// clear fields there at the moment
			clearSolFirms();
			solNameTf.setText(solicitorFirms.get(0).getSolicitorFirmName());
			solAddress1Tf.setText(solicitorFirms.get(0).getAddress1());
			solAddress2Tf.setText(solicitorFirms.get(0).getAddress2());
			solAddress3Tf.setText(solicitorFirms.get(0).getAddress3());
			solAddress4Tf.setText(solicitorFirms.get(0).getAddress4());
			solTownTf.setText(solicitorFirms.get(0).getTown());
			solCountyTf.setText(solicitorFirms.get(0).getCounty());
			solPostcodeTf.setText(solicitorFirms.get(0).getPostcode());
			solDocExRefTf.setText(solicitorFirms.get(0).getDxRef());
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				if (model.getDefOnCaseFirm() == null) {
					model.setDefOnCaseFirm(new DefOnCaseRefSolFirmValue());
				}
				model.getDefOnCaseFirm().setRefSolicitorFirmId(solicitorFirms.get(0).getId());
				solReferenceTf.setEnabled(true);
			} else {
				if (model.getProsFirm() == null) {
					model.setProsFirm(new ProsecutorRefSolFirmValue());
				}
				model.getProsFirm().setRefSolicitorFirmId(solicitorFirms.get(0).getId());
				solReferenceTf.setEnabled(true);
			}

			try {
				ArrayList<XhbContactDetailBasicValue> contactDetails = new ArrayList<XhbContactDetailBasicValue>();
				contactDetails = (ArrayList<XhbContactDetailBasicValue>) bizRefDelegate
						.findContactsByAddressId(solicitorFirms.get(0).getAddressId());
				for (XhbContactDetailBasicValue contactDetail : contactDetails) {
					if (contactDetail.getContactType().equals("Phone")) {
						solTelehponeNoTf.setText(contactDetail.getContactValue());
					}
					if (contactDetail.getContactType().equals("Fax")) {
						solFaxNoTf.setText(contactDetail.getContactValue());
					}
					if (contactDetail.getContactType().equals("Secure Email")) {
						solSecureEmailTf.setText(contactDetail.getContactValue());
					}
					if (contactDetail.getContactType().equals("Non Secure Email")) {
						solNonSecureEmailTf.setText(contactDetail.getContactValue());
					}
				}
			} catch(Exception e) {
				//clear the fields if an error has occurred otherwise left with
				//half solicitor details as EJBException can be thrown fron findContactsByAddressId
				clearSolFirms();
				solReferenceTf.setText("");
				solReferenceTf.setEnabled(false);
				throw new CSRecoverableException(e);
			}
		}

	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
	}

	/**
	 * Method to disable all buttons and only show those that are needed
	 * depending on the action to be performed.
	 */
	private void setUpState() {
		// Set panels to be disabled
		switch (actionTaken) {
		case ADD_NEW_ORDER:
			setUpNewOrder();
			fieldsChangedGlobal = false;
			setUpAmend();
			break;
		case AMEND_ORDER:
			setUpAmendOrder();
			fieldsChangedGlobal = false;
			setUpAmend();
			break;
		case PRIVATE_TO_PUBLIC:
			setUpPrivate();
			fieldsChangedGlobal = true;
			break;
		case REVOKE_ORDER:
			setUpRevoke();
			fieldsChangedGlobal = false;
			setUpAmend();
			break;
		case DEFAULT:
			disableAll();
			fieldsChangedGlobal = false;
			setUpAmend();
		default:
			disableAll();
			fieldsChangedGlobal = false;
			setUpAmend();
			break;
		}
	}

	/**
	 * Method to clear all fields before saving them to the screen.
	 * 
	 */
	private void clearAllFields() {
		clearSolFirms();
		solReferenceTf.setText("");
		Date d = null;

		// clear order details fields
		orderDate.setDate(Calendar.getInstance());
		psdRoRefTf.setText("");
		numOfAdvocatesTf.setText("");
		numOfQcsTf.setText("");

		// clear start/end dates (set start date back to its default
		startDate.setDate(Calendar.getInstance());
		endDate.setDate(d);
		Calendar c = null;
		// clear amend and revoke fields
		amendDate.setDate(c);
		revocationDate.setDate(c);
		reasonForRevocationCmbBx.setSelectedIndex(0);
	}

	public class changeOccurred implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			fieldsChangedGlobal = true;

		}
	}

	/*
	 * Used to dynamically add a asterisk to a label to indicate its mandatory -
	 * C.Kudzin CTX-1810
	 */
	public void addMandatoryLabel(JLabel label) {
		String text = label.getText();
		if (!(text.substring(text.length() - 1).equals("*"))) {
			text = text + "*";
			label.setText(text);
		}
	}

	/*
	 * Used to dynamically remove a asterisk to a label to indicate its no
	 * longer mandatory - C.Kudzin CTX-1810
	 */
	public void removeMandatoryLabel(JLabel label) {
		String text = label.getText();
		if (text.substring(text.length() - 1).equals("*")) {
			text = text.substring(0, text.length() - 1);
			label.setText(text);
		}
	}

	/**
	 * used to set the reason for revocation dropdown values
	 * 
	 * @param firstVal
	 * @param codes
	 * @return
	 */
	private ArrayList<DropdownCodeStringValue> createDropdownValues(String firstVal, Vector<String> codes) {
		ArrayList<DropdownCodeStringValue> val = new ArrayList<DropdownCodeStringValue>();
		val.add(new DropdownCodeStringValue(firstVal, "", ""));
		for (int i = 0; i < codes.size(); i++) {
			String toAdd[] = codes.get(i).split(":");
			val.add(new DropdownCodeStringValue(toAdd[0], toAdd[1], toAdd[2]));
		}
		return val;
	}
	
	/**
	 * used to set the reason for revocation dropdown values
	 * 
	 * @param firstVal
	 * @param codes
	 * @return
	 */
	private ArrayList<DropdownCodeStringValue> createAmendmentValues(String firstVal, Vector<String> codes) {
		ArrayList<DropdownCodeStringValue> val = new ArrayList<DropdownCodeStringValue>();
		val.add(new DropdownCodeStringValue(firstVal, "", ""));
		for (int i = 0; i < codes.size(); i++) {
			String toAdd[] = codes.get(i).split(":");
			val.add(new DropdownCodeStringValue(toAdd[0], toAdd[1],""));
		}
		return val;
	}
	
	/**
	 * Called when you successfully create a new rep (or convert private to public)
	 */
	public void successMethod() {
		JOptionPane.showOptionDialog(parentDialog.getParentFrame(), "Solicitor Representation saved Successfully", "Success",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, null, null);
	}
	
	public String getAmendType() {
		return (((DropdownCodeStringValue) orderAmendmentCmbBx.getSelectedItem()).getCode());
	}
	
	public void setAmendType(String amendType) {
		orderAmendmentCmbBx.setSelectedItemByCode(amendType);		
	}
	
	private void setUpAmend() {
		if(lbv!=null) {
			amendDate.setDate(lbv.getAmendmentDate());
			setAmendType(lbv.getAmendmentType());
		} else {
			Calendar c = null;
			amendDate.setDate(c);
			orderAmendmentCmbBx.setSelectedIndex(0);
		}
	}
}
