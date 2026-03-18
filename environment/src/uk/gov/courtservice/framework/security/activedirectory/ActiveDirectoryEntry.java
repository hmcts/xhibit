package uk.gov.courtservice.framework.security.activedirectory;

/**
 * Represent an active directory user
 * 
 * @author bzjrnl
 */
public abstract class ActiveDirectoryEntry implements Comparable {
    private final String dn;

    private final String principalName;

    public ActiveDirectoryEntry(String dn, String principalName) {
        if (dn == null) {
            throw new IllegalArgumentException("dn: null");
        }
        if (principalName == null) {
            throw new IllegalArgumentException("principalName: null");
        }
        this.dn = dn;
        this.principalName = principalName;
    }

    public String getDn() {
        return dn;
    }

    public String getPrincipalName() {
        return principalName;
    }

    /**
     * @return a hash based on the dn
     */
    public int hashCode() {
        return dn.hashCode();
    }

    /**
     * @return true if the object is an active directory entry with the same dn
     */
    public boolean equals(Object object) {
        return object instanceof ActiveDirectoryEntry && equals((ActiveDirectoryEntry) object);
    }

    /**
     * @return true if dn matches
     */
    public boolean equals(ActiveDirectoryEntry entry) {
        return entry != null && dn.equals(entry.dn);
    }

    /**
     * Comparable implementation. Compare this entry to the object
     * 
     * @throws ClassCastException
     *             if object not an entry
     */
    public int compareTo(Object object) {
        return compareTo((ActiveDirectoryEntry) object);
    }

    /**
     * Comparable implementation. Compare this entry to the entry
     * 
     * @throws NullPointerException
     *             if entry is null
     */
    public int compareTo(ActiveDirectoryEntry entry) {
        return dn.compareTo(entry.dn);
    }
}
