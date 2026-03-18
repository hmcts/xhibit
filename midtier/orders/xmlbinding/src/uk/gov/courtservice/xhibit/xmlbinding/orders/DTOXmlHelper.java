package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;

/**
 * <p>
 * Title: Instance of XmlHelper that populates the Order Castor object for an
 * 'Detention and Training' order.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Instance of XmlHelper that populates the Order Castor object for an
 * 'Detention and Training' order.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Brian Hingston
 * @version 1.0
 */
public class DTOXmlHelper extends OrderXMLHelper {

    private static Logger log = CSServices.getLogger(RemandXmlHelper.class);

    /**
     * Simple constructor instantiating a functional instance of XML helper for
     * an 'Detention and Training' order.
     * 
     * @param typeCode
     *            the type code for this particular order.
     */
    public DTOXmlHelper(String typeCode) {
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
       
        
//      Populate the header.
        if (log.isDebugEnabled()){log.debug("Populating header XML for DT order.");}
        
        if (order.getOrderData().getDetentionAndTrainingOrder() == null)
        {
            log.error("Order data could not be retrieved for DT order.");
            throw new OrderXMLException("order.dataentry.error" ,"Order data could not be retrieved for DT order.");
        }
        OrderHeaderHelper.populateHeader(order.getOrderData().getDetentionAndTrainingOrder().getOrderHeader(), docEntity, this
                .getTypeCode());

        if (log.isDebugEnabled()){log.debug("Populating body XML for DT order.");}
        // Populate the body of the order.
        DTOrderHelper.populateDetentionAndTrainingOrder(order.getOrderData().getDetentionAndTrainingOrder(), docEntity, 
                this.getTypeCode());

        // Use the AssociatedCasesHelper to populate the Associated cases
        // on the remand order. We're using a back-population technique here
        // that ensures that the AssociatedCases remain null if there are truly
        // no AssociatedCases/
        order.getOrderData().getDetentionAndTrainingOrder().setAssociatedCases(
                AssociatedCasesHelper.populateAssociatedCases(order.getOrderData().getDetentionAndTrainingOrder()
                        .getAssociatedCases(), docEntity.getCaze()));
        

    
    }

    /**
     * Overridden to provide logical validation of the order.
     * 
     * @param helper
     *            Class encapsulating the order to be validated and any errors
     *            or warnings to be entered against it.
	 * @param defendantOnCaseID					(Not used)
     */
    protected void logicalValidation(Integer defendantOnCaseID, ValidationHelper helper) {
        
        if (this.getTypeCode().equals( "DTO"))
        {
            DTOrderValidationHelper.logicalValidateDTOrder(helper.getOrderToBeValidated().getOrderData()
                    .getDetentionAndTrainingOrder(), helper);
        }
        else
        {
            DTOrderValidationHelper.logicalValidateDTOrder(helper.getOrderToBeValidated().getOrderData()
                    .getDetentionAndTrainingOrder(), helper);
        }
    }
}