package uk.gov.courtservice.xhibit.web.ps.bean;

/**
 * <p>Title: PSRRecipientDetailsBean</p>
 * <p>Description: This holds the details for a given PSR recipient</p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 *
 * @author  Edward Cawley, Xdevelopment LLP (2003)
 * $Revision: 1.13 $
 * $Log: PSRRecipientDetailsBean.java,v $
 * Revision 1.13  2006/06/05 12:32:27  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 *
 * Revision 1.12  2006/05/31 14:26:55  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting
 *
 * Revision 1.11  2006/04/26 09:01:55  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade
 *
 * Revision 1.10  2004/04/26 09:28:33  fz1f7w
 * Added comment to PSRRecipientDetailsBean constructor.
 *
 * Revision 1.9  2004/04/23 15:20:17  fz1f7w
 * Amend code so that correct "required field" messages are shown in thin client.
 *
 * Revision 1.8  2003/09/18 10:42:18  tzj8k5
 * user entered text validation
 *
 * Revision 1.7  2003/03/24 16:45:22  fz0n8j
 * Added/Modified for Email functionality.
 *
 * Revision 1.6  2003/03/21 19:48:51  fz0n8j
 * Late changes
 *
 * Revision 1.5  2003/03/17 11:32:20  fz0n8j
 * Added revision cvs comments. ecawley
 *
 * Revision 1.4  2003/03/14 20:56:19  fz0n8j
 * Uses modified stringfield. ecawley
 *
 * Revision 1.3  2003/03/11 16:31:45  fz0n8j
 * Added CVS log comments - ecawley
 *
 */

import uk.gov.courtservice.xhibit.web.framework.bean.AbstractBean;
import uk.gov.courtservice.xhibit.web.framework.bean.EmailField;
import uk.gov.courtservice.xhibit.web.framework.bean.StringField;
import uk.gov.courtservice.xhibit.web.framework.bean.TelephoneField;

public class PSRRecipientDetailsBean extends AbstractBean {
    /**
     * The name of the office
     */
    private StringField officeName;

    /**
     * Address bean which holds the address information for this Probation
     * service
     */
    private AddressBean address;

    /**
     * The telephone number for the office
     */
    private TelephoneField telephone;

    /**
     * The fax number for the office
     */
    private TelephoneField fax;

    /**
     * The email address for the office
     */
    private EmailField email;

    /**
     * The method of contact
     */
    private String method;

    /**
     * Construct a bean using name, address, telephone, fax and email
     * 
     * @param newId
     *            the id for the bean
     * @param newOfficeName
     *            the office name
     * @param newAddress
     *            address for the office
     * @param newTelephone
     *            telephone number for the office
     * @param newFax
     *            fax number for the office
     * @param newEmail
     *            email number for the office
     * @throws IllegalArgumentException
     *             if any of the arguments are null or of incorrect format
     */
    public PSRRecipientDetailsBean(long newId, String newOfficeName, AddressBean newAddress, String newTelephone,
            String newFax, String newEmail, String method) throws IllegalArgumentException {
        super(newId);
        setOfficeName(new StringField(newOfficeName));
        setAddress(newAddress);
        setTelephone(new TelephoneField(newTelephone, true));
        setMethod(method);
        if (method.equals("Fax")) {
            setFax(new TelephoneField(newFax, false));
            setEmail(new EmailField(newEmail, 255, true));
        } else if (method.equals("Email")) {
            setEmail(new EmailField(newEmail, 255, false));
            setFax(new TelephoneField(newFax, true));
        } else // Covers the case when method string represents a front end
        // selection of Postal
        {
            setEmail(new EmailField(newEmail, 255, true));
            setFax(new TelephoneField(newFax, true));
        }
    }

    /**
     * Used to find out if the data in the bean is valid
     * 
     * @return a boolean showing the status of the bean
     */
    public boolean isValid() {
        if (officeName.getErrorValue() != null || !address.isValid() || telephone.getErrorValue() != null
                || fax.getErrorValue() != null || email.getErrorValue() != null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Standard java bean accessor
     * 
     * @return the name of the office
     */
    public StringField getOfficeName() {
        return officeName;
    }

    /**
     * Standard java bean setter
     * 
     * @param newOfficeName
     *            the name of the office
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setOfficeName(StringField newOfficeName) throws IllegalArgumentException {
        if (newOfficeName == null) {
            throw new IllegalArgumentException("newOfficeName");
        }
        officeName = newOfficeName;
    }

    /**
     * Standard java bean accessor
     * 
     * @return an AddressBean for the office
     */
    public AddressBean getAddress() {
        return address;
    }

    /**
     * Standard java bean setter
     * 
     * @param newAddress
     *            An AddressBean containing the address details for the
     *            probation service
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setAddress(AddressBean newAddress) throws IllegalArgumentException {
        if (newAddress == null) {
            throw new IllegalArgumentException("newAddress");
        }
        address = newAddress;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the telephone number for the office
     */
    public TelephoneField getTelephone() {
        return telephone;
    }

    /**
     * Standard java bean setter
     * 
     * @param newTelephone
     *            the telephone number for the office
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setTelephone(TelephoneField newTelephone) throws IllegalArgumentException {
        if (newTelephone == null) {
            throw new IllegalArgumentException("newTelephone");
        }
        telephone = newTelephone;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the fax number for the office
     */
    public TelephoneField getFax() {
        return fax;
    }

    /**
     * Standard java bean setter
     * 
     * @param newFax
     *            the fax number for the office
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setFax(TelephoneField newFax) throws IllegalArgumentException {
        if (newFax == null) {
            throw new IllegalArgumentException("newFax");
        }
        fax = newFax;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the email address for the office
     */
    public StringField getEmail() {
        return email;
    }

    /**
     * Standard java bean setter
     * 
     * @param newEmail
     *            the email address for the office
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setEmail(EmailField newEmail) throws IllegalArgumentException {
        if (newEmail == null) {
            throw new IllegalArgumentException("newEmail");
        }
        email = newEmail;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Standard java bean setter
     * 
     * @param newMethod
     *            the method of contact
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setMethod(String newMethod) throws IllegalArgumentException {
        if (newMethod == null) {
            throw new IllegalArgumentException("newMethod");
        }
        method = newMethod;
    }

}
