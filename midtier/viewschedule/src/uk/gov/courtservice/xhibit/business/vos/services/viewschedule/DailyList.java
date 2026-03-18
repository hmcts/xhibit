package uk.gov.courtservice.xhibit.business.vos.services.viewschedule;

import java.io.Serializable;
import java.util.Date;

/**
 * Type definition for a daily list entry. This represents a simple value
 * object.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 */
public interface DailyList extends Serializable {
    /**
     * Method used to acquire the names of all of the defendants on the
     * scheduled hearing that this daily list entry represents.
     * 
     * @return A <code>String[]</code> array of the defendant names, formatted
     *         by the database.
     */
    public String[] getDefendantNames();

    /**
     * Method to determine if the case for the scheduled hearing this daily list
     * entry represents is classes as floating (not assigned to a court room).
     * 
     * @return <i>true</i> if floating, <i>false</i> otherwise.
     */
    public boolean isFloating();

    public String getHearingType();

    /**
     * Method to acquire the formatted case number: i.e. T20040899
     * 
     * @return The case number.
     */
    public String getCase();

    public Date getNotBeforeTime();

    public int getCaseId();

    public int getScheduledHearingId();

    /**
     * Method to acquire the full name of the court room that this scheduled
     * hearing is to be heard in.
     * 
     * @return The court name.
     */
    public String getCourtRoomName();
}
