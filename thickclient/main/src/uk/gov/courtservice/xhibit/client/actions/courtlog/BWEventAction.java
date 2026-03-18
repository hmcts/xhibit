package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.BWEventDialog;
import uk.gov.courtservice.xhibit.client.courtlog.FreeTextModel;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMLModel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
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
public class BWEventAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("IssueBWEventAction - displayScreen(e);");

        final MediumEventMLModel model = (MediumEventMLModel) cloneModel();
        final BWEventDialog issueBW = new BWEventDialog((Frame) getController(), model);
        displayDialog(issueBW);
    }
}
