package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A value object used to represent the a defendant in a court list. This object
 * is immutable, although methods have been provided to allow the addition of
 * advocates to this defendant.
 * 
 * @author tz0d5m
 * @version $Id: Defendant.java,v 1.5 2006/06/05 12:28:44 bzjrnl Exp $
 */
public final class Defendant extends Person {
    // the defendant advocates for this defendants. The map is provided to
    // allow easy searches.
    private final List defendantAdvocates = new ArrayList(3);

    private final Map defendantAdvocatesMap = new HashMap(3);

    private boolean masked;

    private String maskedName;
    
    private static final long serialVersionUID = 4621409278931138070L;

    /**
     * Constructor for use only by the serialization process. This is an
     * immutable class, and should only be constructed with the argument-taking
     * constructor.
     */
    public Defendant() {
        super();
    }

    /**
     * Only useable constructor, takes all parameters required to set up this
     * immutable value object.
     * 
     * @param id
     *            The id of the defendant.
     * @param firstName
     *            The defendants first name.
     * @param middleName
     *            The defendants middle name.
     * @param surname
     *            The defendants surname.
     * @param masked
     *            Flag to indicate if the name should be masked.
     * @param maskedName
     *            The defendants masked name.
     * 
     * @see Person(Integer, String, String, String);
     */
    public Defendant(final Integer id, final String firstName, final String middleName, final String surname,
            final boolean masked, final String maskedName) {
        // defendants do not have titles...
        super(id, null, firstName, middleName, surname);

        this.masked = masked;
        this.maskedName = maskedName;
    }

    /**
     * Add the passed in advocate to this defendant. It is assumed that the
     * passed in <code>Person</code> is not already on this defendants
     * advocate list.
     * 
     * @param defAdvocate
     *            The <code>Person</code> to add. If <i>null</i> then nothing
     *            will get added.
     */
    public void addDefendantAdvocate(final Person defAdvocate) {
        if (defAdvocate != null) {
            this.defendantAdvocates.add(defAdvocate);
            this.defendantAdvocatesMap.put(defAdvocate.getId(), defAdvocate);
        }
    }

    /**
     * Acquire the defendant advocate with the specified id from this defendants
     * advocate list, if found. If the advocate is not currently in this
     * defendants list, then <i>null</i> will be returned.
     * 
     * @param defAdvocateId
     *            The id of the defendant advocate we want to locate.
     * @return The acquired <code>Person</code> object, or <i>null</i> if not
     *         found.
     */
    public Person getDefendantAdvocate(final Integer defAdvocateId) {
        return (Person) this.defendantAdvocatesMap.get(defAdvocateId);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Below are all of the direct accessor methods for this classes
    // variables.
    // /////////////////////////////////////////////////////////////////////////

    public List getDefendantAdvocates() {
        return this.defendantAdvocates;
    }

    public boolean isMasked() {
        return this.masked;
    }

    public String getMaskedName() {
        return this.maskedName;
    }
}
