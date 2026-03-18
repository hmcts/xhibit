package uk.gov.courtservice.xhibit.client.counselfacilities;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: FindLegalRepresentativeTableRowModel
 * </p>
 * <p>
 * Description: The row model for finding a legal representative
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

public class FindLegalRepresentativeTableRowModel {
    private Integer legalRepid;

    private String fullName;

    private String firstName;

    private String surname;

    private String chambersName;

    private String addressLine01;

    private String addressLine02;

    private String town;

    private String county;

    private String postCode;

    private Integer chambersId;

    private String legalRepType;

    private String title;
    
    private String defenceCategory;

    public FindLegalRepresentativeTableRowModel() {
        // empty
    }
    
    public FindLegalRepresentativeTableRowModel(FindLegalRepresentativeTableRowModel trm) {
        this.setAddressLine01(trm.getAddressLine01());
        this.setAddressLine02(trm.getAddressLine02());
        this.setChambersId(trm.getChambersId());
        this.setChambersName(trm.getChambersName());
        this.setCounty(trm.getCounty());
        this.setDefenceCategory(trm.getDefenceCategory());
        this.setFirstName(trm.getFirstName());
        this.setFullName(trm.getFullName());
        this.setLegalRepId(trm.getLegalRepId());
        this.setLegalRepType(trm.getLegalRepType());
        this.setPostCode(trm.getPostCode());
        this.setSurname(trm.getSurname());
        this.setTitle(trm.getTitle());
        this.setTown(trm.getTown());
    }

    /**
     * Get method that concatenates fullname, firstname and surname
     * 
     * @return String
     */
    public String getFullName() {
        return (CounselFacilitiesHelper.getFullName(fullName, new String[] { getTitle(), getFirstName(), getSurname() }));
    }

    // getters
    public Integer getLegalRepId() {
        return legalRepid;
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

    public Integer getChambersId() {
        return chambersId;
    }

    public String getLegalRepType() {
        return legalRepType;
    }

    public String getTitle() {
        return (CounselFacilitiesHelper.tidyUp(title));
    }

    public String getDefenceCategory() {
        return defenceCategory;
    }
    
    // setters
    public void setFullName(String param) {
        fullName = param;
    }

    public void setLegalRepId(Integer param) {
        legalRepid = param;
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
        chambersId = param;
    }

    public void setLegalRepType(String param) {
        legalRepType = param;
    }

    public void setTitle(String param) {
        title = param;
    }
    
    public void setDefenceCategory(String param) {
        defenceCategory = param;
    }

    public void printModel() {
        XHIBITConstant.info("FindLegalRepresentativeTableRowModel");
        XHIBITConstant.info("------------------------------------");
        XHIBITConstant.info("LegalRepId     : " + getLegalRepId());
        XHIBITConstant.info("FullName       : " + getFullName());
        XHIBITConstant.info("Title          : " + getTitle());
        XHIBITConstant.info("FirstName      : " + getFirstName());
        XHIBITConstant.info("Surname        : " + getSurname());
        XHIBITConstant.info("Chambers       : " + getChambersName());
        XHIBITConstant.info("AddressLine01  : " + getAddressLine01());
        XHIBITConstant.info("AddressLine02  : " + getAddressLine02());
        XHIBITConstant.info("Town           : " + getTown());
        XHIBITConstant.info("County         : " + getCounty());
        XHIBITConstant.info("PostCode       : " + getPostCode());
        XHIBITConstant.info("ChambersId     : " + getChambersId());
        XHIBITConstant.info("LegalRepType   : " + getLegalRepType());
        XHIBITConstant.info("DefenceCategory: " + getDefenceCategory());
    }
}
