package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.NumericValidatingDocumentDecorator;

/**
 * <p>
 * Title: XNumericLimitedLengthCellEditor
 * </p>
 * <p>
 * Description: Restrict entered text to set length and numerics. Based on
 * XMaskedCellEditor
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author AW Daley
 * @version 1.0
 * 
 * use this editor by calling : table.getColumn("COL-NAME").setCellEditor(new
 * XNumericLimitedLengthCellEditor(REQUIRED_SIZE));
 */
public class XNumericLimitedLengthCellEditor extends DefaultCellEditor {
    private Object value;

    private JTable table;

    public XNumericLimitedLengthCellEditor(int size) {
        super(new JTextField());
        buildPanel(size);
    }

    /**
     * Builds the panel
     */
    private void buildPanel(int size) {
        ((JTextField) this.getEditor()).setDocument(new NumericValidatingDocumentDecorator(
                new LimitedTextValidatingDocumentDecorator(size)));
        // turn borders off
        ((JTextField) this.getEditor()).setBorder(BorderFactory.createEmptyBorder());
        ((JTextField) this.getEditor()).setHorizontalAlignment(JTextField.RIGHT);
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
