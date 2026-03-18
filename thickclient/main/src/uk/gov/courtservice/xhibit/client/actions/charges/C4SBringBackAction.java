package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.SimpleEventDialog;
import uk.gov.courtservice.xhibit.client.courtlog.SimpleEventModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
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
 * @author Stephen Tully
 * @version 1.0
 */
public class C4SBringBackAction extends XAction {
    private static final String eventCode = "20930";

    public C4SBringBackAction() {
        populateFromBundle("C4SBringBack");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;

        xac = (XhibitApplicationController) getController();

        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

        SimpleEventModel model = new SimpleEventModel();

        String panelText = XHIBITConstant.getResource(resources, "C4SBringBack.panelText");
        model.setPanelText(panelText);
        model.setEventType(eventCode);
        model.setXac(xac);

        SimpleEventDialog sed = new SimpleEventDialog((java.awt.Frame) getController(), model);
        sed.setVisible(true);

        if (sed.isCancelClicked()) {
            throw new UserCancelException();
        }

        if (sed.isOkClicked()) {
            // Refresh Charges tab
            ChargesController cc = (ChargesController) xac.getBodyPanel();
            cc.loadCharges();
        }
    }
}
