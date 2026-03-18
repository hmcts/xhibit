package uk.gov.courtservice.xhibit.client.order.screens.controllers;

import java.awt.Rectangle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderInitialDataException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderNotSelectedException;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrdersWizard;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit2 MonetaryOrderViewController
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

public class MonetaryOrderViewController extends OrderController {

    private static final Logger log = CSServices.getLogger(MonetaryOrderViewController.class);
    
    private static final String NO_MON_DISP_ERROR = "orders.monetary.disposals.empty";

    /**
     * Creates a new instance of OrderInitialDataVO in View mode
     * 
     * @param xac
     *            the controller
     * @throws CSRecoverableException
     */
    public MonetaryOrderViewController(XhibitApplicationController xac) throws CSRecoverableException {
        super();
        setController(xac);
        log.debug("OrderViewAction***: constructor with XhibitApplicationController");
    }

    /**
     * Displays the Orders Wizard
     * 
     * @throws CSRecoverableException
     */
    public void displayWizard() throws CSRecoverableException {
        if (getController() == null) {
            setBounds(new Rectangle(0, 0, 800, 600));
            log.debug("ORDERS***: xac is null");
        } else {
            setBounds(getController().getBounds());
            log.debug("ORDERS***: case id: " + getController().getApplicationCaseModel().getCaseNumber());
        }
        setHelper(new OrderInitialDataHelper(getController()));
        setUpModel();
        getHelper().setModel(getModel());

        try {
            getHelper().loadModel(true, false);
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
        // the
        // model and query that
        if (getModel().getFinished() == true) {
            try {

                switch (isShowListDialog(getModel(), getBounds(), false, false)) {
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
                }
            } catch (CSRecoverableException ex) {
                if (ex.getMessage() != null && ex.getMessage().indexOf("No monetary disposals") > 0) {
                    throw new CSRecoverableException(NO_MON_DISP_ERROR, "No monetary disposals exist for this defendant.");
                } else {
                    displayWizard();
                }
            }
        }
    }

    private void setUpModel() throws CSRecoverableException {
        setModel(new OrderInitialDataVO());
        log.debug("$$$ OrderViewController:this.ordInitData.getMode " + getModel().getMode());
        getModel().setMode(OrderInitialDataVO.VIEW_MODE);
        log.debug("$$$ OrderViewController:this.ordInitData.getMode " + getModel().getMode());
        getModel().setHelper(getHelper());
    }

    private void createController() throws CSRecoverableException {

        getModel().setMode(OrderInitialDataVO.CREATE_MODE);
        MonetaryOrderCreateController createController = new MonetaryOrderCreateController(getController());
        createController.displayWizard();

    }

}