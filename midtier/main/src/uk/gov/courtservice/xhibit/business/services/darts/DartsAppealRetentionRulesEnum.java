
package uk.gov.courtservice.xhibit.business.services.darts;

public enum DartsAppealRetentionRulesEnum {

	USE_LESSER_OFFENCE("C"),
	USE_INCREASED_CROWN_SENTENCE_DISPOSAL("C"),
	USE_INCREASED_SENTENCE_DISPOSAL("C"),
	USE_INCREASED_SENTENCE("C"),
	USE_CROWN_VARIATION_DISPOSAL("C"),
	USE_VARIED_SENTENCE_DISPOSAL("C"),
	USE_VARIED_SENTENCE("C"),
	USE_MAGS_DISPOSAL("M"),
	USE_NG_ACQUITTAL(null);
	
	private String courtType;
	
	DartsAppealRetentionRulesEnum(String courtType) {
		this.courtType = courtType;
    }
	
	public String getCourtType() {
		return courtType;
	}
}
