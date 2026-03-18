package uk.gov.courtservice.xhibit.web.framework.util;

/**
 * <p>
 * Title: Property Not Found Exception
 * </p>
 * <p>
 * Description: Thrown when a requested property can not be found
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
 *         PropertyNotFoundException.java,v $ Revision 1.3 2003/03/21 11:48:31
 *         fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:09 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class PropertyNotFoundException extends RuntimeException {
    /**
     * <p>
     * Constructs an PropertyNotFoundException with the property name as its
     * message.
     * </p>
     * 
     * @param propertyName
     *            a String containing the name of the property that can not be
     *            found
     */
    public PropertyNotFoundException(String propertyName) {
        super(propertyName);
    }
}
