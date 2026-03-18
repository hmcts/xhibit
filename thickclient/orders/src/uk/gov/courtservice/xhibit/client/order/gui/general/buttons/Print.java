package uk.gov.courtservice.xhibit.client.order.gui.general.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.print.OrderPrintHelper;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderValidationDialog;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderDialogFactory;
import uk.gov.courtservice.xhibit.client.order.util.Resource;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * 
 * <p>
 * Title: Print
 * </p>
 * <p>
 * Description: Print button to process the order once Print is selected
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public class Print extends AbstractButton implements PropertyChangeListener, ActionListener {
    private JButton printBtn;

    private OrderStatus status;

    private OrderPrintHelper printHelper;

    private static final Logger log = CSServices.getLogger(Print.class);

    // private PrintScreen screen;

    /**
     * Creates the JButton, sets it as the visual component and add the 2
     * required listeners to detect any changes made to the OrderStatus and to
     * allow the user to directly alter the order status.
     */
    public void initButton() {
        this.status = getHelper().getStatus();
        status.addPropertyChangeListener(this);
        printBtn = new JButton(Resource.getOrdersClientBundle("button.print"));
        setVisualButton(printBtn);
        printBtn.addActionListener(this);
        determineEnabled(printBtn);
        try {
            // Amended to use XSLServices
            String xsltIS = this.getHelper().getOrderValue().getXhbOrderTemplate().getDisplayTransformName();
            log.debug("PRINT &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + xsltIS);
            printHelper = new OrderPrintHelper(xsltIS);
        } catch (OrderTransformException ex) {
            throw new CSUnrecoverableException("Could not initialise order transform for printing.", ex);
        }
    }

    /**
     * Determines whether the buttons functionality should be available or not
     * depending on the order status.
     * 
     * @param b
     */
    private void determineEnabled(JButton b) {
        b.setEnabled(getHelper().getStatus().isSaved());
    }

    /**
     * Method to attempt to alter the status of the current order.
     * 
     * @param ae
     *            A semantic event.
     */
    public void actionPerformed(ActionEvent ae) {
        printBtn.requestFocus();
        try {
            if (getHelper().getOrderValue() != null) {
                getHelper().getGUI().getDisplayHelper().suspendRedisplay();
                ((XMLOrderData) getHelper().getOrderData()).removeNarrativeForSave();
                getHelper().printOrder();
                getHelper().setStatus(OrderStatus.PRINTED);
                ((XMLOrderData) getHelper().getOrderData()).reinstateNarrativeAfterSave();
                getHelper().getGUI().getDisplayHelper().resumeRedisplay();
                print();
            }
        } catch (OrderValidationException ove) {
            // Validation failure - display dialog
            displayValidationDialog(ove);
        } catch (OrderTransformException e) {
            log.debug("Print OrderTransformException " + e.getMessage());
            log.error(e);
            XHIBITConstant.handleError(e, Print.class);
        } catch (OrderException e) {
            log.debug("Print OrderException " + e.getMessage());
            log.error(e);
            XHIBITConstant.handleError(e, Print.class);
        } catch (OrderXMLException e) {
            log.debug("Print OrderException " + e.getMessage());
            log.error(e);
            XHIBITConstant.handleError(e, Print.class);
        } catch (CSRecoverableException e) {
            log.debug("Print CSRecoverableException " + e.getMessage());
            log.error(e);
            XHIBITConstant.handleError(e, Print.class);
        }
    }

    /**
     * Display the validation errors and warnings
     * 
     * @param ove
     *            the ordervalidationexception
     */
    private void displayValidationDialog(OrderValidationException ove) {
        // If
        log.debug("$$$ OrderValidationException $$$");
        XhibitApplicationController xac = getHelper().getController();
        try {
            OrderValidationDialog dialog = OrderDialogFactory.getInstance().getValidationDialog(xac, ove);
            dialog.setLocationRelativeTo(this.getRootPane());
            dialog.show();
        } catch (CSRecoverableException ex) {
            log.debug("Print CSRecoverableException " + ex.getMessage());
            XHIBITConstant.handleError(ex, Print.class);
        } finally {
            ((XMLOrderData) getHelper().getOrderData()).reinstateNarrativeAfterSave();
            getHelper().getGUI().getDisplayHelper().resumeRedisplay();
            this.requestFocus();
        }
    }

    /**
     * Transform the order and display the print dialog
     * 
     * @throws OrderTransformException
     */
    private void print() throws OrderTransformException, CSRecoverableException {
        // Pass the OrderData to the PrintHelper to make sure that any updates
        // to the DOM are printed
        this.printHelper.printTransform(this.getHelper().getOrderData());
        this.requestFocus();
    }

    /**
     * Upon any change to the order status, the button may become enabled or
     * disabled.
     * 
     * @param event
     *            delivered whenever order status changes.
     */
    public void propertyChange(PropertyChangeEvent event) {
        log.debug("$$$ Print: propertyChange");
        determineEnabled(printBtn);
    }
}
