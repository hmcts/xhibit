package uk.gov.courtservice.xhibit.web.framework.util;

/**
 * <p>
 * Title: Resource Not Found Exception
 * </p>
 * <p>
 * Description: Thrown when a requested resource can not be found
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
 *         ResourceNotFoundException.java,v $ Revision 1.3 2003/03/21 11:49:24
 *         fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:09 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * <p>
     * Constructs an ResourceNotFoundException with the resource name as its
     * message.
     * </p>
     * 
     * @param resourceName
     *            a String containing the name of the resource that can not be
     *            found
     */
    public ResourceNotFoundException(String resourceName) {
        super(resourceName);
    }
}
