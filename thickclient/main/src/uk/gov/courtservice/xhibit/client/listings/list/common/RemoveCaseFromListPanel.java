package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class RemoveCaseFromListPanel extends XPanel {

	private static final long serialVersionUID = 1L;
	private static final String YES = "Y";
	private static final String NO = "N";

	private RemoveCaseFromListModel model;
	private XDialog parent;
	private JLabel reasonLabel;
	private JLabel freeTextLabel;
	private XComboBox reason;
	private XTextField freeText;
	private String isPreDefinedReasonMandatory = null;
	

	
	public RemoveCaseFromListPanel(RemoveCaseFromListDialog parent, RemoveCaseFromListModel model)
			throws CSRecoverableException {
		this.model = model;
		this.parent = parent;

		stepInitialise();
		jbInit();
		setOkEnabled();
	}

	/**
	 * Initialise screen
	 */
	private void jbInit() {

		// Add overall panel layout
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		// Setup a main parent panel with vertical and horizontal scroll bars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(375, 175));
		this.add(scrollPane, gbc);

		// Content Panel Element
		JPanel dateAndHearingPanel = initReasonPanel();
		mainPanel.add(dateAndHearingPanel, gbc);
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

	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		 moveScreenToModel();
	}

	private OkCancelPanel getButtonPanel() {
		return (OkCancelPanel) parent.getButtonPanel();
	}
	
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (update) {
			// Save is handled as a modal window (otherwise we rely on the parent to save)
			if (model.isModalSave() && getButtonPanel().okButton.equals(getDeinitialiseSource())){
				XhibitDelegateHelper.getListingsDelegate().deleteCaseOnListAndDefOnCaseOnList(model.getCaseOnListComplexValue(), 
						XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));	
			}
		}
	}

	private void setMandatoryLabels() {
		boolean reasonMandatory = false;
		boolean freeTextMandatory = false;
		if ( isPredefinedReasonMandatory() ) {
			reasonMandatory = true;
		} else {
			reasonMandatory = noReasonProvided() || getSelectedReason() != null;
			freeTextMandatory = noReasonProvided() || isFreeTextPopulated();
		}
		setReasonLabel(reasonMandatory);
		setFreeTextLabel(freeTextMandatory);
	}
	
	private JLabel getReasonLabel() {
		if (reasonLabel == null) {
			setReasonLabel(true);
		}
		return reasonLabel; 
	}
	
	private JLabel getFreeTextLabel() {
		if (freeTextLabel == null) {
			setFreeTextLabel(true);
		}
		return freeTextLabel; 
	}
	
	private void setReasonLabel(boolean isMandatory) {
		String text = isMandatory ? getResourceBundle("listingRemoveCaseMandatoryInd") : "";
		if (reasonLabel == null) {
			reasonLabel = new JLabel(text);
		} else {
			reasonLabel.setText(text);
		}
	}

	private void setFreeTextLabel(boolean isMandatory) {
		String text = isMandatory ? getResourceBundle("listingRemoveCaseMandatoryInd") : "";
		if (freeTextLabel == null) {
			freeTextLabel = new JLabel(text);
		} else {
			freeTextLabel.setText(text);
		}
	}
	
	private String getResourceBundle(String resourceKey) {
		return XHIBITConstant.getResource(XhibitBundles.Listings, resourceKey);
	}
	
	/**
	 * Initialise and return the main 'Reason' panel
	 * @return panel
	 */
	private JPanel initReasonPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
    	panel.setBorder(BorderFactory.createTitledBorder( getResourceBundle("listingRemoveCasePanelLabel") ));
    	panel.setLayout(new GridBagLayout());
    	
    	// Label
    	gbc.gridwidth = 2;
    	String reasonLabelKey = isPredefinedReasonMandatory() ? "listingRemoveCaseReasonLabelPredefinedMandatory" : "listingRemoveCaseReasonLabel" ;
		JLabel reasonLabel = new JLabel(getResourceBundle(reasonLabelKey));
		panel.add(reasonLabel, gbc);
		gbc.gridwidth = 1;
		
		// Reason dropdown field
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.01;
		panel.add(getReasonLabel(), gbc);
		gbc.gridx++;
		gbc.weightx = 0.99;
		reason = new XComboBox();
		setDropdownBoxArray(reason, getReasonsArray().toArray());
		reason.addActionListener(new ReasonComboBoxAction());
		panel.add(reason, gbc);
		
		// Reason free text field
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.01;
		panel.add(getFreeTextLabel(), gbc);
		gbc.gridx++;
		gbc.weightx = 0.99;
		freeText = new XTextField();
		freeText.setMaxLength(35);
		freeText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setOkEnabled();
			}
		});
		panel.add(freeText, gbc);

		return panel;
	}
	
	/**
     * Returns a List of reference data items for the Reasons combo box
     * @return	List of reference data items
     */
	private ArrayList<RefSystemCodeBasicValue> getReasonsArray() {
		ArrayList<RefSystemCodeBasicValue> results = ListingDropdownPopulation.getFixtureVacationReasons();
	    return results;
	}

	/**
	 * Update the CaseOnListComplexValue model from the screen values
	 * @throws CSRecoverableException
	 */
	private void moveScreenToModel() throws CSRecoverableException {

		RefSystemCodeBasicValue selectedReason = (RefSystemCodeBasicValue) reason.getSelectedItem();
		model.getCaseOnListComplexValue().setVacationPreDefinedRsonId(selectedReason.getId());
		model.getCaseOnListComplexValue().setReasonForRemoval(freeText.getText() );
		model.getCaseOnListComplexValue().setObsInd("Y");
		model.getCaseOnListComplexValue().setDateOfRemoval(new Date());
		model.getCaseOnListComplexValue().setDirty(true);
	}
	
	/**
	 * Moves the values from the model to the fields on the screen
	 */
	private void moveModelToScreen() {
		// If we are changing the values then prepopulate the screen
		if (model.getCaseOnListComplexValue() != null && YES.equals(model.getCaseOnListComplexValue().getObsInd())) {
			reason.setSelectedItemById(model.getCaseOnListComplexValue().getVacationPreDefinedRsonId());
			freeText.setText(model.getCaseOnListComplexValue().getReasonForRemoval());
		}
		setMandatoryLabels();
	}
	
	/**
	 * Set the array and if the array is empty disable the drop down box
	 */
	private void setDropdownBoxArray(JComboBox comboBox, final Object[] arrayItems) {
		comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
			DropdownBoxCellRender rend = new DropdownBoxCellRender();
			rend.setFormat(true);
			comboBox.setRenderer(rend);
		} else {
			comboBox.setEnabled(false);
		}
	}

	/**
	 * Action class for the dropdown field
	 * @author vincentc
	 *
	 */
	private class ReasonComboBoxAction extends XAction {

		private static final long serialVersionUID = 1L;

		@Override
		/**
		 * Action performed when drop down value changes (re-evaluate Ok button enablement)
		 */
		public void xActionPerformed(ActionEvent e) throws Exception {
			setOkEnabled();
		}
		
	}

	private RefSystemCodeBasicValue getSelectedReason() {
		RefSystemCodeBasicValue result = null;	
		if (reason.getSelectedIndex() > 0) {
			result = (RefSystemCodeBasicValue) reason.getSelectedItem();
		}
		return result;
	}
	
	private boolean noReasonProvided() {
		return getSelectedReason() == null && !isFreeTextPopulated();
	}

	/**
	 * Determines if the Ok button should be enabled or not and sets the enablement accordingly
	 */
	private void setOkEnabled() {
		OkCancelPanel buttonPanel = (OkCancelPanel) parent.getButtonPanel();
		boolean isValid = true;
		// Ok is disabled if both the drop down and the free text fields are empty
		if ( noReasonProvided() ) {
			isValid = false;
		// Ok is disabled if the predefined reason is not set and it is mandatory
		} else if ( getSelectedReason() == null && isPredefinedReasonMandatory()) {
			isValid = false;
		}
		setMandatoryLabels();
		buttonPanel.okButton.setEnabled(isValid);			
	}
	
	/**
	 * Evaluates whether or not the free text field has at least 2 characters (ignoring
	 * leading and trailing whitespace)
	 * @return true if field populated with at least 2 non-whitespace characters else false
	 */
	private boolean isFreeTextPopulated()
	{
		boolean populated = false;
		String text = freeText.getText();
		if ( !text.isEmpty() )
		{
			// Not empty, check that there are 2 or more characters (ignore trailing and leading whitespace)
			if ( text.trim().length() >= 2 ) {
				populated = true;
			}
		}
		return populated;
	}

	private boolean isPredefinedReasonMandatory() {
		if ( isPreDefinedReasonMandatory == null ) {
			isPreDefinedReasonMandatory = NO;
			try {
				List<String> preDefinedHearingTypes = ListingDropdownPopulation.getRefListingDataValues("PREDEFINED_DELETION_REASON_HEARING_TYPES");
				if (preDefinedHearingTypes != null && !preDefinedHearingTypes.isEmpty()) {
					if ( model.getHearingTypeCode() != null) {
						isPreDefinedReasonMandatory = preDefinedHearingTypes.contains(model.getHearingTypeCode()) ? YES : NO;
					}
				}
			}
			catch (BisRefControllerException brce) {
				XHIBITConstant.handleError(brce);
			}
		}
		return YES.equals(isPreDefinedReasonMandatory);
	}
}