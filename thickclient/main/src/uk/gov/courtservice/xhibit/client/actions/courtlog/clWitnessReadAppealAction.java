package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.WitnessReadDialog;
import uk.gov.courtservice.xhibit.client.courtlog.WitnessReadModel;
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
 * @version $Revision: 1.1 $
 */
public class clWitnessReadAppealAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("clWitnessReadAppealAction - displayScreen(e)");

        final WitnessReadModel model = (WitnessReadModel) cloneModel();
        final WitnessReadDialog myDialog = new WitnessReadDialog((Frame) getController(), model);

        displayDialog(myDialog);
    }
}
