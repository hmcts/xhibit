package uk.gov.courtservice.xhibit.client.results.authorise;
import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class DisplayRecSheetDialog extends XDialog {

	private static final long serialVersionUID = 1L;
	
	private DisplayRecSheetPanel panel;
	private ApplyOkCancelPanel buttonPanel;

	public DisplayRecSheetDialog(Frame xhibitApplicationController,  String dialogTitle, String xml) throws CSRecoverableException{
        super(xhibitApplicationController,dialogTitle, true,XDialog.APPLYOKCANCEL,XDialog.DEFAULTCANCEL);
        
        panel = new DisplayRecSheetPanel(xml);
        buttonPanel = (ApplyOkCancelPanel) getButtonPanel();
        buttonPanel.okButton.setVisible(false);
        buttonPanel.applyButton.setVisible(false);
        buttonPanel.getCancelAction().populateFromBundle("Close");
        addBodyPanel(panel);
        pack();  
	}
}
