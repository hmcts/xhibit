package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * Cancel Tasks to be performed on a Panel when cancel button clicked.
 * 
 * @author grewalg
 *
 */
public class RefCancelTasks {

	public static void performCancelTasks(RefSearchUpdatePanel panel) throws CSRecoverableException {

		try {
			if (RefSearchUpdatePanelUtil.hasUnsavedData(panel.getComponentList(), panel.isUpdate(),
					panel.getModified())) {
				int confirmed = RefSearchUpdatePanelUtil.unsavedChangesDialog();
				if (confirmed == 0) {
					panel.setValueObject(panel.getValueObject());
					panel.hideErrorLabels();
					panel.getParentContainer().dispose();
					panel.setModified(false);
				}
			} else {
				panel.setValueObject(panel.getValueObject());
				panel.hideErrorLabels();
				panel.getParentContainer().dispose();
			}
		} catch (Exception e) {
			CSRecoverableException csre = new CSRecoverableException("gui.user.search.genericFailedMessage",
					"myParentSeachControl.doSearch() returned exception to XHIBITSearchCriteriaPanel's SearchAction.",
					e);
			throw csre;
		}
	}
}
