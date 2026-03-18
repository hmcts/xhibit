package uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomAddressText;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;

/**
 * <p>
 * Title: Validates address string contents.
 * </p>
 * <p>
 * Description: Validates address string contents and utilises a JOptionPane to
 * display error messages to the user.
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
public class AddressStringVerifier extends OrderInputVerifier {
    private static Logger log = CSServices.getLogger(AddressStringVerifier.class);

    // true if the field must contain some data.
    private boolean required = false;

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
    public AddressStringVerifier() {
        super();
    }

    /**
     * Constructor
     * 
     * @param helper
     *            the ordercomponenthelper responsible for updating the dom
     */
    public AddressStringVerifier(OrderComponentHelper helper) {
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
    public AddressStringVerifier(OrderComponentHelper helper, boolean required) {
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
            getHelper().setValues(((CustomAddressText) input).getRef(), getCheckedString());
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
     * Set required to true or false.
     * 
     * @param req
     *            String value which is parsed to the correct boolean value.
     */
    public void setRequired(boolean req) {
        this.required = req;
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
            if (checkLength(s)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
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

        if (required) {
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
