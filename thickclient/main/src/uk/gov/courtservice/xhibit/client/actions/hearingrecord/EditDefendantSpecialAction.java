package uk.gov.courtservice.xhibit.client.actions.hearingrecord;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAmendDefendantAction;
import uk.gov.courtservice.xhibit.client.actions.updatecase.UpdateDefendantModel;
import uk.gov.courtservice.xhibit.client.hearingrecord.DefendantDetails;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
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
 * @author unascribed
 * @version 1.0
 */
public class EditDefendantSpecialAction extends XAction {

	private static final long serialVersionUID = 1L;
	private boolean crestFormAFieldsOnly = false;

    public EditDefendantSpecialAction() {
        setName(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "edit"));
    }

    public void setCrestFormAFieldsOnly(boolean flag) {
        this.crestFormAFieldsOnly = flag;
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        HearingRecordModel model = ((DefendantDetails) this.getCaller()).getModel();
        UpdateDefendantModel udm = new UpdateDefendantModel(model.getDefendantId(), model.getCaseId(), true,
                this.crestFormAFieldsOnly);

        ((OpenAmendDefendantAction) XhibitActions.getAction(model.getXac(), XhibitActions.OpenAmendDefendant))
                .setModel(udm);

        XhibitActions.getAction(model.getXac(), XhibitActions.OpenAmendDefendant).actionPerformed(e);

        // update crest form a screens by call to BD and populating screens with
        // new HearingRecordValue data.
        try {
            model.getHearingRecordPanel().stepInitialise(); // BD call is in
            // stepInitialise
            // method.
            // indicate that we want to only refresh the read-only parts of
            // the screen
            model.getHearingRecordPanel().populateScreens(false, true);
            // model.getHearingRecordPanel().stepActivate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
