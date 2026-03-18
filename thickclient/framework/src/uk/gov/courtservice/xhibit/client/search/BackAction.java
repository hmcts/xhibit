package uk.gov.courtservice.xhibit.client.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;

class BackAction extends XAction {
    /**
	 * 
	 */
	private final XHIBITSearchDetailsPanel xhibitSearchDetailsPanel;

	public BackAction(XHIBITSearchDetailsPanel xhibitSearchDetailsPanel) {
        this.xhibitSearchDetailsPanel = xhibitSearchDetailsPanel;
		populateFromBundle("WizBack");
    }

    public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
        try {
            this.xhibitSearchDetailsPanel.myParentSearchControl.showXSResultsPanelAgain();
        } catch (Exception e) {
            CSRecoverableException csre = new CSRecoverableException(
                    "gui.user.search.genericFailedMessage",
                    "myParentSeachControl.doSearch() returned exception to XHIBITSearchCriteriaPanel's SearchAction.",
                    e);
            throw csre;
        }
    }
}