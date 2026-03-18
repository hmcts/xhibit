package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import uk.gov.courtservice.xhibit.business.services.witness.SkeletonControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;

/**
 * <p>
 * Title: Skeleton Schedule Factory
 * </p>
 * <p>
 * Description: This class provides instances of SkeletonSchedule through either
 * create or get methods..
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * <b>This is not thread safe, under the covers it uses unsynchronized lazy
 * instantiation. You have been warned :-) </b>
 * 
 * @author Neil Ellis
 * 
 */
public class SkeletonScheduleFactory {
    private static final SkeletonScheduleFactory ourInstance = new SkeletonScheduleFactory();

    private transient SkeletonControllerBeanBusinessDelegate delegate;

    private transient WitnessControllerBeanBusinessDelegate witnessDelegate;

    private SkeletonScheduleFactory() {
    }

    public static SkeletonScheduleFactory getInstance() {
        return ourInstance;
    }

    public SkeletonControllerBeanBusinessDelegate getDelegate() {
        if (delegate == null) {
            delegate = SkeletonControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        }
        return delegate;
    }

    public WitnessControllerBeanBusinessDelegate getWitnessDelegate() {
        if (witnessDelegate == null) {
            witnessDelegate = WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        }
        return witnessDelegate;
    }

    /**
     * @pre caseId != null
     * @post return != null
     * @param caseId
     * @return
     * @throws
     *            uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException
     */
    public SkeletonSchedule createSkeletonSchedule(final Integer caseId)
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException {
        return createSkeletonSchedule(caseId, false);
    }

    /**
     * @pre caseId != null
     * @post return != null
     * @param caseId
     * @param deliverableFlag
     * @return
     * @throws
     *            uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException
     */
    public SkeletonSchedule createSkeletonSchedule(final Integer caseId, final boolean deliverableFlag)
            throws ScheduleModificationException {
        return getDelegate().createSkeletonSchedule(caseId, deliverableFlag);
    }

    public SkeletonSchedule getSkeletonSchedule(final Integer caseId) throws ScheduleNotFoundException {
        return getDelegate().getSkeletonSchedule(caseId);
    }

    // Notes: return a list of skeletons where
    // xhb_skeleton_schedule.deliverable = ‘Y’ and
    // xhb_skeleton_schedule.skeleton_delivery_status in (2, 3,4) and no
    // dates "realized". Order by created date (latest first). Criteria
    // specified by Doug.
    public SkeletonSchedule[] getIssuedSkeletonSchedules(Integer courtId) {
        return getDelegate().getIssuedSkeletonSchedules(courtId);

    }
}
