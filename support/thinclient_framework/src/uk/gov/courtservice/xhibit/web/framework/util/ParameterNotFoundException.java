package uk.gov.courtservice.xhibit.web.framework.util;

/**
 * <p>
 * Title: Parameter Not Found Exception
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
 *         ParameterNotFoundException.java,v $ Revision 1.3 2003/03/21 11:48:30
 *         fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:07 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:44 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class ParameterNotFoundException extends RuntimeException {
    /**
     * <p>
     * Constructs an ParameterNotFoundException with the parameter name as its
     * message.
     * </p>
     * 
     * @param parameterName
     *            a String containing the name of the parameter that can not be
     *            found
     */
    public ParameterNotFoundException(String parameterName) {
        super(parameterName);
    }
}
