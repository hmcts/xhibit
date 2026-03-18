package uk.gov.courtservice.xhibit.client.util.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;

import javax.swing.KeyStroke;

import uk.gov.courtservice.xhibit.client.actions.common.HelpAction;

/**
 * <p>
 * Title: Key Listener added to Dialogs and frames to listner for F1 key being
 * pressed. Pressing this key triggers the HelpAction actionperformed which
 * displays Help pages
 * </p>
 * <p>
 * Description: Key Listener added to Dialogs and frames to listner for F1 key
 * being pressed. Pressing this key triggers the HelpAction actionperformed
 * which displays Help pages
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 * 
 */

public class HelpKeyListener extends KeyAdapter {
    public HelpKeyListener() {
    }

    public void keyReleased(java.awt.event.KeyEvent e) {
        // need to fire Help Action only if F1 pressed
        if (e.getKeyCode() == KeyStroke.getKeyStroke("F1").getKeyCode()) {
            HelpAction.getInstance().actionPerformed(new ActionEvent(e, 0, e.toString()));
        }

    }
}