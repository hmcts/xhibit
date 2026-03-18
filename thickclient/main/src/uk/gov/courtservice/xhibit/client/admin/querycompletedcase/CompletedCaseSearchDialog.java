package uk.gov.courtservice.xhibit.client.admin.querycompletedcase;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class CompletedCaseSearchDialog extends XDialog {
	
	/**
	 * serialversion uid created for class
	 */
	private static final long serialVersionUID = 478483546452526711L;
	
	private CompletedCaseSearchPanel caseSearchPanel;
	private CompletedCaseSearchModel caseSearchModel;
	
	public CompletedCaseSearchDialog(Frame frame, CompletedCaseSearchModel caseSearchModel) throws CSRecoverableException { 
		super(frame, "Search For Completed Case", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		this.caseSearchModel = caseSearchModel;
		this.caseSearchPanel = new CompletedCaseSearchPanel(this, this.caseSearchModel);
		addBodyPanel(caseSearchPanel);
		pack();
	}

}
