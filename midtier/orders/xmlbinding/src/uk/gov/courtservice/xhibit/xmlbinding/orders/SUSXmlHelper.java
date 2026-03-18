package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.SuspendedSentenceOrderStructure;

/**
 * <p>
 * Title: SUSXmlHelper.
 * </p>
 * <p>
 * Description: This helper class helps populate the XML for the Suspended Sentence Order.
 * </p>
 * <p/> 
 * This Helper is based on the existing COXmlHelper used for the Community sentence order (COMSENT), it
 * has been defined in a separate class so that the two order types can change independently. 
 * </p>
 * 
 * @author Luis Valenzuela
 * 
 */
public class SUSXmlHelper extends OrderXMLHelper {

    private static final Logger log = CSServices.getLogger(SUSXmlHelper.class);
    
    public SUSXmlHelper(String typeCode) {
        super(typeCode);
    }

    protected void populateSchema(Order order, DefendantOnCase docEntity) throws OrderXMLException {
        SuspendedSentenceOrderStructure susOrder = null;
        
        susOrder = order.getOrderData().getSUSOrder();
        // Populate the header.
        if (log.isDebugEnabled()){log.debug("Populating header XML for SUS order.");}
        
        if (order.getOrderData().getSUSOrder() == null)
        {
            log.error("Order data could not be retrieved for SUS order.");
            throw new OrderXMLException("order.dataentry.error" ,"Order data could not be retrieved for SUS order.");
        }
        OrderHeaderHelper.populateHeader(order.getOrderData().getSUSOrder().getOrderHeader(), docEntity, this
                .getTypeCode());
        // Add place holder for Post code if it has been removed by the order helper.
        rebuildBlankAddressLines(order);
        if (log.isDebugEnabled()){log.debug("Populating body XML for SUS order.");}
        // Populate the body of the order.
        SUSOrderHelper.populateSuspendedSentenceOrder(order.getOrderData().getSUSOrder(), docEntity);

        // Use the LinkedOffenceHelper to populate the Linked Offences
        // for the Suspended Sentence order. We're using a back-population technique here
        // that ensures that the LinkedOffences remain null if there are truly
        // no LinkedOffences
        Integer courtId = docEntity.getCaze().getCourtId();
        order.getOrderData().getSUSOrder().setLinkedOffences(
                LinkedOffenceHelper.populateLinkedOffences(order.getOrderData().getSUSOrder().getLinkedOffences(),
                        docEntity.getCaze(), docEntity.getDefendantId(), courtId, this.getTypeCode()));
        
        //      Use the AssociatedCasesHelper to populate the Associated cases
        // on the suspended sentence order. We're using a back-population technique here
        // that ensures that the AssociatedCases remain null if there are truly
        // no AssociatedCases/
        susOrder.setAssociatedCases(
                AssociatedCasesHelper.populateAssociatedCases(
                        susOrder.getAssociatedCases(), docEntity.getCaze()));
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
        SUSOrderValidationHelper.logicalValidateSuspendedSentenceOrder(helper.getOrderToBeValidated().getOrderData()
                .getSUSOrder(), helper);
    }

    
    /**
     * The address helper returns the Defendant's address information from XHIBIT and inserts it into the the XML
     * representation but if this data is not found then it removes the blank elements. To allow them to be populated
     * through the GUI they need to be replaced.
     * 
     * @param order - the order obejct to replace addressLine 3 and addressLine 4 in.
     *            
     */
    private void rebuildBlankAddressLines(Order order){
    
        uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Address defAdd = 
            order.getOrderData().getSUSOrder().getOrderHeader().getDefendant().getPersonalDetails().getAddress();
        if(defAdd.getLineCount() < 3){
            defAdd.addLine(" ");
            defAdd.addLine(" ");
        }
        
    }
}
