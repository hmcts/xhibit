package uk.gov.courtservice.xhibit.client.listeners.menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title:
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
 * @author unascribed
 * @version 1.0
 */

public class MenuItemMouseListener implements MouseListener, MenuDragMouseListener {
    private XhibitApplicationController xac;

    private String oldText = "";

    private String newText = "";

    public MenuItemMouseListener(XhibitApplicationController xac) {
        this.xac = xac;
    }

    // Mouse Listener events
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        resetStatus();
    }

    public void mouseEntered(MouseEvent e) {
        if (JMenuItem.class.isInstance(e.getSource())) {
            JMenuItem jmi = (JMenuItem) e.getSource();
            oldText = xac.getStatusLabel();
            newText = ((XAction) jmi.getAction()).getLongDescription();
            xac.setStatusLabel(newText);
        }
    }

    public void mouseExited(MouseEvent e) {
        resetStatus();
    }

    // MenuDragMouseListener Events
    public void menuDragMouseEntered(MenuDragMouseEvent e) {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("menu focus!");
        if (JMenuItem.class.isInstance(e.getSource())) {
            JMenuItem jmi = (JMenuItem) e.getSource();
            xac.setStatusLabel(((XAction) jmi.getAction()).getLongDescription());
        }
    }

    public void menuDragMouseExited(MenuDragMouseEvent e) {
        resetStatus();
    }

    public void menuDragMouseDragged(MenuDragMouseEvent e) {
    }

    public void menuDragMouseReleased(MenuDragMouseEvent e) {
        resetStatus();
    }

    private void resetStatus() {
        // if the text hasn't been changed by another process
        // reset it back to the old label
        if (xac.getStatusLabel().equals(newText)) {
            xac.setStatusLabel(oldText);
        }
    }
}