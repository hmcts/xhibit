package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;

/**
 * <p>
 * Title: XMaskedCellEditor
 * </p>
 * <p>
 * Description: Text enetered into the cell must compliy with the specified mask
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Andy Daley
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 30-07-2003 AW Daley Initial Version
 */
public class XMaskedCellEditor extends DefaultCellEditor {
    private final static Logger log = CSServices.getLogger(XMaskedCellEditor.class);

    private Object value;

    private JTable table;

    public XMaskedCellEditor(JTextField textField, String mask) {
        super(textField);
        buildPanel(mask);
    }

    /**
     * Builds the panel
     */
    private void buildPanel(String mask) {
        // Creates the masked document using Regular Expressions and sets
        // the text field to use this document.
        Document maskFieldDoc = DocumentFactory.newDocument(new Capability[] { Capability.maskedText(mask) });
        ((JTextField) this.getEditor()).setDocument(maskFieldDoc);

        // Turn borders off
        ((JTextField) this.getEditor()).setBorder(BorderFactory.createEmptyBorder());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value = value;
        this.table = table;

        // Perform default cell editing function
        Component editor = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        if (value instanceof String)
            // Sets the value of the cell
            ((JTextField) editor).setText((String) value);
        else
            log.error("Value passed is not a String object");

        return editor;

    }

    protected JComponent getEditor() {
        return editorComponent;
    }

}