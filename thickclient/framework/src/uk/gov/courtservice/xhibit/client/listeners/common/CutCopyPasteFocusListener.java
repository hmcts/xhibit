package uk.gov.courtservice.xhibit.client.listeners.common;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import uk.gov.courtservice.xhibit.client.actions.common.CopyAction;
import uk.gov.courtservice.xhibit.client.actions.common.CutAction;
import uk.gov.courtservice.xhibit.client.actions.common.PasteAction;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
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

public class CutCopyPasteFocusListener implements FocusListener {

    public CutCopyPasteFocusListener() {
    }

    public void focusGained(FocusEvent e) {
        CopyAction.getInstance().setModel(e.getSource());
        CutAction.getInstance().setModel(e.getSource());
        PasteAction.getInstance().setModel(e.getSource());
        CopyAction.getInstance().checkState();
        CutAction.getInstance().checkState();
        PasteAction.getInstance().checkState();
    }

    public void focusLost(FocusEvent e) {
        // RL: Do not assign the model in the focus lost event because the
        // cut/copy/paste buttons will never be enabled!

        // CopyAction.getInstance().setModel(null);
        // CopyAction.getInstance().checkState();
        // PasteAction.getInstance().setModel(null);
        // PasteAction.getInstance().checkState();
    }
}