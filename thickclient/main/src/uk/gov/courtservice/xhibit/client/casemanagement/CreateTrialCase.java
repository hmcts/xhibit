package uk.gov.courtservice.xhibit.client.casemanagement;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CreateTrialCase extends ManageCase {

	public CreateTrialCase(XhibitApplicationController xac) throws CSRecoverableException {
		super(xac, CaseType.TRIAL);
	}
}
