package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTimePanel;

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
 * @author Simon Gilmore
 * @version 1.0
 */
public class XTimeTableCellEditor extends XTimePanel implements TableCellEditor {
    private EventListenerList listenerList = new EventListenerList();

    private ChangeEvent event = new ChangeEvent(this);

    private static Calendar calendar = null;

    private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    protected XTimePanel panel = new XTimePanel(null, calendar, format);

    public XTimeTableCellEditor() {
        // Time must allow for seconds to order clourt og correctly. Neil
        // Entwistle 03/06/2003
        super(null, calendar, format);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // If not a Calendar don't try to cast the object
        if (value instanceof Calendar) {
            XHIBITConstant.debug("[TimeTableCellRenderer] value = " + value.getClass().getName());
            setTime((Calendar) value);
            XHIBITConstant.debug("[TimeTableCellRenderer] time = " + getText());
        } else {
            // We need to set the time to something otherwise the
            // TimeValidator
            // complains that the time is in the wrong format. The time is
            // thrown
            // away by the setValueAt method on the model, but this ensures
            // that
            // a valid time is displayed if the user clicks on an
            // unpopulated
            // time cell.
            setTime(Calendar.getInstance());
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        return this;
    }

    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        // tell caller it is ok to select this cell
        return true;
    }

    public void cancelCellEditing() {
        fireEditingCancelled();
    }

    public boolean stopCellEditing() {
        XHIBITConstant.debug("* * *   stopCellEditing   * * * text = " + getText());

        boolean isTimeValid = false;
        try {
            getDate();

            fireEditingStopped();
            isTimeValid = true;
        } catch (CSValidationException csve) {
            XHIBITConstant.handleError(csve);
            isTimeValid = false;
        }
        return isTimeValid;
    }

    public Object getCellEditorValue() {
        XHIBITConstant.debug("getCellEditorValue ");
        Calendar cal = null;
        try {
            cal = getDate();
        } catch (CSValidationException csve) {
            csve.printStackTrace();
        }
        return cal;
    }

    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    protected void fireEditingStopped() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            ((CellEditorListener) listeners[i + 1]).editingStopped(event);
    }

    protected void fireEditingCancelled() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            ((CellEditorListener) listeners[i + 1]).editingCanceled(event);
    }

}