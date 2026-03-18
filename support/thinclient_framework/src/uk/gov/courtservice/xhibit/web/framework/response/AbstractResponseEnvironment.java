package uk.gov.courtservice.xhibit.web.framework.response;

import uk.gov.courtservice.xhibit.web.framework.control.AbstractControlEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Abstract Response Environment
 * </p>
 * <p>
 * Description: The environment to evaluate an response in. This provides common
 * functionality for classes wanting to implement the ResponseEnvironment
 * interface. The primary purpose of this is to allow responses to be decopuled
 * from the servlet environment to facilitate testing, a simple testing class
 * could be writen to replace the ServletResponseEnvironment.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 * 
 * $Revision: 1.3 $ $Log: AbstractResponseEnvironment.java,v $
 * $Revision: 1.3 $ Revision 1.3  2006/06/05 12:30:25  bzjrnl
 * $Revision: 1.3 $ Change: TI901
 * $Revision: 1.3 $ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * $Revision: 1.3 $ $Revision: 1.3 $
 * Revision 1.2 2006/05/31 14:23:54 bzjrnl $Revision: 1.3 $ Change: TI901
 * $Revision: 1.3 $ Comment: Weblogic Upgrade - Standadise code formatting
 * $Revision: 1.3 $ Revision 1.1 2003/03/21 11:48:23 fz0n8j Revised thinclient
 * framework!
 * 
 * Revision 1.2 2003/03/19 12:28:03 fz0n8j Framework update.
 * 
 * Revision 1.1 2003/03/19 11:38:09 fz0n8j Promoted response to a first class
 * object.
 * 
 */

public abstract class AbstractResponseEnvironment extends AbstractControlEnvironment implements ResponseEnvironment {

    /**
     * <p>
     * Method for forwarding (server side) a request, through a
     * RequestDespatcher obtained from the ServletContext.
     * </p>
     * 
     * @param path
     *            a String specifying the location of the resource
     * @throws IllegalArgumentException
     *             if the path is null or an invalid location
     * @throws IllegalStateException
     *             if it or redirect has been called before
     * @throws FrameworkException
     *             if an error occurs
     */
    public void forward(String path) throws IllegalArgumentException, IllegalStateException, FrameworkException {
        if (path == null) {
            throw new IllegalArgumentException("path");
        }
        internalForward(path);
    }

    /**
     * <p>
     * Method for redirecting (client side) a request, through the response it
     * provides symatry with the forward (server side) method. Note before
     * sending we check the path is valid.
     * </p>
     * 
     * @param location
     *            specifying the location of the resource
     * @throws IllegalArgumentException
     *             if the location is null or an invalid location
     * @throws IllegalStateException
     *             if it or forward has been called before
     * @throws FrameworkException
     *             if an error occurs
     */
    public void redirect(String location) throws IllegalArgumentException, IllegalStateException, FrameworkException {
        if (location == null) {
            throw new IllegalArgumentException("location");
        }
        internalRedirect(location);
    }

    /**
     * <p>
     * Abstract method called by public interface method
     * </p>
     * 
     * @param path
     *            a String specifying the location of the resource
     * @throws IllegalStateException
     *             if it or redirect has been called before
     * @throws FrameworkException
     *             if an error occurs
     */
    protected abstract void internalForward(String path) throws IllegalStateException, FrameworkException;

    /**
     * <p>
     * Abstract method called by public interface method
     * </p>
     * 
     * @param location
     *            specifying the location of the resource
     * @throws IllegalStateException
     *             if it or forward has been called before
     * @throws FrameworkException
     *             if an error occurs
     */
    protected abstract void internalRedirect(String location) throws IllegalStateException, FrameworkException;

}
