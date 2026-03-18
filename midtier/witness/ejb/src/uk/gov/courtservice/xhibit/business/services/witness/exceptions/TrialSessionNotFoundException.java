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
 * @version $Id: TrialSessionNotFoundException.java,v 1.3 2004/11/01 15:59:49
 *          tz0d5m Exp $
 */
public class TrialSessionNotFoundException extends CSBusinessException {
    public TrialSessionNotFoundException() {
    }

    public TrialSessionNotFoundException(final String s, final String s1, final Throwable throwable) {
        super(s, s1, throwable);
    }

    public TrialSessionNotFoundException(final String s, final String s1) {
        super(s, s1);
    }

    public TrialSessionNotFoundException(final String s, final Object[] objects, final String s1) {
        super(s, objects, s1);
    }

    public TrialSessionNotFoundException(final String s, final Object[] objects, final String s1,
            final Throwable throwable) {
        super(s, objects, s1, throwable);
    }
}