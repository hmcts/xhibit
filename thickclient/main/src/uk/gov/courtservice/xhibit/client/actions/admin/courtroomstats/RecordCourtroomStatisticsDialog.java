package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class RecordCourtroomStatisticsDialog  extends XDialog{
	private static final long serialVersionUID = 1L;
	
	public RecordCourtroomStatisticsDialog(final Frame frame, final RecordCourtroomStatisticsModel model) throws CSRecoverableException {
		super(frame,
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatisticsDialog.title"),
				true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

		bodyPanel = new RecordCourtroomStatisticsPanel(this, model);
		addBodyPanel(bodyPanel);
		setResizable(true);
		pack();
	}	
	

	@Override
	protected OkCancelPanel createButtonPanel(int panelType, int defaultButton){
		OkCancelPanel buttonPanel = super.createButtonPanel(panelType, defaultButton);
		buttonPanel.okButton.setToolTipText(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.savebutton.tooltip"));
		buttonPanel.okButton.setText(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.savebutton.label"));
		buttonPanel.cancelButton.setToolTipText(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.closebutton.tooltip"));
		buttonPanel.cancelButton.setText(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.closebutton.label"));
		
		return buttonPanel;
	}
	
}
