package uk.gov.courtservice.xhibit.client.results.DOCAR;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayDOCARReportAction;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunDOCARReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the DOCAR report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author David Burden
 * @version 1.0
 */

public class RunDOCARReportDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private static final String DOCAR_REPORT = XhibitBundles.XhibitAdminResources;
    
    private DOCARReportPanel panel;
    
    public RunDOCARReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(DOCAR_REPORT,"RunDOCARReport.dialog_title"),
            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        panel = new DOCARReportPanel(this);
        prepareButtons();
        addBodyPanel(panel);
        makeFocusable();
    
        pack();
        
        //disable OK button after pack() so it stays disabled
        getButtonPanel().okButton.setEnabled(false);
    }

	private void makeFocusable(){
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e)
			{
				RunDOCARReportDialog.this.requestFocusInWindow();
			}
		});
	}
    
    private void prepareButtons(){
        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
        //OK Button 
        buttonPanel.okButton.setAction(new DisplayDOCARReportAction(panel.getPriorToDateField(), getParentFrame()));
        //Cancel Button
        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
    }
	
}
