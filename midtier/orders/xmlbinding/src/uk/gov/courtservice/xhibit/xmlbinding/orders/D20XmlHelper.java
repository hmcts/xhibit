package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;

/**
 * <p>
 * Title: Instance of XmlHelper that populates the Order Castor object for a
 * 'D20' order.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Instance of XmlHelper that populates the Order Castor object for a 'D20' order.
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
//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class D20XmlHelper extends OrderXMLHelper {

    private static Logger log = CSServices.getLogger(D20XmlHelper.class);

    /**
     * Simple constructor instantiating a functional instance of XML helper for
     * a 'Monetary Order' order.
     * 
     * @param typeCode
     *            the type code for this particular order.
     */
    public D20XmlHelper(String typeCode) {
        super(typeCode);
    }

    /**
     * Utility method that populates an Order XML structure using Castor objects
     * and the Xhibit entity beans.
     * 
     * @param order
     *            The Order to be populated.
     * @param docEntity
     *            The defendant on case EB from which to begin population.
     * @throws OrderXMLException
     *             When there is a problem in population.
     */
    protected void populateSchema(Order order, DefendantOnCase docEntity) throws OrderXMLException {
        // Populate the header.
        OrderHeaderHelper.populateHeader(order.getOrderData().getD20().getOrderHeader(), docEntity, this.getTypeCode());
    }

    /**
     * Overridden to provide logical validation of the order.
     * 
     * @param helper
     *            Class encapsulating the order to be validated and any errors
     *            or warnings to be entered against it.
	 * @param defendantOnCaseID				Used in detemining validity of the D20 order
     */
    protected void logicalValidation(Integer defendantOnCaseID, ValidationHelper helper) {
        D20OrderHelper.logicalValidateD20Order(helper.getOrderToBeValidated().getOrderData().getD20(), defendantOnCaseID, this.getTypeCode(), helper);
    }

    protected String structuralValidation(ValidationHelper helper) throws ValidationException, MarshalException,
            OrderXMLException {
        return D20OrderHelper.structuralValidateD20(helper.getOrderToBeValidated().getOrderData().getD20(), this.getTypeCode(), helper);
    }
    
 }