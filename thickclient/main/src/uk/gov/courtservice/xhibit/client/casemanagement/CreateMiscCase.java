package uk.gov.courtservice.xhibit.client.casemanagement;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CreateMiscCase extends ManageCase {

	public CreateMiscCase(XhibitApplicationController xac) throws CSRecoverableException {
		super(xac, CaseType.MISC);
	}
}
