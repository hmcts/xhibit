package uk.gov.courtservice.xhibit.client.listings.list.common;

import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;

/**
 * Default implementation of case transfer handler that removes row from table.
 * 
 * @author uphillj
 *
 */
public class DefaultCaseTransferHandler extends AbstractCaseTransferHandler {

	private static final long serialVersionUID = 1L;

	public DefaultCaseTransferHandler(ListCaseTableModel listCaseTableModel, TreeNodeFactory treeNodeFactory) {
		super(listCaseTableModel, treeNodeFactory);
	}

    @Override
    protected void exportDone(ListCaseTableRow row) {
    	getListCaseTableModel().removeCase(row);
		getListCaseTableModel().fireTableDataChanged();
    }
}
