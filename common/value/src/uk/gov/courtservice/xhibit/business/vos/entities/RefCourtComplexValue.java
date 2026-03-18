package uk.gov.courtservice.xhibit.business.vos.entities;

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
public class RefCourtComplexValue extends RefCourtBasicValue {

	private static final long serialVersionUID = -6280658651995821824L;
	private AddressBasicValue address = null;

    private CourtBasicValue court = null;

    /**
     * Default constructor.
     */
    public RefCourtComplexValue() {
    }

    /**
     * Key constructor.
     */
    public RefCourtComplexValue(Integer key, Integer version) {
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
    public RefCourtComplexValue(Integer key, Integer version, String courtFullName, String courtShortName,
            String namePrefix, String courtType, String crestCode, String obsInd, String dxRef, Boolean isPsd,
            Integer addressId, Integer courtId) {

        super(key, version, courtFullName, courtShortName, namePrefix, courtType, crestCode, obsInd, dxRef, isPsd,
                addressId, courtId);
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