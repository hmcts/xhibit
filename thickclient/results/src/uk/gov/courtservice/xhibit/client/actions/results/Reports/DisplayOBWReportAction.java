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
import uk.gov.courtservice.xhibit.common.results.vos.OBWPrintValue;

public class DisplayOBWReportAction extends XAction implements Action {
	 private static final long serialVersionUID = 1L;
	    
    private XDatePanel priorToDateField;
    private Frame parentFrame = null;
    
    
    public DisplayOBWReportAction(XDatePanel priorToDate, Frame parentFrame){
        populateFromBundle("btnOk");
        this.priorToDateField = priorToDate;
        this.parentFrame = parentFrame;
                       
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		
		Date priorToDate = null;
		if (priorToDateField.getDate() != null) {
			priorToDate = priorToDateField.getDate().getTime();
		}
		//Call Midtier to get result set
        OBWPrintValue printValue = 
             XhibitDelegateHelper.getResults2Delegate().getOBWValues(priorToDate, XhibitSingleton.getInstance().getCourtId());
        
        DisplayActionReportHelper helper = new DisplayActionReportHelper();
        helper.setupReportDefaults(printValue);
		
		printValue.setNumRows(printValue.getOBWDefendantValues().size());
		
        if(printValue.getOBWDefendantValues().size() > 0)
        {
        	PreviewReportAction previewReportAction = new PreviewReportAction(printValue,"config/xsl/results/reports/obw/printOBW.xsl","OBWReportPreview");
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
