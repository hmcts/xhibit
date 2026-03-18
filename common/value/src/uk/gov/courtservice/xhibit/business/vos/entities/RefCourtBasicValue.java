package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * RefCourtComplexValue - the [complex] value object for Court reference data.
 * 
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.1
 */
public class RefCourtBasicValue extends CSAbstractValue {

    private Integer refCourtId = null;

    private String courtFullName = null;

    private String courtShortName = null;

    private String namePrefix = null;

    private String courtType = null;

    private String crestCode = null;

    private String obsInd = null;

    private String dxRef = null;

    private Boolean isPsd = null;

    private Integer addressId = null;

    private Integer courtId = null;
    
    private static final long serialVersionUID = -5454455994675273511L;

    /**
     * Default constructor.
     */
    public RefCourtBasicValue() {
    }

    /**
     * Key constructor.
     */
    public RefCourtBasicValue(Integer key, Integer version) {
        super(key, version);
    }

    /**
     * 
     * @param refCourtId
     * @param courtFullName
     * @param courtShortName
     * @param namePrefix
     * @param courtType
     * @param crestCode
     */
    public RefCourtBasicValue(Integer id, Integer version, String courtFullName, String courtShortName,
            String namePrefix, String courtType, String crestCode, String obsInd, String dxRef, Boolean isPsd,
            Integer addressId, Integer courtId) {

        this(id, version);
        this.courtFullName = courtFullName;
        this.courtShortName = courtShortName;
        this.namePrefix = namePrefix;
        this.courtType = courtType;
        this.crestCode = crestCode;
        this.obsInd = obsInd;
        this.dxRef = dxRef;
        this.isPsd = isPsd;
        this.addressId = addressId;
        this.courtId = courtId;
    }
    
	public RefCourtBasicValue(String courtFullName, String courtShortName, String namePrefix, String courtType,
			String crestCode, String obsInd, String dxRef, Integer addressId, Integer courtId) {

        this.courtFullName = courtFullName;
        this.courtShortName = courtShortName;
        this.namePrefix = namePrefix;
        this.courtType = courtType;
        this.crestCode = crestCode;
        this.obsInd = obsInd;
        this.dxRef = dxRef;
        this.addressId = addressId;
        this.courtId = courtId;
    }

    /**
     * 
     * @param courtFullName
     */
    public void setCourtFullName(String courtFullName) {
        this.courtFullName = courtFullName;
    }

    /**
     * 
     * @return
     */
    public String getCourtFullName() {
        return courtFullName;
    }

    /**
     * 
     * @param courtShortName
     */
    public void setCourtShortName(String courtShortName) {
        this.courtShortName = courtShortName;
    }

    /**
     * 
     * @return
     */
    public String getCourtShortName() {
        return courtShortName;
    }

    /**
     * 
     * @param namePrefix
     */
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    /**
     * 
     * @return
     */
    public String getNamePrefix() {
        return namePrefix;
    }

    /**
     * 
     * @param courtType
     */
    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }

    /**
     * 
     * @return
     */
    public String getCourtType() {
        return courtType;
    }

    /**
     * 
     * @param crestCode
     */
    public void setCrestCode(String crestCode) {
        this.crestCode = crestCode;
    }

    /**
     * 
     * @return
     */
    public String getCrestCode() {
        return crestCode;
    }

    /**
     * 
     * @param obsInd
     */
    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    /**
     * 
     * @return
     */
    public String getObsInd() {
        return obsInd;
    }

    /**
     * 
     * @param dxRef
     */
    public void setDxRef(String dxRef) {
        this.dxRef = dxRef;
    }

    /**
     * 
     * @return
     */
    public String getDxRef() {
        return dxRef;
    }

    /**
     * 
     * @param isPsd
     *            Boolean
     */
    public void setIsPsd(Boolean isPsd) {
        this.isPsd = isPsd;
    }

    // bug fix isPsd is represented in the database as a varchar
    /**
     * 
     * @param isPsd
     *            Y = True, N = False;
     */
    public void setIsPsd(String isPsd) {
        if (isPsd != null) {
            String val = isPsd.trim();
            if (val.equalsIgnoreCase("Y")) {
                this.isPsd = new Boolean(true);
            } else {
                this.isPsd = new Boolean(false);
            }
        }

    }

    /**
     * 
     * @return Boolean
     */
    public Boolean getIsPsd() {
        return this.isPsd;
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
}