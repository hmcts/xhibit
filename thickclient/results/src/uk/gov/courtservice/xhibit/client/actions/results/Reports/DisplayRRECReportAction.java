package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.results.RREC.RRECReportPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.RRECReport;

public class DisplayRRECReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	    
    private RRECReportPanel panel;
    private Frame parentFrame = null;
    
    public DisplayRRECReportAction(RRECReportPanel panel,Frame parentFrame){
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
		
        RRECReport rrecReport1 =
        					XhibitDelegateHelper.getResults2Delegate().getRRECReportSummary(XhibitSingleton.getInstance().getCourtId(),date);
        			
        RRECReport rrecReport2 =
        					XhibitDelegateHelper.getResults2Delegate().getRRECReportDetail(rrecReport1,XhibitSingleton.getInstance().getCourtId(),date);
         
        if (rrecReport2.getRrecSummaryDetail() != null &&  rrecReport2.getRrecSummaryDetail().size() > 0) {
        	
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
   	        helper.setupReportDefaults(rrecReport2);
        	
        	//Set ending date
        	rrecReport2.setWeekmonthdate(weekmonthdate);
        	
        	PreviewReportAction previewReportAction = new PreviewReportAction(rrecReport2,"config/xsl/results/reports/rrec/printRRECReport.xsl","RRECReportPreview");
        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        	
        } else {
        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message" );
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
        						errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
        }
	}

	public RRECReportPanel getPanel() {
		return panel;
	}

	public void setPanel(RRECReportPanel panel) {
		this.panel = panel;
	}
}
