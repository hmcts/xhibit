package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Processes Mouse events on the BreachPanel
 * </p>
 * <p>
 * Description: Used to handle mouse events on the BreachPanel when the
 * BreachPanel is in ViewMode.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class BreachMouseAdapter extends MouseAdapter {
    private XhibitApplicationController xac = null;

    private JPopupMenu popup = null;

    private boolean inEditMode = false;

    public BreachMouseAdapter(XhibitApplicationController xac, JPopupMenu popup) {
        this.xac = xac;
        this.popup = popup;
        inEditMode = xac.getApplicationCaseModel().isInEditMode(FunctionList.ECharge);
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * Called when the mouse is clicked.
     * 
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        if (inEditMode) {
            if (e.getClickCount() == 2) {
                // XHIBITConstant.debug("BreachPanel: in mouseClicked (user
                // double clicked BreachPanel)");
                XhibitActions.getAction(xac, XhibitActions.EditBreachProps).actionPerformed(
                        new ActionEvent(xac, 0, "called from BreachMouseAdapter"));
            }
        }
    }

    /**
     * Decides whether or not to show the context sensitive menu.
     * 
     * @param e
     */
    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (getPopup() != null) {
                getPopup().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private JPopupMenu getPopup() {
        return popup;
    }
}