package uk.gov.courtservice.xhibit.client.actions.hearingrecord;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.hearingrecord.LinkHearingSearchPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
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

public class LinkHearingSearchAction extends XAction {
    HearingRecordModel model;

    public LinkHearingSearchAction() {
        setName(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "search"));
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("In link hearing search action");

        model = ((LinkHearingSearchPanel) this.getCaller()).getModel();
        ((LinkHearingSearchPanel) this.getCaller()).search();
    }
}