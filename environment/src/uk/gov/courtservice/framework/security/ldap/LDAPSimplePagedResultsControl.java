package uk.gov.courtservice.framework.security.ldap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import netscape.ldap.ber.stream.BERInteger;
import netscape.ldap.ber.stream.BEROctetString;
import netscape.ldap.ber.stream.BERSequence;

import org.apache.log4j.Logger;

/**
 * This control allows paged results to be retrieved from ActiveDirectory. This
 * is RFC 2696. See http://www.faqs.org/rfcs/rfc2696.html for more details
 * 
 * @author Will Fardell, Xdevelopment 2005
 */
public class LDAPSimplePagedResultsControl extends LDAPSimplePagedResults {
    private static final Logger log = Logger.getLogger(LDAPSimplePagedResultsResponse.class);

    private static final long serialVersionUID = 5728448460921242551L;

    /**
     * Constuct a new control
     */
    public LDAPSimplePagedResultsControl(int size, byte[] cookie) {
        super(LDAPSimplePagedResults.OID, true, encode(size, cookie), size, cookie);
    }

    /**
     * Overidden to create an instance of LDAPSimplePagedResultsControl not
     * LDAPControl
     */
    public Object clone() {
        return new LDAPSimplePagedResultsControl(getSize(), getCookie());
    }

    //
    // Utils
    //

    private static byte[] encode(int pageSize, byte[] cookie) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BERSequence sequence = new BERSequence();
            sequence.addElement(new BERInteger(pageSize));
            sequence.addElement(new BEROctetString(cookie));
            sequence.write(out);
            return out.toByteArray();
        } catch (IOException ioe) {
            log.error("Could not encode control request.", ioe);
            throw new IllegalArgumentException("pageSize: " + pageSize + " cookie: " + formatCookie(cookie));
        }
    }
}
