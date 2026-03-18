package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

public class RefBackTasks {
	public static void performBackTasks(RefSearchUpdatePanel detailsPanel) throws CSRecoverableException {
		if (RefSearchUpdatePanelUtil.hasUnsavedData(detailsPanel.getComponentList(), detailsPanel.isUpdate(),
				detailsPanel.getModified())) {
			int confirmed = RefSearchUpdatePanelUtil.unsavedChangesDialog();
			if (confirmed == 0) {
				detailsPanel.setValueObject(detailsPanel.getValueObject());
				detailsPanel.hideErrorLabels();
				detailsPanel.setVisible(false);
				if (detailsPanel.isUpdate()) {
					detailsPanel.getRefSearchController().showXSResultsPanelAgain();
				} else {
					detailsPanel.getParentContainer().dispose();
				}
				detailsPanel.setModified(false);
			}
		} else {
			detailsPanel.setValueObject(detailsPanel.getValueObject());
			detailsPanel.hideErrorLabels();
			detailsPanel.setVisible(false);
			if (detailsPanel.isUpdate()) {
				detailsPanel.getRefSearchController().showXSResultsPanelAgain();
			} else {
				detailsPanel.getParentContainer().dispose();
			}
		}
	}
}
