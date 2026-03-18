package uk.gov.courtservice.xhibit.courtlog.probation.exceptions;

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
public class ProbationCourtLogProcessingException extends CSBusinessException {
    public ProbationCourtLogProcessingException() {
    }

    public ProbationCourtLogProcessingException(String s, String s1, Throwable throwable) {
        super(s, s1, throwable);
    }

    public ProbationCourtLogProcessingException(String s, String s1) {
        super(s, s1);
    }

    public ProbationCourtLogProcessingException(String s, Object[] objects, String s1, Throwable throwable) {
        super(s, objects, s1, throwable);
    }

    public ProbationCourtLogProcessingException(String s, Object[] objects, String s1) {
        super(s, objects, s1);
    }

}
