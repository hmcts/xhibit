package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Enumeration;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Add Witness Action
 * </p>
 * <p>
 * Description: The action for adding a witness.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * @author David Duncan (2003)
 * @version $Id: AddWitnessAction.java,v 1.5 2006/06/05 12:32:38 bzjrnl Exp $
 */
public class AddWitnessAction extends AbstractAction {
    /**
     * Empty default constructor
     */
    public AddWitnessAction() {
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

            String caseid = (String) actionEnvironment.getRequestParameter("caseid");
            CaseDetail casedetail;
            Enumeration en = actionEnvironment.getRequestParameterNames();
            Integer week = null;
            while (en.hasMoreElements()) {
                String name = (String) en.nextElement();
                if (name.equals("week")) {
                    week = new Integer((String) actionEnvironment.getRequestParameter(name));
                    actionEnvironment.setRequestParameter("week", week);
                }
            }
            try {
                casedetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));
            } catch (NumberFormatException e) {
                throw new FrameworkException(e);
            } catch (CaseNotFoundException e) {
                throw new FrameworkException(e);
            }

            if ((casedetail != null) && (casedetail.getCaseType() != null)) {
                actionEnvironment.setRequestParameter("casetype", casedetail.getCaseType());
            }

            SkeletonSchedule schedule = null;

            try {
                schedule = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseid));
            } catch (Exception e) {
                throw new FrameworkException(e);
            }

            TrialSession[] session = null;
            if (schedule != null)
                session = schedule.getTrialSessions();

            if ((session != null) && (session.length > 0)) {
                actionEnvironment.setRequestParameter("ts", session);
            }
            /*
             * Refactored out, directly access isUserInRole value in the JSP if
             * (actionEnvironment.isUserInRole("XHBCPS")) {
             * actionEnvironment.setRequestParameter("CPS", "CPS"); }
             */
            actionEnvironment.setRequestParameter("caseid", caseid);
            actionEnvironment.setResponseName("addwitnesscomplete");
        }
    }
}
