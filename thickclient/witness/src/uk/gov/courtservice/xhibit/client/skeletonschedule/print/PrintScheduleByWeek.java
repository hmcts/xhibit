/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: May 14, 2003
 * Time: 10:20:08 AM
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
 * Title: Print Skeleton By Week Action
 * </p>
 * <p>
 * Description: The action for printing case skeleton details by week. Borrows
 * heavily from the thin client version.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003), Neil Entwistle $Revision: 1.11 $
 *         $Log: PrintScheduleByWeek.java,v $
 *         Revision 1.11  2014/06/22 18:05:20  atwells
 *         8.6.2 WebLogic Upgrade - Added argument for passing users display name
 *
 *         Revision 1.10  2006/06/05 12:32:22  bzjrnl
 *         Change: TI901
 *         Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.9 2006/05/31 14:26:47
 *         bzjrnl Change: TI901 Comment: Weblogic Upgrade - Standadise code
 *         formatting Revision 1.8 2004/01/20 10:03:07 xzmw8n Added header title
 *         and court name to document
 * 
 * Revision 1.7 2003/08/15 09:45:52 xztnfq X53822- added a notes column for
 * trial session notes on the weekly Skeleton Schedule.
 * neil.entwistle-eds@eds.com
 * 
 * Revision 1.6 2003/06/17 14:09:19 xztnfq Witness enhancements as identified by
 * Doug Climie neil.entwistle-eds@eds.com
 * 
 * Revision 1.5 2003/05/22 13:19:26 xztnfq Skeleton Schedule daily and weekly
 * reports
 * 
 * Revision 1.4 2003/05/20 13:23:07 qzd3k3 Made XhibitApplicationController
 * parent of the dialog neil.entwistle-eds@eds.com
 * 
 * Revision 1.3 2003/05/20 11:36:21 qzd3k3 Added next/previous week
 * functionality neil.entwistle-eds@eds.com
 * 
 * Revision 1.2 2003/05/19 15:42:05 qzd3k3 Skeleton schedule daily print
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
public class PrintScheduleByWeek {
    private XhibitApplicationController xac;

    private static final Logger log = CSServices.getLogger(PrintScheduleByWeek.class);

    private static final String TRANSFORM_NAME = "skeletonschedule.weekly.print.transform";

    /**
     * Empty default constructor
     */
    public PrintScheduleByWeek(XhibitApplicationController controller) {
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
            int duration = ((int) (ss.getCaseDetail().getEstimatedCaseDuration() + 4)) / 5;
            log.debug("$$$ Duration for case is: " + duration);

            for (int i = 0; i < defendants.length; i++) {
                defs.append(defendants[i] + "\n");
            }
            data.put("defendantvalue", defs.toString());
            data.put("casevalue", ss.getCaseDetail().getCaseNumber());
            try {
                data.put("judgevalue", ss.getCaseDetail().getJudgeName(XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));
            } catch (NoJudgeForCaseException ex) {
                // If no judge name returned, set to blank
                data.put("judgevalue", "");
            }
            data.put("durationvalue", String.valueOf(ss.getCaseDetail().getEstimatedCaseDuration()));
            ArrayList weeks = new ArrayList();
            for (int i = 1; i <= duration; i++) {
                if (WitnessFactory.getInstance().getWitnessSessionSelector().areWitnessesInWeek(caseid, i)) {
                    HashMap week = new HashMap();
                    week.put("number", String.valueOf(i));
                    ArrayList sessions = new ArrayList();
                    WitnessSession[] ws = WitnessSessionSorter.sort(WitnessFactory.getInstance()
                            .getWitnessSessionSelector().getWitnessesForWeek(caseid, i));
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
            PrintTransformUtil.printTransform(getResource(TRANSFORM_NAME), (HashMap) ResourceUtil.loadResources(data),
                    xac);
        } catch (Exception e) {
            log.error("$$$ Exception producing Schedule By Week report ", e);
            throw new CSRecoverableException("WITNES_XXX", "Error producing weekly schedule report", e);
        }
    }

    /*
     * Utility Methods
     */

    private static String getResource(String resourceName) {
        log.debug("getResource(" + resourceName + ")");
        return XHIBITConstant.getResource(XhibitBundles.SkeletonSchedule, resourceName);
    }
}
