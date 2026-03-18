package uk.gov.courtservice.xhibit.client.listings.list.common;


/**
 * Class to represent other cases table row.
 * Comparison using CaseId. 
 * @author westalll
 *
 */
public class OtherCasesTableRow extends AbstractListCaseTableRow {
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caseId == null) ? 0 : caseId.hashCode());
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
		OtherCasesTableRow other = (OtherCasesTableRow) obj;
		if (caseId == null) {
			if (other.caseId != null)
				return false;
		} else if (!caseId.equals(other.caseId))
			return false;
		return true;
	}

}
