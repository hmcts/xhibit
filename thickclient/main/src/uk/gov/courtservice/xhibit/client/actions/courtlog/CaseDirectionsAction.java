package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.CaseDirectionsDialog;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version $Revision: 1.7 $
 */
public class CaseDirectionsAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws CSRecoverableException {
        log.debug("CaseDirections - displayScreen(e);");

        CaseDirectionsDialog cdd = new CaseDirectionsDialog((Frame) getController());

        displayDialog(cdd);
    }
}
