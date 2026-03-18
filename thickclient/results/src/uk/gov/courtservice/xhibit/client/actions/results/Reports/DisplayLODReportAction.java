package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XSLTransformHelper;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.LODReport;

public class DisplayLODReportAction extends XAction implements Action {

	 private static final long serialVersionUID = 1L;
	    
	    private Frame parentFrame = null;
	    private Date diaryDate;
	      
	    public DisplayLODReportAction(Frame parentFrame, Date diaryDate) throws CSRecoverableException {
	    	this.parentFrame = parentFrame;
	    	this.diaryDate = diaryDate;
	    	writeReport();
	    }
	    
		@Override
	    public void xActionPerformed(ActionEvent e) throws Exception {
	     
	    }
		
		public void writeReport() throws CSRecoverableException {
			 try {
				  LODReport reportList = XhibitDelegateHelper.getResults2Delegate().getLODReport(XhibitSingleton.getInstance().getCourtId(),diaryDate);
						  																		 	
				  reportList.setCourtName(XhibitSingleton.getInstance().getCourtBasicValue().getCourtName());
				  reportList.setDiaryDate(XDateFormat.format(diaryDate,XDateFormat.DAYOFWEEKFORMAT));
			 	
			 	  if(reportList.getLodReportValues().size() > 0) {
			 		  
			 		 DisplayActionReportHelper helper = new DisplayActionReportHelper();
			 	        helper.setupReportDefaults(reportList);
			 		  
		        	String reportTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "LODReport.dialog_title");
		        	
		        	new XSLTransformHelper().transformAndPrint(reportList, "results/reports/lod/printLODReport", false, reportTitle ); 
		          
			 	  } else {
		        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
		        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message");
		        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
		        					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		         }
		      } catch (Exception e) {
		    	 throw new CSRecoverableException();
		      }
		}
}
