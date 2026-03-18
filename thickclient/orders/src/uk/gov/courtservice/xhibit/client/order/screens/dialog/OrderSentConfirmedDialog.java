package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.WindowConstants;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.SignedDetailsVO;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderSentConfirmedPanel;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;


/**
 * <p>
 * Title: Xhibit2 OrderSentDialog
 * </p>
 * <p>
 * Description: Builds order sent screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */

public class OrderSentConfirmedDialog extends XDialog {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
 
    private OrderSentConfirmedPanel oscp;

    public OrderSentConfirmedDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal, XDialog.OK_ONLY, XDialog.DEFAULTOK);
        oscp = new OrderSentConfirmedPanel(this);

        this.addBodyPanel(oscp);
        (this.getButtonPanel()).getOkAction().setName(
                ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources, "btnOk"));
        pack();
        setResizable(false);
        setVisible(true);
    }
    

    public void setStatus(String message) {
        oscp.setStatus(message);
    }
}
