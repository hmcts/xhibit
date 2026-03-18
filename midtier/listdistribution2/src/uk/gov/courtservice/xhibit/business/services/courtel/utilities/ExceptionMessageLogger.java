package uk.gov.courtservice.xhibit.business.services.courtel.utilities;

import uk.gov.courtservice.xhibit.business.services.courtel.exceptions.ExceptionMessage;

/**
 * Exception logger utility class for Courtel
 * 
 * @author shaheeni
 *
 */

public class ExceptionMessageLogger {
	
	//exception utility
    /**
     * Status message indicating failure.
     */
    private static final String FAILED_STATUS = "FAILED STATUS";

  
    public ExceptionMessage logErrorCondition (final String errMsg, final String reason)
    {
        final ExceptionMessage exceptionMessage = new ExceptionMessage ();

        exceptionMessage.setStatus (FAILED_STATUS);
        exceptionMessage.setDetail (errMsg);
        exceptionMessage.setReason(reason);

        return exceptionMessage;
    }
}
