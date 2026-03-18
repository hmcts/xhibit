package uk.gov.courtservice.xhibit.web.framework.action;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;

/**
 * <p>
 * Title: Action Not Found Exception
 * </p>
 * <p>
 * Description: Thrown when a requested action can not be found
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 * @version $Id: ActionNotFoundException.java,v 1.2 2004/12/09 15:05:47 sz0t7n
 *          Exp $
 */
public class ActionNotFoundException extends CSUnrecoverableException {
    /**
     * <p>
     * Constructs an ActionNotFoundException with the action name as its
     * message.
     * </p>
     * 
     * @param actionName
     *            a String containing the name of the action that can not be
     *            found
     */
    public ActionNotFoundException(String actionName) {
        super(new Message("web.framework.action.ActionNotFoundException", new Object[] {}),
                "Action not found with name \"" + actionName + "\"");
    }
}
