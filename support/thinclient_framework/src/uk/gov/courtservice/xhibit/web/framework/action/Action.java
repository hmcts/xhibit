package uk.gov.courtservice.xhibit.web.framework.action;

import uk.gov.courtservice.xhibit.web.framework.control.Control;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Action
 * </p>
 * <p>
 * Description: This interface describes an action that can be performed in
 * response to a user request.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.5 $
 * 
 * $Log: Action.java,v $
 * Revision 1.5  2006/06/05 12:30:24  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.4 2006/05/31 14:23:50 bzjrnl Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting Revision 1.3
 * 2003/03/21 11:48:18 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.4 2003/03/19 12:28:03 fz0n8j Framework update.
 * 
 * Revision 1.3 2003/03/17 11:32:02 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 15:46:38 fz0n8j Added CVS Log comments - ecawley
 * 
 */
public interface Action extends Control {
    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     * @throws IllegalArgumentException
     *             if the environment is null
     * @throws FrameworkException
     *             if an error occurs
     */
    public void performAction(ActionEnvironment actionEnvironment) throws IllegalArgumentException, FrameworkException;
}