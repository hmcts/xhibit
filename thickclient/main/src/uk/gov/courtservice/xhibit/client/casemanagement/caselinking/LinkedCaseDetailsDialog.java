package uk.gov.courtservice.xhibit.client.casemanagement.caselinking;

import java.awt.Frame;

import javax.swing.JDialog;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class LinkedCaseDetailsDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private LinkedCaseDetailsPanel linkedCaseDetailsPanel;
	private LinkedCaseDetailsModel linkedCaseDetailsModel;
	
	public LinkedCaseDetailsDialog(Frame frame, LinkedCaseDetailsModel linkedCaseDetailsModel) throws CSRecoverableException { 
		super(frame, "Linked Case Details, Group Number:", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.linkedCaseDetailsModel = linkedCaseDetailsModel;
		linkedCaseDetailsPanel = new LinkedCaseDetailsPanel(this, linkedCaseDetailsModel);
		addBodyPanel(linkedCaseDetailsPanel);
		pack();
	}

	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}
}