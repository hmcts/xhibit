package uk.gov.courtservice.framework.services.validation;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Validator
 * </p>
 * <p>
 * Description: Provides a means of validating attributes against a specified
 * schema or for validation of attributes in a validation object:
 * </p>
 * <p>
 * To validate against a specified schema:
 * <ul>
 * <li> Get the required instance of the schema useing
 * <code>getInstance(String schemaName)</code>
 * <li> Validate each attribute using
 * <code>isValidAttribute(String schemaName, String dataValue)</code>
 * </ul>
 * </p>
 * <p>
 * To validate a ValueObject
 * <ul>
 * <li>Use the <code>validateValue(CSValueObject)</code> method
 * </ul>
 * <b>Important: schema attribute name vos are case sensitive and should exactly
 * match the attrubute name in the corresponding ValueObject. The validation
 * object must inherit from <code>CSAbractValue</code> or implement the
 * <code>CSValueObject</code> interface.</b> <p/>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class Validator {
    private static Map validators;

    private static String GENERAL_USER_MESSAGE = "validation.general";

    private static Logger log = CSServices.getLogger(Validator.class);

    private Map constraints;

    private String schemaName;

    /**
     * <p>
     * Validate a value object. Will validate a value object as true if no xsd
     * schema detailing contraints are found. The assuption is made that if no
     * such schema is available, then there are no constraints on the value
     * object. Similarly, if the schema contains no reference to an attribute on
     * the Validation object the assumption is that there are no constraits for
     * that attribute and will therefore be processed as valid.
     * </p>
     * <p>
     * A CSConfigurationException may be thrown if the schema is invalid.
     * </p>
     * 
     * @param checkValue
     *            value object to validate
     * @return true if no validation errors occur
     * @throws CSValidationException
     *             contains details of errors collected during validation
     */
    public static synchronized boolean validateValue(CSValueObject checkValue) throws CSValidationException {
        // get the name of the value object
        String classname = checkValue.getClass().getName();
        try {
            Validator validation = Validator.getInstance(classname);

            // if performance becomes an issue, consider cacheing methods
            // and attributes against the class name.
            Field[] attributes = checkValue.getClass().getDeclaredFields();
            Vector methods = new Vector();
            PropertyDescriptor[] properties = Introspector.getBeanInfo(checkValue.getClass()).getPropertyDescriptors();
            for (int i = 0; i < properties.length; i++) {
                methods.add(properties[i].getReadMethod());
            }

            for (int i = 0; i < attributes.length; i++) {
                String aTTribute = (attributes[i].getName());
                String attribute = aTTribute.toLowerCase();

                // match methods with attributes
                Iterator it = methods.iterator();
                while (it.hasNext()) {
                    Method method = (Method) it.next();
                    String methodName = method.getName().toLowerCase();

                    if (methodName.endsWith(attribute)) {
                        String val = "" + method.invoke(checkValue, new Object[] {});

                        if (!val.equals("null")) {
                            validation.isValidAttribute(aTTribute, val);
                        }
                        break;
                    }
                }
            }
        } catch (IntrospectionException e) {
            CSServices.getDefaultErrorHandler().handleError(e, Validator.class,
                    "Continue: assume attribute value is valid.");
        } catch (InvocationTargetException e) {
            CSServices.getDefaultErrorHandler().handleError(e, Validator.class,
                    "Continue: assume attribute value is valid.");
        } catch (IllegalAccessException e) {
            CSServices.getDefaultErrorHandler().handleError(e, Validator.class,
                    "Continue: assume attribute value is valid.");
        }

        return true;
    }

    private Validator(String schemaName) {
        if (log.isDebugEnabled()) {
            log.debug("START: Validator( String schemaName )");
            log.debug("ATTRIBUTE: schemaName: " + schemaName);
        }
        this.schemaName = schemaName;
        SchemaParser sp = new SchemaParser(schemaName);

        constraints = sp.getContraints();
        if (log.isDebugEnabled())
            log.debug("Constraints loaded: " + constraints);
    }

    /**
     * @param schemaName
     *            schema name as defined in the validation.properties file
     * @return a Validator instance for the specified schemaName
     */
    public static synchronized Validator getInstance(String schemaName) {
        Validator validator = null;

        if (validators == null) {
            if (log.isDebugEnabled())
                log.debug("validator = null");
            validators = new HashMap();
        }

        if (validators.containsKey(schemaName)) {
            if (log.isDebugEnabled())
                log.debug("validator contains key " + schemaName);
            validator = (Validator) validators.get(schemaName);
        } else {
            validator = new Validator(schemaName);
            validators.put(schemaName, validator);
            if (log.isDebugEnabled())
                log.debug("new validator created with key " + schemaName);
        }

        return validator;
    }

    /**
     * @param constraintName
     *            name matching constaint in schema
     * @param data
     *            value of attribute to be validated
     * @return true if attribute passes all constraints.
     * @throws CSValidationException
     *             contains details of validation errrors.
     */
    public boolean isValidAttribute(String constraintName, String data) throws CSValidationException {
        if (log.isDebugEnabled()) {
            log.debug("START: checkValidity(String,String)");
            log.debug("ATTRIBUTE: constrainName: " + constraintName + ", data: " + data);
        }

        StringBuffer errorMessages = new StringBuffer();
        Vector userMessages = new Vector();
        boolean isValid = true;
        Constraint constraint = null;

        Object o = constraints.get(constraintName);
        if (o != null) {
            if (log.isDebugEnabled())
                log.debug("constraint found");
            constraint = (Constraint) o;

            // check the data type
            if (log.isDebugEnabled())
                log.debug("check data type");
            if (!isCorrectDataType(constraint.getDataType(), data)) {
                errorMessages.append(schemaName).append(".").append(constraintName).append(
                        "\n Invalid data type. Value \"").append(data).append("\" is not of type ").append(
                        DataConverter.getJavaDataType(constraint.getDataType())).append(".");
                userMessages.add(new Message("validation.datatype", data));
            }

            // check the length
            if (log.isDebugEnabled())
                log.debug("check length");
            if (!isCorrectLength(constraint, data)) {
                errorMessages.append("\nInvalid parameter length for ").append(schemaName).append(".").append(
                        constraintName).append(" value: \"").append(data).append("\"");
                Message msg = null;
                if (constraint.hasMinLength()) {
                    if (constraint.hasMaxLength()) {
                        Object[] params = { data, constraint.getMinLength(), constraint.getMaxLength() };
                        msg = new Message("validation.length", params);
                    } else {
                        Object[] params = { data, constraint.getMinLength() };
                        msg = new Message("validation.shortlength", params);
                    }
                } else if (constraint.hasMaxLength()) {
                    Object[] params = { data, constraint.getMaxLength() };
                    msg = new Message("validation.longlength", params);
                }

                userMessages.add(msg);
            }

            // check the range
            if (log.isDebugEnabled())
                log.debug("check range");
            try {
                double testValue = Double.parseDouble(data);

                if (constraint.hasMaxInclusive()) {
                    if ((testValue > constraint.getMaxInclusive())) {
                        errorMessages.append("\nValue out of range. ").append(schemaName).append(".").append(
                                constraintName).append(" value: \"").append(data).append("\"");
                        Object[] params = { data, "" + constraint.getMaxInclusive() };
                        userMessages.add(new Message("validation.maxinclusive", params));
                    }
                    if (log.isDebugEnabled())
                        log.debug("maxInclusive: " + constraint.getMaxInclusive());

                }
                if (constraint.hasMaxExclusive()) {
                    if ((testValue >= constraint.getMaxExclusive())) {
                        errorMessages.append("\nValue out of range. ").append(schemaName).append(".").append(
                                constraintName).append(" value: \"").append(data).append("\"");
                        if (log.isDebugEnabled())
                            log.debug("maxExclusive fail");
                        Object[] params = { data, "" + constraint.getMaxExclusive() };
                        userMessages.add(new Message("validation.maxexclusive", params));
                    }
                    if (log.isDebugEnabled())
                        log.debug("maxExclusive: " + constraint.getMaxExclusive());

                }
                if (constraint.hasMinInclusive()) {
                    if ((testValue < constraint.getMinInclusive())) {
                        errorMessages.append("\nValue out of range. ").append(schemaName).append(".").append(
                                constraintName).append(" value: \"").append(data).append("\"");
                        if (log.isDebugEnabled())
                            log.debug("minInclusive fail");
                        Object[] params = { data, "" + constraint.getMinInclusive() };
                        userMessages.add(new Message("validation.mininclusive", params));
                    }
                    if (log.isDebugEnabled())
                        log.debug("minInclusive " + constraint.getMinInclusive());
                }
                if (constraint.hasMinExclusive()) {
                    if ((testValue <= constraint.getMinExclusive())) {
                        errorMessages.append("\nValue out of range. ").append(schemaName).append(".").append(
                                constraintName).append(" value: \"").append(data).append("\"");
                        Object[] params = { data, "" + constraint.getMinExclusive() };
                        userMessages.add(new Message("validation.minexclusive", params));
                    }
                    if (log.isDebugEnabled())
                        log.debug("minExclusive: " + constraint.getMinExclusive());
                }
            } catch (NumberFormatException e) {
                log
                        .fatal(
                                "thrown if data is not a number, so data is therefore invalid and would have failed previous test!",
                                e);
            }

            // check allowed vos
            if (log.isDebugEnabled())
                log.debug("check permitted vos");
            if (constraint.hasAllowedValues()) {
                if (!constraint.getAllowedValues().contains(data)) {
                    errorMessages.append("\nValue not permitted. ").append(schemaName).append(".").append(
                            constraintName).append(" value: \"").append(data).append("\"");
                    userMessages.add(new Message("validation.allowedvalue", data));
                }
            }
        } else {
            log.info("constraint " + constraintName + " not found. Assumption made that data is valid!");
        }

        if (log.isDebugEnabled())
            log.debug("END: checkValidity(String,String)");

        if (errorMessages.length() > 0) {
            throw new CSValidationException(GENERAL_USER_MESSAGE, errorMessages.toString(), userMessages);
        }
        return isValid; // will only return true, or an exception is thrown.
    }

    private boolean isCorrectLength(Constraint c, String data) {
        boolean isValid = true;
        int testValue = data.length();

        if (c.hasMinLength()) {
            if (testValue < c.getMinLength().intValue()) {
                isValid = false;
            }
        }
        if (isValid && c.hasMaxLength()) {
            if (testValue > c.getMaxLength().intValue()) {
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean isCorrectDataType(int dataType, String data) {
        if (log.isDebugEnabled()) {
            log.debug("isCorrectDataType( int dataType, String data )");
            log.debug("dataType = " + dataType);
            log.debug("data = " + data);
        }
        boolean isValid = true;

        try {
            switch (dataType) {
            case DataConverter.STRING:
                // isValid = true;
                break;
            case DataConverter.BYTE:
                new Byte(data);
                break;
            case DataConverter.DOUBLE:
                new Double(data);
                break;
            case DataConverter.FLOAT:
                new Float(data);
                break;
            case DataConverter.INT:
                new Integer(data);
                break;
            case DataConverter.LONG:
                new Long(data);
                break;
            case DataConverter.SHORT:
                new Short(data);
                break;
            default:
                log.warn("dataType not configured. DataType value = " + dataType);
            }
        } catch (Exception e) { // if the data is not of the correct data type
            // then an exception should be thrown.
            // return false.
            if (log.isDebugEnabled())
                log.debug("Exception in converting datatype " + e.toString());
            isValid = false;
        }
        if (log.isDebugEnabled())
            log.debug("isCorrectDataType( int dataType, String data ) return: " + isValid);
        return isValid;
    }

    /**
     * Obtains a collection of <code>Constraint</code> objects for the
     * speicified value object (if such a schema exists).
     * 
     * @param value
     *            the value object
     * @return collection of <code>Constraint</code> objects.
     */
    public static Collection getConstraints(CSValueObject value) {
        String name = value.getClass().getName();
        Validator validation = Validator.getInstance(name);
        return validation.constraints.values();

    }
}