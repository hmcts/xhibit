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
public class ScheduleModificationException extends CSBusinessException {
    protected static final String EXCEPTION_KEY = "skeletonschedule.midtier.schedulemodification";

    public ScheduleModificationException(final String s1, final Throwable throwable) {
        super(EXCEPTION_KEY, s1, throwable);
    }

    public ScheduleModificationException(final String s1) {
        super(EXCEPTION_KEY, s1);
    }

}
