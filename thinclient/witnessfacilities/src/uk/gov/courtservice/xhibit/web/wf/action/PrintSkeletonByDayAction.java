package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
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
 * Title: Print Skeleton By Day Action
 * </p>
 * <p>
 * Description: The action for printing case skeleton details by day.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.8 $ $Log:
 *         PrintSkeletonByDayAction.java,v $ Revision 1.5 2006/02/09 11:27:52
 *         xztnfq Change: PR 57825 Comment: Thin client version of Print by Day
 *         report did not show week number
 * 
 * Revision 1.4 2005/06/30 15:45:51 xztnfq PR 57104 Thin client reports
 * corrected to match thick client versions
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:44 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.1 2004/11/04 14:35:16 bzjrnl Changes to correct problems with
 * cookie access.
 * 
 * Revision 1.9 2004/01/19 16:42:54 xzmw8n Added court prefix to map for xml
 * 
 * Revision 1.8 2003/11/25 11:17:43 xzmw8n Changed court parameter to courtname
 * (avoids clash with parameter of same name in message properties)
 * 
 * Revision 1.7 2003/08/11 16:42:27 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.6 2003/06/17 14:35:30 xztnfq Retrieves judges name from the mid
 * tier - if non for the case, default to blank. neil.entwistle-eds@eds.com
 * 
 * Revision 1.5 2003/05/22 13:13:33 fz0n8j Bug fix (supposed to show defence)
 * 
 * Revision 1.4 2003/05/20 10:40:24 fz0n8j Bug fixes
 * 
 * Revision 1.3 2003/05/19 08:50:26 qzd3k3 Chnaged to use getWitnessesForDay
 * rather than getWitnessesForWeekAndDay
 * 
 * Revision 1.2 2003/05/15 09:15:09 fz0n8j Does not display defence witnesses
 * when printing from schedule.
 * 
 * Revision 1.1 2003/05/14 14:45:42 fz0n8j Bug fix, added
 * printskeletonbydayaction
 * 
 * 
 */
public class PrintSkeletonByDayAction extends AbstractAction {
    /**
     * The log4j logger
     */

    private final Logger log = CSServices.getLogger(PrintSkeletonByDayAction.class);

    /**
     * Empty default constructor
     */
    public PrintSkeletonByDayAction() {
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

            String courtPrefix = ss.getCaseDetail().getCourtPrefix();
            data.put("courtprefix", courtPrefix);

            data.put("courtname", ss.getCaseDetail().getCourtName());

            String[] defendants = ss.getCaseDetail().getDefendantNames();
            StringBuffer defs = new StringBuffer();
            for (int i = 0; i < defendants.length; i++) {
                defs.append(defendants[i] + "\n");
            }
            data.put("defendantvalue", defs.toString());
            data.put("casevalue", ss.getCaseDetail().getCaseNumber());
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
            ArrayList days = new ArrayList();

            // Add 0.5 to round up to the nearest day
            int duration = (int) (ss.getCaseDetail().getEstimatedCaseDuration() + 0.5);
            for (int i = 0; i <= duration; i++) {
                int week = ((i + 4) / 5);

                WitnessSession[] ws = WitnessFactory.getInstance().getWitnessSessionSelector().getWitnessesForDay(
                        caseid, i);
                java.util.Arrays.sort(ws);

                log.debug("Witness sessions for day " + i + " : " + ws.length);
                if (ws != null && ws.length > 0) {
                    HashMap day = new HashMap();
                    day.put("weeknumber", String.valueOf(week));
                    day.put("daynumber", String.valueOf(i));
                    ArrayList sessions = new ArrayList();
                    ArrayList afternoonSessions = new ArrayList();
                    for (int k = 0; k < ws.length; k++) {
                        HashMap witnesssession = new HashMap();
                        putSessionInHashMap(witnesssession, ws[k]);
                        if (ws[k].getSessionType().equalsIgnoreCase("M")) {
                            sessions.add(witnesssession);
                            if (ws[k].getTrialSession().getNotes() != null
                                    && !ws[k].getTrialSession().getNotes().equals("")) {
                                day.put("morningnotes", ws[k].getTrialSession().getNotes());
                            }
                        } else {
                            afternoonSessions.add(witnesssession);
                            if (ws[k].getTrialSession().getNotes() != null
                                    && !ws[k].getTrialSession().getNotes().equals("")) {
                                day.put("afternoonnotes", ws[k].getTrialSession().getNotes());
                            }
                        }
                    }
                    day.put("morningsessions", sessions);
                    day.put("afternoonsessions", afternoonSessions);
                    days.add(day);
                }
                data.put("days", days);
            }
            actionEnvironment.setRequestParameter(
                    uk.gov.courtservice.xhibit.web.framework.response.PDFResponse.dataKey, data);
            actionEnvironment.setResponseName("printskeletonbydaycomplete");
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }

    public void putSessionInHashMap(HashMap map, WitnessSession session) {
        map.put("day", String.valueOf(session.getDayNumber()));
        map.put("sessiontype", session.getSessionType());
        map.put("name", session.getName());
        map.put("status", session.getStatus());
        map.put("type", session.getType());
        map.put("expected", session.getExpected());
        map.put("notes", session.getTrialSession().getNotes());
    }
}
