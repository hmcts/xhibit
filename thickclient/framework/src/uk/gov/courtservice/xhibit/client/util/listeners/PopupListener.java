package uk.gov.courtservice.xhibit.client.util.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Show a popup menu when triggered
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

public class PopupListener extends MouseAdapter {
    private JPopupMenu thisPopup = null;

    public PopupListener(JPopupMenu pMenu) {
        thisPopup = pMenu;
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    protected void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (thisPopup != null) {
                thisPopup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}