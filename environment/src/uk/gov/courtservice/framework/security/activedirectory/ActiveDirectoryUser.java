package uk.gov.courtservice.framework.security.activedirectory;

/**
 * Represent an active directory user
 * 
 * @author bzjrnl
 */
public class ActiveDirectoryUser extends ActiveDirectoryEntry {
    private final String displayName;

    private final String forename;

    private final String surname;

    /**
     * Construct a new Xhibit User
     * 
     * @param dn
     *            the users dn
     * @param principle
     *            the users principle
     * @param displayName
     *            the optional name to display
     * @param forename
     *            the optional users forename
     * @param surname
     *            the optional users surname
     */
    public ActiveDirectoryUser(String dn, String principalName, String displayName, String forename, String surname) {
        super(dn, principalName);
        this.displayName = displayName;
        this.forename = forename;
        this.surname = surname;
    }

    /**
     * @return Returns the displayName.
     */
    public String getDisplayName() {
        if (displayName != null) {
            return displayName;
        }
        if (forename != null && surname != null) {
            return forename + " " + surname;
        }
        return getPrincipalName();
    }

    /**
     * @return Returns the forename.
     */
    public String getForename() {
        return forename;
    }

    /**
     * @return Returns the surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return a String containing debug details
     */
    public String toString() {
        return "ActiveDirectoryUser[dn=" + getDn() + ",principalName=" + getPrincipalName() + ",displayName="
                + displayName + ",forename=" + forename + "surname=" + surname + "]";
    }
}
