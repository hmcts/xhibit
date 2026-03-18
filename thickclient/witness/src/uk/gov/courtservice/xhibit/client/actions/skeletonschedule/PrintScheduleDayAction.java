package uk.gov.courtservice.xhibit.client.actions.skeletonschedule;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.skeletonschedule.print.PrintScheduleByDay;
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
 * @author Neil Entwistle
 * @version 1.0
 */

public class PrintScheduleDayAction extends XAction {
    private static final Logger log = CSServices.getLogger(PrintScheduleDayAction.class);

    public PrintScheduleDayAction() {
        log.debug("PrintScheduleDayAction()");
        populateFromBundle("PrintScheduleDay");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        log.debug("xActionPerformed(" + e + ")");

        XhibitApplicationController xac = (XhibitApplicationController) getController();
        PrintScheduleByDay printerSchedDay = new PrintScheduleByDay(xac);
        // Add 5 to the duration so that we round up to the week
        printerSchedDay.printSchedule();

        // XhibitApplicationController xac =
        // (XhibitApplicationController)getController();
        // SkeletonSchedulePanel skeletonSchedulePanel =
        // ((SkeletonSchedulePanel)getModel());
        // SkeletonSchedule schedule=
        // skeletonSchedulePanel.getSkeletonSchedule();
        // schedule.remove();
        // skeletonSchedulePanel.setVisible(false);
        // skeletonSchedulePanel.removeAll();
    }
}