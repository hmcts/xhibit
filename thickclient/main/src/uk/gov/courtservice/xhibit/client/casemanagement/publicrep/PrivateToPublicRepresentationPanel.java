package uk.gov.courtservice.xhibit.client.casemanagement.publicrep;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XTitledBorder;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;

/**
 * Panel used when a user clicks on representation order but the case already has private rep
 * @author waltersn
 *
 */
public class PrivateToPublicRepresentationPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private PrivateToPublicRepresentationModel model;
	private PrivateToPublicRepresentationDialog privateToPubParent;
		
	/**
	 * Fields on solicitor firm details panel.
	 */	
	private XTextField solNameTf = null;
	private XTextField solAddress1Tf = null;
	private XTextField solAddress2Tf = null;
	private XTextField solAddress3Tf = null;
	private XTextField solAddress4Tf = null;
	private XTextField solTownTf = null;
	private XTextField solCountyTf = null;
	private XTextField solPostcodeTf = null;
	private XTextField solDocExRefTf = null;
	private XTextField solTelephoneNoTf = null;
	private XTextField solFaxNoTf = null;
	private XTextField solSecureEmailTf = null;
	private XTextField solNonSecureEmailTf = null;
	private XTextField solReferenceTf = null;
	private JCheckBox hasSol;
	
	/**
	 * Start and end date panel.
	 */
	private XDatePanel startDate = null;
	private XDatePanel endDate = null;
		
	public PrivateToPublicRepresentationPanel(PrivateToPublicRepresentationDialog parent, PrivateToPublicRepresentationModel model) throws CSRecoverableException {
		this.model = model;	 		
		this.privateToPubParent = parent;
        jbInit();
	}
	
	/**
	 * Init method called when creating a public rep, sets up the panel look and feel.
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(800, 450));	
		GridBagConstraints gbc = getGridBagConstraints();
		
		OkCancelPanel buttonPanel = privateToPubParent.getButtonPanel();
		buttonPanel.setCancelText("Cancel");
		buttonPanel.setCancelToolTip("Cancel");
		buttonPanel.okButton.setVisible(false);

				
		this.add(getCurrentRep(), gbc);
		JButton convertBtn = new JButton(new ConvertButtonAction(this));	
		gbc.gridy++;
		gbc.fill= GridBagConstraints.NONE;
		gbc.anchor= GridBagConstraints.EAST;
		this.add(convertBtn, gbc);
		
		//disable the fields
		disableEnableSolicitorFields(false);
		disableEnableRepDates(false);

	}
	
	/**
	 * Returns the outer panel.  The method internally calls the 
	 * getSolicitorPanel() method and then adds the start/end dates.
	 * @return the current rep panel
	 */
	public JPanel getCurrentRep() {
		JPanel currentRep = new JPanel();
		currentRep.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getGridBagConstraints();
		
		currentRep.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.privateToPublicPanelLabel")));
		gbc.gridwidth=4;
		currentRep.add(getSolicitorFirmPanel(), gbc);
		
		gbc.gridwidth=1;
		gbc.gridy++;
		JLabel startDateLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.startDateLabel"));
		currentRep.add(startDateLabel, gbc);
		gbc.gridx++;
		currentRep.add(getStartDate(), gbc);

		gbc.gridx++;
		JLabel endDateLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.endDateLabel"));
		currentRep.add(endDateLabel, gbc);
		gbc.gridx++;
		currentRep.add(getEndDate(), gbc);
		

		return currentRep;
	}
	/**
	 * Sets up the solicitor firm panel with all its fields
	 * and components.
	 * @return the Solicitor Firm Panel
	 */
	public JPanel getSolicitorFirmPanel() {
		
		JPanel solicitorFirmPanel = new JPanel();
		hasSol = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.privateToPublic.solPanelLabel"));
		hasSol.setEnabled(false);
		hasSol.setHorizontalTextPosition(SwingConstants.LEFT);
		solicitorFirmPanel.setBorder(new XTitledBorder(hasSol,hasSol,BorderFactory.createTitledBorder("")));
		solicitorFirmPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getGridBagConstraints();
		gbc.weightx=0.5;
		
		JLabel solNameLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.privateToPublic.solNameLabel"));
		solicitorFirmPanel.add(solNameLbl, gbc);
		
		gbc.gridx++;		
		solicitorFirmPanel.add(getSolName(), gbc);

		//address 1
		gbc.gridx=0;
		gbc.gridy++;
		JLabel solAddressLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.solicitor.address1Label"));		
		solicitorFirmPanel.add(solAddressLbl, gbc);
		
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
		
		gbc.gridy=0;
		gbc.gridx++;
		
		JLabel solDocExRefLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.privateToPublic.docExLabel"));		
		solicitorFirmPanel.add(solDocExRefLbl, gbc);
		
		gbc.gridx++;		
		solicitorFirmPanel.add(getSolDocEx(), gbc);

		//Telephone number
		gbc.gridx=2;
		gbc.gridy++;
		JLabel solTelephoneNoLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.privateToPublic.telNoLabel"));		
		solicitorFirmPanel.add(solTelephoneNoLbl, gbc);
		
		gbc.gridx++;		
		solicitorFirmPanel.add(getSolTelNo(), gbc);
		
		//Fax number
		gbc.gridx=2;
		gbc.gridy++;
		JLabel solFaxNoLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.privateToPublic.faxNoLabel"));		

		solicitorFirmPanel.add(solFaxNoLbl, gbc);

		gbc.gridx++;		
		solicitorFirmPanel.add(getSolFaxNo(), gbc);
			
		//secure email
		gbc.gridx=2;
		gbc.gridy++;
		JLabel solSecureEmailLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.privateToPublic.secureEmailLabel"));		

		solicitorFirmPanel.add(solSecureEmailLbl, gbc);

		gbc.gridx++;		
		solicitorFirmPanel.add(getSolSecureEmail(), gbc);
			
		//non secure email
		gbc.gridx=2;
		gbc.gridy++;
		JLabel solNonSecureEmailLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.privateToPublic.nonSecureLabel"));	
		solicitorFirmPanel.add(solNonSecureEmailLbl, gbc);

		gbc.gridx++;	
		solicitorFirmPanel.add(getSolNonSecureEmail(), gbc);
		
		//sol reference
		gbc.gridy++;
		gbc.gridx=2;
		JLabel solReferenceLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.solicitor.referenceLabel"));		
		solicitorFirmPanel.add(solReferenceLbl, gbc);
		
		gbc.gridx++;	
		solicitorFirmPanel.add(getSolReference(), gbc);
		
		//add a vertical structure between the end of solicitor details and start/end dates
		gbc.gridx=0;
		gbc.gridy=8;
		gbc.gridwidth = 4;
		solicitorFirmPanel.add(Box.createVerticalStrut(50), gbc);	
		
		return solicitorFirmPanel;
	}
	
	
	/**
	 * Returns the start date field. 
	 * Sets if it's null.
	 */
	public XDatePanel getStartDate() {
		if(startDate == null) {
			startDate = new XDatePanel(this, null, false);
		}
		return startDate;
	}
	
	/**
	 * Returns the end date field. 
	 * Sets if it's null.
	 */
	public XDatePanel getEndDate() {
		if(endDate == null) {
			endDate = new XDatePanel(this, null, false);
		}
		return endDate;
	}

	/**
	 * Returns the solicitor firm name.
	 * Sets if it's null.
	 */
	public XTextField getSolName () {
		if(solNameTf == null) {
			solNameTf = new XTextField();
			solNameTf.setColumns(20);
			solNameTf.setMinimumSize(solNameTf.getPreferredSize());
		}
		return solNameTf;	
	}
	
	/**
	 * Returns the solicitor address.
	 * Sets if it's null.
	 */
	public XTextField getSolAddress1 () {
		if(solAddress1Tf == null) {
			solAddress1Tf = new XTextField();
		}
		return solAddress1Tf;	
	}
	
	/**
	 * Returns the solicitor address.
	 * Sets if it's null.
	 */
	public XTextField getSolAddress2 () {
		if(solAddress2Tf == null) {
			solAddress2Tf = new XTextField();
		}
		return solAddress2Tf;	
	}
	
	/**
	 * Returns the solicitor address.
	 * Sets if it's null.
	 */
	public XTextField getSolAddress3 () {
		if(solAddress3Tf == null) {
			solAddress3Tf = new XTextField();
		}
		return solAddress3Tf;	
	}
	
	/**
	 * Returns the solicitor address.
	 * Sets if it's null.
	 */
	public XTextField getSolAddress4 () {
		if(solAddress4Tf == null) {
			solAddress4Tf = new XTextField();
		}
		return solAddress4Tf;	
	}
	
	/**
	 * Returns the solicitor town.
	 * Sets if it's null.
	 */
	public XTextField getSolTown() {
		if(solTownTf == null) {
			solTownTf = new XTextField();
		}
		return solTownTf;	
	}
	
	/**
	 * Returns the solicitor County.
	 * Sets if it's null.
	 */
	public XTextField getSolCounty() {
		if(solCountyTf == null) {
			solCountyTf = new XTextField();
		}
		return solCountyTf;	
	}
	
	/**
	 * Returns the solicitor Post code.
	 * Sets if it's null.
	 */
	public XTextField getSolPostcode() {
		if(solPostcodeTf == null) {
			solPostcodeTf = new XTextField();
		}
		return solPostcodeTf;	
	}
	
	/**
	 * Returns the solicitor reference.
	 * Sets if it's null.
	 */
	public XTextField getSolReference() {
		if(solReferenceTf == null) {
			solReferenceTf = new XTextField();
			solReferenceTf.setMaxLength(255);

		}
		return solReferenceTf;	
	}
	
	/**
	 * Returns the solicitor doc ex ref.
	 * Sets if it's null.
	 */
	public XTextField getSolDocEx() {
		if(solDocExRefTf == null) {
			solDocExRefTf = new XTextField();
			solDocExRefTf.setColumns(20);
			solDocExRefTf.setMinimumSize(solNameTf.getPreferredSize());

		}
		return solDocExRefTf;	
	}
	
	/**
	 * Returns the solicitor telephone number.
	 * Sets if it's null.
	 */
	public XTextField getSolTelNo() {
		if(solTelephoneNoTf == null) {
			solTelephoneNoTf = new XTextField();
		}
		return solTelephoneNoTf;	
	}
	
	/**
	 * Returns the solicitor fax number.
	 * Sets if it's null.
	 */
	public XTextField getSolFaxNo() {
		if(solFaxNoTf == null) {
			solFaxNoTf = new XTextField();
		}
		return solFaxNoTf;	
	}
	
	/**
	 * Returns the secure email.
	 * Sets if it's null.
	 */
	public XTextField getSolSecureEmail() {
		if(solSecureEmailTf == null) {
			solSecureEmailTf = new XTextField();
		}
		return solSecureEmailTf;	
	}
	
	/**
	 * Returns the non secure email.
	 * Sets if it's null.
	 */
	public XTextField getSolNonSecureEmail() {
		if(solNonSecureEmailTf == null) {
			solNonSecureEmailTf = new XTextField();
		}
		return solNonSecureEmailTf;	
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
		solTelephoneNoTf.setEnabled(isEnabled);
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
	 * If the model is not null then populate the fields with the values.
	 * Setting the caret to 0 as well because if it's over 20columns it'll show
	 * the first portion of the field not the end.
	 */
	private void moveModelToScreen() {
		if(model!=null) {
			solNameTf.setText(model.getSolicitorName());
			hasSol.setSelected(model.getSolicitorName()!=null && !model.getSolicitorName().equals(""));


			solAddress1Tf.setText(model.getSolicitorAddress1());
			solAddress2Tf.setText(model.getSolicitorAddress2());
			solAddress3Tf.setText(model.getSolicitorAddress3());
			solAddress4Tf.setText(model.getSolicitorAddress4());
			solTownTf.setText(model.getSolicitorTown());
			solCountyTf.setText(model.getSolicitorCounty());
			solPostcodeTf.setText(model.getSolicitorPostCode());
			solReferenceTf.setText(model.getSolicitorRef());
			solDocExRefTf.setText(model.getSolicitorDocExRef());
			solTelephoneNoTf.setText(model.getSolicitorTelephoneNo());
			solFaxNoTf.setText(model.getSolicitorFaxNo());
			solSecureEmailTf.setText(model.getSolicitorSecureEmail());
			solNonSecureEmailTf.setText(model.getSolicitorNonSecureEmail());
			startDate.setDate(model.getStartDate());
			endDate.setDate(model.getEndDate());
			
			solNameTf.setCaretPosition(0);
			solAddress1Tf.setCaretPosition(0);
			solAddress2Tf.setCaretPosition(0);
			solAddress3Tf.setCaretPosition(0);
			solAddress4Tf.setCaretPosition(0);
			solTownTf.setCaretPosition(0);
			solCountyTf.setCaretPosition(0);
			solPostcodeTf.setCaretPosition(0);
			solReferenceTf.setCaretPosition(0);
			solDocExRefTf.setCaretPosition(0);
			solTelephoneNoTf.setCaretPosition(0);
		}
	
	}
	
	 /**
     * Action for what happens when the convert button is clicked
     * @author waltersn
     *
     */
    private class ConvertButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public ConvertButtonAction (PrivateToPublicRepresentationPanel parent) {
			populateFromBundle("ConvertPrivateToPublic");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			PublicRepresentationDialog dialog;
			
			//Open up new public rep
			dialog = new PublicRepresentationDialog(privateToPubParent.getParentFrame(), new PublicRepresentationModel(model.getId(), model.getCallingClass(), model.getCaseType()));
					
			//close current dialog
			privateToPubParent.clearStatusBarScreenCode();
			privateToPubParent.dispose();
				
			dialog.setLocationRelativeTo(privateToPubParent.getParentFrame());
			dialog.setVisible(true);
		}
		
	}
    
    /**
     * Default gridbag constraint used by the panels 
     * @return gridbag constraint with the following properties:
     * gridx + gridy = 0
     * gridwidth + gridheight = 1
     * weightx + weighty = 1.0
     * anchor  = north
     * fill = Horizontal
     * insets = nonContainerInsets
     * ipadx and ipady = 0
     */
    public GridBagConstraints getGridBagConstraints() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);

    }

	@Override
	public void stepInitialise() throws CSRecoverableException {
		// Not needed for private to public rep, leaving blank.
		
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();	
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		// Not needed for private to public rep, leaving blank.		
	}

	@Override
	public void stepValidate() throws CSRecoverableException {
		// Not needed for private to public rep, leaving blank.		
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		// Not needed for private to public rep, leaving blank.		
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		// Not needed for private to public rep, leaving blank.		
	}
	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		// Not needed for private to public rep, leaving blank.		
	}
	

}
