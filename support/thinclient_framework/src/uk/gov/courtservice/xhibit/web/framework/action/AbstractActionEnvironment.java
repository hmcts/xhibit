package uk.gov.courtservice.xhibit.web.framework.action;

import uk.gov.courtservice.xhibit.web.framework.control.AbstractControlEnvironment;

/**
 * <p>
 * Title: Abstract Action Environment
 * </p>
 * <p>
 * Description: The environment to evaluate an action in. This provides common
 * functionality for classes wanting to implement the ActionEnvironment
 * interface. The primary purpose of this is to allow actions to be decupuled
 * from the servlet environment to facilitate testing, a simple testing class
 * could be writen to replace the ServletActionEnvironment.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.6 $
 * 
 * $Log: AbstractActionEnvironment.java,v $
 * Revision 1.6  2006/06/05 12:30:23  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.5 2006/05/31 14:23:50
 * bzjrnl Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting
 * Revision 1.4 2003/10/01 15:23:43 bzw8gp Jon Powell
 * 
 * organise imports (remove unused) unused imports cause misleading dependencies
 * 
 * Revision 1.3 2003/03/21 11:48:18 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.8 2003/03/19 12:28:03 fz0n8j Framework update.
 * 
 * Revision 1.7 2003/03/19 09:10:34 fz0n8j Removed common control functionality
 * from action to control
 * 
 * Revision 1.6 2003/03/17 11:32:02 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.5 2003/03/14 18:38:11 fz0n8j Allow parameters to be updated
 * 
 * Revision 1.4 2003/03/14 12:47:10 fz0n8j Added session parameter
 * 
 * Revision 1.3 2003/03/14 11:17:46 fz0n8j Changed setParameter and getParameter
 * to setRequestParameter and getRequestParameter.
 * 
 * Revision 1.2 2003/03/11 15:46:38 fz0n8j Added CVS Log comments - ecawley
 * 
 */
public abstract class AbstractActionEnvironment extends AbstractControlEnvironment implements ActionEnvironment {

    /*
     * The name of the resource to respond to
     */
    private String responseName = null;

    /**
     * Empty default constructor
     */
    public AbstractActionEnvironment() {

    }

    /**
     * <p>
     * Set the name of the resource to respond too, this method should only be
     * called once.
     * </p>
     * 
     * @param newResponseName
     *            a String specifying the name of the resource to respond to
     * @throws IllegalStateException
     *             if the response name HAS been set
     * @throws IllegalArgumentException
     *             if the new response name is null
     */
    public void setResponseName(String newResponseName) throws IllegalStateException, IllegalArgumentException {
        if (newResponseName == null) {
            throw new IllegalArgumentException("newResponseName");
        }
        if (responseName != null) {
            throw new IllegalStateException("responseName");
        }
        responseName = newResponseName;
    }

    /**
     * <p>
     * Get the name of the resource to respond too, setResponseName must have
     * been called previously
     * </p>
     * 
     * @return parameter the name of the resource to respond to
     * @throws IllegalStateException
     *             if the response name HAS NOT been set
     */
    public String getResponseName() throws IllegalStateException {
        if (responseName == null) {
            throw new IllegalStateException("responseName");
        }
        return responseName;
    }

}
