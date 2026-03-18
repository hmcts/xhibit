package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class DeleteCaseDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	private DeleteCasePanel deleteCasePanel;
	private DeleteCaseModel deleteCaseModel;

	public DeleteCaseDialog(Frame frame, DeleteCaseModel deleteCaseModel) throws CSRecoverableException {
		super(frame, "Delete Case", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		setPreferredSize(new Dimension(500, 300));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.deleteCaseModel = deleteCaseModel;
		deleteCasePanel = new DeleteCasePanel(this, deleteCaseModel);
		addBodyPanel(deleteCasePanel);
		pack();
	}

	@Override
	public void dispose() {
		clearStatusBarScreenCode();
		super.dispose();
	}
}
