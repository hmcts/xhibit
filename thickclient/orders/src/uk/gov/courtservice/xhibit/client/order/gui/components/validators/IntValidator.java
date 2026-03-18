package uk.gov.courtservice.xhibit.client.order.gui.components.validators;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Validates integer values.
 * </p>
 * <p>
 * Description: Validates integer values and utilises a JOptionPane to display
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
/*
 * Ref Date Author Description
 * 
 * 52878 04-09-2003 AW Daley Error messages now read from error text resource
 * bundle.
 * 
 * The error message "Ensure a valid integer value is entered." changed to
 * "Enter numeric characters only"
 */
public class IntValidator extends JOptionPane implements GuiValidator {

    private static final Logger log = CSServices.getLogger(IntValidator.class);

    // Error Message Keys
    private static final String REQUIRED_FIELD_ERROR_MESSAGE = "orders.requiredFieldErrorMessage";

    private static final String REQUIRED_FIELD_ERROR_TITLE = "orders.requiredFieldErrorTitle";

    private static final String MIN_VALUE_ERROR_MESSAGE = "orders.minValueErrorMessage";

    private static final String MIN_VALUE_ERROR_TITLE = "orders.minValueErrorTitle";

    private static final String NUMERIC_VALUE_ERROR_MESSAGE = "orders.numericValueErrorMessage";

    private static final String NUMERIC_VALUE_ERROR_TITLE = "orders.numericValueErrorTitle";

    private static final String RANGE_CHECK_ERROR_MESSAGE1 = "orders.rangeCheckErrorMessage1";

    private static final String RANGE_CHECK_ERROR_MESSAGE2 = "orders.rangeCheckErrorMessage2";

    private static final String RANGE_CHECK_ERROR_TITLE = "orders.rangeCheckErrorTitle";

    // Error Text Resource Bundle
    private ResourceBundle errorResources = XHIBITConstant.getResourceBundle(XhibitBundles.ErrorText);

    // min value the int should be.
    private int minVal;

    // true if the field must contain some data.
    private boolean required;

    // message displayed to the user.
    private String errorMessage;

    // title of error message, appears as title on JOptionPane.
    private String errorTitle;

    private boolean isParentAnOrderOption;

    private OrderOption opt;

    private boolean validationOn = true;

    private int maxVal = 0; // DJ

    /**
     * Constructs an int validator with a default min value = 0, and required =
     * true.
     */
    public IntValidator() {
        this.minVal = 0;
        this.required = true;
    }

    /**
     * Returns the OrderOption to be validated
     * 
     * @return The option
     */
    public OrderOption getOpt() {
        return opt;
    }

    /**
     * Indication of whether validation is set on
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
     *            true if validation is on
     */
    public void setValidationOn(boolean validationOn) {
        this.validationOn = validationOn;
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
     * Indication of whether the parent of the component is an OrderOption
     * 
     * @return true if parent is an OrderOption
     */
    public boolean isParentAnOrderOption() {
        return this.isParentAnOrderOption;
    }

    /**
     * Sets indicator if parent of component is an OrderOption
     * 
     * @param b
     *            true if parent is an OrderOption
     */
    public void setIsParentAnOrderOption(boolean b) {
        this.isParentAnOrderOption = b;
    }

    /**
     * Set minimum value the int can take.
     * 
     * @param minVal
     *            String value which is parsed to am int.
     */
    public void setMinVal(String minVal) {
        if (minVal != null) {
            this.minVal = Integer.parseInt(minVal);
        }
    }

    // Start SCR 52805
    /**
     * Sets the maximum value to be validated against
     * 
     * @param maxVal
     *            The maximul allowable value
     */
    public void setMaxVal(String maxVal) {
        if (maxVal != null) {
            this.maxVal = Integer.parseInt(maxVal);
        }
    }

    // End SCR 52805

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
     * Validates field by checking if it contains a valid int within the correct
     * range.
     * 
     * @param value
     *            String to be parsed to an int and validated.
     * @return true if field contents are valid.
     */
    public boolean isValid(String value) {
        if (validationOn) {
            if ((isLengthValid(value)) && (isValidInt(value))) {
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
    private boolean isLengthValid(String value) {
        if (required) {
            if (value.length() > 0) {
                return true;
            } else {
                this.errorMessage = errorResources.getString(REQUIRED_FIELD_ERROR_MESSAGE);
                this.errorTitle = errorResources.getString(REQUIRED_FIELD_ERROR_TITLE);
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Checks the field contains a valid int.
     * 
     * @param value
     *            String to be parsed to an int.
     * @return true if a string is parsed to an int successfully.
     */
    private boolean isValidInt(String value) {
        if (value.length() > 0) {
            try {
                if (Integer.parseInt(value) >= minVal) {
                    return true;
                } else {
                    this.errorMessage = errorResources.getString(MIN_VALUE_ERROR_MESSAGE) + " " + minVal;
                    this.errorTitle = errorResources.getString(MIN_VALUE_ERROR_TITLE);
                    return false;
                }
            } catch (NumberFormatException nfe) {
                this.errorMessage = errorResources.getString(NUMERIC_VALUE_ERROR_MESSAGE);
                this.errorTitle = errorResources.getString(NUMERIC_VALUE_ERROR_TITLE);
                return false;
            }
        } else {
            return true;
        }
    }

    // Start SCR 53247 Check range of value entered in the textfield
    /**
     * Checks if a given value falls between a specified range of values
     * 
     * @param value
     *            The value to be checked
     * @return true if value is within the range
     */
    public boolean isRangeValid(String value) {
        if (value.length() > 0) {
            if (Integer.parseInt(value) >= minVal && Integer.parseInt(value) <= maxVal) {
                return true;
            } else {
                StringBuffer err = new StringBuffer(errorResources.getString(RANGE_CHECK_ERROR_MESSAGE1) + " ");
                err.append(minVal);
                err.append(" " + errorResources.getString(RANGE_CHECK_ERROR_MESSAGE2) + " ");
                err.append(maxVal);
                this.errorMessage = err.toString();
                this.errorTitle = errorResources.getString(RANGE_CHECK_ERROR_TITLE);
                this.showMessageDialog(null, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    // End SCR 53247

    /**
     * Returns the trimmed int value as a string
     * 
     * @param value
     *            The value to be checked
     * @return the trimmed number with preceding 0's removed
     */
    public String getIntAsString(String value) {
        if (isValid(value)) {
            return new Integer(value).toString();

        } else {
            return value;
        }
    }
}
