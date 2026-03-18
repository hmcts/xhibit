package uk.gov.courtservice.xhibit.web.framework.response;

/**
 * <p>
 * Title: Response Not Found Exception
 * </p>
 * <p>
 * Description: Thrown when a requested response can not be found
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.3 $ $Log:
 *         ResponseNotFoundException.java,v $ Revision 1.1 2003/03/21 11:48:25
 *         fz0n8j Revised thinclient framework!
 * 
 * Revision 1.1 2003/03/19 18:38:55 fz0n8j Framework changes.
 * 
 * Revision 1.3 2003/03/17 11:32:09 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class ResponseNotFoundException extends RuntimeException {
    /**
     * <p>
     * Constructs an ResponseNotFoundException with the response name as its
     * message.
     * </p>
     * 
     * @param responseName
     *            a String containing the name of the response that can not be
     *            found
     */
    public ResponseNotFoundException(String responseName) {
        super(responseName);
    }
}
