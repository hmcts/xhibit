/*
 *   Copyright (c) 2001 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
 *   
 *   Thanks go to David M Karr who, with permission, kindly supplied the code in the form
 *   of his DateTimeEditor, on which this component is based.
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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import mseries.Calendar.MFieldListener;
import mseries.utils.MSpinnerLayout;

/**
 * Integer entry field with spinner buttons to increment and decrement the
 * value. The field may be typed into but is most effective when <i>not</i>
 * editable (see setEditable method) as range checking using the maximum and
 * minimum can be performed properly. The increment value is 1.
 * 
 * @deprecated in favour of MSpinner with MIntegerSpinnerModel and IntegerEditor
 * @see mseries.ui.MSpinner
 * @see mseries.ui.MIntegerSpinnerModel
 * @see mseries.ui.IntegerEditor
 */
public class MIntegerSpinner extends JPanel implements FocusListener {
    public JTextField display;

    protected Vector listeners = new Vector();

    protected Vector fieldListeners = new Vector();

    private static final int UP = 1;

    private static final int DOWN = -1;

    private AbstractAction upAction = new UpDownAction(UP, "up");

    private AbstractAction downAction = new UpDownAction(DOWN, "down");

    private JButton up, down;

    private boolean setting = false;

    private boolean editable = true;

    /** The default minimum value */
    protected int min = -2147483648;

    /** The default maximum value */
    protected int max = 2147483647;

    private boolean hasMin;

    private boolean hasMax;

    private long val = 0;

    protected int incr = 1;

    private boolean roll = false;

    private Toolkit toolkit;

    private NumberFormat integerFormatter;

    public MIntegerSpinner() {
        display = new JTextField();
        init();
    }

    /*
     * @param size the size of the display field, not the number of characters
     */
    public MIntegerSpinner(int size) {
        display = new JTextField(size);
        init();
    }

    private void init() {
        display.addFocusListener(this);

        toolkit = Toolkit.getDefaultToolkit();
        integerFormatter = NumberFormat.getNumberInstance(Locale.US);
        integerFormatter.setParseIntegerOnly(true);

        up = new BasicArrowButton(BasicArrowButton.NORTH);
        up.addActionListener(upAction);
        up.setBackground(UIManager.getColor("control"));

        down = new BasicArrowButton(BasicArrowButton.SOUTH);
        down.addActionListener(downAction);
        down.setBackground(UIManager.getColor("control"));

        setLayout(new MSpinnerLayout());
        display.setDocument(new IntegerDocument());
        add(display);
        add(up);
        add(down);
        // display.setBorder(null);
        // setBorder(UIManager.getBorder("ComboBox.border"));

        setupKeymap();
        setValue((int) val);

    }

    /**
     * The 'magic' behind any custom text field. This document only allows
     * [0..9] to be typed or pasted.
     */
    protected class IntegerDocument extends PlainDocument {
        // This method process the characters that were typed or pasted
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;

            for (int i = 0; i < result.length; i++) {
                if (Character.isDigit(source[i]) || source[i] == '-') {
                    result[j++] = source[i];
                }
            }
            if (setting || isEditable()) {
                super.insertString(offs, new String(result, 0, j), a);
            }
        }

        public void remove(int offs, int len) throws BadLocationException {
            if (setting || isEditable()) {
                super.remove(offs, len);
            }
        }
    }

    /**
     * Returns the current value of the field
     * 
     * @return the current value of the field
     */
    public int getValue() {
        int retVal = 0;
        try {
            retVal = integerFormatter.parse(display.getText()).intValue();
        } catch (ParseException e) {
            // This should never happen because insertString allows
            // only properly formatted data to get in the field.
            toolkit.beep();
        }
        // val=retVal;
        return retVal;
    }

    /**
     * Sets the value, respecting the maximum & minimum
     * 
     * @param value
     *            the new value
     */
    public void setValue(int value) {
        setting = true;
        long x;
        x = value;
        if (value > getMaximum() || value < getMinimum()) {
            x = val;
        }
        display.setText(integerFormatter.format(x));
        val = x;
        setting = false;
    }

    /**
     * Used to force a minimum value when the field is decremented using the
     * down button
     * 
     * @param min
     *            the minimum value
     */
    public void setMinimum(int min) {
        hasMin = true;
        this.min = min;
    }

    /**
     * Returns the current minimum value
     * 
     * @return The current minimum value
     */
    public int getMinimum() {
        return this.min;
    }

    /**
     * Used to force a maximum value when the field is incremented using the up
     * button
     * 
     * @param max
     *            the maximum value
     */
    public void setMaximum(int max) {
        hasMax = true;
        this.max = max;
    }

    /**
     * Returns the current maximum value
     * 
     * @return The current maximum value
     */
    public int getMaximum() {
        return this.max;
    }

    public int getNextValue() {
        long val = getValue();
        if (val + incr > getMaximum()) {
            if (roll) {
                val = min;
            }
        } else {
            val += incr;
        }
        return (int) val;
    }

    public int getPreviousValue() {
        long val = getValue();

        if (val - incr < getMinimum()) {
            if (roll) {
                val = max;
            }
        } else {
            val -= incr;
        }
        return (int) val;

    }

    /**
     * Class to do the increment/decrement when the buttons are pressed
     */
    protected class UpDownAction extends AbstractAction {
        int direction; // +1 = up; -1 = down

        public UpDownAction(int direction, String name) {
            super(name);

            this.direction = direction;
        }

        public void actionPerformed(ActionEvent evt) {

            boolean up = (direction == UP) ? true : false;
            if (up) {
                int x = getNextValue();
                setValue(x);

            } else {
                setValue(getPreviousValue());
            }

            int x = MIntegerSpinner.this.getValue();
            notifyListeners(new MChangeEvent(this, new Integer(x), MChangeEvent.CHANGE));
            display.requestFocus();
        }
    }

    /**
     * Sets whether to rollover when the maximum or minimum are reached
     * 
     * @param rollover
     *            whether or not to rollover
     */
    public void setRollover(boolean rollover) {
        this.roll = rollover;
    }

    /**
     * Is rollover enabled ?
     * 
     * @return true is rollover is enabled, i.e. incrementing beyond the maximum
     *         will 'roll' to the minimum value
     */
    public boolean isRollover() {
        return this.roll;
    }

    /**
     * Associates the Up Arrow and Down Arrow keys with incrment and decrement
     * of the value
     */
    protected void setupKeymap() {
        display.registerKeyboardAction(upAction, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_FOCUSED);
        display
                .registerKeyboardAction(downAction, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                        JComponent.WHEN_FOCUSED);

    }

    /**
     * Sets the field changeable or not, use setEditable if you need to prevent
     * the field being changed by the keyboard and allow changes only by the
     * spinner buttons
     * 
     * @param enable
     *            true if the field can be changed by either typing or
     *            'spinning'
     * @see #setEditable
     */
    public void setEnabled(boolean enable) {
        display.setEnabled(enable);
        up.setEnabled(enable);
        down.setEnabled(enable);
    }

    /**
     * Is the field enabled ?
     * 
     * @return true is the field can be changed
     */
    public boolean isEnabled() {
        return (display.isEnabled() && up.isEnabled() && down.isEnabled());
    }

    /**
     * Disable changing the date using the keyboard, use setEnabled(false) if
     * the date is not to be changed at all.
     * 
     * @param editable
     *            true if the field can be changed by typing.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Can the date be changed using the keyboard ?
     * 
     * @return is the date changeble using the keyboard ?
     */
    public boolean isEditable() {
        return this.editable;
    }

    /**
     * Registers the listeners of the field changes. Fired when the spinner
     * buttons are pushed.
     * 
     * @param listener -
     *            MMonthListener
     */
    public void addMChangeListener(MChangeListener listener) {
        listeners.addElement(listener);
    }

    /**
     * Removes the listener from the registered list of listeners
     * 
     * @param listener -
     *            MMonthListener
     */
    public void removeMChangeListener(MChangeListener listener) {
        listeners.removeElement(listener);

    }

    private void notifyListeners(MChangeEvent event) {
        // Pass these events on to the registered listener

        Vector list = (Vector) listeners.clone();
        for (int i = 0; i < list.size(); i++) {
            MChangeListener l = (MChangeListener) listeners.elementAt(i);
            l.valueChanged(event);
        }
    }

    /**
     * Simply delgates to super.removeFocusListener(),
     * 
     * @deprecated use removeMFieldListener instead, it is much more reliable
     * @param listener
     */
    public void removeFocusListener(FocusListener listener) {
        super.removeFocusListener(listener);
    }

    /**
     * Simply delgates to super.addFocusListener(),
     * 
     * @deprecated use addMFieldListener instead, it is much more reliable
     * @param listener
     */
    public void addFocusListener(FocusListener listener) {
        super.addFocusListener(listener);
    }

    /**
     * Necessary to implement an interface, no public use.
     */
    public void focusLost(FocusEvent e) {
        notifyListeners(e);
    }

    /**
     * Necessary to implement an interface, no public use.
     */
    public void focusGained(FocusEvent e) {
        notifyListeners(e);
    }

    /**
     * Registers the listeners field getting and losing focus
     * 
     * @param listener -
     *            MMonthListener
     */
    public void addMFieldListener(MFieldListener listener) {
        fieldListeners.addElement(listener);
    }

    /**
     * Removes the listener from the registered list of listeners
     * 
     * @param listener -
     *            MMonthListener
     */
    public void removeMFieldListener(MFieldListener listener) {
        fieldListeners.removeElement(listener);
    }

    private void notifyListeners(FocusEvent e) {
        int type = e.getID();
        Vector list = (Vector) fieldListeners.clone();
        for (int i = 0; i < list.size(); i++) {
            MFieldListener l = (MFieldListener) list.elementAt(i);
            if (type == FocusEvent.FOCUS_GAINED) {
                l.fieldEntered(e);
            } else {
                l.fieldExited(e);
            }
        }
    }

    public void setToolTipText(String text) {
        display.setToolTipText(text);
    }

    public String getToolTipText() {
        return display.getToolTipText();
    }

    /*
     * public static void main(String[] argv) { JFrame f = new JFrame("Demo");
     * 
     * final MIntegerSpinner m = new MIntegerSpinner(10); m.setMinimum(0);
     * m.setMaximum(100); m.setEditable(true); m.setRollover(true);
     * m.addMChangeListener(new MChangeListener() { public void
     * valueChanged(MChangeEvent e) { System.out.println("Changed
     * "+m.getValue()); } });
     * 
     * f.getContentPane().add(m);
     * 
     * f.pack(); f.show(); }
     */
}
