package uk.gov.courtservice.xhibit.xmlbinding.orders;
 
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.NoticeOfDefermentSentenceOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: NoticeOfDefermentSentenceOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a Notice of DefermentSentence
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
public class NoticeOfDefermentSentenceOrderHelper {
	private static final Logger log = CSServices.getLogger(NoticeOfDefermentSentenceOrderHelper.class);
	private static final String ORDER_NDS_DATE_MUST_BE_IN_FUTURE_MESSAGE = "ORDER_DATE_MUST_BE_IN_FUTURE";

	/*
	 * Utility methodValidation
	 * 
	 * @param nds
	 * 
	 * @param typeCode
	 * 
	 * @param helper
	 */
	public static void logicalValidateNoticeOfDefermentSentence(NoticeOfDefermentSentenceOrderStructure nds,
			String typeCode, OrderXMLHelper.ValidationHelper helper) {
		log.debug("********************NoticeOfDefermentSentence: Logical Validation");
		validateBeforeReleaseConditions(nds, helper);
		validateAfterReleaseConditions(nds, helper);
	}

	/**
	 * Utility method to check that any elements whose value depends on other
	 * elements are set correctly. In the community orders the FailedToComply
	 * element must be set to match the Breach option. Failed to comply cannot
	 * be set via the gui as this option was removed, but it remains in the
	 * schema.
	 * 
	 * @param nds
	 *            the NoticeOfDefermentsentence
	 * @param typeCode
	 *            the order type
	 * @param helper
	 *            the helper
	 * @return the validated xml
	 * @throws ValidationException
	 * @throws MarshalException
	 * @throws OrderXMLException
	 */
	public static String structuralValidateNoticeOfDefermentSentence(NoticeOfDefermentSentenceOrderStructure nds,
			String typeCode, OrderXMLHelper.ValidationHelper helper)
			throws ValidationException, MarshalException, OrderXMLException {
		log.debug("$$$ NoticeOfDefermentSentenceHelper.structuralValidateNoticeOfDefermentSentence");
		return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
	}

	/**
	 * Validate the Pre Release Conditions
	 * 
	 * @param nds
	 *            the NoticeOfDefermentSentence
	 * @param helper
	 *            the helper
	 */
	private static void validateBeforeReleaseConditions(NoticeOfDefermentSentenceOrderStructure nds,
			ValidationHelper helper) {
		// check to ensure that the PostConditions has been selected
		validateOrderDate(nds, helper);
	}

	/**
	 * Check that the Order Date is today or in the future.
	 * 
	 * @param nds
	 *            the order structure
	 * @param helper
	 *            the helper
	 */
	private static void validateOrderDate(NoticeOfDefermentSentenceOrderStructure nds, ValidationHelper helper) {
		log.debug("$$$ validateOrderDate() " + nds.getDateOfOrder());
		Calendar today = Calendar.getInstance();
		
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		today.set(Calendar.HOUR_OF_DAY, 0);
    
		if (nds.getDateOfOrder().toCalendar().before(today)) {
			log.debug("*************** PROBLEM ADDED");
			helper.addLogicalProblem(ORDER_NDS_DATE_MUST_BE_IN_FUTURE_MESSAGE, null);
		}

	}

	/**
	 * Validate all the After Release Conditions
	 * 
	 * @param nds
	 *            the breach suspended sentence
	 * @param helper
	 *            the helper
	 */
	private static void validateAfterReleaseConditions(NoticeOfDefermentSentenceOrderStructure nds,
			ValidationHelper helper) {
		// check to ensure that the PostConditions has been selected
	}

	 
}