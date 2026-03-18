package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
public class DefendantComplexValue extends DefendantBasicValue {

	private static final long serialVersionUID = -7658771950529590076L;
	
	private java.util.Collection defendantReferences;

    private java.util.Collection defendantOnCases;

    // genders
    public static final Integer GENDER_COMPANY = new Integer(0);

    public static final Integer GENDER_MALE = new Integer(1);

    public static final Integer GENDER_FEMALE = new Integer(2);

    /**
     * @deprecated superseded by GENDER_COMPANY
     */
    public static final Integer GENDER_NOT_KNOWN = new Integer(0);

    /**
     * @deprecated superseded by GENDER_COMPANY
     */
    public static final Integer GENDER_NOT_SPECIFIED = new Integer(9);

    private String addressValue;

    public DefendantComplexValue() {
    }

    public DefendantComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    public java.util.Collection getDefendantReferences() {
        return defendantReferences;
    }

    public void setDefendantReferences(java.util.Collection defendantReferences) {
        this.defendantReferences = defendantReferences;
    }

    public void setDefendantOnCases(java.util.Collection defendantOnCases) {
        this.defendantOnCases = defendantOnCases;
    }

    public java.util.Collection getDefendantOnCases() {
        return defendantOnCases;
    }

    public void setAddressValue(String addressValue) {
        this.addressValue = addressValue;
    }

    public String getAddressValue() {
        return addressValue;
    }
}