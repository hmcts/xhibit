package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * The Back Action.
 * 
 * @author grewalg
 *
 */
class BackAction extends XAction {
	/**
	 * 
	 */
	private final RefSearchUpdatePanel detailsPanel;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5293170752000276224L;

	public BackAction(RefSearchUpdatePanel detailsPanel) {
		this.detailsPanel = detailsPanel;
		populateFromBundle("WizBack");
	}

	public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
		RefBackTasks.performBackTasks(detailsPanel);
	}
}