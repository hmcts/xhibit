package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * The Cancel Action.
 * 
 * @author grewalg
 *
 */
public class CancelAction extends XAction {

	/**
	 * 
	 */
	private final RefSearchUpdatePanel detailsPanel;
	private static final long serialVersionUID = 870460344113126945L;

	public CancelAction(RefSearchUpdatePanel detailsPanel) {
		this.detailsPanel = detailsPanel;
		populateFromBundle("btnCancel");
	}

	public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
		RefCancelTasks.performCancelTasks(detailsPanel);
	}
}