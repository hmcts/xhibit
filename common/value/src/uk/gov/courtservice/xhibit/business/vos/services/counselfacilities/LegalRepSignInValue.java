package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities;

/**
 * <p>Title: LegalRepSignInValue</p>
 * <p>Description: Value Object for representing a Legal Representative display data in for counsel sign in.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Ian Hannaford, Marie Holmberg
 *
 * <Change History/>
 *
 * <P>27/06/03  Ian Hannaford - First issue.</P>
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class LegalRepSignInValue extends CSAbstractValue {

    private Integer shLegRepId;

    private String legalRole;

    private Integer refLegalRepId;

    private String legRepFirstName;

    private String legRepMiddleName;

    private String legRepSurname;

    private String legRepTitle;

    private String legRepInitials;

    private String legalRepType;

    private String solFirmOrRefLegalRep;

    // Advocate specific information
    private Integer refAdvocateId;

    private Integer refChamberId;

    private String chamberFirmName;

    // solicitor specific information
    private Integer solicitorId;

    private Integer refSolicitorFirmId;

    // if this value is set to 'I' the defendant\appellant is representing
    // himself\herself,
    // in this case the other (non-mandatory) details will not be populated
    private String solicitorFirmName;

    // address information
    private Integer addressId;

    private String address1;

    private String address2;

    private String address3;

    private String address4;

    private String town;

    private String county;

    private String country;

    private String postcode;
    private static final long serialVersionUID = -8744474622690257589L;

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getAddress4() {
        return address4;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public String getChamberFirmName() {
        return chamberFirmName;
    }

    public String getCountry() {
        return country;
    }

    public String getCounty() {
        return county;
    }

    public String getLegalRepType() {
        return legalRepType;
    }

    public String getLegalRole() {
        return legalRole;
    }

    public String getLegRepFirstName() {
        return legRepFirstName;
    }

    public String getLegRepInitials() {
        return legRepInitials;
    }

    public String getLegRepMiddleName() {
        return legRepMiddleName;
    }

    public String getLegRepSurname() {
        return legRepSurname;
    }

    public String getLegRepTitle() {
        return legRepTitle;
    }

    public String getPostcode() {
        return postcode;
    }

    public Integer getRefAdvocateId() {
        return refAdvocateId;
    }

    public Integer getRefChamberId() {
        return refChamberId;
    }

    public Integer getRefLegalRepId() {
        return refLegalRepId;
    }

    public Integer getRefSolicitorFirmId() {
        return refSolicitorFirmId;
    }

    public String getSolFirmOrRefLegalRep() {
        return solFirmOrRefLegalRep;
    }

    public Integer getShLegRepId() {
        return shLegRepId;
    }

    public String getSolicitorFirmName() {
        return solicitorFirmName;
    }

    public Integer getSolicitorId() {
        return solicitorId;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setSolicitorId(Integer solicitorId) {
        this.solicitorId = solicitorId;
    }

    public void setShLegRepId(Integer shLegRepId) {
        this.shLegRepId = shLegRepId;
    }

    public void setSolicitorFirmName(String solicitorFirmName) {
        this.solicitorFirmName = solicitorFirmName;
    }

    public void setRefSolicitorFirmId(Integer refSolicitorFirmId) {
        this.refSolicitorFirmId = refSolicitorFirmId;
    }

    public void setSolFirmOrRefLegalRep(String solFirmOrRefLegalRep) {
        this.solFirmOrRefLegalRep = solFirmOrRefLegalRep;
    }

    public void setRefLegalRepId(Integer refLegalRepId) {
        this.refLegalRepId = refLegalRepId;
    }

    public void setRefChamberId(Integer refChamberId) {
        this.refChamberId = refChamberId;
    }

    public void setRefAdvocateId(Integer refAdvocateId) {
        this.refAdvocateId = refAdvocateId;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setLegRepTitle(String legRepTitle) {
        this.legRepTitle = legRepTitle;
    }

    public void setLegRepSurname(String legRepSurname) {
        this.legRepSurname = legRepSurname;
    }

    public void setLegRepMiddleName(String legRepMiddleName) {
        this.legRepMiddleName = legRepMiddleName;
    }

    public void setLegRepInitials(String legRepInitials) {
        this.legRepInitials = legRepInitials;
    }

    public void setLegRepFirstName(String legRepFirstName) {
        this.legRepFirstName = legRepFirstName;
    }

    public void setLegalRole(String legalRole) {
        this.legalRole = legalRole;
    }

    public void setLegalRepType(String legalRepType) {
        this.legalRepType = legalRepType;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setChamberFirmName(String chamberFirmName) {
        this.chamberFirmName = chamberFirmName;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }
}
