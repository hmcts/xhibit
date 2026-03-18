package uk.gov.courtservice.xhibit.business.services.witness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbCaseHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDay;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDayHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDayValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDeliveryStatus;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDeliveryStatusHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDeliveryStatusValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSchedule;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonScheduleHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonScheduleValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSession;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitness;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CouldNotCreateSessionException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonDayHasDateException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonScheduleModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.reference.WitnessReferenceDataFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.TrialSessionImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers.TrialSessionHelper;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;

/**
 * <p>
 * Title: The Skeleton Stateless Session EJB.
 * </p>
 * <p>
 * Description: This is the Skeleton Session Bean that provides core business
 * services required for the Witness Facilities part of XHIBIT .
 * </p>
 * 
 * @author Neil Ellis
 * @ejb.bean name="SkeletonController" description="Skeleton Controller Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="SkeletonControllerHome" <p/>
 *           <p>
 *           Copyright: Copyright (c) 2003
 *           </p>
 *           <p>
 *           Company: EDS
 *           </p>
 * @ejb.transaction type="Required"
 */
public class SkeletonControllerBean extends CSSessionBean implements SessionBean {
    private static final String DELIVERABLE_DEFAULT_VALUE = "N";

    private final HashMap deliveryStatuses = new HashMap();

    private static final String STR_DEF_OBS_IND = "N";

    /**
     * Instantiates all relevant home interfaces for use during active life of
     * Stateless Session Bean.
     * 
     * @throws CreateException
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        log.debug("Entering ejbCreate().");

        try {
            Collection all = XhbSkeletonDeliveryStatusHelper.getLocalHome().findAll();
            for (Iterator iterator = all.iterator(); iterator.hasNext();) {
                final XhbSkeletonDeliveryStatus xhbSkeletonDeliveryStatus = (XhbSkeletonDeliveryStatus) iterator.next();
                deliveryStatuses.put(xhbSkeletonDeliveryStatus.getCode(), xhbSkeletonDeliveryStatus);
            }
        } catch (FinderException e) {
            log.error(e);
            throw new CreateException(e.getMessage());
        }

        log.debug("Exiting ejbCreate");
    }

    /**
     * @param caseId
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public TrialSession[] getTrialSessions(final Integer caseId) {
        return TrialSessionHelper.getTrialSessions(caseId);
    }

    /**
     * @param durationInDays
     * @param caseId
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public TrialSession[] getTrialSessions(final Integer caseId, final float durationInDays) {
        log.debug("*************** getTrialSessions using caseid " + caseId + " for duration of " + durationInDays);
        // round down to the nearest session.
        final String[] sessionNames = WitnessReferenceDataFactory.getWitnessReferenceData().getTrialSessionTypes();
        final int numberOfSessionTypes = sessionNames.length;
        final int numberOfSessions = (int) (durationInDays * numberOfSessionTypes);
        log.debug("No of Sessions :: " + numberOfSessions);

        // build up default trial session list i.e. Day 1 Morning, Day 1
        // Afternoon
        TrialSession[] returnValues = new TrialSession[numberOfSessions];
        for (int i = 0; i < returnValues.length; i++) {
            TrialSessionImpl session = new TrialSessionImpl();
            // As sessions are persisted once created, need to check that we
            // only retrieve session sup to the trial estimate
            log.debug("Adding Session :: " + session.getDayNumber());
            session.setSessionType(sessionNames[i % numberOfSessionTypes]);
            session.setDayNumber((short) (i / numberOfSessionTypes + 1));
            returnValues[i] = session;
        }

        // used to hold any additional trial sessions that may have been added
        ArrayList newSessions = new ArrayList();

        // identify existing trial sessions and match then overwrite default
        // value in returnValue array
        final TrialSession[] trialSessions = TrialSessionHelper.getTrialSessions(caseId);
        log.debug("No of Trial Sessions from DB :: " + trialSessions.length);
        for (int i = 0; i < trialSessions.length; i++) {
            boolean duplicateSession = false;
            final TrialSession trialSession = trialSessions[i];
            for (int t = 0; t < returnValues.length; t++) {
                final TrialSession returnValue = returnValues[t];
                if (returnValue.getSessionType().equals(trialSession.getSessionType())
                        && (returnValue.getDayNumber() == trialSession.getDayNumber())) {
                    duplicateSession = true;
                    returnValues[t] = trialSession;
                }
            }
            // Check that we don't exceed the trial estimate
            if (!duplicateSession) // && trialSession.getDayNumber() <=
            // durationInDays)
            {
                // this session needs to be added to return list
                newSessions.add(trialSession);
            }
        }

        // construct a new trial session array which contain all the trial
        // sessions
        TrialSession[] returnAllSessions = new TrialSession[returnValues.length + newSessions.size()];
        log.debug("Return Array List set up with length :: " + returnAllSessions.length);
        int returnValueLength = returnValues.length;
        for (int i = 0; i < returnValueLength; i++) {
            returnAllSessions[i] = returnValues[i];
        }

        // add in the new sessions
        for (int j = 0; j < newSessions.size(); j++) {
            // starting point is length of all the possible trial sessions +
            // 1
            returnAllSessions[returnValueLength + j] = (TrialSession) newSessions.get(j);
        }
        return returnAllSessions;
    }

    /**
     * @param caseId
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public TrialSession getTrialSession(final Integer caseId, final Integer day, final String session)
            throws TrialSessionNotFoundException {
        try {
            return new TrialSessionImpl(XhbSkeletonSessionHelper.findByCaseDayAndSession(caseId, day, session));
        } catch (FinderException e) {
            log.error(e);
            throw new TrialSessionNotFoundException("WITNESS_XXX", "Could not find trial session for case: " + caseId
                    + ", day: " + day + ", session: " + session, e);
        }
    }

    /**
     * @param sessionId
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public TrialSession getTrialSession(final Integer sessionId) throws TrialSessionNotFoundException {
        try {
            return new TrialSessionImpl(XhbSkeletonSessionHelper.findByPrimaryKey(sessionId));
        } catch (FinderException e) {
            log.error(e);
            throw new TrialSessionNotFoundException("WITNESS_XXX", "Could not retrieve session: " + sessionId, e);
        }
    }

    /**
     * @param
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void createDefaultSkeletonDays(Integer caseId, float duration, Integer skeletonId) {
        final String[] sessionNames = WitnessReferenceDataFactory.getWitnessReferenceData().getTrialSessionTypes();
        final int numberOfSessionTypes = sessionNames.length;
        final int numberOfSessions = (int) (duration * numberOfSessionTypes);
        log.debug("No of Sessions :: " + numberOfSessions);

        // build up default trial session list i.e. Day 1 Morning, Day 1
        // Afternoon
        TrialSession[] allTrialSessions = new TrialSession[numberOfSessions];
        for (int i = 0; i < allTrialSessions.length; i++) {
            TrialSessionImpl session = new TrialSessionImpl();
            session.setSessionType(sessionNames[i % numberOfSessionTypes]);
            session.setDayNumber((short) (i / numberOfSessionTypes + 1));
            allTrialSessions[i] = session;
        }

        // get all the trial session from the database
        final TrialSession[] trialSessions = TrialSessionHelper.getTrialSessions(caseId);
        log.debug("No of Trial Sessions from DB :: " + trialSessions.length);

        ArrayList createSessions = new ArrayList(); // used to hold all
        // those to be create in
        // the database
        for (int i = 0; i < allTrialSessions.length; i++) {
            boolean flag = false;
            TrialSession session = allTrialSessions[i];
            // loop all existing sessions from the database
            for (int j = 0; j < trialSessions.length; j++) {
                TrialSession dbTrialSession = trialSessions[j];
                // check if the same session
                if (session.getSessionType().equals(dbTrialSession.getSessionType())
                        && (session.getDayNumber() == dbTrialSession.getDayNumber())) {
                    flag = true;
                }
            }
            // only add if not already in database already
            if (!flag) {
                createSessions.add(allTrialSessions[i]);
            }
        }

        log.debug("Display all the Sessions to be added to database :: No to add :: " + createSessions.size());
        for (int i = 0; i < createSessions.size(); i++) {
            TrialSession session = (TrialSession) createSessions.get(i);
            log.debug("Session :: Day :: " + session.getDayNumber() + " Type :: " + session.getSessionType());
            Integer day = new Integer(Short.toString(session.getDayNumber()));
            try {
                this.createTrialSession(skeletonId, day, session.getSessionType(), null);
            } catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException e) {
                log.error(e);
            } catch (CouldNotCreateSessionException e) {
                log.error(e);
            } catch (SkeletonDayHasDateException e) {
                log.error(e);
            }
        }
    }

    /**
     * I just haven'tr got time to JavaDoc this :-)
     * 
     * @param session
     * @throws TrialSessionModificationException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void removeTrialSession(final TrialSession session, final boolean force)
            throws TrialSessionModificationException {
        try {
            final TrialSessionImpl trialSession = (TrialSessionImpl) session;
            final XhbSkeletonSessionValue xhbSkeletonSessionValue = trialSession.getSkeletonSession();
            final XhbSkeletonDayValue xhbSkeletonDayValue = xhbSkeletonSessionValue.getXhbSkeletonDay();
            final XhbSkeletonDay skeletonDayBean = XhbSkeletonDayHelper.getLocalHome().findByPrimaryKey(
                    xhbSkeletonDayValue.getPrimaryKey());
            final XhbSkeletonSession skeletonSessionBean = XhbSkeletonSessionHelper.getLocalHome().findByPrimaryKey(
                    xhbSkeletonSessionValue.getPrimaryKey());
            final Collection xhbWitnesss = skeletonSessionBean.getXhbWitnesss();
            if (xhbWitnesss.size() > 0) {
                if (force) {
                    for (Iterator iterator = xhbWitnesss.iterator(); iterator.hasNext();) {

                        final XhbWitness xhbWitness = (XhbWitness) iterator.next();
                        xhbWitness.remove();

                    }
                } else {
                    throw new TrialSessionModificationException("WITNESS_XXX",
                            "Trial session had witnesses but force flag was not set.");
                }
            }
            final Collection xhbSkeletonSessions = skeletonDayBean.getXhbSkeletonSessions();
            if (xhbSkeletonSessions.size() == 1) {
                final XhbSkeletonSession skeletonSessionBeanFromCollection = (XhbSkeletonSession) xhbSkeletonSessions
                        .iterator().next();
                // just being prudent
                if (skeletonSessionBeanFromCollection.getPrimaryKey().equals(skeletonSessionBean.getPrimaryKey())) {
                    skeletonSessionBean.remove();
                } else {
                    throw new TrialSessionModificationException("WITNESS_XXX", "Expected primary key of:"
                            + skeletonSessionBean.getPrimaryKey() + " got: "
                            + skeletonSessionBeanFromCollection.getPrimaryKey());
                }
                skeletonDayBean.remove();
            } else {
                skeletonSessionBean.remove();
            }
        } catch (javax.ejb.RemoveException e) {
            log.error(e);
            throw new TrialSessionModificationException("WITNESS_XXX", "Could not remove the TrialSession with ID:"
                    + session.getId(), e);
        } catch (FinderException e) {
            log.error(e);
            throw new TrialSessionModificationException("WITNESS_XXX", "Could not remove the TrialSession with ID:"
                    + session.getId(), e);
        }
    }

    /**
     * Updates the Trial Session on the database.
     * 
     * @param session
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public TrialSession updateTrialSession(final TrialSession session) throws TrialSessionModificationException {
        // Note we update any changes made to the session value
        // or the skeletonDay will both be updated.
        // Also please note we are updating the TrialSession itself as well as
        // the
        // database so they should be in sync.
        try {
            final TrialSessionImpl trialSession = (TrialSessionImpl) session;

            final XhbSkeletonSession skeletonSession = XhbSkeletonSessionHelper.getLocalHome().findByPrimaryKey(
                    trialSession.getId());
            skeletonSession.setData(trialSession.getSession());
            final XhbSkeletonDay xhbSkeletonDay = skeletonSession.getXhbSkeletonDay();
            if (xhbSkeletonDay != null) {
                xhbSkeletonDay.setData(trialSession.getDay());
            } else {
                log.debug("Skeleton day was null.");
            }
            return new TrialSessionImpl(skeletonSession.getData(true));
        } catch (FinderException e) {
            log.error(e);
            throw new TrialSessionModificationException("WITNESS_XXX", "Could not update the TrialSession with ID:"
                    + session.getId(), e);
        }
    }

    /**
     * @param scheduleId
     * @param dayNumber
     * @param session
     * @return
     * @throws
     *            uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public TrialSession createTrialSession(final Integer scheduleId, final Integer dayNumber, final String session,
            final Date trialDate)
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException,
            CouldNotCreateSessionException, SkeletonDayHasDateException {
        XhbSkeletonSchedule skeletonSchedule = null;
        try {
            skeletonSchedule = XhbSkeletonScheduleHelper.getLocalHome().findByPrimaryKey(scheduleId);
        } catch (FinderException e) {
            log.error(e);
            throw new uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException(
                    "Failed to obtain the schedule with id:" + scheduleId, e);
        }
        XhbSkeletonDay skeletonDay = findSkeletonDayWithEmptySession(skeletonSchedule, dayNumber, session);
        if (skeletonDay == null) {
            try {
                skeletonDay = XhbSkeletonDayHelper.getLocalHome().create(dayNumber.shortValue(), null, null, null,
                        null, null, null, (short) 0, skeletonSchedule);
            } catch (CreateException e) {
                log.error(e);
                throw new uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException(
                        "Failed to create a new day for schedule:" + scheduleId, e);
            }
        }

        // check to ensure that a session for the given session type does not
        // already exist
        if (trialDate != null && checkSessionExists(skeletonDay, session)) {
            throw new SkeletonDayHasDateException("WITNESS_XXX", "Skeleton day has date already.");
        }

        // null trialDate means don't change the date.
        if (trialDate != null) {
            skeletonDay.setSkeletonDate(new Timestamp(trialDate.getTime()));
        }

        XhbSkeletonSession newSkeletonSession = null;
        try {
            newSkeletonSession = XhbSkeletonSessionHelper.getLocalHome().create(session, null, null, null, null, null,
                    null, STR_DEF_OBS_IND, skeletonDay, skeletonSchedule);
        } catch (CreateException e) {
            log.error(e);
            throw new CouldNotCreateSessionException("WITNESS_XXX", "Failed to create a new session for day :"
                    + skeletonDay.getPrimaryKey(), e);
        }

        return new TrialSessionImpl(newSkeletonSession.getData());
    }

    /**
     * Check to see if a session already exist for the given session Type
     * 
     * @param skeletonDay
     *            skeletonday entity
     * @param sessionType
     *            session to be created
     * @return boolean to indicate whether the session type already exists.
     */
    private boolean checkSessionExists(XhbSkeletonDay skeletonDay, String sessionType) {
        Collection sessions = skeletonDay.getXhbSkeletonSessions();
        for (Iterator iterator = sessions.iterator(); iterator.hasNext();) {
            XhbSkeletonSession session = (XhbSkeletonSession) iterator.next();
            if (session.getMorningOrAfternoon().equals(sessionType)
                    && (session.getObsInd().equalsIgnoreCase("N") || null == session.getObsInd())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param sessionId
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public boolean hasSessionWitnesses(final Integer sessionId) {
        try {
            final Collection xhbWitnesses = XhbSkeletonSessionHelper.getLocalHome().findByPrimaryKey(sessionId)
                    .getXhbWitnesss();
            return xhbWitnesses.size() > 0;
        } catch (FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException("No such session:" + sessionId, e);
        }

    }

    /**
     * @param caseId
     * @return
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public SkeletonSchedule getSkeletonSchedule(final Integer caseId) throws ScheduleNotFoundException {
        XhbSkeletonSchedule newSchedule = null;
        try {
            Collection c = XhbSkeletonScheduleHelper.getLocalHome().findByCase(caseId);
            if (c.size() == 0) {
                // no schedule found.
                return null;
            }
            newSchedule = (XhbSkeletonSchedule) c.iterator().next();
        } catch (FinderException e) {
            log.error(e);
            throw new ScheduleNotFoundException("Could not find schedule identified by case:" + caseId, e);
        }
        return new SkeletonScheduleImpl(newSchedule.getData());
    }

    /**
     * @param schedule
     * @return
     * @throws SkeletonScheduleModificationException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public SkeletonSchedule updateSkeletonSchedule(final SkeletonSchedule schedule)
            throws SkeletonScheduleModificationException {
        try {
            XhbSkeletonScheduleValue value = ((SkeletonScheduleImpl) schedule).getXhbSkeletonScheduleValue();
            XhbSkeletonSchedule scheduleVal = XhbSkeletonScheduleHelper.getLocalHome().findByPrimaryKey(
                    value.getPrimaryKey());
            log.debug("updateSkeletonSchedule VO version =" + value.getVersion());
            log.debug("updateSkeletonSchedule entity version =" + scheduleVal.getVersion());
            scheduleVal.setData(value);

            // find the bean
            final XhbSkeletonDeliveryStatusValue deliveryStatus = value.getXhbSkeletonDeliveryStatus();

            Integer key = deliveryStatus.getSkeletonDeliveryStatusId();

            XhbSkeletonDeliveryStatus delStatus = XhbSkeletonDeliveryStatusHelper.getLocalHome().findByPrimaryKey(key);

            scheduleVal.setXhbSkeletonDeliveryStatus(delStatus);

            final XhbSkeletonScheduleValue scheduleValue = XhbSkeletonScheduleHelper.update(value);

            scheduleValue.setVersion(new Integer(scheduleVal.getVersion().intValue()));

            return new SkeletonScheduleImpl(scheduleValue);
        } catch (FinderException e) {
            log.error(e);
            throw new SkeletonScheduleModificationException("WITNESS_XXX",
                    "Could not find schedule to update it, id was: " + schedule.getId(), e);
        }
    }

    /**
     * Recursively delete the entire schedule.
     * 
     * @param id
     * @throws SkeletonScheduleModificationException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void removeSkeletonSchedule(final Integer id) throws SkeletonScheduleModificationException {
        try {
            XhbSkeletonSchedule schedule = XhbSkeletonScheduleHelper.getLocalHome().findByPrimaryKey(id);
            Collection xhbSkeletonDays = schedule.getXhbSkeletonDays();
            ArrayList queue = new ArrayList();

            // Days
            for (Iterator dayIterator = xhbSkeletonDays.iterator(); dayIterator.hasNext();) {
                XhbSkeletonDay xhbSkeletonDay = (XhbSkeletonDay) dayIterator.next();
                Collection xhbSkeletonSessions = xhbSkeletonDay.getXhbSkeletonSessions();

                // Sessions
                for (Iterator sessionIterator = xhbSkeletonSessions.iterator(); sessionIterator.hasNext();) {
                    XhbSkeletonSession xhbSkeletonSession = (XhbSkeletonSession) sessionIterator.next();
                    Collection xhbWitnesss = xhbSkeletonSession.getXhbWitnesss();

                    // Witnesses
                    queue.addAll(xhbWitnesss);
                    queue.add(xhbSkeletonSession);
                }
                queue.add(xhbSkeletonDay);
            }
            for (int i = 0; i < queue.size(); i++) {
                CSEntityLocal csEntityLocal = (CSEntityLocal) queue.get(i);
                csEntityLocal.remove();
            }
            schedule.remove();

        } catch (EJBException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        } catch (FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        } catch (RemoveException e) {
            log.error(e);
            throw new SkeletonScheduleModificationException("WITNESS_XXX", "Could not remove schedule, id was: " + id,
                    e);
        }
    }

    /**
     * @param caseId
     * @return
     * @throws
     *            uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public SkeletonSchedule createSkeletonSchedule(final Integer caseId, final boolean deliverable)
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException {
        try {
            log.debug("Deliverable value passed in :: " + deliverable);
            final XhbCase xhbCase = XhbCaseHelper.getLocalHome().findByPrimaryKey(caseId);
            log.debug("**** " + xhbCase.getCaseId());

            final XhbSkeletonDeliveryStatus NOTREADY = (XhbSkeletonDeliveryStatus) deliveryStatuses
                    .get(SkeletonSchedule.NOTREADY);

            // set up default skeleton schedule
            log.debug("Default Skeleton Schedule Created :: Deliverable N and DeliveryStatus NULL");
            XhbSkeletonSchedule newSchedule = XhbSkeletonScheduleHelper.getLocalHome().create(null, null, null, null, new Integer(0), DELIVERABLE_DEFAULT_VALUE, xhbCase);

            // check if deliverable then set Deliverable as Y and Delivery
            // Status as NOTREADY
            if (deliverable) {
                log.debug("Skeleton Schedule is Deliverable :: set Deliverable Y and DeliveryStatus NOTREADY");
                newSchedule.setDeliverable("Y");
                newSchedule.setXhbSkeletonDeliveryStatus(NOTREADY);
            }
            return new SkeletonScheduleImpl(newSchedule.getData());
        } catch (CreateException e) {
            log.error(e);
            throw new uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException(
                    "Could not create schedule for case: " + caseId, e);
        } catch (FinderException e) {
            log.error(e);
            throw new uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException(
                    "Could not locate case: " + caseId, e);
        }
    }

    /**
     * @return
     * @ejb.interface-method view-type="remote"
     */
    public XhbSkeletonDeliveryStatusValue[] getDeliveryStatusValues() {
        try {
            return XhbSkeletonDeliveryStatusHelper.findAll();
        } catch (FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }

    }

    private XhbSkeletonDay findSkeletonDayWithEmptySession(final XhbSkeletonSchedule skeletonSchedule,
            final Integer dayNumber, final String session) throws SkeletonDayHasDateException {
        final Collection xhbSkeletonDays = skeletonSchedule.getXhbSkeletonDays();
        XhbSkeletonDay skeletonDay = null;
        for (Iterator iterator = xhbSkeletonDays.iterator(); iterator.hasNext();)

        {
            final XhbSkeletonDay xhbSkeletonDay = (XhbSkeletonDay) iterator.next();
            if (xhbSkeletonDay.getDayNumber() == dayNumber.intValue()) {
                final Collection xhbSkeletonSessions = xhbSkeletonDay.getXhbSkeletonSessions();
                for (Iterator iterator2 = xhbSkeletonSessions.iterator(); iterator2.hasNext();) {
                    final XhbSkeletonSession xhbSkeletonSession = (XhbSkeletonSession) iterator2.next();
                    if (xhbSkeletonSession.getMorningOrAfternoon().equals(session)
                            && (xhbSkeletonSession.getObsInd().equalsIgnoreCase("N") || null == xhbSkeletonSession
                                    .getObsInd())) {
                        log.debug("findSkeleton -- Throw SME");
                        throw new SkeletonDayHasDateException("WITNESS_XXX", "Skeleton day has date already.");
                    }
                }
                skeletonDay = xhbSkeletonDay;
            }
        }
        return skeletonDay;
    }

    // Notes: return a list of skeletons where
    // xhb_skeleton_schedule.deliverable = ‘Y’ and
    // xhb_skeleton_schedule.skeleton_delivery_status in (2, 3,4) and no
    // dates "realized". Order by created date (latest first). Criteria
    // specified by Doug.
    // Display Deliverable = Y and status 2 or 3 - no need to display Failed
    // ones.
    /**
     * @return
     * @ejb.interface-method view-type="remote"
     */
    public SkeletonSchedule[] getIssuedSkeletonSchedules(Integer courtId) {
        try {
            final ArrayList values = new ArrayList();
            final uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonScheduleValue[] schedules = XhbSkeletonScheduleHelper
                    .findEmptySchedules(courtId);
            log.debug("SkeletonControllerBean :: no of Issued Skeleton Schedules :: " + schedules.length);

            for (int i = 0; i < schedules.length; i++) {
                final XhbSkeletonScheduleValue emptySchedule = schedules[i];
                values.add(new SkeletonScheduleImpl(emptySchedule));
            }

            final SkeletonSchedule[] skeletonSchedules = (SkeletonSchedule[]) values
                    .toArray(new SkeletonSchedule[values.size()]);
            Arrays.sort(skeletonSchedules);
            return skeletonSchedules;

        } catch (javax.ejb.FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }

    }
}
