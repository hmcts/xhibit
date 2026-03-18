package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.SpecialMeasuresApplicationDialog;
import uk.gov.courtservice.xhibit.client.courtlog.SpecialMeasuresApplicationModel;
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
 * @version $Revision: 1.1 $
 */
public class SpecialMeasuresApplicationAction extends CourtLogAction {

    private static final long serialVersionUID = 1L;

    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("SpecialMeasuresApplicationAction - displayScreen(e);");

        final SpecialMeasuresApplicationModel model = (SpecialMeasuresApplicationModel) cloneModel();
        final SpecialMeasuresApplicationDialog smad = 
            new SpecialMeasuresApplicationDialog((Frame) getController(), model);

        displayDialog(smad);
    }
}
