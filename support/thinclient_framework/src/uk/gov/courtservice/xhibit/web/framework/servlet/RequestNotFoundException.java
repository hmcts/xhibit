package uk.gov.courtservice.xhibit.web.framework.servlet;

/**
 * <p>
 * Title: Request Not Found Exception
 * </p>
 * <p>
 * Description: Thrown when a requested request can not be found
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
 *         RequestNotFoundException.java,v $ Revision 1.3 2003/03/21 11:48:27
 *         fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:06 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:44 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class RequestNotFoundException extends RuntimeException {
    /**
     * <p>
     * Constructs an RequestNotFoundException with the request name as its
     * message.
     * </p>
     * 
     * @param requestName
     *            a String containing the name of the request that can not be
     *            found
     */
    public RequestNotFoundException(String requestName) {
        super(requestName);
    }
}
