package uk.gov.courtservice.xhibit.client.counselfacilities;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: AddLegalRepresentativeModel
 * </p>
 * <p>
 * Description: The model for Add legal Representative
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */

public class AddLegalRepresentativeModel implements Cloneable {
    private String title;

    private String firstName;

    private String surname;

    private String chambers;

    private String addressLine01;

    private String addressLine02;

    private String town;

    private String county;

    private String postCode;

    private Integer chambersId;

    private Integer legalRepId;

    private XhibitApplicationController xac;


    public AddLegalRepresentativeModel() {
        // empty
    }

    // Getters
    public String getTitle() {
        return CounselFacilitiesHelper.tidyUp(title);
    }

    public String getFirstName() {
        return CounselFacilitiesHelper.tidyUp(firstName);
    }

    public String getSurname() {
        return CounselFacilitiesHelper.tidyUp(surname);
    }

    public String getChambers() {
        return CounselFacilitiesHelper.tidyUp(chambers);
    }

    public String getAddressLine01() {
        return CounselFacilitiesHelper.tidyUp(addressLine01);
    }

    public String getAddressLine02() {
        return CounselFacilitiesHelper.tidyUp(addressLine02);
    }

    public String getTown() {
        return CounselFacilitiesHelper.tidyUp(town);
    }

    public String getCounty() {
        return CounselFacilitiesHelper.tidyUp(county);
    }

    public String getPostCode() {
        return CounselFacilitiesHelper.tidyUp(postCode);
    }

    public Integer getChambersId() {
        return chambersId;
    }

    public Integer getLegalRepId() {
        return legalRepId;
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    // Setters
    public void setTitle(String param) {
        title = param;
    }

    public void setFirstName(String param) {
        firstName = param;
    }

    public void setSurname(String param) {
        surname = param;
    }

    public void setChambers(String param) {
        chambers = param;
    }

    public void setAddressLine01(String param) {
        addressLine01 = param;
    }

    public void setAddressLine02(String param) {
        addressLine02 = param;
    }

    public void setTown(String param) {
        town = param;
    }

    public void setCounty(String param) {
        county = param;
    }

    public void setPostCode(String param) {
        postCode = param;
    }

    public void setChambersId(Integer param) {
        chambersId = param;
    }

    public void setLegaRepId(Integer param) {
        legalRepId = param;
    }

    public void setXac(XhibitApplicationController param) {
        xac = param;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("AddLegalRepresentativeModel");
        XHIBITConstant.info("---------------------------");
        XHIBITConstant.info("Title        : " + getTitle());
        XHIBITConstant.info("FirstName    : " + getFirstName());
        XHIBITConstant.info("Surname      : " + getSurname());
        XHIBITConstant.info("Chambers     : " + getChambers());
        XHIBITConstant.info("AddressLine01: " + getAddressLine01());
        XHIBITConstant.info("AddressLine02: " + getAddressLine02());
        XHIBITConstant.info("Town         : " + getTown());
        XHIBITConstant.info("County       : " + getCounty());
        XHIBITConstant.info("PostCode     : " + getPostCode());
        XHIBITConstant.info("ChambersId   : " + getChambersId());
        XHIBITConstant.info("LegalRepId   : " + getLegalRepId());
        XHIBITConstant.info("XAC          : " + getXac());
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setTitle(null);
        setFirstName(null);
        setSurname(null);
        setChambers(null);
        setAddressLine01(null);
        setAddressLine02(null);
        setTown(null);
        setCounty(null);
        setPostCode(null);
        setChambersId(null);
        setLegaRepId(null);
        setXac(null);
    }
}
