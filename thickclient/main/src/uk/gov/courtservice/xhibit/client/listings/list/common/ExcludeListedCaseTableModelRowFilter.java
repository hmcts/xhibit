package uk.gov.courtservice.xhibit.client.listings.list.common;

/**
 * A row filter that filters out data that is marked as listed.
 * 
 * @author westalll
 */
public class ExcludeListedCaseTableModelRowFilter extends AbstractListCaseTableModelRowFilter {

	@Override
	protected boolean include(final ListCaseTableRow rowData) {
		return !rowData.isListedOnRight();
	}

}
