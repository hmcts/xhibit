package uk.gov.courtservice.xhibit.client.casemanagement;

import java.util.Arrays;
import java.util.List;

public enum CaseType {
	APPEAL("A"), SENTENCE("S"), TRIAL("T"), MISC("A");
	
	private String dbValue;
	
	CaseType(String dbValue) {
		this.dbValue = dbValue;
	}

	public String getDbValue() {
		return dbValue;
	}
	
	public static List<String> CaseListingCaseTypes() {
		return Arrays.asList(new String[] {APPEAL.getDbValue(), SENTENCE.getDbValue(), TRIAL.getDbValue()});
	}
}
