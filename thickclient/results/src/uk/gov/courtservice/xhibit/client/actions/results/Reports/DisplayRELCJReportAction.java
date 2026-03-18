package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.RELCJReport;

public class DisplayRELCJReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	private Frame parentFrame = null;
	
	 public DisplayRELCJReportAction(Frame parentFrame){
        populateFromBundle("btnOk");
        this.parentFrame = parentFrame;       
    }

	@Override
	public void xActionPerformed(ActionEvent e) throws Exception {
		//Call Midtier to get result set
        RELCJReport relcjReport = XhibitDelegateHelper.getResults2Delegate().getRELCJReport(XhibitSingleton.getInstance().getCourtId());
      
	        if(relcjReport.getRelcjValues().size() > 0)
	        {         	
	        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
	 	        helper.setupReportDefaults(relcjReport);
	 	      
	 	        PreviewReportAction previewReportAction = new PreviewReportAction(relcjReport,"config/xsl/results/reports/relcj/printRELCJReport.xsl","RELCJReportPreview");
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

