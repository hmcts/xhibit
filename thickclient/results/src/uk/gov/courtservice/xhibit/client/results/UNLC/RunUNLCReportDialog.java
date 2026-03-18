package uk.gov.courtservice.xhibit.client.results.UNLC;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.results.OUTC.OUTCReportModel;
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

public class RunUNLCReportDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private static final String UNLC_REPORT = XhibitBundles.XhibitAdminResources;
    
    private UNLCReportPanel panel;
    private OUTCReportModel model;
    
    public RunUNLCReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(UNLC_REPORT,"UNLCReport.dialog_title"),
            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        model = new OUTCReportModel(xac);
        panel = new UNLCReportPanel(this, model);       
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
				RunUNLCReportDialog.this.requestFocusInWindow();
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