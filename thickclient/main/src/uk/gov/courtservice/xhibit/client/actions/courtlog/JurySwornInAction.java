package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.JurySwornInDialog;
import uk.gov.courtservice.xhibit.client.courtlog.JurySwornInModel;
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
public class JurySwornInAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws CSRecoverableException, UserCancelException {
        log.debug("JurySwornInAction - displayScreen(e)");

        final JurySwornInModel model = (JurySwornInModel) cloneModel();
        final JurySwornInDialog bcd = new JurySwornInDialog((java.awt.Frame) getController(), model);

        displayDialog(bcd);
    }
}
