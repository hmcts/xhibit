package uk.gov.courtservice.xhibit.client.order.exceptions;

/**
 * This exception is thrown when the DataEntryReader cannot load a
 * DataEntryPanel.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class DataEntryReaderException extends uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException {
    private static final String ERROR_KEY = "order.dataentry.error";

    /**
     * Constructs a DataEntryReaderException using a throwable.
     * 
     * @param t
     *            Throwable object required by GenericException.
     */
    public DataEntryReaderException(Throwable t) {
        super(ERROR_KEY, ERROR_KEY, t);
    }

    /**
     * Constructs a DataEntryReaderException with the chosen error message.
     * 
     * @param s
     *            String representation of the error message.
     */
    public DataEntryReaderException(String s) {
        super(ERROR_KEY, s);
    }
}
