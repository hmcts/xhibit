package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.util.OrdersListTableModel;

/**
 * <p>
 * Title: OrderTableSelectionListener
 * </p>
 * <p>
 * Description: Listens on a table selection and on a checkbox. The table
 * displays a list of Orders and, if one is selected, the order reference is set
 * on the model. This also enables/disables a button depending on whether a row
 * and/or the checkbox have been selected.
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

public class OrderTableSelectionListener implements ListSelectionListener, ActionListener {
    private static final Logger log = CSServices.getLogger(OrderTableSelectionListener.class);

    private ListSelectionModel listSelModel;

    private OrderInitialDataVO model;

    private OrdersListTableModel ordLstTblMod;

    // The button to be enabled/disabled
    private JButton dlgButton;

    // boolean to indicate that a row has been selected from the table
    boolean rowSelected = false;

    // boolean to indicate that the chcekbox has been selected
    boolean cBoxSelected = false;

    /**
     * Constructor
     * 
     * @param table
     *            The table to listen to
     * @param oidvo
     *            The model
     * @param button
     *            The button to enable/disable
     */
    public OrderTableSelectionListener(JTable table, OrderInitialDataVO model, JButton button) {
        this.listSelModel = table.getSelectionModel();
        this.model = model;
        this.ordLstTblMod = (OrdersListTableModel) table.getModel();
        this.dlgButton = button;
    }

    /**
     * Handle the ListSelectionEvent firsed from the table
     * 
     * @param lse
     *            The ListSelectionEvent
     */
    public void valueChanged(ListSelectionEvent lse) {
        // Only listen to the main event - miss out the intermediate events
        if (!lse.getValueIsAdjusting()) {
            // Set the selectedOrder to be the selcted row
            this.model.setSelectedOrder((Integer) ordLstTblMod.getValueAt(listSelModel.getLeadSelectionIndex(), 9));
            // if no row is selected, set rowSelected to false
            rowSelected = !listSelModel.isSelectionEmpty();
        }
        checkEnabled();
    }

    /**
     * 
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            cBoxSelected = ((JCheckBox) e.getSource()).isSelected();
            checkEnabled();
        }
    }

    /**
     * Enable/disable the Okay button depending on whether or not a row and/or
     * the checkbox has been selected.
     */
    private void checkEnabled() {
        if (dlgButton != null) {
            dlgButton.setEnabled(rowSelected || cBoxSelected);
        }
    }
}