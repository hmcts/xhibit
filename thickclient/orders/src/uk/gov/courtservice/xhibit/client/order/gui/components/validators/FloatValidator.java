package uk.gov.courtservice.xhibit.client.order.gui.components.validators;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;

/**
 * <p>
 * Title: Validates float values.
 * </p>
 * <p>
 * Description: Validates float values and utilises a JOptionPane to display
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
public class FloatValidator extends JOptionPane implements GuiValidator {
    private static final Logger log = CSServices.getLogger(FloatValidator.class);

    // min value the float should be.
    private float minVal;

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
     * Return the option
     * 
     * @return the option
     */
    public OrderOption getOpt() {
        return opt;
    }

    /**
     * Return the indicator if validation is on
     * 
     * @return true if validation is on
     */
    public boolean isValidationOn() {
        return validationOn;
    }

    /**
     * Sets the validation on
     * 
     * @param validationOn
     *            true if validation is on
     */
    public void setValidationOn(boolean validationOn) {
        this.validationOn = validationOn;
    }

    /**
     * Set the option
     * 
     * @param opt
     *            the option
     */
    public void setOpt(OrderOption opt) {
        this.opt = opt;
    }

    public boolean isParentAnOrderOption() {
        return this.isParentAnOrderOption;
    }

    /**
     * Constructs a float validator with a default min value = 0, and required =
     * true.
     */
    public FloatValidator() {
        this.minVal = 0;
        this.required = true;
    }

    public void setIsParentAnOrderOption(boolean b) {
        this.isParentAnOrderOption = b;
    }

    /**
     * Set minimum value the float can take.
     * 
     * @param val
     *            String value which is parsed to a float.
     */
    public void setMinVal(String val) {
        if (val != null) {
            this.minVal = Float.parseFloat(val);
        }
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
     * Validates field by checking if it contains a valid float within the
     * correct range.
     * 
     * @param value
     *            String to be parsed to a float and validated.
     * @return true if field contents are valid.
     */
    public boolean isValid(String value) {
        if (validationOn) {
            if ((checkLength(value)) && (checkFloat(value))) {
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
     * Checks that a value is entered and returns false only if required is set
     * to true.
     * 
     * @param value
     *            String contents of field.
     * @return true or false.
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

    /**
     * Checks the field contains a valid float.
     * 
     * @param value
     *            String to be parsed to a float.
     * @return true if a string is parsed to a float successfully.
     */
    private boolean checkFloat(String value) {
        log.debug("FloatValidator.checkFloat value = |" + value + "|");
        if (value.length() > 0) {
            try {

                if (Float.parseFloat(value) >= minVal) {
                    return true;
                } else {
                    this.errorMessage = "Value must be greater than " + minVal;
                    this.errorTitle = "Value too small";
                    return false;
                }

            } catch (NumberFormatException nfe) {
                this.errorMessage = "Ensure a valid decimal value is entered";
                this.errorTitle = "Invalid Value";
                return false;
            }
        } else {
            return true;
        }
    }

}
