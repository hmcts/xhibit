package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.controllers.OrderCopyController;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit2 OrderViewAction
 * </p>
 * <p>
 * Description: This must be registered with the main application. The action
 * controls the flow between the wizard in view mode and the other screens.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderCopyAction extends OrderAction {

    private static final Logger log = CSServices.getLogger(OrderAction.class);

    /**
     * Creates a new instance of OrderInitialDataVO in Copy mode
     */
    public OrderCopyAction(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac);
        populateFromBundle(ResourceHelper.getResourceString(ORDER_ACTION_COPY));
        log.debug("OrderCopyAction***: constructor with XhibitApplicationController");
    }

    public OrderCopyAction() throws CSRecoverableException {
        super();
        populateFromBundle(ResourceHelper.getResourceString(ORDER_ACTION_COPY));
        this.setLongDescription(ResourceHelper.getResourceString(ORDER_ACTION_COPY_LDESC));
        this.setShortDescription(ResourceHelper.getResourceString(ORDER_ACTION_COPY_SDESC));
        log.debug("OrderCopyAction***: default constructor");
    }

    /**
     * Controls the flow of the screens between the wizard in copy mode and the
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
        OrderCopyController copyController = new OrderCopyController((XhibitApplicationController) getController());
        copyController.displayWizard();
        // this.displayWizard();
    }

}