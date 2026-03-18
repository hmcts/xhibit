package uk.gov.courtservice.xhibit.web.framework.servlet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.PathNotFoundException;

/**
 * <p>
 * Title: Servlet Mapping Environment
 * </p>
 * <p>
 * Description: This class is a thin adapter around the the servlet request
 * allowing us to very efficiently pass parameters in a servlet environment <p/>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell
 */

public class ServletEnvironment {

    private static final Logger log = CSServices.getLogger(ServletEnvironment.class);

    /**
     * default max age for a cookie
     */
    private static final int DEFAULT_MAX_COOKIE_AGE = 31536000; // a year for

    // the moment

    /**
     * The wrapped HttpServletRequest
     */
    private final HttpServletRequest wrappedRequest;

    /**
     * The wrapped HttpServletResponse
     */
    private final HttpServletResponse wrappedResponse;

    /**
     * Constructs a ServletMappingEnvironment around the HttpServletRequest
     * 
     * @param newWrapped
     *            the HttpServletRequest to be wrappedRequest
     */
    public ServletEnvironment(HttpServletRequest newWrappedRequest, HttpServletResponse newWrappedResponse) {
        wrappedRequest = newWrappedRequest;
        wrappedResponse = newWrappedResponse;
    }

    /**
     * <p>
     * Forward to a local resource
     * </p>
     * 
     * @param path
     *            a String specifying the location of the resource
     * @throws IllegalStateException
     *             if it or redirect has been called before
     * @throws FrameworkException
     *             if an error occurs
     */
    public void forward(String path) throws IllegalStateException, FrameworkException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Forwarding request to \"" + path + "\".");
            }
            RequestDispatcher dispatcher = wrappedRequest.getRequestDispatcher(path);
            if (dispatcher != null) {
                dispatcher.forward(wrappedRequest, wrappedResponse);
            } else {
                throw new FrameworkException(new PathNotFoundException(path));
            }
        } catch (IOException ioe) {
            throw new FrameworkException(ioe);
        } catch (ServletException se) {
            throw new FrameworkException(se);
        }
    }

    /**
     * <p>
     * Send bytes to the client with a response mime type
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
        try {
            wrappedResponse.setContentType(mimeType);
            wrappedResponse.setContentLength(content.length);
            wrappedResponse.getOutputStream().write(content);
            wrappedResponse.getOutputStream().flush();
        } catch (IOException ioe) {
            throw new FrameworkException(ioe);
        }
    }

    /**
     * <p>
     * Redirect
     * </p>
     * 
     * @param location
     *            specifying the location of the resource
     * @throws IllegalStateException
     *             if it or forward has been called before
     * @throws FrameworkException
     *             if an error occurs
     */
    public void redirect(String location) throws IllegalStateException, FrameworkException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Redirecting to \"" + location + "\".");
            }
            wrappedResponse.sendRedirect(location);
        } catch (IOException ioe) {
            throw new FrameworkException(ioe);
        }
    }

    /**
     * <p>
     * Returns the current name of the logged on user
     * </p>
     * 
     * @return the user name
     */
    public String getUserName() {
        return wrappedRequest.getRemoteUser();
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
        return wrappedRequest.isUserInRole(role);
    }

    /**
     * <p>
     * The method for retrieving the parameter for the given name, should return
     * null if the parameter does not exist.
     * </p>
     * 
     * @param name
     *            a String specifying the name of the mapping request parameter
     * @return A String containing the value of the mapping request parameter or
     *         null
     */
    public Object getRequestParameter(String name) {
        Object parameter = wrappedRequest.getParameter(name);
        if (parameter == null) {
            parameter = wrappedRequest.getAttribute(name);
        }
        return parameter;
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
        return wrappedRequest.getParameterNames();
    }

    /**
     * <p>
     * The method for setting the parameter for the given name.
     * 
     * @param name
     *            a String specifying the name of the mapping request parameter
     * @param parameter
     *            the parameter value Object
     */
    public void setRequestParameter(String name, Object parameter) {
        wrappedRequest.setAttribute(name, parameter);
    }

    /**
     * <p>
     * The method for retrieving the parameter for the given name, should return
     * null if the parameter does not exist.
     * </p>
     * 
     * @param name
     *            a String specifying the name of the mapping session parameter
     * @return A String containing the value of the mapping session parameter or
     *         null
     */
    public Object getSessionParameter(String name) {
        HttpSession session = wrappedRequest.getSession(false);

        if (session != null) {
            return session.getAttribute(name);
        }
        return null;
    }

    /**
     * <p>
     * The method for setting the parameter for the given name.
     * 
     * @param name
     *            a String specifying the name of the mapping session parameter
     * @param parameter
     *            the parameter value Object
     */
    public void setSessionParameter(String name, Object parameter) {
        HttpSession session = wrappedRequest.getSession();
        session.setAttribute(name, parameter);
    }

    /**
     * <p>
     * Get the external url of a local resource
     * 
     * @return url the external url
     */
    public String getExternalUrl(String resource) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("http://");
        buffer.append(wrappedRequest.getServerName());

        buffer.append(":");
        buffer.append(wrappedRequest.getServerPort());

        String context = wrappedRequest.getContextPath();
        if (!context.startsWith("/")) {
            buffer.append("/");
        }
        buffer.append(context);

        if (!resource.startsWith("/")) {
            buffer.append("/");
        }
        buffer.append(resource);

        return buffer.toString();
    }

    /**
     * <p>
     * Get the session id
     * 
     * @return the session id or null if no session id
     */
    public String getRequestedSessionId() {
        return wrappedRequest.getRequestedSessionId();
    }

    /**
     * <p>
     * Get the name of the remote host
     * 
     * @return the remote host
     */
    public String getRemoteHost() {
        String remoteHost = wrappedRequest.getRemoteHost();
        if ("127.0.0.1".equals(remoteHost) || "localhost".equalsIgnoreCase(remoteHost)) {
            return wrappedRequest.getServerName();
        } else {
            return remoteHost;
        }
    }

    /**
     * Set the cookie value
     */
    public void setCookie(String name, String value) {
        Cookie c = new Cookie(name, value);
        c.setPath("/");
        c.setMaxAge(DEFAULT_MAX_COOKIE_AGE);
        wrappedResponse.addCookie(c);
        if (log.isDebugEnabled()) {
            log.debug("Set Cookie \"" + name + "\" to \"" + value + "\".");
        }
    }

    /**
     * Get the cookie value
     */
    public String getCookie(String name) {
        Cookie[] cookies = wrappedRequest.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(name)) {
                    if (log.isDebugEnabled()) {
                        log.debug("Found cookie \"" + name + "\" with value \"" + cookies[i].getValue() + "\".");
                    }
                    return cookies[i].getValue();
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Could not find cookie \"" + name + "\".");
        }
        return null;
    }

    /**
     * <p>
     * Get the remote address.
     * <p>
     * 
     * @return the ip address of the remote machine
     */
    public String getRemoteAddress() {
        String remoteAddr = wrappedRequest.getRemoteAddr();
        if ("127.0.0.1".equals(remoteAddr)) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException uhe) {
                // Do Nothing
                log.info(uhe);
            }
        }
        return remoteAddr;
    }

    /**
     * <p>
     * Logs the user out.
     * <p>
     * 
     * @return weather the logout was successful (I assume, weblogics javadocs
     *         don't say)
     */
    public boolean logout() {
        wrappedRequest.getSession().invalidate();
        return weblogic.servlet.security.ServletAuthentication.logout(wrappedRequest);
    }

    /**
     * <p>
     * Get the remote locale.
     * <p>
     * 
     * @return the locale of the remote machine
     */
    public Locale getRemoteLocale() {
        return wrappedRequest.getLocale();
    }

}
