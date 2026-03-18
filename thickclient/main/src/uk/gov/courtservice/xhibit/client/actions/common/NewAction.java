package uk.gov.courtservice.xhibit.client.actions.common;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.xhibitapplication.Xhibit;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Used to copy text onto
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

public class NewAction extends XAction {

    private static NewAction ca = null;

    private NewAction() {
        populateFromBundle("New");
        setMnemonicKeyFromBundle("New");
        setIcon(XHIBITConstant.imageRoot + "new.gif");
        setAccelaratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
    }

    public static NewAction getInstance() {
        if (ca == null)
            ca = new NewAction();
        return ca;
    }

    public void xActionPerformed(ActionEvent e) {
        if (getController() != null) {
            XhibitApplicationController xac = ((Xhibit) getController()).newXhibitApplication();

            java.awt.Frame f = XSwingUtilities.getUltimateFrameAncestor((Component) e.getSource());
            positionNewXAC(xac, f.getX(), f.getY());
        }
    }

    private void positionNewXAC(XhibitApplicationController xac, int origX, int origY) {
        // The amount to shift the x & y by.
        int shift = 25;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = xac.getSize();
        if (origY + frameSize.getHeight() + shift < screenSize.getHeight()
                && origX + frameSize.getWidth() + shift < screenSize.getWidth()) {
            xac.setLocation(origX + shift, origY + shift);
        } else {
            xac.setLocation(origX, origY);
        }

    }
}