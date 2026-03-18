package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Court Site Basic Value.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.1
 */
public class CourtSiteBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = -5435571916127830333L;

	private String courtSiteName = null;

    private String courtSiteCode = null;

    private Integer addressId = null;

    private Integer courtId = null;

    private String displayName;

    private String obsInd;

    private String shortName;
    
    private String crestCourtId;
    
    private String floaterText;
    
    private String listName;
    
    private Integer siteGroup;
    
    private String tier;

    // The ID of the related Court (NOT RefCourt; that is obtainable from
    // Court).

    /**
     * Default constructor
     */
    public CourtSiteBasicValue() {
    }

    /**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public CourtSiteBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Get address id
     * 
     * @return
     */
    public Integer getAddressId() {
        return addressId;
    }

    /**
     * Gets the Court Id
     * 
     * @return Integer
     */
    public Integer getCourtId() {
        return this.courtId;
    }

    /**
     * Gets code
     * 
     * @return
     */
    public String getCourtSiteCode() {
        return courtSiteCode;
    }

    /**
     * Gets the name
     * 
     * @return
     */
    public String getCourtSiteName() {
        return courtSiteName;
    }

    /**
     * Set the related address Id
     * 
     * @param newValue
     */
    public void setAddressId(Integer newValue) {
        addressId = newValue;
    }

    /**
     * Sets the related Court id
     * 
     * @param newValue
     *            Integer
     */
    public void setCourtId(Integer newValue) {
        this.courtId = newValue;
    }

    /**
     * Sets the code
     * 
     * @param newValue
     */
    public void setCourtSiteCode(String newValue) {
        courtSiteCode = newValue;
    }

    /**
     * Sets the name
     * 
     * @param newValue
     */
    public void setCourtSiteName(String newValue) {
        courtSiteName = newValue;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    public String getCrestCourtId() {
    	return crestCourtId;
    }
    
    public void setCrestCourtId(String crestCourtId) {
    	this.crestCourtId = crestCourtId;
    }
    
    public String getFloaterText() {
    	return floaterText;
    }

    public void setFloaterText(String floaterText) {
    	this.floaterText = floaterText;
    }
    
    public String getListName() {
    	return listName;
    }
    
    public void setListName(String listName) {
    	this.listName = listName;
    }
    
    public Integer getSiteGroup() {
    	return siteGroup;
    }
    
    public void setSiteGroup(Integer siteGroup) {
    	this.siteGroup = siteGroup;
    }
    
    public String getTier() {
    	return tier;
    }
    
    public void setTier(String tier) {
    	this.tier = tier;
    }
}