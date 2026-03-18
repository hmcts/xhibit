package uk.gov.courtservice.xhibit.client.util.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class DoubleClickListener extends MouseAdapter {
    XAction toFire;

    public DoubleClickListener(XAction actionToFire) {
        if (actionToFire == null)
            throw new UnsupportedOperationException("Must have an action");
        toFire = actionToFire;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
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