
package uk.gov.courtservice.xhibit.business.services.darts;

import uk.gov.courtservice.xhibit.business.entities.darts.RefDispRetentionPolicy;

public class DartsDisposalLineData extends DartsCaseData {
	Integer disposal2Id;
	String disposalCode;
	Integer defendantId;
	Integer defendantOnCaseId;
	Integer defendantOnOffenceId;
	String courtType;
	Integer countNo;
	RefDispRetentionPolicy dispRetentionPolicy;
	String hasDuration = NO;

	DartsDisposalLineData() {
		
	}
}