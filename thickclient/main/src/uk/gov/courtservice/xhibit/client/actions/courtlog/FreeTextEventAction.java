package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.FreeTextDialog;
import uk.gov.courtservice.xhibit.client.courtlog.FreeTextModel;

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
public class FreeTextEventAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws CSRecoverableException {
        log.debug("FreeTextEventAction - displayScreen(e)");

        final FreeTextModel model = cloneModel();
        final FreeTextDialog fta = new FreeTextDialog((Frame) getController(), model);

        displayDialog(fta);
    }
}
