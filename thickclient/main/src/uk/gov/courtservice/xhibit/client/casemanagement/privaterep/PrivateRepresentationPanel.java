package uk.gov.courtservice.xhibit.client.casemanagement.privaterep;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.services.defoncaserefsolfirm.DefOnCaseRefSolFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.prosecutorrefsolfirm.ProsecutorRefSolFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.refsolicitorfirm.RefSolicitorFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm.ProsecutorRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchSolicitorFirmAction;
import uk.gov.courtservice.xhibit.client.casemanagement.DefendantAppellantTab;
import uk.gov.courtservice.xhibit.client.casemanagement.ProsecutorRespondentTab;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrBeforeTodayValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Panel used when a user clicks on private representation button in the
 * defendant tab
 * 
 * @author kudzinc
 *
 */
public class PrivateRepresentationPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = CSServices.getLogger(PrivateRepresentationPanel.class);

	// For cross-access from other steps in the work flow
	private PrivateRepresentationModel model;
	private PrivateRepresentationDialog parentDialog;

	private Integer courtid = XhibitSingleton.getInstance().getCourtId();
	/**
	 * Needed for knowing whether to write to the log
	 */
	private Date originalEndDate = null;

	// JPanels on the main dialog panel
	private JPanel solicitorFirmPanel = null;

	// Entry fields
	private JCheckBox solicitorRepresentationCheckBox = null;
	private JTextField txtSolicitorName = null;
	private JTextField txtAddressLine1 = null;
	private JTextField txtAddressLine2 = null;
	private JTextField txtAddressLine3 = null;
	private JTextField txtAddressLine4 = null;
	private JTextField txtAddressTown = null;
	private JTextField txtAddressCounty = null;
	private JTextField txtAddressPostcode = null;
	private JTextField txtDocExRef = null;
	private JTextField txtTelephoneNumber = null;
	private JTextField txtFaxNumber = null;
	private JTextField txtSecureEmailAddress = null;
	private JTextField txtNonsecureEmailAddress = null;
	private XTextField txtSolicitorReference = null;
	private XDatePanel dtStartDate = null;
	private XDatePanel dtEndDate = null;
	private JButton btnAddNewRepresentation = null;
	private JButton btnAmendRepresentation = null;
	private JButton btnDeleteRepresentation = null;
	private JButton btnSave = null;
	private JButton btnCancel = null;

	// Error labels
	private JLabel lblIRepresentationStartDate;
	private JLabel lblIRepresentationEndDate;
	private JLabel lblISolicitorReference;

	// State variables and other objects
	private boolean changesMade = false;
	private boolean amendButtonPressed = false;

	// Validators
	private DateValidationController dtStartDateValidator;
	private DateValidationController dtEndDateValidator;
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	
	/**
	 * DelegateHelpers
	 */
	private DefOnCaseRefSolFirmControllerBeanBusinessDelegate defOnCaseDelegate;
	private ProsecutorRefSolFirmControllerBeanBusinessDelegate prosecutorDelegate;
	
	
	private String userDisplayName = XhibitSingleton.getInstance().getUserSession()
			.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);

	public PrivateRepresentationPanel(PrivateRepresentationDialog parent, PrivateRepresentationModel model)
			throws CSRecoverableException {
		
		defOnCaseDelegate = XhibitDelegateHelper.getDefOnCaseRefSolFirmDelegate();
		prosecutorDelegate = XhibitDelegateHelper.getProsRefSolFirmDelegate();

		
		this.model = model;
		parentDialog = parent;
		jbInit();
		moveModelToScreen();
	}

	/**
	 * Init method called when creating a public rep, sets up the panel look and
	 * feel.
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(810, 550));
		GridBagConstraints gbc = getGridBagConstraints();

		JPanel privateRepMainPanel = new JPanel();
		privateRepMainPanel.setLayout(new GridBagLayout());

		JScrollPane scrollPane = new JScrollPane(privateRepMainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		privateRepMainPanel.setPreferredSize(new Dimension(700, 480));
		gbc.fill = GridBagConstraints.BOTH;
		this.add(scrollPane, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0.25;
		privateRepMainPanel.add(getCurrentRep(), gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridy++;

		gbc.gridwidth = 2;
		privateRepMainPanel.add(getButtonPanel(), gbc);
		// disable the fields
		disableEnableSolicitorFields(false);
		disableEnableRepDates(false);
		// Set the save and close on the parent dialog
		CustomButtonPanel saveCancelPanel = (CustomButtonPanel) parentDialog.getButtonPanel();

		// Change so that it doesn't close the page when you click save
		btnSave = saveCancelPanel.addButton("PrivateRepSave", false, true);
		btnSave.setEnabled(false);

		// had to change to its own button so that it doesn't by default close
		// the popup
		btnCancel = saveCancelPanel.addButton("btnCancel", false, false);
		btnCancel.setEnabled(true);
	}

	/**
	 * @return
	 */
	private JPanel getButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getGridBagConstraints();

		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 2;
		btnDeleteRepresentation = new JButton(new DeleteRepresentationAction(this));
		btnDeleteRepresentation.setMinimumSize(new Dimension(175, 25));
		btnDeleteRepresentation.setPreferredSize(btnDeleteRepresentation.getMinimumSize());
		buttonPanel.add(btnDeleteRepresentation, gbc);
		gbc.gridx++;

		btnAddNewRepresentation = new JButton(new AddNewRepresentationAction(this));
		btnAddNewRepresentation.setMinimumSize(new Dimension(175, 25));
		btnAddNewRepresentation.setPreferredSize(btnAddNewRepresentation.getMinimumSize());
		buttonPanel.add(btnAddNewRepresentation, gbc);
		gbc.gridx++;

		btnAmendRepresentation = new JButton(new AmendRepresentationAction(this));
		btnAmendRepresentation.setMinimumSize(new Dimension(175, 25));
		btnAmendRepresentation.setPreferredSize(btnAmendRepresentation.getMinimumSize());
		buttonPanel.add(btnAmendRepresentation, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 4;

		return buttonPanel;
	}

	/**
	 * Returns the outer panel. The method internally calls the
	 * getSolicitorPanel() method and then adds the start/end dates.
	 * 
	 * @return the current rep panel
	 */
	public JPanel getCurrentRep() {
		JPanel currentRep = new JPanel();
		currentRep.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getGridBagConstraints();

		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.fill = GridBagConstraints.NONE;
		Insets newInsets = new Insets(0, 0, 0, 0); // To get checkboxes to
													// line up in panel
		gbc.insets = newInsets;
		solicitorRepresentationCheckBox = new JCheckBox(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.repBySol"));
		solicitorRepresentationCheckBox.setEnabled(false);
		solicitorRepresentationCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
		solicitorRepresentationCheckBox.addActionListener(new SolCheckBoxActionListener(this));

		currentRep.add(solicitorRepresentationCheckBox, gbc);

		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 2;

		currentRep.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.currentRep")));
		gbc.gridwidth = 4;
		currentRep.add(getSolicitorFirmPanel(), gbc);

		gbc.gridwidth = 1;
		gbc.gridy++;
		gbc.gridx = 1;
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.weighty = 0.1;
		lblIRepresentationStartDate = new JLabel(" ");
		currentRep.add(lblIRepresentationStartDate, gbc);

		gbc.gridx += 2;
		lblIRepresentationEndDate = new JLabel(" ");
		currentRep.add(lblIRepresentationEndDate, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.weighty = 0.2;
		JLabel startDateLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.repStartDate"));
		currentRep.add(startDateLabel, gbc);
		gbc.gridx++;
		currentRep.add(getStartDate(), gbc);

		gbc.gridx++;
		JLabel endDateLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.repEndDate"));
		currentRep.add(endDateLabel, gbc);
		gbc.gridx++;
		currentRep.add(getEndDate(), gbc);

		return currentRep;
	}

	/**
	 * Sets up the solicitor firm panel with all its fields and components.
	 * 
	 * @return the Solicitor Firm Panel
	 */
	public JPanel getSolicitorFirmPanel() {
		if (solicitorFirmPanel == null) {
			solicitorFirmPanel = new JPanel();
			solicitorFirmPanel.setBorder(BorderFactory.createTitledBorder(" "));

			solicitorFirmPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getGridBagConstraints();
			gbc.weightx = 0.5;

			JLabel lblSolicitorName = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.solicitorName"));
			solicitorFirmPanel.add(lblSolicitorName, gbc);

			gbc.gridx++;
			solicitorFirmPanel.add(getSolicitorName(), gbc);

			// address 1
			gbc.gridx = 0;
			gbc.gridy++;
			JLabel lblSolicitorAddress = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.solicitorAddress"));
			solicitorFirmPanel.add(lblSolicitorAddress, gbc);

			gbc.gridx++;
			solicitorFirmPanel.add(getSolicitorAddress1(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolicitorAddress2(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolicitorAddress3(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolicitorAddress4(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolicitorAddressTown(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolicitorAddressCounty(), gbc);

			gbc.gridy++;
			solicitorFirmPanel.add(getSolicitorAddressPostcode(), gbc);

			gbc.gridy = 0;
			gbc.gridx++;

			JLabel lblDocumentExchangeReference = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.docExRef"));
			solicitorFirmPanel.add(lblDocumentExchangeReference, gbc);

			gbc.gridx++;
			solicitorFirmPanel.add(getDocExRef(), gbc);

			// Telephone number
			gbc.gridx = 2;
			gbc.gridy++;
			JLabel lblTelephoneNumber = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.telephoneNumber"));
			solicitorFirmPanel.add(lblTelephoneNumber, gbc);

			gbc.gridx++;
			solicitorFirmPanel.add(getTelephoneNumber(), gbc);

			// Fax number
			gbc.gridx = 2;
			gbc.gridy++;
			JLabel lblFaxNumber = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.faxNumber"));

			solicitorFirmPanel.add(lblFaxNumber, gbc);

			gbc.gridx++;
			solicitorFirmPanel.add(getFaxNumber(), gbc);

			// secure email
			gbc.gridx = 2;
			gbc.gridy++;
			JLabel lblSecureEmailAddress = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.secEmail"));

			solicitorFirmPanel.add(lblSecureEmailAddress, gbc);

			gbc.gridx++;
			solicitorFirmPanel.add(getSecureEmailAddress(), gbc);

			// non secure email
			gbc.gridx = 2;
			gbc.gridy++;
			JLabel lblNonsecureEmailAddress = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.nonSecure"));
			solicitorFirmPanel.add(lblNonsecureEmailAddress, gbc);

			gbc.gridx++;
			solicitorFirmPanel.add(getNonsecureEmailAddress(), gbc);

			// sol reference
			gbc.gridy++;
			gbc.gridx = 2;
			JLabel lblSolicitorReference = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.solRef"));
			solicitorFirmPanel.add(lblSolicitorReference, gbc);

			gbc.gridx++;
			solicitorFirmPanel.add(getSolicitorReference(), gbc);

			// add a vertical structure between the end of solicitor details and
			// start/end dates
			gbc.gridx = 0;
			gbc.gridy = 8;
			gbc.gridwidth = 4;
			solicitorFirmPanel.add(Box.createVerticalStrut(50), gbc);
		}
		return solicitorFirmPanel;
	}

	/**
	 * If the model is not null then populate the fields with the values.
	 */
	private void moveModelToScreen() {
		if (model != null) {
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				if (model.getDocRefSolFirm() != null) {
					populate();
				}
			} else {
				if (model.getProsRefSolFirm() != null) {
					populate();
				}
			}
		}

	}

	/**
	 * Set the data entered on screen into the model.
	 * @throws CSValidationException 
	 */
	private void moveScreenToModel() throws CSValidationException {
		// set solicitor details

		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			setDocRefSolFirm();
			
		} else {
			setProsRefSolFirm();

			
		}
	}

	/**
	 * Set up the docRefSolFirm fields.
	 * @param model
	 * @throws CSValidationException
	 */
	private void setDocRefSolFirm() throws CSValidationException {
		if (model.getDocRefSolFirm() == null) {
			model.setDocRefSolFirm(new DefOnCaseRefSolFirmValue());
		}
		if (txtSolicitorReference.isEnabled()) {
			model.getDocRefSolFirm().setSolicitorRef(txtSolicitorReference.getText());
		}
		if (dtStartDate.getDate() != null && dtStartDate.getDateComponent().isEnabled()) {
				model.getDocRefSolFirm().setRepStDate(dtStartDate.getDate().getTime());
		}
		
		setRepEndDate(dtEndDate, model);
		
	}
	
	/**
	 * Set up the prosrefsolfirm fields.
	 * @throws CSValidationException
	 */
	private void setProsRefSolFirm() throws CSValidationException {
		if (model.getProsRefSolFirm() == null) {
			model.setProsRefSolFirm(new ProsecutorRefSolFirmValue());
		}
		if (txtSolicitorReference.isEnabled()) {
			model.getProsRefSolFirm().setSolicitorRef(txtSolicitorReference.getText());
		}
		if (dtStartDate.getDate() != null && dtStartDate.getDateComponent().isEnabled()) {
			model.getProsRefSolFirm().setRepStDate(dtStartDate.getDate().getTime());
		}
		
		setRepEndDate(dtEndDate, model);
		
	}

	private void setRepEndDate(XDatePanel dtEndDate, PrivateRepresentationModel model) throws CSValidationException {
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			if (dtEndDate.getDate() != null && dtEndDate.getDateComponent().isEnabled()) {
				model.getDocRefSolFirm().setRepEndDate(dtEndDate.getDate().getTime());
			} else if (dtEndDate.getDateComponent().isEnabled()){
				model.getDocRefSolFirm().setRepEndDate(null);
			}
		} else {
			if (dtEndDate.getDate() != null && dtEndDate.getDateComponent().isEnabled()) {
				model.getProsRefSolFirm().setRepEndDate(dtEndDate.getDate().getTime());
			} else if (dtEndDate.getDateComponent().isEnabled()) {
				model.getProsRefSolFirm().setRepEndDate(null);
			}
			
		}
		
	}

	public GridBagConstraints getGridBagConstraints() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				XHIBITConstant.nonContainerInsets, 0, 0);

	}

	/**
	 * Returns the start date field. Sets if it's null.
	 */
	public XDatePanel getStartDate() {
		if (dtStartDate == null) {
			dtStartDate = new XDatePanel(this, Calendar.getInstance());
			dtStartDate.getEntryField().getDisplay().setDisabledTextColor(Color.BLACK);
			dtStartDateValidator = ValidationControllerFactory.createDateRequired(this, dtStartDate,
					lblIRepresentationStartDate, new AbstractDateValidator() {
						@Override
						public void validate(XDatePanel target, List<String> errors) {
							dtStartDateValidator.validate(true);
							
							btnSave.setEnabled(hasDate(target));
							
							dtEndDateValidator.validate(true);

						}
					}, new DateEqualOrBeforeTodayValidator());
			dtStartDate.getDateComponent().getDisplay().getDocument().addDocumentListener(new DocListener());
			validationControllers.add(dtStartDateValidator);
		}
		return dtStartDate;
	}

	/**
	 * Returns the end date field. Sets if it's null.
	 */
	public XDatePanel getEndDate() {
		if (dtEndDate == null) {
			dtEndDate = new XDatePanel(this, Calendar.getInstance(), false);
			dtEndDate.getEntryField().getDisplay().setDisabledTextColor(Color.BLACK);
			dtEndDateValidator = ValidationControllerFactory.createDateValid(this, dtEndDate, lblIRepresentationEndDate,
					new AbstractDateValidator() {
						@Override
						public void validate(XDatePanel target, List<String> errors) {
							if (hasDate(target) && hasDate(dtStartDate)
									&& getDate(target).before(getDate(dtStartDate))) {
								errors.add("End date before Start date");
							}
						}
					},new DateEqualOrBeforeTodayValidator());
			dtEndDate.getDateComponent().getDisplay().getDocument().addDocumentListener(new DocListener());
			validationControllers.add(dtEndDateValidator);
		}
		return dtEndDate;
	}

	public JTextField getSolicitorAddress1() {
		if (txtAddressLine1 == null) {
			txtAddressLine1 = new JTextField();
			txtAddressLine1.setDisabledTextColor(Color.BLACK);
			txtAddressLine1.getDocument().addDocumentListener(new DocListener());
		}
		return txtAddressLine1;
	}

	public JTextField getSolicitorAddress2() {
		if (txtAddressLine2 == null) {
			txtAddressLine2 = new JTextField();
			txtAddressLine2.setDisabledTextColor(Color.BLACK);
			txtAddressLine2.getDocument().addDocumentListener(new DocListener());
		}
		return txtAddressLine2;
	}

	public JTextField getSolicitorAddress3() {
		if (txtAddressLine3 == null) {
			txtAddressLine3 = new JTextField();
			txtAddressLine3.setDisabledTextColor(Color.BLACK);
			txtAddressLine3.getDocument().addDocumentListener(new DocListener());
		}
		return txtAddressLine3;
	}

	public JTextField getSolicitorAddress4() {
		if (txtAddressLine4 == null) {
			txtAddressLine4 = new JTextField();
			txtAddressLine4.setDisabledTextColor(Color.BLACK);
			txtAddressLine4.getDocument().addDocumentListener(new DocListener());
		}
		return txtAddressLine4;
	}

	public JTextField getSolicitorAddressTown() {
		if (txtAddressTown == null) {
			txtAddressTown = new JTextField();
			txtAddressTown.setDisabledTextColor(Color.BLACK);
			txtAddressTown.getDocument().addDocumentListener(new DocListener());
		}
		return txtAddressTown;
	}

	public JTextField getSolicitorAddressCounty() {
		if (txtAddressCounty == null) {
			txtAddressCounty = new JTextField();
			txtAddressCounty.setDisabledTextColor(Color.BLACK);
			txtAddressCounty.getDocument().addDocumentListener(new DocListener());
		}
		return txtAddressCounty;
	}

	public JTextField getSolicitorAddressPostcode() {
		if (txtAddressPostcode == null) {
			txtAddressPostcode = new JTextField();
			txtAddressPostcode.setDisabledTextColor(Color.BLACK);
			txtAddressPostcode.getDocument().addDocumentListener(new DocListener());
		}
		return txtAddressPostcode;
	}

	// Methods for instantiating and returning correctly sized text fields for
	// grid bag layout (2nd column)
	public JTextField getDocExRef() {
		if (txtDocExRef == null) {
			txtDocExRef = new JTextField();
			txtDocExRef.setColumns(20);
			txtDocExRef.setDisabledTextColor(Color.BLACK);
			txtDocExRef.setMinimumSize(txtDocExRef.getPreferredSize());
			txtDocExRef.getDocument().addDocumentListener(new DocListener());
		}
		return txtDocExRef;
	}

	public JTextField getTelephoneNumber() {
		if (txtTelephoneNumber == null) {
			txtTelephoneNumber = new JTextField();
			txtTelephoneNumber.setDisabledTextColor(Color.BLACK);
			txtTelephoneNumber.getDocument().addDocumentListener(new DocListener());
		}
		return txtTelephoneNumber;
	}

	public JTextField getFaxNumber() {
		if (txtFaxNumber == null) {
			txtFaxNumber = new JTextField();
			txtFaxNumber.setDisabledTextColor(Color.BLACK);
			txtFaxNumber.getDocument().addDocumentListener(new DocListener());
		}
		return txtFaxNumber;
	}

	public JTextField getSecureEmailAddress() {
		if (txtSecureEmailAddress == null) {
			txtSecureEmailAddress = new JTextField();
			txtSecureEmailAddress.setDisabledTextColor(Color.BLACK);
			txtSecureEmailAddress.getDocument().addDocumentListener(new DocListener());
		}
		return txtSecureEmailAddress;
	}

	public JTextField getNonsecureEmailAddress() {
		if (txtNonsecureEmailAddress == null) {
			txtNonsecureEmailAddress = new JTextField();
			txtNonsecureEmailAddress.setDisabledTextColor(Color.BLACK);
			txtNonsecureEmailAddress.getDocument().addDocumentListener(new DocListener());
		}
		return txtNonsecureEmailAddress;
	}

	public XTextField getSolicitorReference() {
		if (txtSolicitorReference == null) {
			if (lblISolicitorReference == null)
				lblISolicitorReference = new JLabel(" ");
			txtSolicitorReference = new XTextField(10, "^.{0,10}$", lblISolicitorReference, false);
			txtSolicitorReference.setMaxLength(10);
			txtSolicitorReference.setDisabledTextColor(Color.BLACK);
			txtSolicitorReference.getDocument().addDocumentListener(new DocListener());
		}
		return txtSolicitorReference;
	}

	// Methods to instantiate and return the three buttons (delete, add new,
	// amend representation)
	public JButton getAddNewRepButton() {
		if (btnAddNewRepresentation == null) {
			btnAddNewRepresentation = new JButton();
		}
		return btnAddNewRepresentation;
	}

	public JButton getAmendRepButton() {
		if (btnAmendRepresentation == null) {
			btnAmendRepresentation = new JButton();
		}
		return btnAmendRepresentation;
	}

	public JButton getDeleteRepButton() {
		if (btnDeleteRepresentation == null) {
			btnDeleteRepresentation = new JButton();
		}
		return btnDeleteRepresentation;
	}

	// Used to detect whether text fields have been entered/changed - C.Kudzin
	public class DocListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			changesMade = true;
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			changesMade = true;
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			changesMade = true;
		}
	}

	/**
	 * Disable/Enable all fields on the solicitor panel.
	 */
	public void disableEnableSolicitorFields(boolean isEnabled) {
		txtSolicitorName.setEnabled(isEnabled);
		txtAddressLine1.setEnabled(isEnabled);
		txtAddressLine2.setEnabled(isEnabled);
		txtAddressLine3.setEnabled(isEnabled);
		txtAddressLine4.setEnabled(isEnabled);
		txtAddressTown.setEnabled(isEnabled);
		txtAddressCounty.setEnabled(isEnabled);
		txtAddressPostcode.setEnabled(isEnabled);
		txtSolicitorReference.setEnabled(isEnabled);
		txtDocExRef.setEnabled(isEnabled);
		txtTelephoneNumber.setEnabled(isEnabled);
		txtFaxNumber.setEnabled(isEnabled);
		txtSecureEmailAddress.setEnabled(isEnabled);
		txtNonsecureEmailAddress.setEnabled(isEnabled);
	}

	/**
	 * Disable/Enable start/end date.
	 */
	public void disableEnableRepDates(boolean isEnabled) {
		dtStartDate.setEnabled(isEnabled);
		dtEndDate.setEnabled(isEnabled);
	}

	/**
	 * Returns the solicitor firm name. Sets if it's null.
	 */
	public JTextField getSolicitorName() {
		if (txtSolicitorName == null) {
			txtSolicitorName = new JTextField();
			txtSolicitorName.setColumns(20);
			txtSolicitorName.setMinimumSize(txtSolicitorName.getPreferredSize());
			txtSolicitorName.getDocument().addDocumentListener(new DocListener());
			txtSolicitorName.setDisabledTextColor(Color.BLACK);
		}
		return txtSolicitorName;
	}

	/**
	 * Action for what happens when the Amend button is clicked
	 * 
	 * @author kudzinc
	 *
	 */
	private class AmendRepresentationAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AmendRepresentationAction(PrivateRepresentationPanel parent) {
			populateFromBundle("PrivateAmendButton");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			amendButtonPressed = true;
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				DefOnCaseRefSolFirmValue d = model.getDocRefSolFirm();
				if (d.getRefSolicitorFirmId() != null) {
					solicitorRepresentationCheckBox.setSelected(true);
					txtSolicitorReference.setEnabled(true);
				} else {
					solicitorRepresentationCheckBox.setSelected(false);
					txtSolicitorReference.setEnabled(false);
				}
				btnSave.setEnabled(true);
			} else {
				ProsecutorRefSolFirmValue pValue = model.getProsRefSolFirm();
				if (pValue.getRefSolicitorFirmId() != null) {
					solicitorRepresentationCheckBox.setSelected(true);
					txtSolicitorReference.setEnabled(true);
				} else {
					solicitorRepresentationCheckBox.setSelected(false);
					txtSolicitorReference.setEnabled(false);
				}
				btnSave.setEnabled(true);
			}
			btnAmendRepresentation.setEnabled(false);
			btnAddNewRepresentation.setEnabled(false);
			btnCancel.setEnabled(true);
			dtEndDate.setEnabled(true);
			dtStartDate.setEnabled(true);
			changesMade = false;
		}

	}

	/**
	 * Action for what happens when the Add button is clicked
	 * 
	 * @author kudzinc
	 *
	 */
	private class AddNewRepresentationAction extends XAction {
		PrivateRepresentationPanel parent;
		private static final long serialVersionUID = 1L;

		public AddNewRepresentationAction(PrivateRepresentationPanel parent) {
			populateFromBundle("PrivateAddButton");
			setCaller(parent);
			this.parent = parent;

		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			disableEnableRepDates(false);
			disableEnableSolicitorFields(false);
			reloadPageDetailsAfterTransaction();
			amendButtonPressed=false;
			solicitorRepresentationCheckBox.setEnabled(true);
			addNewRepAction(parent, ae);
		}

	}

	/**
	 * Action for what happens when the Delete button is clicked
	 * 
	 * @author kudzinc
	 *
	 */
	private class DeleteRepresentationAction extends XAction {

		private static final long serialVersionUID = 1L;

		public DeleteRepresentationAction(PrivateRepresentationPanel parent) {
			populateFromBundle("PrivateDeleteButton");
			setCaller(parent);
		}

		/**
		 * If button is clicked set obsolete to Y in the database for the
		 * objects.
		 */
		public void xActionPerformed(ActionEvent ae) throws Exception {
			int result = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirm Choice",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				moveScreenToModel();
				if (model.getCallingClass() instanceof DefendantAppellantTab) {
					defOnCaseDelegate.deletePrivateRep(model.getDocRefSolFirm(), userDisplayName);
					originalEndDate = null;	
				}
				if (model.getCallingClass() instanceof ProsecutorRespondentTab) {
						prosecutorDelegate.deletePrivateRep(model.getProsRefSolFirm(), userDisplayName);
						originalEndDate = null;
				}
				clearPanelComponents();
				disablePanelComponents();
				if (model.getCallingClass() instanceof DefendantAppellantTab) {
					model.clearmodel();
					DefOnCaseRefSolFirmValue val = new DefOnCaseRefSolFirmValue();
					val.setDefendantOnCaseId(model.getDefendantOnCaseId());
					model.setDocRefSolFirm(val);
				} else {
					model.clearmodel();
					ProsecutorRefSolFirmValue val = new ProsecutorRefSolFirmValue();
					val.setCaseProsAgencyId(model.getDefendantOnCaseId());
					model.setProsRefSolFirm(val);
				}
				btnAddNewRepresentation.setEnabled(true);
				amendButtonPressed = false;
				changesMade = false;
				repaint();
			}
		}

	}

	private class SolCheckBoxActionListener implements ActionListener  {
		PrivateRepresentationPanel parent;

		SolCheckBoxActionListener(PrivateRepresentationPanel parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e)  {
			try {
				if (getSolicitorRepresentationCheckBox()) {
					addNewRepAction(parent, e);
					txtSolicitorName.requestFocusInWindow();
					getParent().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { btnAmendRepresentation,
							btnAddNewRepresentation, btnDeleteRepresentation, txtSolicitorReference,
							solicitorRepresentationCheckBox, dtStartDate, dtEndDate, btnSave, btnCancel }));
				} else {
					getParent().setFocusTraversalPolicy(new FocusTraversalOnArray(
							new Component[] { solicitorRepresentationCheckBox, btnSave, btnCancel }));
					// if the user unchecks the checkbox, it'll now refresh the
					// page, clearing the details previously entered. - C.Kudzin
					// - ctx-1483
					solicitorRepresentationCheckBox.setEnabled(false);
					disableEnableRepDates(false);
					disableEnableSolicitorFields(false);
					reloadPageDetailsAfterTransaction();
				}
			}
			catch (CSRecoverableException e1) {
				XHIBITConstant.handleError(e1, this.getClass());
			}
		}
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		//Not needed for Private rep so leaving blank.
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		//Not needed for Private rep so leaving blank.
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		//Not needed for Private rep so leaving blank.
	}

	@Override
	public void stepValidate() throws CSRecoverableException {
		//Not needed for Private rep so leaving blank.
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		//Not needed for Private rep so leaving blank.
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (btnSave.equals(getDeinitialiseSource())) {
			if (!ValidationControllerFactory.validateComponents(validationControllers)) {
				throw new CSValidationException("validation.general", "Field validation failed");
			}
			moveScreenToModel();
			Date endDate = null;
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				endDate = model.getDocRefSolFirm().getRepEndDate();
			} else {
				endDate = model.getProsRefSolFirm().getRepEndDate();
			}

			
			if (amendButtonPressed) {
				updateOldSolicitor(endDate);
				 
			} else {
				saveNewSolicitor();
			}
		} else if (btnCancel.equals(getDeinitialiseSource()) && changesMade) {
			
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int result = JOptionPane.showConfirmDialog(this,
					"All the changes will be lost, are you sure?", "Confirm", dialogButton);
			if (result == JOptionPane.YES_OPTION) {
				parentDialog.clearStatusBarScreenCode();
				parentDialog.dispose();
			}
			
		} else {
			parentDialog.clearStatusBarScreenCode();
			parentDialog.dispose();
		}
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		//Not needed for Private rep so leaving blank.
	}

	public Calendar getRepStartDate() throws CSValidationException {
		try {
			if (dtStartDate.getDate() != null) {
				return dtStartDate.getDate();
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * if date not null then sets the start
	 * date an enable the amend buttons, else add is enabled and 
	 * date is blank
	 * @param date
	 */
	public void setRepStartDate(Date date) {
		if(date!=null) {
			dtStartDate.setDate(date);
			enableAmmendButtons();
		} else {
			dtStartDate.clear();
			btnAddNewRepresentation.setEnabled(true);
		}
	}

	public Calendar getRepEndDate() throws CSValidationException {
		try {
			if (dtEndDate.getDate() != null) {
				return dtEndDate.getDate();
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * If end date is not null then it sets the 
	 * end date box and sets originalEndDate variable
	 * Else clear's the box and sets to null.
	 * @param date
	 */
	public void setRepEndDate(Date date) {
		if (date != null) {
			originalEndDate = date;
			dtEndDate.setDate(date);
		} else {
			dtEndDate.clear();
			originalEndDate = null;
		}
		
		
		
		
	}

	public String getTxtSolicitorReference() {
		if (txtSolicitorReference.getText().equals("")) {
			return null;
		} else {
			return txtSolicitorReference.getText();
		}
	}

	public void setTxtSolicitorReference(String txt) {
		txtSolicitorReference.setText(txt);
	}

	public boolean getSolicitorRepresentationCheckBox() {
		return solicitorRepresentationCheckBox.isSelected();
	}

	/**
	 * @param page
	 * @param openSearchSolicitorFirm
	 * @param e
	 *            This is used, after all the validation/error checking has been
	 *            performed, to open up the relevant screen + next steps
	 */
	public void addNewRepAction(PrivateRepresentationPanel page, ActionEvent e) {
		final XAction openSearchSolicitorFirms = XhibitActions.getAction(
				(XhibitApplicationController) parentDialog.getParentFrame(), XhibitActions.OpenSearchSolicitorFirm);
		if (solicitorRepresentationCheckBox.isSelected()) {
			btnCancel.setEnabled(true);
			txtSolicitorReference.setEnabled(true);
			solicitorRepresentationCheckBox.setEnabled(true);
			dtStartDate.setEnabled(true);
			dtEndDate.setEnabled(true);
			lblIRepresentationEndDate.setText(" ");
			openSearchSolicitorFirms.setCaller(page);
			openSearchSolicitorFirms.actionPerformed(e);
		} else {
			dtStartDate.setEnabled(true);
			dtEndDate.setEnabled(true);
			txtSolicitorReference.setEnabled(false);
			btnCancel.setEnabled(true);
			btnAddNewRepresentation.setEnabled(false);
		}
	}

	/**
	 * Used after a transaction (db call) to populate the page so that we don't
	 * get Optimistic Lock Errors
	 */
	public void reloadPageDetailsAfterTransaction() throws CSRecoverableException  {

		clearPanelComponents();

		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			Integer defOnCaseId = model.getDocRefSolFirm().getDefendantOnCaseId();	
			DefOnCaseRefSolFirmValue d = new DefOnCaseRefSolFirmValue();
			d.setDefendantOnCaseId(defOnCaseId);
			model.setDocRefSolFirm(d);
			populate();
			
		} else {
			Integer caseProsValue = model.getProsRefSolFirm().getCaseProsAgencyId();		
			ProsecutorRefSolFirmValue p = new ProsecutorRefSolFirmValue();
			p.setCaseProsAgencyId(caseProsValue);
			model.setProsRefSolFirm(p);
			populate();
		}
	}

	private void clearPanelComponents() {
		Component [] comp = getSolicitorFirmPanel().getComponents();
		dtEndDate.clear();
		dtStartDate.clear();
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof JTextField) {
				((JTextField) comp[i]).setText(" ");
			}
		}
		txtSolicitorReference.setText("");
		solicitorRepresentationCheckBox.setSelected(false);
		btnSave.setEnabled(false);

	}

	public void populate() {
		disablePanelComponents();
		if (model.getDocRefSolFirm() != null) {
			DefOnCaseRefSolFirmValue docRefSolFirmValue = model.getDocRefSolFirm();

			setRepStartDate(docRefSolFirmValue.getRepStDate());

			setRepEndDate(docRefSolFirmValue.getRepEndDate());
			
			if (docRefSolFirmValue.getSolicitorRef() != null) {
				setTxtSolicitorReference(docRefSolFirmValue.getSolicitorRef());
			}
			if (docRefSolFirmValue.getRefSolicitorFirmId() != null) {
				populateRefSolFirmFields(docRefSolFirmValue.getRefSolicitorFirmId());

			}
			
			if(docRefSolFirmValue.getRepEndDate() != null && docRefSolFirmValue.getRepEndDate().compareTo(Calendar.getInstance().getTime())<0){
				btnAddNewRepresentation.setEnabled(true);
			}			
		} else if (model.getProsRefSolFirm() != null) {
			ProsecutorRefSolFirmValue prosValue = model.getProsRefSolFirm();

			setRepStartDate(prosValue.getRepStDate());
			setRepEndDate(prosValue.getRepEndDate());
			
			if (prosValue.getSolicitorRef() != null) {
				setTxtSolicitorReference(prosValue.getSolicitorRef());
			}
			if (prosValue.getRefSolicitorFirmId() != null) {
				populateRefSolFirmFields(prosValue.getRefSolicitorFirmId());
			}
			if(prosValue.getRepEndDate() != null && prosValue.getRepEndDate().compareTo(Calendar.getInstance().getTime())<0){
				btnAddNewRepresentation.setEnabled(true);
			}
			
		} else {
			disablePanelComponents();
			btnAddNewRepresentation.setEnabled(true);
			dtStartDate.clear();
		}
		changesMade = false;
	}

	private void populateRefSolFirmFields(Integer refSolicitorFirmId) {
		RefSolicitorFirmControllerBeanBusinessDelegate refSolDelegate 
		= XhibitDelegateHelper.getRefSolicitorFirmController();

		RefSolicitorFirmComplexValue rCV = refSolDelegate
				.findByPK(refSolicitorFirmId);
		if (rCV != null) {

			txtSolicitorName.setText(rCV.getSolicitorFirmName());
			
			solicitorRepresentationCheckBox.setSelected(rCV.getSolicitorFirmName() != null && !rCV.getSolicitorFirmName().equals(""));
			
			txtAddressLine1.setText(rCV.getAddress1());
			txtAddressLine2.setText(rCV.getAddress2());
			txtAddressLine3.setText(rCV.getAddress3());
			txtAddressLine4.setText(rCV.getAddress4());
			txtAddressTown.setText(rCV.getTown());
			txtAddressCounty.setText(rCV.getCounty());
			txtAddressPostcode.setText(rCV.getPostcode());
			txtDocExRef.setText(rCV.getDxRef());
			txtTelephoneNumber.setText(rCV.getTelephoneNumber());
			txtFaxNumber.setText(rCV.getFaxNumber());
			txtSecureEmailAddress.setText(rCV.getSecureEmailAddress());
			txtNonsecureEmailAddress.setText(rCV.getNonsecureEmailAddress());
		}
		
	}

	private void disablePanelComponents() {
		
		dtStartDate.setEnabled(false);
		dtEndDate.setEnabled(false);
		btnAddNewRepresentation.setEnabled(false);
		btnAmendRepresentation.setEnabled(false);
		btnDeleteRepresentation.setEnabled(false);
		txtSolicitorReference.setEnabled(false);
		solicitorRepresentationCheckBox.setEnabled(false);

		btnSave.setEnabled(false);
	}

	public void processSearchSolicitorFirm(OpenSearchSolicitorFirmAction openSearchSolicitorFirmAction) throws CSRecoverableException {
		log.debug("ProcessSearchSolicitorFirm");
		List<RefSolicitorFirmComplexValue> solicitorFirms = new ArrayList<RefSolicitorFirmComplexValue>(
				openSearchSolicitorFirmAction.getResults());
		if (!solicitorFirms.isEmpty()) {
			// Clears the page first - C.Kudzin
			clearPanelComponents();
			changesMade = true;

			RefSolicitorFirmComplexValue solicitorFirm = solicitorFirms.get(0);

			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				if (model.getDocRefSolFirm() == null) {
					model.setDocRefSolFirm(new DefOnCaseRefSolFirmValue());
				}
				model.getDocRefSolFirm().setRefSolicitorFirmId(solicitorFirm.getId());
			} else {
				if (model.getProsRefSolFirm() == null) {
					model.setProsRefSolFirm(new ProsecutorRefSolFirmValue());
				}
				model.getProsRefSolFirm().setRefSolicitorFirmId(solicitorFirm.getId());
			}

			txtSolicitorName.setText(solicitorFirm.getSolicitorFirmName());
			txtAddressLine1.setText(solicitorFirm.getAddress1());
			txtAddressLine2.setText(solicitorFirm.getAddress2());
			txtAddressLine3.setText(solicitorFirm.getAddress3());
			txtAddressLine4.setText(solicitorFirm.getAddress4());
			txtAddressTown.setText(solicitorFirm.getTown());
			txtAddressCounty.setText(solicitorFirm.getCounty());
			txtAddressPostcode.setText(solicitorFirm.getPostcode());
			txtDocExRef.setText(solicitorFirm.getDxRef());
			btnAddNewRepresentation.setEnabled(false);
			solicitorRepresentationCheckBox.setSelected(true);
			dtStartDate.setEnabled(true);
			dtEndDate.setEnabled(true);

			setUpContactDetails(solicitorFirm.getAddressId());

		} else {
			/*
			 * Used if the user exits out of the window without selecting a
			 * solicitor reset the fields
			 */
			reloadPageDetailsAfterTransaction();
			solicitorRepresentationCheckBox.setSelected(false);
			dtStartDate.setEnabled(true);
			dtEndDate.setEnabled(true);
			txtSolicitorReference.setEnabled(false);
			btnAddNewRepresentation.setEnabled(false);
			solicitorRepresentationCheckBox.setEnabled(true);
			

		}

	}

	/**
	 * sets up the phone/fax/email fields
	 * @param addressId
	 * @throws CSRecoverableException
	 */
	private void setUpContactDetails(Integer addressId) throws CSRecoverableException {
		try {
			BisRefControllerBeanBusinessDelegate bizRefDelegate  = 
						XhibitDelegateHelper.getBizRefDelegate();
			ArrayList<XhbContactDetailBasicValue> contactDetails = (ArrayList<XhbContactDetailBasicValue>) bizRefDelegate
					.findContactsByAddressId(addressId);

			for (XhbContactDetailBasicValue contactDetail : contactDetails) {
				if (contactDetail.getContactType().equals("Phone")) {
					setTxtTelephoneNumber(contactDetail.getContactValue());
				}
				if (contactDetail.getContactType().equals("Fax")) {
					setTxtFaxNumber(contactDetail.getContactValue());
				}
				if (contactDetail.getContactType().equals("Secure Email")) {
					setTxtSecureEmailAddress(contactDetail.getContactValue());
				}
				if (contactDetail.getContactType().equals("Non Secure Email")) {
						setTxtNonsecureEmailAddress(contactDetail.getContactValue());
				}
			}
		} catch(Exception e) {
			//clear the fields if an error has occurred otherwise left with
			//half solicitor details as EJBException can be thrown from findContactsByAddressId
			clearPanelComponents();
			txtSolicitorReference.setEnabled(false);
			throw new CSRecoverableException(e);
		}
		
	}

	public String getTxtTelephoneNumber() {
		return txtTelephoneNumber.getText();
	}

	public void setTxtTelephoneNumber(String txt) {
		txtTelephoneNumber.setText(txt);
	}

	public String getTxtFaxNumber() {
		return txtFaxNumber.getText();
	}

	public void setTxtFaxNumber(String txt) {
		txtFaxNumber.setText(txt);
	}

	public String getTxtSecureEmailAddress() {
		return txtSecureEmailAddress.getText();
	}

	public void setTxtSecureEmailAddress(String txt) {
		txtSecureEmailAddress.setText(txt);
	}

	public String getTxtNonsecureEmailAddress() {
		return txtNonsecureEmailAddress.getText();
	}

	public void setTxtNonsecureEmailAddress(String txt) {
		txtNonsecureEmailAddress.setText(txt);
	}

	public String getTxtSolicitorName() {
		if (txtSolicitorName.getText().equals("") || txtSolicitorName.getText().equals(" ")) {
			return null;
		} else {
			return txtSolicitorName.getText();
		}
	}

	public void setTxtSolicitorName(String txt) {
		txtSolicitorName.setText(txt);
	}

	public void updateOldSolicitor(Date endDate) throws CSRecoverableException {
		ProsecutorRefSolFirmValue p = model.getProsRefSolFirm();
		DefOnCaseRefSolFirmValue d = model.getDocRefSolFirm();
		if (p != null) {
			prosecutorDelegate.updateProsecutorRefSolFirm(p, courtid, originalEndDate, model.getRepType(), userDisplayName);
			originalEndDate = p.getRepEndDate();
		} else if (d != null) {
			defOnCaseDelegate.updateDefOnCaseRefSolFirm(d, courtid, originalEndDate, userDisplayName, model.getCaseType().getDbValue());
			originalEndDate = d.getRepEndDate();
		}
		Calendar cal = Calendar.getInstance();
		
		if (endDate != null && endDate.compareTo(cal.getTime())<0) {
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int result = JOptionPane.showConfirmDialog(this,
					"Representation ended. Would you like to add a new representation?", "Confirm",
					dialogButton);
			if (result == JOptionPane.YES_OPTION) {
				disableEnableRepDates(false);
				disableEnableSolicitorFields(false);
				reloadPageDetailsAfterTransaction();
				amendButtonPressed=false;
			} else {
				parentDialog.dispose();
			}
		} else {
			successMethod();
		}
	}

	public void saveNewSolicitor() {
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			DefOnCaseRefSolFirmValue d = model.getDocRefSolFirm();
			// Rep Type is P for Private Rep
			d.setRepType("P");
			defOnCaseDelegate.createDefOnCaseRefSolFirm(d, courtid, userDisplayName, model.getCaseType().getDbValue());
			successMethod();
		} else {
			// prosecutor/respondent
			ProsecutorRefSolFirmValue pValue = model.getProsRefSolFirm();
			// Rep Type is P for Private Rep
			pValue.setRepType("P");
			prosecutorDelegate.createProsecutorRefSolFirm(pValue, courtid,
				model.getRepType(), userDisplayName);
			successMethod();
		}
	}
	
	private void enableAmmendButtons(){
		btnAmendRepresentation.setEnabled(true);
		btnDeleteRepresentation.setEnabled(true);
		btnAddNewRepresentation.setEnabled(false);
		solicitorRepresentationCheckBox.setEnabled(false);
		btnSave.setEnabled(false);
	}

	// Implemented success method as it is called various times above - C.Kudzin
	public void successMethod() {
		changesMade = false;
		JOptionPane.showOptionDialog(parentDialog.getParentFrame(), "Solicitor Representation saved Successfully", "Success",
				JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		parentDialog.dispose();

	}
}
