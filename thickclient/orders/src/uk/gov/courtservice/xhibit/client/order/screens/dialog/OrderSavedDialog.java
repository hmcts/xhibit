package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.Frame;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.CancelledByUserException;
import uk.gov.courtservice.xhibit.client.order.exceptions.NoDataEnteredException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrdersScreenFactory;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderSavedPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrdersPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Xhibit2 OrderSavedDialog
 * </p>
 * <p>
 * Description: Builds Order Saved screen
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

public class OrderSavedDialog extends XDialog {
    private static final OrdersScreenFactory osf = OrdersScreenFactory.getinstance();

    private static final String ORDER_SAVED_PANEL_TITLE = "OrderSavedTitle";

    private static final Logger log = CSServices.getLogger(OrderSavedDialog.class);

    private OrdersPanel orderSavedPanel;

    public OrderSavedDialog(Frame f, String title) throws CSRecoverableException {
        super(f, title, true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

        orderSavedPanel = (OrdersPanel) buildPanel();
        addBodyPanel(orderSavedPanel);
    }

    private XPanel buildPanel() throws CSRecoverableException {
        return osf.getOrderSavedScreen();
    }

    /**
     * Returns the text from the dialog to the parent
     * 
     * @return String containing the Order Saved details
     * @throws NoDataEnteredException
     *             If no data is entered
     */
    public String getSavedText() throws NoDataEnteredException, CancelledByUserException {
        String details = ((OrderSavedPanel) orderSavedPanel.getDetailPanel()).getOrderSaveDetails();

        // No saved details entered - may be okay
        if (details.equals("")) {
            throw new NoDataEnteredException("No saved details entered");
        }
        return details;
    }

    /**
     * Gets the detail panel of the Order Saved Dialog to enable the saved text
     * to be reset.
     * 
     * @return The detail panel from the Order Saved Dialog
     */
    public OrderSavedPanel getSavedPanel() {
        return (OrderSavedPanel) orderSavedPanel.getDetailPanel();
    }
}