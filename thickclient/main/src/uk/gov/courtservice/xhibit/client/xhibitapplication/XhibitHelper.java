package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.util.Date;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.HearingProgressValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.admin.crestimport.CrestImportController;
import uk.gov.courtservice.xhibit.client.caseprogress.CaseProgressXPanel;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTablePanel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalController;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaController;
import uk.gov.courtservice.xhibit.client.results.verdicts.VerdictsController;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Helper Class to store static methods for re-use
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: XhibitHelper.java,v 1.37 2014/06/20 18:37:54 atwells Exp $
 */
public class XhibitHelper {
    private static final Logger log = CSServices.getLogger(XhibitHelper.class);

    private XhibitHelper() {
    }

    public static void startShHearing(XhibitApplicationController xac) throws CSRecoverableException {
        if (xac.getApplicationCaseModel() == null)
            return;

        ScheduledHearingValue shv = xac.getApplicationCaseModel().getScheduledHearingValue();

        log.debug("startShHearing:: Starting hearing for SHV Id:" + shv.getId());

        if (HearingProgressValue.IN_PROGRESS.intValue() > shv.getHearingProgress().intValue()) {
            // update hearing state to tbh
            XhibitDelegateHelper.getHearingDelegate().updateHearingProgress(
                    shv.getScheduledHearingBasicValue().getId(), HearingProgressValue.IN_PROGRESS,
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        }
    }

    public static void activatePublicDisplay(XhibitApplicationController xac) {
        // Establish there is an open case
        if (xac.getApplicationCaseModel() == null)
            return;

        // Set up instance variables
        ScheduledHearingValue shv = xac.getApplicationCaseModel().getScheduledHearingValue();
        XhibitSingleton xs = XhibitSingleton.getInstance();

        // Determine if User is in same court room as schedule hearing court
        // room
        // Only prompt for Public Display Activation if court rooms are the same
        if (!xac.isScreenActive() && xs.isUserInCourtroom() && shv.getCourtRoomId() != null
                && shv.getCourtRoomId().compareTo(xs.getCourtRoomId()) == 0) {
            log.debug("activatePublicDisplay:: for SHV Id:" + shv.getScheduledHearingId());
            // prompt user
            int rc = JOptionPane.showConfirmDialog(xac, getActiveDisplayPromptText(xac), XHIBITConstant.getResource(
                    XhibitBundles.XhibitClientDefaultResources, "pd_activateTitle"), JOptionPane.YES_NO_OPTION);
            // user responds yes
            if (rc == JOptionPane.YES_OPTION) {
                // Deactivate any other xhibit windows in the same court room
                deactiveOtherPublicDisplays(xac, shv.getCourtRoomId());

                XhibitDelegateHelper.getDisplayDelegate()
                        .activatePublicDisplay(shv.getScheduledHearingId(), new Date(),
                        		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                xac.setScreenActive(true);
                if (xs.isScreenUserDeactived()) {
                    xs.setUserDeactivedScreen(false);
                }
            }
        }
    }

    private static String getActiveDisplayPromptText(XhibitApplicationController xac) {
        StringBuffer sb = new StringBuffer();
        sb.append(XHIBITConstant.getResource(XhibitBundles.XhibitClientDefaultResources, "pd_activatePanel1"));
        sb.append(' ');
        sb.append(xac.getApplicationCaseModel().getCaseType());
        sb.append(xac.getApplicationCaseModel().getCaseNumber());
        sb.append("?\n\n");
        sb.append(XHIBITConstant.getResource(XhibitBundles.XhibitClientDefaultResources, "pd_activatePanel2"));
        return sb.toString();
    }

    /**
     * Deactive any public displays that are active in any other XHIBIT windows
     * that are active and in the same court room.
     * 
     * @param xac
     * @param courtRoomId
     */
    private static void deactiveOtherPublicDisplays(XhibitApplicationController xac, Integer courtRoomId) {
        Iterator xacGroup = XhibitSingleton.getInstance().getApplications().iterator();
        while (xacGroup.hasNext()) {
            XhibitApplicationController currXac = (XhibitApplicationController) xacGroup.next();
            if (!currXac.equals(xac)
                    && currXac.isScreenActive()
                    && currXac.getApplicationCaseModel() != null
                    && courtRoomId
                            .equals(currXac.getApplicationCaseModel().getScheduledHearingValue().getCourtRoomId())) {
                log.debug("deactiveOtherPublicDisplays:: for SHV Id:"
                        + currXac.getApplicationCaseModel().getScheduledHearingValue().getScheduledHearingId());
                XhibitDelegateHelper.getDisplayDelegate().deActivatePublicDisplay(
                        currXac.getApplicationCaseModel().getScheduledHearingValue().getScheduledHearingId(),
                        new Date(), XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                currXac.setScreenActive(false);
            }
        }
    }

    public static String getHearingProgress(Integer status) {
        if (status == null)
            return "";
        String tempStatus;
        if (status.intValue() == HearingProgressValue.TO_BE_HEARD.intValue()) {
            tempStatus = getResource("hp_toBeHeard");
        } else if (status.intValue() == HearingProgressValue.IN_PROGRESS.intValue()) {
            tempStatus = getResource("hp_inProgress");
        } else if (status.intValue() == HearingProgressValue.ADJOURNED.intValue()) {
            tempStatus = getResource("hp_adjourned");
        } else if (status.intValue() == HearingProgressValue.FINISHED.intValue()) {
            tempStatus = getResource("hp_finished");
        } else {
            tempStatus = "";
        }
        return tempStatus;
    }

    public static void setTitle(XhibitApplicationController xac, XPanel bodyPanel) {
        if (bodyPanel instanceof TodaysScheduleController) {
            xac.setTitle(getResource("TodaysSchedulePanel"));
        } else if (bodyPanel instanceof CourtLogController) {
            xac.setTitle(getResource("CourtLogPanel"));
            xac.enableCourtLogActions(xac.getApplicationCaseModel().isInEditMode(FunctionList.ECourtLog));
        } else if (bodyPanel instanceof ChargesController) {
            xac.setTitle(getResource("ChargesPanel"));
            xac.enableCourtLogActions(xac.getApplicationCaseModel().isInEditMode(FunctionList.ECourtLog));
        } else if (bodyPanel instanceof CaseProgressXPanel) {
            xac.setTitle(getResource("CaseProgressPanel"));
            xac.enableCourtLogActions(xac.getApplicationCaseModel().isInEditMode(FunctionList.ECourtLog));
        } else if (bodyPanel instanceof PleaController) {
            xac.setTitle(getResource("PleaPanel"));
            xac.enableCourtLogActions(xac.getApplicationCaseModel().isInEditMode(FunctionList.ECourtLog));
        } else if (bodyPanel instanceof VerdictsController) {
            xac.setTitle(getResource("VerdictPanel"));
            xac.enableCourtLogActions(xac.getApplicationCaseModel().isInEditMode(FunctionList.ECourtLog));
        } else if (bodyPanel instanceof DisposalController) {
            xac.setTitle(getResource("SentencePanel"));
            xac.enableCourtLogActions(xac.getApplicationCaseModel().isInEditMode(FunctionList.ECourtLog));
        } else if (bodyPanel instanceof ManageListTablePanel) {
            xac.setTitle(getResource("ManageListTablePanel"));
        } else if (bodyPanel instanceof CrestImportController) {
            xac.setTitle(getResource("CrestImportController"));
        } else {
            String bodyPanelClassName = bodyPanel.getClass().getName();
            int lastPoint = bodyPanelClassName.lastIndexOf(".");
            String panelName = bodyPanelClassName.substring(lastPoint + 1);
            if (panelName.equals("SkeletonSchedulePanel")) {
                xac.setTitle(getResource("SkeletonSchedulePanel"));
                xac.enableCourtLogActions(xac.getApplicationCaseModel().isInEditMode(FunctionList.ECourtLog));
            }
        }
    }

    // utility methods
    private static final String getResource(String key) {
        return XHIBITConstant.getResource(XhibitBundles.XhibitClientDefaultResources, key);
    }
}