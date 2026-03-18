package uk.gov.courtservice.xhibit.client.listings.list.warned;

import javax.swing.RowFilter;

/**
 * Warned list needs it's own row filter.
 * Based on AbstractListCaseTableModelRowFilter.
 * @author westalll
 *
 */
public class WarnedListCaseTableModelRowFilter extends RowFilter<WarnedFixtureTableModel, Integer> {

	@Override
	public boolean include(Entry<? extends WarnedFixtureTableModel, ? extends Integer> entry) {
		return true;
	}
}
