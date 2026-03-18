package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.xhibitapplication.Xhibit;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Exit XHIBIT
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class ExitAction extends XAction {

    private static ExitAction ca = null;

    private ExitAction() {
        populateFromBundle("Exit");
        setMnemonicKeyFromBundle("Exit");
    }

    public static ExitAction getInstance() {
        if (ca == null)
            ca = new ExitAction();
        return ca;
    }

    public void xActionPerformed(ActionEvent e) {
        if (getController() != null) {
            try {
                Frame callingFrame = null;
                if (e.getSource() instanceof Component) {
                    callingFrame = XSwingUtilities.getUltimateFrameAncestor((Component) e.getSource());
                }
                ((Xhibit) getController()).exitXhibitApplication(callingFrame);
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
        }
    }
}