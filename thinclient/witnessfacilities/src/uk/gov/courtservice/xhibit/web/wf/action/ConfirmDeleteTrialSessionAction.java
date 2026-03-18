package uk.gov.courtservice.xhibit.web.wf.action;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceUtil;

/**
 * <p>
 * Title: Delete Trial Session Action
 * </p>
 * <p>
 * Description: The action for deleting a trial session.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * Author: David Duncan (2003) $$ $Log: ConfirmDeleteTrialSessionAction.java,v $
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
 * Revision 1.5 2004/03/31 11:54:07 tzj8k5 Add page source parameter to set up
 * navigation between the pages
 * 
 * Revision 1.4 2003/12/04 17:37:23 xzmw8n Fix to distinguish between duplicate
 * form submission and session timeout
 * 
 * Revision 1.3 2003/10/14 14:04:12 xzmw8n Added tokens and checks to identify
 * and reject duplicate form submissions
 * 
 * Revision 1.2 2003/04/30 11:44:00 qzd3k3 Merge from dev branch.
 * 
 * Revision 1.1.2.3 2003/04/25 15:29:28 hzf3bb no message
 * 
 * Revision 1.1.2.2 2003/04/24 16:31:14 hzf3bb *** empty log message ***
 * 
 * Revision 1.1.2.1 2003/04/22 17:34:57 hzf3bb *** empty log message ***
 * 
 * 
 * 
 */
public class ConfirmDeleteTrialSessionAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public ConfirmDeleteTrialSessionAction() {
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

            String caseID = (String) actionEnvironment.getRequestParameter("caseid");
            String day = (String) actionEnvironment.getRequestParameter("trialday");
            String session = (String) actionEnvironment.getRequestParameter("trialsession");
            String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");

            actionEnvironment.setRequestParameter("pagesource", pageRequestFrom);
            String errormessage = null;

            SkeletonSchedule schedule = null;
            try {
                schedule = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseID));
            } catch (NumberFormatException e) {
                errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                        .getRemoteLocale(), "invalidcaseid");
            } catch (ScheduleNotFoundException e) {
                errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                        .getRemoteLocale(), "noschedulefound");
            }

            TrialSession trialsession = null;
            try {
                trialsession = schedule.getTrialSession(new Integer(day), session);
            } catch (TrialSessionNotFoundException e) {
                errormessage = ResourceUtil.getPropertyForResourceAndLocale("Messages", actionEnvironment
                        .getRemoteLocale(), "notrialsession");
            }

            try {
                trialsession.remove();
            } catch (ModificationException e) {
                throw new FrameworkException(e);
            }

            TrialSession[] trialsessions = schedule.getTrialSessions();

            if (trialsessions != null && trialsessions.length > 0) {
                actionEnvironment.setRequestParameter("trialsessions", trialsessions);
            }

            if (errormessage == null) {
                actionEnvironment.setRequestParameter("caseid", caseID);
                actionEnvironment.setResponseName("viewtrialsessioncomplete");
            } else {
                actionEnvironment.setRequestParameter("errormessage", errormessage);
                actionEnvironment.setResponseName("deletetrialsessioncomplete");
            }
        }
    }
}
