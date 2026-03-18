package uk.gov.courtservice.xhibit.integration.services;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ReturnMVO;

/**
 * <p>
 * Title: MercatorException
 * </p>
 * <p>
 * Description: This is the super class of the Mercator Exceptions. It has only
 * one attribute which is the ReturnMVO. All the data about the exception will
 * be stored in this Object. See ReturnMVO for more information.
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
public class MercatorException extends CSBusinessException // CSRecoverableException
{

    private ReturnMVO returnValue;

    public MercatorException() {
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     * @param returnValue
     */
    public MercatorException(String errorKey, String logMessage, Throwable cause, ReturnMVO returnValue) {
        super(errorKey, logMessage, cause);
        this.returnValue = returnValue;
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param returnValue
     */
    public MercatorException(String errorKey, String logMessage, ReturnMVO returnValue) {
        super(errorKey, logMessage);
        this.returnValue = returnValue;
    }

    public ReturnMVO getReturnValue() {
        return returnValue;
    }

}