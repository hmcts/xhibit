package uk.gov.courtservice.xhibit.client.listings.createlist;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.createlist.CreateListModel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class CreateListDialog extends XDialog {
	
	private static final long serialVersionUID = 1L;
	
	private CreateListPanel bodyPanel;

	/**
	 *  Action Constructor (from menu)
	 */
	public CreateListDialog(Frame frame, CreateListModel model) throws CSRecoverableException {
		super(frame, XHIBITConstant.getResource(XhibitBundles.Listings, "createListTitle"), 
				true, XDialog.OKCANCEL, XDialog.DEFAULTOK);
		initDialog(model);
	}
	
	/**
	 *  Modal Screen (called from a parent dialog)
	 */
	public CreateListDialog(XDialog parent, CreateListModel model) throws CSRecoverableException {
		super(parent, XHIBITConstant.getResource(XhibitBundles.Listings, "createListTitle"), 
				true, XDialog.OKCANCEL, XDialog.DEFAULTOK);
		initDialog(model);
	}
	
	@Override
	protected OkCancelPanel createButtonPanel(int panelType, int defaultButton){
		OkCancelPanel buttonPanel = super.createButtonPanel(panelType, defaultButton);
		buttonPanel.okButton.setText(XHIBITConstant.getResource(XhibitBundles.Listings, "createListContinue"));
		buttonPanel.okButton.setToolTipText(buttonPanel.okButton.getText());
		return buttonPanel;
	}
	
	/**
	 * Common code used to initialise the Dialog
	 * @param model	Model to setup the Panel
	 * @throws CSRecoverableException
	 */
	private void initDialog(CreateListModel model) throws CSRecoverableException {
       //Create new panel
        this.bodyPanel = new CreateListPanel(this, model);
        addBodyPanel(bodyPanel);
        setResizable(true);
        pack();
	}
}