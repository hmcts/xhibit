package uk.gov.courtservice.xhibit.client.actions.todaysschedule;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.schedule.OpenCaseDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @author Rakesh Lakhani
 * @editor Frederik Vandendriessche
 * 
 * @version 1.1 FV: Changed retrieval of OpenShortDesc to use the cached
 *          XhibitActionResources in XHIBITConstant
 * @version 1.0
 * 
 * 
 */

public class OpenCaseAction extends SynchXAction {
    private XhibitApplicationController xac;

    private OpenCaseDialog ocd;

    public OpenCaseAction() {
        populateFromBundle("Open");
    }

    // public void xActionPerformed(ActionEvent e) throws
    // java.lang.Exception {
    //
    // //Do this regardless of linked or not
    // OpenCaseDialog ocd = new
    // OpenCaseDialog((XhibitApplicationController)getController(),
    // XHIBITConstant.getResource(XhibitBundles.XhibitActionResources,
    // "OpenShortDesc"));
    // ocd.setVisible(true);
    // if (ocd.getShv()!=null) {
    // XhibitApplicationController xac =
    // (XhibitApplicationController)getController();
    // xac.openCase(ocd.getShv(), !ocd.isReadOnly());
    // }
    // }

    public void preSynchActionPerformed(ActionEvent parm1) // throws
    // java.lang.Exception
    {
        xac = (XhibitApplicationController) getController();
    }

    public void synchActionPerformed(ActionEvent e) throws CSRecoverableException {
        if (xac != null) {
            ocd = new OpenCaseDialog((XhibitApplicationController) getController(), XHIBITConstant.getResource(
                    XhibitBundles.XhibitActionResources, "OpenShortDesc"));

        }
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws CSRecoverableException {
        if (xac != null && ocd != null) {
            ocd.setVisible(true);
            if (ocd.getShv() != null) {
                XhibitApplicationController xac = (XhibitApplicationController) getController();
                xac.openCase(ocd.getShv(), !ocd.isReadOnly());
            }
        }
    }
}