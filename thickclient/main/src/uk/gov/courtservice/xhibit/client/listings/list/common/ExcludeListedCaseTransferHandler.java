package uk.gov.courtservice.xhibit.client.listings.list.common;

import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;

/**
 * Implementation of case transfer handler that marks row as being listed.
 * 
 * @author uphillj
 *
 */
public class ExcludeListedCaseTransferHandler extends AbstractCaseTransferHandler {

	private static final long serialVersionUID = 1L;

	public ExcludeListedCaseTransferHandler(ListCaseTableModel listCaseTableModel, TreeNodeFactory treeNodeFactory) {
		super(listCaseTableModel, treeNodeFactory);
	}

    @Override
    protected void exportDone(ListCaseTableRow row) {
		row.setListedOnRight(true);
		getListCaseTableModel().fireTableDataChanged();
    }
}
