package uk.gov.courtservice.xhibit.client.util.table;

import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Scroll Table Row To View
 * </p>
 * <p>
 * Description: The class will scroll the selected row of table into view within
 * its JScrollPane If the row is larger than the view it will display the top of
 * the cell.
 * </p>
 * <p>
 * It is intended for this class to be run in a SwingUtilities.invokeLater as it
 * will typically be required on a table refresh and if called in the main code
 * the screen will not have repainted a newly inserted row. However, simply
 * creating an instance of the class and calling run() will still perform the
 * necessary action.
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */
public class ScrollTableRowToView implements Runnable {
    private static Logger log = CSServices.getLogger(ScrollTableRowToView.class);

    private static boolean retry = true;

    private JScrollPane _toScroll;

    private XTable _table;

    private int _col;

    /**
     * Scroll the selected row to view. Assume column zero to be shown
     * 
     * @param table
     *            The table
     * @param toScroll
     *            The Scroll Pane the table is in
     */
    public ScrollTableRowToView(XTable table, JScrollPane toScroll) {
        this(table, toScroll, 0);
    }

    /**
     * Select the column to be scrolled to view.
     * 
     * @param table
     *            The table
     * @param toScroll
     *            The Scroll Pane the table is in
     * @param col
     *            The column to be scrolled into view
     */
    public ScrollTableRowToView(XTable table, JScrollPane toScroll, int col) {
        _table = table;
        _toScroll = toScroll;
        // check the column is known to the table.
        // if < 0 , then 0
        // if > columnCount , then columnCount-1.
        _col = (col >= 0 ? (col < table.getColumnCount() ? col : table.getColumnCount() - 1) : 0);
    }

    public void run() {
        // _toScroll.revalidate();
        // _toScroll.repaint();
        if (_table.getSelectedRow() >= 0) {
            // Get the rectangle for the selected row, first col
            Rectangle r = _table.getCellRect(_table.getSelectedRow(), _col, false);
            // Get the rectangle that is currently visible in the view port
            Rectangle vr = _toScroll.getViewport().getViewRect();
            // If the selected row is not in the view
            log.debug("run():: contains(vr, r):" + contains(vr, r));
            int loopCheck = 0;
            while (!contains(vr, r) && loopCheck < 3) {
                // Request the row to be scrolled into view
                _table.scrollRectToVisible(r);
                // Get the dimensions of the row again as may have changed due
                // to multiline code
                r = _table.getCellRect(_table.getSelectedRow(), _col, false);
                // Get the new rectangle that is currently visible in the view
                // port
                vr = _toScroll.getViewport().getViewRect();
                // Increment the loopCheck. This is used to limit scroll retries
                // to 4 attempts
                // Anymore than this and something has gone wrong, so break out
                // and stop trying.
                loopCheck++;
                // loop round and check that the row is in view
            }

            if (loopCheck >= 3) {
                if (retry) {
                    retry = false;
                    _table.revalidate();
                    _table.repaint();
                    _toScroll.revalidate();
                    _toScroll.repaint();
                    SwingUtilities.invokeLater(new ScrollTableRowToView(_table, _toScroll, _col));
                } else {
                    retry = true;
                }
            } else {
                retry = true;
            }
        }
    }

    /**
     * In case the rectangle is larger than the view, contains will always
     * return false, in this situation, we just check for intersection.
     * 
     * @param view
     *            The rectangle that represents the view
     * @param toCheck
     *            The rectangle to be scrolled into the view
     * @return
     */
    private boolean contains(Rectangle view, Rectangle toCheck) {
        if (view.height < toCheck.height || view.width < toCheck.width) {
            boolean rc = view.intersects(toCheck);
            log.debug("Contains:: Intersecting: " + rc + ", viewRect=" + view.toString() + ", cellRect="
                    + toCheck.toString());
            return rc;
        } else {
            boolean rc = view.contains(toCheck);
            log.debug("Contains:: Containing: " + rc + ", viewRect=" + view.toString() + ", cellRect="
                    + toCheck.toString());
            return rc;
        }
    }
}
