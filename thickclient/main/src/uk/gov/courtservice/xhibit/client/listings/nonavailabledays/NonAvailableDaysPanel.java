/**
 * <p>
 * Title: NonAvailableDaysPanel
 * </p>
 * <p>
 * Description: NonAvailableDaysPanel represents the panel for the Non Available Days screen
 * CTX-1313
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Gurinder Brar
 * @version 1.0
 */

package uk.gov.courtservice.xhibit.client.listings.nonavailabledays;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseNonAvailDaysBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.MultiLineEditField;
import uk.gov.courtservice.xhibit.client.util.PageController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class NonAvailableDaysPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	
	private JButton newButton;
	private JButton deleteButton;
	private JButton saveButton;
	private JButton closeButton;
	private NonAvailableDaysModel model;
	private XTable nonAvailableDaysTable;
	private XDatePanelWithEvent startDate;
	private XDatePanel endDate;
	private LocalPageController pageController = new LocalPageController();
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private DateValidationController startDateValidation;
	private DateValidationController endDateValidation;
	private TextValidationController reasonValidation;
	private JLabel endDateWarningLabel;
	private JLabel startDateWarningLabel;
	private JLabel reasonWarningLabel;
	private MultiLineEditField reason;
	private DefaultTableModel nonAvailableDaysTableModel;
	private XDialog parent;
	private boolean invalidEntry = true;
	private static final Logger log = CSServices.getLogger(NonAvailableDaysPanel.class);
	private static final int tableWidth = 750;
	private static final int columnWidth1 = 150;
	private static final int columnWidth2 = 450;
	private static final String DATE_FORMAT = "dd-MMM-yyyy";
	private static final String DISPLAY_NAME = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
	
	public NonAvailableDaysPanel(NonAvailableDaysDialog parent, NonAvailableDaysModel model) throws CSRecoverableException {
		this.model = model;	 		
		this.parent = parent;
		
        if (XAction.internalDebug) {
            log.debug("CaseId = " + this.model.getCaseId().toString());
        }
		
        stepInitialise();
        jbInit();
        configureTabOrder();
	}
	
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(750, 300));
		this.add(scrollPane, gbc);
		
        CustomButtonPanel buttonPanel = (CustomButtonPanel)parent.getButtonPanel();
        buttonPanel.setShowCancelButton(true);
	    buttonPanel.setCancelText("Close");
	    buttonPanel.setCancelToolTip("Close");
	    newButton = new JButton(new NewButtonAction(this));
	    buttonPanel.add(newButton);
	    saveButton = buttonPanel.addButton("nonAvailableDaysSave", false, true);
	    deleteButton = buttonPanel.addButton("nonAvailableDaysDelete", false, false);
	    closeButton = buttonPanel.cancelButton; 
	  
	 	// Pane - Table
	    gbc.weighty = 0.6;
	    JPanel tablePanel = initTablePanel();
	    mainPanel.add(tablePanel, gbc);
		
		// Panel - Non available days
	    gbc.weighty = 0.4;
		gbc.gridy++;
		JPanel nonAvailableDaysPanel = initNonAvailableDaysPanel();
		mainPanel.add(nonAvailableDaysPanel, gbc);
	}
	
	private JPanel initTablePanel() {
		String[] columnHeaders = new String[] { 
				XHIBITConstant.getResource(XhibitBundles.Listings, "nonAvailableDaysStartDate"),  
			    XHIBITConstant.getResource(XhibitBundles.Listings, "nonAvailableDaysEndDate"),
			    XHIBITConstant.getResource(XhibitBundles.Listings, "nonAvailableDaysReason")
			    };
		GridBagConstraints gbc = new GridBagConstraints(0, 0, columnHeaders.length, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		// Add the panel elements
		nonAvailableDaysTableModel =  new DefaultTableModel(new Object[][] {}, columnHeaders) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		nonAvailableDaysTable = XTableFactory.getInstance().createDefaultTable(nonAvailableDaysTableModel);
		TableUtils.setupDefaultsOnJTable(nonAvailableDaysTable);
		nonAvailableDaysTable.getCellSelectionEnabled();
		nonAvailableDaysTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				handleSelectionEvent(e);
			}
		});
		
		Object[] longValues = new Object[] { 
                 " Start Date ", 
                 " End Date ",  
                 " Reason " };
         log.debug("Table column count: " + nonAvailableDaysTable.getColumnModel().getColumnCount());
         log.debug("Long values size: " + longValues.length);
         
         nonAvailableDaysTable.initColumnSizes(longValues, tableWidth);
         nonAvailableDaysTable.getColumnModel().getColumn(0).setPreferredWidth(columnWidth1);
         nonAvailableDaysTable.getColumnModel().getColumn(1).setPreferredWidth(columnWidth1);
         nonAvailableDaysTable.getColumnModel().getColumn(2).setPreferredWidth(columnWidth2);
         nonAvailableDaysTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		// Add Table to Scroll Pane
		JScrollPane scrollPane = new JScrollPane();		
		scrollPane.setPreferredSize(new Dimension(gbc.gridwidth, 100));
		scrollPane.setViewportView(nonAvailableDaysTable);		
		panel.add(scrollPane, gbc);
				
		return panel;
	}
	
	private void handleSelectionEvent(ListSelectionEvent e) {
		if (nonAvailableDaysTable.getSelectedRowCount() == 1) {
			try {
				DefaultTableModel model = (DefaultTableModel) nonAvailableDaysTable.getModel();
				String startDte = (String) (model.getValueAt(nonAvailableDaysTable.getSelectedRow(), 0));
				log.debug("tabledata: startDate=" + startDate);
				String endDte = (String) (model.getValueAt(nonAvailableDaysTable.getSelectedRow(), 1));
				log.debug("tabledata: endDate=" + endDate);
				String rsn = (String) (model.getValueAt(nonAvailableDaysTable.getSelectedRow(), 2));
				log.debug("tabledata: reason=" + rsn);
				
				startDate.setDate(getDateFromString(startDte,DATE_FORMAT));
				endDate.setDate(getDateFromString(endDte,DATE_FORMAT));
				reason.setText(rsn);
				
				newButton.setEnabled(true);
				deleteButton.setEnabled(true);
				
				clearAllErrors();
				
				pageController.reset();
				
			} catch (Exception ex) {
				log.debug("Failed to get Non Available Days from table");
			}
		}
		
	}

	private JPanel initNonAvailableDaysPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		// Errors
		gbc.insets = XHIBITConstant.errorLabelInsets;		
		gbc.weightx = 0.1;
		gbc.gridwidth = 2;
		panel.add(getStartDateWarningLabel(), gbc);
		
		gbc.gridx++;
		gbc.gridx++;
		gbc.weightx = 0.9;
		panel.add(getEndDateWarningLabel(), gbc);
		gbc.gridx++;
		gbc.insets = XHIBITConstant.nonContainerInsets;		

		// Dates
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.1;
		JLabel startDateLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "nonAvailableDaysStartDateLabel"));
		panel.add(startDateLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.4;
		startDate = new XDatePanelWithEvent(panel, null, false) {

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void fireEvent() {
				if (startDate.isDateValidate()) {
 					try {
 						if (startDate.getDate() != null) {
 							endDate.setDate(startDate.getDate());
 						}
					} catch (CSValidationException ex) {
						XHIBITConstant.handleError(ex);
					}
				}
				pageController.setPageChanged();
			}
		};
		panel.add(getStartDatePanel(), gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.1;
		JLabel endDateLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "nonAvailableDaysEndDateLabel"));
		panel.add(endDateLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.4;
		endDate = new XDatePanel(panel, null, false);
		panel.add(getEndDatePanel(), gbc);
			
		// New line
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.gridy++;
		gbc.weightx = 1.0;
		panel.add(getReasonWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.1;
		JLabel reasonLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "nonAvailableDaysReasonLabel"));
		panel.add(reasonLabel, gbc);
	
		gbc.gridx++;
		gbc.gridwidth = 5;
		gbc.gridheight = 2;
		gbc.weightx = 0.9;
		gbc.fill = GridBagConstraints.BOTH;
		reason = new MultiLineEditField(2,200);
		panel.add(getReasonTextField(),gbc);
				
		pageController.addChangeListeners(panel.getComponents());
		return panel;
	}
	
	private void moveModelToScreen() {
		// Set the screen title and initialisation
		String dialogTitle = XHIBITConstant.getResource(XhibitBundles.Listings, "nonAvailableDaysTitle");
		parent.setTitle(dialogTitle.concat(" - ").concat(model.getCaseType()).concat(model.getCaseNumber().toString()));
		newButton.setEnabled(false);
		populateHearingsTable(null);
		initialiseScreen(false);
	}
	
	private void configureTabOrder() {
		parent.setFocusTraversalPolicyProvider(true);
		parent.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] {
				nonAvailableDaysTable,startDate, endDate, reason, newButton, saveButton , deleteButton, closeButton
		}, 1));
	}
	
	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		invalidEntry = (validationController.hasErrors()
				|| !ValidationControllerFactory.validateComponents(validationControllers));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void stepInitialise() throws CSRecoverableException {
		
		try {
			Collection<CaseNonAvailDaysBasicValue> hearings = 
					XhibitDelegateHelper.getListingsDelegate().findCaseNonAvailDaysByCaseId(model.getCaseId());
			populateModel(hearings);
		} catch(ListingsControllerException e) {
			throw new CSRecoverableException();
		}
	
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		resetEntryPanel();
	}
	
	private void resetEntryPanel() {
		pageController.setEnabled(false);
		nonAvailableDaysTable.getSelectionModel().clearSelection();
		startDate.setDate(new Date());
		endDate.setDate(new Date());
		reason.setText("");
		reason.requestFocus();
		pageController.setEnabled(true);
		clearAllErrors();
		pageController.reset();
	}
	
	private void disableButtons() {
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
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
			if (closeButton.equals(getDeinitialiseSource())) {
				if (pageController.isPageChanged()) {
					showCancelConfirmationMsg();
				}
			} else if (deleteButton.equals(getDeinitialiseSource())) {
			      delete();
			}
		} else {
			if (saveButton.equals(getDeinitialiseSource())) {
				if (!invalidEntry) {
					checkAffectedDiaryFixturesBeforeSaving();
				}
			} 
		} 
	}
	
	private void checkAffectedDiaryFixturesBeforeSaving()  throws CSRecoverableException {
		try {
			Collection fixtures = XhibitDelegateHelper.getListingsDelegate().findCaseDiaryFixturesImpacted(
														model.getCaseEntryId(), startDate.getDate().getTime(), endDate.getDate().getTime(), new Date());
			
			if (fixtures != null && !fixtures.isEmpty()) {
				displaySaveWarning();
			} else {
				save();
			}
		} catch (ListingsControllerException e) {
			throw new CSRecoverableException(e);
		} 
	}
	
	private void displaySaveWarning() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parent, XHIBITConstant.getResource(XhibitBundles.Listings, 
					"nonAvailableDaysCancelConfirmationTitle"), true,
					XMessageBox.ICONWARNING, XHIBITConstant.getResource(XhibitBundles.Listings, 
							"nonAvailableDaysSaveWarningMessage"), XMessageBox.YESNO, 
					XMessageBox.DEFAULTNO);
			
		if (messageBoxReply) {
			save();
		}
	}
	
	private class NewButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public NewButtonAction(NonAvailableDaysPanel parent) {
			populateFromBundle("nonAvailableDaysNew");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {
			initialiseScreen(false);
		}
	}

	private void initialiseScreen(boolean buttonState) {
		resetEntryPanel();
		if (!buttonState) {
			disableButtons();
		}
	}
		
	private void populateModel(Collection<CaseNonAvailDaysBasicValue> hearings) {
		model.setHearings(hearings);
	}
	
	private void populateHearingsTable(Integer selectionId) {
		Integer rowSelected = null;
		
		nonAvailableDaysTableModel.setRowCount(0);
	
		if (model.getHearings() != null && !model.getHearings().isEmpty()) {
			String formattedStartDate;
			String formattedEndDate;
			for (int row =0; row < model.getHearings().size(); row++) {
				CaseNonAvailDaysBasicValue caseNonAvailDaysBasicValue= ((List<CaseNonAvailDaysBasicValue>)model.getHearings()).get(row);

				formattedStartDate = getFormattedDate(caseNonAvailDaysBasicValue.getStartDate());
				formattedEndDate = getFormattedDate(caseNonAvailDaysBasicValue.getEndDate());
				if (selectionId != null && caseNonAvailDaysBasicValue.getId().equals(selectionId)) {
					rowSelected = row;
				}
				nonAvailableDaysTableModel
					.addRow(new Object[] { formattedStartDate, formattedEndDate, caseNonAvailDaysBasicValue.getReason()});
			}
			
			if (rowSelected != null) {
				nonAvailableDaysTable.setRowSelectionInterval(rowSelected, rowSelected);
			}  
		}
	}
	
	private String getFormattedDate(Date date) {
		return date != null ? new SimpleDateFormat(DATE_FORMAT).format(date.getTime()) : null;
	}
	
	private Date getDateFromString(String date, String format) throws CSRecoverableException {
		try {
			return date!=null ? new SimpleDateFormat(format).parse(date) : null;
		} catch (ParseException e) {
			log.debug("Error parsing date");
			throw new CSRecoverableException(e);
		}
	}
	
	private void showCancelConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parent, XHIBITConstant.getResource(XhibitBundles.Listings, 
				"nonAvailableDaysCancelConfirmationTitle"), true,
				XMessageBox.ICONQUESTION, XHIBITConstant.getResource(XhibitBundles.Listings, 
						"nonAvailableDaysCancelConfirmationMessage"), XMessageBox.YESNO,
				XMessageBox.DEFAULTCANCEL);

		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}
	
	private void save() throws CSRecoverableException {
		CaseNonAvailDaysBasicValue caseNonAvailDaysBasicValue;
		
		if (nonAvailableDaysTable.getSelectedRow() >= 0) {
			caseNonAvailDaysBasicValue = ((List<CaseNonAvailDaysBasicValue>) model.getHearings()).get(nonAvailableDaysTable.getSelectedRow());
		} else {
			caseNonAvailDaysBasicValue  = new CaseNonAvailDaysBasicValue();
			caseNonAvailDaysBasicValue.setCaseId(model.getCaseId());
		}
		caseNonAvailDaysBasicValue.setStartDate(getDateFromString(startDate.getText(),DATE_FORMAT));
		caseNonAvailDaysBasicValue.setEndDate(getDateFromString(endDate.getText(),DATE_FORMAT));
		caseNonAvailDaysBasicValue.setReason(reason.getText());
		
		// Save the data returning the updated Id
		Integer nadId = 
				XhibitDelegateHelper.getListingsDelegate().saveCaseListingNonAdditionalDays(caseNonAvailDaysBasicValue,DISPLAY_NAME);
		
		// Get the newly updated values (after the db triggers have fired upon commit)
		CaseNonAvailDaysBasicValue updatedBasicValue  = XhibitDelegateHelper.getListingsDelegate().findCaseNonAvailDays(nadId);
		
		if (caseNonAvailDaysBasicValue.getId() == null) {
			model.addHearing(updatedBasicValue);
		} else { 
			model.updateHearing(updatedBasicValue);
		}								
		 
		populateHearingsTable(caseNonAvailDaysBasicValue.getId());
		
		if (caseNonAvailDaysBasicValue.getId() == null) {
			resetEntryPanel();
		}
	}
	
	private void delete() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parent, XHIBITConstant.getResource(XhibitBundles.Listings, 
				"nonAvailableDaysCancelConfirmationTitle"), true,
				XMessageBox.ICONQUESTION, XHIBITConstant.getResource(XhibitBundles.Listings, 
						"nonAvailableDaysYesConfirmationMessage"), XMessageBox.YESNO,
				XMessageBox.DEFAULTNO);
		if (messageBoxReply && model.getHearings() != null && !model.getHearings().isEmpty()) {
			CaseNonAvailDaysBasicValue caseNonAvailDaysBasicValue = ((List<CaseNonAvailDaysBasicValue>) model.getHearings()).get(nonAvailableDaysTable.getSelectedRow());
			XhibitDelegateHelper.getListingsDelegate().deleteCaseNonAvailDaysBasicValue(caseNonAvailDaysBasicValue,DISPLAY_NAME);
			model.deleteHearing(caseNonAvailDaysBasicValue);
			populateHearingsTable(null);
			resetEntryPanel();
			disableButtons();
		}		
	}
	
	private JLabel getEndDateWarningLabel() {
    	if (endDateWarningLabel == null) {
    		endDateWarningLabel = new JLabel(" ");
    	}
    	return endDateWarningLabel;
    }
	
	private JLabel getStartDateWarningLabel() {
    	if (startDateWarningLabel == null) {
    		startDateWarningLabel = new JLabel(" ");
    	}
    	return startDateWarningLabel;
    }
	
	private JLabel getReasonWarningLabel() {
    	if (reasonWarningLabel == null) {
    		reasonWarningLabel = new JLabel(" ");
    	}
    	return reasonWarningLabel;
    }
	
	private MultiLineEditField getReasonTextField() {
		reasonValidation = ValidationControllerFactory.createTextRequired(this, 
								reason, getReasonWarningLabel());
		
		validationControllers.add(reasonValidation);
		
		return reason;
	}
	
	private void clearAllErrors() {
		startDateValidation.clearErrors();
		endDateValidation.clearErrors();
		reasonValidation.clearErrors();
	}
	
	private XDatePanel getStartDatePanel(){
		startDateValidation = ValidationControllerFactory.createDateRequired(this,
								startDate, getStartDateWarningLabel(), new AbstractDateValidator() {
			 @Override
			 public void validate(XDatePanel target, List<String> errors) {
				 	if (hasDate(target) && nonAvailableDaysTable.getSelectedRow() == -1
							&& getDate(target).before(getCalendarNoTime()) ) {
						// Start date cannot be in the past (new entries only)
						errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText,"listings.nonavailabledays.error.dateInPast"));
					} 
					else {
						if ( hasDate(target) ) {
							startDateValidation.clearErrors();
						}
						pageController.setPageChanged();
					}
			}});
		
		validationControllers.add(startDateValidation);
		
		return startDate;
	 }
	
	 private XDatePanel getEndDatePanel() {
		 endDateValidation = ValidationControllerFactory.createDateRequired(this,
								endDate, getEndDateWarningLabel(), new AbstractDateValidator() {
		 @Override
		 public void validate(XDatePanel target, List<String> errors) {
				if (hasDate(target) && hasDate(startDate) && getDate(target).before(getDate(startDate))) {
					errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText,"listings.nonavailabledays.error.endDateBeforeStartDate"));
					return;
				} 
				
				if (hasDate(endDate)) {
					endDateValidation.clearErrors();
				}
				
				pageController.setPageChanged();
		}});
		 
		validationControllers.add(endDateValidation);
		
		return endDate;
	 }
	 
	// Class to change listeners to each component within form
	private class LocalPageController extends PageController {
	
		@Override
		public void reset() {
			super.reset();
			saveButton.setEnabled(false);
		}
		
		public void setEnabled (boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		protected void setPageChanged() {
			super.setPageChanged();
			if (enabled) {
				saveButton.setEnabled(isPageChanged() && ValidationControllerFactory.validateComponents(validationControllers));
			}
		}
	}
}