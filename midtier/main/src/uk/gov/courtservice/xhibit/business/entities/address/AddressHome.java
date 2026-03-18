package uk.gov.courtservice.xhibit.business.entities.address;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface AddressHome extends javax.ejb.EJBLocalHome {
    public Address create(String address1, String address2, String address3, String address4, String town,
            String county, String postcode, String country, String userDisplayName) throws CreateException;

    public Address findByPrimaryKey(Integer addressId) throws FinderException;
    
}
