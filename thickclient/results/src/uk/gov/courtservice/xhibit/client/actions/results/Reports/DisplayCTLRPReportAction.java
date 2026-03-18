package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.Action;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.results.CTLRP.CTLRPReportPanel;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRLReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPExReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPReport;

public class DisplayCTLRPReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	    
    private CTLRPReportPanel panel;
    private Frame parentFrame = null;
    
    public DisplayCTLRPReportAction(CTLRPReportPanel panel,Frame parentFrame){
        populateFromBundle("btnOk");
        this.panel = panel;
        this.parentFrame = parentFrame;
                       
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		DisplayActionReportHelper helper = new DisplayActionReportHelper();
		
		if (panel.isInvalidEntry())
			return;
		
		// Generate CTLRP report
        CTLRPReport ctlrpReport = 
             XhibitDelegateHelper.getResults2Delegate().getCTLRPReport(XhibitSingleton.getInstance().getCourtId(),panel.getLimitDate().getDate().getTime());

        helper.setupReportDefaults(ctlrpReport);

        //Set expiry date
        ctlrpReport.setInputDate((XDateFormat.format(panel.getLimitDate().getDate().getTime(),XDateFormat.DAYOFWEEKFORMAT)));
        
        PreviewReportAction previewReportAction = new PreviewReportAction(ctlrpReport,"config/xsl/results/reports/ctlrp/printCTLRPReport.xsl","CTLRPReportPreview");
    	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    
    	// Print Exceptions Report
    	if (panel.getChkExceptionsReport().isSelected()) {
        	printExceptionsReport(helper);
        }

    	// Generate CTLRL letters
        if (panel.getChkPrintReminder().isSelected()) {
        	printReminderLetter(helper);
        }
        
	}

	private void printReminderLetter(DisplayActionReportHelper helper) throws Exception {
		CTLRLReport ctlrlReport = XhibitDelegateHelper.getResults2Delegate().getCTLRLReport(XhibitSingleton.getInstance().getCourtId(),panel.getLimitDate().getDate().getTime());
		ArrayList ctlrlCaseData = ctlrlReport.getCases();
		
		if (ctlrlCaseData != null && ctlrlCaseData.size() > 0) {
			String time = XDateFormat.format(Calendar.getInstance(), XDateFormat.TIMEFORMAT);
			
			helper.setupReportDefaults(ctlrlReport);
			ctlrlReport.setTime(time);
			
			String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "CTLRPReport.base");			
			URL url = DisplayCTLRPReportAction.class.getClassLoader().getResource(path);
	        
	        PreviewReportAction previewReportAction2 = new PreviewReportAction(ctlrlReport,"config/xsl/results/reports/ctlrl/printCTLRLReport.xsl","CTLRLReport", ctlrlReport.buildMap(path,url));
        	previewReportAction2.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	        	
	        XhibitDelegateHelper.getResults2Delegate().updateCTLRLCaseReminderPrinted(ctlrlReport.getCasesList());
	       
		} else {
	        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
	        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "CTLRLReport.dialog_letter_already_printed_message" );
	        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
	        						errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	       
		}
	}

	private void printExceptionsReport(DisplayActionReportHelper helper) throws Exception {
		CTLRPExReport ctlrpexReport = 
	             XhibitDelegateHelper.getResults2Delegate().getCTLRPExReport(XhibitSingleton.getInstance().getCourtId());

	        helper.setupReportDefaults(ctlrpexReport);

	        //Set expiry date
	        ctlrpexReport.setInputDate((XDateFormat.format(panel.getLimitDate().getDate().getTime(),XDateFormat.DAYOFWEEKFORMAT)));
	        
	        PreviewReportAction previewReportAction = new PreviewReportAction(ctlrpexReport,"config/xsl/results/reports/ctlrpex/printCTLRPExReport.xsl","CTLRPEXReportPreview");
	    	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	}
}
