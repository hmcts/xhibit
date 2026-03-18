package uk.gov.courtservice.framework.security.ldap;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import netscape.ldap.ber.stream.BERElement;
import netscape.ldap.ber.stream.BERInteger;
import netscape.ldap.ber.stream.BEROctetString;
import netscape.ldap.ber.stream.BERSequence;
import netscape.ldap.client.JDAPBERTagDecoder;

import org.apache.log4j.Logger;

/**
 * This control allows paged results to be retrieved from ActiveDirectory. This
 * is RFC 2696. See http://www.faqs.org/rfcs/rfc2696.html for more details
 * 
 * @author Will Fardell, Xdevelopment 2005
 */
public class LDAPSimplePagedResultsResponse extends LDAPSimplePagedResults {
    private static final Logger log = Logger.getLogger(LDAPSimplePagedResultsResponse.class);

    private static final long serialVersionUID = -1490497033487506726L;

    /**
     * Constuct a new control, constructor required by framework
     */
    public LDAPSimplePagedResultsResponse(String oid, boolean critical, byte[] value) {
        this(oid, critical, value, decode(value));
    }

    // artifical constructor required to allow us to make 2 refs to
    // sequence.
    private LDAPSimplePagedResultsResponse(String oid, boolean critical, byte[] value, BERSequence sequence) {
        super(oid, critical, value, decodeSize(sequence), decodeCookie(sequence));
    }

    /**
     * Overidden to create an instance of LDAPSimplePagedResultsControl not
     * LDAPControl
     */
    public Object clone() {
        return new LDAPSimplePagedResultsResponse(LDAPSimplePagedResults.OID, isCritical(), getCookie());
    }

    //
    // Utils
    //     

    private static BERSequence decode(byte[] value) {
        try {
            return (BERSequence) BERElement.getElement(new JDAPBERTagDecoder(), new ByteArrayInputStream(value),
                    new int[] { 0 });
        } catch (IOException ioe) {
            log.error("Could not decode response.", ioe);
            throw new IllegalArgumentException("value: " + formatCookie(value));
        }
    }

    private static int decodeSize(BERSequence seq) {
        return seq == null ? 0 : ((BERInteger) seq.elementAt(0)).getValue();
    }

    private static byte[] decodeCookie(BERSequence seq) {
        return seq == null ? null : ((BEROctetString) seq.elementAt(1)).getValue();
    }
}
