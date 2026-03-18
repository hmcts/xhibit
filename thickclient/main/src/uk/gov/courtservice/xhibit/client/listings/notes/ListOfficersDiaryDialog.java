package uk.gov.courtservice.xhibit.client.listings.notes;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

public class ListOfficersDiaryDialog extends AbstractNotesDialog {

	private static final long serialVersionUID = 1L;

	public ListOfficersDiaryDialog(Frame frame, ListOfficersDiaryModel model) throws CSRecoverableException {
        super(frame, model);
	}
	
	@Override
	public AbstractNotesPanel initBodyPanel() throws CSRecoverableException {
		return new ListOfficersDiaryPanel(this, getModel());
	}
	
	@Override
	public ListOfficersDiaryModel getModel() {
		return (ListOfficersDiaryModel) super.getModel();
	}
	
	public boolean isDialogValid() {
		return ((AbstractNotesPanel) getBodyPanel()).validateNonSittingDate(getModel().getSelectedDate());
	}
}
