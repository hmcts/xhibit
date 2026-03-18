package uk.gov.courtservice.xhibit.business.services.courtel.exceptions;

/**
 * Custom exception class for courtel.
 * 
 * @author d120520
 *
 */
public class CourtelListException extends AbstractCourtelListException
{

    /**
     * Constant serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param exceptionMessage contains all of the details of the error that has been logged for this exception
     */
    public CourtelListException (final ExceptionMessage exceptionMessage)
    {
        super (exceptionMessage);

    }
}
