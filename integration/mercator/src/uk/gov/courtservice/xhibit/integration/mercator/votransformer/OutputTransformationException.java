package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public class OutputTransformationException extends CSBusinessException {

    public OutputTransformationException() {
    }

    public OutputTransformationException(final String s, final String s1, final Throwable throwable) {
        super(s, s1, throwable);
    }

    public OutputTransformationException(final String s, final String s1) {
        super(s, s1);
    }

    public OutputTransformationException(final String s, final Object[] objects, final String s1) {
        super(s, objects, s1);
    }

    public OutputTransformationException(final String s, final Object[] objects, final String s1,
            final Throwable throwable) {
        super(s, objects, s1, throwable);
    }
}