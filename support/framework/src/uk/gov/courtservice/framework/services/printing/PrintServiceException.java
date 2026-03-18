package uk.gov.courtservice.framework.services.printing;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

public class PrintServiceException extends CSUnrecoverableException {

    public PrintServiceException() {
        super();
    }

    public PrintServiceException(Throwable e) {
        super(e);
    }
}