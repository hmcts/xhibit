package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
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
 * @author Sherie De Silva
 * @version 1.0
 */

public class LinkHearingSearchDialog extends XDialog {
    HearingRecordModel model;

    LinkHearingSearchPanel bodyPanel;

    public LinkHearingSearchDialog(Frame frame, HearingRecordModel model) throws CSRecoverableException {
        super(frame, "", true);

        this.model = model;
        this.bodyPanel = new LinkHearingSearchPanel(model);
        super.setTitle(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "linkHearingsSearch"));
        super.addBodyPanel(this.bodyPanel);
        super.pack();
    }
}