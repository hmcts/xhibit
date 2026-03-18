package uk.gov.courtservice.xhibit.courtlog.witness;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_witness.XhbWitness;
import uk.gov.courtservice.xhibit.courtlog.CourtLogCategoryDescription;
import uk.gov.courtservice.xhibit.courtlog.exceptions.WitnessReleasedException;
import uk.gov.courtservice.xhibit.courtlog.exceptions.WitnessSwornException;
import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.xml.CourtLogXmlHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * Helper class for the witness released and witness sworn subscribers.
 * 
 * @author tz0d5m
 * @version $Revision: 1.20 $
 */
public class WitnessHelper {
    private static final Logger LOG = CSServices.getLogger(WitnessHelper.class);

    /** Constant for the trial witness sworn event id */
    public static final Integer TRIAL_WITNESS_SWORN_INT = new Integer(20904);
    
    /** Constant for the trial witness read event id */
    public static final Integer TRIAL_WITNESS_READ_INT = new Integer(20935);

    /** Constant for the trial witness released event id */
    public static final Integer TRIAL_WITNESS_RELEASED_INT = new Integer(20905);

    /** Constant for the appeal witness sworn event id */
    public static final Integer APPEAL_WITNESS_SWORN_INT = new Integer(20603);

    /** Constant for the appeal witness released event id */
    public static final Integer APPEAL_WITNESS_RELEASED_INT = new Integer(20604);

    /** Constant representing appeal case type */
    protected static final String APPEAL_CASE_TYPE = "A";

    /** Constant representing trial case type */
    protected static final String TRIAL_CASE_TYPE = "T";

    /** Constant representing event type as held in the logEntryXml */
    protected static final String EVENT_TYPE = "type";

    private static final Properties XPATH_CONFIG = CSServices.getConfigServices().getProperties(
            "witnessreleased.xpaths");

    /**
     * Private constructor, used to prevent external instantiation
     */
    private WitnessHelper() {
        // do not allow external instantiation...
    }

    /**
     * Method to determine if the passed in eventId represents an appeal witness
     * event.
     * 
     * @param eventId
     *            The id of the event
     * @return <i>true</i> if for an appeal case, <i>false</i> otherwise.
     */
    public static boolean isAppealWitnessEvent(Integer eventId) {
        final boolean appealWitnessEvent = ((eventId != null) && (eventId.equals(APPEAL_WITNESS_SWORN_INT) || eventId.equals(APPEAL_WITNESS_RELEASED_INT)));

        LOG.debug("isAppealWitnessEvent(" + eventId + ") = " + appealWitnessEvent);
        return appealWitnessEvent;
    }

    /**
     * Method to determine if the passed in eventId represents a trial witness
     * event.
     * 
     * @param eventId
     *            The id of the event
     * @return <i>true</i> if for a trial case, <i>false</i> otherwise.
     */
    public static boolean isTrialWitnessEvent(Integer eventId) {
        final boolean trialWitnessEvent = ((eventId != null) && (eventId.equals(TRIAL_WITNESS_SWORN_INT) || eventId.equals(TRIAL_WITNESS_RELEASED_INT)));

        LOG.debug("isTrialWitnessEvent(" + eventId + ") = " + trialWitnessEvent);
        return trialWitnessEvent;
    }

    /**
     * Method to acquire the most recent court log event for a particular case
     * that occurs before the passed in date for the witness sworn category. If
     * the passed in date is <i>null</i>, then it will be defaulted to the
     * current server date/time.
     * 
     * @param caseId
     *            The primary key of the case we want the court log event for
     * @param toDate
     *            The log entry returned will be before this date
     * @return The most recent <code>XhbCourtLogEntry</code>.
     */
    public static XhbCourtLogEntry getMostRecentEventForCase(Integer caseId, String categoryDesc, Date toDate,
            Integer[] eventTypes) {
        final Collection beans = getEventsForCase(caseId, categoryDesc, toDate);
        final Iterator it = beans.iterator();

        while (it.hasNext()) {
            XhbCourtLogEntry xcle = (XhbCourtLogEntry) it.next();

            for (int i = 0; i < eventTypes.length; i++) {
                if (eventTypes[i].equals(xcle.getEventType())) {
                    return xcle;
                }
            }
        }

        return null;
    }

    /**
     * Method to acquire the most recent court log event for a particular case
     * that occurs after the passed in date for the witness sworn category.
     * 
     * @param caseId
     *            The primary key of the case we want the court log event for
     * @param fromDate
     *            The log entry returned will be after this date
     * @return The most recent <code>XhbCourtLogEntry</code>.
     */
    public static XhbCourtLogEntry getNextEventForCase(Integer caseId, String categoryDesc, Date fromDate,
            Integer[] eventTypes) {
        LOG.debug("getNextEventForCase");
        final Collection beans = getEventsForCaseFromDate(caseId, categoryDesc, fromDate);
        LOG.debug("beans returned:" + beans.size());

        Sorter.sort((List) beans, new String[] { "dateTime", "entryId" });

        final Iterator it = beans.iterator();

        while (it.hasNext()) {
            XhbCourtLogEntry xcle = (XhbCourtLogEntry) it.next();

            for (int i = 0; i < eventTypes.length; i++) {
                if (eventTypes[i].equals(xcle.getEventType())) {
                    return xcle;
                }
            }
        }

        return null;
    }

    /**
     * Method to acquire all of the court log events for a particular case that
     * occur before the passed in date for the witness sworn category. If the
     * passed in date is <i>null</i>, then it will be defaulted to the current
     * server date/time.
     * 
     * @param caseId
     *            The primary key of the case we want the court log events for
     * @param toDate
     *            All log entries returned will be before this date
     * @return A <code>Collection</code> of <code>XhbCourtLogEntry</code>
     */
    public static Collection getEventsForCase(Integer caseId, String categoryDesc, Date toDate) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getEventsForCase(" + caseId + ", " + categoryDesc + ", " + toDate);
        }

        final long millis = ((toDate != null) ? toDate.getTime() : System.currentTimeMillis());
        final Timestamp toTimestamp = new Timestamp(millis);

        return XhbCourtLogEntryBeanHelper2.findByCaseIdCategoryDescDate(caseId, categoryDesc, toTimestamp);
    }

    /**
     * Method to acquire all of the court log events for a particular case for
     * the witness category.
     * 
     * @param caseId
     *            The primary key of the case we want the court log events for
     * @param categoryDesc
     *            The witness category description
     * @return A <code>Collection</code> of
     *         <code>XhbCourtLogEntryBasicValue</code>
     */
    public static XhbCourtLogEntryBasicValue[] getAllEventsForCase(Integer caseId, String categoryDesc) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getAllEventsForCase(" + caseId + ", " + categoryDesc + ")");
        }

        return XhbCourtLogEntryBeanHelper2.findByCaseIdCategoryDescValue(caseId, categoryDesc);
    }

    /**
     * Method to acquire all of the court log events for a particular case that
     * occur after the passed in date for the witness sworn category. If the
     * passed in date is <i>null</i>, then it will be defaulted to the current
     * server date/time.
     * 
     * @param caseId
     *            The primary key of the case we want the court log events for
     * @param fromDate
     *            All log entries returned will be after this date
     * @return A <code>Collection</code> of <code>XhbCourtLogEntry</code>
     */
    public static Collection getEventsForCaseFromDate(Integer caseId, String categoryDesc, Date fromDate) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getEventsForCaseFromDate(" + caseId + ", " + categoryDesc + ", " + fromDate + ")");
        }

        final long millis = (fromDate != null ? fromDate.getTime() : System.currentTimeMillis());

        return XhbCourtLogEntryBeanHelper2
                .findByCaseIdCategoryDescFromDate(caseId, categoryDesc, new Timestamp(millis));
    }

    /**
     * Validation method to ensure that the appeal case whose CRUD has been
     * passed in contains the correct witness sworn/released events in correct
     * order (i.e. cannot release a witness before one has been sworn in.
     * 
     * @param courtLogCRUDValue
     *            The CRUD value of the case to check
     * @throws WitnessSwornException
     *             If an attempt is made to swear in a witness before a previous
     *             one has been released.
     * @throws WitnessReleasedException
     *             If an attempt is made to release a witness before a witness
     *             has been sworn in, or the previous witness has already been
     *             released.
     */
    public static void checkAppealWitnessEvents(CourtLogCRUDValue courtLogCRUDValue) throws WitnessSwornException,
            WitnessReleasedException {
        checkWitnessEvents(courtLogCRUDValue, APPEAL_WITNESS_SWORN_INT, APPEAL_WITNESS_RELEASED_INT);
    }

    /**
     * Validation method to ensure that the trial case whose CRUD has been
     * passed in contains the correct witness sworn/released events in correct
     * order (i.e. cannot release a witness before one has been sworn in.
     * 
     * @param courtLogCRUDValue
     *            The CRUD value of the case to check
     * @throws WitnessSwornException
     *             If an attempt is made to swear in a witness before a previous
     *             one has been released.
     * @throws WitnessReleasedException
     *             If an attempt is made to release a witness before a witness
     *             has been sworn in, or the previous witness has already been
     *             released.
     */
    public static void checkTrialWitnessEvents(CourtLogCRUDValue courtLogCRUDValue) throws WitnessSwornException,
            WitnessReleasedException {
        checkWitnessEvents(courtLogCRUDValue, TRIAL_WITNESS_SWORN_INT, TRIAL_WITNESS_RELEASED_INT);
    }

    /**
     * Validation method to ensure that the trial case whose CRUD has been
     * passed in contains the correct witness sworn/released events in correct
     * order (i.e. cannot release a witness before one has been sworn in.
     * 
     * @param courtLogCRUDValue
     *            The CRUD value of the case to check
     * @throws WitnessSwornException
     *             If an attempt is made to swear in a witness before a previous
     *             one has been released.
     * @throws WitnessReleasedException
     *             If an attempt is made to release a witness before a witness
     *             has been sworn in, or the previous witness has already been
     *             released.
     */
    public static void checkTrialWitnessEventsForDeletion(CourtLogCRUDValue courtLogCRUDValue)
            throws WitnessSwornException, WitnessReleasedException {
        checkWitnessEventsForDeletion(courtLogCRUDValue, TRIAL_WITNESS_SWORN_INT, TRIAL_WITNESS_RELEASED_INT);
    }

    /**
     * Validation method to ensure that the trial case whose CRUD has been
     * passed in contains the correct witness sworn/released events in correct
     * order (i.e. cannot release a witness before one has been sworn in.
     * 
     * @param courtLogCRUDValue
     *            The CRUD value of the case to check
     * @throws WitnessSwornException
     *             If an attempt is made to swear in a witness before a previous
     *             one has been released.
     * @throws WitnessReleasedException
     *             If an attempt is made to release a witness before a witness
     *             has been sworn in, or the previous witness has already been
     *             released.
     */
    public static void checkTrialWitnessEventsForUpdate(CourtLogCRUDValue courtLogCRUDValue)
            throws WitnessSwornException, WitnessReleasedException {
        checkWitnessEventsForUpdate(courtLogCRUDValue, TRIAL_WITNESS_SWORN_INT, TRIAL_WITNESS_RELEASED_INT);
    }

    /**
     * Validation method to ensure that the appeal case whose CRUD has been
     * passed in contains the correct witness sworn/released events in correct
     * order (i.e. cannot release a witness before one has been sworn in.
     * 
     * @param courtLogCRUDValue
     *            The CRUD value of the case to check
     * @throws WitnessSwornException
     *             If an attempt is made to swear in a witness before a previous
     *             one has been released.
     * @throws WitnessReleasedException
     *             If an attempt is made to release a witness before a witness
     *             has been sworn in, or the previous witness has already been
     *             released.
     */
    public static void checkAppealWitnessEventsForUpdate(CourtLogCRUDValue courtLogCRUDValue)
            throws WitnessSwornException, WitnessReleasedException {
        checkWitnessEventsForUpdate(courtLogCRUDValue, APPEAL_WITNESS_SWORN_INT, APPEAL_WITNESS_RELEASED_INT);
    }

    /**
     * Validation method to ensure that the appeal case whose CRUD has been
     * passed in contains the correct witness sworn/released events in correct
     * order (i.e. cannot release a witness before one has been sworn in.
     * 
     * @param courtLogCRUDValue
     *            The CRUD value of the case to check
     * @throws WitnessSwornException
     *             If an attempt is made to swear in a witness before a previous
     *             one has been released.
     * @throws WitnessReleasedException
     *             If an attempt is made to release a witness before a witness
     *             has been sworn in, or the previous witness has already been
     *             released.
     */
    public static void checkAppealWitnessEventsForDeletion(CourtLogCRUDValue courtLogCRUDValue)
            throws WitnessSwornException, WitnessReleasedException {
        checkWitnessEventsForDeletion(courtLogCRUDValue, APPEAL_WITNESS_SWORN_INT, APPEAL_WITNESS_RELEASED_INT);
    }

    /**
     * Validation method to ensure that the case whose CRUD has been passed in
     * contains the correct witness sworn/released events in correct order (i.e.
     * cannot release a witness before one has been sworn in. For the case type
     * determined by the witnessSworn category, witnessSwornEventType and the
     * witnessReleasedEventType parameters.
     * 
     * @param courtLogCRUDValue
     *            The CRUD value of the case to check
     * @param witnessSwornCategory
     * @param witnessSwornEventType
     * @param witnessReleasedEventType
     * @throws WitnessSwornException
     *             If an attempt is made to swear in a witness before a previous
     *             one has been released.
     * @throws WitnessReleasedException
     *             If an attempt is made to release a witness before a witness
     *             has been sworn in, or the previous witness has already been
     *             released.
     */
    private static void checkWitnessEvents(CourtLogCRUDValue courtLogCRUDValue, Integer witnessSwornEventType,
            Integer witnessReleasedEventType) throws WitnessSwornException, WitnessReleasedException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("checkWitnessEvents(" + witnessSwornEventType + ", " + witnessReleasedEventType + ")");
        }

        // Find the witness event that will follow the one to be created
        final XhbCourtLogEntry nextEvent = getNextEventForCase(courtLogCRUDValue.getCaseId(),
                CourtLogCategoryDescription.WITNESS_CATEGORY_DESC, courtLogCRUDValue.getEntryDate(), new Integer[] {
                        witnessSwornEventType, witnessReleasedEventType });

        // If the event to be created has the same event type as the one that
        // follows it, then throw an appropriate exception
        if (nextEvent != null) {
            if (nextEvent.getEventType().equals(courtLogCRUDValue.getEventType())) {
                if (courtLogCRUDValue.getEventType().equals(witnessSwornEventType)) {
                    throw new WitnessSwornException();
                } else {
                    throw new WitnessReleasedException();
                }
            }
        }

        // Find the witness event that immediately precedes the one to be
        // created
        final XhbCourtLogEntry mostRecentEvent = getMostRecentEventForCase(courtLogCRUDValue.getCaseId(),
                CourtLogCategoryDescription.WITNESS_CATEGORY_DESC, courtLogCRUDValue.getEntryDate(), new Integer[] {
                        witnessSwornEventType, witnessReleasedEventType });

        // Ensure the sworn/release event pair is not violated
        if (mostRecentEvent != null) {
            final Integer lastWitnessEventType = mostRecentEvent.getXhbCourtLogEventDesc().getEventType();

            LOG.debug("checkWitnessEvents - courtLogCRUDValue.getEventType() = " + courtLogCRUDValue.getEventType());
            LOG.debug("checkWitnessEvents - lastWitnessEventType = " + lastWitnessEventType);

            if (courtLogCRUDValue.getEventType().equals(witnessSwornEventType)) {
                if (!lastWitnessEventType.equals(witnessReleasedEventType)) {
                    // unable to swear in before previous witness
                    // released...
                    throw new WitnessSwornException();
                }
            } else // if (newEventType.equals(witnessReleasedEventType))
            {
                if (!lastWitnessEventType.equals(witnessSwornEventType)) {
                    // unable to release before a witness has been sworn
                    // in...
                    throw new WitnessReleasedException();
                } else // if
                // (lastWitnessEventType.equals(witnessSwornEventType))
                {
                    addWitnessNameNumberToReleasedEvent(courtLogCRUDValue, witnessSwornEventType,
                            witnessReleasedEventType, mostRecentEvent);
                }
            }
        } else if (!courtLogCRUDValue.getEventType().equals(witnessSwornEventType)) {
            // unable to release before a witness has been sworn in...
            throw new WitnessReleasedException();
        }
    }

    /**
     * Validation method to ensure that the case whose CRUD has been passed in
     * contains the correct witness sworn/released events in correct order (i.e.
     * cannot release a witness before one has been sworn in.
     * 
     * Try to find a Witness event for the case that occurs after the date/time
     * of the CRUD to be deleted. If one is found, throw an exception.
     * 
     * @param courtLogCRUDValue
     *            The CRUD value of the case to check
     * @param witnessSwornCategory
     * @param witnessSwornEventType
     * @param witnessReleasedEventType
     * @throws WitnessSwornException
     *             If an attempt is made to swear in a witness before a previous
     *             one has been released.
     * @throws WitnessReleasedException
     *             If an attempt is made to release a witness before a witness
     *             has been sworn in, or the previous witness has already been
     *             released.
     */
    private static void checkWitnessEventsForDeletion(CourtLogCRUDValue courtLogCRUDValue,
            Integer witnessSwornEventType, Integer witnessReleasedEventType) throws WitnessSwornException,
            WitnessReleasedException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("checkWitnessEventsForDeletion(" + witnessSwornEventType + ", " + witnessReleasedEventType + ")");
        }

        // Get the witness event for the case that immediately follows the one
        // to be deleted
        final XhbCourtLogEntry nextEvent = getNextEventForCase(courtLogCRUDValue.getCaseId(),
                CourtLogCategoryDescription.WITNESS_CATEGORY_DESC, courtLogCRUDValue.getEntryDate(), new Integer[] {
                        witnessSwornEventType, witnessReleasedEventType });

        // If an event is found, the sworn/release event pair will be violated
        // so throw an exception
        if (nextEvent != null) {
            // unable to swear in before previous witness released...
            if (courtLogCRUDValue.getEventType().equals(witnessSwornEventType)) {
                throw new WitnessReleasedException();
            } else {
                throw new WitnessSwornException();
            }
        }
    }

    /**
     * Validation method to ensure that the case whose CRUD has been passed in
     * contains the correct witness sworn/released events in correct order (i.e.
     * cannot release a witness before one has been sworn in.
     * 
     * Retrieve a collection of all the witness events for the case. Iterate
     * thru the collection searching for the event being updated and when found,
     * set the date/time of the entry to the date/time from the CRUD value
     * passed in. Sort the collection by date/time. Iterate thru the collection
     * ensuring that no two consecutive event types are the same as this
     * suggests a violation of the sworn/released pair. If two consecutive
     * events have the same event type, throw an exception.
     * 
     * @param courtLogCRUDValue
     *            The CRUD value of the case to check
     * @param witnessSwornCategory
     * @param witnessSwornEventType
     * @param witnessReleasedEventType
     * @throws WitnessSwornException
     *             If an attempt is made to swear in a witness before a previous
     *             one has been released.
     * @throws WitnessReleasedException
     *             If an attempt is made to release a witness before a witness
     *             has been sworn in, or the previous witness has already been
     *             released.
     */
    private static void checkWitnessEventsForUpdate(CourtLogCRUDValue courtLogCRUDValue, Integer witnessSwornEventType,
            Integer witnessReleasedEventType) throws WitnessSwornException, WitnessReleasedException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("checkWitnessEventsForUpdate(" + witnessSwornEventType + ", " + witnessReleasedEventType + ")");
        }

        // Get all witness events for the case
        final XhbCourtLogEntryBasicValue[] allEvents = getAllEventsForCase(courtLogCRUDValue.getCaseId(),
                CourtLogCategoryDescription.WITNESS_CATEGORY_DESC);

        // Find the record that is being updated and update the entry date/time
        for (int x = 0; x < allEvents.length; x++) {
            XhbCourtLogEntryBasicValue item = allEvents[x];
            if (item.getEntryId().equals(courtLogCRUDValue.getLogEntryId())) {
                item.setDateTime(courtLogCRUDValue.getEntryDate());
                break;
            }
        }

        // Sort all the events by entry date/time
        Sorter.sort(allEvents, new String[] { "dateTime", "entryId" }, new Boolean(true));

        // Ensure the sworn/released event pair is not violated
        boolean firstItem = true;
        Integer previousEventDescId = null;
        for (int x = 0; x < allEvents.length; x++) {
            XhbCourtLogEntryBasicValue item = allEvents[x];

            String eventType = getElementFromXML(item.getLogEntryXml(), EVENT_TYPE);

            if (firstItem) {
                if (!eventType.equalsIgnoreCase(witnessSwornEventType.toString())) {
                    throw new WitnessReleasedException();
                }
                firstItem = false;
                previousEventDescId = item.getEventDescId();
            } else {
                if (item.getEventDescId().equals(previousEventDescId)) {
                    if (courtLogCRUDValue.getEventType().equals(witnessSwornEventType)) {
                        throw new WitnessReleasedException();
                    } else {
                        throw new WitnessSwornException();
                    }
                } else {
                    previousEventDescId = item.getEventDescId();
                }
            }
        }
    }

    /**
     * Utility method to take the witness name and number details from the
     * previous witness event (which must be a witness sworn event), and copy
     * them onto the new court log entry (which must be a witness released
     * event).
     * 
     * @param courtLogCRUDValue
     *            The <code>CourtLogCRUDValue</code> to copy the values to
     * @param witnessSwornEventType
     *            The witness sworn event type
     * @param witnessReleasedEventType
     *            The witness released event type
     * @param mostRecentEvent
     *            The <code>XhbCourtLogEntry</code> to copy the values from.
     */
    private static void addWitnessNameNumberToReleasedEvent(CourtLogCRUDValue courtLogCRUDValue,
            Integer witnessSwornEventType, Integer witnessReleasedEventType, XhbCourtLogEntry previousCourtLogEntry) {
        // Add witness name and number to released event
        final Map props = CourtLogXmlHelper.getPropertySet(previousCourtLogEntry.getLogEntryXml());
        final Map woOptions = (Map) props.get("E" + witnessSwornEventType + "_Witness_Sworn_Options");
        final String woNumber = (String) woOptions.get("E" + witnessSwornEventType + "_WSO_Number");
        final String woName = (String) woOptions.get("E" + witnessSwornEventType + "_WSO_Name");

        if (woNumber != null) {
            courtLogCRUDValue.getPropertyMap().put("E" + witnessReleasedEventType + "_WSO_Number", woNumber);
        }

        if (woName != null) {
            courtLogCRUDValue.getPropertyMap().put("E" + witnessReleasedEventType + "_WSO_Name", woName);
        }
    }

    /**
     * Method to reset the released date time, and the calculated witness time
     * for the passed in witness to <i>null</i>
     * 
     * @param witnessId
     *            The primary key of the witness to look up
     */
    public static void removeReleaseInfo(Integer witnessId) {
        final XhbWitness witness = EntityHelper.getXhbWitness(witnessId);
        // remove both the released date and the calculated time
        witness.setReleasedDateTime(null);
        witness.setCalculatedWitnessTime(null);
    }

    /**
     * Delegate method used to pass the required trial specific parameters to
     * the validateWitnessId method.
     * 
     * @param courtLogCRUDValue
     * @see #validateWitnessId(uk.gov.courtservice.xhibit.business.vos.services
     *      .courtlog.CourtLogCRUDValue, java.lang.String, java.lang.String)
     */
    public static void validateTrialWitnessId(CourtLogCRUDValue courtLogCRUDValue) {
        validateWitnessId(courtLogCRUDValue, "E20904_Witness_Sworn_Options", "E20904_WSO_ID");
    }
    
    /**
     * Delegate method used to pass the required trial specific parameters to
     * the validateWitnessId method.
     * 
     * @param courtLogCRUDValue
     * @see #validateWitnessId(uk.gov.courtservice.xhibit.business.vos.services
     *      .courtlog.CourtLogCRUDValue, java.lang.String, java.lang.String)
     */
    public static void validateTrialWitnessReadId(CourtLogCRUDValue courtLogCRUDValue) {
        validateWitnessId(courtLogCRUDValue, "E20935_Witness_Read_Options", "E20935_WR_ID");
    }

    /**
     * Delegate method used to pass the required appeal specific parameters to
     * the validateWitnessId method.
     * 
     * @param courtLogCRUDValue
     * @see #validateWitnessId(uk.gov.courtservice.xhibit.business.vos.services
     *      .courtlog.CourtLogCRUDValue, java.lang.String, java.lang.String)
     */
    public static void validateAppealWitnessId(CourtLogCRUDValue courtLogCRUDValue) {
        validateWitnessId(courtLogCRUDValue, "E20603_Witness_Sworn_Options", "E20603_Witness_ID");
    }

    /**
     * Utility method that is used to ensure that a value has been populated in
     * the witness id options field. If no value has been populated, then the
     * default of -1 will be entered (which is used to represent no witness id).
     * The method will actually modify the properties of the courtLogCRUDValue
     * passed in parameter (as it is passed by reference).
     * 
     * @param courtLogCRUDValue
     * @param optionsPropertyName
     * @param witnessPropertyName
     */
    private static void validateWitnessId(CourtLogCRUDValue courtLogCRUDValue, String optionsMapPropertyName,
            String witnessIdPropertyName) {
        final Map woOptions = (Map) courtLogCRUDValue.getProperty(optionsMapPropertyName);

        if (woOptions != null) {
            final String witnessId = (String) woOptions.get(witnessIdPropertyName);

            if ((witnessId == null) || (witnessId.trim().length() == 0)) {
                woOptions.put(witnessIdPropertyName, "-1");
            }
        }
    }

    /**
     * Calculate the time a witness has spent in court set this value on the
     * Witness Entity. Set the released date on the entity as the date of the
     * released event. Nullify the mobile, pager number and pager network of the
     * witness.
     * 
     * @param releaseDate
     *            the date of the release event (from the log event).
     * @param caseId
     *            the primary key for the case entity we are interested in.
     * @param witnessId
     *            the primary key for the witness entity we are interested in.
     */
    public static void releaseWitness(Date releaseDate, Integer caseId, Integer witnessId) {
        XhbWitness witness = EntityHelper.getXhbWitness(witnessId);

        Date witnessTime = WitnessTimeCalculator
                .getWitnessTime(caseId, witness.getActualArrivalDateTime(), releaseDate);

        // releaseDate comes from the court log release event
        witness.setReleasedDateTime(releaseDate);
        witness.setCalculatedWitnessTime(witnessTime);
        witness.setMobilenumber(null);
        witness.setPagernumber(null);
        witness.setPagernet(null);
    }

    /**
     * Helper method to acquire the last witness id for the passed in case, that
     * occurs before the passed in entry date.
     * 
     * @param eventType
     *            The event that we are currently creating, used to acquire the
     *            case type.
     * @param caseId
     *            The primary key of the case we want to get the last witness id
     *            for.
     * @param entryDate
     *            The date that we want to get the witness events prior to
     * @return The primary key of the last witness sworn, or <i>null</i> if
     *         there is no previous witness.
     */
    public static Integer getLastWitnessId(Integer eventType, Integer caseId, Date entryDate) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getLastWitnessId(" + eventType + ", " + caseId + ", " + entryDate + ")");
        }

        if (WitnessHelper.isTrialWitnessEvent(eventType)) {
            return WitnessHelper.findWitnessSwornId(entryDate, caseId, "trialWitnessId", TRIAL_WITNESS_SWORN_INT);
        }

        if (WitnessHelper.isAppealWitnessEvent(eventType)) {
            return WitnessHelper.findWitnessSwornId(entryDate, caseId, "appealWitnessId", APPEAL_WITNESS_SWORN_INT);
        }

        return null;
    }

    /**
     * For the case specified find the last sworn event older than the passed in
     * date, and obtain the witness id from the log.
     * 
     * @param entryDate
     * @param caseId
     *            the primary key value for the case.
     * @param witnessSwornEventType
     * @param witnessIdProperty
     * @return the witness id
     */
    private static Integer findWitnessSwornId(Date entryDate, Integer caseId, String witnessIdProperty,
            Integer eventType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("findWitnessSwornId(" + entryDate + ", " + caseId + ", " + witnessIdProperty + ", " + eventType
                    + ")");
        }

        final Collection events = XhbCourtLogEntryBeanHelper2.findByCaseIdEventTypeToDateDescending(caseId, eventType,
                entryDate);
        final Iterator it = events.iterator();

        // find the witness id - the released event does not contain the
        // witness id so we first need to find the sworn event which matches
        // this released event

        while (it.hasNext()) {
            final XhbCourtLogEntry courtLogEntry = (XhbCourtLogEntry) it.next();
            String witnessId = CSServices.getXMLServices().getXpathValueFromXmlString(courtLogEntry.getLogEntryXml(),
                    XPATH_CONFIG.getProperty(witnessIdProperty));

            if ((witnessId != null) && (!witnessId.equals("-1"))) {
                return new Integer(witnessId);
            }
        }

        return null;
    }

    /**
     * Utility method to extract a top-level element from an XML string
     * 
     * @param xmlFragment -
     *            The XML fragment to be inspected
     * @param elementName -
     *            The tag to be searched for
     * @return String representing the data coresponding to the tag
     */
    private static String getElementFromXML(String xmlFragment, String elementName) {
        Map elements = CourtLogXmlHelper.getPropertySet(xmlFragment);
        return (String) elements.get(elementName);
    }
}
