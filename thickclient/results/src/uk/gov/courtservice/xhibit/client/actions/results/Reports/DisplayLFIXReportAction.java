package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.results.LFIX.LFIXReportPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXReport;

public class DisplayLFIXReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	
	private LFIXReportPanel panel;
    private Frame parentFrame = null;
    
    
    public DisplayLFIXReportAction(LFIXReportPanel panel,Frame parentFrame){
        populateFromBundle("btnOk");
        this.panel = panel;
        this.parentFrame = parentFrame;
                       
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		Date runDate = panel.getRunDate();
		
        LFIXReport lfixReport = 
             XhibitDelegateHelper.getResults2Delegate().getLFIXReport(XhibitSingleton.getInstance().getCourtId(),runDate);
        
        if (lfixReport.getLfixReportValues() != null && lfixReport.getLfixReportValues().size() > 0) {
        	if ( !panel.isRunAgain() ) {
        		// Only update the case diary fixture records if not running the report again for a previous date
        		XhibitDelegateHelper.getResults2Delegate().updateLFIXCaseDiaryFixture(lfixReport.getFixtureList());
        	}
        	
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	        helper.setupReportDefaults(lfixReport);
        	
        	String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "LFIXReport.base");			
			URL url = DisplayLFIXReportAction.class.getClassLoader().getResource(path);
			
			PreviewReportAction previewReportAction = new PreviewReportAction(lfixReport,"config/xsl/results/reports/lfix/printLFIXReport.xsl","LFIXReportPreview",lfixReport.buildMap(path,url));
        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			
        } else {
        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message");
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
        					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
        }
    }
}
