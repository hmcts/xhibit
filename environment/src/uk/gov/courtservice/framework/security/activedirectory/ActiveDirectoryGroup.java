package uk.gov.courtservice.framework.security.activedirectory;

/**
 * Represent an active directory group
 * 
 * @author bzjrnl
 */
public class ActiveDirectoryGroup extends ActiveDirectoryEntry {
    /**
     * Construct a new Xhibit User
     * 
     * @param dn
     *            the users dn
     * @param principalName
     *            the users principle
     */
    public ActiveDirectoryGroup(String dn, String principalName) {
        super(dn, principalName);
    }

    /**
     * @return a String containing debug details
     */
    public String toString() {
        return "ActiveDirectoryGroup[dn=" + getDn() + ",principalName=" + getPrincipalName() + "]";
    }
}
