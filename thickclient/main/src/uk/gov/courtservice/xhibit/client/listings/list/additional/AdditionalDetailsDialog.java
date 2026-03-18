package uk.gov.courtservice.xhibit.client.listings.list.additional;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Title: AdditionalDetailsDialog
 * Description: Dialog to create/amend additional case details.
 * @author westalll
 *
 */
public class AdditionalDetailsDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	
	
	public AdditionalDetailsDialog(final XDialog parentDialog, final AdditionalDetailsModel model) throws CSRecoverableException {
		super(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsTitlePart1")
						+ model.getCaseOnList().getCase().getCaseType()
						+ model.getCaseOnList().getCase().getCaseNumber()
						+ XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsTitlePart2"),
						
				true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

		bodyPanel = new AdditionalDetailsPanel(this, model);
		addBodyPanel(bodyPanel);
		setResizable(true);
		pack();
	}
	
	
}



