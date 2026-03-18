package uk.gov.courtservice.xhibit.client.actions.skeletonschedule;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.skeletonschedule.TrialTimeEstimateDialog;
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

public class EditTrialTimeEstimateAction extends XAction {
    private static final Logger log = CSServices.getLogger(EditTrialTimeEstimateAction.class);

    public EditTrialTimeEstimateAction() {
        log.debug("EditTrialTimeEstimateAction()");

        populateFromBundle("EditTrialTimeEstimate");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        log.debug("xActionPerformed(" + e + ")");

        XhibitApplicationController xac = (XhibitApplicationController) getController();
        new TrialTimeEstimateDialog(xac);
    }
}