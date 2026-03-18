package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Top level class for Home Court panels to factor out common functionality.
 * 
 * @author grewalg
 *
 */
public abstract class RefDataPanel extends XPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1996671342333179224L;

	protected static final Logger log = CSServices.getLogger(RefDataPanel.class);

	protected static final Integer COURT_ID = XhibitSingleton.getInstance().getCourtId();

	protected static final String DISPLAY_NAME = XhibitSingleton.getInstance().getUserSession()
			.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);

	protected static final BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper
			.getBizRefDelegate();

	protected static final String TRUE = "Y";

	protected static final String FALSE = "N";

	protected String resources = XhibitBundles.HomeCourt;

	protected HomeCourtDialog parentDialog;

	protected Vector<JLabel> validationFields = new Vector<JLabel>();

	protected Vector<Object> mandatoryFields = new Vector<Object>();

	protected List<Component> componentList = new ArrayList<Component>();

	protected List<XTextField> textFields = new ArrayList<XTextField>();;

	protected boolean isInitialised;

	protected JButton saveButton;

	/**
	 * Creates an error label.
	 * 
	 * @return JLabel
	 */
	public JLabel getErrorLbl() {
		return getErrorLbl(" ", new Dimension(100, 14));
	}

	public JLabel getErrorLbl(String errorLbl, Dimension dim) {
		JLabel lbl = new JLabel(errorLbl);
		lbl.setVisible(false);
		lbl.setForeground(Color.RED);
		lbl.setPreferredSize(dim);
		lbl.setMinimumSize(dim);
		lbl.setMaximumSize(dim);
		getValidationFields().add(lbl);
		return lbl;
	}

	/**
	 * @return the parentDialog
	 */
	public HomeCourtDialog getParentDialog() {
		return parentDialog;
	}

	/**
	 * @param parentDialog
	 *            the parentDialog to set
	 */
	public void setParentDialog(HomeCourtDialog parentDialog) {
		this.parentDialog = parentDialog;
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
	 * @return the saveButton
	 */
	public JButton getSaveButton() {
		return saveButton;
	}

	/**
	 * @param saveButton
	 *            the saveButton to set
	 */
	public void setSaveButton(JButton saveButton) {
		this.saveButton = saveButton;
	}

	/**
	 * @return the isInitialised
	 */
	public boolean isInitialised() {
		return isInitialised;
	}

	/**
	 * @param isInitialised
	 *            the isInitialised to set
	 */
	public void setInitialised(boolean isInitialised) {
		this.isInitialised = isInitialised;
	}

	/**
	 * Creates a XTextField and adds it to its parent panel.
	 * 
	 * @param parent
	 * @param gbc
	 * @param size
	 * @param columns
	 * @param regex
	 * @param warningLbl
	 * @param mandatory
	 * @param postcode
	 * @param alphanumeric
	 */
	protected XTextField addTextField(JPanel parent, GridBagConstraints gbc, int size, int columns, String regex,
			JLabel warningLbl, boolean mandatory, boolean postcode, boolean alphanumeric) {
		boolean isNumericDefault = false;
		return addTextField(parent, gbc, size, columns, regex, warningLbl, mandatory, postcode, alphanumeric,
				isNumericDefault);
	}

	/**
	 * Creates a XTextField and adds it to its parent panel.
	 * 
	 * @param parent
	 * @param gbc
	 * @param size
	 * @param columns
	 * @param regex
	 * @param warningLbl
	 * @param mandatory
	 * @param postcode
	 * @param alphanumeric
	 * @param isNumeric
	 * @return
	 */
	protected XTextField addTextField(JPanel parent, GridBagConstraints gbc, int size, int columns, String regex,
			JLabel warningLbl, boolean mandatory, boolean postcode, boolean alphanumeric, boolean isNumeric) {
		XTextField comp = null;
		if (postcode) {
			comp = new XTextField(size, regex, warningLbl, mandatory, postcode);
		} else {
			comp = new XTextField(size, regex, warningLbl, mandatory);
		}
		comp.setNumeric(isNumeric);
		comp.setMaxLength(size);
		comp.setGridBagLayout(true);
		comp.setColumns(columns);
		comp.setUpperCase(true);
		comp.setMinimumSize(comp.getPreferredSize());
		comp.setMaximumSize(comp.getPreferredSize());
		if (mandatory) {
			mandatoryFields.add(comp);
		}
		textFields.add(comp);

		validationFields.add(warningLbl);
		comp.getDocument().addDocumentListener(new DocListener());
		comp.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				enableSaveButton();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		parent.add(comp, gbc);

		return comp;
	}

	protected JButton getNewJButton(String title) {
		JButton jButton = new JButton(title);
		jButton.setMinimumSize(jButton.getPreferredSize());
		
		return jButton;
	}
	
	/**
	 * Configure the tab order for the fields.
	 * 
	 * @param parent
	 */
	protected void configureTabOrder(XDialog parent) {
		parent.setFocusTraversalPolicyProvider(true);

		Component[] componentArr = new Component[componentList.size()];
		componentList.toArray(componentArr);

		parent.setFocusTraversalPolicy(new FocusTraversalOnArray(componentArr));
	}

	/**
	 * Enables the Save button if changes are detected and all validations pass.
	 */
	protected void enableSaveButton() {
		saveButton.setEnabled(isInitialised() && getModified());
	}

	protected boolean isSaveButtonEnabled() {
		return saveButton.isEnabled();
	}

	protected boolean isTableValid() {
		return true;
	}

	/**
	 * Only enable Save button if mandatory and invalid checks are met.
	 * 
	 * @return Boolean
	 */
	protected Boolean validateFields() {
		Boolean areFieldsValid = false;

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
					((XTextField)  mandatoryFields.get(i)).setError(true);
					hasMandatoryData = false;
				} else {
					((XTextField)  mandatoryFields.get(i)).setError(false);
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
			if (labels.get(j).getText().length() > 5) {
				log.debug("Invalid entry labels showing");
				hasValidData = false;
				break;
			}
		}
		return hasValidData;
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
	 * Displays a warning message dialog to the user.
	 * 
	 * @param header
	 * @param message
	 */
	public void displayWarningMessage(String header, String message) {
		JOptionPane.showMessageDialog(null, message, header, JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void setModified(boolean isModified) {
		if (isModified) {
			if (isInitialised()) {
				super.setModified(isModified);
			}
		} else {
			super.setModified(isModified);
		}
	}

	protected abstract void resetFields();

	/**
	 * Moves data from the model to the screen fields following a call to the
	 * database.
	 */
	protected abstract void moveModelToScreen();

	/**
	 * Event handler for Combo boxes.
	 * 
	 * @author grewalg
	 *
	 */
	protected class ComboBoxListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			setModified(true);
			enableSaveButton();
		}
	}

	/**
	 * Event handler for Check boxes.
	 * 
	 * @author grewalg
	 *
	 */
	protected class CheckBoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			setModified(true);
			enableSaveButton();
		}
	}

	/**
	 * Used to detect whether text fields have been entered/changed -
	 * 
	 * @author grewalg
	 *
	 */
	protected class DocListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			setModified(true);
			enableSaveButton();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			setModified(true);
			enableSaveButton();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setModified(true);
			enableSaveButton();
		}
	}
}
