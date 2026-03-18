package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class HearingRecordDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private HearingRecordModel model;

	private HearingRecordPanel bodyPanel;

	// cache the parent frame, for use by the cancel and ok events
	private final Frame frame;

	public HearingRecordDialog(Frame frame, HearingRecordModel model) throws CSRecoverableException {
		super(frame, "", true);
		this.setResizable(false);
		super.setTitle(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "formA"));
		this.frame = frame;
		this.model = model;
		this.bodyPanel = new HearingRecordPanel(this, model, model.getXac());
		super.addBodyPanel(bodyPanel);
		super.pack();
	}

	// overriding cancelClicked method of XDialog
	public void cancelClicked(ActionEvent ae) throws Exception {
		XHIBITConstant.debug("HearingRecordDialog Cancel Clicked");

		// bring up message asking user if they want to cancel changes
		boolean caseEditable = model.getXac().getApplicationCaseModel().isInEditMode(FunctionList.EExportHearingRecord);
		if (model.isUpdated() && caseEditable) {
			// PRE00090, used the stored frame instead of creating a new one
			int reply = JOptionPane.showConfirmDialog(frame,
					XHIBITConstant.getResource(XhibitBundles.HearingRecord, "changesMade"),
					XHIBITConstant.getResource(XhibitBundles.HearingRecord, "cancel"), JOptionPane.YES_NO_OPTION,
					JOptionPane.ERROR_MESSAGE);

			if (reply == JOptionPane.YES_OPTION) {
				bodyPanel.stepDeinitialise(false);
				this.dispose();
			}
		} else {
			bodyPanel.stepDeinitialise(false);
			this.dispose();
		}

	}

	public void okClicked(ActionEvent ae) throws Exception {
		XHIBITConstant.debug("HearingRecordDialog Ok Clicked");

		// Validate the tabs
		JPanel panel = null;
		try {
			panel = bodyPanel.getHearingDetails();
			bodyPanel.getHearingDetails().stepValidate();
			panel = bodyPanel.getRepresentation();
			bodyPanel.getRepresentation().stepValidate();
			panel = bodyPanel.getBailCustody();
			bodyPanel.getBailCustody().stepValidate();
			panel = bodyPanel.getDefDetails();
			bodyPanel.getDefDetails().stepValidate();
			panel = bodyPanel.getOther();
			bodyPanel.getOther().stepValidate();
		} 
		catch (CSValidationException e) {
			// Show the tab containing the error
			bodyPanel.getTabPane().setSelectedComponent(panel);
			throw e;
		}

		bodyPanel.getBailCustody().maybeDisplayMessage();

		bodyPanel.stepDeactivate();
		bodyPanel.stepDeinitialise(true);
		this.dispose();
	}
}
