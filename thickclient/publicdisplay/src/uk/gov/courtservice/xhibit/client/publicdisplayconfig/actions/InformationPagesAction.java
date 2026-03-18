package uk.gov.courtservice.xhibit.client.publicdisplayconfig.actions;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.viewinfopages.InformationPagesController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Action To Launch Information Pages
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
 * @author Rakesh Lakhani
 * @version $Id: InformationPagesAction.java,v 1.1 2004/02/04 16:15:51 sz0t7n
 *          Exp $
 */

public class InformationPagesAction extends XAction {

    public InformationPagesAction() {
        populateFromBundle("ViewInformationPages");
    }

    public void xActionPerformed(java.awt.event.ActionEvent ae) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        InformationPagesController.showInformationPages(xac);
    }
}