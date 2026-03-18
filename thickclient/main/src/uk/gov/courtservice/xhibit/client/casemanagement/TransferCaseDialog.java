package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class TransferCaseDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private TransferCasePanel transferCasePanel;
	private TransferCaseModel transferCaseModel;
	
	public TransferCaseDialog(Frame frame, TransferCaseModel transferCaseModel) throws CSRecoverableException { 
		super(frame, "Transfer Case", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		//setPreferredSize(new Dimension(400,200));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.transferCaseModel = transferCaseModel;
		transferCasePanel = new TransferCasePanel(this, transferCaseModel);
		addBodyPanel(transferCasePanel);
		pack();
	}

	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}
}