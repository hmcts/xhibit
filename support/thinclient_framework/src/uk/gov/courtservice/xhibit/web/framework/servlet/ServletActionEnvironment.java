package uk.gov.courtservice.xhibit.web.framework.servlet;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.gov.courtservice.xhibit.web.framework.action.AbstractActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Servlet Action Environment
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
 * @author William Fardell
 */

public class ServletActionEnvironment extends AbstractActionEnvironment {

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
    public ServletActionEnvironment(HttpServletRequest request, HttpServletResponse response) {
        wrapped = new ServletEnvironment(request, response);
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
     * Get the external url of a local resource
     * 
     * @return url the external url
     * @throws IllegalArgumentException
     *             if resource is null
     * @throws FrameworkException
     *             if an error occures
     */
    public String getExternalUrl(String resource) throws IllegalArgumentException, FrameworkException {
        if (resource == null) {
            throw new IllegalArgumentException("resource");
        }
        return wrapped.getExternalUrl(resource);
    }

    /**
     * <p>
     * Get the session id
     * 
     * @return the session id or null if no session id
     */
    public String getRequestedSessionId() {
        return wrapped.getRequestedSessionId();
    }

    /**
     * <p>
     * Get the name of the remote host
     * 
     * @return the remote host
     */
    public String getRemoteHost() {
        return wrapped.getRemoteHost();
    }

    /**
     * <p>
     * Get the address of the remote host
     * 
     * @return the remote host
     */
    public String getRemoteAddress() {
        return wrapped.getRemoteAddress();
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

    /**
     * <p>
     * Returns wheather the user is in a given role
     * </p>
     * 
     * @param role
     *            the name of the role
     * @return the user name
     */
    public boolean isUserInRole(String role) {
        return wrapped.isUserInRole(role);
    }

    /**
     * <p>
     * Get the remote locale.
     * <p>
     * 
     * @return the locale of the remote machine
     */
    public Locale getRemoteLocale() {
        return wrapped.getRemoteLocale();
    }

    /**
     * Set the cookie value
     */
    public void setCookie(String name, String value) {
        wrapped.setCookie(name, value);
    }

    /**
     * Get the cookie value
     */
    public String getCookie(String name) {
        return wrapped.getCookie(name);
    }

    /**
     * Logout the current client.
     */
    public boolean logout() {
        return wrapped.logout();
    }

}
