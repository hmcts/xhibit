package uk.gov.courtservice.xhibit.xmlbinding.orders.factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderNotSupportedException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.business.helpers.CopyOrderHelper;

/**
 * <p>
 * Title: Helper class that creates the order XML for a defendant on a case
 * </p>
 * <p/> Description: This class uses the factory/strategy patterns to accomplish
 * the ability to add a new order type without recompiling and editing all the
 * code.
 * </p>
 * <p/> When a new type of order is created, follow this recipe:
 * <ul>
 * <li> Implement XmlHelper for the order, with a no argument constructor. </li>
 * <li> Add a new entry to the xml helpers properties file defining the order
 * type code and the classname to be used to instantiate it. </li>
 * </ul>
 * This class will automatically instantiate the helper and use it to create the
 * order data XML.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 * @see XmlHelper
 */
public class XmlHelperFactory {
    private java.util.Properties helperProperties;

    private Map helpers;

    /**
     * This constructor expects to be passed the relevant properties for the xml
     * helper classes.
     * 
     * @param helperProperties
     *            the properties defining the xml helper classes.
     */
    public XmlHelperFactory(java.util.Properties helperProperties) {
        this.helperProperties = helperProperties;
        helpers = new HashMap();
    }
    
    /**
     * This method returns the XML for a given order.
     * 
     * @param defendantOnCaseId
     *            The defendant on a given case for whom to create an order.
     * @param orderTypeCode
     *            The particular order type to be created, a text code as
     *            defined by Crest and Xhibit.
     * @return The populated XML.
     * @throws OrderNotSupportedException
     *             When there is a problem with the factory.
     * @throws OrdeXMLException
     *             When there is a problem within the xml creation.
     */
    public String getDataXmlForOrder(Integer caseId, Integer xhibitCaseId, String caseTitle, Integer courtId, boolean isBCase, String orderTypeCode)
            throws OrderNotSupportedException, OrderXMLException {
        XmlHelper helper = getHelper(orderTypeCode);

        // Use helper to create the XML.
        return helper.getOrderXML(caseId, xhibitCaseId, caseTitle, courtId, isBCase);
    }

    /**
     * This method returns the XML for a given order.
     * 
     * @param defendantOnCaseId
     *            The defendant on a given case for whom to create an order.
     * @param orderTypeCode
     *            The particular order type to be created, a text code as
     *            defined by Crest and Xhibit.
     * @return The populated XML.
     * @throws OrderNotSupportedException
     *             When there is a problem with the factory.
     * @throws OrdeXMLException
     *             When there is a problem within the xml creation.
     */
    public String getDataXmlForOrder(Integer defendantOnCaseId, String orderTypeCode, String monetaryDisposalInfoForDisplayOnOrder, String monetaryTotalsForDisplayOnOrder)
            throws OrderNotSupportedException, OrderXMLException {
        XmlHelper helper = getHelper(orderTypeCode);

        // Use helper to create the XML.
        if (monetaryDisposalInfoForDisplayOnOrder == null) { // Not a monetary order
            return helper.getOrderXML(defendantOnCaseId);
        } else {
            return helper.getOrderXML(defendantOnCaseId, monetaryDisposalInfoForDisplayOnOrder, monetaryTotalsForDisplayOnOrder);
        }
    }

    /**
     * This method returns the XML for a given order.
     * 
     * @param defendantOnCaseId
     *            The defendant on a given case for whom to create an order.
     * @param orderTypeCode
     *            The particular order type to be created, a text code as
     *            defined by Crest and Xhibit.
     * @return The populated XML.
     * @throws OrderNotSupportedException
     *             When there is a problem with the factory.
     * @throws OrdeXMLException
     *             When there is a problem within the xml creation.
     */
    public String getDataXmlHelper(String orderTypeCode) throws OrderNotSupportedException, OrderXMLException {
        XmlHelper helper = getHelper(orderTypeCode);
        
        return helper.getOrderXML();
    }
    
    /**
     * This method returns the XML for a copy of a given order.
     * 
     * @param defendantOnCaseId
     *            The defendant on a given case for whom to create an order.
     * @param orderTypeCode
     *            The particular order type to be created, a text code as
     *            defined by Crest and Xhibit.
     * @return The populated XML.
     * @throws OrderNotSupportedException
     *             When there is a problem with the factory.
     * @throws OrdeXMLException
     *             When there is a problem within the xml creation.
     */
    public String getDataXmlForOrder(Integer defendantOnCaseId, String orderTypeCode, String originalXML,
            Integer originalTypeId) throws OrderNotSupportedException, OrderXMLException {
        XmlHelper helper = getHelper(orderTypeCode);

        // Use helper to create the XML.
        return helper.getOrderXML(defendantOnCaseId, originalXML, originalTypeId);
    }

    /**
     * This method is used to validate a given order.
     * 
     * @param defendantOnCaseId
     * @param orderXML
     *            the xml to validate
     * @param orderTypeCode
     *            the order type
     * @param validationLevel
     *            how to valdate the order
     * @return the validated xml
     * @throws OrderNotSupportedException
     *             When the order type cannot be found.
     * @throws OrderValidationException
     *             When there is a problem with the order, the exception can be
     *             used to check whether a critical or non-critical failure
     *             occurred.
     * @throws OrderXMLException
     *             when the xml cannot be marshalled
     * @see XmlHelper
     */
    public String validate(Integer defendantOnCaseId, String orderXML, String orderTypeCode, int validationLevel)
            throws OrderNotSupportedException, OrderValidationException, OrderXMLException {
        XmlHelper helper = getHelper(orderTypeCode);

        // Use the helper to validate the XML.
        return helper.validateOrderXML(defendantOnCaseId, orderXML, validationLevel);
    }

    private XmlHelper getHelper(String orderTypeCode) throws OrderNotSupportedException {
        // Attempt to retrieve an already existing instance of the helper.
        Object obj = helpers.get(orderTypeCode);

        if (obj == null) // Failed retrieval.
        {
            try {
                // Lookup the classnmae and instantiate it.
                String className = helperProperties.getProperty(orderTypeCode);
                if (className == null) {
                    throw new OrderNotSupportedException("ORDER_***", "An order type was requested with code "
                            + orderTypeCode + " that is not currently supported by the application.");
                }

                Class helperClass = Class.forName(className);

                // if not an instance of XmlHelper then application
                // misconfigured.
                if (!XmlHelper.class.isAssignableFrom(helperClass)) {
                    throw new CSConfigurationException("An order type was requested with code " + orderTypeCode
                            + " whose configured helper is not an XmlHelper.");
                }

                // Setup the expected parameters...
                Class[] constructorParmTypes = { String.class };

                // Get the constructor.
                Constructor helperConstructor = helperClass.getConstructor(constructorParmTypes);

                // Pass the expected parameter in.
                Object[] constructorParms = { orderTypeCode };

                // Invoke the constructor and create a new instance of
                // XmlHelper.
                obj = helperConstructor.newInstance(constructorParms);

                helpers.put(orderTypeCode, obj); // Add helper to map of
                // created helpers.
            } catch (InstantiationException ie) // Could not instantiate.
            {
                throw new OrderNotSupportedException("ORDER_***", "An order type was requested with code "
                        + orderTypeCode + " whose helper cannot be instantiated.", ie);
            } catch (IllegalAccessException iae) // Not allowed to
            // instantiate.
            {
                throw new OrderNotSupportedException("ORDER_***", "An order type was requested with code "
                        + orderTypeCode + " whose helper is not allowed to be instantiated.", iae);
            } catch (ClassNotFoundException cnfe) // Class not found on
            // classpath.
            {
                throw new OrderNotSupportedException("ORDER_***", "An order type was requested with code "
                        + orderTypeCode + " whose defined helper class is not on the classpath.", cnfe);
            } catch (NoSuchMethodException nsme) // Expected constructor
            // does not exits.
            {
                throw new CSConfigurationException("An order type was requested with code " + orderTypeCode
                        + " whose configured helper does not implement a constructor "
                        + "with parameters looking like (String aString).", nsme);
            } catch (InvocationTargetException ite) // Constructor threw an
            // exception.
            {
                ite.getTargetException().printStackTrace();
                throw new CSConfigurationException("An order type was requested with code " + orderTypeCode
                        + " whose configured helper's constructor " + "threw an unexpected exception.", ite
                        .getTargetException());

            }
        }
        return (XmlHelper) obj;
    }
    
    
    /**
     * Go through the order and ensure that the data from the original order is populated into the new schema
     * @param originalOrder
     * @param newBlankOrder
     */
    public String copyDataIntoNewSchema(String originalOrder, String newBlankOrder) {
    	String tempNewOrder = new String();
    	
    	CopyOrderHelper coh = new CopyOrderHelper();
    	tempNewOrder = coh.copyDataIntoNewSchema(originalOrder, newBlankOrder);
    	
    	return tempNewOrder.toString();
    }
}