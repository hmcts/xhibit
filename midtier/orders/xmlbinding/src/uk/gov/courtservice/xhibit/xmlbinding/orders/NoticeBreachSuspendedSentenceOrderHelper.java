package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.NoticeBreachSuspendedSentenceOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: NoticeBreachSuspendedSentenceOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a NoticeBreachSuspendedSentence
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jayalakshmi Parthiban
 * @version 1.0
 */

//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class NoticeBreachSuspendedSentenceOrderHelper {
	private static final Logger log = CSServices.getLogger(NoticeBreachSuspendedSentenceOrderHelper.class);
	private static final String SUSPENDED_SENTENCED_DATE_MESSAGE = "NBSS_SUSPENDED_SENTENCED_DATE_MUST_BE_AFTER_DATE_OF_ORDER";
	private static final String CONVICTED_DATE_MESSAGE = "NBSS_CONVICTED_DATE_MUST_BE_EQUAL_TO_OR_AFTER_SUSPENDED_DATE";
	private static final String FINE_MESSAGE = "NBSS_FINE_MUST_BE_GREATER_0_LESS_THAN_99999999.99";
	private static final String COMMUNITY_REQ_MESSAGE = "NBSS_COMMUNITY_REQ_MUST_HAVE_MIN_ONE_CHAR_MAX_500";
	private static final String SUPERVISION_PERIOD_MESSAGE = "NBSS_SUPERVISION_PERIOD_MUST_BE_ATLEAST_ONE_DAY";
	private static final String OPERATIONAL_PERIOD_MESSAGE = "NBSS_OPERATIONAL_PERIOD_MUST_BE_ATLEAST_ONE_DAY";
	private static final String SUSPENDED_SUBSTITUTE_PERIOD_MESSAGE = "NBSS_SUSPENDED_SUBSTITUTE_PERIOD_MUST_BE_ATLEAST_ONE_DAY";

	/**
	 * Utility methodValidation
	 * 
	 * @param nbss
	 * @param typeCode
	 * @param helper
	 */
	public static void logicalValidateNoticeBreachSuspendedSentence(NoticeBreachSuspendedSentenceOrderStructure nbss,
			String typeCode, OrderXMLHelper.ValidationHelper helper) {
		log.debug("********************NoticeBreachSuspenedSentence: Logical Validation");
		validateBeforeReleaseConditions(nbss, helper);
	}

	/**
	 * Utility method to check that any elements whose value depends on other
	 * elements are set correctly. In the community orders the FailedToComply
	 * element must be set to match the Breach option. Failed to comply cannot
	 * be set via the gui as this option was removed, but it remains in the
	 * schema.
	 * 
	 * @param nbss
	 *            the NoticeBreachSuspendedSentence
	 * @param typeCode
	 *            the order type
	 * @param helper
	 *            the helper
	 * @return the validated xml
	 * @throws ValidationException
	 * @throws MarshalException
	 * @throws OrderXMLException
	 */
	public static String structuralValidateNoticeBreachSuspendedSentence(
			NoticeBreachSuspendedSentenceOrderStructure nbss, String typeCode, OrderXMLHelper.ValidationHelper helper)
			throws ValidationException, MarshalException, OrderXMLException {
		log.debug("$$$ NoticeBreachSuspendedSentenceHelper.structuralValidateNoticeBreachSuspendedSentence");
		return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
	}

	/**
	 * Validate the Pre Release Conditions
	 * 
	 * @param nbss
	 *            the NoticeBreachSuspendedSentence
	 * @param helper
	 *            the helper
	 */
	private static void validateBeforeReleaseConditions(NoticeBreachSuspendedSentenceOrderStructure nbss,
			ValidationHelper helper) {
		// check to ensure that the PostConditions has been selected
		validateSuspendedSentenceDate(nbss, helper);
		validateSuspendedSentenceSubstitute(nbss, helper);
		validateConvictionDate(nbss, helper);
		validateFine(nbss, helper);
		validateCommReq(nbss, helper);
		validateSupervisionPeriod(nbss, helper);
		validateOperationalPeriod(nbss, helper);
	}

	/**
	 * Check that supsended sentence imposed Date is in the past.
	 * 
	 * @param nbss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSuspendedSentenceDate(NoticeBreachSuspendedSentenceOrderStructure nbss,
			ValidationHelper helper) {
		log.debug("$$$ Suspended Sentence Date() " + nbss.getSuspendedDate());
		Calendar today = Calendar.getInstance();

		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		if (nbss.getSuspendedDate().toCalendar().equals(today) || nbss.getSuspendedDate().toCalendar().after(today)) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(SUSPENDED_SENTENCED_DATE_MESSAGE, null);
		}
	}

	/**
	 * Check that conviction date must be equal to or later than suspended
	 * sentence date.
	 * 
	 * @param nbss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateConvictionDate(NoticeBreachSuspendedSentenceOrderStructure nbss,
			ValidationHelper helper) {
		log.debug("$$$ ConvictionDate() " + nbss.getConvictionDate());

		if (nbss.getConvictionDate().toCalendar().before(nbss.getSuspendedDate().toCalendar())) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(CONVICTED_DATE_MESSAGE, null);
		}
	}

	/**
	 * Check that the Suspended Sentence Substitution period is at least one
	 * day.
	 * 
	 * @param nbss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSuspendedSentenceSubstitute(NoticeBreachSuspendedSentenceOrderStructure nbss,
			ValidationHelper helper) {

		String zero = "0";
		log.debug("$$$ validateSuspendedSentenceSubstitute " + "Days "
				+ nbss.getOrderEffective().getTermNBSS().getSuspendedSentenceSubstitute().getDays() + "Weeks "
				+ nbss.getOrderEffective().getTermNBSS().getSuspendedSentenceSubstitute().getWeeks() + "Months "
				+ nbss.getOrderEffective().getTermNBSS().getSuspendedSentenceSubstitute().getMonths() + "Years "
				+ nbss.getOrderEffective().getTermNBSS().getSuspendedSentenceSubstitute().getYears());

		if (nbss.getOrderEffective().getTermNBSS().getSelected() == true &&
				nbss.getOrderEffective().getTermNBSS().getSuspendedSentenceSubstitute().getSelected() == true) {
			log.debug("*************** Value is evaluated as SuspendedSentenceSubstitute ******************");
			if (zero.equals(nbss.getOrderEffective().getTermNBSS().getSuspendedSentenceSubstitute().getDays())
					&& zero.equals(nbss.getOrderEffective().getTermNBSS().getSuspendedSentenceSubstitute().getWeeks())
					&& zero.equals(nbss.getOrderEffective().getTermNBSS().getSuspendedSentenceSubstitute().getMonths())
					&& zero.equals(nbss.getOrderEffective().getTermNBSS().getSuspendedSentenceSubstitute().getYears())) {
				log.debug("*************** PROBLEM ADDED");
				helper.addLogicalProblem(SUSPENDED_SUBSTITUTE_PERIOD_MESSAGE, null);
			}
		}

	}

	/**
	 * Check that fine is greater than 0.0 and less than 99999999.99.
	 * 
	 * @param nbss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateFine(NoticeBreachSuspendedSentenceOrderStructure nbss, ValidationHelper helper) {
		log.debug("$$$ Fine " + nbss.getOrderEffective().getFineValue());
 	            
		if (nbss.getOrderEffective().getOrderEffectiveType() != null
				&& "Fine".equals(nbss.getOrderEffective().getOrderEffectiveType().toString().trim())) {
			log.debug("*************** Value is evaluated as Fine ******************");
		    if ( Double.parseDouble(nbss.getOrderEffective().getFineValue().getContent()) < 0.00
						|| Double.parseDouble(nbss.getOrderEffective().getFineValue().getContent()) > 99999999.99) {
 				log.debug("*************** PROBLEM ADDED*************** For Fine Value");
				helper.addLogicalProblem(FINE_MESSAGE, null);
			}
		}
	}

	/**
	 * Check that CommunityReq text has at least one char other than space /
	 * tab.
	 * 
	 * @param nbss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateCommReq(NoticeBreachSuspendedSentenceOrderStructure nbss, ValidationHelper helper) {
		log.debug("$$$ CommunityRequirement " + nbss.getOrderEffective().getCommunityReq());

		if (nbss.getOrderEffective().getOrderEffectiveType() != null
				&& "CommunityRequirement".equals(nbss.getOrderEffective().getOrderEffectiveType().toString().trim())) {
			log.debug("*************** Value is evaluated as CommunityRequirement ******************");
			if (nbss.getOrderEffective().getCommunityReq() == null
					|| (nbss.getOrderEffective().getCommunityReq() != null
					&& (nbss.getOrderEffective().getCommunityReq().getContent().trim().length() == 0
					||  nbss.getOrderEffective().getCommunityReq().getContent().trim().length() > 500))) {

				log.debug("*************** PROBLEM ADDED");
				helper.addLogicalProblem(COMMUNITY_REQ_MESSAGE, null);
			}
		}
	}

	/**
	 * Check that the Supervision period is at least one day.
	 * 
	 * @param nbss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSupervisionPeriod(NoticeBreachSuspendedSentenceOrderStructure nbss,
			ValidationHelper helper) {

		String zero = "0";
		log.debug("$$$ validateSupervisionPeriod " + "Days " + nbss.getOrderEffective().getSupervisionalPeriod().getDays()
				+ "Weeks " + nbss.getOrderEffective().getSupervisionalPeriod().getWeeks() + "Months "
				+ nbss.getOrderEffective().getSupervisionalPeriod().getMonths() + "Years "
				+ nbss.getOrderEffective().getSupervisionalPeriod().getYears());

		if (nbss.getOrderEffective().getSupervisionalPeriod().getSelected() == true) {
			log.debug("*************** Value is evaluated as SupervisionPeriod ******************");
			if (zero.equals(nbss.getOrderEffective().getSupervisionalPeriod().getDays())
					&& zero.equals(nbss.getOrderEffective().getSupervisionalPeriod().getWeeks())
					&& zero.equals(nbss.getOrderEffective().getSupervisionalPeriod().getMonths())
					&& zero.equals(nbss.getOrderEffective().getSupervisionalPeriod().getYears())) {
				log.debug("*************** PROBLEM ADDED");
				helper.addLogicalProblem(SUPERVISION_PERIOD_MESSAGE, null);
			}
		} 

	}

	/**
	 * Check that the Operational period is at least one day.
	 * 
	 * @param nbss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateOperationalPeriod(NoticeBreachSuspendedSentenceOrderStructure nbss,
			ValidationHelper helper) {

		String zero = "0";
		log.debug("$$$ validateOperationalPeriod " + "Days " + nbss.getOrderEffective().getOperationPeriod().getDays()
				+ "Weeks " + nbss.getOrderEffective().getOperationPeriod().getWeeks() + "Months "
				+ nbss.getOrderEffective().getOperationPeriod().getMonths() + "Years "
				+ nbss.getOrderEffective().getOperationPeriod().getYears());

		if (nbss.getOrderEffective().getOperationPeriod().getSelected() == true) {
			log.debug("*************** Value is evaluated as OperationalPeriod ******************");
			if (zero.equals(nbss.getOrderEffective().getOperationPeriod().getDays())
					&& zero.equals(nbss.getOrderEffective().getOperationPeriod().getWeeks())
					&& zero.equals(nbss.getOrderEffective().getOperationPeriod().getMonths())
					&& zero.equals(nbss.getOrderEffective().getOperationPeriod().getYears())) {

				log.debug("*************** PROBLEM ADDED");
				helper.addLogicalProblem(OPERATIONAL_PERIOD_MESSAGE, null);
			}

		} 

	}

}