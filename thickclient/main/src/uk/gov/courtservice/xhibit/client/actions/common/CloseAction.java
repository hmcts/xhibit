package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
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
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class CloseAction extends XAction {

    public CloseAction() {
        populateFromBundle("Close");
        setMnemonicKeyFromBundle("Close");
    }

    public void xActionPerformed(ActionEvent e) {
        try {
            XhibitApplicationController xac = (XhibitApplicationController) getController();

            // remove from xac Group
            XhibitSingleton.removeXacFromGroup(xac);
            xac.getParentController().closeXhibitApplication(xac);
        } catch (CSRecoverableException ex) {
            XHIBITConstant.handleError(ex);
        }
    }

}