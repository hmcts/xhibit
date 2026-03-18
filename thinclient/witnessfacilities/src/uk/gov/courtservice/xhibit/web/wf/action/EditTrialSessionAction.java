package uk.gov.courtservice.xhibit.web.wf.action;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Edit Trial Day List Action
 * </p>
 * <p>
 * Description: The action for editing a trial day list.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * @author David Duncan (2003)
 * @version $Id: EditTrialSessionAction.java,v 1.3 2005/04/27 08:26:58 bzjrnl
 *          Exp $
 */
public class EditTrialSessionAction extends AbstractAction {
    /**
     * Empty default constructor
     */
    public EditTrialSessionAction() {
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

        String caseID = (String) actionEnvironment.getRequestParameter("caseid");

        String day = (String) actionEnvironment.getRequestParameter("trialday");
        String session = (String) actionEnvironment.getRequestParameter("trialsession");
        String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");
        actionEnvironment.setRequestParameter("pagesource", pageRequestFrom);

        SkeletonSchedule schedule;
        TrialSession trialsession;
        try {
            schedule = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseID));
            trialsession = schedule.getTrialSession(new Integer(day), session);
        } catch (NumberFormatException e) {
            throw new FrameworkException(e);
        } catch (TrialSessionNotFoundException e) {
            throw new FrameworkException(e);
        } catch (ScheduleNotFoundException e) {
            throw new FrameworkException(e);
        }

        actionEnvironment.setRequestParameter("caseid", caseID);
        actionEnvironment.setRequestParameter("trialsession", trialsession);
        actionEnvironment.setResponseName("edittrialsessioncomplete");
    }
}
