package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.TakenIntoConsiderationDialog;
import uk.gov.courtservice.xhibit.client.courtlog.TakenIntoConsiderationModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version $Revision: 1.9 $
 */
public class TakenIntoConsiderationAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("TakenIntoConsiderationAction - displayScreen(e);");

        final TakenIntoConsiderationModel model = (TakenIntoConsiderationModel) cloneModel();
        final TakenIntoConsiderationDialog bcd = new TakenIntoConsiderationDialog((Frame) getController(), model);

        displayDialog(bcd);
    }
}
