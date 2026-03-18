package uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionModificationException;

/**
 * <p>
 * Title: Trial Session
 * </p>
 * <p>
 * Description: A TrialSession object represents the day and session of a trial.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * 
 * Comparable by combination of day number and session. So when sorted they are
 * in time orders by day number and session.
 * 
 * @author Neil Ellis
 * 
 * @version $Revision: 1.7 $
 */
public interface TrialSession extends java.io.Serializable, java.lang.Comparable,
        uk.gov.courtservice.xhibit.business.services.witness.interfaces.StateManaged {
    // Convenience identifiers for session types. I would imagine these
    // will be the only session types ever.
    public static final String MORNING = "M";

    public static final String AFTERNOON = "A";

    /**
     * 
     * @pre getMetaState() != REMOVED
     * @post (getMetaState() == UPDATED || getMetaState() == MODIFIED) implies
     *       return != null
     * @return
     */
    public Integer getId();

    /**
     * Returns the day number of the trial.
     * 
     * @pre getMetaState() != REMOVED
     * @post return > 0
     * @return a positive day number for the Trial Session
     */
    public short getDayNumber();

    /**
     * @pre dayNumber >= 0
     * @param dayNumber
     */
    public void setDayNumber(short dayNumber);

    public short getWeek();

    /**
     * @pre getMetaState() != REMOVED
     * @return
     */
    public Date getAppearanceDate();

    /**
     * @pre getMetaState() != REMOVED
     * @param date
     */
    public void setAppearanceDate(Date date);

    /**
     * Returns the session type as an identfier string.
     * 
     * @pre getMetaState() != REMOVED
     * @see TrialSession#MORNING, TrialSession#AFTERNOON
     * @pre getMetaState() != REMOVED
     * @post return != null implies
     *       Arrays.binarySearch(this.getSessionTypes(),return) >= 0
     * @return
     */
    public String getSessionType();

    /**
     * @pre getMetaState() != REMOVED //@pre sessionType != null implies
     *      Arrays.binarySearch(this.getSessionTypes(),sessionType) >= 0
     * @param sessionType
     */
    public void setSessionType(String sessionType);

    /**
     * @pre getMetaState() != REMOVED
     * @return a string containing notes for this session.
     */
    public String getNotes();

    /**
     * @pre getMetaState() != REMOVED
     * @pre notes.length() < 256
     * @param notes
     */
    public void setNotes(String notes);

    /**
     * 
     * @pre getMetaState() != REMOVED
     * @return
     */
    public boolean hasWitnesses();

    /**
     * @pre getMetaState() != REMOVED
     * @return
     */
    public float getDayAndSessionAsDurationInDays();

    /**
     * @pre getMetaState() != REMOVED
     * @post getMetaState() == REMOVED
     * @param force
     *            remove the TrialSession even if there are associated
     *            witnesses.
     * @throws TrialSessionModificationException
     */
    public void remove(boolean force) throws TrialSessionModificationException;
}
