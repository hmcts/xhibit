package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javax.swing.InputVerifier;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import mseries.Calendar.MDefaultPullDownConstraints;
import mseries.Calendar.MFieldListener;
import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import mseries.ui.MDateEntryField;
import mseries.ui.MSimpleDateFormat;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 52275 25-03-2003 AW Daley modifed to read tool tip text from resource bundle
 * and set tool tip on date field.
 */
public class XDatePanel extends XPanel {

	public static final String dateError = "validation.date";

	private static final Logger log = CSServices.getLogger(XDatePanel.class);

	private static final int MIN_YEAR = 1900;

	private static final int MAX_YEAR = 9999;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private boolean required = true;

	private int last = -1;

	private ResourceBundle datePanelResources;

	private JPanel dateContainer;

	// Error handling
	private boolean error;
	
	public boolean hasSecondaryError=false;
	private String secondaryErrorText;

	private JLabel warningLabel = null;

	private JTextField PGTextField = null;
	private JLabel lblIPGuardian = null;

	private Boolean PGRequired = false;
	private boolean notFuture;
	private boolean under18;
	private String beforeOrAfter = null;
	private XDatePanel toCompare = null;
	private String beforeName = null;
	private String afterName = null;
	private String customWarning = "";

	private boolean gridBagLayout = false;

	final MDateEntryField entryField = new MDateEntryField(10);

	/**
	 * Create panel using a reference to the parent and a default date
	 * 
	 * @param containingPanel
	 * @param defaultDate
	 */
	public XDatePanel(JPanel containingPanel, Calendar defaultDate) {
		this.dateContainer = containingPanel;
		stepInitialise();
		init(defaultDate);
		attachDateParser();
		stepActivate();
		setFocus();
	}

	/**
	 * Create panel using a reference to the parent and a default date
	 * Manadatory field included
	 * 
	 * @param containingPanel
	 * @param defaultDate
	 */
	public XDatePanel(JPanel containingPanel, Calendar defaultDate, boolean required) {
		this.dateContainer = containingPanel;
		this.required = required;
		stepInitialise();
		init(defaultDate);
		attachDateParser();
		stepActivate();
		setFocus();
	}

	public XDatePanel(JPanel containingPanel, Calendar defaultDate, boolean required, JLabel WarningLabel) {
		this.dateContainer = containingPanel;
		this.required = required;
		this.warningLabel = WarningLabel;
		stepInitialise();
		init(defaultDate);
		validation(WarningLabel);
		stepActivate();
		setFocus();
	}

	/**
	 * Date panel that lets you compare the value to the current date.
	 * 
	 * @param containingPanel
	 * @param defaultDate
	 * @param required
	 * @param WarningLabel
	 * @param beforeOrAfter
	 */
	public XDatePanel(JPanel containingPanel, Calendar defaultDate, boolean required, JLabel WarningLabel,
			String beforeOrAfter) {
		this.dateContainer = containingPanel;
		this.required = required;
		this.warningLabel = WarningLabel;
		this.beforeOrAfter = beforeOrAfter.toUpperCase();
		stepInitialise();
		init(defaultDate);
		validation(WarningLabel);
		stepActivate();
		setFocus();
	}

	/**
	 * Date panel that lets you compare the value to another panel.
	 * 
	 * @param containingPanel
	 * @param defaultDate
	 * @param required
	 * @param WarningLabel
	 * @param beforeOrAfter
	 * @param toCompare
	 * @param beforeName
	 * @param afterName
	 */
	public XDatePanel(JPanel containingPanel, Calendar defaultDate, boolean required, JLabel WarningLabel,
			String beforeOrAfter, XDatePanel toCompare, String beforeName, String afterName) {
		this.dateContainer = containingPanel;
		this.required = required;
		this.warningLabel = WarningLabel;
		this.beforeOrAfter = beforeOrAfter.toUpperCase();
		this.toCompare = toCompare;
		this.beforeName = beforeName;
		this.afterName = afterName;
		stepInitialise();
		init(defaultDate);
		validation(WarningLabel);
		stepActivate();
		setFocus();
	}

	public XDatePanel(JPanel containingPanel, Calendar defaultDate, boolean required, JLabel WarningLabel,
			JTextField PGTextField, JLabel lblIPGuardian) {
		this.dateContainer = containingPanel;
		this.required = required;
		this.warningLabel = WarningLabel;
		this.PGTextField = PGTextField;
		this.lblIPGuardian = lblIPGuardian;
		PGRequired = true;
		stepInitialise();
		init(defaultDate);
		validation(WarningLabel);
		stepActivate();
		setFocus();
	}

	public XDatePanel(JPanel containingPanel, Calendar defaultDate, boolean required, JLabel WarningLabel,
			JTextField PGTextField, JLabel lblIPGuardian, boolean notFuture) {
		this.dateContainer = containingPanel;
		this.required = required;
		this.warningLabel = WarningLabel;
		this.PGTextField = PGTextField;
		this.lblIPGuardian = lblIPGuardian;
		this.notFuture = notFuture;
		PGRequired = true;
		stepInitialise();
		init(defaultDate);
		validation(WarningLabel);
		stepActivate();
		setFocus();
	}

	/**
	 * Create panel using a reference to the parent. The date defaults to the
	 * current date
	 * 
	 * @param containingPanel
	 */
	public XDatePanel(JPanel containingPanel) {
		this(containingPanel, Calendar.getInstance());
		setFocus();
	}

	private void init(Calendar calendar) {
		setLayout(gridBagLayout1);
		MDefaultPullDownConstraints mdefaultpulldownconstraints = new MDefaultPullDownConstraints();
		mdefaultpulldownconstraints.firstDay = 2;
		mdefaultpulldownconstraints.changerStyle = 4;
		mdefaultpulldownconstraints.hasShadow = true;
		mdefaultpulldownconstraints.selectionClickCount = 1;
		entryField.setConstraints(mdefaultpulldownconstraints);
		entryField.setDateFormatter(new MSimpleDateFormat(XDateFormat.simpleDateFormat));
		entryField.setEditable(true);
		entryField.setToolTipText(XHIBITConstant.getResource(datePanelResources, "CalendarComponent"));
		Dimension dM = new Dimension(100, XHIBITConstant.getLineHeight());
		Dimension dP = new Dimension(150, XHIBITConstant.getLineHeight());
		Dimension dB = new Dimension(150, XHIBITConstant.getLineHeight() + 2);

		Calendar maxCal = Calendar.getInstance();
		maxCal.set(3000, Calendar.DECEMBER, 31);
		entryField.setMaximum(maxCal.getTime());

		Calendar minCal = Calendar.getInstance();
		minCal.set(MIN_YEAR, Calendar.JANUARY, 1);
		entryField.setMinimum(minCal.getTime());

		this.setPreferredSize(dB);
		entryField.setPreferredSize(dP);
		entryField.setMinimumSize(dP);
		setDate(calendar);

		entryField.addMChangeListener(new MChangeListener() {
			public void valueChanged(MChangeEvent e) {
				entryField_changed(e);
			}
		});

		this.add(entryField, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Create calendar with today's date and no time.
	 * 
	 * @return calendar
	 */
	protected Calendar getCalendarNoTime() {
		final Calendar calendar = Calendar.getInstance();
		clearTimeComponents(calendar);
		return calendar;
	}

	/**
	 * Create calendar from supplied date with no time.
	 * 
	 * @param date
	 *            date
	 * @return calendar
	 */
	protected Calendar getCalendarNoTime(Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		clearTimeComponents(calendar);
		return calendar;
	}

	/**
	 * Clear time from supplied calendar.
	 * 
	 * @param calendar
	 *            calendar
	 */
	protected void clearTimeComponents(Calendar calendar) {
		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public void validation(JLabel Label) {
		// Needed due to changes made by GridBag requiring .setVisible never to
		// be used as causes resize issues
		if (gridBagLayout) {
			Label.setVisible(true);
			Label.setText(" ");
		}

		entryField.addMFieldListener(new MFieldListener() {
			@Override
			public void fieldEntered(FocusEvent event) {
			}

			// check whether entry is valid
			@Override
			public void fieldExited(FocusEvent event) {
				parseAndConvertDateString();
				validateDate();
			}
		});
	}
	
	public void attachDateParser() {
		entryField.addMFieldListener(new MFieldListener() {
			@Override
			public void fieldEntered(FocusEvent event) {
			}

			// check whether entry is valid
			@Override
			public void fieldExited(FocusEvent event) {
				parseAndConvertDateString();
			}
		});
	}

	public void validateDate() {
		String date = entryField.getText();
		// Default position is that date is valid
		setError(false);

		// Confirm date is empty and not required, or is a valid date
		if (isDateValidate()) {
			// If date is entered (i.e. not empty and valid format),
			// perform the optional validation of the entered date
			if (date.length() > 0) {
				try {
					Date newDate = XDateFormat.parseAsDate(date);

					// check if value is before or after allowed value
					if (beforeOrAfter != null) {
						if (beforeOrAfter.equalsIgnoreCase("before")) {
							if (toCompare != null) {
								if (checkDateAfterToCompare()) {
									if (warningLabel != null) {
										if (customWarning.isEmpty()) {
											warningLabel.setText(
													beforeName + " must be on the same day or before " + afterName);
										} else {
											warningLabel.setText(customWarning);
										}
										setError(true);
									}
								} else {
									if(!hasSecondaryError){
										if (gridBagLayout)
											warningLabel.setText(" ");
										warningLabel.setVisible(gridBagLayout);
									}
								}
							} else if (newDate.after(getCalendarNoTime().getTime())) {
								if (warningLabel != null) {
									warningLabel.setText("Date cannot be in the future");
									setError(true);
								} else {
									if(!hasSecondaryError){
										if (gridBagLayout)
											warningLabel.setText(" ");
										warningLabel.setVisible(gridBagLayout);
									}
								}
							} else {
								if(!hasSecondaryError){
									if (gridBagLayout)
										warningLabel.setText(" ");
									warningLabel.setVisible(gridBagLayout);
								}
							}
						} else if (beforeOrAfter.equalsIgnoreCase("after")) {
							if (toCompare != null) {
								if (checkDateBeforeToCompare()) {
									if (warningLabel != null) {
										if (customWarning.isEmpty()) {
											warningLabel.setText(
													afterName + " must be on the same day or after " + beforeName);
										} else {
											warningLabel.setText(customWarning);
										}
										setError(true);
									}
								} else {
									if(!hasSecondaryError){
										if (gridBagLayout)
											warningLabel.setText(" ");
										warningLabel.setVisible(gridBagLayout);
									}
								}
							} else if (newDate.before(getCalendarNoTime().getTime())) {
								if (warningLabel != null) {
									warningLabel.setText("Date cannot be in past");
									setError(true);
								}
							} else {
								if(!hasSecondaryError){
									if (gridBagLayout)
										warningLabel.setText(" ");
									warningLabel.setVisible(gridBagLayout);
								}
							}
						}
					} else {
						if(!hasSecondaryError){
							if (gridBagLayout)
								warningLabel.setText(" ");
							warningLabel.setVisible(gridBagLayout);
						}
					}

					if (PGRequired == true) {
						if (calculatePGRequired(date) == false) {
							if (PGTextField != null) {
								PGTextField.setEnabled(true);
								under18 = true;
								setError(true);
							}
						} else {
							if (PGTextField != null) {
								PGTextField.setEnabled(false);
								under18 = false;
							}
						}
						if (notFuture && newDate.after(getCalendarNoTime().getTime())) {
							if (warningLabel != null) {
								warningLabel.setText("Date cannot be in the future");
								setError(true);
							}
						} else {
							if(!hasSecondaryError){
								if (gridBagLayout)
									warningLabel.setText(" ");
								warningLabel.setVisible(gridBagLayout);
							}
						}
					} else {
						under18 = false;
						if (PGTextField != null) {
							PGTextField.setEnabled(false);
							if (lblIPGuardian != null) {
								if (gridBagLayout)
									lblIPGuardian.setText(" ");
								lblIPGuardian.setVisible(gridBagLayout);
							}
						}
					}
				} catch (ParseException e) {
					if (warningLabel != null) {
						warningLabel.setText("Invalid date");
						setError(true);
					}
				}
			} else if( !required && date.length()==0 && gridBagLayout) {
				if(!hasSecondaryError){
					warningLabel.setText(" ");
				}
			} 
			//New Check needed as part of case Create (for cross tab validation)
			if(hasSecondaryError) {
				warningLabel.setText(secondaryErrorText);
				setError(true);
			}
		} else {
			if (required && (date.length() == 0 || date == null)) {
				if (warningLabel != null) {
					warningLabel.setText("Mandatory Field");
					setError(true);
				}
			} else {
				if (warningLabel != null) {
					warningLabel.setText("Invalid date");
					setError(true);
				}
			}
		}
	}

	public boolean calculatePGRequired(String dateString) {
		try {
			Date date = XDateFormat.parseAsDate(dateString);
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 18);
			return calendar.getTime().after(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void setEnabledAndFocusable(Boolean tf) {
		if (tf == true) {
			this.setEnabled(true);
			this.setFocusable(true);
		} else {
			this.setEnabled(false);
			this.setFocusable(false);
		}
	}

	public void clear() {
		init(null);
	}

	public void stepInitialise() {
		datePanelResources = XHIBITConstant.getResourceBundle(XhibitBundles.UtilResources);
	}

	public void stepActivate() {
	}

	public void stepUpdateViewState() throws CSRecoverableException {
		if (dateContainer instanceof XPanel) {
			((XPanel) dateContainer).stepUpdateViewState();
		}

		// Check if ChildOfXPanel
		if (dateContainer instanceof ChildOfXPanel) {
			((ChildOfXPanel) dateContainer).stepUpdateViewState();
		}
	}

	// Validates the date but doesnt check if it's mandatory as this is to be
	// called from a separate date panel
	public void crossValidateDate() {
		boolean originalState = this.required;
		this.required = false;
		validateDate();
		this.required = originalState;
	}

	/**
	 * Checks that a valid date has been entered if the field is editable and
	 * enabled. If the field is not required then it can be left blank.
	 * 
	 * @return true if the entry field:<br/>
	 *         1. is not editable, or<br/>
	 *         2. is disabled, or<br/>
	 *         3. does not contain text and is not required, or<br/>
	 *         4. contains a valid date.
	 */
	public boolean isDateValidate() {
		if (entryField.isEditable() || entryField.isEnabled()) {
			if (entryField.getText().length() == 0 && !required) {
				return true;
			} else {
				Date date = null;
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					date = sdf.parse(getText());
					if (!getText().equals(sdf.format(date))) {
						date = null;
					}
				} catch (ParseException ex) {
					return false;
				}
				if (date == null) {
					return false;
				} else {
					try {
						Calendar cal = XDateFormat.parse(getText());
						if (cal.get(Calendar.YEAR) > MIN_YEAR && cal.get(Calendar.YEAR) <= MAX_YEAR) {
							return true;
						} else {
							return false;
						}
					} catch (ParseException e) {
						return false;
					}
				}
			}
		} else {
			return false;
		}

	}

	/**
	 * XPanel implementaion of life cycle method, called to validate the panel.
	 * Checks if the date field is valid and if not, gives the field the focus
	 * and display the appropriate error message. The parent panel can include a
	 * call to this method in its own stepValidate method.
	 * 
	 * @throws CSValidationException
	 *             if date field is invalid.
	 */
	public void stepValidate() throws CSValidationException {
		if (!isDateValidate()) {
			throw new CSValidationException(dateError, new String[] { XDateFormat.simpleDateFormat },
					" year not integer");
		}
	}

	public void stepDeactivate() {
	}

	public void stepDeinitialise(boolean save) {
	}

	/**
	 * Returns the date in the field as a Calendar. Only a valid date will be
	 * returned
	 * 
	 * @return
	 * @throws CSValidationException
	 */
	public Calendar getDate() throws CSValidationException {
		try {
			Calendar c = null;
			stepValidate();

			// Check to see if field is not required.
			// If not required and no date then return null.
			if (getText().length() <= 0)
				return c;

			c = Calendar.getInstance();
			c.setTime(entryField.getValue());
			return c;
		} catch (ParseException ex) {
			log.debug(ex);
			throw new CSValidationException(dateError, new String[] { XDateFormat.simpleDateFormat },
					"Date format invalid");
		}
	}

	/**
	 * Return the text in the field. This does not guarantee the validity of the
	 * date
	 * 
	 * @return
	 */
	public String getText() {
		return entryField.getText();
	}

	/**
	 * Set the text in the field.
	 */
	public void setText(String date) {
		entryField.setText(date);
	}

	/**
	 * Set a new date for the field
	 * 
	 * @param newDate
	 */
	public void setDate(Calendar newDate) {
		if (newDate == null) {
			Date d = null;
			setDate(d);
		} else {
			setDate(newDate.getTime());
		}
	}

	/**
	 * Set a new date for the field
	 * 
	 * @param newDate
	 */
	public void setDate(Date newDate) {
		entryField.setValue(newDate);
	}

	/**
	 * Set if the user is required to complete the field Defaults to true
	 * 
	 * @param newValue
	 */
	public void setRequired(boolean newValue) {
		required = newValue;
	}

	/**
	 * Set whether the display of the field is enabled. Defaults to true
	 * 
	 * @param newValue
	 */
	public void setDateEnabled(boolean newValue) {
		entryField.setEnabled(newValue);

		if (newValue == false) {
			this.getDateComponent().setToolTipText(null);
		} else {
			// Setup tool tip text
			datePanelResources = XHIBITConstant.getResourceBundle(XhibitBundles.UtilResources);
			this.getDateComponent().setToolTipText(XHIBITConstant.getResource(datePanelResources, "CalendarComponent"));
		}
	}

	/**
	 * Set whether the field can be editted Defaults to true
	 * 
	 * @param newValue
	 */
	public void setDateEditable(boolean newValue) {
		entryField.setEditable(newValue);
	}

	/**
	 * Set the width of the date panel. This requires updating
	 * the dimension objects on the panel and the entry field
	 * which are initially set on construction of the control.
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
        Dimension panelDm = new Dimension(width, XHIBITConstant.getLineHeight() + 2);
        Dimension componentDm = new Dimension(width, XHIBITConstant.getLineHeight());
        this.setPreferredSize(panelDm);
        entryField.setPreferredSize(componentDm);
        entryField.setMinimumSize(componentDm);
	}

	/**
	 * Check if the field is required and whether it has been populated.
	 * 
	 * @return true if there is a valid date. Also returns true if the field is
	 *         not a required field.
	 */
	public boolean isMandatoryFieldsCompleted() {
		if (required) {
			boolean fieldCompleted = false;
			if (isDateValidate()) {
				fieldCompleted = true;
			}
			return fieldCompleted;
		} else {
			return true;
		}
	}

	/**
	 * Allows access to the component for more advanced programming
	 * 
	 * @return
	 */
	public MDateEntryField getDateComponent() {
		return entryField;
	}

	/**
	 * Install the default InputVerifier, currently an XDateVerifier, on this
	 * XDatePanel
	 */
	public void installDefaultInputVerifier() {
		entryField.setInputVerifier(new XDateVerifier(this));
	}

	/**
	 * Sets the give InputVerifier on this XDatePanel
	 * 
	 * @param inputVerifier
	 *            InputVerifier to install on this XDatePanel
	 */
	public void setInputVerifier(InputVerifier inputVerifier) {
		entryField.setInputVerifier(inputVerifier);
	}

	void entryField_changed(MChangeEvent e) {
		if (e.getType() == MChangeEvent.PULLDOWN_OPENED || e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
			// If the chooser is being opened or closed the date will not
			// have
			// changed.
			return;
		}
		try {
			stepUpdateViewState();
		} catch (CSRecoverableException ex) {
			XHIBITConstant.handleError(ex);
		}
	}

	public void setCustomWarning(String customWarning) {
		this.customWarning = customWarning;
	}

	public String getCustomWarning() {
		return customWarning;
	}

	/**
	 * Set the error flag
	 * 
	 * @param error
	 *            The error to set.
	 */
	public void setError(boolean error) {
		this.error = error;
		if (warningLabel != null) {
			if (!gridBagLayout)
				warningLabel.setVisible(error);
		}
	}

	/**
	 * @return true if the error flag has been set
	 */
	public boolean hasError() {
		return error;
	}

	public boolean getUnder18() {
		return under18;
	}

	public void setEnabled(boolean enabled) {
		entryField.setEnabled(enabled);

	}

	public boolean isEnabled() {
		return entryField.isEnabled();
	}

	public boolean checkDateBeforeToCompare() {
		if (toCompare.getText() != null) {
			try {
				if (this.getDate().before(toCompare.getDate())) {
					return true;
				} else {
					return false;
				}
			} catch (CSValidationException e) {
				return false;
			}
		} else {
			return true;
		}
	}

	public boolean checkDateAfterToCompare() {
		if (toCompare.getText() != null) {
			try {
				if (this.getDate().after(toCompare.getDate())) {
					return true;
				} else {
					return false;
				}
			} catch (CSValidationException e) {
				return false;
			}
		} else {
			return true;
		}
	}

	public void setError() {
		if (warningLabel != null) {
			warningLabel.setText("Invalid date");
			setError(true);
		}
	}

	public void clearError() {
		if (warningLabel != null) {
			if (gridBagLayout)
				warningLabel.setText(" ");
			warningLabel.setVisible(gridBagLayout);
			setError(false);
		}
	}

	public void setFocus() {
		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				entryField.grabFocus();
			}
		});

	}

	public void setToCompare(XDatePanel toCompare) {
		this.toCompare = toCompare;
	}

	public void setBeforeOrAfter(String beforeOrAfter) {
		this.beforeOrAfter = beforeOrAfter;
	}

	/**
	 * Get the value in timestamp format
	 * 
	 * @return Timestamp
	 */
	public Timestamp getTimestamp() throws CSRecoverableException {
		Timestamp timestamp = null;
		Calendar cal = getDate();
		if (cal != null) {
			timestamp = new Timestamp(cal.getTimeInMillis());
		}
		return timestamp;
	}

	/**
	 * Set a new timestamp for the field
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(Timestamp timestamp) {
		Calendar cal = null;
		if (timestamp != null) {
			cal = Calendar.getInstance();
			cal.setTimeInMillis(timestamp.getTime());
		}
		setDate(cal);
	}

	public void setGridBagLayout(boolean gridBagLayout) {
		this.gridBagLayout = gridBagLayout;
		warningLabel.setVisible(gridBagLayout);
	}

	public MDateEntryField getEntryField() {
		return entryField;
	}
	
	public void setSecondaryError(Boolean hasSecondaryError) {
		this.hasSecondaryError = hasSecondaryError;
	}
	
	public void setSecondaryErrorText(String secondaryErrorText) {
		this.secondaryErrorText = secondaryErrorText;
	}

	public void parseAndConvertDateString() {
		String date = entryField.getText();
		if(date.contains("/")) {
			date = date.replace("/", "-");
		}
		
		SimpleDateFormat standardDateFormat = new SimpleDateFormat(XDateFormat.simpleDateFormat);
		standardDateFormat.setLenient(true);
		
		Date trueDate = new Date();
		try {
			if(date!=null && !date.equals("")) {
				trueDate = standardDateFormat.parse(date);
				Calendar cal = Calendar.getInstance();
		        cal.setTime(trueDate);
		        entryField.setValue(trueDate);
			}
		} catch (ParseException e) {
			log.debug("Unparseable date:" + date +  ", no need to throw error as 'invalid entry' appears!");
		}
	}
}
