package uk.gov.courtservice.xhibit.client.actions.todaysschedule;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.client.CSUserSession;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.security.GroupNames;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.schedule.movecase.MoveCaseDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
/*
 * 
 * Ref Date Author Description
 * 
 * PRE00121 07-10-2003 AW Daley Modification to check if the user is a List
 * Officer. If not a List Officer then the user is dirceted to consult with the
 * List Officer before continuing.
 * 
 */
public class MoveCaseAction extends XAction {

    private final static String MESSAGE_TITLE_RESOURCE_KEY = "messageTitleListOfficer";

    private final static String MESSAGE_QUESTION_RESOURCE_KEY = "messageQuestionListOfficer";

    public MoveCaseAction() {
        populateFromBundle("MoveCase");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        boolean yes;

        XhibitApplicationController xac;
        xac = (XhibitApplicationController) getController();

        // Get User session so that the security can be queried.
        XhibitSingleton xs = XhibitSingleton.getInstance();
        CSUserSession userSession = xs.getUserSession();

        // If the user is not a List Officer then display a message informing
        // the user to consult with the List Officer before continuing with
        // the operation
        if (!userSession.isUserInGroup(GroupNames.LIST_OFFICER)) {
            // Load in resource bundle that contains the message to be
            // displayed
            ResourceBundle actionResources = XHIBITConstant.getResourceBundle(XhibitBundles.TodaysSchedule);

            yes = XMessageBox.alert((java.awt.Frame) getController(), XHIBITConstant.getResource(actionResources,
                    MESSAGE_TITLE_RESOURCE_KEY), true, XMessageBox.ICONQUESTION, XHIBITConstant.getResource(
                    actionResources, MESSAGE_QUESTION_RESOURCE_KEY), XMessageBox.YESNO, XMessageBox.DEFAULTNO);

            // If the user has not consulted with the List Officer then end
            // the
            // action.
            if (!yes)
                return;

        }

        if (getModel() != null) {
            ScheduledHearingValue shv = ((ApplicationCaseModel) getModel()).getScheduledHearingValue();

            MoveCaseDialog moveCaseDialog = new MoveCaseDialog(xac, shv);
            moveCaseDialog.setSize(new Dimension(650, 600));
            moveCaseDialog.setVisible(true);

            // Reload data if case has been moved
            if (!moveCaseDialog.isCancelClicked()) {
                ((TodaysScheduleController) xac.getBodyPanel()).reloadView();
            }
        }
    }
}