package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings.PreliminaryHearings;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: PreliminaryHearingAction
 * </p>
 * <p>
 * Description: Invokes the preliminary hearing screen dropping it into the main
 * body of the XHIBIT application.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class PreliminaryHearingsAction extends SynchXAction {
    private XhibitApplicationController xac;

    private PreliminaryHearings pc;

    public PreliminaryHearingsAction() {
        populateFromBundle("PreliminaryHearing");
    }

    /**
     * Gets the XhibitApplicationController and calls close life-cycle events on
     * any panel contained in it's body
     * 
     * @param parm1
     * @throws Exception
     */
    public void preSynchActionPerformed(ActionEvent parm1) throws Exception {
        xac = (XhibitApplicationController) getController();
        // If the current XPanel has save functionality, this gives the
        // opportunity to prompt the user to save changes (if applicable).
        // This is to ensure any Pleas have been saved.
        xac.callBodyPanelCloseLifeCycleMethods();
    }

    /**
     * Instantiates a new PreliminaryHearings class
     * 
     * @param parm1
     * @throws Exception
     */
    public void synchActionPerformed(ActionEvent parm1) throws Exception {
        pc = new PreliminaryHearings(xac);
    }

    /**
     * Drops the PreliminaryHearings class into the main body of the XAC
     * 
     * @param parm1
     * @throws Exception
     */
    public void postSynchActionPerformed(ActionEvent parm1) throws Exception {
        if (pc != null && xac != null) {
            xac.open(pc);
        }
    }
}
