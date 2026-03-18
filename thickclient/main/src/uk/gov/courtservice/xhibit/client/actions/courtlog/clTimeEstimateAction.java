package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.EstimateForTrialDialog;
import uk.gov.courtservice.xhibit.client.courtlog.EstimateForTrialModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Unknown
 * @version $Revision: 1.9 $
 */
public class clTimeEstimateAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("clTimeEstimateAction - displayScreen( e );");

        final EstimateForTrialModel model = (EstimateForTrialModel) cloneModel();
        final EstimateForTrialDialog myDialog = new EstimateForTrialDialog((Frame) getController(), model);

        displayDialog(myDialog);
    }
}
