package uk.gov.courtservice.xhibit.client.results.ADJSS;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayADJSSReportAction;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunADJSSReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the ADJSS report
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

public class RunADJSSReportDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private static final String DOCAR_REPORT = XhibitBundles.XhibitAdminResources;
    
    private RunReportWithNoParametersPanel panel;
    
    public RunADJSSReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(DOCAR_REPORT,"ADJSSReport.dialog_title"),
            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        panel = new RunReportWithNoParametersPanel();
        prepareButtons();
        addBodyPanel(panel);
    
        pack();
    }
    
    private void prepareButtons(){
        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
        //OK Button
        buttonPanel.okButton.setAction(new DisplayADJSSReportAction(getParentFrame()));
        //Cancel Button
        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
    }
	
}
