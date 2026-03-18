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
public class DefendantOnCaseComplexValue extends DefendantOnCaseBasicValue {

	private static final long serialVersionUID = -8299052972023251391L;
	
	private DefendantBasicValue defendant;

    private java.util.Collection schedHearingDefendants;

    private java.util.Collection defendantOnCharges;

    private java.util.Collection defendantOnOffences;

    private AddressBasicValue address;

    public DefendantOnCaseComplexValue() {
    }

    public DefendantOnCaseComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    public DefendantBasicValue getDefendant() {
        return defendant;
    }

    public void setDefendant(DefendantBasicValue defendant) {
        this.defendant = defendant;
    }

    public void setSchedHearingDefendants(java.util.Collection schedHearingDefendants) {
        this.schedHearingDefendants = schedHearingDefendants;
    }

    public java.util.Collection getSchedHearingDefendants() {
        return schedHearingDefendants;
    }

    public void setDefendantOnCharges(java.util.Collection defendantOnCharges) {
        this.defendantOnCharges = defendantOnCharges;
    }

    public java.util.Collection getDefendantOnCharges() {
        return defendantOnCharges;
    }

    public void setDefendantOnOffences(java.util.Collection defendantOnOffences) {
        this.defendantOnOffences = defendantOnOffences;
    }

    public java.util.Collection getDefendantOnOffences() {
        return defendantOnOffences;
    }

    public void setAddress(AddressBasicValue address) {
        this.address = address;
    }

    public AddressBasicValue getAddress() {
        return address;
    }

}
