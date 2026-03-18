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
 * @version $Revision: 1.6 $
 * 
 */
public class ScheduleNotFoundException extends CSBusinessException {
    protected static final String EXCPETION_KEY = "skeletonschedule.midtier.schedulenotfound";

    public ScheduleNotFoundException(String s1, Throwable throwable) {
        super(EXCPETION_KEY, s1, throwable);
    }

    public ScheduleNotFoundException(String s1) {
        super(EXCPETION_KEY, s1);
    }

}
