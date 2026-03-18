package uk.gov.courtservice.xhibit.client.actions.skeletonschedule;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.client.skeletonschedule.SkeletonSchedulePanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
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

public class DeleteScheduleAction extends XAction {
    private static final Logger log = CSServices.getLogger(DeleteScheduleAction.class);

    public DeleteScheduleAction() {
        log.debug("DeleteScheduleAction()");
        populateFromBundle("DeleteSchedule");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        log.debug("xActionPerformed(" + e + ")");
        XhibitApplicationController xac = (XhibitApplicationController) getController();

        boolean answer = XMessageBox.alert(xac, XHIBITConstant.getResource(XhibitBundles.XhibitClientDefaultResources,
                "DeleteConfirmTitle"), true, XMessageBox.ICONQUESTION, XHIBITConstant.getResource(
                XhibitBundles.SkeletonSchedule, "skeletonschedule.deleteschedule"), XMessageBox.YESNO,
                XMessageBox.DEFAULTNO);

        if (answer) {
            SkeletonSchedulePanel skeletonSchedulePanel = ((SkeletonSchedulePanel) getModel());
            SkeletonSchedule schedule = skeletonSchedulePanel.getSkeletonSchedule();
            schedule.remove();
            skeletonSchedulePanel.setVisible(false);
            skeletonSchedulePanel.removeAll();
            // Create an empty skeleton schedule if the previous one has
            // been deleted
            xac.open(new SkeletonSchedulePanel(xac));
        }
    }
}