package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.results.NTRSF.NTRSFReportPanel;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XSLTransformHelper;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.NTRSFReport;
import uk.gov.courtservice.xhibit.common.results.vos.NTRSFReportValue;


public class DisplayNTRSFReportAction extends SynchXAction {
	private static final long serialVersionUID = 1L;
	
	private NTRSFReportPanel panel;
    private Frame parentFrame = null;
    private Integer caseId;
    
    public DisplayNTRSFReportAction(){
    	populateFromBundle("NTRSFReport");
     }
    
    public DisplayNTRSFReportAction(NTRSFReportPanel panel,Frame parentFrame){
        populateFromBundle("btnOk");
        this.panel = panel;
        this.parentFrame = parentFrame;
                       
    }
    
    public DisplayNTRSFReportAction(Frame parentFrame, Integer caseId) throws Exception {
    	this.parentFrame = parentFrame;
    	this.caseId = caseId;
    	writeReport(true);
    } 
    
    @SuppressWarnings("unchecked")
	public void writeReport(Boolean autoPrint) throws Exception {
		Integer caseID;

		if (panel!=null) {
			caseID = panel.getCaseId();
		} else {
			caseID = caseId;
		}
		
		NTRSFReport ntrsfReport = 
				XhibitDelegateHelper.getResults2Delegate().getNTRSFReport(XhibitSingleton.getInstance().getCourtId(),caseID);
		
		if (ntrsfReport.getNtrsfValues() != null && ntrsfReport.getNtrsfValues().size() > 0) {
			String courtName = ((ArrayList<NTRSFReportValue>)ntrsfReport.getNtrsfValues()).get(0).getCourtfrom();
			
			ntrsfReport.setCourtName(courtName);
			ntrsfReport.setDateOfReport((XDateFormat.format(new Date(),XDateFormat.DAYOFWEEKFORMAT)));
			ntrsfReport.setCourttoaddress(((ArrayList<NTRSFReportValue>)ntrsfReport.getNtrsfValues()).get(0).getCourttoaddress());
			ntrsfReport.setCourtAddress(((ArrayList<NTRSFReportValue>)ntrsfReport.getNtrsfValues()).get(0).getCourtfromaddress());
			ntrsfReport.setCourtTelephone(((ArrayList<NTRSFReportValue>)ntrsfReport.getNtrsfValues()).get(0).getCourtfromtelephoneno());
			ntrsfReport.setUserName(String.valueOf(XhibitSingleton.getInstance().getUserSession().getUserName()));
			
			String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "NTRSFReport.base");			
			URL url = DisplayNTRSFReportAction.class.getClassLoader().getResource(path);
			
			String reportDialogTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "NTRSFReport.dialog_title");
			String reportLocation = "results/reports/ntrsf/printNTRSFReport";
			
			if (autoPrint) {
				new XSLTransformHelper().transformAndPrint(ntrsfReport, reportLocation, false, reportDialogTitle,ntrsfReport.buildMap(path,url)); 
			} else {
				PreviewReportAction previewReportAction = new PreviewReportAction(ntrsfReport,"config/xsl/results/reports/ntrsf/printNTRSFReport.xsl","NTRSFReportPreview", ntrsfReport.buildMap(path,url));
				previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			}
		} else {
			String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
			String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message" );
			XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
   						errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
	}

	/*@Override
	public void xActionPerformed(ActionEvent e) throws Exception {
		writeReport(false);	
	}*/
	
	public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		writeReport(false);	
   }
}
