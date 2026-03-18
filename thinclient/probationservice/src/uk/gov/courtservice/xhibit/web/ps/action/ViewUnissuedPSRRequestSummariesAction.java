package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.Collections;
import java.util.List;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: ViewUnissuedPSRRequestSummariesAction
 * </p>
 * <p>
 * Description: The the action for PSR request summaries.
 *
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.24 $ $Log:
 *         ViewUnissuedPSRRequestSummariesAction.java,v $ Revision 1.20
 *         2006/04/26 09:01:55 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 *
 * Revision 1.19 2004/11/04 14:35:15 bzjrnl Changes to correct problems with
 * cookie access.
 *
 * Revision 1.18 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 *
 * Revision 1.17 2003/12/10 14:35:08 xzmw8n Implemented check token
 * functionality
 *
 * Revision 1.16 2003/08/11 08:22:15 bzw8gp Jon Powell
 *
 * organise imports (remove unused)
 *
 * Revision 1.15 2003/05/01 15:29:51 fz0n8j Now uses new terminal action.
 *
 * Revision 1.14 2003/04/29 17:15:30 fz0n8j Merged devBranch-2b-030404. (WDF)
 *
 * Revision 1.13.2.1 2003/04/29 17:12:36 fz0n8j new e-mail thing.
 *
 * Revision 1.13 2003/03/28 15:28:32 fz0n8j Changed terminal lookup back to
 * machine name . . .
 *
 * Revision 1.12 2003/03/27 16:24:36 fz0n8j Changed to remote address again.
 *
 * Revision 1.11 2003/03/27 16:21:43 fz0n8j Modified to use get remote address.
 *
 * Revision 1.10 2003/03/26 16:54:51 fz0n8j Bug fixes.
 *
 * Revision 1.9 2003/03/20 17:41:14 fz0n8j *** empty log message ***
 *
 * Revision 1.8 2003/03/19 20:13:32 fz0n8j Added better response functionality
 *
 * Revision 1.7 2003/03/18 22:03:43 fz0n8j Uses dlegate to get from database.
 * ecawley
 *
 * Revision 1.6 2003/03/17 11:32:19 fz0n8j Added revision cvs comments. ecawley
 *
 * Revision 1.5 2003/03/14 11:17:48 fz0n8j Changed setParameter and getParameter
 * to setRequestParameter and getRequestParameter.
 *
 * Revision 1.4 2003/03/12 15:33:23 fz0n8j *** empty log message ***
 *
 * Revision 1.3 2003/03/11 16:13:58 fz0n8j No Longer uses dummy data, added CVS
 * Log, ecawley
 *
 */
public class ViewUnissuedPSRRequestSummariesAction extends TerminalCookieAction {
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
                List requestValues = delegate.findAllUnissuedRequests(getTerminalName(actionEnvironment));
                String id = KeyFactory.getInstance().nextKey();
                actionEnvironment.setSessionParameter(id, requestValues);
                actionEnvironment.setRequestParameter("objectid", id);
                List beans = ActionUtil.getRequestSummaries(requestValues);
                if (!beans.isEmpty()) {
                    Collections.sort(beans);
                    actionEnvironment.setRequestParameter("psrRequestSummaries", beans);
                }
        actionEnvironment.setResponseName("viewunissuedpsrrequestsummaries");
    }
}
