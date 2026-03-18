package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Chamber Basic Value.
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
public class RefChamberBasicValue extends CSAbstractValue {

    private Integer addressId = null;

    private Integer courtId = null;

    private Integer crestChamberId = null;

    private String dxRef = null;

    private String firmName = null;

    private String isGlobal = null;

    private String locationCode = null;

    private String obsInd = null;

    private Integer version = null;
    
    private String clerkName = null;
    
    private static final long serialVersionUID = 7365611394631726379L;

    /**
     * Default constructor.
     */
    public RefChamberBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefChamberBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param crestChamberId
     * @param addressId
     * @param xhbVersion
     * @param obsInd
     * @param isGlobal
     * @param dxRef
     * @param locationCode
     * @param firmName
     */
    public RefChamberBasicValue(Integer id, Integer version, Integer crestChamberId, Integer addressId, String obsInd,
            String isGlobal, String dxRef, String locationCode, String firmName, Integer courtId, String clerkName) {

        this(id, version);
        this.addressId = addressId;
        this.courtId = courtId;
        this.dxRef = dxRef;
        this.firmName = firmName;
        this.isGlobal = isGlobal;
        this.locationCode = locationCode;
        this.obsInd = obsInd;
        this.version = version;
        this.clerkName = clerkName;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setIsGlobal(String isGlobal) {
        this.isGlobal = isGlobal;
    }

    public String getIsGlobal() {
        return isGlobal;
    }

    public void setDxRef(String dxRef) {
        this.dxRef = dxRef;
    }

    public String getDxRef() {
        return dxRef;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setCrestChamberId(Integer crestChamberId) {
        this.crestChamberId = crestChamberId;
    }

    public Integer getCrestChamberId() {
        return crestChamberId;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return courtId;
    }
    
    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    public String getClerkName() {
        return clerkName;
    }
}