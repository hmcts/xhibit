package uk.gov.courtservice.xhibit.web.framework.action;

import uk.gov.courtservice.xhibit.web.framework.control.AbstractControl;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: Action
 * </p>
 * <p>
 * Description: This class provides common functionality that is suitable for
 * most Action implementations.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.8 $
 * 
 * $Log: AbstractAction.java,v $
 * Revision 1.8  2006/06/05 12:30:23  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.7 2006/05/31 14:23:50 bzjrnl Change:
 * TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision 1.6
 * 2003/12/04 17:36:35 xzmw8n Added method to check for session
 * 
 * Revision 1.5 2003/12/04 13:22:39 xzmw8n tidy up
 * 
 * Revision 1.4 2003/10/10 16:02:24 xzmw8n added token generation to enable to
 * check for duplicate submissions
 * 
 * Revision 1.3 2003/03/21 11:48:18 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.4 2003/03/19 12:28:03 fz0n8j Framework update.
 * 
 * Revision 1.3 2003/03/17 11:32:01 fz0n8j Added revision cvs comments. ecawley
 * 
 * Kevin Buckthorpe 10 Oct 2003 Added logic to check for duplicate request
 * submissions by use of session token and parameter submitted in form
 * 
 */
public abstract class AbstractAction extends AbstractControl implements Action {

    private boolean checkToken;

    private boolean tokenInSession;

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
    public final void performAction(ActionEnvironment actionEnvironment) throws IllegalArgumentException,
            FrameworkException {
        String rtoken = ""; // request token
        String stoken = ""; // session token

        if (actionEnvironment == null) {
            throw new IllegalArgumentException("actionEnvironment");
        }

        try {
            rtoken = (String) actionEnvironment.getRequestParameter("rtoken");

            try {
                stoken = (String) actionEnvironment.getSessionParameter("stoken");
                setTokenInSession(true);
                setCheckToken(stoken.equals(rtoken));
            } catch (ParameterNotFoundException pnfe) {
                setCheckToken(false);
                setTokenInSession(false);
            }
        } catch (ParameterNotFoundException e) {
            setCheckToken(false);
        }

        internalPerformAction(actionEnvironment);

    }

    private void setTokenInSession(boolean isTokenInSession) {
        tokenInSession = isTokenInSession;
    }

    public boolean isTokenInSession() {
        return tokenInSession;
    }

    private void setCheckToken(boolean checkToken) {
        this.checkToken = checkToken;
    }

    /**
     * Returns true if this is the first time that the request has been made.
     * Returns false if this is a duplicate request.
     * 
     * @return true if token in session and from form match. False if no token
     *         found in form or do not match.
     */
    public boolean checkToken() {
        return checkToken;
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     * @throws FrameworkException
     *             if an error occurs
     */
    protected abstract void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException;

}
