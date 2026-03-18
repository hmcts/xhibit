package uk.gov.courtservice.framework.util;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.exceptions.AssertionFailureRuntimeException;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.5 $
 */
public class Contract {
    private static final Logger log = CSServices.getLogger(Contract.class);

    public static void assertTrue(boolean condition) {
        if (!condition) {
            log.fatal("Assertion failed.");
            throw new AssertionFailureRuntimeException("Assertion failed.");
        }
    }

    public static void fail(Throwable t) {
        final String message = "An exception occured which is assumed to never be possible within this system. "
                + "Please consider how the exception occured and replace the assertion that generated this with error handling.";
        log.fatal(message, t);
        throw new CSUnrecoverableException(message, t);
    }
}
