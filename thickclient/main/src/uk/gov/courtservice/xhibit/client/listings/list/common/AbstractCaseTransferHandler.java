package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;

/**
 * Base class for transfer handler for dragging cases from the tables
 * on the LHS to the main listing panel on the RHS.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractCaseTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 1L;
	private ListCaseTableModel listCaseTableModel;
	private TreeNodeFactory treeNodeFactory;
	
	public AbstractCaseTransferHandler(ListCaseTableModel listCaseTableModel, TreeNodeFactory treeNodeFactory){
		this.listCaseTableModel = listCaseTableModel;
		this.treeNodeFactory = treeNodeFactory;
	}

	protected ListCaseTableModel getListCaseTableModel() {
		return listCaseTableModel;
	}

	protected TreeNodeFactory getTreeNodeFactory() {
		return treeNodeFactory;
	}
	
	@Override
	public boolean canImport(final TransferSupport support) {
         return false;             
    }

	@Override
    public boolean importData(final TransferSupport support) {
        if (!canImport(support)) {
        	return false;
        }
        return false;
    }
	
	@Override
	public int getSourceActions(JComponent c){
		return TransferHandler.MOVE;
	}
    
    @Override
	public Transferable createTransferable(JComponent c) {
		final int selectedRow = getSelectedRowFromTableSorter(c);
		final ListCaseTableRow listCaseTableRow = (ListCaseTableRow) listCaseTableModel.getDataAt(selectedRow);
		return new ListCaseTransferableCase(treeNodeFactory, listCaseTableRow);
	}

	private int getSelectedRowFromTableSorter(JComponent c) {
		final JTable table = (JTable) c;
		return table.getRowSorter() == null ? table.getSelectedRow()
				: table.convertRowIndexToModel(table.getSelectedRow());
	}

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
		if (action == TransferHandler.MOVE) {
			try {
				// Get the original row of the data being transferred and call
				// sub-class implementation to decide what to do with the row
				ListCaseTableRow row = (ListCaseTableRow)data.getTransferData(AbstractTransferable.sourceFlavor);
				exportDone(row);
			} catch (IOException e) {
				throw new IllegalArgumentException("Unsupported data flavor", e);
			} catch (UnsupportedFlavorException e) {
				throw new IllegalArgumentException("Unsupported data flavor", e);
			}
		}
    }

    /**
     * Sub-classes override this method to process the exported row.
     * 
     * @param row
     */
    abstract protected void exportDone(ListCaseTableRow row);
}