package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.ArrayList;
import java.util.HashMap;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoJudgeForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Print Skeleton By Week Action
 * </p>
 * <p>
 * Description: The action for printing case skeleton details by week.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.7 $ $Log:
 *         PrintSkeletonByWeekAction.java,v $ Revision 1.4 2005/06/30 15:45:52
 *         xztnfq PR 57104 Thin client reports corrected to match thick client
 *         versions
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:44 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.1 2004/11/04 14:35:16 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.12 2004/01/19 16:42:54 xzmw8n Added court prefix to map for xml
 * 
 * Revision 1.11 2003/11/25 11:17:43 xzmw8n Changed court parameter to courtname
 * (avoids clash with parameter of same name in message properties)
 * 
 * Revision 1.10 2003/08/14 13:24:59 rz7jlh Bug fix and added notes column.
 * 
 * Revision 1.9 2003/08/11 16:42:27 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.8 2003/06/17 14:35:30 xztnfq Retrieves judges name from the mid
 * tier - if non for the case, default to blank. neil.entwistle-eds@eds.com
 * 
 * Revision 1.7 2003/05/22 13:13:34 fz0n8j Bug fix (supposed to show defence)
 * 
 * Revision 1.6 2003/05/20 14:51:23 fz0n8j Now sorts witness sessions.
 * 
 * Revision 1.5 2003/05/20 10:40:24 fz0n8j Bug fixes
 * 
 * Revision 1.4 2003/05/15 09:15:09 fz0n8j Does not display defence witnesses
 * when printing from schedule.
 * 
 * Revision 1.3 2003/05/13 16:40:54 fz0n8j Bug fixes
 * 
 * Revision 1.2 2003/05/13 14:42:52 fz0n8j More updates to print schedule by
 * week.
 * 
 * Revision 1.1 2003/05/13 12:55:32 fz0n8j Adding print for schedule by week.
 * 
 * 
 */
public class PrintSkeletonByWeekAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public PrintSkeletonByWeekAction() {
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
            HashMap data = new HashMap();
            Integer caseid = new Integer((String) actionEnvironment.getRequestParameter("caseid"));
            SkeletonSchedule ss = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(caseid);
            data.put("courtname", ss.getCaseDetail().getCourtName());

            String courtPrefix = ss.getCaseDetail().getCourtPrefix();
            data.put("courtprefix", courtPrefix);

            String[] defendants = ss.getCaseDetail().getDefendantNames();
            StringBuffer defs = new StringBuffer();
            for (int i = 0; i < defendants.length; i++) {
                defs.append(defendants[i] + "\n");
            }
            data.put("defendantvalue", defs.toString());
            data.put("casevalue", ss.getCaseDetail().getCaseNumber());
            try {
                data.put("judgevalue", ss.getCaseDetail().getJudgeName((actionEnvironment.getSessionParameter(UserTerminalProperties.DISPLAY_NAME.toString())).toString())); // "causes
                // cc
                // exception");
            } catch (NoJudgeForCaseException ex) {
                // If no judge name returned, set to blank
                data.put("judgevalue", "");
            }
            data.put("durationvalue", String.valueOf(ss.getCaseDetail().getEstimatedCaseDuration()));
            ArrayList weeks = new ArrayList();
            for (int i = 0; i <= (((ss.getCaseDetail().getEstimatedCaseDuration() + 4) / 5)); i++) {
                if (WitnessFactory.getInstance().getWitnessSessionSelector().areWitnessesInWeek(caseid, i)) {
                    HashMap week = new HashMap();
                    week.put("number", String.valueOf(i));
                    ArrayList sessions = new ArrayList();
                    WitnessSession[] ws = WitnessFactory.getInstance().getWitnessSessionSelector().getWitnessesForWeek(
                            caseid, i);
                    java.util.Arrays.sort(ws);

                    for (int j = 0; j < ws.length; j++) {
                        HashMap witnesssession = new HashMap();
                        witnesssession.put("day", String.valueOf(ws[j].getDayNumber()));
                        witnesssession.put("sessiontype", ws[j].getSessionType());
                        witnesssession.put("name", ws[j].getName());
                        witnesssession.put("status", ws[j].getStatus());
                        witnesssession.put("type", ws[j].getType());
                        witnesssession.put("expected", ws[j].getExpected());
                        witnesssession.put("notes", ws[j].getTrialSession().getNotes());
                        sessions.add(witnesssession);
                    }
                    week.put("sessions", sessions);
                    weeks.add(week);
                }
                data.put("weeks", weeks);
            }
            actionEnvironment.setRequestParameter(
                    uk.gov.courtservice.xhibit.web.framework.response.PDFResponse.dataKey, data);
            actionEnvironment.setResponseName("printskeletonbyweekcomplete");
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }
}
