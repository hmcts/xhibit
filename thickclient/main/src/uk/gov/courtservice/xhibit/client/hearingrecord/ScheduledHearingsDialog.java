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

public class ScheduledHearingsDialog extends XDialog {
    private HearingRecordModel model;

    private ScheduledHearingsPanel bodyPanel;

    public ScheduledHearingsDialog(Frame frame, HearingRecordModel model) {
        super(frame, "", true);
        this.model = model;

        super.setTitle(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "scheduledHearings"));
        this.bodyPanel = new ScheduledHearingsPanel(model);
        super.addBodyPanel(bodyPanel);
        super.pack();
    }
}