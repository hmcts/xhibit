package uk.gov.courtservice.xhibit.client.listeners.common;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.xhibit.client.actions.common.CopyAction;
import uk.gov.courtservice.xhibit.client.actions.common.CutAction;
import uk.gov.courtservice.xhibit.client.actions.common.PasteAction;

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
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class CutCopyListSelectionListener implements ListSelectionListener {

    public CutCopyListSelectionListener() {
    }

    public void valueChanged(ListSelectionEvent e) {
        // Ignore extra messages.
        if (e.getValueIsAdjusting())
            return;

        CopyAction.getInstance().checkState();
        CutAction.getInstance().checkState();
        PasteAction.getInstance().checkState();
    }
}