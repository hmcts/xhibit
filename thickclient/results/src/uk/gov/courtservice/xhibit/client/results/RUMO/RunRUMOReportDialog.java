package uk.gov.courtservice.xhibit.client.results.RUMO;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayRUMOReportAction;
import uk.gov.courtservice.xhibit.client.results.ADJSS.RunReportWithNoParametersPanel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunRUMOReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the RUMO report
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

public class RunRUMOReportDialog extends XDialog {

    private static final long serialVersionUID = 1L;
    
    private RunReportWithNoParametersPanel panel;
    
    public RunRUMOReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,"RUMOReport.dialog_title"),
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
        buttonPanel.okButton.setAction(new DisplayRUMOReportAction(getParentFrame()));
        //Cancel Button
        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
    }
}
