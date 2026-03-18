package uk.gov.courtservice.xhibit.client.maintaincharges;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Popup menu for the BreachPanel
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

public class BreachPopupMenu extends JPopupMenu {
    public BreachPopupMenu(XhibitApplicationController xac) {
        JMenuItem menuItem = new JMenuItem(XhibitActions.getAction(xac, XhibitActions.EditBreachProps));
        add(menuItem);
        addSeparator();
        menuItem = new JMenuItem(XhibitActions.getAction(xac, XhibitActions.RemoveBreach));
        add(menuItem);
    }
}