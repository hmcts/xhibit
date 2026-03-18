/*
 *   Copyright (c) 2001 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
 *
 *   The author makes no representations or warranties about the suitability of the
 *   software, either express or implied, including but not limited to the
 *   implied warranties of merchantability, fitness for a particular
 *   purpose, or non-infringement. The author shall not be liable for any damages
 *   suffered by licensee as a result of using, modifying or distributing
 *   this software or its derivatives.
 *
 *   The author requests that he be notified of any application, applet, or other binary that
 *   makes use of this code and that some acknowedgement is given. Comments, questions and
 *   requests for change will be welcomed.
 */
package mseries.ui;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Date;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TextUI;

import mseries.Calendar.MDateSelectorConstraints;
import mseries.Calendar.MDefaultPullDownConstraints;
import mseries.Calendar.MFieldListener;

/**
 * Date entry component which looks like a combobox, when the button is pushed a
 * calendar drops down for selection of the date.
 */
public class MDateEntryField extends JComponent implements FocusListener {
    private static final String uiClassID = "DateEntryUI";

    protected MDateField display;

    private boolean todayButton = false;

    private boolean hasBorder = true;

    private boolean nullOnEmpty = false;

    protected EventListenerList listenerList = new EventListenerList();

    MDateSelectorConstraints panelConstraints = new MDefaultPullDownConstraints();

    /**
     * Creates a MDateEntryField with size 6 and a button
     */
    public MDateEntryField() {
        this(6);
    }

    /**
     * Creates a MDateEntryField
     * 
     * @param size
     *            the size of the display part of the component
     */
    public MDateEntryField(int size) {
        super();
        display = new MDateField(size) {
            public void setUI(TextUI ui) {
                super.setUI(ui);
                setBorder(null);
            }
        };
        setDateFormatter(new ShortFormatter());
        setValue(new Date());
        display.addFocusListener(this);
        updateUI();
    }
    
    public void initializeMe() {
        display = new MDateField(6) {
            public void setUI(TextUI ui) {
                super.setUI(ui);
                setBorder(null);
            }
        };
        setDateFormatter(new ShortFormatter());
        setValue(new Date());
        display.addFocusListener(this);
        updateUI();
    }

    /**
     * Returns the textfield that handles the date editing. Used by the
     * UIDelegates
     */
    public MDateField getDisplay() {
        return display;
    }

    public void updateUI() {
        setUI(registerUIDelegate());
    }

    /**
     * Used to automatically install the UIDelagate for Windows & Metal Look &
     * Feels. Any other Look & Feel will get the basic look and feel unless a
     * UIDelegate is provided and set in the look and feel class.
     * 
     * @see #getUIClassID
     */
    protected ComponentUI registerUIDelegate() {
        ComponentUI compUI = (ComponentUI) UIManager.get(uiClassID);
        if (compUI == null) {
            String uiDelegateClassName = "mseries.plaf.basic.BasicDateEntryUI";
            String lafName = UIManager.getLookAndFeel().getID();
            uiDelegateClassName = "mseries.plaf." + lafName + "." + lafName + "DateEntryUI";

            try {
                compUI = (ComponentUI) (Class.forName(uiDelegateClassName)).newInstance();
            } catch (Exception e) {
                uiDelegateClassName = "mseries.plaf.basic.BasicDateEntryUI";
                try {
                    System.out.println(e);
                    compUI = (ComponentUI) (Class.forName(uiDelegateClassName)).newInstance();
                } catch (Exception e1) {
                    System.out.println(e1);
                }
            }
        }
        return compUI;
    }

    /**
     * This method gives the UI Manager a constant to use to look up in the UI
     * Defaults table to find the class name of the UI Delegate for the
     * installed L&F.
     * 
     * @return string "DateEntryUI"
     */
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * @return the nullOnEmpty attribute
     */
    public boolean getNullOnEmpty() {
        return this.nullOnEmpty;
    }

    /**
     * If set to true the getValue method will return null when the text field
     * is empty, otherwise a ParseException is thrown since "" is not a valid
     * date. Default is false.
     * 
     * @param nullOnEmpty
     *            set to true if a null is required when the textfield is empty
     */
    public void setNullOnEmpty(boolean nullOnEmpty) {
        this.nullOnEmpty = nullOnEmpty;
    }

    /**
     * @return the current value of the field The value of the entry field, no
     *         parsing is performed, use getValue() to get a Date
     */
    public String getText() {
        return display.getText();
    }

    /**
     * This method does not do anything, the implementation is empty and it only
     * present to make the component a JavaBean
     */
    public void setText(String text) {
    }

    /**
     * @return the current value of the field
     * @exception ParseException
     *                if it is not a valid date
     */
    public Date getValue() throws ParseException {
        if (getText().trim().equals("") && nullOnEmpty) {
            return null;
        }
        return display.getValue();
    }

    /**
     * Sets the current value in the field, parsed using the current date
     * formatter
     * 
     * @param newValue
     *            the new value
     */
    public void setValue(Date newValue) {
        display.setValue(newValue);
    }

    /**
     * Tells the component what date formatter to use for parsing and formatting
     * the user input.
     * 
     * @param df
     *            the date formatter
     */
    public void setDateFormatter(MDateFormat df) {
        display.setDateFormatter(df);
    }

    public MDateFormat getDateFormatter() {
        return display.getDateFormatter();
    }

    /**
     * Sets the earliest value that may be selected for this field when the poup
     * calendar in invoked. The default is 1 January 1900
     * 
     * @param date
     *            the ealiest date
     */
    public void setMinimum(Date date) {
        display.setMinimum(date);
    }

    /**
     * Sets the latest value that may be selected for this field when the poup
     * calendar in invoked. The default is 31 December 2037
     * 
     * @param date
     *            the latest date
     */
    public void setMaximum(Date date) {
        display.setMaximum(date);
    }

    public Date getMinimum() {
        return display.getMinimum();
    }

    public Date getMaximum() {
        return display.getMaximum();
    }

    /**
     * @return true if a border will be drawn
     */
    public boolean hasBorder() {
        return hasBorder;
    }

    /**
     * causes a border to be drawn around the component
     * 
     * @param border
     *            true is a border is to be drawn (default=true)
     */
    public void drawBorder(boolean border) {
        hasBorder = border;
    }

    /**
     * Returns the font that the editor part (the textfield) uses
     * 
     * @return the font for the date display
     */
    public Font getFont() {
        return display.getFont();
    }

    public void setFont(Font font) {
        display.setFont(font);
    }

    /**
     * @return the date fields editable property
     */
    public boolean isEnabled() {
        return display.isEnabled();
    }

    /**
     * Sets the date entry field editable or not, the button can still be used
     * to allow date selection.
     * 
     * @param editable
     *            <I>true</I> if the field can be typed into
     */
    public void setEditable(boolean editable) {
        display.setEditable(editable);
    }

    /**
     * @return the date fields editable property
     */
    public boolean isEditable() {
        return display.isEditable();
    }

    public void setInputVerifier(InputVerifier inputVerifier) {
        display.setInputVerifier(inputVerifier);
    }

    public InputVerifier getInputVerifier() {
        return display.getInputVerifier();
    }

    /**
     * Sets the constraints object that contains the parameters used to
     * configure the pull down calendar. Constraints include the visual aspects
     * of the pull down such as the colours, including the colours of the month
     * and date changer, if the look and feel permits it.
     * 
     * @param c
     *            the constraints object
     * @see #getConstraints
     */
    public void setConstraints(MDateSelectorConstraints c) {
        this.panelConstraints = c;
    }

    /**
     * Gets the constraints object that contains the parameters used to
     * configure the pull down calendar
     * 
     * @see #getConstraints
     */
    public MDateSelectorConstraints getConstraints() {
        return this.panelConstraints;
    }

    public void setToolTipText(String text) {
        display.setToolTipText(text);
    }

    public String getToolTipText() {
        return display.getToolTipText();
    }

    /**
     * This method is public as an implementation side effect, <b>do not
     * override or call it directly</b>
     */
    public void opened() {
        display.removeFocusListener(this);

    }

    /**
     * This method is public as an implementation side effect, <b>do not
     * override or call it directly</b>
     */
    public void closed() {
        display.addFocusListener(this);

    }

    /*
     * These private variables are concerned with managing the focus on the
     * compound component.
     */
    private int last = FocusEvent.FOCUS_LOST;

    /**
     * This method is public as an implementation side effect, <b>do not
     * override or call it directly</b>
     */
    public void focusLost(FocusEvent e) {
        notifyListeners(e);
    }

    /**
     * Set focus on the receiving component if isRequestFocusEnabled returns
     * true
     */
    public void requestFocus() {
        display.requestFocus();
    }

    /**
     * This method is public as an implementation side effect, <b>do not
     * override or call it directly</b>
     */
    public void focusGained(FocusEvent e) {
        notifyListeners(e);
    }

    /**
     * Registers the listeners of the field changes. The event is fired when the
     * component gets and loses focus.
     * 
     * @param listener -
     *            MFieldListener
     * @see #removeMFieldListener
     */
    public void addMFieldListener(MFieldListener listener) {
        listenerList.add(MFieldListener.class, listener);
    }

    /**
     * Removes the listener from the registered list of listeners
     * 
     * @param listener -
     *            MFieldListener
     */
    public void removeMFieldListener(MFieldListener listener) {
        listenerList.remove(MFieldListener.class, listener);
    }

    public void notifyListeners(FocusEvent e) {
        int type = e.getID();

        if (last != type) {

            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == MFieldListener.class) {
                    if (type == FocusEvent.FOCUS_GAINED) {
                        ((MFieldListener) listeners[i + 1]).fieldEntered(new FocusEvent(this, type, e.isTemporary()));
                    } else {
                        ((MFieldListener) listeners[i + 1]).fieldExited(new FocusEvent(this, type, e.isTemporary()));
                    }
                }
            }
            last = type;
        }
    }

    /**
     * Registers the listeners of the field changes. Fired when the user changes
     * a value in the dropped down calendar
     * 
     * @param listener -
     *            MMonthListener
     */
    public void addMChangeListener(MChangeListener listener) {
        listenerList.add(MChangeListener.class, listener);
    }

    /**
     * Removes the listener from the registered list of listeners
     * 
     * @param listener -
     *            MMonthListener
     */
    public void removeMChangeListener(MChangeListener listener) {
        listenerList.remove(MChangeListener.class, listener);
    }

    /**
     * Causes the MChangeEvents to be fired. This is called by the L&F Delegate
     * and should not be overloaded or called directly.
     */
    public void notifyListeners(int type) {
        // Pass these events on to the registered listener
        MChangeEvent event;
        Date date;
        try {
            date = getValue();
        } catch (ParseException e) {
            date = null;
        }
        event = new MChangeEvent(this, date, type);

        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MChangeListener.class) {
                ((MChangeListener) listeners[i + 1]).valueChanged(event);
            }
        }
    }

    /**
     * The pull down can be configured with a button at the bottom to quickly
     * select the current date. The label is translated using the ResourceBundle
     * given in the contraints object passed in the setConstraints() method (or
     * the default which is false), by default the popup closes when the button
     * is pushed, this behaviour can be changed by setting the closeOnToday
     * attribute.
     * 
     * @param show
     *            set this to true if the button is required.
     * @see mseries.Calendar.MDateSelectorConstraints
     */
    public void setShowTodayButton(boolean show) {
        setShowTodayButton(show, true);
    }

    /**
     * Does this field have a today button when the pull down is shown ?
     * 
     * @return true if a 'today' button is displayed.
     * @see #setShowTodayButton
     */
    public boolean getShowTodayButton() {
        return todayButton;
    }

    private boolean closeOnToday = true;

    /**
     * The pull down can be configured with a button at the bottom to quickly
     * select the current date. The label is translated using the ResourceBundle
     * given in the contraints object passed in the setConstraints() method (or
     * the default which is false).
     * 
     * @param show
     *            set this to true if the button is required.
     * @param close
     *            the popup closes when close is true (default)
     * @see mseries.Calendar.MDateSelectorConstraints
     */
    public void setShowTodayButton(boolean show, boolean close) {
        this.todayButton = show;
        this.closeOnToday = close;
    }

    /**
     * @return the close on today attribute
     */
    public boolean getCloseOnToday() {
        return this.closeOnToday;
    }
}

class ShortFormatter implements MDateFormat {

    DateFormat formatter;

    public ShortFormatter() {
        formatter = DateFormat.getDateInstance(DateFormat.SHORT);
    }

    public StringBuffer format(Date d, StringBuffer appendTo, FieldPosition pos) {
        return formatter.format(d, appendTo, pos);
    }

    public String format(Date d) {
        return formatter.format(d);
    }

    public Date parse(String s) throws ParseException {
        return formatter.parse(s);
    }
}

// $Log: MDateEntryField.java,v $
// Revision 1.5  2014/06/22 17:57:32  atwells
// 8.6.2 WebLogic Upgrade - JDK1.6 amendments as isValid method had changed
//
// Revision 1.4  2006/06/05 12:31:53  bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting tab fix
//
// Revision 1.3 2006/05/31 14:26:08 bzjrnl
// Change: TI901
// Comment: Weblogic Upgrade - Standadise code formatting
//
// Revision 1.2 2004/04/05 16:13:37 sz0t7n
// Added isTemporary to the constructor of focus event
//
// Revision 1.1 2004/04/02 15:44:05 sz0t7n
// Adding mseries to the build so we can bug fix it from now on
//
// Revision 1.18 2003/01/18 16:40:09 martin
// *** empty log message ***
//
// Revision 1.17 2003/01/15 21:47:34 martin
// *** empty log message ***
//
// Revision 1.16 2003/01/08 20:47:19 martin
// Overrode setInputVerifier method and delegate to display
//
// Revision 1.15 2002/12/21 23:03:25 martin
// *** empty log message ***
//
// Revision 1.14 2002/12/21 22:53:16 martin
// *** empty log message ***
//
// Revision 1.13 2002/12/15 17:44:16 martin
// *** empty log message ***
//
// Revision 1.12 2002/06/13 19:25:24 martin
// Added closeOnToday button support
//
// Revision 1.11 2002/06/09 13:59:45 martin
// Adjusted Javadoc comment
//
// Revision 1.10 2002/06/09 13:58:51 martin
// Adjusted Javadoc comment
//
// Revision 1.9 2002/06/09 13:53:44 martin
// Adjusted Javadoc comment
//
// Revision 1.8 2002/06/09 13:48:05 martin
// Added 'Today' button
//
// Revision 1.7 2002/04/19 20:41:40 martin
// Make the default date format locale sensitive and SHORT
//
// Revision 1.6 2002/03/03 09:49:38 martin
// Changed listener list to javax.swing.event.ListenerList
//
// Revision 1.5 2002/02/19 20:28:44 martin
// Ensure that the MFieldEvents have the correct source
//
