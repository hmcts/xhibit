package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSavedDialog;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
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

public class OrderSavedDialogAction extends XAction {

    public OrderSavedDialogAction() {
    }

    public OrderSavedDialogAction(OrderInitialDataVO model) {
        this();
        setModel(model);
    }

    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        XHIBITConstant.debug("OrderSavedDialogAction");
        OrderSavedDialog osDlg = new OrderSavedDialog(null, "OrdersavedDialog");
        osDlg.setSize(new Dimension(800, 600));
        osDlg.setVisible(true);

    }
}