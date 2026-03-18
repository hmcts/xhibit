package uk.gov.courtservice.xhibit.web.wf.action;

import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Add Witness Action
 * </p>
 * <p>
 * Description: The action for confirming a witness delete.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * @author David Duncan (2003)
 * @version $Id: ConfirmDeleteWitnessAction.java,v 1.3 2005/04/27 08:26:58
 *          bzjrnl Exp $
 */
public class ConfirmDeleteWitnessAction extends AbstractAction {
    private static final String MAINTAINWITNESSALL = "maintainwitnessdetailsall";

    private static final String MAINTAINWITNESSFUTURE = "maintainwitnessdetailsfuture";

    /**
     * Empty default constructor
     */
    public ConfirmDeleteWitnessAction() {
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
            try {
                WitnessDetail wd = WitnessFactory.getInstance().getWitnessDetail(new Integer(id));
                wd.remove();
                CaseDetail caseDetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));

                // set up witnesses according to which page to be displayed next
                WitnessSummary witnesses[];
                // by default display todays unless deleted from another page
                witnesses = WitnessFactory.getInstance().getWitnessSummarySelector().getTodaysWitnesses(
                        new Integer(caseid));

                if (pageRequestFrom.equals(MAINTAINWITNESSALL)) {
                    // same logic as per MaintainWitnessDetailsAction.java
                    if (actionEnvironment.isUserInRole("XHBCPS")) {
                        actionEnvironment.setRequestParameter("CPS", "CPS");
                        witnesses = WitnessFactory.getInstance().getWitnessSummarySelector().getAllWitnesses(
                                new Integer(caseid));
                    } else {
                        witnesses = WitnessFactory.getInstance().getWitnessSummarySelector()
                                .getTodayAndFutureWitnesses(new Integer(caseid));
                    }
                }
                if (pageRequestFrom.equals(MAINTAINWITNESSFUTURE)) {
                    witnesses = WitnessFactory.getInstance().getWitnessSummarySelector().getFutureWitnesses(
                            new Integer(caseid));
                }

                if (caseDetail != null) {
                    /*
                     * Refactored out, directly access isUserInRole value in the
                     * JSP if (actionEnvironment.isUserInRole("XHBCPS")) {
                     * actionEnvironment.setRequestParameter("CPS", "CPS"); }
                     */
                    actionEnvironment.setRequestParameter("caseDetail", caseDetail);
                    actionEnvironment.setRequestParameter("caseid", actionEnvironment.getRequestParameter("caseid"));
                    if (witnesses != null && witnesses.length > 0) {
                        actionEnvironment.setRequestParameter("witnesses", witnesses);
                    }
                }
                actionEnvironment.setResponseName(pageRequestFrom + "complete");

            } catch (Exception e) {
                throw new FrameworkException(e);
            }
        }
    }
}