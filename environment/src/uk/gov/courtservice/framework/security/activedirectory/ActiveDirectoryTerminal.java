package uk.gov.courtservice.framework.security.activedirectory;

/**
 * Represent an active directory terminal
 * 
 * @author bzjrnl
 */
public class ActiveDirectoryTerminal extends ActiveDirectoryEntry {
    private final String location;

    /**
     * Construct a new Active Directory Terminal
     * 
     * @param dn
     *            the terminal's dn
     * @param principalName
     *            the terminal's name
     * @param location
     *            the terminals location
     */
    public ActiveDirectoryTerminal(String dn, String principalName, String location) {
        super(dn, principalName);
        if (location == null) {
            throw new IllegalArgumentException("location: null");
        }
        this.location = location;
    }

    /**
     * @return Returns the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return a String containing debug details
     */
    public String toString() {
        return "ActiveDirectoryTerminal[dn=" + getDn() + ",principalName=" + getPrincipalName() + ",location="
                + location + "]";
    }
}
