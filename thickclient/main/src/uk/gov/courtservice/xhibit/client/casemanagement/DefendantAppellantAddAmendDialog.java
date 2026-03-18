package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class DefendantAppellantAddAmendDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private DefendantAppellantAddAmendPanel defBodyPanel;
	private DefendantAppellantAddAmendModel model;
	private DefendantAppellantSearchDialog defParent;

	public DefendantAppellantAddAmendDialog(Frame frame, DefendantAppellantAddAmendModel model, DefendantAppellantSearchDialog parent) throws CSRecoverableException {
		super(frame, XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
				"defendantAppellantAddAmend.mainTitle"), true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
        
        this.model = model;
        this.defParent = parent;
        this.defBodyPanel = new DefendantAppellantAddAmendPanel(this, this.model, this.defParent);
        
        addBodyPanel(defBodyPanel);
        pack();
	}
	
	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}
}
