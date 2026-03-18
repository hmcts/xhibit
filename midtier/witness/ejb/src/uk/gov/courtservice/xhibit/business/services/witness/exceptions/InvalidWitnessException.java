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
 * @author Neil Ellis
 * @version $Id: InvalidWitnessException.java,v 1.2 2004/11/01 15:59:49 tz0d5m
 *          Exp $
 */
public class InvalidWitnessException extends CSBusinessException {
    public InvalidWitnessException() {
    }

    public InvalidWitnessException(final String s, final String s1, final Throwable throwable) {
        super(s, s1, throwable);
    }

    public InvalidWitnessException(final String s, final String s1) {
        super(s, s1);
    }

    public InvalidWitnessException(final String s, final Object[] objects, final String s1, final Throwable throwable) {
        super(s, objects, s1, throwable);
    }

    public InvalidWitnessException(final String s, final Object[] objects, final String s1) {
        super(s, objects, s1);
    }
}
