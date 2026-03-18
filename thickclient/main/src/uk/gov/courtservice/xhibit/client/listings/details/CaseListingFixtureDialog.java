package uk.gov.courtservice.xhibit.client.listings.details;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class CaseListingFixtureDialog extends XDialog {

	
	private static final long serialVersionUID = 1L;
	private CaseListingFixturePanel bodyPanel;
	private CaseListingFixtureModel model;

	

	public CaseListingFixtureDialog(XDialog parentDialog, CaseListingFixtureModel model) throws CSRecoverableException {
		super(parentDialog, XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingFixtureTitle"), true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

		this.model = model;
		this.bodyPanel = new CaseListingFixturePanel(this,this.model);

		addBodyPanel(bodyPanel);
		setResizable(true);
		pack();
	}	
	
	@Override
	protected OkCancelPanel createButtonPanel(int panelType, int defaultButton){
		OkCancelPanel buttonPanel = super.createButtonPanel(panelType, defaultButton);
		buttonPanel.okButton.setToolTipText("Save");
		buttonPanel.okButton.setText("Save");
		buttonPanel.cancelButton.setToolTipText("Close");
		buttonPanel.cancelButton.setText("Close");
		
		return buttonPanel;
	}

}
