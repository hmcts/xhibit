package uk.gov.courtservice.xhibit.client.results.DARTS;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayDARTSReportAction;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunDARTSReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the DARTS report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2021
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Luke Gittins
 * @version 1.0
 */

public class RunDARTSReportDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private static final String DARTS_REPORT = XhibitBundles.XhibitAdminResources;
    
    private DARTSReportPanel panel;
    
    public RunDARTSReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(DARTS_REPORT,"DARTSReport.dialog_title"),
            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        panel = new DARTSReportPanel(this);
        prepareButtons();
        addBodyPanel(panel);
        makeFocusable();
    
        pack();
    }

	private void makeFocusable(){
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e)
			{
				RunDARTSReportDialog.this.requestFocusInWindow();
			}
		});
	}
    
    private void prepareButtons() throws CSValidationException, CSRecoverableException{
        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
        //OK Button 
        buttonPanel.okButton.setAction(new DisplayDARTSReportAction(panel,getParentFrame()));
        //Cancel Button
        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
    }
	
}
