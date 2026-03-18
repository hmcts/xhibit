package uk.gov.courtservice.xhibit.client.im.exception;

/**
 * <p>
 * Title: IMTextException
 * </p>
 * <p>
 * Description: Exception thrown when the IM text area has invalid content
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class IMTextException extends IMGenericException {

    public IMTextException() {
        super();
    }

    public IMTextException(String key, String message) {
        super(key, message);
    }

}