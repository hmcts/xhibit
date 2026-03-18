package uk.gov.courtservice.xhibit.client.listings.notes;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class CaseNotesDialog extends AbstractNotesDialog {

	private static final long serialVersionUID = 1L;

	public CaseNotesDialog(XDialog parent, CaseNotesModel model) throws CSRecoverableException {
        super(parent, model);
	}
	
	@Override
	public AbstractNotesPanel initBodyPanel() throws CSRecoverableException {
		return new CaseNotesPanel(this, getModel());
	}
	
	@Override
	public CaseNotesModel getModel() {
		return (CaseNotesModel) super.getModel();
	}
}
