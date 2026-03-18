package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.pleas.PleaController;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: PleaAction
 * </p>
 * <p>
 * Description: action for pleas
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class PleaAction extends SynchXAction {
    private XhibitApplicationController xac;

    private PleaController pc;

    public PleaAction() {
        populateFromBundle("Plea");
    }

    public void preSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        xac = (XhibitApplicationController) getController();
        xac.callBodyPanelCloseLifeCycleMethods();
    }

    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        pc = new PleaController(xac.getApplicationCaseModel());
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        if (pc != null && xac != null) {
            xac.open(pc);
        }
    }
}