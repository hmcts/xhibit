package uk.gov.courtservice.xhibit.client.util;

public class DropdownCodeStringValue {

	private String displayName;
	private String code;
	private boolean isIntCode;
	private String printString;
	
	public DropdownCodeStringValue(String displayName, String code) {
		this.displayName = displayName;
		this.code = code;
	}
	
	public DropdownCodeStringValue(String displayName, String code, String printString) {
		this.displayName = displayName;
		this.code = code;
		this.printString = printString;
	}
	
	public DropdownCodeStringValue(String displayName, String code, boolean isIntCode) {
		this.displayName = displayName;
		this.code = code;
		this.isIntCode=isIntCode;
	}

	public String getPrintString() {
		return printString;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getCode() {
		return code;
	}
	
	public boolean getIntCode() {
		return isIntCode;
	}
	
	public String toString() {
		return displayName;
	}
}
