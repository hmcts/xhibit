package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class AddressBasicValue extends CSAbstractValue {

    private Integer addressId;

    private String address1;

    private String address2;

    private String address3;

    private String address4;

    private String town;

    private String county;

    private String postcode;

    private String country;
    
    private static final long serialVersionUID = 4822426650835426438L;

    public AddressBasicValue() {
    }

    public AddressBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress4() {
        return address4;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTown() {
        return town;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCounty() {
        return county;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

}