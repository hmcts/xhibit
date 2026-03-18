package uk.gov.courtservice.xhibit.business.entities.address;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface Address extends CSEntityLocal {

    public Integer getAddressId();

    public String getAddress1();

    public String getAddress2();

    public String getAddress3();

    public String getAddress4();

    public String getCountry();

    public String getCounty();

    public String getPostcode();

    public String getTown();

    public Integer getVersion();

    public void setAddress1(String address1);

    public void setAddress2(String address2);

    public void setAddress3(String address3);

    public void setAddress4(String address4);

    public void setCountry(String country);

    public void setCounty(String county);

    public void setPostcode(String postcode);

    public void setTown(String town);

    public void setVersion(Integer version);
}