package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class AddJudgeUsageDialog extends XDialog {

	private static final long serialVersionUID = 1L;
	private AddJudgeUsagePanel bodyPanel;
	private AddJudgeUsageModel model;

	public AddJudgeUsageDialog(XDialog parentDialog, AddJudgeUsageModel model) throws CSRecoverableException {
		super(parentDialog, XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "AddJudgeUsageDialog.title"), true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);

		this.model = model;
		this.bodyPanel = new AddJudgeUsagePanel(this,this.model);

		addBodyPanel(bodyPanel);
		setResizable(true);
		pack();
	}	
}