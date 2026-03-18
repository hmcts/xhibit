package uk.gov.courtservice.xhibit.business.services.monetaryordertracking;

import uk.gov.courtservice.framework.exception.CSBusinessException;

public class MonetaryOrderTrackingControllerException extends CSBusinessException {
    public MonetaryOrderTrackingControllerException(String errorKey, String logMessage, Throwable e) {
        super(errorKey, logMessage, e);
    }
}