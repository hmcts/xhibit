package uk.gov.courtservice.xhibit.client.listings.list.common;

/**
 * Default implementation of Row Filter that does not filter.
 * 
 * @author westalll
 *
 */
public class DefaultTableModelRowFilter extends AbstractListCaseTableModelRowFilter {

	@Override
	protected boolean include(final ListCaseTableRow rowData) {
		return true;
	}

}
