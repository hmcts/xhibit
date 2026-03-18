package uk.gov.courtservice.xhibit.client.casemanagement;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CreateSentenceCase extends ManageCase {

	public CreateSentenceCase(XhibitApplicationController xac) throws CSRecoverableException {
		super(xac, CaseType.SENTENCE);
	}
}
