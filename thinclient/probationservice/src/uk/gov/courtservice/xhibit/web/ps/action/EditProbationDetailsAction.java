package uk.gov.courtservice.xhibit.web.ps.action;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRProbationValueSet;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.ps.bean.ProbationServiceDetailsBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: EditProbationDetailsAction
 * </p>
 * <p>
 * Description: The the action for showing the probation details to edit.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.26 $ $Log:
 *         EditProbationDetailsAction.java,v $ Revision 1.24 2006/04/26 09:01:52
 *         bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.23 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.22 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.21 2003/12/10 14:35:05 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.20 2003/08/11 08:22:11 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.19 2003/05/01 15:29:50 fz0n8j Now uses new terminal action.
 * 
 * Revision 1.18 2003/03/28 15:28:31 fz0n8j Changed terminal lookup back to
 * machine name . . .
 * 
 * Revision 1.17 2003/03/27 16:24:33 fz0n8j Changed to remote address again.
 * 
 * Revision 1.16 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 * 
 * Revision 1.15 2003/03/21 17:28:39 fz0n8j *** empty log message ***
 * 
 * Revision 1.14 2003/03/19 21:14:55 fz0n8j Modified for new mappings.
 * 
 * Revision 1.13 2003/03/18 13:25:08 fz0n8j Moved action utils
 * 
 * Revision 1.12 2003/03/17 11:32:18 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.11 2003/03/17 11:14:51 fz0n8j Now uses delegates to access the
 * database. ecawley
 * 
 * Revision 1.10 2003/03/14 11:17:47 fz0n8j Changed setParameter and
 * getParameter to setRequestParameter and getRequestParameter.
 * 
 * Revision 1.9 2003/03/12 15:32:55 fz0n8j Sets default bean
 * 
 * Revision 1.8 2003/03/10 18:27:11 fz0n8j No longer creates a default bean
 * 
 * Revision 1.7 2003/03/07 16:30:58 fz0n8j Added code to put the bean in
 * beanstore
 * 
 * Revision 1.6 2003/03/06 18:56:05 fz0n8j *** empty log message ***
 * 
 * Revision 1.5 2003/03/06 18:07:32 fz0n8j Testing log
 * 
 * 
 */
public class EditProbationDetailsAction extends TerminalCookieAction {

    /**
     * Empty default constructor
     */
    public EditProbationDetailsAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) {
        PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        PSRProbationValueSet probationValue = delegate.findProbation(getTerminalName(actionEnvironment));
        ProbationServiceDetailsBean bean = ActionUtil.getProbationDetails(probationValue);
        actionEnvironment.setRequestParameter("probationServiceDetails", bean);
        String id = KeyFactory.getInstance().nextKey();
        actionEnvironment.setSessionParameter(id, probationValue); // put
        // value
        // objects
        // into
        // session
        // . . .
        actionEnvironment.setRequestParameter("objectid", id);
        actionEnvironment.setResponseName("editprobationdetailscomplete");
    }
}
