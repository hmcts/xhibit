package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.WitnessSwornDialog;
import uk.gov.courtservice.xhibit.client.courtlog.WitnessSwornModel;
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
 * @version $Revision: 1.8 $
 */
public class clWitnessSwornAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("clWitnessSwornAction - displayScreen(e)");

        final WitnessSwornModel model = (WitnessSwornModel) cloneModel();
        final WitnessSwornDialog myDialog = new WitnessSwornDialog((Frame) getController(), model);

        displayDialog(myDialog);
    }
}
