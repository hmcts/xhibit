package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A value object used to represent a sitting for a court list. This object is
 * immutable, although methods have been provided to allow the addition of court
 * clerks, ushers and scheduled hearings.
 * 
 * @author tz0d5m
 * @version $Id: Sitting.java,v 1.5 2006/06/05 12:28:44 bzjrnl Exp $
 */
public final class Sitting implements Serializable {
    // all of the court clerks in this sitting. The map is provided to
    // allow easy searches.
    private final List courtClerks = new ArrayList(5);

    private final Map courtClerksMap = new HashMap(5);

    // all of the ushers in this sitting. The map is provided to
    // allow easy searches.
    private final List ushers = new ArrayList(5);

    private final Map ushersMap = new HashMap(5);

    // all of the scheduled hearings for this sitting. The map is provided
    // to
    // allow easy searches.
    private final List scheduledHearings = new ArrayList();

    private final Map scheduledHearingsMap = new HashMap();

    private Integer id;

    private String judgeName;

    private Date sittingTime;
    private static final long serialVersionUID = 3778669690748468809L;

    /**
     * Constructor for use only by the serialization process. This is an
     * immutable class, and should only be constructed with the argument-taking
     * constructor.
     */
    public Sitting() {
        super();
    }

    /**
     * Only useable constructor, takes all parameters required to set up this
     * immutable value object.
     * 
     * @param id
     *            The id for the sitting.
     * @param judgeName
     *            The name of the judge assigned to the sitting.
     * @param sittingTime
     *            The time of the sitting.
     */
    public Sitting(final Integer id, final String judgeName, final Date sittingTime) {
        this.id = id;
        this.judgeName = judgeName;
        this.sittingTime = sittingTime;
    }

    /**
     * Add the passed in scheduled hearing to this sitting. It is assumed that
     * the passed in <code>ScheduledHearing</code> is not already on this
     * sittings scheduled hearing list.
     * 
     * @param scheduledHearing
     *            The <code>ScheduledHearing</code> to add. If <i>null</i>
     *            then nothing will get added.
     */
    public void addScheduedHearing(final ScheduledHearing scheduledHearing) {
        if (scheduledHearing != null) {
            this.scheduledHearings.add(scheduledHearing);
            this.scheduledHearingsMap.put(scheduledHearing.getId(), scheduledHearing);
        }
    }

    /**
     * Add the passed in staff member to the list of court clerks. If either
     * parameter is <i>null</i> then nothing will be added. Also, if the staff
     * id matches that of a staff member already in the list then the passed in
     * staff member will also not get added.
     * 
     * @param staffId
     *            The id of the staff member we are trying to add.
     * @param staffName
     *            The name of the staff member we are trying to add.
     */
    public void addCourtClerk(final Integer staffId, final String staffName) {
        if ((staffId != null) && (staffName != null)) {
            // if not in map, then okay to add...
            if (this.courtClerksMap.get(staffId) == null) {
                this.courtClerks.add(staffName);
                this.courtClerksMap.put(staffId, staffName);
            }
        }
    }

    /**
     * Add the passed in staff member to the list of ushers. If either parameter
     * is <i>null</i> then nothing will be added. Also, if the staff id matches
     * that of a staff member already in the list then the passed in staff
     * member will also not get added.
     * 
     * @param staffId
     *            The id of the staff member we are trying to add.
     * @param staffName
     *            The name of the staff member we are trying to add.
     */
    public void addUsher(final Integer staffId, final String staffName) {
        if ((staffId != null) && (staffName != null)) {
            // if not in map, then okay to add...
            if (this.ushersMap.get(staffId) == null) {
                this.ushers.add(staffName);
                this.ushersMap.put(staffId, staffName);
            }
        }
    }

    /**
     * Acquire the prosecution advocate with the specified id from this
     * scheduled hearings prosecution advocate list, if found. If the advocate
     * is not currently in the list, then <i>null</i> will be returned.
     * 
     * @param scheduledHearingId
     *            The id of the scheduled hearing that we want to locate
     * @return The acquired <code>ScheduledHearing</code> object, or <i>null</i>
     *         if not found.
     */
    public ScheduledHearing getScheduledHearing(final Integer scheduledHearingId) {
        return (ScheduledHearing) this.scheduledHearingsMap.get(scheduledHearingId);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Below are all of the direct accessor methods for this classes
    // variables.
    // /////////////////////////////////////////////////////////////////////////

    public List getScheduledHearings() {
        return this.scheduledHearings;
    }

    public List getCourtClerks() {
        return this.courtClerks;
    }

    public List getUshers() {
        return this.ushers;
    }

    public Integer getId() {
        return this.id;
    }

    public String getJudgeName() {
        return this.judgeName;
    }

    public Date getSittingTime() {
        return this.sittingTime;
    }
}
