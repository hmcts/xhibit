package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrdersScreenFactory;
import uk.gov.courtservice.xhibit.client.order.screens.helper.SignedDetailsVO;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit2 OrderSignedDialog
 * </p>
 * <p>
 * Description: Builds order signed screen
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

public class OrderValidationDialog extends XDialog {
    private static final String OK_ACTION_CMD = "OK";

    private static final String CANCEL_ACTION_CMD = "Cancel";

    private static final String VALIDATION_DIALOG_TITLE = "order.validation.dialog.title";

    private OrderValidationModel model;

    private OrdersScreenFactory osf = null;

    private SignedDetailsVO signedVO = null;

    private boolean judgeToSign = false;

    private static Logger log;

    static {
        log = CSServices.getLogger(OrderSignedDialog.class);
    }

    public OrderValidationDialog(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac, XHIBITConstant.getResource(XHIBITConstant.getResourceBundle(XhibitBundles.OrdersValidation),
                VALIDATION_DIALOG_TITLE), true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public OrderValidationDialog(OrderValidationException errors, XhibitApplicationController xac)
            throws CSRecoverableException {
        this(xac);

        populatePanel(errors);
    }

    private void populatePanel(OrderValidationException errors) throws CSRecoverableException {
        log.debug("$$$ Entering populatePanel()");
        log.debug("$$$ no components = |" + this.getComponentCount() + "|");
        model = new OrderValidationModel(errors.getErrors(), errors.getWarnings());

        this.bodyPanel = new OrderValidationPanel(this, model);
        getButton(CANCEL_ACTION_CMD).setVisible(false);
        this.getContentPane().add(bodyPanel);
        // super.addBodyPanel( bodyPanel );
        this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
        super.pack();
        log.debug("$$$ Leaving populatePanel()");
    }

    public void setMessages(OrderValidationException errors) throws CSRecoverableException {
        log.debug("$$$ Entering setMessages()");
        if (bodyPanel != null) {
            log.debug("$$$ Removing bodyPanel");
            log.debug("$$$ no components before = |" + this.getContentPane().getComponentCount() + "|");
            this.getContentPane().removeAll();
            bodyPanel = null;
            log.debug("$$$ no components after = |" + this.getContentPane().getComponentCount() + "|");
            validate();
        }

        this.populatePanel(errors);
        log.debug("$$$ Leaving setMessages()");
    }

    /**
     * Returns the OK button from the XDialog
     * 
     * @return JButton
     */
    private JButton getButton(String btnType) {
        Component[] components = this.getButtonPanel().getComponents();
        JButton btn = null;

        for (int i = 0; i < components.length; i++) {
            if ((components[i] instanceof JButton) && (((JButton) components[i]).getActionCommand().equals(btnType))) {
                btn = (JButton) this.getButtonPanel().getComponent(i);
                break;
            }
        }
        return btn;
    }
}
