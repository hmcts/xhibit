package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Arrays;
import java.util.Enumeration;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Case Skeleton Schedule Action
 * </p>
 * <p>
 * Description:
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * Author: David Duncan (2003) $$ $Log: CaseSkeletonScheduleAction.java,v $
 * Author: David Duncan (2003) $$ Revision 1.6  2014/06/22 18:06:44  atwells
 * Author: David Duncan (2003) $$ 8.6.2 WebLogic Upgrade - Added argument for passing users display name
 * Author: David Duncan (2003) $$
 * Author: David Duncan (2003) $$ Revision 1.5  2006/06/05 12:32:38  bzjrnl
 * Author: David Duncan (2003) $$ Change: TI901
 * Author: David Duncan (2003) $$ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Author: David Duncan (2003) $$
 * Author: David Duncan (2003) $$ Revision 1.4 2006/05/31 14:27:10 bzjrnl
 * Author: David Duncan (2003) $$ Change: TI901 Author: David Duncan (2003) $$
 * Comment: Weblogic Upgrade - Standadise code formatting Author: David Duncan
 * (2003) $$ Revision 1.3 2005/04/27 08:26:58 bzjrnl Manual Merge From
 * BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:43 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.7 2004/01/23 15:02:58 xzmw8n Fixed judge not found exception
 * 
 * Revision 1.6 2003/12/04 17:37:23 xzmw8n Fix to distinguish between duplicate
 * form submission and session timeout
 * 
 * Revision 1.5 2003/10/14 14:04:12 xzmw8n Added tokens and checks to identify
 * and reject duplicate form submissions
 * 
 * Revision 1.4 2003/08/11 16:41:14 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.3 2003/05/20 14:51:23 fz0n8j Now sorts witness sessions.
 * 
 * Revision 1.2 2003/05/09 15:52:10 fz0n8j *** empty log message ***
 * 
 * Revision 1.1 2003/04/04 08:50:36 hzf3bb new actions
 * 
 * 
 * 
 */
public class CaseSkeletonScheduleAction extends AbstractAction {

    /**
     * The log4j logger
     */

    private static final Logger log = CSServices.getLogger(CaseSkeletonScheduleAction.class);

    /**
     * Empty default constructor
     */
    public CaseSkeletonScheduleAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("loggedout");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }

            String id = (String) actionEnvironment.getRequestParameter("id");
            Enumeration en = actionEnvironment.getRequestParameterNames();
            Integer week = null;
            while (en.hasMoreElements()) {
                String name = (String) en.nextElement();
                if (name.equals("week")) {
                    week = new Integer((String) actionEnvironment.getRequestParameter(name));
                }
            }
            try {
                if (week == null || week.intValue() < 1) {
                    week = new Integer(1);
                }
                Integer caseid = new Integer(id);
                SkeletonSchedule ss = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(caseid);
                WitnessSession[] ws = WitnessFactory.getInstance().getWitnessSessionSelector().getWitnessesForWeek(
                        caseid, week.intValue());
                Arrays.sort(ws);
                String judgeName = "";
                try {
                    judgeName = ss.getCaseDetail().getJudgeName((actionEnvironment.getSessionParameter(UserTerminalProperties.DISPLAY_NAME.toString())).toString());
                } catch (CSRecoverableException be) {
                    log.log(Level.WARN, "No PAD judge found for caseid " + caseid + ".", be);
                }

                actionEnvironment.setRequestParameter("judgeName", judgeName);
                actionEnvironment.setRequestParameter("caseSkeleton", ss);
                actionEnvironment.setRequestParameter("caseid", caseid);
                actionEnvironment.setRequestParameter("week", week);
                if (ws != null && ws.length > 0) {
                    actionEnvironment.setRequestParameter("witnessSessions", ws);
                }
            } catch (ScheduleNotFoundException e) {
                throw new FrameworkException(e);
            }
            actionEnvironment.setResponseName("caseskeletonschedulecomplete");
        }
    }
}
