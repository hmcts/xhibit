/**
 * Created by IntelliJ IDEA.
 * Determines if a field is required or not, and validates that it contains
 * data.
 * User: hzf3bb
 * Date: Jan 24, 2003
 * Time: 3:48:26 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.components.validators;

import javax.swing.JOptionPane;

import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;

public class RequiredFieldValidator extends JOptionPane implements GuiValidator {
    private boolean required;

    private String errorMessage;

    private String errorTitle;

    private boolean isParentAnOrderOption;

    private OrderOption opt;

    private boolean validationOn = true;

    /**
     * Constructor
     */
    public RequiredFieldValidator() {
        this.required = true;
    }

    /**
     * Indicates whether validation is set on.
     * 
     * @return true if validation is on
     */
    public boolean isValidationOn() {
        return validationOn;
    }

    /**
     * Sets indicator to determine if validation is on.
     * 
     * @param validationOn
     *            true if validation is to be set on.
     */
    public void setValidationOn(boolean validationOn) {
        this.validationOn = validationOn;
    }

    /**
     * Returns the OrderOption to be validated.
     * 
     * @return The option
     */
    public OrderOption getOpt() {
        return opt;
    }

    /**
     * Sets the OrderOption to be validated
     * 
     * @param opt
     *            The option
     */
    public void setOpt(OrderOption opt) {
        this.opt = opt;
    }

    /**
     * Sets indicator to determine if parent component is an OrderOption
     * 
     * @param b
     *            true if parent is an OrderOption
     */
    public void setIsParentAnOrderOption(boolean b) {
        this.isParentAnOrderOption = b;
    }

    /**
     * Returns indicator to show that parent is an OrderOption
     * 
     * @return true if parent is an OrderOption
     */
    public boolean isParentAnOrderOption() {
        return this.isParentAnOrderOption;
    }

    /**
     * Sets an indicator to show if the component is required.
     * 
     * @param req
     *            false if component is not required
     */
    public void setRequired(boolean req) {
        this.required = req;
    }

    /**
     * Empty implementation of setting of minimum value to validate against.
     * 
     * @param min
     *            The minimum value
     */
    public void setMinVal(String min) {
    }

    /**
     * Returns an indicator to show if the component has been validated
     * successfully.
     * 
     * @param s
     *            The string to validate
     * @return true if valid
     */
    public boolean isValid(String s) {
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
     * Checks that a required field has had some data entered.
     * 
     * @param value
     *            The string to check
     * @return true if required field contains data, false if required field
     *         contains no data, otherwise true
     */
    private boolean checkLength(String value) {
        if (required) {
            if (value.length() > 0) {
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
