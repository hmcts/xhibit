package uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces;

import java.sql.Time;
import java.util.Date;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This is a simple summary and read only view of a Witness.
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
 * @version $Revision: 1.10 $
 */

public interface WitnessSummary extends java.io.Serializable, java.lang.Comparable {

    public static final String DEFENCE_TYPE = "Defence";

    public static final String PROSECUTION_TYPE = "Prosecution";

    /**
     * 
     * @pre getMetaState() != REMOVED
     * @post (getMetaState() == UPDATED || getMetaState() == MODIFIED) implies
     *       return != null
     * @return
     */
    Integer getId();

    /**
     * Gets the witness status eg.(Profesional, Jouvernile etc).
     * 
     * @pre getMetaState() != REMOVED
     * @return
     */
    public String getStatus();

    /**
     * Gets the witness type eg.(defense/prosecution,).
     * 
     * @return the type
     */
    public String getType();

    /**
     * The full name of the witness.
     * 
     * @pre getMetaState() != REMOVED
     * @return the full name of the witness.
     */
    public String getName();

    /**
     * @pre getMetaState() != REMOVED
     * @return
     */
    public Date getReleased();

    /**
     * @pre getMetaState() != REMOVED
     * @return
     */
    public Date getArrived();

    /**
     * Returns the age of the witness.
     * 
     * @pre getMetaState() != REMOVED
     * @post getMetaState() != REMOVED && return >= MIN_AGE && return < MAX_AGE
     * 
     * @return The witnesses age
     */
    public int getAge();

    /**
     * 
     * @return weather the witness has a pager/mobile number
     */
    public boolean getHasNumber();

    /**
     * 
     * @return
     */
    public Time getDueAt();

    /**
     * 
     * @return
     */
    public int getTotalTimeHours();

    /**
     * 
     * @return
     */
    public int getTotalTimeMinutes();
}
