package uk.gov.courtservice.xhibit.client.listings.list.common;


/**
 * Class to represent a general fixture table row.
 * Comparison using caseDiaryFixtureId. 
 * @author westalll
 *
 */
public class GeneralFixtureTableRow extends AbstractListCaseTableRow {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caseDiaryFixtureId == null) ? 0 : caseDiaryFixtureId.hashCode());
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
		GeneralFixtureTableRow other = (GeneralFixtureTableRow) obj;
		if (caseDiaryFixtureId == null) {
			if (other.caseDiaryFixtureId != null)
				return false;
		} else if (!caseDiaryFixtureId.equals(other.caseDiaryFixtureId))
			return false;
		return true;
	}

}
