package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.DRSRReport;

public class DisplayDRSRReportAction extends XAction implements Action {
	 private static final long serialVersionUID = 1L;
	    

	    private Frame parentFrame = null;
	    private MonthYearDatePeriodReportModel model;
	        
	    public DisplayDRSRReportAction(MonthYearDatePeriodReportModel model, Frame parentFrame){
	        this.parentFrame = parentFrame;
	        this.model = model;  
	    } 
	    @Override
	    public void xActionPerformed(ActionEvent e) throws Exception {
	    	String monthPeriod = model.getMonthPeriod() != null ? model.getMonthPeriod(): null;
	    	String yearPeriod = model.getYearPeriod() != null ? model.getYearPeriod(): null;
	    	
	    	
	    	//Call Midtier to get result set
	    	DRSRReport drsrReport = 
	    			 XhibitDelegateHelper.getResults2Delegate().getDRSRReport( XhibitSingleton.getInstance().getCourtId(), monthPeriod, yearPeriod);
	    	
	    	 if(drsrReport.getDrsrReportValues().size() > 0)
	        {
	        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
	 	        helper.setupReportDefaults(drsrReport);
	        	
	        	//Create and display the OUTC Display Dialog
	 	        PreviewReportAction previewReportAction = new PreviewReportAction(drsrReport,"config/xsl/results/reports/drsr/printDRSRReport.xsl","DRSRReportPreview");
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

