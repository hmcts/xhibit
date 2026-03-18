package uk.gov.courtservice.xhibit.client.linkedcases;

import java.awt.event.ActionEvent;
import java.util.Iterator;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.LinkSuggestionValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Helper to encapsulating necessary linked case functionality
 * </p>
 * <p>
 * Description: The helper provides methods which perform linking/unlinking,
 * delegate calls and identifying linked and available cases
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 * 
 */
public class LinkedCasesHelper {
    /**
     * boolean isOpeningLinkedCases
     */
    public static boolean isOpeningLinkedCases = false;

    /**
     * XhibitApplicationController xac
     */
    private XhibitApplicationController xac;

    // private ResourceBundle menuResources;

    /**
     * Constructor takes in xac as a parameter which is used to manipulate
     * windows <init>
     * 
     * @param xac
     *            parameter for <init>
     */
    public LinkedCasesHelper(XhibitApplicationController xac) {
        this.xac = xac;
        // menuResources = XHIBITConstant.getResourceBundle(XhibitBundles.Menu);

    }

    /**
     * getDelegate
     * 
     * @return the returned HearingScheduleControllerBusinessDelegate
     */
    public HearingScheduleControllerBeanBusinessDelegate getDelegate() {
        return XhibitDelegateHelper.getHearingDelegate();
    }

    /**
     * previouslyLinked - makes delegate call to determine if the case is
     * previoulsy linked
     * 
     * @param caseID
     *            parameter for previouslyLinked
     * @return the returned boolean
     */
    public boolean previouslyLinked(Integer caseID) {
        boolean previouslyLinked = false;
        try {
            previouslyLinked = getDelegate().previouslyLinked(caseID);
        } catch (HearingScheduleException ex) {
            XHIBITConstant.handleError(ex);
        }
        return previouslyLinked;
    }

    /**
     * suggestLinkCases - obtains cases which are available for linking
     * 
     * @param scheduleHearingID
     *            parameter for suggestLinkCases
     * @return the returned LinkSuggestionValue
     */
    public LinkSuggestionValue suggestLinkCases(Integer scheduleHearingID) {
        LinkSuggestionValue lsv = null;
        try {
            lsv = getDelegate().suggestLinkCases(scheduleHearingID);
        } catch (HearingScheduleException ex) {
            XHIBITConstant.handleError(ex);
        }
        return lsv;
    }

    /**
     * linkCases - links cases selected cases in LinkedCasesPanel to lead case
     * 
     * @param linkedCases
     *            parameter for linkCases
     * @param leadScheduleHearingID
     *            parameter for linkCases
     */
    public void linkCases(CaseSchedHearingValue[] linkedCases, Integer leadScheduleHearingID)
            throws HearingScheduleException {
        // Attempts to link the cases
        getDelegate().linkCases(linkedCases, leadScheduleHearingID,
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
    }

    /**
     * unLinkCase - Unlinks selected case
     * 
     * @param shbv
     *            parameter for unLinkCase
     * @throws UserCancelException -
     */
    public void unLinkCase(ScheduledHearingBasicValue shbv) throws UserCancelException {
        try {
            getDelegate().unLinkCase(shbv,
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        } catch (HearingScheduleException ex) {
            XHIBITConstant.handleError(ex);
            throw new UserCancelException();
        }
    }

    /**
     * getScheduleHearingValues - returns ScheduledHearingValue[] using an
     * argument of CaseSchedHearingValue []
     * 
     * @param cshv
     *            parameter for getScheduleHearingValues
     * @return the returned ScheduledHearingValue[]
     */
    public ScheduledHearingValue[] getScheduleHearingValues(CaseSchedHearingValue[] cshv) {
        ScheduledHearingValue[] shvs = null;
        Integer[] schedHearingIDs = null;
        if (cshv != null && cshv.length != 0) {
            schedHearingIDs = new Integer[cshv.length];

            // loop through cshv and create and integer of
            // SchedHearingIDsScheduledHearingValue[] shvs
            for (int i = 0; i < cshv.length; i++) {
                schedHearingIDs[i] = ((CaseSchedHearingValue) cshv[i]).getScheduledHearingId();
            }
            try {
                shvs = getDelegate().getScheduledHearings(schedHearingIDs);
            } catch (HearingScheduleException ex) {
                XHIBITConstant.handleError(ex);
            }
        }
        return shvs;
    }

    /**
     * openSHVWindows - opens a window for each case in the
     * ScheduledHearingValue[] argument passed in
     * 
     * @param shvs
     *            parameter for openSHVWindows
     * @throws CSRecoverableException -
     */
    public void openSHVWindows(ScheduledHearingValue[] shvs) throws CSRecoverableException {
        XhibitApplicationController linkedXac;
        ScheduledHearingValue shv = null;

        try {
            if (shvs != null && shvs.length != 0) {
                // Opening linked cases now
                LinkedCasesHelper.isOpeningLinkedCases = true;

                for (int i = 0; i < shvs.length; i++) {
                    shv = (ScheduledHearingValue) shvs[i];
                    if (shv != null) {
                        // check caseid with existing collection of open windows
                        if (!xacLinkIfExistInGroup(shv.getCaseId())) {
                            // Doesn't exist so open new window
                            linkedXac = ((XhibitInterface) xac.getParentController()).newXhibitApplication();

                            // pass in edit mode from current/lead case
                            linkedXac.openCase(shv, xac.getApplicationCaseModel().isInEditMode(
                                    FunctionList.EExportHearingRecord));
                            // linkedXac.getToolBarHelper().setToolBarStatus(
                            // XHIBITConstant.getResource(menuResources,"GeneralMenu"),XToolBarHelper.TOOLBAR_ON_PERSISTENT);
                            // linkedXac.getToolBarHelper().setToolBarStatus(
                            // XHIBITConstant.getResource(menuResources,"CourtLogMenu"),XToolBarHelper.TOOLBAR_ON_PERSISTENT);
                            // linkedXac.getToolBarHelper().setToolBarStatus(
                            // XHIBITConstant.getResource(menuResources,"TrialMenu"),XToolBarHelper.TOOLBAR_ON_PERSISTENT);
                            XHIBITConstant.debug("Opening Linked Case SHV: " + i);
                        }
                    }
                }
            }
        } catch (CSRecoverableException ex) {
            XHIBITConstant.handleError(ex);
        } finally {
            // Stopped opening linked cases
            LinkedCasesHelper.isOpeningLinkedCases = false;
        }
    }

    /**
     * xacLinkIfExistInGroup
     * 
     * @param caseId
     *            parameter for xacLinkIfExistInGroup
     * @return the returned boolean
     */
    public static boolean xacLinkIfExistInGroup(Integer caseId) {
        boolean xacExists = false;
        XhibitApplicationController xac;
        Iterator xacIter = XhibitSingleton.getXacGroup().iterator();
        while (xacIter.hasNext()) {
            xac = (XhibitApplicationController) xacIter.next();
            if (xac.getApplicationCaseModel() != null
                    && caseId.intValue() == xac.getApplicationCaseModel().getCaseId().intValue()
                    && !(xac.getBodyPanel() instanceof TodaysScheduleController)) {
                xacExists = true;
                // set linked to true (may already be linked)
                xac.getApplicationCaseModel().setIsLinked(true);
                break;
            }
        }

        return xacExists;
    }

    /**
     * processLinkedCases - checked if opened case has been previously linked.
     * If this is the case then launch linked case functionality to allow
     * linking.
     */
    public void processLinkedCases() {
        // Check if we are opening linked cases
        if (!LinkedCasesHelper.isOpeningLinkedCases) {
            // Is it linked?
            boolean linked = previouslyLinked(xac.getApplicationCaseModel().getCaseId());

            if (linked) {
                // Call action event to allow linking of other cases (source to
                // be xac
                // so mouse cursor works.)
                XhibitActions.getAction(xac, XhibitActions.LinkCases).actionPerformed(
                        new ActionEvent(xac, 0, "LinkCases"));
            }
        }
    }
}