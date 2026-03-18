package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ActionConditionalDischargeOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: ActionConditionalDischargeOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a Action Conditional Discharge
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
public class ActionConditionalDischargeOrderHelper {
	private static final Logger log = CSServices.getLogger(ActionConditionalDischargeOrderHelper.class);
	private static final String ORDER_DATE_MUST_BE_IN_PAST_MESSAGE = "ORDER_ADS_DATE_MUST_BE_IN_PAST";
 	private static final String SUBSEQUENT_CONVICTION_DATE_MESSAGE = "ADS_CONVICTION_DATE_MUST_BE_TODAY_OR_PAST_NOT_BEFORE_DATE_OF_ORDER";
 
	/**
	 * Utility methodValidation
	 * 
	 * @param acd
	 * @param typeCode
	 * @param helper
	 */
	public static void logicalValidateActionConditionalDischarge(ActionConditionalDischargeOrderStructure acd,
			String typeCode, OrderXMLHelper.ValidationHelper helper) {
		log.debug("********************ActionConditionalDischarge: Logical Validation");
		validateBeforeReleaseConditions(acd, helper);
		validateAfterReleaseConditions(acd, helper);
	}

	/**
	 * Utility method to check that any elements whose value depends on other
	 * elements are set correctly. In the community orders the FailedToComply
	 * element must be set to match the Breach option. Failed to comply cannot
	 * be set via the gui as this option was removed, but it remains in the
	 * schema.
	 * 
	 * @param acd
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
	public static String structuralValidateActionConditionalDischarge(ActionConditionalDischargeOrderStructure acd,
			String typeCode, OrderXMLHelper.ValidationHelper helper)
			throws ValidationException, MarshalException, OrderXMLException {
		log.debug("$$$ ActionConditionalDischargeHelper.structuralValidateActionConditionalDischarge");
		return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
	}

	/**
	 * Validate the Pre Release Conditions
	 * 
	 * @param acd
	 *            the breach suspended sentence
	 * @param helper
	 *            the helper
	 */
	private static void validateBeforeReleaseConditions(ActionConditionalDischargeOrderStructure acd,
			ValidationHelper helper) {
		// check to ensure that the PostConditions has been selected
		validateOrderDate(acd, helper);
		validateSubsequentConvictionDate(acd, helper);
 	}
 	 
	/**
	 * Check that the SubsequentConviction date is on or after order date.
	 * 
	 * @param brss
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateSubsequentConvictionDate(ActionConditionalDischargeOrderStructure acd, ValidationHelper helper) {

		log.debug("$$$ validateSubsequentConvictionDate " + "Date " + acd.getConvictionDate());
 		Calendar orderDate = acd.getDateOfOrder().toCalendar();
		Calendar subsequentConvictionDate = acd.getConvictionDate().toCalendar();
		Calendar today = Calendar.getInstance();
		
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0); 

		if (subsequentConvictionDate.after(today) || subsequentConvictionDate.before(orderDate) ) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(SUBSEQUENT_CONVICTION_DATE_MESSAGE, null);
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
	private static void validateOrderDate(ActionConditionalDischargeOrderStructure acd, ValidationHelper helper) {
		log.debug("$$$ validateOrderDate() " + acd.getDateOfOrder());
		Calendar today = Calendar.getInstance();
		
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		
		Calendar orderDate = acd.getDateOfOrder().toCalendar();

		if (orderDate.equals(today) || orderDate.after(today)) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(ORDER_DATE_MUST_BE_IN_PAST_MESSAGE, null);
		}

	}

	/**
	 * Validate all the After Release Conditions
	 * 
	 * @param acd
	 *            the breach suspended sentence
	 * @param helper
	 *            the helper
	 */
	private static void validateAfterReleaseConditions(ActionConditionalDischargeOrderStructure acd,
			ValidationHelper helper) {
		// check to ensure that the PostConditions has been selected
	}
 
}