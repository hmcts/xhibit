/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 27, 2002
 * Time: 6:26:03 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.xml.sax;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.gui.entry.DataEntryPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentFactory;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentHelper;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderComboCourts;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;

/**
 * <p>
 * Title: DataEntryFormSAXHandler
 * </p>
 * <p>
 * Description: Orders specif implemntation of a SAX event handler
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Ellis & Neil Entwistle
 * @version 1.0
 */
public class DataEntryFormSAXHandler extends AbstractSAXHandler implements ContentHandler {
    private static final Logger log = CSServices.getLogger(DataEntryFormSAXHandler.class);

    private DataEntryPanel form = new DataEntryPanel();

    private OrderData orderData;

    private boolean formStarted = false;

    private Vector componentStack = new Vector();

    private Vector nameStack = new Vector();

    private Vector componentStore = new Vector();

    /**
     * Constructor
     * 
     * @param orderData
     *            Object encapsulating the dom
     */
    public DataEntryFormSAXHandler(OrderData orderData) {
        this.orderData = orderData;
    }

    /**
     * @throws SAXException
     * @todo use this properly!
     */
    public void endDocument() throws SAXException {
    }

    /**
     * Implementation of endElement
     * 
     * @param URI
     * @param localName
     * @param qName
     * @throws SAXException
     */
    public void endElement(String URI, String localName, String qName) throws SAXException {

        if (localName.equals("form")) {
            formStarted = false;
        }

        // throw runtime in else (except for the nameStack.size()
        if (nameStack.size() > 0 && nameStack.lastElement().equals(localName)) {
            nameStack.removeElementAt(nameStack.size() - 1);
            componentStack.removeElementAt(componentStack.size() - 1);
        }

    }

    /**
     * Empty implementation of startDocument
     * 
     * @throws SAXException
     */
    public void startDocument() throws SAXException {
    }

    /**
     * Implementation of startDocument
     * 
     * @param URI
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    public void startElement(String URI, String localName, String qName, Attributes attributes) throws SAXException {
        if (formStarted) {
            try {
                OrderComponent component = OrderComponentFactory.createComponent(localName);
                
                componentStore.add(component);

                if ((componentStack.size() != 0) && (componentStack.lastElement() instanceof OrderOption)) {
                    component.init(storeAttributes(attributes, localName), true, (OrderOption) componentStack
                            .lastElement());
                    ((OrderOption) componentStack.lastElement()).addChild(component);
                } else {
                    component.init(storeAttributes(attributes, localName), false, null);
                    if(component instanceof OrderComboCourts && component.getHelper().getOrderDataReference().contains("//ord:D20"))
                    {
                    	((OrderComboCourts)component).setEnabled(false);
                    	
                    }
                }
     
                addFormComponents(component, localName);
                
                
            } catch (Exception e) {
                log.error("Orders exception while parsing XML: localName of element " + localName +
                        " qName: " + qName, e);
                throw new SAXException(e);
            } catch (Throwable t) {
                log.error("Orders throwable while parsing XML: localName of element" + localName +
                        " qName: " + qName , t);
                throw new SAXException("RuntimeException: " + t + " occured.");
            }
        } else if (localName.equals("form")) {
            formStarted = true;
        }
    }

    /**
     * Add components to form
     * 
     * @param component
     *            the component to add
     * @param localName
     *            the local reference
     */
    private void addFormComponents(OrderComponent component, String localName) {
        if (componentStack.size() != 0) {
            form.addComponent((OrderComponent) componentStack.lastElement(), component);
        } else {
            form.addComponent(component);
        }

        componentStack.add(component);
        nameStack.add(localName);
       
    }

    /**
     * Store attribute values in the dom
     * 
     * @param attributes
     *            attribute to store
     * @param localName
     *            the local reference
     * @return the helper
     */
    private OrderComponentHelper storeAttributes(Attributes attributes, String localName) {
        String reference = attributes.getValue("ref");
        OrderComponentHelper helper = new OrderComponentHelper(orderData, reference, attributes);
        return helper;
    }

    /**
     * Empty implementation of startPrefixMapping
     * 
     * @param s
     * @param s1
     * @throws SAXException
     */
    public void startPrefixMapping(String s, String s1) throws SAXException {
    }

    /**
     * Return the data entry panel
     * 
     * @return the entry panel
     */
    public DataEntryPanel getEntryTemplate() {
        // throw an exception if the document is not ended.
        form.setEnabled(true);
        return form;
    }

}
