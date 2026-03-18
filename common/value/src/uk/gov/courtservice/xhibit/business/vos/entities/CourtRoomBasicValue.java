package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Court Room Basic Value.
 * <p>
 * Returns IDs of related Entities.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2002, 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.2
 */
public class CourtRoomBasicValue extends CSAbstractValue {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2050707464817393999L;

	private Integer courtSiteId = null;

    private String courtRoomName = null;

    private Integer crestCourtRoomNo = null;

    private String description = null;

    private String location = null;

    private String displayName;

    private String obsInd;
    
    private String securityInd;
    
    private String videoInd;

    /**
     * Default constructor
     */
    public CourtRoomBasicValue() {
    }

    /**
     * Default constructor
     * 
     */
    public CourtRoomBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    public String getCourtRoomName() {
        return this.courtRoomName;
    }

    public Integer getCourtSiteId() {
        return this.courtSiteId;
    }

    public Integer getCrestCourtRoomNo() {
        return this.crestCourtRoomNo;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public void setCourtSiteId(Integer newValue) {
        this.courtSiteId = newValue;
    }

    public void setCourtRoomName(String newValue) {
        this.courtRoomName = newValue;
    }

    public void setCrestCourtRoomNo(Integer newValue) {
        this.crestCourtRoomNo = newValue;
    }

    public void setDescription(String newValue) {
        this.description = newValue;
    }

    public void setLocation(String newValue) {
        this.location = newValue;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public String getObsInd() {
        return obsInd;
    }
    
    public void setSecurityInd(String securityInd) {
        this.securityInd = securityInd;
    }

    public String getSecurityInd() {
        return securityInd;
    }
    
    public void setVideoInd(String videoInd) {
        this.videoInd = videoInd;
    }

    public String getVideoInd() {
        return videoInd;
    }
    
    public String toString() {

        return " CourtRoomName : " + this.getCourtRoomName() + "  CourtSiteId :" + this.getCourtSiteId()
                + " CrestCourtRoomNo :" + this.getCrestCourtRoomNo() + " Description : " + this.getDescription()
                + " Location " + this.getLocation() + " DisplayName : " + getDisplayName() + " ObsInd :"
                + this.getObsInd() + " SecurityInd : " + this.getSecurityInd() + " VideoInd : " + this.getVideoInd();
    }
}