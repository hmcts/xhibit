package uk.gov.courtservice.xhibit.client.util.table.multiline;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: The Default Editor used for the MultiLineTable. Represents a
 * TextArea for editing, and becomes scrollable as the content grows bigger than
 * the <b>actual</b> editor's size.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

public class MultiLineCellEditor implements TableCellEditor, FocusListener, MouseListener {

    /** The text area used as the editor. */
    private JTextArea textArea = null;

    /** The scrollpane for the editor. */
    private JScrollPane scroller = null;

    /** A reference to the table that is using this editor. */
    private JTable table = null;

    /** The point on screen referenced where the mouse is clicked. */
    private Point mousePoint = null;

    /**
     * The editable mode of this editor. <b>false</b> means its not editable.
     */
    private boolean cellEditable;

    /** The list that is used to contain the CellEditorListener listeners. */
    protected EventListenerList listenerList = null;

    /**
     * This is a reference to the event generated from the CellEditorListener.
     */
    protected ChangeEvent changeEvent = null;

    /**
     * This is used to hold editable states during the lifecycle of the
     * setFocusEdit() method. If this cell editor is in editable mode, i.e
     * <code>cellEditable</code> is set to true, then <code>editable</code>references
     * <b>true</b> when the cell becomes editable via a double-click action. If
     * the action is just a single click, then <code>editable</code> will
     * reference <b>false</b> and no edit will be able to be made. Edits can
     * only happen with double-click actions!
     * 
     */
    protected boolean editable;

    /**
     * Will initialise the editor with the given editable mode and associated
     * table.
     * 
     * @param table
     *            the table that holds a reference to this editor.
     * @param cellEditable
     *            determines if table using this editor is editable or not.
     */
    public MultiLineCellEditor(JTable table, boolean cellEditable) {
        super();

        // Initialise the text area.
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(XHIBITConstant.getCurrentFont());

        // The scroller is the actual editor component - the text area is its
        // view!
        scroller = new JScrollPane(textArea);

        // Default this to false.
        this.editable = false;

        // Initialise listeners and the table.
        this.cellEditable = cellEditable;
        listenerList = new EventListenerList();
        changeEvent = new ChangeEvent(this);
        this.table = table;
        this.table.addFocusListener(this);
        this.table.addMouseListener(this);
    }

    /**
     * This method shouldn't be invoked and is called by the JTable for its
     * editing.
     * 
     * Note that if <code>value</code> is null, then the method internally
     * converts this to an empty string.
     * 
     * @param table
     *            the table that holds a reference to this editor.
     * @param value
     *            the value that this editor will contain.
     * @param isSelected
     *            determines whether the editor is selected or not.
     * @param row
     *            the row that this editor is in.
     * @param column
     *            the column that this editor is in.
     * 
     * @return the editor - a scrollable text area.
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        textArea.setText(value == null ? "" : value.toString());
        textArea.requestFocus();
        return scroller;
    }

    /**
     * Implementation of CellEditorListener interface.
     * 
     * Add a listener to the list that's notified when the editor starts, stops,
     * or cancels editing.
     * 
     * @param l
     *            the cell editor listener to be added.
     */
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
     * Implementation of TableCellEditor interface.
     * 
     * Tell the editor to cancel editing and not accept any partially edited
     * value.
     */
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    /**
     * Implementation of TableCellEditor interface.
     * 
     * @returns the value contained in the editor.
     */
    public Object getCellEditorValue() {
        return this.textArea.getText();
    }

    /**
     * Implementation of TableCellEditor interface.
     * 
     * Asks the editor if it can start editing using anEvent.
     * 
     * @param anEvent
     *            the event used for editing.
     */
    public boolean isCellEditable(EventObject anEvent) {
        return this.cellEditable ? this.editable : this.cellEditable;
    }

    /**
     * Implementation of TableCellEditor interface.
     * 
     * Remove a listener from the list that's notified.
     * 
     * @param l
     *            the celle editor listener to be removed.
     */
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /**
     * Implementation of TableCellEditor interface.
     * 
     * @param anEvent
     *            the event generated for selecting a cell.
     * 
     * @return the value of shouldSelectCell() indicating whether the editing
     *         cell should be selected or not.
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    /**
     * Implementation of TableCellEditor interface.
     * 
     * Tells the editor to stop editing and accept any partially edited value as
     * the value of the editor.
     */
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    /**
     * Called by stopCellEditing(). Stopped editing on all the listeners in the
     * EvenListenerList.
     */
    protected void fireEditingStopped() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingStopped(changeEvent);
            }
        }
    }

    /**
     * Called by cancelCellEditing(). Traverses the EventListenerList and
     * cancels editing on each ChangeEvent.
     */
    protected void fireEditingCanceled() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingCanceled(changeEvent);
            }
        }
    }

    /**
     * Implementations of FocustListener.
     */
    public void focusGained(FocusEvent e) {
    }

    /**
     * Makes the cell that is to be in focus not editable. setFocusEdit() method
     * will override <code>editable</code> for a double-click and make it
     * editable.
     */
    public void focusLost(FocusEvent e) {
        this.editable = false;
    }

    /**
     * Implementations of MouseListener interface. Will edit cells on
     * double-clicks, or just set the focus otherwise.
     */
    public void mouseClicked(MouseEvent e) {
        setFocusEdit(e);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        setFocusEdit(e);
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    /**
     * This method is responsible for editing/setting the focus of the cell. If
     * this editor is in editable mode, and there is a double-click event, then
     * the cell becomes editable with focus set on the editor. Otherwise, the
     * focus is set on the cell.
     * 
     * @param e
     *            the mouse event that is intercepted and examined for
     *            double-click event.
     */
    private void setFocusEdit(MouseEvent e) {
        // The point on the screen where the mouse event was generated.
        mousePoint = new Point(e.getX(), e.getY());
        if (mousePoint != null) {
            // Get the row and column that the mouse is on top of.
            int row = this.table.rowAtPoint(mousePoint);
            int col = this.table.columnAtPoint(mousePoint);

            // Confirm that the editor the mouse is clicking on is
            // MultiLineCellEditor, otherwise return.
            if (!isEditorMultiLine(col)) {
                return;
            }

            // This is needed to provide normal edit funcionality for when
            // switching cells,
            // without this there will be problems!
            // Maybe find a better solution later on, 27-01-03...
            this.stopCellEditing();
            this.table.editCellAt(row, col);

            // Intercept the double-click event and set the edit/focus.
            if (e.getClickCount() >= 2) {
                Component c = getTableCellEditorComponent(this.table, this.table.getModel().getValueAt(row, col), true,
                        row, col);
                editable = true;
                c.requestFocus();

                // This should always happen..
                if (c instanceof JScrollPane) {
                    ((JScrollPane) c).getViewport().getView().requestFocus();
                }
            } else {
                editable = false;
            }
        }
        // Revalidate/repaint - may not be needed, needs more testing..27-01-03
        this.table.revalidate();
        this.table.repaint();
    }

    /**
     * Will check to see if the editor for a particular column is a
     * MultiLineCellEditor.
     * 
     * @param colIndex
     *            Index of the column
     * 
     * @return true if the editor is a MultiLineCellEditor.
     */
    private boolean isEditorMultiLine(int colIndex) {
        Object editor = this.table.getColumnModel().getColumn(colIndex).getCellEditor();
        return (editor instanceof MultiLineCellEditor);
    }
}
