package uk.gov.courtservice.xhibit.client.listings.list.warned;

import java.util.Date;

import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractListCaseTableRow;

/**
 * Warned list needs it's own row model since it
 * contains extra columns.
 * Comparison using caseDiaryFixtureId.
 * 
 * @author westalll
 *
 */
class WarnedFixtureTableRow extends AbstractListCaseTableRow {
	
	private Date listingDate;
	private String courtSiteCode;


	public Date getListingDate() {
		return listingDate;
	}

	public void setListingDate(Date listingDate) {
		this.listingDate = listingDate;
	}

	public String getCourtSiteCode() {
		return courtSiteCode;
	}

	public void setCourtSiteCode(String courtSiteCode) {
		this.courtSiteCode = courtSiteCode;
	}
	
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
		WarnedFixtureTableRow other = (WarnedFixtureTableRow) obj;
		if (caseDiaryFixtureId == null) {
			if (other.caseDiaryFixtureId != null)
				return false;
		} else if (!caseDiaryFixtureId.equals(other.caseDiaryFixtureId))
			return false;
		return true;
	}
	

}
