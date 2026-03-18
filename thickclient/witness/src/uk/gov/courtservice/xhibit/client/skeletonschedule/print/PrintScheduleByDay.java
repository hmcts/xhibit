/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: May 19, 2003
 * Time: 3:15:54 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.skeletonschedule.print;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoJudgeForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.PrintTransformUtil;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.ResourceUtil;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.WitnessSessionSorter;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Print Skeleton By Day Action
 * </p>
 * <p>
 * Description: The action for printing case skeleton details by day. Borrows
 * heavily from the thin client version.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003), Neil Entwistle $Revision: 1.9 $
 *         $Log: PrintScheduleByDay.java,v $
 *         Revision 1.9  2014/06/22 18:05:19  atwells
 *         8.6.2 WebLogic Upgrade - Added argument for passing users display name
 *
 *         Revision 1.8  2006/06/05 12:32:22  bzjrnl
 *         Change: TI901
 *         Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.7 2006/05/31 14:26:47
 *         bzjrnl Change: TI901 Comment: Weblogic Upgrade - Standadise code
 *         formatting Revision 1.6 2004/01/20 10:03:07 xzmw8n Added header title
 *         and court name to document
 * 
 * Revision 1.5 2003/06/17 14:09:19 xztnfq Witness enhancements as identified by
 * Doug Climie neil.entwistle-eds@eds.com
 * 
 * Revision 1.4 2003/05/22 13:19:26 xztnfq Skeleton Schedule daily and weekly
 * reports
 * 
 * Revision 1.3 2003/05/20 13:23:07 qzd3k3 Made XhibitApplicationController
 * parent of the dialog neil.entwistle-eds@eds.com
 * 
 * Revision 1.2 2003/05/20 11:36:21 qzd3k3 Added next/previous week
 * functionality neil.entwistle-eds@eds.com
 * 
 * Revision 1.1 2003/05/19 15:42:05 qzd3k3 Skeleton schedule daily print
 * neil.entwistle-eds@eds.com
 * 
 * Revision 1.1 2003/05/19 13:52:35 qzd3k3 Print report for skeleton schedule on
 * a weekly basis neil.entwistle-eds@eds.com
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
public class PrintScheduleByDay {
    private XhibitApplicationController xac;

    private static final Logger log = CSServices.getLogger(PrintScheduleByWeek.class);

    private static final String TRANSFORM_NAME = "skeletonschedule.daily.print.transform";

    /**
     * Empty default constructor
     */
    public PrintScheduleByDay(XhibitApplicationController controller) {
        this.xac = controller;
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     */
    public void printSchedule() throws CSRecoverableException {
        try {
            HashMap data = new HashMap();
            Integer caseid = xac.getApplicationCaseModel().getCaseId();
            SkeletonSchedule ss = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(caseid);
            data.put("courtname", ss.getCaseDetail().getCourtName());

            String courtPrefix = ss.getCaseDetail().getCourtPrefix();
            data.put("courtprefix", courtPrefix);

            String[] defendants = ss.getCaseDetail().getDefendantNames();
            StringBuffer defs = new StringBuffer();
            for (int i = 0; i < defendants.length; i++) {
                defs.append(defendants[i] + "\n");
            }
            // Add 0.5 to round up to the nearest day
            int duration = (int) (ss.getCaseDetail().getEstimatedCaseDuration() + 0.5);
            log.debug("$$$ Duration for case is: " + duration);

            data.put("defendantvalue", defs.toString());
            data.put("casevalue", ss.getCaseDetail().getCaseNumber());
            try {
                data.put("judgevalue", ss.getCaseDetail().getJudgeName(XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));
            } catch (NoJudgeForCaseException ex) {
                // If no judge name returned, set to blank
                data.put("judgevalue", "");
            }
            data.put("durationvalue", String.valueOf(ss.getCaseDetail().getEstimatedCaseDuration()));
            ArrayList days = new ArrayList();
            for (int i = 1; i <= duration; i++) {
                log.debug("$$$ Day " + i);
                // Convert no days to weeks
                int week = ((i + 4) / 5);
                log.debug("$$$ Week " + week);
                // Sort the Witness Sessions in the appropriate order for
                // display -
                // day, session (DESC), time
                WitnessSession[] ws = WitnessSessionSorter.sort(WitnessFactory.getInstance()
                        .getWitnessSessionSelector().getWitnessesForDay(caseid, i));
                log.debug("Witness sessions for week " + week + " and day " + i + " : " + ws.length);
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
            }
            data.put("days", days);
            PrintTransformUtil.printTransform(getResource(TRANSFORM_NAME), (HashMap) ResourceUtil.loadResources(data),
                    xac);
        } catch (Exception e) {
            log.error("$$$ Exception producing Schedule By Day report ", e);
            throw new CSRecoverableException("WITNES_XXX", "Error producing daily schedule report", e);
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

    /*
     * Utility Methods
     */

    private static String getResource(String resourceName) {
        log.debug("getResource(" + resourceName + ")");
        return XHIBITConstant.getResource(XhibitBundles.SkeletonSchedule, resourceName);
    }
}
