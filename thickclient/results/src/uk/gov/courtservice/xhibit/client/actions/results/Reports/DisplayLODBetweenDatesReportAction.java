package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.results.LOD.LODReportPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.LODReport;

public class DisplayLODBetweenDatesReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	    
    private LODReportPanel panel;
    private Frame parentFrame = null;
    
    
    public DisplayLODBetweenDatesReportAction(LODReportPanel panel,Frame parentFrame){
        populateFromBundle("btnOk");
        this.panel = panel;
        this.parentFrame = parentFrame;
                       
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		//Call Midtier to get result set
		
		if (panel.isInvalidEntry())
			return;
		
		Date fromDate = panel.getStartDate().getDate().getTime();
		Date toDate =  panel.getEndDate().getDate()!=null?panel.getEndDate().getDate().getTime():null;
		
		LODReport lodReport = XhibitDelegateHelper.getResults2Delegate().getLODBetweenDatesReport(XhibitSingleton.getInstance().getCourtId(), fromDate, toDate);
       
      
        if (lodReport.getLodDiaryInfo() != null && lodReport.getLodDiaryInfo().size() > 0) {
        	
           DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	       helper.setupReportDefaults(lodReport);
 	        
 	       PreviewReportAction previewReportAction = new PreviewReportAction(lodReport,"config/xsl/results/reports/lod/printLODReportBetweenDates.xsl","LODReportPreview");
 	       previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        } else {
        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message");
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
        					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
        }
	}      
}
