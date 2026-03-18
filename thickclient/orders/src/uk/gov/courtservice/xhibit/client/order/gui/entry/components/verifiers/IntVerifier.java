package uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers;

import java.awt.Color;
import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class IntVerifier extends OrderInputVerifier {
    private static final Logger log = CSServices.getLogger(IntVerifier.class);

    // Error Text Resource Bundle
    private ResourceBundle errorResources = XHIBITConstant.getResourceBundle(XhibitBundles.ErrorText);

    // min value the int should be.
    private int minVal;

    // message displayed to the user.
    private String errorMessage;

    // title of error message, appears as title on JOptionPane.
    private String errorTitle;

    private boolean isParentAnOrderOption;

    private OrderOption opt;

    private boolean validationOn = true;

    private boolean numeric = false;

    private OrderComponentHelper helper;

    private int maxVal = 0; // DJ

    // Indicate if a range of values should be checked
    private boolean validateRange = false;

    private Component component;

    /**
     * Constructor for IntVerifier
     */
    public IntVerifier() {
        super();
        setRequired(true);
    }

    /**
     * Verify the component
     * 
     * @param input
     *            the component to verify
     * @return true if varification is okay
     */
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        input.setBorder(UIManager.getBorder("TextField.border"));
        boolean verified = isValidString(text);
        if (verified && isNumeric()) {
            if (isValidInt(text)) {
                verified = isRangeValid(text);
            } else {
                verified = false;
            }
        }

        if (verified && getHelper() != null) {
            getHelper().setValue(text);
        }
        return verified;
    }

    /**
     * Indicates if the component should allow focus to be yielded
     * 
     * @param input
     *            the component to check
     * @return true if focus can be yielded
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
    protected boolean isLengthValid(String value) {
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
    protected boolean isValidInt(String value) {
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
        if (value.length() > 0 && validateRange) {
            if (Integer.parseInt(value) >= minVal && Integer.parseInt(value) <= maxVal) {
                return true;
            } else {
                StringBuffer err = new StringBuffer(errorResources.getString(RANGE_CHECK_ERROR_MESSAGE1) + " ");
                err.append(minVal);
                err.append(" " + errorResources.getString(RANGE_CHECK_ERROR_MESSAGE2) + " ");
                err.append(maxVal);
                this.errorMessage = err.toString();
                this.errorTitle = errorResources.getString(RANGE_CHECK_ERROR_TITLE);
                return false;
            }
        } else {
            return true;
        }
    }

    // End SCR 53247
    /**
     * Validates field by checking if it contains a valid string.
     * 
     * @param s
     *            String to be validated.
     * @return true if field contents are valid.
     */
    public boolean isValidString(String s) {
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
     * Indicates if the field to verify is numeric
     * 
     * @return true if the field is numeric
     */
    public boolean isNumeric() {
        return numeric;
    }

    /**
     * Set the numeric flag
     * 
     * @param num
     *            true if numeric
     */
    public void setIsNumeric(boolean num) {
        numeric = num;
    }

    /**
     * Set the validateRange flag
     * 
     * @param val
     *            true if range to be checked
     */
    public void setValidateRange(boolean val) {
        validateRange = val;
    }

    /**
     * Indicates if the field to be validatade against a range
     * 
     * @return true if the field is to be within a range
     */
    public boolean isValidateRange() {
        return validateRange;
    }
}