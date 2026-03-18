package uk.gov.courtservice.xhibit.client.listings.list.sitting;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: SittingDialog
 * </p>
 * <p>
 * Description: Dialog to create/amend sittings
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Groen
 * @version 1.0
 */

public class SittingDialog extends XDialog {
	
	private static final long serialVersionUID = 1L;
	private SittingPanel bodyPanel;
	private SittingModel model;
	private AbstractDailyFirmListModel listModel;

	public SittingDialog(XDialog parentDialog, SittingModel model, ListModel listModel) throws CSRecoverableException {
		super(parentDialog, XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingSittingTitle") + " " + model.getSitting().getSittingNumber(), true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

		this.model = model;
		this.listModel = (AbstractDailyFirmListModel)listModel;
		this.bodyPanel = new SittingPanel(this, this.model, this.listModel);
		addBodyPanel(bodyPanel);
		setResizable(true);
		pack();
	}
}
