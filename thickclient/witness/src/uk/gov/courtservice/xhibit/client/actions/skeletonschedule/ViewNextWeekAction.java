package uk.gov.courtservice.xhibit.client.actions.skeletonschedule;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.skeletonschedule.SkeletonSchedulePanel;
import uk.gov.courtservice.xhibit.client.util.XAction;

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

public class ViewNextWeekAction extends XAction {
    private static final Logger log = CSServices.getLogger(ViewNextWeekAction.class);

    public ViewNextWeekAction() {
        log.debug("ViewNextWeekAction()");

        populateFromBundle("ViewNextWeek");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        log.debug("xActionPerformed(" + e + ")");

        SkeletonSchedulePanel skeletonSchedulePanel = ((SkeletonSchedulePanel) getModel());
        int currentWeek = skeletonSchedulePanel.getCurrentWeek();
        skeletonSchedulePanel.setCurrentWeek(skeletonSchedulePanel.getNextPopulatedWeek(currentWeek));
    }
}