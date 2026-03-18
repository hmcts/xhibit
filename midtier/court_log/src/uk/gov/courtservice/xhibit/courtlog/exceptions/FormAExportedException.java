package uk.gov.courtservice.xhibit.courtlog.exceptions;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * Exception class used when a an end of hearing event is deleted
 * after the Form A has been exported.
 */
public class FormAExportedException extends CourtLogBusinessException {

    private static final long serialVersionUID = 1L;

    // declared this initialisation externally to the constructor to prevent
    // it
    // being left out when adding extra constructors...
    {
        // when processing linked cases, this should not abort the process if
        // the linked case has already ended...
        this.setAborted(false);
    }

    /**
     * Constructor that takes all of the details to be logged and/or raised as a
     * business exception
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param parameters
     *            The parameters for the message
     * @param logMessage
     *            error message for log
     */
    public FormAExportedException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }

    /**
     * Constructor that takes all of the details to be logged and/or raised as a
     * business exception
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public FormAExportedException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * Constructor that takes another business exception to allow this exception
     * to simply wrap the exception.
     * 
     * @param cause
     *            The original exception.
     */
    public FormAExportedException(CSBusinessException cause) {
        super(cause);
    }
}
