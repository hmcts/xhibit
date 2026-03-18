package uk.gov.courtservice.xhibit.web.framework.control;

import uk.gov.courtservice.xhibit.web.framework.util.ParameterNameConflictException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: Control Environment
 * </p>
 * <p>
 * Description: The environment to evaluate an control in.
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

public interface ControlEnvironment {
    /**
     * Returns the current name of the logged on user
     * 
     * @return the user name
     */
    public String getUserName();

    /**
     * <p>
     * If the requested control request parameter does not exist return the
     * alternative
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control request parameter
     * @return A String containing the value of the control request parameter
     * @throws IllegalArgumentException
     *             if the name is null
     * @throws ParameterNotFoundException
     *             if the requested parameter can not be found
     */
    public Object getRequestParameter(String name) throws IllegalArgumentException, ParameterNotFoundException;

    /**
     * <p>
     * If the requested control request parameter does not exist return the
     * alternative
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control request parameter
     * @param alternative
     *            a String containing the value to return if the control request
     *            parameter is not found
     * @return A String containing the value of the control request parameter or
     *         the alternative*
     * @throws IllegalArgumentException
     *             if the name is null
     */
    public Object getRequestParameter(String name, Object alternative) throws IllegalArgumentException;

    /**
     * <p>
     * Add the parameter to the control control
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control control parameter
     * @param parameter
     *            the parameter value Object
     * @throws IllegalArgumentException
     *             if the name or parameter is null
     * @throws ParameterNameConflictException
     *             if the parameter has already been set
     */
    public void setRequestParameter(String name, Object parameter) throws IllegalArgumentException,
            ParameterNameConflictException;

    /**
     * <p>
     * If the requested control session parameter does not exist return the
     * alternative
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control session parameter
     * @return A String containing the value of the control session parameter
     * @throws IllegalArgumentException
     *             if the name is null
     * @throws ParameterNotFoundException
     *             if the requested parameter can not be found
     */
    public Object getSessionParameter(String name) throws IllegalArgumentException, ParameterNotFoundException;

    /**
     * <p>
     * If the requested control session parameter does not exist return the
     * alternative
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control session parameter
     * @param alternative
     *            a String containing the value to return if the control session
     *            parameter is not found
     * @return A String containing the value of the control session parameter or
     *         the alternative*
     * @throws IllegalArgumentException
     *             if the name is null
     */
    public Object getSessionParameter(String name, Object alternative) throws IllegalArgumentException;

    /**
     * <p>
     * Add the parameter to the control control
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control control parameter
     * @param parameter
     *            the parameter value Object
     * @throws IllegalArgumentException
     *             if the name or parameter is null
     * @throws ParameterNameConflictException
     *             if the parameter has already been set
     */
    public void setSessionParameter(String name, Object parameter) throws IllegalArgumentException,
            ParameterNameConflictException;

    /**
     * <p>
     * Method returning the names of all the parameters available on the
     * request, of particular use when using checkboxes
     * </p>
     * 
     * @return an enumeration of the parameter names on this request.
     */
    public java.util.Enumeration getRequestParameterNames();
}
