package uk.gov.courtservice.xhibit.client.order.gui.entry.components.verifiers;

import java.awt.Component;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: OrderInputVerifier:
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

public abstract class OrderInputVerifier extends InputVerifier {
    private static final Logger log = CSServices.getLogger(OrderInputVerifier.class);

    // Error Message Keys
    protected static final String REQUIRED_FIELD_ERROR_MESSAGE = "orders.requiredFieldErrorMessage";

    protected static final String REQUIRED_FIELD_ERROR_TITLE = "orders.requiredFieldErrorTitle";

    protected static final String MIN_VALUE_ERROR_MESSAGE = "orders.minValueErrorMessage";

    protected static final String MIN_VALUE_ERROR_TITLE = "orders.minValueErrorTitle";

    protected static final String NUMERIC_VALUE_ERROR_MESSAGE = "orders.numericValueErrorMessage";

    protected static final String NUMERIC_VALUE_ERROR_TITLE = "orders.numericValueErrorTitle";

    protected static final String RANGE_CHECK_ERROR_MESSAGE1 = "orders.rangeCheckErrorMessage1";

    protected static final String RANGE_CHECK_ERROR_MESSAGE2 = "orders.rangeCheckErrorMessage2";

    protected static final String RANGE_CHECK_ERROR_TITLE = "orders.rangeCheckErrorTitle";

    protected static final String VALUE_IN_LIST_CHECK_ERROR_TITLE = "orders.offenceCodeListCheckErrorTitle";
    
    protected static final String VALUE_IN_LIST_CHECK_ERROR_MESSAGE = "orders.offenceCodeListCheckErrorMessage";

    public static final String ADDRESS_TYPE = "address";

    public static final String POSTCODE_TYPE = "postcode";

    public static final String STRING_TYPE = "string";
    
    public static final String NAME_TYPE = "name";
    
    public static final String ALPHANUMERIC_TYPE = "alphanumeric";

    public static final String INT_TYPE = "int";

    public static final String COMBO_TYPE = "combo";

    // Error Text Resource Bundle
    private ResourceBundle errorResources = XHIBITConstant.getResourceBundle(XhibitBundles.ErrorText);

    // min value the int should be.
    private int minVal;

    // true if the field must contain some data.
    protected boolean required = false;

    // message displayed to the user.
    private String errorMessage;

    // title of error message, appears as title on JOptionPane.
    private String errorTitle;

    private boolean isParentAnOrderOption;

    private OrderOption opt;

    public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorTitle() {
		return errorTitle;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

	private boolean validationOn = true;

    private boolean numeric = false;

    private OrderComponentHelper helper;

    private int maxVal = 0; // DJ

    // Indicate if a range of values should be checked
    private boolean validateRange = false;

    private Component component;
    
    // Allow to validate against an list of values
    private boolean validateInArray = false;
    
    private ArrayList<String> validValuesList;

    /**
     * Constructor for IntVerifier
     */
    public OrderInputVerifier() {
    }

    /**
     * Verify the component
     * 
     * @param input
     *            the component to verify
     * @return true if varification is okay
     */
    public abstract boolean verify(JComponent input);

    /**
     * Indicates if the component should allow focus to be yielded
     * 
     * @param input
     *            the component to check
     * @return true if focus can be yielded
     */
    public boolean shouldYieldFocus(JComponent input) {
        return super.shouldYieldFocus(input);
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
    public abstract boolean isValid(String value);

    // {
    // if (validationOn)
    // {
    // if ((isLengthValid(value)) && (isValidInt(value)))
    // {
    // return true;
    // }
    // else
    // {
    // return false;
    // }
    // }else
    // {
    // return true;
    // }
    // }

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
                setErrorMessage(errorResources.getString(REQUIRED_FIELD_ERROR_MESSAGE));
                setErrorTitle(errorResources.getString(REQUIRED_FIELD_ERROR_TITLE));
                return false;
            }
        } else {
            return true;
        }
    }

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
     * Checks that the value passed in exists in the definitive
     * list of values allowed.
     * 
     * @param s
     * @return
     */
    public boolean isValidValueInList(String s, ArrayList listOfValues) {
    	log.debug("$$$>>> isValidValueInList |" + s + "|");
        log.debug("$$$>>> validationOn |" + validationOn + "|");
        if (validationOn) {
        	if (listOfValues == null || listOfValues.size() == 0) {
        		return false;
        	}
        	
        	boolean exists = false;
        	int i = 0;
        	while (!exists && i<listOfValues.size()) {
        		String listValue = listOfValues.get(i).toString(); 
        		if (s.equalsIgnoreCase(listValue)) {
        			return true;
        		}
        		i++;
        	}
        	setErrorMessage(errorResources.getString(VALUE_IN_LIST_CHECK_ERROR_MESSAGE));
            setErrorTitle(errorResources.getString(VALUE_IN_LIST_CHECK_ERROR_TITLE));
        	return false;
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
    protected abstract boolean checkLength(String value);

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
     * Indicates if the field to be validate against a range
     * 
     * @return true if the field is to be within a range
     */
    public boolean isValidateRange() {
        return validateRange;
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
     * Returns the OrderComponentHelper
     * 
     * @return the helper
     */
    public OrderComponentHelper getHelper() {
        return this.helper;
    }

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

    /**
     * Set required to true or false.
     * 
     * @param req
     *            String value which is parsed to the correct boolean value.
     */
    public void setRequired(boolean req) {
        this.required = req;
    }

	public boolean isValidateInArray() {
		return validateInArray;
	}

	public void setValidateInArray(boolean validateInArray) {
		this.validateInArray = validateInArray;
	}

	public ArrayList<String> getValidValuesList() {
		return validValuesList;
	}

	public void setValidValuesList(ArrayList<String> validValuesList) {
		this.validValuesList = validValuesList;
	}

}