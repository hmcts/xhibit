package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.ShortAdjournmentDialog;
import uk.gov.courtservice.xhibit.client.courtlog.ShortAdjournmentModel;
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
public class clShortAdjournmentAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("clShortAdjournmentAction - displayScreen(e)");

        final ShortAdjournmentModel model = (ShortAdjournmentModel) cloneModel();
        final ShortAdjournmentDialog myDialog = new ShortAdjournmentDialog((Frame) getController(), model);

        displayDialog(myDialog);
    }
}
