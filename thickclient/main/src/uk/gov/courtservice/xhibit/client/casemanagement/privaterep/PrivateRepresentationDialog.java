package uk.gov.courtservice.xhibit.client.casemanagement.privaterep;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class PrivateRepresentationDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private PrivateRepresentationPanel privateBodyPanel;
	private PrivateRepresentationModel privateModel;

	public PrivateRepresentationDialog(Frame frame, PrivateRepresentationModel model) throws CSRecoverableException {
		super(frame, XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "private.privateRepMainTitle"),
				true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);

		this.privateModel = model;
		this.privateBodyPanel = new PrivateRepresentationPanel(this, this.privateModel);

		addBodyPanel(privateBodyPanel);
		pack();
	}
}