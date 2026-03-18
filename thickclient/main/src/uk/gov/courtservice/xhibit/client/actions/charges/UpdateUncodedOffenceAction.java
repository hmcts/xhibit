package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.maintaincharges.UncodedOffenceController;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: UpdateUncodedOffenceAction
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version $Id: UpdateUncodedOffenceAction.java,v 1.2 2005/01/27 13:32:27
 *          xztnfq Exp $
 * 
 */
public class UpdateUncodedOffenceAction extends XAction {
    /**
     * Retrieve properties from config.bundles.XhibitActionResources.properties
     */
    public UpdateUncodedOffenceAction() {
        populateFromBundle(XhibitActions.UpdateUncodedOffence);
    }

    /**
     * In order to add an uncoded offence, the save operation is carried over
     * the Xhibit Application Controller for the session information.
     * 
     * @param e
     *            save command
     */
    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        UncodedOffenceController uoc = new UncodedOffenceController(xac);
        uoc.openDialog(xac, false);

    }

}
