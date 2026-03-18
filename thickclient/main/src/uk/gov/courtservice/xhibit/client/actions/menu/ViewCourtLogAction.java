package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogControllerModel;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Action to view the Court Log
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
 * @author unascribed
 * @version $Revision: 1.10 $
 */

public class ViewCourtLogAction extends SynchXAction {

    /**
     * The main application screen
     */
    private XhibitApplicationController xac;

    /**
     * The court log controller
     */
    private CourtLogController clc;

    /**
     * Creates a ViewCourtLogAction
     */
    public ViewCourtLogAction() {
        populateFromBundle("ViewCourtLog");
        setMnemonicKeyFromBundle("ViewCourtLog");
    }

    /**
     * Overrides SynchXAction - called on the current thread before the
     * synchActionPerformed is called. Used to get the current controller and
     * close the previous screen.
     * 
     * @param parm1
     *            ActionEvent not currently used
     * @throws java.lang.Exception
     */
    public void preSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        xac = (XhibitApplicationController) getController();

        // If the current XPanel has save functionality, gives the opportunity
        // to
        // prompt the user to save changes (if applicable). If the changes are
        // saved then any events will be displayed when the court log is opened.
        xac.callBodyPanelCloseLifeCycleMethods();
    }

    /**
     * SynchXAction implementaion - Called on the sync thread (DO NOT UPDATE GUI
     * IN THIS CALL). Used to create the Court Log Controller
     * 
     * @param e
     *            ActionEvent not currently used
     * @throws CSRecoverableException
     */
    public void synchActionPerformed(ActionEvent e) throws CSRecoverableException {
        if (xac != null && getModel() != null) {
            CourtLogControllerModel clcModel = new CourtLogControllerModel();
            clcModel.setAcm((ApplicationCaseModel) getModel());
            clc = new CourtLogController(clcModel);
        }
    }

    /**
     * Overrides SynchXAction - Called on the awt thread after
     * synchActionPerformed is called. Used to display the Court Log Controller
     * 
     * @param parm1
     *            ActionEvent not currently used
     * @throws CSRecoverableException
     */
    public void postSynchActionPerformed(ActionEvent parm1) throws CSRecoverableException {
        if (xac != null && clc != null) {
            xac.open(clc);
        }
    }
}