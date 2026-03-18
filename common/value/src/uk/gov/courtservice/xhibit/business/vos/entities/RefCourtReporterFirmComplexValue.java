package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * RefCourtReporterFirmComplexValue.
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

public class RefCourtReporterFirmComplexValue extends RefCourtReporterFirmBasicValue {

	private static final long serialVersionUID = -7065647284745387000L;
	private AddressBasicValue address;

    private CourtBasicValue court;

    /**
     * Default Constructor.
     */
    public RefCourtReporterFirmComplexValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefCourtReporterFirmComplexValue(Integer id, Integer version) {
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
    public RefCourtReporterFirmComplexValue(Integer id, Integer version, String obsInd, String displayFirst,
            String dxRef, String vatNo, String firmName, Integer addressId, Integer courtId,
            Integer crestCourtReporterFirmId) {
        super(id, version, obsInd, displayFirst, dxRef, vatNo, firmName, addressId, courtId, crestCourtReporterFirmId);
    }

    public void setAddress(AddressBasicValue newValue) {
        this.address = newValue;
    }

    public AddressBasicValue getAddress() {
        return address;
    }

    public void setCourt(CourtBasicValue newValue) {
        this.court = newValue;
    }

    public CourtBasicValue getCourt() {
        return court;
    }
}