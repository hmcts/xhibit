package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DARTS.DARTSReportPanel;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.DARTSPrintValue;

public class DisplayDARTSReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	
	private DARTSReportPanel panel;
    private Frame parentFrame = null;
    
    
    public DisplayDARTSReportAction(DARTSReportPanel panel,Frame parentFrame){
        populateFromBundle("btnOk");
        this.panel = panel;
        this.parentFrame = parentFrame;
    }
    
    // Run Report
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		if (panel.isInvalidEntry())
			return;
		
		// Parameters
		Date startDate = panel.getStartDate().getDateComponent().getValue();
		Date endDate = panel.getEndDate().getDate()!=null ? panel.getEndDate().getDate().getTime() : null;
		
		if(endDate == null){
			endDate = new Date();
		}
		
		long difference = endDate.getTime() - startDate.getTime();
		float daysBetween = (difference / (1000*60*60*24));
		
		// Get Data
        DARTSPrintValue reportData = 
             XhibitDelegateHelper.getResults2Delegate().getDARTSReport(XhibitSingleton.getInstance().getCourtId(),startDate, endDate);
        
        // Process Data
        if (reportData.getEndDate() != null && daysBetween > 60){
        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "DARTSReport.error_title");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "DARTSReport.error_message");
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
    						errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
        }
        else if (reportData.getDARTSRetentionPolicyValues() != null && reportData.getDARTSRetentionPolicyValues().size() > 0) {
        	
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	        helper.setupReportDefaults(reportData);
        	
        	String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "DARTSReport.base");			
			URL url = DisplayDARTSReportAction.class.getClassLoader().getResource(path);
			
			PreviewReportAction previewReportAction = new PreviewReportAction(reportData,"config/xsl/results/reports/darts/printDARTSReport.xsl","DARTSReportPreview",reportData.buildMap(path,url));
        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			
        }
        else {
        	// No Data Found
        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message");
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
        					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
        }
    }
}
