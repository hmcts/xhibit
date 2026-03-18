package uk.gov.courtservice.xhibit.client.actions.hearingrecord;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.hearingrecord.LinkedHearingSummaryPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */

public class UnlinkHearingAction extends XAction {
	private HearingRecordModel model;

	public UnlinkHearingAction() {
		setName(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "unlink"));
	}

	public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
		XHIBITConstant.debug("in unlink hearing action");
		((LinkedHearingSummaryPanel) this.getCaller()).getSelectedRowDetails(null);
		model = ((LinkedHearingSummaryPanel) this.getCaller()).getModel();

		XhibitDelegateHelper.getHearingDelegate().unlinkHearing(model.getHearingId(),
				XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

		try {
			((LinkedHearingSummaryPanel) this.getCaller()).stepInitialise(); // BD
			// call
			// is
			// in
			// stepInitialise
			// method.
			((LinkedHearingSummaryPanel) this.getCaller()).stepActivate(); // Populating
			// screens
			// with
			// retrieved
			// data
			// is
			// done
			// in
			// stepActivate
			// method.
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}