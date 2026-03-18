package uk.gov.courtservice.xhibit.client.casemanagement;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CreateAppealCase extends ManageCase {
	private CaseStatus caseStatus;
	
	public CreateAppealCase(XhibitApplicationController xac) throws CSRecoverableException {
		super(xac, CaseType.APPEAL);
	}
}
