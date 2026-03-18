
package mseries.ui;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Collection;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TextUI;
import javax.swing.text.Document;
import mseries.nationality.MNationalitySelectorConstraints;
import mseries.nationality.MDefaultPullDownConstraints;
import mseries.Calendar.MFieldListener;

/**
 * MNationalityEntryField for the entry of nationalities.
 * Modeled on MDateEntryField for the entry of dates.
 */
public class MNationalityEntryField extends JComponent implements FocusListener {

    private static final long serialVersionUID = 1L;

    // Nationality is the same class as date
    private static final String uiClassID = "NationalityEntryUI";

    protected MNationalityField display;

    private boolean selectButton = false;

    private boolean hasBorder = true;

    private boolean nullOnEmpty = false;

    protected EventListenerList listenerList = new EventListenerList();

    MNationalitySelectorConstraints panelConstraints = new MDefaultPullDownConstraints();


    /**
     * Creates a MNationalityEntryField
     * 
     * @param size
     *            the size of the display part of the component
     */
    public MNationalityEntryField(Document doc, Collection<String> nationalities) {
        super();
        display = new MNationalityField(nationalities) {
            private static final long serialVersionUID = 1L;
            public void setUI(TextUI ui) {
                super.setUI(ui);
                setBorder(null);
            }
        };
        display.addFocusListener(this);
        display.setDocument(doc);
        updateUI();
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
    public void setConstraints(MNationalitySelectorConstraints c) {
        this.panelConstraints = c;
    }

    /**
     * Gets the constraints object that contains the parameters used to
     * configure the pull down calendar
     * 
     * @see #getConstraints
     */
    public MNationalitySelectorConstraints getConstraints() {
        return this.panelConstraints;
    }
    
    /**
     * Returns the textfield that handles the nationality editing. Used by the
     * UIDelegates
     */
    public MNationalityField getDisplay() {
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
            String uiDelegateClassName = "mseries.plaf.basic.BasicNationalityEntryUI";
            String lafName = UIManager.getLookAndFeel().getID();
            uiDelegateClassName = "mseries.plaf." + lafName + "." + lafName + "NationalityEntryUI";

            try {
                compUI = (ComponentUI) (Class.forName(uiDelegateClassName)).newInstance();
            } catch (Exception e) {
                uiDelegateClassName = "mseries.plaf.basic.NationalityDateEntryUI";
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
     * @return string "NationalityEntryUI"
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
     * nationality. Default is false.
     * 
     * @param nullOnEmpty
     *            set to true if a null is required when the textfield is empty
     */
    public void setNullOnEmpty(boolean nullOnEmpty) {
        this.nullOnEmpty = nullOnEmpty;
    }

    /**
     * @return the current value of the field The value of the entry field, no
     *         parsing is performed, use getValue() to get a nationality
     */
    public String getText() {
        return display.getText();
    }

    /**
     * This method does not do anything, the implementation is empty and it only
     * present to make the component a JavaBean
     */
    public void setText(@SuppressWarnings("unused")String text) {
        // empty
    }

    /**
     * @return the current value of the field
     * @exception ParseException
     *                if it is not a valid nationality
     */
    public String getValue() throws ParseException {
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
    public void setValue(String newValue) {
        display.setValue(newValue);
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
        String nationality;
        try {
            nationality = getValue();
        } catch (ParseException e) {
            nationality = null;
        }
        event = new MChangeEvent(this, nationality, type);

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
     * is pushed.
     * 
     * @param show
     *            set this to true if the button is required.
     * @see mseries.Calendar.MDateSelectorConstraints
     */
    public void setShowSelectButton(boolean show) {
        this.selectButton = show;
    }

    /**
     * Does this field have a select button when the pull down is shown ?
     * 
     * @return true if a 'select' button is displayed.
     */
    public boolean getShowSelectButton() {
        return selectButton;
    }
}

