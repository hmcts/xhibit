package uk.gov.courtservice.xhibit.client.util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class XDocumentListener implements DocumentListener {
    private boolean isChanged = false;

    public boolean xIsChanged() {
        return this.isChanged;
    }

    public void setChanged(boolean flag) {
        this.isChanged = flag;
    }

    public void changedUpdate(DocumentEvent e) {
        // Gives notification that an attribute or set of attributes changed.
        this.isChanged = true;
    }

    public void insertUpdate(DocumentEvent e) {
        // Gives notification that there was an insert into the document.
        this.isChanged = true;
    }

    public void removeUpdate(DocumentEvent e) {
        // Gives notification that a portion of the document has been removed.
        this.isChanged = true;
    }
}