package uk.gov.courtservice.xhibit.client.crestformsbf.swing.table;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * A header for a sorted table
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */
public class SortTableHeader extends JTableHeader implements MouseListener, MouseMotionListener {

    private int pressedColumn = -1;

    private int armedColumn = -1;

    /**
     * Construct a sort table header
     */
    public SortTableHeader(TableColumnModel columnModel) {
        super(columnModel);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Returns a new instance of the SortTableHeaderRenderer
     * 
     * @return new SortTableHeaderRenderer
     */
    protected TableCellRenderer createDefaultRenderer() {
        return new SortTableHeaderRenderer(this);
    }

    /**
     * @return true if the column isPressed
     */
    public boolean isPressedColumn(int column) {
        return column == pressedColumn;
    }

    /**
     * @return true if the column isArmed
     */
    public boolean isArmedColumn(int column) {
        return true; // column == armedColumn; armed needs a more complex
        // implementation
    }

    /**
     * Return true if the column is the sorted column
     */
    public boolean isSortColumn(int column) {
        return convertColumnIndexToModel(column) == getSortTableModel().getSortedColumn();
    }

    /**
     * @return the converted column index
     */
    public int convertColumnIndexToModel(int column) {
        return getTable().convertColumnIndexToModel(column);
    }

    /**
     * @return true if the sort is ascending
     */
    public boolean isSortAscending() {
        return getSortTableModel().isSortAscending();
    }

    /**
     * Get the SortTableModel from the underlying table
     * 
     * @returns the SortTableModel
     */
    public SortTableModel getSortTableModel() {
        return SortTableModel.getSortTableModel(getTable().getModel());
    }

    /**
     * Get the index of the column in the model that coresponds to the given
     * point.
     * 
     * @param the
     *            point to look up
     * @return the column index of the point
     */

    /*
     * public int modelColumnAtPoint(Point point) { return
     * columnAtPoint(point);//getTable().convertColumnIndexToModel(columnAtPoint(point)); }
     */

    /**
     * @return true if the header is a sort table header
     */
    public static boolean isSortTableHeader(JTableHeader header) {
        return header != null && header instanceof SortTableHeader;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on
     * a component.
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {
        int column = columnAtPoint(e.getPoint());
        if (pressedColumn != column) {
            pressedColumn = column;
            repaint();
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {
        if (pressedColumn != -1) {
            if (isArmedColumn(pressedColumn)) {
                getSortTableModel().sort(convertColumnIndexToModel(pressedColumn));
            }
            pressedColumn = -1;
            repaint();
        }

    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {
        int column = columnAtPoint(e.getPoint());
        if (column != armedColumn) {
            armedColumn = column;
        }

    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {
        if (armedColumn != -1) {
            armedColumn = -1;
        }
    }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged.
     */
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Invoked when the mouse cursor has been moved onto a component but no
     * buttons have been pushed.
     */
    public void mouseMoved(MouseEvent e) {
    }
}
