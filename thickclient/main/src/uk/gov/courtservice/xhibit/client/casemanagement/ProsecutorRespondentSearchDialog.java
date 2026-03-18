package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class ProsecutorRespondentSearchDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private ProsecutorRespondentSearchPanel prosBodyPanel;
	private ProsecutorRespondentSearchModel prosModel;

	public ProsecutorRespondentSearchDialog(Frame frame, ProsecutorRespondentSearchModel model)
			throws CSRecoverableException {
		super(frame,
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosecutorRespondentSearchTitle"),
				true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);

		this.prosModel = model;
		this.prosBodyPanel = new ProsecutorRespondentSearchPanel(this, this.prosModel);
		addBodyPanel(prosBodyPanel);
		pack();
	}
}
