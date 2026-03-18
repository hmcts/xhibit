package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit2 OrderAction
 * </p>
 * <p>
 * Description: Abstract order action - superclass of CreateOrderAction and
 * ViewOrderAction. Should be extended if any more Orders related actions are
 * required
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
public abstract class OrderAction extends XAction {

    /**
     * View Action
     */
    public static final String ORDER_ACTION_VIEW = "order.action.view";

    /**
     * Create Action
     */
    public static final String ORDER_ACTION_CREATE = "order.action.create";

    /**
     * Copy Action
     */
    public static final String ORDER_ACTION_COPY = "order.action.copy";

    /**
     * View Action (Monetary Order)
     */
    public static final String MONETARY_ORDER_ACTION_VIEW = "monetary.order.action.view";

    /**
     * Create Action (Monetary Order)
     */
    public static final String MONETARY_ORDER_ACTION_CREATE = "monetary.order.action.create";

    /**
     * Copy Action (Monetary Order)
     */
    public static final String MONETARY_ORDER_ACTION_COPY = "monetary.order.action.copy";
    
    /**
     * View Action (D20 Order)
     */
    public static final String D20_ACTION_VIEW = "d20.action.view";

    /**
     * Create Action (D20 Order)
     */
    public static final String D20_ACTION_CREATE = "d20.action.create";

    /**
     * Copy Action (D20 Order)
     */
    public static final String D20_ACTION_COPY = "d20.action.copy";
    
    /**
     * Create Action Short Desc
     */
    public static final String ORDER_ACTION_CREATE_SDESC = "orders.action.create.shortdesc";

    /**
     * Create Action Long Desc
     */
    public static final String ORDER_ACTION_CREATE_LDESC = "orders.action.create.longdesc";

    /**
     * View Action Short Desc
     */
    public static final String ORDER_ACTION_VIEW_SDESC = "orders.action.view.shortdesc";

    /**
     * View Action Long Desc
     */
    public static final String ORDER_ACTION_VIEW_LDESC = "orders.action.view.longdesc";

    /**
     * Copy Action Short Desc
     */
    public static final String ORDER_ACTION_COPY_SDESC = "orders.action.copy.shortdesc";

    /**
     * Copy Action Long Desc
     */
    public static final String ORDER_ACTION_COPY_LDESC = "orders.action.copy.longdesc";
    
    /**
     * Create Action Short Desc (Monetary Order)
     */
    public static final String MONETARY_ORDER_ACTION_CREATE_SDESC = "monetary.orders.action.create.shortdesc";

    /**
     * Create Action Long Desc (Monetary Order)
     */
    public static final String MONETARY_ORDER_ACTION_CREATE_LDESC = "monetary.orders.action.create.longdesc";

    /**
     * View Action Short Desc (Monetary Order)
     */
    public static final String MONETARY_ORDER_ACTION_VIEW_SDESC = "monetary.orders.action.view.shortdesc";

    /**
     * View Action Long Desc (Monetary Order)
     */
    public static final String MONETARY_ORDER_ACTION_VIEW_LDESC = "monetary.orders.action.view.longdesc";

    /**
     * Copy Action Short Desc (Monetary Order)
     */
    public static final String MONETARY_ORDER_ACTION_COPY_SDESC = "monetary.orders.action.copy.shortdesc";

    /**
     * Copy Action Long Desc (Monetary Order)
     */
    public static final String MONETARY_ORDER_ACTION_COPY_LDESC = "monetary.orders.action.copy.longdesc";

    public static final String MONETARY_ORDER_ACKNOWLEDGEMENT_SDESC = "monetary.orders.action.acknowledgement.shortdesc";
    public static final String MONETARY_ORDER_ACKNOWLEDGEMENT_LDESC = "monetary.orders.action.acknowledgement.longdesc";

    /** D20 constants **/
    /**
     * Create Action Short Desc (D20 Order)
     */
    public static final String D20_ACTION_CREATE_SDESC = "d20.action.create.shortdesc";

    /**
     * Create Action Long Desc (D20 Order)
     */
    public static final String D20_ACTION_CREATE_LDESC = "d20.action.create.longdesc";

    /**
     * View Action Short Desc (D20 Order)
     */
    public static final String D20_ACTION_VIEW_SDESC = "d20.action.view.shortdesc";

    /**
     * View Action Long Desc (D20 Order)
     */
    public static final String D20_ACTION_VIEW_LDESC = "d20.action.view.longdesc";

    /**
     * Copy Action Short Desc (D20 Order)
     */
    public static final String D20_ACTION_COPY_SDESC = "d20.action.copy.shortdesc";

    /**
     * Copy Action Long Desc (D20 Order)
     */
    public static final String D20_ACTION_COPY_LDESC = "d20.action.copy.longdesc";
    
    /**
     * Holder for the initial data VO
     */
    private OrderInitialDataVO model = null;

    /**
     * Data helper
     */
    private OrderInitialDataHelper helper = null;

    /**
     * The controller
     */
    private XhibitApplicationController xac;

    /**
     * Constructor
     */
    public OrderAction() {
        super();
    }

    /**
     * Constructor
     * 
     * @param xac
     */
    public OrderAction(XhibitApplicationController xac) {
        helper = new OrderInitialDataHelper(xac);
    }

    /**
     * Ation listener on the XAction
     * 
     * @param parm1
     *            ActionEvent
     * @throws java.lang.Exception
     */
    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        // No implementation
    }

    /**
     * Return the model
     * 
     * @return the model
     */
    public OrderInitialDataVO getOrderModel() {
        return this.model;
    }

    /**
     * Set the model
     * 
     * @param hlpr
     *            model
     */
    public void setOrderModel(OrderInitialDataVO model) {
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
     * @param hlpr
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
    public XhibitApplicationController getOrderController() {
        return xac;
    }

    /**
     * Set the controller
     * 
     * @param ctrllr
     *            the controller
     */
    public void setOrderController(XhibitApplicationController ctrllr) {
        xac = ctrllr;
    }

}