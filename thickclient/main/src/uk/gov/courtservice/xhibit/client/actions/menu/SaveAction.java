package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2 Save
 * </p>
 * <p>
 * Description: Save the data in the current active panel
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

public class SaveAction extends SynchXAction {

    public SaveAction() {
        populateFromBundle("Save");
        setAccelaratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
    }

    SaveFunction panel = null;

    public void preSynchActionPerformed(ActionEvent e) throws Exception {
        if (((XhibitApplicationController) getController()).getBodyPanel() instanceof SaveFunction) {
            panel = (SaveFunction) ((XhibitApplicationController) getController()).getBodyPanel();
            panel.savePreSynchAction();
        }
    }

    public void synchActionPerformed(ActionEvent e) throws Exception {
        if (panel != null) {
            panel.saveSynchAction();
        }
    }

    public void postSynchActionPerformed(ActionEvent e) throws Exception {
        if (panel != null) {
            panel.savePostSynchAction();
        }
    }
}