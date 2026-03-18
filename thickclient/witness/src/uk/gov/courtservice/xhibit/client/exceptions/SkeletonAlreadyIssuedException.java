package uk.gov.courtservice.xhibit.client.exceptions;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;

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
public class SkeletonAlreadyIssuedException extends CSRecoverableException {
    SkeletonSchedule schedule;

    public SkeletonAlreadyIssuedException(SkeletonSchedule schedule) {
        super("skeletonschedule.skeletonpanel.issuedalready", "Skeleton schedule has already been issued.");
        this.schedule = schedule;
    }

    public SkeletonSchedule getSchedule() {
        return schedule;
    }

}
