package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.BailCustodyDialog;
import uk.gov.courtservice.xhibit.client.courtlog.BailCustodyModel;
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
 * @version $Revision: 1.10 $
 */
public class BailCustodyAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("BailCustodyAction - displayScreen(e);");

        final BailCustodyModel model = (BailCustodyModel) cloneModel();
        final BailCustodyDialog bcd = new BailCustodyDialog((Frame) getController(), model);

        displayDialog(bcd);
    }
}
