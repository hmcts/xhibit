package uk.gov.courtservice.framework.security.activedirectory;

import netscape.ldap.LDAPRebind;
import netscape.ldap.LDAPRebindAuth;

import org.apache.log4j.Logger;

/**
 * Used to authenticate connections to referals.
 * 
 * @author Will Fardell, Xdevelopment LLP.
 */
public class ActiveDirectoryLDAPRebind implements LDAPRebind {
    private static final Logger log = Logger.getLogger(ActiveDirectoryLDAPRebind.class);

    private final LDAPRebindAuth ldapRebindAuth;

    public ActiveDirectoryLDAPRebind() {
        this("", "");
    }

    public ActiveDirectoryLDAPRebind(String user, String password) {
        ldapRebindAuth = new LDAPRebindAuth(user, password);
    }

    public LDAPRebindAuth getRebindAuthentication(String host, int port) {
        if (log.isDebugEnabled()) {
            if ("".equals(ldapRebindAuth.getDN())) {
                log.debug("Rebinding to " + host + ":" + port + " anonymously.");
            } else {
                log.debug("Rebinding to " + host + ":" + port + " with user " + ldapRebindAuth.getDN() + ".");
            }
        }
        return ldapRebindAuth;
    }
}