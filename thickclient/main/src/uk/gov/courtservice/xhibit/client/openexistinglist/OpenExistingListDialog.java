package uk.gov.courtservice.xhibit.client.openexistinglist;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class OpenExistingListDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private OpenExistingListPanel bodyPanel;
	
	private OpenExistingListModel model;

	public OpenExistingListDialog(Frame frame, OpenExistingListModel model) throws CSRecoverableException {
		super(frame, "Open/Delete Existing List", true, XDialog.CUSTOM,  XDialog.DEFAULTCANCEL);
        
        this.model = model;
        this.bodyPanel = new OpenExistingListPanel(this, this.model);
        
        addBodyPanel(bodyPanel);
        setResizable(true);
        pack();
	}

}
