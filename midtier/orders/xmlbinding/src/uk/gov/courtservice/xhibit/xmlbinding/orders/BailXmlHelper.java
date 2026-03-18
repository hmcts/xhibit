package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Address;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;

/**
 * <p>
 * Title: Instance of XmlHelper that populates the Order Castor object for a
 * 'Bail' order.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Instance of XmlHelper that populates the Order Castor object for a 'Bail'
 * order.
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
public class BailXmlHelper extends OrderXMLHelper {

    private static final Logger log = CSServices.getLogger(BailXmlHelper.class);

    private static final String DEFAULT_POSTCODE = "AA1 1AA";

    /**
     * Simple constructor instantiating a functional instance of XML helper for
     * a 'Bail' order.
     * 
     * @param typeCode
     *            the type code for this particular order.
     */
    public BailXmlHelper(String typeCode) {
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
        OrderHeaderHelper.populateHeader(order.getOrderData().getBailOrder().getOrderHeader(), docEntity, this.getTypeCode());

        // Set the live/sleep address to be the same as the defendant address.
        // This can then be overwritten in the GUI
        Address address = order.getOrderData().getBailOrder().getOrderHeader().getDefendant().getPersonalDetails().getAddress();

        // ensure that there is a full address
        int noOfAddLines = address.getLineCount();
        int noOfAddLinesReq = 5;
        for (int i = noOfAddLines; i < noOfAddLinesReq; i++) {
            address.addLine(i, " ");
        }

        // If the defendant address deos not have a valid postcode
        // set the live/sleep postcode to the default (AA1 1AA).
        // This will allow the postcode to be updated
        if (address.getPostCode() == null) {
            address.setPostCode(DEFAULT_POSTCODE);
        }
        order.getOrderData().getBailOrder().getBailPostConditions().getLiveSleep().setAddress(address);

        // Use the AssociatedCasesHelper to populate the Associated cases
        // on the remand order. We're using a back-population technique here
        // that ensures that the AssociatedCases remain null if there are truly
        // no AssociatedCases/
        order.getOrderData().getBailOrder().setAssociatedCases(
                AssociatedCasesHelper.populateAssociatedCases(order.getOrderData().getBailOrder().getAssociatedCases(),
                        docEntity.getCaze()));
    }
    

    /**
     * Overridden to provide logical validation of the order.
     * 
     * @param helper
     *            Class encapsulating the order to be validated and any errors
     *            or warnings to be entered against it.
	 * @param defendantOnCaseID			(Not used)
     */
    protected void logicalValidation(Integer defendantOnCaseID, ValidationHelper helper) {
        BailOrderHelper.logicalValidateBailOrder(helper.getOrderToBeValidated().getOrderData().getBailOrder(), this.getTypeCode(), helper);
    }

}
