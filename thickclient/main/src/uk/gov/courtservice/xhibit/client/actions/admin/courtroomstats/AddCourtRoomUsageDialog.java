package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class AddCourtRoomUsageDialog extends XDialog {

	private static final long serialVersionUID = 1L;
	private AddCourtRoomUsagePanel bodyPanel;
	private AddCourtRoomUsageModel model;

	public AddCourtRoomUsageDialog(XDialog parentDialog, AddCourtRoomUsageModel model) throws CSRecoverableException {
		super(parentDialog, XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddCourtRoomUsageDialog.title"), true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);

		this.model = model;
		this.bodyPanel = new AddCourtRoomUsagePanel(this,this.model);

		addBodyPanel(bodyPanel);
		setResizable(true);
		pack();
	}	
}