package uk.gov.courtservice.xhibit.web.framework.control;

import java.util.Iterator;

import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: Control
 * </p>
 * <p>
 * Description: This interface describes a control
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
 * $Log: Control.java,v $
 * Revision 1.3  2006/06/05 12:30:24  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.2 2006/05/31 14:23:53 bzjrnl Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting Revision 1.1
 * 2003/03/21 11:48:21 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.2 2003/03/19 18:38:52 fz0n8j Framework changes.
 * 
 * Revision 1.1 2003/03/19 12:28:04 fz0n8j Framework update.
 * 
 * 
 */
public interface Control {

    /**
     * return the control name
     * 
     * @throws IllegalArgumentException
     *             if name has been set*
     * @throws IllegalStateException
     *             if name has been set
     */
    public void setName(String newName) throws IllegalStateException, IllegalArgumentException;

    /**
     * return the control name
     * 
     * @throws IllegalStateException
     *             if name has not been set
     */
    public String getName() throws IllegalStateException;

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
    public String getParameter(String name) throws IllegalArgumentException, ParameterNotFoundException;

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
    public void setParameter(String name, String value) throws IllegalArgumentException;

    /*
     * Return an iterator over the parameter names @return the parameter name
     * iterator
     */
    public Iterator getParameterNames();
}