package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.RJS.RunRJSReportDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * Description: The action which is called to run the Report of Judge Sittings (RJS) report
 * @author westalll
 *
 */
public class RJSMenuItemAction extends SynchXAction {

	private static final long serialVersionUID = 1L;

	private RunRJSReportDialog dialog = null;

	public RJSMenuItemAction() {
		populateFromBundle("RJSReport");
	}

	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		XhibitApplicationController xac = (XhibitApplicationController) getController();
		dialog = new RunRJSReportDialog(xac);
	}

	public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		if (dialog != null)
			dialog.setVisible(true);
	}
}
