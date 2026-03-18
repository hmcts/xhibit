package uk.gov.courtservice.xhibit.business.services.witness.exceptions;

import uk.gov.courtservice.framework.exception.CSBusinessException;

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
public class InvalidContactNumberException extends CSBusinessException {
    public InvalidContactNumberException() {
    }

    public InvalidContactNumberException(final String s, final String s1, final Throwable throwable) {
        super(s, s1, throwable);
    }

    public InvalidContactNumberException(final String s, final String s1) {
        super(s, s1);
    }

    public InvalidContactNumberException(final String s, final Object[] objects, final String s1) {
        super(s, objects, s1);
    }

    public InvalidContactNumberException(final String s, final Object[] objects, final String s1,
            final Throwable throwable) {
        super(s, objects, s1, throwable);
    }
}
