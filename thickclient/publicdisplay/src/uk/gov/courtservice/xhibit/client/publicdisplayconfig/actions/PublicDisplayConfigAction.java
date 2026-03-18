package uk.gov.courtservice.xhibit.client.publicdisplayconfig.actions;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.publicdisplayconfig.PublicDisplayConfigController;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description: Lauches the public display configurator.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PublicDisplayConfigAction.java,v 1.1 2004/01/15 16:57:48 sz0t7n
 *          Exp $
 */

public class PublicDisplayConfigAction extends XAction {

    public PublicDisplayConfigAction() {
        populateFromBundle("PublicDisplayConfiguration");
    }

    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        PublicDisplayConfigController.showConfigurationController(xac);
    }
}