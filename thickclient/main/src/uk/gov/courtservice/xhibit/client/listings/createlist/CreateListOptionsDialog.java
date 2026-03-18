package uk.gov.courtservice.xhibit.client.listings.createlist;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class CreateListOptionsDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private CreateListOptionsPanel bodyPanel;
    private CreateListOptionsModel model;

	public CreateListOptionsDialog(XDialog parentDialog, CreateListOptionsModel model) throws CSRecoverableException {
        super(parentDialog, XHIBITConstant.getResource(XhibitBundles.Listings, getTitleResourceKey(model)), 
        		true, XDialog.OKCANCEL, XDialog.DEFAULTCANCEL);
        
        this.model = model;
        this.bodyPanel = new CreateListOptionsPanel(this, this.model);
        
        addBodyPanel(bodyPanel);
        pack();
	}
	
	public boolean isCancelled() {
		return CreateListOptionsModel.Action.CANCEL.equals(model.getAction());
	}
	
	public boolean isOpenRequired() {
		return CreateListOptionsModel.Action.OPEN.equals(model.getAction());
	}
	
	public boolean isCreateRequired() {
		return CreateListOptionsModel.Action.CREATE.equals(model.getAction());
	}
	
	public Integer getSelectedListId() {
		return model.getSelectedListId();
	}	
	
	private static String getTitleResourceKey(CreateListOptionsModel model) {
		if (model.getListType().isDaily()) {
			return "createListOptionsDailyTitle";
		} else if (model.getListType().isFirm()) {
			return "createListOptionsFirmTitle";
		} else if (model.getListType().isWarned()) {
			return "createListOptionsWarnTitle";
		}
		return null;
	}

}
