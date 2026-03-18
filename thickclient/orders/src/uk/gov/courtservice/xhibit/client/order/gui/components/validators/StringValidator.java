package uk.gov.courtservice.xhibit.client.order.gui.components.validators;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;

/**
 * <p>
 * Title: Validates string contents.
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
public class StringValidator extends JOptionPane implements PostCodeValidator {
    private static Logger log = CSServices.getLogger(StringValidator.class);

    // true if the field must contain some data.
    private boolean required;

    // message displayed to the user.
    private String errorMessage;

    // title of error message, appears as title on JOptionPane.
    private String errorTitle;

    private boolean isParentAnOrderOption;

    private boolean validationOn = true;

    private OrderOption opt;

    /**
     * Return the OrderOption
     * 
     * @return The option
     */
    public OrderOption getOpt() {
        return opt;
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
     * Sets the OrderOption
     * 
     * @param opt
     *            The option
     */
    public void setOpt(OrderOption opt) {
        this.opt = opt;
    }

    /**
     * Constructs a string validator with a default min value = 0, and required =
     * true.
     */
    public StringValidator() {
        this.required = true;
    }

    /**
     * Sets indicator to show if the parent of the component is an OrderOption
     * 
     * @param b
     *            true if parent is an OrderOption
     */
    public void setIsParentAnOrderOption(boolean b) {
        this.isParentAnOrderOption = b;
    }

    /**
     * Return indicator if parent is an OrderOption
     * 
     * @return true if parent is an OrderOption
     */
    public boolean isParentAnOrderOption() {
        return this.isParentAnOrderOption;
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
                this.showMessageDialog(null, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }
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
        boolean matched = false;
        try {
            RE regexp = new RE("(GIR 0AA)|" + "((([A-Z][0-9][0-9]?)|" + "(([A-Z][A-HJ-Y][0-9][0-9]?)|"
                    + "(([A-Z][0-9][A-Z])|" + "([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})");
            if (!regexp.match(postcode)) {
                if (postcode.equals("")) {
                    // Set to false as original value will be reinstated
                    matched = false;
                } else {
                    this.errorMessage = "Post Code entered is not of a valid format";
                    this.errorTitle = "Post Code error";
                    this.showMessageDialog(null, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
                    matched = false;
                }
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
    private boolean checkLength(String value) {

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

}
