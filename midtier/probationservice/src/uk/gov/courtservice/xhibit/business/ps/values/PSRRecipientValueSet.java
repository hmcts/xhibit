package uk.gov.courtservice.xhibit.business.ps.values;

import java.io.Serializable;
import java.util.HashMap;

import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_psr_recipient.XhbPsrRecipientBasicValue;

/**
 * <p>
 * Title: PSRRecipientValueSet
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
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.7 $
 */
public class PSRRecipientValueSet implements Serializable {
    /**
     * The recipient value object
     */
    private XhbPsrRecipientBasicValue recipient;

    /**
     * The address value object
     */
    private XhbAddressBasicValue address;

    /**
     * The contacts hashmap
     */
    private HashMap contacts;
    
    private static final long serialVersionUID = -1894108358079329795L;

    /**
     * Construct a recipient value object from a value objects
     * 
     * @param newRecipient
     *            the recipient value
     * @param newAddress
     *            the address value
     * @param newContacts
     *            a hashmap of the ontacts
     * @throws IllegalArgumentException
     *             if any of the String arguments are null
     */
    public PSRRecipientValueSet(XhbPsrRecipientBasicValue newRecipient, XhbAddressBasicValue newAddress,
            HashMap newContacts) throws IllegalArgumentException {
        setRecipient(newRecipient);
        setAddress(newAddress);
        setContacts(newContacts);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the recipient value pobject
     */
    public XhbPsrRecipientBasicValue getRecipient() {
        return recipient;
    }

    /**
     * Standard java bean setter
     * 
     * @param newRecipeint
     *            the recipient value object
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setRecipient(XhbPsrRecipientBasicValue newRecipient) throws IllegalArgumentException {
        if (newRecipient == null) {
            throw new IllegalArgumentException("newRecipient");
        }
        recipient = newRecipient;
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
