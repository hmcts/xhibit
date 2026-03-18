package uk.gov.courtservice.xhibit.client.actions.skeletonschedule;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.client.skeletonschedule.SkeletonSchedulePanel;
import uk.gov.courtservice.xhibit.client.skeletonschedule.WitnessDialog;
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

public class AddWitnessAction extends XAction {
    private static final Logger log = CSServices.getLogger(AddWitnessAction.class);

    public AddWitnessAction() {
        log.debug("AddWitnessAction()");

        populateFromBundle("AddWitness");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        log.debug("xActionPerformed(" + e + ")");

        XhibitApplicationController xac = (XhibitApplicationController) getController();
        WitnessDialog witnessDialog = new WitnessDialog(xac, WitnessDialog.ADD);
        SkeletonSchedulePanel skeletonSchedulePanel = ((SkeletonSchedulePanel) getModel());

        try {
            witnessDialog.init(null);
            skeletonSchedulePanel.refreshWitnessPanel();
        } catch (WitnessNotFoundException ex) {
            // todo: nice dialog or something here would be good.
            log.error(ex);

        }
    }
}