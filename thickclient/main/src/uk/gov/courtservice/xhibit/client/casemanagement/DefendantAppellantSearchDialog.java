package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class DefendantAppellantSearchDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private DefendantAppellantSearchPanel bodyPanel;
	private DefendantAppellantSearchModel model;

	public DefendantAppellantSearchDialog(Frame frame, DefendantAppellantSearchModel model)
			throws CSRecoverableException {
		super(frame, XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
				"defendantAppellantSearch.mainTitle"), true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);

		this.model = model;
		this.bodyPanel = new DefendantAppellantSearchPanel(this, this.model);

		addBodyPanel(bodyPanel);
		pack();
	}

	@Override
	public void dispose() {
		clearStatusBarScreenCode();
		super.dispose();
	}
}
