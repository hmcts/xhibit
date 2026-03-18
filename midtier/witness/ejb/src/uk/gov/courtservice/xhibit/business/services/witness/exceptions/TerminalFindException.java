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
public class TerminalFindException extends CSUnrecoverableException {

    public TerminalFindException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public TerminalFindException() {
    }

    public TerminalFindException(final Message message) {
        super(message);
    }

    public TerminalFindException(final Message message, final String s) {
        super(message, s);
    }

    public TerminalFindException(final Message message, final Throwable throwable) {
        super(message, throwable);
    }

    public TerminalFindException(final Message message, final Throwable throwable, final String s) {
        super(message, throwable, s);
    }

    public TerminalFindException(final Throwable throwable) {
        super(throwable);
    }

    public TerminalFindException(final String s) {
        super(s);
    }

}
