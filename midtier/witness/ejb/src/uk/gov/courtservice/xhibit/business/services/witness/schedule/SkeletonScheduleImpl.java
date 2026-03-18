package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDeliveryStatusValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonScheduleValue;
import uk.gov.courtservice.xhibit.business.services.witness.SkeletonControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CouldNotCreateSessionException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleAlreadyIssued;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonDayHasDateException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.interfaces.AbstractStateManagedObject;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;

/**
 * @author Neil Ellis
 * 
 * @version $Revision: 1.9 $
 * 
 * <H1>This is not thread safe, under the covers it uses unsynchronized lazy
 * instantiation. You have been warned :-) </H1>
 * 
 */
public class SkeletonScheduleImpl extends AbstractStateManagedObject implements SkeletonSchedule {
    private static final Logger log = CSServices.getLogger(SkeletonScheduleImpl.class);
    
    private static final long serialVersionUID = -4147332768368073051L;

    private XhbSkeletonScheduleValue xhbSkeletonScheduleValue;

    private transient SkeletonControllerBeanBusinessDelegate skeletonControllerBeanBusinessDelegate;

    private transient WitnessControllerBeanBusinessDelegate witnessControllerBeanBusinessDelegate;

    private static final String DELIVERABLE_FLAG_YES_VALUE = "Y";

    private static final String DELIVERABLE_FLAG_NO_VALUE = "N";

    private static HashMap deliveryStatuses = null;

    private static synchronized HashMap getDeliveryStatuses() {
        if (deliveryStatuses == null) {
            deliveryStatuses = new HashMap();
            final XhbSkeletonDeliveryStatusValue[] deliveryStatusValues = SkeletonControllerBeanBusinessDelegate.DelegateFactory
                    .getInstance().getDeliveryStatusValues();
            for (int i = 0; i < deliveryStatusValues.length; i++) {
                final XhbSkeletonDeliveryStatusValue value = deliveryStatusValues[i];
                deliveryStatuses.put(value.getCode(), value);
                log.debug(value);

            }
        }
        return deliveryStatuses;

    }

    public SkeletonScheduleImpl(final XhbSkeletonScheduleValue xhbSkeletonScheduleValue) {
        this.xhbSkeletonScheduleValue = xhbSkeletonScheduleValue;
    }

    public void setXhbSkeletonScheduleValue(final XhbSkeletonScheduleValue xhbSkeletonScheduleValue) {
        this.xhbSkeletonScheduleValue = xhbSkeletonScheduleValue;
    }

    /**
     * Note the lazy instantiation makes sure we don't get a copy on the
     * serverside.
     * 
     * @return
     */
    public SkeletonControllerBeanBusinessDelegate getSkeletonControllerBeanBusinessDelegate() {
        if (skeletonControllerBeanBusinessDelegate == null) {
            skeletonControllerBeanBusinessDelegate = SkeletonControllerBeanBusinessDelegate.DelegateFactory
                    .getInstance();
        }
        return skeletonControllerBeanBusinessDelegate;
    }

    /**
     * Note the lazy instantiation makes sure we don't get a copy on the
     * serverside.
     * 
     * @return
     */
    public WitnessControllerBeanBusinessDelegate getWitnessControllerBeanBusinessDelegate() {
        if (witnessControllerBeanBusinessDelegate == null) {
            witnessControllerBeanBusinessDelegate = WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        }
        return witnessControllerBeanBusinessDelegate;
    }

    public XhbSkeletonScheduleValue getXhbSkeletonScheduleValue() {
        return xhbSkeletonScheduleValue;
    }

    public Integer getId() {
        checkForRemoval();
        return xhbSkeletonScheduleValue.getSkeletonId();
    }

    public Integer getCaseId() {
        return xhbSkeletonScheduleValue.getCaseId();
    }

    public boolean isDeliverable() {
        return xhbSkeletonScheduleValue.getDeliverable().equals(DELIVERABLE_FLAG_YES_VALUE);
    }

    public void setDeliverable(final boolean deliverable) {
        xhbSkeletonScheduleValue.setDeliverable(deliverable ? DELIVERABLE_FLAG_YES_VALUE : DELIVERABLE_FLAG_NO_VALUE);
        markAsModified();

    }

    public String getDeliveryStatus() {
        final XhbSkeletonDeliveryStatusValue deliveryStatus = xhbSkeletonScheduleValue.getXhbSkeletonDeliveryStatus();
        if (deliveryStatus == null) {
            return null;
        }
        return deliveryStatus.getCode();
    }

    /**
     * 
     * @param status
     */
    public void setDeliveryStatus(final String status) {
        xhbSkeletonScheduleValue.setXhbSkeletonDeliveryStatus((XhbSkeletonDeliveryStatusValue) getDeliveryStatuses()
                .get(status));
        markAsModified();
    }

    public void issue() throws ScheduleAlreadyIssued, ModificationException {
        setDeliveryStatus(READY);
        setDeliverable(true);
        markAsModified();
        update();
    }

    public boolean isIssued() {

        return (isDeliverable() && (getDeliveryStatus() == null || getDeliveryStatus().equals(READY)
                || getDeliveryStatus().equals(DELIVERED) || getDeliveryStatus().equals(FAILED)));
    }

    public CaseDetail getCaseDetail()
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException {
        checkForRemoval();
        return getWitnessControllerBeanBusinessDelegate().getCaseDetails(xhbSkeletonScheduleValue.getCaseId());

    }

    public TrialSession[] getTrialSessions() {
        return getSkeletonControllerBeanBusinessDelegate().getTrialSessions(getCaseId());
    }

    /**
     * 
     * 
     * @param estimatedDuration
     * @return all the sessions that are less than or equal to estimatedDuration
     *         rounded up to the nearest half day.
     */
    public TrialSession[] getTrialSessionsIncludingDummyValuesForDuration(final float estimatedDuration) {
        checkForRemoval();
        return getSkeletonControllerBeanBusinessDelegate().getTrialSessions(getCaseId(), estimatedDuration);
    }

    public TrialSession getTrialSession(final Integer day, final String session) throws TrialSessionNotFoundException {
        checkForRemoval();
        final TrialSession trialSession = getSkeletonControllerBeanBusinessDelegate().getTrialSession(getCaseId(), day,
                session);
        return trialSession;
    }

    public TrialSession getTrialSession(final Integer sessionId) throws TrialSessionNotFoundException {
        checkForRemoval();
        return getSkeletonControllerBeanBusinessDelegate().getTrialSession(sessionId);
    }

    public TrialSession createTrialSession(final Integer day, final String session)
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException,
            SkeletonDayHasDateException, CouldNotCreateSessionException {
        checkForRemoval();
        final TrialSession trialSession = getSkeletonControllerBeanBusinessDelegate().createTrialSession(
                xhbSkeletonScheduleValue.getPrimaryKey(), day, session, null);
        log.debug("Created:" + trialSession + " with metaState:" + trialSession.getMetaState());
        return trialSession;
    }

    public TrialSession createTrialSession(final Integer day, final String session, final Date trialDate)
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException,
            SkeletonDayHasDateException, CouldNotCreateSessionException {
        checkForRemoval();
        final TrialSession trialSession = getSkeletonControllerBeanBusinessDelegate().createTrialSession(
                xhbSkeletonScheduleValue.getPrimaryKey(), day, session, trialDate);
        log.debug("Created:" + trialSession);
        return trialSession;

    }

    public int compareTo(final Object o) {
        if (o instanceof SkeletonScheduleImpl) {
            return (int) (((SkeletonScheduleImpl) o).getXhbSkeletonScheduleValue().getCreationDate().getTime() - this
                    .getXhbSkeletonScheduleValue().getCreationDate().getTime());
        }
        return 0;
    }

    /**
     * @pre getMetaState() != REMOVED
     * @post getMetaState() == UPDATED
     * @throws ModificationException
     */
    public void update() throws ModificationException {
        checkForRemoval();
        final SkeletonScheduleImpl skeletonSchedule = (SkeletonScheduleImpl) getSkeletonControllerBeanBusinessDelegate()
                .updateSkeletonSchedule(this);
        setXhbSkeletonScheduleValue(skeletonSchedule.getXhbSkeletonScheduleValue());
        setMetaState(UPDATED);
    }

    /**
     * @pre getMetaState() != REMOVED
     * @post getMetaState() == REMOVED
     * @throws ModificationException
     */
    public void remove() throws ModificationException {
        checkForRemoval();
        getSkeletonControllerBeanBusinessDelegate().removeSkeletonSchedule(xhbSkeletonScheduleValue.getPrimaryKey());
        setMetaState(REMOVED);
    }
}
