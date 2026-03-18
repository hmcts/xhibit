package uk.gov.courtservice.xhibit.client.casemanagement.caselinking;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CaseUnlinkingDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private CaseUnlinkingPanel caseUnlinkingPanel;
	private CaseUnlinkingModel caseUnlinkingModel;
	
	public CaseUnlinkingDialog(Frame frame, CaseUnlinkingModel caseUnlinkingModel) throws CSRecoverableException { 
		super(frame, "View/Un-Link Case Links - ", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.caseUnlinkingModel = caseUnlinkingModel;
		caseUnlinkingPanel = new CaseUnlinkingPanel(this, caseUnlinkingModel, (XhibitApplicationController) frame);
		addBodyPanel(caseUnlinkingPanel);
		pack();
	}
	
	@Override
	public void cancelClicked(ActionEvent ae) throws Exception {
		int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel this process?", "Are you sure?", 
				  JOptionPane.YES_NO_OPTION);
		if ((n == JOptionPane.NO_OPTION) || (n == JOptionPane.CLOSED_OPTION)) {
			return;
		} else {
			super.cancelClicked(ae);
		}
	}
}