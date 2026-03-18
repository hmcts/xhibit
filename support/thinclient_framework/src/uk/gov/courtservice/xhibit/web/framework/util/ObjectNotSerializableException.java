package uk.gov.courtservice.xhibit.web.framework.util;

/**
 * <p>
 * Title: Object Not Serializable Exception
 * </p>
 * <p>
 * Description: Thrown when an object to be added is not serializable
 * </p>
 */
public class ObjectNotSerializableException extends RuntimeException {
    /**
     * <p>
     * Constructs an ObjectNotSerializableException with the property name as
     * its message.
     * </p>
     * 
     * @param propertyName
     *            a String containing the name of the object that can not be
     *            serialized
     */
    public ObjectNotSerializableException(String propertyName) {
        super(propertyName);
    }
}
