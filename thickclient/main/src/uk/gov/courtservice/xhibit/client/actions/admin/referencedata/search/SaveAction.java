package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * The Save Action.
 * 
 * @author grewalg
 *
 */
public class SaveAction extends XAction {

	/**
	 * 
	 */
	private final RefSearchUpdatePanel detailsPanel;
	private static final long serialVersionUID = 870460344113126945L;

	public SaveAction(RefSearchUpdatePanel detailsPanel) {
		this.detailsPanel = detailsPanel;
		populateFromBundle("btnSave");
	}

	public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
		RefSaveTasks.performSaveTasks(detailsPanel);
	}
}