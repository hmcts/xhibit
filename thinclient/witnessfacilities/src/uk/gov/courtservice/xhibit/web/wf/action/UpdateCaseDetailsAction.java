package uk.gov.courtservice.xhibit.web.wf.action;

import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Update Case Details Action
 * </p>
 * <p>
 * Description: The action for updating case details.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         UpdateCaseDetailsAction.java,v $ Revision 1.3 2005/04/27 08:26:59
 *         bzjrnl Manual Merge From BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:45 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.1 2004/11/04 14:35:17 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.7 2003/12/04 17:37:22 xzmw8n Fix to distinguish between duplicate
 * form submission and session timeout
 * 
 * Revision 1.6 2003/10/14 14:04:14 xzmw8n Added tokens and checks to identify
 * and reject duplicate form submissions
 * 
 * Revision 1.5 2003/05/27 16:00:12 rz3jq5 Fixed to ensure that case details are
 * properly saved.
 * 
 * Revision 1.4 2003/05/23 08:30:18 fz0n8j Bug fix
 * 
 * Revision 1.3 2003/04/30 17:13:04 fz0n8j Quick fixes.
 * 
 * Revision 1.2 2003/04/30 11:44:03 qzd3k3 Merge from dev branch.
 * 
 * Revision 1.1.2.3 2003/04/25 15:29:29 hzf3bb no message
 * 
 * Revision 1.1.2.2 2003/04/23 14:15:18 fz0n8j Bug fix
 * 
 */
public class UpdateCaseDetailsAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public UpdateCaseDetailsAction() {
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
                WitnessSummary[] witnesses = WitnessFactory.getInstance().getWitnessSummarySelector()
                        .getTodaysWitnesses(new Integer(caseid));

                if (caseDetail != null) {
                    caseDetail.setCpsCaseWorker((String) actionEnvironment.getRequestParameter("cpscaseworker"));
                    caseDetail.setPoliceOfficerAttending((String) actionEnvironment
                            .getRequestParameter("policeofficer"));
                    caseDetail.update();
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

            } catch (Exception e) {
                throw new FrameworkException(e);
            }
        }
    }
}
