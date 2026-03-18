package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Enumeration;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Edit Witness Action
 * </p>
 * <p>
 * Description: The action for editing witness details.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * @author David Duncan (2003)
 * @version $Id: EditWitnessAction.java,v 1.6 2006/06/05 12:32:38 bzjrnl Exp $
 */
public class EditWitnessAction extends AbstractAction {
    private static final Logger log = CSServices.getLogger(EditWitnessAction.class);

    /**
     * Empty default constructor
     */
    public EditWitnessAction() {
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
        log.debug("EditWitnessAction");

        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("loggedout");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }
            String caseid = (String) actionEnvironment.getRequestParameter("caseid");
            String id = (String) actionEnvironment.getRequestParameter("id");
            String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");
            actionEnvironment.setRequestParameter("pagesource", pageRequestFrom);

            CaseDetail casedetail;
            WitnessDetail witnessdetail;
            TrialSession trialsession;
            TrialSession[] ts;
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
                witnessdetail = WitnessFactory.getInstance().getWitnessDetail(new Integer(id));
                trialsession = WitnessFactory.getInstance().getWitnessSession(new Integer(id)).getTrialSession();
                ts = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(new Integer(caseid)).getTrialSessions();
            } catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e) {
                throw new FrameworkException(e);
            } catch (NumberFormatException e) {
                throw new FrameworkException(e);
            } catch (WitnessNotFoundException e) {
                throw new FrameworkException(e);
            } catch (ScheduleNotFoundException e) {
                throw new FrameworkException(e);
            }

            if ((casedetail != null) && (casedetail.getCaseType() != null)) {
                actionEnvironment.setRequestParameter("casedetail", casedetail);
            }

            if (witnessdetail != null) {
                actionEnvironment.setRequestParameter("witnessdetail", witnessdetail);
            }

            if (trialsession != null) {
                actionEnvironment.setRequestParameter("trialsession", trialsession);
            }

            if (ts != null) {
                actionEnvironment.setRequestParameter("ts", ts);
            }

            actionEnvironment.setRequestParameter("caseid", caseid);
            actionEnvironment.setResponseName("editwitnesscomplete");
        }
    }
}
