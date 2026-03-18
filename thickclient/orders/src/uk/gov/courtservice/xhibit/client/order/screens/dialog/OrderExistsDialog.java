package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.actions.OrderExistsAction;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrdersScreenFactory;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderExistsPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrdersPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Xhibit2 OrderExistsDialog
 * </p>
 * <p>
 * Description: Builds the OrderExists screen
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

public class OrderExistsDialog extends XDialog {
    private static final Logger log = CSServices.getLogger(OrderExistsDialog.class);

    /**
     * Reference to the OrdersScreenFactory
     */
    private OrdersScreenFactory osf = null;

    /**
     * Reference to the OrderInitialDataVO
     */
    private OrderInitialDataVO model = null;

    private OrdersPanel orderPanel;

    private OrdersPanel orderExistsPanel;

    /**
     * Constructor for the OrderExistsDialog
     * 
     * @param f
     *            Frame (may be null)
     * @param title
     *            To be displayed
     * @param odm
     *            OrderInitialDataVO
     * @throws CSRecoverableException
     */
    public OrderExistsDialog(Frame f, String title, OrderInitialDataVO model, boolean replace)
            throws CSRecoverableException {
        super(f, title, true, XDialog.OKCANCEL, XDialog.DEFAULTOK);
        this.model = model;
        addListener();

        setLookAndFeel();

        orderExistsPanel = (OrdersPanel) buildPanel(replace);

        addBodyPanel(orderExistsPanel);
    }

    /**
     * Builds the panel containing the order data and returns it to the dialog
     * 
     * @return XPanel (OrderExistsPanel)
     * @throws CSRecoverableException
     */
    private XPanel buildPanel(boolean replace) throws CSRecoverableException {
        log.debug("OrderExistsDialog.buildPanel()");
        osf = OrdersScreenFactory.getinstance();
        return osf.getOrderExistsOptionScreen(this.model, replace);
    }

    /**
     * REturns the option chosen on th ecsreen
     * 
     * @return the option action command
     */
    public String getScreenOption() {
        return ((OrderExistsPanel) orderExistsPanel.getDetailPanel()).getOptionGroup().getSelection()
                .getActionCommand();
    }

    private void addListener() {
        getOKButton().addActionListener(new OrderExistsAction(this));
    }

    /**
     * Returns the OK button from the XDialog
     * 
     * @return JButton
     */
    private JButton getOKButton() {
        Component[] components = this.getButtonPanel().getComponents();
        JButton btn = null;

        for (int i = 0; i < components.length; i++) {
            if ((components[i] instanceof JButton) && (((JButton) components[i]).getActionCommand().equals("OK"))) {
                btn = (JButton) this.getButtonPanel().getComponent(i);
                break;
            }
        }
        return btn;
    }

    /**
     * Returns the model
     * 
     * @return the model
     */
    public OrderInitialDataVO getModel() {
        return this.model;
    }

    /**
     * Has the dialog been cancelled
     * 
     * @return true if cancelled
     */
    public boolean isCancelled() {
        return ((OrderExistsPanel) orderExistsPanel.getDetailPanel()).isCancelled();
    }

    /**
     * Sets the look and feel to match that of the main application
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logLookAndFeelError(ex);
        }
    }

    private void logLookAndFeelError(Exception ex) {
        log.error("OrderGUI: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " " + ex.getMessage());

    }
}
