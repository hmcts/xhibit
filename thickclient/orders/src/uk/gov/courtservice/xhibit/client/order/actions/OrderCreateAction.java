package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.controllers.OrderCreateController;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit2 OrdersCreateAction
 * </p>
 * <p>
 * Description: This must be registered with the main application. The action
 * controls the flow between the wizard and the other screens.
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

public class OrderCreateAction extends OrderAction {
    private static final Logger log = CSServices.getLogger(OrderCreateAction.class);

    /**
     * Creates a new instance of OrderInitialDataVO in Create mode
     */
    public OrderCreateAction(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac);
        setEnabled(true);
        populateFromBundle(ResourceHelper.getResourceString(ORDER_ACTION_CREATE));
    }

    public OrderCreateAction() throws CSRecoverableException {
        super();
        log.debug("OrderCreateAction: entering default contructor");
        populateFromBundle(ResourceHelper.getResourceString(ORDER_ACTION_CREATE));
        this.setLongDescription(ResourceHelper.getResourceString(ORDER_ACTION_CREATE_LDESC));
        this.setShortDescription(ResourceHelper.getResourceString(ORDER_ACTION_CREATE_SDESC));
        log.debug("OrderCreateAction: leaving default contructor");
    }

    /**
     * Creates a new instance of OrderInitialDataVO in Create mode
     */
    public OrderCreateAction(OrderInitialDataVO model) throws CSRecoverableException {
        this();
        setOrderModel(model);
        // this.ordInitData.setMode(OrderInitialDataVO.CREATE_MODE);
    }

    /**
     * actionPerformed event on XAction. Invokes the OrdersWizard and controls
     * the flow between the various orders screens. This should be regis    tered
     * with the Xhibit application (refer to developers guide).
     * 
     * @param parm1
     *            ActionEvent
     * @throws java.lang.Exception
     */
    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        log.debug("ORDERS***:xActionPerformed: entering method");

        super.xActionPerformed(parm1);
        // setOrderController(getOrderController());
        setHelper(new OrderInitialDataHelper((XhibitApplicationController) getController()));
        OrderCreateController createController = new OrderCreateController((XhibitApplicationController) getController());
        createController.displayWizard();
        log.debug("ORDERS***:xActionPerformed: leaving method");
    }

}