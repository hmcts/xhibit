package uk.gov.courtservice.xhibit.web.framework.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.gov.courtservice.xhibit.web.framework.response.AbstractResponseEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Servlet Response Environment
 * </p>
 * <p>
 * Description: This class is a thin adapter around the the servlet request
 * allowing us to very efficiently pass parameters in a servlet environment
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 */

public class ServletResponseEnvironment extends AbstractResponseEnvironment {

    /**
     * We do not store data directly in the session we store it inside a map
     * (this isolates the mappings from the general session)
     */

    // private static final String SESSION_DATA_KEY = "session.data";
    /**
     * The wrapped HttpServletRequest
     */
    private final ServletEnvironment wrapped;

    /**
     * Constructs a ServletMappingEnvironment around the HttpServletRequest
     * 
     * @param request
     *            the HttpServletRequest to be wrapped
     * @param response
     *            the HttpServletResponse to be wrapped
     */
    public ServletResponseEnvironment(HttpServletRequest request, HttpServletResponse response) {
        wrapped = new ServletEnvironment(request, response);
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
    protected void internalForward(String path) throws IllegalStateException, FrameworkException {
        wrapped.forward(path);
    }

    /**
     * <p>
     * Abstract method called by public interface method
     * </p>
     * 
     * @param mimeType
     *            a String specifying the mimetype
     * @param content
     *            the bytes to send . . .
     * @throws FrameworkException
     *             if an error occurs
     */
    public void sendBytes(String mimeType, byte[] content) throws FrameworkException {
        wrapped.sendBytes(mimeType, content);
    }

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
    protected void internalRedirect(String location) throws IllegalStateException, FrameworkException {
        wrapped.redirect(location);
    }

    /**
     * <p>
     * Returns the current name of the logged on user
     * </p>
     * 
     * @return the user name
     */
    public String getUserName() {
        return wrapped.getUserName();
    }

    /**
     * <p>
     * Implements AbstractMappingRequest abstract method. The abstract method
     * for retrieving the parameter for the given name, should retund null if
     * the parameter does not exist.
     * </p>
     * 
     * @param name
     *            a String specifying the name of the mapping request parameter
     * @return A String containing the value of the mapping request parameter or
     *         null
     */
    protected Object internalGetRequestParameter(String name) {
        return wrapped.getRequestParameter(name);
    }

    /**
     * <p>
     * The abstract method for setting the parameter for the given name.
     * 
     * @param name
     *            a String specifying the name of the mapping request parameter
     * @param parameter
     *            the parameter value Object
     */
    protected void internalSetRequestParameter(String name, Object parameter) {
        wrapped.setRequestParameter(name, parameter);
    }

    /**
     * <p>
     * Implements AbstractMappingRequest abstract method. The abstract method
     * for retrieving the parameter for the given name, should return null if
     * the parameter does not exist.
     * </p>
     * 
     * @param name
     *            a String specifying the name of the mapping session parameter
     * @return A String containing the value of the mapping session parameter or
     *         null
     */
    protected Object internalGetSessionParameter(String name) {
        return wrapped.getSessionParameter(name);
    }

    /**
     * <p>
     * The abstract method for setting the parameter for the given name.
     * 
     * @param name
     *            a String specifying the name of the mapping session parameter
     * @param parameter
     *            the parameter value Object
     */
    protected void internalSetSessionParameter(String name, Object parameter) {
        wrapped.setSessionParameter(name, parameter);
    }

    /**
     * <p>
     * Method returning the names of all the parameters available on the
     * request, of particular use when using checkboxes
     * </p>
     * 
     * @return an enumeration of the parameter names on this request.
     */
    public java.util.Enumeration getRequestParameterNames() {
        return wrapped.getRequestParameterNames();
    }
}
