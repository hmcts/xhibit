package uk.gov.courtservice.framework.services.conversion;

import java.io.Serializable;

import uk.gov.courtservice.framework.exception.CSBusinessException;

public class ValueConvertException extends CSBusinessException implements Serializable {

    private String propertyName;

    private Class propertyType;

    private String propertyValue;

    private static final String VALUE_CONVERSION_ERROR_KEY = "framework.services.conversion.error";

    public ValueConvertException(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    /**
     * 
     * @param propertyValue
     * @param e
     *            the initial cause
     */
    public ValueConvertException(String propertyValue, Throwable e) {
        super(VALUE_CONVERSION_ERROR_KEY, "propValue=" + propertyValue, e);
        this.propertyValue = propertyValue;
    }

    public ValueConvertException(String propertyName, Class propertyType, String propertyValue, Throwable e) {
        // following pattern established in ValueConvertException(String
        // propertyName,
        // Class propertyType,
        // String propertyValue)
        super(VALUE_CONVERSION_ERROR_KEY, "propValue=" + propertyValue + "propertyName=" + propertyName, e);
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.propertyValue = propertyValue;
    }

    /**
     * An ConvertException has a name, type and value.
     * 
     * @param propertyName
     *            identifies the field whose parse failed.
     * @param propertyType
     *            is the type required, e.g. java.util.Date.class, or
     *            Double.TYPE.
     * @param propertyValue
     *            is the actual value that failed to parse.
     */
    public ValueConvertException(String propertyName, Class propertyType, String propertyValue) {

        super(VALUE_CONVERSION_ERROR_KEY, "propertyValue=" + propertyValue + "propertyName=" + propertyName);
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class getPropertyType() {
        return propertyType;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
}