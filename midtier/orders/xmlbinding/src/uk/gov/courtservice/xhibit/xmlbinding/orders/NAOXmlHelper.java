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
 * 'Notice of Acquittal' order.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Instance of XmlHelper that populates the Order Castor object for a 'Notice of Acquittal'
 * order.
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
public class NAOXmlHelper extends OrderXMLHelper {

    private static final Logger log = CSServices.getLogger(NAOXmlHelper.class);



    /**
     * Simple constructor instantiating a functional instance of XML helper for
     * a 'Notice of Acquittal' order.
     * 
     * @param typeCode
     *            the type code for this particular order.
     */
    public NAOXmlHelper(String typeCode) {
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
        if (log.isDebugEnabled()){log.debug("Populating header XML for Notice of Acquittal order.");}
        
        if (order.getOrderData().getNoticeOfAcquittalOrder() == null)
        {
            log.error("Order data could not be retrieved for Notice of Acquittal order.");
            throw new OrderXMLException("order.dataentry.error" ,"Order data could not be retrieved for Notice of Acquittal order.");
        }
        OrderHeaderHelper.populateHeader(order.getOrderData().getNoticeOfAcquittalOrder().getOrderHeader(), docEntity, this
                .getTypeCode());
        // Add place holder for Post code if it has been removed by the order helper.
        rebuildBlankAddressLines(order);
        if (log.isDebugEnabled()){log.debug("Populating body XML for Notice of Acquittal order.");}
        

        
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
        NAOrderHelper.logicalValidateNAOrder(helper.getOrderToBeValidated().getOrderData().getNoticeOfAcquittalOrder(), this.getTypeCode(), helper);
    }
    
    
    private void rebuildBlankAddressLines(Order order){
        
        uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Address defAdd = 
            order.getOrderData().getNoticeOfAcquittalOrder().getOrderHeader().getDefendant().getPersonalDetails().getAddress();
        if(defAdd.getLineCount() < 3){
            defAdd.addLine(" ");
            defAdd.addLine(" ");
        }
        
    }

}
