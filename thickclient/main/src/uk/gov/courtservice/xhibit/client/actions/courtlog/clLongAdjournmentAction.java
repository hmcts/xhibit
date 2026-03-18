package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.LongAdjournmentDialog;
import uk.gov.courtservice.xhibit.client.courtlog.LongAdjournmentModel;
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
public class clLongAdjournmentAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("clLongAdjournmentAction - displayScreen(e)");

        final LongAdjournmentModel model = (LongAdjournmentModel) cloneModel();
        final LongAdjournmentDialog myDialog = new LongAdjournmentDialog((Frame) getController(), model);

        displayDialog(myDialog);
    }
}
