package uk.gov.courtservice.xhibit.business.entities.address;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class AddressBean extends CSEntityBean {

    public Integer ejbCreate(String address1, String address2, String address3, String address4, String town,
            String county, String postcode, String country, String userDisplayName) throws CreateException {
        setAddress1(address1);
        setAddress2(address2);
        setAddress3(address3);
        setAddress4(address4);
        setTown(town);
        setCounty(county);
        setPostcode(postcode);
        setCountry(country);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String address1, String address2, String address3, String address4, String town,
            String county, String postcode, String country, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract Integer getAddressId();

    public abstract String getAddress1();

    public abstract String getAddress2();

    public abstract String getAddress3();

    public abstract String getAddress4();

    public abstract String getTown();

    public abstract String getCounty();

    public abstract String getPostcode();

    public abstract String getCountry();

    public abstract String getCreatedBy();

    public abstract String getLastUpdatedBy();

    public abstract Integer getVersion();

    public abstract void setAddressId(Integer addressId);

    public abstract void setAddress1(String address1);

    public abstract void setAddress2(String address2);

    public abstract void setAddress3(String address3);

    public abstract void setAddress4(String address4);

    public abstract void setTown(String town);

    public abstract void setCounty(String county);

    public abstract void setPostcode(String postcode);

    public abstract void setCountry(String country);

    public abstract void setCreatedBy(String createdBy);

    public abstract void setLastUpdatedBy(String lastUpdatedBy);

    public abstract void setVersion(Integer version);
}