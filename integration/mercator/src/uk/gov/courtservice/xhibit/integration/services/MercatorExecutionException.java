package uk.gov.courtservice.xhibit.integration.services;

import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ReturnMVO;

/**
 * <p>
 * Title: MercatorExecutionException
 * </p>
 * <p>
 * Description: Sub class of Mercator Exception. This will be used when the
 * Mercator map failed the execution. For more comments see super class.
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
public class MercatorExecutionException extends MercatorException {

    public MercatorExecutionException() {
    }

    /**
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     * @param returnValue
     */
    public MercatorExecutionException(String errorKey, String logMessage, Throwable cause, ReturnMVO returnValue) {
        super(errorKey, logMessage, cause, returnValue);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param returnValue
     */
    public MercatorExecutionException(String errorKey, String logMessage, ReturnMVO returnValue) {
        super(errorKey, logMessage, returnValue);
    }

}