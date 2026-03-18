package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchOffenceAction;
import uk.gov.courtservice.xhibit.client.maintaincharges.UncodedOffenceController;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteriaPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: AddUncodedOffenceAction
 * </p>
 * <p>
 * Description: After you have opened the Search Dialog for Coded Offences, you
 * can bring the dialog to record Uncoded Offences.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @see OpenSearchOffenceAction
 * @author Cag Onganer
 * @version $Id: AddUncodedOffenceAction.java,v 1.2 2004/11/29 18:00:21 lzqbry
 *          Exp $
 * 
 */

public class AddUncodedOffenceAction extends XAction {

    /**
     * Retrieve properties from config.bundles.XhibitActionResources.properties
     */
    public AddUncodedOffenceAction() {
        populateFromBundle(XhibitActions.AddUncodedOffence);
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
        XHIBITSearch xSearch = ((XHIBITSearchCriteriaPanel) (this.getCaller())).getMyParentSeachControl();

        UncodedOffenceController uoc = new UncodedOffenceController(xac);
        uoc.openDialog(xSearch);
    }

}
