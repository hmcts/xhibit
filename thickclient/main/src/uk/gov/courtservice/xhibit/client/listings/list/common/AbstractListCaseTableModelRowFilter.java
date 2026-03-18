package uk.gov.courtservice.xhibit.client.listings.list.common;

import javax.swing.RowFilter;

public abstract class AbstractListCaseTableModelRowFilter extends RowFilter<ListCaseTableModel, Integer> {

	@Override
	public boolean include(Entry<? extends ListCaseTableModel, ? extends Integer> entry) {
		final ListCaseTableRow rowData = (ListCaseTableRow) entry.getModel().getDataAt(entry.getIdentifier());
		return include(rowData);
	}
	
	/**
	 * Method to determine if a row should be included.
	 * @param rowData The rowData to filter.
	 * @return True if the data should be shown.
	 */
	protected abstract boolean include(final ListCaseTableRow rowData);
}
