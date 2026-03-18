package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import mseries.Calendar.MFieldListener;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * CourtCalendarPanel class.
 */
public class CourtCalendarPanel extends XPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger log = CSServices.getLogger(CourtCalendarPanel.class);

	private static final int FROM_DATE_NULL = 1;

	private static final int TO_DATE_NULL = 2;

	private static final int FROM_DATE_INVALID = 3;

	private static final int TO_DATE_INVALID = 4;

	private static final int FROM_DATE_GREATER_THAN_TO_DATE = 5;

	private static final int FROM_DATE_NOT_NULL = 6;

	private static final int TO_DATE_NOT_NULL = 7;

	private static final int FROM_AND_TO_DATES_VALID = 8;

	private static final int FROM_OR_TO_INVALID = 9;

	private static final boolean ENABLE = true;

	private static final boolean DISABLE = false;

	private String resources = XhibitBundles.CourtCalendar;

	private CourtCalendarModel model;

	private JPanel searchPanel;

	private JPanel errorPanel;

	private JPanel calendarPanel;

	private OkCancelPanel buttonPanel;

	private JLabel datesFromLbl;

	private JLabel datesToLbl;

	private JLabel numberOfDaysLbl;

	private JLabel datesFromControlLbl;

	private JLabel datesToControlLbl;

	private JTextField numberOfDaysControl;

	private XDatePanel datesFromControl;

	private XDatePanel datesToControl;

	private JButton viewCalendarBtn;

	private JButton saveButton;

	private JTable calendarTable;

	private JScrollPane calendarScrollPane;

	private CourtCalendarTableModel tableModel;

	private List<Integer> validationStatus;

	private Integer courtId;

	private List<RefCalendarBasicValue> refCalendarList;

	public CourtCalendarPanel(XDialog parent, CourtCalendarModel model) throws CSRecoverableException {
		super();
		this.model = model;

		// add event handler to Save Button
		buttonPanel = (OkCancelPanel) parent.getButtonPanel();
		saveButton = buttonPanel.okButton;

		courtId = XhibitSingleton.getInstance().getCourtId();

		stepInitialise();
		jbInit();
		stepActivate();
	}

	public void enableSaveButton(boolean enable) {
		saveButton.setEnabled(enable);
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);

		// Search Panel
		JPanel mainPanel = new JPanel(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(1150, 700));
		this.add(scrollPane, gbc);

		searchPanel = initSearchPanel();
		gbc.weighty = 0.1;
		mainPanel.add(searchPanel, gbc);
		
		gbc.weighty = 0.9;
    	gbc.gridy++;
		mainPanel.add(getCalendarPanel(), gbc);
	}
	
	private JPanel initSearchPanel() {
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;

		// Column 1
		c.anchor = GridBagConstraints.LINE_START;
		c.gridy++;
		c.insets = new Insets(0, 25, 0, 5);
		datesFromLbl = new JLabel(XHIBITConstant.getResource(resources, "lblSelectDatesFrom"));
		panel.add(datesFromLbl, c);

		c.gridy++;
		c.insets = new Insets(0, 25, 5, 5);
		numberOfDaysLbl = new JLabel(XHIBITConstant.getResource(resources, "lblNumberOfDays"));
		panel.add(numberOfDaysLbl, c);

		c.gridy++;
		c.gridheight = 1;
		c.insets = new Insets(5, 25, 5, 5);
		viewCalendarBtn = new JButton(XHIBITConstant.getResource(resources, "lblViewCalendar"));
		viewCalendarBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				log.debug("View Calendar button clicked");
				if (!validateDateRange()) {
					log.debug("Invalid date range");
					// set date range error flag
					getModel().setDateRangeInvalid(true);

					// recreate error panel row to allow error message to
					// stretch 3 columns
					updateErrorLabelPanel(true);
					setLabel(getDatesFromControlLbl(),
							XHIBITConstant.getResource(resources, "dateValidationErrorMessage"));

				} else {
					log.debug("valid date range");
					getModel().setDateRangeInvalid(false);
					updateErrorLabelPanel(false);
					setLabel(getDatesFromControlLbl(), " ");
					populateCalendar(courtId, new Date(getModel().getFromDate().getTimeInMillis()),
							new Date(getModel().getToDate().getTimeInMillis()));
				}
			}
		});
		viewCalendarBtn.setEnabled(false);
		panel.add(viewCalendarBtn, c);

		// Column 2
		c.gridx++;
		c.gridy = 0;
		c.weightx = 0.1;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.insets = new Insets(0, 5, 0, 5);

		getErrorPanel().add(getDatesFromControlLbl());
		panel.add(getErrorPanel(), c);

		c.gridwidth = 1;
		c.gridy++;
		c.insets = new Insets(0, 5, 5, 5);
		datesFromControl = new XDatePanel(panel, null, false);
		datesFromControl.setName("datesFromControl");
		datesFromControl.setFocusable(true);
		datesFromControl.getDateComponent().addMFieldListener(new XDatePanelFieldListener(datesFromControl));
		panel.add(datesFromControl, c);

		c.gridy++;
		c.insets = new Insets(5, 5, 5, 5);
		numberOfDaysControl = new JTextField(5);
		numberOfDaysControl.setEditable(false);
		panel.add(numberOfDaysControl, c);

		// Column 3
		c.gridx++;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.01;
		c.insets = new Insets(0, 5, 0, 5);
		datesToLbl = new JLabel(XHIBITConstant.getResource(resources, "lblToDate"));

		panel.add(datesToLbl, c);

		// Column 4
		c.gridx++;
		c.gridy = 0;
		c.weightx = 0.79;
		c.insets = new Insets(0, 5, 0, 5);

		panel.add(getDatesToControlLbl(), c);

		c.gridy++;
		c.insets = new Insets(0, 5, 5, 5);
		datesToControl = new XDatePanel(panel, null, false);
		datesToControl.setName("datesToControl");
		datesToControl.setFocusable(true);
		datesToControl.getDateComponent().addMFieldListener(new XDatePanelFieldListener(datesToControl));
		panel.add(datesToControl, c);
		return panel;
	}

	/**
	 * Method to change the GridBagLayout for the top error row at runtime. When
	 * error text is long after failing date validation, the error label needs
	 * to fit 3 columns but when other errors are shown for individual field
	 * validations then only 1 column needed. Without this the whole look and
	 * feel would get messed up.
	 * 
	 * @param isErrorTextLong
	 */
	private void updateErrorLabelPanel(boolean isErrorTextLong) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_START;

		// Make width = Columns 2 -> 4
		if (isErrorTextLong) {
			gbc.gridwidth = 3;
		} else {
			gbc.gridwidth = 1;
		}
		gbc.weightx = 0.1;
		gbc.insets = new Insets(0, 5, 0, 5);

		// remove datesToControlLbl from searchPanel and set to null
		getSearchPanel().remove(getDatesToControlLbl());
		setDatesToControlLbl(null);

		// remove datesFromControlLbl from searchPanel and set to null
		getErrorPanel().remove(getDatesFromControlLbl());
		setDatesFromControlLbl(null);

		// remove errorPanel from searchPanel
		getSearchPanel().remove(getErrorPanel());
		setErrorPanel(null);

		// create new datesFromControlLbl, add to new errorPanel
		getErrorPanel().add(getDatesFromControlLbl());
		setBorder(getDatesFromControl(), isErrorTextLong);
		setBorder(getDatesToControl(), isErrorTextLong);

		// add new errorPanel to searchPanel
		getSearchPanel().add(getErrorPanel(), gbc);

		if (!isErrorTextLong) {
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 3;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 5);
			c.anchor = GridBagConstraints.LINE_START;

			getSearchPanel().add(getDatesToControlLbl(), c);

			// Clear error flag
			getModel().setDateRangeInvalid(false);
		}
		getSearchPanel().revalidate();
		this.repaint();
	}

	public void stepInitialise() throws CSRecoverableException {
		setModified(false);
	}

	public void stepActivate() throws CSRecoverableException {
		// empty
	}

	/**
	 * Update the model based on which date control changed. The 'To Date' is
	 * defaulted to the 'From Date'.
	 * 
	 * @param control
	 * @param date
	 */
	private void moveScreenToModel(XDatePanel control, Calendar date) {
		if (control.getName().equals("datesFromControl")) {
			getModel().setFromDate(date);
			getModel().setFromDateChanged(true);
			if (date != null && getModel().getToDate() == null) {
				getModel().setToDate(date);
				getModel().setToDateChanged(true);
			}
		} else {
			getModel().setToDate(date);
			getModel().setToDateChanged(true);
		}
	}

	/**
	 * Performs validation according to business rules. Saves validation
	 * outcomes in a list to be used later when updating the View.
	 * 
	 */
	public void stepValidate() throws CSRecoverableException {
		boolean isValid = true;
		// Validate From Date
		validationStatus = new ArrayList<Integer>();
		if (getModel().isFromDateChanged()) {
			if (getModel().getFromDate() == null) {
				String text = getDatesFromControl().getText();
				if (text.length() > 0) {
					getValidationStatus().add(FROM_DATE_INVALID);
				} else {
					getValidationStatus().add(FROM_DATE_NULL);
				}
				isValid = false;
			} else {
				getValidationStatus().add(FROM_DATE_NOT_NULL);
			}
		}
		if (getModel().isToDateChanged()) {
			if (getModel().getToDate() == null) {
				String text = getDatesToControl().getText();
				if (text.length() > 0) {
					getValidationStatus().add(TO_DATE_INVALID);
				} else {
					getValidationStatus().add(TO_DATE_NULL);
				}
				isValid = false;
			} else {
				getValidationStatus().add(TO_DATE_NOT_NULL);
			}
		}
		if (getModel().getFromDate() != null && getModel().getToDate() != null) {
			if (getModel().getFromDate().after(getModel().getToDate())) {
				getValidationStatus().add(FROM_DATE_GREATER_THAN_TO_DATE);
				isValid = false;
			} else {
				getValidationStatus().add(FROM_AND_TO_DATES_VALID);
			}
		}
		if (!isValid) {
			getValidationStatus().add(FROM_OR_TO_INVALID);
		}
	}

	/**
	 * Updates the View based on the Date Control Validation results (Not the
	 * validation after the View Calendar button is clicked).
	 */
	public void stepUpdateViewState() {
		// Update controls based on validation
		if (getModel().isDateRangeInvalid()) {
			updateErrorLabelPanel(false);
		}
		for (Integer i : getValidationStatus()) {
			if (i == FROM_DATE_NULL) {
				setBorder(getDatesFromControl(), true);
				setLabel(getDatesFromControlLbl(), XHIBITConstant.getResource(resources, "lblDateFromIsMandatory"));
			}
			if (i == TO_DATE_NULL) {
				setBorder(getDatesToControl(), true);
				setLabel(getDatesToControlLbl(), XHIBITConstant.getResource(resources, "lblDateToIsMandatory"));
			}
			if (i == FROM_DATE_INVALID) {
				setBorder(getDatesFromControl(), true);
				setLabel(getDatesFromControlLbl(), "Invalid Entry");
			}
			if (i == TO_DATE_INVALID) {
				setBorder(getDatesToControl(), true);
				setLabel(getDatesToControlLbl(), "Invalid Entry");
			}
			if (i == FROM_DATE_GREATER_THAN_TO_DATE) {
				setBorder(getDatesFromControl(), true);
				setBorder(getDatesToControl(), true);
				setLabel(getDatesFromControlLbl(), " ");
				setLabel(getDatesToControlLbl(), XHIBITConstant.getResource(resources, "lblInvalidDates"));
			}
			if (i == FROM_DATE_NOT_NULL) {
				if (!(getDatesToControl().getText().length() > 0) && getModel().isFromDateChanged()) {
					getDatesToControl().setDate(getModel().getToDate());
				}
				setBorder(getDatesFromControl(), false);
				setLabel(getDatesFromControlLbl(), " ");
			}
			if (i == TO_DATE_NOT_NULL) {
				setBorder(getDatesToControl(), false);
				setLabel(getDatesToControlLbl(), " ");
			}
			if (i == FROM_AND_TO_DATES_VALID) {
				handleNumberOfDays(ENABLE);
				handleViewCalendarButton(ENABLE);
				setBorder(getDatesFromControl(), false);
				setLabel(getDatesFromControlLbl(), " ");
				setBorder(getDatesToControl(), false);
				setLabel(getDatesToControlLbl(), " ");
			}
			if (i == FROM_OR_TO_INVALID) {
				handleNumberOfDays(DISABLE);
				handleViewCalendarButton(DISABLE);
			}
		}
		// set event flags back to false after validation
		getModel().setFromDateChanged(false);
		getModel().setToDateChanged(false);
	}

	public void stepDeactivate() throws CSRecoverableException {
		// empty
	}

	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (update) {
			if (buttonPanel.okButton.equals(getDeinitialiseSource())) {
				if (getCalendarTable().isEditing()) {
					getCalendarTable().getCellEditor().stopCellEditing();
				}

				RefCalendarVO refCalendarVO = getTableModel().updateTableModelData();
				BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();

				if (refCalendarVO.getUpdateList() != null) {
					bizRefDelegate.updateRefCalendarDates(refCalendarVO.getUpdateList(),
							XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				}

				// Show success pop up Dialog
				JOptionPane.showMessageDialog(null, XHIBITConstant.getResource(resources, "saveSuccessMessage"),
						XHIBITConstant.getResource(resources, "saveSuccessHeading"), JOptionPane.INFORMATION_MESSAGE);

				// refresh calendar with saved data
				populateCalendar(courtId, new Date(getModel().getFromDate().getTimeInMillis()),
						new Date(getModel().getToDate().getTimeInMillis()));

				setModified(false);
			}
		}
	}

	/**
	 * Convenience method used to update the error label fields in the View.
	 * 
	 * @param label
	 * @param text
	 */
	private void setLabel(JLabel label, String text) {
		label.setText(text);
	}

	/**
	 * Convenience method to get a date control's value.
	 * 
	 * @param dateField
	 * @return
	 */
	private Calendar getDate(XDatePanel dateField) {
		try {
			return dateField.getDate();
		} catch (CSValidationException csve) {
			return null;
		}
	}

	/**
	 * Sets the error border around mandatory fields after validation and
	 * updating the View.
	 * 
	 * @param dateControl
	 * @param isError
	 */
	private void setBorder(XDatePanel dateControl, boolean isError) {
		if (isError) {
			dateControl.setBorder(BorderFactory.createLineBorder(Color.RED));
		} else {
			dateControl.setBorder(BorderFactory.createEmptyBorder());
		}
	}

	/**
	 * Calculates and sets number of days field when valid dates entered.
	 * 
	 * @param enabled
	 */
	private void handleNumberOfDays(boolean enabled) {
		if (enabled == true) {
			long numberOfDays = getNumberOfDays(getModel().getFromDate(), getModel().getToDate());
			getNumberOfDaysControl().setText(String.valueOf(numberOfDays));
		} else {
			getNumberOfDaysControl().setText(" ");
		}
	}

	/**
	 * Gets the number of days between From and To Dates. Takes into account the
	 * extra hour lost or gained in autumn and spring.
	 * 
	 * @return
	 */
	private int getNumberOfDays(Calendar fromDate, Calendar toDate) {
		String METHOD_NAME = "getNumberOfDays";
		log.debug("Entering " + METHOD_NAME + "(" + fromDate + "," + toDate +")");
		long timeInMs = toDate.getTime().getTime() - fromDate.getTime().getTime();
		double daysInDouble = timeInMs / (24.0 * 60.0 * 60.0 * 1000.0);
		double offset = daysInDouble % 1;
		int daysInInt = (int) (daysInDouble - offset);
		
		log.debug("timeinMs: " + timeInMs);
		log.debug("daysInDouble: " + daysInDouble);
		log.debug("offset: " + offset);
		log.debug("daysInInt: " + daysInInt);

		if (offset > 0.9) {
			daysInInt += 1;
		}
		log.debug("daysInInt after offset check: " + daysInInt);
		
		log.debug("return value: " + (daysInInt + 1));

		return (daysInInt + 1);
	}

	/**
	 * Enables the View Calendar button if date validation is successful
	 * 
	 * @param enabled
	 */
	private void handleViewCalendarButton(boolean enabled) {
		getViewCalendarBtn().setEnabled(enabled);
	}

	/**
	 * Validates the date range entered by user.
	 * 
	 * @return
	 */
	private boolean validateDateRange() {
		String METHOD_NAME = "validateDateRange";
		log.debug("Entering " + METHOD_NAME + "(" + ")");
		int numberOfDays = getNumberOfDays(getModel().getFromDate(), getModel().getToDate());
		log.debug("numberOfDays");

		if (numberOfDays > 365) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Get Calendar dates for dates specified.
	 * 
	 * @param courtId
	 * @param fromDate
	 * @param toDate
	 */
	@SuppressWarnings("unchecked")
	private void populateCalendar(Integer courtId, Date fromDate, Date toDate) {
		String METHOD_NAME = "populateCalendar";
		log.debug("Entering " + METHOD_NAME + "(" + courtId + "," + fromDate + "," + toDate +")");
		try {
		BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		log.debug("Getting results from getRefCalendarDatesByCourt");
		Collection<?> results = bizRefDelegate.getRefCalendarDatesByCourt(courtId, fromDate, toDate);
		if (results != null && results.size() == getNumberOfDays(getModel().getFromDate(), getModel().getToDate())) {
			log.debug("got results: " + results.size());
			log.debug("populating refCalendarList");
			refCalendarList = (ArrayList<RefCalendarBasicValue>) results;
			log.debug("populating table model");
			getTableModel().populateTableModel(refCalendarList, getCalendarDate(fromDate), getCalendarDate(toDate));
			log.debug("Fire table changed");
			getTableModel().fireTableChanged(null);
		} else {
			log.debug("missing data error message");
			JOptionPane.showMessageDialog(null, XHIBITConstant.getResource(resources, "missingDataErrorMessage"),
					"Missing data", JOptionPane.ERROR_MESSAGE);
		}
		} catch (Exception ex) {
			ex.printStackTrace();
			XHIBITConstant.handleError(ex);
		}
	}

	@Override
	public void setModified(boolean isModified) {
		super.setModified(isModified);
		enableSaveButton(isModified);
	}

	@Override
	public boolean getModified() {
		return super.getModified();
	}

	/**
	 * Listener class for handling events when the date fields are exited.
	 * 
	 * @author grewalg
	 *
	 */
	class XDatePanelFieldListener implements MFieldListener {
		XDatePanel dateField;

		public XDatePanel getDateField() {
			return dateField;
		}

		public void setDateField(XDatePanel dateField) {
			this.dateField = dateField;
		}

		XDatePanelFieldListener(XDatePanel dateField) {
			this.dateField = dateField;
		}

		@Override
		public void fieldEntered(FocusEvent event) {
			// empty
		}

		@Override
		public void fieldExited(FocusEvent event) {
			getDateField().parseAndConvertDateString();
			moveScreenToModel(getDateField(), getDate(getDateField()));
			try {
				stepValidate();
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
			stepUpdateViewState();
		}
	}

	public JPanel getSearchPanel() {
		return searchPanel;
	}

	public void setSearchPanel(JPanel searchPanel) {
		this.searchPanel = searchPanel;
	}

	public JPanel getCalendarPanel() {
		if (calendarPanel == null) {
			calendarPanel = new JPanel(new BorderLayout());
			calendarPanel.add(getCalendarScrollPane(), BorderLayout.CENTER);
		}
		return calendarPanel;
	}

	public void setCalendarPanel(JPanel calendarPanel) {
		this.calendarPanel = calendarPanel;
	}

	public JScrollPane getCalendarScrollPane() {
		if (calendarScrollPane == null) {
			calendarScrollPane = new JScrollPane(getCalendarTable());
			calendarScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			calendarScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		return calendarScrollPane;
	}

	public JTable getCalendarTable() {
		if (calendarTable == null) {
			calendarTable = new JTable();
			calendarTable.setFillsViewportHeight(true);
			calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			calendarTable.setModel(getTableModel());
			TableColumnModel tcm = calendarTable.getColumnModel();
			for (int i = 0; i < 7; i++) {
				tcm.getColumn(i).setPreferredWidth(250);
			}
			calendarTable.setRowHeight(100);
			CourtCalendarTableCellRenderer cellRenderer = new CourtCalendarTableCellRenderer(this);
			calendarTable.setDefaultRenderer(RefCalendarValue.class, cellRenderer);
			CourtCalendarTableCellEditor cellEditor = new CourtCalendarTableCellEditor(this);
			cellEditor.addCellEditorListener(cellEditor);
			calendarTable.setDefaultEditor(RefCalendarValue.class, cellEditor);

			JTableHeader header = calendarTable.getTableHeader();
			final TableCellRenderer tcr = header.getDefaultRenderer();
			header.setDefaultRenderer(new TableCellRenderer() {
				private JLabel lbl;

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					lbl = (JLabel) tcr.getTableCellRendererComponent(table, value, false, false, row, column);
					if (column == 6) {
						lbl.setForeground(Color.RED);
					}
					return lbl;
				}
			});
		}
		return calendarTable;
	}

	public void setCalendarTable(JTable calendarTable) {
		this.calendarTable = calendarTable;
	}

	public CourtCalendarTableModel getTableModel() {
		if (tableModel == null) {
			tableModel = new CourtCalendarTableModel(refCalendarList, this);
		}
		return tableModel;
	}

	public void setTableModel(CourtCalendarTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public JPanel getErrorPanel() {
		if (errorPanel == null) {
			errorPanel = new JPanel();
		}
		return errorPanel;
	}

	public void setErrorPanel(JPanel errorPanel) {
		this.errorPanel = errorPanel;
	}

	public OkCancelPanel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(OkCancelPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public JLabel getDatesFromLbl() {
		return datesFromLbl;
	}

	public void setDatesFromLbl(JLabel datesFromLbl) {
		this.datesFromLbl = datesFromLbl;
	}

	public JLabel getDatesToLbl() {
		return datesToLbl;
	}

	public void setDatesToLbl(JLabel datesToLbl) {
		this.datesToLbl = datesToLbl;
	}

	public JLabel getNumberOfDaysLbl() {
		return numberOfDaysLbl;
	}

	public void setNumberOfDaysLbl(JLabel numberOfDaysLbl) {
		this.numberOfDaysLbl = numberOfDaysLbl;
	}

	public JLabel getDatesFromControlLbl() {
		if (datesFromControlLbl == null) {
			datesFromControlLbl = new JLabel(XHIBITConstant.getResource(resources, "lblDateFromIsMandatory"));

			datesFromControlLbl.setForeground(Color.RED);
			datesFromControlLbl.setText(" ");
			datesFromControlLbl.setVisible(true);
		}
		return datesFromControlLbl;
	}

	public void setDatesFromControlLbl(JLabel datesFromControlLbl) {
		this.datesFromControlLbl = datesFromControlLbl;
	}

	public JLabel getDatesToControlLbl() {
		if (datesToControlLbl == null) {
			datesToControlLbl = new JLabel(XHIBITConstant.getResource(resources, "lblDateToIsMandatory"));
			datesToControlLbl.setSize(8, 1);
			datesToControlLbl.setForeground(Color.RED);
			datesToControlLbl.setText(" ");
			datesToControlLbl.setVisible(true);
		}
		return datesToControlLbl;
	}

	public void setDatesToControlLbl(JLabel datesToControlLbl) {
		this.datesToControlLbl = datesToControlLbl;
	}

	public JTextField getNumberOfDaysControl() {
		return numberOfDaysControl;
	}

	public void setNumberOfDaysControl(JTextField numberOfDaysControl) {
		this.numberOfDaysControl = numberOfDaysControl;
	}

	public XDatePanel getDatesFromControl() {
		return datesFromControl;
	}

	public void setDatesFromControl(XDatePanel datesFromControl) {
		this.datesFromControl = datesFromControl;
	}

	public XDatePanel getDatesToControl() {
		return datesToControl;
	}

	public void setDatesToControl(XDatePanel datesToControl) {
		this.datesToControl = datesToControl;
	}

	public JButton getViewCalendarBtn() {
		return viewCalendarBtn;
	}

	/**
	 * @return the saveButton
	 */
	public JButton getSaveButton() {
		return saveButton;
	}

	public void setViewCalendarBtn(JButton viewCalendarBtn) {
		this.viewCalendarBtn = viewCalendarBtn;
	}

	public CourtCalendarModel getModel() {
		return model;
	}

	public void setModel(CourtCalendarModel model) {
		this.model = model;
	}

	public List<Integer> getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(List<Integer> validationStatus) {
		this.validationStatus = validationStatus;
	}

	private Calendar getCalendarDate(Date date) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);
		return calDate;
	}
}
