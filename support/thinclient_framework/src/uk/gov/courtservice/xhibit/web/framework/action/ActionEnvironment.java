package uk.gov.courtservice.xhibit.web.framework.action;

import java.util.Locale;

import uk.gov.courtservice.xhibit.web.framework.control.ControlEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Action Environment
 * </p>
 * <p>
 * Description: The environment to evaluate an action in.
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
public interface ActionEnvironment extends ControlEnvironment {
    /**
     * <p>
     * Set the name of the resource to respond too
     * </p>
     * 
     * @param name
     *            a String specifying the name of the resource to respond to
     * @throws IllegalStateException
     *             if the response name HAS been set
     * @throws IllegalArgumentException
     *             if the response name is null
     */
    public void setResponseName(String name) throws IllegalStateException, IllegalArgumentException;

    /**
     * <p>
     * Get the name of the resource to respond too
     * </p>
     * 
     * @return parameter the name of the resource to respond to
     * @throws IllegalStateException
     *             if the response name HAS NOT been set
     */
    public String getResponseName() throws IllegalStateException;

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
    public String getExternalUrl(String resource) throws IllegalArgumentException, FrameworkException;

    /**
     * <p>
     * Get the session id
     * 
     * @return the session id or null if no session id
     */
    public String getRequestedSessionId();

    /**
     * <p>
     * Get the name of the remote host
     * 
     * @return the remote host
     */
    public String getRemoteHost();

    /**
     * <p>
     * Get the remote address.
     * <p>
     * 
     * @return the ip address of the remote machine
     */
    public String getRemoteAddress();

    /**
     * <p>
     * Returns wheather the user is in a given role
     * </p>
     * 
     * @param role
     *            the name of the role
     * @return the user name
     */
    public boolean isUserInRole(String role);

    /**
     * <p>
     * Get the remote locale.
     * <p>
     * 
     * @return the locale of the remote machine
     */
    public Locale getRemoteLocale();

    /**
     * Set the cookie value
     */
    public void setCookie(String name, String value);

    /**
     * Get the cookie value return null if not found
     */
    public String getCookie(String name);

    /**
     * <p>
     * logout
     * 
     * @return success
     */
    public boolean logout();
}
