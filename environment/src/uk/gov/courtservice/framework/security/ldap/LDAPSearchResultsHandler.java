package uk.gov.courtservice.framework.security.ldap;

import java.util.Enumeration;

import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPEntry;

/**
 * This class handles the results of an LDAP search, this allows common search
 * code to be used with the results handled in an efficient custom way.
 * 
 * @author Will Fardell, Xdevelopment LLP
 */
public abstract class LDAPSearchResultsHandler {
    /**
     * Perform any additional processing and return!
     * 
     * @return the value
     */
    public abstract Object getValue();

    /**
     * This method is used to process the entry, while the entry is being
     * processed the connection is held open. As little work as possible should
     * be done during this method. Where possible work should be performed in
     * the getValue method. It is important not to do subsequent searches from
     * this method as that could lead to thread starvation!
     * 
     * @param entry
     *            the entry to process
     */
    public abstract void handleEntry(LDAPEntry entry);

    //
    // Utilities to get the values
    //     

    protected String getStringValue(LDAPEntry entry, String attributeName) {
        LDAPAttribute attribute = entry.getAttribute(attributeName);
        if (attribute != null) {
            Enumeration values = attribute.getStringValues();
            if (values.hasMoreElements()) {
                return (String) values.nextElement();
            }
        }
        return null;
    }

    protected byte[][] getByteValueArray(LDAPEntry entry, String attributeName) {
        LDAPAttribute attribute = entry.getAttribute(attributeName);
        if (attribute != null) {
            return attribute.getByteValueArray();
        }
        return new byte[0][];
    }

    protected String[] getStringValueArray(LDAPEntry entry, String attributeName) {
        LDAPAttribute attribute = entry.getAttribute(attributeName);
        if (attribute != null) {
            return attribute.getStringValueArray();
        }
        return new String[0];
    }
}