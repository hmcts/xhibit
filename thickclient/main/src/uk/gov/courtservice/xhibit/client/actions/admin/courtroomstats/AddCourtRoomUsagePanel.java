package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomUsageComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractTextValidator;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class AddCourtRoomUsagePanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	
	private DocumentListener TimeFieldsDocListener;
	
	private TextValidationController amHoursValidation;
	private TextValidationController amMinsValidation;
	private TextValidationController pmHoursValidation;
	private TextValidationController pmMinsValidation;
	
	private ArrayList<CourtRoomBasicValue> courtRoomArray;
	private XComboBox courtRoom = null;
	private XTextField amHours;
	private XTextField amMins;
	private XTextField pmHours;
	private XTextField pmMins;
	private JLabel timesWarningLabel;
	
	private JButton saveButton;

	private AddCourtRoomUsageModel model;
	private XDialog parent;
	
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();

	public AddCourtRoomUsagePanel(AddCourtRoomUsageDialog parent, AddCourtRoomUsageModel model)
			throws CSRecoverableException {
		this.model = model;
		this.parent = parent;

		stepInitialise();
		jbInit();
	}

	/**
	 * Initialise screen
	 */
	private void jbInit() {

		// Add overall panel layout
		this.setLayout(new GridBagLayout());
		
		CustomButtonPanel buttonPanel = (CustomButtonPanel) parent.getButtonPanel();
		buttonPanel.setShowCancelButton(true);
		buttonPanel.setCancelText("Close");
		buttonPanel.setCancelToolTip("Close");
		saveButton = buttonPanel.addButton("RCSAddCourtRoomUsageSave", false, true);
		saveButton.setEnabled(false);
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(375, 135));
		this.add(scrollPane, gbc);

		// Content Panel Element
		JPanel courtRoomPanel = initCourtRoomPanel();
		mainPanel.add(courtRoomPanel, gbc);
		
		gbc.gridy++;
		JPanel hoursMinsPanel = initHoursMinutesPanel();
		mainPanel.add(hoursMinsPanel, gbc);
	}
	
	/**
     * Initialises the Court Room panel
     * @return	Panel to be added to the parent
     */
    private JPanel initCourtRoomPanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	
		gbc.weightx = 0.15;
		JLabel courtRoomLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddCourtRoomUsageDialog.courtroom.label"));
		panel.add(courtRoomLabel, gbc);
		
		// Court Room dropdown component
		gbc.gridx++;
		gbc.gridwidth = 4;
		gbc.weightx = 0.85;
		courtRoom = new XComboBox();
		if (courtRoomArray == null) {
			courtRoomArray = getCourtRoomArray();
		}
		setComboBoxArray(courtRoom, courtRoomArray.toArray());
		panel.add(courtRoom, gbc);
        return panel;
    }
    
    /**
     * Initialises the AM and PM Hours and Minutes panel
     * @return	Panel to be added to the parent
     */
    private JPanel initHoursMinutesPanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	
    	TimeFieldsDocListener = new TimeFieldsDocListener();
		
		// AM Hours and Minutes components
    	gbc.weighty = 0.25;
		gbc.gridx = 0;
		gbc.weightx = 0.35;
		JLabel amLabel1 = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddCourtRoomUsageDialog.am.label"));
		panel.add(amLabel1, gbc);
		
		// AM Hours
		gbc.gridx++;
		gbc.weightx = 0.2;
		amHours = new XTextField();
		amHours.setMaxLength(2);
		amHours.setNumeric(true);
		amHours.getDocument().addDocumentListener(TimeFieldsDocListener);
		amHoursValidation = ValidationControllerFactory.createText(this, amHours,
				getTimesWarningLabel(), new AbstractTextValidator() {
				@Override
				public void validate(JTextComponent target, List<String> errors) {
					if ( amHours.getText() != null && !amHours.getText().equals("") && 
							(Integer.parseInt(amHours.getText()) < 0 || Integer.parseInt(amHours.getText()) > 12) ) {
						errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "CourtroomStatistics.invalidHours")); 
					} else {
						amHoursValidation.clearErrors();
					} 
				}
			});
		validationControllers.add(amHoursValidation);
		panel.add(amHours, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.1;
		JLabel amLabel2 = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddCourtRoomUsageDialog.amhours.label"));
		panel.add(amLabel2, gbc);
		
		// AM Minutes
		gbc.gridx++;
		gbc.weightx = 0.2;
		amMins = new XTextField();
		amMins.setMaxLength(2);
		amMins.setNumeric(true);
		amMins.getDocument().addDocumentListener(TimeFieldsDocListener);
		amMinsValidation = ValidationControllerFactory.createText(this, amMins,
				getTimesWarningLabel(), new AbstractTextValidator() {
				@Override
				public void validate(JTextComponent target, List<String> errors) {
					if ( amMins.getText() != null && !amMins.getText().equals("") && 
							(Integer.parseInt(amMins.getText()) < 0 || Integer.parseInt(amMins.getText()) > 59) ) {
						errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "CourtroomStatistics.invalidMinutes")); 
					} else {
						amMinsValidation.clearErrors();
					} 
				}
			});
		validationControllers.add(amMinsValidation);
		panel.add(amMins, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.15;
		JLabel amLabel3 = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddCourtRoomUsageDialog.ammins.label"));
		panel.add(amLabel3, gbc);
		
		// PM Hours and Minutes components
		gbc.gridy++;
		gbc.weighty = 0.25;
		gbc.gridx = 0;
		gbc.weightx = 0.35;
		JLabel pmLabel1 = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddCourtRoomUsageDialog.pm.label"));
		panel.add(pmLabel1, gbc);
		
		// PM Hours
		gbc.gridx++;
		gbc.weightx = 0.2;
		pmHours = new XTextField();
		pmHours.setMaxLength(2);
		pmHours.setNumeric(true);
		pmHours.getDocument().addDocumentListener(TimeFieldsDocListener);
		pmHoursValidation = ValidationControllerFactory.createText(this, pmHours,
				getTimesWarningLabel(), new AbstractTextValidator() {
				@Override
				public void validate(JTextComponent target, List<String> errors) {
					if ( pmHours.getText() != null && !pmHours.getText().equals("") && 
							(Integer.parseInt(pmHours.getText()) < 0 || Integer.parseInt(pmHours.getText()) > 12) ) {
						errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "CourtroomStatistics.invalidHours"));
					} else {
						pmHoursValidation.clearErrors();
					} 
				}
			});
		validationControllers.add(pmHoursValidation);
		panel.add(pmHours, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.1;
		JLabel pmLabel2 = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddCourtRoomUsageDialog.pmhours.label"));
		panel.add(pmLabel2, gbc);
		
		// PM Minutes
		gbc.gridx++;
		gbc.weightx = 0.2;
		pmMins = new XTextField();
		pmMins.setMaxLength(2);
		pmMins.setNumeric(true);
		pmMins.getDocument().addDocumentListener(TimeFieldsDocListener);
		pmMinsValidation = ValidationControllerFactory.createText(this, pmMins,
				getTimesWarningLabel(), new AbstractTextValidator() {
				@Override
				public void validate(JTextComponent target, List<String> errors) {
					if ( pmMins.getText() != null && !pmMins.getText().equals("") && 
							(Integer.parseInt(pmMins.getText()) < 0 || Integer.parseInt(pmMins.getText()) > 59) ) {
						errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "CourtroomStatistics.invalidMinutes")); 
					} else {
						pmMinsValidation.clearErrors();
					} 
				}
			});
		validationControllers.add(pmMinsValidation);
		panel.add(pmMins, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.15;
		JLabel pmLabel3 = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddCourtRoomUsageDialog.pmmins.label"));
		panel.add(pmLabel3, gbc);
		
    	// New line
		gbc.gridy++;
		gbc.weighty = 0.5;
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridx = 0;
		gbc.gridwidth = 5;
		panel.add(getTimesWarningLabel(), gbc);
        
        return panel;
    }

	@Override
	public void stepInitialise() throws CSRecoverableException {
		// Note: No required populateModel as the model is passed in
		
	}
	
	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {

	}

	@Override
	public void stepActivate() throws CSRecoverableException {
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
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (!update) {
			// Do nothing, just close
		} else {
			// User clicked Save
			if ( allFieldsPopulated() && !entryExists() ) {
				String username = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
				CourtRoomUsageComplexValue newEntry = new CourtRoomUsageComplexValue();
				newEntry.setAmTimeHours(Integer.parseInt(amHours.getText()));
				newEntry.setAmTimeMins(Integer.parseInt(amMins.getText()));
				newEntry.setPmTimeHours(Integer.parseInt(pmHours.getText()));
				newEntry.setPmTimeMins(Integer.parseInt(pmMins.getText()));
				newEntry.setCourtRoomId(getSelectedCourtRoomId());
				newEntry.setSittingDate(model.getSittingDate());
				XhibitDelegateHelper.getBizRefDelegate().addCourtRoomUsageTime(newEntry, username);
				resetTimeFields();
			}
		}
	}
	
	/**
	 * Checks to see if the new record to be created aleady exists in the database
	 * @return true if it exists, else false
	 */
	private boolean entryExists() {
		boolean entryExists = false;
		String exists = XhibitDelegateHelper.getBizRefDelegate().getCourtRoomUsageExists(getSelectedCourtRoomId(), model.getSittingDate());
		if ( exists.equals("Y") ) {
			// An entry for the court room on this sitting date already exists
			entryExists = true;
			String errorMsg = XHIBITConstant.getResource(XhibitBundles.ErrorText,
					"CourtroomStatistics.alert.courtRoomUsageEntryExists");
			XMessageBox.alert(parent,
					XHIBITConstant.getResource(XhibitBundles.ErrorText, "CourtroomStatistics.alert.title"),
					true, XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
		return entryExists;
	}
	
	/**
	 * Warning label for error messages
	 * @return Warning label
	 */
	private JLabel getTimesWarningLabel() {
    	if (timesWarningLabel == null) {
    		timesWarningLabel = new JLabel(" ");
    	}
    	return timesWarningLabel;
    }
	
	/**
     * Set the array and if the array is empty disable the combo box
     * @param comboBox	Combo Box object to set up
     * @param arrayItems	Array to populate the combo box with
     */
    private void setComboBoxArray(XComboBox comboBox, final Object[] arrayItems) 
    {
    	comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
	    	comboBox.setRenderer(new DropdownBoxCellRender());
			comboBox.enableAutoSelect();
		} else {
			comboBox.setEnabled(false);
		}
    }
	
	/**
     * Returns a List of reference data items for the Court Room combo box
     * @return	List of reference data items
     */
	private ArrayList<CourtRoomBasicValue> getCourtRoomArray() {
		ArrayList<CourtRoomBasicValue> results = ListingDropdownPopulation.getCourtRooms(model.getCourtSiteId());
	    return results;
	}
	
	/**
	 * Returns the court room id for the currently selected court room
	 * @return court room id
	 */
	private Integer getSelectedCourtRoomId() {
		Integer selectedCourtRoomId = 0;
		if (courtRoom != null) {
			selectedCourtRoomId = ((CourtRoomBasicValue) courtRoom.getSelectedItem()).getId();
		}
		return selectedCourtRoomId;
	}
	
	/**
	 * Resets the AM and PM time fields following save
	 */
	private void resetTimeFields() {
		amHours.setText("");
		amMins.setText("");
		pmHours.setText("");
		pmMins.setText("");
	}
	
	/**
	 * Determines if all fields required to create a new entry have been populated and are valid
	 * @return true if all fields ok, else false
	 */
	private boolean allFieldsPopulated() {
		boolean valid = ValidationControllerFactory.validateComponents(validationControllers);
		if ( courtRoom == null || courtRoom.getItemCount() == 0) {
			valid = false;
		}
		if (amHours.getText() == null || amHours.getText().length() == 0) {
			valid = false;
		}
		if (amMins.getText() == null || amMins.getText().length() == 0) {
			valid = false;
		}
		if (pmHours.getText() == null || pmHours.getText().length() == 0) {
			valid = false;
		}
		if (pmMins.getText() == null || pmMins.getText().length() == 0) {
			valid = false;
		}
		return valid;
	}

	/**
	 * Custom DocumentListener class to only enable the Save button when
	 * either all fields contain data, otherwise disable.
	 */
	private class TimeFieldsDocListener implements DocumentListener {
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			if ( allFieldsPopulated() ) {
				saveButton.setEnabled(true);
			} else {
				saveButton.setEnabled(false);
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			if ( allFieldsPopulated() ) {
				saveButton.setEnabled(true);
			} else {
				saveButton.setEnabled(false);
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			if ( allFieldsPopulated() ) {
				saveButton.setEnabled(true);
			} else {
				saveButton.setEnabled(false);
			}
		}
	}
}