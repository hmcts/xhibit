package uk.gov.courtservice.xhibit.client.casemanagement.publicrep;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class  PrivateToPublicRepresentationDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private PrivateToPublicRepresentationPanel privateToPubBody;
    private PrivateToPublicRepresentationModel model;

	public PrivateToPublicRepresentationDialog(Frame frame, PrivateToPublicRepresentationModel model) throws CSRecoverableException {
        super(frame, XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "repOrder.privatetoPublicRepMainTitle"), 
        		true, XDialog.CANCEL, XDialog.DEFAULTCANCEL);
        
        this.model = model;
        this.privateToPubBody = new PrivateToPublicRepresentationPanel(this, this.model);
        
        addBodyPanel(privateToPubBody);
        pack();
	}
}
