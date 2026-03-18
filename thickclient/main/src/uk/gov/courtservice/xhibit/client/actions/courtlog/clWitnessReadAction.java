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
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Unknown
 * @version $Revision: 1.1 $
 */
public class clWitnessReadAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("clWitnessReadAction - displayScreen(e)");

        final WitnessReadModel model = (WitnessReadModel) cloneModel();
        final WitnessReadDialog myDialog = new WitnessReadDialog((Frame) getController(), model);

        displayDialog(myDialog);
    }
}
