package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetailsPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Base class for Reference Data to encourage code reuse between related
 * functionality
 * 
 * @author grewalg
 *
 */
public abstract class RefSearchUpdatePanel extends XHIBITSearchDetailsPanel {
	/**
	 * 
	 */
	protected static final String resources = XhibitBundles.XhibitSearch;

	protected List<Component> componentList;
	protected List<XTextField> textFields;
	protected JButton saveButton, cancelButton;
	protected RefSearch refSearchController;
	/**
	 * If updating an existing Judge (update) or creating a new Judge (new).
	 */
	protected boolean isUpdate;

	protected Vector<JLabel> validationFields = new Vector<JLabel>();
	protected Vector<Object> mandatoryFields = new Vector<Object>();

	/**
	 * Flag to indicate whether has been created. To prevent re-creating every
	 * time.
	 */
	private boolean panelCreated;
	private static final long serialVersionUID = 2471099177203310903L;

	protected RefSearchUpdatePanel(XHIBITSearchDetails xsDetails, RefSearch xsSearch) {
		super(xsDetails, xsSearch);
		this.refSearchController = xsSearch;
	}

	/*
	 * Getters and Setters
	 */

	/**
	 * @return the isUpdate
	 */
	protected boolean isUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate
	 *            the isUpdate to set
	 */
	protected void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * @return the componentList
	 */
	protected List<Component> getComponentList() {
		return componentList;
	}

	/**
	 * @return the validationFields
	 */
	protected Vector<JLabel> getValidationFields() {
		return validationFields;
	}

	/**
	 * @return the mandatoryFields
	 */
	protected Vector<Object> getMandatoryFields() {
		return mandatoryFields;
	}

	/**
	 * @return the parentContainer
	 */
	protected XDialog getParentContainer() {
		return parentContainer;
	}

	/**
	 * @param parentContainer
	 *            the parentContainer to set
	 */
	protected void setParentContainer(XDialog parentContainer) {
		this.parentContainer = parentContainer;
	}

	/**
	 * @return the myParentSearchController
	 */
	protected RefSearch getRefSearchController() {
		return refSearchController;
	}

	/**
	 * @param refSearchController
	 *            the refSearchController to set
	 */
	protected void setRefSearchController(RefSearch refSearchController) {
		this.refSearchController = refSearchController;
	}

	/**
	 * Creates an error label.
	 * 
	 * @return JLabel
	 */
	protected JLabel getErrorLbl(String errorLbl) {
		return getErrorLbl(errorLbl, new Dimension(100, 14));
	}

	/**
	 * Creates an error label.
	 * 
	 * @return JLabel
	 */
	protected JLabel getErrorLbl() {
		return getErrorLbl("Invalid Entry", new Dimension(100, 14));
	}

	protected JLabel getErrorLbl(String errorLbl, Dimension dim) {
		JLabel lbl = new JLabel(errorLbl);
		lbl.setVisible(false);
		lbl.setForeground(Color.RED);
		lbl.setPreferredSize(dim);
		getValidationFields().add(lbl);
		return lbl;
	}

	/**
	 * Creates a dummy label to be added to the placeholder panel.
	 * 
	 * @return JLabel
	 */
	protected JLabel getDummyLabel() {
		JLabel dummyLabel = new JLabel("   ");
		dummyLabel.setPreferredSize(new Dimension(10, 10));

		return dummyLabel;
	}

	/**
	 * Creates a placeholder panel for spacing purposes.
	 * 
	 * @return
	 */
	protected JPanel getPlaceholderPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(15, 12));

		panel.setLayout(new GridBagLayout());

		JLabel dummyLabel = getDummyLabel();
		panel.add(dummyLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));

		return panel;
	}

	/**
	 * Creates a errorholder panel for spacing purposes.
	 * 
	 * @return
	 */
	protected JPanel getErrorHolderPanel(JLabel errorLabel) {

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(195, 12));
		panel.setLayout(new GridBagLayout());
		panel.add(errorLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));

		return panel;
	}

	/**
	 * Creates the screen controls.
	 */
	protected abstract void createPanelControls(XDialog parent);

	/**
	 * Creates the Update Panel - called when the Update button is pressed in
	 * the Results panel.
	 * 
	 * @param parent
	 */
	protected void createUpdatePanel(XDialog parent, boolean isUpdate) {
		this.parentContainer = parent;
		this.isUpdate = isUpdate;
		if (!panelCreated) {
			createPanelControls(parent);
			panelCreated = true;
		}
		configureTabOrder(parentContainer);
		saveButton.setEnabled(false);
	}

	/**
	 * Configure the tab order for the fields.
	 * 
	 * @param parent
	 */
	private void configureTabOrder(XDialog parent) {
		parent.setFocusTraversalPolicyProvider(true);

		Component[] componentArr = new Component[componentList.size()];
		componentList.toArray(componentArr);

		parent.setFocusTraversalPolicy(new FocusTraversalOnArray(componentArr));
	}

	/**
	 * Hide any error labels displaying.
	 */
	public void hideErrorLabels() {
		for (JLabel lbl : validationFields) {
			lbl.setVisible(false);
		}
		for (XTextField field : textFields) {
			field.setError(false);
			field.getForeground();
		}
	}

	/**
	 * Only enable Save button if mandatory and invalid checks are met.
	 * 
	 * @return Boolean
	 */
	public Boolean validateFields() {
		Boolean areFieldsValid = false;

		// If mandatory fields are missing or there is invalid data then disable
		// the save button
		areFieldsValid = (hasMandatoryData() & hasValidData());
		return areFieldsValid;
	}

	/**
	 * Check Mandatory Fields.
	 * 
	 * @return Boolean
	 */
	protected Boolean hasMandatoryData() {
		Boolean hasMandatoryData = true;

		for (int i = 0; i < mandatoryFields.size(); i++) {
			if (mandatoryFields.get(i).getClass() == XTextField.class) {
				String s = ((XTextField) mandatoryFields.get(i)).getText();
				if (s == null || s.equals("")) {
					log.debug("Mandatory field label showing");
					hasMandatoryData = false;
					break;
				}
			}
		}
		return hasMandatoryData;
	}

	/**
	 * Check for invalid fields.
	 * 
	 * @return Boolean
	 */
	protected Boolean hasValidData() {
		Boolean hasValidData = true;

		Vector<JLabel> labels = new Vector<JLabel>();
		labels = getValidationFields();

		for (int j = 0; j < labels.size(); j++) {
			if (labels.get(j).isVisible()) {
				log.debug("Invalid entry labels showing");
				hasValidData = false;
				break;
			}
		}
		return hasValidData;
	}

	/**
	 * Actions to perform when Save is pressed.
	 * 
	 * @throws CSRecoverableException
	 */
	protected void performSave() throws CSRecoverableException {
		save();
	}

	/**
	 * Ref data functionality-specific Save. To be implemented in sub classes.
	 * 
	 * @throws CSRecoverableException
	 */
	protected abstract void save() throws CSRecoverableException;
	
	/**
	 * Actions to perform when Delete is pressed.
	 * 
	 * @throws CSRecoverableException
	 */
	protected void performDelete() throws CSRecoverableException {
		delete();
	}

	/**
	 * Ref data functionality-specific Delete. To be implemented in sub classes.
	 * 
	 * @throws CSRecoverableException
	 */
	protected abstract void delete() throws CSRecoverableException;
}
