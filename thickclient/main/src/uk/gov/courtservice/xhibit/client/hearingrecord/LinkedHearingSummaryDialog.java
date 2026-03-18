package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Frame;

import uk.gov.courtservice.xhibit.client.util.XDialog;
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
 * @author unascribed
 * @version 1.0
 */

public class LinkedHearingSummaryDialog extends XDialog {

	private static final long serialVersionUID = 1L;

	private HearingRecordModel model;

	private LinkedHearingSummaryPanel bodyPanel;

	public LinkedHearingSummaryDialog(Frame frame, HearingRecordModel model) {
		super(frame, "", true);
		//super(frame, "", true, OK_ONLY , DEFAULTOK);

		super.setTitle(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "linkedHearingsSummary"));
		this.setResizable(false);
		this.model = model;
		this.bodyPanel = new LinkedHearingSummaryPanel(this.model, this);
		super.addBodyPanel(bodyPanel);
		super.pack();
	}

	/*
	 * public static void main( String argv[] ) { LinkedHearingSummaryDialog d =
	 * new LinkedHearingSummaryDialog(new Frame(), new HearingRecordModel());
	 * d.setVisible(true); }
	 */
}