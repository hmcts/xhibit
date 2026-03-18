package uk.gov.courtservice.xhibit.web.wf.action;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Edit Case Details Action
 * </p>
 * <p>
 * Description: The action for editing Case Details.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         EditCaseDetailsAction.java,v $ Revision 1.3 2005/04/27 08:26:58
 *         bzjrnl Manual Merge From BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:43 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.1 2004/11/04 14:35:16 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.6 2003/12/04 17:37:22 xzmw8n Fix to distinguish between duplicate
 * form submission and session timeout
 * 
 * Revision 1.5 2003/10/14 14:04:14 xzmw8n Added tokens and checks to identify
 * and reject duplicate form submissions
 * 
 * Revision 1.4 2003/05/27 16:00:12 rz3jq5 Fixed to ensure that case details are
 * properly saved.
 * 
 * Revision 1.3 2003/04/30 17:13:03 fz0n8j Quick fixes.
 * 
 * Revision 1.2 2003/04/30 11:44:03 qzd3k3 Merge from dev branch.
 * 
 * Revision 1.1.2.2 2003/04/25 15:29:29 hzf3bb no message
 * 
 * Revision 1.1.2.1 2003/04/23 14:14:32 fz0n8j Added edit case details for cps
 * 
 * 
 */
public class EditCaseDetailsAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public EditCaseDetailsAction() {
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
                if (actionEnvironment.isUserInRole("XHBCPS")) {
                    String caseid = (String) actionEnvironment.getRequestParameter("caseid");
                    CaseDetail caseDetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));
                    if (caseDetail != null) {
                        actionEnvironment.setRequestParameter("caseDetail", caseDetail);
                        actionEnvironment
                                .setRequestParameter("caseid", actionEnvironment.getRequestParameter("caseid"));
                    }
                }
            } catch (CaseNotFoundException e) {
                throw new FrameworkException(e);
            }
            actionEnvironment.setResponseName("editcasedetailscomplete");
        }
    }
}
