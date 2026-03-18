package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.results.RSIT.RSITReportPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.RSITReport;

public class DisplayRSITReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	    
    private RSITReportPanel panel;
    private Frame parentFrame = null;
    
    public DisplayRSITReportAction(RSITReportPanel panel,Frame parentFrame){
        populateFromBundle("btnOk");
        this.setPanel(panel);
        this.parentFrame = parentFrame;
                       
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		
		if (panel.getInvalidEntry())
        	return;
		
		Date date = panel.getDateToBeUsed();
		String weekmonthdate = panel.getReportWeekMonthEnding();
		Integer courtSiteId = new Integer((("0".equals(panel.getCourtSiteSelection())?"0":panel.getCourtSiteSelection())));
		
        RSITReport rsitReport = 
        					XhibitDelegateHelper.getResults2Delegate().getRSITReport(XhibitSingleton.getInstance().getCourtId(),courtSiteId,date);
        			
        if (rsitReport != null &&  rsitReport.getRsitValues().size() > 0) {
        	
        	// Retrieve defaulst such as court name, user name, report date and report time
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	        helper.setupReportDefaults(rsitReport);
        	
        	//Set ending date
        	rsitReport.setWeekdate(weekmonthdate);
            
        	PreviewReportAction previewReportAction = new PreviewReportAction(rsitReport,"config/xsl/results/reports/rsit/printRSITReport.xsl","RSITReportPreview");
        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        	
        } else {
        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message" );
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
        						errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
        }
	}

	public RSITReportPanel getPanel() {
		return panel;
	}

	public void setPanel(RSITReportPanel panel) {
		this.panel = panel;
	}
}
