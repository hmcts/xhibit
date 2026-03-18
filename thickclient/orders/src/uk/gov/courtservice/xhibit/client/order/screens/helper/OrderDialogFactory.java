package uk.gov.courtservice.xhibit.client.order.screens.helper;

import java.awt.Dimension;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSavedDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSentConfirmedDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSentDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSignedDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderValidationDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: OrderDialogFactory
 * </p>
 * <p>
 * Description: Builds instances of the Order Dialogs
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

public class OrderDialogFactory {

    private static final String ORDER_SAVED_TITLE = "order.save.dlg.title";

    private static final String ORDER_SIGNED_TITLE = "order.sign.dlg.title";
    
    private static final String ORDER_SENT_TITLE = "order.send.dlg.title";
    
    private static final String ORDER_SENT_CONFIRMED_TITLE = "order.send.confirmed.dlg.title";

    private static final Logger log = CSServices.getLogger(OrderDialogFactory.class);

    private static OrderDialogFactory factory = null;

    private OrderValidationDialog validationDialog;

    private OrderSavedDialog savedDialog;

    private OrderSignedDialog signedDialog;
    
    private OrderSentDialog sentDialog;
    
    private OrderSentConfirmedDialog sentConfirmedDialog;

    static {
        factory = new OrderDialogFactory();
    }

    public static OrderDialogFactory getInstance() {
        return factory;
    }

    /**
     * Returns an instance of OrderSavedDialog
     * 
     * @return OrderSavedDialog
     */
    public OrderSavedDialog getSavedDialog(XhibitApplicationController xac) throws CSRecoverableException {
        if (savedDialog == null) {
            savedDialog = new OrderSavedDialog(xac, ResourceHelper.getResourceString(ORDER_SAVED_TITLE));
            savedDialog.setSize(new Dimension(400, 300));
        } else {
            savedDialog.getSavedPanel().getSaveDetails().setText("");
            savedDialog.getSavedPanel().getSaveDetails().requestFocus();
        }
        return savedDialog;

    }

    /**
     * Returns an instance of OrderSignedDialog
     * 
     * @return OrderSignedDialog
     */
    public OrderSignedDialog getSignedDialog(boolean judgeToSign, XhibitApplicationController xac) throws CSRecoverableException {
        if (signedDialog == null) {
            signedDialog = new OrderSignedDialog(xac, ResourceHelper.getResourceString(ORDER_SIGNED_TITLE), judgeToSign, xac);
            signedDialog.setSize(new Dimension(400, 300));
        } else {
            signedDialog.populateDetails();
        }
        return signedDialog;
    }
    
    

    /**
     * Returns an instance of OrderValidationDialog
     * 
     * @return OrderValidationDialog
     */
    public OrderValidationDialog getValidationDialog(XhibitApplicationController xac, OrderValidationException ove) throws CSRecoverableException {
        if (validationDialog == null) {
            validationDialog = new OrderValidationDialog(ove, xac);
        } else {
            validationDialog.setMessages(ove);
        }
        return validationDialog;
    }
    
    /**
     * Returns an instance of OrderValidationDialog
     * 
     * @return OrderValidationDialog
     */
    public OrderSentConfirmedDialog getSentConfirmedDialog(XhibitApplicationController xac) throws CSRecoverableException {
        if (sentConfirmedDialog == null) {
            sentConfirmedDialog = new OrderSentConfirmedDialog(xac, ResourceHelper.getResourceString(ORDER_SENT_TITLE), true);
            sentConfirmedDialog.setSize(new Dimension(80, 50));
        } else {
            log.debug("OrderDialogFactory.getSentConfirmedDialog: dialog is null and we have no messages to populate");
            //sentConfirmedDialog.setMessages(ove);
        }
        return sentConfirmedDialog;
    }

	public OrderSentDialog getSentDialog(boolean b, XhibitApplicationController xac) throws CSRecoverableException {
		if (sentDialog == null) {
            sentDialog = new OrderSentDialog(xac, ResourceHelper.getResourceString(ORDER_SIGNED_TITLE), false, xac);
            sentDialog.setSize(new Dimension(400, 300));
        } else {
            sentDialog.populateDetails();
        }
        return sentDialog;
	}
}