package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.SimpleEventDialog;
import uk.gov.courtservice.xhibit.client.courtlog.SimpleEventModel;
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
public class SimpleEventAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("SimpleEventAction - displayScreen(e);");

        final SimpleEventModel model = (SimpleEventModel) cloneModel();
        final SimpleEventDialog med = new SimpleEventDialog((Frame) getController(), model);

        displayDialog(med);
    }
}
