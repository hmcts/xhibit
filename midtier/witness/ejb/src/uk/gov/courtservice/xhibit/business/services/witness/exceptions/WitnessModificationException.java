package uk.gov.courtservice.xhibit.business.services.witness.exceptions;

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
 * @version $Revision: 1.5 $
 */
public class WitnessModificationException extends ModificationException {
    public WitnessModificationException() {
    }

    public WitnessModificationException(final String s, final String s1, final Throwable throwable) {
        super(s, s1, throwable);
    }

    public WitnessModificationException(final String s, final String s1) {
        super(s, s1);
    }

    public WitnessModificationException(final String s, final Object[] objects, final String s1) {
        super(s, objects, s1);
    }

    public WitnessModificationException(final String s, final Object[] objects, final String s1,
            final Throwable throwable) {
        super(s, objects, s1, throwable);
    }
}