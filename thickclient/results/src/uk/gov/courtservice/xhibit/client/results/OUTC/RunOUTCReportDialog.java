package uk.gov.courtservice.xhibit.client.results.OUTC;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunOUTCReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the OUTC report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Ervin P 
 * @version 1.0
 */

public class RunOUTCReportDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private static final String OUTC_REPORT = XhibitBundles.XhibitAdminResources;
    
    private OUTCReportPanel panel;
    private OUTCReportModel model;
    
    public RunOUTCReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(OUTC_REPORT,"OUTCReport.dialog_title"),
            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        model = new OUTCReportModel(xac);
        panel = new OUTCReportPanel(this, model);       
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
				RunOUTCReportDialog.this.requestFocusInWindow();
			}
		});
	}
    
    private void prepareButtons(){
        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 

        //OK Button 
        //buttonPanel.okButton.setAction(new DisplayOUTCReportAction(model, getParentFrame()));
        //Cancel Button
        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
    }
	
}