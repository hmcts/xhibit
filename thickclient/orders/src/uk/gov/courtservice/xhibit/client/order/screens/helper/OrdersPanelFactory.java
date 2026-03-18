package uk.gov.courtservice.xhibit.client.order.screens.helper;

import javax.swing.JButton;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.panel.D20TypeListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.DefendantListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.DisposalListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderExistsPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderSavedPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderSentConfirmedPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderSignedPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderTypeListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrdersSummaryPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Xhibit2: OrdersPanelFactory
 * </p>
 * <p>
 * Description: Creates the panels to add to the order screens
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

public class OrdersPanelFactory {
    private static final Logger log = CSServices.getLogger(OrdersPanelFactory.class);

    private static OrdersPanelFactory orderPanelFactory;

    private OrdersSummaryPanel summaryPanel;

    private DefendantListPanel defendantListPanel;

    private DisposalListPanel disposalListPanel;

    private OrderTypeListPanel orderTypeListPanel;

    private OrderExistsPanel orderExistsOptionPanel;

    private OrderSignedPanel orderSignedPanel;
    
    private D20TypeListPanel d20TypeListPanel;

    private OrderSentConfirmedPanel orderSentPanel;
    
    private OrderSavedPanel orderSavedPanel;

    private OrderListPanel orderListPanel;

    /**
     * Creates an instance of the factory
     */
    static {
        orderPanelFactory = new OrdersPanelFactory();
    }

    /**
     * Returns a static instance of the OrdersPanelFactory
     * 
     * @return
     */
    public static OrdersPanelFactory getinstance() {
        return orderPanelFactory;
    }

    /**
     * Returns a new instance of the OrderSummaryPanel
     * 
     * @return XPanel The OrderSummaryPanel
     * @throws CSRecoverableException
     */
    public XPanel getOrderSummaryPanel() throws CSRecoverableException {
        return new OrdersSummaryPanel();
    }

    /**
     * Creates a new instance of OrderSummaryPanel and sets the initial data up
     * from the OrderInitialDataVO
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @return XPanel The OrderSummaryPanel
     * @throws CSRecoverableException
     */
    public XPanel getOrderSummaryPanel(OrderInitialDataVO model) throws CSRecoverableException {
        OrdersSummaryPanel sp = (OrdersSummaryPanel) getOrderSummaryPanel();
        sp.setOrderDataModel(model);
        sp.setPanelFromModel(model);
        return sp;
    }

    /**
     * Returns the DefendantListPanel
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @param cBL
     *            An OrderComboBoxListener to listen for changes on the dropdown
     * @return XPanel The DefendantListPanel
     * @throws CSRecoverableException
     */
    public XPanel getDefendantListPanel(OrderInitialDataVO model, OrderComboBoxListener cBL)
            throws CSRecoverableException {
        DefendantListPanel dtlp = (DefendantListPanel) getDefendantListPanel(model);
        dtlp.addListener(cBL);
        return dtlp;
    }

    /**
     * Returns a new instance of the defendant list panel, if one has not been
     * created.
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @return XPanel The DefendantListPanel
     * @throws CSRecoverableException
     */
    public XPanel getDefendantListPanel(OrderInitialDataVO model) throws CSRecoverableException {
        if (defendantListPanel == null) {
            defendantListPanel = new DefendantListPanel(model);
        }
        return defendantListPanel;
    }

    /**
     * Returns the DisposalListPanel
     * 
     * @return XPanel DisposalListPanel
     * @throws CSRecoverableException
     */
    public XPanel getDisposalListPanel() throws CSRecoverableException {
        if (disposalListPanel == null) {
            disposalListPanel = new DisposalListPanel();
        }
        return disposalListPanel;
    }

    /**
     * Returns the OrderTypeListPanel
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @return XPanel The OrderTypeListPanel
     * @throws CSRecoverableException
     */
    public XPanel getOrderTypeListPanel(OrderInitialDataVO model) throws CSRecoverableException {
        if (orderTypeListPanel == null) {
            orderTypeListPanel = new OrderTypeListPanel(model);
        }
        return orderTypeListPanel;
    }
    
    /**
     * Returns the D20TypeListPanel
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @return XPanel The OrderTypeListPanel
     * @throws CSRecoverableException
     */
    public XPanel getD20TypeListPanel(OrderInitialDataVO model, OrderComboBoxListener cBL) throws CSRecoverableException {
        if (d20TypeListPanel == null) {
        	d20TypeListPanel = new D20TypeListPanel(model);
        }
        
        d20TypeListPanel.addListener(cBL);
      
        return d20TypeListPanel;
    }


    /**
     * Returns the OrderTypeListPanel and adds an OrderComboBoxListener to
     * listen for changes to the selected OrderType
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @param cBL
     *            The OrderComboBoxListener listening for changes on the
     *            selected order type
     * @return XPanel The OrderTypeListPanel
     * @throws CSRecoverableException
     */
    public XPanel getOrderTypeListPanel(OrderInitialDataVO model, OrderComboBoxListener cBL)
            throws CSRecoverableException {
        OrderTypeListPanel otlp = (OrderTypeListPanel) getOrderTypeListPanel(model);
        otlp.addListener(cBL);
        return otlp;
    }

    /**
     * Returns the OrderExistsPanel
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @return XPanel The OrderExistsPanel
     * @throws CSRecoverableException
     */
    public XPanel getOrderExistsPanel(OrderInitialDataVO model) throws CSRecoverableException {
        if (orderExistsOptionPanel == null) {
            orderExistsOptionPanel = new OrderExistsPanel(model);
        }
        return orderExistsOptionPanel;
    }

    /**
     * Returns the OrderSignedPanel
     * 
     * @param sign
     *            the model
     * @return XPanel OrderSignedPanel
     * @throws CSRecoverableException
     */
    public XPanel getOrderSignedPanel(SignedDetailsVO sign) throws CSRecoverableException {
        orderSignedPanel = new OrderSignedPanel();
        orderSignedPanel.setSignedDetails(sign);

        return orderSignedPanel;
    }
    

    /**
     * Return the OrderSavedPanel
     * 
     * @return XPanel The OrderSavedPanel
     * @throws CSRecoverableException
     */
    public XPanel getOrderSavedPanel() throws CSRecoverableException {
        if (orderSavedPanel == null) {
            orderSavedPanel = new OrderSavedPanel();
        }
        orderSavedPanel.getSaveDetails().setText("");
        return orderSavedPanel;
    }

    /**
     * Return the OrderListPanel
     * 
     * @param odm
     *            The OrderInitialDataVO holding the initial order data
     * @param button
     *            the button to enable/disable
     * @return XPanel The OrderListPanel
     * @throws CSRecoverableException
     */
    public XPanel getOrderListPanel(OrderInitialDataVO model, JButton button, boolean replace, boolean copy)
            throws CSRecoverableException {
        orderListPanel = new OrderListPanel(model, button, replace, copy);
        return orderListPanel;
    }

	public OrderSignedPanel getOrderSignedPanel(SentDetailsVO sentVO) throws CSRecoverableException {
		orderSignedPanel = new OrderSignedPanel();
        orderSignedPanel.setSignedDetails(sentVO);

        return orderSignedPanel;
	}
}