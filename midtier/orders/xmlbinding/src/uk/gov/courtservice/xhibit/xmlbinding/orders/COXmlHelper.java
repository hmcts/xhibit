package uk.gov.courtservice.xhibit.xmlbinding.orders;

import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CommunityOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;

/**
 * Created by IntelliJ IDEA. User: tzj8k5 Date: 21-Mar-2005 Time: 12:25:34 To
 * change this template use File | Settings | File Templates.
 */
public class COXmlHelper extends OrderXMLHelper {

    public COXmlHelper(String typeCode) {
        super(typeCode);
    }

    protected void populateSchema(Order order, DefendantOnCase docEntity) throws OrderXMLException {
        CommunityOrderStructure comOrder = null;
        
        comOrder = order.getOrderData().getCOOrder();
        
        // Populate the header.
        OrderHeaderHelper.populateHeader(order.getOrderData().getCOOrder().getOrderHeader(), docEntity, this
                .getTypeCode());

        // Populate the body of the order.
        XhibitCOMSENTOrderHelper.populateXhibitCommunityOrder(order.getOrderData().getCOOrder(), docEntity);

        // Use the LinkedOffenceHelper to populate the Linked Offences
        // for the community order. We're using a back-population technique here
        // that ensures that the LinkedOffences remain null if there are truly
        // no LinkedOffences
        Integer courtId = docEntity.getCaze().getCourtId();
        order.getOrderData().getCOOrder().setLinkedOffences(
                LinkedOffenceHelper.populateLinkedOffences(order.getOrderData().getCOOrder().getLinkedOffences(),
                        docEntity.getCaze(), docEntity.getDefendantId(), courtId, this.getTypeCode()));
    
        //      Use the AssociatedCasesHelper to populate the Associated cases
        // on the suspended sentence order. We're using a back-population technique here
        // that ensures that the AssociatedCases remain null if there are truly
        // no AssociatedCases/
        comOrder.setAssociatedCases(
                AssociatedCasesHelper.populateAssociatedCases(
                        comOrder.getAssociatedCases(), docEntity.getCaze()));
    
    
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
        XhibitCOMSENTOrderValidationHelper.logicalValidateCommunityOrder(helper.getOrderToBeValidated().getOrderData()
                .getCOOrder(), helper);
    }
}
