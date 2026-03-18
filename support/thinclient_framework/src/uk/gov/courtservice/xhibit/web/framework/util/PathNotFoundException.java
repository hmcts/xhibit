package uk.gov.courtservice.xhibit.web.framework.util;

/**
 * <p>
 * Title: Path Not Found Exception
 * </p>
 * <p>
 * Description: Thrown when a requested path can not be found
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
 *         PathNotFoundException.java,v $ Revision 1.3 2003/03/21 11:48:30
 *         fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:08 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:44 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class PathNotFoundException extends RuntimeException {
    /**
     * <p>
     * Constructs a PathNotFoundException with the path as its message.
     * </p>
     * 
     * @param path
     *            a String containing the path of the resource that can not be
     *            found.
     */
    public PathNotFoundException(String path) {
        super(path);
    }
}
