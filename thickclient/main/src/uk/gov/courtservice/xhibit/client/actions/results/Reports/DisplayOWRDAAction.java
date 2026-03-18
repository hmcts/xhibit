package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PublicRepresentationModel;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.OWRDAPrintInformation;

public class DisplayOWRDAAction extends XAction implements Action {

	private static final long serialVersionUID = 1L;

	private PublicRepresentationModel model;    
    
    public DisplayOWRDAAction(PublicRepresentationModel model){
    	this.model = model;
    	populateFromBundle("PrintRevokeOrder");
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
			OWRDAPrintInformation order = 
             XhibitDelegateHelper.getResults2Delegate().getOWRDAPrintInformation(XhibitSingleton.getInstance().getCourtId(), model.getLegalAidOrderValue().getId());
        
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	        helper.setupReportDefaults(order);
 	        order.setDateOfReport(XDateFormat.format(new Date(), XDateFormat.FULLMONTHFORMAT));
 	        
 	        String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OGRDAOrder.base");			
			URL url = DisplayOARDAAction.class.getClassLoader().getResource(path);
			
			PreviewReportAction previewReportAction = new PreviewReportAction(order,"config/xsl/results/reports/owrda/printOWRDAReport.xsl","OWRDAPreview",order.buildMap(path,url));
        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }
}
