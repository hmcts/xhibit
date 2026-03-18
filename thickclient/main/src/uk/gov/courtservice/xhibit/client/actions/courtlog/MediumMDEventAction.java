package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMDDialog;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMDModel;
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
 * @version $Revision: 1.7 $
 */
public class MediumMDEventAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("MediumMDEventAction - displayScreen(e);");

        final MediumEventMDModel model = (MediumEventMDModel) cloneModel();
        final MediumEventMDDialog med = new MediumEventMDDialog((Frame) getController(), model);

        displayDialog(med);
    }
}
