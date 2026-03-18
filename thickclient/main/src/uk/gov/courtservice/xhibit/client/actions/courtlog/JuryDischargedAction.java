package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.JuryDischargedDialog;
import uk.gov.courtservice.xhibit.client.courtlog.JuryDischargedModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version $Revision: 1.10 $
 */
public class JuryDischargedAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws CSRecoverableException, UserCancelException {
        log.debug("JuryDischarged - displayScreen(e);");

        final JuryDischargedModel model = (JuryDischargedModel) cloneModel();
        final JuryDischargedDialog bcd = new JuryDischargedDialog((Frame) getController(), model);

        displayDialog(bcd);
    }
}
