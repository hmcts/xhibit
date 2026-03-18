package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Vector;

import uk.gov.courtservice.xhibit.client.results.NHA.PreviewPrintOnceLetterReportAction;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.NHACaseValue;
import uk.gov.courtservice.xhibit.common.results.vos.NHAReport;

public class DisplayNHAReportAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private Frame parentFrame = null;
	
	 public DisplayNHAReportAction(){
		 populateFromBundle("NHAReport");
     }
	 
	 public DisplayNHAReportAction(Frame parentFrame){
		 populateFromBundle("btnOk");
		 this.parentFrame = parentFrame;       
     }
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		//Call Midtier to get result set
        NHAReport reportList = XhibitDelegateHelper.getResults2Delegate().getNHAReport(XhibitSingleton.getInstance().getCourtId());
        
        //Set the court code here as we don't retrieve it from the database
        reportList.setCourtCode(XhibitSingleton.getInstance().getCourtBasicValue().getCourtCode());
        
        if(reportList.getNHACaseValues().size() > 0)
        {         	
        	String courtName = ((Vector<NHACaseValue>)reportList.getNHACaseValues()).get(0).getCourtName();
        	reportList.setCourtName(courtName);
        	reportList.setCourtTelephone(((Vector<NHACaseValue>)reportList.getNHACaseValues()).get(0).getCourtPhoneNumber());
        	reportList.setCourtAddressFormatted(((Vector<NHACaseValue>)reportList.getNHACaseValues()).get(0).getCourtAddress());
        	reportList.setCourtAddress(((Vector<NHACaseValue>)reportList.getNHACaseValues()).get(0).getCourtAddress().replace("\n", ""));
			reportList.setUserName(String.valueOf(XhibitSingleton.getInstance().getUserSession().getUserName()));
			
			String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "NHAReport.base");			
			URL url = DisplayNHAReportAction.class.getClassLoader().getResource(path);	
			
			PreviewPrintOnceLetterReportAction previewReportAction = new PreviewPrintOnceLetterReportAction(reportList,"config/xsl/results/reports/nha/printNHAReport.xsl","NHAReportPreview", reportList.buildMap(path,url));
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
