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
public class WitnessCourtLogProcessingException extends CSBusinessException {
    public WitnessCourtLogProcessingException() {
    }

    public WitnessCourtLogProcessingException(String s, String s1, Throwable throwable) {
        super(s, s1, throwable);
    }

    public WitnessCourtLogProcessingException(String s, String s1) {
        super(s, s1);
    }

    public WitnessCourtLogProcessingException(String s, Object[] objects, String s1, Throwable throwable) {
        super(s, objects, s1, throwable);
    }

    public WitnessCourtLogProcessingException(String s, Object[] objects, String s1) {
        super(s, objects, s1);
    }
}
