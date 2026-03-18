package uk.gov.courtservice.xhibit.client.casemanagement.publicrep;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class PublicRepresentationDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private PublicRepresentationPanel bodyPanel;
    private PublicRepresentationModel model;

	public PublicRepresentationDialog(Frame frame, PublicRepresentationModel model) throws CSRecoverableException {
        super(frame, XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.publicRepMainTitle"), 
        		true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
        
        this.model = model;
        this.bodyPanel = new PublicRepresentationPanel(this, this.model);
        
        addBodyPanel(bodyPanel);
        pack();
	}
}
