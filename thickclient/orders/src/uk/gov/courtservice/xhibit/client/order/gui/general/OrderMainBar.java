package uk.gov.courtservice.xhibit.client.order.gui.general;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.gui.general.buttons.ButtonComponent;
import uk.gov.courtservice.xhibit.client.order.gui.general.buttons.ButtonFactory;
import uk.gov.courtservice.xhibit.client.order.gui.general.buttons.ButtonHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.util.Resource;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * 
 * <p>
 * Title: OrderMainBar
 * </p>
 * <p>
 * Description: Tool bar for displaying the "print", "cancel" and "sign" buttons
 * along the south border of the main Gui.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public class OrderMainBar extends JPanel {

    private static final Logger log = CSServices.getLogger(OrderMainBar.class);

    private static final String[] labelsNonMonetaryOrD20Order = {
        ResourceHelper.getResourceString(OrderGUI.BUTTON_SAVE_LBL),
        ResourceHelper.getResourceString(OrderGUI.BUTTON_SIGN_LBL),
        ResourceHelper.getResourceString(OrderGUI.BUTTON_PRINT_LBL),
        ResourceHelper.getResourceString(OrderGUI.BUTTON_CANCEL_LBL)
    };
    
    private static final String[] labelsMonetaryOrder = {
        ResourceHelper.getResourceString(OrderGUI.BUTTON_SAVE_LBL),
        ResourceHelper.getResourceString(OrderGUI.BUTTON_SEND_LBL),
        ResourceHelper.getResourceString(OrderGUI.BUTTON_PRINT_LBL),
        ResourceHelper.getResourceString(OrderGUI.BUTTON_CANCEL_LBL)
    };
    
    private static final String[] labelsD20Order = {
        ResourceHelper.getResourceString(OrderGUI.BUTTON_SAVE_LBL),
        ResourceHelper.getResourceString(OrderGUI.BUTTON_SIGN_LBL),
        ResourceHelper.getResourceString(OrderGUI.BUTTON_PRINT_LBL),
        ResourceHelper.getResourceString(OrderGUI.BUTTON_CANCEL_LBL)
    };

    private JToolBar toolBarToolBar = new JToolBar();

    private OrderStatus status;

    private ButtonComponent[] buttons = new ButtonComponent[4];

    private JComboBox scale;

    private XhbOrderValue orderValue;

    private XhibitApplicationController xController;

    OrderData orderData = null;

    OrderGUI mainGUI = null;

    /**
     * Creates tool bar and adds it to the parent panel.
     * 
     * @param status
     *            The order status
     * @param scale
     *            The scale combobox
     * @param data
     *            The dom
     * @param value
     *            The value object containing the order details
     * @param xac
     *            The XhibitApplicationController
     * @param gui
     *            The Orders screens
     */
    public OrderMainBar(OrderStatus status, JComboBox scale, OrderData data, XhbOrderValue value,
            XhibitApplicationController xac, OrderGUI gui) {
        this.scale = scale;
        orderValue = value;
        xController = xac;
        mainGUI = gui;
        setLookAndFeel();
        this.setStatus(status);
        this.orderData = data;
        createButtons();
        initBar();
    }

    /**
     * Set the status of the order
     * 
     * @param status
     *            The order status
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * Returns the status of the order
     * 
     * @return the status
     */
    public OrderStatus getStatus() {
        return this.status;
    }

    /**
     * Creates the buttons
     */
    private void createButtons() {
        ButtonHelper helper = new ButtonHelper(status, orderData, orderValue, xController, mainGUI);
        if (orderValue.getXhbOrderTemplate().getXhbOrderType().getCode().equals("MO")) {
            for (int i = 0; i <= 3; i++) {
                buttons[i] = ButtonFactory.createButtonComponent(labelsMonetaryOrder[i]);
                buttons[i].init(helper);
            }
        } else if (orderValue.getXhbOrderTemplate().getXhbOrderType().getCode().equals("D20")) {
            for (int i = 0; i <= 3; i++) {
                buttons[i] = ButtonFactory.createButtonComponent(labelsD20Order[i]);
                buttons[i].init(helper);
            }
        } else {
            for (int i = 0; i <= 3; i++) {
                buttons[i] = ButtonFactory.createButtonComponent(labelsNonMonetaryOrD20Order[i]);
                buttons[i].init(helper);
            }
        }
    }

    /**
     * Creates the tool bar using GridBag layout to position the included
     * buttons.
     */
    private void initBar() {
        JPanel barPanel = addButtons(buttons);
        toolBarToolBar.setFloatable(false); // Des Johnston SCR 52675
        toolBarToolBar.add(barPanel);
    }

    /**
     * Adds the buttons to the bar
     * 
     * @param buttons
     *            An array of ButtonComponents - the buttons
     * @return The panel containing the buttons
     */
    private JPanel addButtons(ButtonComponent[] buttons) {
        JPanel barPanel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        barPanel.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i <= 3; i++) {
            c.gridx = i;
            barPanel.add((JButton) buttons[i].getVisualButton(), c);
        }
        c.gridx = 4;
        JLabel scaleLabel = new JLabel(Resource.getOrdersClientBundle("panel.zoom"));
        // 52738
        barPanel.add(scaleLabel, c);
        c.gridx = 5;
        barPanel.add(scale, c);

        return barPanel;
    }

    /**
     * @return the toolbar contained within the main bar
     */
    public JToolBar getToolBar() {
        return this.toolBarToolBar;
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
        log.error("OrderMainBar: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " " + ex.getMessage());

    }
}