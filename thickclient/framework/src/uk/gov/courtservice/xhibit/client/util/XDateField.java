package uk.gov.courtservice.xhibit.client.util;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import mseries.Calendar.MDefaultPullDownConstraints;
import mseries.ui.MChangeEvent;
import mseries.ui.MDateEntryField;
import mseries.ui.MDateFormat;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: XDateField
 * </p>
 * <p>
 * Description: Field for entering dates
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell
 * @version $Revision: 1.8 $
 */
public class XDateField extends MDateEntryField implements MDateFormat {
	/**
	 * The name of the action fired when the value has been updated
	 */
	public static final String UPDATE_ACTION_COMMAND = "Update";

	/**
	 * The name of the action fired when the value has been updated
	 */
	public static final int UPDATE_ACTION_ID = ActionEvent.RESERVED_ID_MAX + 1;

	/**
	 * The color used to render the foreground if the date is invalid
	 */
	public static final Color ERROR_COLOR = new Color(244, 0, 0);

	/**
	 * The default format for displaying and entering dates
	 */
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

	private ResourceBundle dateFieldResources;

	public boolean under18 = false;

	private JPanel dateContainer;

	// Boolean for mandatory field
	private boolean isMandatory;
	// Error handling
	private boolean error;

	private JLabel WarningLabel = null;

	private JTextField PGTextField = null;
	private JLabel lblIPGuardian = null;

	private static final Logger log = CSServices.getLogger(XDateField.class);
	private static final int MIN_YEAR = 1900;
	private static final int MAX_YEAR = 9999;
	private Boolean PGRequired = false;
	private String beforeOrAfter = null;
	private XDateField toCompare = null;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	final MDateEntryField entryField = new MDateEntryField(10);
	private boolean required = true;

	/**
	 * The constraints configure the behaviour of the pull down editor
	 */
	private static final MDefaultPullDownConstraints DATE_CONSTRAINTS = new MDefaultPullDownConstraints();
	static {
		DATE_CONSTRAINTS.firstDay = Calendar.MONDAY;
		DATE_CONSTRAINTS.changerStyle = mseries.Calendar.MDateChanger.BUTTON;
		// DATE_CONSTRAINTS.changerStyle=MDateChanger.SPINNER;
		// DATE_CONSTRAINTS.changerStyle=MDateChanger.SCROLLBAR;
		DATE_CONSTRAINTS.hasShadow = true;
		// DATE_CONSTRAINTS.selectionEventsEnabled=false;
		// DATE_CONSTRAINTS.todayForeground=Color.red;
		// DATE_CONSTRAINTS.todayBackground=Color.green;
		// DATE_CONSTRAINTS.imageFile="d:\\temp\\fineview.jpg";
		// DATE_CONSTRAINTS.background=Color.red;
	}

	/**
	 * Cache the foreground color used to restor color when value becomes valid
	 */
	private final Color foregroundCache = getForeground();

	/**
	 * The formater used for dates
	 */
	private final SimpleDateFormat dateFormatter;

	public XDateField() {
		this(new Date(), DEFAULT_DATE_FORMAT);
	}

	public XDateField(String format) {
		this(new Date(), format);
	}

	public XDateField(Date date, String format) {
		// Configure
		dateFormatter = new SimpleDateFormat(format);
		dateFormatter.setLenient(false);
		setNullOnEmpty(true);
		setConstraints(DATE_CONSTRAINTS);
		setDateFormatter(this);
		setValue(date);
		display.setColumns(format.length());
		// Verifier and Listener
		XDateFieldHelper helper = new XDateFieldHelper();
		display.setInputVerifier(helper);
		display.getDocument().addDocumentListener(helper);
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

	public void stepUpdateViewState() throws CSRecoverableException {
		if (dateContainer instanceof XPanel) {
			((XPanel) dateContainer).stepUpdateViewState();
		}
		// Check if ChildOfXPanel
		if (dateContainer instanceof ChildOfXPanel) {
			((ChildOfXPanel) dateContainer).stepUpdateViewState();
		}
	}

	// MDateFormat Implementation
	public StringBuffer format(Date d, StringBuffer appendTo, FieldPosition pos) {
		return dateFormatter.format(d, appendTo, pos);
	}

	// MDateFormat Implementation
	public String format(Date d) {
		return dateFormatter.format(d);
	}

	// MDateFormat Implementation
	public Date parse(String s) throws ParseException {
		return dateFormatter.parse(s);
	}

	/**
	 * Set the tooltip tex
	 */
	public void setToolTipText(String tooltip) {
		super.setToolTipText(tooltip);
		display.setToolTipText(tooltip);
	}

	/**
	 * Return true if the we have a date or the field is empty
	 */
	public boolean isValid() {
		try {
			if (display == null) {
				initializeMe();
				return true;
			}
			getValue();
			return false;
		} catch (ParseException pe) {
			return false;
		}
	}

	public boolean isDateFormatValid() {
		try {
			getValue();
			return true;
		} catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * Overridden as getValue on MDateField calls SetValue!
	 * 
	 * @return the date value
	 * @exception ParseException
	 *                if it is not a valid date
	 */
	public Date getValue() throws ParseException {
		// return new Date();
		String text = "";
		// if (display != null) {
		text = getText();

		if (text.length() == 0 && getNullOnEmpty()) {
			return null;
		}
		return getDateFormatter().parse(text);
		// return getDateFormatter().parse("23-Apr-2014");
		/*
		 * } else { //setValue(new Date()); return new Date(); }
		 */
	}

	/**
	 * Add the listener
	 */
	public void addActionListener(ActionListener listener) {
		listenerList.add(ActionListener.class, listener);
	}

	/**
	 * Remove the listener
	 */
	public void removeActionListener(ActionListener listener) {
		listenerList.remove(ActionListener.class, listener);
	}

	/**
	 * Notifies all interested listeners that the command has occured
	 */
	protected void fireActionPerformed(int id, String command) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			ActionEvent event = null;
			if (listeners[i] == ActionListener.class) {
				// Lazily create the event:
				if (event == null) {
					event = new ActionEvent(this, id, command);
				}
				((ActionListener) listeners[i + 1]).actionPerformed(event);
			}
		}
	}

	/**
	 * Class to implement all the listeners
	 */
	private class XDateFieldHelper extends InputVerifier implements DocumentListener {
		// InputVerifier implementation
		public boolean verify(JComponent component) {
			return isDateFormatValid();
		}

		// DocumentListener implemntation
		public void insertUpdate(DocumentEvent e) {
			update();
		}

		// DocumentListener implemntation
		public void removeUpdate(DocumentEvent e) {
			update();
		}

		// DocumentListener implemntation
		public void changedUpdate(DocumentEvent e) {
			update();
		}

		private void update() {
			// Ok so isValid will always return false now as of JDK1.6 in order
			// not to invalidate the component
			// Therefore will do a further check here to see if date is valid
			boolean valid1 = isValid();
			boolean isDateFormatValid = isDateFormatValid();
			display.setForeground(isDateFormatValid() ? foregroundCache : ERROR_COLOR);
			fireActionPerformed(UPDATE_ACTION_ID, UPDATE_ACTION_COMMAND);
		}
	}

	/**
	 * Used to test the XDateField
	 * 
	 * @param args
	 *            the command line arguments, these are not used
	 */
	public static void main(String[] args) throws Exception {
		// Set look and feel to system look and feel.
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		// Create the test frame
		JFrame frame = createTestFrame();

		// Pack. Center. Show!
		frame.pack();
		Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
		frame.setLocation(bounds.x + ((bounds.width - frame.getWidth()) / 2),
				bounds.y + ((bounds.height - frame.getHeight()) / 2));
		frame.setVisible(true);
	}

	private static JFrame createTestFrame() {
		JFrame frame = new JFrame("Test: " + XDateField.class.getName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		XDateField field = new XDateField();
		frame.getContentPane().add(field);
		return frame;
	}

}
