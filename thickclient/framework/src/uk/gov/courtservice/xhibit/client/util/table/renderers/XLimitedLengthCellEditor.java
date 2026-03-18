package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;

/**
 * <p>
 * Title: XLimitedLengthCellEditor
 * </p>
 * <p>
 * Description: Restrict entered text to set length. Based on XMaskedCellEditor
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jon Powell
 * @version 1.0
 * 
 * use this editor by calling : table.getColumn("COL-NAME").setCellEditor(new
 * XLimitedLengthCellEditor(REQUIRED_SIZE));
 */
public class XLimitedLengthCellEditor extends DefaultCellEditor {
    private Object value;

    private JTable table;

    public XLimitedLengthCellEditor(int size) {
        super(new JTextField());
        buildPanel(size);
    }

    /**
     * Builds the panel
     */
    private void buildPanel(int size) {
        ((JTextField) this.getEditor()).setDocument(new LimitedTextValidatingDocumentDecorator(size));
        // turn borders off
        ((JTextField) this.getEditor()).setBorder(BorderFactory.createEmptyBorder());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value = value;
        this.table = table;

        // Perform default cell editing function
        Component editor = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        return editor;
    }

    protected JComponent getEditor() {
        return editorComponent;
    }

}