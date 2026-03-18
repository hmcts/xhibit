package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Court Basic Value.
 * <p>
 * Not to be confused with RefCourt.
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
 * @version 1.1
 * 
 *  @amended groenmg 02-07-18
 * CTX-1976 - default time marking and time listed correctly. Add court start time
 */
public class CourtBasicValue extends CSAbstractValue {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6157458303832092119L;

	private String courtType;

    private String circuit;

    private String courtName;

    private String crestCourtId;
    
    private Integer addressId;

    private String courtPrefix;

    private String shortName;

    private String displayName;

    private String obsInd;

    private String courtCode;
    
    private Integer policeForceCode;
    
    private String flRepSort;
    
    private String courtStartTime;
    
    private String wlRepSort; 
    
    private Integer wlRepPeriod;
    
    private String wlRepTime;
    
    private String wlFreeText;
    
    private String dxRef;
    
    private String countyLocCode;
    
    private String tier;

    /**
     * Default constructor
     */
    public CourtBasicValue() {
    }

    /**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public CourtBasicValue(Integer id, Integer version) {
        super(id, version);
    }

	/**
	 * @return the courtType
	 */
	public String getCourtType() {
		return courtType;
	}

	/**
	 * @param courtType the courtType to set
	 */
	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}

	/**
	 * @return the circuit
	 */
	public String getCircuit() {
		return circuit;
	}

	/**
	 * @param circuit the circuit to set
	 */
	public void setCircuit(String circuit) {
		this.circuit = circuit;
	}

	/**
	 * @return the courtName
	 */
	public String getCourtName() {
		return courtName;
	}

	/**
	 * @param courtName the courtName to set
	 */
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	/**
	 * @return the crestCourtId
	 */
	public String getCrestCourtId() {
		return crestCourtId;
	}

	/**
	 * @param crestCourtId the crestCourtId to set
	 */
	public void setCrestCourtId(String crestCourtId) {
		this.crestCourtId = crestCourtId;
	}

	/**
	 * @return the addressId
	 */
	public Integer getAddressId() {
		return addressId;
	}

	/**
	 * @param addressId the addressId to set
	 */
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	/**
	 * @return the courtPrefix
	 */
	public String getCourtPrefix() {
		return courtPrefix;
	}

	/**
	 * @param courtPrefix the courtPrefix to set
	 */
	public void setCourtPrefix(String courtPrefix) {
		this.courtPrefix = courtPrefix;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the obsInd
	 */
	public String getObsInd() {
		return obsInd;
	}

	/**
	 * @param obsInd the obsInd to set
	 */
	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}

	/**
	 * @return the courtCode
	 */
	public String getCourtCode() {
		return courtCode;
	}

	/**
	 * @param courtCode the courtCode to set
	 */
	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}

	/**
	 * @return the policeForceCode
	 */
	public Integer getPoliceForceCode() {
		return policeForceCode;
	}

	/**
	 * @param policeForceCode the policeForceCode to set
	 */
	public void setPoliceForceCode(Integer policeForceCode) {
		this.policeForceCode = policeForceCode;
	}

	/**
	 * @return the flRepSort
	 */
	public String getFlRepSort() {
		return flRepSort;
	}

	/**
	 * @param flRepSort the flRepSort to set
	 */
	public void setFlRepSort(String flRepSort) {
		this.flRepSort = flRepSort;
	}

	/**
	 * @return the courtStartTime
	 */
	public String getCourtStartTime() {
		return courtStartTime;
	}

	/**
	 * @param courtStartTime the courtStartTime to set
	 */
	public void setCourtStartTime(String courtStartTime) {
		this.courtStartTime = courtStartTime;
	}

	/**
	 * @return the wlRepSort
	 */
	public String getWlRepSort() {
		return wlRepSort;
	}

	/**
	 * @param wlRepSort the wlRepSort to set
	 */
	public void setWlRepSort(String wlRepSort) {
		this.wlRepSort = wlRepSort;
	}

	/**
	 * @return the wlRepPeriod
	 */
	public Integer getWlRepPeriod() {
		return wlRepPeriod;
	}

	/**
	 * @param wlRepPeriod the wlRepPeriod to set
	 */
	public void setWlRepPeriod(Integer wlRepPeriod) {
		this.wlRepPeriod = wlRepPeriod;
	}

	/**
	 * @return the wlRepTime
	 */
	public String getWlRepTime() {
		return wlRepTime;
	}

	/**
	 * @param wlRepTime the wlRepTime to set
	 */
	public void setWlRepTime(String wlRepTime) {
		this.wlRepTime = wlRepTime;
	}

	/**
	 * @return the wlFreeText
	 */
	public String getWlFreeText() {
		return wlFreeText;
	}

	/**
	 * @param wlFreeText the wlFreeText to set
	 */
	public void setWlFreeText(String wlFreeText) {
		this.wlFreeText = wlFreeText;
	}

	/**
	 * @return the dxRef
	 */
	public String getDxRef() {
		return dxRef;
	}

	/**
	 * @param dxRef the dxRef to set
	 */
	public void setDxRef(String dxRef) {
		this.dxRef = dxRef;
	}

	/**
	 * @return the countyLocCode
	 */
	public String getCountyLocCode() {
		return countyLocCode;
	}

	/**
	 * @param countyLocCode the countyLocCode to set
	 */
	public void setCountyLocCode(String countyLocCode) {
		this.countyLocCode = countyLocCode;
	}

	/**
	 * @return the tier
	 */
	public String getTier() {
		return tier;
	}

	/**
	 * @param tier the tier to set
	 */
	public void setTier(String tier) {
		this.tier = tier;
	}  
}