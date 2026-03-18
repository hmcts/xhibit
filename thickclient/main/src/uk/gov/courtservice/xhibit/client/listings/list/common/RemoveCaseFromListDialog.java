package uk.gov.courtservice.xhibit.client.listings.list.common;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class RemoveCaseFromListDialog extends XDialog {

	
	private static final long serialVersionUID = 1L;
	private RemoveCaseFromListPanel bodyPanel;
	private RemoveCaseFromListModel model;

	public RemoveCaseFromListDialog(XDialog parentDialog, RemoveCaseFromListModel model) throws CSRecoverableException {
		super(parentDialog, XHIBITConstant.getResource(XhibitBundles.Listings, "listingRemoveCaseTitle"), true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

		this.model = model;
		this.bodyPanel = new RemoveCaseFromListPanel(this,this.model);

		addBodyPanel(bodyPanel);
		setResizable(true);
		pack();
	}	
}