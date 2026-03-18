package uk.gov.courtservice.xhibit.web.wf.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Maintain Witness Details Action
 * </p>
 * <p>
 * Description: The action for maintaining witness details. <p/>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         MaintainWitnessDetailsAction.java,v $ Revision 1.3 2005/04/27
 *         08:26:59 bzjrnl Manual Merge From BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:44 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.5 2005/01/17 13:58:25 tzj8k5 PRE 317 Display and sort trial
 * session info
 * 
 * Revision 1.4 2004/12/09 15:27:31 sz0t7n Make thin client exceptions use the
 * CSException framework
 * 
 * Revision 1.3 2004/11/04 14:35:16 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.11 2004/03/11 16:42:23 qzd3k3 Code linting.
 * 
 * Revision 1.10.18.1 2004/03/10 15:47:07 qzd3k3 Code linting, most of these
 * fixes are poor exception handling. This may result as a side effect in more
 * bugs appearing; but that is a necessary as they were hidden bugs before.
 * 
 * Revision 1.10 2003/12/04 17:37:22 xzmw8n Fix to distinguish between duplicate
 * form submission and session timeout <p/> Revision 1.9 2003/10/13 15:28:24
 * xzmw8n Added check for duplicate form submission <p/> Revision 1.8 2003/08/11
 * 16:42:27 bzw8gp Jon Powell <p/> organise imports (remove unused) <p/>
 * Revision 1.7 2003/05/20 16:17:32 fz0n8j CPS now automatically creates a
 * schedule for a case if one does not exist. <p/> Revision 1.6 2003/05/13
 * 10:49:42 fz0n8j Bug fix. <p/> Revision 1.5 2003/05/07 16:09:12 fz0n8j Now
 * calls witness summary factory. <p/> Revision 1.4 2003/04/30 17:13:03 fz0n8j
 * Quick fixes. <p/> Revision 1.3 2003/04/30 11:44:02 qzd3k3 Merge from dev
 * branch. <p/> Revision 1.2.2.8 2003/04/30 11:25:43 fz0n8j Commited before
 * merge <p/> Revision 1.2.2.7 2003/04/25 15:29:29 hzf3bb no message <p/>
 * Revision 1.2.2.6 2003/04/23 14:14:30 fz0n8j Added edit case details for cps
 * <p/> Revision 1.2.2.5 2003/04/23 08:41:45 fz0n8j MErged <p/> Revision 1.2.2.4
 * 2003/04/22 17:34:34 hzf3bb *** empty log message *** <p/> Revision 1.2.2.3
 * 2003/04/14 13:25:15 fz0n8j Now possible to view all witnesses. <p/> Revision
 * 1.2.2.2 2003/04/14 10:21:12 fz0n8j Modified actions to work with latest
 * middle tier code. <p/> Revision 1.2.2.1 2003/04/11 08:41:23 qzd3k3
 * Refactoring work. <p/> Revision 1.2 2003/04/04 08:41:36 fz0n8j More pages now
 * use database. <p/> Revision 1.1 2003/04/01 13:59:53 fz0n8j Added actions
 * again and modified jsps/properites. <p/> Revision 1.1 2003/03/28 09:58:46
 * fz0n8j Added witness details and court status.
 */
public class MaintainWitnessDetailsAction extends AbstractAction {
    private static final Logger log = CSServices.getLogger(MaintainWitnessDetailsAction.class);

    /**
     * Empty default constructor
     */
    public MaintainWitnessDetailsAction() {
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

            try {

                String caseid = (String) actionEnvironment.getRequestParameter("caseid");
                try {
                    SkeletonSchedule ss = SkeletonScheduleFactory.getInstance()
                            .getSkeletonSchedule(new Integer(caseid));
                    if (ss == null) // there is no scedule for this case . .
                    // .
                    {
                        makeSchedule(actionEnvironment, new Integer(caseid));
                    }
                } catch (ScheduleNotFoundException e) { // there is no scedule
                    // for this case . . .
                    makeSchedule(actionEnvironment, new Integer(caseid));
                }
                CaseDetail caseDetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));
                // use witness session object
                WitnessSession[] witnesses = WitnessFactory.getInstance().getWitnessSessionSelector()
                        .getTodayWitnesses(new Integer(caseid));
                if (caseDetail != null) {
                    if (actionEnvironment.isUserInRole("XHBCPS")) {
                        actionEnvironment.setRequestParameter("CPS", "CPS");
                    }
                    actionEnvironment.setRequestParameter("caseDetail", caseDetail);
                    actionEnvironment.setRequestParameter("caseid", actionEnvironment.getRequestParameter("caseid"));
                    if (witnesses != null && witnesses.length > 0) {
                        actionEnvironment.setRequestParameter("witnesses", witnesses);
                    }
                }
                actionEnvironment.setResponseName("maintainwitnessdetailscomplete");

            } catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e) {
                throw new FrameworkException(e);
            }
        }
    }

    public void makeSchedule(ActionEnvironment actionEnvironment, Integer caseid) throws FrameworkException {
        if (actionEnvironment.isUserInRole("XHBCPS")) // if the user is cps
        // create one . . . .
        {
            try {
                log.debug("------------- About to create schedule . . .");
                SkeletonScheduleFactory.getInstance().createSkeletonSchedule(caseid);
            } catch (ScheduleModificationException sme) {
                throw new FrameworkException("witness.createschedule", "Cannot create scedule for case!", sme);
            }
        }
    }
}
