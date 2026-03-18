package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class REDELDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	private REDELPanel redelPanel;
	private REDELModel redelModel;

	public REDELDialog(Frame frame, REDELModel redelModel) throws CSRecoverableException {
		super(frame, "Replace Delete Defendant", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		setPreferredSize(new Dimension(800, 400));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.redelModel = redelModel;
		redelPanel = new REDELPanel(this, redelModel);
		addBodyPanel(redelPanel);
		pack();
	}

	@Override
	public void dispose() {
		clearStatusBarScreenCode();
		super.dispose();
	}
}
