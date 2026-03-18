package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.CFIXReport;


public class DisplayCFIXReportAction extends XAction implements Action {
	 private static final long serialVersionUID = 1L;
	    
	    private XDatePanel priorToDateField;
	    private XDatePanel endDateField;
	    private Frame parentFrame = null;
	    
	    public DisplayCFIXReportAction(XDatePanel priorToDate,XDatePanel endDateField, Frame parentFrame){
	        populateFromBundle("btnOk");
	        this.priorToDateField = priorToDate;
	        this.endDateField = endDateField;
	        this.parentFrame = parentFrame;

}
	    @Override
	    public void xActionPerformed(ActionEvent e) throws Exception {			
			Date priorToDate = priorToDateField.getDate().getTime();
			Date endDate = !"".equals(endDateField.getText())  ? endDateField.getDate().getTime() : null;
			//Call Midtier to get result set
			  CFIXReport cfixReport =
	               XhibitDelegateHelper.getResults2Delegate().getCFIXReport(XhibitSingleton.getInstance().getCourtId(), priorToDate,endDate);
	       
			  if(cfixReport.getHearingValues().size() > 0) 
	        {
				DisplayActionReportHelper helper = new DisplayActionReportHelper();
				helper.setupReportDefaults(cfixReport);
				
				PreviewReportAction previewReportAction = new PreviewReportAction(cfixReport,"config/xsl/results/reports/cfix/printCFIXReport.xsl","CFIXReportPreview");
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
