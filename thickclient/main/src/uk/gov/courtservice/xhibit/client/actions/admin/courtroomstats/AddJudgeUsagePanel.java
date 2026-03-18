package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.JudgeUsageComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchListingJudgeAction;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class AddJudgeUsagePanel extends XPanel {

	private static final long serialVersionUID = 1L;
	
	private DocumentListener TimeFieldsDocListener;
	
	private ArrayList<CourtRoomBasicValue> courtRoomArray;
	private XComboBox courtRoom = null;
	private XTextField reqJudgeText;
	private JButton reqJudgeSearchButton;
	private XComboBox satInCombo;
	
	private ArrayList<DropdownCodeStringValue> satInArray;
	
	private JButton saveButton;

	private AddJudgeUsageModel model;
	private XDialog parent;

	public AddJudgeUsagePanel(AddJudgeUsageDialog parent, AddJudgeUsageModel model)
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
		mainPanel.setPreferredSize(new Dimension(500, 125));
		this.add(scrollPane, gbc);

		// Content Panel Element
		JPanel courtRoomPanel = initCourtRoomPanel();
		mainPanel.add(courtRoomPanel, gbc);
		
		gbc.gridy++;
		JPanel judgePanel = initSelectJudgePanel();
		mainPanel.add(judgePanel, gbc);
		
		gbc.gridy++;
		JPanel satInPanel = initSatInPanel();
		mainPanel.add(satInPanel, gbc);
	}
	
	/**
     * Initialises the Court Room Dropdown panel
     * @return	Panel to be added to the parent
     */
    private JPanel initCourtRoomPanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	
		gbc.weightx = 0.05;
		JLabel courtRoomLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddJudgeUsageDialog.courtroom.label"));
		panel.add(courtRoomLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.95;
		courtRoom = new XComboBox();
		if (courtRoomArray == null) {
			courtRoomArray = getCourtRoomArray();
		}
		setComboBoxArray(courtRoom, courtRoomArray.toArray());
		panel.add(courtRoom, gbc);
        return panel;
    }
    
    /**
     * Initialises the Sat In Dropdown panel
     * @return	Panel to be added to the parent
     */
    private JPanel initSatInPanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
		
    	gbc.weightx = 0.05;
		JLabel courtRoomLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddJudgeUsageDialog.courtroom.label"));
		panel.add(courtRoomLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.95;
		satInCombo = new XComboBox();
		if (satInArray == null) {
			satInArray = getSatInArray();
		}
		setComboBoxArray(satInCombo, satInArray.toArray());
		panel.add(satInCombo, gbc);
        return panel;
    }
    
    /**
     * Initialises the Select Judge panel
     * @return	Panel to be added to the parent
     */
    private JPanel initSelectJudgePanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	
    	TimeFieldsDocListener = new TimeFieldsDocListener();
		
    	gbc.weightx = 0.15;
		JLabel judgeLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddJudgeUsageDialog.judge.label"));
		panel.add(judgeLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.7;
		reqJudgeText = new XTextField();
		reqJudgeText.setEditable(false);
		reqJudgeText.setFocusable(false);
		reqJudgeText.getDocument().addDocumentListener(TimeFieldsDocListener);
		panel.add(reqJudgeText, gbc);

		gbc.gridx++;
		gbc.weightx = 0.15;
		XAction openSearchListingJudgeAction = XhibitActions.getAction(model.getXac(),
				XhibitActions.OpenSearchListingJudge);
		openSearchListingJudgeAction.setCaller(this);
		reqJudgeSearchButton = new JButton(openSearchListingJudgeAction);
		panel.add(reqJudgeSearchButton, gbc);
		
        return panel;
    }
    
    /**
     * Method to handle the return of a selected judge from the Judge Search popup
     * @param action Object returned from Judge Search popup
     */
    public void processAddJudge(OpenSearchListingJudgeAction action) {
        Collection col = action.getResults();

        Iterator it = col.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            try {
                // Update the model with the selected judge
                RefJudgeBasicValue refJudgeBasicValue = (RefJudgeBasicValue) o;
                model.setRefJudge(refJudgeBasicValue);

                // Update the display
                reqJudgeText.setText(model.getReqJudge());
            } catch (final Exception e) {
            }
        }
    }

	@Override
	public void stepInitialise() throws CSRecoverableException {
		// Note: No required populateModel as the model is passed in
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
		} else{
			// User clicked Save
			if ( allFieldsPopulated() && !entryExists() ) {
				String username = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
				JudgeUsageComplexValue newEntry = new JudgeUsageComplexValue();
				newEntry.setCourtRoomId(getSelectedCourtRoomId());
				newEntry.setSittingDate(model.getSittingDate());
				newEntry.setJudge(model.getRefJudge());
				newEntry.setCourtChambersInd(((DropdownCodeStringValue) satInCombo.getSelectedItem()).getCode());
				XhibitDelegateHelper.getBizRefDelegate().addJudgeUsage(newEntry, username);
				reqJudgeText.setText("");
			}
		}
	}
	
	/**
	 * Checks to see if the new record to be created aleady exists in the database
	 * @return true if it exists, else false
	 */
	private boolean entryExists() {
		boolean entryExists = false;
		String exists = XhibitDelegateHelper.getBizRefDelegate().getJudgeUsageExists(model.getSittingDate(), model.getRefJudge().getId());
		if ( exists.equals("Y") ) {
			// An entry for the judge in this court room on this sitting date already exists
			entryExists = true;
			String errorMsg = XHIBITConstant.getResource(XhibitBundles.ErrorText,
					"CourtroomStatistics.alert.judgeUsageEntryExists");
			XMessageBox.alert(parent,
					XHIBITConstant.getResource(XhibitBundles.ErrorText, "CourtroomStatistics.alert.title"),
					true, XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
		return entryExists;
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
     * Returns a List of reference data items for the Court Room combo box
     * @return	List of reference data items
     */
	private ArrayList<DropdownCodeStringValue> getSatInArray() {
		ArrayList<DropdownCodeStringValue> results = new ArrayList<DropdownCodeStringValue>();
		results.add(new DropdownCodeStringValue("Court", "CRT","Court"));
		results.add(new DropdownCodeStringValue("Chambers", "CHA","Chambers"));
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
	 * Determines if all fields required to create a new entry have been populated and are valid
	 * @return true if all fields ok, else false
	 */
	private boolean allFieldsPopulated() {
		boolean valid = true;
		if ( courtRoom == null || courtRoom.getItemCount() == 0) {
			valid = false;
		}
		if (reqJudgeText.getText() == null || reqJudgeText.getText().length() == 0) {
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