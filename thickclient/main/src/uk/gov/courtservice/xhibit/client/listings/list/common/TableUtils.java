package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.KeyboardFocusManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 * A utility class to help with JTables.
 * @author westalll
 *
 */

public class TableUtils {
	
    public static void setupDefaultsOnJTable(final JTable table) {
    	table.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
    	table.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public static void setupDragAndDropOnJTable(final JTable table, final TransferHandler transferHandler) {
		// setup the drag sources for drag and drop
		table.setDragEnabled(true);
		table.setDropMode(DropMode.INSERT_ROWS);
		table.setTransferHandler(transferHandler);
	}
    
    public static void setupColumnHeaderToolTips(final JTable table, final List<String> toolTips) {
    	MouseMotionListener toolTipsListener = new ColumnHeaderToolTipsListener(toolTips);
    	table.getTableHeader().addMouseMotionListener(toolTipsListener);
    }
    
    public static void setupColumnHorizontalAlignment(final JTable table, final int column, final int alignment) {
		final TableColumnModel columModel = table.getColumnModel();
		final TableColumn tableComumn = columModel.getColumn(column);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(alignment);
		tableComumn.setCellRenderer(renderer);
    }

    /**
     * This method sets the row filter and row sorter. Currently is tied to a ListCaseTableModel and sorts on
     * GroupNo, CaseNo.
     *  
     * @param table The JTable
     * @param filter The filter.
     */
	public static void setTableRowFilter(final JTable table, final AbstractListCaseTableModelRowFilter filter) {

		final TableRowSorter<ListCaseTableModel> sorter = new TableRowSorter<ListCaseTableModel>(
				(ListCaseTableModel) table.getModel());

		sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(ListCaseTableModel.COL_GROUP_NO, SortOrder.ASCENDING),
				new RowSorter.SortKey(ListCaseTableModel.COL_CASE_NUMBER, SortOrder.ASCENDING)));
		sorter.setRowFilter(filter);
		table.setRowSorter(sorter);
	}
    
	/**
	 * Mouse listener to set the table header tooltip based on the
	 * column header which the mouse is currently hovering over.
	 */
	private static class ColumnHeaderToolTipsListener extends MouseMotionAdapter {
		
		private int currentColumn = -1;
		private List<String> columnToolTips;
		
		public ColumnHeaderToolTipsListener(final List<String> toolTips) {
			columnToolTips = new ArrayList<String>(toolTips);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// Get the index for the column header that the mouse is currently hovering over
			JTableHeader header = (JTableHeader)e.getSource();
			TableColumnModel model = header.getTable().getColumnModel();
			int index = model.getColumnIndexAtX(e.getX());
			int realIndex = model.getColumn(index).getModelIndex();
			
			// If current column has changed, update table header tooltip and current index
			if (currentColumn != realIndex) {
				header.setToolTipText(columnToolTips.get(realIndex));
				currentColumn = realIndex;
			}
		}
	}
}
