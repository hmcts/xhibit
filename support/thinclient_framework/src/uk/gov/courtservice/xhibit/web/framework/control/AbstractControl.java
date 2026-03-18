package uk.gov.courtservice.xhibit.web.framework.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;
import uk.gov.courtservice.xhibit.web.framework.util.PrimitiveUtil;
import uk.gov.courtservice.xhibit.web.framework.util.SafeIterator;

/**
 * <p>
 * Title: Control
 * </p>
 * <p>
 * Description: This class provides a common implementation of the control
 * interface
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.3 $
 * 
 * $Log: AbstractControl.java,v $
 * Revision 1.3  2006/06/05 12:30:24  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.2 2006/05/31 14:23:53 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.1 2003/03/21 11:48:21 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.2 2003/03/19 18:38:52 fz0n8j Framework changes.
 * 
 * Revision 1.1 2003/03/19 12:28:03 fz0n8j Framework update.
 * 
 * 
 */
public abstract class AbstractControl {

    /**
     * The parameter map
     */
    private final Map paramMap = new HashMap();

    /**
     * The control name
     */
    private String name;

    /**
     * Construct a default object
     */
    public AbstractControl() {

    }

    /**
     * return the control name
     * 
     * @throws IllegalArgumentException
     *             if name has been set*
     * @throws IllegalStateException
     *             if name has been set
     */
    public void setName(String newName) throws IllegalStateException, IllegalArgumentException {
        if (newName == null) {
            throw new IllegalArgumentException("newName");
        }
        if (name != null) {
            throw new IllegalStateException("Name already set.");
        }
        name = newName;
    }

    /**
     * return the control name
     * 
     * @throws IllegalStateException
     *             if name has not been set
     */
    public String getName() throws IllegalStateException {
        if (name == null) {
            throw new IllegalStateException("Name not set.");
        }
        return name;
    }

    /**
     * get a control parameter
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     * @throws IllegalArgumentException
     *             if name is null
     * @throws ParameterNotFoundException
     *             if parameter not found
     */
    public String getParameter(String name) throws IllegalArgumentException, ParameterNotFoundException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }
        String param = (String) paramMap.get(name);
        if (param != null) {
            return param;
        } else {
            throw new ParameterNotFoundException("param");
        }
    }

    /**
     * Set a control parameter
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     * @throws IllegalArgumentException
     *             if either parameter is null
     */
    public void setParameter(String name, String value) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("name");
        }
        if (value == null) {
            throw new IllegalArgumentException("value");
        }
        paramMap.put(name, value);
    }

    /*
     * Return an iterator over the parameter names @return the parameter name
     * iterator
     */
    public Iterator getParameterNames() {
        return new SafeIterator(paramMap.keySet().iterator());
    }

    /**
     * Get information about the object
     * 
     * @return a String repsentation of the object useful for debuging
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getName());
        buffer.append(" (");
        buffer.append(getShortClassName());
        buffer.append(")");
        buffer.append(PrimitiveUtil.NL);

        Iterator names = getParameterNames();
        while (names.hasNext()) {
            String name = (String) names.next();
            buffer.append(PrimitiveUtil.TAB);
            buffer.append(name);
            buffer.append(": ");
            buffer.append(getParameter(name));
            buffer.append(PrimitiveUtil.NL);
        }

        return buffer.toString();
    }

    /**
     * Get the short unqualified class name
     * 
     * @return the short class name
     */
    public String getShortClassName() {
        String name = getClass().getName();
        int index = name.lastIndexOf(".");
        if (index == -1) {
            return name;
        } else {
            return name.substring(index + 1);
        }
    }

}