package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.NoticeOfAcquittalOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.XHIBITCountType;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: NAOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a Notice Of Acquittal Order
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Brian Hingston
 * @version 1.0
 */


public class NAOrderHelper {
    private static final Logger log = CSServices.getLogger(NAOrderHelper.class);

    private static final String GOVERNOR_MESSAGE = "ORDER_Notice_Of_Acquittal_Governor_Empty";
    
    private static final String CHARGES_MESSAGE = "ORDER_Notice_Of_Acquittal_Charges_Empty";
    
    private static final String COUNT_MESSAGE = "ORDER_Notice_Of_Acquittal_Counts_Empty";

    


    /**
     * Utility methodValidation
     * 
     * @param bos
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateNAOrder(NoticeOfAcquittalOrderStructure naos, String typeCode,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("********************Notice of Acquittal Order: Logical Validation");
        validatePrison(naos, helper);
        validateCharges(naos, helper);
        validateCountNumbers(naos, helper);
    }
    
    
    /**
     * Validate the Prison details
     * 
     * @param naos
     *            the notice of acquittal order
     * @param helper
     *            the helper
     */
    private static void validatePrison(NoticeOfAcquittalOrderStructure naos, ValidationHelper helper) {
        String governor = naos.getGovernor();

        if (isEmptyString(governor)) {
            // No prison details entered
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(GOVERNOR_MESSAGE, null);
        }
    }
    
    /**
     * Validate the charges details
     * 
     * @param naos
     *            the notice of acquittal order
     * @param helper
     *            the helper
     */
    private static void validateCharges(NoticeOfAcquittalOrderStructure naos, ValidationHelper helper) {
        String charges = naos.getCharges();

        if (isEmptyString(charges)) {
            // No charges details entered
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(CHARGES_MESSAGE, null);
        }
    }
    
    /**
     * Validate the count details
     * 
     * @param naos
     *            the notice of acquittal order
     * @param helper
     *            the helper
     */
    private static void validateCountNumbers(NoticeOfAcquittalOrderStructure naos, ValidationHelper helper) {
        // check to ensure that the specific counts has been selected
        XHIBITCountType Count = naos.getCount().getWhichCounts();
        if (Count.equals(XHIBITCountType.SPECIFICCOUNTS) ) {
            String CountNumbers = naos.getCount().getCountNumbers();
            if (isEmptyString(CountNumbers)) {
                // No count details entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(COUNT_MESSAGE, null);
            }
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
