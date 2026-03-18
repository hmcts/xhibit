package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.LegalArgumentOptionsDialog;
import uk.gov.courtservice.xhibit.client.courtlog.LegalArgumentOptionsModel;

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
 * @version $Revision: 1.8 $
 */
public class LegalArgumentOptionsAppealAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws CSRecoverableException {
        log.debug("LegalArgumentOptionsAppealAction - displayScreen( e );");

        final LegalArgumentOptionsModel model = (LegalArgumentOptionsModel) cloneModel();
        final LegalArgumentOptionsDialog bcd = new LegalArgumentOptionsDialog((Frame) getController(), model);

        displayDialog(bcd);
    }
}
