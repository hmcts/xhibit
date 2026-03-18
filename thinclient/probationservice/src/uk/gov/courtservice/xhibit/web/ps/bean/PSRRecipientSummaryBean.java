package uk.gov.courtservice.xhibit.web.ps.bean;

/**
 * <p>Title: PSRRecipientSummaryBean</p>
 * <p>Description: This holds summary information for a given PSR Recipient Summary</p>
 * 
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * 
 * @author  Edward Cawley, Xdevelopment LLP (2003)
 * $Revision: 1.8 $
 * $Log: PSRRecipientSummaryBean.java,v $
 * Revision 1.8  2006/06/05 12:32:27  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 *
 * Revision 1.7  2006/05/31 14:26:55  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting
 *
 * Revision 1.6  2006/04/26 09:01:55  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade
 *
 * Revision 1.5  2003/03/26 16:54:52  fz0n8j
 * Bug fixes.
 *
 * Revision 1.4  2003/03/17 11:32:20  fz0n8j
 * Added revision cvs comments. ecawley
 *
 * Revision 1.3  2003/03/11 16:31:45  fz0n8j
 * Added CVS log comments - ecawley
 *
 */

import uk.gov.courtservice.xhibit.web.framework.bean.AbstractBean;

public class PSRRecipientSummaryBean extends AbstractBean implements Comparable {
    /**
     * The name of the office
     */
    private String officeName;

    /**
     * The office address
     */
    private String address;

    /**
     * The telephone number
     */
    private String telephone;

    /**
     * The fax number
     */
    private String fax;

    /**
     * The e-mail for the office
     */
    private String email;

    /**
     * Construct a recipient summary from the office name, address, telephone,
     * fax and e-mail
     * 
     * @param newId
     *            the bean id
     * @param newOfficeName
     *            the office name
     * @param newAddress
     *            the office address
     * @param newTelephone
     *            the telephone number
     * @param newFax
     *            the fax number
     * @param newEmail
     *            the e-mail for the recipient
     * @throws IllegalArgumentException
     *             if any of the String arguments are null
     */
    public PSRRecipientSummaryBean(long newId, String newOfficeName, String newAddress, String newTelephone,
            String newFax, String newEmail) throws IllegalArgumentException {
        super(newId);
        setOfficeName(newOfficeName);
        setAddress(newAddress);
        setTelephone(newTelephone);
        setFax(newFax);
        setEmail(newEmail);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the office name
     */
    public String getOfficeName() {
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
    public void setOfficeName(String newOfficeName) throws IllegalArgumentException {
        if (newOfficeName == null) {
            throw new IllegalArgumentException("newOfficeName");
        }
        officeName = newOfficeName;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the office address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Standard java bean setter
     * 
     * @param newAddress
     *            the address of the office
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setAddress(String newAddress) throws IllegalArgumentException {
        if (newAddress == null) {
            throw new IllegalArgumentException("newAddress");
        }
        address = newAddress;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the telephone number
     */
    public String getTelephone() {
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
    public void setTelephone(String newTelephone) throws IllegalArgumentException {
        if (newTelephone == null) {
            throw new IllegalArgumentException("newTelephone");
        }
        telephone = newTelephone;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the fax number
     */
    public String getFax() {
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
    public void setFax(String newFax) throws IllegalArgumentException {
        if (newFax == null) {
            throw new IllegalArgumentException("newFax");
        }
        fax = newFax;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the e-mail for the office
     */
    public String getEmail() {
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
    public void setEmail(String newEmail) throws IllegalArgumentException {
        if (newEmail == null) {
            throw new IllegalArgumentException("newEmail");
        }
        email = newEmail;
    }

    public int compareTo(Object o) {
        if (o instanceof PSRRecipientSummaryBean) {
            PSRRecipientSummaryBean bean = (PSRRecipientSummaryBean) o;
            return officeName.compareToIgnoreCase(bean.getOfficeName());
        }
        return 0;
    }

}
