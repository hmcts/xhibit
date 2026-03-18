package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDayValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionValue;
import uk.gov.courtservice.xhibit.business.services.witness.SkeletonControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.interfaces.AbstractStateManagedObject;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;

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
 * 
 * @version $Revision: 1.9 $
 * 
 */
public class TrialSessionImpl extends AbstractStateManagedObject implements TrialSession {
    private static final Logger log = CSServices.getLogger(TrialSessionImpl.class);
    
    private static final long serialVersionUID = 570317258811442242L;

    private XhbSkeletonSessionValue session;

    private static String[] sessionTypes = { AFTERNOON, MORNING };

    private final String STR_AM_TYPE = "M";

    private final float FL_AM_ADJUSTMENT = 0.5f;

    private final float FL_PM_ADJUSTMENT = 0.0f;

    public TrialSessionImpl(final XhbSkeletonSessionValue session) {
        this.session = session;
        setMetaState(UPDATED);
        log.debug("set meta state to :" + getMetaState());
    }

    public TrialSessionImpl() {
        this.session = new XhbSkeletonSessionValue();
        this.session.setXhbSkeletonDay(new XhbSkeletonDayValue());
        log.debug("set meta state to :" + getMetaState());
        setMetaState(UNSAVEABLE);
    }

    public XhbSkeletonDayValue getDay() {
        return session.getXhbSkeletonDay();
    }

    public Integer getId() {
        return session.getPrimaryKey();
    }

    public short getDayNumber() {
        return session.getXhbSkeletonDay().getDayNumber();
    }

    public void setDayNumber(final short dayNumber) {
        session.getXhbSkeletonDay().setDayNumber(dayNumber);
        markAsModified();

    }

    public short getWeek() {
        return (short) ((getDayNumber() - 1) / 5 + 1);
    }

    public Date getAppearanceDate() {
        return session.getXhbSkeletonDay().getSkeletonDate();
    }

    public void setAppearanceDate(final Date date) {
        session.getXhbSkeletonDay().setSkeletonDate(date == null ? null : new Timestamp(date.getTime()));
        markAsModified();
    }

    public String getSessionType() {
        // if(!session.getMorningOrAfternoon().equals(MORNING) &&
        // !session.getMorningOrAfternoon().equals(AFTERNOON) ) {
        // log.debug("INVALID SESSION TYPE");
        // } else {
        // log.debug("Array search:
        // "+Arrays.binarySearch(this.getSessionTypes(),session.getMorningOrAfternoon()));
        // }
        return session.getMorningOrAfternoon();
    }

    public void setSessionType(final String sessionType) {
        session.setMorningOrAfternoon(sessionType);
        markAsModified();
    }

    public String getNotes() {
        return session.getNotes();
    }

    public void setNotes(final String notes) {
        session.setNotes(notes);
        markAsModified();
    }

    public void update() throws TrialSessionModificationException {
        checkForUpdate();
        if (isModified()) {
            final TrialSessionImpl trialSession = (TrialSessionImpl) SkeletonControllerBeanBusinessDelegate.DelegateFactory
                    .getInstance().updateTrialSession(this);
            this.setSession(trialSession.getSession());
            log.debug("Updated TrialSession okay.");
        }
        setMetaState(UPDATED);
    }

    public void remove() throws TrialSessionModificationException {
        remove(true);
    }

    public void remove(final boolean force) throws TrialSessionModificationException {
        if (getMetaState() == REMOVED) {
            throw new TrialSessionModificationException("WITNESS_XXX", "This Trial Session has already been deleted.");
        }
        SkeletonControllerBeanBusinessDelegate.DelegateFactory.getInstance().removeTrialSession(this, force);
        setMetaState(REMOVED);
    }

    public static String[] getSessionTypes() {
        return sessionTypes;
    }

    public boolean hasWitnesses() {
        boolean result = SkeletonControllerBeanBusinessDelegate.DelegateFactory.getInstance().hasSessionWitnesses(
                session.getPrimaryKey());
        log.debug("HAS WITNESSES " + session.getPrimaryKey() + " - " + result);
        return result;
    }

    /**
     * Calculate the duration of the trial based on the last session
     * 
     * @return the trial duration
     */
    public float getDayAndSessionAsDurationInDays() {
        // If the session is the morning, subtract 0.5 (i.e. if the trial is
        // 10 days and the last session is the morning of day 10, the duration
        // is 9.5 days)
        return getDayNumber() - (getSessionType().equals(STR_AM_TYPE) ? FL_AM_ADJUSTMENT : FL_PM_ADJUSTMENT);
    }

    public XhbSkeletonSessionValue getSkeletonSession() {
        return session;
    }

    public int compareTo(final Object o) {
        return compareTo((TrialSession) o);
    }

    // Compare days, then put morning sessions before afternoon
    // M = Morning Session
    // A = Afternoon Session
    public int compareTo(final TrialSession o) {
        int result = getDayNumber() - o.getDayNumber();
        if (result != 0) {
            return result;
        } else {
            String st1 = getSessionType();
            String st2 = o.getSessionType();
            if (st1.equals(STR_AM_TYPE)) {
                if (st2.equals(STR_AM_TYPE)) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (st2.equals(STR_AM_TYPE)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    public int hashCode() {
        final String[] types = getSessionTypes();
        final int lhsValue = Arrays.binarySearch(types, getSessionType());
        return ((getDayNumber() * types.length) + lhsValue);
    }

    public boolean equals(final Object obj) {
        return hashCode() == obj.hashCode();
    }

    public void setSession(final XhbSkeletonSessionValue session) {
        this.session = session;
        markAsModified();
    }

    public XhbSkeletonSessionValue getSession() {
        return session;
    }

    public String toString() {
        return "uk.gov.courtservice.xhibit.business.services.witness.schedule.TrialSessionImpl{" + "session=" + session
                + ", sessionTypes="
                + (sessionTypes == null ? null : "length:" + sessionTypes.length + Arrays.asList(sessionTypes)) + "}";
    }

    static {
        // kept in this order for pre- and post- condition checks.
        Arrays.sort(sessionTypes);
    }

}
