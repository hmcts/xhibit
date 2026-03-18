package uk.gov.courtservice.xhibit.web.ps.bean;

/**
 * <p>Title: ProbationServiceDetailsBean</p>
 * <p>Description: This holds the details for a given probation service</p>
 * 
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * 
 * @author  Edward Cawley, Xdevelopment LLP (2003)
 * $Revision: 1.14 $
 * $Log: ProbationServiceDetailsBean.java,v $
 * Revision 1.14  2006/06/05 12:32:27  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 *
 * Revision 1.13  2006/05/31 14:26:55  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting
 *
 * Revision 1.12  2006/04/26 09:01:56  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade
 *
 * Revision 1.11  2005/06/29 10:47:34  tzj8k5
 * PR 57282 - Thin Client Probation - edit Probation details and issue PSR request
 *
 * Revision 1.10  2003/09/18 10:42:18  tzj8k5
 * user entered text validation
 *
 * Revision 1.9  2003/03/24 16:45:22  fz0n8j
 * Added/Modified for Email functionality.
 *
 * Revision 1.8  2003/03/17 11:32:21  fz0n8j
 * Added revision cvs comments. ecawley
 *
 * Revision 1.7  2003/03/17 11:15:36  fz0n8j
 * Bug fix.
 *
 * Revision 1.6  2003/03/14 20:56:45  fz0n8j
 * Uses new stringfield. ecawley
 *
 * Revision 1.5  2003/03/11 16:31:45  fz0n8j
 * Added CVS log comments - ecawley
 *
 */

import uk.gov.courtservice.xhibit.web.framework.bean.AbstractBean;
import uk.gov.courtservice.xhibit.web.framework.bean.EmailField;
import uk.gov.courtservice.xhibit.web.framework.bean.StringField;
import uk.gov.courtservice.xhibit.web.framework.bean.TelephoneField;

public class ProbationServiceDetailsBean extends AbstractBean {
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
     * Construct a bean using name, address, telephone, fax and email
     * 
     * @param newId
     *            the id for the bean
     * @param newOfficeName
     *            name of the office
     * @param newAddress
     *            office address
     * @param newTelephone
     *            telephone number for the office
     * @param newFax
     *            fax number for the office
     * @param newEmail
     *            email number for the office
     * @throws IllegalArgumentException
     *             if any of the arguments are null or of incorrect format
     */
    public ProbationServiceDetailsBean(long newId, String newOfficeName, AddressBean newAddress, String newTelephone,
            String newFax, String newEmail) throws IllegalArgumentException {
        super(newId);
        setOfficeName(new StringField(newOfficeName, 50, false));
        setAddress(newAddress);
        setTelephone(new TelephoneField(newTelephone, true));
        setFax(new TelephoneField(newFax, true));
        setEmail(new EmailField(newEmail, 255, true));
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
    public EmailField getEmail() {
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

}
