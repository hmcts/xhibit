/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 2, 2003
 * Time: 1:45:39 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.gui.general.buttons;

import java.io.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.OrderFactory;
import uk.gov.courtservice.xhibit.client.order.exceptions.IllegalOrderStateException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderWriterException;
import uk.gov.courtservice.xhibit.client.order.gui.general.OrderGUI;
import uk.gov.courtservice.xhibit.client.order.io.OrderWriter;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Helper class with methods to set and get the current order status. Various
 * stages of the order process can be obtained as boolean or integer values.
 */
public class ButtonHelper {
    private static final Logger log = CSServices.getLogger(ButtonHelper.class);

    private OrderStatus status;

    private XMLOrderData orderData;

    private XhbOrderValue orderValue;

    private XhibitApplicationController controller;

    private OrderGUI mainGUI;

    /**
     * Return the current application controller
     * 
     * @return The current XhibitApplicationController
     */
    public XhibitApplicationController getController() {
        return controller;
    }

    /**
     * Retruns the Logger for the helper
     * 
     * @return The current Logger
     */
    public Logger getLog() {
        return log;
    }

    /**
     * Sets the application controller
     * 
     * @param controller
     */
    public void setController(XhibitApplicationController controller) {
        this.controller = controller;
    }

    /**
     * Returns the object representin the current order
     * 
     * @return The XhbOrderValue as retrieved from the mid tier
     */
    public XhbOrderValue getOrderValue() {
        return orderValue;
    }

    /**
     * Public constructor
     * 
     * @param status
     *            The Status of the order
     * @param data
     *            The OrderData (dom) that represents the order
     * @param value
     *            The value object holding the data
     * @param xac
     *            The XhibitApplicationController
     * @param gui
     *            The Screens that display the order
     */
    public ButtonHelper(OrderStatus status, OrderData data, XhbOrderValue value, XhibitApplicationController xac,
            OrderGUI gui) {
        this(status);
        this.orderData = (XMLOrderData) data;
        orderValue = value;
        setController(xac);
        setGUI(gui);
        setStatus(orderValue.getXhbOrderStatus().getOrderStatusId().intValue());
    }

    /**
     * Public constructor
     * 
     * @param status
     *            The order status
     */
    public ButtonHelper(OrderStatus status) {
        this.status = status;
    }

    /**
     * Returns the OrderData encapsulating the dom
     * 
     * @return The OrderData object
     */
    public OrderData getOrderData() {
        return this.orderData;
    }

    /**
     * Returns the Status of the order
     * 
     * @return The orderStatus - NEW, SAVED, PRINTED, SIGNED
     */
    public OrderStatus getStatus() {
        return this.status;
    }

    /**
     * Returns the int representation of the status 1 = NEW, 2 = SAVED, 3 =
     * PRINTED, 4 = SIGNED
     * 
     * @return the int representing the status
     */
    public int getStatusAsInt() {
        return status.getStatus();
    }

    /**
     * Is the Order SAVED?
     * 
     * @return true if saved
     */
    public boolean isSaved() {
        return status.isSaved();
    }

    /**
     * Is the Order SIGNED?
     * 
     * @return true if signed
     */
    public boolean isSigned() {
        return status.isSigned();
    }

    /**
     * Is teh Order PRINTED?
     * 
     * @return true if printed
     */
    public boolean isPrinted() {
        return status.isPrinted();
    }
    
    /**
     * Is the Order SENT?
     * 
     * @return true if printed
     */
    public boolean isSent() {
        return status.isSent();
    }

    /**
     * Sets order status to one of the possible integer values within the range
     * 0-5. Attempting to set the status to a value outwith this range will
     * result in an IllegalOrderStateException.
     * 
     * @param i
     *            order status where 0<=i<=6.
     */
    public void setStatus(int i) {
        if ((i >= 0) && (i < 7)) {
            status.setStatus(i);
        } else {
            throw new IllegalOrderStateException("OrderStatus int value is outwith range");
        }
    }

    /**
     * Save the order
     * 
     * @throws OrderXMLException
     * @throws OrderException
     */
    public void saveOrder() throws OrderXMLException, OrderException {
        log.debug("orderValue.getOrderId(): " + orderValue.getOrderId());
        String xmlAsString = getXMLAsString(orderData);
        xmlAsString = formatISO8859Chars(xmlAsString);
        orderValue.setDataXml(xmlAsString);
        log.debug("Save Order XML: " + getXMLAsString(orderData));
        try {
            //Save order details
            orderValue = XhibitDelegateHelper.getOrdersDelegate().saveOrder(orderValue);
        } catch (OrderException ex) {
            log.error(ex);
            throw ex;
        } catch (OrderXMLException ex) {
            log.error(ex);
            throw ex;
        } finally {
        }
        log.debug("Save Order status: " + orderValue.getXhbOrderStatus().getCode());
        log.debug("Save Order: " + orderValue.toString());
        reloadOrder(orderValue.getOrderId());
    }
    

    /**
     * Reloads the order by doing another fetch fro the mid tier
     * 
     * @param orderId
     *            The id of the order to be retrieved
     * @throws CSUnrecoverableException
     * @throws OrderException
     */
    public void reloadOrder(Integer orderId) throws CSUnrecoverableException, OrderException {
        log.debug("reloadOrder: " + orderId);
        orderValue = XhibitDelegateHelper.getOrdersDelegate().getOrder(orderId);
    }

    /**
     * Signs the order
     * 
     * @throws OrderException
     * @throws OrderXMLException
     */
    public void signOrder() throws OrderException, OrderXMLException {
        signOrder(orderData);
    }
    
    /**
     * Sends the order
     * 
     * @throws OrderException
     * @throws OrderXMLException
     */
    public void sendOrder(byte[] orderPdf) throws OrderException, OrderXMLException {
        sendOrder(orderData, orderPdf);
    }

    /**
     * Signs the order
     * 
     * @param data
     *            the OrderData object to sign
     * @throws OrderException
     * @throws OrderXMLException
     */
    public void signOrder(OrderData data) throws OrderException, OrderXMLException {
        try {
            orderValue.setDataXml(getXMLAsString(data));
            log.debug("Sign Order XML: " + getXMLAsString(orderData));
            final String userDisplayName = XhibitSingleton.getInstance().getUserSession().getUserName();
            orderValue = XhibitDelegateHelper.getOrdersDelegate().signOrder(orderValue, userDisplayName);
        } catch (OrderException ex) {
            log.error(ex);
            throw ex;
        } catch (OrderXMLException ex) {
            log.error(ex);
            throw ex;
        } finally {
            // Retrieve the saved order from the database
            reloadOrder(orderValue.getOrderId());
            // Set the OrderData to be the temporary OrderData
            orderData = (XMLOrderData) data;
        }
    }
    
    /**
     * Sends the order
     * 
     * @param data
     *            the OrderData object to send
     * @throws OrderException
     * @throws OrderXMLException
     */
    public void sendOrder(OrderData data, byte[] pdfOrder) throws OrderException, OrderXMLException {
        try {
        	String xmlAsString = getXMLAsString(data);
        	xmlAsString = formatISO8859Chars(xmlAsString);
            orderValue.setDataXml(xmlAsString);
            log.debug("Send Order XML: " + getXMLAsString(orderData));
            orderValue = XhibitDelegateHelper.getOrdersDelegate().sendOrder(orderValue, pdfOrder);
        } catch (OrderException ex) {
            log.error(ex);
            throw ex;
        } catch (OrderXMLException ex) {
            log.error(ex);
            throw ex;
        } finally {
            // Retrieve the saved order from the database
            reloadOrder(orderValue.getOrderId());
            // Set the OrderData to be the temporary OrderData
            orderData = (XMLOrderData) data;
        }
    }
    
    private String formatISO8859Chars(String inString) {
    	String outString = inString.replaceAll("&#xA3;", "£");
    	outString = outString.replaceAll("&amp;#xA3;", "£");
    	//outString = outString.replaceAll("Ã*Â", "");
    	//outString = outString.replaceAll("[ÃÂ]", "");
    	
        StringBuffer outStringB = new StringBuffer(outString);
        int startChar;
        do {
        	startChar = outStringB.indexOf("Ã‚Â");
        	if (startChar > 0) {
        		outStringB = outStringB.replace(startChar, startChar+3, "");
        	}
        } while (outStringB.indexOf("Ã‚Â") > 0);
        
        do {
        	startChar = outStringB.indexOf("Â");
        	if (startChar > 0) {
        		outStringB = outStringB.replace(startChar, startChar+1, "");
        	}
        } while (outStringB.indexOf("Â") > 0);
        
        startChar = outStringB.indexOf("UTF-8");
        if (startChar > 0) {
        	outStringB = outStringB.replace(startChar, startChar+5, "ISO-8859-1");
        }
        	
        return outStringB.toString();
    }
    

    /**
     * Print the order
     * 
     * @throws OrderException
     * @throws OrderXMLException
     */
    public void printOrder() throws OrderException, OrderXMLException {
        // orderValue.setDataXml(getXMLAsString(orderData));
        try {
            log.debug("Print Order status: " + orderValue.getXhbOrderStatus().getCode());
            log.debug("Print Order: " + orderValue.toString());
            log.debug("Print Order XML: " + getXMLAsString(orderData));
            if (orderValue.getOrderStatusId().intValue() == OrderStatus.SAVED) {
                orderValue.setDataXml(getXMLAsString(orderData));
                orderValue = XhibitDelegateHelper.getOrdersDelegate().printOrder(orderValue);
                reloadOrder(orderValue.getOrderId());
            }
        } catch (OrderException ex) {
            log.error(ex);
            throw ex;
        } catch (OrderXMLException ex) {
            log.error(ex);
            throw ex;
        } catch (CSUnrecoverableException ex) {
            log.error(ex);
            throw ex;
        }
    }

    /**
     * Retrieve the XML from the OrderData object
     * 
     * @param data
     *            the OrderData
     * @return the XML as a String
     */
    private String getXMLAsString(OrderData data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String result = null;
        try {
            OrderWriter writer = OrderFactory.getWriter(outputStream);
            writer.write(data);
            result = outputStream.toString();
        } catch (OrderWriterException e) {
            log.error(e);
        }
        return result;
    }

    /**
     * Return the gui component
     * 
     * @return the current OrderGUI
     */
    public OrderGUI getGUI() {
        return mainGUI;
    }

    /**
     * Sets the gui component
     * 
     * @param gui
     *            the orders gui
     */
    public void setGUI(OrderGUI gui) {
        this.mainGUI = gui;
    }

    /**
     * Performs any logging of subclasses
     * 
     * @param buttonName
     *            he button that is to be logged
     * @param message
     *            The message to log
     */
    private void log(String buttonName, String message) {
        log.debug(buttonName + ": " + message);
    }

    /**
     * Description: Checks if a deportation reason has been entered if the deportation check box is checked.
     * @return boolean
     */
    public boolean validDeportation(){
        boolean validated = false;
        String order = getXMLAsString(orderData);
        
        // Retrieve Deportation reasons tag
        int trueIndex = order.indexOf("<ord:DeportationSection selected=\"true\">");
        
        // selected
        if (trueIndex != -1){
            // blank reason present
            int blankReason = order.indexOf("<ord:Reason> </ord:Reason>");
            if (blankReason == -1){
                validated = true;
            }
        }else{
            validated = true;
        }
        
        return validated;
    }

}
