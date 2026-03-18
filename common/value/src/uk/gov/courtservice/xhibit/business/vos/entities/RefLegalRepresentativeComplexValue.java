package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * RefLegalRepresentative Basic Value.
 * 
 * <p>
 * Description:
 * </p>
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
public class RefLegalRepresentativeComplexValue extends RefLegalRepresentativeBasicValue {

	static final long serialVersionUID = 3555653738856269636L;
	
	private CourtBasicValue court = null;

    /**
     * Default constructor.
     * <p>
     * Do not use.
     * </p>
     */
    public RefLegalRepresentativeComplexValue() {
    }

    /**
     * @param id
     * @param version
     */
    public RefLegalRepresentativeComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * @param id
     * @param version
     * @param firstName
     * @param midleName
     * @param surname
     * @param title
     * @param initials
     * @param repType
     * @param refAdvocate
     */
    public RefLegalRepresentativeComplexValue(Integer id, Integer version, String firstName, String middleName,
            String surname, String title, String initials, String repType, Integer courtId) {

        super(id, version, firstName, middleName, surname, title, initials, repType, courtId);
    }

    public CourtBasicValue getCourt() {
        /** @todo lazy initialise */
        return court;
    }

    public void setCourt(CourtBasicValue newValue) {
        this.court = newValue;
    }

}