package uk.gov.courtservice.xhibit.web.framework.control;

import uk.gov.courtservice.xhibit.web.framework.util.ParameterNameConflictException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: Abstract Control Environment
 * </p>
 * <p>
 * Description: The environment to evaluate an control in. This provides common
 * functionality for classes wanting to implement the ControlEnvironment
 * interface. The primary purpose of this is to allow controls to be decupuled
 * from the servlet environment to facilitate testing, a simple testing class
 * could be writen to replace the ServletControlEnvironment.
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
 * $Revision: 1.3 $
 * 
 * $Log: AbstractControlEnvironment.java,v $
 * Revision 1.3  2006/06/05 12:30:24  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.2 2006/05/31 14:23:53
 * bzjrnl Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting
 * Revision 1.1 2003/03/21 11:48:21 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.1 2003/03/19 12:28:04 fz0n8j Framework update.
 * 
 * Revision 1.1 2003/03/19 09:10:34 fz0n8j Removed common control functionality
 * from action to control
 * 
 */
public abstract class AbstractControlEnvironment implements ControlEnvironment {
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
    public final Object getRequestParameter(String name) throws IllegalArgumentException, ParameterNotFoundException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }

        Object parameter = internalGetRequestParameter(name);
        if (parameter != null) {
            return parameter;
        }
        throw new ParameterNotFoundException(name);

    }

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
    public Object getRequestParameter(String name, Object alternative) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }

        Object parameter = internalGetRequestParameter(name);
        if (parameter != null) {
            return parameter;
        }
        return alternative;
    }

    /**
     * <p>
     * Add the parameter to the control response
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control response parameter
     * @param parameter
     *            the parameter value Object
     * @throws IllegalArgumentException
     *             if the name or parameter is null
     * @throws ParameterNameConflictException
     *             if the parameter has already been set
     */
    public void setRequestParameter(String name, Object parameter) throws IllegalArgumentException,
            ParameterNameConflictException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }
        if (parameter == null) {
            throw new IllegalArgumentException("parameter");
        }
        /*
         * 
         * This will be readded when we use client side redirect
         * 
         * if (internalGetRequestParameter(name) != null) { throw new
         * ParameterNameConflictException(name); }
         */
        internalSetRequestParameter(name, parameter);
    }

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
    public final Object getSessionParameter(String name) throws IllegalArgumentException, ParameterNotFoundException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }

        Object parameter = internalGetSessionParameter(name);
        if (parameter != null) {
            return parameter;
        }
        throw new ParameterNotFoundException(name);

    }

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
    public Object getSessionParameter(String name, Object alternative) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }

        Object parameter = internalGetSessionParameter(name);
        if (parameter != null) {
            return parameter;
        }
        return alternative;
    }

    /**
     * <p>
     * Add the parameter to the control response
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control response parameter
     * @param parameter
     *            the parameter value Object
     * @throws IllegalArgumentException
     *             if the name or parameter is null
     * @throws ParameterNameConflictException
     *             if the parameter has already been set
     */
    public void setSessionParameter(String name, Object parameter) throws IllegalArgumentException,
            ParameterNameConflictException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }
        if (parameter == null) {
            throw new IllegalArgumentException("parameter");
        }
        /*
         * 
         * This will be readded when we use client side redirect
         * 
         * if (internalGetSessionParameter(name) != null) { throw new
         * ParameterNameConflictException(name); }
         * 
         */
        internalSetSessionParameter(name, parameter);
    }

    /**
     * <p>
     * The abstract method for retrieving the parameter for the given name,
     * should retund null if the parameter does not exist.
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control session parameter
     * @return A String containing the value of the control session parameter or
     *         null
     */
    protected abstract Object internalGetSessionParameter(String name);

    /**
     * <p>
     * The abstract method for setting the parameter for the given name.
     * 
     * @param name
     *            a String specifying the name of the control session parameter
     * @param parameter
     *            the parameter value Object
     */
    protected abstract void internalSetSessionParameter(String name, Object parameter);

    /**
     * <p>
     * The abstract method for retrieving the parameter for the given name,
     * should retund null if the parameter does not exist.
     * </p>
     * 
     * @param name
     *            a String specifying the name of the control request parameter
     * @return A String containing the value of the control request parameter or
     *         null
     */
    protected abstract Object internalGetRequestParameter(String name);

    /**
     * <p>
     * The abstract method for setting the parameter for the given name.
     * 
     * @param name
     *            a String specifying the name of the control request parameter
     * @param parameter
     *            the parameter value Object
     */
    protected abstract void internalSetRequestParameter(String name, Object parameter);

}
