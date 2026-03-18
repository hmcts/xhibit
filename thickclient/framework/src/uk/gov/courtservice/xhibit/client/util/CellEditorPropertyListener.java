package uk.gov.courtservice.xhibit.client.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.CellEditor;

/**
 * <p>
 * Title: Property change listener for Cell Editors
 * </p>
 * <p>
 * Description: Used to stop Cell Editing. E.g. a table may have an editor that
 * is a JPanel that contains some text fields and a button. When a field is
 * updated, you require the table editing process to stop. In the editor create
 * an instance of this PropertyChangeListener, passing the contructor a
 * reference to the editor. Then add the listener to the component you want to
 * check for an update.
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

public class CellEditorPropertyListener implements PropertyChangeListener, Serializable {
    private CellEditor editor = null;

    public CellEditorPropertyListener(CellEditor editor) {
        this.editor = editor;
    }

    public void propertyChange(PropertyChangeEvent pce) {
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("Property
        // changed");
        editor.stopCellEditing();
    }
}