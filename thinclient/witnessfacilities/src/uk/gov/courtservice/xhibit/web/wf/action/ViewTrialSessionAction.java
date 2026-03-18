package uk.gov.courtservice.xhibit.web.wf.action;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: View Trial Session Action
 * </p>
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * @author David Duncan (2003)
 * @version $Id: ViewTrialSessionAction.java,v 1.3 2005/04/27 08:26:59 bzjrnl
 *          Exp $
 */
public class ViewTrialSessionAction extends AbstractAction {
    /**
     * Empty default constructor
     */
    public ViewTrialSessionAction() {
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
            String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");
            actionEnvironment.setRequestParameter("pagesource", pageRequestFrom);
            SkeletonSchedule schedule = null;

            try {
                schedule = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseID));

            } catch (NumberFormatException e) {
                throw new FrameworkException(e);
            } catch (ScheduleNotFoundException e) {
                throw new FrameworkException(e);
            }

            TrialSession[] trialsessions = null;
            if (schedule != null) {
                trialsessions = schedule.getTrialSessions();
            }

            if (trialsessions != null && trialsessions.length > 0) {
                actionEnvironment.setRequestParameter("trialsessions", trialsessions);
            }
            actionEnvironment.setRequestParameter("caseid", caseID);

            actionEnvironment.setResponseName("viewtrialsessioncomplete");

        }
    }
}
