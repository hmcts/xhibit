package uk.gov.courtservice.xhibit.client.util.table.model.sort;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;

/**
 * <p>
 * Title: XHIBIT2
 * </p>
 * <p>
 * Description: HeaderListener
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

public class SortHeaderListener extends MouseAdapter {
    JTableHeader header;

    SortButtonRenderer renderer;

    XSortableTableModel sortModel;

    public SortHeaderListener(JTableHeader header, XSortableTableModel sortModel, SortButtonRenderer renderer) {
        this.header = header;
        this.renderer = renderer;
        this.sortModel = sortModel;
    }

    // Display the header as pressed down
    public void mousePressed(MouseEvent e) {
        int col = header.columnAtPoint(e.getPoint());
        renderer.setPressedColumn(col);
        // renderer.setSelectedColumn(col);
        header.repaint();
    }

    public void mouseClicked(MouseEvent e) {
        Window jw = null;
        if (!e.isPopupTrigger()) {
            try {
                if (e.getSource() instanceof Component) {
                    jw = XSwingUtilities.getWindowAncestor((Component) e.getSource());
                }
                Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
                if (jw != null) {
                    jw.setCursor(waitCursor);
                    jw.validate();
                    jw.repaint();
                }
                renderer.setCursor(waitCursor);

                int col = header.columnAtPoint(e.getPoint());
                int sortCol = header.getTable().convertColumnIndexToModel(col);

                renderer.setPressedColumn(col);
                renderer.setSelectedColumn(col);
                header.repaint();
                XHIBITConstant.debug("Did stuff with the renderer (column " + col
                        + ") and a header.repaint()  Can you see it?");

                if (header.getTable().isEditing())
                    header.getTable().getCellEditor().stopCellEditing();
                boolean isAscent;
                if (SortButtonRenderer.DOWN == renderer.getState(col)) {
                    isAscent = true;
                } else {
                    isAscent = false;
                }

                long start = System.currentTimeMillis();
                sortModel.sortByColumn(col, isAscent);
                header.getTable().tableChanged(new TableModelEvent((TableModel) this.header.getTable().getModel()));
                long end = System.currentTimeMillis();

                XHIBITConstant.debug("Sorting " + sortModel.getRowCount() + " rows in "
                        + ((isAscent == true) ? "ascending" : "descending") + " took " + (end - start) + "ms.");

                // Reset renderer
                renderer.setPressedColumn(-1); // clear

            } catch (Exception ex) {
                XHIBITConstant.handleError(ex);
            } finally {
                Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                renderer.setCursor(defCursor);
                header.repaint();
                if (jw != null) {
                    jw.setCursor(defCursor);
                    jw.validate();
                    jw.repaint();
                }
            }
        }
    }

    // Reset render display
    public void mouseReleased(MouseEvent e) {
        renderer.setPressedColumn(-1); // clear
        header.repaint();
    }
}
