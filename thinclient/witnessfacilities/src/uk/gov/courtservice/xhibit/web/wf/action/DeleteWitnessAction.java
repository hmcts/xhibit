package uk.gov.courtservice.xhibit.web.wf.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
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
 * Title: Delete Witness Action
 * </p>
 * <p>
 * Description: The action for deleting witness details.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * Author: David Duncan (2003) $$ $Log: DeleteWitnessAction.java,v $
 * Author: David Duncan (2003) $$ Revision 1.5  2006/06/05 12:32:38  bzjrnl
 * Author: David Duncan (2003) $$ Change: TI901
 * Author: David Duncan (2003) $$ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Author: David Duncan (2003) $$ Author:
 * David Duncan (2003) $$ Revision 1.4 2006/05/31 14:27:10 bzjrnl Author: David
 * Duncan (2003) $$ Change: TI901 Author: David Duncan (2003) $$ Comment:
 * Weblogic Upgrade - Standadise code formatting Author: David Duncan (2003) $$
 * Revision 1.3 2005/04/27 08:26:58 bzjrnl Manual Merge From BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:43 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.7 2004/04/19 14:39:51 tzj8k5 Navigation when deleting a witness
 * 
 * Revision 1.6 2003/12/04 17:37:23 xzmw8n Fix to distinguish between duplicate
 * form submission and session timeout
 * 
 * Revision 1.5 2003/10/14 14:04:12 xzmw8n Added tokens and checks to identify
 * and reject duplicate form submissions
 * 
 * Revision 1.4 2003/05/07 12:54:41 fz0n8j Uses newer access for the trial
 * session
 * 
 * Revision 1.3 2003/05/07 10:26:29 fz0n8j *** empty log message ***
 * 
 * Revision 1.2 2003/04/30 11:44:00 qzd3k3 Merge from dev branch.
 * 
 * Revision 1.1.2.3 2003/04/30 11:25:41 fz0n8j Commited before merge
 * 
 * Revision 1.1.2.2 2003/04/25 15:29:28 hzf3bb no message
 * 
 * Revision 1.1.2.1 2003/04/24 16:31:14 hzf3bb *** empty log message ***
 * 
 * Revision 1.1 2003/04/02 09:02:05 hzf3bb added new action
 * 
 * 
 */
public class DeleteWitnessAction extends AbstractAction {
    private static final Logger log = CSServices.getLogger(DeleteWitnessAction.class);

    /**
     * Empty default constructor
     */
    public DeleteWitnessAction() {
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
            String id = (String) actionEnvironment.getRequestParameter("id");
            String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");
            actionEnvironment.setRequestParameter("pagesource", pageRequestFrom);
            CaseDetail casedetail;
            WitnessDetail witnessdetail;
            TrialSession trialsession;
            try {
                casedetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));
                witnessdetail = WitnessFactory.getInstance().getWitnessDetail(new Integer(id));
                trialsession = WitnessFactory.getInstance().getWitnessSession(new Integer(id)).getTrialSession();
            } catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException e) {
                throw new FrameworkException(e);
            } catch (NumberFormatException e) {
                throw new FrameworkException(e);
            } catch (WitnessNotFoundException e) {
                throw new FrameworkException(e);
            }

            if ((casedetail != null) && (casedetail.getCaseType() != null)) {
                actionEnvironment.setRequestParameter("casedetail", casedetail);
            }

            if (witnessdetail != null) {
                log.debug("***************" + witnessdetail.getName());
                log.debug("***************" + witnessdetail.getExpected());
                log.debug("***************" + witnessdetail.getStatus());
                actionEnvironment.setRequestParameter("witnessdetail", witnessdetail);
            }

            if (trialsession != null) {
                actionEnvironment.setRequestParameter("trialsession", trialsession);
            }

            actionEnvironment.setRequestParameter("caseid", caseid);
            actionEnvironment.setResponseName("deletewitnesscomplete");
        }
    }

}
