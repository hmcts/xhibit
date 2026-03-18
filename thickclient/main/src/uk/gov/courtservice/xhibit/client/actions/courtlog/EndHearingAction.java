package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.courtlog.EndHearingDialog;
import uk.gov.courtservice.xhibit.client.courtlog.EndHearingModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: EndHearingAction
 * </p>
 * <p>
 * Description: Executes the End Hearing court log event screen. This screen
 * takes two forms depending on whether or not there are defendants for a given
 * scheduled hearing - this is reflected in the case type.<br>
 * For case types B and U, the basic form of the screen is shown with no
 * defendants shown. For case types A, S and T, the screen is displayed with the
 * defendants displayed.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version $Revision: 1.5 $
 */
public class EndHearingAction extends CourtLogAction {
    /**
     * Displays the end hearing event popup dialog. This dialog supports two
     * events; 30500 - for case level end hearing events, 30600 - for defendant
     * level end hearing events. The screen displayed to the user depends upon
     * what type of event is being generated and this, in turn, is determined by
     * the case type.
     * 
     * For case types A, S or T, the screen is shown with a list of the
     * defendants.
     * 
     * For case types B or U, no defendants are available.
     * 
     * @param e
     * @throws UserCancelException
     * @throws CSRecoverableException
     */
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("EndHearingAction - displayScreen(e)");

        final EndHearingModel model = (EndHearingModel) cloneModel();

        // Check the case type to determine the event code
        ScheduledHearingValue shv = model.getXac().getApplicationCaseModel().getScheduledHearingValue();

        if (XHIBITConstant.isCriminalAppeal_CaseType(shv) || XHIBITConstant.isMiscelleanousAppeal_CaseType(shv)
                || XHIBITConstant.isTrial_CaseType(shv) || XHIBITConstant.isSentence_CaseType(shv)) {
            // Defendant level event
            model.setEventType("30600");
        } else {
            // Case level event
            model.setEventType("30500");
        }

        final EndHearingDialog myDialog = new EndHearingDialog((Frame) getController(), model);

        displayDialog(myDialog);
    }
}
