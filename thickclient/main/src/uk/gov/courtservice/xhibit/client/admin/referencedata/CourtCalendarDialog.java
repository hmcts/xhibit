package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search.RefSearchUpdatePanelUtil;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Main screen for maintaining the Court Calendar
 */

public class CourtCalendarDialog extends XDialog {

	private static final long serialVersionUID = 1L;
	private static String resources = XhibitBundles.CourtCalendar;

	public CourtCalendarDialog(Frame frame) throws CSRecoverableException {

		super(frame, "", true, XDialog.OKCANCEL, XDialog.DEFAULTCANCEL);
		super.setTitle(XHIBITConstant.getResource(resources, "lblTitleBarMaintainCourtCalendar"));
		this.bodyPanel = new CourtCalendarPanel(this, new CourtCalendarModel());
		super.addBodyPanel(bodyPanel);
		super.pack();
		super.setResizable(true);
	}

	@Override
	protected OkCancelPanel createButtonPanel(int panelType, int defaultButton) {
		OkCancelPanel buttonPanel = super.createButtonPanel(panelType, defaultButton);
		buttonPanel.okButton.setToolTipText(XHIBITConstant.getResource(resources, "lblSaveButton"));
		buttonPanel.okButton.setText(XHIBITConstant.getResource(resources, "lblSaveButton"));
		return buttonPanel;
	}

	@Override
	public void cancelClicked(ActionEvent ae) throws Exception {

		if (this.bodyPanel.getModified()) {
			int confirmed = RefSearchUpdatePanelUtil.unsavedChangesDialog();
			if (confirmed == 0) {
				dispose();
			} else {
				return;
			}
		} else {
			int result = JOptionPane.showConfirmDialog((Component) null,
					XHIBITConstant.getResource(resources, "calendarExitMessage"),
					XHIBITConstant.getResource(XhibitBundles.XhibitClientDefaultResources, "ExitConfirmTitle"),
					JOptionPane.YES_NO_OPTION);
			if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
				return;
			} else {
				super.cancelClicked(ae);
			}
		}
	}

	@Override
	public void okClicked(ActionEvent ae) throws Exception {
		// Save button clicked ('Ok' button was renamed to 'Save')
		super.applyClicked(ae);
	}
}
