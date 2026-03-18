package uk.gov.courtservice.xhibit.xmlbinding.orders;
 
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
 

/**
 * <p>
 * Title: Instance of XmlHelper that populates the Order Castor object for a
 * 'BreachConditionalDischarge ' order.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Instance of XmlHelper that populates the Order Castor object for a 'BreachConditionalDischarge' order.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author J Parthiban
 * @version 1.0
 */
//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class BreachConditionalDischargeXmlHelper extends OrderXMLHelper {

    /**
     * Simple constructor instantiating a functional instance of XML helper for
     * a 'BreachConditionalDischarge ' order.
     * 
     * @param typeCode
     *            the type code for this particular order.
     */
    public BreachConditionalDischargeXmlHelper(String typeCode) {
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
        OrderHeaderHelper.populateHeader(order.getOrderData().getBreachConditionalDischargeOrder().getOrderHeader(), docEntity, this.getTypeCode());
        order.getOrderData().getBreachConditionalDischargeOrder().setAssociatedCases(
                AssociatedCasesHelper.populateAssociatedCases(order.getOrderData().getBreachConditionalDischargeOrder()
                        .getAssociatedCases(), docEntity.getCaze()));
    }

    /**
     * Overridden to provide logical validation of the order.
     * 
     * @param helper
     *            Class encapsulating the order to be validated and any errors
     *            or warnings to be entered against it.
	 * @param defendantOnCaseId					(Not used)
     */
    protected void logicalValidation(Integer defendantOnCaseId, ValidationHelper helper) {
        BreachConditionalDischargeOrderHelper.logicalValidateBreachConditionalDischarge(helper.getOrderToBeValidated().getOrderData().getBreachConditionalDischargeOrder(), this.getTypeCode(), helper);
    }

    protected String structuralValidation(ValidationHelper helper) throws ValidationException, MarshalException,
            OrderXMLException {
        return BreachConditionalDischargeOrderHelper.structuralValidateBreachConditionalDischarge(helper.getOrderToBeValidated().getOrderData().getBreachConditionalDischargeOrder(), this.getTypeCode(), helper);
    }
}