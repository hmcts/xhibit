package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class ChamberDetailsDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private ChamberDetailsPanel bodyPanel;
	private ChamberDetailsModel model;

	public ChamberDetailsDialog(Frame frame, ChamberDetailsModel model) throws CSRecoverableException {
		super(frame, XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
				"ChamberDetails.mainTitle"), true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		if (model.getReadOnly()) {
			setTitle(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetailsReadOnly.mainTitle"));
		}

		this.model = model;
		this.bodyPanel = new ChamberDetailsPanel(this, this.model);

		addBodyPanel(bodyPanel);
		pack();
	}
	
	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}
}
