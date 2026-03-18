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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A SpinnerEditor that renders integer (int)data.
 * 
 * @see mseries.ui.MIntegerSpinnerModel
 */
public class IntegerEditor extends DefaultSpinnerEditor {

    private NumberFormat integerFormatter;

    boolean setting = false;

    public IntegerEditor() {
        integerFormatter = NumberFormat.getNumberInstance(Locale.US);
        integerFormatter.setParseIntegerOnly(true);
        display.setDocument(new IntegerDocument());
    }

    public Object getValue() {
        Number retVal = new Long(0);

        try {
            retVal = integerFormatter.parse(display.getText());
        } catch (ParseException e) {
            System.out.println(e);
            // This should never happen because insertString allows
            // only properly formatted data to get in the field.
        }
        int x = ((Long) retVal).intValue();
        return new Integer(x);
    }

    public JTextField getTextfield() {
        return display;
    }

    public void setValue(Object value) {
        setting = true;
        Integer x;
        if (value instanceof java.lang.Integer) {
            x = (Integer) value;
            display.setText(integerFormatter.format(x.intValue()));
        }
        setting = false;
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
                /*
                 * if(!setting) { model.setValue(getValue()); }
                 */
            }
        }

        public void remove(int offs, int len) throws BadLocationException {
            if (setting || isEditable()) {
                super.remove(offs, len);
                /*
                 * if(!setting) { model.setValue(getValue()); }
                 */
            }
        }
    }
}
