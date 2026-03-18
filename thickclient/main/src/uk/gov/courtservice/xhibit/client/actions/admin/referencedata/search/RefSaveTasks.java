package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

public class RefSaveTasks {

	public static void performSaveTasks(RefSearchUpdatePanel panel) throws CSRecoverableException {
		panel.performSave();
	}
}
