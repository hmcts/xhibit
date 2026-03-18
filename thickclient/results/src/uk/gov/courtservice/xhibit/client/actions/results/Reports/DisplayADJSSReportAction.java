package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.ADJSSReportList;

public class DisplayADJSSReportAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	private Frame parentFrame = null;
	
	 public DisplayADJSSReportAction(){
		populateFromBundle("ADJSSReport");
     }
	 
	 public DisplayADJSSReportAction(Frame parentFrame){
	    populateFromBundle("btnOk");
	    this.parentFrame = parentFrame;       
	 }
	 
	 public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		   ADJSSReportList reportList = XhibitDelegateHelper.getResults2Delegate().getListOfDefendantsPutBackReport(XhibitSingleton.getInstance().getCourtId(), "SC,SE,SM,SO,SS,S", XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, "ADJSSReportActionName"));
	        if(reportList.getADJSSDefendantValues().size() > 0)
	        {
	        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
	   	        helper.setupReportDefaults(reportList);
	   	        reportList.setShortReportCode("ADJSS");
	        
	   	        PreviewReportAction previewReportAction = new PreviewReportAction(reportList,"config/xsl/results/reports/adjss/printADJSSReport.xsl","ADJSSReportPreview");
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
	    
 /* public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
	    if (dialog != null)
	      dialog.setVisible(true);
	 }

	@Override
	public void xActionPerformed(ActionEvent e) throws Exception {
		//Call Midtier to get result set
        ADJSSReportList reportList = XhibitDelegateHelper.getResults2Delegate().getListOfDefendantsPutBackReport(XhibitSingleton.getInstance().getCourtId(), "SC,SE,SM,SO,SS,S", XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, "ADJSSReportActionName"));
        if(reportList.getADJSSDefendantValues().size() > 0)
        {
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
   	        helper.setupReportDefaults(reportList);
   	        reportList.setShortReportCode("ADJSS");
        	
        	//Set the court name here as we don't retrieve it from the database
        	reportList.setCourtName(XhibitSingleton.getInstance().getCourtBasicValue().getCourtName());
        	
        	//Create and display the DOCAR Display Dialog
        	String reportDialogTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "ADJSSReport.dialog_title");
        	DisplayReportDialog dlg = new DisplayReportDialog(parentFrame,reportList, reportDialogTitle, "results/reports/adjss/printADJSSReport" );
        	dlg.setVisible(true);   
        }
        else
        {
        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message");
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
        			errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
        }
		
	}*/
}
