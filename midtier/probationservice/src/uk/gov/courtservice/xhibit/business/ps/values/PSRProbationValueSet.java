package uk.gov.courtservice.xhibit.business.ps.values;

import java.io.Serializable;
import java.util.HashMap;

import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;

/**
 * <p>
 * Title: PSRProbationValueSet
 * </p>
 * <p>
 * Description: This holds a set of value objects used to describe a recipient
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.6 $
 */
public class PSRProbationValueSet implements Serializable {
    /**
     * The court value object
     */
    private XhbCourtBasicValue court;

    /**
     * The address value object
     */
    private XhbAddressBasicValue address;
    
    private static final long serialVersionUID = -7738502327504325911L;

    /**
     * The contacts hashmap
     */
    private HashMap contacts;

    /**
     * Construct a probation value object from value objects
     * 
     * @param newCourt
     *            the court value
     * @param newAddress
     *            the address value
     * @param newContacts
     *            the telephone number
     * @throws IllegalArgumentException
     *             if any of the String arguments are null
     */
    public PSRProbationValueSet(XhbCourtBasicValue newCourt, XhbAddressBasicValue newAddress, HashMap newContacts)
            throws IllegalArgumentException {
        setCourt(newCourt);
        setAddress(newAddress);
        setContacts(newContacts);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the court value pobject
     */
    public XhbCourtBasicValue getCourt() {
        return court;
    }

    /**
     * Standard java bean setter
     * 
     * @param newCourt
     *            the court value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setCourt(XhbCourtBasicValue newCourt) throws IllegalArgumentException {
        if (newCourt == null) {
            throw new IllegalArgumentException("newCourt");
        }
        court = newCourt;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the address value object
     */
    public XhbAddressBasicValue getAddress() {
        return address;
    }

    /**
     * Standard java bean setter
     * 
     * @param newAddress
     *            the address value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setAddress(XhbAddressBasicValue newAddress) throws IllegalArgumentException {
        if (newAddress == null) {
            throw new IllegalArgumentException("newAddress");
        }
        address = newAddress;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the contacts value object
     */
    public HashMap getContacts() {
        return contacts;
    }

    /**
     * Standard java bean setter
     * 
     * @param newContact
     *            the address value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setContacts(HashMap newContacts) throws IllegalArgumentException {
        if (newContacts == null) {
            throw new IllegalArgumentException("newContacts");
        }
        contacts = newContacts;
    }
}
