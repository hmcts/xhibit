package uk.gov.courtservice.framework.security.ldap;

import netscape.ldap.LDAPControl;
import netscape.ldap.LDAPException;

/**
 * This control allows paged results to be retrieved from ActiveDirectory. This
 * is RFC 2696. See http://www.faqs.org/rfcs/rfc2696.html for more details
 * 
 * @author Will Fardell, Xdevelopment 2005
 */
public abstract class LDAPSimplePagedResults extends LDAPControl {
    /**
     * The unique identifier for the control and response
     */
    public static final String OID = "1.2.840.113556.1.4.319";

    private final int size;

    private final byte[] cookie;

    /**
     * Constuct a new results control
     */
    public LDAPSimplePagedResults(String oid, boolean critical, byte[] value, int size, byte[] cookie) {
        super(oid, critical, value);
        this.size = size;
        this.cookie = cookie;
    }

    /**
     * @return Returns the cookie.
     */
    public byte[] getCookie() {
        return cookie;
    }

    /**
     * @return Returns the resultSize.
     */
    public int getSize() {
        return size;
    }

    /**
     * Get object info
     */
    public String toString() {
        return "LDAPSimplePagedResults[oid=" + getID() + ",size=" + size + ",cookie=" + formatCookie(cookie)
                + ",critical=" + isCritical() + "]";
    }

    //
    // Utils
    //    

    private final static char[] DIGITS = "0123456789ABCDEF".toCharArray();

    protected final static String formatCookie(byte[] cookie) {
        if (cookie == null) {
            return "null";
        }
        char[] buffer = new char[cookie.length * 2];
        for (int readIndex = 0, writeIndex = 0; readIndex < cookie.length; readIndex++) {
            buffer[writeIndex++] = DIGITS[(cookie[readIndex] >> 4) & 0xf];
            buffer[writeIndex++] = DIGITS[cookie[readIndex] & 0xf];
        }
        return new String(buffer);
    }

    /**
     * Register the control if not allready registered
     * 
     * @throws LDAPException
     */
    public static synchronized void register() throws LDAPException {
        if (LDAPControl.lookupControlClass(LDAPSimplePagedResults.OID) == null) {
            LDAPControl.register(LDAPSimplePagedResults.OID, LDAPSimplePagedResultsResponse.class);
        }
    }
}
