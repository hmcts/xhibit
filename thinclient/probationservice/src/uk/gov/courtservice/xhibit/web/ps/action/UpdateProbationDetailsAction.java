package uk.gov.courtservice.xhibit.web.ps.action;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRProbationValueSet;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.ps.bean.ProbationServiceDetailsBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: UpdateProbationDetailsAction
 * </p>
 * <p>
 * Description: The the action to update probation details.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.20 $ $Log:
 *         UpdateProbationDetailsAction.java,v $ Revision 1.18 2006/04/26
 *         09:01:53 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.17 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.16 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.15 2003/12/10 14:35:06 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.14 2003/09/23 08:27:59 tzj8k5 Set bean value for saved pages
 * 
 * Revision 1.13 2003/08/11 08:22:14 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.12 2003/05/01 15:29:51 fz0n8j Now uses new terminal action.
 * 
 * Revision 1.11 2003/03/28 15:28:31 fz0n8j Changed terminal lookup back to
 * machine name . . .
 * 
 * Revision 1.10 2003/03/27 16:24:35 fz0n8j Changed to remote address again.
 * 
 * Revision 1.9 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 * 
 * Revision 1.8 2003/03/24 17:14:50 fz0n8j Fixed bug.
 * 
 * Revision 1.7 2003/03/19 21:14:56 fz0n8j Modified for new mappings.
 * 
 * Revision 1.6 2003/03/18 13:25:09 fz0n8j Moved action utils
 * 
 * Revision 1.5 2003/03/17 11:14:51 fz0n8j Now uses delegates to access the
 * database. ecawley
 * 
 * Revision 1.4 2003/03/14 11:17:48 fz0n8j Changed setParameter and getParameter
 * to setRequestParameter and getRequestParameter.
 * 
 * Revision 1.3 2003/03/12 15:32:18 fz0n8j Address no longer editable
 * 
 * Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 * Revision 1.1 2003/03/07 16:40:07 fz0n8j Added files to CVS $Log:
 * UpdateProbationDetailsAction.java,v $ Revision 1.18 2006/04/26 09:01:53
 * bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.17 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.16 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.15 2003/12/10 14:35:06 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.14 2003/09/23 08:27:59 tzj8k5 Set bean value for saved pages
 * 
 * Revision 1.13 2003/08/11 08:22:14 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.12 2003/05/01 15:29:51 fz0n8j Now uses new terminal action.
 * 
 * Revision 1.11 2003/03/28 15:28:31 fz0n8j Changed terminal lookup back to
 * machine name . . .
 * 
 * Revision 1.10 2003/03/27 16:24:35 fz0n8j Changed to remote address again.
 * 
 * Revision 1.9 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 * 
 * Revision 1.8 2003/03/24 17:14:50 fz0n8j Fixed bug.
 * 
 * Revision 1.7 2003/03/19 21:14:56 fz0n8j Modified for new mappings.
 * 
 * Revision 1.6 2003/03/18 13:25:09 fz0n8j Moved action utils
 * 
 * Revision 1.5 2003/03/17 11:14:51 fz0n8j Now uses delegates to access the
 * database. ecawley
 * 
 * Revision 1.4 2003/03/14 11:17:48 fz0n8j Changed setParameter and getParameter
 * to setRequestParameter and getRequestParameter.
 * 
 * Revision 1.3 2003/03/12 15:32:18 fz0n8j Address no longer editable
 * 
 * Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class UpdateProbationDetailsAction extends TerminalCookieAction {

    /**
     * Empty default constructor
     */
    public UpdateProbationDetailsAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("loggedout");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }

            String id = (String) actionEnvironment.getRequestParameter("objectid");
            PSRProbationValueSet probationValue = (PSRProbationValueSet) actionEnvironment.getSessionParameter(id); // get
                                                                                                                    // the
                                                                                                                    // value
                                                                                                                    // objects
                                                                                                                    // back
            // from the session
            ProbationServiceDetailsBean probationServiceDetailsBean = ActionUtil.getProbationDetails(probationValue); // re-populeate
            // th bean from
            // session
            ProbationServiceDetailsBean newBean = new ProbationServiceDetailsBean(0, (String) actionEnvironment
                    .getRequestParameter("officeName"), probationServiceDetailsBean.getAddress(),
                    (String) actionEnvironment.getRequestParameter("telephone"), (String) actionEnvironment
                            .getRequestParameter("fax"), (String) actionEnvironment.getRequestParameter("email"));
            actionEnvironment.setRequestParameter("probationServiceDetails", newBean);
            if (newBean.isValid()) {
                PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory
                        .getInstance();
                probationValue = ActionUtil.getModifiedProbationValue(probationValue, newBean);
                delegate.updateProbationValues(probationValue);
                probationValue = delegate.findProbation(getTerminalName(actionEnvironment));
                id = KeyFactory.getInstance().nextKey(); // override the
                // old value
                // object and id
                // with a new
                // one . . .
                actionEnvironment.setSessionParameter(id, probationValue); // put
                // value
                // objects
                // into
                // session
                // . .
                // .
                actionEnvironment.setRequestParameter("statusKey", "status.saved");
            }
            actionEnvironment.setRequestParameter("objectid", id);
            actionEnvironment.setResponseName("updateprobationdetailscomplete");
        }
    }

}
