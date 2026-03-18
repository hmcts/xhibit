package uk.gov.courtservice.xhibit.client.results.PRLIS;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayPRLISReportAction;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class RunPRLISReportDialog extends XDialog {
	private static final long serialVersionUID = 1L;
    
    private PRLISReportPanel panel;
    
    public RunPRLISReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,"PRLISReport.dialog_title"),
            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        panel = new PRLISReportPanel();
        prepareButtons();
        addBodyPanel(panel);
    
        pack();
    }
    
    private void prepareButtons(){
        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
        buttonPanel.okButton.setAction(new DisplayPRLISReportAction(getParentFrame(), panel.getPublishedListComboBox(), panel.getMandatoryErrorMessage(), panel.getPublishAction()));
        //Cancel Button
        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
    }
	
}
