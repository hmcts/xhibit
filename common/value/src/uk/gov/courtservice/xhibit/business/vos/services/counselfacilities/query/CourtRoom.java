package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A value object used to represent a court room in a court list. This object is
 * immutable, although methods have been provided to allow the addition of
 * sittings.
 * 
 * @author tz0d5m
 * @version $Id: CourtRoom.java,v 1.3 2006/06/05 12:28:44 bzjrnl Exp $
 */
public final class CourtRoom implements Serializable {
    // the sittings for this list. The map is provided to allow easy
    // searches.
    private final List sittings = new ArrayList();

    private final Map sittingsMap = new HashMap();

    private Integer id;

    private String courtSiteShortName;

    private String courtRoomDisplayName;

    private boolean floating;

    private static final long serialVersionUID = -5567051897514348138L;
    
    /**
     * Constructor for use only by the serialization process. This is an
     * immutable class, and should only be constructed with the argument-taking
     * constructor.
     */
    public CourtRoom() {
        super();
    }

    /**
     * Constructor for use when creating a floating court room, takes floating
     * parameter required to set up this immutable value object.
     * 
     * @param floating
     *            Whether this court room represents the floating sittings (not
     *            assigned to a court room).
     */
    public CourtRoom(final boolean floating) {
        this.floating = floating;
    }

    /**
     * Full constructor, takes all parameters required to set up this immutable
     * value object.
     * 
     * @param id
     *            The id for the court room.
     * @param courtSiteShortName
     *            The short name for the court site this sitting is in
     * @param courtRoomDisplayName
     *            The court rooms display name.
     * @param floating
     *            Whether this court room represents the floating sittings (not
     *            assigned to a court room).
     */
    public CourtRoom(final Integer id, final String courtSiteShortName, final String courtRoomDisplayName,
            final boolean floating) {
        this.id = id;
        this.courtSiteShortName = courtSiteShortName;
        this.courtRoomDisplayName = courtRoomDisplayName;
        this.floating = floating;
    }

    /**
     * Add the passed in sitting to this court room. It is assumed that the
     * passed in <code>Sitting</code> is not already in this list.
     * 
     * @param sitting
     *            The <code>Sitting</code> to add. If <i>null</i> then
     *            nothing will get added.
     */
    public void addSitting(final Sitting sitting) {
        if (sitting != null) {
            this.sittings.add(sitting);
            this.sittingsMap.put(sitting.getId(), sitting);
        }
    }

    /**
     * Acquire the sitting with the specified id from this court list if found.
     * If the sitting is not currently in this court list, then <i>null</i>
     * will be returned.
     * 
     * @param sittingId
     *            The id of the sitting we want to locate
     * @return The acquired <code>Sitting</code> object, or <i>null</i> if
     *         not found.
     */
    public Sitting getSitting(final Integer sittingId) {
        return (Sitting) this.sittingsMap.get(sittingId);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Below are all of the direct accessor methods for this classes
    // variables.
    // /////////////////////////////////////////////////////////////////////////

    public Integer getId() {
        return this.id;
    }

    public String getCourtSiteShortName() {
        return this.courtSiteShortName;
    }

    public String getCourtRoomDisplayName() {
        return this.courtRoomDisplayName;
    }

    public boolean isFloating() {
        return this.floating;
    }

    public List getSittings() {
        return this.sittings;
    }
}
