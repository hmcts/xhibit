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
 * 'Community Rehabilitation' order.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Instance of XmlHelper that populates the Order Castor object for a 'Community
 * Rehabilitation' order.
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
 */
public class CRXmlHelper extends OrderXMLHelper {

    private static Logger log = CSServices.getLogger(RemandXmlHelper.class);

    /**
     * Simple constructor instantiating a functional instance of XML helper for
     * a 'Community Rehabilitation' order.
     * 
     * @param typeCode
     *            the type code for this particular order.
     */
    public CRXmlHelper(String typeCode) {
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
        OrderHeaderHelper.populateHeader(order.getOrderData().getCROrder().getOrderHeader(), docEntity, this
                .getTypeCode());

        // Populate the body of the order.
        XhibitCommunityOrderHelper.populateXhibitCommunityOrder(order.getOrderData().getCROrder(), docEntity, false,
                true, this.getTypeCode());

        // Use the AssociatedCasesHelper to populate the Associated cases
        // on the remand order. We're using a back-population technique here
        // that ensures that the AssociatedCases remain null if there are truly
        // no AssociatedCases/
        order.getOrderData().getCROrder().setAssociatedCases(
                AssociatedCasesHelper.populateAssociatedCases(order.getOrderData().getCROrder().getAssociatedCases(),
                        docEntity.getCaze()));
    }

    protected void logicalValidation(Integer defendantOnCaseId, ValidationHelper helper) {
        XhibitCommunityOrderValidationHelper.logicalValidateCommunityOrder(helper.getOrderToBeValidated()
                .getOrderData().getCROrder(), this.getTypeCode(), helper);
    }

    protected String structuralValidation(ValidationHelper helper) throws ValidationException, MarshalException,
            OrderXMLException {
        return XhibitCommunityOrderValidationHelper.structuralValidateCommunityOrder(helper.getOrderToBeValidated()
                .getOrderData().getCROrder(), this.getTypeCode(), helper);
    }

}