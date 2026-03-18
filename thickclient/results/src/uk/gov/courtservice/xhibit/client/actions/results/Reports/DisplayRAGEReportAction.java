package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.results.RAGE.RAGEReportPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.RAGEReport;
import uk.gov.courtservice.xhibit.common.results.vos.RAGEReportValue;

public class DisplayRAGEReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	    
    private RAGEReportPanel panel;
    private Frame parentFrame = null;
    
    public DisplayRAGEReportAction(RAGEReportPanel panel,Frame parentFrame){
        populateFromBundle("btnOk");
        this.panel = panel;
        this.parentFrame = parentFrame;
                       
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		if (panel.isInvalidEntry())
			return;
		
		ArrayList<String> selections = panel.getScreenSelections();
		Integer fromWeeks = selections.get(0)==""?null:new Integer(selections.get(0));
		Integer toWeeks = selections.get(1).isEmpty()?null:new Integer(selections.get(1));
		String bcStatus = selections.get(2);
		String caseClass = selections.get(3);
		
		if ( !"".equals(caseClass) ) {
	        RAGEReport rageReport = 
	        			XhibitDelegateHelper.getResults2Delegate().getRAGEReport(XhibitSingleton.getInstance().getCourtId(),bcStatus,caseClass,fromWeeks,toWeeks);
	        
	        
	        if (rageReport.getRageValues() != null && rageReport.getRageValues().size() > 0) {
	        	
	        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
	 	        helper.setupReportDefaults(rageReport);
	 	        
	            //Set current date
	        	rageReport.setDateOfRequest(DateFormat.getDateInstance().format(new Date()));
	          
	        	//Set from weeks
	        	rageReport.setWeekFrom(selections.get(0));
	        	
	        	//Set to weeks
	        	rageReport.setWeekTo(selections.get(1));
	        	
	        	//Set case classes
	        	rageReport.setCaseclasses(caseClass);
	        	
	        	//Set site committed
	        	rageReport.setSitecommited(((RAGEReportValue)rageReport.getRageValues().get(0)).getSitecommited());
	        	
	        	PreviewReportAction previewReportAction = new PreviewReportAction(rageReport,"config/xsl/results/reports/rage/printRAGEReport.xsl","RAGEReportPreview");
	        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	        	
	        } else {
	        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
	        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message" );
	        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
	        						errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	        }
		}
		else {
			// No Case Class specified, display error message
			String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.RAGE.alert.errorTitle");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.RAGE.alert.noClassMessage" );
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
        						errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
	}
}
