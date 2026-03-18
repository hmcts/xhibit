package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class ChamberAndAdvocateDetailsDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private ChamberAndAdvocateDetailsPanel bodyPanel;
	private ChamberAndAdvocateDetailsModel model;

	public ChamberAndAdvocateDetailsDialog(Frame frame, ChamberAndAdvocateDetailsModel model)
			throws CSRecoverableException {
		super(frame, XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
				"ChamberAndAdvocateDetails.mainTitle"), true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		if (model.getReadOnly()) {
			setTitle(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberAndAdvocateDetailsReadOnly.mainTitle"));
		}

		this.model = model;
		this.bodyPanel = new ChamberAndAdvocateDetailsPanel(this, this.model);

		addBodyPanel(bodyPanel);
		pack();
	}
	
	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}
}
