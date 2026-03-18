package uk.gov.courtservice.xhibit.client.listings.notes;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

public abstract class AbstractNotesDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private static final String TITLE = ResourceBundleHelper.getResource(XhibitBundles.Notes,"caseNotesTitle");
	
	
	private AbstractNotesPanel bodyPanel;
	private AbstractNotesModel model;

	/*
	 *  Action Constructor (from menu)
	 */
	public AbstractNotesDialog(Frame frame, AbstractNotesModel model) throws CSRecoverableException {
		super(frame, TITLE, true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
        initDialog(model);	
	}
	
	/*
	 *  Modal Screen (called from a parent dialog)
	 */
	public AbstractNotesDialog(XDialog parent, AbstractNotesModel model) throws CSRecoverableException {
        super(parent, TITLE, true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);       
        initDialog(model);
	}
	
	private void initDialog(AbstractNotesModel model) throws CSRecoverableException {
		setModel(model);
		setBodyPanel(initBodyPanel());
        
        addBodyPanel(getBodyPanel());
        pack();
	}
		
	public abstract AbstractNotesPanel initBodyPanel() throws CSRecoverableException;

	public AbstractNotesModel getModel() {
		return this.model;
	}

	public void setModel(AbstractNotesModel model) {
		this.model = model;
	}

	public AbstractNotesPanel getBodyPanel() {
		return this.bodyPanel;
	}

	public void setBodyPanel(AbstractNotesPanel bodyPanel) {
		this.bodyPanel = bodyPanel;
	}
}
