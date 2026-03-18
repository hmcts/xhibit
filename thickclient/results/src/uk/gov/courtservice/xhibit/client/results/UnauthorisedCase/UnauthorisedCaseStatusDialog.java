package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: UnauthorisedCaseStatusDialog
 * </p>
 * <p>
 * Description: The Dialog which displays Unauthorised Cases
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class UnauthorisedCaseStatusDialog extends XDialog{
    private static final long serialVersionUID = 1L;

    private static final String UNAUTH_CASE_STAT = XhibitBundles.XhibitAdminResources;
    
    private UnauthorisedCaseStatusPanel panel;
    
    public UnauthorisedCaseStatusDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(UNAUTH_CASE_STAT,"UnauthorisedCaseStatus.dialog_title"),
            true,XDialog.APPLYOKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        panel = new UnauthorisedCaseStatusPanel(this);
        prepareButtons();
        addBodyPanel(panel);
    
        pack();
    }
    
    private void prepareButtons(){
        ApplyOkCancelPanel buttonPanel = (ApplyOkCancelPanel) getButtonPanel();        
        //OK Button becomes 'Report'
        buttonPanel.okButton.setAction(new UnauthorisedCaseStatusReportAction(panel));
        //Apply Button becomes 'Select'
        buttonPanel.applyButton.setAction(new UnauthorisedCaseStatusSelectAction(panel));
        //Cancel Button becomes 'Close'
        buttonPanel.getCancelAction().populateFromBundle("Close");
    }
}
