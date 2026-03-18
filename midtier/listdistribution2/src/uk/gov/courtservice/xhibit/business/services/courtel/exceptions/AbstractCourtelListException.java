package uk.gov.courtservice.xhibit.business.services.courtel.exceptions;

/**
 * Abstract Exception class that all custom application exception classes should inherit from.
 * 
 * @author d120520
 *
 */
public abstract class AbstractCourtelListException extends Exception
{

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The details of the error that has been logged for this exception.
     */
    // CHECKSTYLE:OFF
    private ExceptionMessage exceptionMessage = null;
    // CHECKSTYLE:ON

    /**
     * Constructor.
     * 
     * @param exceptionMessage contains all of the details of the error that has been logged for this exception
     */
    public AbstractCourtelListException (final ExceptionMessage exceptionMessage)
    {
        super (exceptionMessage.getDetail ());
        setExceptionDetails (exceptionMessage);
    }

    /**
     * @return the exceptionDetails
     */
    public ExceptionMessage getExceptionDetails ()
    {
        return exceptionMessage;
    }

    /**
     * @param exceptionMessage the exceptionDetails to set
     */
    public void setExceptionDetails (final ExceptionMessage exceptionMessage)
    {
        this.exceptionMessage = exceptionMessage;
    }
}
