package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.math.BigDecimal;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.address.types.YesNoType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Breach;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Charge;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Conditions;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.XHIBITCommunityOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * XHIBITCommunityOrderStructure
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Validates the XHIBITCommunityOrderStructure for 'Community Punishment and
 * Rehabilitation', 'Community Rehabilitation' and 'Community Punishment'
 * orders.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class XhibitCommunityOrderValidationHelper {

    // set up logger
    private static final Logger log = CSServices.getLogger(XhibitCommunityOrderValidationHelper.class);

    private static final String REHABILITATION_PERIOD_MESSAGE = "ORDER_Rehabilitation_Period_must_be_non_zero";

    private static final String PUNISHMENT_PERIOD_MESSAGE = "ORDER_Punishment_Period_must_be_non_zero";

    private static final String PUNISHMENT_HOURS_MESSAGE = "ORDER_Punishment_Hours_between_1_and_500";

    private static final String EMPTY_CONVICTION_MESSAGE = "ORDER_Empty_Conviction";

    private static final String ADDITIONAL_NOTES_NON_ENTERED_MESSAGE = "ORDER_Additional_Notes_incorrect_non_entered";

    private static final String ADDITIONAL_NOTES_MESSAGE = "ORDER_Additional_Notes_incorrect";

    private static final int PUNISHMENT_HRS_MIN = 1;

    private static final int PUNISHMENT_HOURS_MAX = 500;

    // Statically initialise the required home interface.
    // Should be done for all home interfaces used.

    /**
     * Utility methodValidation
     * 
     * @param cos
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateCommunityOrder(XHIBITCommunityOrderStructure cos, String typeCode,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("$$$ XhibitCommunityOrderHelper.logicalValidateCommunityOrder");
        validateOffenceStatements(cos, helper);
        validateRehabDetails(cos, typeCode, helper);
        validatePunishmentDetails(cos, typeCode, helper);
        validateAdditionalNotes(cos, helper);
        validateBreachOption(cos, typeCode, helper);
    }

    /**
     * Utility method to check that any elements whose value depends on other
     * elements are set correctly. In the community orders the FailedToComply
     * element must be set to match the Breach option. Failed to comply cannot
     * be set via the gui as this option was removed, but it remains in the
     * schema.
     * 
     * @param cos
     *            the community order
     * @param typeCode
     *            the order type
     * @param helper
     *            the helper
     * @return the validated xml
     * @throws ValidationException
     * @throws MarshalException
     * @throws OrderXMLException
     */
    public static String structuralValidateCommunityOrder(XHIBITCommunityOrderStructure cos, String typeCode,
            OrderXMLHelper.ValidationHelper helper) throws ValidationException, MarshalException, OrderXMLException {
        log.debug("$$$ XhibitCommunityOrderHelper.structuralValidateCommunityOrder");
        validateBreachOption(cos, typeCode, helper);
        return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
    }

    private static void validateRehabDetails(XHIBITCommunityOrderStructure cos, String typeCode, ValidationHelper helper) {
        // only applies to CRO and CPRO Order Types
        if (typeCode.equals("CRO") || typeCode.equals("CMPRO")) {
            validateRehabPeriod(cos, helper);
        }
    }

    private static void validateRehabPeriod(XHIBITCommunityOrderStructure cos, ValidationHelper helper) {
        // only applies to CRO and CPRO Order Types
        // check if Community ehabilitation Period Entered
        XhibitPeriodHelper.validatePeriodNonZero(cos.getRehabilitationPeriod(), helper, REHABILITATION_PERIOD_MESSAGE);
    }

    private static void validatePunishmentDetails(XHIBITCommunityOrderStructure cos, String typeCode,
            ValidationHelper helper) {
        // only applies to CPO and CPRO Order Types
        if (typeCode.equals("CMPO") || typeCode.equals("CMPRO")) {
            validatePunishmentPeriod(cos, helper);
            validatePunishmentHours(cos, helper);
        }
    }

    private static void validatePunishmentPeriod(XHIBITCommunityOrderStructure cos, ValidationHelper helper) {
        // only applies to CPO and CPRO Order Types
        // check if Community Punishment Period Entered
        XhibitPeriodHelper.validatePeriodNonZero(cos.getCommunityPunishment().getPeriod(), helper,
                PUNISHMENT_PERIOD_MESSAGE);
    }

    /**
     * Check that the punishment hours are between
     * 
     * @param cos
     * @param typeCode
     * @param helper
     */
    private static void validatePunishmentHours(XHIBITCommunityOrderStructure cos, ValidationHelper helper) {
        // only applies to CPO and CPRO Order Types
        // check if Community Punishment Hours between 1 and 500
        BigDecimal hoursValue = cos.getCommunityPunishment().getHours().getContent();
        int hours = hoursValue == null ? 0 : hoursValue.intValue();

        if (hours < PUNISHMENT_HRS_MIN || hours > PUNISHMENT_HOURS_MAX) {
            helper.addLogicalProblem(PUNISHMENT_HOURS_MESSAGE, null);
        }
    }

    /**
     * Check that the Offence Statements are valid
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateOffenceStatements(XHIBITCommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ cos.getOrderHeader().....getChargeCount() "
                + cos.getOrderHeader().getDefendant().getCharges().getChargeCount());
        Enumeration charges = cos.getOrderHeader().getDefendant().getCharges().enumerateCharge();
        boolean validOffenceStatement = false;
        // Check all charges until we find a valid offence statement
        while (charges.hasMoreElements() && !validOffenceStatement) {
            Charge charge = (Charge) charges.nextElement();
            log.debug("$$$ charge.getOffenceStatement() = |" + charge.getOffenceStatement() + "|");
            if (charge.getOffenceStatement().equals("") || charge.getOffenceStatement().equals(" ")) {
                // Do nothing
            } else {
                validOffenceStatement = true;
            }
        }
        log.debug("$$$ validCharge =  " + validOffenceStatement);

        if (!validOffenceStatement) {
            helper.addLogicalProblem(EMPTY_CONVICTION_MESSAGE, null);
        }
    }

    /**
     * Validate the Additional Notes Option
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateAdditionalNotes(XHIBITCommunityOrderStructure xcos, ValidationHelper helper) {
        Conditions conditions = xcos.getConditions();
        // check to ensure that the additional notes option has been selected
        if (conditions.hasSelected() && conditions.getSelected()) {
            log.debug("*****************AdditionalNotes Selected");
            // check to ensure that the Additional Notes have been set
            if (conditions.getConditionCount() == 0) {
                // No additional notes entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(ADDITIONAL_NOTES_NON_ENTERED_MESSAGE, null);
            } else if (isEmptyString(conditions.getCondition(0).getDescription())) {
                // No additional notes entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(ADDITIONAL_NOTES_MESSAGE, null);
            }
        }
    }

    /**
     * Sets the FailedToComply option to match the Breach Option - this
     * maintains the integriry of the XML.
     * 
     * @param xcos
     *            the community order
     * @param typeCode
     *            the order type
     * @param helper
     *            the helper
     */
    private static void validateBreachOption(XHIBITCommunityOrderStructure xcos, String typeCode,
            ValidationHelper helper) {
        log.debug("*****************Validating Breach");
        Breach breach = xcos.getBreach();
        // If Breach option has been selected, the FailedToComply option
        // should also be selected, and vice verca
        YesNoType yesno = (breach.hasSelected() && breach.getSelected()) ? YesNoType.YES : YesNoType.NO;
        log.debug("*****************YesNoOption = " + yesno.toString());
        log.debug("*****************validateBreachOption BEFORE FTC = " + xcos.getFailedToComply().toString());
        if (typeCode.equals("CMPO")) {
            helper.getOrderToBeValidated().getOrderData().getCPOrder().setFailedToComply(yesno);
        }
        if (typeCode.equals("CMPRO")) {
            helper.getOrderToBeValidated().getOrderData().getCPROrder().setFailedToComply(yesno);
        }
        if (typeCode.equals("CRO")) {
            helper.getOrderToBeValidated().getOrderData().getCROrder().setFailedToComply(yesno);
        }

        log.debug("*****************validateBreachOption AFTER FTC = " + xcos.getFailedToComply().toString());
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