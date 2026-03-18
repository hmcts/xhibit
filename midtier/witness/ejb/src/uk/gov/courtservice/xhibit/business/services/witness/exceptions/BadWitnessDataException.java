package uk.gov.courtservice.xhibit.business.services.witness.exceptions;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

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
public class BadWitnessDataException extends CSUnrecoverableException {
    public BadWitnessDataException(final String s) {
        super(s);
    }

}
