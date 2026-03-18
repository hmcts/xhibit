package uk.gov.courtservice.xhibit.client.order.gui.general.buttons;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import javax.swing.JButton;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.OrderFactory;
import uk.gov.courtservice.xhibit.client.order.exceptions.CancelledByUserException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;
import uk.gov.courtservice.xhibit.client.order.gui.general.OrderGUI;
import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSignedDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderValidationDialog;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderDialogFactory;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.SignedDetailsVO;
import uk.gov.courtservice.xhibit.client.order.util.Resource;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * 
 * <p>
 * Title: Sign
 * </p>
 * <p>
 * Description: Represents a JButton whose functionality is specific to the
 * signing of a legal document as required by XHibit2.
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
public class Sign extends AbstractButton implements PropertyChangeListener {

    private static final Logger log = CSServices.getLogger(Sign.class);

    private JButton signBtn;

    private OrderStatus status;

    private SignedDetailsVO signDetails = null;

    /**
     * Creates the JButton, sets it as the visual component and add the 2
     * required listeners to detect any changes made to the OrderStatus and to
     * allow the user to directly alter the order status.
     */
    public void initButton() {
        this.status = getHelper().getStatus();
        status.addPropertyChangeListener(this);
        signBtn = new JButton(Resource.getOrdersClientBundle("button.sign"));
        signBtn.setDefaultCapable(false);
        setVisualButton(signBtn);
        signBtn.addActionListener(this);
        determineEnabled();
    }

    /**
     * Determines whether the buttons functionality should be available or not
     * depending on the order status.
     */
    private void determineEnabled() {
        signBtn.setEnabled((getHelper().isPrinted()) // Printed
                && // AND
                (!getHelper().isSigned())); // Signed
    }

    /**
     * Method to attempt to alter the status of the current order.
     * 
     * @param ae
     *            A semantic event.
     */
    public void actionPerformed(ActionEvent ae) {
        signBtn.requestFocus();
        try {
            signDetails = showSignDialog();
            XMLOrderData tempOrderData = null;
            // Create a copy of the OrderData and set the signed details on
            // this.
            // If there is a problem signing the order, the gui will not
            // reflect the signed
            // details
            tempOrderData = (XMLOrderData) copyOrderData(getHelper().getOrderValue(), ((XMLOrderData) getHelper()
                    .getOrderData()));

            tempOrderData = (XMLOrderData) setSignDetails(signDetails, isJudgeToSign(), tempOrderData);

            signOrder(tempOrderData);
            // setSignDetails(signDetails, isJudgeToSign(),
            // getHelper().getOrderData());
        } catch (CancelledByUserException ex) {
            // do nothing
        } catch (OrderException e) {
            // Report the exception to the framework
            log.error(e);
            XHIBITConstant.handleError(e, Save.class);
        } catch (OrderXMLException e) {
            // Report the exception to the framework
            log.error(e);
            XHIBITConstant.handleError(e, Save.class);
        } catch (CSRecoverableException csre) {
            // Report the exception to the framework
            log.error(csre);
            XHIBITConstant.handleError(csre, Save.class);
        }
    }

    /**
     * Sign the Order
     * 
     * @param tempData
     * @throws OrderException
     * @throws OrderXMLException
     */
    private void signOrder(OrderData tempData) throws OrderException, OrderXMLException {
        try {
            if (getHelper().getOrderValue() != null) {
                // Upadte the copy of OrderData with the signed datails
                updateOrder(tempData);
            }
        } catch (OrderValidationException ove) {
            showValidationDialog(ove);
        }
    }

    /**
     * Validation error/warnings have occurred so show the validation dialog
     * 
     * @param ove
     *            the OrdersValidationException
     */
    private void showValidationDialog(OrderValidationException ove) {
        log.debug("$$$ OrderValidationException $$$");
        XhibitApplicationController xac = getHelper().getController();
        try {
            OrderValidationDialog dialog = OrderDialogFactory.getInstance().getValidationDialog(xac, ove);
            dialog.setLocationRelativeTo(this.getRootPane());
            dialog.show();
        } catch (CSRecoverableException ex) {
            XHIBITConstant.handleError(ex, Sign.class);
        } finally {
            // Make sure that the narrative is put back into the order
            // and the redisplay is restarted
            ((XMLOrderData) getHelper().getOrderData()).reinstateNarrativeAfterSave();
            getHelper().getGUI().getDisplayHelper().resumeRedisplay();
        }
    }

    /**
     * Update the order with the signed details
     * 
     * @param tempOrderData
     * @throws OrderException
     * @throws OrderXMLException
     */
    private void updateOrder(OrderData tempOrderData) throws OrderException, OrderXMLException {
        // As the redisplay of the order is controlled by the timer
        // thread, we need to stop this process while the dom
        // is updated.
        try {
            getHelper().getGUI().getDisplayHelper().suspendRedisplay();
            ((XMLOrderData) tempOrderData).removeNarrativeForSave();
            getHelper().signOrder(tempOrderData);
            getHelper().setStatus(OrderStatus.SIGNED);
        } catch (OrderException ex) {
            log.error(ex);
            throw ex;
        } catch (OrderXMLException ex) {
            log.error(ex);
            throw ex;
        } finally {
            ((XMLOrderData) getHelper().getOrderData()).reinstateNarrativeAfterSave();
            getHelper().getGUI().getDisplayHelper().setupDOM(getHelper().getOrderData());
            getHelper().getGUI().getDisplayHelper().resumeRedisplay();

        }

    }

    /**
     * Set the details of who signed the order. For certain orders, currently
     * Bench Warrant, the judge must sign the order. Other oders can be signed
     * by the court clerks
     * 
     * @param details
     *            The value object containg the signing details
     * @param judge
     *            yes if the judge has to sign
     * @param tempData
     *            the copy of OrderData to update
     * @return the updated OrderData object
     */
    private OrderData setSignDetails(SignedDetailsVO details, boolean judge, OrderData tempData) {
        StringBuffer xPathRoot = new StringBuffer();
        xPathRoot.append(ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_REF));

        SimpleDateFormat xFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        ParsePosition xPos = new ParsePosition(0);

        java.util.Date xmlDate = xFormatter.parse(details.getSignedDate(), xPos);
        SimpleDateFormat xFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
        String xFormattedDate = xFormatter2.format(xmlDate);
        log.debug("$$$ ORDERS***: Sign - Signed XML Date: " + xFormattedDate + " $$$");

        setupXpath(judge, xPathRoot);

        setSignedDetails(details, xPathRoot, xFormattedDate, tempData);

        return tempData;

    }

    /**
     * Set the Xpath referenence depending on if a Judge is to sign the order
     * 
     * @param judge
     *            true if a Judge is to sign (BW)
     * @param xPathRoot
     *            the XPATH reference
     */
    private void setupXpath(boolean judge, StringBuffer xPathRoot) {
        if (judge) {
            xPathRoot.append(ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_JUDICIARY_REF));
            xPathRoot.append(ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_JUDGE_REF));
        } else {
            xPathRoot.append(ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_CLERK_REF));
        }
    }

    /**
     * Set he signed details on the dom
     * 
     * @param details
     *            The Value object holding the details
     * @param xPathRoot
     *            The xpath root node
     * @param xFormattedDate
     *            The correctly formatted date
     * @param tempData
     *            the copy of OrderData to update
     * @return the updated OrderData object
     */
    private OrderData setSignedDetails(SignedDetailsVO details, StringBuffer xPathRoot, String xFormattedDate,
            OrderData tempData) {
        log.debug("$$$ ORDERS***: Sign - Signed Surname: " + details.getSignedSurname() + " $$$");
        tempData = setDOM(xPathRoot + ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_SNAME_REF), details
                .getSignedSurname(), tempData);
        log.debug("$$$ ORDERS***: Sign - Signed Forename: " + details.getSignedForename() + " $$$");
        tempData = setDOM(xPathRoot + ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_FNAME_REF), details
                .getSignedForename(), tempData);
        log.debug("$$$ ORDERS***: Sign - Signed Title: " + details.getSignedTitle() + " $$$");
        tempData = setDOM(xPathRoot + ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_TITLE_REF), details
                .getSignedTitle(), tempData);
        log.debug("$$$ ORDERS***: Sign - Signed Date: " + xFormattedDate + " $$$");
        tempData = setDOM(ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_DATE_REF), xFormattedDate, tempData);
        String user = XhibitSingleton.getInstance().getUserSession().getUserName();
        log.debug("$$$ ORDERS***: Sign - User: " + user + " $$$");
        getHelper().getOrderValue().setSignedBy(user);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        ParsePosition pos = new ParsePosition(0);

        java.util.Date xhibitDate = formatter.parse(details.getSignedDate(), pos);
        log.debug("$$$ ORDERS***: Sign - Signed VO Date: " + xhibitDate.toString() + " $$$");

        getHelper().getOrderValue().setSigningDate(new Timestamp(xhibitDate.getTime()));
        return tempData;
    }

    /**
     */
    /**
     * Updates the dom with the new values
     * 
     * @param xPath
     *            The xpath node
     * @param value
     *            The value to set
     * @param tempData
     *            the copy of OrderData to update
     * @return the updated OrderData object
     */
    private OrderData setDOM(String xPath, String value, OrderData tempData) {
        log.debug("Xpath: " + xPath + " value: " + value);
        tempData.setValue(xPath, value);
        return tempData;
    }

    /**
     * Upon any change to the order status, the button may become enabled or
     * disabled.
     * 
     * @param event
     *            delivered whenever order status changes.
     */
    public void propertyChange(PropertyChangeEvent event) {
        determineEnabled();
    }

    /**
     * Displays the Sign Order Dialog
     * 
     * @return SignedDetailsVO
     * @throws CancelledByUserException
     */
    private SignedDetailsVO showSignDialog() throws CancelledByUserException, CSRecoverableException {
        OrderSignedDialog dialog = OrderDialogFactory.getInstance().getSignedDialog(isJudgeToSign(),
                getHelper().getController());

        dialog.setLocationRelativeTo(this.getRootPane());
        dialog.setVisible(true);

        try {
            signDetails = ((OrderSignedDialog) dialog).getSignDetails();
        } catch (CancelledByUserException cbue) {
            log.debug("CancelledByUserException " + cbue.getMessage());
            throw cbue;
        }
        dialog = null;
        return signDetails;
    }

    /**
     * Determines if a judge is to sign the order. As far as we know, the only
     * order that requires a judges signature is a Bench Warrant or the new
     * Warrant after Failure orders as of 2010401. 
     * This may change as other orders are introduced. The signatory details are held in
     * different locations in the schema for judges and everyone else. This is
     * why we need to determine if a judge is required, and hence the xpath
     * reference.
     * 
     * @return true if a judge has to sign the order - i.e the order is a BENCH
     *         WARRANT.
     */
    private boolean isJudgeToSign() {
        String benchWarrantOrderCode = ResourceHelper.getResourceString(OrderGUI.BENCH_WARRANT_CDE);
        String warrantAfterFailureOrderCode1 = ResourceHelper.getResourceString(OrderGUI.WARRANT_AFTER_FAILURE_CDE1);
        String warrantAfterFailureOrderCode2 = ResourceHelper.getResourceString(OrderGUI.WARRANT_AFTER_FAILURE_CDE2);
        String signedOrderCode =  getHelper().getOrderValue().getXhbOrderTemplate().getXhbOrderType().getCode();
        
        log.debug("BW ORDER CODE" + benchWarrantOrderCode);
        log.debug("BWA ORDER CODE" + warrantAfterFailureOrderCode1);
        log.debug("BWC ORDER CODE" + warrantAfterFailureOrderCode2);
               
        log.debug("Sign Order type : " + signedOrderCode);
        boolean judgeSign = false;
        /* Test to see if the current signing order is any kind of Warrant */
        judgeSign = ( 
                        signedOrderCode.equalsIgnoreCase(benchWarrantOrderCode)
                        || 
                        signedOrderCode.equalsIgnoreCase( warrantAfterFailureOrderCode1)
                        ||
                        signedOrderCode.equalsIgnoreCase( warrantAfterFailureOrderCode2)
                    );        
        return judgeSign;
    }

    /**
     * Creates a copy of an OrderData object. This allows us to try to save the
     * temporary copy, but revert back to the original version in case of
     * problems
     * 
     * @param value
     *            The XhbOrderValue
     * @param data
     *            the original OrderData object
     * @return the copy of the OrderData object
     * @throws CSRecoverableException
     */
    private OrderData copyOrderData(XhbOrderValue value, OrderData data) throws CSRecoverableException {
        OrderData orderData = null;

        try {
        	OrderReader reader = OrderFactory.getReader(new ByteArrayInputStream(value.getDataXml().getBytes("UTF8")));
        
            orderData = reader.read();
            ((XMLOrderData) orderData).mergeNarrative(((XMLOrderData) data).getNarrativeDOM());
        } catch (OrderReaderException ex) {
            log.error(ex);
            throw new CSRecoverableException("order.validation.general.error",
                    "Could not create a copy of the OrderData object", ex);
        } catch (UnsupportedEncodingException uee) {
        	log.error(uee);
        	throw new CSRecoverableException("order.validation.general.error",
                    "Could not create a copy of the OrderData object", uee);
        }
        return orderData;
    }

}