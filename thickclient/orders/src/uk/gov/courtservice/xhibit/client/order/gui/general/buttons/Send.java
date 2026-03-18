package uk.gov.courtservice.xhibit.client.order.gui.general.buttons;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XSLServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.OrderFactory;
import uk.gov.courtservice.xhibit.client.order.exceptions.CancelledByUserException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.gui.general.OrderGUI;
import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
import uk.gov.courtservice.xhibit.client.order.print.OrderEmailHelper;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSentConfirmedDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSentDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderSignedDialog;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderValidationDialog;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderDialogFactory;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.SentDetailsVO;
import uk.gov.courtservice.xhibit.client.order.screens.helper.SignedDetailsVO;
import uk.gov.courtservice.xhibit.client.order.util.Resource;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * 
 * <p>
 * Title: Send
 * </p>
 * <p>
 * Description: Represents a JButton whose functionality is specific to the
 * sending of a legal document as required by XHibit2.
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
public class Send extends AbstractButton implements PropertyChangeListener {

    private static final Logger log = CSServices.getLogger(Send.class);

    private JButton sendBtn;

    private OrderStatus status;

    private SentDetailsVO sentDetails = null;
    
    private OrderEmailHelper emailHelper;

    /**
     * Creates the JButton, sets it as the visual component and add the 2
     * required listeners to detect any changes made to the OrderStatus and to
     * allow the user to directly alter the order status.
     */
    public void initButton() {
        this.status = getHelper().getStatus();
        status.addPropertyChangeListener(this);
        sendBtn = new JButton(Resource.getOrdersClientBundle("button.send"));
        sendBtn.setDefaultCapable(false);
        setVisualButton(sendBtn);
        sendBtn.addActionListener(this);
        determineEnabled();
        try {
            // Amended to use XSLServices
            String xsltIS = this.getHelper().getOrderValue().getXhbOrderTemplate().getDisplayTransformName();
            log.debug("EMAIL &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + xsltIS);
            emailHelper = new OrderEmailHelper(xsltIS);
        } catch (OrderTransformException ex) {
            throw new CSUnrecoverableException("Could not initialise order transform for printing.", ex);
        }
    }

    /**
     * Determines whether the buttons functionality should be available or not
     * depending on the order status.
     */
    private void determineEnabled() {
    	// Saved and not already sent or signed
        //sendBtn.setEnabled(getHelper().isSaved() && !getHelper().isSent() && !getHelper().isSigned());
        sendBtn.setEnabled((getHelper().isPrinted()) // Printed
                && (getHelper().isPrinted()) // Saved
                && (!getHelper().isSent()) // Not sent
                && (!getHelper().isSigned())); // Not Signed
    }

    /**
     * Method to attempt to alter the status of the current order.
     * 
     * @param ae
     *            A semantic event.
     */
    public void actionPerformed(ActionEvent ae) {
        sendBtn.requestFocus();
        try {
        	sentDetails = showSignDialog();
            
            if(sentDetails.getSentDate() == null) {
            	Format formatter = new SimpleDateFormat("dd-MM-yyyy");
                String today = formatter.format(new Date());
                sentDetails.setSentDate(today);
            } else {
            	// Convert into correct format
            	try {
            		Date date = new SimpleDateFormat("dd-MMM-yyyy").parse(sentDetails.getSentDate());
            		String correctlyFormattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
            		sentDetails.setSentDate(correctlyFormattedDate);
            	} catch (ParseException pe) {
            		// Set to today
            		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String today = formatter.format(new Date());
                    sentDetails.setSentDate(today);
            	}
            }
            
            if (log.isDebugEnabled()) {
            	// List all nodes in the DOM
            	Document dom = getHelper().getGUI().getDisplayHelper().getDOM();
            	Element docEle = dom.getDocumentElement();
            	new XMLOrderData(dom).listAllNodes(docEle.getChildNodes());
            	//Transformer transformer2 = XSLServices.getInstance().getTransformer(this._transformName, Locale.getDefault(), map);
	            //StringWriter writer = new StringWriter();
	            //transformer2.transform(new DOMSource(dom), new StreamResult(writer));
	            //String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
	            //log.debug("xmlDomSource = "  + output);
            }
            
            XMLOrderData tempOrderData = null;
            XMLOrderData tempOrderData2 = null;
            // Create a copy of the OrderData and set the sent details on this.
            // If there is a problem sending the order, the gui will not
            // reflect the sending details
            log.debug("####################");
            log.debug(((XMLOrderData) getHelper().getOrderData()).toString());
            log.debug("####################");
            tempOrderData = (XMLOrderData) copyOrderData(getHelper().getOrderValue(), ((XMLOrderData) getHelper().getOrderData()));
            log.debug(tempOrderData.toString());
            log.debug("####################");
            tempOrderData2 = new XMLOrderData(getHelper().getGUI().getDisplayHelper().getDOM());
            tempOrderData2.setNarrativeDOM(getHelper().getGUI().getDisplayHelper().getNarrativeDOM());
            //tempOrderData2.mergeNarrative(((XMLOrderData) OrderFactory.getReader(narrativeInputStream).read()).getDom());
            tempOrderData2 = (XMLOrderData) copyOrderData(getHelper().getOrderValue(), tempOrderData2);

            tempOrderData = (XMLOrderData) setSentDetails(sentDetails, tempOrderData);
            tempOrderData2 = (XMLOrderData) setSentDetails(sentDetails, tempOrderData2);
            log.debug(tempOrderData2.toString());

            sendOrder(tempOrderData, tempOrderData2);
            //showSentConfirmation();
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
     * Send the Order
     * 
     * @param tempData
     * @throws OrderException
     * @throws OrderXMLException
     */
    private void sendOrder(OrderData tempData, OrderData tempData2) throws OrderException, OrderXMLException, CSRecoverableException {
        try {
            if (getHelper().getOrderValue() != null) {
                // Update the copy of OrderData with the sent details
                updateOrder(tempData, tempData2);
                //showSentConfirmation();
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
            XHIBITConstant.handleError(ex, Send.class);
        } finally {
            // Make sure that the narrative is put back into the order
            // and the redisplay is restarted
            ((XMLOrderData) getHelper().getOrderData()).reinstateNarrativeAfterSave();
            getHelper().getGUI().getDisplayHelper().resumeRedisplay();
        }
    }
    
    /**
     * Displays the Sent Order Confirmation
     * 
     * @return SentDetailsVO
     * @throws CancelledByUserException
     */
    private void showSentConfirmation() throws CancelledByUserException, CSRecoverableException {
        OrderSentConfirmedDialog dialog = OrderDialogFactory.getInstance().getSentConfirmedDialog(getHelper().getController());

        dialog.setLocationRelativeTo(this.getRootPane());
        dialog.setVisible(true);
    }

    /**
     * Update the order with the sent details
     * 
     * @param tempOrderData
     * @throws OrderException
     * @throws OrderXMLException
     */
    private void updateOrder(OrderData tempOrderData, OrderData tempOrderData2) throws OrderException, OrderXMLException {
        // As the redisplay of the order is controlled by the timer
        // thread, we need to stop this process while the dom
        // is updated.
        try {
            getHelper().getGUI().getDisplayHelper().suspendRedisplay();
            ((XMLOrderData) tempOrderData).removeNarrativeForSave();
            
            // For sending we need to keep the narrative - hence there is tempOrderData2
            byte[] pdfAsByteArray = this.emailHelper.createPDF(tempOrderData2);
            getHelper().setStatus(OrderStatus.SENT);
            getHelper().sendOrder(tempOrderData, pdfAsByteArray);
        } catch (OrderException ex) {
            log.error(ex);
            throw ex;
        } catch (OrderXMLException ex) {
            log.error(ex);
            throw ex;
        } catch (OrderTransformException ote) {
            log.error(ote);
        } catch (CSRecoverableException csre) {
            log.error(csre);
        } finally {
            ((XMLOrderData) getHelper().getOrderData()).reinstateNarrativeAfterSave();
            getHelper().getGUI().getDisplayHelper().setupDOM(getHelper().getOrderData());
            getHelper().getGUI().getDisplayHelper().resumeRedisplay();

        }

    }

    /**
     * Set the details of who sent the order.
     * 
     * @param details
     *            The value object containg the sent details
     * @param tempData
     *            the copy of OrderData to update
     * @return the updated OrderData object
     */
    private OrderData setSentDetails(SentDetailsVO details, OrderData tempData) {
        StringBuffer xPathRoot = new StringBuffer();
        xPathRoot.append(ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_REF));

        SimpleDateFormat xFormatter = new SimpleDateFormat("dd-MM-yyyy");
        ParsePosition xPos = new ParsePosition(0);

        java.util.Date xmlDate = xFormatter.parse(details.getSentDate(), xPos);
        SimpleDateFormat xFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
        String xFormattedDate = xFormatter2.format(xmlDate);
        log.debug("$$$ ORDERS***: Sign - Sent XML Date: " + xFormattedDate + " $$$");
        
        setupXpath(xPathRoot);

        setSentDetails(details, xPathRoot, xFormattedDate, tempData);

        return tempData;

    }

    /**
     * Set the Xpath reference
     * 
     * @param xPathRoot
     *            the XPATH reference
     */
    private void setupXpath(StringBuffer xPathRoot) {
        xPathRoot.append(ResourceHelper.getResourceString(OrderGUI.XPATH_SENT_CLERK_REF));
    }

    /**
     * Set the sent details on the dom
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
    private OrderData setSentDetails(SentDetailsVO details, StringBuffer xPathRoot, String xFormattedDate, OrderData tempData) {
        
        log.debug("$$$ ORDERS***: Sent - Sent Surname: " + details.getSentSurname() + " $$$");
        tempData = setDOM(xPathRoot + ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_SNAME_REF), details.getSentSurname(), tempData);
        log.debug("$$$ ORDERS***: Sent - Sent Forename: " + details.getSentForename() + " $$$");
        tempData = setDOM(xPathRoot + ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_FNAME_REF), details.getSentForename(), tempData);
        log.debug("$$$ ORDERS***: Sent - Sent Title: " + details.getSentTitle() + " $$$");
        tempData = setDOM(xPathRoot + ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_TITLE_REF), details.getSentTitle(), tempData);
        log.debug("$$$ ORDERS***: Sent - Sent Date: " + xFormattedDate + " $$$");
        tempData = setDOM(ResourceHelper.getResourceString(OrderGUI.XPATH_SIGN_DATE_REF), xFormattedDate, tempData);
        String user = XhibitSingleton.getInstance().getUserSession().getUserName();
        log.debug("$$$ ORDERS***: Sent - User: " + user + " $$$");
        getHelper().getOrderValue().setSignedBy(user); //Using signed by field for sent data

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ParsePosition pos = new ParsePosition(0);

        java.util.Date xhibitDate = formatter.parse(details.getSentDate(), pos);
        log.debug("$$$ ORDERS***: Sent - Sent VO Date: " + xhibitDate.toString() + " $$$");

        getHelper().getOrderValue().setSigningDate(new Timestamp(xhibitDate.getTime()));
        return tempData;
    }

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
    private SentDetailsVO showSignDialog() throws CancelledByUserException, CSRecoverableException {
        OrderSentDialog dialog = OrderDialogFactory.getInstance().getSentDialog(false, getHelper().getController());

        dialog.setLocationRelativeTo(this.getRootPane());
        dialog.setVisible(true);

        try {
            sentDetails = ((OrderSentDialog) dialog).getSignDetails();
        } catch (CancelledByUserException cbue) {
            log.debug("CancelledByUserException " + cbue.getMessage());
            throw cbue;
        }
        dialog = null;
        return sentDetails;
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

        // Amend encoding from UTF-8 to ISO-8859-1 for dealing with dodgy chars
        String tempDataXml = amendEncoding(value.getDataXml());
        OrderReader reader = OrderFactory.getReader(new ByteArrayInputStream(tempDataXml.getBytes()));
        try {
        	orderData = reader.read();
            ((XMLOrderData) orderData).mergeNarrative(((XMLOrderData) data).getNarrativeDOM());
        } catch (OrderReaderException ex) {
            log.error(ex);
            throw new CSRecoverableException("order.validation.general.error",
                    "Could not create a copy of the OrderData object", ex);
        }
        return orderData;
    }
    
    private String amendEncoding(String inString) {
    	StringBuffer outString = new StringBuffer(inString);
    	int startChar = inString.indexOf("UTF-8");
    	if (startChar>0)
    		outString = outString.replace(startChar, startChar+5, "ISO-8859-1");
    	
    	return outString.toString();
    }

}