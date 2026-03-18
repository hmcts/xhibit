package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.XHIBITPeriodStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.XHIBITExtendedPeriodStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;

/**
 * <p>
 * Title: Helper class for dealing with XhibitPeriodStructuress
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Provides functionality that supports the manipulation and validation of
 * XhibitPeriodStructuress.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public class XhibitPeriodHelper {
    private static final Logger log = CSServices.getLogger(XhibitPeriodHelper.class);

    // Not allowed to instantiate.
    private XhibitPeriodHelper() {
    }

    /**
     * This method validate that the passed in period structure is non-zero. If
     * the passed in period structure is null, then it will fail the check. So
     * if it may logically be null, perform a null check to see if this method
     * should be called.
     * 
     * @param xps
     *            The period structure to check.
     * @param helper
     *            The ValidationHelper that keeps track of any failures.
     * @param messageCode
     *            The message code to use to record this particular problem.
     */
    public static void validateExtendedPeriodNonZero(XHIBITExtendedPeriodStructure xps, OrderXMLHelper.ValidationHelper helper,
            String messageCode) {
        if (xps == null // We have a null period structure
                || // OR
                !( // NOT Either
                (xps.hasDays() && xps.getDays() > 0) // Has days > 0
                        || // OR
                        (xps.hasMonths() && xps.getMonths() > 0) // Has
                // months
                // > 0
                || //OR
                (xps.hasWeeks() && xps.getWeeks() > 0) //Has weeks > 0
                || // OR
                (xps.hasYears() && xps.getYears() > 0) // Has years > 0
                ))// Then there is a problem.
        {
            log.debug("*****************validatePeriodNonZero: failed for : " + messageCode);
            helper.addLogicalProblem(messageCode, null);
        }
    }
    
    /**
     * This method validate that the passed in exteneded period structure is non-zero. If
     * the passed in extended period structure is null, then it will fail the check. So
     * if it may logically be null, perform a null check to see if this method
     * should be called.
     * 
     * @param xps
     *            The extended period structure to check.
     * @param helper
     *            The ValidationHelper that keeps track of any failures.
     * @param messageCode
     *            The message code to use to record this particular problem.
     */
    public static void validatePeriodNonZero(XHIBITPeriodStructure xps, OrderXMLHelper.ValidationHelper helper,
            String messageCode) {
        if (xps == null // We have a null period structure
                || // OR
                !( // NOT Either
                (xps.hasDays() && xps.getDays() > 0) // Has days > 0
                        || // OR
                        (xps.hasMonths() && xps.getMonths() > 0) // Has
                // months
                // > 0
                || //OR
                (xps.hasWeeks() && xps.getWeeks() > 0) //Has weeks > 0
                || // OR
                (xps.hasYears() && xps.getYears() > 0) // Has years > 0
                ))// Then there is a problem.
        {
            log.debug("*****************validatePeriodNonZero: failed for : " + messageCode);
            helper.addLogicalProblem(messageCode, null);
        }
    }
}