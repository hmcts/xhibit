package uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;

/**
 * <p>
 * Title: Validates string contents for being a valid name.
 * </p>
 * <p>
 * Description: Validates string contents and utilises a JOptionPane to display
 * error messages to the user.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public class NameVerifier extends OrderInputVerifier {
    private static Logger log = CSServices.getLogger(NameVerifier.class);

    // message displayed to the user.
    private String errorMessage;

    // title of error message, appears as title on JOptionPane.
    private String errorTitle;

    private boolean validationOn = true;

    private String checkedString;

    private OrderComponentHelper helper;

    /**
     * Constructor
     * 
     * @param helper
     *            the ordercomponenthelper responsible for updating the dom
     */
    public NameVerifier() {
        super();
        setRequired(false);
    }

    /**
     * Constructor
     * 
     * @param helper
     *            the ordercomponenthelper responsible for updating the dom
     */
    public NameVerifier(OrderComponentHelper helper) {
        this(helper, false);
    }

    /**
     * Constructor
     * 
     * @param helper
     *            the ordercomponenthelper responsible for updating the dom
     * @param required
     *            true if this is a required field
     */
    public NameVerifier(OrderComponentHelper helper, boolean required) {
        super();
        setHelper(helper);
        setRequired(required);
    }

    /**
     * Verify the input
     * 
     * @param input
     *            a JComponent to verify
     * @return true if verified
     */
    public boolean verify(JComponent input) {
        boolean inRange = false;
        String text = ((JTextField) input).getText();
        input.setBorder(UIManager.getBorder("TextField.border"));
        setCheckedString(text);

        boolean verified = isValid(text);
        if (verified && getHelper() != null) {
            getHelper().setValue(text);
        }

        return isValid(text);
    }

    /**
     * Determines if JComponent should let focus pass to another component
     * 
     * @param input
     * @return
     */
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = super.shouldYieldFocus(input);
        JTextField tf = (JTextField) input;

        // if not valid show an error message
        if (!valid) {
            tf.setBorder(BorderFactory.createLineBorder(Color.red));
            tf.requestFocus();
            JOptionPane.showMessageDialog(null, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
        }
        return valid;
    }

    /**
     * Return indicator if validation is on
     * 
     * @return true if validation is on
     */
    public boolean isValidationOn() {
        return validationOn;
    }

    /**
     * Sets the validation on or off
     * 
     * @param validationOn
     *            true if validation on
     */
    public void setValidationOn(boolean validationOn) {
        this.validationOn = validationOn;
    }

    /**
     * Method not implemented since in this case no minimum value is needed.
     */
    public void setMinVal(String min) {
        // no implementation
    }

    /**
     * Validates field by checking if it contains a valid string.
     * 
     * @param s
     *            String to be validated.
     * @return true if field contents are valid.
     */
    public boolean isValid(String s) {
        log.debug("$$$>>> isValid |" + s + "|");
        log.debug("$$$>>> validationOn |" + validationOn + "|");
        if (validationOn) {
            if (checkLength(s) && containsLegalChars(s)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
    
    public boolean containsLegalChars(String s) {
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c) && !isSpecialNameCharacter(c)) {
                this.errorMessage = "Illegal characters are not allowed. Valid characters here are 0-9 a-z A-Z , - .";
                return false;
            }
        }
        return true;
    }
    
    public boolean isSpecialNameCharacter(char c) {
        if (c == '-' || c == ',' || c == '.' || c == ' ') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks that a value is entered and returns false only if required is set
     * to true.
     * 
     * @param value
     *            String contents of field.
     * @return true or false.
     */
    protected boolean checkLength(String value) {

        if (this.required) {
            if (value.length() > 0 && (value.trim().equals("") == false)) {
                setCheckedString(value);
                return true;
            } else {
                this.errorMessage = "This is a required field";
                this.errorTitle = "No value given";
                return false;
            }
        } else {
            if (value.length() == 0 || (value.trim().equals(""))) {
                // set the checked string to a space to maintain the schema
                // integrity
                setCheckedString(" ");
            }
            return true;
        }
    }

    /**
     * Set the checked string value
     * 
     * @param s
     */
    private void setCheckedString(String s) {
        checkedString = s;
    }

    /**
     * Get the checked string value
     * 
     * @return
     */
    private String getCheckedString() {
        return checkedString;
    }

}
