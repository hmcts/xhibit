package uk.gov.courtservice.xhibit.client.order.actions;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderExistsDialog;

/**
 * <p>
 * Title: OrderExistsAction
 * </p>
 * <p>
 * Description: Listens to OrderExistsDialog
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

public class OrderExistsAction extends OrderAction {
    private static final Logger log = CSServices.getLogger(OrderExistsAction.class);

    private OrderExistsDialog ordExistsDlg = null;

    public OrderExistsAction(OrderExistsDialog oed) {
        ordExistsDlg = oed;
    }

    /**
     * Controls the flow of the csreens between the OrderExistsDialog and the
     * rest of the orders screens
     * 
     * @param parm1
     *            ActionEvent
     * @throws java.lang.Exception
     */
    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        ordExistsDlg.getModel().setCVOption(ordExistsDlg.getScreenOption());
    }

    public void displayWizard() {
        //
    }

}