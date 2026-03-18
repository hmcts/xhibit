package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.ArrayList;
import java.util.HashMap;

import uk.gov.courtservice.xhibit.business.services.witness.schedule.CaseDetailFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Print Witness Details Future Action
 * </p>
 * <p>
 * Description: The action for printing future witness details.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         PrintWitnessDetailsFutureAction.java,v $ Revision 1.3 2005/04/27
 *         08:26:59 bzjrnl Manual Merge From BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:45 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.2 2005/01/17 13:58:25 tzj8k5 PRE 317 Display and sort trial
 * session info
 * 
 * Revision 1.1 2004/11/04 14:35:17 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.7 2004/03/26 13:05:39 tzj8k5 Set up header parameters for printing
 * 
 * Revision 1.6 2003/08/11 16:42:28 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.5 2003/05/07 16:09:15 fz0n8j Now calls witness summary factory.
 * 
 * Revision 1.4 2003/05/02 15:33:56 fz0n8j *** empty log message ***
 * 
 * Revision 1.3 2003/04/30 11:44:02 qzd3k3 Merge from dev branch.
 * 
 * Revision 1.2 2003/04/08 21:00:03 sz0t7n result of merge - 08/04/2003
 * 
 * Revision 1.1.2.1 2003/04/07 15:14:21 fz0n8j Added to cvs.
 * 
 * 
 */
public class PrintWitnessDetailsFutureAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public PrintWitnessDetailsFutureAction() {
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
        try {
            String caseid = (String) actionEnvironment.getRequestParameter("caseid");
            CaseDetail caseDetail = CaseDetailFactory.getInstance().getCaseDetail(new Integer(caseid));
            WitnessSession[] witnesses = WitnessFactory.getInstance().getWitnessSessionSelector()
                    .getAllFutureWitnesses(new Integer(caseid));
            if (caseDetail != null) {

                HashMap data = new HashMap();
                data.put("courtname", caseDetail.getCourtName());
                data.put("courtprefix", caseDetail.getCourtPrefix());
                StringBuffer defendants = new StringBuffer();
                String[] defendantsArray = caseDetail.getDefendantNames();
                for (int i = 0; i < defendantsArray.length; i++) {
                    defendants.append(defendantsArray[i] + " ");
                }
                data.put("defendantsValue", defendants.toString()); // use
                // "value"
                // to
                // avoid
                // existing
                // messages
                // . . .
                data.put("caseValue", caseDetail.getCaseNumber());
                data.put("police", caseDetail.getPoliceOfficerAttending());
                data.put("caseworker", caseDetail.getCpsCaseWorker());

                actionEnvironment.setRequestParameter(
                        uk.gov.courtservice.xhibit.web.framework.response.PDFResponse.dataKey, data);

                // actionEnvironment.setRequestParameter("caseDetail",
                // caseDetail);
                // actionEnvironment.setRequestParameter("caseid",
                // actionEnvironment.getRequestParameter("caseid"));
                if (witnesses != null && witnesses.length > 0) {
                    ArrayList witnessList = new ArrayList();
                    for (int i = 0; i < witnesses.length; i++) {
                        HashMap witness = new HashMap();
                        witness.put("dayno", String.valueOf(witnesses[i].getDayNumber()));
                        witness.put("appearancedate", witnesses[i].getTrialSession().getAppearanceDate());
                        witness.put("sessiontype", witnesses[i].getSessionType());
                        witness.put("name", witnesses[i].getName());
                        witness.put("status", witnesses[i].getStatus());
                        witness.put("age", String.valueOf(witnesses[i].getAge()));
                        witness.put("dueat", witnesses[i].getDueAt());
                        witness.put("arrived", witnesses[i].getArrived());
                        witness.put("released", witnesses[i].getReleased());
                        witness.put("totaltimehr", String.valueOf(witnesses[i].getTotalTimeHours()));
                        witness.put("totaltimemin", String.valueOf(witnesses[i].getTotalTimeMinutes()));
                        witnessList.add(witness);
                    }
                    data.put("witness", witnessList);
                    // actionEnvironment.setRequestParameter("witnesses",
                    // witnesses);
                }
            }

            actionEnvironment.setResponseName("printwitnessdetailspdfcomplete");
            // actionEnvironment.setResponseName("printwitnessdetailspdfcomplete");
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }
}
