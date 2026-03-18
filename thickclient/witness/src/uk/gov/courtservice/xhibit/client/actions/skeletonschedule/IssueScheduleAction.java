package uk.gov.courtservice.xhibit.client.actions.skeletonschedule;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.client.exceptions.SkeletonAlreadyIssuedException;
import uk.gov.courtservice.xhibit.client.skeletonschedule.SkeletonSchedulePanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */

public class IssueScheduleAction extends XAction {
    private static final Logger log = CSServices.getLogger(IssueScheduleAction.class);

    private static final String ISSUE_MESSAGE = "skeleton.schedule.issue.Message";

    private static final String ISSUE_TITLE = "skeleton.schedule.issue.Title";

    public IssueScheduleAction() {
        log.debug("IssueScheduleAction()");

        populateFromBundle("IssueSchedule");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        log.debug("xActionPerformed(" + e + ")");

        /**
         * PR 56354: Neil Entwistle - display warning message when issuing a
         * skeleton schedule
         */
        if (JOptionPane.showConfirmDialog((SkeletonSchedulePanel) getModel(), XHIBITConstant.getResource(
                XhibitBundles.SkeletonSchedule, ISSUE_MESSAGE), XHIBITConstant.getResource(
                XhibitBundles.SkeletonSchedule, ISSUE_TITLE), JOptionPane.WARNING_MESSAGE) == 0) {

            SkeletonSchedulePanel skeletonSchedulePanel = ((SkeletonSchedulePanel) getModel());
            SkeletonSchedule schedule = skeletonSchedulePanel.getSkeletonSchedule();
            if (schedule.isIssued()) {
                throw new SkeletonAlreadyIssuedException(schedule);
            }
            schedule.issue();

            // set up any required skeleton days that have not been created
            float trialTimeEst = skeletonSchedulePanel.getTrialTimeEstimate();
            log.debug("Issue Schedule Action called :: set up default trial session");
            log.debug("CASE ID :: " + schedule.getCaseId());
            log.debug("TRIAL TIME EST :: " + trialTimeEst);
            log.debug("ID :: " + schedule.getId());
            SkeletonScheduleFactory.getInstance().getDelegate().createDefaultSkeletonDays(schedule.getCaseId(),
                    trialTimeEst, schedule.getId());

            skeletonSchedulePanel.getIssueScheduleButton().setEnabled(false);
            skeletonSchedulePanel.getAddWitnessButton().setEnabled(false);
            skeletonSchedulePanel.getEditWitnessButton().setEnabled(false);
            skeletonSchedulePanel.getDeleteScheduleButton().setEnabled(false);
            skeletonSchedulePanel.getTrialTimeButton().setEnabled(false);
            skeletonSchedulePanel.getNotesButton().setEnabled(false);

        }
    }
}