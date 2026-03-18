package uk.gov.courtservice.xhibit.client.listings.details;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class CaseListingDeleteFixtureDialog extends XDialog {

	
	private static final long serialVersionUID = 1L;
	private CaseListingDeleteFixturePanel bodyPanel;
	private CaseListingDeleteFixtureModel model;

	public CaseListingDeleteFixtureDialog(XDialog parentDialog, CaseListingDeleteFixtureModel model) throws CSRecoverableException {
		super(parentDialog, XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDeleteFixtureTitle"), true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

		this.model = model;
		this.bodyPanel = new CaseListingDeleteFixturePanel(this,this.model);

		addBodyPanel(bodyPanel);
		setResizable(true);
		pack();
	}	
}
