package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderExistsRButtonListener;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Xhibit2 OrderExistsPanel
 * </p>
 * <p>
 * Description: Displays options if an order exists
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

public class OrderExistsPanel extends AbstractOrdersPanel {
    private static final Logger log = CSServices.getLogger(OrderExistsPanel.class);

    private static final String ORDER_EXISTS_PANEL_TITLE = "OrderExistsPanelTitle";

    private static final String ORDER_EXISTS_MESSAGE1 = "OrderExistsMessage1";

    private static final String ORDER_EXISTS_MESSAGE2 = "OrderExistsMessage2";

    private static final String ORDER_EXISTS_MESSAGE3 = "OrderExistsMessage3";

    private static final String ORDER_EXISTS_MESSAGE_REPLACE = "OrderExistsMessageReplace";

    private static final String ORDER_EXISTS_RBUTTON1 = "OrderExistsCreateNew";

    private static final String ORDER_EXISTS_RBUTTON2 = "OrderExistsListOrders";

    private static final String ORDER_EXISTS_RBUTTON3 = "OrderExistsListBaseOrders";

    private static final boolean DEFAULT_BUTTON = true;

    private ButtonGroup optionGroup = null;

    private JRadioButton optCreate = null;

    private JRadioButton optView = null;

    private JRadioButton optReplace = null;

    private JLabel messageNoReplace = null;

    private JLabel messageReplace = null;

    private JLabel orderTypeLabel = null;

    private boolean cancelled = false;

    /**
     * The Order create action
     */
    public static final String ORDER_CREATE_ACTION = "0";

    /**
     * The Order view action
     */
    public static final String ORDER_VIEW_ACTION = "1";

    /**
     * The Order replace action
     */
    public static final String ORDER_REPLACE_ACTION = "2";

    /**
     * Constructor
     * 
     * @param odm
     *            the model
     * @throws CSRecoverableException
     */
    public OrderExistsPanel(OrderInitialDataVO model) throws CSRecoverableException {
        this.model = model;
        initialisePanel();
    }

    /**
     * Initialise the panel
     * 
     * @throws CSRecoverableException
     */
    protected void initialisePanel() throws CSRecoverableException {
        super.initialisePanel();

        add(getMessagePanel(), getConstraints());

        getConstraints().gridy++;

        getConstraints().gridx++;

        add(getRadioButtonPanel(), getConstraints());

        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), ResourceHelper
                .getResourceString(ORDER_EXISTS_PANEL_TITLE)));
    }

    /**
     * Returns the first section of the Order Exists message
     * 
     * @return the message
     */
    private JLabel getMessagePrefix() {
        return new JLabel(ResourceHelper.getResourceString(ORDER_EXISTS_MESSAGE1));
    }

    /**
     * 
     * @return
     */
    private JLabel getMessageNoReplace() {
        if (null == messageNoReplace) {
            messageNoReplace = new JLabel(ResourceHelper.getResourceString(ORDER_EXISTS_MESSAGE2));
        }
        return messageNoReplace;
    }

    /**
     * 
     * @return
     */
    private JLabel getMessageReplace() {
        if (null == messageReplace) {
            messageReplace = new JLabel(ResourceHelper.getResourceString(ORDER_EXISTS_MESSAGE_REPLACE));
        }
        return messageReplace;
    }

    /**
     * 
     * @return
     */
    private JLabel getMessage3() {
        return new JLabel(ResourceHelper.getResourceString(ORDER_EXISTS_MESSAGE3));
    }

    /**
     * Returns the order type to be displayed as part of the mesaaage
     * 
     * @return the order type
     */
    private JLabel getOrderType() {
        if (orderTypeLabel == null) {
            orderTypeLabel = new JLabel(this.model.getOrderType());
            orderTypeLabel.setFont(orderTypeLabel.getFont().deriveFont(Font.ITALIC));
        } else {
            log.debug("OrderExistsPanel.getOrderType = " + this.model.getOrderType());
            orderTypeLabel.setText(this.model.getOrderType());
        }
        log.debug("OrderExistsPanel.orderTypeLabel = " + orderTypeLabel.getText());
        return orderTypeLabel;
    }

    /**
     * Returns the message panel on which the mnesage is displayed
     * 
     * @return the panel
     * @throws CSRecoverableException
     */
    private XPanel getMessagePanel() throws CSRecoverableException {
        OrdersPanel messagePanel = new OrdersPanel(this.model);

        GridBagConstraints msgConstraints = messagePanel.getConstraints();
        // Set left and right insets to 0 to prevent
        // excess padding
        msgConstraints.insets = new Insets(4, 0, 4, 0);

        messagePanel.add(getMessagePrefix(), msgConstraints);
        msgConstraints.gridx++;
        messagePanel.add(getOrderType(), msgConstraints);
        msgConstraints.gridx++;
        messagePanel.add(getMessageReplace(), msgConstraints);
        msgConstraints.gridx++;
        messagePanel.add(getMessageNoReplace(), msgConstraints);
        msgConstraints.gridx++;
        msgConstraints.gridx++;
        messagePanel.add(getMessage3(), msgConstraints);

        return messagePanel;
    }

    /**
     * Returns the radio button panel to give the user the choices
     * 
     * @return the button panel
     * @throws CSRecoverableException
     */
    private XPanel getRadioButtonPanel() throws CSRecoverableException {
        OrdersPanel radioPanel = new OrdersPanel(this.model);

        GridBagConstraints radioConstraints = radioPanel.getConstraints();

        radioConstraints.gridy++;

        add(getCreateRButton(), radioConstraints);

        radioConstraints.gridy++;

        add(getViewRButton(), radioConstraints);

        radioConstraints.gridy++;

        add(getReplaceRButton(), radioConstraints);

        optionGroup = new ButtonGroup();
        optionGroup.add(getCreateRButton());
        optionGroup.add(getViewRButton());
        optionGroup.add(getReplaceRButton());

        return radioPanel;
    }

    /**
     * Sets the panel from the model
     * 
     * @param odm
     *            the model
     */
    public void setPanelFromModel(OrderInitialDataVO odm) {
        // No implementation
    }

    /**
     * Upadte the model
     */
    public void updateModel() {
        // No implementation
    }

    /**
     * Returns the create radio button
     * 
     * @return JRadioButton Create option
     */
    public JRadioButton getCreateRButton() {
        if (optCreate == null) {
            optCreate = new JRadioButton(ResourceHelper.getResourceString(ORDER_EXISTS_RBUTTON1), DEFAULT_BUTTON);
            optCreate.setActionCommand(ORDER_CREATE_ACTION);
            optCreate.addActionListener(new OrderExistsRButtonListener(optCreate, this.model));
            this.model.setCVOption(optCreate.getActionCommand());
        }
        return optCreate;
    }

    /**
     * Returns the view radio button
     * 
     * @return JRadioButton View option
     */
    private JRadioButton getViewRButton() {
        if (optView == null) {
            optView = new JRadioButton(ResourceHelper.getResourceString(ORDER_EXISTS_RBUTTON2));
            optView.setActionCommand(ORDER_VIEW_ACTION);
            optView.addActionListener(new OrderExistsRButtonListener(optView, this.model));
        }
        return optView;
    }

    /**
     * Returns the replace radio button
     * 
     * @return JRadioButton Replace option
     */
    public JRadioButton getReplaceRButton() {
        if (optReplace == null) {
            optReplace = new JRadioButton(ResourceHelper.getResourceString(ORDER_EXISTS_RBUTTON3));
            optReplace.setActionCommand(ORDER_REPLACE_ACTION);
            optReplace.addActionListener(new OrderExistsRButtonListener(optReplace, this.model));
            this.model.setCVOption(optReplace.getActionCommand());
        }
        return optReplace;
    }

    /**
     * Returns the option group
     * 
     * @return the group
     */
    public ButtonGroup getOptionGroup() {
        return optionGroup;
    }

    /**
     * Framework method - set the cancelled flag
     * 
     * @param parm1
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws CSRecoverableException {
        log.debug("OrderExistsPanel:stepDeinitialise" + parm1);
        setCancelled(!parm1);
    }

    /**
     * Return true if the dialog was cancelled
     * 
     * @return true if cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelled flag
     * 
     * @param can
     *            true if cancelled
     */
    public void setCancelled(boolean can) {
        cancelled = can;
    }

    /**
     * Resets the OrderType to be the current type
     */
    public void resetOrderType() {
        getOrderType();
    }

    public void setMessage(boolean replace) {
        getMessageNoReplace().setVisible(!replace);
        getMessageReplace().setVisible(replace);
    }
}