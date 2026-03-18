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
class DeleteAction extends XAction {
	/**
	 * 
	 */
	private RefSearchUpdatePanel detailsPanel;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5293170752000276224L;

	public DeleteAction(RefSearchUpdatePanel detailsPanel) {
		this.detailsPanel = detailsPanel;
		populateFromBundle("btnDelete");
	}

	public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
		detailsPanel.performDelete();
	}
}