package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.Action;
import javax.swing.JLabel;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.business.entities.xhb_pub_running_list.XhbPubRunningListBasicValue;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.results.PRLIS.PRLISReportPanel.PublishPRLISAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.PRLISReport;

public class DisplayPRLISReportAction extends XAction implements Action {
	 private static final long serialVersionUID = 1L;
	    
    private Frame parentFrame = null;
	private XComboBox publishedListComboBox;

	private JLabel mandatoryErrorLabel;

	private PublishPRLISAction publishAction;
    
    
    public DisplayPRLISReportAction(Frame parentFrame, XComboBox publishedListComboBox, JLabel mandatoryErrorLabel, PublishPRLISAction refreshAction){
        populateFromBundle("btnOk");
        this.parentFrame = parentFrame;       
        this.publishedListComboBox = publishedListComboBox;
        this.mandatoryErrorLabel = mandatoryErrorLabel;
        this.publishAction = refreshAction;
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		
		if(this.publishedListComboBox.isEnabled() && this.publishedListComboBox.getSelectedIndex() < 0){
			this.mandatoryErrorLabel.setText(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "PRLISReport.missing_previous_list_selection"));
		}else{		
			Integer selectedReportId = 0;
			String dateofReport = XDateFormat.format(Calendar.getInstance(), XDateFormat.DATEFORMAT);
			if(publishedListComboBox.isEnabled() && publishedListComboBox.getSelectedItem() != null)
			{
				XhbPubRunningListBasicValue selectedPublishedList = (XhbPubRunningListBasicValue) publishedListComboBox.getSelectedItem();
				selectedReportId = selectedPublishedList.getPubRunningListId();
				dateofReport = XDateFormat.format(selectedPublishedList.getPublishedDate(), XDateFormat.DATEFORMAT);
			}
			
			//Call Midtier to get result set
	        PRLISReport printValue = 
	             XhibitDelegateHelper.getResults2Delegate().getPRLISReport( XhibitSingleton.getInstance().getCourtId(), selectedReportId);
	        printValue.setDateofReport(dateofReport);
	        
	        DisplayActionReportHelper helper = new DisplayActionReportHelper();
	        helper.setupReportDefaults(printValue);
			
	        if(printValue.getPrlisCaseValues().size() > 0)
	        {
	        	//If no previous report has been selected to re-run, we do not want to allow the users to re-publish so just display the normal print preview
	        	publishAction.setEnabled(selectedReportId <= 0);
	        	publishAction.setReportList(printValue);
	        	PreviewReportAction previewReportAction = new PreviewReportAction(printValue,"config/xsl/results/reports/prlis/printPRLISReport.xsl","PRLISReportPreview", publishAction);
	        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	        	
	        }
	        else
	        {
	        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
	        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message");
	        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
	        					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	        }
		}
    }  
	
}
