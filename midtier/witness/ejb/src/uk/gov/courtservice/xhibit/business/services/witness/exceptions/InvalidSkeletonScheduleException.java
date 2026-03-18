package uk.gov.courtservice.xhibit.business.services.witness.exceptions;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author qzd3k3
 * 
 */
public class InvalidSkeletonScheduleException extends CSUnrecoverableException {

    public InvalidSkeletonScheduleException() {
    }

    public InvalidSkeletonScheduleException(final Message message) {
        super(message);
    }

    public InvalidSkeletonScheduleException(final Message message, final String s) {
        super(message, s);
    }

    public InvalidSkeletonScheduleException(final Message message, final Throwable throwable) {
        super(message, throwable);
    }

    public InvalidSkeletonScheduleException(final Message message, final Throwable throwable, final String s) {
        super(message, throwable, s);
    }

    public InvalidSkeletonScheduleException(final String s) {
        super(s);
    }

    public InvalidSkeletonScheduleException(final Throwable throwable) {
        super(throwable);
    }

    public InvalidSkeletonScheduleException(final String s, final Throwable throwable) {
        super(s, throwable);
    }
}
