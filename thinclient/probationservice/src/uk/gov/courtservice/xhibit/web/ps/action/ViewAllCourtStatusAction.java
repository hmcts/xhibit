package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: View Summary By Name Action
 * </p>
 * <p>
 * Description: The action for viewing the all court status.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.13 $ $Log:
 *         ViewAllCourtStatusAction.java,v $ Revision 1.11 2006/04/26 09:01:54
 *         bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.10 2004/11/04 14:35:14 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.9 2004/09/24 06:52:05 tzj8k5 CR65 - All Case Status Page
 * ThinClient
 * 
 * Revision 1.8 2004/07/13 14:30:18 tzj8k5 55636 - Thin Client All Court Status
 * to use new flr
 * 
 * Revision 1.7 2004/05/18 10:39:04 xztnfq PRE00256 - Chnages to JMS Instant
 * Messaging topics. The number of topics is to be reduced to one per court, and
 * selectors will be used to determine the destination within the court. As a
 * result, we can no longer build the destination tree from the available
 * topics, but need to retrieve the court room list from the database. This
 * change was made as part of Xhibit 6.0 neil.entwistle-eds@eds.com
 * 
 * Revision 1.6 2004/02/27 16:53:02 xzmw8n fixed array index out of bounds
 * exception
 * 
 * Revision 1.5 2004/02/03 08:58:52 tzj8k5 Amendment to check that all the court
 * room details are displayed
 * 
 * Revision 1.4 2004/01/15 11:07:40 xzmw8n Refactored All Court Status page to
 * only display the status for the most recent event, bringing this more in line
 * with public displays and internet web page.
 * 
 * Revision 1.3 2003/12/10 14:35:07 xzmw8n Implemented check token functionality
 * 
 * Revision 1.2 2003/08/11 08:22:14 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.1 2003/05/09 08:06:23 rz3jq5 Completion of base functionality.
 * 
 * Revision 1.4 2003/05/01 15:51:40 fz0n8j Added terminal selection to witness.
 * 
 * Revision 1.3 2003/04/30 11:44:02 qzd3k3 Merge from dev branch.
 * 
 * Revision 1.2.2.2 2003/04/14 10:21:12 fz0n8j Modified actions to work with
 * latest middle tier code.
 * 
 * Revision 1.2.2.1 2003/04/11 08:41:23 qzd3k3 Refactoring work.
 * 
 * Revision 1.2 2003/04/04 08:41:36 fz0n8j More pages now use database.
 * 
 * Revision 1.1 2003/04/01 13:59:53 fz0n8j Added actions again and modified
 * jsps/properites.
 * 
 * Revision 1.1 2003/03/28 09:58:47 fz0n8j Added witness details and court
 * status.
 * 
 */

public class ViewAllCourtStatusAction extends TerminalCookieAction {

    protected static Logger log = CSServices.getLogger(ViewAllCourtStatusAction.class);

    /**
     * Empty default constructor
     */
    public ViewAllCourtStatusAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironments
     *            the environment to evaluate the action in
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        WitnessControllerBeanBusinessDelegate delegate = WitnessControllerBeanBusinessDelegate.DelegateFactory
                .getInstance();
        Integer courtId = delegate.getCourtIdByTerminalName(getTerminalName(actionEnvironment));
        Collection details = delegate.getAllCourtStatus(courtId.intValue(), new java.util.Date());

        AllCourtStatusValue[] acs = new AllCourtStatusValue[details.size()];
        int i = 0;
        for (Iterator it = details.iterator(); it.hasNext();) {
            AllCourtStatusValue als = (AllCourtStatusValue) it.next();
            acs[i] = als;
            i++;
        }
        actionEnvironment.setRequestParameter("courts", acs);

        actionEnvironment.setResponseName("viewallcourtstatuscomplete");
    }
}
