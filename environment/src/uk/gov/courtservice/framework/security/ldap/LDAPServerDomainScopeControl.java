package uk.gov.courtservice.framework.security.ldap;

import netscape.ldap.LDAPControl;

/**
 * This control allows paged results to be retrieved from ActiveDirectory. This
 * is RFC 2696. See http://www.faqs.org/rfcs/rfc2696.html for more details
 * 
 * @author Will Fardell, Xdevelopment 2005
 */
public class LDAPServerDomainScopeControl extends LDAPControl {
    private static final long serialVersionUID = 5728448460921242551L;

    /**
     * The unique identifier for the control and response
     */
    public static final String OID = "1.2.840.113556.1.4.1339";

    /**
     * Constuct a new domain scope control
     */
    public LDAPServerDomainScopeControl(boolean critical) {
        super(OID, critical, new byte[0]);
    }

    /**
     * Overidden to create an instance of LDAPServerDomainScopeControl not
     * LDAPControl
     */
    public Object clone() {
        return new LDAPServerDomainScopeControl(isCritical());
    }

    /**
     * Get object info
     */
    public String toString() {
        return "LDAPServerDomainScopeControl[oid=" + getID() + ",critical=" + isCritical() + "]";
    }
}
