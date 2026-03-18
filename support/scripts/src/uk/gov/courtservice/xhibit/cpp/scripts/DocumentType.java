package uk.gov.courtservice.xhibit.cpp.scripts;

public enum DocumentType {

	DL("DailyList"),
	WL("WarnedList"),
	FL("FirmList"),
	WP("WebPage"),
	PD("PublicDisplay");
	
	private String docName;
	
	DocumentType(String docName) {
		this.docName = docName;
	}
	
	public String getDocName() {
		return docName;
	}
}
