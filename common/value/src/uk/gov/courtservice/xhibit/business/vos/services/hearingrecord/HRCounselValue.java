package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;

/**
 * Value object to store the counsel information required on a CREST form 'A'.
 * This is a read-only object.
 *
 * From refLegalRepresentative on System CMR
 *
 */

/**
 * <p>
 * Title: HRCounselValue
 * </p>
 * <p>
 * Description: Value object to store the counsel information required on a
 * CREST form 'A'. This is a read-only object. This is a mix of RefLegReps and
 * solicitor/advocate data including chamber/solicitorfirm
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class HRCounselValue implements HRValueObject {

    private static final long serialVersionUID = 1L;

    private Integer refLegalRepID;

    private String firstName;

    private String middleName;

    private String surname;

    private String legalRepType;

    private String legalRole; // use refLegRepID on SHLegRep to determine this

    private Integer refAdvocateOrSolicitorID; // generalised name for

    // refAdvocateID or
    // refSolicitorID

    private Integer refChamberOrSolicitorFirmID; // generalised name for

    // refChamberID or
    // refSolicitorFirmID

    private AddressBasicValue addressBasicValue;

    private Integer barNumber;

    private String solFirmOrRefLegalRep; // Solicitor(S), Barrister(L) or

    // "S" if substitute, "I" if instructed advcoate.  Applies to Barristers only.
    private String substituteOrInstructed;
    
    private Integer substitutedRefLegalRepId;
    
    // In Person(I)

    private HRStartEndDates[] startEndDatesArray;
    
    private Integer instructedAdvocateRefLegalRepId;
    
    

    public HRCounselValue(Integer rlrID) {
        this.refLegalRepID = rlrID;
    }

    public HRCounselValue() {
        // empty
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLegalRepType() {
        return legalRepType;
    }

    public String getLegalRole() {
        return legalRole;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Integer getRefLegalRepID() {
        return refLegalRepID;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getBarNumber() {
        return this.barNumber;
    }

    public HRStartEndDates[] getStartEndDatesArray() {
        return startEndDatesArray;
    }

    public String getSubstituteOrInstructed() {
        return substituteOrInstructed;
    }
    
    public Integer getSubstitutedRefLegalRepId() {
        return substitutedRefLegalRepId;
    }
    
    public Integer getInstructedAdvocateRefLegalRepId() {
        return this.instructedAdvocateRefLegalRepId;
    }
    
    public void setRefLegalRepID(Integer id) {
        this.refLegalRepID = id;
    }

    public void setBarNumber(Integer barNumber) {
        this.barNumber = barNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLegalRepType(String legalRepType) {
        this.legalRepType = legalRepType;
    }

    public void setLegalRole(String legalRole) {
        this.legalRole = legalRole;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getRefAdvocateOrSolicitorID() {
        return refAdvocateOrSolicitorID;
    }

    public Integer getRefChamberOrSolicitorFirmID() {
        return refChamberOrSolicitorFirmID;
    }

    public void setRefAdvocateOrSolicitorID(Integer refAdvocateOrSolicitorID) {
        this.refAdvocateOrSolicitorID = refAdvocateOrSolicitorID;
    }

    public void setRefChamberOrSolicitorFirmID(Integer refChamberOrSolicitorFirmID) {
        this.refChamberOrSolicitorFirmID = refChamberOrSolicitorFirmID;
    }

    public AddressBasicValue getAddressBasicValue() {
        return addressBasicValue;
    }

    public void setAddressBasicValue(AddressBasicValue addressBasicValue) {
        this.addressBasicValue = addressBasicValue;
    }

    public String getSolFirmOrRefLegalRep() {
        return solFirmOrRefLegalRep;
    }

    public void setSolFirmOrRefLegalRep(String solFirmOrRefLegalRep) {
        this.solFirmOrRefLegalRep = solFirmOrRefLegalRep;
    }

    public void setStartEndDatesArray(HRStartEndDates[] startEndDatesArray) {
        this.startEndDatesArray = startEndDatesArray;
    }
    
    public void setSubstituteOrInstructed(String substituteOrInstructed) {
        this.substituteOrInstructed = substituteOrInstructed;
    }
    
    public void setSubstitutedRefLegalRepId(Integer substitutedRefLegalRepId) {
        this.substitutedRefLegalRepId = substitutedRefLegalRepId;
    }
    
    public void setInstructedAdvocateRefLegalRepId(Integer instructedAdvocateRefLegalRepId) {
        this.instructedAdvocateRefLegalRepId = instructedAdvocateRefLegalRepId;
    }
    
}
