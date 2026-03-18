package uk.gov.courtservice.xhibit.client.listings.details;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class CaseListingDetailDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private CaseListingDetailPanel bodyPanel;
    private CaseListingDetailModel model;
    private boolean fromListScreen;

    /*
	 *  Action Constructor (from menu)
	 */
	public CaseListingDetailDialog(Frame frame, CaseListingDetailModel model) throws CSRecoverableException {
        this(frame, model, false);
	}
	
	/*
	 *  Action Constructor (from menu)
	 */
	public CaseListingDetailDialog(Frame frame, CaseListingDetailModel model, boolean fromListScreen) throws CSRecoverableException {
        super(frame, XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailTitle"), 
        		true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
        this.fromListScreen = fromListScreen;
        initDialog(model);
	}
	
	/*
	 *  Modal Screen (called from a parent dialog)
	 */
	public CaseListingDetailDialog(XDialog parent, CaseListingDetailModel model) throws CSRecoverableException {
        super(parent, XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailTitle"), 
        		true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
        this.fromListScreen = false;
        initDialog(model);
	}
	
	/**
	 * Private initialise method to setup the model and the body
	 * @param model	Model to initialise with
	 * @throws CSRecoverableException
	 */
	private void initDialog(CaseListingDetailModel model) throws CSRecoverableException {
        this.model = model;
        this.bodyPanel = new CaseListingDetailPanel(this, this.model);
        
       	addBodyPanel(bodyPanel);
       	pack();
	}

	/**
	 * @return the fromListScreen
	 */
	public boolean isFromListScreen() {
		return fromListScreen;
	}
	
	/**
	 * @return the model
	 */
	public CaseListingDetailModel getModel() {
		return model;
	}

	@Override
	public void setVisible(boolean isVisible) {
		try {
			if (isVisible && isCaseTransferredOut() && !showConfirmationMsg(
					XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCaseTransferredOutTitle"),
					XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingDetailCaseTransferredOutMessage"))) {
				super.setVisible(false);
			} else {
				super.setVisible(isVisible);
			}
		} catch (CSRecoverableException ex) {
			XHIBITConstant.handleError(ex);
		}
	}

	private boolean isCaseTransferredOut() {
		return getModel().getCaseBasicValue() != null && getModel().getCaseBasicValue().getDateTransTo() != null; 
	}
	
	private boolean showConfirmationMsg(String title, String msg) throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(this, title, true,
				XMessageBox.ICONQUESTION, msg, XMessageBox.YESNO,
				XMessageBox.DEFAULTCANCEL);
		return messageBoxReply;
	}
}
