package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class CounselDetailsDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private CounselDetailsPanel bodyPanel;
	private CounselDetailsModel model;

	public CounselDetailsDialog(Frame frame, CounselDetailsModel model) throws CSRecoverableException {
		super(frame, XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
				"CounselDetails.mainTitle"), true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		if (model.getReadOnly()) {
			setTitle(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetailsReadOnly.mainTitle"));
		}

		this.model = model;
		this.bodyPanel = new CounselDetailsPanel(this, this.model);

		addBodyPanel(bodyPanel);
		pack();
	}
	
	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}
}
