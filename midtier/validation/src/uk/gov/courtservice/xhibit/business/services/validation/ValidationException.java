package uk.gov.courtservice.xhibit.business.services.validation;

public class ValidationException extends Exception {

    /**
     * Serialization id, increment if class structure changes. 
     */
    private static final long serialVersionUID = 1L;

    public ValidationException() {
       super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
