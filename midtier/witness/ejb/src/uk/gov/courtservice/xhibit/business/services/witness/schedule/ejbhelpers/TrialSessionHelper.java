package uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSession;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionHome;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionValue;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.TrialSessionImpl;
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
 * @author qzd3k3
 * 
 */
public class TrialSessionHelper {
    private static Logger log = CSServices.getLogger(TrialSessionHelper.class);

    private static TrialSession lastWitnessSession;

    private static final String STR_YES_OBS_IND = "Y";

    private static XhbSkeletonSessionHome sessionHome;

    static {
        sessionHome = XhbSkeletonSessionHelper.getLocalHome();
    }

    /**
     * Create sessions from the EJBs
     * 
     * @param ejbs
     *            a collection of Skeleton Session EJBs
     * @return an array of TrialSession
     */
    public static TrialSession[] getSessionsFromEJBs(final Collection ejbs) {
        final TrialSession[] sessions = new TrialSession[ejbs.size()];
        int count = 0;
        for (Iterator iterator = ejbs.iterator(); iterator.hasNext();) {
            final XhbSkeletonSession skeletonSession = (XhbSkeletonSession) iterator.next();
            sessions[count++] = new TrialSessionImpl(skeletonSession.getData());
            if (log.isDebugEnabled()) {
                log.debug("Added trial session:" + sessions[count - 1]);
            }
        }
        // Put trial session into day order and session type order
        // Morning Session first following by Afternoon Session
        Arrays.sort(sessions, TrialSessionComparator.getInstance());

        return sessions;
    }

    /**
     * Return all the trial sessions for the case
     * 
     * @param caseId
     *            the case id
     * @return an array of TrialSession
     */
    public static TrialSession[] getTrialSessions(final Integer caseId) {
        final Vector sessions = new Vector();
        try {
            Iterator iter = sessionHome.findByCaseId(caseId).iterator();
            while (iter.hasNext()) {
                XhbSkeletonSession tSession = ((XhbSkeletonSession) iter.next());
                log.debug("Adding sessions from day:" + tSession.getXhbSkeletonDay().getDayNumber());
                sessions.add(tSession);
            }

            return getSessionsFromEJBs(sessions);

        } catch (FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * Get the last trial session that has a witness assigned. This allows us to
     * check the minimum we can adjust the trial estimate to. See PR 57105
     * 
     * @param caseId
     *            the case id
     * @return the latest TrialSession with a witness or null (if no trial
     *         sessions)
     */
    public static TrialSession getLastTrialSessionWithWitnesses(final Integer caseId) {
        // Get all the trial sessions for the case
        TrialSession[] sessions = getTrialSessions(caseId);

        // Vector to hold all the sessions with a witness
        Vector sessionVector = new Vector(0);

        for (int x = 0; x < sessions.length; x++) {
            if (sessions[x].hasWitnesses()) {
                sessionVector.add(sessionVector.size(), sessions[x]);
            }
        }
        if (sessionVector.size() > 0) {
            // return the last session with a witness
            return (TrialSession) sessionVector.lastElement();
        } else {
            // no trial sessions
            return null;
        }
    }

    /**
     * Mark any sessions that are after the trial estimate as obsolete (can
     * happen if a session was created, the witness moved to an earlier session,
     * and the trial time estimate reduced). These sessions are then ignored in
     * any subsequent searches.
     * 
     * @param caseId
     *            the case id
     * @param duration
     *            the duration (trial time estimate) of the case
     */
    public static void markSessionsObsolete(final Integer caseId, final Integer duration) {
        log.debug("start - markSessionsObsolete");
        try {
            Iterator iter = sessionHome.findByCaseId(caseId).iterator();
            while (iter.hasNext()) {
                XhbSkeletonSessionValue value = ((XhbSkeletonSession) iter.next()).getData();
                // If the session day number is after the duration (trial time
                // estimate)
                // set the OBS_IND to "Y"
                if (value.getXhbSkeletonDay().getDayNumber() > duration.shortValue()) {
                    value.setObsInd(STR_YES_OBS_IND);
                    XhbSkeletonSessionHelper.update(value);
                }
            }
        } catch (FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
    }
}
