package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SelectAllFunction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2 Save
 * </p>
 * <p>
 * Description: Select all, e.g. all the rows in a table in the current active
 * panel
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

public class SelectAllAction extends XAction {

    public SelectAllAction() {
        populateFromBundle("SelectAll");
        setAccelaratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_A, java.awt.Event.CTRL_MASK));
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        if (((XhibitApplicationController) getController()).getBodyPanel() instanceof SelectAllFunction) {
            ((SelectAllFunction) ((XhibitApplicationController) getController()).getBodyPanel()).selectAll();
        }
    }
}