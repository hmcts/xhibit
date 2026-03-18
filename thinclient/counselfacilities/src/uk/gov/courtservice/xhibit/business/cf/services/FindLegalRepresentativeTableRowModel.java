package uk.gov.courtservice.xhibit.business.cf.services;

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
 * @author unascribed
 * @version 1.0
 */

public class FindLegalRepresentativeTableRowModel implements java.io.Serializable {
    public String legalRepId;

    public String fullName;

    public String firstName;

    public String surname;

    public String chambersName;

    public String addressLine01;

    public String addressLine02;

    public String town;

    public String county;

    public String postCode;

    public String chambersId;

    public String legalRepType;

    public FindLegalRepresentativeTableRowModel() {
    }

    public String getFullName() {
        return (CounselFacilitiesHelper.getFullName(fullName, getFirstName(), getSurname()));
    }

    public String getLegalRepId() {
        return legalRepId;
    }

    public String getFirstName() {
        return (CounselFacilitiesHelper.tidyUp(firstName));
    }

    public String getSurname() {
        return (CounselFacilitiesHelper.tidyUp(surname));
    }

    public String getChambersName() {
        return (CounselFacilitiesHelper.tidyUp(chambersName));
    }

    public String getAddressLine01() {
        return (CounselFacilitiesHelper.tidyUp(addressLine01));
    }

    public String getAddressLine02() {
        return (CounselFacilitiesHelper.tidyUp(addressLine02));
    }

    public String getTown() {
        return (CounselFacilitiesHelper.tidyUp(town));
    }

    public String getCounty() {
        return (CounselFacilitiesHelper.tidyUp(county));
    }

    public String getPostCode() {
        return (CounselFacilitiesHelper.tidyUp(postCode));
    }

    public String getChambersId() {
        return chambersId;
    }

    public String getLegalRepType() {
        return legalRepType;
    }

    public void setFullName(String param) {
        fullName = param;
    }

    public void setLegalRepId(Integer param) {
        legalRepId = param == null ? "" : param.toString();
    }

    public void setFirstName(String param) {
        firstName = param;
    }

    public void setSurname(String param) {
        surname = param;
    }

    public void setChambersName(String param) {
        chambersName = param;
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
        chambersId = param == null ? "" : param.toString();
    }

    public void setLegalRepType(String param) {
        legalRepType = param;
    }

}
