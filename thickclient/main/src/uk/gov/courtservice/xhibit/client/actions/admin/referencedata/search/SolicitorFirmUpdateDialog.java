package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.Dimension;
import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class SolicitorFirmUpdateDialog extends XDialog{

	private static final long serialVersionUID = 6142669445493190674L;
	private static final String resources = XhibitBundles.XhibitSearch;

	private boolean isUpdate;
	
    public SolicitorFirmUpdateDialog(Frame frame, XPanel updatePanel, boolean isUpdate)
            throws CSRecoverableException {
		
        super(frame, "", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
        String title;
        if (isUpdate) {
        	title = XHIBITConstant.getResource(resources, "solicitorFirm.details.xxtitle");
        } else {
			title = XHIBITConstant.getResource(resources, "solicitorFirm.new.xxtitle");
        }
        super.setTitle(title);
    	this.setPreferredSize(new Dimension(550, 500));	
    	this.setMinimumSize(this.getPreferredSize());	
        this.bodyPanel = updatePanel;
        this.isUpdate = isUpdate;
        super.addBodyPanel(bodyPanel);
        super.pack();
        super.setResizable(true);
    }

	/**
	 * @return the isUpdate
	 */
	public boolean isUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate the isUpdate to set
	 */
	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
}
