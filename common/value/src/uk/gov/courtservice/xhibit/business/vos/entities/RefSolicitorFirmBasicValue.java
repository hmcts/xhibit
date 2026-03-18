package uk.gov.courtservice.xhibit.business.vos.entities;

//framework
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: RefSolicitorFirmValue
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
 * @author Khanh Tran
 * @version 1.0
 */

public class RefSolicitorFirmBasicValue extends CSAbstractValue {

    private Integer courtId;

    private Integer addressId;

    private Integer crestSofId;

    private String solicitorFirmName;

    private String vatNo;

    private String dxRef;

    private String obsInd;

    private String shortName;
    
    private String laCode;
    
    private String lastUpdatedBy;
    
    private static final long serialVersionUID = -8497116002114613459L;
    /**
     * Default constructor.
     */
    public RefSolicitorFirmBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefSolicitorFirmBasicValue(Integer id, Integer version) {

        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param courtId
     * @param addressId
     * @param crestSofId
     * @param solicitorFirmName
     * @param vatNo
     * @param dxRef
     * @param obsInd
     */
    public RefSolicitorFirmBasicValue(Integer id, Integer version, Integer courtId, Integer addressId,
            Integer crestSofId, String solicitorFirmName, String vatNo, String dxRef, String obsInd, String shortName, String laCode) {

        this(id, version);
        this.courtId = courtId;
        this.addressId = addressId;
        this.crestSofId = crestSofId;
        this.solicitorFirmName = solicitorFirmName;
        this.vatNo = vatNo;
        this.dxRef = dxRef;
        this.obsInd = obsInd;
        this.shortName = shortName;
        this.laCode = laCode;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    /**
     * 
     * @return
     */
    public Integer getAddressId() {
        return addressId;
    }

    /**
     * 
     * @param addressId
     */
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    /**
     * 
     * @return
     */
    public Integer getCrestSofId() {
        return crestSofId;
    }

    /**
     * 
     * @param crestSofId
     */
    public void setCrestSofId(Integer crestSofId) {
        this.crestSofId = crestSofId;
    }

    /**
     * 
     * @return
     */
    public String getSolicitorFirmName() {
        return solicitorFirmName;
    }

    /**
     * 
     * @param solicitorFirmName
     */
    public void setSolicitorFirmName(String solicitorFirmName) {
        this.solicitorFirmName = solicitorFirmName;
    }

    /**
     * 
     * @return
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 
     * @param solicitorFirmName
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * 
     * @param vatNo
     */
    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    /**
     * 
     * @return
     */
    public String getVatNo() {
        return vatNo;
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

	public String getLaCode() {
		return laCode;
	}

	public void setLaCode(String laCode) {
		this.laCode = laCode;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}