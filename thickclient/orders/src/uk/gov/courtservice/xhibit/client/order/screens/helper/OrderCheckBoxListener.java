package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: Xhibit2 OrderCheckBoxListener
 * </p>
 * <p>
 * Description: Listens on a JCheckBox and sets a value on an instance of
 * OrderInitialDataVO. Also disables a JTable if selected.
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

public class OrderCheckBoxListener extends XAction {
    private OrderInitialDataVO model = null;

    private JTable ordTable = null;

    /**
     * Constructor
     * 
     * @param oidvo
     *            The model
     * @param table
     *            The table to listen to
     */
    public OrderCheckBoxListener(OrderInitialDataVO model, JTable table) {
        this.model = model;
        ordTable = table;
    }

    /**
     * If the checkbox is selected, disable the table and vice versa. If the
     * checkbox is selected, clear the table selection so that it is apparent
     * what is happening.
     * 
     * @param e
     *            The ActionEvent
     */
    public void xActionPerformed(ActionEvent e) {
        boolean createFlag = ((JCheckBox) e.getSource()).isSelected();
        this.model.setCreateOrder(createFlag);
        ordTable.setEnabled(!createFlag);
        if (createFlag) {
            ordTable.getSelectionModel().clearSelection();
        }
    }
}