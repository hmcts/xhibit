package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

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
 * @version 1.0
 */
public class RefLegalRepresentativeBasicValue extends CSAbstractValue {

	static final long serialVersionUID = 3579372174827132013L;
	
	private String firstName = null;

    private String middleName = null;

    private String surname = null;

    private String title = null;

    private String initials = null;

    private String legalRepType = null;

    private Integer courtId = null;

    private String obsInd = null;

    /**
     * Default constructor.
     */
    public RefLegalRepresentativeBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefLegalRepresentativeBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param firstName
     * @param midleName
     * @param surname
     * @param title
     * @param initials
     * @param legalRepType
     * @param refAdvocate
     */
    public RefLegalRepresentativeBasicValue(Integer id, Integer version, String firstName, String middleName,
            String surname, String title, String initials, String legalRepType, Integer courtId) {
        this(id, version);
        this.courtId = courtId;
        this.firstName = firstName;
        this.initials = initials;
        this.legalRepType = legalRepType;
        this.middleName = middleName;
        this.surname = surname;
        this.title = title;
    }

    public void setCourtId(Integer newValue) {
        this.courtId = newValue;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String newValue) {
        this.firstName = newValue;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String newValue) {
        this.middleName = newValue;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String newValue) {
        this.surname = newValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newValue) {
        this.title = newValue;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String newValue) {
        this.initials = newValue;
    }

    public String getLegalRepType() {
        return legalRepType;
    }

    public void setLegalRepType(String newValue) {
        this.legalRepType = newValue;
    }

    public void setObsInd(String newValue) {
        this.obsInd = newValue;
    }

    public String getObsInd() {
        return obsInd;
    }
}