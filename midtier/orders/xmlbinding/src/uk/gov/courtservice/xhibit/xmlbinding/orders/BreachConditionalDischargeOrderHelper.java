package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Time;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BreachConditionalDischargeOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: BreachConditionalDischargeOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a BreachConditionalDischargeOrder
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
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
public class BreachConditionalDischargeOrderHelper {
	private static final Logger log = CSServices.getLogger(BreachConditionalDischargeOrderHelper.class);
	private static final String ORDER_DATE_MUST_BE_IN_PAST_MESSAGE = "ORDER_BRCD_DATE_MUST_BE_IN_PAST";
  	private static final String SUBSEQUENT_CONVICTION_COURT_MESSAGE = "BRCD_SUBSEQUENT_CONVICTION_COURT_MUST_BE_ENTERED";
	private static final String SUBSEQUENT_CONVICTION_DATE_MESSAGE = "BRCD_SUBSEQUENT_CONVICTION_DATE_MUST_BE_EQUAL_OR_BEFORE_DATE_OF_ORDER";
	private static final String SUMMONED_TO_APPEAR_COURT_MESSAGE = "BRCD_SUMMONED_TO_APPEAR_COURT_MUST_BE_SELECTED";
	private static final String SUMMONED_TO_APPEAR_DATE_MESSAGE = "BRCD_SUMMONED_TO_APPEAR_DATE_MUST_BE_EQUAL_OR_AFTER_TODAY";

	/**
	 * Utility methodValidation
	 * 
	 * @param brcd
	 * @param typeCode
	 * @param helper
	 */
	public static void logicalValidateBreachConditionalDischarge(BreachConditionalDischargeOrderStructure brcd,
			String typeCode, OrderXMLHelper.ValidationHelper helper) {
		log.debug("********************BreachConditionalDischarge: Logical Validation");
		validateBeforeReleaseConditions(brcd, helper);
		validateAfterReleaseConditions(brcd, helper);
	}

	/**
	 * Utility method to check that any elements whose value depends on other
	 * elements are set correctly. In the community orders the FailedToComply
	 * element must be set to match the Breach option. Failed to comply cannot
	 * be set via the gui as this option was removed, but it remains in the
	 * schema.
	 * 
	 * @param brcd
	 *            the breach conditional discharge
	 * @param typeCode
	 *            the order type
	 * @param helper
	 *            the helper
	 * @return the validated xml
	 * @throws ValidationException
	 * @throws MarshalException
	 * @throws OrderXMLException
	 */
	public static String structuralValidateBreachConditionalDischarge(BreachConditionalDischargeOrderStructure brcd,
			String typeCode, OrderXMLHelper.ValidationHelper helper)
			throws ValidationException, MarshalException, OrderXMLException {
		log.debug("$$$ BreachConditionalDischargeHelper.structuralValidateBreachConditionalDischarge");
		return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
	}

	/**
	 * Validate the Pre Release Conditions
	 * 
	 * @param brcd
	 *            the breach conditional discharge
	 * @param helper
	 *            the helper
	 */
	private static void validateBeforeReleaseConditions(BreachConditionalDischargeOrderStructure brcd,
			ValidationHelper helper) {
		// check to ensure that the PostConditions has been selected
		validateOrderDate(brcd, helper);
 		validateSubsequentConvictionDate(brcd, helper);
 		validateSubsequentCourt(brcd, helper);
		validateSummonedCourt(brcd, helper);
		validateSummonedDate(brcd, helper);
	}

	 
	/**
	 * Check that the SubsequentConviction date is on or after order date.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSubsequentConvictionDate(BreachConditionalDischargeOrderStructure brcd, ValidationHelper helper) {

		log.debug("$$$ validateSubsequentConvictionDate " + "Date " + brcd.getSubsequentConv().getSubsConvDate());
 		Calendar orderDate = brcd.getDateOfOrder().toCalendar();
		Calendar subsequentConvictionDate = brcd.getSubsequentConv().getSubsConvDate().toCalendar();

		if (subsequentConvictionDate.before(orderDate)) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(SUBSEQUENT_CONVICTION_DATE_MESSAGE, null);
		}
	}

	/**
	 * Check that the Summoned court is selected.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSummonedCourt(BreachConditionalDischargeOrderStructure brcd, ValidationHelper helper) {

		log.debug("$$$ validateSummonedCourt " + "Court Selected "
				+ brcd.getSummonedToAppearAt().getCourtHouseDetails().getCourtHouseName());

		if (brcd.getSummonedToAppearAt().getCourtHouseDetails().getCourtHouseName() == null
				|| "".equals(brcd.getSummonedToAppearAt().getCourtHouseDetails().getCourtHouseName())) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(SUMMONED_TO_APPEAR_COURT_MESSAGE, null);
		}
	 
	}
	
	/**
	 * Check that the subsequent conviction court.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSubsequentCourt(BreachConditionalDischargeOrderStructure brcd, ValidationHelper helper) {

		log.debug("$$$ validateSubsequentCourt " + "Court entered "
				+ brcd.getSubsequentConv().getCourtName());

		if (brcd.getSubsequentConv().getCourtName() == null || 
			(brcd.getSubsequentConv().getCourtName() != null && "".equals(brcd.getSubsequentConv().getCourtName().trim()))) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(SUBSEQUENT_CONVICTION_COURT_MESSAGE, null);
		}
	 
	}

	/**
	 * Check that the Summoned date is on or before order date.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSummonedDate(BreachConditionalDischargeOrderStructure brcd,
			ValidationHelper helper) {

		
		
		//adding null check as may not have specified date/time
		if(brcd.getSummonedToAppearAt().getSummonedDateTime()!=null &&
				brcd.getSummonedToAppearAt().getSummonedDateTime().getSummonedDate()!=null) {
	 		Calendar summonedDate = brcd.getSummonedToAppearAt().getSummonedDateTime().getSummonedDate().toCalendar();
log.debug("$$$ validateSummonedDate " + "Date " + summonedDate);
	 		Calendar today = Calendar.getInstance();
			Time time = brcd.getSummonedToAppearAt().getSummonedDateTime().getSummonedTime();
			summonedDate.set(Calendar.HOUR_OF_DAY, time.getHour());
			summonedDate.set(Calendar.MINUTE, time.getMinute());
			summonedDate.set(Calendar.SECOND, time.getSeconds());
			summonedDate.set(Calendar.MILLISECOND, time.getMilli());
			
			log.debug("$$$ about to validate "+summonedDate.getTime().toString());
			
			if (summonedDate.before(today)) {
				log.debug("*************** PROBLEM ADDED");
				helper.addLogicalProblem(SUMMONED_TO_APPEAR_DATE_MESSAGE, null);
			}
		}
	}

	/**
	 * Check that the Order Date is today or in the past.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateOrderDate(BreachConditionalDischargeOrderStructure brcd, ValidationHelper helper) {
		log.debug("$$$ validateOrderDate() " + brcd.getDateOfOrder());
		Calendar today = Calendar.getInstance();
		
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		
		Calendar orderDate = brcd.getDateOfOrder().toCalendar();

		if (orderDate.after(today)) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(ORDER_DATE_MUST_BE_IN_PAST_MESSAGE, null);
		}

	}

	/**
	 * Validate all the After Release Conditions
	 * 
	 * @param brcd
	 *            the breach conditional discharge
	 * @param helper
	 *            the helper
	 */
	private static void validateAfterReleaseConditions(BreachConditionalDischargeOrderStructure brcd,
			ValidationHelper helper) {
		// check to ensure that the PostConditions has been selected
	}
  
}