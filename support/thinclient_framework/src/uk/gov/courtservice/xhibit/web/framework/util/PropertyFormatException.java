package uk.gov.courtservice.xhibit.web.framework.util;

/**
 * <p>
 * Title: Property Not Found Exception
 * </p>
 * <p>
 * Description: Thrown when a requested property can not be formated as
 * requested
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         PropertyFormatException.java,v $ Revision 1.3 2003/03/21 11:48:31
 *         fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:08 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class PropertyFormatException extends RuntimeException {
    /**
     * <p>
     * Constructs an PropertyFormatException with the property name, value and
     * type.
     * </p>
     * 
     * @param propertyName
     *            a String containing the name of the property
     * @param propertyValue
     *            a String containing the value of the property
     * @param propertyType
     *            a String containing the type we are trying to format too
     */
    public PropertyFormatException(String propertyName, String propertyValue, String propertyType) {
        super(propertyName + ": " + propertyValue + " - " + propertyType);
    }

}
