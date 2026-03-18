package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.CancelledByUserException;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderSignedListener;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrdersScreenFactory;
import uk.gov.courtservice.xhibit.client.order.screens.helper.SentDetailsVO;
import uk.gov.courtservice.xhibit.client.order.screens.helper.SignedDetailsVO;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderSignedPanel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrdersPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XPanel;
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

public class OrderSentDialog extends XDialog {
    private OrdersScreenFactory osf = null;

    private SentDetailsVO sentVO = null;

    private boolean judgeToSign = false;

    private OrdersPanel signPanel;

    private static Logger log;

    static {
        log = CSServices.getLogger(OrderSentDialog.class);
    }

    public OrderSentDialog(Frame f, String title, boolean judgeToSign, XhibitApplicationController xac)
            throws CSRecoverableException {
        super(f, title, true, XDialog.OKCANCEL, XDialog.DEFAULTOK);
        this.judgeToSign = false;

        sentVO = new SentDetailsVO();
        sentVO.setController(xac);

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setDetails();
    }

    /**
     * Sets the signing details on the signed value object
     * 
     * @throws CSRecoverableException
     */
    private void setDetails() throws CSRecoverableException {
        if (sentVO != null) {
        	sentVO.reset();
        } else {
        	sentVO = new SentDetailsVO();
        }
        signPanel = (OrdersPanel) buildPanel();
        ((OrderSignedPanel) signPanel.getDetailPanel()).setJudgeToSign(this.judgeToSign);
        try {
            signPanel.stepInitialise();
        } catch (CSRecoverableException ex) {
            log.error("Order Signatory Not Initialised");
        }
        addBodyPanel(signPanel);
    }

    /**
     * Resets the details held on the dialog
     */
    public void populateDetails() throws CSRecoverableException {
        if (signPanel != null) {
            this.remove(signPanel);
        }

        setDetails();
    }

    /**
     * Builds an OrderSignedPanel to be added to the dialog
     * 
     * @return XPanel
     */
    private XPanel buildPanel() throws CSRecoverableException {
        OrderSignedListener signListener = new OrderSignedListener(sentVO, getOKButton());
        osf = OrdersScreenFactory.getinstance();
        getOKButton().setEnabled(false);
        return osf.getOrderSignedScreen(signListener);
    }

    /**
     * Returns the OK button from the XDialog
     * 
     * @return JButton
     */
    private JButton getOKButton() {
        Component[] components = this.getButtonPanel().getComponents();
        JButton btn = null;

        for (int i = 0; i < components.length; i++) {
            if ((components[i] instanceof JButton) && (((JButton) components[i]).getText().equals("OK"))) {
                btn = (JButton) this.getButtonPanel().getComponent(i);
                break;
            }
        }
        return btn;
    }

    /**
     * Returns the signed details value object
     * 
     * @return SignedDetailsVO
     */
    public SentDetailsVO getSignDetails() throws CancelledByUserException {
        if (sentVO.getCancelledBy() == true) {
            throw new CancelledByUserException("OrderSignedDialog cancelled by user");
        }
        return sentVO;
    }
}
