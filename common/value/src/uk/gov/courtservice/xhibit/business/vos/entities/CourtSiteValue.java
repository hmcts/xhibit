package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CourtSiteValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version 1.0
 * @deprecated - This version is not up to date with the DB. Use
 *             Basic/ComplexValue instead.
 */

public class CourtSiteValue extends CSAbstractValue {

	private static final long serialVersionUID = -8644955169397656760L;
	// Name
    private String courtSiteName;

    // Site code
    private String courtSiteCode;

    // Address
    private Integer addressId;

    // Id
    private Integer courtSiteId;

    /**
     * Default constructor
     */
    public CourtSiteValue() {
    }

    /**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public CourtSiteValue(Integer id, Integer version) {
        super(id, version);
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
     * Sets the name
     * 
     * @param val
     */
    public void setCourtSiteName(String val) {
        courtSiteName = val;
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
     * Sets the code
     * 
     * @param val
     */
    public void setCourtSiteCode(String val) {
        courtSiteCode = val;
    }

    /**
     * Get address id
     * 
     * @deprecated Should use CMR
     * @return
     */
    public Integer getAddressId() {
        return addressId;
    }

    /**
     * Sets address id
     * 
     * @deprectaed Should be using CMR
     * @param val
     */
    public void setAddressId(Integer val) {
        addressId = val;
    }

    /**
     * Gets the site id
     * 
     * @deprecated Call getId instead
     * @return
     */
    public Integer getCourtSiteId() {
        return new Integer(-1);
    }

    /**
     * Sets the site id
     * 
     * @deprecated Ids are immutable
     * @param val
     */
    public void setCourtSiteId(Integer val) {
    }

}