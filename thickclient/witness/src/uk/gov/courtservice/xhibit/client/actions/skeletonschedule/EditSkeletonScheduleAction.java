package uk.gov.courtservice.xhibit.client.actions.skeletonschedule;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.client.skeletonschedule.SkeletonSchedulePanel;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.ControllerUtil;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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

public class EditSkeletonScheduleAction extends XAction {
    private static final Logger log = CSServices.getLogger(EditSkeletonScheduleAction.class);

    public EditSkeletonScheduleAction() {
        log.debug("EditSkeletonScheduleAction()");

        populateFromBundle("EditSkeletonSchedule");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        log.debug("EditSkeletonScheduleAction(" + e + ")");
        XhibitApplicationController xac = (XhibitApplicationController) getController();

        // check that a time trial estimate has been entered
        CaseDetail caseDetail = ControllerUtil.getCaseDetail(xac);
        caseDetail.getEstimatedCaseDuration();

        xac.open(new SkeletonSchedulePanel(xac));
    }
}