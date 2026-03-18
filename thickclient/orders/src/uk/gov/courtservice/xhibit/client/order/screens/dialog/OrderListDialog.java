package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrdersScreenFactory;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderListPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrdersPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Xhibit2 OrderListDialog
 * </p>
 * <p>
 * Description: Builds the OrderList screen
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

public class OrderListDialog extends XDialog {

    private static final Logger log = CSServices.getLogger(OrderListDialog.class);

    private static final String OK_ACTION_CMD = "OK";

    /**
     * Reference to the OrdersScreenFactory
     */
    private OrdersScreenFactory osf = null;

    private OrdersPanel orderListPanel;

    /**
     * Reference to the OrderInitialDataVO
     */
    private OrderInitialDataVO model = null;

    /**
     * Constructor
     * 
     * @param f
     *            Parent Frame
     * @param title
     * @param odm
     *            OrderInitialDataVO - the model
     */
    public OrderListDialog(Frame f, String title, OrderInitialDataVO model, boolean replace, boolean copy)
            throws CSRecoverableException {
        super(f, title, true, XDialog.OKCANCEL, XDialog.DEFAULTOK);
        this.model = model;

        // Make sure that the consistant L&F is set
        setLookAndFeel();

        orderListPanel = (OrdersPanel) buildPanel(replace, copy);
        addBodyPanel(orderListPanel);
    }

    private XPanel buildPanel(boolean replace, boolean copy) throws CSRecoverableException {
        log.debug("START - buildPanel");
        log.debug("Replace: " + replace);
        log.debug("Copy: " + copy);
        osf = OrdersScreenFactory.getinstance();
        JButton button = getButton(OK_ACTION_CMD);
        button.setEnabled(false);
        return osf.getOrderListScreen(this.model, button, replace, copy);
    }

    /**
     * If the dialog has been cancelled, this method will return true
     * 
     * @return boolean True if cancelled
     */
    public boolean isCancelled() {
        return ((OrderListPanel) orderListPanel.getDetailPanel()).isCancelled();
    }

    /**
     * Returns the OK button from the XDialog
     * 
     * @return JButton
     */
    private JButton getButton(String btnType) {
        Component[] components = this.getButtonPanel().getComponents();
        JButton btn = null;

        for (int i = 0; i < components.length; i++) {
            if ((components[i] instanceof JButton) && (((JButton) components[i]).getActionCommand().equals(btnType))) {
                btn = (JButton) this.getButtonPanel().getComponent(i);
                break;
            }
        }
        return btn;
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
        log.error("OrderListDialog: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " "
                + ex.getMessage());

    }
}
