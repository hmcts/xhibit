package uk.gov.courtservice.xhibit.client.casemanagement.publicrep;

/**
 * Enum for Granted By for an order in public rep
 * @author waltersn
 *
 */
public enum GrantedBy {
	LA("LAA"),
	CC("Crown Court");
	
	private final String displayName;
	
	private GrantedBy(String s) {
		displayName = s;
	}
	
	public boolean equalsDisp(String dispName) {
		return displayName.equals(dispName);
	}
	
	public String nameToStore() {
		return this.name();
	}
	
	public String getDisplayName() {
		return displayName;
	}

}
