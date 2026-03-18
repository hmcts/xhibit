package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import uk.gov.courtservice.xhibit.client.results.NHA.PreviewPrintOnceLetterReportAction;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.NFIXFixtureValue;
import uk.gov.courtservice.xhibit.common.results.vos.NFIXReport;

public class DisplayNFIXReportAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private Frame parentFrame = null;
	
	 public DisplayNFIXReportAction(){
        populateFromBundle("NFIXReport");
     }
 	 
	 public DisplayNFIXReportAction(Frame parentFrame) {
	    populateFromBundle("btnOk");
	    this.parentFrame = parentFrame;       
	 }
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		//Call Midtier to get result set
        NFIXReport reportList = XhibitDelegateHelper.getResults2Delegate().getNFIXReport(XhibitSingleton.getInstance().getCourtId());
        if(reportList.getNfixFixtureValues().size() > 0)
        {         	
        	String courtName = ((List<NFIXFixtureValue>)reportList.getNfixFixtureValues()).get(0).getCourtName();
        	reportList.setCourtName(courtName);
        	reportList.setCourtTelephone(((List<NFIXFixtureValue>)reportList.getNfixFixtureValues()).get(0).getCourtTelephone());
        	reportList.setCourtAddress(((List<NFIXFixtureValue>)reportList.getNfixFixtureValues()).get(0).getCourtAddress());
			reportList.setUserName(String.valueOf(XhibitSingleton.getInstance().getUserSession().getUserName()));
        	
			String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "NFIXReport.base");			
			URL url = DisplayNFIXReportAction.class.getClassLoader().getResource(path);
			
			PreviewPrintOnceLetterReportAction previewReportAction = new PreviewPrintOnceLetterReportAction(reportList,"config/xsl/results/reports/nfix/printNFIXReport.xsl","NFIXReportPreview", reportList.buildMap(path,url));
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
