package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

public class CaseSummaryDialog extends XDialog {

	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = ResourceBundleHelper.getResource(XhibitBundles.CaseSummaryResources,"dialogTitle");
	
	/*
	 *  Action Constructor (from menu)
	 */
	public CaseSummaryDialog(Frame frame, CaseSummaryModel model) throws CSRecoverableException{
		 super(frame, TITLE, true, XDialog.OKCANCEL, XDialog.DEFAULTCANCEL);		
		 initDialog(model);
	}
	
	/*
	 *  Modal Screen (called from a parent dialog)
	 */
	public CaseSummaryDialog(XDialog parent, CaseSummaryModel model) throws CSRecoverableException{
		super(parent, TITLE, true, XDialog.OKCANCEL, XDialog.DEFAULTCANCEL);
		initDialog(model);
	}
	
	private void initDialog(CaseSummaryModel model) throws CSRecoverableException {
       //Create new panel
       CaseSummaryPanel panel = new CaseSummaryPanel(this, model);
       
       OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel();
       buttonPanel.okButton.setVisible(false);
       buttonPanel.getCancelAction().populateFromBundle("Close");
       addBodyPanel(panel);
       pack();
	}	
}
