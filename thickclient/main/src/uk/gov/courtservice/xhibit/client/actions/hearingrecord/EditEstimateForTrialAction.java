package uk.gov.courtservice.xhibit.client.actions.hearingrecord;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.hearingrecord.BailCustody;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.hearingrecord.TimeEstimateDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

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

public class EditEstimateForTrialAction extends XAction {
    HearingRecordModel model;

    public EditEstimateForTrialAction() {
        setName(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "edit"));
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        this.model = ((BailCustody) this.getCaller()).getModel();

        // crud.setEventType(PDHConstants.CASE_TRIALTIME);
        // DirectionsSingleEventDialog d = new
        // DirectionsSingleEventDialog((Frame)getController(), crud);
        // TimeEstimateDialog d = new TimeEstimateDialog(new Frame(), model);
        // PRE00090 - ensure we use the main frame
        TimeEstimateDialog d = new TimeEstimateDialog((Frame) getController(), model);
        d.setVisible(true);

        if (d.isOkClicked()) {
            CourtLogCRUDValue crud = new CourtLogCRUDValue();
            HashMap h = new HashMap();
            d.getBodyPanel().populateCRUD(h, null);
            crud.setEntryFreeText("");
            crud.setEventType(PDHConstants.CASE_TRIALTIME);
            crud.setEntryDate(Calendar.getInstance().getTime());
            crud.setCaseId(model.getCaseId());
            crud.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom()); // XI2B134
            crud.setProperty("Directions_By_Case_Options", h);

            if (model.getHearingRecordUpdateVal().getDirectionsForCaseValue().getCourtLogCRUDValues() == null
                    || model.getHearingRecordUpdateVal().getDirectionsForCaseValue().getCourtLogCRUDValues().length == 0) {
                CourtLogCRUDValue[] cruds = new CourtLogCRUDValue[] { crud };
                model.getHearingRecordUpdateVal().getDirectionsForCaseValue().setCourtLogCRUDValues(cruds);
            } else {
                // Check if there is already a trial time estimate event listed
                // if so replace it, otherwise add it to the end
                CourtLogCRUDValue[] cruds = model.getHearingRecordUpdateVal().getDirectionsForCaseValue()
                        .getCourtLogCRUDValues();
                Collection c = new ArrayList();
                boolean replaced = false;
                if (cruds != null) {
                    for (int i = 0; i < cruds.length; i++) {
                        if (cruds[i].getEventType().equals(PDHConstants.CASE_TRIALTIME)) {
                            cruds[i] = crud;
                            replaced = true;
                            break;
                        } else {
                            c.add(cruds[i]);
                        }
                    }
                }

                if (!replaced) {
                    c.add(crud);
                    cruds = new CourtLogCRUDValue[c.size()];
                    c.toArray(cruds);
                }

                model.getHearingRecordUpdateVal().getDirectionsForCaseValue().setCourtLogCRUDValues(cruds);
            }
            // model.getHearingRecordUpdateVal().getDirectionsForCaseValue().getCourtLogCRUDValue().setEntryFreeText("");
            // model.getHearingRecordUpdateVal().getDirectionsForCaseValue().getCourtLogCRUDValue().setEventType(new
            // Integer(40702));
            // model.getHearingRecordUpdateVal().getDirectionsForCaseValue().getCourtLogCRUDValue().setEntryDate(Calendar.getInstance().getTime());
            // model.getHearingRecordUpdateVal().getDirectionsForCaseValue().getCourtLogCRUDValue().setCaseId(model.getCaseId());
            // model.getHearingRecordUpdateVal().getDirectionsForCaseValue().getCourtLogCRUDValue().setInCourt(
            // XhibitSingleton.getInstance( ).isUserInCourtroom( ) ); //
            // XI2B134
            // model.getHearingRecordUpdateVal().getDirectionsForCaseValue().getCourtLogCRUDValue().setProperty("E40702_Directions_By_Case_Options",
            // h);
        }
    }
}