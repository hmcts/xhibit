package uk.gov.courtservice.xhibit.client.listings;

/**
 * An Enum to represent the list types returned.
 * @author westalll
 *
 */
public enum ListTypeEnum {
	Daily,
	Warned,
	Firm;
	
	
	public boolean isDaily() {
		return Daily.equals(this);
	}
	
	public boolean isFirm() {
		return Firm.equals(this);
	}
	
	public boolean isWarned() {
		return Warned.equals(this);
	}
}
