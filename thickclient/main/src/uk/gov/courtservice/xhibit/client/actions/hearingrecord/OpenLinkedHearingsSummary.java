package uk.gov.courtservice.xhibit.client.actions.hearingrecord;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.hearingrecord.LinkedHearingSummaryDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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

public class OpenLinkedHearingsSummary extends XAction {
    HearingRecordModel model;

    public OpenLinkedHearingsSummary() {
        populateFromBundle("OpenLinkedHearingsSummary");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        model = new HearingRecordModel();
        model.setXac((XhibitApplicationController) this.getController());
        model.setHearingId(model.getXac().getApplicationCaseModel().getScheduledHearingValue()
                .getScheduledHearingBasicValue().getHearingID());
        model.setLeadHearingId(model.getXac().getApplicationCaseModel().getScheduledHearingValue()
                .getScheduledHearingBasicValue().getHearingID());

        // opening linked hearing summary dialog
        LinkedHearingSummaryDialog d = new LinkedHearingSummaryDialog((Frame) getController(), model);
        d.setVisible(true);
    }
}