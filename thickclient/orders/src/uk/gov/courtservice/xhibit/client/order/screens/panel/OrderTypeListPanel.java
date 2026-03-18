package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderComboBoxListener;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderInitialDataHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;

/**
 * <p>
 * Title: Xhibit2 OrderTypeListPanel
 * </p>
 * <p>
 * Description: Displays list of order types
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

public class OrderTypeListPanel extends AbstractOrdersPanel {
    private static final Logger log = CSServices.getLogger(OrderTypeListPanel.class);

    private static final String ORDER_PANEL_TITLE = "Order.Type.Panel.Title";

    private static final String ORDER_TYPE_LABEL = "Order.Type.Label";

    private static final String ORDER_CBOX_LABEL = "Order.Type.CBox.Label";

    private JComboBox comboBox;

    private JCheckBox checkBox;

    private OrderInitialDataHelper helper = null;

    private OrderComboBoxListener listener;

    /**
     * Constructor
     * 
     * @param odm
     *            the model
     * @throws CSRecoverableException
     */
    public OrderTypeListPanel(OrderInitialDataVO model) throws CSRecoverableException {
        super(model);

        helper = model.getHelper();

        loadOrderTypeLists();

        initialisePanel();

        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), ResourceHelper
                .getResourceString(ORDER_PANEL_TITLE)));
    }

    /**
     * Framework method - initialises the combo box
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        log.debug("Order Type Panel stepInitialise");
        // Only set the Order Type if the defendant name has also
        // been set
        if (null != model.getDefendantName() && !model.getDefendantName().trim().equals("")) {
            this.getOrderTypeList().setSelectedIndex(0);
        }
    }

    /**
     * Framework method - update the model
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        log.debug("Order Type Panel stepDeactivate");
        updateModel();
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        log.debug("Order Type Panel stepValidate");
    }

    /**
     * Framework method - sets combo box
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        log.debug("Order Type Panel stepUpdateViewState");
        // If no order type selected set to first on list, otherwise set to
        // selected
        if (this.model.getOrderType() == null // OrderType is null
                || // OR
                this.model.getOrderType().substring(0, 0).equals(" ")// OrderType
                // is
                // space
                || // OR
                this.model.getOrderType().equals("")) // OrderType is empty
        {
            getOrderTypeList().setSelectedIndex(0);
        } else {
            getOrderTypeList().setSelectedItem(this.model.getOrderType());
        }

    }

    /**
     * Framework method
     * 
     * @param parm1
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws CSRecoverableException {
        log.debug("Order Type Panel stepDeinitialise");
    }

    /**
     * Framework method
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        log.debug("Order Type Panel stepActivate");
    }

    /**
     * Initialise the panel
     * 
     * @throws CSRecoverableException
     */
    protected void initialisePanel() throws CSRecoverableException {
        super.initialisePanel();
        addComponents();
    }

    /**
     * Add the compoinents to the panel
     */
    private void addComponents() {
        add(getOrderTypeLabel(), getConstraints());
        getConstraints().gridx++;
        add(getOrderTypeList(), getConstraints());
        getConstraints().gridx++;
    }

    /**
     * Return the order type label
     * 
     * @return the label
     */
    private JLabel getOrderTypeLabel() {
        return new JLabel(ResourceHelper.getResourceString(ORDER_TYPE_LABEL));
    }

    /**
     * Return the order type combo box
     * 
     * @return the combo box
     */
    public JComboBox getOrderTypeList() {
        if (comboBox == null) {
            // Des Johnston - SCR 52564
            
            comboBox = new JComboBox(this.model.getHelper().getCurrentOrderTypes());

            // Des Johnston - SCR 52564
            comboBox.setSelectedIndex(0);

            comboBox.setEditable(false);
            comboBox.setMaximumRowCount(10);
            comboBox.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, COMBO_BOX_HEIGHT*2));
        }
        return comboBox;
    }

    /**
     * Return the order check box
     * 
     * @return the check box
     */
    public JCheckBox getOrderCheckBox() {
        if (checkBox == null) {
            checkBox = new JCheckBox();
            checkBox.setSelected(false);
            checkBox.setText(ResourceHelper.getResourceString(ORDER_CBOX_LABEL));
        }
        return checkBox;
    }

    /**
     * Add a listener to the panel
     * 
     * @param cBL
     *            the listener
     */
    public void addListener(OrderComboBoxListener cBL) {
        this.listener = cBL;
        getOrderTypeList().addActionListener(cBL);
    }

    /**
     * Return the listener
     * 
     * @return the listener
     */
    public OrderComboBoxListener getListener() {
        return listener;
    }

    /**
     * Update the model with the selected Order Type
     */
    public void updateModel() {
        // No implementation
    }

    /**
     * Load all the order types from the middle tier
     */
    private void loadOrderTypeLists() {
        this.model.getHelper().getCurrentOrderTypes();
    }

    /**
     * Set the panel data from the model
     * 
     * @param odm
     *            the model
     */
    public void setPanelFromModel(OrderInitialDataVO model) {
        getOrderTypeList().setSelectedItem(model.getOrderType());
    }

    /**
     * Set the reference to the model
     * 
     * @param odm
     *            the model
     */
    public void setModel(OrderInitialDataVO model) {
        this.model = model;
        this.getListener().setModel(model);
    }

}