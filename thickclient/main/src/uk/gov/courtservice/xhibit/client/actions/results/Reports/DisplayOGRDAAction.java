package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PublicRepresentationModel;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.OGRDAOrder;

public class DisplayOGRDAAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	
	private PublicRepresentationModel model;   
    private Frame parentFrame = null;
    
    public DisplayOGRDAAction(PublicRepresentationModel model,Frame parentFrame){
        this.model = model;
        this.parentFrame = parentFrame;
        populateFromBundle("PrintOrder");
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
	
		OGRDAOrder order = 
             XhibitDelegateHelper.getResults2Delegate().getOGRDAOrder(XhibitSingleton.getInstance().getCourtId(), model.getLegalAidOrderValue().getId());
        
        if (order.getOGRDADefendantValues() != null && order.getOGRDADefendantValues().size() > 0) {
        	
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	        helper.setupReportDefaults(order);
 	        order.setDateOfReport(XDateFormat.format(new Date(), XDateFormat.FULLMONTHFORMAT));
 	        
 	        String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OGRDAOrder.base");			
			URL url = DisplayOGRDAAction.class.getClassLoader().getResource(path);
			
			PreviewReportAction previewReportAction = new PreviewReportAction(order,"config/xsl/results/reports/ogrda/printOGRDAReport.xsl","OGRDAOrderPreview",order.buildMap(path,url));
        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			
        } else {
        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message");
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
        					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
        }
    }
}
