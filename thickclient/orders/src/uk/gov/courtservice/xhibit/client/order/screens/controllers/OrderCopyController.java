package uk.gov.courtservice.xhibit.client.order.screens.controllers;

import java.awt.Rectangle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderInitialDataException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderNotSelectedException;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrdersWizard;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: Xhibit2 OrderCopyAction
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

public class OrderCopyController extends OrderController {

    private static final Logger log = CSServices.getLogger(OrderCopyController.class);

    /**
     * Creates a new instance of OrderInitialDataVO in View mode
     * 
     * @param xac
     *            the controller
     * @throws CSRecoverableException
     */
    public OrderCopyController(XhibitApplicationController xac) throws CSRecoverableException {
        super();
        setController(xac);
        log.debug("OrderCopyController***: constructor with XhibitApplicationController");
    }

    /**
     * Displays the Orders Wizard
     * 
     * @throws CSRecoverableException
     */
    public void displayWizard() throws CSRecoverableException {
        if (getController() == null) {
            setBounds(new Rectangle(0, 0, 800, 600));
            log.debug("OrderCopyController***: xac is null");
        } else {
            setBounds(getController().getBounds());
            log.debug("OrderCopyController***: case id: " + getController().getApplicationCaseModel().getCaseNumber());
        }
        setHelper(new OrderInitialDataHelper(getController()));
        setUpModel();
        getHelper().setModel(getModel());

        try {
            getHelper().loadModel(false, false);
        } catch (OrderInitialDataException oide) {
            log.error("OrderInitialDataException");
            throw new CSUnrecoverableException(oide);
        }
        /**
         * Holder for the Orders Wizard
         */
        OrdersWizard wizard = null;
        wizard = new OrdersWizard(getController(), getModel());
        wizard.setBounds(getBounds());
        wizard.setVisible(true);

        // wizard is modal so set reference to null once the dialog has been
        // closed
        wizard = null;

        // The only way to tell how the wizard was closed is to set a flag on
        // the model and query that
        if (getModel().getFinished() == true) {
            try {

                switch (isShowListDialog(getModel(), getBounds(), false, true)) {
                    case OrderInitialDataVO.CREATE_MODE: {
                        wizard = null;
                        createController();
                        break;
                    }
                    case OrderInitialDataVO.VIEW_MODE:
                        // {
                        // wizard = null;
                        // try
                        // {
                        // Integer orderID = getModel().getSelectedOrder();
                        // showExistingOrder(orderID);
                        // }
                        // catch (OrderNotSelectedException ex) {
                        // log.info(ex.getMessage());
                        // }
                        // break;
                        // }
                    case OrderInitialDataVO.REPLACE_MODE: {
                        // Display the selected order
                        Integer orderID = null;
                        try {
                            orderID = getModel().getSelectedOrder();
                            showExistingOrder(orderID);
                        } catch (OrderNotSelectedException ex) {
                            log.info(ex.getMessage());
                        }
                        break;
                    }
                    case OrderInitialDataVO.COPY_MODE: {
                        // Display a copy of the selected order. Regardless of the status of the selected order, it will be opened in edit mode, unsaved and unsigned.
                        Integer orderID = null;
                        try {
                            orderID = getModel().getSelectedOrder();
                            //Integer xhibitCaseId = getModel().getXhibitCaseId();
                            //XhbOrderValue order = XhibitDelegateHelper.getOrdersDelegate().getOrderForCopying(orderID);
                            //Integer orderTypeID = order.getXhbOrderTemplate().getOrderTypeId();
                            //XhbOrderValue newOrder = XhibitDelegateHelper.getOrdersDelegate().createOrder(order.getDefendantOnCaseId(), xhibitCaseId, xhibitCaseId, getModel().getCaseTitle(), new Integer(getModel().getXhibitCourtId()), orderTypeID, null, true, getModel().isBCase(), "");
                            //Integer newOrderID = createCopy(orderID, new Integer(getModel().getCaseID().substring(1)), xhibitCaseId, getModel().getCaseTitle(), new Integer(getModel().getXhibitCourtId()), getModel().isBCase());
                            showExistingOrder(orderID);
                        } catch (OrderNotSelectedException ex) {
                            log.info(ex.getMessage());
                        }
                        break;
                    }
                }
            } catch (CSRecoverableException ex) {
                displayWizard();
            }
        }
    }
    
    /**
     * Display an existing order
     * 
     * @param orderID
     *            The id of the order to display
     * @throws OrderException
     * @throws CSUnrecoverableException
     */
    protected void showExistingOrder(Integer orderID) throws OrderException, CSUnrecoverableException {
        XhbOrderValue order = XhibitDelegateHelper.getOrdersDelegate().getOrderForCopying(orderID);
        order.setOrderId(-1); // Reset to -1 (new order) now that we have got the details from the selected order
        
        // Reset the following for new order types of data
        order.setDescription("");
        order.setSignedBy(null);
        order.setSigningDate(null);
        
        order.getXhbDeliverOrderStatus().setCode("NOTREADY");
        order.getXhbDeliverOrderStatus().setOrderDeliveryStatusId(new Integer(1));
        order.setOrderDeliveryStatusId(new Integer(1));
        
        order.getXhbOrderStatus().setCode("NEW");
        order.getXhbOrderStatus().setOrderStatusId(new Integer(1));
        order.setOrderStatusId(new Integer(1));
        showOrderGUI(order);
    }

    private void setUpModel() throws CSRecoverableException {
        setModel(new OrderInitialDataVO());
        log.debug("$$$ OrderCopyController:this.ordInitData.getMode " + getModel().getMode());
        getModel().setMode(OrderInitialDataVO.COPY_MODE);
        log.debug("$$$ OrderCopyController:this.ordInitData.getMode " + getModel().getMode());
        getModel().setHelper(getHelper());
    }

    private void createController() throws CSRecoverableException {

        getModel().setMode(OrderInitialDataVO.CREATE_MODE);
        OrderCreateController createController = new OrderCreateController(getController());
        createController.displayWizard();

    }

}