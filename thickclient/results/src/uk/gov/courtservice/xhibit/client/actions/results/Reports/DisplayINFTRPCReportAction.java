package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.actions.results.INFTRPC.INFTRPCModel;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCCaseNumReport;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCReport;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;


public class DisplayINFTRPCReportAction extends XAction implements Action {
	 private static final long serialVersionUID = 1L;
	    

	 private Frame parentFrame = null;
	
     private INFTRPCModel model;
     protected MonthYearDatePeriodReportModel model1;
        
    public DisplayINFTRPCReportAction(INFTRPCModel model, Frame parentFrame){
        this.parentFrame = parentFrame;
        this.model = model;  
    } 
    
    
    @Override
    public void xActionPerformed(ActionEvent e) throws Exception {
    	
    	//Call Midtier to get result set
    	if (model.getCountsPercentageReport().equals(model.COUNTS_PERCENTAGE_REPORT)) {
    		// Run INFTRPC report
    		runINFTRPCReport();
    	} else if (model.getCaseNumbersReport().equals(model.CASENUMBER_DETAILS_REPORT)) {
    		// Run INFTRPCCaseNumReport report
    		runINFTRPCCaseNumReport();	
    	}     
    }   
    	
    	
    private void runINFTRPCReport() throws Exception {
    	String monthPeriod = model.getMonthPeriod() != null ? model.getMonthPeriod(): null;   	
   	    String yearPeriod = model.getYearPeriod() != null ? model.getYearPeriod(): null;
    	
    	INFTRPCReport inftrpcReport = 
   			 XhibitDelegateHelper.getResults2Delegate().getINFTRPCReport( XhibitSingleton.getInstance().getCourtId(), monthPeriod, yearPeriod);
    	
    	DisplayActionReportHelper helper = new DisplayActionReportHelper();
	    helper.setupReportDefaults(inftrpcReport);
    	
    	inftrpcReport.setId(0);
    	runReport(inftrpcReport, inftrpcReport.getInftrpcMainValues().size(), "INFTRPCReportPreview", "config/xsl/results/reports/inftrpc/printINFTRPCReport.xsl");
    }
    
    private void runINFTRPCCaseNumReport() throws Exception {
    	String monthPeriod = model.getMonthPeriod() != null ? model.getMonthPeriod(): null;
    	String yearPeriod = model.getYearPeriod() != null ? model.getYearPeriod(): null;
    
    	INFTRPCCaseNumReport inftrpcCaseNumReport = 
   			 XhibitDelegateHelper.getResults2Delegate().getINFTRPCCaseNumReport( XhibitSingleton.getInstance().getCourtId(), monthPeriod, yearPeriod);
    	//Set the court name here as we don't retrieve it from the database
    	inftrpcCaseNumReport.setCourtName(XhibitSingleton.getInstance().getCourtBasicValue().getCourtName());
    	inftrpcCaseNumReport.setUserName(XhibitSingleton.getInstance().getUserSession().getUserName());
    	//Set time of report to now.
    	inftrpcCaseNumReport.setTimeOfReport(XDateFormat.format(Calendar.getInstance(), XDateFormat.TIMEFORMAT));	
		//Set date of report to today.
    	inftrpcCaseNumReport.setDateOfReport(DateFormat.getDateInstance().format(new Date()));
    	inftrpcCaseNumReport.setId(0);
    	runReport(inftrpcCaseNumReport, inftrpcCaseNumReport.getCaseDescriptionValues().size(), "INFTRPCReportPreview", "config/xsl/results/reports/inftrpcsummary/printINFTRPCCaseNumReport.xsl");
    }
    
    private void runReport(ReportAbsttractValue report, int size, String titleKey, String xslLocation) throws Exception {
    	if(size > 0)	{
    		PreviewReportAction previewReportAction = new PreviewReportAction(report, xslLocation, titleKey);
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