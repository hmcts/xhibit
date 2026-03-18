package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ReleaseFromPrisonOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: ReleaseFromPrisonOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a Release From Prison Order
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


public class RPOrderHelper {
    private static final Logger log = CSServices.getLogger(RPOrderHelper.class);

    private static final String GOVERNOR_MESSAGE = "ORDER_Release_From_Prison_Governor_Empty";

    


    /**
     * Utility methodValidation
     * 
     * @param bos
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateRPOrder(ReleaseFromPrisonOrderStructure rpos, String typeCode,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("********************Release From Prison Order: Logical Validation");
        validatePrison(rpos, helper);
    }
    
    
    /**
     * Validate the Prison details
     * 
     * @param rpos
     *            the release from prison order
     * @param helper
     *            the helper
     */
    private static void validatePrison(ReleaseFromPrisonOrderStructure rpos, ValidationHelper helper) {
        String governor = rpos.getGovernor();
        // check to ensure that the additional notes option has been selected
        if (isEmptyString(governor)) {
            // No prison details entered
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(GOVERNOR_MESSAGE, null);
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
