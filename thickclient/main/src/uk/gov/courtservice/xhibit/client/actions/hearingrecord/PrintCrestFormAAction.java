package uk.gov.courtservice.xhibit.client.actions.hearingrecord;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.menu.PrintAction;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.hearingrecord.LinkedHearingSummaryPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

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

public class PrintCrestFormAAction extends PrintAction {
	HearingRecordModel model;

	private String xslFOString = null;

	public PrintCrestFormAAction() {
		super();
		setName(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "print"));
		setIcon(null);
	}

	public void preSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
		model = ((LinkedHearingSummaryPanel) this.getCaller()).getModel();
		((LinkedHearingSummaryPanel) this.getCaller()).getSelectedRowDetails(null);

		setXslFoStrings(((LinkedHearingSummaryPanel) this.getCaller()).print());
	}
}