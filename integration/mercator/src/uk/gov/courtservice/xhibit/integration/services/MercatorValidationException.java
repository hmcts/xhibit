package uk.gov.courtservice.xhibit.integration.services;

import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ReturnMVO;

/**
 * <p>
 * Title: MercatorValidationException
 * </p>
 * <p>
 * Description: This is a sub class of Mercator Exception. It will be used when
 * Mercator failed a map because of Validation failure. For more comments see
 * super class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @author Marie Holmberg
 * @version 1.0
 */
public class MercatorValidationException extends MercatorException {

    public MercatorValidationException() {
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     * @param returnValue
     */
    public MercatorValidationException(String errorKey, String logMessage, Throwable cause, ReturnMVO returnValue) {
        super(errorKey, logMessage, cause, returnValue);
    }

    /**
     * @param errorMessage
     *            error message for log
     * @param userMessage
     *            message for user of application
     * @param returnValue
     */
    public MercatorValidationException(String errorKey, String logMessage, ReturnMVO returnValue) {
        super(errorKey, logMessage, returnValue);
    }
}