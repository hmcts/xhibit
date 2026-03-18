package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentListener;
import uk.gov.courtservice.xhibit.client.util.XColor;
import uk.gov.courtservice.xhibit.client.util.XTextField;

/**
 * <p>
 * Title: TextFieldDataComponent
 * </p>
 * <p>
 * Description: Use a label for the data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: TextFieldDataComponent.java,v 1.13 2005/06/02 17:47:33 bzjrnl
 *          Exp $
 */
public class TextFieldDataComponent extends XTextField implements DelegatorDataComponent {

    /**
     * The delegate responsible for doing the work
     */
    private final DelegateDataComponent delegate = new DelegateDataComponent(this);

    /**
     * Construct a new instance
     */
    public TextFieldDataComponent() {
        super();
    }

    /**
     * DataComponent Implementation
     */
    public Component getComponent() {
        return this;
    }

    /**
     * DataComponent Implementation
     */
    public boolean isFixedSize() {
        return true;
    }

    // Delegate Callbacks

    /**
     * DelegatorDataComponent Implementation
     */
    public Color getBackgroundImpl() {
        return getBackground();
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public void setBackgroundImpl(Color color) {
        setBackground(color);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String getDataImpl() {
        String text = getText();
        if (text.length() > 0) {
            return text;
        }
        return null;
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public void setDataImpl(String data) {
        if (data == null) {
            setText("");
        } else {
            setText(data);
        }
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public void paintImpl(Graphics g) {
        super.paint(g);
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public Insets getPaintInsetsImpl() {
        return DEFAULT_PAINT_INSETS;
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public String createToolTipTextImpl() {
        return null;
    }

    /**
     * DelegatorDataComponent Implementation
     */
    public boolean hasError() {
        // Added to clarify where this functionality is implemented
        return super.hasError();
    }

    // Delegate

    /**
     * Return true if the component should be displayed
     */
    public boolean isScreenPrint() {
        return delegate.isScreenPrint();
    }

    /**
     * Set to true to display the component
     */
    public void setScreenPrint(boolean screenPrint) {
        delegate.setScreenPrint(screenPrint);
    }

    /**
     * DataComponent Implementation
     */
    public void setData(String data) {
        delegate.setData(data);
    }

    /**
     * DataComponent Implementation
     */
    public String getData() {
        return delegate.getData();
    }

    /**
     * DataComponent Implementation
     */
    public void setNameG1(String nameG1) {
        delegate.setNameG1(nameG1);
    }

    /**
     * DataComponent Implementation
     */
    public void setNameG2(String nameG2) {
        delegate.setNameG2(nameG2);
    }

    /**
     * DataComponent Implementation
     */
    public String getNameG1() {
        return delegate.getNameG1();
    }

    /**
     * DataComponent Implementation
     */
    public String getNameG2() {
        return delegate.getNameG2();
    }

    /**
     * DataComponent Implementation
     */
    public void setColorG1(XColor colorG1) {
        delegate.setColorG1(colorG1);
    }

    /**
     * DataComponent Implementation
     */
    public void setColorG2(XColor colorG2) {
        delegate.setColorG2(colorG2);
    }

    /**
     * DataComponent Implementation
     */
    public XColor getColorG1() {
        return delegate.getColorG1();
    }

    /**
     * DataComponent Implementation
     */
    public XColor getColorG2() {
        return delegate.getColorG2();
    }

    /**
     * DataComponent Implementation
     */
    public boolean isMandatory() {
        return delegate.isMandatory();
    }

    /**
     * DataComponent Implementation
     */
    public void setMandatory(boolean mandatory) {
        delegate.setMandatory(mandatory);
    }

    /**
     * DataComponent Implementation
     */
    public boolean isComplete() {
        return delegate.isComplete();
    }

    /**
     * DataComponent Implementation
     */
    public boolean isDeletedG1() {
        return delegate.isDeletedG1();
    }

    /**
     * DataComponent Implementation
     */
    public boolean isDeletedG2() {
        return delegate.isDeletedG2();
    }

    /**
     * DataComponent Implementation
     */
    public void setDeletedG1(boolean deletedG1) {
        delegate.setDeletedG1(deletedG1);
    }

    /**
     * DataComponent Implementation
     */
    public void setDeletedG2(boolean deletedG2) {
        delegate.setDeletedG2(deletedG2);
    }

    /**
     * Add the listener
     */
    public void addDataComponentListener(DataComponentListener listener) {
        delegate.addDataComponentListener(listener);
    }

    /**
     * Remove the listener
     */
    public void removeDataComponentListener(DataComponentListener listener) {
        delegate.removeDataComponentListener(listener);
    }

    /**
     * DataComponent Implementation
     */
    public void setMaxChars(int maxChars) {
        delegate.setMaxChars(maxChars);
    }

    /**
     * DataComponent Implementation
     */
    public int getMaxChars() {
        return delegate.getMaxChars();
    }

    /**
     * Component Override
     */
    public void paint(Graphics g) {
        delegate.paint(g);
    }

    /**
     * Validating documnent simplifies the coding required to create validate
     * documents your documents
     */
    protected abstract class ValidatingDocument extends PlainDocument {

        public ValidatingDocument() {
            addDocumentListener(delegate);
        }

        /**
         * This is where the validating code should go
         * 
         * @return true if the candidate is valid for your document
         */
        public abstract boolean validate(String candidate);

        /**
         * PlainDocument Implementation
         * 
         * @see javax.swing.text.PlainDocument#insertString(int, String,
         *      AttributeSet);
         */
        public final void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            String currentText = getText(0, getLength());
            String beforeOffset = currentText.substring(0, offs);
            String afterOffset = currentText.substring(offs, currentText.length());
            String proposedResult = beforeOffset + str + afterOffset;

            if (validate(proposedResult)) {
                super.insertString(offs, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        /**
         * PlainDocument Implementation
         * 
         * @see javax.swing.text.PlainDocument#remove(int, int);
         */
        public final void remove(int offs, int len) throws BadLocationException {
            String currentText = getText(0, getLength());
            String beforeOffset = currentText.substring(0, offs);
            String afterOffset = currentText.substring(len + offs, currentText.length());
            String proposedResult = beforeOffset + afterOffset;

            if (validate(proposedResult)) {
                super.remove(offs, len);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

    }

    /**
     * Utility return true if number is valid integer less than specified number
     * of digits
     */
    protected static boolean isInteger(String integer, int integerDigits) {
        if (integer == null) {
            throw new IllegalArgumentException("integer: null");
        }
        if (integerDigits < 0) {
            throw new IllegalArgumentException("integerDigits: " + integerDigits);
        }

        return integer.length() <= integerDigits && isInteger(integer);
    }

    /**
     * Utility return true if number is valid deimal less than specified number
     * of digits
     */
    protected static boolean isNumber(String number, int integerDigits, int decimalDigits) {
        if (number == null) {
            throw new IllegalArgumentException("number: null");
        }
        if (integerDigits < 0) {
            throw new IllegalArgumentException("integerDigits: " + integerDigits);
        }
        if (decimalDigits < 0) {
            throw new IllegalArgumentException("decimalDigits: " + decimalDigits);
        }

        int index = number.indexOf('.');
        if (index == -1) {
            return number.length() <= integerDigits && isInteger(number);
        } else {
            String integer = number.substring(0, index);
            String decimal = number.substring(index + 1);

            return integer.length() <= integerDigits && isInteger(integer) && decimal.length() <= decimalDigits
                    && isDecimal(decimal);
        }
    }

    /**
     * Check number in range
     * 
     * @param number
     *            the number to check
     * @param min
     *            the min value the number can have
     * @param max
     *            the max value the number can have
     * @return true if the number is greater than or equal to the min value and
     *         less than or equal to the max value
     */
    protected static boolean between(String number, int min, int max) {
        try {
            return between(Integer.parseInt(number), min, max);
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Check number in range
     * 
     * @param number
     *            the number to check
     * @param min
     *            the min value the number can have
     * @param max
     *            the max value the number can have
     * @return true if the number is greater than or equal to the min value and
     *         less than or equal to the max value
     */
    protected static boolean between(int number, int min, int max) {
        return number >= min && number <= max;
    }

    /**
     * Utility to test if the data is all digits (no longer testing leading
     * digit seperatly as can lead with 0)
     */
    private static boolean isInteger(String integer) {
        for (int i = 0, l = integer.length(); i < l; i++) {
            if (!isDigit(integer.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Utility to test if the data is all digits
     */
    private static boolean isDecimal(String decimal) {
        for (int i = 0, l = decimal.length(); i < l; i++) {
            if (!isDigit(decimal.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Utility to test if the data is a digit
     */
    private static boolean isDigit(char c) {
        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8'
                || c == '9';
    }

    /**
     * Set the previous data component.
     */
    public void setPreviousDataComponent(DataComponent previousDataComponent) {
        // Dont need to access the previous component
    }

}
