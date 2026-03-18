package uk.gov.courtservice.xhibit.client.listings.preview;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XPanel;

public class PreviewListDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private XPanel bodyPanel;

    private PreviewListModel model;

	public PreviewListDialog(Frame frame, PreviewListModel model) throws CSRecoverableException {
        super(frame, "List - Preview", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);
        this.model = model;
        if(model.getListTypeEnum().isDaily()){
        	this.bodyPanel = new PreviewDailyListPanel(this.model);
        }else if (model.getListTypeEnum().isWarned()){
        	this.bodyPanel = new PreviewWarnedListPanel(this.model, this);
        }
        //Else throw new exception
        
        addBodyPanel(bodyPanel);
        setResizable(true);
        pack();
	}

	@Override
	protected OkCancelPanel createButtonPanel(int panelType, int defaultButton){
		OkCancelPanel buttonPanel = super.createButtonPanel(panelType, defaultButton);
		buttonPanel.okButton.setToolTipText("Preview");
		buttonPanel.okButton.setText("Preview");
		buttonPanel.cancelButton.setToolTipText("Close");
		buttonPanel.cancelButton.setText("Close");
		return buttonPanel;
	}
}