package uk.gov.courtservice.xhibit.client.order.screens.controllers;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;


import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderAlreadyCreatedException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderNotSupportedException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.order.delegate.OrdersControllerBusinessDelegate;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderListDialog;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderGuiHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.util.OrderDefendantWrapper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: OrderController
 * </p>
 * <p>
 * Description: Superclass of Orders controllers
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

public abstract class OrderController {

    private static final Logger log = CSServices.getLogger(OrderController.class);

    /**
     * Holder for the initial data VO
     */
    private OrderInitialDataVO model = null;

    /**
     * Data helper
     */
    private OrderInitialDataHelper helper = null;

    private OrderGuiHelper guiHelper = null;

    private XhibitApplicationController xac;

    /**
     * Rectangle to size and position the dialogs
     */
    private Rectangle wizBounds;

    private static OrdersControllerBusinessDelegate ocbDelegate;
    
 
    /**
     * Abstract method to display wizards
     */
    abstract public void displayWizard() throws CSRecoverableException;

    /**
     * Displays the Orders list dialog
     */
    protected int isShowListDialog(OrderInitialDataVO model, Rectangle bounds, boolean replace, boolean copy)
            throws CSRecoverableException {
        log.debug("Start - isShowListDialog");
        log.debug("Replace: " + replace);
        log.debug("Displaying OrderListDialog............................");
        OrderListDialog listDlg = null;
        listDlg = new OrderListDialog(this.xac, "List", model, replace, copy);
        listDlg.setBounds(bounds);
        listDlg.setVisible(true);
        if (listDlg.isCancelled()) {
            listDlg = null;
            throw new UserCancelException("OrderListDialog:Cancelled", "Dialog Cancelled");
        } else {
            listDlg = null;
        }
        
        
        // if createOrder box has been checked, return 0 (CREATE), 
        // if in copy mode then return 3 (COPY)
        // otherwise if
        // CVROption is null return 1 (VIEW) otherwise return CVROption
        int mode = 0;
        if (model.isCreateOrder()) {
            mode = model.CREATE_MODE;
        } else if (copy){ // We know its not a "Create" so object selected from list of orders must be copy if copy is true
            mode = model.COPY_MODE;
        } else if (model.getCVROption() == null) {
            mode = model.VIEW_MODE;
        } else {
            mode = new Integer(model.getCVROption()).intValue();
        }
        
        return mode;
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
        XhbOrderValue order = XhibitDelegateHelper.getOrdersDelegate().getOrder(orderID);

        // Ensure model is set to View Mode as its possible to get to View Order from the Create Order process
        model.setMode(OrderInitialDataVO.VIEW_MODE);
        showOrderGUI(order);
    }
    
    protected boolean isCreateAllowed() {
        if (this.model.isMonetaryOrder()) {
            // Check defendants are allowed
            
            // Get selected defendant
            HashMap orderDisposalDefendantData = this.getHelper().getOrderDisposalData();
            if (orderDisposalDefendantData.containsKey(this.model.getDefendantName())) {
                HashMap defendantData = (HashMap) orderDisposalDefendantData.get(this.model.getDefendantName());
                boolean allowed = ((Boolean)defendantData.get("hasMonetaryDisposals")).booleanValue();
                return allowed;
            } else {
                // A problem? As all defendants should be listed in orderDisposalDefendantData
            }
        }
        
        return true;
    }
    
    /**
     * Create a copy of an existing order with the following defaults:
     * - signed_by is null
     * - signing_date is null
     * - order_delivery_status_id = 1
     * - order_status_id = 2
     * 
     * @param orderId
     * @return
     */
    protected Integer createCopy(Integer orderId, Integer caseId, Integer xhibitCaseId, String caseTitle, Integer courtId, boolean isBCase)  throws OrderException, OrderAlreadyCreatedException, CSUnrecoverableException, OrderNotSupportedException, OrderXMLException {
        /*XhbOrderValue order = XhibitDelegateHelper.getOrdersDelegate().getOrder(orderId);
        Integer orderTypeID = order.getXhbOrderTemplate().getOrderTypeId();
        XhbOrderValue newOrder = XhibitDelegateHelper.getOrdersDelegate().createOrder(order.getDefendantOnCaseId(), caseId, xhibitCaseId, caseTitle, courtId, orderTypeID, null, true, isBCase, "");
        return newOrder.getOrderId();*/
    	return new Integer(0);
    }

    /**
     * Display the Orders GUI with the order components and widgets
     * 
     * @param order
     *            The order to display
     */
    protected void showOrderGUI(XhbOrderValue order) {
        guiHelper = null;
        guiHelper = new OrderGuiHelper(this.xac);
        // Get the window toolkit
        Toolkit theKit = guiHelper.getToolkit();
        // Get the screen size
        Dimension wndSize = theKit.getScreenSize();

        // Set the position to top left of screen (0, 0)
        // and the size to full width and 95% of full height
        guiHelper.setBounds(0, 0, wndSize.width, (int) (wndSize.height * 0.95)); // Size

        guiHelper.showOrderGUI(order, this.model, helper.getXAppController());
        
    }

    /**
     * Return the model
     * 
     * @return the model
     */
    public OrderInitialDataVO getModel() {
        return this.model;
    }

    /**
     * Set the model
     * 
     * @param the
     *            model
     */
    public void setModel(OrderInitialDataVO model) {
        this.model = model;
    }

    /**
     * Return the helper
     * 
     * @return the helper
     */
    public OrderInitialDataHelper getHelper() {
        return helper;
    }

    /**
     * Set the helper
     * 
     * @param the
     *            helper
     */
    public void setHelper(OrderInitialDataHelper hlpr) {
        helper = hlpr;
    }

    /**
     * Return the controller
     * 
     * @return the controller
     */
    public XhibitApplicationController getController() {
        return xac;
    }

    /**
     * Set the controller
     * 
     * @param the
     *            controller
     */
    public void setController(XhibitApplicationController ctrlr) {
        xac = ctrlr;
    }

    /**
     * Return the Rectangle bounds
     * 
     * @return the bounds
     */
    public Rectangle getBounds() {
        return wizBounds;
    }

    /**
     * Set the Rectangle bounds
     * 
     * @param the
     *            bounds
     */
    public void setBounds(Rectangle bounds) {
        wizBounds = bounds;
    }
}