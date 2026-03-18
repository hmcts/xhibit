package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BWSurety;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DeportationSection;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ImprisonmentOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.MonetaryDisposals;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.MonetaryOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.BailDecisionType;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.WarrantAfterFailureType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.PreConditionDetails;

/**
 * 
 * <p>
 * Title: MonetaryOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a Monetary Order
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
public class MonetaryOrderHelper {
    private static final Logger log = CSServices.getLogger(MonetaryOrderHelper.class);
    
    private static final String COLLECTION_ORDER_MESSAGE = "ORDER_MON_ORD_No_collection_order";
    
    private static final String CUSTODY_STATUS_MESSAGE = "ORDER_MON_ORD_No_custody_status";
    
    private static final String FINE_PERIOD_NON_ZERO_MESSAGE = "ORDER_MON_Fine_Non_Zero";
    
    private static final String IMPRISONMENT_PERIOD_NON_ZERO_MESSAGE = "ORDER_MON_Imprisonment_Non_Zero";
    

    /**
     * Utility methodValidation
     * 
     * @param mos
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateMonetaryOrder(MonetaryOrderStructure mos, String typeCode,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("********************Monetary Order: Logical Validation");
        validateCollectionOrder(mos, helper);
        validateCustodyStatus(mos, helper);
        
        if(mos.getFine().getSelected() 
                && mos.getFine().hasSelected()){
                    validateFinePeriod(mos, helper);
        }
        
        if(mos.getImprisonment().getSelected()
                && mos.getImprisonment().hasSelected()){
                    validateImprisonmentPeriod(mos, helper);
        }
    }
    
    
    
    /**
     * Check that the Fine Period is not zero
     * 
     * @param mos
     *            the monetary order
     * @param helper
     *            the helper
     */
    private static void validateFinePeriod(MonetaryOrderStructure mos, ValidationHelper helper) {
        log.debug("*****************validateFinePeriod");
        XhibitPeriodHelper.validatePeriodNonZero(mos.getFine().getDefaultPeriod(), helper, FINE_PERIOD_NON_ZERO_MESSAGE);

    }
    
    /**
     * Check that the Imprisonment Period is not zero
     * 
     * @param mos
     *            the monetary order
     * @param helper
     *            the helper
     */
    private static void validateImprisonmentPeriod(MonetaryOrderStructure mos, ValidationHelper helper) {
        log.debug("*****************validateFinePeriod");
        XhibitPeriodHelper.validatePeriodNonZero(mos.getImprisonment().getLengthOfSentence(), helper, IMPRISONMENT_PERIOD_NON_ZERO_MESSAGE);

    }

    /**
     * Utility method to check that any elements whose value depends on other
     * elements are set correctly.
     * 
     * @param mos
     *            the Monetary Order
     * @param typeCode
     *            the order type
     * @param helper
     *            the helper
     * @return the validated xml
     * @throws ValidationException
     * @throws MarshalException
     * @throws OrderXMLException
     */
    public static String structuralValidateMonetaryOrder(MonetaryOrderStructure mos, String typeCode,
            OrderXMLHelper.ValidationHelper helper) throws ValidationException, MarshalException, OrderXMLException {
        log.debug("$$$ MonetaryOrderHelper.structuralValidateMonetaryOrder");
        // Nothing to do here as all elements are independent
        return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
    }

    /**
     * Validate a Collection Order button has been selected 
     * 
     * @param mos
     *            the Monetary Order
     * @param helper
     *            the helper
     */
    private static void validateCollectionOrder(MonetaryOrderStructure mos, ValidationHelper helper) {
        // check to ensure that the PostConditions has been selected
        if ((mos.getCollectionOrderMade().getContent() != null) && (mos.getCollectionOrderMade().getContent().equals("no"))) { // lower case no indicates nothing selected
            log.warn("*******************No value selected for Collection Order Made");
            helper.addLogicalProblem(COLLECTION_ORDER_MESSAGE, null);
        }
    }
    
    /**
     * Validate an option for In Custody has been selected
     * 
     * @param mos
     *            the Monetary Order
     * @param helper
     *            the helper
     */
    private static void validateCustodyStatus(MonetaryOrderStructure mos, ValidationHelper helper) {
    	
        if ((mos.getInCustody().getContent() != null) && (mos.getInCustody().getContent().equals("no"))) {
            log.warn("*******************No value selected for In Custody");
            helper.addLogicalProblem(CUSTODY_STATUS_MESSAGE, null);
        }
    }
    
    /**
     * Utility method to populate an ImprisonmentOrderStructure from the rest of
     * the Xhibit entities.
     * 
     * @param mos
     *            The MonetaryOrderStructure to be populated (pass by
     *            reference).
     * @param docEntity
     *            The Defendant on case for which to populate the order.
     * @param typeCode
     *            The type code of the order.
     * @throws OrderXMLException
     *             When there is a problem in population.
     */
    public static void populateMonetaryOrder(MonetaryOrderStructure mos, DefendantOnCase docEntity, String typeCode) throws OrderXMLException {
        Case caseEntity = docEntity.getCaze();
    }


    // Utility Methods

    /**
     * Checks if a passed string is null, empty or a single space
     * 
     * @param value
     *            the string
     * @return true if string is null, empty or a single space
     */
    private static boolean isEmptyString(String value) {
        boolean result = false;
        if (value == null // if string is null
                || // OR
                value.equals("") // empty string
                || // OR
                value.equals(" ")) // single space
        {
            result = true;
        }
        return result;
    }
}