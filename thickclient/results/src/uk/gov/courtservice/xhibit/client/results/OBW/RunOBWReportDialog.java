package uk.gov.courtservice.xhibit.client.results.OBW;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayOBWReportAction;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunOBWReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the DOCAR report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */

public class RunOBWReportDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private static final String OBW_REPORT = XhibitBundles.XhibitAdminResources;
    
    private OBWReportPanel panel;
    
    public RunOBWReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(OBW_REPORT,"OBWReport.dialog_title"),
            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        panel = new OBWReportPanel(this);
        prepareButtons();
        addBodyPanel(panel);
        makeFocusable();
    
        pack();
        
        //disable OK button after pack() so it stays disabled
        getButtonPanel().okButton.setEnabled(true);
    }

	private void makeFocusable(){
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e)
			{
				RunOBWReportDialog.this.requestFocusInWindow();
			}
		});
	}
    
    private void prepareButtons(){
        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
        //OK Button 
        buttonPanel.okButton.setAction(new DisplayOBWReportAction(panel.getPriorToDateField(), getParentFrame()));
        //Cancel Button
        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
    }
	
}
