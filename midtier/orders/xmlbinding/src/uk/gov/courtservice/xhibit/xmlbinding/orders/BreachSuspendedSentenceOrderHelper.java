package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.BreachSuspendedSentenceOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: BreachSuspendedSentenceOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a BreachSuspendedSentenceOrder
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
public class BreachSuspendedSentenceOrderHelper {
	private static final Logger log = CSServices.getLogger(BreachSuspendedSentenceOrderHelper.class);
	private static final String ORDER_DATE_MUST_BE_IN_PAST_MESSAGE = "ORDER_BRSS_DATE_MUST_BE_IN_PAST";
	private static final String TERM_OF_IMPRISONMENT_MESSAGE = "TERM_OF_IMPRISONMENT_MUST_BE_ATLEAST_ONE_DAY";
	private static final String SUSPENDED_FOR_MESSAGE = "SUSPENDED_FOR_MUST_BE_ATLEAST_ONE_DAY";
	private static final String SUBSEQUENT_CONVICTION_COURT_MESSAGE = "SUBSEQUENT_CONVICTION_COURT_MUST_BE_SELECTED";
	private static final String SUBSEQUENT_CONVICTION_DATE_MESSAGE = "SUBSEQUENT_CONVICTION_DATE_MUST_BE_EQUAL_OR_BEFORE_DATE_OF_ORDER";
	private static final String SUMMONED_TO_APPEAR_COURT_MESSAGE = "SUMMONED_TO_APPEAR_COURT_MUST_BE_SELECTED";
	private static final String SUMMONED_TO_APPEAR_DATE_MESSAGE = "SUMMONED_TO_APPEAR_DATE_MUST_BE_EQUAL_OR_AFTER_DATE_OF_ORDER";

	/**
	 * Utility methodValidation
	 * 
	 * @param bss
	 * @param typeCode
	 * @param helper
	 */
	public static void logicalValidateBreachSuspendedSentence(BreachSuspendedSentenceOrderStructure bss,
			String typeCode, OrderXMLHelper.ValidationHelper helper) {
		log.debug("********************BreachSuspenedSentence: Logical Validation");
		validateBeforeReleaseConditions(bss, helper);
		validateAfterReleaseConditions(bss, helper);
	}

	/**
	 * Utility method to check that any elements whose value depends on other
	 * elements are set correctly. In the community orders the FailedToComply
	 * element must be set to match the Breach option. Failed to comply cannot
	 * be set via the gui as this option was removed, but it remains in the
	 * schema.
	 * 
	 * @param bss
	 *            the breach suspended sentence
	 * @param typeCode
	 *            the order type
	 * @param helper
	 *            the helper
	 * @return the validated xml
	 * @throws ValidationException
	 * @throws MarshalException
	 * @throws OrderXMLException
	 */
	public static String structuralValidateBreachSuspendedSentence(BreachSuspendedSentenceOrderStructure bss,
			String typeCode, OrderXMLHelper.ValidationHelper helper)
			throws ValidationException, MarshalException, OrderXMLException {
		log.debug("$$$ BreachSuspendedSentenceHelper.structuralValidateBreachSuspendedSentence");
		return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
	}

	/**
	 * Validate the Pre Release Conditions
	 * 
	 * @param bss
	 *            the breach suspended sentence
	 * @param helper
	 *            the helper
	 */
	private static void validateBeforeReleaseConditions(BreachSuspendedSentenceOrderStructure bss,
			ValidationHelper helper) {
		// check to ensure that the PostConditions has been selected
		validateOrderDate(bss, helper);
		validateTermOfImprisonment(bss, helper);
		validateSuspendedFor(bss, helper);
		validateSubsequentConvictionCourt(bss, helper);
		validateSubsequentConvictionDate(bss, helper);
		validateSummonedCourt(bss, helper);
		validateSummonedDate(bss, helper);
	}

	/**
	 * Check that the TermOfImprisonment is at least one day.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateTermOfImprisonment(BreachSuspendedSentenceOrderStructure bss, ValidationHelper helper) {

		String zero = "0";
		log.debug("$$$ validateTermOfImprisonment " + "Days " + bss.getTermOfImprisonment().getDays() + "Weeks "
				+ bss.getTermOfImprisonment().getWeeks() + "Months " + bss.getTermOfImprisonment().getMonths()
				+ "Years " + bss.getTermOfImprisonment().getYears());

		if (zero.equals(bss.getTermOfImprisonment().getDays()) && zero.equals(bss.getTermOfImprisonment().getWeeks())
				&& zero.equals(bss.getTermOfImprisonment().getMonths())
				&& zero.equals(bss.getTermOfImprisonment().getYears())) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(TERM_OF_IMPRISONMENT_MESSAGE, null);
		}

	}

	/**
	 * Check that the Suspension is at least one day.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSuspendedFor(BreachSuspendedSentenceOrderStructure bss, ValidationHelper helper) {

		String zero = "0";
		log.debug("$$$ validateSuspendedFor " + "Days " + bss.getSuspendedFor().getDays() + "Weeks "
				+ bss.getSuspendedFor().getWeeks() + "Months " + bss.getSuspendedFor().getMonths() + "Years "
				+ bss.getSuspendedFor().getYears());

		if ((bss.getSuspendedFor().getDays() == null || zero.equals(bss.getSuspendedFor().getDays()))
				&& (bss.getSuspendedFor().getWeeks() == null || zero.equals(bss.getSuspendedFor().getWeeks()))
				&& (bss.getSuspendedFor().getMonths() == null || zero.equals(bss.getSuspendedFor().getMonths()))
				&& (bss.getSuspendedFor().getYears() == null || zero.equals(bss.getSuspendedFor().getYears()))) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(SUSPENDED_FOR_MESSAGE, null);
		}

	}

	/**
	 * Check that the Subsequent Conviction court is selected.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
 	 private static void validateSubsequentConvictionCourt(BreachSuspendedSentenceOrderStructure bss,
			ValidationHelper helper) {

		log.debug("$$$ validateSubsequentConvictionCourt " + "Court Selected "
				+ bss.getSubsequentConviction().getCourtHouseDetails().getCourtHouseName());

		if (bss.getSubsequentConviction().getCourtHouseDetails().getCourtHouseName() == null
				|| "".equals(bss.getSubsequentConviction().getCourtHouseDetails().getCourtHouseName())) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(SUBSEQUENT_CONVICTION_COURT_MESSAGE, null);
		}
	}  

	/**
	 * Check that the SubsequentConviction date is on or after order date.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSubsequentConvictionDate(BreachSuspendedSentenceOrderStructure bss, ValidationHelper helper) {

		log.debug("$$$ validateSubsequentConvictionDate " + "Date " + bss.getSubsequentConviction().getSubsConvDate());
 		Calendar orderDate = bss.getDateOfOrder().toCalendar();
		Calendar subsequentConvictionDate = bss.getSubsequentConviction().getSubsConvDate().toCalendar();
		
		Calendar today = Calendar.getInstance();
		
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
   
		if (subsequentConvictionDate.before(orderDate) || subsequentConvictionDate.after(today)) {
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
	private static void validateSummonedCourt(BreachSuspendedSentenceOrderStructure bss, ValidationHelper helper) {

		log.debug("$$$ validateSummonedCourt " + "Court Selected "
				+ bss.getSummonedToAppearAt().getCourtHouseDetails().getCourtHouseName());

		if (bss.getSummonedToAppearAt().getCourtHouseDetails().getCourtHouseName() == null
				|| "".equals(bss.getSummonedToAppearAt().getCourtHouseDetails().getCourtHouseName())) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(SUMMONED_TO_APPEAR_COURT_MESSAGE, null);
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
	private static void validateSummonedDate(BreachSuspendedSentenceOrderStructure bss,
			ValidationHelper helper) {

		//adding null check as may not have specified date/time
		if(bss.getSummonedToAppearAt().getSummonedDateTime()!=null &&				bss.getSummonedToAppearAt().getSummonedDateTime().getSummonedDate()!=null) {
	 		Calendar summonedDate = bss.getSummonedToAppearAt().getSummonedDateTime().getSummonedDate().toCalendar();
log.debug("$$$ validateSummonedDate " +summonedDate);
	 
			if (summonedDate.before(bss.getSubsequentConviction().getSubsConvDate().toCalendar())) {
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
	private static void validateOrderDate(BreachSuspendedSentenceOrderStructure bss, ValidationHelper helper) {
		log.debug("$$$ validateOrderDate() " + bss.getDateOfOrder());
		Calendar today = Calendar.getInstance();
		
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		today.set(Calendar.HOUR_OF_DAY, 0);
     
		Calendar orderDate = bss.getDateOfOrder().toCalendar();

		if (orderDate.after(today)) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(ORDER_DATE_MUST_BE_IN_PAST_MESSAGE, null);
		}

	}

	/**
	 * Validate all the After Release Conditions
	 * 
	 * @param bss
	 *            the breach suspended sentence
	 * @param helper
	 *            the helper
	 */
	private static void validateAfterReleaseConditions(BreachSuspendedSentenceOrderStructure bss,
			ValidationHelper helper) {
		// check to ensure that the PostConditions has been selected
	}
 
 
}