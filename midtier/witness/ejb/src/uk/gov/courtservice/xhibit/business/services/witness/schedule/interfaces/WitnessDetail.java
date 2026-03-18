package uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces;

import java.sql.Time;
import java.util.Date;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.InvalidArrivedDateException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.InvalidNetworkException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.InvalidNoteException;

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
 * @version $Revision: 1.7 $
 */

public interface WitnessDetail extends WitnessSummary,
        uk.gov.courtservice.xhibit.business.services.witness.interfaces.StateManaged {
    /**
     * A convenient constant to specify a limit for the age of a witness. This
     * is only used if this class is processed with iContract.
     */
    public static final short MAX_AGE = 200;

    /**
     * A convenient constant to specify a minimum for the age of a witness. This
     * is only used if this class is processed with iContract.
     */
    public static final short MIN_AGE = 0;

    /**
     * A convenient constant to specifythe maximum length of notes. This will be
     * explicitly checked by the appropriate method.
     */
    public static final int MAXIMUM_NOTE_LENGTH = 256;

    /**
     * Sets the name of the witness.
     * 
     * @pre getMetaState() != REMOVED
     * @pre name != null
     * @post getName().equals(name)
     * @post getName() != null
     * 
     * 
     * @param name
     *            the full name of the witness.
     */
    public void setName(String name);

    /**
     * Sets the witness status eg.(Profesional, Jouvernile etc).
     * 
     * @pre getMetaState() != REMOVED
     * @post getStatus().equals(status)
     * @param status
     */
    public void setStatus(String status);

    /**
     * @pre getMetaState() != REMOVED
     * @pre age >= MIN_AGE && age < MAX_AGE
     * @post getAge() > MIN_AGE && getAge() < MAX_AGE
     * @post getAge() == age
     * @param age
     *            the age of the witness
     */
    public void setAge(int age);

    /**
     * @pre getMetaState() != REMOVED
     * @return
     */
    public Time getExpected();

    /**
     * @pre getMetaState() != REMOVED
     * @pre time != null
     * @param time
     */
    public void setExpected(Time time);

    /**
     * @pre getMetaState() != REMOVED
     * @param date
     * @throws InvalidArrivedDateException
     */
    public void setArrived(Date date) throws InvalidArrivedDateException;

    /**
     * @pre getMetaState() != REMOVED
     * @return
     */
    public String getPagerNetwork();

    /**
     * 
     * @pre getMetaState() != REMOVED && network != null
     * @param network
     * @throws InvalidNetworkException
     *             if the network is not valid.
     */
    public void setPagerNetwork(String network) throws InvalidNetworkException;

    /**
     * @pre getMetaState() != REMOVED
     * @post return == null || return.length() < 256
     * @return
     */
    public String getNotes();

    /**
     * Sets the notes for the witness, maimum length is 256 characters.
     * 
     * @pre getMetaState() != REMOVED
     * @pre note == null || note.length() < 256
     * @param note
     * 
     * @throws InvalidNoteException
     *             if the length exceeds that specified
     */
    public void setNotes(String note) throws InvalidNoteException;

    /**
     * 
     * @return
     */
    public String getMobileNumber();

    /**
     * @pre getMetaState() != REMOVED
     * @param number
     */
    public void setMobileNumber(String number);

    /**
     * Returns the pager number of the witness if specified.
     * 
     * @pre getMetaState() != REMOVED
     * @return
     */
    public String getPagerNumber();

    /**
     * Sets the pager number.
     * 
     * @pre getMetaState() != REMOVED
     * @post getPagerNumber().equals(number)
     * @param number
     */
    public void setPagerNumber(String number);

    /**
     * Gets the witness type eg. Defendant, Prosecution.
     * 
     * @pre getMetaState() != REMOVED
     * @return
     */
    public String getType();

    /**
     * Sets the witness type eg. Defendant, Prosecution.
     * 
     * @pre getMetaState() != REMOVED
     * @param type
     */
    public void setType(String type);

    /**
     * Converts a WitnessSession to a WitnessSummary.
     * 
     * @return
     */
    public WitnessSummary getAssociatedWitnessSummary();

}
