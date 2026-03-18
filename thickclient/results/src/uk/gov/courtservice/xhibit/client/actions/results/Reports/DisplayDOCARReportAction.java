package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.DOCARPrintValue;

public class DisplayDOCARReportAction extends XAction implements Action {
	 private static final long serialVersionUID = 1L;
	    
    private XDatePanel priorToDateField;
    private Frame parentFrame = null;
    
    
    public DisplayDOCARReportAction(XDatePanel priorToDate, Frame parentFrame){
        populateFromBundle("btnOk");
        this.priorToDateField = priorToDate;
        this.parentFrame = parentFrame;
                       
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		
		Calendar priorToDate = priorToDateField.getDate();
		//Call Midtier to get result set
        DOCARPrintValue printValue = 
             XhibitDelegateHelper.getResults2Delegate().getDOCARValues(priorToDate.getTime(), XhibitSingleton.getInstance().getCourtId());
       
        if(printValue.getDOCARDefendantValues().size() > 0)
        {
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	        helper.setupReportDefaults(printValue);
 	        
 	       PreviewReportAction previewDailyListAction = new PreviewReportAction(printValue,"config/xsl/results/reports/docar/printDOCAR.xsl","DOCARReportPreview");
 	       previewDailyListAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        	
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
