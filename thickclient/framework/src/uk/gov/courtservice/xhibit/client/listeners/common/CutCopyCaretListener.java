package uk.gov.courtservice.xhibit.client.listeners.common;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import uk.gov.courtservice.xhibit.client.actions.common.CopyAction;
import uk.gov.courtservice.xhibit.client.actions.common.CutAction;

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

public class CutCopyCaretListener implements CaretListener {

    public CutCopyCaretListener() {
    }

    public void caretUpdate(CaretEvent e) {
        // if ((e.getDot() - e.getMark()) != 0) {
        CopyAction.getInstance().checkState();
        CutAction.getInstance().checkState();
        // }
    }
}