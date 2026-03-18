package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.business.database.results.OARDAPrintInformation;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidAmendmentBasicValue;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PublicRepresentationModel;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class DisplayOARDAAction extends XAction implements Action {

	private static final long serialVersionUID = 1L;

	private PublicRepresentationModel model;    
    
    public DisplayOARDAAction(PublicRepresentationModel model){
    	this.model = model;
    	populateFromBundle("PrintAmendmentOrder");
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
	
		LegalAidAmendmentBasicValue legalAidAmendment = XhibitDelegateHelper.getLegalAidDelegate().getLatestLegalAidAmendmentByLegalAidOrderId(model.getLegalAidOrderValue().getId());
		OARDAPrintInformation order = 
             XhibitDelegateHelper.getResults2Delegate().getOARDAPrintInformation(XhibitSingleton.getInstance().getCourtId(),legalAidAmendment.getId());
        
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	        helper.setupReportDefaults(order);
 	        order.setDateOfReport(XDateFormat.format(new Date(), XDateFormat.FULLMONTHFORMAT));
 	        
 	        String path = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OGRDAOrder.base");			
			URL url = DisplayOARDAAction.class.getClassLoader().getResource(path);
			
			PreviewReportAction previewReportAction = new PreviewReportAction(order,"config/xsl/results/reports/oarda/printOARDAReport.xsl","OARDAPreview",order.buildMap(path,url));
        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }

}
