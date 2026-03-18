package uk.gov.courtservice.framework.services.xml;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Faisal Shoukat
 * @version 1.0
 */

public class CSXMLServicesException extends CSUnrecoverableException {

    public CSXMLServicesException() {
    }

    /**
     * Extended constructor
     * 
     * @param String
     */
    public CSXMLServicesException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Constructor to store original exception with
     * 
     * @param Throwable
     */
    public CSXMLServicesException(Throwable cause) {
        super(cause);
    }

    // public CSXMLServicesException( Message userMessage, Throwable cause )
    // { super( userMessage, cause );
    // }

    /**
     * Extended constructor
     * 
     * @param String
     * @param Throwable
     */
    public CSXMLServicesException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

    // public CSXMLServicesException( Message userMessage, String errorMsg,
    // Throwable cause )
    // { super( userMessage, errorMsg, cause );
    // }

    /**
     * Constructor that takes an errorMessage and a Message as an argument. The
     * error message here is the message caught from an other exception
     * 
     * @param String
     * @param Message
     */
    // public CSXMLServicesException(String errorMsg, Message userMessage)
    // { super( errorMsg, userMessage );
    // }
}