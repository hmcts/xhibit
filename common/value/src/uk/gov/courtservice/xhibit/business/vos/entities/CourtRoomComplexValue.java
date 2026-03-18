package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Court Room Basic Value.
 * <p>
 * Returns BasicValues of related Entities.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.2
 */
public class CourtRoomComplexValue extends CourtRoomBasicValue {
	private static final long serialVersionUID =  5349193902567343270L;
    private CourtSiteBasicValue courtSite = null;

    /**
     * Default constructor
     */
    public CourtRoomComplexValue() {
    }

    /**
     * Initialises the Id and version number
     * 
     * @param id
     * @param version
     */
    public CourtRoomComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Returns the Court Site (Basic) value.
     * 
     * @return CourtSiteBasicValue
     */
    public CourtSiteBasicValue getCourtSite() {
        return this.courtSite;
    }

    /**
     * Sets the CourtSite (Basic) value.
     * 
     * @param newValue
     *            CourtSiteBasicValue
     */
    public void setCourtSite(CourtSiteBasicValue newValue) {
        this.courtSite = newValue;
    }

}