package uk.gov.courtservice.framework.exception;

/**
 * <p>
 * Title: Exception to use for optimistic locking problems.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @version 1.0
 */

public class OptimisticLockException extends CSUnrecoverableException {

    private static Message msg = new Message(Message.OPTIMISTIC_LOCK_VIOLATED);

    public OptimisticLockException() {
        super(msg);
    }

    /**
     * 
     * @param logMessage
     */
    public OptimisticLockException(String logMessage) {
        super(msg, logMessage);
    }

    /**
     * 
     * @param cause
     *            original exception caught
     */
    public OptimisticLockException(Throwable cause) {
        super(msg, cause);
    }

    /**
     * 
     * @param logMessage
     * @param cause
     *            original exception caught
     */
    public OptimisticLockException(String logMessage, Throwable cause) {
        super(msg, cause, logMessage);
    }
}