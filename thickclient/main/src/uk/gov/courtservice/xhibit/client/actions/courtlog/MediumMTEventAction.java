package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMTDialog;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMTModel;

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
public class MediumMTEventAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws CSRecoverableException {
        log.debug("MediumMTEventAction - displayScreen(e)");

        final MediumEventMTModel model = (MediumEventMTModel) cloneModel();
        final MediumEventMTDialog med = new MediumEventMTDialog((Frame) getController(), model);

        displayDialog(med);
    }
}
