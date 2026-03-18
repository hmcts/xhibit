package uk.gov.courtservice.xhibit.client.listings.list.common;

/**
 * Class to represent non-fixed cases table row.
 * Comparison using parentCaseOnListId. 
 * @author westalll
 *
 */
public class NonFixedCaseTableRow extends AbstractListCaseTableRow {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parentCaseOnListId == null) ? 0 : parentCaseOnListId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NonFixedCaseTableRow other = (NonFixedCaseTableRow) obj;
		if (parentCaseOnListId == null) {
			if (other.parentCaseOnListId != null)
				return false;
		} else if (!parentCaseOnListId.equals(other.parentCaseOnListId))
			return false;
		return true;
	}

}
