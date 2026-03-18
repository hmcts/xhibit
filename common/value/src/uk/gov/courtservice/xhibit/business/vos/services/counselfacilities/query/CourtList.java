package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * A value object used to represent the top level object in a court list. This
 * object is immutable, although methods have been provided to allow the
 * addition of <code>Sitting</code>s to this list.
 * 
 * @author tz0d5m
 * @version $Id: CourtList.java,v 1.5 2006/06/05 12:28:44 bzjrnl Exp $
 */
public final class CourtList extends CSAbstractValue {
    // the court rooms for this list. Map is provided to allow easy
    // searches.
    private final List courtRooms = new ArrayList();

    private final Map courtRoomsMap = new HashMap();

    private final CourtRoom floatingCourtRoom = new CourtRoom(true);

    private Integer courtId;

    private String courtType;

    private String courtName;

    private String courtShortName;

    private Date startDate;

    private Date requestDate;
    private static final long serialVersionUID = -385230232625094674L;

    /**
     * Constructor for use only by the serialization process. This is an
     * immutable class, and should only be constructed with the argument-taking
     * constructor.
     */
    public CourtList() {
        super();
    }

    /**
     * Only useable constructor, takes all parameters required to set up this
     * immutable value object.
     * 
     * @param courtId
     *            The id of the court this list is for.
     * @param courtType
     *            The type of the court this list is for.
     * @param courtName
     *            The name of the court this list is for.
     * @param courtShortName
     *            The short name of the court this list is for.
     * @param startDate
     *            The date this list represents.
     * @param requestDate
     *            The date this list was requested.
     */
    public CourtList(final Integer courtId, final String courtType, final String courtName,
            final String courtShortName, final Date startDate, final Date requestDate) {
        this.courtId = courtId;
        this.courtType = courtType;
        this.courtName = courtName;
        this.courtShortName = courtShortName;
        this.startDate = startDate;
        this.requestDate = requestDate;
    }

    /**
     * Add the passed in court room to this court list. It is assumed that the
     * passed in <code>CourtRoom</code> is not already in this list.
     * 
     * @param courtRoom
     *            The <code>CourtRoom</code> to add. If <i>null</i> then
     *            nothing will get added.
     */
    public void addCourtRoom(final CourtRoom courtRoom) {
        if (courtRoom != null) {
            this.courtRooms.add(courtRoom);
            this.courtRoomsMap.put(courtRoom.getId(), courtRoom);
        }
    }

    /**
     * Acquire the court room with the specified id from this court list if
     * found. If the court room is not currently in this court list, then
     * <i>null</i> will be returned.
     * 
     * @param courtRoomId
     *            The id of the court room we want to locate
     * @return The acquired <code>CourtRoom</code> object, or <i>null</i> if
     *         not found.
     */
    public CourtRoom getCourtRoom(final Integer courtRoomId) {
        return (CourtRoom) this.courtRoomsMap.get(courtRoomId);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Below are all of the direct accessor methods for this classes
    // variables.
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Accessor for all of the court rooms created. Also returns the
     * <code>CourtRoom</code> that represents floating cases.
     * 
     * @return The <code>List</code> of <code>CourtRoom</code>s.
     */
    public List getCourtRooms() {
        final List floatingSittings = this.floatingCourtRoom.getSittings();

        // if there are no floating sittings, return court rooms directly...
        if (floatingSittings.size() == 0) {
            return this.courtRooms;
        } else {
            // otherwise we need to create a new list containing all of the
            // court rooms in the list plus the floating court room...
            final List allCourtRooms = new ArrayList(this.courtRooms);
            allCourtRooms.add(this.floatingCourtRoom);

            return allCourtRooms;
        }
    }

    public Integer getCourtId() {
        return this.courtId;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getRequestDate() {
        return this.requestDate;
    }

    public String getCourtType() {
        return this.courtType;
    }

    public String getCourtName() {
        return this.courtName;
    }

    public String getCourtShortName() {
        return this.courtShortName;
    }

    /**
     * This method should not be called directly except for construction of
     * court information, as the getCourtRooms method includes this
     * <code>CourtRoom</code> object in its returned <code>List</code>.
     * 
     * The method naming used here goes against standard naming conventions.
     * This is for a reason! java bean introspection acquires method
     * names/values based upon certain naming conventions and as we do not want
     * this method to be displayed in the ultimately produced XML document the
     * method name goes against convention.
     * 
     * @return The <code>CourtRoom</code> representing floating cases.
     * @see #getCourtRooms()
     */
    public CourtRoom acquireFloatingCourtRoom() {
        return this.floatingCourtRoom;
    }
}
