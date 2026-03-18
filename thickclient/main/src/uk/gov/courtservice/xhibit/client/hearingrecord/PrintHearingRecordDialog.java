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

public class PrintHearingRecordDialog extends XDialog {
    HearingRecordModel model;

    PrintHearingRecordPanel bodyPanel;

    public PrintHearingRecordDialog(Frame frame, HearingRecordModel model) {
        super(frame, "", true);

        this.model = model;
        this.setTitle(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "lblPrintCRESTFormA"));
        this.bodyPanel = new PrintHearingRecordPanel(model);
        super.addBodyPanel(this.bodyPanel);
        super.pack();
    }
}