package uk.gov.courtservice.xhibit.client.actions.hearingrecord;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.hearingrecord.LinkHearingSearchDialog;
import uk.gov.courtservice.xhibit.client.hearingrecord.LinkedHearingSummaryPanel;
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
public class LinkHearingAction extends XAction {
    public LinkHearingAction() {
        setName(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "link"));
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        XHIBITConstant.debug("In link hearing action");

        LinkedHearingSummaryPanel panel = (LinkedHearingSummaryPanel) this.getCaller();
        HearingRecordModel model = panel.getModel();

        // setting link hearings table data vector to null to clear table model
        model.setLinkResultsTableData(null);

        // PRE00090 - ensure we use the main frame
        Frame parentFrame = (Frame) getController();

        LinkHearingSearchDialog d = new LinkHearingSearchDialog(parentFrame, model);
        d.setVisible(true);

        if (d.isOkClicked()) {
            try {
                panel.stepInitialise(); // BD call is in stepInitialise method.
                panel.stepActivate(); // Populating screens with retrieved
                // data is done in stepActivate method.
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
