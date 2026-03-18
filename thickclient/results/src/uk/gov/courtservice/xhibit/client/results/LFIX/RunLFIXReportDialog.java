package uk.gov.courtservice.xhibit.client.results.LFIX;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayLFIXReportAction;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunLFIXReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the LFIX report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Gurinder Brar
 * @version 1.0
 */

public class RunLFIXReportDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private static final String LFIX_REPORT = XhibitBundles.XhibitAdminResources;
    
    private LFIXReportPanel panel;
    
    public RunLFIXReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(LFIX_REPORT,"LFIXReport.dialog_title"),
            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        panel = new LFIXReportPanel(this);
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
				RunLFIXReportDialog.this.requestFocusInWindow();
			}
		});
	}
    
    private void prepareButtons() throws CSValidationException, CSRecoverableException{
        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
        //OK Button 
        buttonPanel.okButton.setAction(new DisplayLFIXReportAction(panel,getParentFrame()));
        //Cancel Button
        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
    }
	
}
