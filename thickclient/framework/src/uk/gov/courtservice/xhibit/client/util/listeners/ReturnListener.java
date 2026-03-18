package uk.gov.courtservice.xhibit.client.util.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Fire an action when return is released
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class ReturnListener implements KeyListener {
    XAction toFire;

    public ReturnListener(XAction actionToFire) {
        if (actionToFire == null)
            throw new UnsupportedOperationException("Must have an action");
        toFire = actionToFire;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            e.consume();
            ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), "Enter");
            try {
                if (toFire.isEnabled()) {
                    toFire.actionPerformed(ae);
                }
            } catch (Exception ex) {
                XHIBITConstant.handleError(ex, toFire, ae);
            }
        }
    }
}