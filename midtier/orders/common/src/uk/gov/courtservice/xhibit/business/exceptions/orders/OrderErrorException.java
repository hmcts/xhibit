package uk.gov.courtservice.xhibit.business.exceptions.orders;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

public class OrderErrorException extends CSUnrecoverableException {
	
	static final long serialVersionUID = 5606856676569345354L;
	
    public OrderErrorException() {
    }

    public OrderErrorException(Throwable throwable) {
        super(throwable);
    }

    public OrderErrorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public OrderErrorException(String s) {
        super(s);
    }
}
