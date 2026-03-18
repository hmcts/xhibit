/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 2, 2003
 * Time: 1:39:54 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.general.buttons;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.exceptions.CancelledByUserException;
import uk.gov.courtservice.xhibit.client.order.exceptions.NoDataEnteredException;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSavedDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderValidationDialog;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderDialogFactory;
import uk.gov.courtservice.xhibit.client.order.util.Resource;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * Represents a JButton whose functionality is specific to saving the order as
 * it currently stands.
 */
public class Save extends AbstractButton implements PropertyChangeListener {
    private static final Logger log = CSServices.getLogger(Save.class);

    private JButton saveBtn;

    private OrderStatus status;

    /**
     * Creates the JButton, sets it as the visual component and add the 2
     * required listeners to detect any changes made to the OrderStatus and to
     * allow the user to directly alter the order status.
     */
    public void initButton() {
        this.status = getHelper().getStatus();
        this.status.addPropertyChangeListener(this);
        saveBtn = new JButton(Resource.getOrdersClientBundle("button.save"));
        saveBtn.setDefaultCapable(false);
        setVisualButton(saveBtn);
        saveBtn.addActionListener(this);
        determineEnabled(saveBtn);
    }

    /**
     * Determines whether the buttons functionality should be available or not
     * depending on the order status.
     */
    private void determineEnabled(JButton b) {
        b.setEnabled(!getHelper().isSigned() && !getHelper().isSent());
    }

    /**
     * Method to attempt to alter the status of the current order.
     * 
     * @param ae
     *            A semantic event.
     */
    public void actionPerformed(ActionEvent ae) {
        saveBtn.requestFocus();
        try {
            String result = showSaveDialog();
            log.debug("Description " + result);
            saveAndValidateOrder(result);
        } catch (CancelledByUserException ex) {
            // do nothing as dialog was cancelled
            log.debug("DIALOG CANCELLED BY USER");
        } catch (OrderException oe) {
            log.debug("ORDER EXCEPTION");
            handleError(oe);
        } catch (CSRecoverableException csre) {
            log.debug("CS RECOVERABLE EXCEPTION");
            handleError(csre);
        }
    }

    /**
     * Log the error via the framework
     * 
     * @param ex
     */
    private void handleError(Exception ex) {
        log.error(ex);
        XHIBITConstant.handleError(ex, Save.class);
    }

    /**
     * Save the order - the validation takes place as the first stage of saving.
     * 
     * @param result
     *            the saved description
     * @throws CSUnrecoverableException
     * @throws OrderException
     */
    private void saveAndValidateOrder(String result) throws CSUnrecoverableException, OrderException {
        try {
            if (getHelper().getOrderValue() != null) {
                // check to ensure deportation reason has been entered
                if (getHelper().validDeportation()){
                    processOrder(result);
                    getHelper().setStatus(OrderStatus.SAVED);
                }else{
                    JOptionPane.showMessageDialog(this,Resource.getOrdersClientBundle("order.save.error.deport.message"), 
                            Resource.getOrdersClientBundle("order.save.error.title"),1);
                }
            }
        } catch (OrderValidationException ove) {
            log.debug("$$$ OrderValidationException $$$");
            XhibitApplicationController xac = getHelper().getController();
            try {
                showValidationDialog(ove, xac);
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex, Save.class);
            } finally {
                // Do not do this after conversation with Alan Brightmore
                // Only if there's no validation errors can it be "SAVED"
                //resetOrder(ove);
                //getHelper().setStatus(OrderStatus.SAVED);
            }
        } catch (OrderException e) {
            log.error(e);
            XHIBITConstant.handleError(e, Save.class);
        } catch (OrderXMLException e) {
            log.error(e);
            XHIBITConstant.handleError(e, Save.class);
        }

    }

    /**
     * Reinstate the order narrative
     * 
     * @param ove
     *            the validation exception
     * @throws OrderException
     * @throws CSUnrecoverableException
     */
    private void resetOrder(OrderValidationException ove) throws OrderException, CSUnrecoverableException {
        log.debug("$$$ Finally ove.getSavedWithWarningsID() = $$$" + ove.getSavedWithWarningsID());
        getHelper().reloadOrder(ove.getSavedWithWarningsID());
        ((XMLOrderData) getHelper().getOrderData()).reinstateNarrativeAfterSave();
        getHelper().getGUI().getDisplayHelper().resumeRedisplay();
    }

    /**
     * Show the error/warning dialog to display validation errors
     * 
     * @param ove
     *            the validation exception
     * @param xac
     *            the controller
     * @throws CSRecoverableException
     */
    private void showValidationDialog(OrderValidationException ove, XhibitApplicationController xac)
            throws CSRecoverableException {
        OrderValidationDialog dialog = OrderDialogFactory.getInstance().getValidationDialog(xac, ove);
        dialog.setLocationRelativeTo(this.getRootPane());
        dialog.show();
    }

    /**
     * Strip the narrative, save the order and reinstate the narrative
     * 
     * @param result
     * @throws OrderException
     */
    private void processOrder(String result) throws OrderXMLException, OrderException {
        getHelper().getOrderValue().setDescription(result);
        getHelper().getGUI().getDisplayHelper().suspendRedisplay();
        ((XMLOrderData) getHelper().getOrderData()).removeNarrativeForSave();
        try {
            getHelper().saveOrder();
        } catch (OrderXMLException ex) {
            throw ex;
        } catch (OrderException ex) {
            throw ex;
        } finally {
            ((XMLOrderData) getHelper().getOrderData()).reinstateNarrativeAfterSave();
            getHelper().getGUI().getDisplayHelper().resumeRedisplay();
        }
    }

    /**
     * Upon any change to the order status, the button may become enabled or
     * disabled.
     * 
     * @param event
     *            delivered whenever order status changes.
     */
    public void propertyChange(PropertyChangeEvent event) {
        determineEnabled(saveBtn);
    }

    /**
     * Show the save dialog and return the entered description
     * 
     * @return the saved description
     * @throws CancelledByUserException
     * @throws CSRecoverableException
     */
    private String showSaveDialog() throws CancelledByUserException, CSRecoverableException {
        String result = null;

        OrderSavedDialog dialog = showDialog();

        result = getSavedDetails(dialog);
        dialog = null;
        return result;

    }

    /**
     * Display the dialog to capture the saved details
     * 
     * @return the dialog
     * @throws CSRecoverableException
     */
    private OrderSavedDialog showDialog() throws CSRecoverableException {
        // Get the single instance of the dialog
        OrderSavedDialog dialog = OrderDialogFactory.getInstance().getSavedDialog(getHelper().getController());
        dialog.setLocationRelativeTo(this.getRootPane());
        // If the order has a previous saved description, display it
        String orderDesc = getHelper().getOrderValue().getDescription();
        if (orderDesc != null) {
            dialog.getSavedPanel().getSaveDetails().setText(orderDesc);
        }
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Get the details captured by the save dialog and return them as a String
     * 
     * @param dialog
     *            the dialog
     * @return the saved description
     * @throws CancelledByUserException
     */
    private String getSavedDetails(OrderSavedDialog dialog) throws CancelledByUserException {
        try {
            return ((OrderSavedDialog) dialog).getSavedText();
        } catch (NoDataEnteredException nde) {
            return "";
        }
    }
}
