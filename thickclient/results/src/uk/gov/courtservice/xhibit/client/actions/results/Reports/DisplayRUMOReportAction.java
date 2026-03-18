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
import uk.gov.courtservice.xhibit.common.results.vos.RUMOReport;

public class DisplayRUMOReportAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	private Frame parentFrame = null;
	
	public DisplayRUMOReportAction(){
		 populateFromBundle("RUMOReport");
    }
	
	 public DisplayRUMOReportAction(Frame parentFrame){
        populateFromBundle("btnOk");
        this.parentFrame = parentFrame;       
    }

	@Override
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		//Call Midtier to get result set
        RUMOReport reportList = XhibitDelegateHelper.getResults2Delegate().getRUMOReport(XhibitSingleton.getInstance().getCourtId());
        
        //Set the court name here as we don't retrieve it from the database
		//reportList.setCourtName(XhibitSingleton.getInstance().getCourtBasicValue().getCourtName());
        
        if(reportList.getCollectCourtsValues().size() > 0)
        {   
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
   	        helper.setupReportDefaults(reportList);
   	        
        	reportList.setId(0);
    		
        	PreviewReportAction previewReportAction = new PreviewReportAction(reportList,"config/xsl/results/reports/rumo/printRUMO.xsl","RUMOReportPreview");
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
