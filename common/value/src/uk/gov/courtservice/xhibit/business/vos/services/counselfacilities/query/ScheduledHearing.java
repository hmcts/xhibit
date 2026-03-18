package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A value object used to represent a scheduled hearing in a sitting. This
 * object is immutable, although methods have been provided to allow the
 * addition of prosecution advocates and defendants.
 * 
 * @author tz0d5m
 * @version $Id: ScheduledHearing.java,v 1.5 2006/06/05 12:28:44 bzjrnl Exp $
 */
public final class ScheduledHearing implements Serializable {
    // the defendants for this scheduled hearing. The map is provided to
    // allow easy searches.
    private final List defendants = new ArrayList(5);

    private final Map defendantsMap = new HashMap(5);

    // the prosecution advocates for this scheduled hearing. The map is
    // provided to allow easy searches.
    private final List prosecutionAdvocates = new ArrayList(5);

    private final Map prosecutionAdvocatesMap = new HashMap(5);

    private Integer id;

    private String caseTitle;

    private String hearingType;

    private String caseType;

    private String caseNumber;

    private Integer caseId;

    private Date notBeforeTime;
    
    private static final long serialVersionUID = 6557806518344743512L;

    /**
     * Constructor for use only by the serialization process. This is an
     * immutable class, and should only be constructed with the argument-taking
     * constructor.
     */
    public ScheduledHearing() {
        super();
    }

    /**
     * Only useable constructor, takes all parameters required to set up this
     * immutable value object.
     * 
     * @param id
     *            The id of the scheduled hearing.
     * @param caseTitle
     *            The title of the case the scheduled hearing represents.
     * @param hearingType
     *            The type of this hearing.
     * @param caseType
     *            The type of the case.
     * @param caseNumber
     *            The number of the case.
     * @param caseId
     *            The id of the case.
     * @param notBeforeTime
     *            The not before time of the scheduled hearing.
     */
    public ScheduledHearing(final Integer id, final String caseTitle, final String hearingType, final String caseType,
            final String caseNumber, final Integer caseId, final Date notBeforeTime) {
        this.id = id;
        this.caseTitle = caseTitle;
        this.hearingType = hearingType;
        this.caseType = caseType;
        this.caseNumber = caseNumber;
        this.caseId = caseId;
        this.notBeforeTime = notBeforeTime;
    }

    /**
     * Add the passed in defendant to this scheduled hearing. It is assumed that
     * the passed in <code>Defendant</code> is not already on this scheduled
     * hearings defendants list.
     * 
     * @param defendant
     *            The <code>Defendant</code> to add. If <i>null</i> then
     *            nothing will get added.
     */
    public void addDefendant(final Defendant defendant) {
        if (defendant != null) {
            this.defendants.add(defendant);
            this.defendantsMap.put(defendant.getId(), defendant);
        }
    }

    /**
     * Add the passed in prosecution advocate to this scheduled hearing. It is
     * assumed that the passed in <code>Person</code> is not already on this
     * scheduled hearings prosecution advocate list.
     * 
     * @param prosAvocate
     *            The <code>Person</code> to add. If <i>null</i> then nothing
     *            will get added.
     */
    public void addProsecutionAdvocate(final Person prosAvocate) {
        if (prosAvocate != null) {
            this.prosecutionAdvocates.add(prosAvocate);
            this.prosecutionAdvocatesMap.put(prosAvocate.getId(), prosAvocate);
        }
    }

    /**
     * Acquire the prosecution advocate with the specified id from this
     * scheduled hearings prosecution advocate list, if found. If the advocate
     * is not currently in the list, then <i>null</i> will be returned.
     * 
     * @param id
     *            The id of the prosecution advocate we want to locate
     * @return The acquired <code>Person</code> object, or <i>null</i> if not
     *         found.
     */
    public Person getProsecutionAdvocate(final Integer advocateId) {
        return (Person) this.prosecutionAdvocatesMap.get(advocateId);
    }

    /**
     * Acquire the defendant with the specified id from this scheduled hearings
     * defendants list, if found. If the defendant is not currently in the list,
     * then <i>null</i> will be returned.
     * 
     * @param id
     *            The id of the defendant we want to locate
     * @return The acquired <code>Defendant</code> object, or <i>null</i> if
     *         not found.
     */
    public Defendant getDefendant(final Integer defendantId) {
        return (Defendant) this.defendantsMap.get(defendantId);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Below are all of the direct accessor methods for this classes
    // variables.
    // /////////////////////////////////////////////////////////////////////////

    public List getDefendants() {
        return this.defendants;
    }

    public List getProsecutionAdvocates() {
        return this.prosecutionAdvocates;
    }

    public Integer getId() {
        return this.id;
    }

    public String getCaseTitle() {
        return this.caseTitle;
    }

    public String getHearingType() {
        return this.hearingType;
    }

    public String getCaseType() {
        return this.caseType;
    }

    public String getCaseNumber() {
        return this.caseNumber;
    }

    public Integer getCaseId() {
        return this.caseId;
    }

    public Date getNotBeforeTime() {
        return this.notBeforeTime;
    }
}
