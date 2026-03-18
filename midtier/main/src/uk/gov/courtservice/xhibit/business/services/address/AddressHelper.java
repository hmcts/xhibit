package uk.gov.courtservice.xhibit.business.services.address;

import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;

/**
 * Helper class to assist with Address processing.
 * 
 * @author Simon Gilmore
 */
public class AddressHelper {
    
    private AddressHelper() {
        // private constructor
    }

    /**
     * Gets an AddressValue for the given address id
     * @param addressId the id of an address
     * @return AddressValue
     */
    public static AddressValue getAddressValue(Integer addressId) {
        if (addressId == null) {
            return null;
        }
        XhbAddress address = XhbAddressBeanHelper2.findByPrimaryKey(addressId);
        return createAddressValue(address);
    }

    /**
     * Gets an AddressValue for the given XhbAddress EJBLocalObject
     * @param address an XhbAddress EJBLocalObject
     * @return AddressValue
     */
    public static AddressValue getAddressValue(XhbAddress address) {
        return createAddressValue(address);
    }

    /**
     * Gets an AddressBasicValue from the given Address EJBLocalObject
     * @param address an Address EJBLocalObject
     * @return AddressBasicValue
     */
    public static AddressBasicValue getAddressBasicValue(Address address) {

        AddressBasicValue value = new AddressBasicValue(address.getAddressId(), address.getVersion());
        value.setAddress1(address.getAddress1());
        value.setAddress2(address.getAddress2());
        value.setAddress3(address.getAddress3());
        value.setAddress4(address.getAddress4());
        value.setCountry(address.getCountry());
        value.setCounty(address.getCounty());
        value.setPostcode(address.getPostcode());
        value.setTown(address.getTown());
        
        return value;
    }
    
    /**
     * Creates an AddressValue from the given XhbAddress EJBLocalObject
     * @param address an XhbAddress EJBLocalObject
     * @return AddressValue
     */
    private static AddressValue createAddressValue(final XhbAddress address) {
        final AddressValue addressValue = new AddressValue();
        
        addressValue.setAddress1(address.getAddress1());
        addressValue.setAddress2(address.getAddress2());
        addressValue.setAddress3(address.getAddress3());
        addressValue.setAddress4(address.getAddress4());
        addressValue.setCountry(address.getCountry());
        addressValue.setCounty(address.getCounty());
        addressValue.setId(address.getAddressId());
        addressValue.setPostcode(address.getPostcode());
        addressValue.setTown(address.getTown());
        addressValue.setVersion(address.getVersion());

        return addressValue;
    }
}
