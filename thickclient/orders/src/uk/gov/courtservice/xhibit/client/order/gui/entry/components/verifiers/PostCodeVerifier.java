package uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomAddressText;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;

/**
 * <p>
 * Title: Verifies postcode contents.
 * </p>
 * <p>
 * Description: Verifies postcode contents and utilises a JOptionPane to display
 * error messages to the user.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */
public class PostCodeVerifier extends OrderInputVerifier {
    private static Logger log = CSServices.getLogger(PostCodeVerifier.class);

    // String to hold Default PostCode held within Blank Schema
    private static final String DEFAULT_POSTCODE = "AA1 1AA";

    // true if the field must contain some data.
    private boolean required;

    // message displayed to the user.
    private String errorMessage = "Post Code entered is not of a valid format";

    // title of error message, appears as title on JOptionPane.
    private String errorTitle = "Post Code error";

    private boolean validationOn = true;

    private Component component;

    private OrderComponentHelper helper;

    public PostCodeVerifier(OrderComponentHelper helper) {
        this(helper, false);
    }

    public PostCodeVerifier(OrderComponentHelper helper, boolean required) {
        super();
        setHelper(helper);
        setRequired(required);
    }

    public PostCodeVerifier(Component component) {
        this.component = component;
    }

    public PostCodeVerifier() {
        super();
    }

    /**
     * Verify that the postcode netered is valid
     * 
     * @param input
     *            the component (textfield) containing the postcode
     * @return true if valid
     */
    public boolean verify(JComponent input) {
        boolean inRange = false;
        String text = ((JTextField) input).getText();
        if (text.length() == 0) {
            text = DEFAULT_POSTCODE;
        }
        input.setBorder(UIManager.getBorder("TextField.border"));
        boolean verified = isValid(text);
        if (verified && getHelper() != null) {
            getHelper().setValues(((CustomAddressText) input).getRef(), text);
        }
        return verified;
    }

    /**
     * Checks is the component should yield focus
     * 
     * @param input
     *            the component to verify
     * @return true if verified
     */
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = super.shouldYieldFocus(input);
        JTextField tf = (JTextField) input;

        if (!valid) {
            tf.setBorder(BorderFactory.createLineBorder(Color.red));
            tf.requestFocus();
            JOptionPane.showMessageDialog(component, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
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
            return (checkLength(s) && isPostCodeValid(s));
        } else {
            return true;
        }
    }

    /**
     * Validates to check if the string matches the postcode regular expression.
     * 
     * @param postcode
     *            String to be validated.
     * @return true if field contents are valid.
     */

    public boolean isPostCodeValid(String postcode) {
        if (postcode != null && postcode.length() == 0) {
            return true;
        }
        boolean matched = false;
        try {
            // Surrounding the postcode regualr expressions with ^$ means
            // that
            // the string must match one of the options. If we omit these
            // metacharacters
            // a best match may be achieved so allowing through some invalid
            // postcodes
            // NB: These metacharacters are NOT included in the
            // BS7666-v1.xsd so care
            // should be taken if the pattern changes
            // See also
            // uk.gov.courtservice.xhibit.xmlbinding.orders.AddressHelper
            RE regexp = new RE("(GIR 0AA)|" + "^((([A-Z][0-9][0-9]?)|" + "(([A-Z][A-HJ-Y][0-9][0-9]?)|"
                    + "(([A-Z][0-9][A-Z])|" + "([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$");
            if (!regexp.match(postcode)) {
                matched = false;
            } else {
                matched = true;
            }
        } catch (RESyntaxException re) {
            re.printStackTrace(); // To change body of catch statement use
            // Options | File Templates.
            log.error(re);
        }
        return matched;
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
                return true;
            } else {
                this.errorMessage = "This is a required field";
                this.errorTitle = "No value given";
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Sets the OrderComponentHelper
     * 
     * @param helper
     *            the helper
     */
    public void setHelper(OrderComponentHelper helper) {
        this.helper = helper;
    }

    /**
     * Gets the helper
     * 
     * @return
     */
    public OrderComponentHelper getHelper() {
        return this.helper;
    }

}
