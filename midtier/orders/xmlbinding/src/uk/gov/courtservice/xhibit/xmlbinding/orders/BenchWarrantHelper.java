package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BWSurety;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BenchWarrantStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.BailDecisionType;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.WarrantAfterFailureType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.PreConditionDetails;

/**
 * 
 * <p>
 * Title: BenchWarrantHelper
 * </p>
 * <p>
 * Description: Helper class to validate a Bench Warrant
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class BenchWarrantHelper {
    private static final Logger log = CSServices.getLogger(BenchWarrantHelper.class);

    private static final String SURETY_AMOUNT_MESSAGE = "ORDER_BW_Surety_Amount_incorrect";
    private static final String AFTER_DETAILS_MESSAGE = "ORDER_BW_After_Details_incorrect";
    private static final String BEFORE_DETAILS_MESSAGE = "ORDER_BWF_Before_Details_incorrect";
    private static final String NO_BEFORE_CONDITIONS_MESSAGE = "ORDER_BWF_No_Before_incorrect";
    

    /**
     * Utility methodValidation
     * 
     * @param bws
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateBenchWarrant(BenchWarrantStructure bws, String typeCode,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("********************Bench Warrant: Logical Validation");
        validateBeforeReleaseConditions(bws, helper);
        validateAfterReleaseConditions(bws, helper);
    }

    /**
     * Utility method to check that any elements whose value depends on other
     * elements are set correctly. In the community orders the FailedToComply
     * element must be set to match the Breach option. Failed to comply cannot
     * be set via the gui as this option was removed, but it remains in the
     * schema.
     * 
     * @param bws
     *            the bench warrant
     * @param typeCode
     *            the order type
     * @param helper
     *            the helper
     * @return the validated xml
     * @throws ValidationException
     * @throws MarshalException
     * @throws OrderXMLException
     */
    public static String structuralValidateBenchWarrant(BenchWarrantStructure bws, String typeCode,
            OrderXMLHelper.ValidationHelper helper) throws ValidationException, MarshalException, OrderXMLException {
        log.debug("$$$ BenchWarrantHelper.structuralValidateBenchWarrant");
        validateReleaseOption(bws, helper);
        return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
    }

    /**
     * Validate the Pre Release Conditions
     * 
     * @param bws
     *            the bench warrant
     * @param helper
     *            the helper
     */
    private static void validateBeforeReleaseConditions(BenchWarrantStructure bws, ValidationHelper helper) {
        // check to ensure that the PostConditions has been selected
        if (bws.getRelease().equals(BailDecisionType.CONDITIONAL) && bws.getBWPreConditions().hasSelected()
                && bws.getBWPreConditions().getSelected()) 
        {
            log.debug("*******************Pre Conditions Selected");
            /* Normal Bench Warrant Order so only Surety needs checking */
            if( bws.getWarrantType() == null )
            {
                validatePreSurety(bws, helper);
            } 
            /* Warrant After Failure Orders have other conditions in preconditions so need checking also */
            else if (bws.getWarrantType().equals(WarrantAfterFailureType.FAILURETOATTEND) ||
                    bws.getWarrantType().equals(WarrantAfterFailureType.FAILURETOCOMPLY ) )
            {
                validateWarrantAfterFailurePreConditions(bws, helper);
            }
        }
    }

    
    /**
     * Validate the Pre Conditions of the Warrant After Fialure orders - Surety + Other conditions
     * 
     * @param bws
     *            the bench warrant
     * @param helper
     *            the helper
     */
    private static void validateWarrantAfterFailurePreConditions(BenchWarrantStructure bws, ValidationHelper helper) {
        
       
        /* check Warrant After failure orders for optional surety and validate if necessary */
        BWSurety benchSurety = bws.getBWPreConditions().getBWSurety();
        if (benchSurety.hasSelected() && benchSurety.getSelected() ) 
        {
            validatePreSurety(bws, helper);
        }
        /* check Warrant After failure orders for optional Pre Conditions and validate if necessary */
        PreConditionDetails preConditionDetail = bws.getBWPreConditions().getPreConditionDetails();
        if (preConditionDetail.hasSelected() && preConditionDetail.getSelected())
        {
            validatePreOther(bws, helper);
        }
        /* check to make sure that at least one condition is entered if the preconditions is selected */
        if( (benchSurety.hasSelected() && benchSurety.getSelected()) 
              ||
            (preConditionDetail.hasSelected() && preConditionDetail.getSelected())) 
        {
            // do nothing as at least one option is selected;
        } else {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(NO_BEFORE_CONDITIONS_MESSAGE, null);
        }
    }
    
    /**
     * Validate the Pre Condition - Surety
     * 
     * @param bws
     *            the bench warrant
     * @param helper
     *            the helper
     */
    private static void validatePreSurety(BenchWarrantStructure bws, ValidationHelper helper) {
        BWSurety benchSurety = bws.getBWPreConditions().getBWSurety();
        // check to ensure that an amount has been entered if the Surety
        // option has been selected
        log.debug("*****************Bench Warrant Surety Selected");
        // check to ensure that the amount is > £0.00
        if (benchSurety.getMonetaryValue().getAmount().doubleValue() <= 0.00) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(SURETY_AMOUNT_MESSAGE, null);
        }
    }

    /**
     * Validate all the After Release Conditions
     * 
     * @param bws
     *            the bench warrant
     * @param helper
     *            the helper
     */
    private static void validateAfterReleaseConditions(BenchWarrantStructure bws, ValidationHelper helper) {
        // check to ensure that the PostConditions has been selected
        if (bws.getRelease().equals(BailDecisionType.CONDITIONAL) && bws.getBWPostConditions().hasSelected()
                && bws.getBWPostConditions().getSelected()) {
            log.debug("*******************Post Conditions Selected");
            validatePostOther(bws, helper);
        }
    }
    
    /**
     * Validate the Pre Condition - Other
     * 
     * @param bws
     *            the bench warrant
     * @param helper
     *            the helper
     */
    private static void validatePreOther(BenchWarrantStructure bws, ValidationHelper helper) {
        // check to ensure that the Other condition has been selected
        log.debug("*****************Before Details Selected");
        // check to ensure that the Other details have been set
        if (isEmptyString(bws.getBWPreConditions().getPreConditionDetails().getContent() )) {
            // No other details entered
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(BEFORE_DETAILS_MESSAGE, null);
        }
    }
    
    /**
     * Validate the Post Condition - Other
     * 
     * @param bws
     *            the bench warrant
     * @param helper
     *            the helper
     */
    private static void validatePostOther(BenchWarrantStructure bws, ValidationHelper helper) {
        // check to ensure that the Other condition has been selected
        log.debug("*****************After Details Selected");
        // check to ensure that the Other details have been set
        if (isEmptyString(bws.getBWPostConditions().getPostConditionDetails())) {
            // No other details entered
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(AFTER_DETAILS_MESSAGE, null);
        }
    }

    /**
     * Validate the Release Option
     * 
     * @param bws
     *            the bench warrant
     * @param helper
     *            the helper
     */
    private static void validateReleaseOption(BenchWarrantStructure bws, ValidationHelper helper) {
        log.debug("*****************Release Option Selected");

        // If release is not Refused
        if (bws.getRelease().getType() != BailDecisionType.REFUSED_TYPE) {
            log.debug("*****************NOT REFUSED TYPE :" + bws.getRelease().getType());
        }
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