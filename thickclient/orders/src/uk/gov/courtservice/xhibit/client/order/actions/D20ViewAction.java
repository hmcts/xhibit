package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.controllers.D20ViewController;
import uk.gov.courtservice.xhibit.client.order.screens.controllers.MonetaryOrderViewController;
import uk.gov.courtservice.xhibit.client.order.screens.controllers.OrderViewController;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit2 D20ViewAction
 * </p>
 * <p>
 * Description: This must be registered with the main application. The action
 * controls the flow between the wizard in view mode and the other screens.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */

public class D20ViewAction extends OrderAction {

    private static final Logger log = CSServices.getLogger(OrderAction.class);

    /**
     * Creates a new instance of OrderInitialDataVO in View mode
     */
    public D20ViewAction(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac);
        populateFromBundle(ResourceHelper.getResourceString(D20_ACTION_VIEW));
        log.debug("D20ViewAction***: constructor with XhibitApplicationController");
    }

    public D20ViewAction() throws CSRecoverableException {
        super();
        populateFromBundle(ResourceHelper.getResourceString(D20_ACTION_VIEW));
        this.setLongDescription(ResourceHelper.getResourceString(D20_ACTION_VIEW_LDESC));
        this.setShortDescription(ResourceHelper.getResourceString(D20_ACTION_VIEW_SDESC));
        log.debug("D20ViewAction***: default constructor");
    }

    /**
     * Controls the flow of the screens between the wizard in view mode and the
     * rest of the orders screens
     * 
     * @param parm1
     *            ActionEvent
     * @throws java.lang.Exception
     */
    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        super.xActionPerformed(parm1);
        // setOrderController((XhibitApplicationController)getController());
        setHelper(new OrderInitialDataHelper((XhibitApplicationController) getController()));
        D20ViewController viewController = new D20ViewController((XhibitApplicationController) getController());
        viewController.displayWizard();
    }

}