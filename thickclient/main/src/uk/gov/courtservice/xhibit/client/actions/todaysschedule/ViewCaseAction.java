package uk.gov.courtservice.xhibit.client.actions.todaysschedule;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class ViewCaseAction extends XAction {

    public ViewCaseAction() {
        populateFromBundle("ViewCase");
        // setMnemonicKeyFromBundle("ViewCase");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        if (getModel() != null) {
            ((XhibitApplicationController) getController()).openCase((ApplicationCaseModel) getModel(), false);
        }
    }
}