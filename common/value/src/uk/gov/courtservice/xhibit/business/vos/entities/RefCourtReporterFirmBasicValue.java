package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * RefCourtReporterFirmBasicValue.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.0
 */
public class RefCourtReporterFirmBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = -7773613894547697355L;
	
	private Integer addressId = null;

    private Integer courtId = null;

    private Integer crestCourtReporterFirmId = null;

    private String displayFirst = null;

    private String dxRef = null;

    private String firmName = null;

    private String obsInd = null;

    private String vatNo = null;

    /**
     * Default constructor.
     */
    public RefCourtReporterFirmBasicValue() {
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefCourtReporterFirmBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param obsInd
     * @param displayFirst
     * @param dxRef
     * @param vatNo
     * @param firmName
     */
    public RefCourtReporterFirmBasicValue(Integer id, Integer version, String obsInd, String displayFirst,
            String dxRef, String vatNo, String firmName, Integer addressId, Integer courtId,
            Integer crestCourtReporterFirmId) {

        this(id, version);
        this.obsInd = obsInd;
        this.displayFirst = displayFirst;
        this.dxRef = dxRef;
        this.vatNo = vatNo;
        this.firmName = firmName;
        this.addressId = addressId;
        this.courtId = courtId;
        this.crestCourtReporterFirmId = crestCourtReporterFirmId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
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
     * @param displayFirst
     */
    public void setDisplayFirst(String displayFirst) {
        this.displayFirst = displayFirst;
    }

    /**
     * 
     * @return
     */
    public String getDisplayFirst() {
        return displayFirst;
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
     * @param firmName
     */
    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    /**
     * 
     * @return
     */
    public String getFirmName() {
        return firmName;
    }

    public void setCrestCourtReporterFirmId(Integer crestCourtReporterFirmId) {
        this.crestCourtReporterFirmId = crestCourtReporterFirmId;
    }

    public Integer getCrestCourtReporterFirmId() {
        return crestCourtReporterFirmId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return courtId;
    }
}