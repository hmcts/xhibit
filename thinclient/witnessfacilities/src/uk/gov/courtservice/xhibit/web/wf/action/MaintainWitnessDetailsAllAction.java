package uk.gov.courtservice.xhibit.web.wf.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Maintain Witness Details All Future
 * </p>
 * <p>
 * Description: The action for maintaining witness details (future view).
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         MaintainWitnessDetailsAllAction.java,v $ Revision 1.3 2005/04/27
 *         08:26:59 bzjrnl Manual Merge From BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:44 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.2 2005/01/17 13:58:25 tzj8k5 PRE 317 Display and sort trial
 * session info
 * 
 * Revision 1.1 2004/11/04 14:35:16 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.7 2003/12/04 17:37:22 xzmw8n Fix to distinguish between duplicate
 * form submission and session timeout
 * 
 * Revision 1.6 2003/10/14 14:04:14 xzmw8n Added tokens and checks to identify
 * and reject duplicate form submissions
 * 
 * Revision 1.5 2003/08/11 16:42:27 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.4 2003/05/07 16:09:13 fz0n8j Now calls witness summary factory.
 * 
 * Revision 1.3 2003/04/30 17:13:03 fz0n8j Quick fixes.
 * 
 * Revision 1.2 2003/04/30 11:44:03 qzd3k3 Merge from dev branch.
 * 
 * Revision 1.1.2.2 2003/04/23 14:14:31 fz0n8j Added edit case details for cps
 * 
 * Revision 1.1.2.1 2003/04/14 13:25:17 fz0n8j Now possible to view all
 * witnesses.
 * 
 * 
 * 
 */
public class MaintainWitnessDetailsAllAction extends AbstractAction {

    /**
     * The log4j logger
     */

    private static final Logger log = CSServices.getLogger(MaintainWitnessDetailsAllAction.class);

    /**
     * Empty default constructor
     */
    public MaintainWitnessDetailsAllAction() {
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
                CaseDetail caseDetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));
                WitnessSession witnesses[];
                if (actionEnvironment.isUserInRole("XHBCPS")) {
                    actionEnvironment.setRequestParameter("CPS", "CPS");
                    witnesses = WitnessFactory.getInstance().getWitnessSessionSelector().getAllWitnessesForSession(
                            new Integer(caseid));
                } else {
                    witnesses = WitnessFactory.getInstance().getWitnessSessionSelector().getAllTodayAndFutureWitnesses(
                            new Integer(caseid));
                }
                if (caseDetail != null) {
                    actionEnvironment.setRequestParameter("caseDetail", caseDetail);
                    actionEnvironment.setRequestParameter("caseid", actionEnvironment.getRequestParameter("caseid"));
                    if (witnesses != null && witnesses.length > 0) {
                        actionEnvironment.setRequestParameter("witnesses", witnesses);
                    }
                }
                actionEnvironment.setResponseName("maintainwitnessdetailsallcomplete");
            } catch (Exception e) {
                throw new FrameworkException(e);
            }
        }
    }
}
