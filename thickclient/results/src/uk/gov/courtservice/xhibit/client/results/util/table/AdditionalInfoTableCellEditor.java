package uk.gov.courtservice.xhibit.client.results.util.table;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.client.util.CellEditorPropertyListener;
import uk.gov.courtservice.xhibit.client.util.table.multiline.MultiLineHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @author tz0d5m
 * @version $Revision: 1.7 $
 */
public class AdditionalInfoTableCellEditor extends AdditionalInfoTableCellRenderer implements TableCellEditor {
    // log directly from the class
    private static final Logger log = Logger.getLogger(AdditionalInfoTableCellEditor.class);

    // flag to indicate whether the offence text field is editable for this
    // screen
    private final boolean offenceTextFieldEditable;

    private final EventListenerList listenerList = new EventListenerList();

    private final ChangeEvent event = new ChangeEvent(this);

    private String originalText;

    public AdditionalInfoTableCellEditor(XhibitApplicationController xac, MultiLineHelper multiLineHelper) {
        this(xac, multiLineHelper, false);
    }

    public AdditionalInfoTableCellEditor(XhibitApplicationController xac, MultiLineHelper multiLineHelper,
            boolean offenceTextFieldEditable) {
        super(xac, multiLineHelper, offenceTextFieldEditable);

        this.offenceTextFieldEditable = offenceTextFieldEditable;

        CellEditorPropertyListener cepl = new CellEditorPropertyListener(this);
        getPanelTableCell().addPropertyChangeListener(AdditionalInfoTableCell.OFFENCE_SELECTED, cepl);
        
        // only need to display the scroll bars when using the editor...
        getPanelTableCell().displayScrollBars();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        AdditionalInfoTableCellComponent tclc = (AdditionalInfoTableCellComponent) value;
        this.originalText = tclc.getText();

        return getTableCellRendererComponent(table, value, isSelected, true, row, column);
    }

    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        // tell caller it is ok to select this cell
        return true;
    }

    public void cancelCellEditing() {
        log.debug("cancelCellEditing - BEGIN");
        fireEditingCancelled();
    }

    public boolean stopCellEditing() {
        log.debug("stopCellEditing - BEGIN");

        if (originalText.equals(getPanelTableCell().getText())) {
            cancelCellEditing();
        } else {
            fireEditingStopped();
        }
        

        // tell caller is is ok to use color value
        return true;
    }

    /**
     * Returns the value contained in the editor.
     * 
     * @return The "other text" if the panel type is SHOW_OTHER, the "offence
     *         text" if the panel type is SHOW_OFFENCE and the offence text
     *         field is editable, or the RefOffenceBasicValue selected during
     *         the search if none of the others.
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        log.debug("getCellEditorValue");
        if (getPanelTableCell().getType() == AdditionalInfoTableCell.SHOW_OTHER
                || (getPanelTableCell().getType() == AdditionalInfoTableCell.SHOW_OFFENCE && offenceTextFieldEditable)) {
            return getPanelTableCell().getText();
        }

        return getPanelTableCell().getRefOffenceBasicValue();
    }
    
    public RefOffenceBasicValue getRefOffence()
    {
    	return getPanelTableCell().getRefOffenceBasicValue();
    }

    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    protected void fireEditingStopped() {
        log.debug("fireEditingStopped - BEGIN");
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            log.debug("fireEditingStopped " + listeners[i + 1].getClass().getName());
            ((CellEditorListener) listeners[i + 1]).editingStopped(event);
        }
    }

    protected void fireEditingCancelled() {
        log.debug("fireEditingCancelled - BEGIN");
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((CellEditorListener) listeners[i + 1]).editingCanceled(event);
        }
    }
}